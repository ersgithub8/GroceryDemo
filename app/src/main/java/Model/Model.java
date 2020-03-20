package Model;

import android.widget.ImageView;
import android.widget.TextView;

public class Model {


    TextView user_fullname ;
    TextView user_id;
    ImageView user_image;

    public Model() {
    }

    public Model(TextView user_fullname, TextView user_id, ImageView user_image) {
        this.user_fullname = user_fullname;
        this.user_id = user_id;
        this.user_image = user_image;
    }

    public TextView getUser_fullname() {
        return user_fullname;
    }

    public void setUser_fullname(TextView user_fullname) {
        this.user_fullname = user_fullname;
    }

    public TextView getUser_id() {
        return user_id;
    }

    public void setUser_id(TextView user_id) {
        this.user_id = user_id;
    }

    public ImageView getUser_image() {
        return user_image;
    }

    public void setUser_image(ImageView user_image) {
        this.user_image = user_image;
    }
}
