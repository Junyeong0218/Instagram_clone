const searcher = document.querySelector(".searcher");
const searcher_placeholder = document.querySelector(".searcher-placeholder");
const searcher_input = document.querySelector(".searcher > input");
const result_wrapper = document.querySelector(".result-wrapper");

let prevKeyword = "";

searcher_placeholder.onclick = focusSearchInput;
searcher_input.onblur = blurSearchInput;
searcher_input.oninput = (event) => {
	console.log(searcher_input.value);
	if(searcher_input.value == null || searcher_input.value == "" || searcher_input.value.trim() == "") return;
	$.ajax({
		type: "get",
		url: "/search/select-keyword",
		data: { "keyword": searcher_input.value },
		dataType: "text",
		success: function (data) {
			data = JSON.parse(data);
			console.log(data);
			const result_header = result_wrapper.querySelector(".result-header");
			if(result_header != null && typeof result_header != "undefined") result_header.remove();
			const results = result_wrapper.querySelector(".results");
			results.innerHTML = "";
			addSearchResults(data);
			
			const a_tags = results.querySelectorAll("a");
			for(let i = 0; i < a_tags.length; i++) {
				a_tags[i].onclick = (event) => {
					event.preventDefault();
					console.log(data[i]);
					const user_id = data[i].user_id;
					const hash_tag_id = data[i].hash_tag_id;
					const isUser = user_id > 0 ? true : false;
					let insertData;
					if(isUser) {
						insertData = { "isUser": isUser,
													"id": user_id}
					} else {
						insertData = { "isUser": isUser,
													"id": hash_tag_id }
					}
					console.log(insertData);
					$.ajax({
						type: "post",
						url: "/search/insert-latest-search",
						data: insertData,
						dataType: "text",
						success: function (data) {
							console.log(data);
							if(data == "true") {
								location.href = event.target.href;
							} else {
								console.log("데이터 처리 오류");	
							}
						},
						error: function (xhr, status, error) {
							console.log(xhr);
							console.log(status);
							console.log(error);
						}
					});
				}
			} 
		},
		error: function (xhr, status, error) {
			console.log(xhr);
			console.log(status);
			console.log(error);
		}
	});
}

function focusSearchInput(event) {
        if (prevKeyword != null || prevKeyword != "") {
                searcher_input.value = prevKeyword;
        }
        const eraseButton = makeEraseKeywordButton();
        $.ajax({
			type: "get",
			url: "/search/latest-searches",
			dataType: "text",
			success: function (data) {
				data = JSON.parse(data);
        		const result_box = makeResultBox(data);
				
		        searcher.insertBefore(eraseButton, result_wrapper);
		        result_wrapper.appendChild(result_box);
		        event.target.classList.add("hidden");
		        
		        result_box.onmouseenter = () => searcher_input.onblur = null;
		        result_box.onmouseleave = () => {
					searcher_input.onblur = blurSearchInput;
					window.onclick = (event) => {
						if(searcher_input.onblur != null) return;
						if(event.target.tagName != "INPUT" && !event.target.className.includes("result")) {
							if (event.relatedTarget == null || event.relatedTarget != eraseButton) {
					            prevKeyword = searcher_input.value;
					            if ((prevKeyword != null || prevKeyword != "") && searcher_input.value != "") {
					                    searcher_placeholder.querySelector(".placeholder-text").innerText = prevKeyword;
					            }
						    } else {
						            prevKeyword = "";
						            searcher_placeholder.querySelector(".placeholder-text").innerText = "검색";
						    }
						    eraseButton.remove();
						    searcher_placeholder.classList.remove("hidden");
						    result_wrapper.querySelector(".result-box").remove();
						    window.onclick = null;
						}
					}
				}
			},
			error: function (xhr, status, error) {
				console.log(xhr);
				console.log(status);
				console.log(error);
			}
		});
        searcher_input.focus();
}

