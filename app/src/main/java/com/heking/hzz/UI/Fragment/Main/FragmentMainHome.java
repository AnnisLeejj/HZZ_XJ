package com.heking.hzz.UI.Fragment.Main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.heking.hzz.Base.OnFragmentInteractionListener;
import com.heking.hzz.Helper.HttpHelper.HttpHelper;
import com.heking.hzz.Helper.LogUtils.LogUtils;
import com.heking.hzz.Helper.ShipinHelper.SuccessActivity;
import com.heking.hzz.Helper.ShipinHelper.VideoRecord;
import com.heking.hzz.Model.Test2;
import com.heking.hzz.Model.Zhengfudaohang;
import com.heking.hzz.R;
import com.heking.hzz.Service.GetMyLocaltion_IntentService;
import com.heking.hzz.Service.GetMyLocaltion_Service;
import com.mabeijianxi.smallvideorecord2.LocalMediaCompress;
import com.mabeijianxi.smallvideorecord2.model.AutoVBRMode;
import com.mabeijianxi.smallvideorecord2.model.LocalMediaConfig;
import com.mabeijianxi.smallvideorecord2.model.OnlyCompressOverBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentMainHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMainHome extends Fragment implements View.OnClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.text_view)
    TextView textView;
    @BindView(R.id.htpp_requet)
    Button htpp_requet;
    @BindView(R.id.htpp_requet2)
    Button htpp_requet2;
    @BindView(R.id.htpp_requet3)
    Button htpp_requet3;
    @BindView(R.id.htpp_requet4)
    Button htpp_requet4;
    @BindView(R.id.htpp_requet5)
    Button htpp_requet5;
    @BindView(R.id.htpp_requet6)
    Button htpp_requet6;
    @BindView(R.id.htpp_requet7)
    Button htpp_requet7;
    @BindView(R.id.htpp_requet8)
    Button htpp_requet8;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentMainHome() {
        // Required empty public constructor
    }

    public static FragmentMainHome newInstance(String param1, String param2) {
        FragmentMainHome fragment = new FragmentMainHome();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        LogUtils.w("onCreate");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtils.w("onCreateView");
        View view = inflater.inflate(R.layout.fragment_main_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        textView.setOnClickListener(this);
        htpp_requet.setOnClickListener(this);
        htpp_requet2.setOnClickListener(this);
        htpp_requet3.setOnClickListener(this);
        htpp_requet4.setOnClickListener(this);
        htpp_requet5.setOnClickListener(this);
        htpp_requet6.setOnClickListener(this);
        htpp_requet7.setOnClickListener(this);
        htpp_requet8.setOnClickListener(this);
        return view;
    }

    Intent intent_getLocaltion;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        intent_getLocaltion = new Intent(getActivity(), GetMyLocaltion_Service.class);
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
        LogUtils.w("onAttach");
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

    String url = "http://192.168.0.224:89/YZTQW/SJSPZHAPI/api/pzh/getNavigationGovernment";

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.htpp_requet:
                HttpHelper.getInstance().service.test("http://117.173.38.55:84/YZTQW/SJSPZHAPI/api/Firm/GetMo?page=0&rows=999999")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<Test2>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                LogUtils.w("http", "onSubscribe");
                            }

                            @Override
                            public void onSuccess(Test2 zhengfudaohangs) {
                                LogUtils.w("http", "onSuccess:",zhengfudaohangs);
                            }

                            @Override
                            public void onError(Throwable e) {
                                LogUtils.w("http", "onError:"+e.getMessage());
                            }
                        });
                break;
            case R.id.htpp_requet2:
                HttpHelper.getInstance().service.get(url)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())

                        .subscribe(new SingleObserver<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                LogUtils.w("http", "onSubscribe");
                            }

                            @Override
                            public void onSuccess(String zhengfudaohangs) {
                                LogUtils.w("http", "onSuccess:"+zhengfudaohangs);
                            }

                            @Override
                            public void onError(Throwable e) {
                                LogUtils.w("http", "onError:"+e.getMessage());
                            }
                        });
                break;
            case R.id.htpp_requet3:


                break;
            case R.id.htpp_requet4:
                getActivity().startService(new Intent(getActivity(), GetMyLocaltion_IntentService.class));
                break;
            case R.id.htpp_requet5://绑定
                mListener.onFragmentInteraction(Uri.parse("startservice://add"));
                break;
            case R.id.htpp_requet6://解绑
                mListener.onFragmentInteraction(Uri.parse("stopservice://add"));
                break;
            case R.id.htpp_requet7://视频录制
                // 录制
                startActivityForResult(new Intent(getActivity(), VideoRecord.class), IWantToVideo);
                break;
            case R.id.htpp_requet8:
                // 选择本地视频压缩
                LocalMediaConfig.Buidler buidler = new LocalMediaConfig.Buidler();
                final LocalMediaConfig config1 = buidler
                        //.setVideoPath(path)
                        .captureThumbnailsTime(1)
                        .doH264Compress(new AutoVBRMode())
                        .setFramerate(15)
                        .setScale(1.0f)
                        .build();
                OnlyCompressOverBean onlyCompressOverBean = new LocalMediaCompress(config1).startCompress();
                break;
            case R.id.text_view:
                mListener.onFragmentInteraction(Uri.parse("changeui://add"));
                break;
        }
    }

    public static int IWantToVideo = 1023;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IWantToVideo && resultCode == SuccessActivity.VideoSure) {
            //录像后的视频位置
            LogUtils.w("shipin", data.getStringExtra(SuccessActivity.VideoPath));
        }
    }
}