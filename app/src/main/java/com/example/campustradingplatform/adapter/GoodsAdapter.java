package com.example.campustradingplatform.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.campustradingplatform.Goods.MyGoods;
import com.example.campustradingplatform.R;

import java.util.List;

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.ViewHolder> {
    private Context mcontext;
    private List<MyGoods> myGoodsList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView goodsimage_card;
        TextView goodsname_card;
        public ViewHolder(View view){
            super(view);
            cardView=(CardView)view;
            goodsimage_card=(ImageView)view.findViewById(R.id.goods_image_card);
            goodsname_card=(TextView)view.findViewById(R.id.goods_name_card);

        }
    }

    public GoodsAdapter(List<MyGoods> goodsList){
        myGoodsList=goodsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mcontext==null){
            mcontext=parent.getContext();
        }
        View view= LayoutInflater.from(mcontext).inflate(R.layout.mygoods_item_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoodsAdapter.ViewHolder holder, int position) {
        MyGoods myGoods=myGoodsList.get(position);
        holder.goodsname_card.setText(myGoods.getName());
        Glide.with(mcontext).load(myGoods.getImageId()).into(holder.goodsimage_card);
    }

    @Override
    public int getItemCount() {

        return myGoodsList.size();
    }
}
