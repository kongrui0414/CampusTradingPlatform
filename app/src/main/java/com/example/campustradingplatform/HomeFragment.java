package com.example.campustradingplatform;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.ColumnLayoutHelper;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.example.campustradingplatform.Home.*;
import com.sunfusheng.marqueeview.MarqueeView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.*;

public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    ClassifyAdapter Adapter_ColumnLayout;

    String[] ITEM_NAMES = {"天猫", "聚划算", "天猫国际", "外卖"};

    private ArrayList<HashMap<String,Object>> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        recyclerView = view.findViewById(R.id.my_recycle_view);
//        init();
        initView();

        return view;
    }

    private void initView() {
        VirtualLayoutManager virtualLayoutManager = new VirtualLayoutManager(getActivity());
        recyclerView.setLayoutManager(virtualLayoutManager);
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        recyclerView.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 10);
        /**
         * 轮播Adapter
         */
        BaseDelegeteAdapter bannerAdapter = new BaseDelegeteAdapter(getActivity(),
                new LinearLayoutHelper(), R.layout.vlayout_banner, 1) {
            @Override
            public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int i) {
                ArrayList<Object> arrayList = new ArrayList<>();
                arrayList.add(R.mipmap.ic_launcher);
                arrayList.add("https://gw.alicdn.com/imgextra/i3/20/O1CN01UTTfyr1C1CSm4hzOv_!!20" +
                        "-0" +
                        "-lubanu.jpg");
                arrayList.add("https://gw.alicdn.com/imgextra/i3/106/O1CN01etvHvW1CeaWUFfTu9_" +
                        "!!106-0" +
                        "-lubanu.jpg");
                arrayList.add("https://gw.alicdn.com/imgextra/i3/20/O1CN01UTTfyr1C1CSm4hzOv_!!20" +
                        "-0" +
                        "-lubanu.jpg");
                arrayList.add("https://gw.alicdn.com/imgextra/i2/14/O1CN019L7VgZ1ByS5PJJpnO_!!14" +
                        "-0" +
                        "-lubanu.jpg");
                arrayList.add("https://img.alicdn.com/tps/i4/TB1YMbpeoKF3KVjSZFESutExFXa.jpg");
                // 绑定数据
                Banner mBanner = baseViewHolder.getView(R.id.banner);
                //设置banner样式
                mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                //设置图片集合
                mBanner.setImages(arrayList);
                //设置banner动画效果
                mBanner.setBannerAnimation(Transformer.Default);
                //设置标题集合（当banner样式有显示title时）
                //        mBanner.setBannerTitles(titles);
                //设置自动轮播，默认为true
                mBanner.isAutoPlay(true);
                mBanner.setImageLoader(new GlideImageLoader());
                //设置轮播时间
                mBanner.setDelayTime(3000);
                //设置指示器位置（当banner模式中有指示器时）
                mBanner.setIndicatorGravity(BannerConfig.CENTER);
                //banner设置方法全部调用完毕时最后调用
                mBanner.start();

                mBanner.setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        Toast.makeText(getContext(), "banner点击了" + position,
                                Toast.LENGTH_SHORT).show();
                    }
                });
                super.onBindViewHolder(baseViewHolder, i);
            }
        };
        /**
         *Success
         */
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(4);
        gridLayoutHelper.setPadding(0, 20, 0, 0);
        gridLayoutHelper.setVGap(20);//垂直间距
        gridLayoutHelper.setHGap(0);//水平间距
        BaseAdapter menuAdapter = new BaseAdapter(gridLayoutHelper, R.layout.classify_item, Arrays.asList(ITEM_NAMES)){
            @Override
            public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                holder.setText(R.id.classify_text, ITEM_NAMES[position%4]);
                holder.setImageResource(R.id.classify_image, R.mipmap.ic_launcher);
                holder.getView(R.id.classify_item).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(), "click", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
//        BaseDelegeteAdapter menuAdapter = new BaseDelegeteAdapter(getActivity(), gridLayoutHelper,
//                R.layout.classify_item, 4) {
//            @Override
//            public void onBindViewHolder(@NonNull BaseViewHolder holder, final int position) {
//                holder.setText(R.id.classify_text, ITEM_NAMES[position] + "");
//                holder.setImageResource(R.id.classify_image, R.mipmap.ic_launcher);
//                holder.getView(R.id.classify_text).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Toast.makeText(getContext(), ITEM_NAMES[position],
//                                Toast.LENGTH_SHORT).show();
//                    }
//                });
//                super.onBindViewHolder(holder, position);
//            }
//        };


        /**
         *
         */
        GridLayoutHelper gridLayoutHelper_list = new GridLayoutHelper(2);
        gridLayoutHelper.setPadding(0, 20, 0, 0);
        gridLayoutHelper.setVGap(20);//垂直间距
        gridLayoutHelper.setHGap(0);//水平间距
        BaseDelegeteAdapter listAdapter = new BaseDelegeteAdapter(getActivity(), gridLayoutHelper_list,
                R.layout.classify_item, 10) {
            @Override
            public void onBindViewHolder(@NonNull BaseViewHolder holder, final int position) {
                holder.setText(R.id.classify_text, ITEM_NAMES[position%4] + "");
                holder.setImageResource(R.id.classify_image, R.mipmap.ic_launcher);
                holder.getView(R.id.classify_text).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(), ITEM_NAMES[position],
                                Toast.LENGTH_SHORT).show();
                    }
                });
                super.onBindViewHolder(holder, position);
            }
        };

        DelegateAdapter delegateAdapter = new DelegateAdapter(virtualLayoutManager, true);
        delegateAdapter.addAdapter(bannerAdapter);
        delegateAdapter.addAdapter(menuAdapter);
        delegateAdapter.addAdapter(listAdapter);

        recyclerView.setAdapter(delegateAdapter);
    }
    }
