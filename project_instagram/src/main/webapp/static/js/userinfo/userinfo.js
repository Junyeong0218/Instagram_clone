const userinfo_menus = document.querySelector(".userinfo-menus").children;
const to_userinfo_button = document.querySelector(".to-userinfo");
const to_password_button = document.querySelector(".to-password");
const userinfo_form = document.querySelector(".userinfo-form");
const password_form = document.querySelector(".password-form");
const profile_image_tag = document.querySelector(".userinfo-profile-image > img");
const profile_image_input = document.querySelector("#profile-image");
const userinfo_inputs = userinfo_form.querySelectorAll(".userinfo-input-wrapper input");
const password_inputs = password_form.querySelectorAll("input[type='password']");
const description = document.querySelector("textarea[name='description']");
const userinfo_submit_button = document.querySelector(".submit-button");
const password_submit_button = document.querySelector(".password-submit-button");
const origin_userinfo = {
	"name": userinfo_inputs[0].value,
	"username": userinfo_inputs[1].value,
	"website": userinfo_inputs[2].value,
	"description": description.value,
	"email": userinfo_inputs[3].value,
	"phone": userinfo_inputs[4].value,
	"gender": userinfo_inputs[5].value
};

let profile_change_flag = false;
let userinfo_change_flag = false;
let is_origin_password_equal = false;
let is_password_pair_equal = false;

// --------------------------------------------------
// eventListeners

window.onload = (event) => {
	focusFormWithQueryString();
	initUserData();
}

for(let i = 0; i < userinfo_inputs.length; i++) {
	userinfo_inputs[i].oninput = evaluateChange;	
}
description.oninput = evaluateChange;

for (let i = 0; i < userinfo_menus.length; i++) {
	userinfo_menus[i].onclick = changeForm;
}

profile_image_input.onchange = previewInputImage;
userinfo_submit_button.onclick = () => {
	if(profile_image_input.files.length > 0) changeImageFileName();
	const formData = {};
	for(let i = 0; i < userinfo_inputs.length; i++) {
		const input_name = userinfo_inputs[i].name;
		if(input_name == "file" || input_name == "recommend") continue;
		if(input_name == "gender") {
			formData["" + input_name] = userinfo_inputs[i].value == "남성" ? 0 :
																	userinfo_inputs[i].value == "여성" ? 1 :
																	userinfo_inputs[i].value == "맞춤 성별" ? 2 :
																	userinfo_inputs[i].value == "밝히고 싶지 않음" ? 3 : null;
		} else {
			formData["" + input_name] = userinfo_inputs[i].value;
		}
	}
	formData.description = description.value;
	console.log(formData);
	let data = new FormData(document.createElement("form"));
	data.append("formData", JSON.stringify(formData));
	if(profile_image_input.files[0] != null) {
		data.append("file", profile_image_input.files[0]);
	}
	
	$.ajax({
		type: "put",
		url: "/auth/userinfo",
		headers: { "Authorization": token},
		data: data,
		encType: "multipart/form-data",
		processData: false,
		contentType: false,
		dataType: "text",
		success: function (data) {
			console.log(data);
			if(data == "true") {
				location.reload();
			} else {
				alert("정보 변경에 실패했습니다.");
			}
		},
		error: function (xhr, status) {
			console.log(xhr);
			console.log(status);
			/*정보 변경이 정상적으로 이루어지지 않았습니다. 다시 시도해주세요.*/
		}
	});
}

password_inputs[0].onblur = checkOriginPassword;
password_inputs[1].onblur = checkPasswordEquality;
password_inputs[2].onblur = checkPasswordEquality;

password_submit_button.onclick = () => {
	/*const formData = new FormData();
	formData.append("password", password_inputs[1].value);*/
	console.log(password_inputs[1].value);
	$.ajax({
		type: "put",
		url: "/auth/password",
		headers: { "Authorization": token}, 
		data: JSON.stringify({ "password": password_inputs[1].value }),
		encType: "application/json; charset=UTF-8",
		dataType: "text",
		success: function (data) {
			console.log(data);
			if(data == "true") {
				alert("비밀번호 변경이 완료되었습니다!\n로그인 화면으로 이동합니다.");
				location.href = "/auth/logout";
			} else {
				alert("비밀번호 변경에 실패했습니다.");
			}
		},
		error: function (xhr, status) {
			console.log(xhr);
			console.log(status);
		}
	});
}

// --------------------------------------------------
// functions

function initUserData() {
	$.ajax({
		type: "get",
		url: "/auth/user/detail",
		headers: { "Authorization": token },
		dataType: "text",
		success: function (data) {
			data = JSON.parse(data);
			console.log(data);
			if(data.has_profile_image == "true") {
				console.log("has_profile_image == true");
				const userinfo_profile_images = document.querySelectorAll(".userinfo-profile-image");
				userinfo_profile_images.forEach(e => {
					e.querySelector("img").src = "/static/file_upload" + data.file_name;
				});
			}
			userinfo_form.querySelector(".username").innerText = data.username;
			password_form.querySelector(".username").innerText = data.username;
			userinfo_form.querySelector("input[name='username']").value = data.username;
			const last_username_update_date = new Date(data.last_username_update_date);
			const now = new Date();
			if(now - last_username_update_date < 1000 * 60 * 60 * 24 * 14) {
				userinfo_form.querySelector("input[name='username']").readOnly = true;
			} 
			userinfo_form.querySelector("input[name='name']").value = data.name;
			userinfo_form.querySelector("input[name='website']").value = data.website;
			userinfo_form.querySelector("textarea[name='description']").value = data.description;
			userinfo_form.querySelector("input[name='email']").value = data.email;
			userinfo_form.querySelector("input[name='phone']").value = data.phone;
			const gender_value = data.gender == "0" ? "남성" :
													 data.gender == "1" ? "여성" :
													 data.gender == "2" ? "맞춤 성별" :
													 data.gender == "3" ? "밝히고 싶지 않음" : "";
			userinfo_form.querySelector("input[name='gender']").value = gender_value;
		},
		error: function (xhr, status) {
			console.log(xhr);
			console.log(status);
		}
	});
}

