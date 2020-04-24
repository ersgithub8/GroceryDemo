package Fragment;


import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
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

import Adapter.Area_Adapter;
import Adapter.New_Adapter;
import Adapter.New_search_Adapter;
import Adapter.Product_adapter;
import Adapter.Search_Vender_Adapter;
import Adapter.Search_adapter;
import Adapter.SuggestionAdapter;
import Config.BaseURL;
import Model.Product_model;
import Model.Search_Store_Model;
import gogrocer.tcc.AppController;
import gogrocer.tcc.MainActivity;
import gogrocer.tcc.R;
import util.CustomVolleyJsonRequest;
import util.RecyclerTouchListener;
import util.Session_management;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Vender_SearchFragment extends android.app.Fragment {

    public String cityname;

    //    String[] fruits = {"MIlk butter & cream", "Bread Buns & Pals", "Dals Mix Pack", "buns-pavs", "cakes", "Channa Dal", "Toor Dal", "Wheat Atta"
//            , "Beson", "Almonds", "Packaged Drinking", "Cola drinks", "Other soft drinks", "Instant Noodles", "Cup Noodles", "Salty Biscuits", "cookie", "Sanitary pads", "sanitary Aids"
//            , "Toothpaste", "Mouthwash", "Hair oil", "Shampoo", "Pure & pomace olive", "ICE cream", "Theme Egg", "Amul Milk", "AMul Milk Pack power", "kaju pista dd"};
    private AutoCompleteTextView acTextView;

    private RecyclerView rv_search,rv_searchp;
    EditText et_search;
   public Boolean status;
    private static String TAG = Area.class.getSimpleName();

    private List<Search_Store_Model> product_modelList = new ArrayList<>();
    private New_Adapter adapter_product;



    private List<Product_model> product_modelListp = new ArrayList<>();
    private Search_adapter adapter_productp;

    public Vender_SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_vender__search, container, false);



        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.search));

        SharedPreferences prefs = getActivity().getSharedPreferences("city", MODE_PRIVATE);
         cityname = prefs.getString("city_name", "No name defined");

         et_search=view.findViewById(R.id.search_edit);



        rv_search = (RecyclerView) view.findViewById(R.id.rv_searchs);
        rv_search.setLayoutManager(new LinearLayoutManager(getActivity()));
//        makeGetcity();
        rv_searchp = (RecyclerView) view.findViewById(R.id.rv_searchp);
        rv_searchp.setLayoutManager(new LinearLayoutManager(getActivity()));






        rv_search.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                rv_search, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String getid = product_modelList.get(position).getUser_id();

//               String getid = category_modelList.get(position).getId();
//               String getcat_title = category_modelList.get(position).getTitle();

              //  Toast.makeText(getActivity(), getid, Toast.LENGTH_SHORT).show();

                Bundle args = new Bundle();
                android.app.Fragment fm = new Product_fragment();
                args.putString("Store_id", getid);
                args.putString("category_id","all");
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

        }));


        rv_searchp.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                rv_searchp, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String getid = product_modelListp.get(position).getStoreid();

//               String getid = category_modelList.get(position).getId();
//               String getcat_title = category_modelList.get(position).getTitle();

                //  Toast.makeText(getActivity(), getid, Toast.LENGTH_SHORT).show();

                Bundle args = new Bundle();
                android.app.Fragment fm = new Product_fragment();
                args.putString("Store_id", getid);
                args.putString("category_id","all");
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

        }));





        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if(status){
//
//                   adapter_product.getFilter().filter(charSequence);
//                }else
//                {
//                   // no_record.setVisibility(View.VISIBLE);
//                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                makeGetcity(et_search.getText().toString());
//                makeGetProductRequest("%"+et_search.getText().toString()+"%");
            }
        });
        return view;
    }
