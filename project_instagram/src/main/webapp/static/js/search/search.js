let hashtag = location.pathname.replace("/search/", "");
const articles = document.querySelector(".articles");

loadArticlesAboutHashTag();

function loadArticlesAboutHashTag() {
	console.log(hashtag);
	$.ajax({
		type: "get",
		url: "/search/hashtag/" + hashtag,
		headers: { "Autorization": token },
		dataType: "text",
		success: function (data) {
			console.log(data);
			data = JSON.parse(data);
			console.log(data);
			if(data.articles.length > 0) {
				setSummary(data);
			} else {
				alert("존재하지 않는 해시태그입니다.\n메인화면으로 돌아갑니다.");
				location.href =  "/main";
			}
		},
		error: function (xhr, status) {
			console.log(xhr);
			console.log(status);
		}
	});
}

function setSummary(data) {
	const main_image = document.querySelector(".profile-image");
	const tag_name = document.querySelector(".tag-name");
	const article_count = document.querySelector(".article-count");
	main_image.src = "/static/file_upload" + data.articles[0].media_name;
	tag_name.innerText = `#${hashtag}`;
	article_count.innerText = data.related_article_count;
	appendArticles(data.articles);
}

function appendArticles(article_list) {
	const contents = document.createElement("div");
	contents.className = "contents";
	for(let i = 0; i < Math.ceil(article_list.length / 3); i++) {
		// make new row
		const row = document.createElement("div");
		row.className = "row";
		for(let j = i * 3; j < (i + 1) * 3; j++) {
			const article_data = article_list[j];
			if(article_data == null || typeof article_data == "undefined") {
				// empty a tag
				const empty = document.createElement("a");
				empty.href = "#";
				row.appendChild(empty);
			} else{
				const article = document.createElement("a");
				article.href = "#";
				if(article_data.media_type == "image") {
					article.innerHTML = `<img src="/static/file_upload${article_data.media_name}" alt="">
															<div class="summary">
																<div class="like-info">
																	<img src="/static/images/profile_article_summary_like_icon.png" alt="">
																	<span class="like-count">${article_data.like_count}</span>
																</div>
																<div class="comment-info">
																	<img src="/static/images/profile_article_summary_comment_icon.png" alt="">
																	<span class="comment-count">${article_data.comment_count}</span>
																</div>
															</div>`;
				} else {
					article.innerHTML = `<img src="/static/file_upload${article_data.media_name}" alt="">`;
				}
				row.appendChild(article);
			}
		}
		contents.appendChild(row);
	}
	articles.appendChild(contents);
}