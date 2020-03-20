
package Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OurProductModel {

    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_arb_name")
    @Expose
    private String productArbName;
    @SerializedName("product_description")
    @Expose
    private String productDescription;
    @SerializedName("product_arb_description")
    @Expose
    private String productArbDescription;
    @SerializedName("product_image")
    @Expose
    private String productImage;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("in_stock")
    @Expose
    private String inStock;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("mrp")
    @Expose
    private String mrp;
    @SerializedName("unit_value")
    @Expose
    private String unitValue;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("arb_unit")
    @Expose
    private Object arbUnit;
    @SerializedName("increament")
    @Expose
    private String increament;
    @SerializedName("rewards")
    @Expose
    private String rewards;
    @SerializedName("tax")
    @Expose
    private String tax;
    @SerializedName("storeid")
    @Expose
    private String storeid;
    @SerializedName("storeName")
    @Expose
    private String storeName;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductArbName() {
        return productArbName;
    }

    public void setProductArbName(String productArbName) {
        this.productArbName = productArbName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductArbDescription() {
        return productArbDescription;
    }

    public void setProductArbDescription(String productArbDescription) {
        this.productArbDescription = productArbDescription;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getInStock() {
        return inStock;
    }

    public void setInStock(String inStock) {
        this.inStock = inStock;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getUnitValue() {
        return unitValue;
    }

    public void setUnitValue(String unitValue) {
        this.unitValue = unitValue;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Object getArbUnit() {
        return arbUnit;
    }

    public void setArbUnit(Object arbUnit) {
        this.arbUnit = arbUnit;
    }

    public String getIncreament() {
        return increament;
    }

    public void setIncreament(String increament) {
        this.increament = increament;
    }

    public String getRewards() {
        return rewards;
    }

    public void setRewards(String rewards) {
        this.rewards = rewards;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getStoreid() {
        return storeid;
    }

    public void setStoreid(String storeid) {
        this.storeid = storeid;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

}
