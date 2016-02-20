Local.getLocalJSON = function(address, blankString) {
	var string = window.localStorage[address];
	if (string == null) string = window.localStorage[address] = blankString;
	return JSON.parse(string);
}

Local.getMeetingsLocal = function() { 
	return getLocalJSON("meetings","[]");
}

Local.getScheduleLocal = function() { 
	return getLocalJSON("schedule","[]");
}

Local.getPeopleList = function() {
	return getLocalJSON("people","[]");
}

Local.getUser = function() { 
	return getLocalJSON("user","{}");
}

Local.getFriends = function() {
	return getLocalJSON("friends","[]");
}

Local.addFriend = function(person) {
	var friends = getFriends();
	friends.push(person);
	window.localStorage["friends"] = JSON.stringify(friends);
}

Local.removeFriend = function(person) {
	var friends = getFriends();
	friends.splice(friends.indexOf(person), 1);
	window.localStorage["friends"] = JSON.stringify(friends);
}

Local.storeMeetingsLocal = function(userID) {
	window.localStorage["meetings"] = JSON.stringify(getUserMeetings(userID));
}

Local.storeScheduleLocal = function(userID) {
	window.localStorage["schedule"] = JSON.stringify(getSchedule(userID));
}

Local.storeUserLocal = function(userID, password) {
	window.localStorage["user"] = getUser(userID, null, password);
}