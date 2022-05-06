const textarea = document.querySelector("textarea[name='written-message']");
const new_message = document.querySelector(".new-message");
const main_new_message = document.querySelector(".main-new-message");
const new_target_modal = document.querySelector(".new-target-modal");
const modal_closer = new_target_modal.querySelector(".close-modal");
const receivers = new_target_modal.querySelector(".receivers");
const receiver = receivers.querySelector(".receiver");
const recommend_receivers = document.querySelector(".recommend-receivers");
const next_button = new_target_modal.querySelector(".next");

let followers;
let target_users = new Array();

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













// ------------------------------------------------------------------------------
// Functions

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
	if(event.target.checked == true) {
		event.target.checked = false;
		receivers.children[index].remove();
	} else {
		event.target.checked = true;
		const div = document.createElement("div");
		div.innerHTML = `${followers[index].username}<button type='button'><img src="/static/images/recommend_receiver_remove_user.png"></button>`;
		target_users.push(followers[index]);
		receivers.insertBefore(div, receiver);
	}
	activeNextButton();
}

function activeNextButton() {
	if(target_users.length > 0) {
		next_button.disabled = false;
	} else {
		next_button.disabled = "disabled";
	}
}