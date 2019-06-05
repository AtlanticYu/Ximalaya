package com.example.ximalaya.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.ximalaya.R;
import com.example.ximalaya.base.BaseApplication;

/**
 * Created by Android Studio.
 * User: AtlanticYu
 * Date: 2019/5/29
 * Time: 0:11
 */
public abstract class UILoader extends FrameLayout {

    private View mLoadingView;
    private View mSuccessView;
    private View mNetWorkErrorView;
    private View mEmptyView;
    private OnRetryClickListener mOnRetryClickListener = null;

    public enum UIStatus {
        LOADING,SUCCESS,NETWORK_ERROE,EMPTY,NONE
    }

    public UIStatus mCurrentStatus = UIStatus.NONE;

    public UILoader(@NonNull Context context) {
        this(context,null);
    }

    public UILoader(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public UILoader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void updateStatus(UIStatus status) {
        mCurrentStatus = status;
        //更新UI一定要在主线程
        BaseApplication.getHandler().post(new Runnable() {
            @Override
            public void run() {
                switchUIByCurrentStatus();
            }
        });
    }

    /*
        初始化UI
     */
    private void init() {
        switchUIByCurrentStatus();
    }

    private void switchUIByCurrentStatus() {
        //加载中
        if (mLoadingView == null) {
            mLoadingView = getLoadingView();
            addView(mLoadingView);
        }
        //根据状态设置是否可见
        mLoadingView.setVisibility(mCurrentStatus == UIStatus.LOADING ? VISIBLE : GONE);

        //成功
        if (mSuccessView == null) {
            mSuccessView = getSuccessView(this);
            addView(mSuccessView);
        }
        //根据状态设置是否可见
        mSuccessView.setVisibility(mCurrentStatus == UIStatus.SUCCESS ? VISIBLE : GONE);

        //网络错误界面
        if (mNetWorkErrorView == null) {
            mNetWorkErrorView = getNetWorkErrorView();
            addView(mNetWorkErrorView);
        }
        //根据状态设置是否可见
        mNetWorkErrorView.setVisibility(mCurrentStatus == UIStatus.NETWORK_ERROE ? VISIBLE : GONE);

        //数据为空界面
        if (mEmptyView == null) {
            mEmptyView = getEmptyView();
            addView(mEmptyView);
        }
        //根据状态设置是否可见
        mEmptyView.setVisibility(mCurrentStatus == UIStatus.EMPTY ? VISIBLE : GONE);
    }

    private View getEmptyView() {

        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_empty_view,this,false);
    }

    private View getNetWorkErrorView() {
        View networkErrorView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_error_view,this,false);
        networkErrorView.findViewById(R.id.network_error_icon).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //重新获取数据
                if (mOnRetryClickListener != null) {
                    mOnRetryClickListener.OnRetryClick();
                }
            }
        });
        return networkErrorView;
    }

    //因为不确定哪个界面，所以设置抽象方法，子类来具体实现
    public abstract View getSuccessView(ViewGroup container);

    private View getLoadingView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_loading_view,this,false);
    }

    //不设置点击的时候就不会响应
    public void setOnRetryClickListener(OnRetryClickListener listener) {
        this.mOnRetryClickListener = listener;
    }


    public interface OnRetryClickListener {
        //点击的时候就会调用此方法
        void OnRetryClick();
    }
}
