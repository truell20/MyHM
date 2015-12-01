package backend;

import org.json.JSONObject;

import java.util.ArrayList;

public class Meeting {
    public String name;
    public int meetingID, dayIndex, periodIndex;
    public ArrayList<Integer> memberIDs;
    
    public Meeting() {
        memberIDs = new ArrayList<Integer>();
    }

    public static Meeting meetingFromJSON(JSONObject json) {
        Meeting meeting = new Meeting();
        try {
            meeting.meetingID = json.getInt("meetingID");
            meeting.dayIndex = json.getInt("dayIndex");
            meeting.periodIndex = json.getInt("periodIndex");
            meeting.name = json.getString("name");
            return meeting;
        } catch (Exception e) {
            return null;
        }
    }
}