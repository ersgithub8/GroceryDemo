package Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
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

import Adapter.Master_Subcat;
import Config.BaseURL;
import Model.Sub_Categories;
import gogrocer.tcc.AppController;
import gogrocer.tcc.R;
import util.CustomVolleyJsonRequest;
import util.RecyclerTouchListener;

public class Subsub_categories extends Fragment {
    private Master_Subcat master_subcat_adapter;
    private static String TAG = Subcategory_fragment.class.getSimpleName();
    private List<Sub_Categories> subcat_models = new ArrayList<>();
    Fragment fm = null;
    RecyclerView rv_subsubcategories;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.subsub_categories, container, false);


        rv_subsubcategories=view.findViewById(R.id.subsubcat_id);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        rv_subsubcategories.setLayoutManager(gridLayoutManager);
        rv_subsubcategories.setItemAnimator(new DefaultItemAnimator());
        rv_subsubcategories.setNestedScrollingEnabled(true);
        String cat_id=getArguments().getString("subcat_id");
        makeGetCategoryRequest(cat_id);
        rv_subsubcategories.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_subsubcategories, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String  getid = subcat_models.get(position).getId();
                Bundle args = new Bundle();


                fm =new StoreFragment();
                args.putString("subsubcat_id", getid);

                fm.setArguments(args);
                FragmentManager fragmentManager1=getFragmentManager();
                fragmentManager1.beginTransaction().replace(R.id.contentPanel,fm).addToBackStack(null).commit();


            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


        return view;
    }
    private void makeGetCategoryRequest(String cat_id) {
        String tag_json_obj = "json_category_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("cat_id", cat_id);

       /* if (parent_id != null && parent_id != "") {
        }*/

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_SUBSUB_CATEGORY_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Cat", response.toString());
                try {
//                     Boolean status = response.getBoolean("response");
//                    Toast.makeText(getActivity(), String.valueOf(status), Toast.LENGTH_SHORT).show();
//                    if (status) {

                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Sub_Categories>>() {
                            }.getType();
                            subcat_models = gson.fromJson(response.getString("data"), listType);
                            master_subcat_adapter = new Master_Subcat(subcat_models);
                            rv_subsubcategories.setAdapter(master_subcat_adapter);
                            master_subcat_adapter.notifyDataSetChanged();
//                    } else {
//                        Toast.makeText(getActivity(), "No Data found", Toast.LENGTH_SHORT).show();
//                    }
//
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
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
