package com.au.commons.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * APP安装卸载工具类
 * <p/>
 * Created  on 2015/6/11.
 */
public class AppInstallUtils {
    /**
     * 安装
     *
     * @param context 接收外部传进来的context
     */
    public static void install(Context context, String mUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(mUrl)),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }


    /**
     * 卸载
     *
     * @param context 接收外部传进来的context
     */
    public static void Unintall(Context context, String packname) {
        Uri packageURI = Uri.parse("package:" + packname);
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        context.startActivity(uninstallIntent);
    }

    /**
     * 获取包名
     *
     * @param ctx
     * @return
     */
    public static String getPackageName(Context ctx) {
        try {
            PackageInfo info = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            return info.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取设备信息
     *
     * @param context
     * @return
     */
    public static Map<String, String> getDeviceInfo(Context context) {
        Map<String, String> map = new HashMap<String, String>();
        android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        String device_id = tm.getDeviceId();
        String msisdn = tm.getLine1Number(); // 手机号码
        String iccid = tm.getSimSerialNumber(); // sim卡号ICCID
        String imsi = tm.getSubscriberId(); // imsi

        android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);

        int i = wifi.getConnectionInfo().getIpAddress();
        String ip = ((i & 0xff) + "." + (i >> 8 & 0xff) + "." + (i >> 16 & 0xff) + "." + (i >> 24 & 0xff));
        String mac = wifi.getConnectionInfo().getMacAddress();

        if (TextUtils.isEmpty(device_id)) {
            device_id = mac;
        }

        if (TextUtils.isEmpty(device_id)) {
            device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID);
        }

        map.put("ip", doNullStr(ip));
        map.put("mac", doNullStr(mac));
        map.put("device_id", doNullStr(device_id));
        map.put("msisdn", doNullStr(msisdn));
        map.put("iccid", doNullStr(iccid));
        map.put("imsi", doNullStr(imsi));

        return map;
    }

    /**
     * 判断当前应用程序是否处于后台，通过getRunningTasks的方式
     *
     * @return true 在后台; false 在前台
     */
    public static boolean isApplicationBroughtToBackgroundByTask(String packageName, Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 获取Manifest中的meta-data值
     *
     * @param context
     * @param metaKey
     * @return
     */
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String values = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),

                    PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                values = metaData.getString(metaKey);
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return values;
    }

    /**
     * 判断字符串是否有空
     *
     * @param str
     * @return
     */
    public static String doNullStr(String str) {
        return TextUtils.isEmpty(str) ? "" : str;
    }

}
