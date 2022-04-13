const input_tags = document.querySelectorAll("input");
const submit_button = document.querySelector("button[type='submit']");

for (let i = 0; i < input_tags.length; i++) {
        input_tags[i].oninput = keydownEvent;
        input_tags[i].onkeydown = keydownEvent;
        input_tags[i].onpaste = keydownEvent;
        input_tags[i].onkeyup = keyupEvent;
}

function checkUsername(event) {
	if(event.target.value.length < 7 || event.target.value.length > 16) {
        event.target.nextElementSibling.children[0].className = "wrong-input";
	} else {
		$.ajax({
			type: "get",
			url: "/check-username",
			data: { "username": event.target.value },
			dataType: "text",
			success: function (data) {
				console.log(data);
				if(data == "0") {
					event.target.className = "typed-input";
                    event.target.previousElementSibling.className = "typed-span";
                    event.target.nextElementSibling.children[0].className = "right-input";
				} else {
					event.target.nextElementSibling.children[0].className = "wrong-input";
				}
			},
			error: function (xhr, status, error) {
				console.log(xhr);
				console.log(status);
				console.log(error);
			}
		});
	}
}

function keyupEvent(event) {
        if (isLengthZero(event)) {
                event.target.className = "";
                event.target.previousElementSibling.className = "no-value-span";
                event.target.nextElementSibling.children[0].className = "wrong-input";
                checkValue();
        }
}

function keydownEvent(event) {
        if (!isLengthZero(event) || event.target.value != null) {
                if (event.target.name != "password" && event.keyCode == 13) {
                        event.path[2].nextElementSibling.querySelector("input").focus();
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
						if(event.target.name == "username") {
							checkUsername(event);
						} else {
	                        event.target.nextElementSibling.children[0].className = "right-input";
						}
                        checkValue();
                }
        }
}

function checkValue() {
        let isValid = true;
        for (let i = 0; i < input_tags.length; i++) {
                if (input_tags[i].value == null || input_tags[i].value == "") {
                        isValid = false;
                        break;
                }
        }
        if (isValid == true) {
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