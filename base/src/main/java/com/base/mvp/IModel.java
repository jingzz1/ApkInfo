package com.base.mvp;

/**
 * Created by Administrator - PC on 2017/12/1.
 */

public interface IModel<T> {
    //初始化数据并回调
    void initData(MVPCallBack<T> mvpCallBack);

    //返回数据
    T getData();
}
