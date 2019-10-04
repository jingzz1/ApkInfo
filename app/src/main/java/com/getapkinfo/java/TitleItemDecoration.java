package com.getapkinfo.java;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;


import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getapkinfo.R;
import com.github.promeg.pinyinhelper.Pinyin;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 有分类title的 ItemDecoration
 * Created by jingzz .
 */

public class TitleItemDecoration extends RecyclerView.ItemDecoration {
    private List<? extends TagBean> mDatas;
    private Paint mPaint;
    private Rect mBounds;//用于存放测量文字Rect

    private int mTitleHeight;//title的高
    private static int COLOR_TITLE_BG = Color.parseColor("#FFDFDFDF");
    private static int COLOR_TITLE_FONT = Color.parseColor("#FF000000");
    private static int mTitleFontSize;//title字体大小
    private Context context;
    private int width;


    public TitleItemDecoration(Context context, List<? extends TagBean> datas, int title_bg_color, int title_font_color) {
        super();
        this.context = context;
        mDatas = datas;
        mPaint = new Paint();
        mBounds = new Rect();
        mTitleHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, context.getResources().getDisplayMetrics());
        mTitleFontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, context.getResources().getDisplayMetrics());
        mPaint.setTextSize(mTitleFontSize);
        mPaint.setAntiAlias(true);
        COLOR_TITLE_BG = title_bg_color;
        COLOR_TITLE_FONT = title_font_color;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);
        width = size.x;
    }

    public TitleItemDecoration(Context context, List<? extends TagBean> datas) {
        this(context, datas, ContextCompat.getColor(context, android.R.color.white), ContextCompat.getColor(context, R.color.gray33));
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            int position = params.getViewLayoutPosition();
            //我记得Rv的item position在重置时可能为-1.保险点判断一下吧
            if (position > -1) {
                if (position == 0) {//等于0肯定要有title的
                    drawTitleArea(c, left, right, child, params, position);

                } else {//其他的通过判断
                    if (position < mDatas.size() && null != mDatas.get(position).getTag() && !mDatas.get(position).getTag().equals(mDatas.get(position - 1).getTag())) {
                        //不为空 且跟前一个tag不一样了，说明是新的分类，也要title
                        drawTitleArea(c, left, right, child, params, position);
                    } else {
                        //none
                    }
                }
            }
        }
    }

    /**
     * 绘制Title区域背景和文字的方法
     *
     * @param c
     * @param left
     * @param right
     * @param child
     * @param params
     * @param position
     */
    private void drawTitleArea(Canvas c, int left, int right, View child, RecyclerView.LayoutParams params, int position) {//最先调用，绘制在最下层
/*
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;*/
        if (mDatas != null &&position < mDatas.size()) {
            mPaint.setColor(COLOR_TITLE_BG);
            c.drawRect(left, child.getTop() - params.topMargin - mTitleHeight, right, child.getTop() - params.topMargin, mPaint);
            mPaint.setColor(COLOR_TITLE_FONT);
            mPaint.getTextBounds(mDatas.get(position).getTag(), 0, mDatas.get(position).getTag().length(), mBounds);
            c.drawText(mDatas.get(position).getTag(), width / 50, child.getTop() - params.topMargin - (mTitleHeight / 2 - mBounds.height() / 2), mPaint);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {//最后调用 绘制在最上层
        int pos = ((LinearLayoutManager) (parent.getLayoutManager())).findFirstVisibleItemPosition();

        if (pos > -1 && pos < mDatas.size()) {
            String tag = mDatas.get(pos).getTag();
            //View child = parent.getChildAt(pos);
            View child = parent.findViewHolderForLayoutPosition(pos).itemView;//出现一个奇怪的bug，有时候child为空，所以将 child = parent.getChildAt(i)。-》 parent.findViewHolderForLayoutPosition(pos).itemView

            boolean flag = false;//定义一个flag，Canvas是否位移过的标志
            if ((pos + 1) < mDatas.size()) {//防止数组越界（一般情况不会出现）
                if (null != tag && !tag.equals(mDatas.get(pos + 1).getTag())) {//当前第一个可见的Item的tag，不等于其后一个item的tag，说明悬浮的View要切换了
                    if (child.getHeight() + child.getTop() < mTitleHeight) {//当第一个可见的item在屏幕中还剩的高度小于title区域的高度时，我们也该开始做悬浮Title的“交换动画”
                        c.save();//每次绘制前 保存当前Canvas状态，
                        flag = true;

                        //一种头部折叠起来的视效，个人觉得也还不错~
                        //可与123行 c.drawRect 比较，只有bottom参数不一样，由于 child.getHeight() + child.getTop() < mTitleHeight，所以绘制区域是在不断的减小，有种折叠起来的感觉
                        //c.clipRect(parent.getPaddingLeft(), parent.getPaddingTop(), parent.getRight() - parent.getPaddingRight(), parent.getPaddingTop() + child.getHeight() + child.getTop());

                        //类似饿了么点餐时,商品列表的悬停头部切换“动画效果”
                        //上滑时，将canvas上移 （y为负数） ,所以后面canvas 画出来的Rect和Text都上移了，有种切换的“动画”感觉
                        c.translate(0, child.getHeight() + child.getTop() - mTitleHeight);
                    }
                }
            }
            mPaint.setColor(COLOR_TITLE_BG);
            c.drawRect(parent.getPaddingLeft(), parent.getPaddingTop(), parent.getRight() - parent.getPaddingRight(), parent.getPaddingTop() + mTitleHeight, mPaint);
            mPaint.setColor(COLOR_TITLE_FONT);
            mPaint.getTextBounds(tag, 0, tag.length(), mBounds);
            c.drawText(tag, width / 50,
                    parent.getPaddingTop() + mTitleHeight - (mTitleHeight / 2 - mBounds.height() / 2),
                    mPaint);
            if (flag)
                c.restore();//恢复画布到之前保存的状态

        }


    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        //我记得Rv的item position在重置时可能为-1.保险点判断一下吧
        if (position > -1 && mDatas != null) {
            if (position < mDatas.size()) {
                if (position == 0) {//等于0肯定要有title的
                    outRect.set(0, mTitleHeight, 0, 0);
                } else {//其他的通过判断
                    if (null != mDatas.get(position).getTag() && !mDatas.get(position).getTag().equals(mDatas.get(position - 1).getTag())) {
                        outRect.set(0, mTitleHeight, 0, 0);//不为空 且跟前一个tag不一样了，说明是新的分类，也要title
                    } else {
                        outRect.set(0, 0, 0, 0);
                    }
                }
            }
        }
    }

    public static interface TagBean {
        public String getTag();
    }

    /**
     * tab协助方法，传入字符串，返回拼音或英文首字母
     *
     * @param tabText
     * @return
     */
    public static String tagHelper(String tabText) {
        if (!TextUtils.isEmpty(tabText)) {
            char c = tabText.charAt(0);
            if (isInteger(c))
                return "#";
            else{
                String s = String.valueOf(Pinyin.toPinyin(c).charAt(0)).toUpperCase();
                return isSpecialChar(s)?s:"*";
            }
        }
        return "☆";
    }

    /**
     * tab协助方法，传入两个字符窜，判断先后顺序
     * @param tab1
     * @param tab2
     * @return
     */
    public static int compareHelper(String tab1,String tab2){
        int c1 = tab1.charAt(0);
        int c2 = tab2.charAt(0);

        if(c1 == '#')
            c1 = 0;
        if(c2 == '#')
            c2 = 0;
        if(c1 == '*')
            c1 = Integer.MAX_VALUE;
        if(c2 == '*')
            c2 = Integer.MAX_VALUE;
        return c1-c2;
    }

    /**
     * 判断字符是否数字
     *
     * @param c
     * @return
     */
    public static boolean isInteger(char c) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(String.valueOf(c)).matches();
    }

    /**
     * 字符是字母
     * @param c
     * @return
     */
    public static boolean isSpecialChar(String c) {

        Pattern pattern = Pattern.compile("^[-\\+]?[A-z]*$");
        return pattern.matcher(c).matches();
    }

}
