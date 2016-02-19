function getLocalJSON(address,blankString) {
	var string = window.localStorage[address];
	if (string == null) string = window.localStorage[address] = "{}";
	return JSON.parse(string);
}

function getMeetingsLocal() { 
	return getLocalJSON("meetings","[]");
}

function getScheduleLocal() { 
	return getLocalJSON("schedule","[]");
}

function getPeopleList() {
	return getLocalJSON("people","[]");
}

function getUserLocal() { 
	return getLocalJSON("user","{}");
}

function getFriends() {
	return getLocalJSON("friends","[]");
}

function lookupUser(name) {
	var people = getPeopleList();
	var minPerson;
	var minProb = 99999;
	for (var p=0; p<people.length; p++) {
		var prob = levenshtein(people[p].name,name)
    	if (prob < minProb) {
    		minProb = prob;
    		minPerson = people[p];
    	}
	} return minPerson;
}

function addFriend(person) {
	var friends = getFriends();
	friends.push(person);
	window.localStorage["friends"] = JSON.stringify(friends);
}

function removeFriend(person) {
	var friends = getFriends();
	friends.splice(friends.indexOf(person), 1);
	window.localStorage["friends"] = JSON.stringify(friends);
}

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