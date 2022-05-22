const	form = document.querySelector("form");
const usernameTag = document.querySelector("input[name='username']");
const passwordTag = document.querySelector("input[name='password']");
const usernameSpan = document.querySelectorAll("form span")[0];
const passwordSpan = document.querySelectorAll("form span")[1];
const submit_button = document.querySelector(".submit-button");

const username_regex = /^[a-z][A-Za-z0-9]{1,15}$/;
const symbol_regex = /[!@#$%^&*`~=+_]{1,16}/;
const password_regex = /^[A-za-z0-9!@#$%^&*`~=+_]{8,16}$/;

console.log(window.navigator.plugins);

// ------------------------------------
usernameTag.onkeydown = keydownEvent;
usernameTag.oninput = keydownEvent;
usernameTag.onpaste = keydownEvent;
// ------------------------------------
passwordTag.onkeydown = keydownEvent;
passwordTag.oninput = keydownEvent;
passwordTag.onpaste = keydownEvent;
// ------------------------------------
usernameTag.onkeyup = keyupEvent;
passwordTag.onkeyup = keyupEvent;
// ------------------------------------
submit_button.onclick = () => {
	$.ajax({
		type: "post",
		url: "/auth/signin",
		data: { "plugins" : window.navigator.plugins.length,
					  "username": usernameTag.value,
					  "password": passwordTag.value },
		async: false,
		dataType: "text",
		success: function (xhr, status, data) {
			console.log(data);
			
			if(data == null || data == "") {
				alert("아이디 혹은 비밀번호를 확인해주세요.");
			} else {
				const token = data.getResponseHeader("Authorization");
				const request = new XMLHttpRequest();
				request.open("GET", "http://localhost:8080/verify/token/main", true);
				request.setRequestHeader("Authorization", token);
				
				request.send();
				request.onloadend = (event) => {
					console.log(event);
					if(request.response == "true") {
						location.replace("/main");
					}
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

function keyupEvent(event) {
    if (isLengthZero(event)) {
        event.target.className = "";
        event.target.parentElement.querySelector("span").className = "no-value-span";
        event.target.nextElementSibling.querySelector("span").className = "wrong-input";
        checkValue();
    }
}

function keydownEvent(event) {
    if (!isLengthZero(event) || event.target.value != null) {
        if (event.target.name == "username" && event.keyCode == 13) {
            passwordTag.focus();
            return;
        }
        if (!isFunctionKey(event)) {
            event.target.className = "typed-input";
            event.target.parentElement.querySelector("span").className = "typed-span";
            event.target.nextElementSibling.querySelector("span").className = "right-input";
            checkValue();
        }
    }
}

function checkValue() {
	const username_result = usernameTag.value.match(username_regex);
	const symbol_result = passwordTag.value.match(symbol_regex);
	const password_result = passwordTag.value.match(password_regex);
    if (username_result != null && username_result[0] == username_result.input &&
    	 symbol_result != null && password_result != null &&
    	 password_result[0] == password_result.input) {
        submit_button.disabled = false;
        submit_button.className = "active-button";
    } else {
        submit_button.disabled = true;
        submit_button.className = "";
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