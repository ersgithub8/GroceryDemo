package Model;

public class Sub_Categories {
    public Sub_Categories(String id, String title, String slug, String parent, String leval, String description, String arb_title, String image, String status, String image2, String image2_status, String master_cat) {
        this.id = id;
        this.title = title;
        this.slug = slug;
        this.parent = parent;
        this.leval = leval;
        this.description = description;
        this.arb_title = arb_title;
        this.image = image;
        this.status = status;
        this.image2 = image2;
        this.image2_status = image2_status;
        this.master_cat = master_cat;
    }

    public Sub_Categories() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getLeval() {
        return leval;
    }

    public void setLeval(String leval) {
        this.leval = leval;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArb_title() {
        return arb_title;
    }

    public void setArb_title(String arb_title) {
        this.arb_title = arb_title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage2_status() {
        return image2_status;
    }

    public void setImage2_status(String image2_status) {
        this.image2_status = image2_status;
    }

    public String getMaster_cat() {
        return master_cat;
    }

    public void setMaster_cat(String master_cat) {
        this.master_cat = master_cat;
    }

    String id;
    String title;
    String slug;

    String parent;
    String leval;
    String description;
    String arb_title;
    String image;
    String status;

    String image2;
    String image2_status;
    String master_cat;

}
