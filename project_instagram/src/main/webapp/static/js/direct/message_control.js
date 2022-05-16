const textarea = document.querySelector("textarea[name='written-message']");
const new_message = document.querySelector(".new-message");
const main_new_message = document.querySelector(".main-new-message");
const new_target_modal = document.querySelector(".new-target-modal");
const modal_closer = new_target_modal.querySelector(".close-modal");
const receivers = new_target_modal.querySelector(".receivers");
const receiver = receivers.querySelector(".receiver");
const recommend_receivers = document.querySelector(".recommend-receivers");
const new_target_next_button = new_target_modal.querySelector(".next");
const main_content = document.querySelector(".main-content");
const message_wrapper = document.querySelector(".message-wrapper");
const message_description = document.querySelector(".message-description");
const target_user = document.querySelector(".target-user");
const user_list_tag = document.querySelector(".user-list");
const send_image_button = document.querySelector(".send-image");
const image_input = document.querySelector(".image-input");

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
	origin_room_data = loadMessageData();
	for(let i = 0; i < origin_room_data.room_summary.length; i++) {
		addRoomListTag(origin_room_data.room_summary[i]);
	}
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
new_target_next_button.onclick = makeNewRoom;

send_image_button.onclick = () => image_input.click();
image_input.onchange = () => {
	const file_reader = new FileReader();
	const file = image_input.files[0];
	
	file_reader.onloadend = () => {
		const formData = new FormData();
		formData.append("room_id", origin_room_data.room_summary[activated_room_index].room_id);
		formData.append("file", file);
		$.ajax({
			type: "post",
			url: "/direct/insert-direct-image-message",
			data: formData,
			encType: "multipart/form-data",
			processData: false,
			contentType: false,
			dataType: "text",
			success: function (data) {
				if(data == "true") {
					selectMessages();
				} else {
					alert("이미지 업로드 실패");
				}
			},
			error: function (xhr, status, error) {
				console.log(xhr);
				console.log(status);
				console.log(error);
			}
		});
	}
	
	file_reader.readAsDataURL(file);
}

// ------------------------------------------------------------------------------
// Functions

