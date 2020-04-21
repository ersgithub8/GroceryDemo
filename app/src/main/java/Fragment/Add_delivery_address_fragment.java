package Fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Config.BaseURL;
import gogrocer.tcc.AppController;
import gogrocer.tcc.MainActivity;
import gogrocer.tcc.MapsActivity;
import gogrocer.tcc.R;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;
import util.Session_management;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Rajesh Dabhi on 6/7/2017.
 */

public class Add_delivery_address_fragment extends Fragment implements View.OnClickListener {

    private static String TAG = Add_delivery_address_fragment.class.getSimpleName();

    private EditText et_phone, et_name, et_pin,et_Address,et_Address2,et_Address3;
    private RelativeLayout btn_update;
    private Button et_house;
    private TextView tv_phone, tv_name, tv_pin, tv_house, tv_socity, btn_socity,tv_address,tv_address1,tv_address2,tv_address3,tv_area,tv_apartment ;
    private String getsocity = "";
    float lang;
    float lat;
    public pl.droidsonroids.gif.GifImageView gifImageView;
    private List<Model.Area> area_modelList = new ArrayList<>();

    private Session_management sessionManagement;

    private boolean isEdit = false;
    public String getarea_name;

    private String getlocation_id;

    public Add_delivery_address_fragment() {
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
        View view = inflater.inflate(R.layout.fragment_add_delivery_address, container, false);

        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.add));

        sessionManagement = new Session_management(getActivity());
        gifImageView=(pl.droidsonroids.gif.GifImageView)view.findViewById(R.id.giff);

        et_phone = (EditText) view.findViewById(R.id.et_add_adres_phone);
        et_name = (EditText) view.findViewById(R.id.et_add_adres_name);
        tv_phone = (TextView) view.findViewById(R.id.tv_add_adres_phone);
        tv_name = (TextView) view.findViewById(R.id.tv_add_adres_name);
        tv_pin = (TextView) view.findViewById(R.id.tv_add_adres_pin);
        et_pin = (EditText) view.findViewById(R.id.et_add_adres_pin);
        et_house = (Button) view.findViewById(R.id.et_add_adres_home);
        tv_house = (TextView) view.findViewById(R.id.tv_add_adres_home);
        tv_socity = (TextView) view.findViewById(R.id.tv_add_adres_socity);
        btn_update = (RelativeLayout) view.findViewById(R.id.btn_add_adres_edit);
        btn_socity = (TextView) view.findViewById(R.id.btn_add_adres_socity);
        et_Address=(EditText)view.findViewById(R.id.et_Address);
        et_Address2=(EditText)view.findViewById(R.id.et_Address2);
        et_Address3=(EditText)view.findViewById(R.id.et_Address3);
        tv_address=(TextView)view.findViewById(R.id.tv_Address);
        tv_address1=(TextView)view.findViewById(R.id.tv_Address2);
        tv_address2=(TextView)view.findViewById(R.id.tv_Address23);
        tv_address3=(TextView)view.findViewById(R.id.tv_add_adres_home);
        tv_area=(TextView) view.findViewById(R.id.btn_area);
        tv_apartment=(TextView)view.findViewById(R.id.btn_apartment);
        tv_area.setText("choose area");




        et_house.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gifImageView.setVisibility(View.VISIBLE);
                    }
                }, 100);



            }
        });





        String getsocity_name = sessionManagement.getUserDetails().get(BaseURL.KEY_SOCITY_NAME);
        getarea_name = sessionManagement.getUserDetails().get(BaseURL.KEY_AREA_NAME);

        String getapartment_name = sessionManagement.getUserDetails().get(BaseURL.KEY_APARTMENT_NAME);
        String getarea_id = sessionManagement.getUserDetails().get(BaseURL.AREA_ID);
        String getapartment_id = sessionManagement.getUserDetails().get(BaseURL.APARTMENT_ID);
        String getsocity_id = sessionManagement.getUserDetails().get(BaseURL.KEY_SOCITY_ID);

        Bundle args = getArguments();

        if (args != null) {
            getlocation_id = getArguments().getString("location_id");
            String get_name = getArguments().getString("name");
            String get_phone = getArguments().getString("mobile");
            String get_pine = getArguments().getString("pincode");
            String get_socity_id = getArguments().getString("socity_id");
            String get_socity_name = getArguments().getString("socity_name");
            String get_house = getArguments().getString("house");
            String get_area = getArguments().getString("area_name");
            String get_apartment = getArguments().getString("apartment_name");

            String get_flat = getArguments().getString("flat_no");
            String get_floor = getArguments().getString("floor");
            String get_building = getArguments().getString("building");



            if (TextUtils.isEmpty(get_name) && get_name == null) {
                isEdit = false;

            }else  if (TextUtils.isEmpty(get_floor) && get_floor == null) {
                isEdit = false;

            }else  if(TextUtils.isEmpty(get_building) && get_building == null) {
                isEdit = false;

            }else  if(TextUtils.isEmpty(get_flat) && get_flat == null) {
                isEdit = false;

            }else  if (TextUtils.isEmpty(get_area) && get_area == null) {
                isEdit = false;

            }else  if (TextUtils.isEmpty(get_apartment) && get_apartment == null) {
                isEdit = false;

            }
            else {
                isEdit = true;

                Toast.makeText(getActivity(), "edit", Toast.LENGTH_SHORT).show();

                et_Address.setText(get_flat);
                et_Address2.setText(get_floor);
                et_Address3.setText(get_building);
                et_name.setText(get_name);
                et_phone.setText(get_phone);
                et_pin.setText(get_pine);
                //et_house.setText(get_house);
                btn_socity.setText(get_socity_name);
                tv_area.setText(getarea_name);
                tv_apartment.setText(getapartment_name);

                sessionManagement.updateSocity(get_socity_name, get_socity_id);
                sessionManagement.updateArea(getarea_name, getarea_id);
                sessionManagement.updateApartment(getapartment_name, getapartment_id);
            }
        }
        if (TextUtils.isEmpty(getsocity_name)) {

            isEdit=false;
           // Toast.makeText(getActivity(), "Select City First", Toast.LENGTH_SHORT).show();
        }else if(!TextUtils.isEmpty(getsocity_name)) {

            btn_socity.setText(getsocity_name);
            tv_area.setText(getarea_name);
            tv_apartment.setText(getapartment_name);



            sessionManagement.updateSocity(getsocity_name, getsocity_id);
        }
