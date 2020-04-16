package Fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.Get_membership_adapter;
import Adapter.Home_Icon_Adapter;
import Adapter.Master_category_adapter;
import Config.BaseURL;
import Model.Home_Icon_model;
import Model.Master_category;
import Model.get_membership_model;
import gogrocer.tcc.AppController;
import gogrocer.tcc.R;
import util.CustomVolleyJsonRequest;

public class Membership_fragment extends Fragment {
    private Get_membership_adapter membership_adapter;
    private List<get_membership_model> membership_models = new ArrayList<>();
    RecyclerView rv_items;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.membership_fragment, container, false);

        rv_items = (RecyclerView) view.findViewById(R.id.membership_rv);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        rv_items.setLayoutManager(gridLayoutManager);
        rv_items.setItemAnimator(new DefaultItemAnimator());
        makeGetCategoryRequest();


        return view;
    }
    private void makeGetCategoryRequest() {
        final AlertDialog loading=new ProgressDialog(getActivity());
        loading.setMessage("Loading...");
        loading.setCancelable(false);
        loading.show();

        String tag_json_obj = "json_category_req";

        Map<String, String> params = new HashMap<String, String>();


       /* if (parent_id != null && parent_id != "") {
        }*/

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_MEMBERSHIP, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                        loading.dismiss();
                        Boolean status = response.getBoolean("response");

                        if (status) {

                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<get_membership_model>>() {
                    }.getType();
                    membership_models = gson.fromJson(response.getString("data"), listType);
                    membership_adapter = new Get_membership_adapter(membership_models);
                    rv_items.setAdapter(membership_adapter);
                    membership_adapter.notifyDataSetChanged();
                        }else {
                            Toast.makeText(getActivity(), "No Record Found", Toast.LENGTH_SHORT).show();
                        }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
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
