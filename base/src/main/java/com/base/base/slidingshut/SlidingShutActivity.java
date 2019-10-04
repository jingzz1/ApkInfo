package com.base.base.slidingshut;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.base.base.activity.BasicActivity;
import com.base.base.activity.JumpActivity;

/**
 * 侧滑退出的activity
 */
public class SlidingShutActivity extends BasicActivity {

    protected SlidingShutManager slidingShutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        slidingShutManager = new SlidingShutManager(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        slidingShutManager = new SlidingShutManager(this);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        slidingShutManager = new SlidingShutManager(this);
    }

    /**
     * 绑定手势
     */
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (null != slidingShutManager && slidingShutManager.motionEvent(ev)) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 开启滑动关闭界面
     *
     * @param open
     */
    protected void openSlideFinish(boolean open) {
        if (slidingShutManager == null) {
            return;
        }
        slidingShutManager.openSlideFinish(open);
    }

    /**
     * 抬起关闭
     *
     * @param upFinish 【true：手指抬起后再关闭页面】
     *                 【false：进度条圆满就立刻关闭页面】
     */
    public void setUpFinish(boolean upFinish) {
        if (slidingShutManager == null) {
            return;
        }
        slidingShutManager.setUpFinish(upFinish);
    }

    /**
     * 设置进度条颜色
     */
    public void setProgressColor(int color) {
        if (slidingShutManager != null)
            slidingShutManager.setProgressColor(color);
    }

    /**
     * 设置拦截比例
     * @param slideScreenWidthScale
     */
    public void setSlideScreenWidthScale(int slideScreenWidthScale){
        if (slidingShutManager != null)
            slidingShutManager.setSlideScreenWidthScale(slideScreenWidthScale);
    }

    /**
     * 滑动View
     * 【滑动过程中会移动的View】
     */
    public void setMoveView(View SlideView) {
        slidingShutManager.setRootView(SlideView);
    }

}
