function Meeting(day, index, people) {
	this.day = day;
	this.index = index;
	this.people = people;
}

Meeting.randomMeeting = function(user) {
	return new Meeting(Math.floor(Math.random()*Schedule.numDays), Math.floor(Math.random()*Schedule.numPeriods), [user, Person.randomPerson()]);
}