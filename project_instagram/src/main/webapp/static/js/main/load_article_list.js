const container = document.querySelector(".container");
const article_wrapper = document.querySelector(".articles");
let origin_article_list = new Array();
let origin_article_detail_data;

let article_load_count = 0;
let relate_comment_flag = false;
let relate_comment_id;

function loadArticleList() {
	$.ajax({
		type: "get",
		url: "/load-articles",
		dataType: "text",
		success: function (data) {
			data = JSON.parse(data);
			if(data.length == 1 && data[0].id == 0) return;
			for(let i = 0; i < data.length; i++) {
				origin_article_list.push(data[i]);
				const article_tag = makeArticleTag(data[i]);
				article_wrapper.appendChild(article_tag);
				
				const article_menu = article_tag.querySelector(".article-menu");
				article_menu.onclick = showArticleMenu;
				
				const like_button = article_tag.querySelector(".like-button").parentElement;
				like_button.onclick = toggleLikeArticle;
				
				const textarea = article_tag.querySelector("textarea");
				textarea.oninput = activeCommentSubmitButton;
				
				const comment_submit_button = article_tag.querySelector(".comment-submit-button");
				comment_submit_button.onclick = submitComment;
				
				const show_comments_button = article_tag.querySelector(".show-comments-button");
				if(show_comments_button != null && typeof show_comments_button != "undefined") show_comments_button.onclick = showArticleDetail;
				
				const comment_button = article_tag.querySelector(".comment-button").parentElement;
				comment_button.onclick = showArticleDetail;
				
				const picture_controller = article_tag.querySelector(".picture-controller");
				if(picture_controller != null && typeof picture_controller != "undefined") {
					picture_controller.children[0].onclick = moveToPrevImage;
					picture_controller.children[1].onclick = moveToNextImage;
				}
				
			}
		},
		error: function (xhr, status, error) {
			console.log(xhr);
			console.log(status);
			console.log(error);
		}
	});
	article_load_count++;
}

