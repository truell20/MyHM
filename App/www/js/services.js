angular.module('ApPosition.services', [])

.service("$localStorage", function() {
	return {
		getLocalJSON: function(address, blankString) {
			var string = window.localStorage.getItem(address);
			if (string == null) {
				string = blankString;
				window.localStorage.setItem(address, blankString);
			}
			return JSON.parse(string);
		},

		storeLocalJSON: function(address, object) {
			window.localStorage.setItem(address, JSON.stringify(object));
		},

		getMeetings: function() { 
			return this.getLocalJSON("meetings","[]");
		},

		getPeople: function() {
			return this.getLocalJSON("people","[]");
		},

		getUser: function() { 
			var user = this.getLocalJSON("user","{},");
			user.__proto__ = Person.prototype;
			return user;
		},

		getFriends: function() {
			return this.getLocalJSON("friends","[]");
		},

		addFriend: function(person) {
			var friends = this.getFriends();
			console.log(friends)
			friends.push(person);
			this.storeLocalJSON("friends", friends);
		},

		removeFriend: function(person) {
			var friends = this.getFriends();
			friends.splice(friends.indexOf(person), 1);
			this.storeLocalJSON("friends", friends);
		},

		storeMeetings: function(meetings) {
			this.storeLocalJSON("meetings", meetings);
		},

		storePeople: function(people) {
			this.storeLocalJSON("people", people);
		},

		storeUser: function(user) {
			this.storeLocalJSON("user", user);
		}
	};
})

.service("$backend", function($http) {
	var url = "http://localhost:80/MyHM/Backend/";
	return {
		getUser: function(userID, email, password) {
			return $http({
				method: "GET",
				url:url+"user",
				data: {userID: userID, email: email, password: password}
			});
		},

		searchForUser: function(searchTerm) {
			return $http({
				url: url+"user",
				method: "GET",
				data: {searchTerm: searchTerm}
			});
		},

		getSchedule: function(userID) {
			return $http({
				method: "GET",
				url:url+"schedule",
				data: {userID: userID}
			});
		},

		getUserMeetings: function(userID) {
			return $http({
				url: url+"meeting",
				method: "GET",
				data: {userID: userID}
			});
		},
		
		getMeeting: function(meetingID) {
			return $http({
				url: url+"meeting",
				method: "GET",
				data: {meetingID: meetingID}
			});
		},

		storeMeeting: function(name, day, index, memberIDs) {
			return $http({
				url: url+"meeting",
				method: "POST",
				data: {name: index, day: day, period: index, memberIDs: memberIDs}
			});
		},

		getUserWithBarcode: function (barcode) {
			return $http({
				url: url+"user",
				method: "GET",
				data: {barcode: barcode}
			});
		}
	};
});