package Model;

/**
 * Created by Aws on 11/03/2018.
 */

public class Anime {



    String user_fullname ,user_image;
      String user_id,type;


    public Anime(String user_fullname, String user_image, String user_id) {
        this.user_fullname = user_fullname;
        this.user_image = user_image;
        this.user_id = user_id;

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser_fullname() {
        return user_fullname;
    }

    public void setUser_fullname(String user_fullname) {
        this.user_fullname = user_fullname;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }



    public Anime() {
    }

}
