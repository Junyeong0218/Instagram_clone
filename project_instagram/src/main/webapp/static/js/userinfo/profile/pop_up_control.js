const account_menus = document.querySelector("#account-menus");
const account_menu_button = document.querySelector(".account-menu-button");
const buttons = account_menus.querySelectorAll("button");

const follow_info = document.querySelector("#follow-info");
const follower_button = document.querySelector(".follower").parentElement;
const following_button = document.querySelector(".following").parentElement;

let people_tab_count = 0;
let has_more_users = true;
let hashtag_tab_count = 0;
let has_more_hashtags = true;
let follower_tab_count = 0;
let has_more_followers = true;

let origin_following_list;
let origin_hashtag_list;
let origin_follower_list;

if(account_menu_button != null && typeof account_menu_button != "undefined") account_menu_button.onclick = showAccountMenus;
account_menus.onclick = closeAccountMenus;

follower_button.onclick = showFollowInfo;
following_button.onclick = showFollowInfo;

for (let i = 0; i < buttons.length; i++) {
    buttons[i].onclick = executeSeparateEvent;
}

function showFollowInfo(event) {
	const span = event.target.children[0];
	if(span.className == "following") {
		people_tab_count = 0;
		hashtag_tab_count = 0;
		has_more_users = true;
		origin_following_list = null;
		origin_hashtag_list = null;
		follow_info.querySelector(".follow-description").innerHTML = 
				`<div class="follow-info-header">
						<div class="follow-title">팔로잉</div>
						<div class="modal-closer">
							<img src="/static/images/modal_closer.png" alt="" >
						</div>
					</div>
					<div class="following-tab">
						<button type="button" class="follow-people active">사람</button>
						<button type="button" class="follow-hashtag">해시태그</button>
					</div>
					<div class="people-info active">
					
					</div>
					<div class="hashtag-info">
					
					</div>`;
		loadFollowing();
		follow_info.querySelector(".follow-people").onclick = changeTabForFollowing;
		follow_info.querySelector(".follow-hashtag").onclick = changeTabForFollowing;
	} else {
		follower_tab_count = 0;
		has_more_followers = true;
		origin_follower_list = null;
		follow_info.querySelector(".follow-description").innerHTML = 
				`<div class="follow-info-header">
					<div class="follow-title">팔로워</div>
					<div class="modal-closer">
						<img src="/static/images/modal_closer.png" alt="" >
					</div>
				</div>
				<div class="people-info active">
				
				</div>`;
		loadFollowers();
	}
	follow_info.classList.add("to-show");
	follow_info.classList.remove("to-hidden");
	
	follow_info.querySelector(".modal-closer").onclick = () => {
		follow_info.classList.add("to-hidden");
		follow_info.classList.remove("to-show");
	}
}

function changeTabForFollowing(event) {
	if(event.target.classList[0] == "follow-people") {
		follow_info.querySelector(".follow-people").classList.add("active");
		follow_info.querySelector(".follow-hashtag").classList.remove("active");
		follow_info.querySelector(".people-info").classList.add("active");
		follow_info.querySelector(".hashtag-info").classList.remove("active");
	} else {
		follow_info.querySelector(".follow-people").classList.remove("active");
		follow_info.querySelector(".follow-hashtag").classList.add("active");
		follow_info.querySelector(".people-info").classList.remove("active");
		follow_info.querySelector(".hashtag-info").classList.add("active");
	}
}

function loadFollowers() {
	$.ajax({
		type: "get",
		url: "/follow/select-followers",
		data: { "count_indicator": follower_tab_count },
		dataType: "text",
		success: function (data) {
			data = JSON.parse(data);
			follower_tab_count++;
			has_more_followers = data.has_more_followers == "true" ? true : false;
			
			const people_info = follow_info.querySelector(".people-info");
			
			origin_follower_list = data.follower_list;
			
			for(let i=0; i<origin_follower_list.length; i++) {
				const row = makePeopleTag(origin_follower_list[i]);
				people_info.appendChild(row);
			}
		},
		error: function (xhr, status, error) {
			console.log(xhr);
			console.log(status);
			console.log(error);
		}
	});
}

function loadFollowingHashTag() {
	$.ajax({
		type: "get",
		url: "/follow/select-following-hashtags",
		data: { "count_indicator": hashtag_tab_count },
		dataType: "text",
		success: function (data) {
			data = JSON.parse(data);
			hashtag_tab_count++;
			
			const hashtag_info = follow_info.querySelector(".hashtag-info");
			
			for(let i=0; i<data.length; i++) {
				const row = makePeopleTag(data[i]);
				hashtag_info.appendChild(row);
			}
		},
		error: function (xhr, status, error) {
			console.log(xhr);
			console.log(status);
			console.log(error);
		}
	});
}

function loadFollowing() {
	if(has_more_users == false) return;
	$.ajax({
		type: "get",
		url: "/follow/select-following-users",
		data: { "count_indicator": people_tab_count },
		dataType: "text",
		success: function (data) {
			data = JSON.parse(data);
			people_tab_count++;
			has_more_users = data.has_more_users == "true" ? true : false;
			
			const people_info = follow_info.querySelector(".people-info");
			
			origin_following_list = data.following_user_list;
			
			for(let i=0; i<origin_following_list.length; i++) {
				const row = makePeopleTag(origin_following_list[i]);
				people_info.appendChild(row);
				row.querySelector(".follow-button").onclick = (event) => {
					unfollowUser(event, i);
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

function unfollowUser(event, index) {
	console.log(event);
	$.ajax({
		type: "post",
		url: "/follow/delete-follow-user",
		data: { "partner_user_id": origin_following_list[index].id },
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

function makePeopleTag(user_data) {
	const row = document.createElement("div");
	row.className = "row";
	row.innerHTML = `<div class="user-profile-image">
											<img src="/static/images/${user_data.has_profile_image == 'true' ? 'user_profile_images/' + user_data.file_name : 'basic_profile_image.jpg'}" alt="">
										</div>
										<div class="summary">
											<span class="user-summary-username">${user_data.username}</span>
											<span class="user-summary-name">${user_data.name}</span>
										</div>
										<div>
											<button type="button" class="follow-button">팔로잉</button>
										</div>`;
	return row;
}

function showAccountMenus() {
    account_menus.classList.add("to-show");
    account_menus.classList.remove("to-hidden");
    document.querySelector("body").style = "overflow: hidden;";
}

function closeAccountMenus(event) {
    if (event.target == account_menus || event.target.id == "pop-up-closer") {
        account_menus.classList.add("to-hidden");
        account_menus.classList.remove("to-show");
        document.querySelector("body").style = "";
    }
}

function executeSeparateEvent(event) {
    const button = event.target;
    switch (button.id) {
        case "change-password":
            location.href = "/userinfo?change-password=true";
            break;
        case "qr-code":
            //
            break;
        case "website":
            //
            break;
        case "alert":
            //
            break;
        case "privacy":
            //
            break;
        case "activity":
            //
            break;
        case "email-from-instagram":
            //
            break;
        case "report-problem":
            //
            break;
        case "logout":
            location.href = "/logout";
            break;
    }
}