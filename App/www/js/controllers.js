angular.module('ApPosition.controllers', [])

.controller('MeetingsCtrl', function($scope) {
	
})

.controller('MakeMeetingCtrl', function($scope) {
	$scope.query = "";
	$scope.teachers = [Person.randomPerson(), Person.randomPerson(), Person.randomPerson()];
	$scope.students = [Person.randomPerson(), Person.randomPerson(), Person.randomPerson()];
	$scope.sharedFrees = function(people) {
		var sharedFrees = [];
		Schedule.forPossiblePeriods(function(day, index) {
			var inAll = true;			people.forEach(function(person) {
				if(!person.schedule.getPeriod(day, index).isFree) inAll = false;
			});
			if(inAll) sharedFrees.push(Schedule.periodString(day, index));
		});
		return sharedFrees;
	}
})

.controller('HomeCtrl', function($scope) {
	console.log(JSON.parse(window.localStorage['user']))
})

.controller('SettingsCtrl', function($scope,$ionicPopup,$location) {
	$scope.data = {
		showDelete: false
	};

	$scope.addFriend = function(friendName) {
		$scope.friends.push({ name: friendName });
        $scope.friendName = "";
    };

	$scope.deleteFriend = function(friend) {
		$scope.friends.splice($scope.friends.indexOf(friend), 1);
	};

	$scope.addFriendPrompt = function() {
	   var promptPopup = $ionicPopup.prompt({
	      title: 'Add Friend',
	      template: "Enter your friend's name."
	   });
	   promptPopup.then(function(res) {
	      if (res) {
	         $scope.addFriend(res);
	      }
	   });
	};

	$scope.logout = function() {
		$location.path('/login');
	};

	$scope.friends = [
		{ name: 'Michael Truell' },
		{ name: 'Luca Koval' },
		{ name: 'Joshua Gruenstein' }
	];
})

.controller('LoginCtrl', function($scope, $cordovaBarcodeScanner,$location) {
	$scope.skipLogin = function() {
		window.localStorage['user'] = JSON.stringify({
			email: "michael_truell@horacemann.org", 
			firstName: "Michael", 
			lastName: "Truell",
			password: "sa3tHJ3/KuYvI"
		});

		$location.path('/tab/home');
	};

	$scope.scanBarcode = function() {
		$cordovaBarcodeScanner.scan().then(function(imageData) {
			if (imageData.cancelled) return;

			alert(imageData.text);
			$location.path('/tab/home');
		}, function(error) {
			alert("An error happened -> " + error);
		});
	};

});