package com.getapkinfo.base;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.getapkinfo.BuildConfig;

public class BaseApplication extends Application {

    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = context;
        LogUtils.getConfig().setLogSwitch(BuildConfig.DEBUG);

    }
}
