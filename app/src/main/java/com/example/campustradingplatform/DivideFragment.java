package com.example.campustradingplatform;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campustradingplatform.Goods.MyGoods;
import com.example.campustradingplatform.adapter.GoodsAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Rui
 */
public class DivideFragment extends Fragment {

    private MyGoods[] myGoods={new MyGoods("Basketball",R.drawable.basketball),
            new MyGoods("球拍",R.drawable.racket),
            new MyGoods("手表",R.drawable.watch),
            new MyGoods("《深入理解计算机系统》",R.drawable.book),
            new MyGoods("苹果手机",R.drawable.iphone),
            new MyGoods("高帮帆布鞋",R.drawable.shoes),
            new MyGoods("帆布包",R.drawable.bag),
            new MyGoods("帆布包-fw",R.drawable.bag2),
            new MyGoods("书包",R.drawable.schoolbag),
            new MyGoods("书包-粉蓝",R.drawable.schoolbag2),
            new MyGoods("书包-金色",R.drawable.schoolbag3),
            new MyGoods("鼠标",R.drawable.mouse),
            new MyGoods("镜子",R.drawable.mirror),
            new MyGoods("《计算机操作系统》",R.drawable.book2),
            new MyGoods("《第一行代码》",R.drawable.book3),
            new MyGoods("保温杯",R.drawable.bottle),
            new MyGoods("蓝月亮洗衣液",R.drawable.xiyiye),
            new MyGoods("头戴式耳机",R.drawable.earphone),
            new MyGoods("蓝牙耳机",R.drawable.earphone2),
            new MyGoods("安耐晒防晒霜",R.drawable.fangshai),
            new MyGoods("键盘",R.drawable.keyboard),
            new MyGoods("帆布包-special",R.drawable.bag3)
    };

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.divide_fragment, container, false);
        recyclerView=view.findViewById(R.id.recycler_view_card);
        initView();
        return view;
    }

    private List<MyGoods> myGoodsList=new ArrayList<>();
    private GoodsAdapter adapterCard;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("abc123","setContentView前");
//        setContentView(R.layout.divide_fragment);
//        scrollView = (ScrollView) findViewById(R.id.tools_scrlllview);
//        shopAdapter = new ShopAdapter(getSupportFragmentManager());
//        inflater = LayoutInflater.from(this);
//        showToolsView();
//        initPager();
        Log.d("abc123","initGoodsCard前");
        initGoodsCard();
//        RecyclerView recyclerView=(RecyclerView)getActivity().findViewById(R.id.recycler_view_card);

//        try{
//            GridLayoutManager layoutManager = new GridLayoutManager(this.getActivity(), 2);//参数：context，列数
//            if(layoutManager != null){
//                recyclerView.setLayoutManager(layoutManager);
//                Log.d("abc123","对象不为空");
//            }else {
//                Log.d("abc123","对象为空");
//            }
//        }catch (Exception e){
//            Log.d("abc123","错误");
//            e.printStackTrace();
//
//        }
////        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
//
////        layoutManager.setOrientation(RecyclerView.VERTICAL);
//
//        adapterCard=new GoodsAdapter(myGoodsList);
//        recyclerView.setAdapter(adapterCard);
    }

    private void initView(){
        try{
            GridLayoutManager layoutManager = new GridLayoutManager(this.getActivity(), 2);//参数：context，列数
            if(layoutManager != null){
                recyclerView.setLayoutManager(layoutManager);
                Log.d("abc123","对象不为空");
            }else {
                Log.d("abc123","对象为空");
            }
        }catch (Exception e){
            Log.d("abc123","错误");
            e.printStackTrace();

        }
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());

//        layoutManager.setOrientation(RecyclerView.VERTICAL);

        adapterCard=new GoodsAdapter(myGoodsList);
        recyclerView.setAdapter(adapterCard);
    }

    private void initGoodsCard(){
        myGoodsList.clear();
        for(int i=0;i<22;i++){
//            Random random=new Random();
//            int index=random.nextInt(myGoods.length);
            myGoodsList.add(myGoods[i]);
//            Log.d("log:","initGoodsCard");
        }
        Log.d("log:","initGoodsCard");
    }

}

