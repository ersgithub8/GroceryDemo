package Adapter;

import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.bumptech.glide.Glide;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Config.BaseURL;

import Model.Master_category;
import Model.Store_model;
import gogrocer.tcc.AppController;
import gogrocer.tcc.R;
import gogrocer.tcc.RatingActivity;
import util.CustomVolleyJsonRequest;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Rajesh Dabhi on 22/6/2017.
 */


public class Stores_adapter extends RecyclerView.Adapter<Stores_adapter.MyViewHolder> {

    private List<Store_model> modelList1;
    private Context context;
    String language;
    SharedPreferences preferences;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;
        public RatingBar progressBar;
        public MyViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.tv_home_title);
            image = (ImageView) view.findViewById(R.id.iv_home_img);
            progressBar=(RatingBar)view.findViewById(R.id.ratingstore);
        }
    }

    public Stores_adapter(List<Store_model>modelList) {
        this.modelList1 = modelList;
    }

    @Override
    public Stores_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_home_rv, parent, false);

        context = parent.getContext();

        return new Stores_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        Store_model mList1 = modelList1.get(position);
        Glide.with(context)
                .load(BaseURL.GET_STOREImages_URL + mList1.getUser_image())
                .placeholder(R.drawable.icon)
                .crossFade()
               .dontAnimate()
                .into(holder.image);
      preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
        language=preferences.getString("language","");
        if (language.contains("english")) {
            holder.title.setText(mList1.getUser_fullname());
        }
        else {String tag_json_obj = "json_category_req";

            Map<String, String> params = new HashMap<String, String>();
            params.put("store_id", mList1.getStoreid());

       /* if (parent_id != null && parent_id != "") {
        }*/

            CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                    BaseURL.GET_STARS, params, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    //                    if (response != null && response.length() > 0) {
                    try {
                        Boolean status = response.getBoolean("response");
                       if (status){
                           JSONArray jsonArray = response.getJSONArray("data");

                           for (int i=0;i<jsonArray.length();i++) {
                               JSONObject object = jsonArray.getJSONObject(i);
                               String stars=object.getString("rating");
                               if (stars.equals("null")){
                                   holder.progressBar.setRating(0);
                               }else if(stars.equals("1")){
                                   holder.progressBar.setRating(1);
                               }else if(stars.equals("2")){
                                   holder.progressBar.setRating(2);
                               }else if(stars.equals("3")){
                                   holder.progressBar.setRating(3);
                               }else if(stars.equals("4")){
                                   holder.progressBar.setRating(4);
                               }else if(stars.equals("5")){
                                   holder.progressBar.setRating(5);
                               }

                           }


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(context, "Connection Time out", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
            holder.title.setText(mList1.getUser_fullname());

        }


    }


    @Override
    public int getItemCount() {
        return modelList1.size();
    }

}

