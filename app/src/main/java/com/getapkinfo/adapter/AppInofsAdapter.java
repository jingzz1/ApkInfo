package com.getapkinfo.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.getapkinfo.R;
import com.getapkinfo.bean.AppBean;
import com.getapkinfo.view.LetterIndexBar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AppInofsAdapter extends BaseQuickAdapter<AppBean, BaseViewHolder> implements LetterIndexBar.SectionIndexer {

    private Map<String, Integer> indexs = new HashMap<>();

    public AppInofsAdapter() {
        super(R.layout.main_appinfo_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, AppBean item) {
        helper.setText(R.id.appName, item.getName())
                .setText(R.id.packname, item.getPackageName())
                .setText(R.id.versionName, item.getVersionName())
                .setImageBitmap(R.id.imageView, item.getIcon());
    }

    @Override
    public void setNewData(@Nullable List<AppBean> data) {
        super.setNewData(data);

        Observable.create(new ObservableOnSubscribe<Map<String, Integer>>() {
            @Override
            public void subscribe(ObservableEmitter<Map<String, Integer>> emitter) throws Exception {
                Map<String,Integer> map = new HashMap<>();
                for (int i = 0; i < data.size();i++){
                    AppBean appBaen = data.get(i);
                    if(map.get(appBaen.getTag())==null)
                        map.put(appBaen.getTag(),i);
                }

                emitter.onNext(map);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Map<String, Integer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Map<String, Integer> map) {
                        indexs = map;
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public int indexFromTag(String tag) {
        Integer integer = indexs.get(tag);
        if(integer == null)
            return  -1;
        return indexs.get(tag);
    }
}
