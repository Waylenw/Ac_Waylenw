package com.loveplusplus.demo.image;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button btn = (Button) findViewById(R.id.button1);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ttt();
			}
		});

	}

	private void ttt() {
		Intent intent = new Intent(this, ImagePagerActivity.class);
		//图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra("images", T.imageUrls);
		startActivity(intent);
	}

}
