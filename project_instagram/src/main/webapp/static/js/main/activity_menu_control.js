const activity_button = document.querySelector(".show-activity");
const activity_menu_wrapper = document.querySelector(".activity-menu-wrapper");

activity_button.onclick = (event) => {
	console.log(event);
	$.ajax({
		type: "get",
		url: "/follow/select-activities",
		dataType: "text",
		success: function (data) {
			data = JSON.parse(data);
			console.log(data);
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
		const row = document.createElement("div");
		row.className = "row";
		row.innerHTML = `<div class="profile-image"><img src="/static/images/user_profile_images/hippo2003.ico" alt="">
										  </div>
										  <div class="activity-message">
										  	  <span class="target-username">hippo2003</span>님이 댓글에서 회원님을 언급했습니다:<span class="message"> <span class="tag">@hippo2003</span> 댓글 -> 답글 -> 답글 test22</span><span class="ago">1주</span>
									  	  </div>
									  	  <div class="origin-image">
												<img src="/static/images/article_medias/1/media01.webp" alt="">
										  </div>`;
		activities.appendChild(row);
	}
	activity_menu.appendChild(activities);
	
	return activity_menu;
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