package com.example.campustradingplatform.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campustradingplatform.Goods.Goods;
import com.example.campustradingplatform.Goods.changeGoods;
import com.example.campustradingplatform.MyCenter_mySellGoods;
import com.example.campustradingplatform.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class SellGoodsAdapter extends RecyclerView.Adapter<SellGoodsAdapter.ViewHolder>{
    private List<Goods> mgoodsList;

    private static MyCenter_mySellGoods myCenter_mySellGoods;

    @NonNull
    @Override
    public SellGoodsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sellgoods_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Goods goods = mgoodsList.get(position);
        holder.goodsName.setText(goods.getGoodsName());
        holder.originalPrice.setText(Float.toString(goods.getOriginalPrice()));
        holder.presentPrice.setText(Float.toString(goods.getPresentPrice()));
        holder.oldorNew.setText(goods.getOldorNew());
        holder.description.setText(goods.getDescription());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        holder.time.setText(dateFormat.format(goods.getLaunchTime()));
        holder.editGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.goodsName.setVisibility(View.INVISIBLE);
                holder.editGoodsName.setVisibility(View.VISIBLE);
                holder.editGoodsName.setText(goods.getGoodsName());

                holder.originalPrice.setVisibility(View.INVISIBLE);
                holder.editOriginalPrice.setVisibility(View.VISIBLE);
                holder.editOriginalPrice.setText(Float.toString(goods.getOriginalPrice()));

                holder.presentPrice.setVisibility(View.INVISIBLE);
                holder.editPresentPrice.setVisibility(View.VISIBLE);
                holder.editPresentPrice.setText(Float.toString(goods.getPresentPrice()));

                holder.oldorNew.setVisibility(View.INVISIBLE);
                holder.editOldOrNew.setVisibility(View.VISIBLE);
                holder.editOldOrNew.setText(goods.getOldorNew());

                holder.description.setVisibility(View.INVISIBLE);
                holder.editDescription.setVisibility(View.VISIBLE);
                holder.editDescription.setText(goods.getDescription());

                holder.editGoods.setVisibility(View.INVISIBLE);
                holder.saveGoods.setVisibility(View.VISIBLE);
            }
        });
        holder.saveGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.editGoodsName.getText().toString().isEmpty() || holder.editOriginalPrice.getText().toString().isEmpty() ||
                holder.editPresentPrice.getText().toString().isEmpty() || holder.editOldOrNew.getText().toString().isEmpty() || holder.description.getText().toString().isEmpty())
                {
                    Toast.makeText(MyCenter_mySellGoods.myCenter_mySellGoods,"内容不能为空",Toast.LENGTH_SHORT).show();
                }
                else {
                    String goodsname = holder.editGoodsName.getText().toString();
                    float originalprice = Float.parseFloat(holder.editOriginalPrice.getText().toString());
                    float presentprice = Float.parseFloat(holder.editPresentPrice.getText().toString());
                    String oldornew = holder.editOldOrNew.getText().toString();
                    String description = holder.description.getText().toString();

                    goods.setGoodsName(goodsname);
                    goods.setOriginalPrice(originalprice);
                    goods.setPresentPrice(presentprice);
                    goods.setOldorNew(oldornew);
                    goods.setDescription(description);
                    new changeGoods().UpdateGoods(goods);

                    holder.goodsName.setVisibility(View.VISIBLE);
                    holder.editGoodsName.setVisibility(View.INVISIBLE);
                    holder.goodsName.setText(goods.getGoodsName());

                    holder.originalPrice.setVisibility(View.VISIBLE);
                    holder.editOriginalPrice.setVisibility(View.INVISIBLE);
                    holder.originalPrice.setText(Float.toString(goods.getOriginalPrice()));

                    holder.presentPrice.setVisibility(View.VISIBLE);
                    holder.editPresentPrice.setVisibility(View.INVISIBLE);
                    holder.presentPrice.setText(Float.toString(goods.getPresentPrice()));

                    holder.oldorNew.setVisibility(View.VISIBLE);
                    holder.editOldOrNew.setVisibility(View.INVISIBLE);
                    holder.oldorNew.setText(goods.getOldorNew());

                    holder.description.setVisibility(View.VISIBLE);
                    holder.editDescription.setVisibility(View.INVISIBLE);
                    holder.description.setText(goods.getDescription());

                    holder.editGoods.setVisibility(View.VISIBLE);
                    holder.saveGoods.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mgoodsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView goodsName;
        EditText editGoodsName;
        TextView originalPrice;
        EditText editOriginalPrice;
        TextView presentPrice;
        EditText editPresentPrice;
        TextView oldorNew;
        EditText editOldOrNew;
        TextView description;
        EditText editDescription;
        TextView time;
        Button editGoods;
        Button saveGoods;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            goodsName = (TextView) itemView.findViewById(R.id.sellgoods_goodsName);
            editGoodsName = (EditText) itemView.findViewById(R.id.sellgoods_editGoodsName);
            originalPrice = (TextView) itemView.findViewById(R.id.sellgoods_originalPrice);
            editOriginalPrice = (EditText) itemView.findViewById(R.id.sellgoods_editOriginalPrice);
            presentPrice = (TextView) itemView.findViewById(R.id.sellgoods_presentPrice);
            editPresentPrice = (EditText) itemView.findViewById(R.id.sellgoods_editPresentPrice);
            oldorNew  = (TextView) itemView.findViewById(R.id.sellgoods_oldorNew);
            editOldOrNew = (EditText) itemView.findViewById(R.id.sellgoods_editOldOrNew);
            description = (TextView) itemView.findViewById(R.id.sellgoods_description);
            editDescription = (EditText) itemView.findViewById(R.id.sellgoods_editDescription);
            time = (TextView) itemView.findViewById(R.id.sellgoods_time);
            editGoods = (Button) itemView.findViewById(R.id.sellgoods_editGoods);
            saveGoods = (Button) itemView.findViewById(R.id.sellgoods_saveGoods);
        }
    }

    public SellGoodsAdapter(List<Goods> goodsList){
        mgoodsList = goodsList;
    }
}