function insertNewMessage(event) {
	const room_id = activated_room_data[0].room_id;
	$.ajax({
		type: "post",
		url: "/direct/insert-direct-text-message",
		data: { "room_id": room_id, 
					  "contents": event.target.value },
		dataType: "text",
		success: function (data) {
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
	let room_data;
	$.ajax({
		type: "get",
		url: "/direct/select-rooms",
		async: false,
		dataType: "text",
		success: function (data) {
			room_data = JSON.parse(data);
		},
		error: function (xhr, status, error) {
			console.log(xhr);
			console.log(status);
			console.log(error);
		}
	});
	return room_data;
}

function reloadMessageListTags() {
	const room_data = loadMessageData();
	if(origin_room_data != null) {
		const room_summary = origin_room_data.room_summary;
		if(room_data.room_summary.length != room_summary.length) {
			const new_rooms = new Array();
			let is_exist = false;
			for(let i = 0; i < room_data.room_summary.length; i++) {
				for(let j = 0; j < room_summary.length; j++) {
					if(room_data.room_summary[i].room_id == room_summary.room_id) {
						origin_room_data.room_summary[j] = room_data.room_summary[i];
						is_exist = true;
						break;
					}
				}
				if(! is_exist) {
					new_rooms.push(i);
				}
				is_exist = false;
			}
			for(let i = 0; i < new_rooms.length; i++) {
				addRoomListTag(room_data.room_summary[new_rooms[i]]);
				origin_room_data.room_summary.push(room_data.room_summary[new_rooms[i]]);
			}
		}
		if(activated_room_index != null) {
			const current_room_id = room_summary[activated_room_index].room_id;
			for(let i = 0; i < room_summary.length; i++) {
				if(room_summary[i].room_id == current_room_id) {
					user_list_tag.children[i].click();
					break;
				}
			}
		}
		console.log(room_summary);
		console.log(room_data.room_summary);
		for(let i = 0; i < room_summary.length; i++) {
			for(let j  = 0; j < room_data.room_summary.length; j++) {
				if(room_summary[i].room_id == room_data.room_summary[j].room_id) {
					if(room_data.room_summary[j].message.contents == room_summary[i].message.contents) {
						break;
					} else {
						const contents = room_data.room_summary[j].message.contents == "null" ? "" : room_data.room_summary[j].message.contents.length > 14 ? room_data.room_summary[j].message.contents.substring(0, 15) + "..." : room_data.room_summary[j].message.contents;
						user_list_tag.children[i].querySelector(".recent-activity").innerHTML = contents != "" ? `${contents}<span class="date"> · ${makeRecentMessageDate(room_data.room_summary[j].message.create_date)}</span>` : "";
						user_list_tag.children[i].classList.add("non-read");
						origin_room_data.room_summary[i] = room_data.room_summary[j];
						break;
					}
				}
			}
		}
	}
}

function addRoomListTag(room_data) {
	const session_user_id = origin_room_data.user_id;
	
	const user_list = room_data.entered_users;
	const all_message_count = Number(room_data.all_message_count);
	const read_message_count = Number(room_data.read_message_count);
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
	
	const message = room_data.message.message_id == 0 ? "" : room_data.message.contents.length > 14 ? room_data.message.contents.substring(0, 15) + "..." : room_data.message.contents;
	const date = makeRecentMessageDate(room_data.message.create_date);
	const button = document.createElement("button");
	button.type = "button";
	button.className = all_message_count > read_message_count ? "user non-read" : "user";
	button.innerHTML = `<img src="/static/images/${user_list[represent_user_index].has_profile_image == true ? 'user_profile_image/' + user_list[represent_user_index].file_name : 'basic_profile_image.jpg'}">
											<div class="user-description">
												<span class="name">${names}</span>
												<span class="recent-activity">${message}<span class="date">${date == "" ? "" : " · " + date}</span></span>
											</div>
	${all_message_count > read_message_count ? "<div class='non-read-dot'></div>" : ""}`;
	user_list_tag.appendChild(button);
	
	button.onclick = activeRoom;
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
		console.log(message_info);
		if(message_info.id == 0) continue;
		if(i == 0) {
			const room_tag = user_list_tag.children[room_index];
			const last_message_date = makeRecentMessageDate(message_info.create_date);
			room_tag.querySelector(".recent-activity").innerHTML = `${message_info.contents}<span class="date"> · ${last_message_date}</span>`;
		}
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
			const entered_users = origin_room_data.room_summary[activated_room_index].entered_users;
			let sended_user_info;
			for(let j = 0; j < entered_users.length; j++) {
				if(entered_users[j].user_id == message_info.user_id) {
					sended_user_info = entered_users[j];
					break;
				}
			}
			line.className =  "line receive";
			line.innerHTML = `<div class="user-profile-image">
											 	 <img src="${sended_user_info.has_profile_image == "true" ? '/static/file_upload' + sended_user_info.file_name : '/static/images/basic_profile_image.jpg'}">
											 </div>`;
		} else {
			line.className = "line send";
		}
		const message = document.createElement("div");
		if(message_info.is_image == "true") {
			message.className = "image";
			message.innerHTML = `<img src="/static/file_upload${message_info.file_name}">`;
		} else {
			message.className = "message";
			message.innerText = message_info.contents;
		}
		
		line.appendChild(message);
		
		if(message_info.like_users.length > 0) {
			const like = document.createElement("div");
			like.className = "like";
			like.innerHTML = "<img src='/static/images/message_reaction.png'>";
			message.appendChild(like);
			message.classList.add("liked");
			if(line.className.includes("receive")) {
				message.previousElementSibling.classList.add("liked");
			}
		}
		
		message_description.appendChild(line);
		
		message.ondblclick= toggleMessageLike;
	}
	user_list_tag.children[room_index].classList.remove("non-read");
	const dot = user_list_tag.children[room_index].querySelector(".non-read-dot");
	if(dot != null) {
		dot.remove();
	}
	new_target_modal.classList.remove("active");
	main_content.classList.remove("active");
	message_wrapper.classList.add("active");
	message_description.classList.remove("flex-end");
	if(message_description.scrollHeight > 697) {
		message_description.scrollTop = message_description.scrollHeight;
	} else {
		message_description.classList.add("flex-end");
	}
}

function collectMessagesExceptDate() {
	const messages = message_description.children;
	const except_date = new Array();
	for(let i = 0; i < messages.length; i++) {
		if(messages[i].className.includes("date")) continue;
		except_date.push(messages[i]);
	}
	return except_date;
}

function toggleMessageLike(event) {
	const lines = collectMessagesExceptDate();
	let current_index = -1;
	for(let i = 0; i < lines.length; i++) {
		const message = lines[i].querySelector(".message");
		const image = lines[i].querySelector(".image");
		if(message != null) {
			if(message == event.target) {
				current_index = lines.length - i - 1;
				break;
			}
		} else if(image != null) {
			if(image == event.target) {
				current_index = lines.length - i - 1;
				break;
			}
		}
	}
	if(current_index == -1) return;
	const message_data = activated_room_data[current_index];
	$.ajax({
		type: "post",
		url: "/direct/toggle-message-reaction",
		data: { "message_id": message_data.id },
		dataType: "text",
		success: function (data) {
			data = JSON.parse(data);
			activated_room_data[current_index].like_users = data;
			const like_flag = data.length > 0 ? true : false;
			console.log(like_flag);
			console.log(lines[current_index].querySelector(".like"));
			if(like_flag && lines[current_index].querySelector(".like") == null) {
				console.log("create");
				const like = document.createElement("div");
				like.className = "like";
				like.innerHTML = `<img src="/static/images/message_reaction.png">`;
				event.target.appendChild(like);
				event.target.classList.add("liked");
				if(event.target.parentElement.className.includes("receive")) {
					event.target.previousElementSibling.classList.add("liked");
				}
			} else if(like_flag == false) {
				console.log("delete");
				const exist_like = event.target.querySelector(".like");
				if(exist_like != null) {
					exist_like.remove();
				}
				event.target.classList.remove("liked");
				if(event.target.parentElement.className.includes("receive")) {
					event.target.previousElementSibling.classList.remove("liked");
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
	let minute = String(upload_time.getMinutes()).padStart(2, "0");
	
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
		url: "/search/users/" + keyword,
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
		new_target_next_button.disabled = false;
	} else {
		new_target_next_button.disabled = true;
	}
}