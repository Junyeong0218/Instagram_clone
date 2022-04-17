const tabs = document.querySelectorAll(".tab-buttons > button");
const contents = document.querySelectorAll(".contents-wrapper > div");

for (let i = 0; i < tabs.length; i++) {
    tabs[i].onclick = activeTab;
}

function activeTab(event) {
    for (let i = 0; i < tabs.length; i++) {
        if (event.target == tabs[i]) {
            event.target.classList.add("active");
            contents[i].classList.add("active");
        } else {
            tabs[i].classList.remove("active");
            contents[i].classList.remove("active");
        }
    }
}