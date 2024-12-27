package datastruct.le;

public final class Task implements Comparable<Task>{
    private String code, address, resources;
    private int prioLevel, pubtime, severity, taskID;

    public Task (int taskID, String code, String address, int currtime, int severity){
        this.taskID = taskID;
        this.code = code; 
        this.address = address;
        this.pubtime = currtime;
        this.severity = severity; 
        this.prioLevel = calculatePriority(severity, pubtime);
    }

    public Task (Task copy){
        this.taskID = copy.taskID;
        this.code = copy.code; 
        this.address = copy.address;
        this.pubtime = copy.pubtime;
        this.severity = copy.severity; 
        this.prioLevel = calculatePriority(severity, pubtime);
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

    public String getResources() {
        return resources;
    }
    
    public String getAddress() {
        return address;
    }

//------------------------------Setters------------------------------//
    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public void setResources(String resources) {
        this.resources = resources;
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
    
    @Override
    public String toString(){
        return  this.taskID + " " + this.code + " " + this.address + " " + this.pubtime + " " + this.severity + " " + getPrioLevel();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return taskID == task.taskID; 
    }
}   
