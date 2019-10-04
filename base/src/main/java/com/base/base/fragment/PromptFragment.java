package com.base.base.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.base.base.activity.BasicActivity;

/**
 * Created by Administrator on 2018/7/10.
 * 提示层，封装了toast等提示
 */

public abstract class PromptFragment extends RxJavaFragment {
    protected BasicActivity activity;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (BasicActivity) getActivity();
    }

    public void showToast(String text){
        activity.showToast(text);
    }

    public void showLongToast(String text){
       activity.showLongToast(text);
    }

    public void showToast(String text, int duration) {
       activity.showToast(text,duration);

    }

    public void showSnckbar(String text){
        showSnckbar(text,null,null);
    }

    public void showSnckbar(String text,String actionText,View.OnClickListener listener){
        activity.showSnckbar(text,actionText,listener);
    }

    public void showSnckbar(String text,int textColor,int backColor
            ,String actionText, View.OnClickListener listener,int actionTextColor) {

     activity.showSnckbar(text,textColor,backColor,actionText,listener,actionTextColor);

    }

    protected void showAlerter(String text) {
        showAlerter("提示", text);
    }

    protected void showAlerter(String title, String text) {
        activity.showAlerter(title,text);
    }

    protected void finishShow(String text){
//        SnackBarHelper.finishShow(text);
       activity.finishToast(text);
    }
}
