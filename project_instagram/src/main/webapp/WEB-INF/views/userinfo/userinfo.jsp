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
	<link rel="shortcut icon" href="/static/images/shortcut.ico" type="image/x-icon">
	<link rel="stylesheet" href="/static/css/container.css">
	<link rel="stylesheet" href="/static/css/main/header.css">
	<link rel="stylesheet" href="/static/css/userinfo/userinfo.css">
	<script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>
	<script type="text/javascript" src="/static/js/principal.js"></script>
</head>

<body>
	<div class="container">
		
		<jsp:include page="/WEB-INF/views/templates/header.jsp" />
		
		<main>
			<ul class="userinfo-menus">
				<li class="userinfo-menu selected-menu">
					<button type="button" class="to-userinfo">프로필 편집</button>
				</li>
				<li class="userinfo-menu">
					<button type="button" class="to-password">비밀번호 변경</button>
				</li>
				<li class="userinfo-menu">
					<button type="button">앱 및 웹사이트</button>
				</li>
				<li class="userinfo-menu">
					<button type="button">이메일 및 SNS</button>
				</li>
				<li class="userinfo-menu">
					<button type="button">푸시 알림</button>
				</li>
				<li class="userinfo-menu">
					<button type="button">연락처 관리</button>
				</li>
				<li class="userinfo-menu">
					<button type="button">개인정보 및 보안</button>
				</li>
				<li class="userinfo-menu">
					<button type="button">로그인 활동</button>
				</li>
				<li class="userinfo-menu">
					<button type="button">Instagram에서 보낸 이메일</button>
				</li>
				<li class="userinfo-menu">
					<button type="button">도움말</button>
				</li>
				<div class="change-professional">
					<button type="button">
						<span>프로페셔널 계정으로 전환</span>
					</button>
				</div>
				<hr class="menu-footer-separator">
				<div class="menu-footer">
					<span class="menu-footer-image"></span>
					<div class="menu-footer-title">계정 센터</div>
					<div class="menu-fotter-text">스토리 및 게시물 공유, 로그인 등 Instagram,
						Facebook 앱, Messenger 간에 연결된 환경에 대한 설정을 관리하세요.</div>
				</div>
			</ul>
			<div class="userinfo-wrapper">
				<form class="userinfo-form" enctype="multipart/form-data">
					<div class="userinfo-profile-image">
						<img src="/static/images/basic_profile_image.jpg" alt="프로필">

						<div class="profile-image-input-wrapper">
							<span class="username"></span>
							<label class="" for="profile-image">프로필 사진 바꾸기</label>
							<input class="hidden" id="profile-image" name="file" type="file" accept="image/*">
						</div>
					</div>
					<div class="userinfo-input-wrapper">
						<span class="field-name">이름</span>
						<div class="description-wrapper">
							<input type="text" name="name" value="">
							<span class="description">
								<p>사람들이 이름, 별명 또는 비즈니스 이름 등 회원님의 알려진 이름을 사용하여 회원님의 계정을 찾을 수
									있도록 도와주세요.</p>
								<p>이름은 14일 안에 두 번만 변경할 수 있습니다.</p>
							</span>
						</div>
					</div>
					<div class="userinfo-input-wrapper">
						<span class="field-name">사용자 이름</span>
						<div class="description-wrapper">
						
							<input type="text" name="username" value="">
							<span class="description">
								<p>In most cases, you'll be able to change your username
									back to ${sessionScope.user.username} for another 14 days.</p>
							</span>
						</div>
					</div>
					<div class="userinfo-input-wrapper">
						<span class="field-name">웹 사이트</span>
						<div class="description-wrapper">
							<input type="text" name="website" placeholder="웹사이트" value="">
						</div>
					</div>
					<div class="userinfo-input-wrapper">
						<span class="field-name">소개</span>
						<div class="description-wrapper">
							<textarea name="description" value=""></textarea>
						</div>
					</div>
					<div class="userinfo-input-wrapper">
						<span class="field-name"></span>
						<div class="description-wrapper">
							<span class="no-input-description-wrapper">
								<div class="no-input-description">
									<h2>개인정보</h2>
									<span>비즈니스나 반려동물 등에 사용된 계정인 경우에도 회원님의 개인정보를 입력하세요. 공개
										프로필에는 포함되지 않습니다.</span>
								</div>
							</span>
						</div>
					</div>
					<div class="userinfo-input-wrapper">
						<span class="field-name">이메일</span>
						<div class="description-wrapper">
							<input type="email" name="email" value="">
						</div>
					</div>
					<div class="userinfo-input-wrapper">
						<span class="field-name">전화번호</span>
						<div class="description-wrapper">
							<input type="tel" name="phone" value="">
						</div>
					</div>
					<div class="userinfo-input-wrapper">
						<span class="field-name">성별</span>
						<div class="description-wrapper">
							<input type="text" name="gender" value="" readonly>
						</div>
					</div>
					<div class="userinfo-input-wrapper">
						<span class="field-name">비슷한 계정 추천</span> <label
							for="recommend-user" class="check-wrapper"> <input
							type="checkbox" id="recommend-user" name="recommend" checked>
							<span>팔로우할 만한 비슷한 계정을 추천할 때 회원님의 계정을 포함합니다.</span>
						</label>
					</div>
					<div class="userinfo-input-wrapper">
						<span class="field-name"></span>
						<div class="form-buttons">
							<button class="submit-button disabled" type="button" disabled>제출</button>
							<button type="button">계정을 일시적으로 비활성화</button>
						</div>
					</div>
				</form>
				<form class="password-form hidden" >
					<div class="userinfo-profile-image">
						<img src="/static/images/basic_profile_image.jpg" alt="프로필">
						
						<div class="profile-image-input-wrapper">
							<span class="username"></span>
						</div>
					</div>
					<div class="userinfo-input-wrapper">
						<span class="field-name">이전 비밀번호</span>
						<div class="description-wrapper">
							<input type="password" name="origin_password">
						</div>
					</div>
					<div class="userinfo-input-wrapper">
						<span class="field-name">새 비밀번호</span>
						<div class="description-wrapper">
							<input type="password" name="password">
						</div>
					</div>
					<div class="userinfo-input-wrapper">
						<span class="field-name">새 비밀번호 확인</span>
						<div class="description-wrapper">
							<input type="password" name="password_confirm">
						</div>
					</div>
					<div class="userinfo-input-wrapper">
						<span class="field-name"></span>
						<div class="form-buttons">
							<button class="password-submit-button disabled" type="button" disabled>비밀번호 변경</button>
							<button type="button">비밀번호를 잊으셨나요?</button>
						</div>
					</div>
				</form>
			</div>
		</main>
	</div>
	
	
    <jsp:include page="/WEB-INF/views/templates/new_file_picker.jsp"></jsp:include>
	
	<jsp:include page="/WEB-INF/views/templates/short_footer.jsp" />
	
	<div class="pop-up">
		<div class="gender-control">
			<div class="popup-header">
				<span>성별</span>
				<button type="button"></button>
			</div>
			<div class="radio-buttons">
				<label for="male"> <input type="radio" id="male"
					name="gender" value="남성" checked> <span>남성</span>
				</label> <label for="female"> <input type="radio" id="female"
					name="gender" value="여성"> <span>여성</span>
				</label> <label for="lgbtq"> <input type="radio" id="lgbtq"
					name="gender" value="맞춤 성별"> <span>맞춤 성별</span>
				</label> <label for="cryptonyme"> <input type="radio"
					id="cryptonyme" name="gender" value="밝히고 싶지 않음"> <span>밝히고
						싶지 않음</span>
				</label>
			</div>
			<button type="button">완료</button>
		</div>
	</div>
   	<script src="/static/js/main/searcher_control.js"></script>
	<script src="/static/js/main/profile_menu_control.js"></script>
    <script src="/static/js/main/activity_menu_control.js"></script>
    <script src="/static/js/main/new_file_picker.js"></script>
	<script src="/static/js/userinfo/userinfo.js"></script>
	<script src="/static/js/userinfo/gender_popup.js"></script>
</body>

</html>