package com.example.campustradingplatform.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.example.campustradingplatform.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ClassifyAdapter extends DelegateAdapter.Adapter<ClassifyAdapter.ViewHolder> {

    private ArrayList<HashMap<String, Object>> list;

    private Context context;
    private LayoutHelper layoutHelper;
    private ViewGroup.LayoutParams params;
    private int count = 0;

//    private ClassifyItemClickListener itemClickListener;

    public ClassifyAdapter(Context context, LayoutHelper layoutHelper, int count, ArrayList<HashMap<String, Object>> list) {
        this(context,layoutHelper,count,new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300),list);
    }

    public ClassifyAdapter(Context context, LayoutHelper layoutHelper, int count, ViewGroup.LayoutParams params, ArrayList<HashMap<String, Object>> list) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.count = count;
        this.params = params;
        this.list = list;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    @NonNull
    @Override
    public ClassifyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.classify_item, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ClassifyAdapter.ViewHolder holder, int position) {
        holder.textView.setText((String)list.get(position).get("text"));
        holder.imageView.setImageResource((Integer) list.get(position).get("image"));
    }

    @Override
    public int getItemCount() {
        return count;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.classify_text);
            imageView = itemView.findViewById(R.id.classify_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}
