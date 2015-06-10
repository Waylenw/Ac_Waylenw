package com.au.wxl.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;


/**
 *
 * 快捷方式工具类
 *
 */
public class AuShortCutUtil {

	private static final String ACTION_UNINSTALL_SHORTCUT = "com.android.launcher.action.UNINSTALL_SHORTCUT";

	/****启动页的class**/
	private static Class classSplash;

	/**
	 * 传入启动Activity的class
	 * @param classSplash
	 */
	public  static void init(Class classSplash){
		AuShortCutUtil.classSplash=classSplash;
	}

	/**
	 * 判断是不是 已经拥有图标了
	 *
	 * @param context
	 * @param resname app的资源名称
	 * @return
	 */
	public static boolean hasShortCut(Context context,int resname) {
		// 判断是不是已经有了
		boolean isInstallShortcut = false;
		ContentResolver resolver = context.getContentResolver();
		String AUTHORITY = "com.android.launcher.settings";
		final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/favorites?notify=true");
		Cursor cursor = resolver.query(CONTENT_URI, new String[]{"title", "iconResource"}, "title = ?", new String[]{context.getResources()
				.getString(resname).trim()}, null);
		if (null != cursor && cursor.getCount() > 0) {
			isInstallShortcut = true;
		}
		return isInstallShortcut;
	}

	/**
	 * 加桌面快捷图标(新版本的快捷方式(点击后不重启的意图))
	 *
	 * @param context
	 * @param resAPPIcon app图片资源
	 */
	public static void addShortCust(Context context,int resAPPIcon,int resAPPName) {
		Intent intent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		// 创建快捷方式的名字
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getResources().getString(resAPPName));
		// 不允许重复创建
		intent.putExtra("duplicate", false);
		Intent intentShort = new Intent(Intent.ACTION_MAIN);
		intentShort.addCategory(Intent.CATEGORY_LAUNCHER);

		// 解决点击app图标重新启动的问题
		intentShort.setClass(context, AuShortCutUtil.classSplash);
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intentShort);
		// 快捷方式图标
		ShortcutIconResource resource = ShortcutIconResource.fromContext(context, resAPPIcon);
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, resource);
		context.sendBroadcast(intent);
	}

	/**
	 * 删除快捷方式(老版本的快捷方式(点击后程序重启的意图))
	 * 
	 * @param context
	 */
	public static void delShortCust(Context context,int resAPPName) {
		Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
		// 快捷方式的名称
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(resAPPName));

		Intent intentShort1 = new Intent(Intent.ACTION_MAIN);
		intentShort1.setClass(context, AuShortCutUtil.classSplash);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intentShort1);
		context.sendBroadcast(shortcut);
	}
}
