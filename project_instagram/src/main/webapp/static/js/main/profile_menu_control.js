const nav_profile_button = document.querySelector(".profile > button");
const profile_menu_wrapper = document.querySelector(".profile-menu-wrapper");

nav_profile_button.onclick = toggleProfileMenu;

function toggleProfileMenu(event) {
    if (event.target.className == "nonclicked-nav-profile") {
        const home_button_image = document.querySelector(".home img");
        const menu_box = makeProfileFloating();
        event.target.className = "clicked-nav-profile";
        home_button_image.src = "/static/images/menu_home_clicked.png";
        profile_menu_wrapper.appendChild(menu_box);
        const logout_button = menu_box.querySelectorAll(".profile-menu-button")[1];
        logout_button.onclick = toLogout;
    } else {
        const home_button_image = document.querySelector(".home img");
        const menu_box = document.querySelector(".profile-menu-box");
        event.target.className = "nonclicked-nav-profile";
        home_button_image.src = "/static/images/menu_home.png";
        menu_box.remove();
    }
}

function toLogout() {
    location.href = "/auth/logout";
}

function makeProfileFloating() {
    const menu_box = document.createElement("div");
    menu_box.className = "profile-menu-box";
    menu_box.innerHTML = `<div class="profile-menu-arrow"></div>
                                                <div class="profile-menu">
                                                        <a href="/profile/${principal.username}" class="profile-menu-link">
                                                                <span class="profile-menu-icon"></span>
                                                                <span class="profile-menu-text">프로필</span>
                                                        </a>
                                                        <a href="#" class="profile-menu-link">
                                                                <span class="profile-menu-icon"></span>
                                                                <span class="profile-menu-text">저장됨</span>
                                                        </a>
                                                        <a href="/userinfo" class="profile-menu-link">
                                                                <span class="profile-menu-icon"></span>
                                                                <span class="profile-menu-text">설정</span>
                                                        </a>
                                                        <button type="button" class="profile-menu-button">
                                                                <span class="profile-menu-icon"></span>
                                                                <span class="profile-menu-text">계정 전환</span>
                                                        </button>
                                                        <hr class="profile-menu-separator">
                                                        <button type="button" class="profile-menu-button">
                                                                <span class="profile-menu-text">로그아웃</span>
                                                        </button>
                                                </div>`;
    return menu_box;
}