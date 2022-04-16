const recommend_users = document.querySelector(".recommend-users");

function loadRecommendUsers() {
	$.ajax({
		type: "get",
		url: "/load-recommend-users",
		dataType: "text",
		success: function (data) {
			data = JSON.parse(data);
			for(let i=0; i < data.length; i++) {
				const tag = makeRecommendUserTag(data[i]);
				recommend_users.appendChild(tag);
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
						<img src="/static/images/${userData.has_profile_image == "true" ? 'user_profile_images/' + userData.file_name : 'basic_profile_image.jpg'}" alt="추천 유저 프로필 이미지">
					</div>
					<div class="user-description">
						<span class="recommend-username">${userData.username}</span>
						<span class="recommend-info">Instagram 신규 가입</span>
					</div>
					<button type="button>팔로우</button>"`;
	
	return div;
}


/*<div class="recommend-user">
			<div class="user-image">
				<img src="/static/images/profile_image.png" alt="추천 유저 프로필 이미지">
			</div>
			<div class="user-description">
				<span class="recommend-username">sweet.bbom</span> <span
					class="recommend-info">Instagram 신규 가입</span>
			</div>
			<button type="button">팔로우</button>
		</div>*/