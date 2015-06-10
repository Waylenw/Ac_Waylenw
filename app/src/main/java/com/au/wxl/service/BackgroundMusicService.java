package com.au.wxl.service;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.au.wxl.R;

/**
 * 播放背景音乐
 *
 */
public class BackgroundMusicService  extends Service{

	private MediaPlayer mediaPlayer;
	@Override
	public IBinder onBind(Intent intent) {
	// TODO Auto-generated method stub
	return null;
	}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent,flags, startId);
        if(mediaPlayer==null){
            //开始播放背景音
//			mediaPlayer = MediaPlayer.create(this, R.raw.discovery);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
        return super.onStartCommand(intent, flags, startId);
    }



	@Override
	public void onDestroy() {
		super.onDestroy();
		mediaPlayer.stop();
	}
}

//配置
//<service android:name="com.linnan.xzlm.music.MusicServer">
//<intent-filter>
//<action android:name="com.angel.Android.MUSIC"/>
//<category android:name="android.intent.category.default" />
//</intent-filter>



//使用
//Intent intent = new Intent("com.angel.Android.MUSIC");
//startService(intent);
//this.stopService(intent);