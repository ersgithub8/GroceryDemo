package Fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import Adapter.Home_adapter;
import Adapter.Product_adapter;
import Adapter.deals_adapter_in_products;
import Config.BaseURL;
import Config.SharedPref;
import Model.Category_model;
import Model.Product_model;
import Model.Slider_subcat_model;
import gogrocer.tcc.AppController;
import gogrocer.tcc.CustomSlider;
import gogrocer.tcc.MainActivity;
import gogrocer.tcc.R;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;
import util.RecyclerTouchListener;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Rajesh Dabhi on 26/6/2017.
 */

public class Product_fragment extends Fragment {
    private static String TAG = Product_fragment.class.getSimpleName();
    private RecyclerView rv_cat;
    private TabLayout tab_cat;
    String Storeid;
    TextView storename;
    private  View product,deals;
    ArrayList<String> ids=new ArrayList<String>();
    ArrayList<String> name=new ArrayList<String>();
    private List<Category_model> category_modelList = new ArrayList<>();
    private List<Slider_subcat_model> slider_subcat_models = new ArrayList<>();
    private List<String> cat_menu_id = new ArrayList<>();
    private List<Product_model> product_modelList = new ArrayList<>();
    private Product_adapter adapter_product;
    private SliderLayout  banner_slider;
    public LinearLayout Search_layout;
    public RecyclerView rv_items;
    String language;
    SharedPreferences preferences;
    private Home_adapter adapter;
    private Button Product_btn,Deals_btn;
    ProgressDialog progressDialog;
    Spinner store;
    public Product_fragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        store=view.findViewById(R.id.spinnerstore);
        product=view.findViewById(R.id.pview);
        deals=view.findViewById(R.id.dview);
        storename=view.findViewById(R.id.sn);
        tab_cat = (TabLayout) view.findViewById(R.id.tab_cat);
        banner_slider = (SliderLayout) view.findViewById(R.id.relative_banner);
        rv_cat = (RecyclerView) view.findViewById(R.id.rv_subcategory);
        Product_btn =(Button)view.findViewById(R.id.product_btn);
        Deals_btn =(Button)view.findViewById(R.id.deals_btn);
        rv_cat.setLayoutManager(new LinearLayoutManager(getActivity()));
        String getcat_id = getArguments().getString("cat_id");
        String id = getArguments().getString("id");
        String get_deal_id = getArguments().getString("cat_deal");
        String get_top_sale_id = getArguments().getString("cat_top_selling");
        String getcat_title = getArguments().getString("cat_title");
        String category_id=getArguments().getString("category_id");
        progressDialog = new ProgressDialog(getActivity());
        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.tv_product_name));

        Storeid = getArguments().getString("Store_id");

        deals.setVisibility(View.INVISIBLE);
        product.setVisibility(View.VISIBLE);
        getStore(category_id);
   //     Toast.makeText(getActivity(), getcat_id, Toast.LENGTH_SHORT).show();
        Product_btn.setBackgroundResource(R.color.bg);
        Product_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                 Storeid = getArguments().getString("Store_id");
                product_modelList.clear();

                product.setVisibility(View.VISIBLE);
                deals.setVisibility(View.INVISIBLE);

                makeGetProductRequest(Storeid);
                Deals_btn.setBackgroundResource(android.R.color.transparent);
                Product_btn.setBackgroundResource(R.color.bg);
                progressDialog.show();


            }
        });
        Deals_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                 Storeid = getArguments().getString("Store_id");
                product_modelList.clear();

                deals.setVisibility(View.VISIBLE);
                product.setVisibility(View.INVISIBLE);
                makeGetProductRequest_by_Deals(Storeid);
                Deals_btn.setBackgroundResource(R.color.bg);
                Product_btn.setBackgroundResource(android.R.color.transparent);
                progressDialog.show();

            }
        });

        rv_items = (RecyclerView) view.findViewById(R.id.rv_home);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        rv_items.setLayoutManager(layoutManager);
//        recyclerView.getAdapter().notifyDataSetChanged();
//        recyclerView.getAdapter().notifyItemRangeChanged(0, recyclerView.getAdapter().getItemCount());

        rv_items.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_items, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String getid = category_modelList.get(position).getId();
                String getcat_title = category_modelList.get(position).getTitle();
//                Storeid = getArguments().getString("Store_id");
                makeGetProductsRequest(Storeid, getid);


