const userinfo_profile_image = document.querySelector(".profile-image");
const target_userinfo = document.querySelector(".menu-buttons");
const account_menu_modal_button = target_userinfo.querySelector(".account-menu-button");
const send_message_button = target_userinfo.querySelector(".send-message");
const follow_user_button = target_userinfo.querySelector(".follow-user");
const target_user_follow_info = document.querySelector(".follow-info");
const to_update_article_button = document.querySelector("#change-article-content");
const delete_article_button = document.querySelector("#delete-article");
const update_article_modal = document.querySelector(".update-article-modal");
const update_article_contents = update_article_modal.querySelector("textarea");

const articles = document.querySelector(".articles");

const uri_username = location.pathname.replace("/profile/", "");

let origin_articles;
let origin_article_detail;

to_update_article_button.onclick = (event) => {
	event.target.parentElement.children[2].click();
	document.querySelector(".article-detail-wrapper").click();
	update_article_modal.querySelector(".principal-user > span").innerText = principal.username;
	if(principal.has_profile_image == "true") {
		update_article_modal.querySelector(".principal-user > img").src = "/static/file_upload" + principal.file_name;
	}
	const update_content_length = update_article_modal.querySelector(".current-length");
	update_article_contents.oninput = (event1) => {
		if(event1.target.value.length > 2200) {
			alert("2,200자 이상 입력할 수 없습니다.");
			event1.target.value = event.target.value.substring(0, 2200);
			update_content_length.innerText = event1.target.value.length;
		} else {
			update_content_length.innerText = event1.target.value.length;
		}
	}
	update_article_contents.value = origin_article_detail.contents;
	update_article_modal.querySelector("input[name='feature']").value = origin_article_detail.feature;
	update_content_length.innerText = origin_article_detail.contents.length;
	update_article_modal.classList.add("active");
	
	update_article_modal.querySelector(".update-article-closer").onclick = () => {
		update_article_modal.classList.remove("active");
	}
	
	update_article_modal.querySelector(".update_article").onclick = () => {
		$.ajax({
			type: "put",
			url: "/article/"
		});
	}
}

delete_article_button.onclick = () => {
	console.log("delete article : " + origin_article_detail.id + " !!!");
	$.ajax({
		type: "delete",
		url: "/article/" + origin_article_detail.id,
		headers: { "Authorization": token },
		dataType: "text",
		success: function (data) {
			console.log(data);
			location.reload();
		},
		error: function (xhr, status) {
			console.log(xhr);
			console.log(status);
		}
	});
}

getUserProfileData();

function getUserProfileData() {
	$.ajax({
		type: "get",
		url: "/auth/profile/" + uri_username,
		headers: { "Authorization": token },
		dataType: "text",
		success: function (data) {
			data = JSON.parse(data);
			console.log(data);
			origin_articles = data;
			applyData();
		},
		error: function (xhr, status) {
			console.log(xhr);
			console.log(status);
		}
	});
}

function applyData() {
	appendTargetUserData();
	if(origin_articles.article_count == "0") {
		appendNonArticles();
	} else {
		appendArticleTags(origin_articles.article_list);
	}
}

function appendTargetUserData() {
	target_user_follow_info.querySelector(".article-quantity").innerText = origin_articles.article_count;
	target_user_follow_info.querySelector(".follower").innerText = origin_articles.follower;
	target_user_follow_info.querySelector(".following").innerText = origin_articles.following;
	if(origin_articles.has_profile_image == "true") {
		userinfo_profile_image.src = "/static/file_upload" + origin_articles.file_name;
	}
	target_userinfo.innerHTML = `<span class="target-username">${origin_articles.username}</span>`;
	if(principal.id == origin_articles.user_id) {
		target_userinfo.innerHTML += `
			<a class="edit-profile" href="/userinfo">프로필 편집</a>
	        <button type="button" class="account-menu-button">
	        	<img src="/static/images/profile-menu-settings.png" alt="">
	        </button>`;
		const account_menu_button = document.querySelector(".account-menu-button");
		account_menu_button.onclick = showAccountMenus;
	} else {
		if(origin_articles.follow_flag == "true") {
			target_userinfo.innerHTML += `
				<button type="button" class="send-message">메세지 보내기</button>
				<button type="button" class="follow-user">팔로우</button>`;
		} else {
			target_userinfo.innerHTML += `<button type="button" class="follow-user">팔로우</button>`;
		}
	}
}

