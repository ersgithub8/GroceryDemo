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
import Model.Sub_Categories;
import gogrocer.tcc.R;

import static android.content.Context.MODE_PRIVATE;

public class Master_Subcat extends RecyclerView.Adapter<Master_Subcat.MyViewHolder> {

    private List<Sub_Categories> modelList;
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

    public Master_Subcat(List<Sub_Categories> modelList) {
        this.modelList = modelList;
    }

    @Override
    public Master_Subcat.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_headre_catogaries, parent, false);

        context = parent.getContext();

        return new Master_Subcat.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Master_Subcat.MyViewHolder holder, int position) {
        Sub_Categories mList = modelList.get(position);
        Glide.with(context)
                .load(BaseURL.GET_SUBCATImages_URL + mList.getImage())
                .placeholder(R.drawable.icon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image);
        preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
        language=preferences.getString("language","");
        if (language.contains("english")) {
            holder.title.setText(mList.getTitle());
        }
        else {
            holder.title.setText(mList.getTitle());

        }
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}
