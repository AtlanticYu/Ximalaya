package com.example.ximalaya;

import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ximalaya.adapters.DetailListAdapter;
import com.example.ximalaya.base.BaseActivity;
import com.example.ximalaya.interfaces.IAlbumDetailPresenter;
import com.example.ximalaya.interfaces.IAlbumDetailViewCallback;
import com.example.ximalaya.presenters.AlbumDetailPresenter;
import com.example.ximalaya.utils.ImageBlur;
import com.example.ximalaya.utils.LogUtil;
import com.example.ximalaya.views.RoundRectImageView;
import com.example.ximalaya.views.UILoader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

/**
 * Created by Android Studio.
 * User: AtlanticYu
 * Date: 2019/6/5
 * Time: 23:25
 */
public class DetailActivity extends BaseActivity implements IAlbumDetailViewCallback, UILoader.OnRetryClickListener {

    private static final String TAG = "DetailActivity";
    private ImageView mLargeCover;
    private RoundRectImageView mSmallCover;
    private TextView mAlbumTitlt;
    private TextView mAlbumAuthor;
    private AlbumDetailPresenter mAlbumDetailPresenter;
    private int mCurrentPage = 1;
    private RecyclerView mDetailList;
    private DetailListAdapter mDetailListAdapter;
    private FrameLayout mDetailListContainer;
    private UILoader mUILoader;
    private long mCurrentId = -1;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //手机状态栏和界面颜色一致
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        initView();

        mAlbumDetailPresenter = AlbumDetailPresenter.getsInstance();
        mAlbumDetailPresenter.registerViewCallback(this);
    }

    private void initView() {
        mDetailListContainer = this.findViewById(R.id.detail_list_container);
        //注意UILoader的使用
        if (mUILoader == null) {
            //基类为Context
            mUILoader = new UILoader(this) {
                @Override
                public View getSuccessView(ViewGroup container) {
                    return creatSuccessView(container);
                }
            };
            mDetailListContainer.removeAllViews();
            mDetailListContainer.addView(mUILoader);
            mUILoader.setOnRetryClickListener(DetailActivity.this);
        }

        mLargeCover = this.findViewById(R.id.iv_large_cover);
        mSmallCover = this.findViewById(R.id.iv_small_cover);
        mAlbumTitlt = this.findViewById(R.id.tv_album_title);
        mAlbumAuthor = this.findViewById(R.id.tv_album_author);

    }

    private View creatSuccessView(ViewGroup container) {
        //装载一个界面,不用自定义控件最后就填false
        View detailListView = LayoutInflater.from(this).inflate(R.layout.item_detail_list,container,false);

        mDetailList = detailListView.findViewById(R.id.album_list_detail);
        //1.RecyclerView需要一个布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mDetailList.setLayoutManager(layoutManager);
        //2.适配器
        mDetailListAdapter = new DetailListAdapter();
        mDetailList.setAdapter(mDetailListAdapter);
        //3.设置item间距
        mDetailList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = UIUtil.dip2px(view.getContext(),3);
                outRect.bottom = UIUtil.dip2px(view.getContext(),3);
                outRect.left = UIUtil.dip2px(view.getContext(),3);
                outRect.right = UIUtil.dip2px(view.getContext(),3);
            }
        });
        return detailListView;
    }

    @Override
    public void onDetailListLoaded(List<Track> tracks) {
        //根据结果控制UI
        if (tracks == null || tracks.size() == 0) {
            if (mUILoader != null) {
                mUILoader.updateStatus(UILoader.UIStatus.EMPTY);
            }
        }
        if (mUILoader != null) {
            mUILoader.updateStatus(UILoader.UIStatus.SUCCESS);
        }
        //更新，设置UI数据
        mDetailListAdapter.setData(tracks);
    }

    @Override
    public void OnNetworkError(int errorCode, String errMsg) {
        //请求发生错误，显示网络异常状态
        mUILoader.updateStatus(UILoader.UIStatus.NETWORK_ERROE);
    }

    @Override
    public void onAlbumLoaded(Album album) {
        //AlbumDetailPresenter返回推荐列表中用户点击的item
        long id = album.getId();
        mCurrentId = id;
        //获取单个节目的列表详情
        if (mAlbumDetailPresenter != null) {
            mAlbumDetailPresenter.getAlbumDetail((int) id,mCurrentPage);
        }
        //拿数据，显示load状态
        if (mUILoader != null) {
            mUILoader.updateStatus(UILoader.UIStatus.LOADING);
        }


        if (mAlbumTitlt != null) {
            mAlbumTitlt.setText(album.getAlbumTitle());
        }
        if (mAlbumAuthor != null) {
            mAlbumAuthor.setText(album.getAnnouncer().getNickname());
        }
        if (mLargeCover != null) {
            //网络加载成功才开始模糊，这样安全，picasso为异步，若失败则会崩溃，所以需要处理
            Picasso.with(this).load(album.getCoverUrlLarge()).into(mLargeCover, new Callback() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                public void onSuccess() {
                    Drawable drawable = mLargeCover.getDrawable();
                    if(drawable != null) {
                        //到这里才有图片
                        //设置模糊,这里的this必须指定
                        ImageBlur.makeBlur(mLargeCover,DetailActivity.this);
                    }

                }

                @Override
                public void onError() {
                    LogUtil.d(TAG,"onError 加载详情的封面失败");
                }
            });
        }

        if (mSmallCover != null) {
            Picasso.with(this).load(album.getCoverUrlLarge()).into(mSmallCover);
        }
    }

    @Override
    public void OnRetryClick() {
        //这里面表示网络不佳后点击重试
        if (mAlbumDetailPresenter != null) {
            mAlbumDetailPresenter.getAlbumDetail((int) mCurrentId,mCurrentPage);
        }
    }
}
