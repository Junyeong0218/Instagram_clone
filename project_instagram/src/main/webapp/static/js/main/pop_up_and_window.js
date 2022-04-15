const article_menu = document.querySelector(".article-menu");
const pop_up = document.querySelector(".pop-up");
const aside = document.querySelector("aside");

window.onresize = resizeAsideLeft;
article_menu.onclick = showArticleMenu;

function resizeAsideLeft(event) {
        aside.style.left = (642 + (event.currentTarget.innerWidth - 952) / 2) + 'px';
}

function showArticleMenu(event) {
        pop_up.classList.remove("to-hidden");
        pop_up.classList.add("to-show");

        const buttons = pop_up.querySelectorAll(".article-control-button");
        buttons[buttons.length - 1].onclick = () => {
                pop_up.classList.remove("to-show");
                pop_up.classList.add("to-hidden");
        }
}