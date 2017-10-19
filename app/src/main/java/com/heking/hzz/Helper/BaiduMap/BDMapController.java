package com.heking.hzz.Helper.BaiduMap;

import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;

/**
 * Created by Lee on 2017/10/17.
 */

public interface BDMapController {
    enum MarkerLevel {
        Grey, Green, Red;
    }

    /**
     * 移动视图到我的位置
     */
    void moveToMyLocaltion();

    /**
     * 在MapView上添加一个marker,
     *
     * @param latLng      marker的位置
     * @param markerLevel marker的颜色控制 有 红/绿/灰
     * @return
     */
    Marker addMaker(LatLng latLng, MarkerLevel markerLevel);
}
