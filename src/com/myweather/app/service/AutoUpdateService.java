package com.myweather.app.service;

import com.myweather.app.MyApplication;
import com.myweather.app.activity.WeatherActivity;
import com.myweather.app.database.MyWeatherDB;
import com.myweather.app.receiver.AutoUpdateReceiver;
import com.myweather.app.util.HttpCallbackListener;
import com.myweather.app.util.HttpUtil;
import com.myweather.app.util.Utility;
import com.myweather.app.util.WeatherUrlUtil;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

public class AutoUpdateService extends Service {
	Context c = MyApplication.getInstance();
	MyWeatherDB myWeatherDB = MyWeatherDB.getInstance(c);

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				updateWeather();
			}
		}).start();
		AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
		int anHour = 8 * 60 * 60 * 1000; // 这是8小时的毫秒数
		long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
		Intent i = new Intent(this, AutoUpdateReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 更新天气信息。
	 */
	private void updateWeather() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		String weatherCode = prefs.getString("weather_code", "");
		String address = WeatherUrlUtil.getInterfaceURL(weatherCode,
				WeatherActivity.FORECAST_INTERFACE);
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			@Override
			public void onFinish(String response) {
				Utility.gsonHandleWeatherResponse(AutoUpdateService.this,
						myWeatherDB, response);
			}

			@Override
			public void onError(Exception e) {
				e.printStackTrace();
			}
		});
	}

}
