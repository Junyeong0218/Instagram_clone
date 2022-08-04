# 인스타그램
> 수업에서 배운 내용들로 인스타그램을 클론 코딩했습니다.
> 커뮤니티 기능을 주로 구현했습니다.
<br>

![image](https://user-images.githubusercontent.com/94529254/182906796-08b86f06-0dfe-461e-a6ed-dd502b23026a.png)
<br>

## 사용 기술 스택
<p>
  <img src="https://img.shields.io/badge/HTML-e34f26?style=flat-square&logo=HTML5&logoColor=white">&nbsp;
  <img src="https://img.shields.io/badge/Javascript-f7df1e?style=flat-square&logo=Javascript&logoColor=black">&nbsp;
  <img src="https://img.shields.io/badge/CSS-1572b6?style=flat-square&logo=css3&logoColor=white">
</p>
<p>
  <img src="https://img.shields.io/badge/Java-007396?style=flat-square&logo=Java&logoColor=white">&nbsp;
  <img src="https://img.shields.io/badge/MariaBD-1f305f?style=flat-square&logo=MariaDB%20Foundation&logoColor=white">&nbsp;
  <img src="https://img.shields.io/badge/Tomcat-F8DC75?style=flat-square&logo=Apache%20Tomcat&logoColor=black">
</p>
<br>

## 구현 기능
### Restful Api 및 PathVariable 구현
> Restful Api을 구현하다 보니 PathVariable의 필요성을 느껴 Filter를 이용하여 Pathvariable을 구현했습니다.

![image](https://user-images.githubusercontent.com/94529254/182889609-ce5178a5-c9f8-44e4-81ac-ef7d82497faa.png)
<br>

### JWT를 이용한 stateless 및 security 약식 구현
> JWT를 이용하여 서버가 유저 정보를 최대한 가지지 않도록 Stateless를 구현하면서 페이지 접근 및 api 호출을 제어할 수 있도록 했습니다.

![image](https://user-images.githubusercontent.com/94529254/182893462-16d3bfe2-4995-41a6-a908-6ff5f745484c.png)
<br>

### 실시간 채팅 및 알림 기능 구현
> 관련 라이브러리를 사용하지 않고 구현해보았습니다.<br>
> 알림 : 알림에 대한 데이터베이스 테이블에 데이터를 추가하며 알림을 읽은 경우 읽음에 해당하는 열의 값을 true로 변경한다.<br>
> 채팅 : 메세지를 읽은 경우 읽음에 해당하는 열의 값을 true로 변경한다.<br>
> 
> 로그인 -> 알림 및 읽지 않은 메세지 정보 로드 -> 화면에 표시 -> 5초에 한 번 알림 및 읽지 않은 메세지 정보 요청<br>

![image](https://user-images.githubusercontent.com/94529254/182902775-7a76dd59-f090-4d21-851a-9756711532da.png)
![image](https://user-images.githubusercontent.com/94529254/182903882-6c624c86-2b27-49d1-b047-a1e75ce634f0.png)
![image](https://user-images.githubusercontent.com/94529254/182904139-45bd2edc-d6b0-4f40-830b-08d786c828da.png)

> 알림이 있는 경우의 헤더

![image](https://user-images.githubusercontent.com/94529254/182907005-b30e36d7-dc0b-4397-b6a5-32462ebf9d89.png)

<br>

### 외부 파일 로깅 구현
> 콘솔에 로깅하는 것은 실제로 로그가 데이터로 남는 것이 아니라 단순 모니터링 목적이기 때문에 파일로 남겨야한다고 생각하여 외부 txt 파일에 select 쿼리문을 제외한 나머지 쿼리에 대한 로그를 남기도록 구현했습니다.
> Proxy 객체를 이용하여 모든 Dao의 메소드에 대해 같은 내용이 실행될 수 있도록 구현했습니다.

![image](https://user-images.githubusercontent.com/94529254/182905808-d2557035-d9d2-46cb-98d7-38e6dcfa0efd.png)
![image](https://user-images.githubusercontent.com/94529254/182905922-06416137-431e-4121-9867-ab09461abbfe.png)

<br>

## 배운 점 & 아쉬운 점
### 배운 점
> Spring Framework에서는 쉽게 사용할 수 있는 기능들이지만 직접 구현해서 사용하려니 굉장히 어려웠습니다.<br>
> 시간은 오래 걸렸지만 실제 구동되는 것을 보고 차근차근 문제들을 해결해나가 결국에는 모두 구현해낼 수 있었습니다.<br>
> 이번 개발을 통해 Framework의 중요성과 로직을 작성하는 다양한 방법을 익힐 수 있었습니다.
<br>

### 아쉬운 점
> 프로젝트를 마무리하며 돌아보니 코드 가독성이 낮아 다른 사람이 봤을 때 이해할 수 없을 수도 있겠다는 생각이 들었습니다.<br>
> 앞으로는 조금 더 읽히기 쉽도록 코드를 작성해야겠다고 다짐했습니다.
