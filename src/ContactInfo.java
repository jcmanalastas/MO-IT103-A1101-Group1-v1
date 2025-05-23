public class ContactInfo {

    private String address = " ";
    private String phone = " ";

    public ContactInfo(String address, String phone){
        this.address = address;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}