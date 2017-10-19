package com.heking.hzz.UI.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.heking.hzz.Base.BaseFramentActivity;
import com.heking.hzz.Base.Constant;
import com.heking.hzz.Base.MessageEvent;
import com.heking.hzz.Base.OnFragmentInteractionListener;
import com.heking.hzz.Base.ToolbarInfo;
import com.heking.hzz.Helper.BottomNavigationHelper;
import com.heking.hzz.R;
import com.heking.hzz.Service.GetMyLocaltion_Service;
import com.heking.hzz.UI.Fragment.Main.FragmentMainHome;
import com.heking.hzz.UI.Fragment.Main.FragmentMainPersonal;
import com.heking.hzz.UI.Fragment.Main.FragmentMainSecond;
import com.heking.hzz.UI.Fragment.Main.FragmentMainThird;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseFramentActivity implements OnFragmentInteractionListener {
    @BindView(R.id.main_content_view)
    FrameLayout main_content;
    @BindView(R.id.navigation4)
    BottomNavigationView navigationView4;
    @BindView(R.id.navigation3)
    BottomNavigationView navigationView3;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener1
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_home://主页
                BottomWithFragmentsControler.setIndexSelected(0);
                showToast("navigation_home");
                return true;
            case R.id.navigation_xunjian://巡检
                BottomWithFragmentsControler.setIndexSelected(1);
                showToast("navigation_xunjian");
                return true;
            case R.id.navigation_xunjian_mine:
                BottomWithFragmentsControler.setIndexSelected(2);
                showToast("navigation_xunjian_mine");
                return true;
            case R.id.navigation_mine://个人中心
                BottomWithFragmentsControler.setIndexSelected(3);
                showToast("navigation_mine");
                return true;
        }
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void initView() {
        //底部导航栏加载
        BottomNavigationHelper.disableShiftMode(navigationView4);
        BottomNavigationHelper.disableShiftMode(navigationView3);
        navigationView4.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener1);
        navigationView3.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener1);
        //Frament 加载
        BottomWithFragmentsControler.init(getSupportFragmentManager(), navigationView3, navigationView4);
        BottomWithFragmentsControler.setIndexSelected(0);
//        navigationView3.setSelectedItemId(R.id.navigation_home);
    }

    static class BottomWithFragmentsControler {
        static Fragment mainHome, mainSecond, mainThird, mainPersonal;
        static List<Fragment> fragments;
        static BottomNavigationView currentNavigation, first, second;
        static FragmentManager fragmentManager;

        static void init(FragmentManager fragmentManager_, BottomNavigationView first_, BottomNavigationView second_) {
            first = first_;
            second = second_;
            fragmentManager = fragmentManager_;
            currentNavigation = first;

            fragments = new ArrayList<>();
            mainHome = FragmentMainHome.newInstance("FragmentMainHome", "FragmentMainHome");
            mainSecond = FragmentMainSecond.newInstance("FragmentMainSecond", "FragmentMainSecond");
            mainThird = FragmentMainThird.newInstance("FragmentMainThird", "FragmentMainThird");
            mainPersonal = FragmentMainPersonal.newInstance("FragmentMainPersonal", "FragmentMainPersonal");
            fragments.add(mainHome);
            fragments.add(mainSecond);
            fragments.add(mainThird);
            fragments.add(mainPersonal);
        }

        private static void setShowBottom(BottomNavigationView toShow) {
            currentNavigation = toShow;
            if (second == toShow) {
                second.setVisibility(View.VISIBLE);
                first.setVisibility(View.GONE);
            } else {
                second.setVisibility(View.GONE);
                first.setVisibility(View.VISIBLE);
            }
        }

        static int curentIndex = 10;
        static FragmentTransaction ft;

        private static void setIndexSelected(int index) {
            if (curentIndex != 10 && curentIndex == index) {
                return;
            }
            ft = fragmentManager.beginTransaction();
            for (int i = 0; i < fragments.size(); i++) {
                if (!fragments.get(i).isAdded()) {
                    ft.add(R.id.main_content_view, fragments.get(i));
                }
                ft.hide(fragments.get(i));
            }
            ft.show(fragments.get(index));
            ft.commit();
            // 再次赋值
            curentIndex = index;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }




    @Override
    public ToolbarInfo getMyTitle() {
        return new ToolbarInfo("主页");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        showToast(uri.getScheme());
        if ("changeui".equals(uri.getScheme())) {
            BottomWithFragmentsControler.setShowBottom(navigationView4);
        } else if ("startservice".equals(uri.getScheme())) {
            startService(new Intent(this, GetMyLocaltion_Service.class));
        } else if ("stopservice".equals(uri.getScheme())) {
            stopService(new Intent(this, GetMyLocaltion_Service.class));
        }
    }
}
