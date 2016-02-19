var url = "http://localhost:80/MyHM/Backend/"

function getUser(userID, email, password) {
	return $http({
		method: "GET",
		url:url+"user",
		data: {userID: userID, email: email, password: password}
	});
}

function searchForUser(searchTerm) {
	return $http({
		url: url+"user",
		method: "GET",
		data: {searchTerm: searchTerm}
	});
}

function getSchedule(userID) {
	return $http({
		method: "GET",
		url:url+"schedule",
		data: {userID: userID}
	});

function getUserMeetings(userID) {
	return $http({
		url: url+"meeting",
		method: "GET",
		data: {userID: userID}
	});
}

function getMeeting(meetingID) {
	return $http({
		url: url+"meeting",
		method: "GET",
		data: {meetingID: meetingID}
	});
}