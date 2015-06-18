package com.wheelview.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.Arrays;

public class MainActivity extends Activity implements OnClickListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    private static final String[] PLANETS = new String[] { "Mercury", "Venus", "Earth", "Mars", "Jupiter", "Uranus", "Neptune", "Pluto" };

    private WheelView wva;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wva = (WheelView) findViewById(R.id.main_wv);

        findViewById(R.id.main_show_dialog_btn).setOnClickListener(this);

        wva.setOffset(1);//设置偏移量(选中框上面和下面显示个数)
        wva.setItems(Arrays.asList(PLANETS));//设置数据
        //选择器的选中的事件
        wva.setOnWheelViewListener(new WheelView.OnWheelViewListener() {

            @Override
            public void onSelected(int selectedIndex, String item) {
                // Logger.d(TAG, "selectedIndex: " + selectedIndex + ", item: " + item);
            }
        });

    }

    public void onClickCallbackSample(View view) {
        switch (view.getId()) {
            case R.id.main_show_dialog_btn:
                View outerView = LayoutInflater.from(MainActivity.this).inflate(R.layout.wheel_view, null);
                WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
                wv.setOffset(2);
                wv.setItems(Arrays.asList(PLANETS));
                wv.setSeletion(3);
                wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {

                    @Override
                    public void onSelected(int selectedIndex, String item) {
                        // Logger.d(TAG, "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
                    }
                });

                new AlertDialog.Builder(MainActivity.this).setTitle("WheelView in Dialog").setView(outerView).setPositiveButton("OK", null).show();

                break;
        }
    }

    @Override
    public void onClick(View v) {
        onClickCallbackSample(v);
    }

}
