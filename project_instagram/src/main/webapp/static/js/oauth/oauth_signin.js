const provider = location.pathname.split("/")[3];
let code = location.search.replace("?", "").split("&")[0];
code = code.substring(code.indexOf("=") + 1, code.length);
console.log(code);

$.ajax({
	type: "post",
	url: "/oauth/signin/" + provider,
	data: { "code": code },
	dataType: "text",
	success: function (data) {
		console.log(data);
		if(data == "true") {
			location.href = "/main";
		} else {
			alert("네이버 로그인에 실패했습니다.\n로그인 화면으로 이동합니다.");
			location.href = "/index";
		}
	},
	error: function (xhr, status) {
		console.log(xhr);
		console.log(status);
	}
});