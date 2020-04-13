package Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.swipe.util.Attributes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.Bestselling;
import Adapter.Deal_OfDay_Adapter;
import Adapter.Home_Icon_Adapter;
import Adapter.Home_adapter;
import Adapter.Master_category_adapter;
import Adapter.Product_adapter;
import Adapter.Stores_adapter;
import Adapter.Top_Selling_Adapter;
import Adapter.View_address_adapter;
import Adapter.my_last_order_adapter;
import Config.BaseURL;
import Model.Category_model;
import Model.Deal_Of_Day_model;
import Model.Delivery_address_model;
import Model.Home_Icon_model;
import Model.Master_category;
import Model.My_Pending_order_model;
import Model.Product_model;
import Model.Store_model;
import Model.Top_Selling_model;
import gogrocer.tcc.AppController;
import gogrocer.tcc.CustomSlider;
import gogrocer.tcc.LoginActivity;
import gogrocer.tcc.MainActivity;
import gogrocer.tcc.My_Order_activity;
import gogrocer.tcc.R;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;
import util.RecyclerTouchListener;
import util.Session_management;

import static android.content.Context.MODE_PRIVATE;


public class Home_fragment extends Fragment {
    private static String TAG = Home_fragment.class.getSimpleName();
    Animation fade_in,move_left;
    private SliderLayout imgSlider, banner_slider, featuredslider;
    private RecyclerView rv_items, rv_top_selling, rv_deal_of_day, rv_headre_icons,rv_store,last_order_rv,rv_best_selling,rv_recomended;
    private List<Category_model> category_modelList = new ArrayList<>();
    private List<Store_model> store_modelList = new ArrayList<>();
    private Home_adapter adapter;
    private Stores_adapter stores_adapter;
    private boolean isSubcat = false;
    LinearLayout Search_layout;
    RelativeLayout rl_address;
    private Session_management sessionManagement;
    String getid,cat_id;
    String storename;
    Boolean status_member;
    String storeimg;
    String getcat_title;
    ScrollView scrollView;
     String user_id_for_last_order ="0";
    SharedPreferences sharedpreferences;
    public List<My_Pending_order_model> my_order_modelList = new ArrayList<>();
    Intent intent;

    //Home Icons
    private Home_Icon_Adapter menu_adapter;
    private List<Home_Icon_model> menu_models = new ArrayList<>();
//master categories
private Master_category_adapter master_adapter;
    private List<Master_category> master_models = new ArrayList<>();

    //Deal O Day
    private Deal_OfDay_Adapter deal_ofDay_adapter;
    private List<Deal_Of_Day_model> deal_of_day_models = new ArrayList<>();
    LinearLayout Deal_Linear_layout;
    FrameLayout Deal_Frame_layout, Deal_Frame_layout1;


    //Top Selling Products
    private Top_Selling_Adapter top_selling_adapter;
    private List<Top_Selling_model> top_selling_models = new ArrayList<>();
    //Best Selling Products
    private Bestselling bestselling;
    private List<Product_model> product_models = new ArrayList<>();

    //product model
    private Product_adapter productselling;



    private ImageView iv_Call, iv_Whatspp, iv_reviews, iv_share_via,membership_tv;
    private TextView timer,tv_address;
    Button View_all_deals, View_all_TopSell,btn1,btn2,btn3,nextDay_delivery;

    private ImageView Top_Selling_Poster, Deal_Of_Day_poster;

    View view;

    public Home_fragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    Fragment fm = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.app_name));
        ((MainActivity) getActivity()).updateHeader();
        membership_tv=view.findViewById(R.id.membership_tv);
        rl_address=view.findViewById(R.id.rl_address);
        tv_address=view.findViewById(R.id.address_tv_home1);
        btn1=(Button)view.findViewById(R.id.grocery_btn);
        btn2=(Button)view.findViewById(R.id.vegetable_btn);
        btn3=(Button)view.findViewById(R.id.fruits_btn);
        nextDay_delivery=(Button)view.findViewById(R.id.nextday_delivery);
        fade_in= AnimationUtils.loadAnimation(getActivity(),R.anim.fade_in);
        move_left=AnimationUtils.loadAnimation(getActivity(),R.anim.another_animation);
        //best selling products

        rv_best_selling= (RecyclerView) view.findViewById(R.id.best_selling_rv);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);

        rv_best_selling.setLayoutManager(layoutManager1);
        rv_best_selling.startAnimation(move_left);



        //Address
        if (ConnectivityReceiver.isConnected()) {
            sessionManagement=new Session_management(getActivity());

            if(sessionManagement.isLoggedIn()) {
                String user_id= sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
                // Toast.makeText(getActivity(), user_id, Toast.LENGTH_SHORT).show();
                makeGetAddressRequest(user_id);

            }
        }

