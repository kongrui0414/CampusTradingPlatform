package com.example.campustradingplatform.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campustradingplatform.Order.Order;
import com.example.campustradingplatform.Order.changeOrder;
import com.example.campustradingplatform.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.ViewHolder> {
    private List<Order> morderList;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = morderList.get(position);
        changeOrder changeOrder = new changeOrder(order);
        holder.sellerName.setText(order.getSellerName());
        holder.goodsName.setText(order.getGoodsName());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        holder.time.setText(dateFormat.format(order.getOrderTime()));
        holder.price.setText(Float.toString(order.getPrice()));
        if(order.getState()==0){
            holder.state.setText("交易中");
            holder.button.setVisibility(View.VISIBLE);
            holder.finish.setVisibility(View.INVISIBLE);
            Log.d("zjj1","正在交易的订单");
        }else {
            holder.state.setText("交易完成");
            holder.button.setVisibility(View.INVISIBLE);
            holder.finish.setVisibility(View.VISIBLE);
        }
        holder.button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                changeOrder.changeState();
                holder.state.setText("交易完成");
                holder.button.setVisibility(View.INVISIBLE);
                holder.finish.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return morderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView sellerName;
        TextView goodsName;
        TextView time;
        TextView price;
        Button button;
        TextView finish;
        TextView state;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            sellerName = (TextView) itemView.findViewById(R.id.myOrder_sellerName);
            goodsName = (TextView) itemView.findViewById(R.id.myOrder_goodsName);
            time = (TextView) itemView.findViewById(R.id.myOrder_time);
            price = (TextView) itemView.findViewById(R.id.myOrder_price);
            button = (Button) itemView.findViewById(R.id.myOrder_button);
            finish = (TextView) itemView.findViewById(R.id.myOrder_finish);
            state = (TextView) itemView.findViewById(R.id.myOrder_state);
        }
    }

    public MyOrdersAdapter(List<Order> orderList){
        this.morderList = orderList;
    }
}
