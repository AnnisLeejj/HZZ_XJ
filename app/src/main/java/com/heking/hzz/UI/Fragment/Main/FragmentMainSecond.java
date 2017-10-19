package com.heking.hzz.UI.Fragment.Main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.MapFragment;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.google.gson.Gson;
import com.heking.hzz.Base.Constant;
import com.heking.hzz.Base.MessageEvent;
import com.heking.hzz.Base.OnFragmentInteractionListener;
import com.heking.hzz.Helper.BaiduMap.BDMapController;
import com.heking.hzz.Helper.LogUtils.LogUtils;
import com.heking.hzz.Helper.SensorHelper.MyOrientationListener;
import com.heking.hzz.R;
import com.heking.hzz.Service.GetMyLocaltion_IntentService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import AppConfig.WPConfig;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentMainSecond#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMainSecond extends Fragment implements BDMapController {
    @BindView(R.id.bmapView)
    MapView bmapView;
    @BindView(R.id.move_to_mylocaltion)
    ImageButton move_to_mylocaltion;
    @BindView(R.id.test_1)
    ImageButton test_1;
    BaiduMap baiduMap;//地图控制器
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private OnFragmentInteractionListener mListener;

    public FragmentMainSecond() {

    }

    public static FragmentMainSecond newInstance(String param1, String param2) {
        FragmentMainSecond fragment = new FragmentMainSecond();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_second, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setMyMapView();
        startService();
    }

    private void setMyMapView() {
        baiduMap = bmapView.getMap();
        bmapView.showScaleControl(false);
        move_to_mylocaltion.setOnClickListener(view -> moveToMyLocaltion());
        if (WPConfig.isDebug) {
            test_1.setOnClickListener(view -> {
                addMaker(new LatLng(lastLocation.getLatitude() + 0.1, lastLocation.getLongitude() + 0.1), MarkerLevel.Red);
            });
        } else {
            test_1.setVisibility(View.GONE);
        }
        LogUtils.w("map", new Gson().toJson(baiduMap.getLocationData()));
    }

    private void startService() {
        realLocaltion(1000);//开始定位
        orientationListener = new MyOrientationListener(getActivity());
        orientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                Direction = x;
            }
        });
        orientationListener.start();
    }

    MyOrientationListener orientationListener;
    LocationClient locationClient;
    float Direction;

    /**
     * @param interval 定位的间隔时间(毫秒) , 为0 时,只定位一次
     */
    private void realLocaltion(int interval) {
        locationClient = new LocationClient(getActivity());// 实例化location类
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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

    private void closeLocalation() {
        // 当不需要定位图层时关闭定位图层
        // baiduMap.setMyLocationEnabled(false);
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
}
