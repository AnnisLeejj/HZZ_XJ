package com.heking.hzz.Base;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.baidu.mapapi.SDKInitializer;
import com.heking.hzz.Helper.AppUtils.AppUtils;
import com.mabeijianxi.smallvideorecord2.DeviceUtils;
import com.mabeijianxi.smallvideorecord2.JianXiCamera;

import java.io.File;

import butterknife.internal.Utils;

/**
 * Created by Lee on 2017/10/11.
 */

public class MyAPP extends Application {
    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        //百度地图  在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(this);
        initSmallVideo();
        AppUtils.init(this);
    }

    /**
     * 视频的初始化
     */
    public static void initSmallVideo() {
        // 设置拍摄视频缓存路径
        File dcim = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        if (DeviceUtils.isZte()) {
            if (dcim.exists()) {
                JianXiCamera.setVideoCachePath(dcim + "/mabeijianxi/");
            } else {
                JianXiCamera.setVideoCachePath(dcim.getPath().replace("/sdcard/",
                        "/sdcard-ext/") + "/mabeijianxi/");
            }
        } else {
            JianXiCamera.setVideoCachePath(dcim + "/mabeijianxi/");
        }
        // 初始化拍摄，遇到问题可选择开启此标记，以方便生成日志
        //JianXiCamera.initialize(false, null);
    }
}
