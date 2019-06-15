package com.example.ximalaya.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

/**
 * Created by Android Studio.
 * User: AtlanticYu
 * Date: 2019/6/10
 * Time: 23:20
 */
public interface IAlbumDetailViewCallback {

    /*
     *专辑详情内容加载出来了
     *
     * track -->声音
     */
    void onDetailListLoaded(List<Track> tracks);

    //网络错误
    void OnNetworkError(int errorCode, String errMsg);


    //把album传给UI
    void onAlbumLoaded(Album album);
}
