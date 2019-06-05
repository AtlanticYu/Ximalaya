package com.example.ximalaya.base;

import android.app.Application;
import android.util.Log;
import android.os.Handler;

import com.example.ximalaya.utils.LogUtil;
import com.example.ximalaya.views.UILoader;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;


/*
**
**AndroidManifest中的android:name=".base.BaseApplication"使得可先执行此文件，初始化我们自定义的日志工具，初始化喜马拉雅接口
 */

public class BaseApplication extends Application {

    private static final String TAG = "BaseApplication";

    private static Handler sHandler = null;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化LogUtil
        LogUtil.init(this.getPackageName(),false);

        CommonRequest mXimalaya = CommonRequest.getInstanse();
        if(DTransferConstants.isRelease) {
            String mAppSecret = "8646d66d6abe2efd14f2891f9fd1c8af";
            mXimalaya.setAppkey("9f9ef8f10bebeaa83e71e62f935bede8");
            mXimalaya.setPackid("com.app.test.android");
            mXimalaya.init(this ,mAppSecret);
        } else {
            String mAppSecret = "0a09d7093bff3d4947a5c4da0125972e";
            mXimalaya.setAppkey("f4d8f65918d9878e1702d49a8cdf0183");
            mXimalaya.setPackid("com.ximalaya.qunfeng");
            mXimalaya.init(this ,mAppSecret);
        }
        //UILoader需要借助它在主线程更新UI
        sHandler = new Handler();
    }

    public static Handler getHandler() {
        return sHandler;
    }
}
