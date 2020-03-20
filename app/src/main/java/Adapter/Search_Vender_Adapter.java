package Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import Config.BaseURL;
import Model.Search_Store_Model;
import gogrocer.tcc.R;

public class Search_Vender_Adapter extends RecyclerView.Adapter<Search_Vender_Adapter.MyViewHolder>{



    private List<Search_Store_Model> modelList;
    private Context context;

    public Search_Vender_Adapter(List<Search_Store_Model> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }

    public Search_Vender_Adapter() {
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_vender_search, viewGroup, false);
        context = viewGroup.getContext();
        return new Search_Vender_Adapter.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        Search_Store_Model mList = modelList.get(i);

        Glide.with(context)
                .load(BaseURL.IMG_PROFILE_URL + mList.getUser_image())
                .centerCrop()
                .placeholder(R.drawable.icon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(myViewHolder.Store_logo);

        myViewHolder.Store_title.setText(mList.getUser_name());

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView Store_title;
        public ImageView Store_logo;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);



            Store_title = (TextView) itemView.findViewById(R.id.Store_name);
            Store_logo = (ImageView) itemView.findViewById(R.id.vender_img);
        }
    }
}
