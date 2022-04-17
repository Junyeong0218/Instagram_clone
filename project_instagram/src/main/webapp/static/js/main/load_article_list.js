const container = document.querySelector(".container");
const article_wrapper = document.querySelector(".articles");
let origin_article_list = new Array();

let article_load_count = 0;

function loadArticleList() {
	$.ajax({
		type: "get",
		url: "/load-articles",
		dataType: "text",
		success: function (data) {
			data = JSON.parse(data);
			for(let i = 0; i < data.length; i++) {
				origin_article_list.push(data[i]);
				const articleTag = makeArticleTag(data[i]);
				article_wrapper.appendChild(articleTag);
				
				const article_menu = articleTag.querySelector(".article-menu");
				article_menu.onclick = showArticleMenu;
				
				const like_button = articleTag.querySelector(".like-button").parentElement;
				like_button.onclick = toggleLikeArticle;
				
				const textarea = articleTag.querySelector("textarea");
				textarea.oninput = activeCommentSubmitButton;
				
				const comment_submit_button = articleTag.querySelector(".comment-submit-button");
				comment_submit_button.onclick = submitComment;
				
				const show_comments_button = articleTag.querySelector(".show-comments-button");
				show_comments_button.onclick = showArticleDetail;
				
				console.log(origin_article_list);
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
												<img src="/static/images/${articleData.has_profile_image == "true" ? articleData.file_name : 'basic_profile_image.jpg'}" alt="게시글 작성자 프로필 이미지">
											</div>
											<div class="writer-info">
												<a href="#" class="writer-username">${articleData.username}</a>
												${articleData.feature == "true" ? '<span class="remark">' + articleData.feature + '</span>' : ''}
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
	
	const mediaList = articleData.media_name_list;
	for(let i = 0; i < mediaList.length; i++) {
		const li = document.createElement("li");
		li.className = "picture";
		li.innerHTML = `<img src="/static/images/article_medias/${articleData.id}/${mediaList[i]}" alt="게시글 이미지">`;
		ul.appendChild(li);
		
		if(mediaList.length > 1) {
			const dot = document.createElement("span");
			dot.className = i == 0 ? "dot current-index" : "dot";
			dots.appendChild(dot);
		}
	}
	
	pictures.appendChild(ul);
	pictures.appendChild(dots);
	
	// ------------------------------------
	// make article pictures and dots
	
	const upload_time_message = makeUploadTimeMessage(articleData.create_date);
	
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
														<span class="description-text">${articleData.contents}</span>
													</div>
${articleData.total_commented_user_count > 2 ? `<div class="article-texts">
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

function makeUploadTimeMessage(create_date) {
	const upload_time = new Date(create_date);
	const now = new Date();
	const ago = new Date(now - upload_time);
	
	const year = ago.getFullYear() - 1970;
	const month = ago.getMonth() + 1;
	const date = ago.getDate();
	const hour = ago.getHours();
	const minute = ago.getMinutes();
	const second = ago.getSeconds();
	
	if(year > 0) {
		return `${year}년 전`;
	} else if(month > 1) {
		return `${month}개월 전`;
	} else if(date > 1) {
		return `${date - 1}일 전`;
	} else if(hour > 0) {
		return `${hour}시간 전`;
	} else if(minute > 0) {
		return `${minute}분 전`;
	} else {
		return `${second}초 전`;
	}
}

function showArticleMenu(event) {
        pop_up.classList.remove("to-hidden");
        pop_up.classList.add("to-show");

        const buttons = pop_up.querySelectorAll(".article-control-button");
        buttons[buttons.length - 1].onclick = () => {
                pop_up.classList.remove("to-show");
                pop_up.classList.add("to-hidden");
        }
}

function toggleLikeArticle(event) {
	const span = event.target.children[0];
	const article_index = getArticleIndex(event.path[4]);
	if(article_index != -1) {
		const article_data = origin_article_list[article_index];
		if(article_data.like_flag == "true") {
			$.ajax({
				type: "post",
				url: "/article/delete-like-article",
				data: { "article_id": article_data.id },
				dataType: "text",
				success: function (data) {
					if(data == "1") {
						span.classList.remove("pressed");
						const how_many_likes = event.path[3].querySelector(".how-many-likes");
						article_data.like_flag = "false";
						
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
						const how_many_likes = event.path[3].querySelector(".how-many-likes");
						
						how_many_likes.innerText = `좋아요 ${article_data.total_like_count + 1}개`;
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
	const article_index = getArticleIndex(event.path[3]);
	const article_id = origin_article_list[article_index].id;
	const comment = event.target.previousElementSibling.value;
	
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

function showArticleDetail(event) {
	const article_index = getArticleIndex(event.path[3]);
	const article_id = origin_article_list[article_index].id;
	$.ajax({
		type: "get",
		url: "/article/select-article-detail",
		data: { "article_id": article_id },
		dataType: "text",
		async: "false",
		success: function (data) {
			data = JSON.parse(data);
			console.log(data);
			
			const article_detail_tag = makeArticleDetail(data);
		},
		error: function (xhr, status, error) {
			console.log(xhr);
			console.log(status);
			console.log(error);
		}
	});
}

function makeArticleDetail(article_data) {
	const detail_container = document.createElement("div");
}
