package com.au.wxl.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;

/**
 * 手机设备，和程序访问工具类
 * 
 * @author Administrator
 */
public class GetDriver {

	private Context mContext;

	public GetDriver(Context mContext) {
		this.mContext = mContext;
	}

	/**
	 * 获取手机串号（IMEI）
	 */
	public String getDeviceId() {

		TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();

		return imei;
	}

	/**
	 * 获取程序版本
	 */
	public String getVersion() {

		try {

			// 获取PackaGemanager的实例
			PackageManager packageManager = mContext.getPackageManager();
			// getPackageName()是你当前类的包名，0代表是获取版本信息
			PackageInfo packInfo;
			packInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);

			return packInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取程序版本编号
	 */
	public int getVersionCode() {

		try {
			// 获取PackaGemanager的实例
			PackageManager packageManager = mContext.getPackageManager();
			// getPackageName()是你当前类的包名，0代表是获取版本信息
			PackageInfo packInfo;
			packInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
			return packInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
