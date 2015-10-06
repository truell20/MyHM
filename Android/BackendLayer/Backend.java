public class Backend
{
    // Gets the UserData of the user who has the userID given
    public static UserData getUserData(int userID) {
        UserData data = new UserData();
        data.name = "Michael Truell";
        data.email = "michael_truell@horacemann.org";
        data.password = "password";
        
        return data;
    }
    
    /* Returns the userID the user whose email and password match the ones given.
     * If there is no user with that combination of password and username,
     * -1 is returned.
     * This method may be used for signing a user in.
     */
    public static int getUserID(String email, String password) {
        if(email == "michael_truell@horacemann.org" && password == "password") {
            return 1;
        } else {
            return -1;
        }
    }
    
    // Gets the schedule of a user using there userID
    public static Day getSchedule(int userID) {
        Day day = new Day();
        for(int a = 0; a < 8; a++) {
            Period english = new Period();
            english.activity = "English";
            day.setPeriod(a, english);
        }
        return day;
    }
}
