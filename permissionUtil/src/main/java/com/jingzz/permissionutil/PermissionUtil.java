package com.jingzz.permissionutil;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import android.text.TextUtils;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator - PC on 2017/12/28.
 */

public class PermissionUtil {

    private static boolean isoneAccept = true;
    private static boolean isoneRefuse = true;

    private static void permissions(Activity context, PermissionsInterface permissionsInterface, boolean isShowDialog,
                                    final String explain,String[] permissions) {
        permissions(context, permissionsInterface, true, isShowDialog,explain, permissions);
    }

    private static void permissions(final Activity context,
                                    final PermissionsInterface permissionsInterface,
                                    boolean isoneRefuse1,
                                    final boolean isShowDialog,
                                    final String explain,
                                    final String[] permissions) {


        final List<String> list = new ArrayList<>();

        for (String ps : permissions) {
            if (!AndPermission.hasPermissions(context, ps))
                list.add(ps);
        }

        isoneAccept = true;
        isoneRefuse = isoneRefuse1;
        if (list.size() <= 0) {
            if (permissionsInterface != null)
                permissionsInterface.allAgree();
            return;
        }

        final String[] ss = new String[list.size()];
        list.toArray(ss);

        AndPermission.with(context)
                .runtime()
                .permission(ss)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        if (permissionsInterface != null) {
                        }
                        permissionsInterface.allAgree();
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        List<String> deniedPermissions = new ArrayList<>();
                        List<String> alwaysDeniedPermissions = new ArrayList<>();
                        boolean isAllaccept = true;
                        for (String permission : data) {
                            if (!ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {
                                alwaysDeniedPermissions.add(permission);
                                isAllaccept = false;
                            } else {
                                deniedPermissions.add(permission);
                            }
                        }
                        if (isShowDialog || alwaysDeniedPermissions.size() > 0) {
//                            if(data.size()>0){
//                                if(isAllaccept){
//                                    showRefuseDialog(context,permissionsInterface,data);
//                                }else {
//                                    showWithNever(context,data);
//                                }
//                            }

                            if (deniedPermissions.size() > 0) {
                                showRefuseDialog(context, permissionsInterface, data,explain);
                            }
                            if (alwaysDeniedPermissions.size() > 0) {
                                showWithNever(context, alwaysDeniedPermissions, explain);
                            }
                        } else {

                            if (permissionsInterface != null) {
                                permissionsInterface.refuse();
                                if (deniedPermissions.size() > 0)
                                    permissionsInterface.refuse(deniedPermissions);
                                if (alwaysDeniedPermissions.size() > 0)
                                    permissionsInterface.refusAndNotPrompt(alwaysDeniedPermissions);
                            }
                        }
                    }
                })
                .start();


    }

    private static AlertDialog refuseDialog;

    private static void showRefuseDialog(final Activity context,
                                         final PermissionsInterface permissionsInterface,
                                         final List<String> Permissionsss,
                                         String msg) {

        if (refuseDialog != null && refuseDialog.isShowing()) {
            return;
        }

        List<String> permissionNames = Permission.transformText(context, Permissionsss);
        String permissionText = TextUtils.join("、", permissionNames);

        if(TextUtils.isEmpty(msg))
        msg = "你拒绝了" + permissionText + "等权限," + getAppName(context) + "或无法正常运行，是否重新申请该权限?";

        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.BDAlertDialog);
        builder.setTitle("提示")
                .setMessage(msg)
                .setPositiveButton("重新申请", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String[] ss = new String[Permissionsss.size()];
                        Permissionsss.toArray(ss);
                        permissions(context, permissionsInterface, false, "",ss);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (permissionsInterface != null)
                            permissionsInterface.refuse();
                    }
                });
        refuseDialog = builder.show();
        refuseDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.GRAY);
    }

    private static AlertDialog withNeverDialog;

    private static void showWithNever(final Activity context, final List<String> Permissionsss,String msg) {
        if (withNeverDialog != null && withNeverDialog.isShowing()) {
            return;
        }
        List<String> permissionNames = Permission.transformText(context, Permissionsss);
        String permissionText = TextUtils.join("、", permissionNames);
        if(TextUtils.isEmpty(msg))
        msg = "由于" + getAppName(context) + "无法获取到" + permissionText + "等权限,app或无法正常运行,请开户权限后再使用。" ;
        msg += "\n开户权限路径：设置->应用管理->" + getAppName(context) + "->权限";

        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.BDAlertDialog);
        builder.setTitle("提示")
                .setMessage(msg)
                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent localIntent = new Intent();
                        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));

                        context.startActivity(localIntent);

                    }
                })
                .setNegativeButton("取消", null);
        withNeverDialog = builder.show();
        withNeverDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.GRAY);
    }

    private static String getAppName(Context context) {
        PackageManager pm = context.getPackageManager();
        String appName = context.getApplicationInfo().loadLabel(pm).toString();
        return appName;
    }

    public static Builder builder(Activity context) {
        return new Builder(context);
    }

    /**
     *
     */
    public static class Builder {
        private Activity context;
        private List<String> permissionList = new ArrayList<>();
        private boolean isShowDialog = true;
        private String explain;

        private Builder(Activity context) {
            this.context = context;
        }

        /**
         * 添加一个权限，如：Manifest.permission.READ_CALENDAR
         *
         * @param permission
         * @return
         */
        public Builder addPermissions(@Nullable String permission) {
            if (permissionList == null)
                permissionList = new ArrayList<>();

            permissionList.add(permission);
            return this;
        }

        /**
         * 添加一个权限组，如：PermissionInit.CAMERA
         *
         * @param permission
         * @return
         */
        public Builder addPermissions(String... permission) {
            if (permissionList == null)
                permissionList = new ArrayList<>();

            permissionList.addAll(Arrays.asList(permission));
            return this;
        }

        /**
         * 添加多个权限组，如:PermissionInit.CAMERA,PermissionInit.LOCATION
         *
         * @param permissions
         * @return
         */
        public Builder addPermissions(String[]... permissions) {
            if (permissionList == null)
                permissionList = new ArrayList<>();
            for (String[] ps : permissions) {
                permissionList.addAll(Arrays.asList(ps));
            }
            return this;
        }

        /**
         * 添加多个权限，如list.add(Manifest.permission.READ_CALENDAR)
         *
         * @param permissions
         * @return
         */
        public Builder addPermissions(List<String> permissions) {
            if (permissionList == null)
                permissionList = new ArrayList<>();
            permissionList.addAll(permissions);
            return this;
        }

        /**
         * 添加多个权限组，如：list.add(PermissionInit.CAMERA)
         *
         * @param permissions
         * @return
         */
        public Builder addPermissionList(List<String[]> permissions) {
            if (permissionList == null)
                permissionList = new ArrayList<>();
            for (String[] ps : permissions) {
                permissionList.addAll(Arrays.asList(ps));
            }
            return this;
        }

        /**
         * 是否弹出对话框，默认true
         *
         * @param isShowDialog true 拒绝权限则弹提示是否重新申请，拒绝权限并选择不再提示，则弹提示去设置权限
         *                     false 不弹提示
         * @return
         */
        public Builder isShowDialog(boolean isShowDialog) {
            this.isShowDialog = isShowDialog;
            return this;
        }

        public Builder setExplain(String explain) {
            this.explain = explain;
            return this;
        }

        /**
         * 申请权限
         *
         * @param permissionsInterface
         */
        public void execute(PermissionsInterface permissionsInterface) {

            String[] ps = new String[permissionList.size()];
            permissionList.toArray(ps);
            PermissionUtil.permissions(context, permissionsInterface, isShowDialog, explain,ps);
        }

        public void execute() {
            execute(null);
        }
    }


}
