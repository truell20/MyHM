var backend = {
	url: "http://localhost:80/MyHM/Backend/",

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

	getBarcode: function (barcode) {
		return $http({
			url: url+"user",
			method: "GET",
			data: {barcode: barcode}
		});
	}
}