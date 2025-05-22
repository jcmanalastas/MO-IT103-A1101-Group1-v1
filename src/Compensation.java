public class Compensation {

    private double basicSalary;
    private double riceSubsidy;
    private double phoneAllowance;
    private double clothingAllowance;
    private double hourlyRate;
    private double semiGross;

    public Compensation(double basicSalary, double riceSubsidy, double phoneAllowance, double clothingAllowance, double hourlyRate, double semiGross){
        this.basicSalary = basicSalary;
        this.riceSubsidy = riceSubsidy;
        this.phoneAllowance = phoneAllowance;
        this.clothingAllowance = clothingAllowance;
        this.hourlyRate = hourlyRate;
        this.semiGross = semiGross;
    }

    // Getters and Setters

    public double getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(double basicSalary) {
        this.basicSalary = basicSalary;
    }

    public double getRiceSubsidy() {
        return riceSubsidy;
    }

    public void setRiceSubsidy(double riceSubsidy) {
        this.riceSubsidy = riceSubsidy;
    }

    public double getPhoneAllowance() {
        return phoneAllowance;
    }

    public void setPhoneAllowance(double phoneAllowance) {
        this.phoneAllowance = phoneAllowance;
    }

    public double getClothingAllowance() {
        return clothingAllowance;
    }

    public void setClothingAllowance(double clothingAllowance) {
        this.clothingAllowance = clothingAllowance;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public double getSemiGross() {
        return semiGross;
    }

    public void setSemiGross(double semiGross) {
        this.semiGross = semiGross;
    }

    public double getMonthlyGrossSalary() {
        return basicSalary + riceSubsidy + phoneAllowance + clothingAllowance;
    }

    public double calculateHourlyRate(){
        double annualSalary = getMonthlyGrossSalary()* 12;
        return annualSalary/ 1760;

    }
}