//    private void makeGetVenderRequest() {
//
//        // Tag used to cancel the request
//        String tag_json_obj = "json_product_reqs";
//
//        Map<String, String> params = new HashMap<String, String>();
//
//        params.put("city",cityname);
//
//     //   Toast.makeText(getActivity(), search_text+cityname, Toast.LENGTH_SHORT).show();
//        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
//                BaseURL.GET_STORE_SEARCH, params, new Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject response) {
//
//
//                try {
//                     status = response.getBoolean("responce");
//                    if (status) {
//
//                        Gson gson = new Gson();
//                        Type listType = new TypeToken<List<Search_Store_Model>>() {
//                        }.getType();
//
//                        product_modelList = gson.fromJson(response.getString("data"), listType);
//                       adapter_product = new New_search_Adapter(product_modelList, getActivity());
//                        rv_search.setAdapter(adapter_product);
//
//                        if (getActivity() != null) {
//                            if (product_modelList.isEmpty()) {
//                                Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
//    }
    private void makeGetcity(String name)
    {
        final AlertDialog loading=new ProgressDialog(getActivity());
        loading.setMessage("Loading...");
        loading.setCancelable(false);
        loading.show();
        String tag_json_obj = "json_product_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("city",cityname);
        params.put("name",name);
        // Toast.makeText(getActivity(), Store_id, Toast.LENGTH_SHORT).show();

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_STORE_SEARCH, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {

                    loading.dismiss();
                    status = response.getBoolean("responce");
//                    Toast.makeText(getActivity(),String.valueOf(status), Toast.LENGTH_SHORT).show();

                    if(status) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Search_Store_Model>>() {
                        }.getType();

                        product_modelList = gson.fromJson(response.getString("data"), listType);


                        adapter_product = new New_Adapter(product_modelList);
                        rv_search.setAdapter(adapter_product);
                        adapter_product.notifyDataSetChanged();


                        Gson gson1 = new Gson();
                        Type listTypep = new TypeToken<List<Product_model>>() {
                        }.getType();

                        product_modelListp = gson1.fromJson(response.getString("products"), listTypep);


                        adapter_productp = new Search_adapter(product_modelListp,getActivity());
                        rv_searchp.setAdapter(adapter_productp);
                        adapter_productp.notifyDataSetChanged();
                    }else{
                        Session_management sessionManagement = new Session_management(getActivity());
                        String area_name="choose area";
                        String area_id= null;
                        sessionManagement.updateArea(area_name,area_id);
                        Toast.makeText(getActivity(), "No record found", Toast.LENGTH_SHORT).show();
                    }
//
//                    if(area_modelList.isEmpty()){
//                        if(getActivity() != null) {
//                            Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
//                        }
//                    }
////
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
                loading.dismiss();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    private void makeGetProductRequest(String search_text) {

        // Tag used to cancel the request
        String tag_json_obj = "json_product_req";



        Map<String, String> params = new HashMap<String, String>();
        params.put("search", search_text);
//        String Storeid = getArguments().getString("Store_id_S");
//        params.put("storeid",Storeid);
//        if(min.getText().toString().isEmpty()){
//            mins="";
//        }else{
//            mins=min.getText().toString();
//        }
//        if(max.getText().toString().isEmpty()){
//            maxs="";
//        }else{
//            maxs=min.getText().toString();
//        }

//        params.put("min",mins);
//        params.put("max",maxs);
        final AlertDialog loading=new ProgressDialog(getActivity());
        loading.setMessage("Loading...");
        loading.setCancelable(false);
        loading.show();
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_PRODUCT_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    loading.dismiss();
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Product_model>>() {
                        }.getType();

                        product_modelListp = gson.fromJson(response.getString("data"), listType);

                        Product_adapter adapter_product = new Product_adapter(product_modelListp, getActivity());
                        rv_searchp.setAdapter(adapter_product);
                        adapter_product.notifyDataSetChanged();


                        if (getActivity() != null) {
                            if (product_modelList.isEmpty()) {
                                Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                            }
                        }

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
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


}