//        if (TextUtils.isEmpty(getarea_name)) {
//
//            isEdit=false;
//        }else if(!TextUtils.isEmpty(getarea_name)) {
//
//            tv_area.setText(getarea_name);
//
//            sessionManagement.updateArea(getarea_name, getarea_id);
//        }
//        if (TextUtils.isEmpty(getapartment_name)) {
//
//            isEdit=false;
//        }else if(!TextUtils.isEmpty(getapartment_name)) {
//
//            tv_apartment.setText(getapartment_name);
//
//            sessionManagement.updateApartment(getapartment_name, getapartment_id);
//        }
        btn_update.setOnClickListener(this);
        btn_socity.setOnClickListener(this);
        tv_area.setOnClickListener(this);
        tv_apartment.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btn_add_adres_edit) {
            attemptEditProfile();
        } else if (id == R.id.btn_add_adres_socity) {

            /*String getpincode = et_pin.getText().toString();

            if (!TextUtils.isEmpty(getpincode)) {*/

            tv_area.setText(getarea_name);
                Bundle args = new Bundle();
                Fragment fm = new Socity_fragment();
                //args.putString("pincode", getpincode);
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();
            /*} else {
                Toast.makeText(getActivity(), getResources().getString(R.string.please_enter_pincode), Toast.LENGTH_SHORT).show();
            }*/

        }else if (id == R.id.btn_area) {

            /*String getpincode = et_pin.getText().toString();

            if (!TextUtils.isEmpty(getpincode)) {*/


            Bundle args = new Bundle();
            Fragment fm = new Area();
            //args.putString("pincode", getpincode);
            fm.setArguments(args);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                    .addToBackStack(null).commit();
            /*} else {
                Toast.makeText(getActivity(), getResources().getString(R.string.please_enter_pincode), Toast.LENGTH_SHORT).show();
            }*/

        }else if (id == R.id.btn_apartment) {

            /*String getpincode = et_pin.getText().toString();

            if (!TextUtils.isEmpty(getpincode)) {*/

            Bundle args = new Bundle();
            Fragment fm = new Apartment();
            //args.putString("pincode", getpincode);
            fm.setArguments(args);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                    .addToBackStack(null).commit();
            /*} else {
                Toast.makeText(getActivity(), getResources().getString(R.string.please_enter_pincode), Toast.LENGTH_SHORT).show();
            }*/

        }
    }

    private void attemptEditProfile() {

        tv_phone.setText(getResources().getString(R.string.receiver_mobile_number));
        tv_pin.setText(getResources().getString(R.string.tv_reg_pincode));
        tv_name.setText(getResources().getString(R.string.receiver_name_req));
        tv_area.setText("area required...");
//        tv_apartment.setText("apartment required...");
       // tv_house.setText(getResources().getString(R.string.tv_reg_house));
       // tv_socity.setText(getResources().getString(R.string.tv_reg_socity));

        tv_name.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_phone.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_pin.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_house.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_socity.setTextColor(getResources().getColor(R.color.dark_gray));

        String getphone = et_phone.getText().toString();
        String getname = et_name.getText().toString();
        String getpin = et_pin.getText().toString();
        String getAddress1=et_Address.getText().toString();
        String getAddress2=et_Address2.getText().toString();
        String getAddress3=et_Address3.getText().toString();
        String getAddress="";

        if(!et_Address.getText().toString().isEmpty()){
            getAddress = "Flat no & Plot no. " + getAddress1 ;
        }
        if(!et_Address2.getText().toString().isEmpty()){
            getAddress =getAddress+"," + "Floor no: " + getAddress2;
        }
        if(!et_Address3.getText().toString().isEmpty()){
            getAddress =getAddress+"," + "Building name & Colony Name: " + getAddress3;
        }

        //getAddress = "Flat no & Plot no. " + getAddress1 + "," + "Floor no: " + getAddress2+"," + "Building name & Colony Name: " + getAddress3;
       // Toast.makeText(getActivity(), getAddress, Toast.LENGTH_SHORT).show();
        SharedPreferences prefs =getActivity().getSharedPreferences("7", MODE_PRIVATE);
        float lat = prefs.getFloat("8",1);
         float lang= prefs.getFloat("9",1);
        String gethouse = lat+","+lang;
      //  Toast.makeText(getActivity(), gethouse, Toast.LENGTH_SHORT).show();
        String getsocity = sessionManagement.getUserDetails().get(BaseURL.KEY_SOCITY_ID);
        String getarea = sessionManagement.getUserDetails().get(BaseURL.AREA_ID);
        String getapartment="";
                getapartment = sessionManagement.getUserDetails().get(BaseURL.APARTMENT_ID);

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(getphone)) {
            tv_phone.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_phone;
            cancel = true;
        } else if (!isPhoneValid(getphone)) {
            tv_phone.setText(getResources().getString(R.string.phone_too_short));
            tv_phone.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_phone;
            cancel = true;
        } else if (TextUtils.isEmpty(getAddress)) {
//            tv_address.setTextColor(getResources().getColor(R.color.colorPrimary));
//            focusView = et_Address;
            cancel = false;
        }

        if (TextUtils.isEmpty(getname)) {
            tv_name.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_name;
            cancel = true;
        }

        if (TextUtils.isEmpty(getpin)) {
            tv_pin.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_pin;
            cancel = true;
        }
        if (TextUtils.isEmpty(getAddress1)) {
            //tv_address.setTextColor(getResources().getColor(R.color.colorPrimary));
            //focusView = et_Address;
            cancel = false;
        }if (TextUtils.isEmpty(getAddress2)) {
//            tv_address1.setTextColor(getResources().getColor(R.color.colorPrimary));
//            focusView = et_Address2;
            cancel = false;
        }if (TextUtils.isEmpty(getAddress3)) {
//            tv_address2.setTextColor(getResources().getColor(R.color.colorPrimary));
//            tv_address3.setTextColor(getResources().getColor(R.color.colorPrimary));
//
//            focusView = et_Address2;
            cancel = false;
        }

