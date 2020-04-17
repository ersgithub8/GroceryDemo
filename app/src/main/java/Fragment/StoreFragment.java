package Fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import Adapter.RecyclerViewAdapter;
import Adapter.Stores_adapter;
import Config.BaseURL;
import Model.Anime;
import Model.ShopNow_model;
import Model.Store_model;
import Model.Sub_Categories;
import gogrocer.tcc.AppController;
import gogrocer.tcc.CustomSlider;
import gogrocer.tcc.R;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;
import util.RecyclerTouchListener;

import static android.content.Context.MODE_PRIVATE;


public class StoreFragment extends Fragment {

    private List<ShopNow_model> category_modelList = new ArrayList<>();
   public LinearLayout Search_layout;
    private RequestQueue requestQueue ;
    private List<Model.Anime> lstAnime ;
    public RecyclerView recyclerView ;
    public TextView tollbar;
    private SliderLayout imgSlider;
    public String StoreName,cityname;
    private Stores_adapter store_adapter;
    private static String TAG = StoreFragment.class.getSimpleName();
    private List<Store_model> store_models = new ArrayList<>();

    public StoreFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_store, container, false);

        imgSlider = (SliderLayout) view.findViewById(R.id.home_img_slider3);
        requestQueue = Volley.newRequestQueue(getActivity());
        lstAnime = new ArrayList<>() ;
        recyclerView =(RecyclerView)view.findViewById(R.id.recycler);
        tollbar=(TextView)view.findViewById(R.id.tollbar1);
        SharedPreferences prefs = getActivity().getSharedPreferences("city", MODE_PRIVATE);

        final String cat_id=getArguments().getString("subsubcat_id");
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));

        if (ConnectivityReceiver.isConnected()) {
            makeGetSliderRequest();
        }

        jsonrequest(cat_id);







        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
               String getid = store_models.get(position).getUser_id();
               String getname=store_models.get(position).getUser_fullname();
               String getimg=store_models.get(position).getUser_image();




                Bundle args = new Bundle();
                Fragment fm = new Product_fragment();
                args.putString("Store_id", getid);
                args.putString("store_name",getname);
                args.putString("store_image",getimg);
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


        return view;
    }

//    private void jsonrequest(String cat_id) {
//
//
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("master_cat_id", cat_id);
//
//
//        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BaseURL.GET_Store_URL,params
//        new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//
//
//                try {
//
//
//                    JSONArray array = response.getJSONArray("data");
////
//                    for (int i=0; i<array.length(); i++) {
//                        JSONObject actor = array.getJSONObject(i);
//
//                        String image =actor.getString("user_image");
//                        Anime anime = new Anime(actor.getString("user_fullname"),BaseURL.IMG_PROFILE_URL+image,actor.getString("user_id"));
//
//                        lstAnime.add(anime);
//
//                    }setuprecyclerview(lstAnime);
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                //Toast.makeText(getActivity(), (CharSequence) error, Toast.LENGTH_SHORT).show();
//                error.printStackTrace();
//            }
//        });
//
//        requestQueue.add(request);





//    }
    private void jsonrequest(String cat_id) {


//        Toast.makeText(getActivity(), cat_id, Toast.LENGTH_SHORT).show();
        String tag_json_obj = "json_category_req";
        final AlertDialog loading=new ProgressDialog(getActivity());
        loading.setMessage("Loading...");
        loading.setCancelable(false);
        loading.show();
        Map<String, String> params = new HashMap<String, String>();
        params.put("subCat_id", cat_id);

       /* if (parent_id != null && parent_id != "") {
        }*/

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_Store_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Cat", response.toString());
                try {
//
                    Boolean status = response.getBoolean("response");
                    if (status) {

                        loading.dismiss();

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Store_model>>() {
                        }.getType();
                        store_models = gson.fromJson(response.getString("data"), listType);
                        store_adapter = new Stores_adapter(store_models);
                        recyclerView.setAdapter(store_adapter);
                        store_adapter.notifyDataSetChanged();
                    } else {
                        loading.dismiss();
                        Toast.makeText(getActivity(), "No Data found", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    loading.dismiss();
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
//                loading.dismiss();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    private void setuprecyclerview(List<Anime> lstAnime) {


        RecyclerViewAdapter myadapter = new RecyclerViewAdapter(getActivity(),lstAnime) ;

        recyclerView.setAdapter(myadapter);


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
}
