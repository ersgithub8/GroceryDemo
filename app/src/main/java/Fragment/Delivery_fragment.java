package Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import Adapter.Area_Adapter;
import Adapter.Delivery_get_address_adapter;
import Config.BaseURL;
import Model.Delivery_address_model;
import gogrocer.tcc.AppController;
import gogrocer.tcc.MainActivity;
import gogrocer.tcc.R;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;
import util.DatabaseHandler;
import util.Session_management;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Rajesh Dabhi on 27/6/2017.
 */

public class Delivery_fragment extends Fragment implements View.OnClickListener {

    private static String TAG = Delivery_fragment.class.getSimpleName();

    private TextView tv_afternoon, tv_morning, tv_total, tv_item, tv_socity;
    private TextView tv_date, tv_time;
    private EditText et_address;
    private RelativeLayout btn_checkout, tv_add_adress;
    private RecyclerView rv_address;
    public TextView delivery_charges;


    public String formattedDate;
    Date currentTime;
    String currentTime1,dmethod;

    private Delivery_get_address_adapter adapter;
    private List<Delivery_address_model> delivery_address_modelList = new ArrayList<>();

    private DatabaseHandler db_cart;
SharedPreferences preferences;
    private Session_management sessionManagement;

    private int mYear, mMonth, mDay, mHour, mMinute;

    private String gettime = "";
    private String getdate = "";

    public RadioButton FastRadioBtn,CustomRadioBtn;
    public TextView nextdaytext;
    public String tomorrowAsString;

    private String deli_charges;
    String store_id;
    String time;
    public String total;
String language;
    public Delivery_fragment() {
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
        View view = inflater.inflate(R.layout.fragment_delivery_time, container, false);

        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.delivery));

//        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
//        String name = prefs.getString("name", "No name defined");
       // store_id = SharedPref.getString(getActivity(), BaseURL.STORE_ID);
        store_id=getArguments().getString("storeid");
        preferences = getActivity().getSharedPreferences("lan", MODE_PRIVATE);

        tv_date = (TextView) view.findViewById(R.id.tv_deli_date);
        tv_time = (TextView) view.findViewById(R.id.tv_deli_fromtime);
        tv_add_adress = (RelativeLayout) view.findViewById(R.id.tv_deli_add_address);
        tv_total = (TextView) view.findViewById(R.id.tv_deli_total);
        tv_item = (TextView) view.findViewById(R.id.tv_deli_item);
        btn_checkout = (RelativeLayout) view.findViewById(R.id.btn_deli_checkout);
        rv_address = (RecyclerView) view.findViewById(R.id.rv_deli_address);
        rv_address.setLayoutManager(new LinearLayoutManager(getActivity()));

        FastRadioBtn=(RadioButton)view.findViewById(R.id.fast_delivery);
        CustomRadioBtn=(RadioButton)view.findViewById(R.id.custom_delivery);
        nextdaytext=(TextView)view.findViewById(R.id.nextdaytextview);

        makeslotrequest(store_id);


//        if(dmethod.equalsIgnoreCase("instant")){
//
//            FastRadioBtn.setVisibility(View.VISIBLE);
//            FastRadioBtn.setChecked(true);
//            nextdaytext.setText("your order will be delivered within one hour");
//        }else if (dmethod.equalsIgnoreCase("slot")){
//            CustomRadioBtn.setVisibility(View.VISIBLE);
//            CustomRadioBtn.setChecked(true);
//            tv_date.setVisibility(View.VISIBLE);
//            tv_time.setVisibility(View.VISIBLE);
//        }else {
//            Toast.makeText(getActivity(), "Cannot deliver", Toast.LENGTH_SHORT).show();
//        }





        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c);
       // Toast.makeText(getActivity(), formattedDate, Toast.LENGTH_SHORT).show();
       // currentTime= Calendar.getInstance().getTime();
        currentTime1 = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        //Toast.makeText(getActivity(), currentTime+formattedDate, Toast.LENGTH_SHORT).show();

