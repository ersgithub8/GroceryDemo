package Model;

public class Area {
    String id;
    String area;
    String pincode;

    public Area(String id, String area, String pincode, String city_id) {
        this.id = id;
        this.area = area;
        this.pincode = pincode;
        this.city_id = city_id;
    }

    public Area() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    String city_id;
}
