package com.example.ximalaya.presenters;

import android.support.annotation.Nullable;

import com.example.ximalaya.interfaces.IAlbumDetailPresenter;
import com.example.ximalaya.interfaces.IAlbumDetailViewCallback;
import com.example.ximalaya.utils.Constants;
import com.example.ximalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Android Studio.
 * User: AtlanticYu
 * Date: 2019/6/10
 * Time: 23:10
 */
public class AlbumDetailPresenter implements IAlbumDetailPresenter {

    private static final String TAG = "AlbumDetailPresenter";

    private List<IAlbumDetailViewCallback> mCallbacks = new ArrayList<>();

    private Album mTargetAlbum = null;

    private AlbumDetailPresenter() {

    }

    private static AlbumDetailPresenter sInstance = null;

    public static AlbumDetailPresenter getsInstance() {
        if (sInstance == null) {
            //synchronized(this)为一个对象锁,同一时间只有一个线程访问
            synchronized (AlbumDetailPresenter.class) {
                if (sInstance == null) {
                    sInstance = new AlbumDetailPresenter();
                }
            }
        }
        return sInstance;
    }

    @Override
    public void pullRefreshMore() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    public void getAlbumDetail(int albumId, int page) {
        //根据页码和id获取单个节目详情，源代码可参考接入文档说明
        Map<String, String> map = new HashMap<>();
        //+ ""即可把albumId转化为String类型
        map.put(DTransferConstants.ALBUM_ID, albumId + "");
        map.put(DTransferConstants.SORT, "asc");
        map.put(DTransferConstants.PAGE, page + "");
        map.put(DTransferConstants.PAGE_SIZE, Constants.COUNT_DEFAULT + "");
        CommonRequest.getTracks(map, new IDataCallBack<TrackList>() {
            @Override
            public void onSuccess(@Nullable TrackList trackList) {
                if (trackList != null) {
                    List<Track> tracks = trackList.getTracks();
                    LogUtil.d(TAG, "track size --->" + tracks.size());
                    handlerAlbumDetailResult(tracks);
                }
            }

            @Override
            public void onError(int errorCode, String errMsg) {
                LogUtil.d(TAG, "error code  --->" + errorCode);
                LogUtil.d(TAG, "error message  ---->" + errMsg);
                handlerError(errorCode, errMsg);
            }
        });

    }

    //如果发生错误，就通知UI
    private void handlerError(int errorCode, String errMsg) {
        for (IAlbumDetailViewCallback callback : mCallbacks) {
            callback.OnNetworkError(errorCode,errMsg);
        }
    }

    //回调到fragment处理数据进行显示
    //因为这里是主线程(通过打印Thread.currentThread().getName()可得知)，所以可直接通知UI，不用handler
    private void handlerAlbumDetailResult(List<Track> tracks) {
        for (IAlbumDetailViewCallback mCallback : mCallbacks) {
            mCallback.onDetailListLoaded(tracks);
        }
    }

    @Override
    public void registerViewCallback(IAlbumDetailViewCallback detailViewCallback) {
        if (!mCallbacks.contains(detailViewCallback)) {
            mCallbacks.add(detailViewCallback);
            if (mTargetAlbum != null) {
                detailViewCallback.onAlbumLoaded(mTargetAlbum);
            }
        }
    }

    @Override
    public void unregisterViewCallback(IAlbumDetailViewCallback detailViewCallback) {
        mCallbacks.remove(detailViewCallback);
    }


    public void setTargetAlbum(Album targetAlbum) {
        this.mTargetAlbum = targetAlbum;
    }
}
