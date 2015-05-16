package com.au.wxl.recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.util.Log;
/**
 * 网络连接监听的广播
 * 
 * @author wxl
 *
 */
public class NetConnListenerRecevier extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo activeInfo = manager.getActiveNetworkInfo();
		String action = intent.getAction();
		if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
			@SuppressWarnings("deprecation")
			NetworkInfo ni = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
			if (ni.getState() == State.CONNECTED && ni.getType() == ConnectivityManager.TYPE_WIFI) {
				Log.i("kevin", "wifi connected");
			} else if (ni.getState() == State.DISCONNECTED && ni.getType() == ConnectivityManager.TYPE_WIFI) {
				Log.i("kevin", "wifi disconnected");
			} else if (ni.getState() == State.DISCONNECTED && ni.getType() == ConnectivityManager.TYPE_MOBILE) {
				Log.i("kevin", "数据开关 DISCONNECTED");
			} else if (ni.getState() == State.CONNECTED && ni.getType() == ConnectivityManager.TYPE_MOBILE) {
				Log.i("kevin", "数据开关connected");
			}
		}
		
		
		  // 第一种
		// ConnectivityManager connectivityManager = (ConnectivityManager) context
		// .getSystemService(Context.CONNECTIVITY_SERVICE);
		// NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		// NetworkInfo mobileNetInfo = connectivityManager
		// .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	}

	// 如果无网络连接activeInfo为null
	// Toast.makeText(context,
	// "mobile:" + mobileInfo.isConnected() + "\n" + "wifi:" + wifiInfo.isConnected() + "\n" + "active:" + activeInfo.getTypeName(), 1)
	// .show();
	
	
	
	
	/* 
	 * 相关配置
	 * <receiver android:name=".MyRecevier" >
	 <intent-filter android:priority="800" >
	     <action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
	 </intent-filter>
	</receiver>
	</application>

	<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" >
	</uses-permission>*/
}
