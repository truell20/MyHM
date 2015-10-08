import java.util.Scanner;
import java.io.*;
import java.net.*;
import org.json.*;

public class Backend
{
    public static final String domain = "http://www.truellprojects.com/ApPosition/";
    
    public static String getText(String url) {
        System.out.println("url: " + url);
        try {
            Scanner webScanner = new Scanner(new URL(url).openStream(), "UTF-8");
            String out = webScanner.useDelimiter("\\A").next();
            System.out.println("out : " + out);
            webScanner.close();
        
            return out;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Gets the UserData of the user who has the userID given
    // If the user doesn't exist, a null object is returned
    public static UserData getUserData(int userID) {
        String webContents = getText(domain+"credentials?userID="+userID);
        JSONObject obj = new JSONObject(webContents);
        
        if(obj.getString("email") == "") {
            System.out.println("That user doesn't exist!");
                    return null;
        }
        
        UserData data = new UserData();
        data.email = obj.getString("email");
        data.firstName = obj.getString("firstName");
        data.lastName = obj.getString("lastName");
        data.password = obj.getString("password");
        data.userID = userID;
        
        return data;
    }
    
    /* Returns the userID the user whose email and password match the ones given.
     * If there is no user with that combination of password and username,
     * -1 is returned.
     * This method may be used for signing a user in.
     */
    public static int getUserID(String email, String password) {
        String webContents = getText(domain+"userID?email="+email+"&password="+password);
        JSONObject obj = new JSONObject(webContents);
        String userID = obj.getString("userID");
        
        if(userID != "") {
            return Integer.parseInt(userID);
        }
        
        return -1;
    }
    
    // Gets a day in a user's schedule using their userID and the number of days this day is removed from today
    // If distanceFromToday = 0, then today is returned. If distanceFromToday = 1, tomorrow is returned, and so on
    public static Day getDay(int distanceFromToday, int userID) {
        Day day = new Day();
        for(int a = 0; a < 8; a++) {
            Period english = new Period();
            english.activity = "English";
            day.setPeriod(a, english);
        }
        return day;
    }
    
    public static void main(String[] args) {
        System.out.println("\u000c");
        
        Backend backend = new Backend();
        
        System.out.println("ID: " + backend.getUserData(1).lastName);
    }
}
