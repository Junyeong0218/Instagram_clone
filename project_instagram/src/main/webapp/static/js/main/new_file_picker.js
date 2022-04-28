const show_new_article = document.querySelector(".show-new-article");
const show_new_article_modal = document.querySelector(".new-article-modal");
const new_article_closer = document.querySelector(".new-article-closer");
const non_picked_image = document.querySelector(".non-picked-image");
const picked_image = document.querySelector(".picked-image");
const select_image_button = document.querySelector(".select-image-button");
const image_picker = document.querySelector(".image-picker");
const add_new_media_button = document.querySelector(".add-new-media");

const file_tag = document.querySelector("#new-file");
let files = new Array();
let last_file_index = 0;

//----------------------------------------------------------------------
// EventListeners

show_new_article.onclick = () => show_new_article_modal.classList.add("active");
new_article_closer.onclick = () => {
	show_new_article_modal.classList.remove("active");
	const medias = picked_image.children;
	for(let i=0; i < medias.length; i++) {
		if(medias[i].tagName == "IMG" || medias[i].tagName == "VIDEO") {
			medias[i].remove();
			i--;
		}
	}
	picked_image.classList.add("disabled");
	non_picked_image.classList.remove("disabled");
	last_file_index = 0;
	files = new Array();
}

select_image_button.onclick = (event) => {
	image_picker.classList.toggle("active");
}

file_tag.onchange = (event) => {
	console.log(event);
	const file = file_tag.files[last_file_index++];
	const fileReader = new FileReader();
	fileReader.readAsDataURL(file);
	files.push(file);
	
	fileReader.onloadend = (event) => {
		console.log(event);
		if(file.type.split("/")[0] == "image") {
			const img = document.createElement("img");
			img.src = event.target.result;
			picked_image.insertBefore(img, select_image_button);
			image_picker.insertBefore(img, );
		} else {
			const video = document.createElement("video");
			video.src = event.target.result;
			video.autoplay = "autoplay";
			picked_image.insertBefore(video, select_image_button);
		}
		non_picked_image.classList.add("disabled");
		picked_image.classList.remove("disabled");
	}
}