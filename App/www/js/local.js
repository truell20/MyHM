function getMeetingsLocal() { 
	return JSON.parse(window.localStorage["meetings"]);
}

function getScheduleLocal() { 
	return JSON.parse(window.localStorage["schedule"]);
}

function getPeopleList() {
	return JSON.parse(window.localStorage["people"]);
}

function getUserLocal() { 
	return JSON.parse(window.localStorage["user"]); 
}

function getFriends() {
	return JSON.parse(window.localStorage["friends"]); 
}

function lookupUser(name) {
	var people = getPeopleList();
	var maxPerson;
	var maxProb = 0;
	people.forEach(function(person) {
		for (var result = 0, i = person.name.length; i--;){
	        if (typeof name[i] == 'undefined' || person.name[i] == name[i]);
		    else if (person.name[i].toLowerCase() == name[i].toLowerCase()) result++;
		    else result += 4;
    	} var prob = 1 - (result + 4*Math.abs(person.name.length - name.length))/(2*(person.name.length+name.length));

    	if (prob > maxProb) {
    		maxProb = prob;
    		maxPerson = person;
    	}
	}); return maxPerson;
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