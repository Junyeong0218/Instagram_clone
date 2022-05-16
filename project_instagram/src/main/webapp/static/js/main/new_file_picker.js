const show_new_article = document.querySelector(".show-new-article");
const new_article_modal = document.querySelector(".new-article-modal");
const new_article_wrapper = document.querySelector(".new-article-wrapper");
const new_article_closer = document.querySelector(".new-article-closer");
const non_picked_image = document.querySelector(".non-picked-image");
const picked_image = document.querySelector(".picked-image");
const select_image_button = document.querySelector(".select-image-button");
const image_picker = document.querySelector(".image-picker");
const small_wrapper = document.querySelector(".small-wrapper");
const liner = document.querySelector(".liner");
const add_new_media_button = document.querySelector(".add-new-media");
const prev_form_button = document.querySelector(".prev-form");
const next_form_button = document.querySelector(".next-form");
const submit_article_button = document.querySelector(".submit-article");
const contents_form = document.querySelector(".contents-form");
const new_article_contents = document.querySelector(".article-contents");
const current_content_length = document.querySelector(".current-length");
const feature_input = document.querySelector("input[name='feature']");

const prev_button = new_article_modal.querySelector(".prev-button");
const next_button = new_article_modal.querySelector(".next-button");

const file_tag = document.querySelector("#new-file");
let files = new Array();
let last_file_index = 0;

//----------------------------------------------------------------------
// EventListeners

show_new_article.onclick = () => {
	new_article_modal.classList.add("active");
	document.body.style = "overflow: hidden;";
	show_new_article.querySelector("img").src = "/static/images/menu_upload_clicked.png";
}
new_article_closer.onclick = () => {
	new_article_modal.classList.remove("active");
	const medias = picked_image.children;
	for(let i=0; i < medias.length; i++) {
		if(medias[i].tagName == "IMG" || medias[i].tagName == "VIDEO") {
			medias[i].remove();
			i--;
		}
	}
	picked_image.classList.add("disabled");
	non_picked_image.classList.remove("disabled");
	document.body.style = "";
	show_new_article.querySelector("img").src = "/static/images/menu_upload.png";
	last_file_index = 0;
	files = new Array();
}

select_image_button.onclick = () => image_picker.classList.toggle("active");

add_new_media_button.onclick = () => file_tag.click();

file_tag.onchange = () => {
	const file = file_tag.files[0];
	const fileReader = new FileReader();
	fileReader.readAsDataURL(file);
	
	fileReader.onloadend = (event) => {
		if(file.size < 1024) {
			alert("1KB 이상의 파일만 등록할 수 있습니다.");
			return;
		}
		files.push(file);
		const div = document.createElement("div");
		const div_for_small = document.createElement("div");
		const src = event.target.result;
		const remove_button = makeRemoveImageButtonTag();
		if(file.type.split("/")[0] == "image") {
			const img = document.createElement("img");
			img.src = src;
			div.appendChild(img)
			picked_image.insertBefore(div, select_image_button);
			
			const small_img = document.createElement("img");
			small_img.src = src;
			div_for_small.appendChild(small_img);
			div_for_small.appendChild(remove_button);
			small_wrapper.appendChild(div_for_small);
		} else {
			const video = document.createElement("video");
			video.src = src;
			video.autoplay = "autoplay";
			div.appendChild(video);
			picked_image.insertBefore(div, select_image_button);
			
			const small_video = document.createElement("video");
			small_video.src = src + "#t=0.5";
			small_video.preload = "metadata";
			div_for_small.appendChild(small_video);
			div_for_small.appendChild(remove_button);
			small_wrapper.appendChild(div_for_small);
		}
		non_picked_image.classList.add("disabled");
		picked_image.classList.remove("disabled");
		next_form_button.classList.add("active");
		const index = files.length - 1;
		resizeMedia(div);
		resizeMedia(div_for_small);
		activeMedia(index);
		div_for_small.onclick = activeSpecificIndexMedia;
		remove_button.onclick = removeImage;
		if(files.length > 5) {
			if(files.length > 9) {
				add_new_media_button.classList.add("disabled");
			}
			next_button.classList.add("active");
		}
		console.log(files);
	}
}

next_button.onclick = () => {
	const move_x = files.length * 106 - 596;
	small_wrapper.scrollTo(move_x, 0);
	next_button.classList.remove("active");
	prev_button.classList.add("active");
}

prev_button.onclick = () => {
	const move_x = 596 - files.length * 106;
	small_wrapper.scrollTo(move_x, 0);
	next_button.classList.add("active");
	prev_button.classList.remove("active");
}

prev_form_button.onclick = () => {
	select_image_button.classList.remove("disabled");
	new_article_wrapper.classList.remove("text-form");
	contents_form.classList.remove("active");
	submit_article_button.classList.remove("active");
	prev_form_button.classList.remove("active");
	next_form_button.classList.add("active");
}

next_form_button.onclick = () => {
	select_image_button.classList.add("disabled");
	new_article_wrapper.classList.add("text-form");
	contents_form.classList.add("active");
	submit_article_button.classList.add("active");
	prev_form_button.classList.add("active");
	next_form_button.classList.remove("active");
	image_picker.classList.remove("active");
}

new_article_contents.oninput = (event) => {
	if(event.target.value.length > 2200) {
		alert("2,200자 이상 입력할 수 없습니다.");
		event.target.value = event.target.value.substring(0, 2200);
		current_content_length.innerText = event.target.value.length;
	} else {
		current_content_length.innerText = event.target.value.length;
	}
}

