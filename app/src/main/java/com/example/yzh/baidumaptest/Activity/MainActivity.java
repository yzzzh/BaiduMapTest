package com.example.yzh.baidumaptest.Activity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.yzh.baidumaptest.R;
import com.example.yzh.baidumaptest.database.RadarDB;
import com.example.yzh.baidumaptest.model.Person;

import java.util.List;

import cn.bmob.v3.Bmob;

public class MainActivity extends Activity implements View.OnClickListener{

    //地图布局
    private MapView mapView;
    //地图控制器
    private BaiduMap baiduMap;
    //定位类
    private LocationClient locationClient = null;
    //监听器，用于监听位置是否有刷新
    private BDLocationListener listener = new MyLocationListener();
    //是否第一次定位
    private boolean isFirstLoc = true;
    //自己的坐标点
    private LatLng myPoint;
    //朋友/敌人的坐标点
    private LatLng point;
    //覆盖物图片
    private BitmapDescriptor bitmap;
    //组件
    private Button btnLocate;
    private Button btnSendMessage;
    private Button btnFriend;
    private Button btnEnermy;
    //数据库
    private RadarDB db;
    //朋友列表
    private List<Person> friendList;
    //敌人列表
    private List<Person> enermyList;

    private final String TAG = "RECEIVER";

    //中心点
    MapStatusUpdate mapStatusUpdate;

    //监听广播
    private SmsStatusReceiver statusReceiver;
    private SmsDeliveryStatusReceiver deliveryStatusReceiver;
    private SmsReceiver smsReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //使用百度地图sdk任何组件之前都要调用这个方法
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        //初始化数据库
        db = RadarDB.getInstance(this);

        //初始化组件
        initViews();
        //加载朋友/敌人
        loadFriends();
        loadEnermies();

        //获取控制器
        baiduMap = mapView.getMap();
        //显示模式，普通显示
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //开启定位功能
        baiduMap.setMyLocationEnabled(true);


        //定位器
        locationClient = new LocationClient(getApplicationContext());
        //定位器的相关设置
        initLocation();
        //安装监听器
        locationClient.registerLocationListener(listener);
        //开始定位
        locationClient.start();

        //获取覆盖物图片
        bitmap = BitmapDescriptorFactory.fromResource(R.drawable.logo);

