var user = Person.randomPerson();

angular.module('ApPosition.controllers', ["ApPosition.services"])

.controller('MeetingsCtrl', function($scope) {
	$scope.user = user;
	$scope.peopleString = function(people) {
		return people.filter(function(p){return p != $scope.user;}).map(function(p){return p.name;}).join(", ");
	};
	$scope.periodString = function(day, index) {
		console.log(day + ", " + index)
		return Schedule.periodLabel(index) + " period, " +  ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday"][day%5] + ", Day " + (day+1);
	};

	$scope.acceptMeeting = function() {
		$ionicPopup.alert({
			title: 'Meeting scheduled.',
		});
		$location.path('/tab/home')
	};
})

.controller('MakeMeetingCtrl', function($scope, $ionicPopup,$location) {
	$scope.query = "";
	$scope.headings = [{name: "Teachers", people: [Person.sadPerson(), Person.sadPerson(), Person.sadPerson()]}, {name: "Students", people: user.friends}];
	
	$scope.checkedPeople = function() {
		var checkedPeople = [];
		$scope.headings.forEach(function(header) {
			header.people.forEach(function(person) {
				if(person.isChecked) checkedPeople.push(person);
			});
		});
		return checkedPeople;
	};

	$scope.makeMeeting = function() {
		$ionicPopup.alert({
			title: 'Meeting requested.',
			template: 'A meeting request has been sent to the meeting participants.'
		});
		$location.path('/tab/home')
	}


})

.controller('HomeCtrl', function($scope, $localStorage) {
	$scope.user = user;
	console.log("User");
	console.log($scope.user);
	function getFreeFriends() {
		return $scope.user.getFreeFriends(Schedule.currentDay(), Schedule.currentIndex());;
	}

	$scope.freeFriends = getFreeFriends();

	// Setup a timer that is called every period, so that we can update free friends
	function setupInterval(callback) {
		var interval = window.setInterval(function() {
			clearInterval(interval);
			callback();
		}, Schedule.millisUntilPeriod(), 1);
	}
	setupInterval(getFreeFriends);

	// Build the alerts
	var currentDay = 7;

	var periodString = $scope.user.schedule.getDay(currentDay).map(function(period, index) { 
		period.index = index; 
		return period; 
	}).filter(function(period) { 
		return period.isFree; 
	}).map(function(period) { 
		return Schedule.periodLabel(period.index)
	}).join(", ");

	$scope.alerts = ["It is Day " + currentDay + ". You are free " + periodString + "."];
	$scope.alerts = $scope.alerts.concat($scope.user.meetings.filter(function(meeting) {
		return meeting.day != -1;
	}).map(function(meeting) {
		return "You are meeting with "+meeting.people.filter(function(p){return p != $scope.user}).map(function(p){return p.name}).join(", ")+" at period "+Schedule.periodLabel(meeting.index)+".";
	}));
	console.log($scope.alerts)
})

.controller('SettingsCtrl', function($scope, $ionicPopup, $location, $localStorage) {
	$scope.data = {
		showDelete: false
	};

	$scope.addFriend = function(friendName) {
		function lookupUser(name) {
			function levenshtein(a, b) {
				var matrix = [], i, j;
				for (i = 0; i <= b.length; i++) matrix[i] = [i];
				for (j = 0; j <= a.length; j++) matrix[0][j] = j;
				for (i = 1; i <= b.length; i++){
					for (j = 1; j <= a.length; j++){
						if (b.charAt(i-1) == a.charAt(j-1)) matrix[i][j] = matrix[i-1][j-1];
						else matrix[i][j] =  Math.min(matrix[i-1][j-1] + 1,
											 Math.min(matrix[i][j-1] + 1,
											 matrix[i-1][j] + 1));
					}
				} return matrix[b.length][a.length];
			}

			var people = $localStorage.getPeople();
			var minPerson = null;
			var minProb = 99999;
			for (var p=0; p<people.length; p++) {
				var prob = levenshtein(people[p].name,name)
				if (prob < minProb) {
					minProb = prob;
					minPerson = people[p];
				}
			} return minPerson;
		}

		var friend = lookupUser(friendName);
		
		if(friend != null) {
			$localStorage.addFriend(friend);
			$scope.friends.push(friend);

			$scope.friendName = "";
		}
	};

	$scope.deleteFriend = function(friend) {
		$scope.friends.splice($scope.friends.indexOf(friend), 1);
		$localStorage.removeFriend(friend);
	};

	$scope.addFriendPrompt = function() {
		var promptPopup = $ionicPopup.prompt({
			title: 'Add Friend',
			template: "Enter your friend's name."
		});
		promptPopup.then(function(res) {
			if (res) $scope.addFriend(res);
		});
	};

	$scope.logout = function() {
		$location.path('/login');
	};

	$scope.friends = $localStorage.getFriends();
})

.controller('LoginCtrl', function($scope, $cordovaBarcodeScanner,$location,$localStorage) {

	$scope.skipLogin = function() {
		$location.path('/tab/home');
	};

	$scope.scanBarcode = function() {
		$cordovaBarcodeScanner.scan().then(function(imageData) {
			if (imageData.cancelled) return;

			$location.path('/tab/home');
		}, function(error) {
			alert("An error happened -> " + error);
		});
	};

});