function blurSearchInput(event) {
    const eraseButton = document.querySelector(".erase-keyword");
    if (event.relatedTarget == null || event.relatedTarget != eraseButton) {
            prevKeyword = searcher_input.value;
            if ((prevKeyword != null || prevKeyword != "") && searcher_input.value != "") {
                    searcher_placeholder.querySelector(".placeholder-text").innerText = prevKeyword;
            }
    } else {
            prevKeyword = "";
            searcher_placeholder.querySelector(".placeholder-text").innerText = "검색";
    }
    eraseButton.remove();
    searcher_placeholder.classList.remove("hidden");
    result_wrapper.querySelector(".result-box").remove();
}

function makeEraseKeywordButton() {
        const button = document.createElement("button");
        button.type = "button";
        button.className = "erase-keyword";
        return button;
}

function addSearchResults(search_results) {
	const results = document.querySelector(".results");
	for(let i = 0; i < search_results.length; i++) {
		const div = document.createElement("div");
		div.className = "result";
		div.innerHTML = `<img src="${search_results[i].hash_tag_id == 0 ? search_results[i].has_profile_image == true ? '../../../../file_upload/user_profile_images/' + search_results[i].file_name : '/static/images/basic_profile_image.jpg' : '/static/images/search_result_hash_tag.png'}" alt='유저 프로필 이미지'>
										<div class="name-wrapper"></div>`;
		const name_wrapper = div.querySelector(".name-wrapper");
		if(search_results[i].hash_tag_id == 0) {
			name_wrapper.innerHTML = `<a class="username" href="/profile?username=${search_results[i].username}">${search_results[i].username}</a>
																    <span class="follow-info">${search_results[i].user_follow_flag == true ? search_results[i].name + ' • 팔로잉' : search_results[i].name}</span>`;
		} else {
			name_wrapper.innerHTML = `<a class="tag-name" href="/search?tag_name=${search_results[i].tag_name}">${search_results[i].tag_name}</a>
																    <span class="follow-info">${search_results[i].hash_tag_follow_flag == true ? '팔로잉' : ''}</span>`;
		}
		results.appendChild(div);
	}
	if(search_results.length == 0) results.innerHTML = "<div class='no-results'>검색 결과 없음.</div>";
	else 												  results.classList.add("has-results");
}

function makeResultBox(latest_searches) {
		console.log(latest_searches);
        const result_box = document.createElement("div");
        result_box.className = "result-box";
        result_box.innerHTML = `<div class="arrow"></div>
                                                <div class="result-lists">
                                                        <div class="result-header">
                                                                <span>최근 검색 항목</span>
                                                                <button class="remove-all-results ${latest_searches.length > 0 ? 'active' : ''}" type="button">모두 지우기</button>
                                                        </div>
                                                        <div class="results"></div>
                                                </div>`;
                                                
		const results = result_box.querySelector(".results");
		for(let i = 0; i < latest_searches.length; i++) {
			const div = document.createElement("div");
			div.className = "result";
			div.innerHTML = `<img src="${latest_searches[i].hash_tag_id != 0 ? '/static/images/search_result_hash_tag.png' : latest_searches[i].has_profile_image == true ? '../../../../file_upload/user_profile_images/' + latest_searches[i].file_name : '/static/images/basic_profile_image.jpg'}" alt='유저 프로필 이미지'>
											<div class="name-wrapper"></div>
											<button class="remove-latest-result" type="button">
												<img src="/static/images/remove_search_result.png">
											</button>`;
			const name_wrapper = div.querySelector(".name-wrapper");
			if(latest_searches[i].hash_tag_id == 0) {
				name_wrapper.innerHTML = `<a class="username" href="/profile?username=${latest_searches[i].username}">${latest_searches[i].username}</a>
																	    <span class="follow-info">${latest_searches[i].user_follow_flag == true ? latest_searches[i].name + ' • 팔로잉' : latest_searches[i].name}</span>`;
			} else {
				name_wrapper.innerHTML = `<a class="tag-name" href="/search?tag_name=${latest_searches[i].tag_name}">#${latest_searches[i].tag_name}</a>
																	    <span class="follow-info">${latest_searches[i].hash_tag_follow_flag == true ? '팔로잉' : ''}</span>`;
			}
			results.appendChild(div);
		}
		if(latest_searches.length == 0) results.innerHTML = "<div class='no-results'>최근 검색 내역 없음.</div>";
		else														results.classList.add("has-results");
		
        return result_box;
}

// • 