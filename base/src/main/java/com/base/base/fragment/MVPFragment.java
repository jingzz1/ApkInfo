package com.base.base.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.mvp.BasePresenter;
import com.base.mvp.MVPComposite;


/**
 * Created by Administrator on 2018/7/10.
 * mvp层
 */

public abstract class MVPFragment extends LazyLoadFragment{
    protected MVPComposite mvpComposite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mvpComposite = new MVPComposite();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected void addPresenter(BasePresenter presenter){
        mvpComposite.addPresenter(presenter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mvpComposite != null)
            mvpComposite.clear();
    }


}
