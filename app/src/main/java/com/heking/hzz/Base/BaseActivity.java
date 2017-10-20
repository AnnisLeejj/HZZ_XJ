package com.heking.hzz.Base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.heking.hzz.Helper.LogUtils.LogUtils;
import com.heking.hzz.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Lee on 2017/10/11.
 */


public abstract class BaseActivity extends AppCompatActivity {
    private View view;
    @BindView(R.id.title)
    TextView tv_title;
    @BindView(R.id.title_left)
    TextView tv_title_left;
    @BindView(R.id.title_right)
    TextView tv_title_right;
    @BindView(R.id.base_activity_toolbar)
    public RelativeLayout myToolbar;
    @BindView(R.id.fab)
    public FloatingActionButton fab;
    @BindView(R.id.navigation_back)
    public ImageButton navigation_back;

    /**
     * 标题栏内容,
     *
     * @return null 则整个标题栏不显示,不为null  则根据传入数据显示
     */
    public abstract ToolbarInfo getMyTitle();

    /**
     * 晚于 setContentView() 执行
     */
    public abstract void initView();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public void setContentView(int layoutResID) {
        view = getLayoutInflater().inflate(R.layout.activity_base_layout, null);
        ViewGroup viewGroup = view.findViewById(R.id.base_activity_content);
        getLayoutInflater().inflate(layoutResID, viewGroup, true);
        initBase(view);
    }

    @Override
    public void setContentView(View view1) {
        view = getLayoutInflater().inflate(R.layout.activity_base_layout, null);
        ViewGroup viewGroup = view.findViewById(R.id.base_activity_content);
        viewGroup.addView(view1);
        initBase(view);
    }

    /**
     * BaseActivity 的 界面加载完了,在这里加载继承者的界面
     *
     * @param view
     */
    private void initBase(View view) {
        super.setContentView(view);
        ButterKnife.bind(this); //注解
        // toolbar = view.findViewById(R.id.base_activity_toolbar);
        //setSupportActionBar(toolbar);
        setToolBar(getMyTitle());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initView();
    }

    public void setToolBar(ToolbarInfo toolbarInfo) {
        if (toolbarInfo == null) {
            myToolbar.setVisibility(View.GONE);
        } else {
            tv_title.setText(toolbarInfo.title);
            tv_title_left.setText(toolbarInfo.title_left);
            tv_title_right.setText(toolbarInfo.title_right);
            navigation_back.setVisibility(toolbarInfo.havBack ? View.VISIBLE : View.GONE);

            if (toolbarInfo.havBack) {
                navigation_back.setBackgroundResource(R.drawable.back);
                navigation_back.setOnClickListener(v -> finish());
            }
            if (toolbarInfo.callBack != null) {
                tv_title_left.setOnClickListener(view -> {
                    toolbarInfo.callBack.left(tv_title_left.getText().toString());
                });
                tv_title_right.setOnClickListener(view -> {
                    toolbarInfo.callBack.right(tv_title_right.getText().toString());
                });
            }
        }
    }

    // Activity 常用
    @SuppressLint("ResourceType")
    public void useFat(@NonNull final String message, @IntegerRes int imageResource, @Nullable final View.OnClickListener listener) {
        if (imageResource != 0) {
            fab.setImageResource(imageResource);
        }
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(listener);
//      Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction("Action", listener).show()
    }

    public void startActivity(@NonNull Class turnTo) {
        startActivity(new Intent(this, turnTo));
    }

    public void showToast(@NonNull String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showSnackbar(@NonNull String message, String ActionMessage, View.OnClickListener actionListener) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction("Action", actionListener).show();
    }
}
