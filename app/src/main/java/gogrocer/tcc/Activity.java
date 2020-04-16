package gogrocer.tcc;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Adapter.RecyclerViewAdapter;
import Fragment.Product_fragment;
import Model.Anime;
import Model.ShopNow_model;

public class Activity extends AppCompatActivity  {


    private List<ShopNow_model> category_modelList = new ArrayList<>();

    private RequestQueue requestQueue ;
    private List<Model.Anime> lstAnime ;
    private RecyclerView recyclerView ;
    public TextView tollbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_);


        requestQueue = Volley.newRequestQueue(Activity.this);
        lstAnime = new ArrayList<>() ;
        recyclerView = findViewById(R.id.recycler);
        tollbar=(TextView)findViewById(R.id.tollbar1);


        Fragment fm = new Fragment();
        Product_fragment pf= new Product_fragment();
        String StoreName = getIntent().getStringExtra("1");
//
//        if (StoreName.equals("Grocery"))
//        {
//            tollbar.setText("Grocery");
//        }
//        if (StoreName.equals("vegetables"))
//        {
//            tollbar.setText("Vegetables");
//        }
//        if (StoreName.equals("fruits"))
//        {
//            tollbar.setText("Fruits");
//        }

        jsonrequest();
       // recyclerView.setOnClickListener(new View.OnClickListener() {
         //   @Override
         //   public void onClick(View v) {
//                Intent intent = new Intent(Activity.this, Activity2Activity.class);
//                startActivity(intent);


//                Fragment fm = new Product_fragment();
//                String getid = category_modelList.get(2).getId();
//                String getcat_title = category_modelList.get(2).getTitle();
//                Bundle args = new Bundle();
//                Fragment fm1 = new Product_fragment();
//                args.putString("cat_id", getid);
//                args.putString("cat_title", getcat_title);
//                fm.setArguments(args);
//                FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm1)
//                        .addToBackStack(null).commit();

//            }
//        });

    }

    private void jsonrequest() {
        final AlertDialog loading=new ProgressDialog(Activity.this);
        loading.setMessage("Loading...");
        loading.setCancelable(false);
        loading.show();


        String URL = "http://kitchenbasket.eparking.website/backend/index.php/api/get_stores" ;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {
                    loading.dismiss();
                    JSONArray array = response.getJSONArray("data");
//
                    for (int i=0; i<array.length(); i++) {
                        JSONObject actor = array.getJSONObject(i);

                        String image =actor.getString("user_image");
                        Anime anime = new Anime(actor.getString("user_fullname"),"http://kitchenbasket.eparking.website/backend/uploads/profile/"+image,actor.getString("user_id"));

                        lstAnime.add(anime);

                    }setuprecyclerview(lstAnime);


                } catch (JSONException e) {
                    e.printStackTrace();
                    loading.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                error.printStackTrace();
            }
        });

        requestQueue.add(request);




    }

    private void setuprecyclerview(List<Anime> lstAnime) {


        RecyclerViewAdapter myadapter = new RecyclerViewAdapter(this,lstAnime) ;
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(myadapter);


    }






}