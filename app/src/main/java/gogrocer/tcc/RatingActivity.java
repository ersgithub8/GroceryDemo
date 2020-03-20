package gogrocer.tcc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.my_last_order_adapter;
import Config.BaseURL;
import Model.My_Pending_order_model;
import util.CustomVolleyJsonRequest;
import util.Session_management;

public class RatingActivity extends AppCompatActivity {
    EditText description_st,description_db;
    Button submit;
    RatingBar ratingBar_st,ratingBar_db;
    Float rate_st,rate_db;
    String desc_st,desc_db,store_id,db_id,user_id,sale_id;
    TextView ratingtv_st,ratingtv_db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
    description_st=findViewById(R.id.ratdescription_store);
    submit=findViewById(R.id.subrat);
    ratingBar_st=findViewById(R.id.ratingbarstore);
    ratingtv_st=findViewById(R.id.rattv_store);

        description_db=findViewById(R.id.ratdescription_db);
        ratingBar_db=findViewById(R.id.ratingbar_db);
        ratingtv_db=findViewById(R.id.rattv_db);

        store_id=getIntent().getStringExtra("store_id");
        db_id=getIntent().getStringExtra("db_id");
        sale_id=getIntent().getStringExtra("sale_id");
    ratingBar_st.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            switch ((int) rating){
                case 1:
                    ratingtv_st.setText("hate it.");
                    break;
                case 2:
                    ratingtv_st.setText("dislike it.");

                case 3:
                    ratingtv_st.setText("Good.");
                    break;
                case 4:
                    ratingtv_st.setText("like it.");

                case 5:
                    ratingtv_st.setText("Love it.");
                    break;

            }
        }
    });
        ratingBar_db.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                switch ((int) rating){
                    case 1:
                        ratingtv_db.setText("hate it.");
                        break;
                    case 2:
                        ratingtv_db.setText("dislike it.");

                    case 3:
                        ratingtv_db.setText("Good.");
                        break;
                    case 4:
                        ratingtv_db.setText("like it.");

                    case 5:
                        ratingtv_db.setText("Love it.");
                        break;

                }
            }
        });
        Session_management sessionManagement = new Session_management(this);

        user_id=sessionManagement.getUserDetails().get(BaseURL.KEY_ID);


    submit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(description_st.getText().toString().isEmpty())
            {

                rate_st=ratingBar_st.getRating();
                desc_st="";
            }else{
                rate_st= ratingBar_st.getRating();
                desc_st=description_st.getText().toString();
            }
            if(description_db.getText().toString().isEmpty())
            {

                rate_db=ratingBar_db.getRating();
                desc_db="";
            }else{
                rate_db=ratingBar_db.getRating();
                desc_db=description_db.getText().toString();
            }
//            Toast.makeText(RatingActivity.this, user_id+store_id+sale_id+db_id+rate_db.toString()+desc_db, Toast.LENGTH_SHORT).show();
            data1(user_id,store_id,sale_id,db_id,rate_db.toString(),desc_db,rate_st.toString(),desc_st);
            SharedPreferences.Editor editor = getSharedPreferences("rating_store", MODE_PRIVATE).edit();
            editor.putString("rating","added");
            editor.apply();
            Intent i=new Intent(RatingActivity.this,MainActivity.class);
            startActivity(i);
        }
    });

    }
    private void data1(String userid,String store_id,String order_id,String db_id,String db_rating,String db_cmnts,String str_rating,String str_cmnts) {
        String tag_json_obj = "json_socity_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("store_id", store_id);
        params.put("user_id", userid);
        params.put("order_id", order_id);
        params.put("delivery_boy_id", db_id);
        params.put("delivery_boy_rating", db_rating);
        params.put("delivery_boy_comments", db_cmnts);
        params.put("store_rating", str_rating);
        params.put("store_comments", str_cmnts);
        // Toast.makeText(getActivity(), Store_id, Toast.LENGTH_SHORT).show();

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.ADD_RATING_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                try {


                    Boolean status = response.getBoolean("responce");
                    String msg=response.getString("data");

                    if(status) {

                        Toast.makeText(RatingActivity.this, "Review Added Successfuly", Toast.LENGTH_SHORT).show();

                    }else{

                        Toast.makeText(RatingActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(RatingActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
}