//        if (TextUtils.isEmpty(gethouse)) {
//            tv_house.setTextColor(getResources().getColor(R.color.colorPrimary));
//            focusView = et_house;
//            cancel = true;
//        }

        if (TextUtils.isEmpty(getsocity) && getsocity == null) {
            tv_socity.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = btn_socity;
            cancel = true;
        }
        if (TextUtils.isEmpty(getarea) && getarea == null) {
            tv_socity.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = tv_area;
            cancel = true;
        }
        if (TextUtils.isEmpty(getapartment)
        //        && getapartment == null
        ) {
//            tv_socity.setTextColor(getResources().getColor(R.color.colorPrimary));
//            focusView = tv_apartment;

//            getapartment="";
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            if (focusView != null)
                focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            if (ConnectivityReceiver.isConnected()) {

                String user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);

                // check internet connection
                if (ConnectivityReceiver.isConnected()) {
                    if (isEdit) {
                        makeEditAddressRequest(getlocation_id, getpin, getsocity, gethouse, getname, getphone,getAddress,getarea,getapartment);
                    } else {
                        makeAddAddressRequest(user_id, getpin, getsocity, gethouse, getname, getphone,getAddress,getarea,getapartment);
                    }
                }
            }
        }
    }

    private boolean isPhoneValid(String phoneno) {
        //TODO: Replace this with your own logic
        return phoneno.length() > 9;
    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeAddAddressRequest(String user_id, String pincode, String socity_id,
                                       String house_no, String receiver_name, String receiver_mobile,String address,String area,String apartment) {

        // Tag used to cancel the request
        String tag_json_obj = "json_add_address_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);
        params.put("pincode", pincode);
        params.put("city_id", socity_id);
        params.put("house_no", house_no);
        params.put("receiver_name", receiver_name);
        params.put("receiver_mobile", receiver_mobile);
        params.put("address",address);
        params.put("area_id",area);
        params.put("apartment_id",apartment);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.ADD_ADDRESS_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        ((MainActivity) getActivity()).onBackPressed();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeEditAddressRequest(String location_id, String pincode, String socity_id,
                                        String house_no, String receiver_name, String receiver_mobile,String address ,String area,String apartment){
        final AlertDialog loading=new ProgressDialog(getActivity());
        loading.setMessage("Loading...");
        loading.setCancelable(false);
        loading.show();
        // Tag used to cancel the request
        String tag_json_obj = "json_edit_address_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("location_id", location_id);
        params.put("pincode", pincode);
        params.put("socity_id", socity_id);
        params.put("house_no", house_no);
        params.put("receiver_name", receiver_name);
        params.put("receiver_mobile", receiver_mobile);
       params.put("address",address);
        params.put("area_id",area);
        params.put("apartment_id",apartment);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.EDIT_ADDRESS_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    loading.dismiss();
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        String msg = response.getString("data");
                        Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_SHORT).show();

                        ((MainActivity) getActivity()).onBackPressed();

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
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
                loading.dismiss();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

}
