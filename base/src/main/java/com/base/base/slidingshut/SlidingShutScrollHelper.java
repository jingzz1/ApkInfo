package com.base.base.slidingshut;

import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class SlidingShutScrollHelper {
    private List<View> scrollViews = new ArrayList<>();
    private View rootView;

    /**
     * 绑定rootView
     *
     * @param rootView
     */
    public void setRootView(View rootView) {
        this.rootView = rootView;
    }

    /**
     * 手指按下的位置
     *
     * @param x
     * @param y 获取手指按下位置的所有子控件，筛选出所有可以左右滑动的子控件
     */
    public void onDown(int x, int y) {
        if (scrollViews == null)
            scrollViews = new ArrayList<>();
        scrollViews.clear();
        initViewGroup(rootView, x, y);
    }

    /**
     * 筛选出触点上的所有 HorizontalScrollView、ViewPager、RecyclerView、SlidingShutChidren
     *
     * @param view
     * @param x
     * @param y
     */
    private void initViewGroup(View view, int x, int y) {
        if (!isTouchPointInView(view, x, y))
            return;
        if (view instanceof HorizontalScrollView) {
            scrollViews.add(view);
        } else if (view instanceof ViewPager) {
            scrollViews.add(view);
        } else if (view instanceof RecyclerView) {
            scrollViews.add(view);
        } else if (view instanceof SlidingShutChidren) {
            scrollViews.add(view);
        }
        //如果该控件是一个ViewGroup，需要继续获取下行的所有子控件
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            int count = group.getChildCount();
            for (int i = 0; i < count; i++)
                initViewGroup(group.getChildAt(i), x, y);
        }

    }

    /**
     * 判断控件是否在指定位置上
     *
     * @param view
     * @param x
     * @param y
     * @return
     */
    private boolean isTouchPointInView(View view, int x, int y) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        if (y >= top && y <= bottom && x >= left
                && x <= right) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否执行右滑退出(判断子控件是否能向右滑动，可以的话，则不执行右滑退出，即不拦截子控件的滑动操作)
     *
     * @param direction 滑动距离,<0 向右滑动，>0 向左滑动
     * @return true 执行右滑退出
     */
    public boolean canScroll(int direction) {
        if (scrollViews == null || scrollViews.size() <= 0)
            return true;
        boolean canScroll = true;
        for (View view : scrollViews) {
            //如果控件是viewPager或HorizontalScrollView的话比较简单，判断canScrollHorizontally就可以
            if (view instanceof ViewPager) {
                ViewPager viewPager = (ViewPager) view;
                canScroll = canScroll && !viewPager.canScrollHorizontally(direction);
            } else if (view instanceof HorizontalScrollView) {
                HorizontalScrollView scrollView = (HorizontalScrollView) view;
                canScroll = canScroll && !scrollView.canScrollHorizontally(direction);
            } else if (view instanceof RecyclerView) {
                //如果子控件是recyclerView，则需要满足四个条件才拦截
                //1、direction < 0 (不是右滑，没必要拦截)
                //2、LinearLayoutManager.HORIZONTAL，即水平滑动(垂直滑动没必要拦截)
                //3、第一个条目可见,即findFirstVisibleItemPosition==0(第一个条目不可见的话，说明recyclerView可以右滑，这时候不应该拦截recyclerView的滑动)
                //4、NestedScrollingEnabled == true(recyclerView本身需要能滑动才需要判断是否拦截)

                boolean canRecyclerViewScroll = true;
                RecyclerView recyclerView = (RecyclerView) view;
                RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                if (direction < 0) {
                    if (manager instanceof LinearLayoutManager) {
                        LinearLayoutManager manager1 = (LinearLayoutManager) manager;
                        int position = manager1.findFirstVisibleItemPosition();
                        boolean isHorizontal = manager1.getOrientation() == LinearLayoutManager.HORIZONTAL;
                        boolean scrollingEnabled = recyclerView.isNestedScrollingEnabled();
                        if(isHorizontal){
                            if (position == 0 && scrollingEnabled && isHorizontal)
                                canRecyclerViewScroll = false;
                        }else
                            canRecyclerViewScroll = false;
                    }
                }
                canScroll = canScroll && !canRecyclerViewScroll;
            } else if (view instanceof SlidingShutChidren) {
                //SlidingShutChidren 用户自定义控件
                //自定义控件实现SlidingShutChidren接口，自行判断要不要拦截
                SlidingShutChidren slidingShutChidren = (SlidingShutChidren) view;
                canScroll = canScroll && !slidingShutChidren.canScrollToRight(direction);
            }
        }
        return canScroll;
    }


}
