package com.base.mvp;

/**
 * Created by Administrator - PC on 2017/12/1.
 */

public interface MVPCallBack<T> {
    void onSuccess(T t);

    void onFail(String code);
}
