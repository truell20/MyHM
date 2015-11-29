package backend;

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
    public static final String domain = "http://10.0.2.2:80/ApPosition/Backend/";
    
    // IGNORE THIS: This is my main function for testing the backend
    public static void mainTest(String[] args) {
        System.out.println("\u000c");

        Backend backend = new Backend();

        System.out.println("ID: " + backend.getUserData(1).lastName);
        System.out.println("ID: " + backend.getUserDataWithSignIn("michael_truell@horacemann.org", "password"));
        System.out.println("ID: " + backend.getDay(0, 1).toString());

        UserData data = new UserData();
        data.userID = 1;
        data.firstName = "Michael";
        data.lastName = "Truell";
        data.email = "michael_truell@horacemann.org";
        data.password = "password";
        data.currentLocation = "Tillinghast";
        backend.setUserData(data, "password");
        
        System.out.println("ID: " + backend.getMeetings("2015-10-21", 1).get(0).name);
    }

    // Gets the UserData of the user who has the userID given
    // If the user doesn't exist, a null object is returned
    public static UserData getUserData(final int userID) {
        HashMap<String,Object> arguments = new HashMap<String,Object>() {{
                    put("userID", userID);
                }};

        try {
            String webContents = queryURL(domain + "credentials", HTTPMethod.GET, arguments);
            JSONObject obj = new JSONObject(webContents);

            if (obj.getString("email") == "") {
                System.out.println("That user doesn't exist!");
                return null;
            }

            UserData data = new UserData();
            data.email = obj.getString("email");
            data.firstName = obj.getString("firstname");
            data.lastName = obj.getString("lastname");
            data.password = obj.getString("password");
            data.userID = userID;

            return data;
        } catch (Exception e) {
            System.out.println("There was a problem getting UserData");
            return null;
        }
    }
    
    // Finds Users with a name that contains the searchTerm specified
    public static ArrayList<UserData> searchForUsers(final String searchTerm) {
        HashMap<String,Object> arguments = new HashMap<String,Object>() {{
                    put("searchTerm", searchTerm);
                }};
        try {
            String webContents = queryURL(domain + "credentials", HTTPMethod.GET, arguments);
            JSONArray arrayOfUsers = new JSONArray(webContents);

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

            return returnList;
        } catch (Exception e) {
            return null;
        }
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

        System.out.println("contents " + queryURL(domain+"credentials", HTTPMethod.PUT, arguments));
    }

    /* Returns the UserData the user whose email and password match the ones given.
     * If there is no user with that combination of password and username,
     * -1 is returned.
     * This method may be used for signing a user in.
     */
    public static UserData getUserDataWithSignIn(final String email, final String password) {
        System.out.println("Start " + email + " " + password);
        HashMap<String,Object> arguments = new HashMap<String,Object>() {{
                    put("email", email);
                    put("password", password);
                }};
       
        try {
            String webContents = queryURL(domain + "credentials", HTTPMethod.GET, arguments);
            JSONObject obj = new JSONObject(webContents);

            if (obj.getString("email") == "") {
                System.out.println("Cannot find");
                System.out.println("That user doesn't exist!");
                return null;
            }

            UserData data = new UserData();
            data.email = email;
            data.firstName = obj.getString("firstname");
            data.lastName = obj.getString("lastname");
            data.password = password;
            data.userID = obj.getInt("userID");

            return data;
        } catch (Exception e) {
            System.out.println("Error");
            e.printStackTrace();
            return null;
        }
    }

    // Gets a day in a user's schedule using their userID and the number of the day (ranging from 0 - Day.numberOfPeriods)
    // If distanceFromToday = 0, then today is returned. If distanceFromToday = 1, tomorrow is returned, and so on
    public static Day getDay(final int dayIndex, final int userID) {
        HashMap<String,Object> arguments = new HashMap<String,Object>() {{
                    put("day", dayIndex);
                    put("userID", userID);
                }};
        try {
            String webContents = queryURL(domain + "classes", HTTPMethod.GET, arguments);
            JSONArray periods = new JSONArray(webContents);
            Day day = new Day();

            for (int a = 0; a < periods.length(); a++) {
                JSONObject period = periods.getJSONObject(a);
                day.setPeriod(period.getInt("period"), new Period(period.getString("name")));
            }

            return day;
        } catch (Exception e) {
            return null;
        }
    }
    
    public static ArrayList<Meeting> getMeetings(final String date, final int userID) {
        HashMap<String,Object> arguments = new HashMap<String,Object>() {{
                    put("date", date);
                    put("userID", userID);
                }};
        try {
            String webContents = queryURL(domain + "meetings", HTTPMethod.GET, arguments);
            JSONArray jsonMeetings = new JSONArray(webContents);

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

            return meetings;
        } catch (Exception e) {
            return null;
        }
    }

    static String urlEncodeUTF8(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    static String urlEncodeUTF8(Map<?,?> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<?,?> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(String.format("%s=%s",
                    urlEncodeUTF8(entry.getKey().toString()),
                    urlEncodeUTF8(entry.getValue().toString())
                ));
        }
        return sb.toString();       
    }

    // Returns the contents of the webpage at the url specified. Uses GET to make the query
    public static String queryURL(String urlText, HTTPMethod method, HashMap<String, Object> arguments) {
        String argumentString = urlEncodeUTF8(arguments);

        // If GET add argumentString to end of URL
        if(method == HTTPMethod.GET) {
            urlText += "?"+argumentString;
        }
        
        System.out.println("url: " + urlText);

        URL url = null;
        try {
            url = new URL(urlText);
        } catch (MalformedURLException exception) {
            exception.printStackTrace();
        }

        HttpURLConnection connection = null;
        DataOutputStream dataOutputStream = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            if(method == HTTPMethod.PUT || method == HTTPMethod.POST) {
                if(method == HTTPMethod.PUT) {
                    connection.setRequestMethod("PUT");
                } else {
                    connection.setRequestMethod("POST");
                }
                
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                dataOutputStream = new DataOutputStream(connection.getOutputStream());
                dataOutputStream.writeBytes(argumentString);
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) response.append(inputLine);
            in.close();
            
            System.out.println("out: " + response.toString());
            
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (dataOutputStream != null) {
                try {
                    dataOutputStream.flush();
                    dataOutputStream.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
