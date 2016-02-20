angular.module('ApPosition.controllers', [])

.controller('MeetingsCtrl', function($scope) {
	
})

.controller('MakeMeetingCtrl', function($scope) {
	$scope.query = "";
	$scope.headings = [{name: "Teachers", people: [Person.randomPerson(), Person.randomPerson(), Person.randomPerson()]}, {name: "Students", people: [Person.randomPerson(), Person.randomPerson(), Person.randomPerson()]}];
	
	$scope.checkedPeople = function() {
		var checkedPeople = [];
		$scope.headings.forEach(function(header) {
			header.people.forEach(function(person) {
				if(person.isChecked) checkedPeople.push(person);
			});
		});
		return checkedPeople;
	};

	$scope.sharedFrees = function(people) {
		if(people.length < 1) return;

		var sharedFrees = [];
		Schedule.forPossiblePeriods(function(day, index) {
			var inAll = true;
			people.forEach(function(person) {
				if(!person.schedule.getPeriod(day, index).isFree) inAll = false;
			});
			if(inAll) sharedFrees.push(Schedule.periodString(day, index));
		});
		return sharedFrees;
	};
})

.controller('HomeCtrl', function($scope) {

})

.controller('SettingsCtrl', function($scope,$ionicPopup,$location) {
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

			var people = local.getPeople();
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
			local.addFriend(friend);
			$scope.friends.push(friend);

			$scope.friendName = "";
		}
	};

	$scope.deleteFriend = function(friend) {
		$scope.friends.splice($scope.friends.indexOf(friend), 1);
		local.removeFriend(friend);
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

	$scope.friends = local.getFriends();
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