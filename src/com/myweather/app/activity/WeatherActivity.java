package com.myweather.app.activity;

import java.util.Calendar;

import com.myweather.app.R;
import com.myweather.app.database.MyWeatherDB;
import com.myweather.app.service.AutoUpdateService;
import com.myweather.app.util.HttpCallbackListener;
import com.myweather.app.util.HttpUtil;
import com.myweather.app.util.Utility;
import com.myweather.app.util.WeatherUrlUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherActivity extends Activity implements OnClickListener {
	/**
	 * 指数: index_f(基础接口)； index_v(常规接口)； 3天预报: forecast_f(基础接口)；
	 * forecast_v(常规接口)
	 */
	public static final String FORECAST_INTERFACE = "forecast_v";
	public static final String INDEX_INTERFACE = "index_v";
	private MyWeatherDB myWeatherDB;
	private LinearLayout wholeWeatherLayout;
	private LinearLayout weatherInfoLayout;
	/**
	 * 用于显示选中城市名
	 */
	private TextView cityNameTv;
	/**
	 * 用于显示当天气温
	 */
	private TextView tempTv;
	/**
	 * 用于显示当天天气现象描述信息
	 */
	private TextView weatherPheTv;
	/**
	 * 用于显示当天风向信息
	 */
	private TextView windDirTv;
	/**
	 * 用于显示当天风力信息
	 */
	private TextView windPowTv;
	/**
	 * 用于显示当天日期
	 */
	private TextView currentDateTv;
	/**
	 * 用于显示当天天气现象图片
	 */
	private ImageView weatherImg;
	/**
	 * 用于显示当天夜间天气现象图片
	 */
	private ImageView firstImg;
	/**
	 * 用于显示当天夜间天气现象
	 */
	private TextView firstWPheTv;
	/**
	 * 用于显示当天夜间天气温度
	 */
	private TextView firstTempTv;

	/**
	 * 用于显示第二天白天天气现象图片
	 */
	private ImageView secondDayImg;
	/**
	 * 用于显示第二天夜间天气现象图片
	 */
	private ImageView secondNightImg;
	/**
	 * 用于显示第二天天气现象
	 */
	private TextView secondWPheTv;
	/**
	 * 用于显示第二天天气温度
	 */
	private TextView secondTempTv;

	/**
	 * 用于显示第三天白天天气现象图片
	 */
	private ImageView thirdDayImg;
	/**
	 * 用于显示第三天天气现象图片
	 */
	private ImageView thirdNightImg;
	/**
	 * 用于显示第三天天气现象
	 */
	private TextView thirdWPheTv;
	/**
	 * 用于显示第三天天气温度
	 */
	private TextView thirdTempTv;
	/**
	 * 切换城市按钮
	 */
	private Button switchCity;
	/**
	 * 更新天气按钮
	 */
	private Button refreshWeather;

	private boolean pressFlag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);

		// 初始化各控件
		initView();

		myWeatherDB = MyWeatherDB.getInstance(this);

		String countyCode = getIntent().getStringExtra("county_code");

		if (!TextUtils.isEmpty(countyCode)) {
			// 有县级代号时就去查询天气
			currentDateTv.setText("同步中...");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameTv.setVisibility(View.INVISIBLE);
			queryWeatherCode(countyCode);
		} else {
			// 没有县级代号时就直接显示本地天气
			showWeather();
		}
		tempTv.setOnClickListener(this);
		switchCity.setOnClickListener(this);
		refreshWeather.setOnClickListener(this);
	}

	private void initView() {
		wholeWeatherLayout = (LinearLayout) findViewById(R.id.whole_weather_layout);
		
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		cityNameTv = (TextView) findViewById(R.id.city_name);
		tempTv = (TextView) findViewById(R.id.temp);
		weatherPheTv = (TextView) findViewById(R.id.weather_phenomenon);
		windDirTv = (TextView) findViewById(R.id.wind_direction);
		windPowTv = (TextView) findViewById(R.id.wind_power);
		currentDateTv = (TextView) findViewById(R.id.current_date);
		weatherImg = (ImageView) findViewById(R.id.weather_img);
		switchCity = (Button) findViewById(R.id.switch_city);
		refreshWeather = (Button) findViewById(R.id.refresh_weather);

		firstImg = (ImageView) findViewById(R.id.night_first_img);
		firstWPheTv = (TextView) findViewById(R.id.first_wphe);
		firstTempTv = (TextView) findViewById(R.id.first_temp);
		secondDayImg = (ImageView) findViewById(R.id.day_second_img);
		secondNightImg = (ImageView) findViewById(R.id.night_second_img);
		secondWPheTv = (TextView) findViewById(R.id.second_wphe);
		secondTempTv = (TextView) findViewById(R.id.second_temp);
		thirdDayImg = (ImageView) findViewById(R.id.day_third_img);
		thirdNightImg = (ImageView) findViewById(R.id.night_third_img);
		thirdWPheTv = (TextView) findViewById(R.id.third_wphe);
		thirdTempTv = (TextView) findViewById(R.id.third_temp);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.switch_city:
			Intent intent = new Intent(this, ChooseAreaActivity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;
		case R.id.refresh_weather:
			currentDateTv.setText("同步中...");
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(this);
			String weatherCode = prefs.getString("weather_code", "");
			if (!TextUtils.isEmpty(weatherCode)) {
				queryWeatherInfo(weatherCode, FORECAST_INTERFACE);
			}
			break;
		case R.id.temp:
			if (pressFlag) {
				SharedPreferences prefs1 = PreferenceManager
						.getDefaultSharedPreferences(this);
				String temp = "";
				if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= 18
						|| Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 8) {
					temp = prefs1.getString("nightTemp", "") + "\u00B0";
				} else {
					temp = prefs1.getString("dayTemp", "") + "\u00B0";
				}
				pressFlag = false;
				tempTv.setText(temp);
			} else {
				pressFlag = true;
				tempTv.setText("hello");
			}

			break;
		default:
			break;
		}
	}

	/**
	 * 查询县级代号所对应的天气代号。
	 */
	private void queryWeatherCode(String countyCode) {
		String address = "http://www.weather.com.cn/data/list3/city"
				+ countyCode + ".xml";
		queryFromServer(address, "countyCode");
	}

	/**
	 * 查询天气代号所对应的天气。
	 */
	private void queryWeatherInfo(String weatherCode, String type) {
		String address = WeatherUrlUtil.getInterfaceURL(weatherCode, type);
		queryFromServer(address, "weatherCode");
	}

	/**
	 * 根据传入的地址和类型去向服务器查询天气代号或者天气信息。
	 */
	private void queryFromServer(final String address, final String type) {
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			@Override
			public void onFinish(final String response) {
				if ("countyCode".equals(type)) {
					if (!TextUtils.isEmpty(response)) {
						// 从服务器返回的数据中解析出天气代号
						String[] array = response.split("\\|");
						if (array != null && array.length == 2) {
							String weatherCode = array[1];
							queryWeatherInfo(weatherCode, FORECAST_INTERFACE);
						}
					}
				} else if ("weatherCode".equals(type)) {
					// 处理服务器返回的天气信息
					Utility.gsonHandleWeatherResponse(WeatherActivity.this,
							myWeatherDB, response);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							showWeather();
						}
					});
				}
			}

			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						currentDateTv.setText("同步失败");
					}
				});
			}
		});
	}

	/**
	 * 从SharedPreferences文件中读取存储的天气信息,并显示到界面上。
	 */
	private void showWeather() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		String day_or_night = "";
		int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		if ((currentHour >= 18) || (currentHour < 8)) {
			day_or_night = "night";
		} else {
			day_or_night = "day";
		}
		cityNameTv.setText(prefs.getString("city_name", ""));

		tempTv.setText(prefs.getString(day_or_night + "Temp", "") + "\u00B0");
		weatherImg.setImageResource(getResources().getIdentifier(
				day_or_night.substring(0, 1)
						+ prefs.getString(day_or_night + "Wphe_code", ""),
				"drawable", getApplicationInfo().packageName));
		weatherPheTv.setText(prefs.getString(day_or_night + "Wphe_cname", ""));
		windDirTv.setText(prefs.getString(day_or_night + "Wdir_cname", ""));
		windPowTv.setText(prefs.getString(day_or_night + "Wpow_cname", ""));
		currentDateTv.setText("");
		firstImg.setImageResource(getResources().getIdentifier(
				"n" + prefs.getString("nightWphe_code", ""), "drawable",
				getApplicationInfo().packageName));
		firstWPheTv.setText(prefs.getString("nightWphe_cname", ""));
		firstTempTv.setText(prefs.getString("nightTemp", "") + "\u00B0");
		secondDayImg.setImageResource(getResources().getIdentifier(
				"d" + prefs.getString("second_dayWphe_code", ""), "drawable",
				getApplicationInfo().packageName));
		secondNightImg.setImageResource(getResources().getIdentifier(
				"n" + prefs.getString("second_nightWphe_code", ""), "drawable",
				getApplicationInfo().packageName));
		if (prefs.getString("second_dayWphe_code", "").equals(
				prefs.getString("second_nightWphe_code", ""))) {
			secondWPheTv.setText(prefs.getString("second_dayWphe_cname", ""));
		} else {
			secondWPheTv.setText(prefs.getString("second_dayWphe_cname", "")
					+ this.getResources().getString(R.string.turn_to)
					+ prefs.getString("second_nightWphe_cname", ""));
		}
		secondTempTv.setText(prefs.getString("second_dayTemp", "") + "/"
				+ prefs.getString("second_nightTemp", "") + "\u00B0");

		thirdDayImg.setImageResource(getResources().getIdentifier(
				"d" + prefs.getString("third_dayWphe_code", ""), "drawable",
				getApplicationInfo().packageName));
		thirdNightImg.setImageResource(getResources().getIdentifier(
				"n" + prefs.getString("third_nightWphe_code", ""), "drawable",
				getApplicationInfo().packageName));

		if (prefs.getString("third_dayWphe_code", "").equals(
				prefs.getString("third_nightWphe_code", ""))) {
			thirdWPheTv.setText(prefs.getString("third_dayWphe_cname", ""));
		} else {
			thirdWPheTv.setText(prefs.getString("third_dayWphe_cname", "")
					+ this.getResources().getString(R.string.turn_to)
					+ prefs.getString("third_nightWphe_cname", ""));
		}
		thirdTempTv.setText(prefs.getString("third_dayTemp", "") + "/"
				+ prefs.getString("third_nightTemp", "") + "\u00B0");

		wholeWeatherLayout.setBackgroundResource(getResources().getIdentifier(
				prefs.getString("province_name", ""), "drawable",
				getApplicationInfo().packageName));
		//蒙化各个省背景图片
		wholeWeatherLayout.getBackground().setAlpha(160);
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameTv.setVisibility(View.VISIBLE);

		Intent intent = new Intent(this, AutoUpdateService.class);
		startService(intent);
	}

	/**
	 * 捕获Back按键,根据当前的级别来判断,此时应该返回市列表、省列表、还是直接退出。
	 * */
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, ChooseAreaActivity.class);
		intent.putExtra("from_weather_activity", true);
		startActivity(intent);
		finish();
	}

}
