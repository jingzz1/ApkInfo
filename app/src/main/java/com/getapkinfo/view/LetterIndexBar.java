package com.getapkinfo.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.getapkinfo.R;


/**
 * Created by Administrator on 2018/2/25.
 * 侧边字母索引
 * 需要索引的listView或recyclerView的adapter里面实现SectionIndexer
 * SectionIndexer用法参考：https://www.cnblogs.com/zhujiabin/p/5681063.html
 */

public class LetterIndexBar extends View {

    private float textSize = 0;
    private int textColor = Color.GRAY;
    private int selectTextColor;
    private Paint paint, paint2;
    private int selectIndex = -1;

    private TextView mPressedShowTextView;

    private String[] letters = {"*", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z", "#"};

    public LetterIndexBar(Context context) {
        this(context, null);
    }

    public LetterIndexBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterIndexBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        selectTextColor = ContextCompat.getColor(context, R.color.colorAccent);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LetterIndexBar);
        textSize = array.getDimension(R.styleable.LetterIndexBar_LIB_textSize, dip2px(context, 15));
        textColor = array.getColor(R.styleable.LetterIndexBar_LIB_textColor, textColor);
        selectTextColor = array.getColor(R.styleable.LetterIndexBar_LIB_selectTextColor, selectTextColor);
        array.recycle();

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        paint2 = new Paint(paint);
        paint2.setTextSize(textSize + 1);
        paint2.setColor(selectTextColor);
        paint2.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
    }

    public void setPressedShowTextView(TextView mPressedShowTextView) {
        this.mPressedShowTextView = mPressedShowTextView;
    }

    public void setLetters(String[] letters) {
        this.letters = letters;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        float maxWidth = 0.0f;

        for (String letter : letters) {
            maxWidth = Math.max(maxWidth, paint.measureText(letter));
        }
        int width = (int) (maxWidth) + getPaddingLeft() + getPaddingRight();

        if (widthMode == MeasureSpec.EXACTLY)
            width = MeasureSpec.getSize(widthMeasureSpec);

        Rect bounds = new Rect();
        paint.getTextBounds("A", 0, "A".length(), bounds);
        int heigth = bounds.height() * letters.length * 4 / 3;
        if (heightMode == MeasureSpec.EXACTLY)
            heigth = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, heigth);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int dy = (Math.abs(fontMetrics.bottom) + Math.abs(fontMetrics.top)) / 2 - Math.abs(fontMetrics.bottom);

        float letterHeigth = (getHeight() - getPaddingTop() - getPaddingBottom()) / letters.length;
        for (int i = 0; i < letters.length; i++) {
            float x = getWidth() / 2 - paint.measureText(letters[i]) / 2;
            float y = letterHeigth * i + letterHeigth / 2 + dy + getPaddingTop();
            if (selectIndex == i)
                canvas.drawText(letters[i], x, y, paint2);
            else
                canvas.drawText(letters[i], x, y, paint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {
            selectIndex = -1;
            if (onSelectChatListener != null) {
                onSelectChatListener.onSelectChar(null, false);
            }

            if (mPressedShowTextView != null) {
                mPressedShowTextView.setVisibility(View.GONE);
            }

            invalidate();
            return true;
        }
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
            float y = event.getY() - getPaddingTop();
            int index = (int) (y / ((getHeight() - getPaddingTop() - getPaddingBottom()) / letters.length));
            if (index != selectIndex) {
                selectIndex = index;
                if (onSelectChatListener != null) {
                    if (selectIndex < letters.length && selectIndex >= 0)
                        onSelectChatListener.onSelectChar(letters[selectIndex], true);
                }

                if (mPressedShowTextView != null && selectIndex < letters.length) { //显示hintTexView
                    mPressedShowTextView.setVisibility(View.VISIBLE);
                    mPressedShowTextView.setText(letters[selectIndex]);
                }

                invalidate();


            }
            return true;
        }
        return true;
    }

    /**
     * 根据手机分辨率从DP转成PX
     *
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 获取屏幕尺寸
     *
     * @return
     */
    private DisplayMetrics getScreen() {
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
//        float density = dm.density;
//        int width = dm.widthPixels;
//        int height = dm.heightPixels;
        return dm;

    }

    private OnSelectCharListener onSelectChatListener;

    public void setOnSelectChatListener(OnSelectCharListener onSelectChatListener) {
        this.onSelectChatListener = onSelectChatListener;
    }

    public interface OnSelectCharListener {
        public void onSelectChar(String char1, boolean isTouch);
    }

    public interface SectionIndexer {
        public int indexFromTag(String tag);
    }

}
