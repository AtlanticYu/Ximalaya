package com.example.ximalaya.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

/**
 * Created by Android Studio.
 * User: AtlanticYu
 * Date: 2019/5/28
 * Time: 22:09
 */
public interface IRecommendViewCallback {

    /*
        获取推荐内容的结果
     */
    void onRecommendListLoaded(List<Album> result);


    /*
        下滑加载更多
     */
    void onLoadMore(List<Album> result);

    /*
        下接加载更多
     */
    void onRefreshMore(List<Album> result);

}
