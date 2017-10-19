package com.heking.hzz.Helper;

import java.text.DecimalFormat;

/**
 * Created by Lee on 2017/10/17.
 */

public class MyTextUtils {
    public  static String getFileSizeByByte(Float length) {
        String danwei = length > 1024 * 1024 ? "M" : length > 1024 ? "KB" : "Byte";
        float myLength = length > 1024 * 1024 ? length / (1024 * 1024) : length > 1024 ? length / 1024 : 0;
        // System.out.println((length % (1024 * 1024))/1024/1024);

        DecimalFormat fnum = new DecimalFormat("##0.00");
        String dd = fnum.format(myLength);
        return dd + danwei;
    }
}
