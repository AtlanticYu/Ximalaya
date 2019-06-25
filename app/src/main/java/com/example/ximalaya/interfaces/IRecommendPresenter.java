package com.example.ximalaya.interfaces;

import com.example.ximalaya.base.IBasePresenter;

/**
 * Created by Android Studio.
 * User: AtlanticYu
 * Date: 2019/5/26
 * Time: 22:48
 */
public interface IRecommendPresenter extends IBasePresenter< IRecommendViewCallback> {
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

}
