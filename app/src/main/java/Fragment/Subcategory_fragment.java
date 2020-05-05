package Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.facebook.shimmer.ShimmerFrameLayout;
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

import Adapter.Master_Subcat;
import Adapter.Master_category_adapter;
import Adapter.RecyclerViewAdapter;
import Config.BaseURL;
import Model.Anime;
import Model.Master_category;
import Model.ShopNow_model;
import Model.Sub_Categories;
import cn.pedant.SweetAlert.SweetAlertDialog;
import gogrocer.tcc.AppController;
import gogrocer.tcc.CustomSlider;
import gogrocer.tcc.R;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;
import util.RecyclerTouchListener;

import static android.content.Context.MODE_PRIVATE;

public class Subcategory_fragment extends Fragment {
    Fragment fm = null;
    private Master_Subcat master_subcat_adapter;
    private static String TAG = Subcategory_fragment.class.getSimpleName();
    private List<Sub_Categories> subcat_models = new ArrayList<>();
    private ShimmerFrameLayout mShimmerViewContainer;
    RecyclerView rv_subcategories;
    private SliderLayout imgSlider;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ew, container, false);

        rv_subcategories=view.findViewById(R.id.subcat_id);
        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);


        imgSlider = (SliderLayout) view.findViewById(R.id.home_img_slider2);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        rv_subcategories.setLayoutManager(gridLayoutManager);
        rv_subcategories.setItemAnimator(new DefaultItemAnimator());
        rv_subcategories.setNestedScrollingEnabled(true);
        String cat_id=getArguments().getString("cat_id");
        makeGetCategoryRequest(cat_id);

        if (ConnectivityReceiver.isConnected()) {
            makeGetSliderRequest();
        }


        rv_subcategories.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_subcategories, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
              String  getid = subcat_models.get(position).getId();
                Bundle args = new Bundle();


                fm =new Subsub_categories();
                args.putString("subcat_id", getid);

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
        params.put("master_cat_id", cat_id);

       /* if (parent_id != null && parent_id != "") {
        }*/
//        final AlertDialog loading=new ProgressDialog(getActivity());
//        loading.setMessage("Loading...");
//        loading.setCancelable(false);
//        loading.show();


        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_MASTER_SUB_CATEGORY_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Cat", response.toString());
                try {
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
//
//                    loading.dismiss();
                    Boolean status = response.getBoolean("response");
                   // Toast.makeText(getActivity(), String.valueOf(status), Toast.LENGTH_SHORT).show();
                    if (status) {


                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Sub_Categories>>() {
                        }.getType();
                        subcat_models = gson.fromJson(response.getString("data"), listType);
                        master_subcat_adapter = new Master_Subcat(subcat_models);
                        rv_subcategories.setAdapter(master_subcat_adapter);
                        master_subcat_adapter.notifyDataSetChanged();
                    } else {
//                            Toast.makeText(getActivity(), "No Data found", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getActivity(), "No Data found", Toast.LENGTH_SHORT).show();
                        SweetAlertDialog alertDialog=new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE);
                        alertDialog.setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        getActivity().onBackPressed();
                                    }
                                }).setTitleText("No data Found")
                                .setCancelable(false);
                        alertDialog.setConfirmButtonBackgroundColor(Color.RED);
                        alertDialog.show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
//                    loading.dismiss();
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
//                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
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
//                                textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
//                                    @Override
//                                    public void onSliderClick(BaseSliderView slider) {
                                        //   Toast.makeText(getActivity(), "" + sub_cat, Toast.LENGTH_SHORT).show();
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
//                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(req);

    }
    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }
}
