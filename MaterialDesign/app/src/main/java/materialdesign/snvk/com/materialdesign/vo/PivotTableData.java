package materialdesign.snvk.com.materialdesign.vo;

/**
 * Created by Venkata on 3/28/2018.
 */

public class PivotTableData {
    private int id;
    public String identifier;
    public float latitude;
    public float longitude;
    public String name;
    public String brand;
    public String address;
    public int zipcode;
    public String city;
    public String district;
    public String state;

    public PivotTableData(String identifier, float latitude, float longitude, String name, String brand, String address, int zipcode, String city, String district, String state){
        this.identifier = identifier;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.brand = brand;
        this.address = address;
        this.zipcode = zipcode;
        this.city = city;
        this.district = district;
        this.state = state;
    }

    public PivotTableData(int id, String identifier, float latitude, float longitude, String name, String brand, String address, int zipcode, String city, String district, String state){
        this.id = id;
        this.identifier = identifier;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.brand = brand;
        this.address = address;
        this.zipcode = zipcode;
        this.city = city;
        this.district = district;
        this.state = state;
    }

    public PivotTableData(int i, String string) {

    }

    public PivotTableData() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.name = brand;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
