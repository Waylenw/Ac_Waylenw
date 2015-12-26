package com.au.commons.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

/**
 * 图片处理工具类
 */
public class BitmapUtils {

    /**
     * app包名
     */
    public static String app_packagename = "";

    /**
     * 帖子图片的大小
     */
    public static int postpic_width = 640;

    /**
     * 帖子图片的压缩率
     */
    public static int postpic_compresssize = 95;

    /**
     * 我的头像截取的大小
     */
    public static int mypic_szie = 640;

    private static final int DEFAULT_WIDTH = 2000;

    private static final int DEFAULT_HEIGHT = 2000;

    public static int sampleSize = 1;

    //

    /**
     * 通过文件路径获取到bitmap,并将文件压缩成制定宽高
     *
     * @param path
     * @param w
     * @param h
     * @return
     */
    public static Bitmap getBitmapFromPath(String path, int w, int h) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为ture只获取图片大小
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        // 返回为空
        BitmapFactory.decodeFile(path, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        float scaleWidth = 0.f, scaleHeight = 0.f;
        if (width > w || height > h) {
            // 缩放
            scaleWidth = ((float) width) / w;
            scaleHeight = ((float) height) / h;
        }
        opts.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth, scaleHeight);
        opts.inSampleSize = (int) scale;
        WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
        return Bitmap.createScaledBitmap(weak.get(), w, h, true);
    }

    /**
     * 创建BitMap减少内存消耗
     *
     * @param bitmap
     * @return
     */
    public static Bitmap decodeFileForRGB(String path) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Config.RGB_565;
        opt.inSampleSize = 2;
        opt.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, opt);
    }

    /**
     * 压缩bitmap根据RGB
     *
     * @param bitmap
     * @return
     */
    public static Bitmap compressBitmapForRGB(Bitmap bitmap) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Config.RGB_565;
        opt.inJustDecodeBounds = false;
        opt.inSampleSize = 1;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 100, out);
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        freeeBitmap(bitmap);
        freeeOutputStream(out);
        Bitmap newbitmap = BitmapFactory.decodeStream(in, null, opt);
        return newbitmap;
    }

    /**
     * 释放bitmap
     *
     * @param bitmap
     */
    public static void freeeBitmap(Bitmap bitmap) {
        // 判断是否为空或者已经释放
        if (bitmap != null || !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        System.gc();
    }

    /**
     * 默认的bitmap压缩==》默认保存到sd/yueniapp/下 以原大小压缩
     *
     * @param bitmap
     * @return
     */
    public static String compressBitmapDefault(Context context, Bitmap bitmap, int compressSize, CompressFormat type) {
        AssetFileDescriptor fileDescriptor = null;
        FileOutputStream fileOutputStream = null;
        String path = BitmapUtils.getProjectSDCardPath(context);
        try {
            fileOutputStream = new FileOutputStream(new File(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        bitmap.compress(type, compressSize, fileOutputStream);
        try {
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fileOutputStream = null;
        }
        return path;
    }

    /**
     * bitmap压缩 根据图片大小
     *
     * @param oldpath
     * @param size    图片的宽度或者高度
     * @return
     */
    public static String compressBitmapAboutSize(Context context, String oldpath, int size) {
        // BitmapFactory.Options options = new BitmapFactory.Options();
        // options.inSampleSize = ImageUtil.computeSampleSize(options, -1, size * size);
        Bitmap bitmap = BitmapFactory.decodeFile(oldpath/*, options*/);
        Bitmap crop_bitmap = ThumbnailUtils.extractThumbnail(bitmap, size, size);
        BitmapUtils.freeeBitmap(bitmap);
        return BitmapUtils.compressBitmapDefault(context, crop_bitmap, postpic_compresssize, CompressFormat.JPEG);
    }

    /**
     * bitmap压缩根据穿传入的地址
     *
     * @param bitmap
     * @return
     */
    public static String compressBitmapAboutPath(Context context, Bitmap bitmap, String path) {
        AssetFileDescriptor fileDescriptor = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(new File(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        bitmap.compress(CompressFormat.PNG, 100, fileOutputStream);
        return path;
    }

    /**
     * 获得项目SD卡的位置
     *
     * @param bitmap
     */
    public static String getProjectSDCardPath(Context context) {
        String sdcard_path = "";
        // 判断SD卡是否为空
        if (!Environment.MEDIA_REMOVED.equals(Environment.getExternalStorageState())) {
            sdcard_path = Environment.getExternalStorageDirectory().getAbsolutePath();
            String strDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            if (app_packagename.equals("")) {
                app_packagename = "";
            }
            sdcard_path = sdcard_path + File.separator + app_packagename + File.separator + "ypcrop" + strDate + ".jpg";
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + app_packagename);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        return sdcard_path;
    }

    /**
     * 将网络贴纸保存到本地
     *
     * @param url
     * @return
     */
    public static void GeBitmapForUri(final Context context, final String url) {
        HttpPost postMethod = new HttpPost(url);
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = null;
        InputStream inputStream = null;
        Bitmap bitmap = null;
        try {
            response = httpClient.execute(postMethod);
            inputStream = response.getEntity().getContent();
            byte[] data = readInputStream(inputStream); // 获得图片的二进制数据
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String path = BitmapUtils.compressBitmapDefault(context, bitmap, 100, CompressFormat.PNG);
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
    }

    /**
     * @param drawable drawable ת Bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888 : Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 此处写方法描述
     *
     * @param intent
     * @return void
     * @Title: getBitmap
     * @date 2012-12-13 下午8:22:23
     */

    public static Bitmap getBitmap(Uri targetUri, Context context) {
        InputStream is = null;
        int width = 0;
        int height = 0;
        Bitmap mBitmap;
        try {
            try {
                is = getInputStream(targetUri, context);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // shark 如果图片太大的话，压缩
            while ((width / sampleSize > DEFAULT_WIDTH * 2) || (height / sampleSize > DEFAULT_HEIGHT * 2)) {
                sampleSize *= 2;
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = sampleSize;

            mBitmap = BitmapFactory.decodeStream(is, null, options);

            // 缩放图片的尺寸
            int ww = mBitmap.getWidth();
            int wh = mBitmap.getHeight();
            float scaleWidth = 0;
            if (ww > wh) {
                scaleWidth = (float) ViewUtil.getDisplayMetrics(context).widthPixels / wh;
            } else {
                scaleWidth = (float) ViewUtil.getDisplayMetrics(context).widthPixels / ww;
            }

            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleWidth);
            // 产生缩放后的Bitmap对象
            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, ww, wh, matrix, false);

        } finally {
            if (is != null) {
                try {
                    is.close();
                    is = null;
                    System.gc();
                } catch (IOException ignored) {
                }
            }
        }
        return mBitmap;
    }

    /**
     * 获取输入流
     *
     * @param mUri
     * @return InputStream
     * @Title: getInputStream
     * @date 2012-12-14 上午9:00:31
     */
    public static InputStream getInputStream(Uri mUri, Context context) throws IOException {
        try {
            if (mUri.getScheme().equals("file")) {
                return new java.io.FileInputStream(mUri.getPath());
            } else {
                return context.getContentResolver().openInputStream(mUri);
            }
        } catch (FileNotFoundException ex) {
            return null;
        }
    }

    /**
     * 获得图片Bitmap不压缩
     *
     * @param intent
     * @return void
     * @Title: getBitmap
     * @date 2012-12-13 下午8:22:23
     */
    public static Bitmap getBitmapOld(Uri targetUri, Context context) {
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            is = getInputStream(targetUri, context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bitmap = BitmapFactory.decodeStream(is);
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            is = null;
        }
        return bitmap;
    }

    /*
    * 从数据流中获得数据
    */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        // bos.close();
        return bos.toByteArray();

    }

    /**
     * 释放bitmap
     *
     * @param bitmap
     */
    public static void freeeOutputStream(Object stream) {
        if (stream instanceof ByteArrayOutputStream) {
            ByteArrayOutputStream outputFormat = (ByteArrayOutputStream) stream;
            try {
                outputFormat.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                outputFormat = null;
            }
        }
        System.gc();
    }
}
