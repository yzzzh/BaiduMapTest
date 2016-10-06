package com.example.yzh.baidumaptest;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.baidu.lbsapi.BMapManager;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

public class MainActivity extends Activity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //使用百度地图sdk任何组件之前都要调用这个方法
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        //地图布局的基本显示
        mapView = (MapView) findViewById(R.id.mapView);
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

        super.onDestroy();
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


            //第一次定位,暂时不知道有什么用
            if (isFirstLoc) {
                isFirstLoc = false;

                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 16);   //设置地图中心点以及缩放级别
                baiduMap.animateMapStatus(u);
            }
        }
    }
}
