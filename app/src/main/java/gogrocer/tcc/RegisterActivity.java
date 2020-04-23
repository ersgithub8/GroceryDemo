package gogrocer.tcc;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
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

import java.util.HashMap;
import java.util.Map;

import Config.BaseURL;
import cn.pedant.SweetAlert.SweetAlertDialog;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;

public class RegisterActivity extends AppCompatActivity {

    private static String TAG = RegisterActivity.class.getSimpleName();

    private EditText et_phone, et_name, et_password, et_email,otp_et;
    private RelativeLayout btn_register;
    private TextView tv_phone, tv_name, tv_password, tv_email,otp_tv,terms;
    public EditText Referal_code;
    CheckBox checkBox;
    String referal;
    public String otp;
    int i=0,count=1;
    Handler handler=new Handler();
    public String code="0";
    public String getphone;
    public String getname;
    public String getpassword;
    public String getemail;
    SweetAlertDialog alertDialog;
    @Override
    protected void attachBaseContext(Context newBase) {



        newBase = LocaleHelper.onAttach(newBase);
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title

        setContentView(R.layout.activity_register);

        et_phone = (EditText) findViewById(R.id.et_reg_phone);
        et_name = (EditText) findViewById(R.id.et_reg_name);
        et_password = (EditText) findViewById(R.id.et_reg_password);
        et_email = (EditText) findViewById(R.id.et_reg_email);
        tv_password = (TextView) findViewById(R.id.tv_reg_password);
        tv_phone = (TextView) findViewById(R.id.tv_reg_phone);
        tv_name = (TextView) findViewById(R.id.tv_reg_name);
        tv_email = (TextView) findViewById(R.id.tv_reg_email);
        terms=(TextView)findViewById(R.id.terms);
        checkBox=(CheckBox)findViewById(R.id.checkBox);
        Referal_code = (EditText)findViewById(R.id.et_referal_code);
        btn_register = (RelativeLayout) findViewById(R.id.btnRegister);
        otp_et = (EditText)findViewById(R.id.otp_et);
        otp_tv=(TextView)findViewById(R.id.otp_tv);


        alertDialog=new SweetAlertDialog(RegisterActivity.this,1);
        alertDialog.setConfirmButtonBackgroundColor(Color.RED);
        alertDialog.setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
            }
        });

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(RegisterActivity.this, Terms_and_Condition.class);

                startActivity(i);
            }
        });


        otp_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isPhoneValid(et_phone.getText().toString().trim())){
                    new SweetAlertDialog(RegisterActivity.this,1)
                            .setConfirmButtonBackgroundColor(Color.RED)
                            .setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            }).setTitleText("Enter a valid phone number").show();

                    return;
                }
                if(i==0){
                get_otp();
                }else{
                    alertDialog.show();
                }


            }
        });



        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               otp=otp_et.getText().toString();
               if(code!="0") {
                   if (code.equals(otp)) {
                       attemptRegister();
                   } else {
                       Toast.makeText(RegisterActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                   }
               }else
               {
                   Toast.makeText(RegisterActivity.this, "Please Click On GET OTP", Toast.LENGTH_SHORT).show();
               }


            }
        });
    }

    private void attemptRegister() {

        tv_phone.setText(getResources().getString(R.string.et_login_phone_hint));
        tv_email.setText(getResources().getString(R.string.tv_login_email));
        tv_name.setText(getResources().getString(R.string.tv_reg_name_hint));
        tv_password.setText(getResources().getString(R.string.tv_login_password));

        tv_name.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_phone.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_password.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_email.setTextColor(getResources().getColor(R.color.dark_gray));
        getphone= et_phone.getText().toString();
        getname= et_name.getText().toString();
        getpassword= et_password.getText().toString();
        getemail = et_email.getText().toString();
        otp = otp_et.getText().toString();

         referal= Referal_code.getText().toString();





        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(getphone)) {
            tv_phone.setTextColor(getResources().getColor(R.color.black));
            focusView = et_phone;
            cancel = true;
        } else if (!isPhoneValid(getphone)) {
            tv_phone.setText(getResources().getString(R.string.phone_too_short));
            tv_phone.setTextColor(getResources().getColor(R.color.black));
            focusView = et_phone;
            cancel = true;
        }

        if (TextUtils.isEmpty(getname)) {
            tv_name.setTextColor(getResources().getColor(R.color.black));
            focusView = et_name;
            cancel = true;
        }

        if (TextUtils.isEmpty(getpassword)) {
            tv_password.setTextColor(getResources().getColor(R.color.black));
            focusView = et_password;
            cancel = true;
        } else if (!isPasswordValid(getpassword)) {
            tv_password.setText(getResources().getString(R.string.password_too_short));
            tv_password.setTextColor(getResources().getColor(R.color.black));
            focusView = et_password;
            cancel = true;
        }

        if (TextUtils.isEmpty(getemail)) {
            focusView = et_email;
            cancel = true;
        } else if (!isEmailValid(getemail)) {
            tv_email.setText(getResources().getString(R.string.invalide_email_address));
            tv_email.setTextColor(getResources().getColor(R.color.black));
            focusView = et_email;
            cancel = true;
        }
        if(!checkBox.isChecked())
        {
            focusView=checkBox;
            cancel=true;

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
                makeRegisterRequest(getname, getphone, getemail, getpassword);
            }
        }


    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private boolean isPhoneValid(String phoneno) {
        //TODO: Replace this with your own logic
        return phoneno.length() > 9;
    }

    /**
     * Method to make json object request where json response starts wtih
     */

    private void get_otp() {

        final AlertDialog loading=new ProgressDialog(RegisterActivity.this);
        loading.setMessage("Loading...");
        loading.setCancelable(false);
        loading.show();




        getphone= et_phone.getText().toString();
        getname= et_name.getText().toString();
        getpassword= et_password.getText().toString();
        getemail = et_email.getText().toString();
        otp = otp_et.getText().toString();

        // Tag used to cancel the request
        String tag_json_obj = "json_register_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", getphone);


        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_OTP, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    loading.dismiss();
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        code = response.getString("code");
//                        otp_tv.setVisibility(View.GONE);
                        i=1;
