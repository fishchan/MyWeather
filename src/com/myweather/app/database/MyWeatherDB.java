package com.myweather.app.database;

import java.util.ArrayList;
import java.util.List;

import com.myweather.app.model.City;
import com.myweather.app.model.County;
import com.myweather.app.model.CodeBean;
import com.myweather.app.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MyWeatherDB {
	/**
	 * 数据库名
	 */
	public static final String DB_NAME = "my_weather";
	/**
	 * 数据库版本
	 */
	public static final int VERSION = 1;
	private static MyWeatherDB myWeatherDB;
	private SQLiteDatabase db;

	/**
	 * 将构造方法私有化
	 */
	private MyWeatherDB(Context context) {
		MyWeatherOpenHelper dbHelper = new MyWeatherOpenHelper(context,
				DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}

	/**
	 * 获取MyWeatherDB的实例。
	 */
	public synchronized static MyWeatherDB getInstance(Context context) {
		if (myWeatherDB == null) {
			myWeatherDB = new MyWeatherDB(context);
		}
		return myWeatherDB;
	}

	/**
	 * 将Province实例存储到数据库。
	 */
	public void saveProvince(Province province) {
		if (province != null) {
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("Province", null, values);
		}
	}

	/**
	 * 从数据库读取全国所有的省份信息。
	 */
	public List<Province> loadProvinces() {
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db
				.query("Province", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor
						.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor
						.getColumnIndex("province_code")));
				list.add(province);
			} while (cursor.moveToNext());
		}
		return list;
	}

	/**
	 * 将City实例存储到数据库。
	 */
	public void saveCity(City city) {
		if (city != null) {
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_id", city.getProvinceId());
			db.insert("City", null, values);
		}
	}

	/**
	 * 从数据库读取某省下所有的城市信息。
	 */
	public List<City> loadCities(int provinceId) {
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_id = ?",
				new String[] { String.valueOf(provinceId) }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor
						.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor
						.getColumnIndex("city_code")));
				city.setProvinceId(provinceId);
				list.add(city);
			} while (cursor.moveToNext());
		}
		return list;
	}

	/**
	 * 将County实例存储到数据库。
	 */
	public void saveCounty(County county) {
		if (county != null) {
			ContentValues values = new ContentValues();
			values.put("county_name", county.getCountyName());
			values.put("county_code", county.getCountyCode());
			values.put("city_id", county.getCityId());
			db.insert("County", null, values);
		}
	}

	/**
	 * 从数据库读取某城市下所有的县信息。
	 */
	public List<County> loadCounties(int cityId) {
		List<County> list = new ArrayList<County>();
		Cursor cursor = db.query("County", null, "city_id = ?",
				new String[] { String.valueOf(cityId) }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				County county = new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountyName(cursor.getString(cursor
						.getColumnIndex("county_name")));
				county.setCountyCode(cursor.getString(cursor
						.getColumnIndex("county_code")));
				county.setCityId(cityId);
				list.add(county);
			} while (cursor.moveToNext());
		}
		return list;
	}

	/**
	 * 从数据库读取某天气现象编号的信息。
	 */
	public CodeBean.WeatherPhenomenon loadWeatherPhenomenon(String code) {
		CodeBean.WeatherPhenomenon wphe = new CodeBean.WeatherPhenomenon();
		Cursor cursor = db.query("WeatherPhenomenon", null, "wphe_code = ?",
				new String[] { code }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				wphe.setWphe_cname(cursor.getString(cursor
						.getColumnIndex("wphe_cname")));
				wphe.setWphe_ename(cursor.getString(cursor
						.getColumnIndex("wphe_ename")));
				wphe.setWphe_code(cursor.getString(cursor
						.getColumnIndex("wphe_code")));
			} while (cursor.moveToNext());
		}
		return wphe;
	}

	/**
	 * 从数据库读取某风向编号的信息。
	 */
	public CodeBean.WindDirection loadWindDirection(String code) {
		CodeBean.WindDirection wdir = new CodeBean.WindDirection();
		Cursor cursor = db.query("WindDirection", null, "wdir_code = ?",
				new String[] { code }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				wdir.setWdir_cname(cursor.getString(cursor
						.getColumnIndex("wdir_cname")));
				wdir.setWdir_ename(cursor.getString(cursor
						.getColumnIndex("wdir_ename")));
				wdir.setWdir_code(cursor.getString(cursor
						.getColumnIndex("wdir_code")));
			} while (cursor.moveToNext());
		}
		return wdir;
	}

	/**
	 * 从数据库读取某风力编号的信息。
	 */
	public CodeBean.WindPower loadWindPower(String code) {
		CodeBean.WindPower wpow = new CodeBean.WindPower();
		Cursor cursor = db.query("WindPower", null, "wpow_code = ?",
				new String[] { code }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				wpow.setWpow_cname(cursor.getString(cursor
						.getColumnIndex("wpow_cname")));
				wpow.setWpow_ename(cursor.getString(cursor
						.getColumnIndex("wpow_ename")));
				wpow.setWpow_code(cursor.getString(cursor
						.getColumnIndex("wpow_code")));
			} while (cursor.moveToNext());
		}
		return wpow;
	}

}
