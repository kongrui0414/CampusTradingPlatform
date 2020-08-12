package com.example.campustradingplatform;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTabHost;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabWidget;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private String[] tabs = new String[]{"首页", "分类", "案例", "设置"};
    private Class[] mFragmentClasses = new Class[]{MainActivity.class, Category.class,Publish.class, Message.class,Mine.class};
    private int[] selectorImg = new int[]{R.drawable.tab_shouye, R.drawable.tab_shouye, R.drawable.tab_shouye, R.drawable.tab_shouye,R.drawable.tab_shouye};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTabHost tabHost = findViewById(R.id.tab);
        // 初始化tabHost
        tabHost.setup(MainActivity.this, getSupportFragmentManager(), R.id.fragment_content);
        for (int i = 0; i <=4; i++) {
            tabHost.addTab(tabHost.newTabSpec(tabs[i]).setIndicator(getTabView(i)), mFragmentClasses[i], null);
        }
        // 设置默认tab
        tabHost.setCurrentTab(1);
    }
    /**
     * tab的view对象
     *
     * @param index 索引
     * @return view对象
     */
    private View getTabView(int index) {
        View inflate = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_tab, null);
        ImageView tabImage = inflate.findViewById(R.id.tab_image);
        TextView tabTitle = inflate.findViewById(R.id.tab_title);
        tabImage.setImageResource(selectorImg[index]); // 通过selector来控制图片的改变
        tabTitle.setText(tabs[index]);// 通过selector来控制文字颜色的改变
        return inflate;
    }
}
