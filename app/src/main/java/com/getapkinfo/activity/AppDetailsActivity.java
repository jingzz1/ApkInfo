package com.getapkinfo.activity;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.palette.graphics.Palette;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.getapkinfo.R;
import com.getapkinfo.base.BaseActivity;
import com.getapkinfo.bean.AppBean;
import com.jingzz.permissionutil.PermissionAdapter;
import com.jingzz.permissionutil.PermissionUtil;

import java.io.File;

public class AppDetailsActivity extends BaseActivity {

    private AppBean bean;
    private TextView tv_name;
    private ImageView iv_icon;
    private View background;
    private TextView mAppName;
    private TextView mPackageName;
    private TextView mPath;
    private TextView mVersionName;
    private TextView mVersionCode;
    private TextView mIsSystem;
    private TextView mSHA1;
    private TextView mSHA256;
    private TextView mMD5;
    private TextView mSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_details);
        bean = getIntent().getParcelableExtra("bean");

        findViewById(R.id.back).setOnClickListener(view -> onBackPressed());
        tv_name = findViewById(R.id.name);
        tv_name.setText(bean.getName());
        iv_icon = findViewById(R.id.icon);
        iv_icon.setImageBitmap(bean.getIcon());

        background = findViewById(R.id.background);
        setBackground(bean.getIcon());

        mAppName = findViewById(R.id.appName);
        mPackageName = findViewById(R.id.packageName);
        mSize = findViewById(R.id.size);
        mPath = findViewById(R.id.path);
        mVersionName = findViewById(R.id.versionName);
        mVersionCode = findViewById(R.id.versionCode);
        mIsSystem = findViewById(R.id.isSystem);
        mSHA1 = findViewById(R.id.SHA1);
        mSHA256 = findViewById(R.id.SHA256);
        mMD5 = findViewById(R.id.MD5);

        mAppName.setText(bean.getName());
        mPackageName.setText(bean.getPackageName());
        mPath.setText(bean.getPackagePath());
        mVersionName.setText(bean.getVersionName());
        mVersionCode.setText(bean.getVersionCode() + "");
        mIsSystem.setText(bean.isSystem() + "");
        mSHA1.setText(AppUtils.getAppSignatureSHA1(bean.getPackageName()));
        mSHA256.setText(AppUtils.getAppSignatureSHA256(bean.getPackageName()));
        mMD5.setText(AppUtils.getAppSignatureMD5(bean.getPackageName()).toLowerCase().replace(":",""));
        mSize.setText(Formatter.formatFileSize(this, new File(bean.getPackagePath()).length()));

    }

    private int setBackground(Bitmap icon) {
        Palette.from(icon)
                .generate(palette -> {
                    int color;
                    Palette.Swatch swatch = palette.getMutedSwatch();
                    if (swatch == null)
                        swatch = palette.getLightMutedSwatch();
                    if (swatch == null)
                        swatch = palette.getDarkMutedSwatch();
                    if (swatch == null)
                        swatch = palette.getVibrantSwatch();
                    if (swatch == null)
                        color = palette.getMutedColor(getColor1(R.color.colorPrimary));
                    else
                        color = swatch.getRgb();
                    background.setBackgroundColor(color);
                });
        return 0;
    }
}
