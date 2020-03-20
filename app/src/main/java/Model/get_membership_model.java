package Model;

public class get_membership_model {
    public get_membership_model() {
    }

    public get_membership_model(String id, String membership, String type, String amount, String status, String discount) {
        this.id = id;
        this.membership = membership;
        this.type = type;
        this.amount = amount;
        this.status = status;
        this.discount = discount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMembership() {
        return membership;
    }

    public void setMembership(String membership) {
        this.membership = membership;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    String  id;
    String   membership;
    String  type;
    String   amount;
    String   status;
    String  discount;
}
