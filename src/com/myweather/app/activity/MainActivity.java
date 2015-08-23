package com.myweather.app.activity;

import com.myweather.app.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
	// 手动设置城市Button
	private Button manualBtn;
	// 自动定位城市Button
	private Button locationBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		// 得到布局中的控件对象
		manualBtn = (Button) findViewById(R.id.manualBtn);
		locationBtn = (Button) findViewById(R.id.locationBtn);
		// 注册点击监听器
		manualBtn.setOnClickListener(this);
		locationBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.manualBtn:
			// 启动手动设置城市的Activity
			Intent intent = new Intent(MainActivity.this,
					ChooseAreaActivity.class);
			startActivity(intent);
			break;
		case R.id.locationBtn:
			// 启动自动定位城市的Activity

			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
