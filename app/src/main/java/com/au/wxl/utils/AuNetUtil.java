package com.au.wxl.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络工具类
 */
public class AuNetUtil {

    /**
     * 获取网络信息
     *
     * @param context
     * @return
     */
    public static NetworkInfo getNetworkInfo(Context context, int networkType) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info;
        if (networkType == 0) {
            info = connMgr.getActiveNetworkInfo();
        } else {
            info = connMgr.getNetworkInfo(networkType);
        }
        return info;
    }

    /**
     * 是否已连接网络
     *
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        NetworkInfo info = getNetworkInfo(context, 0);
        return info != null && info.isConnected();
    }

    /**
     * 判断wifi是否连接
     *
     * @param context
     * @return
     */

    public static boolean isWifiConnenct(Context context) {
        NetworkInfo info = getNetworkInfo(context, ConnectivityManager.TYPE_WIFI);
        return info != null && info.isConnected();
    }

    /**
     * 判断手机是否处在漫游状态
     *
     * @param context
     * @return
     */
    public static boolean isRoaming(Context context) {
        NetworkInfo info = getNetworkInfo(context, 0);
        if (info.isRoaming()) {
            return true;
        }
        return false;
    }

    /**
     * 打开网络设置界面
     */
    public static void openSetting(Activity context) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings",
                "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        context.startActivityForResult(intent, 0);
    }

}
