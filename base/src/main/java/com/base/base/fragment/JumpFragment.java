package com.base.base.fragment;

import android.content.Intent;
import android.os.Bundle;

import com.base.R;

/**
 * Created by Administrator on 2018/7/10.
 * 跳转层，封装了跳转方法
 */

public abstract class JumpFragment extends PromptFragment {
    public void startActivity(Class<?> clz) {
        startActivity(clz, null);
    }

    /**
     * 携带数据的页面跳转
     *
     * @param clz
     * @param bundle
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(activity, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
//        activity_information.overridePendingTransition(R.anim.translate_start_in, R.anim.translate_start_out);;
    }

    /**
     * 含有Bundle通过Class打开编辑界面
     *
     * @param cls
     * @param bundle
     * @param requestCode
     */
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }

        startActivityForResult(intent, requestCode);
//        activity_information.overridePendingTransition(R.anim.translate_start_in,R.anim.translate_start_out);;
    }
}
