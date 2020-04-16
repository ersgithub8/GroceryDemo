package Fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Visibility;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.daimajia.swipe.util.Attributes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.Delivery_get_address_adapter;
import Adapter.View_address_adapter;
import Config.BaseURL;
import Model.Delivery_address_model;
import gogrocer.tcc.AppController;
import gogrocer.tcc.MainActivity;
import gogrocer.tcc.R;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;
import util.Session_management;

public class Show_Address extends Fragment {
    private RecyclerView rv_address;
    private static String TAG = Show_Address.class.getSimpleName();
    private Session_management sessionManagement;
    public Boolean status;
    TextView no_record;
    private View_address_adapter adapter;
    private List<Delivery_address_model> delivery_address_modelList = new ArrayList<>();
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_address, container, false);
        rv_address = (RecyclerView) view.findViewById(R.id.rv_deli_address);
        rv_address.setLayoutManager(new LinearLayoutManager(getActivity()));
        no_record=(TextView)view.findViewById(R.id.no_record);


        if (ConnectivityReceiver.isConnected()) {
            sessionManagement=new Session_management(getActivity());

            if(sessionManagement.isLoggedIn()) {
                String user_id= sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
               // Toast.makeText(getActivity(), user_id, Toast.LENGTH_SHORT).show();
                makeGetAddressRequest(user_id);

            }
        } else {
            ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
        }



        return view;
    }
    private void makeGetAddressRequest(String user_id) {
        final AlertDialog loading=new ProgressDialog(getActivity());
        loading.setMessage("Loading...");
        loading.setCancelable(false);
        loading.show();
        // Tag used to cancel the request
        String tag_json_obj = "json_get_address_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);


        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_ADDRESS_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    loading.dismiss();
                     status = response.getBoolean("responce");
                    if (status) {

                        delivery_address_modelList.clear();

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Delivery_address_model>>() {
                        }.getType();

                        delivery_address_modelList = gson.fromJson(response.getString("data"), listType);

                        //RecyclerView.Adapter adapter1 = new Delivery_get_address_adapter(delivery_address_modelList);
                        adapter = new View_address_adapter(delivery_address_modelList);
                        ((View_address_adapter) adapter).setMode(Attributes.Mode.Single);
                        rv_address.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        if (delivery_address_modelList.isEmpty()) {
                            no_record.setVisibility(View.VISIBLE);
                            if (getActivity() != null) {

                                //Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                            }}


                    }else{
                        no_record.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    loading.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

}
