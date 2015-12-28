package backend;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/* Holds all of a User's meta-data
 */
public class UserData implements java.io.Serializable {
    public int userID;
    public String firstName, lastName, email, password, currentLocation;

    UserData() {}

    UserData(int userID, String firstName, String lastName, String email, String password, String currentLocation) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.currentLocation = currentLocation;
    };

    public static UserData userDataFromJSON(JSONObject json) {
        UserData data = new UserData();
        try {
            data.userID = json.getInt("userID");
            data.firstName = json.getString("firstName");
            data.lastName = json.getString("lastName");
            data.email = json.getString("email");
            data.password = json.getString("password");
        } catch (Exception e) {
            return null;
        }

        // Optional parameter
        try {
            data.currentLocation = json.getString("currentLocation");
        } catch (Exception e) { }
        return data;
    }

}