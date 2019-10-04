package com.base.base.activity;

import android.content.Intent;
import android.os.Bundle;

import com.base.R;

/**
 * Created by Administrator on 2018/7/10.
 * 关于activity跳转的方法可以写在这里
 */

public class JumpActivity extends PromptActivity {
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
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);

//        overridePendingTransition(R.anim.translate_start_in, R.anim.translate_start_out);
    }

    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
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
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
//        overridePendingTransition(R.anim.translate_start_in, R.anim.translate_start_out);
    }


    protected Bundle getBundle() {
        return getIntent().getExtras();
    }


    @Override
    public void finish() {
        super.finish();
//        overridePendingTransition(R.anim.translate_finish_in, R.anim.translate_finish_out);
    }

    private long mExitTime = 0;

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            if (System.currentTimeMillis() - mExitTime > 3000) {
                showToast("再按返回键退出程序!");
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
        } else
            super.onBackPressed();
    }

}
