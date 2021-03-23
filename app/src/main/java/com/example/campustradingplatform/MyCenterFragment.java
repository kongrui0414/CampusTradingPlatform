package com.example.campustradingplatform;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.campustradingplatform.Login.User;
import com.leon.lib.settingview.LSettingItem;

/**
 * Created by 武当山道士 on 2017/8/16.
 */

public class MyCenterFragment extends Fragment {
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_center_fragment, container, false);

        TextView my_center_username_text =(TextView)view.findViewById(R.id.my_center_username);     //获取个人主页中的用户名TextView对象
        my_center_username_text.setText(user.getUserName());                                        //显示用户名

        return view;
    }

    public void getUser(User user){//获取用户类对象的方法
        this.user = user;
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

