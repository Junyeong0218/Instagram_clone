<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<head>
    <link rel="stylesheet" href="/static/css/new_article_modal.css">
</head>

<div class="new-article-modal">
    <button class="new-article-closer" type="button">
    	<img src="/static/images/new_article_modal_closer.png" alt="">
    </button>
    <div class="new-article-wrapper">
        <div class="title">새 게시물 만들기</div>
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
        </div>
    </div>
</div>
