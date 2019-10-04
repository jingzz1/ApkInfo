package com.base.mvp;

public interface IPresenter<V extends IView> {
    void bindPresenter(V view);
    void unBindPresenter();
    boolean isBind();
}
