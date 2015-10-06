public class Backend
{
    public static UserData getUserData(int userID) {
        UserData data = new UserData();
        data.name = "Michael Truell";
        data.email = "michael_truell@horacemann.org";
        data.password = "password";
        
        return data;
    }
}
