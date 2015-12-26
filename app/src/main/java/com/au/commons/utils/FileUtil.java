package com.au.commons.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;


/**
 * 文件工具类
 */
public class FileUtil {

	/**
	 * 图片缓存目录
	 */
	public static final String IMAGES_FOLD = "images";

	/**
	 * 对象缓存目录
	 */
	public static final String OBJECTS_FOLD = "objects";

	/**
	 * 文本缓存目录
	 */
	public static final String TEXT_FOLD = "texts";

	/**
	 * APK下载目录
	 */
	public static final String APK_DOWNLOAD_FOLD = "apk";

	/**
	 * 裁剪过的图片地址
	 */
	public static final String CUT_PHOTO = "cut";

	/**
	 * 判断是否存在应用外部存储
	 * 
	 * @return
	 */
	private static final String SDCARD_MNT = "/mnt/sdcard";

	private static final String SDCARD = "/sdcard";

	public static boolean isExistSDCard() {
		return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
	}

	/**
	 * 获取裁剪后的照片地址
	 * 
	 * @param context
	 * @return
	 */
	public static final String getCutPhotoPath(Context context) {
		File file = FileUtil.getDiskCacheDir(context, FileUtil.CUT_PHOTO);
		String path = file.getPath() + File.separator;
		return path;
	}

	/**
	 * 获取文本目录 , 如果没有，则生成一个
	 * 
	 * @param context
	 * @return
	 */
	public static String getTextStorePath(Context context) {
		File file = FileUtil.getDiskCacheDir(context, FileUtil.TEXT_FOLD);
		String path = file.getPath() + File.separator;
		return path;
	}

	/**
	 * 删除目录下的所有子目录及文件
	 * 
	 * @param fold
	 */
	public static void deleteFold(File fold) {
		if (fold != null && fold.isDirectory()) {
			File[] files = fold.listFiles();
			if (null == files)
				return;
			for (File file : files) {
				if (file.isDirectory()) {
					deleteFold(file);
				} else {
					file.delete();
				}
			}
		}
	}

	/**
	 * 获取图片缓存目录 , 如果没有，则生成一个
	 * 
	 * @param context
	 * @return
	 */
	public static String getImageCachePath(Context context) {
		File file = FileUtil.getDiskCacheDir(context, FileUtil.IMAGES_FOLD);
		String path = file.getPath() + File.separator;
		return path;
	}

	/**
	 * 获取对象目录 , 如果没有，则生成一个
	 * 
	 * @param context
	 * @return
	 */
	public static String getObjectStorePath(Context context) {
		File file = FileUtil.getDiskCacheDir(context, FileUtil.OBJECTS_FOLD);
		String path = file.getPath() + File.separator;
		return path;
	}

	/**
	 * 上传对象目录
	 * 
	 * @param context
	 */
	public static final void clearObjectStorePath(Context context) {
		File fold = FileUtil.getDiskCacheDir(context, FileUtil.OBJECTS_FOLD);
		deleteFold(fold);
	}

	/**
	 * 获取APK下载目录
	 * 
	 * @param context
	 * @return
	 */
	public static String getApkDownloadPath(Context context) {
		File file = FileUtil.getDiskCacheDir(context, FileUtil.APK_DOWNLOAD_FOLD);
		String path = file.getPath() + File.separator;
		return path;
	}

	/**
	 * 打开并安装APK
	 * 
	 * @param saveApkName APK名字
	 */
	public static void openAndInstallApk(Context context, File apkFile) {
		context.startActivity(getInstallApkIntent(apkFile));
	}

	public static Intent getInstallApkIntent(File apkFile) {
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		String mimeType = "application/vnd.android.package-archive";
		intent.setDataAndType(Uri.fromFile(apkFile), mimeType);
		return intent;
	}

	/**
	 * 清除APK下载目录的文件
	 * 
	 * @param context
	 */
	public static void clearApkDownloadPath(Context context) {
		File fold = FileUtil.getDiskCacheDir(context, FileUtil.APK_DOWNLOAD_FOLD);
		if (fold != null && fold.isDirectory()) {
			File[] files = fold.listFiles();
			if (null == files)
				return;
			for (File file : files) {
				file.delete();
			}
		}
	}

