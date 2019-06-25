package com.example.ximalaya.base;

/**
 * Created by Android Studio.
 * User: AtlanticYu
 * Date: 2019/6/20
 * Time: 22:04
 */
//泛型,其余更详细的接口继承此接口，然后在指定更具体的参数类型
public interface IBasePresenter<T> {

    /*
        *注册UI的回调接口
        *
        *
     */
    void registerViewCallback(T t);

    /*
     * 取消注册
     *
     *
     */
    void unRegisterViewCallback(T t);
}
