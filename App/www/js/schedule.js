Schedule.numDays = 10;
Schedule.numPeriods = 8;
Schedule.startingDate = new Date("2015-09-14");
Schedule.periodTimes = [{hours: 8, minutes: 20}, {hours: 9, minutes: 10}, {hours: 10, minutes: 0}, {hours: 11, minutes: 5}, {hours: 11, minutes: 55}, {hours: 12, minutes: 45}, {hours: 1, minutes: 35}, {hours: 2, minutes: 25}];

function Schedule(periods) {
	// Check that periods is formatted correctly
	if(periods.length != Schedule.numDays) {
		throw "Wrong number of days";
	}
	periods.forEach(function(day) { if(day.length != Schedule.numPeriods) throw "Wrong number of period in a day"; });

	this.periods = periods;
	this.setPeriod = function(day, index, period) {
		if(day > -1 && day < Schedule.numDays && index > -1 && index < schedule.numPeriods) this.periods[day][index] = period;
		else throw "Invalid day, index combo: " + day + ", " + index;
	}
	this.getPeriod = function(day, index) {
		return this.periods[day][index];
	}
}

Schedule.currentDay = function() {
	var today = new Date();
	if(today.getDay() == 6 || today.getDay() == 0) return -1;
	else {
		var twoWeekRange = Math.floor((today.getTime() - Schedule.startingDate.getTime())/(1000*60*60*24)) % 14;
		return twoWeekRange > 6 ? twoWeekRange - 2 : twoWeekRange;
	}
};
Schedule.currentIndex = function() {
	var totalMinutes = new Date().getHours()*60 + new Date().getMinutes();
	var index = Schedule.periodTimes.length-1;
	Schedule.periodTimes.every(function(time, index) {
		if(time.hours*60 + time.minutes > totalMinutes) {
			return false;
		}
	})
	return index;
};
Schedule.millisUntilNextPeriod = function() {
	var currentIndex = Schedule.currentIndex();
	return 1000*((Schedule.periodTimes[currentIndex+1].hour*60 + Schedule.periodTimes[currentIndex+1].minute) - (Schedule.periodTimes[currentIndex].hour*60 + Schedule.periodTimes[currentIndex].minute));
}
Schedule.periodString = function(day, index) {
	return String.fromCharCode(65+index) + " period, " +  ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday"][day%5] + ", day " + day;
};
Schedule.forPossiblePeriods = function(callback) {
	for(var a = 0; a < Schedule.numDays; a++) for(var b = 0; b < Schedule.numPeriods; b++) callback(a, b);
};
Schedule.sharedFrees = function(people) {
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


function Person(userID, name, email, password, schedule, friends) {
	this.userID = userID;
	this.name = name;
	this.email = email;
	this.password = password;
	this.schedule = schedule;
	this.friends = friends;

	this.getFreeFriends = function(day, index) {
		console.log(this.friends)
		return this.friends.filter(function(friend) {
			return friend.isFree;
		});
	}
}

var names = ["Fred", "Lisa", "Ben", "Josh", "Michael", "Teddy", "Luca", "Stephanie", "Henry", "Danah", "Nick"];
Person.randomPerson = function() {
	if(names.length < 1) return null;
	console.log(names.length);
	var periods = [];
	for(var a = 0; a < Schedule.numDays; a++) {
		var day = [];
		for(var b = 0; b < Schedule.numPeriods; b++) {
			if(a == 1 && b == 2) day.push({ name: "Free", isFree: true});
			else day.push({ name: "Art", isFree: false })
		}
		periods.push(day);
	}
	var nameIndex = Math.floor(Math.random()*names.length);
	var name = names[nameIndex];
	names.splice(nameIndex, 1);

	return new Person(Math.floor(Math.random()*names.length), name, null, null, new Schedule(periods), [Person.randomPerson()]);
}