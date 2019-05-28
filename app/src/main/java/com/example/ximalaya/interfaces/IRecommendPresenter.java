package com.example.ximalaya.interfaces;

/**
 * Created by Android Studio.
 * User: AtlanticYu
 * Date: 2019/5/26
 * Time: 22:48
 */
public interface IRecommendPresenter {
    /**
     * 获取推荐内容
     */

    void getRecommendList();


    /**
     * 下拉刷新内容
     */

    void pullRefreshMore();

    /**
     * 上接加载更多
     */

    void loadMore();

    /*
        这个方法用于注册UI的回调
     */
    void registerViewCallback(IRecommendViewCallback callback);

    /*
        取消UI的回调注册，避免内存泄漏
     */
    void unRegisterViewCallback(IRecommendViewCallback callback);
}
