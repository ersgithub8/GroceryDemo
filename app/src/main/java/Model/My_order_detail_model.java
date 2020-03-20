package Model;

/**
 * Created by Rajesh Dabhi on 30/6/2017.
 */

public class My_order_detail_model {

    String sale_item_id;
    String sale_id;
    String product_id;
    String product_name;
    String qty;
    String unitt;
    String unit_value;
    String pricce;
    String qty_in_kg;
    String product_image;

    public String getSale_item_id(){
        return sale_item_id;
    }

    public String getSale_id(){
        return sale_id;
    }

    public String getProduct_id(){
        return product_id;
    }

    public String getProduct_name(){
        return product_name;
    }

    public String getQty(){
        return qty;
    }

    public String getUnit(){
        return unitt;
    }

    public String getUnit_value(){
        return unit_value;
    }

    public String getPrice(){
        return pricce;
    }

    public String getQty_in_kg(){
        return qty_in_kg;
    }

    public String getProduct_image(){
        return product_image;
    }

}
