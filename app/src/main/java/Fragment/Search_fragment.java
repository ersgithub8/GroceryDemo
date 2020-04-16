package Fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
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

import Adapter.Product_adapter;
import Adapter.Search_adapter;
import Adapter.SuggestionAdapter;
import Config.BaseURL;
import Model.Product_model;
import gogrocer.tcc.AppController;
import gogrocer.tcc.MainActivity;
import gogrocer.tcc.R;
import util.CustomVolleyJsonRequest;

/**
 * Created by Yasir Shahzad on 14/7/2017.
 */

public class Search_fragment extends Fragment {

    private static String TAG = Search_fragment.class.getSimpleName();
    //    String[] fruits = {"MIlk butter & cream", "Bread Buns & Pals", "Dals Mix Pack", "buns-pavs", "cakes", "Channa Dal", "Toor Dal", "Wheat Atta"
//            , "Beson", "Almonds", "Packaged Drinking", "Cola drinks", "Other soft drinks", "Instant Noodles", "Cup Noodles", "Salty Biscuits", "cookie", "Sanitary pads", "sanitary Aids"
//            , "Toothpaste", "Mouthwash", "Hair oil", "Shampoo", "Pure & pomace olive", "ICE cream", "Theme Egg", "Amul Milk", "AMul Milk Pack power", "kaju pista dd"};
    private AutoCompleteTextView acTextView;
    private RelativeLayout btn_search;
    private RecyclerView rv_search;

    private List<Product_model> product_modelList = new ArrayList<>();
    private Search_adapter adapter_product;

    public Search_fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.search));


        acTextView = (AutoCompleteTextView) view.findViewById(R.id.et_search);


        acTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acTextView.requestFocus();
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        });
       // acTextView.setThreshold(1);

        acTextView.setAdapter(new SuggestionAdapter(getActivity(), acTextView.getText().toString()));

        acTextView.setTextColor(getResources().getColor(R.color.green));
        btn_search = (RelativeLayout) view.findViewById(R.id.btn_search);
        rv_search = (RecyclerView) view.findViewById(R.id.rv_search);
        rv_search.setLayoutManager(new LinearLayoutManager(getActivity()));


        String Storeid = getArguments().getString("Store_id_S");
       // Toast.makeText(getActivity(), Storeid, Toast.LENGTH_SHORT).show();
        acTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String get_search_txt ="%"+ acTextView.getText().toString() +"%";
                makeGetProductRequest(get_search_txt);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


//        btn_search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String get_search_txt ="%"+ acTextView.getText().toString() +"%";
//                if (TextUtils.isEmpty(get_search_txt)) {
//                    Toast.makeText(getActivity(), getResources().getString(R.string.enter_keyword), Toast.LENGTH_SHORT).show();
//                } else {
//                    if (ConnectivityReceiver.isConnected()) {
//                        makeGetProductRequest(get_search_txt);
//                    } else {
//                        ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
//                    }
//                }
//
//            }
//        });

        return view;
    }

    /**
     * Method to make json object request where json response starts wtih {
     */
    private void makeGetProductRequest(String search_text) {

        // Tag used to cancel the request
        String tag_json_obj = "json_product_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("search", search_text);
        String Storeid = getArguments().getString("Store_id_S");
        params.put("storeid",Storeid);

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

                        product_modelList = gson.fromJson(response.getString("data"), listType);

                      Product_adapter adapter_product = new Product_adapter(product_modelList, getActivity());
                        rv_search.setAdapter(adapter_product);
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
