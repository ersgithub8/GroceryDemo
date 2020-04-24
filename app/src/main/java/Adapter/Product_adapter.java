package Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Config.BaseURL;
import Model.Product_model;
import gogrocer.tcc.MainActivity;
import gogrocer.tcc.R;
import util.DatabaseHandler;

import static android.content.Context.MODE_PRIVATE;


public class Product_adapter extends RecyclerView.Adapter<Product_adapter.MyViewHolder> {

    private List<Product_model> modelList;
    private Context context;
    private DatabaseHandler dbcart;
    String language;
    String selectedunit="";

    public ArrayList<String> asubunit;
    public ArrayList<String> bsubprice;
    SharedPreferences preferences;


    public Product_adapter(List<Product_model> modelList, Context context) {
        this.modelList = modelList;
        dbcart = new DatabaseHandler(context);
    }

    public Product_adapter(List<Product_model> modelList) {
        this.modelList = modelList;

    }

    @Override
    public Product_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product_rv, parent, false);
        context = parent.getContext();

        return new Product_adapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final Product_adapter.MyViewHolder holder, final int position) {


        Product_model mList = modelList.get(position);

        String mrp=mList.getMrp();
        holder.tv_mrp.setText(mrp+context.getResources().getString(R.string.currency)+"mrp");
        int dis=((Integer.parseInt(mList.getMrp())-Integer.parseInt(mList.getPrice()))*100)/Integer.parseInt(mList.getMrp());

        holder.tv_disc.setText(dis+"%OFF");
        Glide.with(context)
                .load(BaseURL.IMG_PRODUCT_URL + mList.getProduct_image())
                .centerCrop()
                .placeholder(R.drawable.icon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.iv_logo);

        preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
        language = preferences.getString("language", "");
        if (language.contains("english")) {
            holder.tv_title.setText(mList.getProduct_name());
        } else {
            holder.tv_title.setText(mList.getProduct_name());

        }
        String storename= mList.getStorename();




        holder.tv_reward.setText(mList.getRewards());
//        holder.tv_price.setText(mList.getUnit_value());
        holder.tv_price1.setText(mList.getUnit());
        holder.tv_price2.setText(mList.getPrice());


        asubunit = mList.getSubunits();
        bsubprice = mList.getSubprice();


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, asubunit);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.dropdown.setAdapter(dataAdapter);

        holder.dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(pos);
                // Display the selected item into the TextView
                try {

                    asubunit =  modelList.get(position).getSubunits();
                    bsubprice =  modelList.get(position).getSubprice();
                    holder.tv_price1.setText("");
                    holder.tv_price1.setText(selectedItemText);
                    selectedunit=selectedItemText;
                    Log.e("prices"+pos,""+pos);
                    holder.tv_price2.setText(bsubprice.get(pos));

                    holder.tv_contetiy.setText("0");
                }
                catch (Exception e)
                {


                }




            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        holder.tv_currency.setText(context.getResources().getString(R.string.currency));
        if (Integer.valueOf(modelList.get(position).getStock()) <= 0) {
            holder.tv_add.setText(R.string.tv_out);
            holder.tv_add.setTextColor(context.getResources().getColor(R.color.black));
            holder.tv_add.setBackgroundColor(context.getResources().getColor(R.color.gray));
            holder.tv_add.setEnabled(false);
            holder.iv_minus.setEnabled(false);
            holder.iv_plus.setEnabled(false);
        } else if (dbcart.isInCart(mList.getProduct_id())) {
            holder.tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
            holder.tv_contetiy.setText(dbcart.getCartItemQty(mList.getCart_id()));
        } else {
            holder.tv_add.setText(context.getResources().getString(R.string.tv_pro_add));
        }

        //change cart id
        Double items = Double.parseDouble(dbcart.getInCartItemQty(mList.getCart_id()));
        Double price = Double.parseDouble(mList.getPrice());
        Double reward = Double.parseDouble(mList.getRewards());
        holder.tv_total.setText("" + price * items);
        holder.ll.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }


    private void showImage(String image) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.product_image_dialog);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();


        ImageView iv_image_cancle = (ImageView) dialog.findViewById(R.id.iv_dialog_cancle);
        ImageView iv_image = (ImageView) dialog.findViewById(R.id.iv_dialog_img);

        Glide.with(context)
                .load(BaseURL.IMG_PRODUCT_URL + image)
                .centerCrop()
                .placeholder(R.drawable.icon)
                .crossFade()
                .into(iv_image);

        iv_image_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private void showProductDetail(String image, String title, String description, String detail, final int position, String qty, final String pricee, final String unit, final String unitvalue) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_product_detail);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();



        asubunit = new ArrayList<>();
        bsubprice = new ArrayList<>();

        ImageView iv_image = (ImageView) dialog.findViewById(R.id.iv_product_detail_img);
        final ImageView iv_minus = (ImageView) dialog.findViewById(R.id.iv_subcat_minus);
        final ImageView iv_plus = (ImageView) dialog.findViewById(R.id.iv_subcat_plus);
        TextView tv_title = (TextView) dialog.findViewById(R.id.tv_product_detail_title);

        TextView tv_detail = (TextView) dialog.findViewById(R.id.tv_product_detail);
        final TextView tv_contetiy = (TextView) dialog.findViewById(R.id.tv_subcat_contetiy);
        final TextView tv_add = (TextView) dialog.findViewById(R.id.tv_subcat_add);


        tv_title.setText(title);
        tv_detail.setText(detail);
        tv_contetiy.setText(qty);
        tv_detail.setText(description);

        Glide.with(context)
                .load(BaseURL.IMG_PRODUCT_URL + image)
                .centerCrop()
                .crossFade()
                .into(iv_image);
        if (Integer.valueOf(modelList.get(position).getStock()) <= 0) {
            tv_add.setText(R.string.tv_out);
            tv_add.setTextColor(context.getResources().getColor(R.color.black));
            tv_add.setBackgroundColor(context.getResources().getColor(R.color.gray));
            tv_add.setEnabled(false);
            iv_minus.setEnabled(false);
            iv_plus.setEnabled(false);
        } else if (dbcart.isInCart(modelList.get(position).getCart_id())) {
            tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
            tv_contetiy.setText(dbcart.getCartItemQty(modelList.get(position).getCart_id()));
        } else {
            tv_add.setText(context.getResources().getString(R.string.tv_pro_add));
        }

