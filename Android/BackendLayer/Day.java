import java.util.ArrayList;

public class Day {
    private ArrayList<Period> periods = new ArrayList<Period>(8);
    
    public Day() {
        for(int a = 0; a < 8; a++) {
            periods.set(a, new Period());
        }
    }
    
    public void setPeriod(int periodIndex, Period newPeriod) {
        if(periodIndex > -1 && periodIndex < 8) {
            periods.set(periodIndex, newPeriod);
        } else {
            System.out.println("Cannot set that period.\n");
        }
    }
    
    public ArrayList<Period> getPeriods() {
        return periods;
    }
}