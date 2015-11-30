package backend;

/* A period in a schedule.
 * Each period has a name.
 */
public class Period {
    public static final int numberOfPeriods = 8;

    public String name;
    
    public Period() {
        name = null;
    }
    
    public Period(String name_) {
        name = name_;
    }
    
    public String toString() {
        return "name: " + name;
    }
}