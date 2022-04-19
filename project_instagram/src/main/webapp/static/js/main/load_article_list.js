const container = document.querySelector(".container");
const article_wrapper = document.querySelector(".articles");
let origin_article_list = new Array();
let origin_article_detail_data;

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
				console.log(origin_article_list);
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
				show_comments_button.onclick = showArticleDetail;
				
				const comment_button = article_tag.querySelector(".comment-button").parentElement;
				comment_button.onclick = showArticleDetail;
				
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
	} else if(date == 1 && upload_time.getDay() != now.getDay()) {
		return `${date}일 전`;
	} else if(hour > 0) {
		return `${hour}시간 전`;
	} else if(minute > 0) {
		return `${minute}분 전`;
	} else {
		return `${second}초 전`;
	}
}

function showArticleMenu(event) {
	console.log(event);
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
        if(event.path[4].className != "article-detail-wrapper") {
            document.querySelector("body").style = "";
		}
    }
}

function toggleLikeArticle(event) {
	const span = event.target.children[0];
	const article_index = getArticleIndex(event.path[4]);
	const wrapper = event.path[6];
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
	console.log(event);
	const article_index = getArticleIndex(event.path[3]);
	const wrapper = event.path[5];
	let article_data;
	if(wrapper.className == "article-detail-wrapper") {
		article_data = origin_article_detail_data;
	} else {
		article_data = origin_article_list[article_index];
	}
	console.log(article_data);
	const article_id = article_data.id;
	let comment = event.target.previousElementSibling.value;
	
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
	let article_index = getArticleIndex(event.path[3]);
	let article_id;
	if(article_index == -1) {
		article_index = getArticleIndex(event.path[4]);
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
			console.log(data);
			
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
				comment_like_buttons[i].onclick = toggleCommentLike;
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

function toggleCommentLike(event) {
	const img = event.target.children[0];
	const current_comment = event.path[1];
	const comment_wrapper = event.path[2].children;
	let index = -1;
	for(let i = 1; i < comment_wrapper.length; i++) {
		if(comment_wrapper[i] == current_comment) {
			index = i - 1;
			break;
		}
	}
	
	const comment_list = origin_article_detail_data.article_comment_list;
	const comment_id = comment_list[index].id;
	const comment_like_flag = comment_list[index].comment_like_flag;
	
	if(comment_like_flag == "true") {
		// delete
		$.ajax({
			type: "post",
			url: "/article/delete-comment-like",
			data: { "comment_id": comment_id },
			dataType: "text",
			success: function (data) {
				if(data == "1") {
					origin_article_detail_data.article_comment_list[index].comment_like_flag = "false";
					
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
					origin_article_detail_data.article_comment_list[index].comment_like_flag = "true";
					
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
	}
}

function makeArticleDetail(article_data) {
	const article_detail_wrapper = document.createElement("div");
	article_detail_wrapper.className = "article-detail-wrapper";
	
	const detail_container = document.createElement("div");
	detail_container.className = "detail-container";
	
	const detail_images = document.createElement("div");
	detail_images.className = "detail-images";
	
	for(let i=0; i< article_data.media_name_list.length; i++) {
		const image = document.createElement("img");
		image.src = `/static/images/article_medias/${article_data.id}/${article_data.media_name_list[i]}`;
		detail_images.appendChild(image);
	}
	
	detail_container.appendChild(detail_images);
	
	// -----------------------------------------------------
	// article images added
	
	const article_info = document.createElement("div");
	article_info.className = "article-info";
	
	const article_header = document.createElement("div");
	article_header.className = "article-header";
	article_header.innerHTML = `<div class="writer-image">
										                        <img src="/static/images/${article_data.has_profile_image == 'true' ? 'user_profile_image/' + article_data.file_name : 'basic_profile_image.jpg'}" alt="게시글 작성자 프로필 이미지">
										                    </div>
										                    <div class="writer-info">
										                        <a href="#" class="writer-username">${article_data.username}</a>
${article_data.feature == "null" ? '' : '<span class="remark">' + article_data.feature + '</span>'}
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
	const article = document.createElement("div");
	article.className = "detail-contents";
	article.innerHTML = `<div>
					                            <div class="writer-image">
					                                <img src="/static/images/${article_data.has_profile_image == 'true' ? 'user_profile_images/' + article_data.file_name : 'basic_profile_image.jpg'}" alt="">
					                            </div>
					                            <div class="detail-texts">
					                                <div class="detail-content">
					                                    <span class="writer-username">${article_data.username}</span>
					                                    <span
					                                        class="content-description">${article_data.contents}</span>
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
	for(let i=0; i< comment_list.length; i++) {
		const comment = comment_list[i];
		const upload_time = makeUploadTimeMessage(comment.create_date);
		const detail_contents = document.createElement("div");
		detail_contents.className = "detail-contents";
		detail_contents.innerHTML = `<div>
										                            <div class="writer-image">
										                                <img src="/static/images/${comment.has_profile_image == 'true' ? 'user_profile_images/' + comment.file_name : 'basic_profile_image.jpg'}" alt="">
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
