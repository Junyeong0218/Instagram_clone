const story_area = document.querySelector(".story-area");

function loadRecentStories() {
	$.ajax({
		type: "get",
		url: "/story/recent",
		headers: { "Authorization" : token },
		dataType: "text",
		success: function (data) {
			data = JSON.parse(data);
			if(data.length == 0) {
				const tag = makeNoStoryTag();
				story_area.appendChild(tag);
			} else {
				for(let i=0; i<data.length; i++) {
					const tag = makeStoryTag(data[i]);
					story_area.appendChild(tag);
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

function makeStoryTag(userData) {
	const li = document.createElement("li");
	li.className = "story";
	li.innerHTML = `<button type="button" class="story-image" id="${userData.id}">
						<img src="${userData.has_profile_image == "true" ? '../../../../file_upload/user_profile_images/' + userData.file_name : '/static/images/basic_profile_image.jpg'}" alt="스토리 프로필 이미지">
					</button> <span class="story-username">${userData.username}</span>`;
	
	return li;						
}

function makeNoStoryTag() {
	const li = document.createElement("li");
	li.className = "has-no-stories";
	li.innerText = "최신 스토리가 없습니다.";
	
	return li;
}