package Adapter;

import android.content.Context;
import android.content.SharedPreferences;
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

import Model.Master_category;
import gogrocer.tcc.R;

import static android.content.Context.MODE_PRIVATE;

public class Master_category_adapter extends RecyclerView.Adapter<Master_category_adapter.MyViewHolder> {

    private List<Master_category> modelList;
    private Context context;
    String language;
    SharedPreferences preferences;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.service_text);
            image = (ImageView) view.findViewById(R.id.service_image);


        }
    }

    public Master_category_adapter(List<Master_category> modelList) {
        this.modelList = modelList;
    }

    @Override
    public Master_category_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_headre_catogaries, parent, false);

        context = parent.getContext();

        return new Master_category_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Master_category_adapter.MyViewHolder holder, int position) {
        Master_category mList = modelList.get(position);
//        Glide.with(context)
//                .load(BaseURL.GET_MASTERImages_URL + mList.getImage())
//                .placeholder(R.drawable.icon)
//                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .dontAnimate()
//                .into(holder.image);
        preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
        language=preferences.getString("language","");
        if (language.contains("english")) {
            holder.title.setText(mList.getName());
        }
        else {
            holder.title.setText(mList.getName());

        }
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}