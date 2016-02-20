var local = {
	getLocalJSON: function(address, blankString) {
		var string = window.localStorage[address];
		if (string == null) string = window.localStorage[address] = blankString;
		return JSON.parse(string);
	},

	getMeetings: function() { 
		return this.getLocalJSON("meetings","[]");
	},

	getPeople: function() {
		return this.getLocalJSON("people","[]");
	},

	getUser: function() { 
		return this.getLocalJSON("user","{},");
	},

	getFriends: function() {
		return this.getLocalJSON("friends","[]");
	},

	addFriend: function(person) {
		var friends = this.getFriends();
		console.log(friends)
		friends.push(person);
		window.localStorage["friends"] = JSON.stringify(friends);
	},

	removeFriend: function(person) {
		var friends = this.getFriends();
		friends.splice(friends.indexOf(person), 1);
		window.localStorage["friends"] = JSON.stringify(friends);
	},

	storeMeetings: function(userID) {
		window.localStorage["meetings"] = JSON.stringify(this.getUserMeetings(userID));
	},

	storePeople: function(people) {
		window.localStorage["people"] = JSON.stringify(this.getPeople(userID));
	},

	storeUser: function(userID, password) {
		window.localStorage["user"] = this.getUser(userID, null, password);
	}
}