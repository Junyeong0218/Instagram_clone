<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>받은 메세지함 • Direct</title>
    <link rel="shortcut icon" href="/static/images/shortcut.ico" type="image/x-icon">
    <link rel="stylesheet" href="/static/css/container.css">
    <link rel="stylesheet" href="/static/css/main/header.css">
    <link rel="stylesheet" href="/static/css/direct/message.css">
    <link rel="stylesheet" href="/static/css/direct/new_target_modal.css">
    <script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>
</head>

<body>
    <div class="container">
        
        <jsp:include page="/WEB-INF/views/templates/header.jsp" />

        <main>
            <div class="message-box">
            	<div class="users">
	            	<div class="session-user">
	                    <span class="username">hippo2003</span>
	                    <button type="button" class="new-message">
	                        <img src="/static/images/direct_new_message.png">
	                    </button>
	                </div>
	            	<div class="user-list">
	                    <button type="button" class="user active">
	                        <img src="/static/images/basic_profile_image.jpg">
	                        <div class="user-description">
	                            <span class="name">박준영님</span>
	                            <span class="recent-activity">ㅋㅋㅋㅋㅋㅋㅋㅋ<span class="date"> · 2일</span></span>
	                        </div>
	                    </button>
	                </div>
            	</div>
            	
            	<div class="main-content active">
            		<img src="/static/images/direct_main_image.png">
            		<span class="big-text">내 메시지</span>
            		<span class="small-text">친구나 그룹에 비공개 사진과 메시지를 보내보세요.</span>
            		<button type="button" class="main-new-message">메시지 보내기</button>
            	</div>
                    
                <div class="message-wrapper">
                    <div class="target-user">
                        <button type="button" class="target-user-info">
                            <img src="/static/images/basic_profile_image.jpg">
                            <span class="name">박준영님</span>
                        </button>
                        <button type="button" class="message-info">
                            <img src="/static/images/message_info.png">
                        </button>
                    </div>
                    
                    <div class="message-description">
                        <div class="line date">
                            2022년 4월 30일 오후 9:37
                        </div>
                        <div class="line receive">
                            <div class="user-profile-image">
                                <img src="/static/images/basic_profile_image.jpg">
                            </div>
                            <div class="message">
                                차삿다 ㅎㅎㅎㅎㅎ
                            </div>
                        </div>
                        <div class="line send">
                            <div class="message">
                                헐 뭐 샀어요??ㅋㅋ
                            </div>
                        </div>
	                </div>
                    <div class="message-sender">
                        <div>
                            <button type="button" class="insert-emoji">
                                <img src="/static/images/emoji_icon.png">
                            </button>
                            <textarea name="written-message" placeholder="메시지 입력..."></textarea>
                            <input type="file" name="file" class="image-input">
                            <button type="button" class="send-image">
                                <img src="/static/images/direct_send_image.png" alt="">
                            </button>
                            <button type="button" class="send-reaction">
                                <img src="/static/images/menu_activity.png" alt="">
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>
    
    <div class="new-target-modal">
    	<div class="followers">
    		<div class="modal-header">
    			<button type="button" class="close-modal">
    				<img src="/static/images/direct_modal_closer.png">
    			</button>
    			<span class="title">새로운 메시지</span>
    			<button type="button" class="next" disabled="disabled">다음</button>
    		</div>
    		<div class="select-receiver">
    			<span>받는 사람:</span>
    			<div class="receivers">
	    			<input type="text" name="receiver"  class="receiver" placeholder="검색...">
    			</div>
    		</div>
    		<div class="recommend-receivers">
    			<div class="title">추천</div>
    			<div class="row">
    				<img src="/static/images/basic_profile_image.jpg">
    				<div class="user-info">
    					<span class="username">hippo2004</span>
    					<span class="name">박준영</span>
    				</div>
    				<input type="checkbox"  name="selected-users" class="selected-users">
    			</div>
    		</div>
    	</div>
    </div>
    <script src="/static/js/direct/message_control.js"></script>
    <script src="/static/js/main/profile_menu_control.js"></script>
    <script src="/static/js/main/searcher_control.js"></script>
</body>

</html>