        //注册监听器
        registerRec();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSendMessage:
                sendMessage();
                break;
            case R.id.btnLocate:
                setCenter();
                break;
            case R.id.btnFriend:
                Intent intent_friend = new Intent(MainActivity.this,FriendListActivity.class);
                startActivity(intent_friend);
                finish();
                break;
            case R.id.btnEnermy:
                Intent intent_enermy = new Intent(MainActivity.this,EnermyListActivity.class);
                startActivity(intent_enermy);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }



    //同时释放布局，控制器，定位器，监听器的资源
    @Override
    protected void onDestroy() {
        //释放布局
        mapView.onDestroy();
        mapView = null;

        //释放控制器
        baiduMap.setMyLocationEnabled(false);

        //释放监听器
        if (locationClient != null)
            locationClient.unRegisterLocationListener(listener);

        //释放定位器
        locationClient.stop();

        //释放广播
        unregisterRec();

        super.onDestroy();
    }

    //初始化组件
    private void initViews(){
        mapView = (MapView) findViewById(R.id.mapView);
        btnLocate = (Button) findViewById(R.id.btnLocate);
        btnSendMessage = (Button) findViewById(R.id.btnSendMessage);
        btnFriend = (Button) findViewById(R.id.btnFriend);
        btnEnermy = (Button) findViewById(R.id.btnEnermy);
        btnFriend.setOnClickListener(this);
        btnEnermy.setOnClickListener(this);
        btnLocate.setOnClickListener(this);
        btnSendMessage.setOnClickListener(this);
    }

    //定位的相关设置
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度（GPS+NETWORK)，设置定位模式：GPS+NETWORK,GPS,NETWORK
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系,这个是百度的坐标系
        option.setScanSpan(1000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的,即1秒定位一次
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果，即1秒刷新一次画面
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        locationClient.setLocOption(option);
    }

    //监听器，接收位置
    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //监听实时位置

            // mapView 销毁后不再处理新接收的位置
            if (location == null || mapView == null)
                return;

            //获取位置，并改变地图的显示
            //获取精度半径，方向（0-360），，纬度，经度
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();

            //更新地图中心到定位的位置
            baiduMap.setMyLocationData(locData);    //设置定位数据

            //随时更新自己的位置
            myPoint = new LatLng(location.getLatitude(), location.getLongitude());

            drawPositon();

            //第一次定位,暂时不知道有什么用
            if (isFirstLoc) {
                isFirstLoc = false;

                setCenter();
            }
        }
    }

    private void loadFriends(){
        friendList = db.loadPeople("friend");
    }

    private void loadEnermies(){
        enermyList = db.loadPeople("enermy");
    }

    private void sendMessage(){
        SmsManager smsManager = SmsManager.getDefault();

        //发送广播，监听是否发送和接收成功
        PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SEND"), 0);
        PendingIntent deliveryIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED"), 0);

        for (int i = 0; i < friendList.size(); i++) {
            smsManager.sendTextMessage(friendList.get(i).getNumber(), null, "请发送你的经纬度给我", sentIntent, deliveryIntent);
        }

        for (int i = 0; i < enermyList.size(); i++) {
            smsManager.sendTextMessage(enermyList.get(i).getNumber(), null, "请发送你的经纬度给我", sentIntent, deliveryIntent);
        }

    }

    private void registerRec(){
        statusReceiver = new SmsStatusReceiver();
        deliveryStatusReceiver = new SmsDeliveryStatusReceiver();
        smsReceiver = new SmsReceiver();

        //接收用IntentFilter
        //发送用PendingIntent和Intent
        registerReceiver(statusReceiver,new IntentFilter("SMS_SEND"));
        registerReceiver(deliveryStatusReceiver,new IntentFilter("SMS_DELIVERED"));
        registerReceiver(smsReceiver,new IntentFilter("sms_received"));
    }

    private void unregisterRec(){
        unregisterReceiver(statusReceiver);
        unregisterReceiver(deliveryStatusReceiver);
        unregisterReceiver(smsReceiver);
    }

    //设置自己的坐标点到屏幕中心
    private void setCenter(){
        mapStatusUpdate= MapStatusUpdateFactory.newLatLngZoom(myPoint, 16);   //设置地图中心点以及缩放级别
        baiduMap.animateMapStatus(mapStatusUpdate);
    }

    private void drawPositon(){
        double latitude;
        double logitude;

        for (int i = 0;i < friendList.size();i++){
            if (friendList.get(i).getLatitude() != null && friendList.get(i).getLongitude() != null) {
                latitude = Double.parseDouble(friendList.get(i).getLatitude());
                logitude = Double.parseDouble(friendList.get(i).getLongitude());

                point = new LatLng(latitude, logitude);
                OverlayOptions options = new MarkerOptions().position(point).icon(bitmap).draggable(true);
                baiduMap.addOverlay(options);
            }
        }
        for (int i = 0;i < enermyList.size();i++){
            if (friendList.get(i).getLatitude() != null && friendList.get(i).getLongitude() != null) {
                latitude = Double.parseDouble(enermyList.get(i).getLatitude());
                logitude = Double.parseDouble(enermyList.get(i).getLongitude());

                point = new LatLng(latitude, logitude);
                OverlayOptions options = new MarkerOptions().position(point).icon(bitmap).draggable(true);
                baiduMap.addOverlay(options);
            }
        }
    }

    //监听是否发送成功
    public class SmsStatusReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG,"SmsStatusReceiver onReceive.");
            switch(getResultCode()) {
                case Activity.RESULT_OK:
                    Log.d(TAG, "Activity.RESULT_OK");
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    Log.d(TAG, "RESULT_ERROR_GENERIC_FAILURE");
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    Log.d(TAG, "RESULT_ERROR_NO_SERVICE");
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    Log.d(TAG, "RESULT_ERROR_NULL_PDU");
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    Log.d(TAG, "RESULT_ERROR_RADIO_OFF");
                    break;
            }
        }
    }

    //监听对方是否接收成功
    public class SmsDeliveryStatusReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "SmsDeliveryStatusReceiver onReceive.");
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Log.i(TAG, "RESULT_OK");
                    break;
                case Activity.RESULT_CANCELED:
                    Log.i(TAG, "RESULT_CANCELED");
                    break;
            }
        }
    }

    //获取解析好的短信
    public class SmsReceiver extends BroadcastReceiver {
        public static final String SMS_RECEIVED_ACTION = "sms_received";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG,"action: "+action);
            if (SMS_RECEIVED_ACTION.equals(action)) {
                Bundle bundle = intent.getExtras();

                String message = bundle.getString("message");
                String sender = bundle.getString("sender");

                String[] Latlng = message.split("/");
                String lat = Latlng[0];
                String lng = Latlng[1];

                Person person = new Person();
                person.setNumber(sender);
                person.setLatitude(lat);
                person.setLongitude(lng);

                db.updatePerson(person);
            }
        }
    }
}
