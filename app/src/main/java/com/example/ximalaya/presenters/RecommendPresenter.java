package com.example.ximalaya.presenters;

import android.support.annotation.Nullable;

import com.example.ximalaya.interfaces.IRecommendPresenter;
import com.example.ximalaya.interfaces.IRecommendViewCallback;
import com.example.ximalaya.utils.Constants;
import com.example.ximalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import java.lang.invoke.MutableCallSite;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Android Studio.
 * User: AtlanticYu
 * Date: 2019/5/28
 * Time: 22:22
 */
public class RecommendPresenter implements IRecommendPresenter {

    private static final String TAG = "RecommendPresenter";

    private List<IRecommendViewCallback> mCallback = new ArrayList<>();

    private RecommendPresenter() {

    }
    private static RecommendPresenter sInstance = null;

    /*
        获取单例对象
     */

    public static RecommendPresenter getInstance() {
        if (sInstance == null) {
            //synchronized(this)为一个对象锁,同一时间只有一个线程访问
            synchronized (RecommendPresenter.class) {
                if (sInstance == null) {
                    sInstance = new RecommendPresenter();
                }
            }
        }
        return sInstance;
    }

    @Override
    public void getRecommendList() {
        /**
         * 获取推荐内容，即猜你喜欢
         */
        updateLoading();
        //封装参数
        Map<String, String> map = new HashMap<>();
        //一页数据返回多少条
        map.put(DTransferConstants.LIKE_COUNT, Constants.COUNT_RECOMMEND + "");
        CommonRequest.getGuessLikeAlbum(map, new IDataCallBack<GussLikeAlbumList>() {
            @Override
            public void onSuccess(@Nullable GussLikeAlbumList gussLikeAlbumList) {
                //数据获取成功
                if (gussLikeAlbumList != null) {
                    List<Album> albumList = gussLikeAlbumList.getAlbumList();
                    if (albumList != null) {
                        LogUtil.d(TAG,"size -- >" + albumList.size());
                        handlerRecommendResult(albumList);
                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                //数据获取出错
                LogUtil.d(TAG,"error  -- > " + i);
                LogUtil.d(TAG,"errorMsg  -- > " + s);
                handlerError();
            }
        });
    }

    //数据获取失败
    private void handlerError() {
        if (mCallback != null) {
            for (IRecommendViewCallback callback : mCallback) {
                callback.onNetworkError();
            }
        }
    }

    //获取成功后的两种判断，已经处理了数据请求，根据返回的结果集分发（即回调）给相应注册的Fragment
    // （可能包含多个界面，这时候mCallback列表的优势就体现了，很方便地把数据分发给需要的Fragment们）
    private void handlerRecommendResult(List<Album> albumList) {
        if (albumList != null) {
            //test,可观察没找到的界面
            //albumList.clear();
            if (albumList.size() == 0) {
                for (IRecommendViewCallback callback : mCallback) {
                    callback.onEmpty();
                }
            } else {
                for (IRecommendViewCallback callback : mCallback) {
                    //回调到RecommendFragment类中执行onRecommendListLoaded（）方法
                    callback.onRecommendListLoaded(albumList);
                }
            }
        }
    }

    private void updateLoading() {
        for (IRecommendViewCallback callback : mCallback) {
            callback.onLoading();
        }
    }


    @Override
    public void pullRefreshMore() {

    }

    @Override
    public void loadMore() {

    }
    //Fragment登记到mCallback列表里
    @Override
    public void registerViewCallback(IRecommendViewCallback callback) {
        if (mCallback != null && !mCallback.contains(callback)) {
            mCallback.add(callback);
        }
    }

    @Override
    public void unRegisterViewCallback(IRecommendViewCallback callback) {
        if (mCallback != null)
            mCallback.remove(callback);
    }


}
