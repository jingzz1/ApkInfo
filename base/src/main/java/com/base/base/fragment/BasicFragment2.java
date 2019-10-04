package com.base.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.base.base.activity.BasicActivity;

/**
 * Created by Administrator on 2018/7/10.
 * 底层基础Fragment
 */

public abstract class BasicFragment2 extends Fragment {
    protected View view;
    protected BasicActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (BasicActivity) getActivity();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(getLayoutId(), container, false);
    }

    protected <T extends View> T findViewById(int id) {
        return view.findViewById(id);
    }

    public abstract int getLayoutId();

    //防多次重复操作
    private long lastClick = 0;

    protected boolean isFast() {
        if (System.currentTimeMillis() - lastClick <= 1000) {
            return false;
        }
        lastClick = System.currentTimeMillis();
        return true;
    }
}
