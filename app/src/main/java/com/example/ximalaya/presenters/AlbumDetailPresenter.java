package com.example.ximalaya.presenters;

import com.example.ximalaya.interfaces.IAlbumDetailPresenter;
import com.example.ximalaya.interfaces.IAlbumDetailViewCallback;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android Studio.
 * User: AtlanticYu
 * Date: 2019/6/10
 * Time: 23:10
 */
public class AlbumDetailPresenter implements IAlbumDetailPresenter {

    private List<IAlbumDetailViewCallback> mCallbacks = new ArrayList<>();

    private Album mTargetAlbum = null;

    private AlbumDetailPresenter() {

    }

    private static AlbumDetailPresenter sInstance = null;

    public static AlbumDetailPresenter getsInstance() {
        if (sInstance == null)
        {
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

    }

    @Override
    public void registerViewCallback(IAlbumDetailViewCallback detailViewCallback) {
        if(!mCallbacks.contains(detailViewCallback)) {
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
