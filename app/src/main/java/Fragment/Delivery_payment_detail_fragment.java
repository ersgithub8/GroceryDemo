package Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Config.BaseURL;
import Config.SharedPref;
import cn.pedant.SweetAlert.SweetAlertDialog;
import gogrocer.tcc.AppController;
import gogrocer.tcc.LoginActivity;
import gogrocer.tcc.MainActivity;
import gogrocer.tcc.R;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;
import util.DatabaseHandler;
import util.Session_management;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Rajesh Dabhi on 29/6/2017.
 */

public class Delivery_payment_detail_fragment extends Fragment {

    private static String TAG = Delivery_payment_detail_fragment.class.getSimpleName();

    private TextView tv_timeslot, tv_address, tv_total,tv_grandtotal,tv_discount;
    ImageView membership_tv;
    private LinearLayout btn_order;

    private String getlocation_id = "";
    private String gettime = "";
    private String getdate = "";
    private String getuser_id = "";
    private String getstore_id = "";
    String delivery_method;
    private int deli_charges;
    String time;
    String date;
    Double total,a;
    LinearLayout layout1,layout2;
SharedPreferences preferences;
    String unit;
    private DatabaseHandler db_cart;
    private Session_management sessionManagement;

    public Delivery_payment_detail_fragment() {
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
        View view = inflater.inflate(R.layout.fragment_confirm_order, container, false);

        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.payment));

        db_cart = new DatabaseHandler(getActivity());
        sessionManagement = new Session_management(getActivity());

        tv_timeslot = (TextView) view.findViewById(R.id.textTimeSlot);
        membership_tv = (ImageView) view.findViewById(R.id.membership_tv);
        tv_discount = (TextView) view.findViewById(R.id.tvdiscount);
        tv_address = (TextView) view.findViewById(R.id.txtAddress);
        //tv_item = (TextView) view.findViewById(R.id.textItems);
        //tv_total = (TextView) view.findViewById(R.id.textPrice);
        tv_total = (TextView) view.findViewById(R.id.txtTotal);

        tv_grandtotal = (TextView) view.findViewById(R.id.txtgrandTotal);
        btn_order = (LinearLayout) view.findViewById(R.id.btn_order_now);
        layout1 = (LinearLayout) view.findViewById(R.id.layout1);
        layout2 = (LinearLayout) view.findViewById(R.id.layout2);
        membership_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManagement.isLoggedIn()){
                    Fragment fm = new Membership_fragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                            .addToBackStack(null).commit();

                }else {
                    Intent inten=new Intent(getActivity(), LoginActivity.class);
                    startActivity(inten);
                }
            }
        });

        getdate = getArguments().getString("getdate");
         String user_id=sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
        membership(user_id);

        preferences = getActivity().getSharedPreferences("lan", MODE_PRIVATE);
        String language=preferences.getString("language","");
        if (language.contains("spanish")) {
            gettime = getArguments().getString("time");

            gettime=gettime.replace("PM","م");
            gettime=gettime.replace("AM","ص");

        }else {
            gettime = getArguments().getString("time");

        }
        getlocation_id = getArguments().getString("location_id");
        getstore_id = getArguments().getString("storeid");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String name = preferences.getString("delivery_charges", "");
        String city = preferences.getString("citid","");
        String area = preferences.getString("areid","");
        String apartm = preferences.getString("apartid", "");

        deli_charges = Integer.parseInt(name);
        String getaddress = getArguments().getString("address");
        String newgetaddresss = getArguments().getString("newaddresss");
        final String house_no=getArguments().getString("house_no");
        delivery_method = getArguments().getString("delivery_method");
        date = getArguments().getString("getdate");
        time = getArguments().getString("time");
        deliverycaharges(city,area,apartm,db_cart.getTotalAmount());

        if (getdate.equals("")&&gettime.equals(""))
        {
            tv_timeslot.setText(delivery_method);
        }else {
            tv_timeslot.setText(getdate+"\n"+time);
        }
       // Toast.makeText(getActivity(), newgetaddresss, Toast.LENGTH_SHORT).show();

        tv_address.setText(getaddress);

        total = Double.parseDouble(db_cart.getTotalAmount()) + deli_charges;

        //tv_total.setText("" + db_cart.getTotalAmount());
        //tv_item.setText("" + db_cart.getWishlistCount());
        tv_total.setText(getResources().getString(R.string.tv_cart_item)+ db_cart.getCartCount() + "\n" +
                getResources().getString(R.string.amount)+ db_cart.getTotalAmount() + "\n" +
                getResources().getString(R.string.delivery_charge)+ deli_charges + "\n" +
                getResources().getString(R.string.total_amount) +
                db_cart.getTotalAmount() + " + " + deli_charges + " = " + total+ getResources().getString(R.string.currency));


        //Toast.makeText(getActivity(), String.valueOf(deli_charges), Toast.LENGTH_SHORT).show();

        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConnectivityReceiver.isConnected()) {
                    Fragment fm = new Payment_fragment();
                    Bundle args = new Bundle();
                    args.putString("total", String.valueOf(a));
                    args.putString("getdate", date);
                    args.putString("delivery_method", delivery_method);
                    args.putString("gettime", time);
                    args.putString("getlocationid", getlocation_id);
                    args.putString("getstoreid", getstore_id);
                    args.putString("delivery_charge",String.valueOf(deli_charges));
                    args.putString("house_no",house_no);
                    fm.setArguments(args);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                            .addToBackStack(null).commit();
                    SharedPref.putString(getActivity(),BaseURL.TOTAL_AMOUNT, String.valueOf(total));
                } else {
                    ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
                }
            }
        });

        return view;
    }

     private void membership(String userid) {
         final AlertDialog loading=new ProgressDialog(getActivity());
         loading.setMessage("Loading...");
         loading.setCancelable(false);
         loading.show();

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id",userid);

       /* if (parent_id != null && parent_id != "") {
        }*/

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.CHECK_MEMBERSHIP, params, new Response.Listener<JSONObject>() {

            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    loading.dismiss();
                    if (response != null && response.length() > 0) {
                        Boolean status = response.getBoolean("response");
                        if (status) {
                            layout1.setVisibility(View.VISIBLE);
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i=0;i<jsonArray.length();i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                               tv_discount.setText( object.getString("discount")+"%");
                               Double discount=total*Integer.parseInt(object.getString("discount"))/100;
                                a=  (total-discount);
                               tv_grandtotal.setText("Grand Total : "+total+" - "+discount+" = "+a+getResources().getString(R.string.currency));


                            }


                        }else {
                            layout1.setVisibility(View.INVISIBLE);
                            layout2.setVisibility(View.VISIBLE);
                            a=total;
                        }

                    }
                } catch (Exception  e) {
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
                    loading.dismiss();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }


    private void deliverycaharges(String city,String area,String appartment,String amount) {
        final AlertDialog loading=new ProgressDialog(getActivity());
        loading.setMessage("Loading...");
        loading.setCancelable(false);
        loading.show();

        Map<String, String> params = new HashMap<String, String>();
        params.put("city_id",city);
        params.put("area_id",area);
        params.put("apartment_id",appartment);
        params.put("order_amount",amount);
        Toast.makeText(getActivity(), params+"", Toast.LENGTH_SHORT).show();
        /* if (parent_id != null && parent_id != "") {
        }*/

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.get_DeliveryCharges, params, new Response.Listener<JSONObject>() {

            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    loading.dismiss();
                    if (response != null && response.length() > 0) {
                        Boolean status = response.getBoolean("response");
                        if (status) {

                            String delivery_charges=response.getString("delivery_charges");

                            Toast.makeText(getActivity(), delivery_charges, Toast.LENGTH_SHORT).show();

                        }else {
                            SweetAlertDialog alertDialog=new SweetAlertDialog(getActivity(),1);
                            alertDialog.setCancelable(false);
                            alertDialog.setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    getActivity().onBackPressed();
                                }
                            }).setConfirmButtonBackgroundColor(Color.RED);
                            alertDialog.setTitleText("Delivery not availible in this area.");
                            alertDialog.show();
//                            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }

                    }
                } catch (Exception  e) {
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
                    loading.dismiss();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

}
