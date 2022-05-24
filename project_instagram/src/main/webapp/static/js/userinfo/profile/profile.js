const userinfo_profile_image = document.querySelector(".profile-image");
const target_userinfo = document.querySelector(".menu-buttons");
const account_menu_modal_button = target_userinfo.querySelector(".account-menu-button");
const send_message_button = target_userinfo.querySelector(".send-message");
const follow_user_button = target_userinfo.querySelector(".follow-user");
const target_user_follow_info = document.querySelector(".follow-info");

const articles = document.querySelector(".articles");

const uri_username = location.pathname.replace("/profile/", "");

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
			applyData(data);
		},
		error: function (xhr, status) {
			console.log(xhr);
			console.log(status);
		}
	});
}

function applyData(data) {
	appendTargetUserData(data);
	if(data.article_count == "0") {
		appendNonArticles();
	} else {
		appendArticleTags(data.article_list);
	}
}

function appendTargetUserData(data) {
	target_user_follow_info.querySelector(".article-quantity").innerText = data.article_count;
	target_user_follow_info.querySelector(".follower").innerText = data.follower;
	target_user_follow_info.querySelector(".following").innerText = data.following;
	if(data.has_profile_image == "true") {
		userinfo_profile_image.src = "/static/file_upload/user_profile_image/" + data.file_name;
	}
	target_userinfo.innerHTML = `<span class="target-username">${data.username}</span>`;
	if(principal.id == data.user_id) {
		target_userinfo.innerHTML += `
			<a class="edit-profile" href="/userinfo">프로필 편집</a>
	        <button type="button" class="account-menu-button">
	        	<img src="/static/images/profile-menu-settings.png" alt="">
	        </button>`;
		const account_menu_button = document.querySelector(".account-menu-button");
		account_menu_button.onclick = showAccountMenus;
	} else {
		if(data.follow_flag == "true") {
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
				const empty = document.createElement("a");
				empty.href = "#";
				row.appendChild(empty);
			} else{
				const article = document.createElement("a");
				article.href = "#";
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
			}
		}
		contents.appendChild(row);
	}
	articles.appendChild(contents);
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