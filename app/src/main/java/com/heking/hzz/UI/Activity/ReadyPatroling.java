package com.heking.hzz.UI.Activity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.heking.hzz.Base.BaseActivity;
import com.heking.hzz.Base.ToolbarInfo;
import com.heking.hzz.Helper.BaiduMap.BDMapController;
import com.heking.hzz.Helper.LogUtils.LogUtils;
import com.heking.hzz.Helper.SensorHelper.MyOrientationListener;
import com.heking.hzz.R;

import java.util.ArrayList;
import java.util.List;

import AppConfig.WPConfig;
import butterknife.BindView;

public class ReadyPatroling extends BaseActivity implements BDMapController, View.OnClickListener {
    @BindView(R.id.bmapView)
    MapView bmapView;
    @BindView(R.id.move_to_mylocaltion)
    ImageButton move_to_mylocaltion;
    @BindView(R.id.test_1)
    ImageButton test_1;
    @BindView(R.id.bt_filtrate)//筛选
            Button bt_filtrate;
    @BindView(R.id.cotnent_search)//搜索
            AutoCompleteTextView cotnent_search;
    BaiduMap baiduMap;//地图控制器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready_patroling);
    }

    @Override
    public void initView() {
        setAutoCompleteText();
        setMyMapView();
        startService();
    }

    ArrayAdapter autoCompleteTextAdapter;

    //搜索的自动提示
    private void setAutoCompleteText() {
        String[] arr = {"好人", "坏人", "坏蛋","大坏蛋","aa","ab","aab","bba"};
        autoCompleteTextAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arr);
        cotnent_search.setAdapter(autoCompleteTextAdapter);
    }

    @Override
    public ToolbarInfo getMyTitle() {
        return new ToolbarInfo("开始巡检", "", "任务", true, new ToolbarInfo.BaseActivityCallBack() {
            @Override
            public void left(String title_left) {

            }

            @Override
            public void right(String title_rightt) {
                LogUtils.w("baseactivity", " 右 点击 收到");
                startActivity(TaskOfPatroling.class);
            }
        });
    }

    private void setMyMapView() {
        baiduMap = bmapView.getMap();
        bmapView.showScaleControl(false);
        if (WPConfig.isDebug) {
            test_1.setOnClickListener(view -> {
                addMaker(new LatLng(lastLocation.getLatitude() + 0.1, lastLocation.getLongitude() + 0.1), BDMapController.MarkerLevel.Red);
            });
        } else {
            test_1.setVisibility(View.GONE);
        }
        move_to_mylocaltion.setOnClickListener(this);
        bt_filtrate.setOnClickListener(this);
        LogUtils.w("map", new Gson().toJson(baiduMap.getLocationData()));
    }

    private void startService() {
        realLocaltion(1000);//开始定位
        //方向
        orientationListener = new MyOrientationListener(this);
        orientationListener.setOnOrientationListener(x -> Direction = x);
        orientationListener.start();
    }

    MyOrientationListener orientationListener;
    LocationClient locationClient;
    float Direction;

    /**
     * @param interval 定位的间隔时间(毫秒) , 为0 时,只定位一次
     */
    private void realLocaltion(int interval) {
        locationClient = new LocationClient(this);// 实例化location类
        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                // mMapView 销毁后不再处理新接收的位置
                if (location == null || bmapView == null)
                    return;
                setLocaltion(location);
            }
        });// 注册监听函数
        LocationClientOption clientOption = new LocationClientOption();
        // 打开GPS
        clientOption.setOpenGps(true);
        // 设置定位模式
        clientOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        // 返回的定位结果是百度经纬度，默认gcj02
        clientOption.setCoorType("bd09ll");
        // 设置发送定位请求的间隔时间
        clientOption.setScanSpan(interval);
        // 返回的定位结果包含地址信息
        clientOption.setIsNeedAddress(true);
        // 返回的定位结果包含手机机头的方向
        clientOption.setNeedDeviceDirect(true);
        locationClient.setLocOption(clientOption);
        locationClient.start();
    }

    /**
     * 开启定位图层
     *
     * @param location 定位信息
     */
    BDLocation lastLocation;

    private void setLocaltion(BDLocation location) {
        lastLocation = location;
        // 开启定位图层
        baiduMap.setMyLocationEnabled(true);
        // 构造定位数据
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(Direction)
                .latitude(location.getLatitude()).longitude(location.getLongitude())
                .build();
//        // 设置定位数据
        baiduMap.setMyLocationData(locData);
        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null);
        //R.drawable.icon_geo //arrow  //BitmapDescriptorFactory  .fromResource(R.drawable.arrow)
        baiduMap.setMyLocationConfiguration(config);
        if (moveToMyLocaltion) {
            // 开始移动百度地图的定位地点到中心位置
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 16.0f);
            baiduMap.animateMapStatus(u);
            moveToMyLocaltion = false;
        }
    }

    boolean moveToMyLocaltion = true;

    @Override
    public void moveToMyLocaltion() {
        // 开始移动百度地图的定位地点到中心位置
        LatLng ll = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 16.0f);
        baiduMap.animateMapStatus(u);
        moveToMyLocaltion = false;
    }

    @Override
    public Marker addMaker(LatLng latLng, MarkerLevel markerLevel) {
        //根据 markerLevel 准备 marker 的图片
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(markerLevel == MarkerLevel.Green ? R.drawable.marker_green : markerLevel == MarkerLevel.Grey ?
                R.drawable.marker_grey : markerLevel == MarkerLevel.Red ? R.drawable.marker_red : 0);//都不是的话让其报错
        //准备 marker option 添加 marker 使用
        MarkerOptions markerOptions = new MarkerOptions().icon(bitmap).position(latLng);
        //获取添加的 marker 这样便于后续的操作
        return (Marker) baiduMap.addOverlay(markerOptions);
    }

    private void closeLocalation() {
        // 当不需要定位图层时关闭定位图层
        // baiduMap.setMyLocationEnabled(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.w("map", "map onResume");
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        if (bmapView != null)
            bmapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.w("map", "map onPause");
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        if (bmapView != null)
            bmapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.w("map", "map onDestroy");
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        if (bmapView != null)
            bmapView.onDestroy();
        if (locationClient != null && locationClient.isStarted())
            locationClient.stop();
        orientationListener.stop();//方位信息
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_filtrate://筛选
                List<String> list = new ArrayList<>();
                list.add("111111111");
                list.add("2222222222");
                openPopupWindow(view, list);
                break;
            case R.id.move_to_mylocaltion://回到我的位置
                moveToMyLocaltion();
                break;
        }
    }

    PopupWindow mPopupWindow;

    private void openPopupWindow(View v, List<String> list) {
        //显示的视图
        ListView popupView = (ListView) getLayoutInflater().inflate(R.layout.popup_list_view, null);
        MyBaseAdapter adapter = new MyBaseAdapter(list);
        popupView.setAdapter(adapter);
        popupView.setOnItemClickListener((adapterView, view, i, l) -> {
            showToast("点击:" + i);
            mPopupWindow.dismiss();
        });
        //PopupWindow 本身
        mPopupWindow = new PopupWindow(popupView, 200, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        mPopupWindow.showAsDropDown(v);
    }

    public class MyBaseAdapter extends BaseAdapter {
        List<String> adapterList;

        MyBaseAdapter(List<String> adapterList) {
            this.adapterList = adapterList;
        }

        @Override
        public int getCount() {
            return adapterList.size();
        }

        @Override
        public Object getItem(int i) {
            return adapterList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.item_list_a_line_wrap, null);
            ((TextView) view).setText(adapterList.get(i));
            return view;
        }
    }
}