//        tv_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                HashMap<String, String> map = new HashMap<>();
//                preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
//                language = preferences.getString("language", "");
//
//                map.put("c_id", modelList.get(position).getCart_id());
//                map.put("product_id", modelList.get(position).getProduct_id());
//                map.put("product_name", modelList.get(position).getProduct_name());
//                map.put("category_id", modelList.get(position).getCategory_id());
//                map.put("product_description", modelList.get(position).getProduct_description());
//                map.put("deal_price", modelList.get(position).getDeal_price());
//                map.put("start_date", modelList.get(position).getStart_date());
//                map.put("start_time", modelList.get(position).getStart_time());
//                map.put("end_date", modelList.get(position).getEnd_date());
//                map.put("end_time", modelList.get(position).getEnd_time());
//                map.put("price", modelList.get(position).getPrice());
//                map.put("product_image", modelList.get(position).getProduct_image());
//                map.put("status", modelList.get(position).getStatus());
//                map.put("in_stock", modelList.get(position).getIn_stock());
//                map.put("unit_value", modelList.get(position).getUnit_value());
//                map.put("unit", modelList.get(position).getUnit());
//                map.put("increament", modelList.get(position).getIncreament());
//                map.put("rewards", modelList.get(position).getRewards());
//                map.put("stock", modelList.get(position).getStock());
//                map.put("title", modelList.get(position).getStoreid());
//
//
//                if (!tv_contetiy.getText().toString().equalsIgnoreCase("0")) {
//
//                    if (dbcart.isTileInCart(map.get("title")) == "empty" || dbcart.isTileInCart(map.get("title")) == (modelList.get(position).getStoreid())) {
//
//                       // Toast.makeText(context, ""+dbcart.getProductUnit(map.get("product_id")), Toast.LENGTH_SHORT).show();
//                     //   if(dbcart.getProductUnit(map.get("product_id")).equals(selectedunit))
//                        {
//
//                            if (dbcart.isInCart(map.get("c_id"))) {
//                                dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
//                                tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
//                            } else {
//                                dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
//                                tv_add.setText( context.getResources().getString(R.string.tv_pro_update));
//                            }
//                        }
//
//
//                    } else {
//                        Toast.makeText(context, "you can only select product from one store at a time", Toast.LENGTH_SHORT).show();
//                    }
//
//
//                } else {
//                    dbcart.removeItemFromCart(map.get("product_id"));
//                    tv_add.setText(context.getResources().getString(R.string.tv_pro_add));
//                }
//
//                Double items = Double.parseDouble(dbcart.getInCartItemQty(map.get("c_id")));
//                Double price = Double.parseDouble(map.get("price"));
//                ((MainActivity) context).setCartCounter("" + dbcart.getCartCount());
//
//                notifyItemChanged(position);
//
//            }
//        });

