const recommend_users = document.querySelector(".recommend-users");
const profile_description_image = document.querySelector(".profile-description-image > img");
const principal_username = document.querySelector(".principal-username");
const principal_nickname = document.querySelector(".principal-nickname");
let recommend_user_data;

if(principal.has_profile_image == "true") {
	profile_description_image.src = "/static/file_upload" + principal.file_name;
}
principal_username.innerText = principal.username;
principal_nickname.innerText = principal.name;

function loadRecommendUsers() {
	$.ajax({
		type: "get",
		url: "/follow/recommendation",
		headers: { "Authorization" : token },
		dataType: "text",
		success: function (data) {
			data = JSON.parse(data);
			recommend_user_data = data;
			console.log(recommend_user_data);
			for(let i=0; i < data.length; i++) {
				const tag = makeRecommendUserTag(data[i]);
				recommend_users.appendChild(tag);
				
				const button = tag.querySelector("button");
				button.onclick = (event) => {
					followUser(event, i);
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

function followUser(event, index) {
	$.ajax({
		type: "post",
		url: "/follow",
		headers: { "Authorization" : token },
		data: { "partner_user_id": recommend_user_data[index].id },
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

function makeRecommendUserTag(userData) {
	const div = document.createElement("div");
	div.className = "recommend-user";
	div.innerHTML = `<div class="user-image">
										<img src="${userData.has_profile_image == "true" ? '/static/file_upload' + userData.file_name : '/static/images/basic_profile_image.jpg'}" alt="추천 유저 프로필 이미지">
									</div>
									<div class="user-description">
										<span class="recommend-username">${userData.username}</span>
										<span class="recommend-info">Instagram 신규 가입</span>
									</div>
									<button type="button">팔로우</button>`;
	
	return div;
}