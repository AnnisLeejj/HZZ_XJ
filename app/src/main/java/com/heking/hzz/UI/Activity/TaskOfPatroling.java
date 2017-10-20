package com.heking.hzz.UI.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.heking.hzz.Base.BaseActivity;
import com.heking.hzz.Base.ToolbarInfo;
import com.heking.hzz.R;

public class TaskOfPatroling extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_of_patroling);

    }

    @Override
    public ToolbarInfo getMyTitle() {
        return new ToolbarInfo("巡检任务");
    }

    @Override
    public void initView() {

    }
}
