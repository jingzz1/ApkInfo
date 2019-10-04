package com.base.base.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.base.mvp.BasePresenter;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2018/7/10.
 * 集成rxjava方法，rxjava公共方法可以在这里添加
 */

public abstract class RxJavaFragment extends BasicFragment2 {


    protected CompositeDisposable disposableDis;

    public void addDisposable(Disposable mDisposable) {
        if (disposableDis == null) {
            disposableDis = new CompositeDisposable();
        }
        disposableDis.add(mDisposable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(disposableDis != null)
        disposableDis.clear();
    }
}
