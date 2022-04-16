const pop_up = document.querySelector(".pop-up");
const aside = document.querySelector("aside");

window.onresize = resizeAsideLeft;

function resizeAsideLeft(event) {
        aside.style.left = (642 + (event.currentTarget.innerWidth - 952) / 2) + 'px';
}