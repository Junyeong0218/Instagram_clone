<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<head>
	<link rel="stylesheet" type="text/css" href="/static/css/update_article_form.css">
</head>
<div class="update-article-modal">
    <button class="update-article-closer" type="button">
    	<img src="/static/images/new_article_modal_closer.png" alt="">
    </button>
    <div class="update-article-wrapper">
        <div class="title">게시글 수정
        	<button type="button" class="update-article">수정하기</button>
        </div>
        <div class="image-wrapper">
            <div class="contents-form">
            	<div class="principal-user">
           			<img src="/static/images/basic_profile_image.jpg" alt="">
            		<span></span>
            	</div>
            	<textarea class="article-contents" placeholder="문구 입력..."></textarea>
            	<div class="contents-helper">
            		<button type="button" class="insert-emoji"><img src="/static/images/emoji_icon.png"></button>
            		<span class="length-indicator">
            			<span class="current-length">0</span>/2,200
            		</span>
            	</div>
            	<input type="text" name="feature" placeholder="위치 추가">
            </div>
        </div>
    </div>
</div>
