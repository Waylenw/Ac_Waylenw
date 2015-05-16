package com.au.wxl.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络工具类
 */
public class NetUtil {

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

}
