public class Period {
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