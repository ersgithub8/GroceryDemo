package gogrocer.tcc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

public class ChangePassword extends AppCompatActivity {
    private static String TAG = ForgotActivity.class.getSimpleName();
    EditText setpassword;
    RelativeLayout btn_newpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        setpassword = findViewById(R.id.setpassword);
        btn_newpassword = findViewById(R.id.btn_newpassword);
        Intent i = getIntent();
        final String number = i.getStringExtra("phone");
        btn_newpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newpasswrd = setpassword.getText().toString();
                if (TextUtils.isEmpty(newpasswrd)) {
                    Toast.makeText(ChangePassword.this, "Please Enter New Password", Toast.LENGTH_SHORT).show();
                } else {

                    makeForgotRequest(number, newpasswrd);
                }
            }
        });

    }

    private void makeForgotRequest(String number, final String newpasswrd) {

        // Tag used to cancel the request
        String tag_json_obj = "json_forgot_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("number", number);
        params.put("password", newpasswrd);

//        Toast.makeText(this, email, Toast.LENGTH_SHORT).show();
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.FORGOT_URL_password, params, new Response.Listener<JSONObject>() {

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
                        Intent i = new Intent(ChangePassword.this, LoginActivity.class);
                        Toast.makeText(ChangePassword.this, "Password is Successfully Updated...", Toast.LENGTH_SHORT).show();
                        startActivity(i);
                        finish();

                    } else {
                        Toast.makeText(ChangePassword.this, error, Toast.LENGTH_SHORT).show();


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
                    Toast.makeText(ChangePassword.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
}
