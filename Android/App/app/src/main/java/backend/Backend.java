package backend;

import android.app.DownloadManager;
import android.os.AsyncTask;

import java.util.Scanner;
import java.io.*;
import java.net.*;
import org.json.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

enum HTTPMethod {
    GET, POST, PUT;
}

// The class containing the functions to comunicate with our RESTful backend
public class Backend
{

    public static final String domain = "http://truellprojects.com/ApPosition/";
    
    // IGNORE THIS: This is my main function for testing the backend
    public static void mainTest(String[] args) {
        System.out.println("\u000c");

        Backend.getUserDataWithSignIn("michael_truell@horacemann.org", "password", new BackendCallback<UserData>() {
            @Override
            public void callback(UserData result) {
                System.out.println(result.userID);
                System.out.println(result.firstName);
                System.out.println(result.lastName);
            }
        });
    }

    // Gets the UserData of the user who has the userID given
    // If the user doesn't exist, a null object is returned
    public static void getUserData(final int userID, final BackendCallback<UserData> callback) {
        HashMap<String,Object> arguments = new HashMap<String,Object>() {{
                    put("userID", userID);
                }};

        new QueryURLTask(new QueryURLCallback() {
            @Override
            public void onFinish(String result) {
                try {
                    JSONObject obj = new JSONObject(result);

                    if (obj.getString("email") == "") {
                        System.out.println("That user doesn't exist!");
                        callback.callback(null);
                    }

                    UserData data = new UserData();
                    data.email = obj.getString("email");
                    data.firstName = obj.getString("firstname");
                    data.lastName = obj.getString("lastname");
                    data.password = obj.getString("password");
                    data.userID = userID;

                    callback.callback(data);
                } catch (Exception e) {
                    System.out.println("There was a problem getting UserData");
                    callback.callback(null);
                }
            }
        }).execute(new QueryURLParams(domain + "credentials", HTTPMethod.GET, arguments));


    }
    
    // Finds Users with a name that contains the searchTerm specified
    public static void searchForUsers(final String searchTerm, final BackendCallback<ArrayList<UserData>> callback) {
        HashMap<String,Object> arguments = new HashMap<String,Object>() {{
                    put("searchTerm", searchTerm);
                }};
        new QueryURLTask(new QueryURLCallback() {
            @Override
            public void onFinish(String result) {
                try {
                    JSONArray arrayOfUsers = new JSONArray(result);

                    ArrayList<UserData> returnList = new ArrayList<UserData>();

                    for (int a = 0; a < arrayOfUsers.length(); a++) {
                        JSONObject user = arrayOfUsers.getJSONObject(a);

                        UserData data = new UserData();
                        data.email = user.getString("email");
                        data.firstName = user.getString("firstname");
                        data.lastName = user.getString("lastname");
                        data.userID = user.getInt("userID");

                        returnList.add(data);
                    }

                    callback.callback(returnList);
                } catch (Exception e) {
                    callback.callback(null);
                }
            }
        }).execute(new QueryURLParams(domain + "credentials", HTTPMethod.GET, arguments));

    }

    // Sets the UserData of the user who has the userID given
    public static void setUserData(final UserData userData, final String password) {
        HashMap<String,Object> arguments = new HashMap<String,Object>() {{
                    put("userID", userData.userID);
                    put("firstname", userData.firstName);
                    put("lastname", userData.lastName);
                    put("email", userData.email);
                    put("password", userData.password);
                    put("currentLocation", userData.currentLocation);
                    
                    put("key", userData.password);
                }};

        new QueryURLTask(new QueryURLCallback() {
            @Override
            public void onFinish(String result) { System.out.println(result); }
        }).execute(new QueryURLParams(domain + "credentials", HTTPMethod.PUT, arguments));
    }

    /* Returns the UserData the user whose email and password match the ones given.
     * If there is no user with that combination of password and username,
     * -1 is returned.
     * This method may be used for signing a user in.
     */
    public static void getUserDataWithSignIn(final String email, final String password, final BackendCallback<UserData> callback) {
        System.out.println("Start " + email + " " + password);
        HashMap<String,Object> arguments = new HashMap<String,Object>() {{
                    put("email", email);
                    put("password", password);
                }};
       
        new QueryURLTask(new QueryURLCallback() {
            @Override
            public void onFinish(String result) {
                System.out.println("Started first callback");
                try {
                    JSONObject obj = new JSONObject(result);

                    if (obj.getString("email") == "") {
                        System.out.println("Cannot find");
                        System.out.println("That user doesn't exist!");
                        callback.callback(null);
                    }

                    UserData data = new UserData();
                    data.email = email;
                    data.firstName = obj.getString("firstname");
                    data.lastName = obj.getString("lastname");
                    data.password = password;
                    data.userID = obj.getInt("userID");

                    callback.callback(data);
                } catch (Exception e) {
                    System.out.println("Error");
                    e.printStackTrace();
                    callback.callback(null);
                }
            }
        }).execute(new QueryURLParams(domain + "credentials", HTTPMethod.GET, arguments));
    }

    // Gets a day in a user's schedule using their userID and the number of the day (ranging from 0 - Day.numberOfPeriods)
    // If distanceFromToday = 0, then today is returned. If distanceFromToday = 1, tomorrow is returned, and so on
    public static void getDay(final int dayIndex, final int userID, final BackendCallback<Day> callback) {
        HashMap<String,Object> arguments = new HashMap<String,Object>() {{
                    put("day", dayIndex);
                    put("userID", userID);
                }};
        new QueryURLTask(new QueryURLCallback() {
            @Override
            public void onFinish(String result) {
                try {
                    JSONArray periods = new JSONArray(result);
                    Day day = new Day();

                    for (int a = 0; a < periods.length(); a++) {
                        JSONObject period = periods.getJSONObject(a);
                        day.setPeriod(period.getInt("period"), new Period(period.getString("name")));
                    }

                    callback.callback(day);
                } catch (Exception e) {
                    callback.callback(null);
                }
            }
        }).execute(new QueryURLParams(domain + "classes", HTTPMethod.GET, arguments));
    }
    
    public static void getMeetings(final String date, final int userID, final BackendCallback<ArrayList<Meeting>> callback) {
        HashMap<String,Object> arguments = new HashMap<String,Object>() {{
                    put("date", date);
                    put("userID", userID);
                }};
        new QueryURLTask(new QueryURLCallback() {
            @Override
            public void onFinish(String result) {
                try {
                    JSONArray jsonMeetings = new JSONArray(result);

                    ArrayList<Meeting> meetings = new ArrayList<Meeting>();

                    for (int a = 0; a < jsonMeetings.length(); a++) {
                        JSONObject jsonMeeting = jsonMeetings.getJSONObject(a);

                        Meeting meeting = new Meeting();
                        meeting.name = jsonMeeting.getString("name");
                        meeting.beginningDateTime = jsonMeeting.getString("beginning");
                        meeting.endDateTime = jsonMeeting.getString("ending");
                        JSONArray memberIDs = jsonMeeting.getJSONArray("members");
                        for (int b = 0; b < memberIDs.length(); b++) {
                            meeting.memberIDs.add(memberIDs.getInt(b));
                        }

                        meetings.add(meeting);
                    }

                    callback.callback(meetings);
                } catch (Exception e) {
                    callback.callback(null);
                }
            }
        }).execute(new QueryURLParams(domain + "meetings", HTTPMethod.GET, arguments));
    }

}
