<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html lang="ko">

    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>회원가입 · Instagram</title>
        <link rel="shortcut icon" href="/static/images/shortcut.ico" type="image/x-icon">
        <link rel="stylesheet" href="/static/css/container.css">
        <link rel="stylesheet" href="/static/css/signup/signup.css">
        <link rel="stylesheet" href="/static/css/short_footer.css">
        <script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>
    </head>

    <body>
        <div class="container">
            <main>
                <div class="form-wrapper">
                    <h1 class="logo"></h1>
                    <span>친구들의 사진과 동영상을 보려면 가입하세요.</span>
                    <button type="button" class="facebook-signin">
                        <img src="/static/images/login_facebook_inverted.png" alt="페이스북 로그인 로고">
                        <span>Facebook으로 로그인</span>
                    </button>
                    <div class="separator">
                        <div class="line"></div>
                        <div class="or">또는</div>
                        <div class="line"></div>
                    </div>
                    <form class="signup-form">
                        <label for="email">
                        	<span>휴대폰 번호 또는 이메일 주소</span>
                       		<input type="text" name="email">
                            <div class="input-indicator">
                                <span></span>
                            </div>
                        </label>
                        <span class="input-message"></span>
						<label for="name">
							<span>성명</span>
							<input type="text" name="name">
                            <div class="input-indicator">
                                <span></span>
                            </div>
                        </label>
                        <span class="input-message"></span>
                        <label for="username">
                        	<span>사용자 이름</span>
                        	<input type="text" name="username">
                            <div class="input-indicator">
                                <span></span>
                            </div>
                        </label>
                        <span class="input-message"></span>
                        <label for="password">
                        	<span>비밀번호</span>
                        	<input type="password" name="password">
                            <div class="input-indicator">
                                <span></span>
                                <button type="button" class="default-toggle hidden">비밀번호 표시</button>
                            </div>
                        </label>
                        <span class="input-message"></span>
                        <button class="submit-button" type="button" disabled>
                            <span>가입</span>
                        </button>
                    </form>
                </div>
                <div class="has-account">
                    <span>계정이 있으신가요? <a href="/index">로그인</a></span>
                </div>
                <div class="download">
                    <p>앱을 다운로드하세요.</p>
                    <div class="download-buttons">
                        <a href="#"><img src="/static/images/download_applestore.png" alt="앱스토어 다운로드"></a>
                        <a href="#"><img src="/static/images/download_playstore.png" alt="플레이스토어 다운로드"></a>
                    </div>
                </div>
            </main>
        </div>

        <jsp:include page="/WEB-INF/views/templates/short_footer.jsp" />

        <script src="/static/js/signup/signup.js"></script>
    </body>

    </html>