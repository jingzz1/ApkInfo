package com.base.base.slidingshut;

public interface SlidingShutChidren {
    /**
     * 是否能向右滑动
     * @param direction 滑动距离，<0 向右滑动，>0 向左滑动
     * @return
     */
    public boolean canScrollToRight(int direction);

}
