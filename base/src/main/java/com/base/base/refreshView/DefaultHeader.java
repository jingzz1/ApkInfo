package com.base.base.refreshView;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DefaultHeader implements IRefreshHeader {

    private static final int complete_state = 0;
    private static final int less_state = 1;
    private static final int more_state = 2;
    private static final int refresh_state = 3;
    public int state = complete_state;

    private Context context;
    private TextView tv_message,tv_time;
    private ImageView iv_arrow;
    private CircularLinesProgress progress;

    private SharedPreferences sp;


    public DefaultHeader(Context context) {
        this.context = context;
        sp = context.getSharedPreferences("time", Context.MODE_PRIVATE);
    }

    @Override
    public View getView() {
        View view = LayoutInflater.from(context).inflate(R.layout.default_header_layout, null);
        tv_message = view.findViewById(R.id.message);
        tv_time = view.findViewById(R.id.time);
        iv_arrow = view.findViewById(R.id.arrow);
        progress = view.findViewById(R.id.progress);
        return view;
    }

    @Override
    public void refreshScrolling(int deltaY) {

    }

    @Override
    public void deltaLessHeight(int deltaY) {
        if(state != less_state){
            state = less_state;
            progress.setVisibility(View.INVISIBLE);
            iv_arrow.setVisibility(View.VISIBLE);
            iv_arrow.animate().rotation(0).setDuration(200).start();
            tv_time.setText(getTimeText());
            tv_message.setText("下拉刷新数据");
        }
    }

    @Override
    public void deltaMoreHeight(int deltaY) {
        if(state != more_state){
            state = more_state;
            progress.setVisibility(View.INVISIBLE);
            iv_arrow.setVisibility(View.VISIBLE);
            iv_arrow.animate().rotation(180).setDuration(200).start();
            tv_time.setText(getTimeText());
            tv_message.setText("释放立即刷新");
        }
    }

    @Override
    public void headerRefreshing() {
        if(state != refresh_state){
            state = refresh_state;
            tv_message.setText("正在刷新…");
            iv_arrow.setVisibility(View.INVISIBLE);
            progress.setVisibility(View.VISIBLE);
            progress.start();
        }

    }

    @Override
    public void headerComplete() {
        state = complete_state;
        progress.cancel();

        tv_message.setText("刷新完成");
        long time = System.currentTimeMillis();
        SharedPreferences.Editor edit = sp.edit();
        edit.putLong("time",time);
        edit.apply();
    }

    private String getTimeText(){
        long time = sp.getLong("time", System.currentTimeMillis());
        return "上次更新时间："+getTime(time);
    }

    //返回时间
    public  String getTime(Long time) {//可根据需要自行截取数据显示
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
        return format.format(date);
    }

}