function makeArticleTag(articleData) {
	if(articleData.id == null || articleData.id == 0) return;
	
	const article = document.createElement("article");
	const header = document.createElement("div");
	header.className = "article-header";
	header.innerHTML = `<div class="writer-image">
												<img src="${articleData.has_profile_image == "true" ? '../../../../file_upload/user_profile_images/' + articleData.file_name : '/static/images/basic_profile_image.jpg'}" alt="게시글 작성자 프로필 이미지">
											</div>
											<div class="writer-info">
												<a href="/profile?username=${articleData.username}" class="writer-username">${articleData.username}</a>
												${articleData.feature != null && articleData.feature != "null" ? '<span class="remark">' + articleData.feature + '</span>' : ''}
											</div>
											<button type="button" class="article-menu">
												<img src="/static/images/article_menu.png" alt="옵션 더 보기">
											</button>`;
	// ------------------------------------
	// make article header
						
	const pictures = document.createElement("div");
	pictures.className = "pictures";
	
	const ul = document.createElement("ul");
	const dots = document.createElement("div");
	dots.className = "image-index";
	
	const mediaList = articleData.media_list;
	for(let i = 0; i < mediaList.length; i++) {
		const li = document.createElement("li");
		li.className = i == 0 ? "picture current" : "picture";
		if(mediaList[i].media_type == "image") {
			li.innerHTML = `<img src="../../../../file_upload/article_medias/${articleData.id}/${mediaList[i].media_name}" alt="게시글 이미지">`;
		} else if(mediaList[i].media_type == "video") {
			li.innerHTML = `<video src="../../../../file_upload/article_medias/${articleData.id}/${mediaList[i].media_name}" autoplay alt="게시글 동영상">`;
		}
		ul.appendChild(li);
		
		if(mediaList.length > 1) {
			const dot = document.createElement("span");
			dot.className = i == 0 ? "dot current-index" : "dot";
			dots.appendChild(dot);
		}
	}
	
	pictures.appendChild(ul);
	pictures.appendChild(dots);
	
	if(mediaList.length > 1) {
		const picture_controller = document.createElement("div");
		picture_controller.className = "picture-controller";
		const prev_image_button = document.createElement("button");
		const next_image_button = document.createElement("button");
		
		prev_image_button.type = "button";
		next_image_button.type = "button";
		
		prev_image_button.className = "prev-image";
		next_image_button.className = "next-image active";
		
		prev_image_button.innerHTML = `<img src="/static/images/new_article_prev_button.png">`;
		next_image_button.innerHTML = `<img src="/static/images/new_article_next_button.png">`;
		
		picture_controller.appendChild(prev_image_button);
		picture_controller.appendChild(next_image_button);
		pictures.appendChild(picture_controller);
	}
	
	// ------------------------------------
	// make article pictures and dots
	
	const upload_time_message = makeUploadTimeMessage(articleData.create_date);
	const tagged_contents = makeContentsTags(articleData.contents);
	
	const description = document.createElement("div");
	description.className = "article-description";
	description.innerHTML = `<div class="article-icons">
														<div class="left-button">
															<button type="button">
																<span class="like-button ${articleData.like_flag == "true" ? 'pressed' : ''}"></span>
															</button>
															<button type="button">
																<span class="comment-button"></span>
															</button>
															<button type="button">
																<span class="share-button"></span>
															</button>
														</div>
														<div class="right-button">
															<button type="button">
																<span class="save-to-collection"></span>
															</button>
														</div>
													</div>
													<div class="who-likes">
														
														<div class="represent-message">
															<span class="represent-comment">
																<span class="how-many-likes">좋아요 ${articleData.total_like_count}개</span>
															</span>
														</div>
													</div>
													<div class="article-texts">
														<span class="description-username">${articleData.username}</span>
														<span class="description-text">${tagged_contents}</span>
													</div>
${Number(articleData.total_commented_user_count) > 1 ? `<div class="article-texts">
																								<button type="button" class="show-comments-button">댓글 ` + articleData.total_commented_user_count + `개 모두 보기</span>
																							</div>` : 
	articleData.total_commented_user_count == 1 ? `<div class="article-texts">
																								<button type="button" class="show-comments-button">댓글 1개 모두 보기</span>
																							</div>` : ''}
													<div class="upload-time-wrapper">
														<span class="upload-time">${upload_time_message}</span>
													</div>
													<form class="write-comment">
														<div class="emoji-wrapper">
															<img src="/static/images/emoji_icon.png" alt="">
														</div>
														<textarea name="" placeholder="댓글 달기..." autocomplete="off"
															autocorrect="off"></textarea>
														<button type="button" disabled class="comment-submit-button disabled">게시</button>
													</form>`;
							
	// ------------------------------------
	// make article description
	
	article.appendChild(header);
	article.appendChild(pictures);
	article.appendChild(description);
	
	return article;
}

function makeContentsTags(contents) {
	let tag = "";
	let has_more_string = true;
	while(has_more_string) {
		let hash_tag_index = contents.indexOf("#");
		let user_tag_index = contents.indexOf("@");
		if(hash_tag_index == -1 && user_tag_index == -1) {
			tag += contents.substring(0, contents.length);
			break;
		} else if(hash_tag_index < user_tag_index || user_tag_index == -1) {
			let blank_index = contents.indexOf(" ", hash_tag_index) == -1 ? contents.length : contents.indexOf(" ", hash_tag_index);
			tag += contents.substring(0, hash_tag_index);
			tag += `<a class="hash-tag-link" href="/search?tag_name=${contents.substring(hash_tag_index + 1, blank_index)}">${contents.substring(hash_tag_index, blank_index)}</a> `;
			contents = contents.substring(blank_index + 1, contents.length);
		} else if(user_tag_index < hash_tag_index || hash_tag_index == -1) {
			let blank_index = contents.indexOf(" ", user_tag_index) == -1 ? contents.length : contents.indexOf(" ", user_tag_index);
			tag += contents.substring(0, user_tag_index);
			tag +=  `<a class="user-tag-link" href="/profile?username=${contents.substring(user_tag_index + 1, blank_index)}">${contents.substring(user_tag_index, blank_index)}</a> `;
			contents = contents.substring(blank_index + 1, contents.length);
		}
		if(contents.length == 0) has_more_string = false;
	}
	return tag;
}