//best selling end
        //recomended
        rv_recomended= (RecyclerView) view.findViewById(R.id.new_offer);
        LinearLayoutManager layoutManager4 = new LinearLayoutManager(getActivity());
        layoutManager4.setOrientation(LinearLayoutManager.HORIZONTAL);

        rv_recomended.setLayoutManager(layoutManager4);



        last_order_rv = (RecyclerView) view.findViewById(R.id.last_order_rv);
        last_order_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        final Session_management sessionManagement = new Session_management(getActivity());
        SharedPreferences editors = getActivity().getSharedPreferences("rating_store",MODE_PRIVATE);

        String israted = editors.getString("rating","null");



        user_id_for_last_order=sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
//
        if(sessionManagement.isLoggedIn()){
            if (israted.equalsIgnoreCase("null")){
                makeGetOrderRequest(user_id_for_last_order);

            }else {
                last_order_rv.setVisibility(View.INVISIBLE);
            }
            membership(user_id_for_last_order);
//            String city= sessionManagement.getUserDetails().get(BaseURL.KEY_SOCITY_NAME);
//            String area= sessionManagement.getUserDetails().get(BaseURL.KEY_AREA_NAME);
//            String apartment= sessionManagement.getUserDetails().get(BaseURL.KEY_APARTMENT_NAME);
//            if (city == ""||area==null||apartment==null) {
//                tv_address.setText("No Address Added");
//            }else{
//                tv_address.setText(city + "," + area + "," + apartment);
//            }

        }



        last_order_rv.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_items, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i = new Intent(getActivity(), My_Order_activity.class);
                startActivity(i);
                ((MainActivity) getActivity()).overridePendingTransition(0, 0);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));



        membership_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (sessionManagement.isLoggedIn()){
                    if (status_member){
                        Toast.makeText(getActivity(), "Membership Active", Toast.LENGTH_SHORT).show();
                    }else {
                        Fragment fm = new Membership_fragment();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                                .addToBackStack(null).commit();
                    }

                }else {
                    Intent inten=new Intent(getActivity(), LoginActivity.class);
                    startActivity(inten);
                }
            }
        });

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    ((MainActivity) getActivity()).finish();
                    return true;
                }
                return false;
            }
        });
        //Check Internet Connection
        if (ConnectivityReceiver.isConnected()) {
            makeGetSliderRequest();
            makeGetBannerSliderRequest();
            makeGetCategoryRequest();
//            makeGetstoreRequest();

            makeGetFeaturedSlider();
            make_menu_items();
            make_deal_od_the_day();
            make_top_selling();
            make_Best_selling();
            make_Recomended_selling();


        }

        View_all_deals = (Button) view.findViewById(R.id.view_all_deals);
        View_all_TopSell = (Button) view.findViewById(R.id.view_all_topselling);
        Deal_Frame_layout = (FrameLayout) view.findViewById(R.id.deal_frame_layout);
        Deal_Frame_layout1 = (FrameLayout) view.findViewById(R.id.deal_frame_layout1);
        Deal_Linear_layout = (LinearLayout) view.findViewById(R.id.deal_linear_layout);





        //Top Selling Poster
        Top_Selling_Poster = (ImageView) view.findViewById(R.id.top_selling_imageview);

        //Deal Of Day Poster
        Deal_Of_Day_poster = (ImageView) view.findViewById(R.id.deal_of_day_imageview);


        //Scroll View
        scrollView = (ScrollView) view.findViewById(R.id.scroll_view);
        scrollView.setSmoothScrollingEnabled(true);

        //Search
        Search_layout = (LinearLayout) view.findViewById(R.id.search_layout);

        Search_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fm = new Vender_SearchFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();

            }
        });
        //Slider
        imgSlider = (SliderLayout) view.findViewById(R.id.home_img_slider);
        banner_slider = (SliderLayout) view.findViewById(R.id.relative_banner);
        featuredslider = (SliderLayout) view.findViewById(R.id.featured_img_slider);
        //shop by store
