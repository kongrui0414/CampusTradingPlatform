package com.example.campustradingplatform;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.leon.lib.settingview.LSettingItem;

/**
 * Created by 武当山道士 on 2017/8/16.
 */

public class MyCenterFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_center_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //对一个控件进行点击事件
        LSettingItem dingdan =(LSettingItem)getActivity().findViewById(R.id.item_dingdan);
//        dingdan.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
//            @Override
//            public void click() {
//                Toast.makeText(getActivity(),"暂时没有订单",Toast.LENGTH_SHORT).show();
//            }
//        });
        dingdan.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click() {
                Toast.makeText(getActivity(),"暂时没有订单",Toast.LENGTH_SHORT).show();
            }
        });
    }
}

