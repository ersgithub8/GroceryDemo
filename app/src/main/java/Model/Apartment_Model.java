package Model;

public class Apartment_Model {
    String id;
    String city_id;
    String area_id;
    String apartment_name;

    public Apartment_Model(String id, String city_id, String area_id, String apartment_name) {
        this.id = id;
        this.city_id = city_id;
        this.area_id = area_id;
        this.apartment_name = apartment_name;
    }

    public Apartment_Model() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getApartment_name() {
        return apartment_name;
    }

    public void setApartment_name(String apartment_name) {
        this.apartment_name = apartment_name;
    }
}
