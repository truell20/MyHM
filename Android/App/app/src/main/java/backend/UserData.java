package backend;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/* Holds all of a User's meta-data
 */
public class UserData implements java.io.Serializable {
    public int userID;
    public String firstName, lastName, email, password, currentLocation;

}