const input_tags = document.querySelectorAll("input");
const submit_button = document.querySelector("button[type='submit']");

const username_regex = /^[a-z][A-Za-z0-9]{1,15}$/;
const phone_regex = /^01(0|1|6|7|8|9)[0-9]{3,4}[0-9]{4}$/;
const email_regex = /^[A-za-z0-9!@#$%^&*`~=+_]{3,}@[A-Za-z0-9]{2,}[\.](com|net|co\.kr|org)$/;
const name_regex = /^[A-Za-z가-힣]{2,20}$/;
const symbol_regex = /[!@#$%^&*`~=+_]{1,16}/;
const password_regex = /^[A-za-z0-9!@#$%^&*`~=+_]{8,16}$/;

let phone_or_email_check_flag = false;
let phone_flag = false;
let email_flag = false;
let username_check_flag = false;
let name_check_flag = false;
let password_check_flag = false;

for (let i = 0; i < input_tags.length; i++) {
        input_tags[i].oninput = keydownEvent;
        input_tags[i].onkeydown = keydownEvent;
        input_tags[i].onpaste = keydownEvent;
        input_tags[i].onkeyup = keyupEvent;
        input_tags[i].onfocus = initFlag;
        input_tags[i].onblur = checkRegex;
}

function initFlag(event) {
	const name = event.target.name;
	switch(name) {
		case "email":
			phone_or_email_check_flag = false;
			phone_flag = false;
			email_flag = false;
			break;
		case "name":
			name_check_flag = false;
			break;
		case "username":
			username_check_flag = false;
			break;
		case "password":
			password_check_flag = false;
			break;
	}
}

function checkRegex(event) {
	const message_tag = event.target.parentElement.nextElementSibling;
	switch(event.target.name) {
		case "email":
			const phone_result = event.target.value.match(phone_regex);
			const email_result = event.target.value.match(email_regex);
			console.log(phone_result);
			console.log(email_result);
			if(email_result != null && email_result[0] == email_result.input) {
				phone_or_email_check_flag = true;
				email_flag = true;
				if(checkInput(event.target)) {
					message_tag.innerText = "가입 가능한 이메일입니다.";
					message_tag.classList.remove("red");
					message_tag.classList.add("green");
				} else {
					message_tag.innerText = "중복된 이메일입니다.";
					message_tag.classList.add("red");
					message_tag.classList.remove("green");
				}
			} else if(phone_result != null && phone_result[0] == phone_result.input) {
				phone_or_email_check_flag = true;
				phone_flag = true;
				if(checkInput(event.target)) {
					message_tag.innerText = "가입 가능한 휴대폰 번호입니다.";
					message_tag.classList.remove("red");
					message_tag.classList.add("green");
				} else {
					message_tag.innerText = "중복된 휴대폰 번호입니다.";
					message_tag.classList.add("red");
					message_tag.classList.remove("green");
				}
			} else {
				phone_or_email_check_flag = false;
				message_tag.innerText = "정확히 입력해주세요.";
				message_tag.classList.add("red");
				message_tag.classList.remove("green");
			}
			break;
		case "name":
			const name_result = event.target.value.match(name_regex);
			if(name_result != null && name_result[0] == name_result.input) {
				name_check_flag = true;
				message_tag.innerText = "가입가능한 이름입니다.";
				message_tag.classList.remove("red");
				message_tag.classList.add("green");
				event.target.nextElementSibling.children[0].className = "right-input";
			} else {
				name_check_flag = false;
				message_tag.innerText = "정확히 입력해주세요.";
				message_tag.classList.add("red");
				message_tag.classList.remove("green");
				event.target.nextElementSibling.children[0].className = "wrong-input";
			}
			break;
		case "username":
			const username_result = event.target.value.match(username_regex);
			console.log(username_result);
			if(username_result != null && username_result[0] == username_result.input) {
				if(checkInput(event.target)) {
					username_check_flag = true;
					message_tag.innerText = "가입가능한 아이디입니다.";
					message_tag.classList.remove("red");
					message_tag.classList.add("green");
				} else {
					message_tag.innerText = "이미 가입된 아이디입니다.";
					message_tag.classList.add("red");
					message_tag.classList.remove("green");
				}
			} else {
				username_check_flag = false;
				message_tag.innerText = "정확히 입력해주세요.";
				message_tag.classList.add("red");
				message_tag.classList.remove("green");
			}
			break;
		case "password":
			const symbol_result = event.target.value.match(symbol_regex)
			const password_result = event.target.value.match(password_regex);
			if(symbol_result == null) {
				password_check_flag = false;
				message_tag.innerText = "!@#$%^&*`~=+_ 를 하나 이상 포함해야합니다.";
				message_tag.classList.add("red");
				message_tag.classList.remove("green");
				event.target.nextElementSibling.children[0].className = "wrong-input";
			} else if(password_result != null && password_result[0] == password_result.input) {
				password_check_flag = true;
				message_tag.innerText = "가입가능한 비밀번호입니다.";
				message_tag.classList.remove("red");
				message_tag.classList.add("green");
				event.target.nextElementSibling.children[0].className = "right-input";
			} else {
				password_check_flag = false;
				message_tag.innerText = "비밀번호는 8자 이상 입력해야합니다.";
				message_tag.classList.add("red");
				message_tag.classList.remove("green");
				event.target.nextElementSibling.children[0].className = "wrong-input";
			}
			break;
	}
	checkValue();
}

function checkInput(input) {
	let target_name = input.name == "email" ? phone_flag ? "phone" : "email" : input.name;
	let data = {};
	data[''+target_name] = input.value;
	console.log(data);
	let flag;
	$.ajax({
		type: "get",
		url: "/check-input",
		data: data,
		dataType: "text",
		async: false,
		success: function (data) {
			console.log(data);
			if(data == "0") {
                input.nextElementSibling.children[0].className = "right-input";
                flag =  true;
			} else {
				input.nextElementSibling.children[0].className = "wrong-input";
				flag =  false;
			}
		},
		error: function (xhr, status, error) {
			console.log(xhr);
			console.log(status);
			console.log(error);
		}
	});
	return flag;
}

function keyupEvent(event) {
        if (isLengthZero(event)) {
                event.target.className = "";
                event.target.previousElementSibling.className = "no-value-span";
                event.target.nextElementSibling.children[0].className = "wrong-input";
        }
}

function keydownEvent(event) {
        if (!isLengthZero(event) || event.target.value != null) {
                if (event.target.name != "password" && event.keyCode == 13) {
                        event.composedPath()[2].nextElementSibling.querySelector("input").focus();
                        return;
                }
                if (event.target.name == "password") {
                        const button = event.target.nextElementSibling.querySelector("button");
                        button.className = "toggle-password-button";
                        button.onclick = () => {
                                if (event.target.type == "password") {
                                        event.target.type = "text";
                                        button.innerText = "숨기기";
                                } else {
                                        event.target.type = "password";
                                        button.innerText = "비밀번호 표시";
                                }
                        }
                }
                if (!isFunctionKey(event)) {
                        event.target.className = "typed-input";
                        event.target.previousElementSibling.className = "typed-span";
                }
        }
}

function checkValue() {
        let isValid = phone_or_email_check_flag && username_check_flag && 
        					  name_check_flag && password_check_flag;
        console.log(isValid);
        if (isValid == true) {
                submit_button.disabled = false;
                submit_button.classList.add("active");
        } else {
                submit_button.disabled = true;
                submit_button.classList.remove("active");
        }
}

function isLengthZero(event) {
        return event.target.value.length == 0 ? true : false;
}

function isFunctionKey(event) {
        if (event.keyCode == 8 || event.keyCode == 9 || event.keyCode == 16 || event.keyCode == 17 || event.keyCode == 18 ||
                event.keyCode == 19 || event.keyCode == 20 || event.keyCode == 27 || event.keyCode == 33 || event.keyCode == 34 ||
                event.keyCode == 35 || event.keyCode == 36 || event.keyCode == 37 || event.keyCode == 38 || event.keyCode == 39 ||
                event.keyCode == 40 || event.keyCode == 45 || event.keyCode == 46 || event.keyCode == 182 || event.keyCode == 183 ||
                event.keyCode == 112 || event.keyCode == 113 || event.keyCode == 114 || event.keyCode == 115 || event.keyCode == 116 ||
                event.keyCode == 117 || event.keyCode == 118 || event.keyCode == 119 || event.keyCode == 120 || event.keyCode == 121 ||
                event.keyCode == 122 || event.keyCode == 123) {

                return true;
        } else {
                return false;
        }
}