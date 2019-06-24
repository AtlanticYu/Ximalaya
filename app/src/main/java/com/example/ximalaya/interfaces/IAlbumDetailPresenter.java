package com.example.ximalaya.interfaces;

import com.example.ximalaya.base.IBasePresenter;

/**
 * Created by Android Studio.
 * User: AtlanticYu
 * Date: 2019/6/10
 * Time: 23:07
 */
public interface IAlbumDetailPresenter extends IBasePresenter<IAlbumDetailViewCallback> {

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

}
