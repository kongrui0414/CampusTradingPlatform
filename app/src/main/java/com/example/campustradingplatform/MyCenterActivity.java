package com.example.campustradingplatform;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.leon.lib.settingview.LSettingItem;

public class MyCenterActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_center_fragment);

        //对一个控件进行点击事件
        LSettingItem one =(LSettingItem)findViewById(R.id.item_guanzhu);


        one.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click() {
                Toast.makeText(MyCenterActivity.this,"点击了钱包",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
