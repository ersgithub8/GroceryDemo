package gogrocer.tcc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import Model.ShopNow_model;

public class Activity2Activity extends AppCompatActivity {

    private List<ShopNow_model> category_modelList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity2);

//        FragmentManager fragmentManager =getSupportFragmentManager();
//        Product_fragment pf = new Product_fragment();
//        fragmentManager.beginTransaction().replace(R.id.container_product,pf).commit();
//

    }



}
