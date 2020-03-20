package Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Model.Apartment_Model;
import Model.Apartment_Model;
import gogrocer.tcc.R;

import static android.content.Context.MODE_PRIVATE;

public class Apartment_adapter extends RecyclerView.Adapter<Apartment_adapter.MyViewHolder> implements Filterable {

    private List<Apartment_Model> modelList;
    private List<Apartment_Model> mFilteredList;
    private Context context;
    SharedPreferences preferences;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView product_nmae;

        public MyViewHolder(View view) {
            super(view);
            product_nmae = (TextView) view.findViewById(R.id.tv_socity_name);

        }
    }

    public Apartment_adapter(List<Apartment_Model> modelList) {
        this.modelList = modelList;
        this.mFilteredList = modelList;
    }

    @Override
    public Apartment_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_socity_rv, parent, false);
        context = parent.getContext();
        return new Apartment_adapter.MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(Apartment_adapter.MyViewHolder holder, int position) {
        Apartment_Model mList = modelList.get(position);
        holder.product_nmae.setText(mList.getApartment_name());
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

                    ArrayList<Apartment_Model> filteredList = new ArrayList<>();

                    for (Apartment_Model androidVersion : modelList) {

                        if (androidVersion.getApartment_name().toLowerCase().contains(charString)) {

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
                mFilteredList = (ArrayList<Apartment_Model>) filterResults.values;
                notifyDataSetChanged();

            }
        };
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

}