//                        Toast.makeText(RegisterActivity.this, "Please Wait for otp" , Toast.LENGTH_SHORT).show();
                        SweetAlertDialog alertDialog=new SweetAlertDialog(RegisterActivity.this,SweetAlertDialog.SUCCESS_TYPE);
                        alertDialog.setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
//                                onBackPressed();
                            }
                        }).setTitleText("An OTP send to your number. Please wait for OTP.")
                                .setCancelable(false);
                        alertDialog.setConfirmButtonBackgroundColor(Color.GREEN);
                        alertDialog.show();
                        timer();



                    } else {
                        String error = response.getString("error");
                        btn_register.setEnabled(true);
                        otp_tv.setVisibility(View.VISIBLE);
                        SweetAlertDialog alertDialog=new SweetAlertDialog(RegisterActivity.this,SweetAlertDialog.ERROR_TYPE);
                        alertDialog.setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
//                                onBackPressed();
                            }
                        }).setTitleText(error)
                                .setCancelable(false);

                        alertDialog.setConfirmButtonBackgroundColor(Color.RED);
                        alertDialog.show();
//                        Toast.makeText(RegisterActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    otp_tv.setVisibility(View.VISIBLE);
                    loading.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
                loading.dismiss();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    private void makeRegisterRequest(String name, String mobile,
                                     String email, String password) {


        final AlertDialog loading=new ProgressDialog(RegisterActivity.this);
        loading.setMessage("Loading...");
        loading.setCancelable(false);
        loading.show();


        // Tag used to cancel the request
        String tag_json_obj = "json_register_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_name", name);
        params.put("user_mobile", mobile);
        params.put("user_email", email);
        params.put("password", password);
        params.put("referal_code",referal);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.REGISTER_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    loading.dismiss();
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        String msg = response.getString("message");
                        Toast.makeText(RegisterActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                        btn_register.setEnabled(false);

                    } else {
                        String error = response.getString("error");
                        btn_register.setEnabled(true);
                        SweetAlertDialog alertDialog=new SweetAlertDialog(RegisterActivity.this,SweetAlertDialog.ERROR_TYPE);
                        alertDialog.setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
//                                onBackPressed();
                            }
                        }).setTitleText(error)
                                .setCancelable(false);

                        alertDialog.setConfirmButtonBackgroundColor(Color.RED);
                        alertDialog.show();
//                        Toast.makeText(RegisterActivity.this, "" + error, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                loading.dismiss();
                }
                loading.dismiss();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }




    public void timer(){
        count=0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (count<60)
                {
                    count++;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
//                            pg.setProgress(count);
                            alertDialog.setTitleText("An OTP already send to your number retry in "+(60-count)+" seconds.");
                            if(count==60){
                                alertDialog.dismiss();
                                i=0;
                            }
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }

}
