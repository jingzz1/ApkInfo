package com.base.base.activity;


/**
 * Created by Administrator - PC on 2018/1/5.
 * 基类activity，需要其他什么方法，在这里添加
 */

public class BasicActivity extends JumpActivity {
    //防多次点击
    private long lastClick = 0;

    public boolean isFast() {
        if (System.currentTimeMillis() - lastClick <= 1000) {
            return false;
        }
        lastClick = System.currentTimeMillis();
        return true;
    }
}
