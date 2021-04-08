package com.example.campustradingplatform.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.campustradingplatform.Chat.ChatBean.ChatItem;
import com.example.campustradingplatform.Chat.Service.LocaService;
import com.example.campustradingplatform.Chat.Service.LocaServiceThread;
import com.example.campustradingplatform.MainActivity;
import com.example.campustradingplatform.R;

public class PayCheckActivity extends AppCompatActivity {
    ChatItem chatItem;
    private MapView mapView;
    private BaiduMap baiduMap;
    private BitmapDescriptor mBitmapDescriptor;
    private Marker mMarkerTrans;
    private TextView buyerName;
    private TextView gNameText;
    private TextView gPriceText;
    private Button cancelBtn;
    private Button comfirmBtn;
    private LocaServiceThread comfirmThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());  //map使用之前需要先初始化
        setContentView(R.layout.activity_pay_check);

        chatItem = (ChatItem)getIntent().getSerializableExtra("chatItem");
//        Log.d("TAG", "onCreate: "+chatItem);

        initMap();
        initUI();
        initListener();
    }
    public void initListener(){

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayCheckActivity.this.finish();
            }
        });

        comfirmBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                comfirmThread = LocaService.comfirmOrder(chatItem);
                while(!comfirmThread.isFinished());

                Intent intent = new Intent(PayCheckActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    /**
     * description: 初始化map，将选定的地点显示在地图上
     */
    public void initMap(){
        mapView = (MapView) findViewById(R.id.trans_addr_map);
        baiduMap = mapView.getMap();   //获取百度map
        //开启定位图层
        baiduMap.setMyLocationEnabled(true);


        //修改地点
        LatLng ll = getLatLngByStr(chatItem.getTranAddr());  //获取经纬度的值
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll); //设置地图新中心点
        baiduMap.animateMapStatus(update);//反复刷新
        update = MapStatusUpdateFactory.zoomTo(16f);
        baiduMap.animateMapStatus(update);
        createCenterMarker(ll);
    }

    public void initUI(){
        buyerName = (TextView)findViewById(R.id.buyer_name_text);
        buyerName.setText(chatItem.getUser().getUserName());

        gNameText = (TextView)findViewById(R.id.gname_text);
        gNameText.setText(chatItem.getGoods().getGoodsName());

        gPriceText = (TextView)findViewById(R.id.gprice_text);
        gPriceText.setText(String.valueOf(chatItem.getGoods().getPresentPrice()));

        cancelBtn = (Button)findViewById(R.id.cancel_btn);
        comfirmBtn = (Button)findViewById(R.id.submit_btn);
    }

    /**
     * 创建地图中心点marker
     */
    private void createCenterMarker(LatLng latLng) {
        if(latLng == null) return ;
        LatLng center = new LatLng(latLng.latitude, latLng.longitude);
        mBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.icon_mark);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(center)
                .icon(mBitmapDescriptor);
        mMarkerTrans = (Marker) baiduMap.addOverlay(markerOptions);
    }

    /**
     * description:切分/ 字符获取经纬度
     */
    public LatLng getLatLngByStr(String addr){
//        Log.d("TAG", "handleMessage:更新卖家地址 "+addr);
        if("0".equals(addr) || addr==null) return null;

        double lat = Double.valueOf(addr.split("/")[0]);
        double lng = Double.valueOf(addr.split("/")[1]);

        return new LatLng(lat,lng);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();//第三方控件状态
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();  //第三方控件状态
    }

    @Override
    protected void onDestroy() {

        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);  //先删除第三方控件
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}