//        CustomRadioBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (dmethod.equalsIgnoreCase("slot")){
//                    tv_date.setVisibility(View.VISIBLE);
//                    tv_time.setVisibility(View.VISIBLE);
//                    tv_time.setEnabled(true);
//                    tv_date.setEnabled(true);
//
//                                   }else {
//                    Toast.makeText(getActivity(), "Time slot not Available", Toast.LENGTH_SHORT).show();
//                    btn_checkout.setEnabled(false);
//                }
//
//
//            }
//        });
//
//
//        FastRadioBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                nextdaytext.setText("your order will be delivered within one hour");
//            }
//        });
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
         tomorrowAsString = dateFormat.format(tomorrow);





//
//        if (store_id.equals("232"))
//        {
//            FastRadioBtn.setChecked(true);
//           // Toast.makeText(getActivity(), tomorrowAsString, Toast.LENGTH_SHORT).show();
//
//            nextdaytext.setText("your order will be delivered Tomorrow 5:30 am to 8:00 am");
//        }else {
//            nextdaytext.setText("your order will be delivered within one hour");
//        }
        //tv_socity = (TextView) view.findViewById(R.id.tv_deli_socity);
        //et_address = (EditText) view.findViewById(R.id.et_deli_address);

        db_cart = new DatabaseHandler(getActivity());

        tv_total.setText(db_cart.getTotalAmount());
        tv_item.setText("" + db_cart.getCartCount());
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

         total= db_cart.getTotalAmount();



        // get session user data
        sessionManagement = new Session_management(getActivity());
        String getsocity = sessionManagement.getUserDetails().get(BaseURL.KEY_SOCITY_NAME);
        String getaddress = sessionManagement.getUserDetails().get(BaseURL.KEY_HOUSE);

        //tv_socity.setText("Socity Name: " + getsocity);
        //et_address.setText(getaddress);

        tv_date.setOnClickListener(this);
        tv_time.setOnClickListener(this);
        tv_add_adress.setOnClickListener(this);
        btn_checkout.setOnClickListener(this);

        String date = sessionManagement.getdatetime().get(BaseURL.KEY_DATE);
         time = sessionManagement.getdatetime().get(BaseURL.KEY_TIME);



        if (date != null && time != null) {

            getdate = date;


            try {
                String inputPattern = "yyyy-MM-dd";
                String outputPattern = "dd-MM-yyyy";
                SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

                Date date1 = inputFormat.parse(getdate);
                String str = outputFormat.format(date1);

                tv_date.setText(getResources().getString(R.string.delivery_date) + str);

            } catch (ParseException e) {
                e.printStackTrace();

                tv_date.setText(getResources().getString(R.string.delivery_date) + getdate);
            }
            language=preferences.getString("language","");
//            if (language.contains("spanish")) {
//                String timeset=time;
//                 timeset=timeset.replace("PM","م");
//                 timeset=timeset.replace("AM","ص");
//                tv_time.setText(timeset);
//
//            }
//            else {

                tv_time.setText(time);
//            }
        }


        if (ConnectivityReceiver.isConnected()) {
            String user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
            makeGetAddressRequest(user_id);
        } else {
            ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btn_deli_checkout) {
            attemptOrder();
        } else if (id == R.id.tv_deli_date) {
            getdate();
        } else if (id == R.id.tv_deli_fromtime) {

            if (TextUtils.isEmpty(getdate)) {
                Toast.makeText(getActivity(), getResources().getString(R.string.please_select_date), Toast.LENGTH_SHORT).show();
            } else {
                Bundle args = new Bundle();
                Fragment fm = new View_time_fragment();
                args.putString("date", getdate);
                args.putString("store_id",store_id);
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();
            }
        } else if (id == R.id.tv_deli_add_address) {

            sessionManagement.updateSocity("", "");

            Fragment fm = new Add_delivery_address_fragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                    .addToBackStack(null).commit();

        }

    }

    private void getdate() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                R.style.datepicker,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        getdate = "" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                        tv_date.setText(getResources().getString(R.string.delivery_date) + getdate);

                        try {
                            String inputPattern = "yyyy-MM-dd";
                            String outputPattern = "dd-MM-yyyy";
                            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

                            Date date = inputFormat.parse(getdate);
                            String str = outputFormat.format(date);

                            tv_date.setText(getResources().getString(R.string.delivery_date) + str);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            tv_date.setText(getResources().getString(R.string.delivery_date) + getdate);
                        }

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();

    }

    private void attemptOrder() {

//        if(CustomRadioBtn.isChecked()||FastRadioBtn.isChecked())
//
//        {
        String location_id = "";
        String address = "";
        String house_no="";
        String newaddresss="";

        boolean cancel = false;

        if (CustomRadioBtn.isChecked()) {
            if (TextUtils.isEmpty(getdate)) {
                Toast.makeText(getActivity(), getResources().getString(R.string.please_select_date_time), Toast.LENGTH_SHORT).show();
                cancel = true;
            }
            if (TextUtils.isEmpty(time)) {
                Toast.makeText(getActivity(), "Select Time Slot", Toast.LENGTH_SHORT).show();
                cancel = true;
            }
        }

//        else if (TextUtils.isEmpty(gettime)) {
//            Toast.makeText(getActivity(), getResources().getString(R.string.please_select_date_time), Toast.LENGTH_SHORT).show();
//            cancel = true;
//        }

        if (!delivery_address_modelList.isEmpty()) {
            if (adapter.ischeckd()) {
                location_id = adapter.getlocation_id();
                address = adapter.getaddress();
                house_no=adapter.getHouse();
                newaddresss=adapter.getaddresss();
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.please_select_address), Toast.LENGTH_SHORT).show();
                cancel = true;
            }
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.please_add_address), Toast.LENGTH_SHORT).show();
            cancel = true;
        }

        /*if (TextUtils.isEmpty(getaddress)) {
            Toast.makeText(getActivity(), "Please add your address", Toast.LENGTH_SHORT).show();
            cancel = true;
        }*/

        if (!cancel) {
            //Toast.makeText(getActivity(), "date:"+getdate+"Fromtime:"+getfrom_time+"Todate:"+getto_time, Toast.LENGTH_SHORT).show();

            sessionManagement.cleardatetime();


                if (FastRadioBtn.isChecked())
                {
                    Bundle args = new Bundle();
                    Fragment fm = new Delivery_payment_detail_fragment();
                    args.putString("delivery_method","instantdelivery");
                    args.putString("getdate", formattedDate);
                    args.putString("time", currentTime1);
                    args.putString("location_id", location_id);
                    args.putString("address", address);
                    args.putString("newaddresss", newaddresss);
                    int charge = getArguments().getInt("charges");
                    String chargeS = String.valueOf(charge);
                    args.putString("deli_charges", chargeS);
                    args.putString("storeid", store_id);
                    args.putString("house_no",house_no);
                    fm.setArguments(args);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                            .addToBackStack(null).commit();
                }else if (CustomRadioBtn.isChecked())
                {
                    Bundle args = new Bundle();
                    Fragment fm = new Delivery_payment_detail_fragment();
                    args.putString("delivery_method", "customdelivery");
                    args.putString("getdate", getdate);
                    args.putString("time", time);
                    args.putString("location_id", location_id);
                    args.putString("address", address);
                    args.putString("newaddresss", newaddresss);
                    int charge = getArguments().getInt("charges");
                    String chargeS = String.valueOf(charge);
                    args.putString("deli_charges", chargeS);
                    args.putString("storeid", store_id);
                    args.putString("house_no",house_no);
                    fm.setArguments(args);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                            .addToBackStack(null).commit();
                   } else {

                    Toast.makeText(getActivity(), "Select delivery method first", Toast.LENGTH_SHORT).show();
                }
//            if (store_id.equals("232")) {
//                args.putString("delivery_method", "nextdaydelivery");
//                args.putString("getdate", tomorrowAsString);
//                args.putString("time", currentTime1);
//            } else {
//
//                args.putString("delivery_method", "fastdelivery");
//                args.putString("getdate", formattedDate);
//                args.putString("time", currentTime1);
//
//            }
          // Toast.makeText(getActivity(), location_id+address, Toast.LENGTH_SHORT).show();

//            args.putString("location_id", location_id);
//            args.putString("address", address);
//            args.putString("newaddresss", newaddresss);
//            int charge = getArguments().getInt("charges");
//            String chargeS = String.valueOf(charge);
//            args.putString("deli_charges", chargeS);
//            args.putString("storeid", store_id);
//            args.putString("house_no",house_no);
//            fm.setArguments(args);
//            FragmentManager fragmentManager = getFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                    .addToBackStack(null).commit();

        }


