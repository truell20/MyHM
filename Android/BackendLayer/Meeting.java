import java.util.ArrayList;

public class Meeting {
    String name, beginningDateTime, endDateTime;
    ArrayList<Integer> memberIDs;
    
    public Meeting() {
        memberIDs = new ArrayList<Integer>();
    }
}