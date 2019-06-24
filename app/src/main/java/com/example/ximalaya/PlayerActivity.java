package com.example.ximalaya;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.example.ximalaya.base.BaseActivity;
import com.example.ximalaya.presenters.PlayerPresenter;

/**
 * Created by Android Studio.
 * User: AtlanticYu
 * Date: 2019/6/16
 * Time: 20:36
 */
public class PlayerActivity extends BaseActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        //
        PlayerPresenter playerPresenter = PlayerPresenter.getPlayerPresenter();
        playerPresenter.play();
    }
}
