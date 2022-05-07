const textarea = document.querySelector("textarea[name='written-message']");
const new_message = document.querySelector(".new-message");
const main_new_message = document.querySelector(".main-new-message");
const new_target_modal = document.querySelector(".new-target-modal");
const modal_closer = new_target_modal.querySelector(".close-modal");
const receivers = new_target_modal.querySelector(".receivers");
const receiver = receivers.querySelector(".receiver");
const recommend_receivers = document.querySelector(".recommend-receivers");
const next_button = new_target_modal.querySelector(".next");
const main_content = document.querySelector(".main-content");
const message_wrapper = document.querySelector(".message-wrapper");
const target_user = document.querySelector(".target-user");

let followers;
let target_users = new Array();
let selected_users;

// ------------------------------------------------------------------------------
// EventListeners

textarea.onkeypress = (event) => {
	if(event.keyCode == 13) {
		console.log("insert into direct_message_mst");
		event.target.value = "";
	}
}

textarea.oninput = (event) => {
	console.log(event);
	if(event.inputType == "insertLineBreak") {
		event.target.value = "";
	}
}

new_message.onclick = activeNewTargetModal;
main_new_message.onclick = activeNewTargetModal;
modal_closer.onclick = () => new_target_modal.classList.remove("active");
receiver.oninput = selectUser;
next_button.onclick = makeNewRoom;











// ------------------------------------------------------------------------------
// Functions

function addToUserList(userinfo) {
	const user_list = document.querySelector(".user-list").children;
	let is_exist = false;
	for(let i = 0; i < user_list.length; i++) {
		if(user_list[i].querySelector(".username").innerText == userinfo.username) {
			is_exist = true;
			break;
		}
	}
	if(! is_exist) {
		const button = document.createElement("button");
		button.type = "button";
		button.className = "user";
		button.innerHTML = `<img src="/static/images/${userinfo.has_profile_image == true ? 'user_profile_image/' + userinfo.file_name : 'basic_profile_image.jpg'}">
												<div class="user-description">
													<span class="username">${userinfo.username}</span>
													<span class="name">${userinfo.name}</span>
													<span class="recent-activity"><span class="date"></span></span>
												</div>`;
		if(user_list.length == 0) document.querySelector(".user-list").appendChild(button);
		else										 document.querySelector(".user-list").insertBefore(button, user_list[0]);
		
		// button에 active 이벤트 넣기
	}
	return is_exist;
}

function makeNewRoom() {
	if(target_users.length == 0) return;
	if(target_users.length == 1) {
		target_user.querySelector(".name").innerText = target_users[0].name + "님";
		console.log(target_users[0]);
		const is_exist = addToUserList(target_users[0]);
		
	} else {
		for(let i = 0; i < target_users.length; i++) {
			target_user.querySelector(".name").innerText += target_users[i].name + ", ";
		}
	}
	new_target_modal.classList.remove("active");
	main_content.classList.remove("active");
	message_wrapper.classList.add("active");
}

function insertNewRoomToDB() {
	let data = {};
	const target_user_id = new Array();
	for(let i = 0; i < target_users.length; i++) {
		target_user_id.push(target_users[i].id);
	}
	data["target_users"] = target_user_id;
	$.ajax({
		type: "post",
		url: "/direct/insert-new-room",
		data: data,
		dataType: "text",
		success: function (data) {
			console.log(data);
			if(data == "true") {
				console.log("insert 완료");
			} else if(data == "false") {
				console.log("insert 실패");
			} else {
				console.log(data);
			}
		},
		error: function (xhr, status, error) {
			console.log(xhr);
			console.log(status);
			console.log(error);
		}
	});
	
}

function selectUser(event) {
	console.log(event);
	console.log(event.target.value);
	let keyword = event.target.value.trim();
	if(keyword == null || keyword == "" || typeof keyword == "undefined") return;
	$.ajax({
		type: "get",
		url: "/search/select-users",
		data: { "keyword": keyword },
		dataType: "text",
		async: false,
		success: function (data) {
			selected_users = JSON.parse(data);
			console.log(selected_users);
			replaceSelectResult();
		},
		error: function (xhr, status, error) {
			console.log(xhr);
			console.log(status);
			console.log(error);
		}
	});
}

