package com.heking.hzz.Base;

/**
 * Created by Lee on 2017/10/11.
 */

public class ToolbarInfo {
    public String title;
    public boolean havBack;

    public ToolbarInfo(String title, boolean havBack) {
        this.title = title;
        this.havBack = havBack;
    }

    public ToolbarInfo(String title) {
        this.title = title;
    }
}