//        iv_plus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int qty = Integer.valueOf(tv_contetiy.getText().toString());
//                qty = qty + 1;
//
//
//                tv_contetiy.setText(String.valueOf(qty));
//            }
//        });
//
//        iv_minus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int qty = 0;
//                if (!tv_contetiy.getText().toString().equalsIgnoreCase(""))
//                    qty = Integer.valueOf(tv_contetiy.getText().toString());
//
//                if (qty > 0) {
//                    qty = qty - 1;
//                    tv_contetiy.setText(String.valueOf(qty));
//                }
//            }
//        });

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_title, tv_price, tv_price1, tv_price2, tv_currency, tv_reward, tv_total, tv_contetiy, tv_add;
        public ImageView iv_logo, iv_plus, iv_minus, iv_remove;
        public Double reward;

        public String pricee;
        public String unitvalue;
        String unit;
        public TextView textView;
        public LinearLayout llMain;

        Spinner dropdown;
        public TextView textView1, textView2, textView3, textView4, textView5, textView6,tv_mrp,tv_disc;
        LinearLayout ll;
        public MyViewHolder(View view) {
            super(view);

//            textView1 = (TextView) view.findViewById(R.id.one);
//            textView2 = (TextView) view.findViewById(R.id.two);
//            textView3 = (TextView) view.findViewById(R.id.three);
//            textView4 = (TextView) view.findViewById(R.id.four);
//            textView5 = (TextView) view.findViewById(R.id.five);
//            textView6 = (TextView) view.findViewById(R.id.six);
            tv_mrp = (TextView) view.findViewById(R.id.price_mrp);
            tv_disc = (TextView) view.findViewById(R.id.disc);
            tv_title = (TextView) view.findViewById(R.id.tv_subcat_title);
            tv_price = (TextView) view.findViewById(R.id.tv_subcat_price);
            tv_price1 = (TextView) view.findViewById(R.id.tv_subcat_price1);
            tv_price2 = (TextView) view.findViewById(R.id.tv_subcat_price2);
            tv_currency = (TextView) view.findViewById(R.id.tv_subcat_currency);
            tv_reward = (TextView) view.findViewById(R.id.tv_reward_point);
            tv_total = (TextView) view.findViewById(R.id.tv_subcat_total);
            tv_contetiy = (TextView) view.findViewById(R.id.tv_subcat_contetiy);
            tv_add = (TextView) view.findViewById(R.id.tv_subcat_add);
            iv_logo = (ImageView) view.findViewById(R.id.iv_subcat_img);
            iv_plus = (ImageView) view.findViewById(R.id.iv_subcat_plus);
            iv_minus = (ImageView) view.findViewById(R.id.iv_subcat_minus);
            iv_remove = (ImageView) view.findViewById(R.id.iv_subcat_remove);
            tv_mrp.setPaintFlags( tv_price.getPaintFlags()|Paint.STRIKE_THRU_TEXT_FLAG);
            ll=view.findViewById(R.id.pll);
            iv_remove.setVisibility(View.GONE);
            iv_minus.setOnClickListener(this);
            iv_plus.setOnClickListener(this);
            tv_add.setOnClickListener(this);
            iv_logo.setOnClickListener(this);
//            textView1.setOnClickListener(this);
//            textView2.setOnClickListener(this);
//            textView3.setOnClickListener(this);
//            textView4.setOnClickListener(this);
//            textView5.setOnClickListener(this);
//            textView6.setOnClickListener(this);
            dropdown = view.findViewById(R.id.spinner_prize);

//            llMain=(LinearLayout)view.findViewById(R.id.units_layout);
//            textView= new TextView(context);

            CardView cardView = (CardView) view.findViewById(R.id.card_view);
            cardView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            int position = getAdapterPosition();


//            if (id == R.id.iv_subcat_plus) {
//                int qty = Integer.valueOf(tv_contetiy.getText().toString());
//                qty = qty + 1;
//                tv_contetiy.setText(String.valueOf(qty));
//
//            } else
            if (id == R.id.iv_subcat_minus) {

                int qty = 0;
                if (!tv_contetiy.getText().toString().equalsIgnoreCase(""))
                    qty = Integer.valueOf(tv_contetiy.getText().toString());

                if (qty > 0) {
                    qty = qty - 1;
                    tv_contetiy.setText(String.valueOf(qty));
                }

            } else if (id == R.id.iv_subcat_plus) {


                int qty = Integer.valueOf(tv_contetiy.getText().toString());
                qty = qty + 1;
                tv_contetiy.setText(String.valueOf(qty));


                HashMap<String, String> map = new HashMap<>();

                preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
                language = preferences.getString("language", "");

                map.put("product_id", modelList.get(position).getProduct_id());

                map.put("c_id" , modelList.get(position).getCart_id());
                asubunit = modelList.get(position).getSubunits();
                bsubprice = modelList.get(position).getSubprice();
//


                // map.put("storeid", modelList.get(position).getStoreid());
                unit = (String) tv_price.getText();
                unitvalue = (String) tv_price1.getText();
                pricee = (String) tv_price2.getText();

                //  Log.e("unit",unit+" "+unitvalue);

                map.put("product_name", modelList.get(position).getProduct_name());
                map.put("category_id", modelList.get(position).getCategory_id());
                map.put("product_description", modelList.get(position).getProduct_description());
                map.put("deal_price", modelList.get(position).getDeal_price());
                map.put("start_date", modelList.get(position).getStart_date());
                map.put("start_time", modelList.get(position).getStart_time());
                map.put("end_date", modelList.get(position).getEnd_date());
                map.put("end_time", modelList.get(position).getEnd_time());
                map.put("price", pricee);
                map.put("product_image", modelList.get(position).getProduct_image());
                map.put("status", modelList.get(position).getStatus());
                map.put("in_stock", modelList.get(position).getStoreid());
                map.put("unit_value", unitvalue + unit);
                map.put("unit", unit + unitvalue);
                map.put("increament", modelList.get(position).getIncreament());
                map.put("rewards", modelList.get(position).getRewards());
                map.put("stock", modelList.get(position).getStock());
                map.put("title", modelList.get(position).getStoreid());


                if (!tv_contetiy.getText().toString().equalsIgnoreCase("0")) {


                    if (dbcart.isTileInCart(map.get("title")) == "empty" || dbcart.isTileInCart(map.get("title")) == (modelList.get(position).getStoreid())) {


                        Cursor res=dbcart.viewAll();

                        while (res.moveToNext()){
                            String a=res.getString(2);
                            String b=res.getString(9);


                            if(a.equals(map.get("product_id"))
                                    && b.equals(map.get("unit"))){
                                String qty1=res.getString(1);
                                String id1=res.getString(0);
                                int no= Integer.parseInt(qty1)+1;
                                dbcart.update(no+"",id1);
//                                Toast.makeText(context, Integer.parseInt(qty1)+"", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }


                        if(dbcart.getProductUnit(map.get("c_id")).equals(""))
                        {
                            if (dbcart.isInCart(map.get("c_id"))) {

                                dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
                                tv_add.setText(context.getResources().getString(R.string.tv_pro_update));

                            } else {

                                dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
                                tv_add.setText(context.getResources().getString(R.string.tv_pro_update));

                            }
                        }
                        else if(dbcart.getProductUnit(map.get("c_id")).equals(selectedunit))
                        {
                            if (dbcart.isInCart(map.get("c_id"))) {
                                dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
                                tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
                            } else {
                                dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
                                tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
                            }
                        }
                        else
                        {
                            dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));

                            Toast.makeText(context, "Please update with the same unit you selected previously", Toast.LENGTH_SHORT).show();

                        }


                    } else {
                        Toast.makeText(context.getApplicationContext(), "you can only select product from one store at a time", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    dbcart.removeItemFromCart(map.get("c_id"));
                    tv_add.setText(context.getResources().getString(R.string.tv_pro_add));
                }

                Double items = Double.parseDouble(dbcart.getInCartItemQty(map.get("c_id")));
                Double price = Double.parseDouble(map.get("price").trim());
                Double reward = Double.parseDouble(map.get("rewards"));
                tv_reward.setText("" + reward * items);
                tv_total.setText("" + price * items);
                tv_total.setVisibility(View.GONE);
                ((MainActivity) context).setCartCounter("" + dbcart.getCartCount());

            } else if (id == R.id.iv_subcat_img) {
                preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
                language = preferences.getString("language", "");
                Log.d("lang", language);
                if (language.contains("english")) {
                    showProductDetail(modelList.get(position).getProduct_image(),
                            modelList.get(position).getProduct_name(),
                            modelList.get(position).getProduct_description(),
                            "",
                            position, tv_contetiy.getText().toString(), pricee, unit, unitvalue);
                }
                else {

                    showProductDetail(modelList.get(position).getProduct_image(),
                            modelList.get(position).getProduct_name(),
                            modelList.get(position).getProduct_description(),
                            "",
                            position, tv_contetiy.getText().toString(), pricee, unit, unitvalue);
                }
            }



//            if (id == R.id.one) {
//
//                String aa = (String) textView1.getText();
//
//                String subprce = b.get(0);
//                tv_price.setText(aa);
//                tv_price2.setText(subprce);
//                tv_currency.setText(context.getResources().getString(R.string.currency));
//                tv_price1.setText("");
//                // Toast.makeText(context, "ssdf", Toast.LENGTH_SHORT).show();
//                textView1.setBackgroundResource(R.color.color_1);
//                textView2.setBackgroundResource(R.color.gray);
//                textView3.setBackgroundResource(R.color.gray);
//                textView4.setBackgroundResource(R.color.gray);
//                textView5.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
//                textView6.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
//
//
//            } else if (id == R.id.two) {
//
//
//                String subprce = b.get(1);
//                String aa = (String) textView2.getText();
//                tv_price.setText(aa);
//                tv_price1.setText("");
//                tv_price2.setText(subprce);
//                textView1.setBackgroundResource(R.color.gray);
//                textView2.setBackgroundResource(R.color.color_1);
//                textView3.setBackgroundResource(R.color.gray);
//                textView4.setBackgroundResource(R.color.gray);
//                textView5.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
//                textView6.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
//
//
//            } else if (id == R.id.three) {
//
//                String subprce = b.get(2);
//                String aa = (String) textView3.getText();
//                tv_price.setText(aa);
//                tv_price1.setText("");
//                tv_price2.setText(subprce);
//                textView1.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
//                textView2.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
//                textView3.setBackgroundColor(ContextCompat.getColor(context, R.color.color_1));
//                textView4.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
//                textView5.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
//                textView6.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
//
//
//            } else if (id == R.id.four) {
//
//
//                String subprce = b.get(3);
//                String aa = (String) textView4.getText();
//                tv_price.setText(aa);
//                tv_price2.setText(subprce);
//                tv_price1.setText("");
//                textView1.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
//                textView2.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
//                textView3.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
//                textView4.setBackgroundColor(ContextCompat.getColor(context, R.color.color_1));
//                textView5.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
//                textView6.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
//
//            } else if (id == R.id.five) {
//
//                String subprce = b.get(4);
//                String aa = (String) textView5.getText();
//                tv_price.setText(aa);
//                tv_price2.setText(subprce);
//                tv_price1.setText("");
//                textView1.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
//                textView2.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
//                textView3.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
//                textView4.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
//                textView5.setBackgroundColor(ContextCompat.getColor(context, R.color.color_1));
//                textView6.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
//
//
//            } else if (id == R.id.six) {
//
//                String aa = (String) textView1.getText();
//                String subprce = b.get(5);
//                tv_price.setText(aa);
//
//                tv_price2.setText(subprce);
//                tv_currency.setText(context.getResources().getString(R.string.currency));
//                tv_price1.setText("");
//
//                textView1.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
//                textView2.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
//                textView3.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
//                textView4.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
//                textView5.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
//                textView6.setBackgroundColor(ContextCompat.getColor(context, R.color.color_1));
//
//
//            }

        }
    }
}