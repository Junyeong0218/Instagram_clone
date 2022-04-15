<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">


<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Instagram</title>
	<link rel="stylesheet" href="/static/css/container.css">
	<link rel="stylesheet" href="/static/css/main/header.css">
	<link rel="stylesheet" href="/static/css/main/main.css">
	<script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>
</head>

<body>
	<div class="container">
		
		<jsp:include page="/WEB-INF/views/templetes/header.jsp" />
		
		<main>
			<div class="main-wrapper">
				<div class="article-area">
					<ul class="story-area">
						
					</ul>
					<div class="articles">
						<article>
							<div class="article-header">
								<div class="writer-image">
									<img src="/static/images/profile_image.png" alt="게시글 작성자 프로필 이미지">
								</div>
								<div class="writer-info">
									<a href="#" class="writer-username">killiling____.time</a> <span
										class="remark">Original Audio</span>
								</div>
								<button type="button" class="article-menu">
									<img src="/static/images/article_menu.png" alt="옵션 더 보기">
								</button>
							</div>
							<div class="pictures">
								<ul>
									<li class="picture"><img src="/static/images/article-image1.webp"
										alt="게시글 이미지"></li>
									<!-- 이미지 크기가 다른 여러 이미지 업로드시 위아래 맞춰야함 -->
									<!-- <li class="picture">
                                                                                <img src="images/main3.png"
                                                                                        alt="게시글 이미지">
                                                                        </li>
                                                                        <li class="picture">
                                                                                <img src="images/main2.png"
                                                                                        alt="게시글 이미지">
                                                                        </li> -->
								</ul>
								<div class="image-index">
									<span class="dot current-index"></span> <span class="dot"></span>
								</div>
							</div>
							<div class="article-description">
								<div class="article-icons">
									<div class="left-button">
										<button type="button">
											<span></span>
										</button>
										<button type="button">
											<span></span>
										</button>
										<button type="button">
											<span></span>
										</button>
									</div>
									<div class="right-button">
										<button type="button">
											<span></span>
										</button>
									</div>
								</div>
								<div class="who-likes">
									<div class="represent-image">
										<img src="/static/images/profile_image.png" alt="좋아요 대표자 프로필 이미지">
									</div>
									<div class="represent-message">
										<span class="represent-comment"> <span
											class="represent-username">xxx</span>님 <span
											class="how-many-likes">외 20명</span>이 좋아합니다.
										</span>
									</div>
								</div>
								<div class="article-texts">
									<span class="description-username">jy960218</span> <span
										class="description-text">asdadlkadflkvfdhlvahd;lkfjv;ajdslvkja;lskdvnlkanvkjanlkvnlakdv</span>
								</div>
								<div class="upload-time-wrapper">
									<span class="upload-time">6시간 전</span>
								</div>
								<form class="write-comment">
									<div class="emoji-wrapper">
										<img src="/static/images/emoji_icon.png" alt="">
									</div>
									<textarea name="" placeholder="댓글 달기..." autocomplete="off"
										autocorrect="off"></textarea>
									<button type="button" disabled class="comment-submit-button">게시</button>
								</form>
							</div>
						</article>
					</div>
				</div>
				
				<jsp:include page="/WEB-INF/views/templetes/main_aside.jsp" />
				
			</div>
		</main>
	</div>
	<div class="pop-up">
		<div class="article-control">
			<button class="article-control-button">
				<span class="red-text">신고</span>
			</button>
			<button class="article-control-button">
				<span class="red-text">팔로우 취소</span>
			</button>
			<button class="article-control-button">
				<span>게시물로 이동</span>
			</button>
			<button class="article-control-button">
				<span>공유 대상...</span>
			</button>
			<button class="article-control-button">
				<span>링크 복사</span>
			</button>
			<button class="article-control-button">
				<span>퍼가기</span>
			</button>
			<button class="article-control-button">
				<span>취소</span>
			</button>
		</div>
	</div>
	<script src="/static/js/main/searcher_control.js"></script>
	<script src="/static/js/main/profile_menu_control.js"></script>
	<script src="/static/js/main/pop_up_and_window.js"></script>
	<script src="/static/js/main/load_aside_recommend.js"></script>
	<script src="/static/js/main/load_recent_stories.js"></script>
	<script src="/static/js/main/main.js"></script>
</body>

</html>