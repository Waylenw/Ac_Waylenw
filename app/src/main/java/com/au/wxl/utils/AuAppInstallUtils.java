package com.au.wxl.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 *
 *
 * Created  on 2015/6/11.
 */
public class AuAppInstallUtils {
    /**
     * 安装
     *
     * @param context
     *            接收外部传进来的context
     */
    public static void install(Context context,String mUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(mUrl)),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }


    /**
     * 卸载
     *
     * @param context
     *            接收外部传进来的context
     */
    public static void Unintall(Context context,String packname) {
        Uri packageURI = Uri.parse("package:"+packname);
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        context.startActivity(uninstallIntent);
    }

}
