package com.heking.hzz.Base;

/**
 * Created by Lee on 2017/10/11.
 */

public class ToolbarInfo {
    public String title;
    public String title_left;
    public String title_right;
    public boolean havBack;
    BaseActivityCallBack callBack;

    public ToolbarInfo(String title) {
        this.title = title;
    }

    public ToolbarInfo(String title, boolean havBack) {
        this.title = title;
        this.havBack = havBack;
    }

    public ToolbarInfo(String title, String title_left, String title_right, BaseActivityCallBack callBack) {
        this.title = title;
        this.title_right = title_right;
        this.title_left = title_left;
        this.callBack = callBack;
    }

    public ToolbarInfo(String title, String title_left, String title_right, boolean havBack, BaseActivityCallBack callBack) {
        this.title = title;
        this.havBack = havBack;
        this.title_left = title_left;
        this.title_right = title_right;
        this.callBack = callBack;
    }

    public interface BaseActivityCallBack {
        void left(String title_left);

        void right(String title_rightt);
    }
}
