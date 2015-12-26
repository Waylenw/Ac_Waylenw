package com.au.commons.activity.sys;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


import android.app.Activity;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import com.au.commons.utils.FileUtil;


public class CamereAcitiity extends Activity {

	public static String imagePath = null;

	// 指定uri
	public static Uri getCameraUri() {
		Uri uri = null;
		String state = Environment.getExternalStorageState();
		if (!TextUtils.isEmpty(state) && state.equals(Environment.MEDIA_MOUNTED)) {
			File file = new File(FileUtil.PATH);
			if (!file.exists()) {
				file.mkdirs();
			}
		} else { // 返回手机的内存地址
			File file = new File(Environment.getDataDirectory().getAbsolutePath() + "/data/");
			if (!file.exists()) {
				file.mkdirs();
			}
		}
		String strDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		// 照片的名字
		String imageName = "yeuniapp_" + strDate + ".png";
		// 图片的路径
		imagePath = FileUtil.PATH + imageName;
		uri = Uri.fromFile(new File(imagePath));
		return uri;
	}

}
