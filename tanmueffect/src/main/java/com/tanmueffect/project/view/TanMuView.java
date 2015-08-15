package com.tanmueffect.project.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 实现公告栏(弹幕)的效果
 * <p/>
 * Created by wxl on 2015/8/15.
 */
public class TanMuView extends View {

    private int tX = 1080;//文件的X坐标

    public TanMuView(Context context) {
        super(context);
        viewThread.start();
    }

    public TanMuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        viewThread.start();
    }

    public TanMuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        viewThread.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setTextSize(30);
        canvas.drawText("通知:十月一号放假20天", tX, 30, paint);
    }

    Thread viewThread = new Thread() {

        @Override
        public void run() {
            super.run();
            while (true) {
                    tX -=30;
                    try {
                        Thread.sleep(200);
                        postInvalidate();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (tX<=-200) {
                        tX =1100;
                    }
            }
        }
    };
}
