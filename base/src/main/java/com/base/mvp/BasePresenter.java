package com.base.mvp;

import android.app.Activity;
import android.content.Context;

import java.lang.ref.WeakReference;

public class BasePresenter<V extends IView> implements IPresenter<V>{
    protected Context mContext;
    protected WeakReference<V> vWeakReference;

    public BasePresenter(V view, Context context) {
        this.mContext = context;
        vWeakReference = getvWeakReference(view);
    }


    @Override
    public void bindPresenter(V view) {
        vWeakReference = getvWeakReference(view);
    }

    @Override
    public void unBindPresenter() {
        vWeakReference.clear();
        mContext = null;
    }

    public WeakReference<V> getvWeakReference(V view) {
        return new WeakReference<V>(view);
    }

    @Override
    public boolean isBind() {
        return vWeakReference != null && vWeakReference.get()!= null && mContext != null && !((Activity)mContext).isDestroyed();
    }
}
