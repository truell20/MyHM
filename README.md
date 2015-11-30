# ApPosition

A scheduling and location app for the Horace Mann School. Includes an Android app, a Java API wrapper, and a RESTful API. Work will begin on an iOS app when the Android app is completed. 

Users see their school schedule, can setup meetings with teachers and peers, can share their current location, and can see the locations of others.

## TODO
### Due next Tuesday, 12-01-15 (ACTUALLY)

- Android
  - ~~Integrate the backend with the Sign in tab~~
    - ~~Check to see that the person is a current user~~
  - ~~Integrate backend with Home Tab~~
    - ~~Display current location~~
    - ~~Upload new current location~~s
  - Continue Personal Schedule Tab
    - ~~Allow for the displaying of multiple days~~
    - ~~Sliding back and forth~~
    - Integrate the backend
      - Label periods with class name or "Free"
  - Implement meetings
    - Creation of meetings fragment
      - Each meeting has a name, a period, and collaborators
      - Notify a user through push notifications when someone creates a meeting with their name
    - Integrate meetings with the backend
    - Display meetings on the personal schedule tab
  - Search tab
    - Build a fragment that allows the user to search for other users by name
    - A user may be clicked on and their schedule will show up
    - A user may be favorited d by clicking on a star on the action bar of the user's schedule fragment 

### Future Features

- Allow for the requesting of another users location. A request would send a push notification to that user.
