package Fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Visibility;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.daimajia.swipe.util.Attributes;
import com.facebook.shimmer.ShimmerFrameLayout;
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
import cn.pedant.SweetAlert.SweetAlertDialog;
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
    private ShimmerFrameLayout mShimmerViewContainer;
    TextView no_record;
    RelativeLayout tv_add_adress;
    private View_address_adapter adapter;
    private List<Delivery_address_model> delivery_address_modelList = new ArrayList<>();
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_address, container, false);
        rv_address = (RecyclerView) view.findViewById(R.id.rv_deli_address);
        rv_address.setLayoutManager(new LinearLayoutManager(getActivity()));
        no_record=(TextView)view.findViewById(R.id.no_record);
        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);

        tv_add_adress = (RelativeLayout) view.findViewById(R.id.tv_deli_add_address);

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

        tv_add_adress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManagement.updateSocity("", "");
                Bundle args = new Bundle();
                Fragment fm = new Add_delivery_address_fragment();

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();
            }
        });




        return view;
    }
    private void makeGetAddressRequest(String user_id) {
//        final AlertDialog loading=new ProgressDialog(getActivity());
//        loading.setMessage("Loading...");
//        loading.setCancelable(false);
//        loading.show();
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
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
//                    loading.dismiss();
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
                                //Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                            }}


                    }else{
                        no_record.setVisibility(View.VISIBLE);
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
                } catch (JSONException e) {
                    e.printStackTrace();
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
//                    loading.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
//                        loading.dismiss();
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                    }
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }

}
