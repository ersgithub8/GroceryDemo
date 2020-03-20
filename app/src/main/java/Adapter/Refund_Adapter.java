package Adapter;

import android.content.Context;
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
import Model.My_order_detail_model;
import Model.Refund_model;
import gogrocer.tcc.R;

/**
 * Created by Rajesh Dabhi on 30/6/2017.
 */

public class Refund_Adapter extends RecyclerView.Adapter<Refund_Adapter.MyViewHolder> {

    private List<Refund_model> modelList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_orderid, tv_price, tv_status,tv_date,store_name,tv_total_amount;


        public MyViewHolder(View view) {
            super(view);
            tv_orderid = (TextView) view.findViewById(R.id.tv_order_no);
            tv_price = (TextView) view.findViewById(R.id.tv_order_price);
            tv_status = (TextView) view.findViewById(R.id.tv_order_status);
            tv_date = (TextView) view.findViewById(R.id.tv_order_date);
            store_name = (TextView) view.findViewById(R.id.tv_storename);
            tv_total_amount = (TextView) view.findViewById(R.id.tv_order_total);

        }
    }

    public Refund_Adapter(List<Refund_model> modelList) {
        this.modelList = modelList;
    }

    @Override
    public Refund_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_refund, parent, false);

        context = parent.getContext();

        return new Refund_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Refund_Adapter.MyViewHolder holder, int position) {
        Refund_model mList = modelList.get(position);


        holder.tv_orderid.setText("Order ID: "+mList.getOrder_id());
        holder.tv_price.setText("Return Amount : "+mList.getAmount());
        holder.tv_date.setText(mList.getDate());
        holder.tv_total_amount.setText("Total Amount : "+mList.getTotal_amount());
        holder.store_name.setText("Store Name : "+mList.getStore_name());

        String s=mList.getStatus();
        if(s=="0"){
            holder.tv_status.setText("Pending");
        }else if(s=="1"){
            holder.tv_status.setText("Order Confirmed");
        }else if(s=="2"){
            holder.tv_status.setText("Order Assign");
        }else if(s=="3"){
            holder.tv_status.setText("Order Picked");
        }else if(s=="4"){
            holder.tv_status.setText("Order Delivered");
        }else if(s=="5"){
            holder.tv_status.setText("Cancelled");
        }

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}