function focusFormWithQueryString() {
	const uri = location.href;
	const array = uri.split("?");
	if(array.length > 1 && array[1].includes("change-password")) {
		userinfo_form.classList.add("hidden");
		password_form.classList.remove("hidden");
		to_userinfo_button.parentElement.classList.remove("selected-menu");
		to_password_button.parentElement.classList.add("selected-menu");
	}
}

function isEmpty(inputTag) {
	if(inputTag.value == null || inputTag.value == "" || typeof inputTag.value == "undefined") {
		return true;
	}
	return false;
}

function checkPasswordEquality() {
	if(isEmpty(password_inputs[1]) || isEmpty(password_inputs[2])) return;
	if(password_inputs[1].value == password_inputs[2].value) {
		is_password_pair_equal = true;
	} else {
		is_password_pair_equal = false;
	}
	activePasswordSubmitButton();
}

function checkOriginPassword(event) {
	$.ajax({
		type: "get",
		url: "/auth/password",
		headers: { "Authorization": token},
		data: { "password": event.target.value },
		dataType: "text",
		success: function (data) {
			if(data == "true") {
				alert("이전 비밀번호와 일치합니다.");
				is_origin_password_equal = true;
			} else {
				alert("이전 비밀번호와 다릅니다.");
				is_origin_password_equal = false;
			}
			activePasswordSubmitButton();
		},
		error: function (xhr, status, error) {
			console.log(xhr);
			console.log(status);
			console.log(error);
		}
	});
}

function activePasswordSubmitButton() {
	if(is_origin_password_equal && is_password_pair_equal) {
		password_submit_button.classList.remove("disabled");
		password_submit_button.disabled = false;
	} else {
		password_submit_button.classList.add("disabled");
		password_submit_button.disabled = true;
	}
}

function evaluateChange(event) {
	if(event.target.name == "recommend") return;
	switch(event.target.name) {
		case "name":
			userinfo_change_flag = event.target.value == origin_userinfo.name ? false : true;
			break;
		case "username": 
			userinfo_change_flag = event.target.value == origin_userinfo.username ? false : true;
			break;
		case "website": 
			userinfo_change_flag = event.target.value == origin_userinfo.website ? false : true;
			break;
		case "description": 
			userinfo_change_flag = event.target.value == origin_userinfo.description ? false : true;
			break;
		case "email": 
			userinfo_change_flag = event.target.value == origin_userinfo.email ? false : true;
			break;
		case "phone": 
			userinfo_change_flag = event.target.value == origin_userinfo.phone ? false : true;
			break;
		case "gender": 
			userinfo_change_flag = event.target.value == origin_userinfo.gender ? false : true;
			break;
	}
	activeSubmitButton();
}

function activeSubmitButton() {
	if(profile_change_flag || userinfo_change_flag) {
		userinfo_submit_button.classList.remove("disabled");
		userinfo_submit_button.disabled = false;
	} else {
		userinfo_submit_button.classList.add("disabled");
		userinfo_submit_button.disabled = true;
	}
}

function changeImageFileName() {
	const dataTransfer = new DataTransfer();
	const origin_file_name = profile_image_input.files[0].name;
	const extension = origin_file_name.substring(origin_file_name.lastIndexOf("."), origin_file_name.length);
	const type = profile_image_input.files[0].type;
	const blob = profile_image_input.files[0].slice(0, profile_image_input.files[0].size, type); 
	
	newFile = new File([blob], userinfo_inputs[1].value + extension, {"type": type});
	dataTransfer.items.add(newFile);
	profile_image_input.files = dataTransfer.files;
}

function previewInputImage(event) {
	const fileReader = new FileReader();
	fileReader.readAsDataURL(event.target.files[0]);
	fileReader.onloadend = (e) => {
		profile_image_tag.src = e.target.result;
	}
	profile_change_flag = true;
	activeSubmitButton();
}

function changeForm(event) {
        const button = event.target.querySelector("button");
        if (button.className == null || button.className == "") return;
        if (button.className == "to-userinfo") {
                changeMenuHighlight(event.target);
                userinfo_form.classList.remove("hidden");
                if (!password_form.className.includes("hidden")) {
                        password_form.classList.add("hidden");
                }
        } else if (button.className == "to-password") {
                changeMenuHighlight(event.target);
                password_form.classList.remove("hidden");
                if (!userinfo_form.className.includes("hidden")) {
                        userinfo_form.classList.add("hidden");
                }
        }
}

function changeMenuHighlight(element) {
    for (let i = 0; i < userinfo_menus.length; i++) {
        if (userinfo_menus[i] == element) {
			if (!element.className.includes("selected-menu"))
				element.classList.add("selected-menu");
		} else {
			userinfo_menus[i].classList.remove("selected-menu");
		}
	}
}