package com.example.ximalaya.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ximalaya.DetailActivity;
import com.example.ximalaya.R;
import com.example.ximalaya.adapters.RecommendListAdapter;
import com.example.ximalaya.base.BaseFragment;
import com.example.ximalaya.interfaces.IRecommendViewCallback;
import com.example.ximalaya.presenters.AlbumDetailPresenter;
import com.example.ximalaya.presenters.RecommendPresenter;
import com.example.ximalaya.views.UILoader;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

public class RecommendFragment extends BaseFragment implements IRecommendViewCallback, UILoader.OnRetryClickListener, RecommendListAdapter.onRecommendItemClickListener {

    private static final String TAG = "RecommendFragment";
    private View mRootView;
    private RecyclerView mRecommendRv;
    private RecommendListAdapter mRecommendListAdapter;
    private RecommendPresenter mRecommendPresenter;
    private UILoader mUILoader;

    @Override
    protected View onSubViewLoaded(final LayoutInflater layoutInflater, ViewGroup container) {

        //实现抽象方法
        mUILoader = new UILoader(getContext()) {
            @Override
            public View getSuccessView(ViewGroup container) {
                return createSuccessView(layoutInflater,container);
            }
        };
        //获取到逻辑层的对象
        mRecommendPresenter = RecommendPresenter.getInstance();
        //先要设置通知接口的注册，即回调函数注册，方便presenter给我们传回来数值结果，
        mRecommendPresenter.registerViewCallback(this);
        //获取推荐列表
        mRecommendPresenter.getRecommendList();
        //instanceof 判断其左边对象是否为其右边类的实例
        if (mUILoader.getParent() instanceof  ViewGroup) {
            ((ViewGroup) mUILoader.getParent()).removeView(mUILoader);
        }

        mUILoader.setOnRetryClickListener(this);

        //返回给界面显示
        return mUILoader;
    }

    private View createSuccessView(LayoutInflater layoutInflater,ViewGroup container) {
        //view加载完成
        mRootView = layoutInflater.inflate(R.layout.fragment_recommend,container,false);
        //RecyclerView的使用
        //1.找到控件
        mRecommendRv = mRootView.findViewById(R.id.recommend_list);
        //2.设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(linearLayoutManager.VERTICAL);
        mRecommendRv.setLayoutManager(linearLayoutManager);
        //recyclerview的各条数据之间的分割，用到了量度转化
        mRecommendRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = UIUtil.dip2px(view.getContext(),5);
                outRect.bottom = UIUtil.dip2px(view.getContext(),5);
                outRect.left = UIUtil.dip2px(view.getContext(),5);
                outRect.right = UIUtil.dip2px(view.getContext(),5);
            }
        });
        //3.设置适配器
        mRecommendListAdapter = new RecommendListAdapter();
        mRecommendRv.setAdapter(mRecommendListAdapter);
        //开始注册本Fragment，传递给Adapter，方便回调
        mRecommendListAdapter.setOnRecommendItemClickListener(this);
        return mRootView;
    }

    //Presenter回调此函数完成数据设置
    @Override
    public void onRecommendListLoaded(List<Album> result) {
        //当获取到推荐内容的时候，这个方法就会被调用
        //presenter执行成功后跳转到此处理数据
        //把数据设置给适配器，并且更新UI
        mRecommendListAdapter.setData(result);
        mUILoader.updateStatus(UILoader.UIStatus.SUCCESS);
    }

    @Override
    public void onNetworkError() {
        mUILoader.updateStatus(UILoader.UIStatus.NETWORK_ERROE);
    }

    @Override
    public void onEmpty() {
        mUILoader.updateStatus(UILoader.UIStatus.EMPTY);
    }

    @Override
    public void onLoading() {
        mUILoader.updateStatus(UILoader.UIStatus.LOADING);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //取消接口的注释，避免内存泄漏和内存溢出
        if (mRecommendPresenter != null) {
            mRecommendPresenter.unRegisterViewCallback(this);
        }
    }

    @Override
    public void OnRetryClick() {
        //表示网络不佳时重新获取数据
        //重新获取数据即可
        if (mRecommendPresenter != null) {
            mRecommendPresenter.getRecommendList();
        }
    }
    //RecommendListAdapter的item点击事件捕获函数回调到此处
    @Override
    public void onItemClick(int position, Album album) {
        AlbumDetailPresenter.getsInstance().setTargetAlbum(album);
        //item被点击了，跳转到详情
        Intent intent = new Intent(getContext(), DetailActivity.class);
        startActivity(intent);
    }
}
