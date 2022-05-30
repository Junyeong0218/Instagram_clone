window.onload = (event) => {
	resizeAsideLeft(event);
	loadRecommendUsers();
	loadRecentStories();
	loadArticleList();
}

window.onscroll = detectScroll;