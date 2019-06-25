package com.example.ximalaya;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.ximalaya.base.BaseActivity;
import com.example.ximalaya.interfaces.IPlayerCallback;
import com.example.ximalaya.presenters.PlayerPresenter;
import com.example.ximalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Android Studio.
 * User: AtlanticYu
 * Date: 2019/6/16
 * Time: 20:36
 */
public class PlayerActivity extends BaseActivity implements IPlayerCallback {

    private static final String TAG = "PlayerActivity";

    private ImageView mControlBtn;

    private  PlayerPresenter mPlayerPresenter;

    private TextView mTotalDuration;

    private TextView mCurrentPosition;

    private SeekBar mDurationBar;

    private int mCurrentProgress = 0;
    private boolean mIsUserTouchProgressBar = false;

    private SimpleDateFormat mMinFormat = new SimpleDateFormat("mm:ss");

    private SimpleDateFormat mHourFormat = new SimpleDateFormat("hh:mm:ss");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        mPlayerPresenter = PlayerPresenter.getPlayerPresenter();
        mPlayerPresenter.registerViewCallback(this);
        initView();
        initEvent();
        startPlay();
    }


    private void startPlay() {
        if (mPlayerPresenter != null) {
            mPlayerPresenter.play();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayerPresenter != null) {
            mPlayerPresenter.unRegisterViewCallback(this);
            mPlayerPresenter = null;
        }
    }

    //给控件设置事件
    private void initEvent() {
        mControlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //如果正在播放，就暂停，已暂停，就播放
                if (mPlayerPresenter.isPlay()) {
                    mPlayerPresenter.pause();
                } else {
                    mPlayerPresenter.play();
                }
            }
        });

        mDurationBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean isFromUser) {
                if(isFromUser) {
                    mCurrentProgress = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mIsUserTouchProgressBar = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mIsUserTouchProgressBar = false;
                //拖动进度条手离开
                mPlayerPresenter.seekTo(mCurrentProgress);
            }
        });
    }

    //找到相关控件
    private void initView() {
        mControlBtn = this.findViewById(R.id.play_or_pause_btn);
        mTotalDuration = this.findViewById(R.id.track_duration);
        mCurrentPosition = this.findViewById(R.id.current_position);
        mDurationBar = this.findViewById(R.id.track_seek_bar);
    }

    @Override
    public void onProgressChange(int currentProgress ,int total) {
        //更新播放进度
        mDurationBar.setMax(total);
        String totalDuration;
        String currentPosition;
        if (total > 1000* 60*60) {
            totalDuration = mHourFormat.format(total);
            currentPosition = mHourFormat.format(currentProgress);
        } else {
            totalDuration = mMinFormat.format(total);
            currentPosition = mMinFormat.format(currentProgress);
        }
        //总时间
        if (mTotalDuration != null) {
            mTotalDuration.setText(totalDuration);
        }
        //更新当前时间
        if (mCurrentPosition != null) {
            mCurrentPosition.setText(currentPosition);
        }
        //更新当前进度

        if(!mIsUserTouchProgressBar) {
            mDurationBar.setProgress(currentProgress);
        }
    }


    @Override
    public void onPlayStart() {
        //开始播放，修改UI成暂停状态
        //判空，回调可能比初始化要早
        if (mControlBtn != null) {
            mControlBtn.setImageResource(R.mipmap.stop);
        }
    }

    @Override
    public void onPlayPause() {
        if (mControlBtn != null) {
            mControlBtn.setImageResource(R.mipmap.play);

        }

    }

    @Override
    public void onPlayStop() {
        mControlBtn.setImageResource(R.mipmap.play);
    }

    @Override
    public void onPlayError() {

    }

    @Override
    public void nextPlay(Track track) {

    }

    @Override
    public void onPreplay(Track track) {

    }

    @Override
    public void onListLoaded(List<Track> list) {

    }


    @Override
    public void onPlayModeChange(XmPlayListControl.PlayMode playMode) {

    }

    @Override
    public void onAdLoading() {

    }

    @Override
    public void onAdFinished() {

    }
}
