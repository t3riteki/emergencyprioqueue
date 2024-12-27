package datastruct.le;

public class Task implements Comparable<Task>{
    private String code, address;
    private int prioLevel, pubtime, severity, taskID;

    public Task (int taskID, String code, String address, int currtime, int severity){
        this.taskID = taskID;
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
    public int getTaskID() {
        return taskID;
    }

    public String getCode() {
        return code;
    }

    public int getPrioLevel() {
        return prioLevel;
    }

    public int getPubtime() {
        return pubtime;
    }

    public int getSeverity() {
        return severity;
    }

    
    public String getAddress() {
        return address;
    }

//------------------------------Setters------------------------------//
    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setPubtime(int datetime) {
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
    public int compareTo(Task node) {
        return Integer.compare(getPrioLevel(), node.getPrioLevel());
    }

    public String toString(){
        return this.code + " " + this.address + " " + this.pubtime + " " + this.severity + " " + getPrioLevel();
    }
}   
