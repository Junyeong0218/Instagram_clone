<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
    <link rel="stylesheet" href="/static/css/new_article_modal.css">
</head>

<div class="new-article-modal">
    <button class="new-article-closer" type="button">
    	<img src="/static/images/new_article_modal_closer.png" alt="">
    </button>
    <div class="new-article-wrapper">
        <div class="title">새 게시물 만들기
        	<button type="button" class="prev-form">이전</button>
        	<button type="button" class="next-form">다음</button>
        	<button type="button" class="submit-article">공유하기</button>
        </div>
        <div class="image-wrapper">
            <div class="non-picked-image">
                <img src="/static/images/non_pick_image.png" alt="">
                <span>사진과 동영상을 여기에 끌어다 놓으세요</span>
                <input id="new-file" type="file" name="file" accept="image/*, video/*">
                <label for="new-file">컴퓨터에서 선택</label>
            </div>
            <div class="picked-image disabled">
				
                <button type="button" class="select-image-button">
                    <img src="/static/images/select_image_button.png" alt="">
                </button>
                <div class="image-picker">
                	<div class="liner">
	                	<div class="small-wrapper">
	                		
	                	</div>
	                	<button type="button" class="prev-button"><img src="/static/images/new_article_prev_button.png" alt=""></button>
	                	<button type="button" class="next-button"><img src="/static/images/new_article_next_button.png" alt=""></button>
                	</div>
                	<button type="button" class="add-new-media">
                		<img src="/static/images/new_article_add_new_media.png" alt="">
                	</button>
                </div>
            </div>
            <div class="contents-form">
            	<div class="principal-user">
            		
            		<c:if test="${sessionScope.user.has_profile_image == true}">
            			<img src="/static/images/user_profile_images/${sessionScope.user.file_name}" alt="">
            		</c:if>
            		<c:if test="${sessionScope.user.has_profile_image == false}">
            			<img src="/static/images/basic_profile_image.jpg" alt="">
            		</c:if>
            		
            		<span>${sessionScope.user.username}</span>
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
