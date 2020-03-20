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

public class NewPasswordActivity extends AppCompatActivity {
    private static String TAG = ForgotActivity.class.getSimpleName();
    EditText otp_text;
    RelativeLayout btn_otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        otp_text = findViewById(R.id.otp_text);
        btn_otp = findViewById(R.id.btn_otp);


        Intent intent = getIntent();
        final String otp = intent.getStringExtra("otp");
        final String phone = intent.getStringExtra("phone_no");

        btn_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String get_otp_text = otp_text.getText().toString();
                if (TextUtils.isEmpty(get_otp_text)) {
                    Toast.makeText(NewPasswordActivity.this, "Enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    if (get_otp_text.equals(otp)) {
                        Intent i = new Intent(NewPasswordActivity.this, ChangePassword.class);
                        Toast.makeText(NewPasswordActivity.this, "OTP is Matched", Toast.LENGTH_SHORT).show();
                        i.putExtra("phone", phone);
                        startActivity(i);
                    } else {
                        Toast.makeText(NewPasswordActivity.this, "Wrong OTP", Toast.LENGTH_SHORT).show();
                    }
                }


//                if (TextUtils.isEmpty(getemail)) {
//                    Toast.makeText(NewPasswordActivity.this, "Enter Number", Toast.LENGTH_SHORT).show();
//                } else {
//                    makeForgotRequest(getemail);
//                }
            }
        });

    }
}
