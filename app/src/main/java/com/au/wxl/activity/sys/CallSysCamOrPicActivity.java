package com.au.wxl.activity.sys;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;




import com.androidutils.utils.FileUtil;

import android.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;

/**
 * 统一处理相机及相册
 * 
 * @author wxl
 */
public class CallSysCamOrPicActivity extends Activity implements OnClickListener {

	protected static final int REQ_CODE_PICTURES = 2;// 选择照相册

	protected static final int REQ_CODE_CAMERA = 1;// 选择照相机

	private static final int REQ_CUT_PIC = 3;// 裁剪图片

	private static final int REQ_CHANGE_PICTURE = 0x236;// 获取裁剪图片路径

	private File imageFile;

	// private File tempPhotoFile;
	private String fileName;

	private Uri imgUri, imgUri1;

	private boolean isFromCamera;// 是否来自相机

	/** 用来表示是登陆界面进入的 ,1表示从登陆界面进入 */
	private int flag;

	/**
	 * 是否是取普通状态的图片
	 */
	private boolean isNormalPic;

	public static Intent getIntent(Context context, boolean isNormal, int flag) {
		Intent intent = new Intent(context, CallSysCamOrPicActivity.class);
		intent.putExtra("isNormal", isNormal);
		intent.putExtra("flagtype", flag);
		return intent;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		Intent intent = getIntent();
		isNormalPic = intent.getBooleanExtra("isNormalPic", false);
		flag = intent.getIntExtra("flagtype", -1);
		fileName = FileUtil.getCutPhotoPath(this) + "_temp_photo.jpg";
		fileName = "file:///sdcard/" + File.separator + getPackageName() + "/cache/" + File.separator + FileUtil.CUT_PHOTO + "_temp_photo.jpg";
		imgUri = Uri.parse(fileName);
		Log.e("imgUri     =     ", "imgUri     =     " + imgUri);
		imageFile = new File(fileName);
		Log.e("imgUri     =     ", "imgUri     =     " + imgUri);
	}

	/**
	 * 选取图片的方式（1为相机，2为相册）
	 * 
	 * @param type
	 */
	private void doChoiceGetPicType(int type) {
		Intent intent = null;
		switch (type) {
			case REQ_CODE_CAMERA:// 相机
				intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// intent.putExtra(MediaStore.EXTRA_OUTPUT, CamereAcitiity.getCameraUri());
				startActivityForResult(intent, REQ_CODE_CAMERA);
				break;
			case REQ_CODE_PICTURES:// 相册
				// if (FileUtil.isExistSDCard()) {
				// if (ViewUtil.getSDKVerSion() >= 19) {
				// intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, null);
				// } else {
				// intent = new Intent(Intent.ACTION_GET_CONTENT, null);
				// }
				// intent.setType("image/*");
				// intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
				// startActivityForResult(intent, REQ_CODE_PICTURES);
				// } else {
				// ViewUtil.toast(this, R.string.do_not_support);
				// }
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Bitmap bm;
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case REQ_CODE_PICTURES:// 图库
					isFromCamera = false;
					Uri originalUri = null;
					if (data != null && data.getData() != null) {
						imgUri = data.getData(); // 获得图片的Uri
						imgUri1 = data.getData(); // 获得图片的Uri
						imgUri = Uri.parse(getPath(this, imgUri));
					}
					if (imgUri == null) {
						// ViewUtil.toast(this, "目前只支持JPG及JPEG格式的图片");
					} else {
						submit2Crop();
					}
					break;
				case REQ_CODE_CAMERA:// 相册
					isFromCamera = true;
					if (isNormalPic) {
						submit2Crop();
					} else {
						if (data != null && data.getData() != null) {
							imgUri = data.getData();
							if (imgUri != null) {
								submit2Crop();
							} else {
								finish();
							}
						} else {
							File file = new File(CamereAcitiity.imagePath);
							try {
								imgUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), null, null));
							} catch (Exception e) {
								e.printStackTrace();
							}
							submit2Crop();
						}

					}
					break;
				case REQ_CUT_PIC:
					if (data != null && data.getExtras() != null) {
						Bundle bundle = (Bundle) data.getExtras();
						bm = (Bitmap) bundle.get("data");
						new SaveBitmapThread(bm).start();
					} else {
						submit2Crop();
					}
					break;
				case REQ_CHANGE_PICTURE:// 裁剪返回
					Intent intent = new Intent();
					String cut_path = null;
					try {
						cut_path = data.getStringExtra("cut_path");
					} catch (Exception e) {
						cut_path = "";
					}
					intent.putExtra("picFile", cut_path);
					intent.putExtra("picFile_test", imgUri);
					setResult(RESULT_OK, intent);
					finish();
					break;
			}
		}
	}

	/**
	 * 提交并跳转到裁剪界面
	 */
	public void submit2Crop() {
		if (imgUri != null) {
			if (1 == flag) {
				Intent intent = new Intent();
				intent.putExtra("uri", isFromCamera ? imgUri : imgUri1);
				intent.putExtra("camare", 2);
				startActivityForResult(intent, REQ_CHANGE_PICTURE);
			} else {
				imageFile = new File(imgUri.getPath());
				Intent intent = new Intent();
				intent.putExtra("picFile", imgUri.getPath());
				intent.putExtra("picFile_test", imgUri);
				setResult(RESULT_OK, intent);
				this.finish();
			}
		}
	}

	/**
	 * 保存位图
	 * 
	 * @param bm
	 */
	class SaveBitmapThread extends Thread {

		private Bitmap bm;

		public SaveBitmapThread(Bitmap bm) {
			this.bm = bm;
		}

		@Override
		public void run() {
			super.run();
			File f = new File(imgUri.getPath());
			if (f.exists()) {
				f.delete();
			}
			try {
				FileOutputStream out = new FileOutputStream(f);
				bm.compress(CompressFormat.PNG, 90, out);
				out.flush();
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				finish();
				// TODO 上传操作
			}
		}
	}

	@SuppressLint("NewApi")
	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}

				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for MediaStore Uris, and other file-based ContentProviders.
	 * 
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @param selection (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// case R.id.btnCamera:
		// doChoiceGetPicType(REQ_CODE_CAMERA);
		// break;
		// case R.id.btnPic:
		// doChoiceGetPicType(REQ_CODE_PICTURES);
		// break;
		// case R.id.btnCancel:
		// finish();
		//
		// break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
		}
		return true;
	}

}
