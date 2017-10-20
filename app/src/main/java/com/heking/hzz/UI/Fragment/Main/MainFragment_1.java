package com.heking.hzz.UI.Fragment.Main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.heking.hzz.Base.OnFragmentInteractionListener;
import com.heking.hzz.Helper.LogUtils.LogUtils;
import com.heking.hzz.Helper.ViewHelper.GlideImageLoader;
import com.heking.hzz.R;
import com.heking.hzz.UI.Activity.ReadyPatroling;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment_1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment_1 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.bt_readey_patroling)
    Button bt_readey_patroling;

    public MainFragment_1() {
        // Required empty public constructor
    }

    public static MainFragment_1 newInstance(String param1, String param2) {
        MainFragment_1 fragment = new MainFragment_1();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_fragment_1, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setMyBanner();
        List<GlideImageLoader.LunboBean> list = new ArrayList<>();

        list.add(new GlideImageLoader.LunboBean("", "title", "www.baidu.com"));
        startLunbo(list);
        bt_readey_patroling.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ReadyPatroling.class));
        });
    }

    List<GlideImageLoader.LunboBean> list_lunbo;//轮播图数据

    private void setMyBanner() {

        final float BannerHightRatio = 0.555f;
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        LogUtils.w("load", "height:" + height);

        banner.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) (width * BannerHightRatio)));
        LogUtils.w("lunbo", (int) (width * BannerHightRatio));
        // banner = (Banner) findViewById(R.id.banner);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置轮播时间
        banner.setDelayTime(4000);
        banner.setOnBannerListener(position -> {
            String path = list_lunbo.get(position).getUrl();
            LogUtils.w("webView", "main:" + list_lunbo.get(position));
            //  startActivity(new Intent(this, NewsDetailActivity.class).putExtra("url", path));
//            Intent intent = new Intent();
//            intent.setAction("android.intent.action.VIEW");
//      Uri content_url = Uri.parse(list_lunbo.get(position).getUrl());
//            intent.setData(content_url);
//            startActivity(intent);
        });
    }

    private void startLunbo(List<GlideImageLoader.LunboBean> list) {
        //设置图片集合
        List<String> list1 = new ArrayList<String>();
        List<String> titles = new ArrayList<String>();
        for (GlideImageLoader.LunboBean item : list) {
            if (!TextUtils.isEmpty(item.getImagePath())) {
                list1.add(item.getImagePath());
            } else {
                list1.add("defaultdefault");
            }
            titles.add(item.getTitle());
        }
        //设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(titles);
        banner.setImages(list1);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * onDestroyView中进行解绑操作
     */
    private Unbinder unbinder;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }
}
