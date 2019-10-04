package com.getapkinfo.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.AppUtils;
import com.getapkinfo.R;
import com.getapkinfo.activity.AppDetailsActivity;
import com.getapkinfo.adapter.AppInofsAdapter;
import com.getapkinfo.base.BaseFragment;
import com.getapkinfo.bean.AppBean;
import com.getapkinfo.java.RecyclerViewScrollHelper;
import com.getapkinfo.java.TitleItemDecoration;
import com.getapkinfo.view.LetterIndexBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ApkListFragment extends BaseFragment {
    private boolean isSystem = false;
    private RecyclerView mRecyclerView;
    private AppInofsAdapter adapter;
    private ProgressDialog dialog;
    private TitleItemDecoration decoration;
    private LetterIndexBar indexBar;
    private TextView mTvTab;

    public static ApkListFragment newInstance(boolean isSystem) {
        ApkListFragment fragment = new ApkListFragment();
        Bundle args = new Bundle();
        args.putBoolean("isSystem", isSystem);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isSystem = getArguments().getBoolean("isSystem", isSystem);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_apk_list;
    }


    @Override
    protected void initStart(View view, @Nullable Bundle savedInstanceState) {
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("正在初始化应用");
        dialog.setCancelable(false);

        mTvTab = findViewById(R.id.tab);
        mTvTab.setVisibility(View.GONE);
        mTvTab.setAlpha(0.7f);

        indexBar = findViewById(R.id.indexBar);
        indexBar.setOnSelectChatListener((char1, isTouch) -> {
            if(isTouch){
                int index = adapter.indexFromTag(char1);
                if(index != -1)
                    RecyclerViewScrollHelper.scrollToPosition(mRecyclerView,index);
            }
            mTvTab.setText(char1);
            mTvTab.setVisibility(isTouch?View.VISIBLE:View.GONE);
        });

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AppInofsAdapter();
        mRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((adapter1, view1, position) -> {
            AppBean bean = adapter.getData().get(position);
            Bundle b = new Bundle();
            b.putParcelable("bean",bean);
            startActivity(AppDetailsActivity.class,b);
        });

    }

    @Override
    protected void loadData() {
        initApkInfo();
    }

    private void initApkInfo() {
        dialog.show();
        Observable.create((ObservableOnSubscribe<List<AppBean>>) e -> {
            List<AppBean> apps = new ArrayList<>();
            List<AppUtils.AppInfo> appsInfo = AppUtils.getAppsInfo();
            for (AppUtils.AppInfo a : appsInfo){
                if(a.isSystem() == isSystem){
                    apps.add(new AppBean(a));
                }
            }
            Collections.sort(apps);
            e.onNext(apps);
            e.onComplete();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<AppBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(List<AppBean> appInfos) {
                        adapter.setNewData(appInfos);
                        if(decoration != null)
                            mRecyclerView.removeItemDecoration(decoration);
                        decoration = new TitleItemDecoration(getContext(),appInfos);
                        mRecyclerView.addItemDecoration(decoration);
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        dialog.dismiss();
                    }
                });
    }
}




































