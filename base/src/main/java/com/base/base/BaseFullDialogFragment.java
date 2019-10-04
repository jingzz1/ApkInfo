package com.base.base;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public abstract class BaseFullDialogFragment extends BaseDialogFragment {
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        super.onActivityCreated(savedInstanceState);
        //设置背景为透明
        window.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), android.R.color.transparent));
        int dialogHeight = getContextRect(getActivity());
        //设置弹窗大小为会屏
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
        //去除阴影
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.dimAmount = 0.0f;
        window.setAttributes(layoutParams);
    }

    //获取内容区域
    private int getContextRect(Activity activity){
        //应用区域
        Rect outRect1 = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect1);
        return outRect1.height();
    }
}
