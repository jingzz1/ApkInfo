package com.base.base.refreshView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.base.R;


public class DefaultFooter implements IRefreshFooter {
    private Context context;
    private TextView textView;

    public DefaultFooter(Context context) {
        this.context = context;
    }

    @Override
    public View getView() {
        View view = LayoutInflater.from(context).inflate(R.layout.default_footer_layout, null);
        textView = view.findViewById(R.id.text);
        return view;
    }

    @Override
    public void refreshing() {
        textView.setText("加载中……");
    }

    @Override
    public void refreshScrolling(int deltaY) {

    }

    @Override
    public void deltaLessHeight(int deltaY) {
        textView.setText("上拉加载更多");
    }

    @Override
    public void deltaMoreHeight(int deltaY) {
        textView.setText("释放加载更多");
    }

    @Override
    public void onComplete() {
        textView.setText("加载成功");
    }
}
