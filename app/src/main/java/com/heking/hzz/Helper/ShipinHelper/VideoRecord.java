package com.heking.hzz.Helper.ShipinHelper;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.heking.hzz.Base.BaseActivity;
import com.heking.hzz.Base.ToolbarInfo;
import com.heking.hzz.R;

import butterknife.BindView;

/**
 * Created by Lee on 2017/10/16.
 */

public class VideoRecord extends BaseActivity implements LQRVideoRecordView.OnRecordStausChangeListener {
    @BindView(R.id.vrvVideo)
    LQRVideoRecordView mVrvVideo;
    @BindView(R.id.btnVideo)
    Button mBtnVideo;
    @BindView(R.id.tvTipOne)
    TextView mTvTipOne;
    @BindView(R.id.tvTipTwo)
    TextView mTvTipTwo;
    @BindView(R.id.rp)
    RecordProgress mRp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_record);
    }

    @Override
    public ToolbarInfo getMyTitle() {
        return null;
    }

    public void initView() {
        mRp.setRecordTime(10);
        initListener();
    }

    private void initListener() {
        mBtnVideo.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mRp.start();
                    mRp.setProgressColor(Color.parseColor("#1AAD19"));
                    mTvTipOne.setVisibility(View.VISIBLE);
                    mTvTipTwo.setVisibility(View.GONE);
                    //开始录制
                    mVrvVideo.record(VideoRecord.this);
                    break;
                case MotionEvent.ACTION_UP:
                    mRp.stop();
                    mTvTipOne.setVisibility(View.GONE);
                    mTvTipTwo.setVisibility(View.GONE);
                    //判断时间
                    if (mVrvVideo.getTimeCount() > 3) {
                        if (!isCancel(v, event)) {
                            onRecrodFinish();
                        }
                    } else {
                        if (!isCancel(v, event)) {
                            Toast.makeText(getApplicationContext(), "视频时长太短", Toast.LENGTH_SHORT).show();
                            if (mVrvVideo.getVecordFile() != null)
                                mVrvVideo.getVecordFile().delete();
                        }
                    }
                    resetVideoRecord();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (isCancel(v, event)) {
                        mTvTipOne.setVisibility(View.GONE);
                        mTvTipTwo.setVisibility(View.VISIBLE);
                        mRp.setProgressColor(Color.parseColor("#FF1493"));
                    } else {
                        mTvTipOne.setVisibility(View.VISIBLE);
                        mTvTipTwo.setVisibility(View.GONE);
                        mRp.setProgressColor(Color.parseColor("#1AAD19"));
                    }
                    break;
            }
            return true;
        });
    }

    private boolean isCancel(View v, MotionEvent event) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        if (event.getRawX() < location[0] || event.getRawX() > location[0] + v.getWidth() || event.getRawY() < location[1] - 40) {
            return true;
        }
        return false;
    }

    @Override
    public void onRecrodFinish() {
        //跳转页面
        String path = mVrvVideo.getVecordFile().toString();
        showToast(path);
        openSuccessActivity(path);
    }

    public static int VideoRecordRequestCode = 1066;

    private void openSuccessActivity(String path) {
        runOnUiThread(() -> {
            mTvTipOne.setVisibility(View.GONE);
            mTvTipTwo.setVisibility(View.GONE);
            resetVideoRecord();
            //打开播放界面
            Intent intent = new Intent(VideoRecord.this, SuccessActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(SuccessActivity.VideoPath, path);
            intent.putExtras(bundle);
            startActivityForResult(intent, VideoRecordRequestCode);
        });
    }


    @Override
    public void onRecording(int timeCount, int recordMaxTime) {

    }

    @Override
    public void onRecordStart() {
    }

    /**
     * 停止录制（释放相机后重新打开相机）
     */
    public void resetVideoRecord() {
        mVrvVideo.stop();
        mVrvVideo.openCamera();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VideoRecordRequestCode && resultCode == SuccessActivity.VideoSure) {//录像完成
            setResult(SuccessActivity.VideoSure, data);//new Intent().putExtra(SuccessActivity.VideoPath, file)
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
