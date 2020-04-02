package gogrocer.tcc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.My_Pending_Order_adapter;
import Adapter.My_order_detail_adapter;
import Config.BaseURL;
import Model.My_Pending_order_model;
import Model.My_order_detail_model;
import util.CustomVolleyJsonArrayRequest;
import util.CustomVolleyJsonRequest;
import util.RecyclerTouchListener;
import util.Session_management;

public class HelpActivity extends AppCompatActivity {
    Spinner orders;
    ArrayList<String> nameo=new ArrayList<String>();
    ArrayList<String> id=new ArrayList<String>();
    RecyclerView rv_detail_order;
    Button submit;
    ImageButton back;
    EditText help;
    private List<My_order_detail_model> my_order_detail_modelList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        rv_detail_order=findViewById(R.id.helporder);
        orders=findViewById(R.id.spinnerorder);
        help=findViewById(R.id.ethelp);
        submit=findViewById(R.id.helpsubmit);
        back=findViewById(R.id.bhbtn);
        rv_detail_order.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Session_management sessionManagement = new Session_management(HelpActivity.this);
        final String user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);

        makeGetOrderRequest(user_id);

        orders.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long ids) {
                if(position==0){
                    return;
                }
//                Toast.makeText(HelpActivity.this,id.get(position) , Toast.LENGTH_SHORT).show();

                help.setText("");
                makeGetOrderDetailRequest(id.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }



        });

        rv_detail_order.addOnItemTouchListener(new RecyclerTouchListener(HelpActivity.this
                , rv_detail_order, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String sale_item_id=my_order_detail_modelList.get(position).getSale_item_id();
                String sale_id=my_order_detail_modelList.get(position).getSale_id();
                String product_id=my_order_detail_modelList.get(position).getProduct_id();
                String product_name=my_order_detail_modelList.get(position).getProduct_name();
                String qty=my_order_detail_modelList.get(position).getQty();
                String unitt=my_order_detail_modelList.get(position).getUnit();
                String unit_value=my_order_detail_modelList.get(position).getUnit_value();
                String pricce=my_order_detail_modelList.get(position).getPrice();
                String qty_in_kg=my_order_detail_modelList.get(position).getQty_in_kg();
//                String product_image=my_order_detail_modelList.get(position);

                String item="Sale item Id:"+sale_item_id+
                        "\nSale id:"+sale_id+
                        "\nProduct id:"+product_id+"\n\n"
//                        "\nProduct name:"+product_name+
                        ;
//                Toast.makeText(HelpActivity.this, item, Toast.LENGTH_SHORT).show();
            help.append(item);

            }



            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(help.getText().toString().isEmpty()){
                    Toast.makeText(HelpActivity.this, "Please Describe Your isseue to continue", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent mailIntent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:?subject=" + ""+ "&body=" + help.getText().toString() + "&to=" + "Software.robin@gmail.com");
                mailIntent.setData(data);
                startActivity(Intent.createChooser(mailIntent, "Send mail..."));
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void makeGetOrderRequest(String userid)
    {
        final android.app.AlertDialog loading=new ProgressDialog(HelpActivity.this);
        loading.setMessage("Loading...");
        loading.setCancelable(false);
        loading.show();
        String tag_json_obj = "json_socity_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userid);
        // Toast.makeText(HelpActivity.this, Store_id, Toast.LENGTH_SHORT).show();

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_ORDER_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //Log.d(TAG, response.toString());

                try {


                    Boolean status = response.getBoolean("responce");
                    //  Toast.makeText(HelpActivity.this,String.valueOf(status), Toast.LENGTH_SHORT).show();
                    JSONArray array=response.getJSONArray("data");



                    if(status) {
                    loading.dismiss();
//                        nameo.clear();
                        id.clear();
//                        nameo.add("Select Order");
                        id.add("Please select order id.");
                        for (int i=0;i<array.length();i++) {
                            JSONObject object = array.getJSONObject(i);


                            id.add(object.getString("sale_id"));


                            ArrayAdapter<String> adapter=new ArrayAdapter<String>(HelpActivity.this,
                                    android.R.layout.simple_spinner_item,id);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            orders.setAdapter(adapter);
                        }

                    }else{

                        Toast.makeText(HelpActivity.this, "No record found", Toast.LENGTH_SHORT).show();
                    }
//
//                    if(area_modelList.isEmpty()){
//                        if(HelpActivity.this != null) {
//                            Toast.makeText(HelpActivity.this, getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
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
                //VolleyLog.d(TAG, "Error: " + error.getMessage());
                loading.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(HelpActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }



    private void makeGetOrderDetailRequest(String sale_id) {

        // Tag used to cancel the request
        String tag_json_obj = "json_order_detail_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("sale_id", sale_id);

        CustomVolleyJsonArrayRequest jsonObjReq = new CustomVolleyJsonArrayRequest(Request.Method.POST,
                BaseURL.ORDER_DETAIL_URL, params, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
//                Log.d(TAG, response.toString());

                Gson gson = new Gson();
                Type listType = new TypeToken<List<My_order_detail_model>>() {
                }.getType();

                my_order_detail_modelList = gson.fromJson(response.toString(), listType);

                My_order_detail_adapter adapter = new My_order_detail_adapter(my_order_detail_modelList);

                rv_detail_order.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                if (my_order_detail_modelList.isEmpty()) {
                    Toast.makeText(HelpActivity.this, getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(HelpActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }
}
