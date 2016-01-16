var url = "http://localhost:80/MyHM/Backend/"

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

function storeUserSession(userID, email, password, async) {
	if(userID != null && password != null) {
		var result = $.ajax({
			url: url+"session", 
			async: async,
			method: "POST",
			data: {userID: userID, password: password}
		});
	} else if(email != null && password != null) {
		var result = $.ajax({
			url: url+"session", 
			async: async,
			method: "POST",
			data: {email: email, password: password},
			error: function(xhr, status, error) {
				console.log(xhr.responseText);
			}
		});
	}
}

function locallyStoreInfo(userID) {
	window.localStorage['meetings'] = JSON.stringify(getUserMeetings(userID))
	window.localStorage['schedule'] = JSON.stringify(getSchedule(userID))
}