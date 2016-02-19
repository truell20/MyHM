var url = "http://localhost:80/MyHM/Backend/";

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
}

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

// Local storage
function getMeetingsLocal() { return window.localStorage["meetings"]; }
function getScheduleLocal() { return window.localStorage["schedule"]; }
function getFriendsMeetings() { return window.localStorage["friendsMeetings"]; }
function getUserLocal() { return window.localStorage["user"]; }

function storeMeetingsLocal(userID) {
	window.localStorage["meetings"] = JSON.stringify(getUserMeetings(userID));
}

function storeScheduleLocal(userID) {
	window.localStorage["schedule"] = JSON.stringify(getSchedule(userID));
}

function storeFriendSchedule(friendID) {
	var friendsMeetings = getFriendsMeetings();
	friendsMeetings.push(getUserMeetings(userID));

	window.localStorage["friendsMeetings"] = JSON.stringify(friendsMeetings);
}

function storeUserLocal(userID, password) {
	window.localStorage["user"] = getUser(userID, null, password);
}