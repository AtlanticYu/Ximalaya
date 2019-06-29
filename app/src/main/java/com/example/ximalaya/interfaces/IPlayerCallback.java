package com.example.ximalaya.interfaces;

import android.os.Trace;
import android.widget.LinearLayout;

import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.util.List;

/**
 * Created by Android Studio.
 * User: AtlanticYu
 * Date: 2019/6/20
 * Time: 22:13
 */
public interface IPlayerCallback {

    // 开始播放
    void onPlayStart();

    //暂停播放
    void onPlayPause();

    // 停止播放
    void onPlayStop();

    //播放错误
    void onPlayError();

    // 下一曲
    void nextPlay(Track track);

    // 上一曲
    void onPreplay(Track track);


    //播放列表加载完成
    void onListLoaded(List<Track>  list);

    //播放模式改变
    void onPlayModeChange(XmPlayListControl.PlayMode playMode);

    //进度条改变
    void onProgressChange(int currentProgress ,int total);

    //广告正在加载
    void onAdLoading();

    //广告结束
    void onAdFinished();

    //更新节目标题
    void onTrackTitleUpdate(String title);
}
