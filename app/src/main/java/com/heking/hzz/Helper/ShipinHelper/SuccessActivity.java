package com.heking.hzz.Helper.ShipinHelper;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import com.heking.hzz.Base.BaseActivity;
import com.heking.hzz.Base.ToolbarInfo;
import com.heking.hzz.Helper.LogUtils.LogUtils;
import com.heking.hzz.Helper.MyTextUtils;
import com.heking.hzz.R;

import android.app.Activity;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

import AppConfig.WPConfig;
import butterknife.BindView;

public class SuccessActivity extends BaseActivity implements OnClickListener {
    @BindView(R.id.iv_close)
    ImageView button1;
    @BindView(R.id.iv_next)
    ImageView button3;
    @BindView(R.id.videoView1)
    VideoView videoView1;//视频播放控件
    @BindView(R.id.tv_video_size)
    TextView tv_video_size;//显示视频大小
    private String file;//视频路径
    public static int VideoSure = 1050;
public static String VideoPath ="video_path";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_success);
    }

    @Override
    public void initView() {
        Bundle bundle = getIntent().getExtras();
        file = bundle.getString(VideoPath);//获得拍摄的短视频保存地址
        button1.setOnClickListener(this);
        button3.setOnClickListener(this);
        videoView1.setVideoPath(file);
        videoView1.start();//视频播放
        File file1 = new File(file);
        if (file1 != null)
            tv_video_size.setText(MyTextUtils.getFileSizeByByte((float) file1.length()));
        videoView1.setOnCompletionListener(mPlayer -> {
            mPlayer.start();
            mPlayer.setLooping(true);
        });
        videoView1.setOnErrorListener((mediaPlayer, i, i1) -> {
            if (WPConfig.isDebug) showToast("进来了");
            videoView1.setOnCompletionListener(null);
            return false;
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                finish();
                break;
            case R.id.iv_next:
                setResult(VideoSure, new Intent().putExtra(VideoPath, file));
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public ToolbarInfo getMyTitle() {
        return null;
    }
}
