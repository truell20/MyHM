# ApPosition

**ApPosition is still under development.** 

ApPosition is a scheduling and location app for the Horace Mann School. ApPosition uses the Ionic Framework and is written in HTML5 and AngularJs. On the bakcend, the app utilizes a RESTful API written in PHP using a MYSQL database running on an Apache Server.

Users see their schedule, can setup meetings with teachers and peers, can share their current location, and can see the locations of others.

ApPosition was made by [Michael Truell](https://github.com/truell20), [Josh Gruenstein](https://github.com/joshuagruenstein), and [Luca Koval](https://github.com/G4Cool).

## To Do

- Write backend in JS
- Implement Home Tab (share current location)
- Implement meetings
  - Creation of meetings fragment
    - Each meeting has a name, a period, and collaborators
    - Notify a user through push notifications when someone creates a meeting with their name
  - Integrate meetings with the backend
  - Display meetings schedule
- Search
  - User can search for other users by name
  - A user may be clicked on and their schedule will show up
  - Allow for the favoriting of users. They show up on the search tab before anything is searched. 
- Future Features
  - Allow for the requesting of another users location. A request would send a push notification to that user.