//
//    private void init() {
//        /**
//         * recycle 和 VirtualLayoutManager 绑定
//         */
//        VirtualLayoutManager layoutManager = new VirtualLayoutManager(getActivity());
//        recyclerView.setLayoutManager(layoutManager);
//        /**
//         * 设置复用回收池
//         */
//        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
//        recyclerView.setRecycledViewPool(viewPool);
//        viewPool.setMaxRecycledViews(0, 10);
//        /**
//         * 数据填充
//         */
//
//        list = new ArrayList<>();
//        for (int i = 0;i<4;i++)
//        {
//            HashMap<String,Object> map = new HashMap<>();
//            map.put("text", i+"");
//            map.put("image", R.mipmap.ic_launcher);
//            list.add(map);
//        }
//        /**
//         *根据数据列表，创建对应的LayoutHelper
//         */
//        ColumnLayoutHelper columnLayoutHelper = new ColumnLayoutHelper();
//        // 创建对象
//
//        // 公共属性
//        columnLayoutHelper.setItemCount(4);// 设置布局里Item个数
//        columnLayoutHelper.setPadding(20, 20, 20, 20);// 设置LayoutHelper的子元素相对LayoutHelper边缘的距离
//        columnLayoutHelper.setMargin(20, 20, 20, 20);// 设置LayoutHelper边缘相对父控件（即RecyclerView）的距离
//        columnLayoutHelper.setBgColor(Color.GRAY);// 设置背景颜色
//        columnLayoutHelper.setAspectRatio(6);// 设置设置布局内每行布局的宽与高的比
//
//        // columnLayoutHelper特有属性
//        columnLayoutHelper.setWeights(new float[]{25, 25, 25, 25});// 设置该行每个Item占该行总宽度的比例
//        // 同上面Weigths属性讲解
//
//        // 创建自定义的Adapter对象 & 绑定数据 & 绑定对应的LayoutHelper进行布局绘制
//        Adapter_ColumnLayout  = new ClassifyAdapter(getActivity(), columnLayoutHelper, 4, list);
////        Adapter_ColumnLayout.setOnItemClickListener(this);
//        /**
//         *5.
//         */
//        List<DelegateAdapter.Adapter> adapters = new LinkedList<>();
//        adapters.add(Adapter_ColumnLayout);
//        DelegateAdapter delegateAdapter = new DelegateAdapter(layoutManager);
//        delegateAdapter.setAdapters(adapters);
//        recyclerView.setAdapter(delegateAdapter);
//    }


