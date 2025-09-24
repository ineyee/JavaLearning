public class AddressBean {
    public AddressBean() {
    }

    private String city;
    private String district;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Override
    public String toString() {
        return "AddressBean{" +
                "city='" + city + '\'' +
                ", district='" + district + '\'' +
                '}';
    }
}
