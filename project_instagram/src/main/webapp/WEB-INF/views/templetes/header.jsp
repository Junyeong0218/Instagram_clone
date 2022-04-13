<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<header>
	<div class="header-wrapper">
		<div class="logo">
			<a href="/main"><img src="/static/images/main_logo.png"
				alt="로고"></a>
		</div>
		<div class="searcher">
			<input type="text" placeholder="검색">
			<div class="searcher-placeholder">
				<div class="placeholder-wrapper">
					<span class="placeholder-image"></span> <span
						class="placeholder-text">검색</span>
				</div>
			</div>
			<div class="result-wrapper"></div>
		</div>
		<nav>
			<div class="nav-wrapper">
				<div class="home">
					<a href=""><img src="/static/images/menu_home.png" alt="홈"></a>
				</div>
				<div class="direct-message">
					<a href="#"><img src="/static/images/menu_message.png"
						alt="다이렉트 메세지"></a>
				</div>
				<div class="new-article">
					<a href="#"><img src="/static/images/menu_upload.png" alt="게시글 업로드"></a>
				</div>
				<div class="explore">
					<a href="#"><img src="/static/images/menu_recommends.png"
						alt="추천 게시글"></a>
				</div>
				<div class="activity">
					<a href="#"><img src="/static/images/menu_activity.png" alt="활동 내역"></a>
				</div>
				<div class="profile">
					<button type="button" class="nonclicked-nav-profile">
					<c:if test="${sessionScope.user.has_profile_image == true}">
						<img src="/static/images/${sessionScope.user.username}/${sessionScope.user.username}.png" alt="프로필">					
					</c:if>
					<c:if test="${sessionScope.user.has_profile_image == false}">
						<img src="/static/images/basic_profile_image.jpg" alt="프로필">
					</c:if>
					</button>
					<div class="profile-menu-wrapper"></div>
				</div>
			</div>
		</nav>
	</div>
</header>