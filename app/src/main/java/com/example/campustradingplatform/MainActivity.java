package com.example.campustradingplatform;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView navigation;
    //默认选择第一个fragment
    private int lastSelectedPosition = 0;

    private HomeFragment firstFragment;
    private DivideFragment secondFragment;
    private PublicFragment thirdFragment;
    private MsgListFragment fourthFragment;
    private MyCenterFragment fifthFragment;

    private Fragment[] fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigation = this.findViewById(R.id.navigation);
        initFragments();
    }

    private void initFragments() {
        //监听切换事件
        navigation.setOnNavigationItemSelectedListener(this);
        //平均布局
//        setItemType(navigation);
        //添加角标消息数
//        setAddNumber();
        firstFragment = new HomeFragment();
        secondFragment = new DivideFragment();
        thirdFragment = new PublicFragment();
        fourthFragment = new MsgListFragment();
        fifthFragment = new MyCenterFragment();
        fragments = new Fragment[]{firstFragment, secondFragment, thirdFragment,fourthFragment,fifthFragment};
        lastSelectedPosition = 0;
//        //默认提交第一个
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.tb, firstFragment)//添加
                .show(firstFragment)//展示
                .commit();//提交
    }

//    private void setAddNumber() {
//        //获取整个的NavigationView
//        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
//        //获取所添加的每一个Tab，并给第三个Tab添加消息角标
//        View tabView = menuView.getChildAt(2 );
//        BottomNavigationItemView itemView = (BottomNavigationItemView) tabView;
//        //加载我们的角标布局View，新创建的一个布局
//        View badgeView = LayoutInflater.from(this).inflate(R.layout.number_badge, menuView, false);
//        TextView number=badgeView.findViewById(R.id.msg_number);
//        //设置显示的内容
//        number.setText("99");
//        //添加到Tab上
//        itemView.addView(badgeView);
//    }

    /**
     * 切换Fragment
     * @param lastIndex 上个显示Fragment的索引
     * @param index     需要显示的Fragment的索引
     */
    public void setDefaultFragment(int lastIndex, int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[lastIndex]);
        if (!fragments[index].isAdded()) {
            transaction.add(R.id.tb, fragments[index]);
        }
//        //需要展示fragment下标的位置
//        //commit：安排该事务的提交。这一承诺不会立即发生;它将被安排在主线程上，以便在线程准备好的时候完成。
//        //commitAllowingStateLoss：与 commit类似，但允许在活动状态保存后执行提交。这是危险的，因为如果Activity需要从其状态恢复，
//        // 那么提交就会丢失，因此，只有在用户可以意外地更改UI状态的情况下，才可以使用该提交
        transaction.show(fragments[index]).commit();
    }

    /**
     * 切换事件
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_homefirst:
                if (0 != lastSelectedPosition) {
                    setDefaultFragment(lastSelectedPosition, 0);
                    lastSelectedPosition = 0;
                }
                return true;
            case R.id.navigation_homesecond:
                if (1 != lastSelectedPosition) {
                    setDefaultFragment(lastSelectedPosition, 1);
                    lastSelectedPosition = 1;
                }
                return true;
            case R.id.navigation_homethird:
                if (2 != lastSelectedPosition) {
                    setDefaultFragment(lastSelectedPosition, 2);
                    lastSelectedPosition = 2;
                }
                return true;
            case R.id.navigation_homefourth:
                if (3 != lastSelectedPosition) {
                    setDefaultFragment(lastSelectedPosition, 3);
                    lastSelectedPosition = 3;
                }
                return true;
            case R.id.navigation_homefifth:
                if (4 != lastSelectedPosition) {
                    setDefaultFragment(lastSelectedPosition, 4);
                    lastSelectedPosition = 4;
                }
                return true;
        }
        return false;
    }

    /**
     * 防止超过3个fragment布局不平分
     */
//    @SuppressLint("RestrictedApi")
//    private  void setItemType(BottomNavigationView view) {
//        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
//        try {
//            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
//            shiftingMode.setAccessible(true);
//            shiftingMode.setBoolean(menuView, false);
//            shiftingMode.setAccessible(false);
//            for (int i = 0; i < menuView.getChildCount(); i++) {
//                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
//                //noinspection RestrictedApi
////                item.setShiftingMode(false);
//                // set once again checked value, so view will be updated
//                //noinspection RestrictedApi
//                item.setChecked(item.getItemData().isChecked());
//            }
//        } catch (NoSuchFieldException e) {
//            Log.e("BNVHelper", "Unable to get shift mode field", e);
//        } catch (IllegalAccessException e) {
//            Log.e("BNVHelper", "Unable to change value of shift mode", e);
//        }
//    }

}

