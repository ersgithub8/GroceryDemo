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

import com.koushikdutta.ion.builder.Builders;

import java.util.ArrayList;
import java.util.List;

import Model.Area;
import Model.get_membership_model;
import gogrocer.tcc.R;

import static android.content.Context.MODE_PRIVATE;

public class Get_membership_adapter extends RecyclerView.Adapter<Get_membership_adapter.MyViewHolder> implements Filterable {

    private List<get_membership_model> modelList;
    private List<get_membership_model> mFilteredList;
    private Context context;
    SharedPreferences preferences;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,typpe,ammount,discount,Buy;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            typpe = (TextView) view.findViewById(R.id.);
            ammount = (TextView) view.findViewById(R.id.tv_socity_name);
            discount = (TextView) view.findViewById(R.id.tv_socity_name);
            Buy = (TextView) view.findViewById(R.id.tv_socity_name);

        }
    }

    public Get_membership_adapter(List<get_membership_model> modelList) {
        this.modelList = modelList;
        this.mFilteredList = modelList;
    }

    @Override
    public Get_membership_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_membership_rv, parent, false);
        context = parent.getContext();
        return new Get_membership_adapter.MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(Get_membership_adapter.MyViewHolder holder, int position) {
        get_membership_model mList = modelList.get(position);
        holder.product_nmae.setText(mList.getget_membership_model());
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

                    ArrayList<get_membership_model> filteredList = new ArrayList<>();

                    for (get_membership_model androidVersion : modelList) {

                        if (androidVersion.getget_membership_model().toLowerCase().contains(charString)) {

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
                mFilteredList = (ArrayList<get_membership_model>) filterResults.values;
                notifyDataSetChanged();

            }
        };
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

}

