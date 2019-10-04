package com.base.base.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Administrator on 2018/7/10.
 * 懒加载
 */

public abstract class LazyLoadFragment extends JumpFragment {
    protected boolean isonCreated = false;
    protected boolean isUIVisible = false;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isonCreated = true;

        initStart(view, savedInstanceState);
        lazyLoad();
    }

    private void lazyLoad() {
        if (isonCreated && isUIVisible) {
            loadData();
            //数据加载完毕,恢复标记,防止重复加载
            isonCreated = false;
            isUIVisible = false;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isUIVisible = isVisibleToUser;
        if (isVisibleToUser) {
            lazyLoad();
        }
    }

    //初始化控件,onViewCreated()时之后调用
    protected abstract void initStart(View view, @Nullable Bundle savedInstanceState);

    //懒加载，判断该fragment是否对用户可见，用户可见时才加载 注意，单个fragment不执行此方法
    protected abstract void loadData();

}
