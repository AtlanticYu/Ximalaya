package com.example.ximalaya.interfaces;

/**
 * Created by Android Studio.
 * User: AtlanticYu
 * Date: 2019/6/10
 * Time: 23:07
 */
public interface IAlbumDetailPresenter {

    /**
     * 下拉刷新内容
     */

    void pullRefreshMore();

    /**
     * 上接加载更多
     */

    void loadMore();

    /*
     *获取专辑详情
     */
    void getAlbumDetail(int albumId,int page);

    /*
        注册UI通知的接口
     */
    void registerViewCallback(IAlbumDetailViewCallback detailViewCallback);

    /*
        删除UI通知的接口
     */
    void unregisterViewCallback(IAlbumDetailViewCallback detailViewCallback);

}
