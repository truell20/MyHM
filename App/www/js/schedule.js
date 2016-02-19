Schedule.numDays = 10;
Schedule.numPeriods = 8;

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

Schedule.forPossiblePeriods = function(callback) {
	for(var a = 0; a < Schedule.numDays; a++) {
		for(var b = 0; b < Schedule.numPeriods; b++) {
			callback(a, b);
		}
	}
}

Schedule.periodString = function(day, index) {
	return String.fromCharCode(65+index) + " period, " +  ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday"][day%5];
}

function Person(userID, name, email, password, schedule, friends) {
	this.userID = userID;
	this.name = name;
	this.email = email;
	this.password = password;
	this.schedule = schedule;
	this.friends = friends;
}

Person.randomPerson = function() {
	var names = ["Fred", "Lisa", "Ben", "Josh", "Michael", "Teddy", "Luca", "Stephanie", "Henry", "Danah", "Nick"];
	var periods = [];
	for(var a = 0; a < Schedule.numDays; a++) {
		var day = [];
		for(var b = 0; b < Schedule.numPeriods; b++) {
			if(a == 1 && b == 2) day.push({ name: "Free", isFree: true});
			else day.push({ name: "Art", isFree: false })
		}
		periods.push(day);
	}
	return new Person(Math.floor(Math.random()*names.length), names[Math.floor(Math.random()*names.length)], null, null, new Schedule(periods));
}