<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Instagram</title>
    <link rel="shortcut icon" href="/static/images/shortcut.ico" type="image/x-icon">
    <link rel="stylesheet" href="/static/css/container.css">
    <link rel="stylesheet" href="/static/css/main/header.css">
    <link rel="stylesheet" href="/static/css/short_footer.css">
    <link rel="stylesheet" href="/static/css/search/search.css">
    <script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>
    <script type="text/javascript" src="/static/js/principal.js"></script>
</head>

<body>

    <div class="container">

        <jsp:include page="/WEB-INF/views/templates/header.jsp" />

        <main>
            <div class="main-wrapper">
                <div class="tag-summary">
                    <img src="/static/images/profile_background.png" class="profile-background" alt="">
                    <img src="" class="profile-image" alt="유저 프로필 이미지">
                    
                    <div class="tag-info">
                    	<div class="tag-description">
                            <span class="tag-name"></span>
                            <span class="article-count-message">게시물 <span class="article-count">}</span></span>
                    	</div>
                    	<button type="button" class="follow-button">팔로우</button>
                    </div>
                </div>
                
                <div class="title">최근 사진</div>
                
                <div class="contents-wrapper">
                    <div class="articles">
                    	
                    </div>
                    
                </div>
            </div>
        </main>

        <jsp:include page="/WEB-INF/views/templates/short_footer.jsp" />

    </div>
   	<script src="/static/js/main/searcher_control.js"></script>
	<script src="/static/js/main/profile_menu_control.js"></script>
    <script src="/static/js/main/activity_menu_control.js"></script>
    <script src="/static/js/main/new_file_picker.js"></script>
    <script src="/static/js/main/new_file_picker.js"></script>
   	<script src="/static/js/search/search.js"></script>
</body>

</html>