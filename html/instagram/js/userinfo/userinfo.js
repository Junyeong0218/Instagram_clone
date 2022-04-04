const userinfo_menus = document.querySelector(".userinfo-menus").children;
const to_userinfo_button = document.querySelector(".to-userinfo");
const to_password_button = document.querySelector(".to-password");
const userinfo_form = document.querySelector(".userinfo-form");
const password_form = document.querySelector(".password-form");

for (let i = 0; i < userinfo_menus.length; i++) {
        userinfo_menus[i].onclick = changeForm;
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