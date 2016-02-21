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
	this.getDay = function(day) {
		return this.periods[day];
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
Schedule.millisUntilPeriod = function(index) {
	var currentIndex = Schedule.currentIndex();
	index = index == null ? 1 : index;
	var newIndex = (currentIndex+index) % 8;
	return 1000*((Schedule.periodTimes[newIndex].hours*60 + Schedule.periodTimes[newIndex].minutes) - (Schedule.periodTimes[currentIndex].hours*60 + Schedule.periodTimes[currentIndex].minutes));
};
Schedule.periodLabel = function(index) {
	return String.fromCharCode(65+index);
};
Schedule.sharedFrees = function(people) {
	if(people.length < 1) return;

	var sharedFrees = [];
	for(var a = 0; a < Schedule.numDays; a++) {
		for(var b = 0; b < Schedule.numPeriods; b++) {
			var inAll = true;
			people.forEach(function(person) {
				if(!person.schedule.getPeriod(day, index).isFree) inAll = false;
			});
			if(inAll) sharedFrees.push(Schedule.periodString(day, index));
		}
	}
	return sharedFrees;
};