Local.getLocalJSON = function(address, blankString) {
	var string = window.localStorage[address];
	if (string == null) string = window.localStorage[address] = blankString;
	return JSON.parse(string);
}

Local.getMeetings = function() { 
	return Local.getLocalJSON("meetings","[]");
}

Local.getSchedule = function() { 
	return Local.getLocalJSON("schedule","[]");
}

Local.getPeople = function() {
	return Local.getLocalJSON("people","[]");
}

Local.getUser = function() { 
	return Local.getLocalJSON("user","{}");
}

Local.getFriends = function() {
	return Local.getLocalJSON("friends","[]");
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

Local.storeMeetings = function(userID) {
	window.localStorage["meetings"] = JSON.stringify(getUserMeetings(userID));
}

Local.storePeople = function(people) {
	window.localStorage["people"] = JSON.stringify(getPeople(userID));
}

Local.storeUserLocal = function(userID, password) {
	window.localStorage["user"] = getUser(userID, null, password);
}