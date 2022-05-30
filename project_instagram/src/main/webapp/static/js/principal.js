let sessionUUIDForSessionUser;
let token;
let principal;

let first_load_flag = true;

getUUID();
setInterval(() => {
	getToken();
}, 5000);

function getUUID() {
	$.ajax({
		type: "get",
		url: "/security/uuid",
		async: false,
		dataType: "text",
		success: function (data) {
			console.log("uuid : " + data);
			sessionUUIDForSessionUser = data;
			getToken();
		},
		error: function (xhr, status, error) {
			console.log(xhr);
			console.log(status);
			console.log(error);
		}
	});
}

function getToken() {
	$.ajax({
		type: "get",
		url: "/security/token",
		async: false,
		data: { "uuid": sessionUUIDForSessionUser },
		dataType: "text",
		success: function (data) {
			console.log(data);
			if(data == null || data == "null" || data == "") {
				alert("token load failed");
			} else if(first_load_flag){
				first_load_flag = false;
				token = data;
				getPrincipal();
			} else {
				token = data;
			}
		},
		error: function (xhr, status, error) {
			console.log(xhr);
			console.log(status);
			console.log(error);
		}
	});
}

function getPrincipal() {
	$.ajax({
		type: "get",
		url: "/auth/principal",
		async: false,
		headers: { "Authorization": token },
		dataType: "text",
		success: function (data) {
			principal = JSON.parse(data);
			console.log(principal);
		},
		error: function (xhr, status, error) {
			console.log(xhr);
			console.log(status);
			console.log(error);
		}
	});
}