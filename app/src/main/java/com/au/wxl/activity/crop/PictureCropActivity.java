package com.au.wxl.activity.crop;//package com.androidutils.activity.crop;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.content.res.AssetFileDescriptor;
//import android.graphics.Bitmap;
//import android.graphics.Bitmap.CompressFormat;
//import android.graphics.BitmapFactory;
//import android.graphics.Matrix;
//import android.graphics.PointF;
//import android.graphics.Rect;
//import android.graphics.RectF;
//import android.media.ExifInterface;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Handler;
//import android.provider.MediaStore;
//import android.text.format.DateFormat;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.View.OnTouchListener;
//import android.view.ViewGroup.LayoutParams;
//import android.view.ViewTreeObserver;
//import android.view.ViewTreeObserver.OnGlobalLayoutListener;
//import android.widget.ImageView;
//import android.widget.ImageView.ScaleType;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.androidutils.activity.R;
//import com.androidutils.utils.ViewUtil;
//import com.androidutils.view.ClipView;
//
///**
// * 处理图片的方法
// * 
// * @author admin
// */
//public class PictureCropActivity extends Activity implements OnClickListener, OnTouchListener {
//
//	private Uri uri;
//
//	private TextView tvSave;
//
//	// 8屏幕的款第
//	private int width;
//
//	private Bitmap bm;
//
//	private ImageView ivRotate;
//
//	private ImageView srcPic;
//
//	private ClipView clipview;
//
//	private Bitmap bitmap;
//
//	private Matrix matrix = new Matrix();
//
//	private Matrix savedMatrix = new Matrix();
//
//	/** 记录起始坐标 */
//	private PointF start = new PointF();
//
//	/** 记录缩放时两指中间点坐标 */
//	private PointF mid = new PointF();
//
//	/** 动作标志：无 */
//	private static final int NONE = 0;
//
//	/** 动作标志：拖动 */
//	private static final int DRAG = 1;
//
//	/** 动作标志：缩放 */
//	private static final int ZOOM = 2;
//
//	/** 初始化动作标志 */
//	private int mode = NONE;
//
//	private float oldDist = 1f;
//
//	private RelativeLayout reImageView;
//
//	private View conViewView;
//
//	// 判断图片是不是偏转了90度
//	private boolean isRotate = false;
//
//	// 处理后的图片的大小
//	private Bitmap bitmapResoure;
//
//	/** 判断是从发布进入的还是跟新 图像进入的 ,1表示从发布进入，2表示更新图像 */
//	private int flag;
//
//	private float clip_scale;// 裁剪框的缩放比例
//
//	private boolean isDrag = true;// 是否可以拖动
//
//	private float bitmap_rote_size = 90f;
//
//	private long onclick_time;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		if (null == getIntent()) {
//			return;
//		} else {
//			uri = getIntent().getParcelableExtra("uri");
//			flag = getIntent().getIntExtra("camare", -1);
//			if (uri == null) {
//				finish();
//				return;
//			}
//			if (null == bitmap) {
//				// 判断图片是不是偏转了90度
//				isRotate = getIsRote90(uri.toString());
//				// String path = MyPictureChangeActivity.getPath(PictureSettingActivity.this, uri);
//				bitmap = BitmapUtil.getBitmap(uri, this);
//				// bitmap = BitmapFactory.decodeFile(path);
//				startFaceDetection();
//			}
//		}
//		// Intent intent = new Intent();
//		// intent.setAction("com.android.camera.action.CROP");
//		// intent.setDataAndType(uri, "image/*");// mUri是已经选择的图片Uri
//		// intent.putExtra("crop", "true");
//		// intent.putExtra("aspectX", 1);// 裁剪框比例
//		// intent.putExtra("aspectY", 1);
//		// intent.putExtra("outputX", 600);// 输出图片大小
//		// intent.putExtra("outputY", 600);
//		// intent.putExtra("return-data", true);
//		//
//		// startActivityForResult(intent, 200);
//
//		// file:///storage/sdcard0/yueniapp/mohoo20141204163543.jpg
//		// content: // media/external/images/media/148345
//		DisplayMetrics dm = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(dm);
//		width = dm.widthPixels;
//		// conViewView = LayoutInflater.from(this).inflate(R.layout.actiivty_picture_setting, null);
//
//		// 判断图片的
//		// setContentView(R.layout.actiivty_picture_setting);
//		setActionbarVisible(false);
//		initView();
//
//	}
//
//	// @Override
//	// protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//	// if (resultCode == RESULT_OK) {
//	// if (requestCode == 200) {
//	// // 拿到剪切数据
//	// Bitmap clipBitmap = data.getParcelableExtra("data");
//	// String path = FileUtil.saveCropPic(PictureSettingActivity.this,Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), clipBitmap,
//	// null, null)), 100);
//	// ByteArrayOutputStream baos = new ByteArrayOutputStream();
//	// clipBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//	// Intent intent = new Intent(PictureSettingActivity.this,FilterChooseActivity.class);
//	// intent.putExtra("bitmap", baos.toByteArray());
//	// intent.putExtra("cropuri", path);
//	// startActivity(intent);
//	// // BitmapUtils.freeeBitmap(clipBitmap);
//	//
//	// }
//	// }
//	//
//	// }
//
//	/**
//	 * 初始化界面
//	 */
//	private void initView() {
//		// reImageView = (RelativeLayout) findViewById(R.id.re_image);
//		// tvSave = (TextView) findViewById(R.id.tv_pic_sure);
//		// tvSave.setOnClickListener(this);
//		// findViewById(R.id.tv_pic_cancel).setOnClickListener(this);
//		// ivRotate = (ImageView) findViewById(R.id.tv_pic_rotating);
//		// // cropImageView = (CropImageView) findViewById(R.id.civ_cropImg);
//		// findViewById(R.id.view_line_home).setVisibility(View.GONE);
//		// findViewById(R.id.re_image).setAlpha(80);
//		// ivRotate.setOnClickListener(this);
//		// try {
//		// cropImageView.setDrawable(new BitmapDrawable(getResources(), BitmapUtil.decodeUri(this, uri)), width, width);
//		// cropImageView.setTag(BitmapUtil.decodeUri(this,uri));
//		// } catch (Exception e) {
//		// e.printStackTrace();
//		// }
//		//
//
//		// srcPic = (ImageView) this.findViewById(R.id.src_pic);
//		// srcPic.setOnTouchListener(this);
//		// ViewTreeObserver observer = srcPic.getViewTreeObserver();
//		// observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
//		//
//		// @SuppressWarnings("deprecation")
//		// public void onGlobalLayout() {
//		// srcPic.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//		// initClipView(srcPic.getTop());
//		// }
//		// });
//
//	}
//
//	private void startFaceDetection() {
//		if (isRotate) {
//			initBitmap();
//		} else {
//			bitmapResoure = bitmap;
//		}
//
//	}
//
//	private void initBitmap() {
//		// Matrix matrix = new Matrix();
//		// matrix.setRotate(90);
//		// try {
//		// bitmapResoure = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//		// } catch (OutOfMemoryError e) {
//		// matrix.setScale((float) 1 / BitmapUtils.sampleSize, (float) 1 / BitmapUtils.sampleSize);
//		// bitmapResoure = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//		// e.printStackTrace();
//		// }
//
//	}
//
//	// 判断图片是不是偏转了90度
//	private boolean getIsRote90(String path) {
//		try {
//			ExifInterface exifInterface = new ExifInterface(path);
//
//			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//
//			if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
//				return true;
//			}
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return false;
//	}
//
//	@Override
//	protected void onStart() {
//		super.onStart();
//
//	}
//
//	/**
//	 * 初始化截图区域，并将源图按裁剪框比例缩放
//	 * 
//	 * @param top
//	 */
//	private void initClipView(final int top) {
//		// if (null != bitmapResoure) {
//		// clipview = new ClipView(PictureCropActivity.this);
//		// clipview.setCustomTopBarHeight(top);
//		// clipview.addOnDrawCompleteListener(new OnDrawListenerComplete() {
//		//
//		// public void onDrawCompelete() {
//		// clipview.removeOnDrawCompleteListener();
//		// // 控件的宽高
//		// int clipWidth = clipview.getClipWidth();
//		// int clipHeight = clipview.getClipHeight();
//		// // 屏幕的中间
//		// int midX = clipview.getClipLeftMargin() + (clipWidth / 2);
//		// int midY = clipview.getClipTopMargin() + (clipHeight / 2);
//		// // 图片的宽高
//		// int imageWidth = bitmapResoure.getWidth();
//		// int imageHeight = bitmapResoure.getHeight();
//		// // 按裁剪框求缩放比例
//		// clip_scale = (clipWidth * 1.0f) / imageWidth;
//		// if (imageWidth > imageHeight) {
//		// clip_scale = (clipHeight * 1.0f) / imageHeight;
//		// }
//		// // 起始中心点
//		// float imageMidX = imageWidth * clip_scale / 2;
//		// float imageMidY = clipview.getCustomTopBarHeight() + imageHeight * clip_scale / 2;
//		// srcPic.setScaleType(ScaleType.MATRIX);
//		// // 缩放
//		// matrix.postScale(clip_scale, clip_scale);
//		// matrix.postTranslate(midX - imageMidX, clipview.getClipTopMargin());
//		// srcPic.setImageMatrix(matrix);
//		// srcPic.setImageBitmap(bitmapResoure);
//		// srcPic.setTag(bitmapResoure);
//		//
//		// // 判断是不是gao的比例比kuan大；
//		// boolean isHeight = bitmapResoure.getWidth() > bitmapResoure.getHeight() ? true : false;
//		// if (!isHeight) {
//		// if (bitmapResoure.getHeight() > clipview.getClipHeight()) {
//		// minScaleR = (float) clipview.getClipHeight() / (float) bitmapResoure.getHeight();
//		// }
//		// }
//		// }
//		// });
//		//
//		// // LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//		// reImageView.addView(clipview, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//		// // reImageView.addContentView(clipview, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//		// }
//	}
//
//	public boolean onTouch(View v, MotionEvent event) {
//		ImageView view = (ImageView) v;
//		switch (event.getAction() & MotionEvent.ACTION_MASK) {
//			case MotionEvent.ACTION_DOWN:
//				savedMatrix.set(matrix);
//				// 设置开始点位置
//				start.set(event.getX(), event.getY());
//				mode = DRAG;
//				break;
//			case MotionEvent.ACTION_POINTER_DOWN:
//				oldDist = spacing(event);
//				if (oldDist > 10f) {
//					savedMatrix.set(matrix);
//					midPoint(mid, event);
//					mode = ZOOM;
//				}
//				break;
//			case MotionEvent.ACTION_UP:
//			case MotionEvent.ACTION_POINTER_UP:
//			case MotionEvent.ACTION_CANCEL:
//				if (mode == ZOOM) {
//					checkView();
//				}
//				mode = NONE;
//				break;
//			case MotionEvent.ACTION_MOVE: // 移动
//				if (mode == DRAG) {// 拖动
//					// if(!isDrag){
//					// break;
//					// }
//					bitmap = (Bitmap) srcPic.getTag();
//					DisplayMetrics displayMetrics = ViewUtil.getDisplayMetrics(PictureCropActivity.this);
//					int screenWith = displayMetrics.widthPixels;
//					int screenHeight = displayMetrics.heightPixels;
//					int clipWidth = clipview.getClipWidth();
//					int clipHeight = clipview.getClipHeight();
//					float viewLeft = srcPic.getLeft();
//					float viewX = srcPic.getX();
//					float view_right = srcPic.getRight();
//					// 图片的宽高
//					int imageWidth = srcPic.getWidth();
//					int imageHeight = srcPic.getHeight();
//					// 判断是否可以拖动
//					Log.i("", "imageHeight:" + imageHeight);
//					Log.i("", "clipHeight:" + clipHeight);
//					// if (imageHeight <= clipHeight) {
//					// break;
//					// }
//					// 获得图片最后的x,y坐标点
//					float lastx = (int) (event.getX() - start.x);
//					float lasty = (int) (event.getY() - start.y);
//					// 判断用户本次操作是否是x的操作
//					boolean isX = Math.abs(lastx) > Math.abs(lasty);
//
//					// Log.i(tag, "viewX:" + viewX);
//					// Log.i(tag, "lastx:" + lastx);
//
//					float[] p = new float[9];
//					matrix.getValues(p);
//					float scale = p[0];
//					float left = p[2];
//					float top = p[5];
//					matrix.set(savedMatrix);
//					matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
//
//					matrix.getValues(p);
//					scale = p[0];
//					left = p[2];
//					top = p[5];
//
//					float new_screenWidth = (bitmap.getWidth()) * scale;
//					float viewHeight = bitmapResoure.getHeight();
//					float new_screenHeight = (bitmap.getHeight()) * scale;
//					int view_90dp = ViewUtil.dip2px(appContext, 115);
//					int view_50dp = ViewUtil.dip2px(appContext, 50);
//					// float new_screenWidth = screenHeight * scale;
//					// 裁剪框的上边距
//					int cliptop_height = ((screenHeight - clipHeight) - view_90dp) / 2;
//
//					// 可以向上拖动的范围
//					int isCanDragTop = (int) (0 - new_screenHeight + clipHeight + cliptop_height);
//					// 可以向下拖动的范围
//					int isCanDragButtom = cliptop_height;
//					// 可以向左拖动的范围
//					int isCanDragLeft = (int) (screenWith - new_screenWidth);
//					// 可以向右拖动的范围
//					int isCanDragRight = 0;
//
//					// 图片限制在裁剪框
//					if (isX) {
//						// 条件追加 是否超过左右边缘
//						if (left >= 0) {
//							left = 0;
//						}
//						if (left <= isCanDragLeft) {
//							// 向上拖动会左拖动
//							left = isCanDragLeft;
//						}
//						// 检测下边缘
//						if (top >= isCanDragButtom) {
//							top = isCanDragButtom;
//						} // 检测上边缘
//						if (top <= isCanDragTop) {
//							top = isCanDragTop;
//						}
//
//					}
//					// y轴的移动
//					else {
//						// 检测下边缘
//						if (top >= isCanDragButtom) {
//							top = isCanDragButtom;
//						} // 检测上边缘
//						if (top <= isCanDragTop) {
//							top = isCanDragTop;
//						}
//						// 条件追加 是否超过左右边缘
//						if (left >= 0) {
//							left = 0;
//						}
//						if (left <= isCanDragLeft) {
//							// 向上拖动会左拖动
//							left = isCanDragLeft;
//						}
//					}
//					matrix.set(savedMatrix);
//					matrix.setTranslate(left, top);
//					matrix.postScale(scale, scale, left, top);
//
//				} else if (mode == ZOOM) {// 缩放
//					float newDist = spacing(event);
//					if (newDist > 10f) {
//						float scale = newDist / oldDist;
//						// ViewUtil.toast(appContext, "current_scale:" + scale);
//						// 判断是否可拖动
//						if (isDrag || scale < 1.0f) {
//							matrix.set(savedMatrix);
//							matrix.postScale(scale, scale, mid.x, mid.y);
//						}
//						// 检测是否可拖拽
//						float[] p = new float[9];
//						matrix.getValues(p);
//						isDrag = p[0] <= MAX_SCALE;
//					}
//				}
//				checkView();
//				break;
//		}
//		view.setImageMatrix(matrix);
//		return true;
//	}
//
//	ImageState mapState;
//
//	/** 最小缩放比例 */
//	float minScaleR = 0.8f;
//
//	/** 最大缩放比例 */
//	static final float MAX_SCALE = 2f;
//
//	/**
//	 * 通用检查视图片位置是否符合规定
//	 */
//	private void checkView() {
//		float[] p = new float[9];
//
//		matrix.getValues(p);
//		if (mode == ZOOM) {
//			// 判断缩放是否超过限定
//			if (p[0] <= clip_scale) {
//				resetClipView();
//				return;
//			} else if (p[0] >= MAX_SCALE) {
//				// matrix.set(savedMatrix);
//				// matrix.setScale(MAX_SCALE, MAX_SCALE);
//			}
//		}
//	}
//
//	private void center() {
//		center(true, true);
//	}
//
//	// 缩放到最小的时候是不是放在左上角
//	private void center(boolean horizontal, boolean vertical) {
//		Matrix m = new Matrix();
//		m.set(matrix);
//		RectF rectF = new RectF(0, 0, bitmapResoure.getWidth(), bitmapResoure.getHeight());
//		m.mapRect(rectF);
//
//		float height = rectF.height();
//		float width = rectF.width();
//
//		float deltaX = 0, deltaY = 0;
//		// if (MIN_SCALE) {
//		if (vertical) {
//			// 图片小于屏幕大小，则居中显示。大于屏幕，上方留空则往上移，下方留空则往下移
//			int screenHeight = ViewUtil.getDisplayMetrics(this).heightPixels;
//			if (height < screenHeight) {
//				deltaY = clipview.getClipTopMargin();
//			} else if (rectF.top > 0) {
//				deltaY = -rectF.top;
//			} else if (rectF.bottom < screenHeight) {
//				deltaY = srcPic.getHeight() - rectF.bottom;
//			}
//		}
//
//		if (horizontal) {
//			int screenWidth = ViewUtil.getDisplayMetrics(this).heightPixels;
//			if (width < screenWidth) {
//				deltaX = (ViewUtil.getDisplayMetrics(this).widthPixels - bitmapResoure.getWidth() * minScaleR) / 2;
//			} else if (rectF.left > 0) {
//				deltaX = -rectF.left;
//			} else if (rectF.right < screenWidth) {
//				deltaX = screenWidth - rectF.right;
//			}
//		}
//
//		// }
//		// else {
//		// if (vertical) {
//		// // 图片小于屏幕大小，则居中显示。大于屏幕，上方留空则往上移，下方留空则往下移
//		// int screenHeight = ViewUtil.getDisplayMetrics(this).heightPixels;
//		// if (height < screenHeight) {
//		// deltaY = (screenHeight - height) / 2 - rectF.top;
//		// } else if (rectF.top > 0) {
//		// deltaY = -rectF.top;
//		// } else if (rectF.bottom < screenHeight) {
//		// deltaY = srcPic.getHeight() - rectF.bottom;
//		// }
//		// }
//		//
//		// if (horizontal) {
//		// int screenWidth = ViewUtil.getDisplayMetrics(this).heightPixels;
//		// if (width < screenWidth) {
//		// deltaX = (screenWidth - width) / 2 - rectF.left;
//		// } else if (rectF.left > 0) {
//		// deltaX = -rectF.left;
//		// } else if (rectF.right < screenWidth) {
//		// deltaX = screenWidth - rectF.right;
//		// }
//		// }
//		// // Log.e("info", deltaY+" "+deltaX);
//		// }
//		matrix.postTranslate(deltaX, deltaY);
//
//	}
//
//	/**
//	 * 多点触控时，计算最先放下的两指距离
//	 * 
//	 * @param event
//	 * @return
//	 */
//	private float spacing(MotionEvent event) {
//		float x = event.getX(0) - event.getX(1);
//		float y = event.getY(0) - event.getY(1);
//		return (float) Math.sqrt(x * x + y * y);
//	}
//
//	/**
//	 * 多点触控时，计算最先放下的两指中心坐标
//	 * 
//	 * @param point
//	 * @param event
//	 */
//	private void midPoint(PointF point, MotionEvent event) {
//		float x = event.getX(0) + event.getX(1);
//		float y = event.getY(0) + event.getY(1);
//		point.set(x / 2, y / 2);
//	}
//
//	/**
//	 * 获取裁剪框内截图
//	 * 
//	 * @return
//	 */
//	private Bitmap getBitmap() {
//		// 获取截屏
//		View view = this.getWindow().getDecorView();
//		view.setDrawingCacheEnabled(true);
//		view.buildDrawingCache(true);
//		if (!isFinishing()) {
//			// 获取状态栏高度
//			Rect frame = new Rect();
//			this.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
//			int statusBarHeight = frame.top;
//			Bitmap finalBitmap = Bitmap.createBitmap(view.getDrawingCache(true), clipview.getClipLeftMargin(), clipview.getClipTopMargin()
//					+ statusBarHeight, clipview.getClipWidth(), clipview.getClipHeight());
//			view.setDrawingCacheEnabled(false);
//			// 释放资源\
//			view.destroyDrawingCache();
//			return finalBitmap;
//		}
//		return null;
//	}
//
//	Handler handler = new Handler() {
//
//		public void handleMessage(android.os.Message msg) {
//			CustomProgressDialog customProgressDialog = CustomProgressDialog.getInstance(PictureCropActivity.this);
//			customProgressDialog.createDiaLog("");
//		}
//	};
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//			case R.id.tv_pic_sure:
//				long time = System.currentTimeMillis();
//				System.out.println("time:" + time);
//				if (onclick_time != 0) {
//					if ((time - onclick_time) <= 7000) {
//						break;
//					}
//				} else {
//					onclick_time = time;
//				}
//				// 启动一个线程 用来保存数据
//				save1();
//				break;
//			case R.id.tv_pic_cancel:
//				finish();
//				break;
//			case R.id.tv_pic_rotating:// 图片旋转
//				setDegress(90);
//				break;
//			default:
//				break;
//		}
//	}
//
//	/**
//	 * 重置裁剪View
//	 */
//	private void resetClipView() {
//		// 控件的宽高
//		int clipWidth = clipview.getClipWidth();
//		int clipHeight = clipview.getClipHeight();
//		// 屏幕的中间
//		int midX = clipview.getClipLeftMargin() + (clipWidth / 2);
//		int midY = clipview.getClipTopMargin() + (clipHeight / 2);
//		// 图片的宽高
//		int imageHeight = bitmapResoure.getHeight();
//		int imageWidth = bitmapResoure.getWidth();
//		// int imageWidth = ((Bitmap) srcPic.getTag()).getWidth();
//		// int imageHeight =((Bitmap) srcPic.getTag()).getHeight();
//
//		// 按裁剪框求缩放比例
//		clip_scale = (clipWidth * 1.0f) / imageWidth;
//		if (imageWidth > imageHeight) {
//			clip_scale = (clipHeight * 1.0f) / imageHeight;
//		}
//
//		// 起始中心点
//		float imageMidX = imageWidth * clip_scale / 2;
//		float imageMidY = clipview.getCustomTopBarHeight() + imageHeight * clip_scale / 2;
//		// 缩放
//		matrix.set(savedMatrix);
//		matrix.setScale(clip_scale, clip_scale);
//		matrix.postTranslate(midX - imageMidX, clipview.getClipTopMargin());
//		// matrix.setTranslate(left, top);
//		// matrix.postScale(scale, scale, left, top);
//		// matrix.setTranslate(midX - imageMidX, clipview.getClipTopMargin());
//		// matrix.setScale(clip_scale, clip_scale, midX - imageMidX, clipview.getClipTopMargin());
//
//		float[] p = new float[9];
//		matrix.getValues(p);
//		float scale = p[0];
//		float left = p[2];
//		float top = p[5];
//		matrix.set(savedMatrix);
//		matrix.setTranslate(0, clipview.getClipTopMargin());
//		matrix.postScale(scale, scale, 0, clipview.getClipTopMargin());
//		// matrix.postScale(scale, scale, 0, clipview.getClipTopMargin());
//	}
//
//	/**
//	 * 保存图片
//	 */
//	private void save1() {
//		tvSave.setClickable(false);
//		Bitmap clipBitmap = getBitmap();
//		// 保存原图
//		if (null != clipBitmap) {
//			String path = BitmapUtils.compressBitmapDefault(PictureCropActivity.this, clipBitmap, 100, CompressFormat.PNG);
//			if (1 == flag) {
//				Intent intent = new Intent();
//				intent.setClass(getApplicationContext(), FilterChooseActivity.class);
//				intent.putExtra("cropuri", path);
//				tvSave.setClickable(true);
//				startActivity(intent);
//			} else if (2 == flag) {
//				Intent data = new Intent();
//				path = BitmapUtils.compressBitmapAboutSize(PictureCropActivity.this, path, 600);
//				data.putExtra("cut_path", path);
//				setResult(RESULT_OK, data);
//				BitmapUtils.freeeBitmap(bitmapResoure);
//				BitmapUtils.freeeBitmap(bitmap);
//				BitmapUtils.freeeBitmap(clipBitmap);
//				tvSave.setClickable(true);
//			}
//		}
//		ProgressLoading_dialog.DimssDialog();
//		finish();
//	}
//
//	/**
//	 * 设置偏移的角度
//	 */
//	private void setDegress(float degrees) {
//		Bitmap oldBitmap = (Bitmap) srcPic.getTag();
//		int widthBm = oldBitmap.getWidth();
//		int heightBM = oldBitmap.getHeight();
//		matrix.setRotate(degrees);
//		// matrix.postRotate(degrees);
//		Bitmap newBitmap = Bitmap.createBitmap(oldBitmap, 0, 0, widthBm, heightBM, matrix, true);
//		srcPic.setImageBitmap(newBitmap);
//		srcPic.setTag(newBitmap);
//		BitmapUtils.freeeBitmap(oldBitmap);
//		resetClipView();
//	}
//
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			BitmapUtils.freeeBitmap(bitmapResoure);
//			BitmapUtils.freeeBitmap(bitmap);
//			finish();
//		}
//		return true;
//	}
//}
