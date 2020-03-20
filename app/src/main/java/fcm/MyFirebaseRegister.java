package fcm;

import android.app.Activity;
import android.content.SharedPreferences;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import Config.BaseURL;
import util.ConnectivityReceiver;


/**
 * Created by subhashsanghani on 12/21/16.
 */

public class MyFirebaseRegister {

    Activity _context;
    public SharedPreferences settings;
    ConnectivityReceiver cd;

    public MyFirebaseRegister(Activity context) {
        this._context = context;
        settings = context.getSharedPreferences(BaseURL.PREFS_NAME, 0);
        cd=new ConnectivityReceiver();

    }
    public void RegisterUser(String user_id){
        // [START subscribe_topics]
        String token = FirebaseInstanceId.getInstance().getToken();
        FirebaseMessaging.getInstance().subscribeToTopic("grocery");
        // [END subscribe_topics]
      //  mLogTask = new fcmRegisterTask();
     //   mLogTask.execute(user_id,token);

    }








}
