package Model;

/**
 * Created by Rajesh Dabhi on 6/7/2017.
 */

public class Delivery_address_model {

    String location_id;
    String user_id;
    String socity_id;
    String house_no;
    String receiver_name;
    String receiver_mobile;
    String socity_name;
    String pincode;
    String address;
    String delivery_charges;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getApartment_name() {
        return apartment_name;
    }

    public void setApartment_name(String apartment_name) {
        this.apartment_name = apartment_name;
    }

    public String getFree_delivery_order_value() {
        return free_delivery_order_value;
    }

    public void setFree_delivery_order_value(String free_delivery_order_value) {
        this.free_delivery_order_value = free_delivery_order_value;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getApartment_id() {
        return apartment_id;
    }

    public void setApartment_id(String apartment_id) {
        this.apartment_id = apartment_id;
    }

    String area;
     String apartment_name;
     String free_delivery_order_value;
     String area_id;
     String apartment_id;
    boolean ischeckd;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation_id(){
        return location_id;
    }

    public String getUser_id(){
        return user_id;
    }

    public String getSocity_id(){
        return socity_id;
    }

    public String getHouse_no(){
        return house_no;
    }

    public String getReceiver_name(){
        return receiver_name;
    }

    public String getReceiver_mobile(){
        return receiver_mobile;
    }

    public String getSocity_name(){
        return socity_name;
    }

    public String getPincode(){
        return pincode;
    }



    public String getDelivery_charge(){
        return delivery_charges;
    }

    public boolean getIscheckd(){
        return ischeckd;
    }

    public void setIscheckd(boolean ischeckd){
        this.ischeckd = ischeckd;
    }

}