//                Bundle args = new Bundle();
//                Fragment fm = new Product_fragment();
         //       Toast.makeText(getActivity(), getid + Storeid, Toast.LENGTH_SHORT).show();
//                args.putString("cat_id", getid);
//               args.putString("cat_title", getcat_title);
//                fm.setArguments(args);
//                FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                        .addToBackStack(null).commit();

            }


            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        Search_layout = (LinearLayout) view.findViewById(R.id.search_layout);

        Search_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
//                Storeid = getArguments().getString("Store_id");
                bundle.putString("Store_id_S", Storeid);
                Fragment fm = new Search_fragment();
                fm.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();

            }
        });







        // check internet connection
        if (ConnectivityReceiver.isConnected()) {
            //Shop by Catogary
            //makeGetCategoryRequest(getcat_id);
//             Storeid = getArguments().getString("Store_id");
            makeGetProductRequest(Storeid);
            //makeGetProductsRequest(Storeid,getcat_id);


            //Deal Of The Day Products
         //   makedealIconProductRequest(get_deal_id);
            //Top Sale Products
//            maketopsaleProductRequest(get_top_sale_id);
            makeGetSliderCategoryRequest(id);

            //Slider
            makeGetBannerSliderRequest();

        }

//        tab_cat.setVisibility(View.GONE);
//        tab_cat.setSelectedTabIndicatorColor(getActivity().getResources().getColor(R.color.white));
//
//        tab_cat.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                String getcat_id = cat_menu_id.get(tab.getPosition());
//                if (ConnectivityReceiver.isConnected()) {
//                    //Shop By Catogary Products
//                    makeGetProductRequest(getcat_id);
//                }
//
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//            }
//        });
        store.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    return;
                }
                Storeid=ids.get(position);
                makeGetProductRequest(ids.get(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    private void makeGetProductRequest_by_Deals(String storeid) {


        String tag_json_obj = "json_product_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("storeid", storeid);
        // params.put("type",Type);
       // Toast.makeText(getActivity(), storeid, Toast.LENGTH_SHORT).show();
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_DEALS_PRODUCT_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Product_model>>() {
                        }.getType();
                        product_modelList = gson.fromJson(response.getString("data"), listType);
                        //adapter_product = new Product_adapter(product_modelList, getActivity());
                        deals_adapter_in_products deals_adapter_in_products = new deals_adapter_in_products(product_modelList,getActivity());
                        rv_cat.setAdapter(deals_adapter_in_products);
                        deals_adapter_in_products.notifyDataSetChanged();

                        progressDialog.dismiss();
                        if (getActivity() != null) {
                            if (product_modelList.isEmpty()) {
                                  Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                                deals_adapter_in_products.notifyDataSetChanged();
                                product_modelList.clear();

                            }
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    product_modelList.clear();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    product_modelList.clear();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }


    private void makeGetCategoryRequest() {
        String tag_json_obj = "json_category_req";
        Boolean isSubcat = false;
        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");

//        String Storeid = getArguments().getString("Store_id");
//        params.put("storeid",Storeid);
       // params.putAll();
        isSubcat = true;
       /* if (parent_id != null && parent_id != "") {
        }*/

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_CATEGORY_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    if (response != null && response.length() > 0) {
                        Boolean status = response.getBoolean("responce");
                        if (status) {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Category_model>>() {
                            }.getType();
                            category_modelList = gson.fromJson(response.getString("data"), listType);
                            adapter = new Home_adapter(category_modelList);
                            rv_items.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
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

    /**
     * Method to make json object request where json response starts wtih
     */
    //Get Shop By Catogary
    private void makeGetCategoryRequest(final String parent_id) {
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", parent_id);
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_CATEGORY_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Category_model>>() {}.getType();
                        category_modelList = gson.fromJson(response.getString("data"), listType);
                        if (!category_modelList.isEmpty()) {
                            tab_cat.setVisibility(View.VISIBLE);
                            cat_menu_id.clear();
                            for (int i = 0; i < category_modelList.size(); i++) {
                                cat_menu_id.add(category_modelList.get(i).getId());
                                preferences = getActivity().getSharedPreferences("lan", MODE_PRIVATE);

                                language=preferences.getString("language","");
                                if (language.contains("english")) {
                                    tab_cat.addTab(tab_cat.newTab().setText(category_modelList.get(i).getTitle()));
                                }
                                else {
                                    tab_cat.addTab(tab_cat.newTab().setText(category_modelList.get(i).getArb_title()));

                                }
                            }
                        } else {
                           // makeGetProductRequest(parent_id);
                        }

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

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    //Get Shop By Catogary Products
    private void makeGetProductsRequest(String Store_id, String cat_id)
    {
        final ProgressDialog progressdialog =  new ProgressDialog(getActivity());
        progressdialog.setMessage("Please wait..");
        progressdialog.setCancelable(false);
        progressdialog.show();
        String tag_json_obj = "json_product_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("storeid", Store_id);
        params.put("cat_id", cat_id);
       // params.put("type",Type);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_PRODUCT_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        product_modelList.clear();
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Product_model>>() {
                        }.getType();
                        product_modelList = gson.fromJson(response.getString("data"), listType);
                        adapter_product = new Product_adapter(product_modelList, getActivity());

                        rv_cat.setAdapter(adapter_product);
                        adapter_product.notifyDataSetChanged();
                        if (getActivity() != null) {
                            if (product_modelList.isEmpty()) {
                                //  Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressdialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressdialog.dismiss();
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
    private void makeGetProductRequest(String Store_id)
    {
        final AlertDialog loading=new ProgressDialog(getActivity());
        loading.setMessage("Loading...");
        loading.show();

        String tag_json_obj = "json_product_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("storeid", Store_id);
        // params.put("type",Type);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_PRODUCT_URL, params, new Response.Listener<JSONObject>() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {

                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        loading.dismiss();
                        Gson gson = new Gson();
                        product_modelList.clear();
                        Type listType = new TypeToken<List<Product_model>>() {
                        }.getType();

                        product_modelList = gson.fromJson(response.getString("data"), listType);


                        if (getActivity() != null) {
                            if (product_modelList.isEmpty()) {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                            }
                        }

                        adapter_product = new Product_adapter(product_modelList, getActivity());
                        rv_cat.setAdapter(adapter_product);
                        adapter_product.notifyDataSetChanged();

//                        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("storename",MODE_PRIVATE);
//                        String abc=sharedPreferences.getString("sn","");
                        if(product_modelList.size()>0) {
                            String abc = product_modelList.get(0).getStorename();

                            storename.setText(abc);
                        }
                        progressDialog.dismiss();


                    }
                    else
                    {
                        product_modelList.clear();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    //Get shop by


    //Get Shop By Catogary
    private void makeGetSliderCategoryRequest(final String sub_cat_id) {
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("sub_cat", sub_cat_id);
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_SLIDER_CATEGORY_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Slider_subcat_model>>() {}.getType();
                        slider_subcat_models = gson.fromJson(response.getString("subcat"), listType);
                        if (!slider_subcat_models.isEmpty()) {
                            tab_cat.setVisibility(View.VISIBLE);
                            cat_menu_id.clear();
                            for (int i = 0; i < slider_subcat_models.size(); i++) {
                                cat_menu_id.add(slider_subcat_models.get(i).getId());
                                preferences = getActivity().getSharedPreferences("lan", MODE_PRIVATE);

                                language=preferences.getString("language","");
                                if (language.contains("english")) {
                                    tab_cat.addTab(tab_cat.newTab().setText(slider_subcat_models.get(i).getTitle()));
                                }
                                else {
                                    tab_cat.addTab(tab_cat.newTab().setText(slider_subcat_models.get(i).getArb_title()));

                                }
                            }
                        } else {
                          //  makeGetProductRequest(parent_id);
                        }

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

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }




    ////Get DEal Products
    private void makedealIconProductRequest(String cat_id) {
        String tag_json_obj = "json_product_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("dealproduct", cat_id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_ALL_DEAL_OF_DAY_PRODUCTS, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Product_model>>() {
                        }.getType();
                        product_modelList = gson.fromJson(response.getString("Deal_of_the_day"), listType);
                        adapter_product = new Product_adapter(product_modelList, getActivity());
                        rv_cat.setAdapter(adapter_product);
                        adapter_product.notifyDataSetChanged();
                        if (getActivity() != null) {
                            if (product_modelList.isEmpty()) {
                                Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                            }
                        }

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


    ////Get Top Sale Products
    private void maketopsaleProductRequest(String cat_id) {
        String tag_json_obj = "json_product_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("top_selling_product", cat_id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_ALL_TOP_SELLING_PRODUCTS, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Product_model>>() {
                        }.getType();
                        product_modelList = gson.fromJson(response.getString("top_selling_product"), listType);
                        adapter_product = new Product_adapter(product_modelList, getActivity());
                        rv_cat.setAdapter(adapter_product);
                        adapter_product.notifyDataSetChanged();
                        if (getActivity() != null) {
                            if (product_modelList.isEmpty()) {
                                Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                            }
                        }

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

    private void makeGetBannerSliderRequest() {
        JsonArrayRequest req = new JsonArrayRequest(BaseURL.GET_BANNER_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            ArrayList<HashMap<String, String>> listarray = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = (JSONObject) response.get(i);
                                HashMap<String, String> url_maps = new HashMap<String, String>();
                                url_maps.put("slider_title", jsonObject.getString("slider_title"));
                                url_maps.put("sub_cat", jsonObject.getString("sub_cat"));
                                url_maps.put("slider_image", BaseURL.IMG_SLIDER_URL + jsonObject.getString("slider_image"));
                                listarray.add(url_maps);
                            }
                            for (HashMap<String, String> name : listarray) {
                                CustomSlider textSliderView = new CustomSlider(getActivity());
                                textSliderView.description(name.get("")).image(name.get("slider_image")).setScaleType(BaseSliderView.ScaleType.Fit);
                                textSliderView.bundle(new Bundle());
                                textSliderView.getBundle().putString("extra", name.get("slider_title"));
                                textSliderView.getBundle().putString("extra", name.get("sub_cat"));
                                banner_slider.addSlider(textSliderView);
                                final String sub_cat = (String) textSliderView.getBundle().get("extra");
//                                textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
//                                    @Override
//                                    public void onSliderClick(BaseSliderView slider) {
//                                        //   Toast.makeText(getActivity(), "" + sub_cat, Toast.LENGTH_SHORT).show();
//                                        Bundle args = new Bundle();
//                                        Fragment fm = new Product_fragment();
//                                        args.putString("id", sub_cat);
//                                        fm.setArguments(args);
//                                        FragmentManager fragmentManager = getFragmentManager();
//                                        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                                                .addToBackStack(null).commit();
//                                    }
//                                });

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
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
        AppController.getInstance().addToRequestQueue(req);

    }

public void getStore(String catid){
    final AlertDialog loading=new ProgressDialog(getActivity());
    loading.setMessage("Loading...");
    loading.setCancelable(false);
    loading.show();

    Map<String,String> params=new Hashtable<String, String>();
    params.put("subCat_id",catid);

    CustomVolleyJsonRequest jsonObjectRequest=new CustomVolleyJsonRequest(Request.Method.POST
            , BaseURL.GET_Store_URL
            , params, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            loading.dismiss();

            Boolean status = null;
            try{
                status = response.getBoolean("response");

                if (status){

                    JSONArray jsonArray=response.getJSONArray("data");

                    name.clear();
                    ids.clear();
                    name.add("Select Store");
                    ids.add("0");
//                    Toast.makeText(getActivity(), jsonArray.length()+"", Toast.LENGTH_SHORT).show();
                    for(int i=0;i<jsonArray.length();i++){


                        JSONObject object=jsonArray.getJSONObject(i);
                        if(search(object.getString("user_fullname"))) {
                            name.add(object.getString("user_fullname"));
                            ids.add(object.getString("user_id"));
                        }
                        //Toast.makeText(Signin.this, jsonArray.getString(0), Toast.LENGTH_SHORT).show();

                    }

                    ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_spinner_item,name);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    store.setAdapter(adapter);


                }
                else {
                    Toast.makeText(getActivity(),response.getString("data"),Toast.LENGTH_SHORT).show();
                }




            } catch (JSONException e) {

                Toast.makeText(getActivity(), e+"", Toast.LENGTH_LONG).show();
            }
        }
    }
            , new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            loading.dismiss();
            Toast.makeText(getActivity(),error+"",Toast.LENGTH_SHORT).show();

        }
    });

    jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
        @Override
        public int getCurrentTimeout() {
            return 50000;
        }

        @Override
        public int getCurrentRetryCount() {
            return 50000;
        }

        @Override
        public void retry(VolleyError error) throws VolleyError {

        }
    });
    RequestQueue queue = Volley.newRequestQueue(getActivity());
    queue.add(jsonObjectRequest);
}


public boolean search(String name1){
//        int a=0;
        for(int i =0;i<name.size();i++){
            if (name.get(i).equals(name)){
                return false;
            }
        }
        return true;
}

}