	/**
	 * @param context
	 * @param uniqueName 子目录名,如"images"存图片, "objects"存对象
	 * @return
	 */
	private static File getDiskCacheDir(Context context, String uniqueName) {
		final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ? getExternalCacheDir(context).getPath()
				: context.getCacheDir().getPath();
		File fold = new File(cachePath + File.separator + uniqueName);
		if (!fold.exists()) {
			fold.mkdirs();
		}
		return fold;
	}

	/**
	 * 获取缓存目录
	 * 
	 * @param context
	 * @return
	 */
	private static File getExternalCacheDir(Context context) {
		File fold = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + context.getPackageName() + "/cache/");
		if (!fold.exists()) {
			fold.mkdir();
		}
		return fold;
	}

	public static boolean save(InputStream is, String savePath, long length) throws IOException {
		int total = 0;
		FileOutputStream fos = null;
		try {
			byte[] buff = new byte[1024];
			int len;
			fos = new FileOutputStream(new File(savePath));
			while ((len = is.read(buff)) > 0) {
				fos.write(buff, 0, len);
				total += len;
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			// return false;
			throw e;
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (total != length) {
				throw new IOException("下载不完整，应下" + length + ", 实下" + total);
			}
		}
	}

	// 有sd卡的情况下 下载图片
	public static final String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "yueniapp/";//

	// //
	// public static String imagePath = null;
	//
	// public static Uri getCameraUri() {
	// Uri uri = null;
	// String state = Environment.getExternalStorageState();
	// if (!TextUtils.isEmpty(state)
	// && state.equals(Environment.MEDIA_MOUNTED)) {
	// File file = new File(PATH);
	// if (!file.exists()) {
	// file.mkdirs();
	// }
	// } else { // 返回手机的内存地址
	// Log.e("info", "->>"+uri.toString());
	// File file = new File(Environment.getDataDirectory()
	// .getAbsolutePath() + "/data/");
	// if (!file.exists()) {
	// file.mkdirs();
	// }
	// }
	// String strDate = new SimpleDateFormat("yyyyMMddHHmmss")
	// .format(new Date());
	// // 照片的名字
	// String imageName = "mahoo" + strDate + "jpg";
	// // 图片的路径
	// imagePath = PATH + imageName;
	// uri = Uri.fromFile(new File(imagePath));
	// Log.e("info", "->>"+uri.toString());
	// return uri;
	// }

	// // 将剪切 的图片压缩
	// public static String saveCropPic(Activity activity, Uri uri, CompressFormat type, int size) {
	// String state = Environment.getExternalStorageState();
	// if (!TextUtils.isEmpty(state) && state.equals(Environment.MEDIA_MOUNTED)) {
	// File file = new File(PATH);
	// if (!file.exists()) {
	// file.mkdirs();
	// }
	// } else {
	// File file = new File(Environment.getDataDirectory().getAbsolutePath() + "/data/");
	// if (!file.exists()) {
	// file.mkdirs();
	// }
	// }
	// String strDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	// // 获取图片的绝对地址
	// String thePath = getAbsolutePathFromNoStandardUri(uri);
	// if (TextUtils.isEmpty(thePath)) {
	// // 获取文件的标准路径
	// thePath = getAbsoluteImagePath(activity, uri);
	// }
	// // 获取图片的扩展名
	// String picName = getPicName(thePath);
	// picName = TextUtils.isEmpty(picName) ? "jpg" : picName;
	// // 图片的名字
	// String picFileName = "yueniapp_crop" + strDate + "." + picName;
	// picFileName = PATH + picFileName;
	// // 保存图片到
	// if (createFile(picFileName)) {
	// FileOutputStream fos;
	// try {
	// fos = new FileOutputStream(picFileName);
	// // Bitmap bitmap = BitmapUtil.decodeUri(activity, uri);
	// if (bitmap.compress(type, size, fos)) { // MediaStore.Images.Media.getBitmap(activity.getContentResolver(),
	// BitmapUtils.freeeBitmap(bitmap);
	// // uri)
	// fos.flush();
	// fos.close();
	// fos = null;
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// return picFileName;
	//
	// }

	/**
	 * 创建一个新的文件
	 * 
	 * @param path
	 * @return
	 */
	private static boolean createFile(String path) {

		File file = new File(path);
		if (null != file && !file.exists()) {
			if (!file.getParentFile().exists()) {
				return file.getParentFile().mkdirs();
			}
			try {
				return file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	/**
	 * 获取图片的扩展名
	 * 
	 * @param picPath
	 * @return
	 */
	private static String getPicName(String picPath) {
		if (TextUtils.isEmpty(picPath)) {
			return "";
		}
		int indext = picPath.lastIndexOf(".");
		return picPath.substring(indext + 1);
	}

	/**
	 * 获取文件的标准的路径
	 * 
	 * @return
	 */
	private static String getAbsoluteImagePath(Activity activity, Uri uri) {
		String picPath = "";
		String[] projection = new String[] { Media.DATA };
		Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
		if (null != cursor && cursor.getCount() > 0 && cursor.moveToFirst()) {
			picPath = cursor.getString(cursor.getColumnIndexOrThrow(Media.DATA));
		}
		return picPath;
	}

	/**
	 * 获取图片的绝对地址
	 * 
	 * @param uri 图片的 uri
	 * @return 返回图片的地址
	 */
	private static String getAbsolutePathFromNoStandardUri(Uri uri) {
		String path = null;
		String picPath = uri.toString();
		picPath = Uri.encode(picPath);
		String file1 = "f://" + SDCARD + File.separator;
		String file2 = "f://" + SDCARD_MNT + File.separator;
		if (!TextUtils.isEmpty(picPath)) {
			if (picPath.startsWith(file1)) {
				path = Environment.getExternalStorageDirectory().getPath() + File.separator + picPath.substring(file1.length());
			} else if (picPath.startsWith(file2)) {
				path = Environment.getExternalStorageDirectory().getPath() + File.separator + picPath.substring(file2.length());
			}
		}
		return path;

	}

	/**
	 * 删除创建的图片文件
	 * 
	 * @param url
	 */

	public static void detelePic(Context context, String url) {
		File file = new File(url);
		if (file.exists()) {
			file.delete();
			// 清除残留
			context.getContentResolver().delete(Media.EXTERNAL_CONTENT_URI, Media.DATA + "=?", new String[] { url });
		}
	}

	/**
	 * 第三方的图片保存
	 * 
	 * @param bitmap
	 * @param fileName
	 * @return
	 */
	public static String saveBitmap(Bitmap bitmap, String fileName) {
		String state = Environment.getExternalStorageState();
		if (!TextUtils.isEmpty(state) && state.equals(Environment.MEDIA_MOUNTED)) {
			File file = new File(PATH);
			if (!file.exists()) {
				file.mkdirs();
			}
		} else {
			File file = new File(Environment.getDataDirectory().getAbsolutePath() + "/data/");
			if (!file.exists()) {
				file.mkdirs();
			}
		}
		String strDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		// 图片的名字
		String picFileName = "yueniapp_crop" + strDate + "." + fileName + ".jpg";
		picFileName = PATH + picFileName;
		if (createFile(picFileName)) {
			try {
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(picFileName));
				bitmap.compress(CompressFormat.JPEG, 80, bos);
				bos.flush();
				bos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return picFileName;

	}

	// /**
	// * 下载启动页面图片
	// *
	// * @param bitmap
	// * @param fileName
	// * @return
	// */
	// public static void downloadSplashImage(String url) {
	// ImageLoader.getInstance().loadImage(url, new SimpleImageLoadingListener() {
	//
	// @Override
	// public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
	// super.onLoadingComplete(imageUri, view, loadedImage);
	//
	// saveSplashImage(loadedImage);
	//
	// }
	//
	// });
	// }

	// /**
	// * 保存启动页面的图片
	// *
	// * @param bitmap
	// * @param fileName
	// * @return
	// */
	// public static String saveSplashImage(Bitmap bitmap) {
	// // 图片的名字
	// String picFileName = "splash.jpg";
	// picFileName = FileManager.getFilePath() + picFileName;
	// if (createFile(picFileName)) {
	// try {
	// BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(picFileName));
	// bitmap.compress(CompressFormat.JPEG, 80, bos);
	// bos.flush();
	// bos.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// return picFileName;
	//
	// }
}
