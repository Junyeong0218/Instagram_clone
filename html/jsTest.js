let array = [];
let array2 = new Array(10);
// new Array(); 는 java 에서 ArrayList 처럼 사용.
array2[0] = 10;
array2.push(100);
// array.push 는 배열의 가장 마지막에 요소추가. java ArrayList.add( E e ) 와 같음
// array.pop 은 배열의 가장 마지막 요소를 반환함과 동시에 배열에서 제거
// array.splice(start, count, [replace ... ]) 는 start 인덱스부터 count 갯수만큼의 배열을 생성
// 및 기존 배열에서 제거하고 replace 가 존재하는 경우, 기존 배열에서 제거된 자리를 해당 값으로 대체함
// array.concat(another_array) 는 서로 다른 두 배열을 합친 배열을 반환한다.

// console.log(array2.concat(array));
// console.log(array2.pop());
// console.log(array);
// console.log(array2);

// == 비교 연산자는 자료형이 달라도 내부 값만 같으면 true를 반환
// === 비교 연산자는 내부 값 + 자료형이 같아야만 true를 반환

// 호이스팅(hoisting) 이란 변수를 뒤에서 선언하지만, 마치 앞에서 미리 선언한 것처럼 인식함.
// 함수 실행문을 앞에 두고 선언 부분을 뒤에 두더라도 앞으로 끌어올려 인식함.
// 문법 실행보다 변수 선언을 먼저 확인한 후 문법을 실행한다.
// undefined  == 변수 선언은 완료되었으나 초기화 되지 않아서 자료형을 판단할 수 없는 경우 != null
// var a = 20;
// var a = 20;
// var 타입의 변수는 중복 선언이 가능하기 때문에, 사용하지 말 것.

// let 의 경우 호이스팅이 없다
// 재할당은 가능하지만, 재선언은 불가.

// 변수 사용법
// 전역 변수는 최소한으로 사용

// DOM 객체 == HTML 태그

// 매개변수와 전역변수의 이름은 서로 같을 수 없다.

// 익명 함수 == java 람다식과 같다.
// let result = function(a, b) {
//        return a + b;
// }
// 위 경우 result(a + b); 로 호출하며 return 값이 바로 result에 초기화되어 사용되는 형태.
// 이벤트 처리 시 함수 호출에서 많이 사용

// (function(a, b) {
//      return a + b;
//}) (100, 200);
// 위 경우 익명 함수이기 때문에 함수 자체를 () 로 묶은 뒤 그 이후에 바로 함수 사용하는 경우.

// const varName = ([매개변수]) => { return value; };
// const varName = ([매개변수]) => value;    ==> return이 존재하고, 로직이 return을 포함하여 1문장으로 이루어져 있는 경우 사용.
// const varName = ([매개변수]) => { }; return 값이 없는 경우, return 없이도 사용이 가능하나, 선언한 varName의 값을 사용하려는 경우, 해당 변수는 함수 자체이기 때문에 undefined 이다.

// event
// mouse
// click
// dbclick
// mousedown : 눌렀을 때
// mouseup : 버튼에서 손을 뗄 때
// mousemove : 요소위에서 포인터가 움질일 때,
// mouseover : 요소 위로 포인터가 올라갈 때,
// mouseout : 요소에서 포인터가 벗어날 때,

// key
// keydown : 누르는 동안
// keypress : mouseclick과 같음
// keyup : 키에서 손을 뗄 때,

// 문서 로딩 이벤트
// abort : 문서가 완전히 로딩되기 전에 "불러오기"를 멈췄을 때 발생하는 이벤트 ( 데이터 통신의 의미에서 불러오기 )
// error : 문서가 정확히 로딩되지 않았을 때, 이벤트가 발생. ( html 파일을 열었을 때, 문제가 발생한 경우. )
// load : 문서 로딩이 끝났을 때,
// resize : 문서 화면 크기가 바뀌었을 때 발생
// scroll : 화면이 스크롤 되었을 때 발생
// unload : 문서에서 벗어날 때 발생

// form
// blur : 포커스를 잃었을 때 ex) username input에서 onblur -> 정규식 판단 및 span 출력
// change : 내용이 변한 경우,
// focus : 요소에 포커스가 놓였을 때,
// reset : 폼이 리셋되었을 때 발생
// submit : 폼을 서버로 전송할 때 발생

// 태그 내 onEvent 보다는 javascript를 이용해서 하는 것이 좋다.
// document.querySelector("[css 선택자]");
// dom객체.onEvent = function();
// dom객체에 Event 발생 시 eventHandler 가 해당 객체의 onEvent 메소드를 호출한다.
// 함수의 return으로 또 다른 함수를 return 할 수 있다.

