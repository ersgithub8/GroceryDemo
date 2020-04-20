package Fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.Product_adapter;
import Adapter.Socity_adapter;
import Config.BaseURL;
import Model.Product_model;
import Model.Socity_model;
import cn.pedant.SweetAlert.SweetAlertDialog;
import gogrocer.tcc.AppController;
import gogrocer.tcc.MainActivity;
import gogrocer.tcc.R;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonArrayRequest;
import util.CustomVolleyJsonRequest;
import util.RecyclerTouchListener;
import util.Session_management;

/**
 * Created by Rajesh Dabhi on 29/6/2017.
 */

public class Socity_fragment extends Fragment {

    private static String TAG = Socity_fragment.class.getSimpleName();

    private EditText et_search;
    private RecyclerView rv_socity;

    private List<Socity_model> socity_modelList = new ArrayList<>();
    private Socity_adapter adapter;

    public Socity_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_socity, container, false);

        //String getpincode = getArguments().getString("pincode");

        et_search = (EditText) view.findViewById(R.id.et_socity_search);
        rv_socity = (RecyclerView) view.findViewById(R.id.rv_socity);
        rv_socity.setLayoutManager(new LinearLayoutManager(getActivity()));

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // check internet connection
        if (ConnectivityReceiver.isConnected()) {
//            makeGetSocityRequest();
            makeGetcity();
        } else {
            ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
        }

        rv_socity.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_socity, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                String socity_id = socity_modelList.get(position).getSocity_id();
                String socity_name = socity_modelList.get(position).getSocity_name();


                Session_management sessionManagement = new Session_management(getActivity());
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("city_id",socity_id);
                editor.apply();

                sessionManagement.updateSocity(socity_name,socity_id);

                ((MainActivity) getActivity()).onBackPressed();

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        return view;
    }

    /**
     * Method to make json array request where json response starts wtih
     */
    private void makeGetSocityRequest() {
//        final AlertDialog loading=new ProgressDialog(getActivity());
//        loading.setMessage("Loading...");
//        loading.setCancelable(false);
//        loading.show();
        // Tag used to cancel the request
        String tag_json_obj = "json_socity_req";

        /*Map<String, String> params = new HashMap<String, String>();
        params.put("pincode", pincode);*/

        CustomVolleyJsonArrayRequest jsonObjReq = new CustomVolleyJsonArrayRequest(Request.Method.GET,
                BaseURL.GET_SOCITY_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {




                Log.d(TAG, response.toString());

                Gson gson = new Gson();
                Type listType = new TypeToken<List<Socity_model>>() {
                }.getType();

                socity_modelList = gson.fromJson(response.toString(), listType);


                adapter = new Socity_adapter(socity_modelList);
                rv_socity.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                if(socity_modelList.isEmpty()){
                    if(getActivity() != null) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    if(getActivity() != null) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }
    private void makeGetcity()
    {
        String tag_json_obj = "json_product_req";

        // params.put("type",Type);
        // Toast.makeText(getActivity(), Store_id, Toast.LENGTH_SHORT).show();

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.GET,
                BaseURL.GET_SOCITY_URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {

                    Boolean status = response.getBoolean("response");



                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Socity_model>>() {
                    }.getType();

                    socity_modelList = gson.fromJson(response.getString("data"), listType);



                    adapter = new Socity_adapter(socity_modelList);
                    rv_socity.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    if(socity_modelList.isEmpty()){
                        if(getActivity() != null) {
//                            Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                            SweetAlertDialog alertDialog=new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE);
                            alertDialog.setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    getActivity().onBackPressed();
                                }
                            }).setTitleText("No data Found")
                                    .setCancelable(false);

                            alertDialog.setConfirmButtonBackgroundColor(Color.RED);
                            alertDialog.show();
                        }
                    }
////
                } catch (JSONException e) {
                    e.printStackTrace();
//                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


}