const follower = document.querySelector(".follower");
const following = document.querySelector(".following");

window.onload = loadFollowingInfo;

function loadFollowingInfo() {
	$.ajax({
		type: "get",
		url: "/follow/load-follow-info",
		dataType: "text",
		success: function (data) {
			data = JSON.parse(data);
			follower.innerText = data.follower;
			following.innerText = data.following;
		},
		error: function (xhr, status, error) {
			console.log(xhr);
			console.log(status);
			console.log(error);
		}
	});
}