submit_article_button.onclick = (event) => {
	const formdata = makeFormData();
	console.log(formdata);
	$.ajax({
		type: "post",
		url: "/article",
		data: formdata,
		encType: "multipart/form-data",
		processData: false,
		contentType: false,
		dataType: "text",
		success: function (data) {
			if(data == "true") {
				location.reload();
			} else {
				alert("게시글 업로드 실패");
			}
		},
		error: function (xhr, status, error) {
			console.log(xhr);
			console.log(status);
			console.log(error);
		}
	});
}

//----------------------------------------------------------------------
// Functions

function makeFormData() {
	changeImageName();
	const formdata = new FormData();
	for(let i = 0; i < files.length; i++) {
		formdata.append("file", files[i]);
		formdata.append("type", files[i].type.split("/")[0]);
	}
	formdata.append("contents", new_article_contents.value);
	formdata.append("feature", feature_input.value);
	
	return formdata;
}

function changeImageName() {
	const dataTransfer = new DataTransfer();
	for(let i = 0; i < files.length; i++) {
		const numbering = String(i+1).padStart(2, "0");
		const type = files[i].type;
		const extension = "." + type.split("/")[1];
		const new_name = "media" + numbering + extension;
		const blob = files[i].slice(0, files[i].size, type);
		
		const new_file = new File([blob], new_name, { "type": type });
		dataTransfer.items.add(new_file);
	}
	files = dataTransfer.files;
}

function getCurrentImageIndex(element) {
	const images = small_wrapper.children;
	for(let i=0; i < images.length; i++) {
		if(images[i] == element) return i;
	}
}

function alignImages() {
	const images = small_wrapper.children;
	for(let i=0; i < images.length; i++) {
		if(i == 0) images[i].style = `transform: none`;
		else			 images[i].style = `transform: translate3d(${i * 106}px, 0px, 0px);`;
	}
	liner.style = `width: ${images.length * 106 > 595 ? 596 : images.length * 106}px`;
}

function showNonPickedImage() {
	non_picked_image.classList.remove("disabled");
	picked_image.classList.add("disabled");
	next_form_button.classList.remove("active");
}

function removeImage(event) {
	event.preventDefault();
	let remove_index = getCurrentImageIndex(event.target.parentElement);
	files.splice(remove_index, 1);
	small_wrapper.children[remove_index].remove();
	picked_image.children[remove_index].remove();
	alignImages();
	
	const image = small_wrapper.children[remove_index];
	let active_index;
	if(image == null || typeof image == "undefined") active_index = remove_index - 1;
	else																					   active_index = remove_index;
	
	if(active_index < 0) {
		showNonPickedImage();
	} else {
		activeMedia(active_index);
	}
	
	if(files.length < 6) {
		prev_button.classList.remove("active");
		next_button.classList.remove("active");
	}
	
	if(files.length < 10) {
		add_new_media_button.classList.remove("disabled");
	}
	console.log(files);
}

function makeRemoveImageButtonTag() {
	const button = document.createElement("button");
	button.type = "button";
	button.className = "remove-image-button";
	
	const img = document.createElement("img");
	img.src = "/static/images/new_article_remove_image.png";
	button.appendChild(img);
	
	return button;
}

function activeSpecificIndexMedia(event) {
	if(event.srcElement.tagName == "BUTTON") return;
	const index = getCurrentImageIndex(event.target);
	console.log("activeSpecificIndexMedia : " + index);
	activeMedia(index);
}

function resizeMedia(div) {
	const wrapper = div.parentNode;
	const media = div.children[0];
	const width = media.width;
	const height = media.height;
	if(width > height) {
		if(wrapper.className.includes("small-wrapper")) {
			const size = (files.length - 1) * 106;
			div.style = size == 0 ? `transform: none;` : `transform: translate3d(${size}px, 0px, 0px);`;
			liner.style = size + 106 > 595 ? `width: 596px` : `width: ${size + 106}px`;
		} else {
			div.style = "width: 100%;";
		}
		media.style = "max-height: 100%; position: relative; pointer-events:none;";
	} else {
		if(wrapper.className.includes("small-wrapper")) {
			const size = (files.length - 1) * 106;
			div.style = size == 0 ? `transform: none;` : `transform: translate3d(${size}px, 0px, 0px);`;
			liner.style = size + 106 > 595 ? `width: 596px` : `width: ${size + 106}px`;
		} else {
			div.style = "height: 100%;";
		}
		media.style = "max-width: 100%; position: relative; pointer-events:none;";
	}
}

function activeMedia(activeIndex) {
	console.log(activeIndex);
	const medias = picked_image.children;
	for(let i=0; i < medias.length; i++) {
		if(medias[i].tagName != "DIV" || medias[i].className.includes("image-picker")) continue;
		const media = medias[i].children[0];
		if(i == activeIndex) {
			medias[i].style.display = "";
			media.classList.remove("hidden");
		} else	{
			medias[i].style.display = "none";
			media.classList.add("hidden");
		}
	}
	const buttons = small_wrapper.children;
	for(let i=0; i < buttons.length; i++) {
		if(i == activeIndex) {
			buttons[i].classList.add("active");
		} else {
			buttons[i].classList.remove("active");
		}
	}
}