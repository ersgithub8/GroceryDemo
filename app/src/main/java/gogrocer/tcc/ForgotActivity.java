package gogrocer.tcc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import util.CustomVolleyJsonRequest;

public class ForgotActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = ForgotActivity.class.getSimpleName();
    private RelativeLayout btn_continue;
    private EditText et_email;
    private TextView tv_email;
    String lan;
    SharedPreferences preferences;

    @Override
    protected void attachBaseContext(Context newBase) {



        newBase = LocaleHelper.onAttach(newBase);
        super.attachBaseContext(newBase);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title

        setContentView(R.layout.activity_forgot);
        // Call the function callInstamojo to start payment here

        et_email = (EditText) findViewById(R.id.et_login_email);
        tv_email = (TextView) findViewById(R.id.tv_login_email);
        btn_continue = (RelativeLayout) findViewById(R.id.btnContinue);

        btn_continue.setOnClickListener(this);
        preferences = getSharedPreferences("lan", MODE_PRIVATE);
        lan=preferences.getString("language","");
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btnContinue) {
            //attemptForgot();
            String getemail = et_email.getText().toString();
           // Toast.makeText(this, getemail, Toast.LENGTH_SHORT).show();
            if (TextUtils.isEmpty(getemail))
            {
                Toast.makeText(this, "Enter Number", Toast.LENGTH_SHORT).show();
            }else {
                makeForgotRequest(getemail);
            }

            //Toast.makeText(this, "jkhk", Toast.LENGTH_SHORT).show();
        }
    }



    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeForgotRequest(final String email) {

        // Tag used to cancel the request
        String tag_json_obj = "json_forgot_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", email);
      //  Toast.makeText(this, email, Toast.LENGTH_SHORT).show();
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.FORGOT_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
               // Toast.makeText(ForgotActivity.this, String.valueOf(response), Toast.LENGTH_SHORT).show();

                try {
                    Boolean status = response.getBoolean("responce");
                    String error = response.getString("error");
//                    String error_arb=response.getString("error_arb");
                    if (status) {
//                        Toast.makeText(ForgotActivity.this, error, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(ForgotActivity.this, NewPasswordActivity.class);
                        Toast.makeText(ForgotActivity.this, "OTP is send on your mobile number", Toast.LENGTH_SHORT).show();
                        i.putExtra("otp",error);
                        i.putExtra("phone_no",email);
                        startActivity(i);
                        finish();

                    } else {
                        Toast.makeText(ForgotActivity.this, error, Toast.LENGTH_SHORT).show();


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
                    Toast.makeText(ForgotActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

}
