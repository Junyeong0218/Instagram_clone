const pop_up = document.querySelector(".pop-up");
const account_menu_button = document.querySelector(".account-menu-button");
const buttons = pop_up.querySelectorAll("button");

account_menu_button.onclick = showPopup;
// pop_up.onclick = closePopup;

for (let i = 0; i < buttons.length; i++) {
    buttons[i].onclick = executeSeparateEvent;
}

function showPopup() {
    pop_up.classList.remove("to-hidden");
    pop_up.classList.add("to-show");
}

function closePopup(event) {
    if (event.target == pop_up || event.target == close_button) {
        pop_up.classList.remove("to-show");
        pop_up.classList.add("to-hidden");
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
        case "pop-up-closer":
            pop_up.classList.remove("to-show");
            pop_up.classList.add("to-hidden");
            break;
    }
}