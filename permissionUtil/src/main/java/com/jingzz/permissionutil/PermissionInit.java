package com.jingzz.permissionutil;

import android.Manifest;
import android.os.Build;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator - PC on 2017/12/28.
 */

public class PermissionInit {
    public static final String[] CALENDAR;   // 读写日历。
    public static final String[] CAMERA;     // 相机。
    public static final String[] CONTACTS;   // 读写联系人。
    public static final String[] LOCATION;   // 读位置信息。
    public static final String[] MICROPHONE; // 使用麦克风。
    public static final String[] PHONE;      // 读电话状态、打电话、读写电话记录。
    public static final String[] SENSORS;    // 传感器。
    public static final String[] SMS;        // 读写短信、收发短信。
    public static final String[] STORAGE;    // 读写存储卡。

    public static final Map<String, String> map = new HashMap<>();

    static {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            CALENDAR = new String[]{};
            CAMERA = new String[]{};
            CONTACTS = new String[]{};
            LOCATION = new String[]{};
            MICROPHONE = new String[]{};
            PHONE = new String[]{};
            SENSORS = new String[]{};
            SMS = new String[]{};
            STORAGE = new String[]{};
        } else {
            CALENDAR = new String[]{
                    Manifest.permission.READ_CALENDAR,
                    Manifest.permission.WRITE_CALENDAR};

            CAMERA = new String[]{
                    Manifest.permission.CAMERA};

            CONTACTS = new String[]{
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS,
                    Manifest.permission.GET_ACCOUNTS};

            LOCATION = new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION};

            MICROPHONE = new String[]{
                    Manifest.permission.RECORD_AUDIO};

            PHONE = new String[]{
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.WRITE_CALL_LOG,
                    Manifest.permission.USE_SIP,
                    Manifest.permission.PROCESS_OUTGOING_CALLS};

            SENSORS = new String[]{
                    Manifest.permission.BODY_SENSORS};

            SMS = new String[]{
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.RECEIVE_WAP_PUSH,
                    Manifest.permission.RECEIVE_MMS};

            STORAGE = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }

        for (String p : CALENDAR) {
            map.put(p, "读写日历");
        }
        for (String p : CAMERA) {
            map.put(p, "调用相机");
        }
        for (String p : CONTACTS) {
            map.put(p, "获取联系人");
        }
        for (String p : LOCATION) {
            map.put(p, "获取位置");
        }
        for (String p : MICROPHONE) {
            map.put(p, "使用麦克风");
        }
        for (String p : PHONE) {
            map.put(p, "打电话");
        }
        for (String p : SENSORS) {
            map.put(p, "调用传感器");
        }
        for (String p : SMS) {
            map.put(p, "获取短信信息");
        }
        for (String p : STORAGE) {
            map.put(p, "存储空间");
        }

    }

    public static String getPermissionName(String permission) {
        return map.get(permission);
    }
}