//



        //Catogary Icons
        rv_items = (RecyclerView) view.findViewById(R.id.rv_home);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        rv_items.setLayoutManager(gridLayoutManager);
        rv_items.setItemAnimator(new DefaultItemAnimator());
        rv_items.setNestedScrollingEnabled(true);

        //DealOf the Day
        rv_deal_of_day = (RecyclerView) view.findViewById(R.id.rv_deal);

        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getActivity());
        layoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_deal_of_day.setLayoutManager(layoutManager3);
        rv_deal_of_day.startAnimation(fade_in);

        //Top Selling Products
        rv_top_selling = (RecyclerView) view.findViewById(R.id.top_selling_recycler);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_top_selling.setLayoutManager(layoutManager2);


        //make_menu_items Icons
        rv_headre_icons = (RecyclerView) view.findViewById(R.id.collapsing_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity()) {

            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(getActivity()) {
                    private static final float SPEED = 2000f;// Change this value (default=25f)

                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return SPEED / displayMetrics.densityDpi;
                    }
                };
                smoothScroller.setTargetPosition(position);
                startSmoothScroll(smoothScroller);
            }
        };
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_headre_icons.setLayoutManager(layoutManager);
        rv_headre_icons.setHasFixedSize(true);
        rv_headre_icons.setItemViewCacheSize(10);
        rv_headre_icons.setDrawingCacheEnabled(true);
        rv_headre_icons.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);



        //Recycler View Shop By Catogary
        rv_items.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_items, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                getid = master_models.get(position).getId();
                cat_id = product_models.get(position).getCategory_id();

                Bundle args = new Bundle();


                fm =new Subcategory_fragment();
                args.putString("cat_id", getid);
                args.putString("category_id", cat_id);
//                Toast.makeText(getActivity(), getid, Toast.LENGTH_SHORT).show();
                fm.setArguments(args);
                FragmentManager fragmentManager1=getFragmentManager();
                fragmentManager1.beginTransaction().replace(R.id.contentPanel,fm).addToBackStack(null).commit();


            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
//


        //Recycler View Menu Products
        rv_headre_icons.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_headre_icons, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                getid = menu_models.get(position).getId();
                Bundle args = new Bundle();
                Fragment fm = new Product_fragment();
                args.putString("cat_id", getid);
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        //Recycler View Deal Of Day
        rv_deal_of_day.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_deal_of_day, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
               // getid = deal_of_day_models.get(position).getId();

                String dealOfDayStoreId= deal_of_day_models.get(position).getStoreid();
                String dealof_Day_catId = deal_of_day_models.get(position).getId();
                cat_id=product_models.get(position).getCategory_id();
             //  Toast.makeText(getActivity(), dealOfDayStoreId, Toast.LENGTH_SHORT).show();

                Bundle args = new Bundle();
                Fragment fm = new Product_fragment();
                args.putString("Store_id", dealOfDayStoreId);
                args.putString("cat__id",dealof_Day_catId);
                args.putString("category_id",cat_id);
               // args.putString("store_type",getStore_type);
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();



            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));



        //REcyclerview Top Selling
        rv_top_selling.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_top_selling, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                getid = top_selling_models.get(position).getProduct_id();
