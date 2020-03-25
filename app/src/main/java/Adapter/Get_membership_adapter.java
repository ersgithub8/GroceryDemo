package Adapter;

import android.content.Context;
import android.content.Intent;
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
import gogrocer.tcc.Paytm;
import gogrocer.tcc.R;

import static android.content.Context.MODE_PRIVATE;

public class Get_membership_adapter extends RecyclerView.Adapter<Get_membership_adapter.MyViewHolder>  {

    private List<get_membership_model> modelList;
    private List<get_membership_model> mFilteredList;
    private Context context;
    SharedPreferences preferences;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,typpe,ammount,discount,Buy;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            typpe = (TextView) view.findViewById(R.id.Type);
            ammount = (TextView) view.findViewById(R.id.ammount);
            discount = (TextView) view.findViewById(R.id.Discount);
            Buy = (TextView) view.findViewById(R.id.Buy);

        }
    }

    public Get_membership_adapter(List<get_membership_model> modelList) {
        this.modelList = modelList;

    }

    @Override
    public Get_membership_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_membership_rv, parent, false);
        context = parent.getContext();
        return new Get_membership_adapter.MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(final Get_membership_adapter.MyViewHolder holder, int position) {
        final get_membership_model mList = modelList.get(position);
        holder.name.setText(mList.getMembership());
        holder.typpe.setText(mList.getType());
        holder.ammount.setText(mList.getAmount()+context.getResources().getString(R.string.currency));
        holder.discount.setText(mList.getDiscount()+"% OFF");
        preferences = context.getSharedPreferences("lan", MODE_PRIVATE);

        holder.Buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, Paytm.class);
                i.putExtra("total",mList.getAmount());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}

