package com.myweather.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.myweather.app.database.MyWeatherDB;
import com.myweather.app.model.City;
import com.myweather.app.model.County;
import com.myweather.app.model.Province;
import com.myweather.app.model.WeatherJsonBean;

public class Utility {

	/**
	 * 解析和处理服务器返回的省级数据
	 */
	public synchronized static boolean handleProvincesResponse(
			MyWeatherDB myWeatherDB, String response) {
		if (!TextUtils.isEmpty(response)) {
			String[] allProvinces = response.split(",");
			if (allProvinces != null && allProvinces.length > 0) {
				for (String p : allProvinces) {
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					// 将解析出来的数据存储到Province表
					myWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * 解析和处理服务器返回的市级数据
	 */
	public static boolean handleCitiesResponse(MyWeatherDB myWeatherDB,
			String response, int provinceId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCities = response.split(",");
			if (allCities != null && allCities.length > 0) {
				for (String c : allCities) {
					String[] array = c.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					// 将解析出来的数据存储到City表
					myWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * 解析和处理服务器返回的县级数据
	 */
	public static boolean handleCountiesResponse(MyWeatherDB myWeatherDB,
			String response, int cityId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCounties = response.split(",");
			if (allCounties != null && allCounties.length > 0) {
				for (String c : allCounties) {
					String[] array = c.split("\\|");
					County county = new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(cityId);
					// 将解析出来的数据存储到County表
					myWeatherDB.saveCounty(county);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * Gson解析json数据，获取天气信息
	 * 
	 * @param jsonResponse
	 *            服务器响应返回的json天气数据字符串
	 * @return Gson解析Json数据后的JsonBean，即Weather对象
	 */
	public static void gsonHandleWeatherResponse(Context context,
			MyWeatherDB myWeatherDB, String jsonResponse) {
		try {
			Gson gson = new Gson();
			WeatherJsonBean weatherJsonBean = gson.fromJson(jsonResponse,
					WeatherJsonBean.class);
			saveWeather(context, myWeatherDB, weatherJsonBean);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 将服务器返回的所有天气信息存储到SharedPreferences文件中。
	 */
	public static void saveWeather(Context context, MyWeatherDB myWeatherDB,
			WeatherJsonBean weatherJsonBean) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd E",
				Locale.CHINA);
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(context).edit();
		// 获取选中城市的天气代号
		String weatherCode = weatherJsonBean.getC().getC1();
		// 获取选中城市的城市名字
		String city_name = weatherJsonBean.getC().getC3();
		//获取选中城市所在省的英文名
		String province_name=weatherJsonBean.getC().getC6();
		if(province_name.equals("shan-xi")){
			province_name="shan_xi";
		}
		// 获取天气预报的发布时间
		String publish_text = weatherJsonBean.getF().getF0().substring(0, 4)
				+ "." + weatherJsonBean.getF().getF0().substring(4, 6) + "."
				+ weatherJsonBean.getF().getF0().substring(6, 8) + " "
				+ weatherJsonBean.getF().getF0().substring(8, 10) + ":"
				+ weatherJsonBean.getF().getF0().substring(10, 12);
		// 获取当天白天温度
		String dayTemp = weatherJsonBean.getF().getF1().get(0).getFc();
		// 获取当天夜间温度
		String nightTemp = weatherJsonBean.getF().getF1().get(0).getFd();
		// 获取当天白天天气现象编号
		String dayWphe_code = weatherJsonBean.getF().getF1().get(0).getFa();
		// 根据天气现象编号从数据库查询天气现象
		String dayWphe_cname = myWeatherDB.loadWeatherPhenomenon(dayWphe_code)
				.getWphe_cname();
		// 获取当天夜间天气现象编号
		String nightWphe_code = weatherJsonBean.getF().getF1().get(0).getFb();
		// 根据天气现象编号从数据库查询天气现象
		String nightWphe_cname = myWeatherDB.loadWeatherPhenomenon(
				nightWphe_code).getWphe_cname();
		// 获取当天白天风向编号
		String dayWdir_code = weatherJsonBean.getF().getF1().get(0).getFe();
		// 根据风向编号从数据库查询风向
		String dayWdir_cname = myWeatherDB.loadWindDirection(dayWdir_code)
				.getWdir_cname();
		// 获取当天夜间风向编号
		String nightWdir_code = weatherJsonBean.getF().getF1().get(0).getFf();
		// 根据风向编号从数据库查询风向
		String nightWdir_cname = myWeatherDB.loadWindDirection(nightWdir_code)
				.getWdir_cname();
		// 获取当天白天风力编号
		String dayWpow_code = weatherJsonBean.getF().getF1().get(0).getFg();
		// 根据风力编号从数据库查询风力
		String dayWpow_cname = myWeatherDB.loadWindPower(dayWpow_code)
				.getWpow_cname();
		// 获取当天夜间风力编号
		String nightWpow_code = weatherJsonBean.getF().getF1().get(0).getFh();
		// 根据风力编号从数据库查询风力
		String nightyWpow_cname = myWeatherDB.loadWindPower(nightWpow_code)
				.getWpow_cname();
		// 获取日出日落时间并解析
		String[] rise_set = weatherJsonBean.getF().getF1().get(0).getFi()
				.split("\\|");
		String sunrise = rise_set[0];
		String sunset = rise_set[1];

		// 获取第二天白天温度
		String second_dayTemp = weatherJsonBean.getF().getF1().get(1).getFc();
		// 获取第二天夜间温度
		String second_nightTemp = weatherJsonBean.getF().getF1().get(1).getFd();
		// 获取第二天白天天气现象编号
		String second_dayWphe_code = weatherJsonBean.getF().getF1().get(1)
				.getFa();
		// 根据天气现象编号从数据库查询天气现象
		String second_dayWphe_cname = myWeatherDB.loadWeatherPhenomenon(
				second_dayWphe_code).getWphe_cname();
		// 获取第二天夜间天气现象编号
		String second_nightWphe_code = weatherJsonBean.getF().getF1().get(1)
				.getFb();
		// 根据天气现象编号从数据库查询天气现象
		String second_nightWphe_cname = myWeatherDB.loadWeatherPhenomenon(
				second_nightWphe_code).getWphe_cname();

		// 获取第三天白天温度
		String third_dayTemp = weatherJsonBean.getF().getF1().get(2).getFc();
		// 获取第三天夜间温度
		String third_nightTemp = weatherJsonBean.getF().getF1().get(2).getFd();
		// 获取第三天白天天气现象编号
		String third_dayWphe_code = weatherJsonBean.getF().getF1().get(2)
				.getFa();
		// 根据天气现象编号从数据库查询天气现象
		String third_dayWphe_cname = myWeatherDB.loadWeatherPhenomenon(
				third_dayWphe_code).getWphe_cname();
		// 获取第三天夜间天气现象编号
		String third_nightWphe_code = weatherJsonBean.getF().getF1().get(2)
				.getFb();
		// 根据天气现象编号从数据库查询天气现象
		String third_nightWphe_cname = myWeatherDB.loadWeatherPhenomenon(
				third_nightWphe_code).getWphe_cname();
		
		// 获取第二天日出日落时间并解析
				String[] second_rise_set = weatherJsonBean.getF().getF1().get(1).getFi()
						.split("\\|");
				String second_sunrise = second_rise_set[0];
				String second_sunset = second_rise_set[1];

		editor.putBoolean("city_selected", true);
		editor.putString("city_name", city_name);
		editor.putString("province_name", province_name);
		editor.putString("publish_text", publish_text);
		editor.putString("dayTemp", dayTemp);
		editor.putString("nightTemp", nightTemp);
		editor.putString("dayWphe_code", dayWphe_code);
		editor.putString("dayWphe_cname", dayWphe_cname);
		editor.putString("nightWphe_code", nightWphe_code);
		editor.putString("nightWphe_cname", nightWphe_cname);
		editor.putString("dayWdir_code", dayWdir_code);
		editor.putString("dayWdir_cname", dayWdir_cname);
		editor.putString("nightWdir_code", nightWdir_code);
		editor.putString("nightWdir_cname", nightWdir_cname);
		editor.putString("dayWpow_code", dayWpow_code);
		editor.putString("dayWpow_cname", dayWpow_cname);
		editor.putString("nightWpow_code", nightWpow_code);
		editor.putString("nightWpow_cname", nightyWpow_cname);
		editor.putString("sunrise", sunrise);
		editor.putString("sunset", sunset);
		editor.putString("current_date", sdf.format(new Date()));
		editor.putString("weather_code", weatherCode);

		editor.putString("second_dayTemp", second_dayTemp);
		editor.putString("second_nightTemp", second_nightTemp);
		editor.putString("second_dayWphe_code", second_dayWphe_code);
		editor.putString("second_nightWphe_code", second_nightWphe_code);
		editor.putString("second_dayWphe_cname", second_dayWphe_cname);
		editor.putString("second_nightWphe_cname", second_nightWphe_cname);
		editor.putString("second_sunrise", second_sunrise);
		editor.putString("second_sunset", second_sunset);
		
		editor.putString("third_dayTemp", third_dayTemp);
		editor.putString("third_nightTemp", third_nightTemp);
		editor.putString("third_dayWphe_code", third_dayWphe_code);
		editor.putString("third_nightWphe_code", third_nightWphe_code);
		editor.putString("third_dayWphe_cname", third_dayWphe_cname);
		editor.putString("third_nightWphe_cname", third_nightWphe_cname);
		
		
		editor.commit();
	}

}