//        }else {
//
//            Toast.makeText(getActivity(), "Please select your delivery type", Toast.LENGTH_SHORT).show();
//        }
        //String getaddress = et_address.getText().toString();

    }


    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeGetAddressRequest(String user_id) {

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
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        delivery_address_modelList.clear();

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Delivery_address_model>>() {
                        }.getType();

                        delivery_address_modelList = gson.fromJson(response.getString("data"), listType);

                        //RecyclerView.Adapter adapter1 = new Delivery_get_address_adapter(delivery_address_modelList);
                        adapter = new Delivery_get_address_adapter(delivery_address_modelList);
                        ((Delivery_get_address_adapter) adapter).setMode(Attributes.Mode.Single);
                        rv_address.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        if (delivery_address_modelList.isEmpty()) {
                            if (getActivity() != null) {
                                //Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                            }
                        }

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
                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    @Override
    public void onPause() {
        super.onPause();
        // unregister reciver
        getActivity().unregisterReceiver(mCart);
    }

    @Override
    public void onResume() {
        super.onResume();
        // register reciver
        getActivity().registerReceiver(mCart, new IntentFilter("Grocery_delivery_charge"));
    }

    // broadcast reciver for receive data
    private BroadcastReceiver mCart = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String type = intent.getStringExtra("type");

            if (type.contentEquals("update")) {
                //updateData();
               // deli_charges = intent.getStringExtra("charge");
                int charge = getArguments().getInt("charges");
                int from = getArguments().getInt("from");
               // Toast.makeText(context, charge, Toast.LENGTH_SHORT).show();
                //Toast.makeText(getActivity(), deli_charges, Toast.LENGTH_SHORT).show();


                Double total = Double.parseDouble(db_cart.getTotalAmount()) + charge;


                tv_total.setText("" + db_cart.getTotalAmount() +" = "  + total+ getActivity().getResources().getString(R.string.currency));
            }
        }
    };




    private void makeslotrequest(String storeid)
    {
        final AlertDialog loading=new ProgressDialog(getActivity());
        loading.setMessage("Loading...");
        loading.setCancelable(false);
        loading.show();

        String tag_json_obj = "json_product_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("store_id",storeid);

        // Toast.makeText(getActivity(), Store_id, Toast.LENGTH_SHORT).show();

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_STORE_SLOT, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    loading.dismiss();

                    Boolean status = response.getBoolean("responce");
                    //      Toast.makeText(getActivity(), String.valueOf(status), Toast.LENGTH_SHORT).show();
                    if(status) {

                        dmethod=response.getString("times");
                        if(dmethod.equalsIgnoreCase("instant")){
//
            FastRadioBtn.setVisibility(View.VISIBLE);
            FastRadioBtn.setChecked(true);
            nextdaytext.setText("your order will be delivered within one hour");
        }else if (dmethod.equalsIgnoreCase("slot")){
            CustomRadioBtn.setVisibility(View.VISIBLE);
            CustomRadioBtn.setChecked(true);
            tv_date.setVisibility(View.VISIBLE);
            tv_time.setVisibility(View.VISIBLE);
        }else {
            Toast.makeText(getActivity(), "Cannot deliver", Toast.LENGTH_SHORT).show();
        }
                    }else{
                        dmethod="no";
                    }



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
