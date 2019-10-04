package com.base.base.refreshView;

import android.view.View;

public interface IRefreshFooter {

    View getView();

    //正在加载
    void refreshing();

    //滑动中
    public void refreshScrolling(int deltaY);

    //滑动距离小于footerView高度 此时应该提示继续下滑
    void deltaLessHeight(int deltaY);

    //滑动距离大于等于footerView高度
    void deltaMoreHeight(int deltaY);

    //加载完成,footerView已隐藏
    public void onComplete();

}
