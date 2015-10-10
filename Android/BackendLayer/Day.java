import java.util.ArrayList;

public class Day {
    private final int numberOfPeriods = 8;
    
    private ArrayList<Period> periods = new ArrayList<Period>();
    
    // Initializes periods
    public Day() {
        for(int a = 0; a < numberOfPeriods; a++) {
            periods.add(new Period());
        }
    }
    
    // Change the period at the index specified
    // If the index specified < 0 or >= numberOfPeriods, no period is changed
    public void setPeriod(int periodIndex, Period newPeriod) {
        if(periodIndex > -1 && periodIndex < numberOfPeriods) {
            periods.set(periodIndex, newPeriod);
        } else {
            System.out.println("Cannot set that period.\n");
        }
    }
    
    // Get's the day's periods
    public ArrayList<Period> getPeriods() {
        return periods;
    }
    
    // Prints unique information about this day object. For debugging purposes
    public String toString() {
        String returnString = "";
        for(int a = 0; a < periods.size(); a++) {
            returnString += "Period " + a + ": " + periods.get(a).toString() + "\n";
        }
        
        return returnString;
    }
}