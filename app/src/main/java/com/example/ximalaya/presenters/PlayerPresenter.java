package com.example.ximalaya.presenters;

import com.example.ximalaya.base.BaseApplication;
import com.example.ximalaya.interfaces.IPlayerCallback;
import com.example.ximalaya.interfaces.IPlayerPresenter;
import com.example.ximalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.advertis.Advertis;
import com.ximalaya.ting.android.opensdk.model.advertis.AdvertisList;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.advertis.IXmAdsStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android Studio.
 * User: AtlanticYu
 * Date: 2019/6/20
 * Time: 22:31
 */
public class PlayerPresenter implements IPlayerPresenter, IXmAdsStatusListener, IXmPlayerStatusListener {

    private List<IPlayerCallback> mIPlayerCallbacks = new ArrayList<>();

    private static final String TAG = "PlayerPresenter";
    private XmPlayerManager mPlayerManager;
    private String mTrackTitle;

    private PlayerPresenter() {
        //用以设置播放器列表
        mPlayerManager = XmPlayerManager.getInstance(BaseApplication.getAppContext());

        //广告相关的接口
        mPlayerManager.addAdsStatusListener(this);

        //注册播放器状态相关的接口
        mPlayerManager.addPlayerStatusListener(this);
    }

    private static PlayerPresenter sPlayerPresenter;

    public static PlayerPresenter getPlayerPresenter() {
        if (sPlayerPresenter == null) {
            //访问锁
            synchronized (PlayerPresenter.class) {
                if (sPlayerPresenter == null) {
                    sPlayerPresenter = new PlayerPresenter();
                }
            }
        }
        return sPlayerPresenter;
    }

    private boolean isPlayListSet = false;

    public void setPlayList(List<Track> list,int playIndex) {

        if (mPlayerManager != null) {
            mPlayerManager.setPlayList(list,playIndex);
            isPlayListSet = true;
            //第一次节目标题获取并设置
            Track track = list.get(playIndex);
            mTrackTitle = track.getTrackTitle();
        } else {
            LogUtil.d(TAG,"mPlayerManager 为空");
        }

    }



    @Override
    public void play() {
        if (!isPlayListSet) {
            mPlayerManager.play();
        }
    }

    @Override
    public void pause() {
        if (mPlayerManager != null) {
            mPlayerManager.pause();
        }

    }

    @Override
    public void stop() {

    }

    @Override
    public void playPre() {
        //上一曲
        if (mPlayerManager != null) {
            mPlayerManager.playPre();
        }
    }

    @Override
    public void playNext() {
        //下一曲
        if (mPlayerManager != null) {
            mPlayerManager.playNext();
        }
    }

    @Override
    public void switchPlayMode(XmPlayListControl.PlayMode mode) {

    }

    @Override
    public void getPlayList() {

    }

    @Override
    public void playByIndex(int index) {

    }

    @Override
    public void seekTo(int progress) {
        //更新播放器进度,现在才使用官方封装标准调用
        mPlayerManager.seekTo(progress);
    }

    //====== 广告相关回调方法  start ==========
    @Override
    public void onStartGetAdsInfo() {
        LogUtil.d(TAG,"开始获取广告信息");
    }

    @Override
    public void onGetAdsInfo(AdvertisList advertisList) {
        LogUtil.d(TAG,"得到广告信息");
    }

    @Override
    public void onAdsStartBuffering() {
        LogUtil.d(TAG,"开始广告缓冲");
    }

    @Override
    public void onAdsStopBuffering() {
        LogUtil.d(TAG,"停止广告缓冲");
    }

    @Override
    public void onStartPlayAds(Advertis advertis, int i) {
        LogUtil.d(TAG,"开始播放广告");
    }

    @Override
    public void onCompletePlayAds() {
        LogUtil.d(TAG,"完成广告播放");
    }


    @Override
    public void onError(int what,int extra) {
        LogUtil.d(TAG," 错误信息---->"  + what + " 错误码---->"  + extra);
    }

    @Override
    public boolean isPlay() {
        //返回播放状态
        return mPlayerManager.isPlaying();
    }

    //====== 广告相关回调方法  stop ==========


    @Override
    public void registerViewCallback(IPlayerCallback iPlayerCallback) {
        iPlayerCallback.onTrackTitleUpdate(mTrackTitle);
        if (!mIPlayerCallbacks.contains(iPlayerCallback)) {
            mIPlayerCallbacks.add(iPlayerCallback);
        }
    }

    @Override
    public void unRegisterViewCallback(IPlayerCallback iPlayerCallback) {
        mIPlayerCallbacks.remove(iPlayerCallback);
    }


    //============= 播放器相关的回调方法 start    继承IXmPlayerStatusListener产生=============
    //tip 播放器控制逻辑说明
    //执行此方法是因为此类已获得喜马拉雅播放器状态变化的回调实现，即我们先用XmPlayerManager控制播放或暂停
    //然后IXmPlayerStatusListener回调已实现的回调方法，即下边的实现方法，这些方法再次回调playeractivity中的
    //onPlayStart()、等等 方法来修改UI图片资源
    @Override
    public void onPlayStart() {
        LogUtil.d(TAG,"onPlayStart...");
        for (IPlayerCallback iPlayerCallback : mIPlayerCallbacks) {
            iPlayerCallback.onPlayStart();
        }
    }

    @Override
    public void onPlayPause() {
        LogUtil.d(TAG,"onPlayPause...");
        for (IPlayerCallback iPlayerCallback : mIPlayerCallbacks) {
            iPlayerCallback.onPlayPause();
        }
    }

    @Override
    public void onPlayStop() {
        LogUtil.d(TAG,"onPlayStop...");
        for (IPlayerCallback iPlayerCallback : mIPlayerCallbacks) {
            iPlayerCallback.onPlayStop();
        }
    }

    @Override
    public void onSoundPlayComplete() {
        LogUtil.d(TAG,"onSoundPlayComplete...");
    }

    @Override
    public void onSoundPrepared() {
        LogUtil.d(TAG,"onSoundPrepared...");
    }

    //试听节目回调
    @Override
    public void onSoundSwitch(PlayableModel lastModel, PlayableModel curModel) {
        LogUtil.d(TAG,"onSoundSwitch...");

        //instanceof判断一个实例是否属于某种类型
        if (curModel instanceof Track) {
            Track currentTrack = (Track) curModel;
            mTrackTitle = currentTrack.getTrackTitle();
            LogUtil.d(TAG,"title --->" +mTrackTitle);
            for (IPlayerCallback iPlayerCallback :mIPlayerCallbacks) {
                iPlayerCallback.onTrackTitleUpdate(mTrackTitle);
            }
        }
    }

    @Override
    public void onBufferingStart() {
        LogUtil.d(TAG,"onBufferingStart...");
    }

    @Override
    public void onBufferingStop() {
        LogUtil.d(TAG,"onBufferingStop...");
    }

    @Override
    public void onBufferProgress(int progress) {
        LogUtil.d(TAG,"缓冲进度--->" + progress);
    }

    //单位：毫秒
    @Override
    public void onPlayProgress(int currPos, int duration) {
        for (IPlayerCallback iPlayerCallback :mIPlayerCallbacks) {
            iPlayerCallback.onProgressChange(currPos,duration);
        }
        LogUtil.d(TAG,"onPlayProgress....");
    }

    @Override
    public boolean onError(XmPlayerException e) {
        LogUtil.d(TAG,"onError e --->" +e);
        return false;
    }

    //============= 播放器相关的回调方法 end    继承IXmPlayerStatusListener产生=============
}
