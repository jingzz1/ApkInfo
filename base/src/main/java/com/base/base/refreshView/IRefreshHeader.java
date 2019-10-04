package com.base.base.refreshView;

import android.view.View;

//header接口，关于header变化，需要在这里操作
public interface IRefreshHeader {

    public View getView();

    //滑动中
    public void refreshScrolling(int deltaY);

    //滑动距离小于headerView高度 此时应该提示继续下滑
    public void deltaLessHeight(int deltaY);

    //滑动距离大于等于headerView高度
    public void deltaMoreHeight(int deltaY);

    //手指已释放，但此时滑动距离大于控件高度
    //此时可提示：正在加载
    public void headerRefreshing();

    //加载完成,headerView已隐藏
    public void headerComplete();


}