//

                String StoreidForTopSelling = top_selling_models.get(position).getStoreid();
                Bundle args = new Bundle();
                Fragment fm = new Product_fragment();
                args.putString("Store_id", StoreidForTopSelling);
                args.putString("category_id", cat_id);


                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();




            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        View_all_TopSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                Fragment fm = new Product_fragment();
                args.putString("cat_top_selling", "2");
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();

            }
        });


        //best selling click listener
        rv_best_selling.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_best_selling, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                getid = product_models.get(position).getProduct_id();



                String StoreidForrecomendedSelling = product_models.get(position).getStoreid();
                cat_id=product_models.get(position).getCategory_id();


                Bundle args = new Bundle();
                Fragment fm = new Product_fragment();
                args.putString("Store_id", StoreidForrecomendedSelling);

                args.putString("Store_name",storename);
                args.putString("category_id",cat_id);
                 fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();




            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


        //rcomended for you click listener
        rv_recomended.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_recomended, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                getid = product_models.get(position).getProduct_id();


                String StoreidForBestSelling = product_models.get(position).getStoreid();
                cat_id=product_models.get(position).getCategory_id();
                Bundle args = new Bundle();
                Fragment fm = new Product_fragment();
                args.putString("Store_id", StoreidForBestSelling);

               // Toast.makeText(getActivity(), storename, Toast.LENGTH_SHORT).show();
                args.putString("Store_name",storename);
                args.putString("category_id",cat_id);

                // args.putString("store_type",getStore_type);
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


        return view;
    }





    private void makeGetOrderRequest(String userid) {
        String tag_json_obj = "json_socity_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userid);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_ORDER_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {


                    Boolean status = response.getBoolean("responce");

                    if(status) {



                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<My_Pending_order_model>>() {
                        }.getType();

                        my_order_modelList = gson.fromJson(response.getString("data"), listType);
                        my_last_order_adapter my_last_order_adapter = new my_last_order_adapter(my_order_modelList);
                        last_order_rv.setAdapter(my_last_order_adapter);
                        my_last_order_adapter.notifyDataSetChanged();
                    }else{

                        Toast.makeText(getActivity(), "No record found", Toast.LENGTH_SHORT).show();
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





    private void makeGetSliderRequest() {
        JsonArrayRequest req = new JsonArrayRequest(BaseURL.GET_SLIDER_URL,
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
                                imgSlider.addSlider(textSliderView);
                                final String sub_cat = (String) textSliderView.getBundle().get("extra");
                                textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                    @Override
                                    public void onSliderClick(BaseSliderView slider) {
                                        //   Toast.makeText(getActivity(), "" + sub_cat, Toast.LENGTH_SHORT).show();
                                        Bundle args = new Bundle();
                                        Fragment fm = new Product_fragment();
                                        args.putString("id", sub_cat);
                                        fm.setArguments(args);
                                        FragmentManager fragmentManager = getFragmentManager();
                                        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                                                .addToBackStack(null).commit();
                                    }
                                });


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
                                textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                    @Override
                                    public void onSliderClick(BaseSliderView slider) {
                                        //   Toast.makeText(getActivity(), "" + sub_cat, Toast.LENGTH_SHORT).show();
                                        Bundle args = new Bundle();
                                        Fragment fm = new Product_fragment();
                                        args.putString("id", sub_cat);
                                        fm.setArguments(args);
                                        FragmentManager fragmentManager = getFragmentManager();
                                        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                                                .addToBackStack(null).commit();
                                    }
                                });

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


    private void makeGetFeaturedSlider() {
        JsonArrayRequest req = new JsonArrayRequest(BaseURL.GET_FEAATURED_SLIDER_URL,
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
                                //  textSliderView.getBundle().putString("extra", name.get("slider_title"));
                                textSliderView.getBundle().putString("extra", name.get("sub_cat"));
                                featuredslider.addSlider(textSliderView);
                                final String sub_cat = (String) textSliderView.getBundle().get("extra");
//
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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


    private void makeGetCategoryRequest() {
        String tag_json_obj = "json_category_req";
        isSubcat = false;
        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");
        isSubcat = true;
       /* if (parent_id != null && parent_id != "") {
        }*/

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_MASTER_CATEGORY_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Cat", response.toString());
                try {
//                    if (response != null && response.length() > 0) {
//                        Boolean status = response.getBoolean("responce");
//                        if (status) {

                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Master_category>>() {
                            }.getType();
                            master_models = gson.fromJson(response.getString("data"), listType);
                            master_adapter = new Master_category_adapter(master_models);
                            rv_items.setAdapter(master_adapter);
                            master_adapter.notifyDataSetChanged();
//                        }
    //                    }
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




    private void make_deal_od_the_day() {
        String tag_json_obj = "json_category_req";
        isSubcat = false;
        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");
        isSubcat = true;
       /* if (parent_id != null && parent_id != "") {
        }*/

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.GET,
                BaseURL.GET_DEAL_OF_DAY_PRODUCTS, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    if (response != null && response.length() > 0) {
                        Boolean status = response.getBoolean("responce");
                        Gson gson = new Gson();
                        if (status) {
                            Type listType = new TypeToken<List<Deal_Of_Day_model>>() {
                            }.getType();
                            deal_of_day_models = gson.fromJson(response.getString("Deal_of_the_day"), listType);

                           // String Storeid = response.getString("storeid");
                          //  Toast.makeText(getActivity(), Storeid, Toast.LENGTH_SHORT).show();
                            deal_ofDay_adapter = new Deal_OfDay_Adapter(deal_of_day_models, getActivity());
                            rv_deal_of_day.setAdapter(deal_ofDay_adapter);
                            deal_ofDay_adapter.notifyDataSetChanged();
                            if (getActivity() != null) {
                                if (deal_of_day_models.isEmpty()) {
                                    //  Toast.makeText(getActivity(), "No Deal For Day", Toast.LENGTH_SHORT).show();
                                    rv_deal_of_day.setVisibility(View.GONE);
                                    Deal_Frame_layout.setVisibility(View.GONE);
                                    Deal_Frame_layout1.setVisibility(View.GONE);
                                    Deal_Linear_layout.setVisibility(View.GONE);
                                }
                            }
                        } else {
                            Toast.makeText(getActivity(), "No Response", Toast.LENGTH_SHORT).show();
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

    private void make_top_selling() {
        String tag_json_obj = "json_category_req";
        isSubcat = false;
        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");
        isSubcat = true;
       /* if (parent_id != null && parent_id != "") {
        }*/

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_TOP_SELLING_PRODUCTS, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    if (response != null && response.length() > 0) {
                        Boolean status = response.getBoolean("responce");
                        if (status) {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Top_Selling_model>>() {
                            }.getType();
                            top_selling_models = gson.fromJson(response.getString("top_selling_product"), listType);
                            top_selling_adapter = new Top_Selling_Adapter(top_selling_models);
                            rv_top_selling.setAdapter(top_selling_adapter);
                            top_selling_adapter.notifyDataSetChanged();
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
    private void membership(String userid) {


        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id",userid);

       /* if (parent_id != null && parent_id != "") {
        }*/

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.CHECK_MEMBERSHIP, params, new Response.Listener<JSONObject>() {

            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    if (response != null && response.length() > 0) {
                        status_member = response.getBoolean("response");
                        if (status_member) {



                        }else {
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
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

//best selling products
private void make_Best_selling() {
    String tag_json_obj = "json_category_req";
    isSubcat = false;
    Map<String, String> params = new HashMap<String, String>();
    params.put("parent", "");
    isSubcat = true;
       /* if (parent_id != null && parent_id != "") {
        }*/

    CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
            BaseURL.GET_BEST_SELLING_PRODUCTS, params, new Response.Listener<JSONObject>() {

        @Override
        public void onResponse(JSONObject response) {
            Log.d(TAG, response.toString());

            try {
                if (response != null && response.length() > 0) {
                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Product_model>>() {
                        }.getType();
                        product_models = gson.fromJson(response.getString("data"), listType);
                        bestselling = new Bestselling(product_models);
                        rv_best_selling.setAdapter(bestselling);
                        bestselling.notifyDataSetChanged();
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

//recomended for you
private void make_Recomended_selling() {
    String tag_json_obj = "json_category_req";
    isSubcat = false;
    Map<String, String> params = new HashMap<String, String>();
    params.put("parent", "");
    isSubcat = true;
       /* if (parent_id != null && parent_id != "") {
        }*/

    CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
            BaseURL.GET_BEST_SELLING_PRODUCTS, params, new Response.Listener<JSONObject>() {

        @Override
        public void onResponse(JSONObject response) {
            Log.d(TAG, response.toString());

            try {
                if (response != null && response.length() > 0) {
                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Product_model>>() {
                        }.getType();
                        product_models = gson.fromJson(response.getString("data"), listType);
                        bestselling = new Bestselling(product_models);
                        rv_recomended.setAdapter(bestselling);
                        bestselling.notifyDataSetChanged();
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

    private void make_menu_items() {
        String tag_json_obj = "json_category_req";
        isSubcat = false;
        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");
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
                            Type listType = new TypeToken<List<Home_Icon_model>>() {
                            }.getType();
                            menu_models = gson.fromJson(response.getString("data"), listType);
                            menu_adapter = new Home_Icon_Adapter(menu_models);
                            rv_headre_icons.setAdapter(menu_adapter);
                            menu_adapter.notifyDataSetChanged();
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


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        //Defining retrofit api service

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public void reviewOnApp() {
        Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
        }
    }

    public void shareApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi friends i am using ." + " http://play.google.com/store/apps/details?id=" + getActivity().getPackageName() + " APP");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
//    public  boolean isPermissionGranted() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (getContext().checkSelfPermission(android.Manifest.permission.CALL_PHONE)
//                    == PackageManager.PERMISSION_GRANTED) {
//                Log.v("TAG","Permission is granted");
//                return true;
//            } else {
//
//                Log.v("TAG","Permission is revoked");
//                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
//                return false;
//            }
//        }
//        else { //permission is automatically granted on sdk<23 upon installation
//            Log.v("TAG","Permission is granted");
//            return true;
//        }
//    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//
//            case 1: {
//
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(getActivity(), "Permission granted", Toast.LENGTH_SHORT).show();
//                    call_action();
//                } else {
//                    Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
//                }
//                return;
//            }
//
//            // other 'case' lines to check for other
//            // permissions this app might request
//        }
//    }
//    public void call_action(){
//
//        Intent callIntent = new Intent(Intent.ACTION_CALL);
//        callIntent.setData(Uri.parse("tel:" + "919889887711"));
//        startActivity(callIntent);
//    }

    private void makeGetAddressRequest(String user_id) {
        final AlertDialog loading=new ProgressDialog(getActivity());
        loading.setCancelable(false);
        loading.setMessage("loading...");
//        loading.show();
        // Tag used to cancel the request
        String tag_json_obj = "json_get_address_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);


        final CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_ADDRESS_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    loading.dismiss();
                    Boolean status = response.getBoolean("responce");
                    JSONArray array=response.getJSONArray("data");
//                    Toast.makeText(getActivity(), array+"", Toast.LENGTH_SHORT).show();
                    if (status) {

//                        delivery_address_modelList.clear();

//                        Gson gson = new Gson();
//                        Type listType = new TypeToken<List<Delivery_address_model>>() {
//                        }.getType();


//                        for (int i=0;i<array.length();){
//                            JSONObject jsonObject=array.getJSONObject(i);
//
//                        }

//                        if(array.length()>0){
                        rl_address.setVisibility(View.VISIBLE);
                        JSONObject jsonObject=array.getJSONObject(0);
                        String city=jsonObject.getString("socity_name");
                        String area=jsonObject.getString("area");
                        String apartment=jsonObject.getString("apartment_name");
                        tv_address.setText(city + "," + area + "," + apartment);
//                        }else {
//
//                            rl_address.setVisibility(View.VISIBLE);
//                            tv_address.setText("No Address Added");
//                        }

//                        delivery_address_modelList = gson.fromJson(response.getString("data"), listType);
//
//                        //RecyclerView.Adapter adapter1 = new Delivery_get_address_adapter(delivery_address_modelList);
//                        adapter = new View_address_adapter(delivery_address_modelList);
//                        ((View_address_adapter) adapter).setMode(Attributes.Mode.Single);
//                        rv_address.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
//
//                        if (delivery_address_modelList.isEmpty()) {
//                            no_record.setVisibility(View.VISIBLE);
//                            if (getActivity() != null) {
//
//                                //Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
//                            }}


                    }else{

                        rl_address.setVisibility(View.VISIBLE);
                        tv_address.setText("No Address Added");
//                        no_record.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


}