function makeUploadTimeMessage(create_date) {
	const upload_time = new Date(create_date);
	const now = new Date();
	const ago = new Date(now - upload_time);
	ago.setUTCHours(-9);
	
	let year = ago.getFullYear() - 1970;
	let month = ago.getMonth() + 1;
	let date = ago.getDate();
	let hour = ago.getHours();
	let minute = ago.getMinutes();
	let second = ago.getSeconds();
	
	if(year > 0) {
		return `${year}년 전`;
	} else if(month > 1) {
		return `${month}개월 전`;
	} else if((date == 1 && upload_time.getDay() != now.getDay()) || date > 1) {
		return `${date}일 전`;
	} else if(hour > 0) {
		return `${hour}시간 전`;
	} else if(minute > 0) {
		return `${minute}분 전`;
	} else {
		return `${second}초 전`;
	}
}

function moveToPrevImage(event) {
	let ul = event.composedPath()[2].querySelector("ul");
	let detail_flag = false;
	let image_width = 614;
	if(event.composedPath()[2].className == "detail-images") {
		ul = event.composedPath()[2];
		detail_flag = true;
		image_width = ul.querySelector("img").width;
	}
	const images = ul.children;
	const current_image = ul.querySelector(".current");
	let index = -1;
	for(let i = 0; i < images.length; i++) {
		if(images[i] == current_image) {
			index = i;
			break;
		}
	}
	let max_length = detail_flag ? images.length - 2 : images.length - 1;
	if(index != -1) {
		ul.scrollTo((--index) * image_width, 0);
		if(detail_flag) event.target.parentElement.style = `left: ${(index) * image_width}px;`;
		current_image.classList.remove("current");
		current_image.previousElementSibling.classList.add("current");
	}
	if(index < max_length) {
		event.target.nextElementSibling.classList.add("active");
	}
	if(index == 0) {
		event.target.classList.remove("active");
	}
	if(!detail_flag) activeCurrentDot(ul, index);
}

function moveToNextImage(event) {
	let ul = event.composedPath()[2].querySelector("ul");
	let detail_flag = false;
	let image_width = 614;
	if(event.composedPath()[2].className == "detail-images") {
		ul = event.composedPath()[2];
		detail_flag = true;
		image_width = ul.querySelector("img").width;
	}
	const images = ul.children;
	const current_image = ul.querySelector(".current");
	let index = -1;
	for(let i = 0; i < images.length; i++) {
		if(images[i] == current_image) {
			index = i;
			break;
		}
	}
	let max_length = detail_flag ? images.length - 2 : images.length - 1;
	if(index != -1) {
		ul.scrollTo((++index) * image_width, 0);
		if(detail_flag) event.target.parentElement.style = `left: ${(index) * image_width}px;`;
		current_image.classList.remove("current");
		current_image.nextElementSibling.classList.add("current");
	}
	if(index > 0) {
		event.target.previousElementSibling.classList.add("active");
	}
	if(index == max_length) {
		event.target.classList.remove("active");
	}
	if(!detail_flag) activeCurrentDot(ul, index);
}

function activeCurrentDot(ul, index) {
	const dots = ul.nextElementSibling.children;
	for(let i = 0; i < dots.length; i++) {
		if(i == index) dots[i].classList.add("current-index");
		else					dots[i].classList.remove("current-index");
	}
}

function showArticleMenu(event) {
	const offset = window.pageYOffset;
    pop_up.classList.remove("to-hidden");
    pop_up.classList.add("to-show");
    document.querySelector("body").style = "overflow: hidden;";
    pop_up.style.top = offset + 'px';

    const buttons = pop_up.querySelectorAll(".article-control-button");
    buttons[buttons.length - 1].onclick = () => {
        pop_up.classList.remove("to-show");
        pop_up.classList.add("to-hidden");
        pop_up.style.top = '0px';
        if(event.composedPath()[4].className != "article-detail-wrapper") {
            document.querySelector("body").style = "";
		}
    }
}

