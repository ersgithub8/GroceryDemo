package Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Config.BaseURL;
import Model.Slot_Model;
import gogrocer.tcc.AppController;
import gogrocer.tcc.MainActivity;
import gogrocer.tcc.R;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;
import util.RecyclerTouchListener;
import util.Session_management;

public class View_time_fragment extends Fragment {

    private static String TAG = View_time_fragment.class.getSimpleName();

    private RecyclerView rv_time;

    private List<String> time_list = new ArrayList<>();
    private List<Slot_Model> slot_models = new ArrayList<>();


    private String getdate;
    LinearLayout lmor,lnoon,lnight;
    TextView tv_mor,tv_noon,tv_night;

    private Session_management sessionManagement;

    public View_time_fragment() {
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
        View view = inflater.inflate(R.layout.fragment_time_list, container, false);

        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.delivery_time));

        sessionManagement = new Session_management(getActivity());

        tv_mor=view.findViewById(R.id.morning_time);
        tv_noon=view.findViewById(R.id.noon_time);
        tv_night=view.findViewById(R.id.night_time);
        lmor=view.findViewById(R.id.layout1);
        lnoon=view.findViewById(R.id.layout2);
        lnight=view.findViewById(R.id.layout3);
        rv_time = (RecyclerView) view.findViewById(R.id.rv_times);
        rv_time.setLayoutManager(new LinearLayoutManager(getActivity()));

        getdate = getArguments().getString("date");
     String   getid = getArguments().getString("store_id");

        // check internet connection
        if (ConnectivityReceiver.isConnected()) {
            makeGetTimeRequest(getdate,getid);
        } else {
            ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
        }

        rv_time.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_time, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                String gettime = time_list.get(position);

                sessionManagement.cleardatetime();

                sessionManagement.creatdatetime(getdate,gettime);

                ((MainActivity) getActivity()).onBackPressed();

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        lmor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gettime = tv_mor.getText().toString();

                sessionManagement.cleardatetime();

                sessionManagement.creatdatetime(getdate,gettime);

                ((MainActivity) getActivity()).onBackPressed();

            }
        });
        lnoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gettime = tv_noon.getText().toString();

                sessionManagement.cleardatetime();

                sessionManagement.creatdatetime(getdate,gettime);

                ((MainActivity) getActivity()).onBackPressed();

            }
        });
        lnight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gettime = tv_night.getText().toString();

                sessionManagement.cleardatetime();

                sessionManagement.creatdatetime(getdate,gettime);

                ((MainActivity) getActivity()).onBackPressed();

            }
        });

        return view;
    }

    /**
     * Method to make json object request where json response starts wtih {
     */
    private void makeGetTimeRequest(String date,String storeid) {

        // Tag used to cancel the request
        String tag_json_obj = "json_time_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("date",date);
        params.put("store_id",storeid);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_TIME_SLOT_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
//                    Toast.makeText(getActivity(), status.toString(), Toast.LENGTH_SHORT).show();
                    if (status) {
                        JSONArray jsonArray = response.getJSONArray("times");
//                        Toast.makeText(getActivity(), jsonArray.toString(), Toast.LENGTH_SHORT).show();
                        for (int i=0;i<jsonArray.length();i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            if (object.getString("morning_slot_start").equals("")||object.getString("morning_slot_end").equals("")){
                                lmor.setVisibility(View.INVISIBLE);

                            }else {
                                tv_mor.setText(object.getString("morning_slot_start")+"-"+object.getString("morning_slot_end"));
                            }
                            if (object.getString("noon_slot_start").equals("")||object.getString("noon_slot_end").equals("")){
                                lnoon.setVisibility(View.INVISIBLE);

                            }else {
                                tv_noon.setText(object.getString("noon_slot_start")+"-"+object.getString("noon_slot_end"));
                            }
                            if (object.getString("night_slot_start").equals("")||object.getString("night_slot_end").equals("")){
                                lnight.setVisibility(View.INVISIBLE);

                            }else {
                                tv_night.setText(object.getString("night_slot_start")+"-"+object.getString("night_slot_end"));
                            }
                        }



                    }else {
                        Toast.makeText(getActivity(), "Time slot not available Select another date", Toast.LENGTH_SHORT).show();
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

}
