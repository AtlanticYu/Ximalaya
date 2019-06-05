package com.example.ximalaya.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.example.ximalaya.R;

/**
 * Created by Android Studio.
 * User: AtlanticYu
 * Date: 2019/6/5
 * Time: 19:29
 */
@SuppressLint("AppCompatCustomView")
public class LoadingView  extends ImageView {

    //旋转角度
    private int rotateDegree = 0;
    private Boolean mNeedRotate = false;

    public LoadingView(Context context) {
        this(context,null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public LoadingView(Context context,@ Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //设置图标
        setImageResource(R.mipmap.loading);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mNeedRotate = true;
        //绑定到windows的时候
        post(new Runnable() {
            @Override
            public void run() {
                rotateDegree += 30;
                rotateDegree  = rotateDegree <= 360 ? rotateDegree : 0;
                invalidate();
                //是否继续旋转
                if (mNeedRotate) {
                    //0.2秒后再次调用本身
                    postDelayed(this,100);
                }
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //从windows中解绑了
        mNeedRotate = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        /*
            第一个参数为旋转角度
            第二个参数是旋转的x坐标
            第三个参数是旋转y的坐标
         */
        canvas.rotate(rotateDegree,getWidth()/2,getHeight()/2);
        super.onDraw(canvas);
    }
}
