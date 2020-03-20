package Model;

public class Refund_model {
    public Refund_model(String id, String customer_id, String store_id, String order_id, String amount, String date, String store_name, String status, String total_amount) {
        this.id = id;
        this.customer_id = customer_id;
        this.store_id = store_id;
        this.order_id = order_id;
        this.amount = amount;
        this.date = date;
        this.store_name = store_name;
        this.status = status;
        this.total_amount = total_amount;
    }

    public Refund_model() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    String      id;
    String   customer_id;
    String   store_id;
    String   order_id;
    String   amount;
    String   date;
    String   store_name;
    String   status;
    String   total_amount;
}
