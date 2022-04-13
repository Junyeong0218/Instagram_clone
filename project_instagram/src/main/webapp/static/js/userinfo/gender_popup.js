const pop_up = document.querySelector(".pop-up");
const close_button = document.querySelector(".popup-header > button");
const select_button = document.querySelector(".gender-control > button");
const gender_input = document.querySelector("input[name='gender']");

gender_input.onclick = showGenderPopup;
pop_up.onclick = closePopup;
select_button.onclick = selectGender;

function showGenderPopup(event) {
        pop_up.classList.remove("to-hidden");
        pop_up.classList.add("to-show");
}

function closePopup(event) {
        if (event.target == pop_up || event.target == close_button) {
                pop_up.classList.remove("to-show");
                pop_up.classList.add("to-hidden");
        }
}

function selectGender(event) {
        const gender = pop_up.querySelector("input:checked").value;
        const gender_input = userinfo_form.querySelector("input[name='gender']");
        gender_input.value = gender;
        pop_up.classList.remove("to-show");
        pop_up.classList.add("to-hidden");
}