package Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import Config.BaseURL;
import Model.Area;
import Model.Product_model;
import Model.Socity_model;
import gogrocer.tcc.R;

import static android.content.Context.MODE_PRIVATE;

public class Area_Adapter extends RecyclerView.Adapter<Area_Adapter.MyViewHolder> implements Filterable {

    private List<Area> modelList;
    private List<Area> mFilteredList;
    private Context context;
    SharedPreferences preferences;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView product_nmae;

        public MyViewHolder(View view) {
            super(view);
            product_nmae = (TextView) view.findViewById(R.id.tv_socity_name);

        }
    }

    public Area_Adapter(List<Area> modelList) {
        this.modelList = modelList;
        this.mFilteredList = modelList;
    }

    @Override
    public Area_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_socity_rv, parent, false);
        context = parent.getContext();
        return new Area_Adapter.MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(Area_Adapter.MyViewHolder holder, int position) {
        Area mList = modelList.get(position);
        holder.product_nmae.setText(mList.getArea());
        preferences = context.getSharedPreferences("lan", MODE_PRIVATE);

    }
    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = modelList;
                } else {

                    ArrayList<Area> filteredList = new ArrayList<>();

                    for (Area androidVersion : modelList) {

                        if (androidVersion.getArea().toLowerCase().contains(charString)) {

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
                mFilteredList = (ArrayList<Area>) filterResults.values;
                notifyDataSetChanged();

            }
        };
    }

    @Override
    public int getItemCount() {
            return mFilteredList.size();
    }

}
