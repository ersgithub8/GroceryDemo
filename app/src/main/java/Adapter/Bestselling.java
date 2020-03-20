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
import Model.Product_model;
import gogrocer.tcc.R;

import static android.content.Context.MODE_PRIVATE;

public class Bestselling extends RecyclerView.Adapter<Bestselling.MyViewHolder> {

private List<Product_model> modelList;
private Context context;
        SharedPreferences preferences;
public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView product_nmae, product_prize;
    public ImageView image;

    public MyViewHolder(View view) {
        super(view);
        product_nmae = (TextView) view.findViewById(R.id.product_name);
        product_prize = (TextView) view.findViewById(R.id.product_prize);
        image = (ImageView) view.findViewById(R.id.iv_icon);
    }
}

    public Bestselling(List<Product_model> modelList) {
        this.modelList = modelList;
    }

    @Override
    public Bestselling.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_best_selling, parent, false);
        context = parent.getContext();
        return new Bestselling.MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(Bestselling.MyViewHolder holder, int position) {
        Product_model mList = modelList.get(position);
        preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
        String language=preferences.getString("language","");
        Glide.with(context)
                .load(BaseURL.IMG_PRODUCT_URL + mList.getProduct_image())
                .placeholder(R.drawable.icon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image);
        holder.product_prize.setText( mList.getPrice()+ context.getResources().getString(R.string.currency) );

        if (language.contains("english")) {
            holder.product_nmae.setText(mList.getProduct_name());
        }
        else {
            holder.product_nmae.setText(mList.getProduct_name());

        }
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}
