package gogrocer.tcc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.Area_Adapter;
import Adapter.Refund_Adapter;
import Config.BaseURL;
import Model.Area;
import Model.Refund_model;
import util.CustomVolleyJsonRequest;
import util.Session_management;

public class Need_help extends AppCompatActivity {
    RecyclerView rv_refund;
    private List<Refund_model> area_modelList = new ArrayList<>();
    private Refund_Adapter area_adapter;
    private Session_management sessionManagement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need_help);

        rv_refund=findViewById(R.id.refund_order);
        rv_refund.setLayoutManager(new LinearLayoutManager(this));
        sessionManagement=new  Session_management(this);

        if(sessionManagement.isLoggedIn()) {
            String user_id= sessionManagement.getUserDetails().get(BaseURL.KEY_ID);


            makeGetrefund(user_id);

        }

    }
    private void makeGetrefund(String user_id)
    {
        String tag_json_obj = "json_product_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id",user_id);
        // Toast.makeText(getActivity(), Store_id, Toast.LENGTH_SHORT).show();

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_REFUND_LIST, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                try {

                    Boolean status;

                    status = response.getBoolean("response");
                    Toast.makeText(Need_help.this,String.valueOf(status), Toast.LENGTH_SHORT).show();

                    if(status) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Refund_model>>() {
                        }.getType();

                        area_modelList = gson.fromJson(response.getString("data"), listType);


                        area_adapter = new Refund_Adapter(area_modelList);
                        rv_refund.setAdapter(area_adapter);
                        area_adapter.notifyDataSetChanged();
                    }else{

                        Toast.makeText(Need_help.this, "No record found", Toast.LENGTH_SHORT).show();
                    }
//
//                    if(area_modelList.isEmpty()){
//                        if(getActivity() != null) {
//                            Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
//                        }
//                    }
////
                } catch (JSONException e) {
                    e.printStackTrace();
//                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(Need_help.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
}
