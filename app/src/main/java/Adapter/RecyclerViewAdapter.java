package Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import Model.Anime;
import Model.ShopNow_model;
import gogrocer.tcc.R;



public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext ;
    private List<Anime> mData ;

    private List<ShopNow_model> category_modelList = new ArrayList<>();


    public RecyclerViewAdapter(Context mContext, List<Anime> mData) {
        this.mContext = mContext;
        this.mData = mData;

        // Request option for Glide


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.activity_item,parent,false) ;
        final MyViewHolder viewHolder = new MyViewHolder(view) ;
       // viewHolder.view_container.setOnClickListener(new View.OnClickListener() {
        //    @Override
          //  public void onClick(View v) {
//               Intent intent = new Intent(mContext, Activity2Activity.class);
//////                intent.putExtra("2","2");
//                mContext.startActivity(intent);






                //  Intent i = new Intent(mContext, Activity2Activity.class);
//                i.putExtra("user_fullname",mData.get(viewHolder.getAdapterPosition()).getUser_fullname());
//                i.putExtra("user_image",mData.get(viewHolder.getAdapterPosition()).getUser_image());
//                i.putExtra("user_id",mData.get(viewHolder.getAdapterPosition()).getUser_id());
              //  mContext.startActivity(i);
//
//            }
//        });




        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

//
        holder.user_fullname.setText(mData.get(position).getUser_fullname());
        holder.user_id.setText(mData.get(position).getUser_id());

        // Load Image from the internet and set it into Imageview using Glide

        Glide.with(mContext).load(mData.get(position).getUser_image()).into(holder.user_image);



    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder  {



        TextView user_fullname ;
        TextView user_id;
        ImageView user_image;
        LinearLayout view_container;





        public MyViewHolder(View itemView) {
            super(itemView);

            user_id = itemView.findViewById(R.id.user_id);
            user_image = itemView.findViewById(R.id.user_image);
            user_fullname = itemView.findViewById(R.id.user_fullname);
            view_container=(LinearLayout)itemView.findViewById(R.id.view_container);

        }
    }

}
