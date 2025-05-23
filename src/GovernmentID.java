public class GovernmentID {

    private String sss = " ";
    private String philhealth = " ";
    private String pagibig = " ";
    private String tin = " ";

    public GovernmentID(String sss, String philhealth, String pagibig, String tin){
        this.sss = sss;
        this.philhealth = philhealth;
        this.pagibig = pagibig;
        this.tin = tin;
    }

    //Getters and Setters

    public String getSss() {
        return sss;
    }

    public void setSss(String sss) {
        this.sss = sss;
    }

    public String getPhilhealth() {
        return philhealth;
    }

    public void setPhilhealth(String philhealth) {
        this.philhealth = philhealth;
    }

    public String getPagibig() {
        return pagibig;
    }

    public void setPagibig(String pagibig) {
        this.pagibig = pagibig;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }
}