function toggleLikeArticle(event) {
	const span = event.target.children[0];
	const article_index = getArticleIndex(event.composedPath()[4]);
	const wrapper = event.composedPath()[6];
	let article_data;
	if(wrapper.className == "article-detail-wrapper") {
		article_data = origin_article_detail_data;
	} else {
		article_data = origin_article_list[article_index];
	}
	let article_id = article_data.id;
	if(article_data.like_flag == "true") {
		$.ajax({
			type: "post",
			url: "/article/delete-like-article",
			data: { "article_id": article_id },
			dataType: "text",
			success: function (data) {
				if(data == "1") {
					span.classList.remove("pressed");
					console.log(event.path[3]);
					const how_many_likes = event.path[3].querySelector(".how-many-likes");
					article_data.like_flag = "false";
					
					article_data.total_like_count = Number(article_data.total_like_count) - 1;
					
					how_many_likes.innerText = `좋아요 ${article_data.total_like_count}개`;
				}
			},
			error: function (xhr, status, error) {
				console.log(xhr);
				console.log(status);
				console.log(error);
			}
		});
	} else {
		$.ajax({
			type: "post",
			url: "/article/insert-like-article",
			data: { "article_id": article_data.id },
			dataType: "text",
			success: function (data) {
				if(data == "1") {
					span.classList.add("pressed");
					console.log(event.path[3]);
					const how_many_likes = event.path[3].querySelector(".how-many-likes");
					article_data.like_flag = "true";
					
					article_data.total_like_count = Number(article_data.total_like_count) + 1;
					
					how_many_likes.innerText = `좋아요 ${article_data.total_like_count}개`;
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

function getArticleIndex(article) {
	const article_list = document.querySelectorAll("article");
	for(let i=0; i < article_list.length; i++) {
		if(article_list[i] == article) {
			return i;
		}
	}
	return -1;
}

function activeCommentSubmitButton(event) {
	const button = event.target.nextElementSibling;
	const value = event.target.value;
	if(value != null && typeof value != "undefined" && value != "") {
		button.classList.remove("disabled");
		button.disabled = false;
	} else {
		button.classList.add("disabled");
		button.disabled = true;
	}
}

function submitComment(event) {
	const article_index = getArticleIndex(event.composedPath()[3]);
	const wrapper = event.composedPath()[5];
	let article_data;
	if(wrapper.className == "article-detail-wrapper") {
		article_data = origin_article_detail_data;
	} else {
		article_data = origin_article_list[article_index];
	}
	const article_id = article_data.id;
	let comment = event.target.previousElementSibling.value;
	
	if(relate_comment_flag == true) {
		$.ajax({
			type: "post",
			url: "/article/insert-related-comment",
			data: { "article_id": article_id,
						  "comment": comment,
						  "related_comment_id": relate_comment_id },
			dataType: "text",
			success: function (data) {
				if(data == "1") {
					location.reload();
				}
			},
			error: function (xhr, status, error) {
				console.log(xhr);
				console.log(status);
				console.log(error);
			}
		});
	} else {
		$.ajax({
			type: "post",
			url: "/article/insert-comment",
			data: { "article_id": article_id, 
						  "comment": comment },
			dataType: "text",
			success: function (data) {
				if(data == "1") {
					location.reload();
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

function showArticleDetail(event) {
	let article_index = getArticleIndex(event.composedPath()[3]);
	let article_id;
	if(article_index == -1) {
		article_index = getArticleIndex(event.composedPath()[4]);
		article_id = origin_article_list[article_index].id;
	} else {
		article_id = origin_article_list[article_index].id;
	}
	$.ajax({
		type: "get",
		url: "/article/select-article-detail",
		data: { "article_id": article_id },
		dataType: "text",
		async: "false",
		success: function (data) {
			data = JSON.parse(data);
			relate_comment_flag = false;
			relate_comment_id = 0;
			
			origin_article_detail_data = data;
			
			const article_detail_tag = makeArticleDetail(data);
			container.appendChild(article_detail_tag);
			
			article_detail_tag.onclick = removeArticleDetail;
			
			const article_menu = article_detail_tag.querySelector(".article-menu");
			article_menu.onclick = showArticleMenu;
			
			const like_button = article_detail_tag.querySelector(".like-button").parentElement;
			like_button.onclick = toggleLikeArticle;
			
			const textarea = article_detail_tag.querySelector("textarea");
			textarea.oninput = activeCommentSubmitButton;
			
			const comment_submit_button = article_detail_tag.querySelector(".comment-submit-button");
			comment_submit_button.onclick = submitComment;
			
			const comment_like_buttons = article_detail_tag.querySelectorAll(".comment-like-button");
			for(let i=0; i< comment_like_buttons.length; i++) {
				comment_like_buttons[i].onclick = (event) => {
					toggleCommentLike(event, false, -1, -1);	
				}
			}
			
			const reply_buttons = article_detail_tag.querySelectorAll(".reply");
			for(let i=0; i< reply_buttons.length; i++) {
				reply_buttons[i].onclick = addAtSignTagToTextArea;
			}
			
			const show_reply_buttons = article_detail_tag.querySelectorAll(".show-reply-button");
			if(show_reply_buttons.length != 0) {
				for(let i=0; i < show_reply_buttons.length; i++) {
					show_reply_buttons[i].onclick = toggleReplies;
				}
			}
			
			const picture_controller = article_detail_tag.querySelector(".picture-controller");
			if(picture_controller != null && typeof picture_controller != "undefined") {
				picture_controller.querySelector(".prev-image").onclick = moveToPrevImage;
				picture_controller.querySelector(".next-image").onclick = moveToNextImage;
			}
			
			document.querySelector("body").style = "overflow: hidden;";
		},
		error: function (xhr, status, error) {
			console.log(xhr);
			console.log(status);
			console.log(error);
		}
	});
}

function toggleReplies(event) {
	const reply = event.target.nextElementSibling;
	const show_reply_comment = event.target.parentElement;
	const index = getCurrentCommentIndex(show_reply_comment.previousElementSibling);
	const current_comment_data = origin_article_detail_data.article_comment_list[index];
	if(reply == null || typeof reply  == "undefined") {
		$.ajax({
			type: "get",
			url: "/article/select-related-comments",
			data: { "comment_id": current_comment_data.id },
			dataType: "text",
			success: function (data) {
				data = JSON.parse(data);
				origin_article_detail_data.article_comment_list[index].reply_list = data;
				for(let i=0; i < data.length; i++) {
					const related_comment_tag = makeRelatedCommentTag(data[i]);
					show_reply_comment.appendChild(related_comment_tag);
					
					const like_button = related_comment_tag.querySelector(".comment-like-button");
					like_button.onclick = (event) => {
						toggleCommentLike(event, true, i, index);
					}
					
					const replies_to_reply = related_comment_tag.querySelectorAll(".reply");
					for(let i=0; i < replies_to_reply.length; i++) {
						replies_to_reply[i].onclick = (event) => {
							const origin_comment = event.composedPath()[5].previousElementSibling;
							const origin_comment_index = getCurrentCommentIndex(origin_comment);
							const textarea = event.composedPath()[7].querySelector("textarea");
							
							relate_comment_flag = true;
							relate_comment_id = origin_article_detail_data.article_comment_list[origin_comment_index].id;
							
							textarea.innerText = textarea.innerText + ` @${origin_article_detail_data.article_comment_list[origin_comment_index].reply_list[i].username} `;
						}
					}
				}
				show_reply_comment.classList.add("active");
				show_reply_comment.querySelector(".show-reply-button > button").innerText = `답글 숨기기`;
			},
			error: function (xhr, status, error) {
				console.log(xhr);
				console.log(status);
				console.log(error);
			}
		});
	} else {
		if(show_reply_comment.className.includes("active")) {
			show_reply_comment.classList.remove("active");
			show_reply_comment.querySelector(".show-reply-button > button").innerText = `답글 보기(${current_comment_data.related_comment_count}개)`;
		} else {
			show_reply_comment.classList.add("active");
			show_reply_comment.querySelector(".show-reply-button > button").innerText = `답글 숨기기`;
		}
	}
}

function makeRelatedCommentTag(related_comment) {
	const upload_time = makeUploadTimeMessage(related_comment.create_date);
	const detail_contents = document.createElement("div");
	detail_contents.className = "detail-contents";
	detail_contents.innerHTML = `<div>
									                            <div class="writer-image">
									                                <img src="${related_comment.has_profile_image == 'true' ? '../../../../file_upload/user_profile_images/' + related_comment.file_name : '/static/images/basic_profile_image.jpg'}" alt="">
									                            </div>
									                            <div class="detail-texts">
									                                <div class="detail-content">
									                                    <span class="writer-username">${related_comment.username}</span>
									                                    <span
									                                        class="content-description">${related_comment.contents}</span>
									                                </div>
									                                <div class="reply-buttons">
									                                    <span class="upload-time">${upload_time}</span>
									                                    ${Number(related_comment.comment_like_user_count) > 0 ? '<button type="button" class="comment_like_count">좋아요 ' + related_comment.comment_like_user_count + '개</button>' : ''}
									                                    <button type="button" class="reply">답글 달기</button>
									                                </div>
									                            </div>
									                        </div>
									                        <button class="comment-like-button">
									                            <img src="/static/images/article_detail_like_comment_button${related_comment.like_flag == "true" ? '_pressed' : ''}.png" alt="">
									                        </button>`;
	return detail_contents;
}

function addAtSignTagToTextArea(event) {
	const index = getCurrentCommentIndex(event.composedPath()[4]);
	const current_comment_data = origin_article_detail_data.article_comment_list[index];
	relate_comment_flag = true;
	relate_comment_id = current_comment_data.id;
	const textarea = event.composedPath()[6].querySelector("textarea");
	textarea.innerText = textarea.innerText + ` @${current_comment_data.username} `;
}

function getCurrentCommentIndex(comment) {
	const comments = document.querySelector(".comments-wrapper").children;
	for(let i=1; i < comments.length; i++) {
		if(comments[i] == comment) return i-1;
	}
}

function toggleCommentLike(event, isReply, reply_index, origin_comment_index) {
	const img = event.target.children[0];
	const current_comment = event.composedPath()[1];
	const comment_wrapper = event.composedPath()[2].children;
	let index = -1;
	for(let i = 1; i < comment_wrapper.length; i++) {
		if(comment_wrapper[i] == current_comment) {
			index = i - 1;
			break;
		}
	}
	
	const comment_list = origin_article_detail_data.article_comment_list;
	let comment_id = comment_list[index].id;
	let comment_like_flag = comment_list[index].comment_like_flag;
	
	if(isReply == true) { 
		comment_id = comment_list[origin_comment_index].reply_list[reply_index].id;
		comment_like_flag = comment_list[origin_comment_index].reply_list[reply_index].like_flag;
	}
	
	if(comment_like_flag == "true") {
		// delete
		$.ajax({
			type: "post",
			url: "/article/delete-comment-like",
			data: { "comment_id": comment_id },
			dataType: "text",
			success: function (data) {
				if(data == "1") {
					if(isReply == true) {
						origin_article_detail_data.article_comment_list[origin_comment_index].reply_list[reply_index].like_flag = false;
					} else {
						origin_article_detail_data.article_comment_list[index].comment_like_flag = "false";
					}
					
					img.src = "/static/images/article_detail_like_comment_button.png";
				}
			},
			error: function (xhr, status, error) {
				console.log(xhr);
				console.log(status);
				console.log(error);
			}
		});
	} else {
		// insert
		$.ajax({
			type: "post",
			url: "/article/insert-comment-like",
			data: { "comment_id": comment_id },
			dataType: "text",
			success: function (data) {
				if(data == "1") {
					if(isReply == true) {
						origin_article_detail_data.article_comment_list[origin_comment_index].reply_list[reply_index].like_flag = true;			
					} else {
						origin_article_detail_data.article_comment_list[index].comment_like_flag = "true";
					}
					
					img.src = "/static/images/article_detail_like_comment_button_pressed.png";
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

function removeArticleDetail(event) {
	if(event.target.className == "article-detail-wrapper") {
		event.target.remove();
		document.querySelector("body").style = "";
		relate_comment_flag = false;
	}
}

function makeArticleDetail(article_data) {
	const article_detail_wrapper = document.createElement("div");
	article_detail_wrapper.className = "article-detail-wrapper";
	
	const detail_container = document.createElement("div");
	detail_container.className = "detail-container";
	
	const detail_images = document.createElement("div");
	detail_images.className = "detail-images";
	
	const media_list = article_data.media_list;
	for(let i=0; i< media_list.length; i++) {
		if(media_list[i].media_type == "image") {
			const image = document.createElement("img");
			image.src = `../../../../file_upload/article_medias/${article_data.id}/${article_data.media_list[i].media_name}`;
			if(i == 0) image.className = "current";
			detail_images.appendChild(image);
		} else if(media_list[i].media_type == "video") {
			const video = document.createElement("video");
			video.src = `../../../../file_upload/article_medias/${article_data.id}/${article_data.media_name_list[i].media_name}`;
			video.autoplay = "autoplay";
			if(i == 0) video.className = "current";
			detail_images.appendChild(video);
		}
	}
	
	if(media_list.length > 1) {
		const picture_controller = document.createElement("div");
		picture_controller.className = "picture-controller";
		
		const prev_image_button = document.createElement("button");
		const next_image_button = document.createElement("button");
		
		prev_image_button.className = "prev-image";
		next_image_button.className = "next-image active";
		
		prev_image_button.innerHTML = `<img src="/static/images/new_article_prev_button.png">`;
		next_image_button.innerHTML = `<img src="/static/images/new_article_next_button.png">`;
		
		picture_controller.appendChild(prev_image_button);
		picture_controller.appendChild(next_image_button);
		
		detail_images.appendChild(picture_controller);
	}
	
	detail_container.appendChild(detail_images);
	
	// -----------------------------------------------------
	// article images added
	
	const article_info = document.createElement("div");
	article_info.className = "article-info";
	
	const article_header = document.createElement("div");
	article_header.className = "article-header";
	article_header.innerHTML = `<div class="writer-image">
										                        <img src="${article_data.has_profile_image == 'true' ? '../../../../file_upload/user_profile_image/' + article_data.file_name : '/static/images/basic_profile_image.jpg'}" alt="게시글 작성자 프로필 이미지">
										                    </div>
										                    <div class="writer-info">
										                        <a href="#" class="writer-username">${article_data.username}</a>
${article_data.feature == "null" || article_data.feature == null ? '' : '<span class="remark">' + article_data.feature + '</span>'}
										                    </div>
										                    <button type="button" class="article-menu">
										                        <img src="/static/images/article_menu.png" alt="옵션 더 보기">
										                    </button>`;
										                    
	article_info.appendChild(article_header);
	
	// -----------------------------------------------------
	// article header added
	
	const comments_wrapper = document.createElement("div");
	comments_wrapper.className = "comments-wrapper";
	
	const article_upload_time = makeUploadTimeMessage(article_data.article_create_date);
	const article_contents = makeContentsTags(article_data.contents);
	const article = document.createElement("div");
	article.className = "detail-contents";
	article.innerHTML = `<div>
					                            <div class="writer-image">
					                                <img src="${article_data.has_profile_image == 'true' ? '../../../../file_upload/user_profile_images/' + article_data.file_name : '/static/images/basic_profile_image.jpg'}" alt="">
					                            </div>
					                            <div class="detail-texts">
					                                <div class="detail-content">
					                                    <span class="writer-username">${article_data.username}</span>
					                                    <span
					                                        class="content-description">${article_contents}</span>
					                                </div>
					                                <div class="reply-buttons">
					                                    <span class="upload-time">${article_upload_time}</span>
					                                </div>
					                            </div>
					                        </div>`;
	
	comments_wrapper.appendChild(article);
	
	// -----------------------------------------------------
	// article added
	
	const comment_list = article_data.article_comment_list;
	if(comment_list.length > 0) {
		for(let i=0; i< comment_list.length; i++) {
			const comment = comment_list[i];
			const upload_time = makeUploadTimeMessage(comment.create_date);
			const detail_contents = document.createElement("div");
			detail_contents.className = "detail-contents";
			detail_contents.innerHTML = `<div>
											                            <div class="writer-image">
											                                <img src="${comment.has_profile_image == 'true' ? '../../../../file_upload/user_profile_images/' + comment.file_name : '/static/images/basic_profile_image.jpg'}" alt="">
											                            </div>
											                            <div class="detail-texts">
											                                <div class="detail-content">
											                                    <span class="writer-username">${comment.username}</span>
											                                    <span
											                                        class="content-description">${comment.contents}</span>
											                                </div>
											                                <div class="reply-buttons">
											                                    <span class="upload-time">${upload_time}</span>
											                                    ${Number(comment.comment_like_user_count) > 0 ? '<button type="button" class="comment_like_count">좋아요 ' + comment.comment_like_user_count + '개</button>' : ''}
											                                    <button type="button" class="reply">답글 달기</button>
											                                </div>
											                            </div>
											                        </div>
											                        <button class="comment-like-button">
											                            <img src="/static/images/article_detail_like_comment_button${comment.comment_like_flag == "true" ? '_pressed' : ''}.png" alt="">
											                        </button>`;
											                        
			comments_wrapper.appendChild(detail_contents);
			
			if(comment.related_comment_count > 0) {
				const show_reply_comment = document.createElement("div");
				show_reply_comment.className = "show-reply-comment";
				show_reply_comment.innerHTML = `<div class="show-reply-button">
															                            <span></span>
															                            <button type="button">답글 보기(${comment.related_comment_count}개)</button>
															                        </div>`;
				comments_wrapper.appendChild(show_reply_comment);
			}
		}
	}
	
	article_info.appendChild(comments_wrapper);
	
	// -----------------------------------------------------
	// comments added
	
	const article_description = document.createElement("div");
	article_description.className = "article-description";
	article_description.innerHTML = `<div class="article-icons">
												                        <div class="left-button">
												                            <button type="button">
												                                <span class="like-button ${article_data.like_flag == 'true' ? 'pressed' : ''}"></span>
												                            </button>
												                            <button type="button">
												                                <span class="comment-button"></span>
												                            </button>
												                            <button type="button">
												                                <span class="share-button"></span>
												                            </button>
												                        </div>
												                        <div class="right-button">
												                            <button type="button">
												                                <span class="save-to-collection"></span>
												                            </button>
												                        </div>
												                    </div>
												                    <div class="who-likes">
												
												                        <div class="represent-message">
												                            <span class="represent-comment">
												                                <span class="how-many-likes">좋아요 ${article_data.total_like_count}개</span>
												                            </span>
												                        </div>
												                    </div>
												                    <div class="upload-time-wrapper">
												                        <span class="upload-time">${article_upload_time}</span>
												                    </div>
												                    <form class="write-comment">
												                        <div class="emoji-wrapper">
												                            <img src="/static/images/emoji_icon.png" alt="">
												                        </div>
												                        <textarea name="" placeholder="댓글 달기..." autocomplete="off" autocorrect="off"></textarea>
												                        <button type="button" disabled class="comment-submit-button disabled">게시</button>
												                    </form>`;
	
	article_info.appendChild(article_description);
	
	// -----------------------------------------------------
	// comments added
	
	detail_container.appendChild(article_info);
	
	// -----------------------------------------------------
	// article_info added
	
	article_detail_wrapper.appendChild(detail_container);
	
	return article_detail_wrapper;
}
