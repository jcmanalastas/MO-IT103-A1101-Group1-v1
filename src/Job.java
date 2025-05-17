public class Job {

    private String position = " ";
    private String supervisor = " ";

    public Job(String position, String supervisor){
        this.position = position;
        this.supervisor = supervisor;
    }

    // Getters and Setters
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }
}