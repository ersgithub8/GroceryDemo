package Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import gogrocer.tcc.MainActivity;
import gogrocer.tcc.R;

public class RatingDialogFragment extends Fragment {
    ListView listView;
    Fragment fm;
    String[] imgid={
            "★★★★★ Stars","★★★★ Stars",
            "★★★ Stars","★★ Stars",
            "★ Star",
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_rating_store,container,false);

        ((MainActivity) getActivity()).setTitle("Filter");
//        MyListAdapter adapter=new MyListAdapter(getActivity(),imgid);
        listView=view.findViewById(R.id.lv);
        fm =new StoreFragment();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, imgid);
        listView.setAdapter(adapter);
//        listView.setAdapter(adapter);




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String  getid="0";// = subcat_models.get(position).getId()
                Bundle args = new Bundle();
                if(position==0){
                    getid="5";
                }else if(position==1){
                    getid="4";

                }else if(position==2){
                    getid="3";

                }else if(position==3){
                    getid="2";

                }else if(position==4){
                    getid="1";
                }

//                Toast.makeText(getActivity(), getid, Toast.LENGTH_SHORT).show();


                args.putString("subsubcat_id", getid);

                fm.setArguments(args);
                FragmentManager fragmentManager1=getFragmentManager();
                fragmentManager1.beginTransaction().replace(R.id.contentPanel,fm).addToBackStack(null).commit();


            }
        });
        return view;
    }


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_rating_store);
//
//
//    Z
//
//    }

}