// 객체
// 자바스크립트 안에 미리 객체로 정의해 놓은 것
// DOM 객체, 브라우저 관련 객체, 내장 객체, 사용자 정의 객체
// 객체를 인스턴스화 하는 것은 java 와 같이 new 객체명(); 으로 생성한다.

// window
// document
// loaction
// name = 브라우저 창의 이름
// screenX = 모니터 왼쪽 끝이 브라우저 창의 왼쪽에서 떨어져 있는 거리
// screenY = 위쪽
// scrollX, Y = 픽셀단위
// blur() = 현재 창에서 포커스 제거
// open() = 새로운 창 열기
// print() = 프린트기 출력화면으로 이동

// navigator
// battery, cookieEnabled
// geolocation = 기기의 위치 정보를 나타낸다.
// language = 브라우저 UI의 언어 정보를 나타낸다.
// oscpu = 운영체제 정보
// userAgent = 현재 브라우저 정보 문자열

// history
// length = 항목의 갯수
// back() = 이전 페이지를 불러옴
// forward() = 다음 페이지를 불러옴
// go() = 상대적인 위치를 매개변수로 이동시킴, 0은 자신 양수면 forward, 음수면 back()

// location

// screen

// DOM 객체
// 웹 문서에 접근하여 요소들을 제어함

// DOM 트리
// 웹 문서에 있는 요소들 간의 부모, 자식 관꼐를 계층 구조로 표시한 것
// 노드(node) : DOM 트리에서 가지가 갈라져 나간 항목
// root node : DOM 트리의 시작 부분 (HTML)

// DOM을 구성하는 원칙
// 모든 HTML 태그는 요소(Element) 노드
// 웹 문서의 텍스트 내용은 요소 노드의 자식 노드인 text 노드
// 태그의 속성은 요소 노드의 자식 노드인 속성 노드
// 주석은 comment 노드

// DOM 요소에 접근하는 방법
// getElementById()
// getElementsByClassName()
// getElementsByTagName()
// 위 getElement"s" 들은 HTMLCollection 으로 반환한다.
// querySelector() : 매개변수로 css 선택자로써 접근한다.
// querySelectorAll() : HTMLCollection으로 반환한다.

// `` < 백쿼터 문자열을 concat 함수 사용 불가 ( + 연산자를 이용해 붙여야함 )

const box = document.querySelector("#box");
const button = document.querySelector(".btns");
const alert_button = document.querySelector(".alert");
const color = () => Math.round(Math.random() * 255);
const test1 = () => alert("알림창 호출1");
// class Rgb {
//         constructor() {
//                 this.r = 0;
//                 this.g = 0;
//                 this.b = 0;
//                 this.toArgs = () => `${this.r}, ${this.g}, ${this.b}`;
//         }
// }
// 위 처럼 constructor() 메소드를 이용해 java와 같이 클래스로 이용할 수 있다.
function Rgb() {
        this.r = 0;
        this.g = 0;
        this.b = 0;
        this.toArgs = () => `${this.r}, ${this.g}, ${this.b}`;
}

button.onclick = changeColor;
alert_button.onclick = test1;

function changeColor() {
        const box_color = new Rgb();
        box_color.r = color();
        box_color.g = color();
        box_color.b = color();
        console.log(box_color);
        box.style.backgroundColor = `rgb(${box_color.toArgs()})`;
}

// let click = function (a, b) {
//         return a + b;
// }

// let click2 = (a, b) => a + b;

// console.log(click2(2, 5));

// console.log(click(1, 2));

// console.log((function (a, b) {
//         return a + b;
// })(100, 200));

// const body = document.body;
// body.innerHTML = `<h1>구구단</h1>`;
// const ul_wrapper = document.createElement("div");
// ul_wrapper.className = "wrapper";

// for (let i = 0; i < 9; i++) {
//         const dan = document.createElement("div");
//         dan.className = "dan";
//         const h3 = document.createElement("h3");
//         h3.innerText = `${i + 1}단`;
//         const ul = document.createElement("ul");

//         for (let j = 0; j < 9; j++) {
//                 const li = document.createElement("li");
//                 li.innerText = `${i + 1} * ${j + 1} = ${(i + 1) * (j + 1)}`;
//                 ul.appendChild(li);
//         }

//         dan.appendChild(h3);
//         dan.appendChild(ul);
//         ul_wrapper.appendChild(dan);
// }

// body.appendChild(ul_wrapper);