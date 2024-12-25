package datastruct.le;

public class prioTask implements Comparable<prioTask>{
    private String code, address;
    private int prioLevel, pubtime, severity;

    public prioTask (String code, String address, int currtime, int severity){
        this.code = code; 
        this.address = address;
        this.pubtime = currtime;
        this.severity = severity; 
        this.prioLevel = calculatePriority(severity, currtime);
    }
    
    public int calculatePriority(int severity, int currentTime){
        int prioLevel;
        int elapsedTime = currentTime - this.pubtime;
        prioLevel = severity + (elapsedTime/1000); // scales by the minute
        return prioLevel;
    }

//------------------------------Getters------------------------------//

    public String getCode() {
        return code;
    }

    public int getPrioLevel() {
        return prioLevel;
    }

    public int getDatetime() {
        return pubtime;
    }

    public int getSeverity() {
        return severity;
    }

    
    public String getAddress() {
        return address;
    }

//------------------------------Setters------------------------------//

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDatetime(int datetime) {
        this.pubtime = datetime;
    }

    public void setPrioLevel(int prioLevel) {
        this.prioLevel = prioLevel;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
    }

//------------------------------Overrides------------------------------//

    @Override
    public int compareTo(prioTask node) {
        return Integer.compare(getPrioLevel(), node.getPrioLevel());
    }

    public String toString(){
        return this.code + " " + this.address + " " + this.pubtime + " " + this.severity + " " + getPrioLevel();
    }
}
