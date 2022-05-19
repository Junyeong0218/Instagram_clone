const principal = getPrincipal(); 

function getPrincipal() {
	let userdata;
	$.ajax({
		type: "get",
		url: "/auth/principal",
		async: false,
		dataType: "text",
		success: function (data) {
			userdata = JSON.parse(data);
		},
		error: function (xhr, status, error) {
			console.log(xhr);
			console.log(status);
			console.log(error);
		}
	});
	console.log(userdata);
	return userdata;
}