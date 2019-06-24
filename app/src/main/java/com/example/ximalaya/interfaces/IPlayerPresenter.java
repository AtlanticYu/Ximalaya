package com.example.ximalaya.interfaces;

import com.example.ximalaya.base.IBasePresenter;
import com.ximalaya.ting.android.opensdk.model.advertis.Advertis;
import com.ximalaya.ting.android.opensdk.model.advertis.AdvertisList;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

/**
 * Created by Android Studio.
 * User: AtlanticYu
 * Date: 2019/6/20
 * Time: 22:03
 */
public interface IPlayerPresenter extends IBasePresenter<IPlayerPresenter> {

    // 播放
    void play();

    //暂停
    void pause();

    //停止
    void stop();

    //上一曲
    void playPre();

    //下一曲
    void playNext();

    //切换播放模式
    void switchPlayMode(XmPlayListControl.PlayMode mode);

    //获取播放列表
    void getPlayList();

    /*
     *
     *
     *根据节目位置进行播放
     *
     * @param index 节目在列表中的位置
     */

    void playByIndex(int index);

    //切换进度
    void seekTo(int progress);

    //广告相关
    void onStartGetAdsInfo();

    void onGetAdsInfo(AdvertisList advertisList);

    void onAdsStartBuffering();

    void onAdsStopBuffering();

    void onStartPlayAds(Advertis advertis,int i);

    void onCompletePlayAds();

    void onError(int what,int extra);

}
