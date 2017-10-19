package com.heking.hzz.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Vibrator;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.heking.hzz.Helper.BaiduMap.MyLocationListener;
import com.heking.hzz.Helper.LogUtils.LogUtils;

public class GetMyLocaltion_Service extends Service {
    public GetMyLocaltion_Service() {
    }

    public static final String TAG = "MyService";

    private MyBinder mBinder = new MyBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.d(TAG, "onCreate() executed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.d(TAG, "onStartCommand() executed");

        startGetLocaltion();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //结束获取定位
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
        LogUtils.d(TAG, "onDestroy() executed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    class MyBinder extends Binder {

        public void startDownload() {
            LogUtils.d("TAG", "startDownload() executed");
            // 执行具体的下载任务
        }

    }

    LocationClient mLocationClient = null;

    private void startGetLocaltion() {
        //int interval = getI.getIntExtra("interval", 0);
        //BDAbstractLocationListener为7.2版本新增的Abstract类型的监听接口，原有BDLocationListener接口暂时同步保留。具体介绍请参考后文中的说明
        BDAbstractLocationListener myListener = new MyLocationListener(new MyLocationListener.CallBack() {
            @Override
            public void getLocaltion(BDLocation location) {
                LogUtils.w(new Gson().toJson(location));
            }
        });
        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
        initLocation(30 * 1000);
        mLocationClient.start();
    }

    private void initLocation(int interval) {
        LocationClientOption option = new LocationClientOption();
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认gcj02，设置返回的定位结果坐标系
        option.setCoorType("bd09ll");

        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setScanSpan(interval);
        //可选，设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(true);
        //可选，默认false,设置是否使用gps
        option.setOpenGps(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setLocationNotify(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIsNeedLocationPoiList(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIgnoreKillProcess(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        //option.setIgnoreCacheException(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        option.setEnableSimulateGps(false);
        //可选，7.2版本新增能力，如果您设置了这个接口，首次启动定位时，会先判断当前WiFi是否超出有效期，超出有效期的话，会先重新扫描WiFi，然后再定位
        //option.setWifiValidTime(5*60*1000);
        mLocationClient.setLocOption(option);
    }

    NotifyLister mNotifyer;

    /**
     * 设置 目的地到达提示
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @param range     以经纬度为点,距离range以内,就会提示
     */
    private void set(double latitude, double longitude, float range) {
        //位置提醒相关代码
        mNotifyer = new NotifyLister();
        mNotifyer.SetNotifyLocation(latitude, longitude, range, "gps");
        //4个参数代表要位置提醒的点的坐标，具体含义依次为：纬度，经度，距离范围，坐标系类型(gcj02,gps,bd09,bd09ll)
        mLocationClient.registerNotify(mNotifyer);
        //注册位置提醒监听事件后，可以通过SetNotifyLocation 来修改位置提醒设置，修改后立刻生效。
        //BDNotifyListner实现
    }

    class NotifyLister extends BDNotifyListener {
        public void onNotify(BDLocation mlocation, float distance) {
            Vibrator mVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
            mVibrator.vibrate(1000);
            //振动提醒已到设定位置附近
            //取消位置提醒
            mLocationClient.removeNotifyEvent(mNotifyer);
        }
    }
}
