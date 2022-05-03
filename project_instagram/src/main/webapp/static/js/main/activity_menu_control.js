const activity_button = document.querySelector(".show-activity");
const activity_menu_wrapper = document.querySelector(".activity-menu-wrapper");

activity_button.onclick = (event) => {
	if(activity_menu_wrapper.children.length != 0) {
		activity_menu_wrapper.innerHTML = "";
		return;
	}
	console.log(event);
	$.ajax({
		type: "get",
		url: "/follow/select-activities",
		dataType: "text",
		success: function (data) {
			data = JSON.parse(data);
			console.log(data);
			const activity_menu_tag = makeActivityMenuTag(data);
			activity_menu_wrapper.appendChild(activity_menu_tag);
			
		},
		error: function (xhr, status, error) {
			console.log(xhr);
			console.log(status);
			console.log(error);
		}
	});
};








// -----------------------------------------------
// Functions

function makeActivityMenuTag(activity_list) {
	const activity_menu = document.createElement("div");
	activity_menu.className = "activity-menu";
	activity_menu.innerHTML = `<div class="activity-arrow"></div>`;
	
	const activities = document.createElement("div");
	activities.className = "activities";
	activities.innerHTML = `<span class="this-month">이번 달</span>`;
	
	for(let i=0; i < activity_list.length; i++) {
		const message_tag = makeATags(activity_list[i].contents);
		/*const message_tag = activity_list[i].contents;*/
		console.log(activity_list[i].contents);
		const ago_tag = makeAgoTag(activity_list[i].create_date);
		const row = document.createElement("div");
		row.className = "row";
		row.innerHTML = `<div class="profile-image"><img src="/static/images/${activity_list[i].has_profile_image == true ? 'user_profile_images/' + activity_list[i].file_name : 'basic_profile_image.jpg'}" alt="">
										  </div>
										  <div class="activity-message">
										  	  <span class="target-username">${activity_list[i].username}</span>${activity_list[i].activity_message}<span class="message"> ${message_tag != 'null' ? ':' + message_tag : ''}${ago_tag}</span>
									  	  </div>`;
		if(activity_list[i].activity_flag == "follow") {
			const button = document.createElement("button");
			button.type= "button";
			button.className = "activity-follow";
			button.innerText = "팔로잉";
			row.appendChild(button);
		} else {
			const div = document.createElement("div");
			div.className = "origin-image";
			div.innerHTML = `<img src="/static/images/article_medias/${activity_list[i].article_id}/${activity_list[i].media_name}" alt="">`;
			row.appendChild(div);
			
		}
		activities.appendChild(row);
	}
	activity_menu.appendChild(activities);
	
	return activity_menu;
}

function makeAgoTag(create_date) {
	const activity_create_date = new Date(create_date);
	const now = new Date();
	const ago = new Date(now - activity_create_date);
	ago.setUTCHours(-9);
	
	let year = ago.getFullYear() - 1970;
	let month = ago.getMonth() + 1;
	let date = ago.getDate();
	let hour = ago.getHours();
	let minute = ago.getMinutes();
	let second = ago.getSeconds();

	if(year > 0) {
		return `<span class="ago">${year}년</span>`;
	} else if(month > 1) {
		return `<span class="ago">${month}개월</span>`;
	} else if(date > 6) {
		return `<span class="ago">${date/7}주</span>`;
	} else if((date == 1 && activity_create_date.getDay() != now.getDay()) || date > 1) {
		return `<span class="ago">${date}일</span>`;
	} else if(hour > 0) {
		return `<span class="ago">${hour}시간</span>`;
	} else if(minute > 0) {
		return `<span class="ago">${minute}분</span>`;
	} else {
		return `<span class="ago">${second}초</span>`;
	}
}

function makeATags(contents) {
	if(contents.indexOf("@") == -1) return contents;
	let tag = "";
	let at_index = contents.indexOf("@");
	while(at_index != -1) {
		tag += contents.substring(0, at_index);
		let blank_index = contents.indexOf(" ", at_index);
		if(blank_index == -1) blank_index = contents.length;
		tag += `<a href="/profile?username=${contents.substring(at_index + 1, blank_index)}" class="tag">${contents.substring(at_index, blank_index)}</a>`;
		contents = contents.substring(blank_index, contents.length);
		at_index = contents.indexOf("@");
	}
	tag += contents;
	return tag;
}




/*<div class="activity-menu">
							<div class="activity-arrow"></div>
							<div class="activities">
								<span class="this-month">이번 달</span>
								<div class="row">
								
									<div class="profile-image"><img src="/static/images/user_profile_images/hippo2003.ico" alt=""></div>
									<div class="activity-message">
										<span class="target-username">hippo2003</span>님이 댓글에서 회원님을 언급했습니다:<span class="message"> <span class="tag">@hippo2003</span> 댓글 -> 답글 -> 답글 test22</span><span class="ago">1주</span>
									</div>
									<div class="origin-image"><img src="/static/images/article_medias/1/media01.webp" alt=""></div>
								
								</div>
								<div class="row">
									<div class="profile-image"><img src="/static/images/user_profile_images/hippo2003.ico" alt=""></div>
									<div class="activity-message">
										<span class="target-username">hippo2003</span>님이 댓글에서 회원님을 언급했습니다:<span class="message"> <span class="tag">@hippo2003</span> 댓글 -> 답글 -> 답글 test22</span><span class="ago">1주</span>
									</div>
									<div class="origin-image"><img src="/static/images/article_medias/1/media01.webp" alt=""></div>
								</div>
								<div class="row">
									<div class="profile-image"><img src="/static/images/user_profile_images/hippo2003.ico" alt=""></div>
									<div class="activity-message">
										<span class="target-username">hippo2003</span>님이 댓글에서 회원님을 언급했습니다:<span class="message"> <span class="tag">@hippo2003</span> 댓글 -> 답글 -> 답글 test22</span><span class="ago">1주</span>
									</div>
									<div class="origin-image"><img src="/static/images/article_medias/1/media01.webp" alt=""></div>
								</div>
								<div class="row">
									<div class="profile-image"><img src="/static/images/user_profile_images/hippo2003.ico" alt=""></div>
									<div class="activity-message">
										<span class="target-username">hippo2003</span>님이 댓글에서 회원님을 언급했습니다:<span class="message"> <span class="tag">@hippo2003</span> 댓글 -> 답글 -> 답글 test22</span><span class="ago">1주</span>
									</div>
									<div class="origin-image"><img src="/static/images/article_medias/1/media01.webp" alt=""></div>
								</div>
								<div class="row">
									<div class="profile-image"><img src="/static/images/user_profile_images/hippo2003.ico" alt=""></div>
									<div class="activity-message">
										<span class="target-username">hippo2003</span>님이 댓글에서 회원님을 언급했습니다:<span class="message"> <span class="tag">@hippo2003</span> 댓글 -> 답글 -> 답글 test22</span><span class="ago">1주</span>
									</div>
									<div class="origin-image"><img src="/static/images/article_medias/1/media01.webp" alt=""></div>
								</div>
								<div class="row">
									<div class="profile-image"><img src="/static/images/user_profile_images/hippo2003.ico" alt=""></div>
									<div class="activity-message">
										<span class="target-username">hippo2003</span>님이 댓글에서 회원님을 언급했습니다:<span class="message"> <span class="tag">@hippo2003</span> 댓글 -> 답글 -> 답글 test22</span><span class="ago">1주</span>
									</div>
									<div class="origin-image"><img src="/static/images/article_medias/1/media01.webp" alt=""></div>
								</div>
							</div>
						</div>*/