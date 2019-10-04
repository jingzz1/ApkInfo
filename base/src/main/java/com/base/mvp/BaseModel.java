package com.base.mvp;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Administrator - PC on 2017/12/1.
 */

public abstract class BaseModel<T> implements IModel<T> {
    protected Bundle bundle = new Bundle();
    protected Context context;
    protected T data;
    protected Map<String,String> map = new LinkedHashMap<>();

    public BaseModel(Context context) {
        this.context = context;
    }

    public void params(@NonNull Bundle params) {
        if(bundle == null)
            bundle = new Bundle();

        if (params != null)
            bundle.putAll(params);
    }

    public void params(@NonNull Map<String,String> params) {
        if(map == null)
            map = new LinkedHashMap<>();
        if (params != null)
            map.putAll(params);
    }

    public void paramsClear() {
        if (bundle != null)
            bundle.clear();

    }

    public void clearMap(){
        if(map != null)
            map.clear();
    }

    public void params(String key,float value){
        if(bundle == null)
            bundle = new Bundle();
        bundle.putFloat(key,value);
        paramsMap(key,String.valueOf(value));
    }


    public void params(String key,long value){
        if(bundle == null)
            bundle = new Bundle();
        bundle.putLong(key,value);
        paramsMap(key,String.valueOf(value));
    }

    public void params(String key,double value){
        if(bundle == null)
            bundle = new Bundle();
        bundle.putDouble(key,value);
        paramsMap(key,String.valueOf(value));
    }

    public void params(String key,boolean value){
        if(bundle == null)
            bundle = new Bundle();
        bundle.putBoolean(key,value);
        paramsMap(key,String.valueOf(value));
    }

    public void params(String key,int value){
        if(bundle == null)
            bundle = new Bundle();
        bundle.putInt(key,value);
        paramsMap(key,String.valueOf(value));
    }

    public void params(String key,String value){
        if(bundle == null)
            bundle = new Bundle();
        bundle.putString(key,value);
        paramsMap(key,value);
    }

    private void paramsMap(String key,String value){
        if(map == null)
            map = new LinkedHashMap<>();
        map.put(key,value);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public T getData() {
        return data;
    }


    protected Type getType(){
        Type superclass = getClass().getGenericSuperclass();
        Type type = ((ParameterizedType)superclass).getActualTypeArguments()[0];
        return type;
    }


}
