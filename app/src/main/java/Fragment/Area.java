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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.Area_Adapter;
import Adapter.Socity_adapter;
import Adapter.Top_Selling_Adapter;
import Config.BaseURL;
import Model.Socity_model;
import cn.pedant.SweetAlert.SweetAlertDialog;
import gogrocer.tcc.AppController;
import gogrocer.tcc.MainActivity;
import gogrocer.tcc.R;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;
import util.RecyclerTouchListener;
import util.Session_management;

public class Area extends Fragment {
    private static String TAG = Area.class.getSimpleName();

    private EditText et_search;
    private RecyclerView rv_socity;
    public Boolean status;
    TextView no_record;
    private List<Model.Area> area_modelList = new ArrayList<>();
    private Area_Adapter area_adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.area, container, false);
        et_search = (EditText) view.findViewById(R.id.et_socity_search);
        rv_socity = (RecyclerView) view.findViewById(R.id.rv_socity);
        rv_socity.setLayoutManager(new LinearLayoutManager(getActivity()));
        no_record= (TextView) view.findViewById(R.id.no_record);

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(status){

                    area_adapter.getFilter().filter(charSequence);
                }else
                {
                    no_record.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        // check internet connection
        if (ConnectivityReceiver.isConnected()) {
//            makeGetSocityRequest();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String name = preferences.getString("city_id", "");

          makeGetcity(name);

        } else {
            ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
        }

        rv_socity.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_socity, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                String area_id = area_modelList.get(position).getId();
                String area_name = area_modelList.get(position).getArea();

                Session_management sessionManagement = new Session_management(getActivity());

                sessionManagement.updateArea(area_name,area_id);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("area_id",area_id);
                editor.apply();

                ((MainActivity) getActivity()).onBackPressed();

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        return view;
    }
    private void makeGetcity(String cityid)
    {

        final AlertDialog loading=new ProgressDialog(getActivity());
        loading.setMessage("Loading...");
        loading.setCancelable(false);
        loading.show();

        String tag_json_obj = "json_product_req";
        Map<String, String> params = new HashMap<String, String>();
         params.put("city_id",cityid);
        // Toast.makeText(getActivity(), Store_id, Toast.LENGTH_SHORT).show();

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_AREAS_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {

                    loading.dismiss();

                     status = response.getBoolean("response");
//                    Toast.makeText(getActivity(),String.valueOf(status), Toast.LENGTH_SHORT).show();

                    if(status) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Model.Area>>() {
                        }.getType();

                        area_modelList = gson.fromJson(response.getString("data"), listType);


                        area_adapter = new Area_Adapter(area_modelList);
                        rv_socity.setAdapter(area_adapter);
                        area_adapter.notifyDataSetChanged();
                    }else{
                        Session_management sessionManagement = new Session_management(getActivity());
                        String area_name="choose area";
                        String area_id= null;
                        sessionManagement.updateArea(area_name,area_id);
//                        Toast.makeText(getActivity(), "No record found", Toast.LENGTH_SHORT).show();
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
//
//                    if(area_modelList.isEmpty()){
//                        if(getActivity() != null) {
//                            Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
//                        }
//                    }
////
                } catch (JSONException e) {
                    e.printStackTrace();
                    loading.dismiss();
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
                    loading.dismiss();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

}
