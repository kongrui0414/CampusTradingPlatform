package com.example.campustradingplatform.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;

import java.util.List;

public class BaseAdapter<T> extends DelegateAdapter.Adapter<BaseViewHolder> {
    private LayoutHelper layoutHelper;
    private int LayoutId = -1;

    private List<T> mDatas;

    public BaseAdapter(LayoutHelper layoutHelper,int layoutId,List<T> mDatas){
        this.layoutHelper = layoutHelper;
        this.LayoutId = layoutId;
        this.mDatas = mDatas;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(LayoutId, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
