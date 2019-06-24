package com.example.ximalaya.presenters;

import com.example.ximalaya.base.BaseApplication;
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

import java.util.List;

/**
 * Created by Android Studio.
 * User: AtlanticYu
 * Date: 2019/6/20
 * Time: 22:31
 */
public class PlayerPresenter implements IPlayerPresenter, IXmAdsStatusListener, IXmPlayerStatusListener {

    private static final String TAG = "PlayerPresenter";
    private XmPlayerManager mPlayerManager;

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

    }

    @Override
    public void stop() {

    }

    @Override
    public void playPre() {

    }

    @Override
    public void playNext() {

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

    //====== 广告相关回调方法  stop ==========

    @Override
    public void registerViewCallback(IPlayerPresenter iPlayerPresenter) {

    }

    @Override
    public void unRegisterViewCallback(IPlayerPresenter iPlayerPresenter) {

    }


    //============= 播放器相关的回调方法 start    继承IXmPlayerStatusListener产生=============
    @Override
    public void onPlayStart() {
        LogUtil.d(TAG,"onPlayStart...");
    }

    @Override
    public void onPlayPause() {
        LogUtil.d(TAG,"onPlayPause...");
    }

    @Override
    public void onPlayStop() {
        LogUtil.d(TAG,"onPlayStop...");
    }

    @Override
    public void onSoundPlayComplete() {
        LogUtil.d(TAG,"onSoundPlayComplete...");
    }

    @Override
    public void onSoundPrepared() {
        LogUtil.d(TAG,"onSoundPrepared...");
    }

    @Override
    public void onSoundSwitch(PlayableModel playableModel, PlayableModel playableModel1) {
        LogUtil.d(TAG,"onSoundSwitch...");
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

    @Override
    public void onPlayProgress(int currPos, int duration) {
        LogUtil.d(TAG,"onPlayProgress....");
    }

    @Override
    public boolean onError(XmPlayerException e) {
        LogUtil.d(TAG,"onError e --->" +e);
        return false;
    }
    //============= 播放器相关的回调方法 end    继承IXmPlayerStatusListener产生=============
}
