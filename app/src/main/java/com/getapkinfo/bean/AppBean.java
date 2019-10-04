package com.getapkinfo.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.getapkinfo.java.TitleItemDecoration;

import java.util.regex.Pattern;

public class AppBean implements Parcelable,Comparable<AppBean> , TitleItemDecoration.TagBean{
    private String   packageName;
    private String   name;
    private Bitmap icon;
    private String   packagePath;
    private String   versionName;
    private int      versionCode;
    private boolean  isSystem;

    public AppBean(AppUtils.AppInfo a) {
        packageName = a.getPackageName();
        name = a.getName();
        icon = ConvertUtils.drawable2Bitmap(a.getIcon());
        packagePath = a.getPackagePath();
        versionName = a.getVersionName();
        versionCode = a.getVersionCode();
        isSystem = a.isSystem();

    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public String getPackagePath() {
        return packagePath;
    }

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }

    @Override
    public String toString() {
        return "AppBean{" +
                "packageName='" + packageName + '\'' +
                ", name='" + name + '\'' +
                ", icon=" + icon +
                ", packagePath='" + packagePath + '\'' +
                ", versionName='" + versionName + '\'' +
                ", versionCode=" + versionCode +
                ", isSystem=" + isSystem +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.packageName);
        dest.writeString(this.name);
        dest.writeParcelable(this.icon, flags);
        dest.writeString(this.packagePath);
        dest.writeString(this.versionName);
        dest.writeInt(this.versionCode);
        dest.writeByte(this.isSystem ? (byte) 1 : (byte) 0);
    }

    protected AppBean(Parcel in) {
        this.packageName = in.readString();
        this.name = in.readString();
        this.icon = in.readParcelable(Bitmap.class.getClassLoader());
        this.packagePath = in.readString();
        this.versionName = in.readString();
        this.versionCode = in.readInt();
        this.isSystem = in.readByte() != 0;
    }

    public static final Creator<AppBean> CREATOR = new Creator<AppBean>() {
        @Override
        public AppBean createFromParcel(Parcel source) {
            return new AppBean(source);
        }

        @Override
        public AppBean[] newArray(int size) {
            return new AppBean[size];
        }
    };

    @Override
    public int compareTo(AppBean appBaen) {
        return TitleItemDecoration.compareHelper(getTag(),appBaen.getTag());
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

    @Override
    public String getTag() {
        return TitleItemDecoration.tagHelper(name);
    }
}
