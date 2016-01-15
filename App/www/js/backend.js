var url = "http://localhost:80/ApPosition/Backend/"

function getUser(userID, email, password) {
	var result = null;
	if(userID != null) {
		$.ajax({
			url: url+"user",
			async: false,
			method: "GET",
			data: {userID: userID, password: password}
		})
	} else if(email != null) {
		$.ajax({
			url: url+"user",
			async: false,
			method: "GET",
			data: {email: email, password: password}
		})
	}

	return result.responseJSON
}

function searchForUser(searchTerm) {
	return $.ajax({
		url: url+"user",
		async: false,
		method: "GET",
		data: {searchTerm: searchTerm}
	}).responseJSON;
}

function getSchedule(userID) {
	return $.ajax({
		url: url+"schedule",
		async: false,
		method: "GET",
		data: {userID: userID}
	}).responseJSON;
}

function getUserMeetings(userID) {
	return $.ajax({
		url: url+"meeting",
		async: false,
		method: "GET",
		data: {userID: userID}
	}).responseJSON;
}

function getMeeting(meetingID) {
	return $.ajax({
		url: url+"meeting",
		async: false,
		method: "GET",
		data: {meetingID: meetingID}
	}).responseJSON;
}
