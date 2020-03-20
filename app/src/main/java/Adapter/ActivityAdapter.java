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

import java.util.List;

import Model.Model;
import gogrocer.tcc.R;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.MyViewHolder> {


    public Context mContext ;
    public List<Model> mData ;


    @NonNull
    @Override
    public ActivityAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view ;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.activity_item,viewGroup,false) ;
        final MyViewHolder viewHolder = new MyViewHolder(view) ;


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityAdapter.MyViewHolder myViewHolder, int i) {

        myViewHolder.user_fullname.setText((CharSequence) mData.get(i).getUser_fullname());
        myViewHolder.user_id.setText((CharSequence) mData.get(i).getUser_id());

        Glide.with(mContext).load(mData.get(i).getUser_image()).into(myViewHolder.user_image);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView user_fullname ;
        TextView user_id;
        ImageView user_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            user_id = itemView.findViewById(R.id.user_id);
            user_image = itemView.findViewById(R.id.user_image);
            user_fullname = itemView.findViewById(R.id.user_fullname);
        }
    }
}
