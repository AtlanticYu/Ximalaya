package com.example.ximalaya.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

    private View mRootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRootView = onSubViewLoaded(inflater,container);
        return mRootView;
    }

    //交给其余的子界面进行加载，此处仅声明子布局需要实现此函数，然后把view对象返回给上个方法的mRootView
    protected abstract View onSubViewLoaded(LayoutInflater layoutInflater, ViewGroup container);

}
