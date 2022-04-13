const images = document.querySelectorAll(".inner-img");
let prev_img_index = 3;
let curr_img_index = 0;

changeImage();
setInterval(changeImage, 5000);

function changeImage() {
        for (let i = 0; i < images.length; i++) {
                if (i == curr_img_index) {
                        images[i].classList.add("active-image");
                } else if (i == prev_img_index) {
                        images[i].classList.remove("active-image");
                        images[i].classList.add("inactive-image");
                } else {
                        images[i].className = "inner-img";
                }
        }
        curr_img_index++;
        prev_img_index++;
        if (curr_img_index > 3) {
                curr_img_index = 0;
        }
        if (prev_img_index > 3) {
                prev_img_index = 0;
        }
}