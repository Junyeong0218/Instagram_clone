let sessionUUIDForSessionUser;
let token;
let principal;

getUUID();

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
			} else {
				token = data;
				getPrincipal();
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
	if(data == "true") {
		$.ajax({
			type: "get",
			url: "/auth/principal",
			async: false,
			headers: { "Authorization": token },
			dataType: "text",
			success: function (data) {
				console.log(data);
				principal = JSON.parse(data);
			},
			error: function (xhr, status, error) {
				console.log(xhr);
				console.log(status);
				console.log(error);
			}
		});
	} else {
		alert("userdata load failed");
	}
}