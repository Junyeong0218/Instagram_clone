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
const message_description = document.querySelector(".message-description");
const target_user = document.querySelector(".target-user");
const user_list_tag = document.querySelector(".user-list");

let origin_room_data;
let followers;
let selected_users;
let target_users = new Array();

let modal_user_list;

let activated_room_data;
let activated_room_index;

// ------------------------------------------------------------------------------
// EventListeners

window.onload = () => {
	loadMessageData();
}

textarea.onkeypress = (event) => {
	if(event.keyCode == 13) {
		console.log("insert into direct_message_mst");
		insertNewMessage(event);
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

function insertNewMessage(event) {
	const room_id = activated_room_data[0].room_id;
	console.log(event.target.value);
	
	$.ajax({
		type: "post",
		url: "/direct/insert-direct-text-message",
		data: { "room_id": room_id, 
					  "contents": event.target.value },
		dataType: "text",
		success: function (data) {
			console.log(data);
			if(data == "true") {
				selectMessages();
			} else {
				alert("메시지 전송에 실패했습니다.");
			}
		},
		error: function (xhr, status, error) {
			console.log(xhr);
			console.log(status);
			console.log(error);
		}
	});
}

function loadMessageData() {
	$.ajax({
		type: "get",
		url: "/direct/select-rooms",
		dataType: "text",
		success: function (data) {
			origin_room_data = JSON.parse(data);
			console.log(origin_room_data);
			addRoomListTags(origin_room_data);
		},
		error: function (xhr, status, error) {
			console.log(xhr);
			console.log(status);
			console.log(error);
		}
	});
}

function addRoomListTags(data) {
	const session_user_id = data.user_id;
	const room_summary = data.room_summary;
	
	for(let i = 0; i < room_summary.length; i++) {
		const user_list = room_summary[i].entered_users;
		let represent_user_index;
		let names = "";
		for(let j = 0; j < user_list.length; j++) {
			if(user_list[j].user_id != session_user_id) {
				represent_user_index = j;
				names += user_list[j].name + ", ";
			}
		}
		names = names.substring(0, names.lastIndexOf(","));
		if(user_list.length == 2) names += "님";
		
		const message = room_summary[i].message.message_id != 0 ? room_summary[i].message.contents : "";
		const date = makeRecentMessageDate(room_summary[i].message.create_date);
		const button = document.createElement("button");
		button.type = "button";
		button.className = "user";
		button.innerHTML = `<img src="/static/images/${user_list[represent_user_index].has_profile_image == true ? 'user_profile_image/' + user_list[represent_user_index].file_name : 'basic_profile_image.jpg'}">
												<div class="user-description">
													<span class="name">${names}</span>
													<span class="recent-activity">${message}<span class="date">${date == "" ? "" : " · " + date}</span></span>
												</div>`;
		user_list_tag.appendChild(button);
		
		button.onclick = activeRoom;
	}
}

function findSelectedRoomIndex() {
	if(activated_room_data.length == 0) return -1;
	const room_id = activated_room_data[0].room_id;
	const room_list = origin_room_data.room_summary;
	for(let i = 0; i < room_list.length; i++) {
		if(room_list[i].room_id == room_id) {
			return i;
		}
	}
	return -1;
}

function addMessagesToRoom() {
	const room_index = findSelectedRoomIndex();
	const session_user_id = origin_room_data.user_id;
	const user_list = origin_room_data.room_summary[room_index].entered_users;
	let represent_user_index;
	let names = "";
	for(let j = 0; j < user_list.length; j++) {
		if(user_list[j].user_id != session_user_id) {
			represent_user_index = j;
			names += user_list[j].name + ", ";
		}
	}
	names = names.substring(0, names.lastIndexOf(","));
	if(user_list.length == 2) names += "님";
	target_user.querySelector(".name").innerText = names;
	target_user.querySelector("img").src = "/static/images/" + (user_list[represent_user_index].has_profile_image == "true" ? "user_profile_images/" + user_list[represent_user_index].file_name : "basic_profile_image.jpg");
	message_description.innerHTML = "";
	for(let i = activated_room_data.length - 1; i > -1; i--) {
		const message_info = activated_room_data[i];
		if(message_info.id == 0) continue;
		if(i == activated_room_data.length - 1) {
			const date_line = document.createElement("div");
			date_line.className = "line date";
			date_line.innerText = makeMessageSendedDate(message_info.create_date);
			message_description.appendChild(date_line);
		} else if(new Date(message_info.create_date) - new Date(activated_room_data[i + 1].create_date) > 1000 * 60 * 60 * 24) {
			const date_line = document.createElement("div");
			date_line.className = "line date";
			date_line.innerText = makeMessageSendedDate(message_info.create_date);
			message_description.appendChild(date_line);
		}
		
		const line = document.createElement("div");
		if(message_info.user_id != session_user_id) { 
			line.className =  "line receive";
			line.innerHTML = `<div class="user-profile-image">
											 	 <img src="/static/images/${message_info.has_profile_image == true ? 'user_profile_images/' + message_info.file_name : 'basic_profile_image.jpg'}">
											 </div>`;
		} else {
			line.className = "line send";
		}
		const message = document.createElement("div");
		message.className = "message";
		message.innerText = message_info.contents;
		line.appendChild(message);
		
		message_description.appendChild(line);
		
		console.log(message_info);
	}
	new_target_modal.classList.remove("active");
	main_content.classList.remove("active");
	message_wrapper.classList.add("active");
}

function makeMessageSendedDate(create_date) {
	if(create_date == "null") return "";
	const upload_time = new Date(create_date);
	console.log(upload_time);
	
	let year = upload_time.getFullYear();
	let month = upload_time.getMonth() + 1;
	let date = upload_time.getDate();
	let hour = upload_time.getHours();
	let am_pm = hour > 11 ? "오후" : "오전";
	hour = hour > 12 ? hour - 12 : hour;
	let minute = upload_time.getMinutes();
	
	return `${year}년 ${month}월 ${date}일 ${am_pm} ${hour}:${minute}`;
}

function activeRoom(event) {
	const room_list = user_list_tag.children;
	for(let i = 0; i < room_list.length; i++) {
		if(room_list[i] == event.target) {
			activated_room_index = i;
			room_list[i].classList.add("active");
		} else {
			room_list[i].classList.remove("active");
		}
	}
	selectMessages();
}

function selectMessages() {
	$.ajax({
		type: "get",
		url: "/direct/select-messages",
		data: { "room_id": origin_room_data.room_summary[activated_room_index].room_id },
		dataType: "text",
		success: function (data) {
			activated_room_data = JSON.parse(data);
			console.log(activated_room_data);
			addMessagesToRoom();
		},
		error: function (xhr, status, error) {
			console.log(xhr);
			console.log(status);
			console.log(error);
		}
	});
}

function makeRecentMessageDate(create_date) {
	if(create_date == "null") return "";
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
		return `${year}년`;
	} else if(month > 1) {
		return `${month}개월`;
	} else if(date > 6) {
		return `${date/7}주`;
;	} else if((date == 1 && upload_time.getDay() != now.getDay()) || date > 1) {
		return `${date}일`;
	} else if(hour > 0) {
		return `${hour}시간`;
	} else if(minute > 0) {
		return `${minute}분`;
	} else {
		return `${second}초`;
	}
}

function addToUserList() {
	const user_list = user_list_tag.children;
	let is_exist = false;
	const room_list = origin_room_data.room_summary;
	for(let i = 0; i < room_list.length; i++) {
		if(activated_room_data[0].room_id == room_list[i].room_id) {
			is_exist = true;
			break;
		}
	}
	if(! is_exist) {
		const users = origin_room_data[0].entered_users;
		let names = "";
		for(let i = 0; i < users.length; i++) {
			if(users[i].id != origin_room_data.user_id) {
				names += i == users.length - 1 ? users[i].name + ", " : users[i].name;
			}
		}
		if(users.length == 2) names += "님";
		const button = document.createElement("button");
		button.type = "button";
		button.className = "user active";
		button.innerHTML = `<img src="/static/images/${users[0].has_profile_image == true ? 'user_profile_image/' + userinfo[0].file_name : 'basic_profile_image.jpg'}">
												<div class="user-description">
													<span class="name">${names}</span>
													<span class="recent-activity"><span class="date"></span></span>
												</div>`;
		if(user_list.length == 0) user_list_tag.appendChild(button);
		else										 user_list_tag.insertBefore(button, user_list[0]);
		
		button.onclick = activeRoom;
		removeActiveRoomExcept(button);
	}
}

function removeActiveRoomExcept(button) {
	const user_list = user_list_tag.children;
	for(let i = 0; i < user_list.length; i++) {
		if(user_list[i] != button) user_list[i].classList.remove("active");
	}
	
}

function reloadOriginRoomData() {
	$.ajax({
		type: "get",
		url: "/direct/select-rooms",
		dataType: "text",
		success: function (data) {
			origin_room_data = JSON.parse(data);
		},
		error: function (xhr, status, error) {
			console.log(xhr);
			console.log(status);
			console.log(error);
		}
	});
}

function makeNewRoom() {
	if(target_users.length == 0) return;
	if(target_users.length == 1) {
		target_user.querySelector(".name").innerText = target_users[0].name + "님";
	} else {
		for(let i = 0; i < target_users.length; i++) {
			target_user.querySelector(".name").innerText += i == target_users.length - 1 ? target_users[i].name : target_users[i].name + ", ";
		}
	}
	insertNewRoomToDB();
	reloadOriginRoomData();
	addToUserList();
	addMessagesToRoom();
}

function insertNewRoomToDB() {
	let data = {};
	const target_user_id = new Array();
	for(let i = 0; i < target_users.length; i++) {
		target_user_id.push(target_users[i].id);
	}
	data["target_users"] = target_user_id;
	console.log(data);
	$.ajax({
		type: "post",
		url: "/direct/insert-new-room",
		data: data,
		dataType: "text",
		success: function (data) {
			activated_room_data = JSON.parse(data);
			console.log(data);
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
	const rows = recommend_receivers.querySelectorAll(".row");
	for(let i = 0; i < rows.length; i++) {
		if(rows[i].querySelector("input").checked == false) {
			rows[i].remove();
			modal_user_list.splice(i, 1);
		}
	}
	for(let i = 0; i < selected_users.length; i++) {
		let is_exist = false;
		for(let j = 0; j < target_users.length; j++) {
			if(selected_users[i].username == target_users[j].username) {
				is_exist = true;
				break;
			}
		}
		if(! is_exist) {
			modal_user_list.push(selected_users[i]);
			const row = document.createElement("div");
			row.className = "row";
			row.innerHTML = `<img src="/static/images/${selected_users[i].has_profile_image == true ? 'user_profile_images/' + selected_users[i].file_name : 'basic_profile_image.jpg'}">
						    				  <div class="user-info">
						    					  <span class="username">${selected_users[i].username}</span>
						    					  <span class="name">${selected_users[i].name}</span>
						    				  </div>
						    				  <input type="checkbox"  name="selected-users" class="selected-users">`;
			recommend_receivers.appendChild(row);
		}
	}
	addCheckEventToRows();
}

function addCheckEventToRows() {
	const rows = recommend_receivers.querySelectorAll(".row");
	for(let i = 0; i < rows.length; i++) {
		rows[i].querySelector(".selected-users").onclick = null;
		rows[i].querySelector(".selected-users").onclick = (event) => {
			toggleUserToTarget(event, i);
		};
	}
}

function toggleUserToTarget(event, index) {
	if(event.target.checked == false) {
		const tags = receivers.querySelectorAll("div");
		for(let i = 0; i < tags.length; i++) {
			if(tags[i].innerText == modal_user_list[index].username) {
				tags[i].remove();
				target_users.splice(i, 1);
			}
		}
	} else {
		const div = document.createElement("div");
		div.innerHTML = `${modal_user_list[index].username}<button type='button'><img src="/static/images/recommend_receiver_remove_user.png"></button>`;
		target_users.push(modal_user_list[index]);
		receivers.insertBefore(div, receiver);
		
		div.querySelector("button").onclick = deleteCheck;
	}
	activeNextButton();
	console.log(target_users);
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

function activeNewTargetModal() {
	initializeModal();
	$.ajax({
		type: "get",
		url: "/follow/select-followers",
		data: { "count_indicator": 0},
		async: false,
		dataType: "text",
		success: function (data) {
			followers = JSON.parse(data).follower_list;
			modal_user_list = followers;
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

function initializeModal() {
	clearRecommendRows();
	clearReceivers();
	initializeArrays();
	activeNextButton();
}

function clearRecommendRows() {
	const rows = recommend_receivers.querySelectorAll(".row");
	for(let i = 0; i < rows.length; i++) {
		rows[i].remove();
	}
}

function clearReceivers() {
	const divs = receivers.querySelectorAll("div");
	for(let i = 0; i < divs.length; i++) {
		divs[i].remove();
	}
}

function initializeArrays() {
	followers = null;
	selected_users = null;
	target_users = new Array();
}

function activeNextButton() {
	if(target_users.length > 0) {
		next_button.disabled = false;
	} else {
		next_button.disabled = true;
	}
}