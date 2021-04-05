package com.example.campustradingplatform.Chat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.animation.Animation;
import com.baidu.mapapi.animation.Transformation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.campustradingplatform.Chat.ChatBean.ChatItem;
import com.example.campustradingplatform.Chat.Service.LocaService;
import com.example.campustradingplatform.Chat.Service.LocaServiceThread;
import com.example.campustradingplatform.R;
import com.example.campustradingplatform.UtilTools.GlobalVars;

import java.util.ArrayList;
import java.util.List;

public class BaiduMapActivity extends AppCompatActivity {

    private MapView BaiduMap;
    private LocationClient Client;
    private TextView Position;
    private com.baidu.mapapi.map.BaiduMap baiduMap;//地图总控制器
    private boolean isFirstLocate = true;
    private BitmapDescriptor mBitmapDescriptor=null;

    private Marker mMarkerTrans=null;
    private Marker mMarkerSeller=null;

    private ChatItem chatItem;


    private  String nowPosition="";
    private  String prePosition="";

    private String newTransAddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //一定要在setContentView（）前调用
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_baidu_map);
        chatItem = (ChatItem)getIntent().getSerializableExtra("chatItem");


        initMap();
        initlistener();
        getPermission();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){

                    flashUI();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    public void flashUI(){
//        //先查询到本人地址、对方地址、交易地址
        LocaServiceThread thread = LocaService.getFlashedAddrs(chatItem);;
//
        while(!thread.isFinished()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        Log.d("TAG", "flashUI: "+thread.getTransAddr());
//
//
//        //因为如果数据库中有，就用数据库的内容更新当前的地址
//        //如果没有，就用本人的地址算作交易地址
//        //createCenterMarker(getLatLngByStr(thread.getTransAddr()),GlobalVars.UPDATE_TRANS_ADDR_HANDLER);

        if(null != thread.getTransAddr() && !"".equals(thread.getTransAddr())){
            newTransAddr = thread.getTransAddr();
        }

////
////        //本人是买家
        if(chatItem.getUser().isBuyer()){

            if(!"".equals(thread.getSellerAddr()) && null!=thread.getSellerAddr()){
//                Log.d("TAG", "flashUI:卖家： "+thread.getSellerAddr());
                //发送给handler 添加卖家的地点标记
                chatItem.getSeller().setAddr(thread.getSellerAddr());
                Message msg = new Message();
                msg.what = GlobalVars.UPDATE_SELLER_ADDR_HANDLER;
                handler.sendMessage(msg);

            }
        }
        else{ //本人是卖家
//            Log.d("TAG", "flashUI买家： "+thread.getBuyerAddr());
            if(!"-1".equals(thread.getSellerAddr())){
                //发送给handler 添加卖家的地点标记
                chatItem.getBuyer().setAddr(thread.getSellerAddr());
                Message msg = new Message();
                msg.what = GlobalVars.UPDATE_BUYER_ADDR_HANDLER;
                handler.sendMessage(msg);
            }
        }
    }
    /**
     * description: 不影响，传回的值不重要，会重新查询
     */
    public void initlistener(){

        Button returnBtn = (Button)findViewById(R.id.loca_return_btn);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("transAddr", "-1");
                setResult(GlobalVars.GET_RESULT_FROM_BAIDUMAP, intent);
                finish();//结束当前activity
            }
        });

        //返回确认的交易地址下标给前一个页面
        Button button = (Button)findViewById(R.id.confirm_addr);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //用本次选定的地址更新交易地址
                LocaService.updateTransAddrByClick(chatItem);

                //同时清空TmpTrans地址
                LocaService.clearTmpTransAddrByChatID(chatItem);

                Intent intent = new Intent();
                if(null == chatItem.getTranAddr() || "".equals(chatItem.getTranAddr()))
                    intent.putExtra("transAddr", "-1");
                else
                    intent.putExtra("transAddr", chatItem.getTranAddr());

                /*
                 * 调用setResult方法表示我将Intent对象返回给之前的那个Activity，这样就可以在onActivityResult方法中得到Intent对象，
                 * 参数1：resultCode返回码，跳转之前的activity根据是这个resultCode，区分是哪一个activity返回的
                 * 参数2：数据源
                 */
                setResult(GlobalVars.GET_RESULT_FROM_BAIDUMAP, intent);
                finish();//结束当前activity
            }
        });
    }

    public void initMap(){

        Client = new LocationClient(getApplicationContext());//获取全局Context
        Client.registerLocationListener(new MyLocationListener());//注册一个定位监听器，获取位置信息，回调此定位监听器



        BaiduMap = (MapView) findViewById(R.id.baiduMap);
        Position = (TextView) findViewById(R.id.position);

        baiduMap = BaiduMap.getMap();//获取实例，可以对地图进行一系列操作，比如：缩放范围，移动地图
        baiduMap.setMyLocationEnabled(true);//允许当前设备显示在地图上


        // 解决圆角屏幕手机，地图loggo被遮挡的问题
        baiduMap.setViewPadding(30, 0, 30, 20);



        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (mMarkerTrans == null) {
                    return;
                }
                mMarkerTrans.cancelAnimation();

                startTansAddrTransformTo(latLng);

                chatItem.setTranAddr(latLng.latitude+"/"+latLng.longitude);
                LocaService.updateTmpTransAddrByChatID(chatItem);
                prePosition = nowPosition;
                nowPosition = latLng.latitude+"/"+latLng.longitude;

            }

            @Override
            public void onMapPoiClick(MapPoi mapPoi) {
                if (mMarkerTrans == null || mapPoi == null) {
                    return;
                }
                mMarkerTrans.cancelAnimation();
                startTansAddrTransformTo(mapPoi.getPosition());

                chatItem.setTranAddr(mapPoi.getPosition().latitude+"/"+mapPoi.getPosition().longitude);
                LocaService.updateTmpTransAddrByChatID(chatItem);
                prePosition = nowPosition;
                nowPosition = mapPoi.getPosition().latitude+"/"+mapPoi.getPosition().longitude;
            }
        });
    }


    /**
     * methodName(方法名）
     * description: 获取对方坐标，更新自己坐标，
     */
    public void initUserLocation(BDLocation bdLocation){
        //首先 本地地址需要放入数据库内，方便卖家查询
        chatItem.getUser().setAddr(bdLocation.getLatitude()+"/"+bdLocation.getLongitude());
        if(chatItem.getUser().isBuyer()){
            chatItem.getBuyer().setAddr(bdLocation.getLatitude()+"/"+bdLocation.getLongitude());
        }else{
            chatItem.getSeller().setAddr(bdLocation.getLatitude()+"/"+bdLocation.getLongitude());
        }


        //读取卖家地址消息
        //如果对方没有在线，就不显示
        new Thread(new Runnable() {
            @Override
            public void run() {
                LocaServiceThread thread = LocaService.initAddrs(chatItem);;

                while(!thread.isFinished()){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                //因为如果数据库中有，就用数据库的内容更新当前的地址
                //如果没有，就用本人的地址算作交易地址
                createCenterMarker(getLatLngByStr(thread.getTransAddr()),GlobalVars.UPDATE_TRANS_ADDR_HANDLER);
                chatItem.setTranAddr(thread.getTransAddr());

//                //本人是买家
                if(chatItem.getUser().isBuyer()){
                    if(!"".equals(thread.getSellerAddr()) && null!=thread.getSellerAddr()){
                        //发送给handler 添加卖家的地点标记
                        chatItem.getSeller().setAddr(thread.getSellerAddr());
                        Message msg = new Message();
                        msg.what = GlobalVars.INIT_SELLER_ADDR_HANDLER;
                        handler.sendMessage(msg);
                    }
                }
                else{ //本人是卖家
                    if(!"".equals(thread.getSellerAddr()) && null!=thread.getSellerAddr()){
                        //发送给handler 添加卖家的地点标记
                        chatItem.getBuyer().setAddr(thread.getSellerAddr());
                        Message msg = new Message();
                        msg.what = GlobalVars.INIT_BUYER_ADDR_HANDLER;
                        handler.sendMessage(msg);
                    }
                }
            }
        }).start();

    }

    public Handler handler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            switch (msg.what) {
                case GlobalVars.INIT_SELLER_ADDR_HANDLER:
                    createCenterMarker(getLatLngByStr(chatItem.getSeller().getAddr()),GlobalVars.INIT_SELLER_ADDR_HANDLER);
                    break;
                case GlobalVars.INIT_BUYER_ADDR_HANDLER:
                    createCenterMarker(getLatLngByStr(chatItem.getBuyer().getAddr()),GlobalVars.INIT_BUYER_ADDR_HANDLER);
                    break;
                case GlobalVars.UPDATE_SELLER_ADDR_HANDLER:
                    //移动到目标地点
                    //更新transAddr
                    //更新卖家（对方）

                    if(null!=newTransAddr && !"".equals(newTransAddr) && !newTransAddr.equals(prePosition)) {
                        if (newTransAddr.equals(nowPosition)) {
                            prePosition = nowPosition;
                        } else {
                            startTansAddrTransformTo(getLatLngByStr(newTransAddr));
                            prePosition = nowPosition;
                            nowPosition = newTransAddr;
                        }
                    }

                    if(null!=chatItem.getSeller().getAddr() && !"".equals(chatItem.getSeller().getAddr())){
//                        Log.d("TAG", "flashUI: 发送"+chatItem.getSeller().getAddr());
                        if(mMarkerSeller == null){
                            createCenterMarker(getLatLngByStr(chatItem.getSeller().getAddr()),GlobalVars.INIT_SELLER_ADDR_HANDLER);
                        }else{
                            startFriendTransformTo(getLatLngByStr(chatItem.getSeller().getAddr()));
                        }

                    }
                    break;
                case GlobalVars.UPDATE_BUYER_ADDR_HANDLER:
                    //更新transAddr
                    if(null!=newTransAddr && !"".equals(newTransAddr) && !newTransAddr.equals(prePosition)){
                        if(newTransAddr.equals(nowPosition)){
                            prePosition = nowPosition;
                        }else{
                            startTansAddrTransformTo(getLatLngByStr(newTransAddr));
                            prePosition = nowPosition;
                            nowPosition = newTransAddr;
                        }
                    }

                    //更新卖家（对方）
                    if(null!=chatItem.getSeller().getAddr() && !"".equals(chatItem.getSeller().getAddr())){
                        if(mMarkerSeller == null){
                            createCenterMarker(getLatLngByStr(chatItem.getBuyer().getAddr()),GlobalVars.INIT_BUYER_ADDR_HANDLER);
                        }else{
                            startFriendTransformTo(getLatLngByStr(chatItem.getBuyer().getAddr()));
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };
    /**
     * 开启平移动画
     */
    public void startFriendTransformTo(LatLng mLatLng) {
        if (mMarkerSeller == null || mLatLng == null) {
            return;
        }

        mMarkerSeller.cancelAnimation();
        Transformation transformation = new Transformation(mMarkerSeller.getPosition(), mLatLng);
        transformation.setDuration(2000);
        transformation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationEnd() {
            }

            @Override
            public void onAnimationCancel() {
            }

            @Override
            public void onAnimationRepeat() {

            }
        });
        mMarkerSeller.setAnimation(transformation);
        mMarkerSeller.startAnimation();

    }

    public LatLng getLatLngByStr(String addr){
//        Log.d("TAG", "handleMessage:更新卖家地址 "+addr);
        if("0".equals(addr) || addr==null) return null;

        double lat = Double.valueOf(addr.split("/")[0]);
        double lng = Double.valueOf(addr.split("/")[1]);

        return new LatLng(lat,lng);
    }

    /*
     * 初始化baiduMap 和 动画按钮
     * */
    private void initView() {

    }

    /**
     * 开启平移动画
     */
    public void startTansAddrTransformTo(LatLng mLatLng) {
        if (mMarkerTrans == null || mLatLng == null) {
            return;
        }

        Transformation transformation = new Transformation(mMarkerTrans.getPosition(), mLatLng);
        transformation.setDuration(1);
        transformation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationEnd() {
            }

            @Override
            public void onAnimationCancel() {
            }

            @Override
            public void onAnimationRepeat() {

            }
        });
        mMarkerTrans.setAnimation(transformation);
        mMarkerTrans.startAnimation();
    }

    public void getPermission(){
        List<String> PermissionList = new ArrayList<>();
        //判断权限是否授权
        if (ContextCompat.checkSelfPermission(BaiduMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED){
            PermissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(BaiduMapActivity.this,Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            PermissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(BaiduMapActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            PermissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!PermissionList.isEmpty()){
            String[] Permissions = PermissionList.toArray(new String[PermissionList.size()]);//转化为数组
            ActivityCompat.requestPermissions(BaiduMapActivity.this,Permissions,1);//一次性申请权限
        }else {
            requestLocation();
        }
    }

    /**
     * 创建地图中心点marker
     */
    private void createCenterMarker(LatLng latLng,int mode) {
        if(latLng == null) return ;
        LatLng center = new LatLng(latLng.latitude, latLng.longitude);
        switch (mode){
            case GlobalVars.INIT_SELLER_ADDR_HANDLER:
                mBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ruins);
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(center)
                        .icon(mBitmapDescriptor);
                mMarkerSeller = (Marker) baiduMap.addOverlay(markerOptions);
                break;
            case GlobalVars.INIT_BUYER_ADDR_HANDLER:
                mBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ruins);
                MarkerOptions markerOptions3 = new MarkerOptions()
                        .position(center)
                        .icon(mBitmapDescriptor);
                mMarkerSeller = (Marker) baiduMap.addOverlay(markerOptions3);
                break;
            case GlobalVars.UPDATE_TRANS_ADDR_HANDLER:
                mBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.icon_mark);
                MarkerOptions markerOptions2 = new MarkerOptions()
                        .position(center)
                        .icon(mBitmapDescriptor);
                mMarkerTrans = (Marker) baiduMap.addOverlay(markerOptions2);
            default:
                break;
        }

    }

    private void navigateTo(BDLocation location){
        if (isFirstLocate){
            LatLng center = new LatLng(location.getLatitude(),location.getLongitude());//指定经纬度
//            LatLng center = new LatLng(39.963175, 116.400244);
            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(center, 12);
            baiduMap.setMapStatus(mapStatusUpdate);

//            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(lng);
//            baiduMap.animateMapStatus(update);
//            update = MapStatusUpdateFactory.zoomTo(16f);//百度地图缩放级别限定在3-19
//            baiduMap.animateMapStatus(update);
            isFirstLocate = false;
        }

    }
    private void requestLocation(){
        InitLocation();
        Client.start();
    }
    /*
     * LBS SKD定位模式
     * 1：Hight_Accuracy:高精确模式，优先使用GPS定位，其次使用网络定位
     * 2:Battery_Saving：节电模式，使用网络定位
     * 3:Device_Sensors：传感器模式，使用GPS
     * */
    private void InitLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//高精确模式
        option.setScanSpan(5000);//ms
        option.setIsNeedAddress(true);//获取详细信息许可
        Client.setLocOption(option);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMarkerTrans.cancelAnimation();
        Client.stop();
        BaiduMap.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BaiduMap.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        BaiduMap.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0){
                    for (int result : grantResults){
                        if (result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"必须同意所有权限才能使用本程序",Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                }else {
                    Toast.makeText(this,"发生未知错误",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (isFirstLocate && (bdLocation.getLocType() == BDLocation.TypeGpsLocation || bdLocation.getLocType() == BDLocation.TypeNetWorkLocation)){
                navigateTo(bdLocation);

                initUserLocation(bdLocation);
            }
            MyLocationData locData = new MyLocationData.Builder().accuracy(0)
                    .latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude()).build();
            LatLng point = new LatLng(bdLocation.getLatitude(),
                    bdLocation.getLongitude());
            baiduMap.setMyLocationData(locData);

        }


    }

}