function replaceSelectResult() {
	const follower_list = recommend_receivers.children;
	const checked_list = new Array();
	for(let i = 1; i < follower_list.length; i++) {
		const checkbox = follower_list[i].querySelector(".selected-users");
		if(checkbox.checked == false) {
			follower_list[i].remove();
			followers.splice(i, 1);
		} else {
			checked_list.push(follower_list[i].querySelector(".username").innerText);
		}
	}
	for(let i = 0; i < selected_users.length; i++) {
		let is_exist = false;
		for(let j = 0; j < checked_list.length; j++) {
			if(selected_users[i].username == checked_list[j]) {
				is_exist = true;
				break;
			}
		}
		if(is_exist) continue;
		followers.push(selected_users[i]);
		const row = document.createElement("div");
		row.className = "row";
		row.innerHTML = `<img src="/static/images/${selected_users[i].has_profile_image == true ? 'user_profile_images/' + selected_users[i].file_name : 'basic_profile_image.jpg'}">
					    				  <div class="user-info">
					    					  <span class="username">${selected_users[i].username}</span>
					    					  <span class="name">${selected_users[i].name}</span>
					    				  </div>
					    				  <input type="checkbox"  name="selected-users" class="selected-users">`;
		recommend_receivers.appendChild(row);
		row.querySelector(".selected-users").onclick = (event) => {
			toggleUserToTarget(event, i);	
		}
	}
}

function activeNewTargetModal() {
	clearRecommendRows();
	$.ajax({
		type: "get",
		url: "/follow/select-followers",
		data: { "count_indicator": 0},
		async: false,
		dataType: "text",
		success: function (data) {
			followers = JSON.parse(data).follower_list;
			console.log(followers);
		},
		error: function (xhr, status, error) {
			console.log(xhr);
			console.log(status);
			console.log(error);
		}
	});
	
	for(let i = 0; i < followers.length; i++) {
		const row = document.createElement("div");
		row.className = "row";
		row.innerHTML = `<img src="/static/images/${followers[i].has_profile_image == true ? 'user_profile_images/' + followers[i].file_name : 'basic_profile_image.jpg'}">
					    				  <div class="user-info">
					    					  <span class="username">${followers[i].username}</span>
					    					  <span class="name">${followers[i].name}</span>
					    				  </div>
					    				  <input type="checkbox"  name="selected-users" class="selected-users">`;
		recommend_receivers.appendChild(row);
		row.querySelector(".selected-users").onclick = (event) => {
			toggleUserToTarget(event, i);	
		}
	}
	
	new_target_modal.classList.add("active");
}

function clearRecommendRows() {
	const rows = recommend_receivers.children;
	for(let i = 0; i < rows.length; i++) {
		if(rows[i].className == "row") rows[i].remove();
	}
}

function toggleUserToTarget(event, index) {
	if(event.target.checked == false) {
		receivers.children[index].remove();
		target_users.splice(index, 1);
	} else {
		const div = document.createElement("div");
		div.innerHTML = `${followers[index].username}<button type='button'><img src="/static/images/recommend_receiver_remove_user.png"></button>`;
		target_users.push(followers[index]);
		receivers.insertBefore(div, receiver);
		
		div.querySelector("button").onclick = deleteCheck;
	}
	activeNextButton();
}

function deleteCheck(event) {
	const target_username = event.target.parentElement.innerText;
	const rows = recommend_receivers.querySelectorAll(".row");
	for(let i = 0; i < rows.length; i++) {
		if(rows[i].querySelector(".username").innerText == target_username) {
			rows[i].querySelector("input").checked = false;
			target_users.splice(i, 1);
			break;
		}
	}
	event.target.parentElement.remove();
	activeNextButton();
}

function activeNextButton() {
	if(target_users.length > 0) {
		next_button.disabled = false;
	} else {
		next_button.disabled = true;
	}
}