package com.example.campustradingplatform;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.campustradingplatform.Chat.dao.GoodsDao;
import com.example.campustradingplatform.Goods.Goods;
import com.example.campustradingplatform.Login.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 武当山道士 on 2017/8/16.
 */

public class PublicFragment extends Fragment {
    private EditText goodsNameText;
    private Button publish;
    private EditText goodsDescriptionText;
    private EditText originalPriceText;
    private EditText presentPriceText;
    private EditText oldorNew;
    private EditText position;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.public_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        publish=(Button)getActivity().findViewById(R.id.publish);
        goodsNameText = (EditText)getActivity().findViewById(R.id.borrow_item_title_et);
        goodsDescriptionText = (EditText)getActivity().findViewById(R.id.borrow_item_description_et);
        originalPriceText = (EditText)getActivity().findViewById(R.id.borrow_price_default);
        presentPriceText = (EditText)getActivity().findViewById(R.id.nowprice);
        oldorNew = (EditText)getActivity().findViewById(R.id.borrow_deadline_date_tv);
        position = (EditText) getActivity().findViewById(R.id.borrow_position);
        originalPriceText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        presentPriceText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(addgoods).start();
                Toast.makeText(getActivity(),"添加商品成功",Toast.LENGTH_LONG).show();
                goodsNameText.setText("");
                goodsDescriptionText.setText("");
                originalPriceText.setText("");
                presentPriceText.setText("");
                oldorNew.setText("");
                position.setText("");
            }
        });
    }

    Runnable addgoods = new Runnable() {
        @Override
        public void run() {
            Goods goods = new Goods();
            User user;
            Activity activity = getActivity();
            user = ((MainActivity)activity).getUser();
            goods.setSellerId(user.getId());
            goods.setGoodsName(goodsNameText.getText().toString());
            goods.setOriginalPrice(Float.parseFloat(originalPriceText.getText().toString()));
            goods.setPresentPrice(Float.parseFloat(presentPriceText.getText().toString()));
            goods.setOldorNew(oldorNew.getText().toString());
            goods.setDescription(goodsDescriptionText.getText().toString());
            goods.setLaunchTime(new Date());
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://121.37.212.124:3306/ctp","root","ABC123!!");
                PreparedStatement ps;
                ps = connection.prepareStatement("insert into goods values(?,null,?,?,?,?,?,Now())");
                ps.setString(1,Integer.toString(goods.getSellerId()));
                ps.setString(2,goods.getGoodsName());
                ps.setString(3,goods.getDescription());
                ps.setString(4,Float.toString(goods.getOriginalPrice()));
                ps.setString(5,Float.toString(goods.getPresentPrice()));
                ps.setString(6,goods.getOldorNew());
                int rs = ps.executeUpdate();
                Log.d("zjjWed","终于成功了淦");
                connection.close();
            } catch (Exception e) {
                Log.d("zjjWed","报错了淦");
                e.printStackTrace();
            }
        }
    };
}