function appendArticleTags(article_list) {
	const contents = document.createElement("div");
	contents.className = "contents";
	for(let i = 0; i < Math.ceil(article_list.length / 3); i++) {
		// make new row
		const row = document.createElement("div");
		row.className = "row";
		for(let j = i * 3; j < (i + 1) * 3; j++) {
			const article_data = article_list[j];
			if(article_data == null || typeof article_data == "undefined") {
				// empty a tag
				const empty = document.createElement("button");
				empty.type = "button";
				row.appendChild(empty);
			} else{
				const article = document.createElement("button");
				article.type = "button";
				if(article_data.media_list[0].media_type == "image") {
					article.innerHTML = `<img src="/static/file_upload${article_data.media_list[0].media_name}" alt="">
															<div class="summary">
																<div class="like-info">
																	<img src="/static/images/profile_article_summary_like_icon.png" alt="">
																	<span class="like-count">${article_data.like_count}</span>
																</div>
																<div class="comment-info">
																	<img src="/static/images/profile_article_summary_comment_icon.png" alt="">
																	<span class="comment-count">${article_data.comment_count}</span>
																</div>
															</div>`;
				} else {
					article.innerHTML = `<img src="/static/file_upload${article_data.media_list[0].media_name}" alt="">`;
				}
				row.appendChild(article);
				article.onclick = () => {
					selectArticleDetail(j);
				}
			}
		}
		contents.appendChild(row);
	}
	articles.appendChild(contents);
}

function selectArticleDetail(index) {
	$.ajax({
		type: "get",
		url: "/article/" + origin_articles.article_list[index].id,
		headers: { "Authorization": token },
		dataType: "text",
		success: function (data) {
			origin_article_detail = JSON.parse(data);
			console.log(origin_article_detail);
			const article_detail = makeArticleDetail(origin_article_detail);
			container.appendChild(article_detail);
			article_detail.onclick = removeArticleDetail;
			
			const article_menu = article_detail.querySelector(".article-menu");
			article_menu.onclick = showArticleMenu;
			
			const like_button = article_detail.querySelector(".like-button").parentElement;
			like_button.onclick = toggleLikeArticle;
			
			const textarea = article_detail.querySelector("textarea");
			textarea.oninput = activeCommentSubmitButton;
			
			const comment_submit_button = article_detail.querySelector(".comment-submit-button");
			comment_submit_button.onclick = submitComment;
			
			const comment_like_buttons = article_detail.querySelectorAll(".comment-like-button");
			for(let i=0; i< comment_like_buttons.length; i++) {
				comment_like_buttons[i].onclick = (event) => {
					toggleCommentLike(event, false, -1, -1);	
				}
			}
			
			const reply_buttons = article_detail.querySelectorAll(".reply");
			for(let i=0; i< reply_buttons.length; i++) {
				reply_buttons[i].onclick = addAtSignTagToTextArea;
			}
			
			const show_reply_buttons = article_detail.querySelectorAll(".show-reply-button");
			if(show_reply_buttons.length != 0) {
				for(let i=0; i < show_reply_buttons.length; i++) {
					show_reply_buttons[i].onclick = toggleReplies;
				}
			}
			
			const picture_controller = article_detail.querySelector(".picture-controller");
			if(picture_controller != null && typeof picture_controller != "undefined") {
				picture_controller.querySelector(".prev-image").onclick = moveToPrevImage;
				picture_controller.querySelector(".next-image").onclick = moveToNextImage;
			}
			
			document.querySelector("body").style = "overflow: hidden;";
		},
		error: function (xhr, status) {
			console.log(xhr);
			console.log(status);
		}
	});
}

function appendNonArticles() {
	const div = document.createElement("div");
	div.className = "no-contents";
	div.innerHTML = `
        <img src="/static/images/profile_no_contents_background.jpg" alt="">
        <div class="recommend-to-add">
            <span class="bold-text">소중한 순간을 포착하여 공유해보세요.</span>
            <span class="common-text">앱을 다운로드하고 첫 사진이나 동영상을 공유해보세요.</span>
            <div class="download-app-buttons">
                <a href="#" class="appstore-download"><img
                        src="/static/images/download_applestore.png" alt=""></a>
                <a href="#" class="googleplay-download"><img
                        src="/static/images/download_playstore.png" alt=""></a>
            </div>
        </div>
	`;
	articles.appendChild(div);
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
			image.src = `/static/file_upload${article_data.media_list[i].media_name}`;
			if(i == 0) image.className = "current";
			detail_images.appendChild(image);
		} else if(media_list[i].media_type == "video") {
			const video = document.createElement("video");
			video.src = `/static/file_upload${article_data.media_name_list[i].media_name}`;
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
										                        <img src="${article_data.has_profile_image == 'true' ? '/static/file_upload' + article_data.file_name : '/static/images/basic_profile_image.jpg'}" alt="게시글 작성자 프로필 이미지">
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
					                                <img src="${article_data.has_profile_image == 'true' ? '/static/file_upload' + article_data.file_name : '/static/images/basic_profile_image.jpg'}" alt="">
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
											                                <img src="${comment.has_profile_image == 'true' ? '/static/file_upload' + comment.file_name : '/static/images/basic_profile_image.jpg'}" alt="">
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