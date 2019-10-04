package com.base.base.refreshView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class RefreshView extends LinearLayout {

    private IRefreshHeader refreshHeader;
    private IRefreshFooter refreshFooter;

    private View mHeaderView;
    private View mFooterView;

    private boolean isCanRefreshHeader = true;
    private boolean isCanRefreshfooter = true;

    private int startX = 0;
    private int startY = 0;
    private List<View> downView = new ArrayList<>();

    private int deltaY = 0;
    private boolean isRefreshHeadering = false;
    private boolean isRefreshfootering = false;

    private ValueAnimator headerCompleteAnimator;

    public RefreshView(Context context) {
        this(context, null);
    }

    public RefreshView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public RefreshView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        refreshHeader = new DefaultHeader(getContext());
        refreshFooter = new DefaultFooter(getContext());
        //内容方向设置为垂直
        setOrientation(LinearLayout.VERTICAL);
    }

    //加载完xml布局，在这里把header和footer加进父容器里
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        addHeaerView();
        addFooterView();
    }

    public void setCanRefreshHeader(boolean canRefreshHeader) {
        isCanRefreshHeader = canRefreshHeader;
    }

    public void setCanRefreshfooter(boolean canRefreshfooter) {
        isCanRefreshfooter = canRefreshfooter;
    }

    private void addHeaerView() {
        if (refreshHeader == null)
            throw new RuntimeException("refreshHeader 不能为null");
        if (mHeaderView != null)
            removeView(mHeaderView);
        mHeaderView = refreshHeader.getView();
        if (mHeaderView == null)
            throw new RuntimeException("refreshHeader.getView() 不能为null");

        measureView(mHeaderView);
        int height = mHeaderView.getMeasuredHeight();
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, height);
        params.topMargin = -height;
        addView(mHeaderView, 0, params);
    }

    private void addFooterView() {
        if (refreshFooter == null)
            return;

        View footerView = refreshFooter.getView();
        if (footerView == null)
            return;

        removeView(mFooterView);
        mFooterView = footerView;
        measureView(mFooterView);
        addView(mFooterView, new LayoutParams(LayoutParams.MATCH_PARENT, mFooterView.getMeasuredHeight()));
    }


    //重新测量子控件
    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    child.getMeasuredHeight());
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    public void setRefreshHeader(IRefreshHeader refreshHeader) {
        this.refreshHeader = refreshHeader;
        addHeaerView();
    }

    public void setRefreshFooter(IRefreshFooter refreshFooter) {
        this.refreshFooter = refreshFooter;
        addFooterView();
    }

    //需要在这里判断是否滑动
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int y = (int) ev.getY();
        int x = (int) ev.getX();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downView.clear();
                addDownPager(this, x, y);
                startY = y;
                startX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = startX - x;
                int deltaY = startY - y;
                //判断是否上下滑动
                if (Math.abs(deltaY) > Math.abs(deltaX)) {
                    //判断头部或尾部是否加载中，是的话不拦截
                    if (isRefreshHeadering || isRefreshfootering)
                        return false;

                    //判断是否拦截子控件,如果子控件可以滑动，false
                    return isRefreshViewScroll(deltaY);
                }
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                deltaY = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                //获取获取距离，为了让滑动操作有阻尼效果，滑动距离设置为实际滑动的1/3
                deltaY = (int) (startY - ev.getY()) / 3;
                if (deltaY < 0) {
                    //下滑
                    headerPrepareToRefresh(deltaY);
                } else {
                    //上滑
                    footerPrepareToRefresh(deltaY);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //执行释放操作
                if (deltaY < 0) {
                    //下滑
                    isRefreshHeadering = true;
                    int absY = Math.abs(deltaY);
                    int height = mHeaderView.getMeasuredHeight();
                    if (height > absY) {
                        //滑动中到一半，释放，回复原样
                        headerComplete();
                    } else if (height < absY) {
                        //滑动距离超过headerView的高度
                        refreshHeader.headerRefreshing();
                        if (headerRefreshListener != null)
                            headerRefreshListener.onHeaderRefresh(this);

                        //收起超过view高度的距离
                        LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
                        headerCompleteAnimator(params.topMargin, 0, false);
                    }
                } else if (deltaY > 0) {
                    //上滑
                    isRefreshfootering = true;
                    int height = mFooterView.getMeasuredHeight();
                    if (deltaY >= height) {
                        //开始上拉加载
                        refreshFooter.refreshing();
                        if (footerRefreshListener != null)
                            footerRefreshListener.onFooterRefresh(this);
                    } else {
                        //复原
                        footerComplete();
                    }

                }
                break;
        }

        return super.onTouchEvent(ev);
    }


    //执行上拉，方法也是改变headerView的topMargin
    private void footerPrepareToRefresh(int deltaY) {
        //deltaY肯定是正数
        int footerHeight = mFooterView.getMeasuredHeight();
        if (deltaY > footerHeight) {
            //滑动距离超过footerView高度后，不再执行操作
            return;
        }
        LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
        int height = mHeaderView.getMeasuredHeight();
        params.topMargin = -height - deltaY;
        mHeaderView.setLayoutParams(params);
        refreshFooter.refreshScrolling(deltaY);
        if (deltaY < height - 10)
            refreshFooter.deltaLessHeight(deltaY);
        else
            refreshFooter.deltaMoreHeight(deltaY);

    }

    //执行下拉加载：方法为改变headerView的topMargin
    private void headerPrepareToRefresh(int deltaY) {
        //注意，这里的deltaY肯定小于0，所以判断滑动距离时，一定要先获取绝对值

        LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
        int height = mHeaderView.getMeasuredHeight();
        params.topMargin = -height - deltaY;
        mHeaderView.setLayoutParams(params);
        int delta = Math.abs(deltaY);
        refreshHeader.refreshScrolling(delta);
        if (height > delta) {
            //滑动中 释放不做任何操作
            refreshHeader.deltaLessHeight(delta);
        } else if (height < delta) {
            //滑动距离超过headerView的高度
            refreshHeader.deltaMoreHeight(delta);
        } else {
            //滑动到等于view的高度
            refreshHeader.deltaMoreHeight(Math.abs(deltaY));
        }

    }


    //判断是否拦截子控件
    private boolean isRefreshViewScroll(int deltaY) {
        //deltaY >0 向上滑动 <0 向下滑动
        if (deltaY < 0 && !isCanRefreshHeader)
            return false;
        else if (deltaY > 0 && !isCanRefreshfooter)
            return false;

        if (downView.size() <= 0)
            return true;

        //这里需要判断子控件是否能滑动，能滑动的话，false
        //遍例所有在点击位置的子控件，有一个可以滑动，就返回false
        for (View view : downView) {
            if (view.canScrollVertically(deltaY))
                return false;
        }
        return true;
    }

    //收起headerView
    public void headerComplete() {
        int height = mHeaderView.getMeasuredHeight();
        LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
        refreshHeader.headerComplete();
        //执行动画关闭
        headerCompleteAnimator(params.topMargin, -height, false);
    }

    //收起footer
    public void footerComplete() {
        refreshFooter.onComplete();
        int height1 = mHeaderView.getMeasuredHeight();
        LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
        headerCompleteAnimator(params.topMargin, -height1, true);
    }


    //收起header动画
    private void headerCompleteAnimator(int start, int end, boolean isRefreshfooter) {
        if (headerCompleteAnimator != null && headerCompleteAnimator.isRunning()) {
            if (isRefreshfooter)
                isRefreshfootering = false;
            if (end != 0)
                isRefreshHeadering = false;
            return;
        }

        headerCompleteAnimator = ObjectAnimator.ofInt(start, end);
        headerCompleteAnimator.setDuration(300);
        headerCompleteAnimator.setInterpolator(new OvershootInterpolator());
        headerCompleteAnimator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
            params.topMargin = value;
            mHeaderView.setLayoutParams(params);
        });
        headerCompleteAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (end != 0)
                    isRefreshHeadering = false;
                if (isRefreshfooter)
                    isRefreshfootering = false;
            }
        });
        headerCompleteAnimator.start();

    }

    public void openHeader() {
        ValueAnimator animator = ObjectAnimator.ofInt(0, -mHeaderView.getMeasuredHeight());
        animator.setDuration(300);
        animator.addUpdateListener(valueAnimator -> {
            int values = (int) valueAnimator.getAnimatedValue();
            headerPrepareToRefresh(values);
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                refreshHeader.headerRefreshing();
                if (headerRefreshListener != null)
                    headerRefreshListener.onHeaderRefresh(RefreshView.this);
            }
        });
        animator.start();
    }


    //获取点击位置的view
    private void addDownPager(ViewGroup viewGroup, int x, int y) {
        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = viewGroup.getChildAt(i);
            if (isTouchPointInView(child, x, y)) {
                downView.add(child);
                if (child instanceof ViewGroup)
                    addDownPager((ViewGroup) child, x, y);
            }
        }
    }

    //判断触点是否在view上
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

    private OnHeaderRefreshListener headerRefreshListener;
    private OnFooterRefreshListener footerRefreshListener;

    public void setHeaderRefreshListener(OnHeaderRefreshListener headerRefreshListener) {
        this.headerRefreshListener = headerRefreshListener;
    }

    public void setFooterRefreshListener(OnFooterRefreshListener footerRefreshListener) {
        this.footerRefreshListener = footerRefreshListener;
    }

    /**
     * 脚部监听器
     */
    public interface OnFooterRefreshListener {
        public void onFooterRefresh(RefreshView refreshView);
    }

    /**
     * 头部监听器
     */
    public interface OnHeaderRefreshListener {
        public void onHeaderRefresh(RefreshView refreshView);
    }
}




























