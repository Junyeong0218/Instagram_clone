<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<aside>
	<div class="profile-description">
		<div class="profile-description-image">
		
			<c:if test="${sessionScope.user.has_profile_image == true}">
				<img src="/static/images/user_profile_images/${sessionScope.user.username}.png" alt="프로필">					
			</c:if>
			<c:if test="${sessionScope.user.has_profile_image == false}">
				<img src="/static/images/basic_profile_image.jpg" alt="프로필">
			</c:if>
			
		</div>
		<div class="profile-texts">
			<span class="principal-username">${sessionScope.user.username}</span>
			<span class="principal-nickname">${sessionScope.user.name}</span>
		</div>
		<div>
			<button type="button">전환</button>
		</div>
	</div>
	<div class="recommend-users">
		<div class="recommend-title">
			<span>회원님을 위한 추천</span>
			<button type="button">모두 보기</button>
		</div>
	</div>
	<div class="aside-footer">
		<div class="links">
			<ul>
				<li><a href="#">소개</a></li>
				<li><a href="#">도움말</a></li>
				<li><a href="#">홍보 센터</a></li>
				<li><a href="#">API</a></li>
				<li><a href="#">채용 정보</a></li>
				<li><a href="#">개인정보처리방침</a></li>
				<li><a href="#">약관</a></li>
				<li><a href="#">위치</a></li>
				<li><a href="#">인기 계정</a></li>
				<li><a href="#">해시태그</a></li>
				<li><a href="#">언어</a></li>
			</ul>
		</div>
		<div class="copyright">
			<span>© 2022 INSTAGRAM FROM META</span>
		</div>
	</div>
</aside>