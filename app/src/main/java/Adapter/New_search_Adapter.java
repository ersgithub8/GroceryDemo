package Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import Config.BaseURL;
import Model.Search_Store_Model;
import gogrocer.tcc.R;

import static android.content.Context.MODE_PRIVATE;

public class New_search_Adapter extends RecyclerView.Adapter<New_search_Adapter.MyViewHolder> implements Filterable {

private List<Search_Store_Model> modelList;
private List<Search_Store_Model> mFilteredList;
private Context context;
        SharedPreferences preferences;
public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView Store_title;
    public ImageView Store_logo;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);



        Store_title = (TextView) itemView.findViewById(R.id.Store_name);
        Store_logo = (ImageView) itemView.findViewById(R.id.vender_img);
}
}

    public New_search_Adapter(List<Search_Store_Model> modelList, Activity activity) {
        this.modelList = modelList;
        this.mFilteredList = modelList;
    }

    @Override
    public New_search_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_vender_search, parent, false);
        context = parent.getContext();
        return new New_search_Adapter.MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(New_search_Adapter.MyViewHolder holder, int position) {
        Search_Store_Model mList = modelList.get(position);


        Glide.with(context)
                .load(BaseURL.IMG_PROFILE_URL + mList.getUser_image())
                .centerCrop()
                .placeholder(R.drawable.icon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.Store_logo);

        holder.Store_title.setText(mList.getUser_name());

    }
    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();
                Toast.makeText(context,charString, Toast.LENGTH_SHORT).show();

                if (charString.isEmpty()) {

                    mFilteredList = modelList;
                } else {

                    ArrayList<Search_Store_Model> filteredList = new ArrayList<>();

                    for (Search_Store_Model androidVersion : modelList) {

                        if (androidVersion.getUser_name().toLowerCase().contains(charString)) {

                            filteredList.add(androidVersion);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Search_Store_Model>) filterResults.values;
                notifyDataSetChanged();

            }
        };
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

}