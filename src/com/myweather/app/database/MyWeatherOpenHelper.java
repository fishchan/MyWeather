package com.myweather.app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyWeatherOpenHelper extends SQLiteOpenHelper {

	/**
	 * Province表建表语句
	 */
	// id 自增长主键
	// province_name 表示省名
	// province_code 表示省级代号

	public static final String CREATE_PROVINCE = "create table Province ("
			+ "id integer primary key autoincrement, " + "province_name text, "
			+ "province_code text)";
	/**
	 * City表建表语句
	 */
	// id 自增长主键
	// city_name 表示城市名
	// city_code 表示市级代号
	// province_id City表关联Province表的外键

	public static final String CREATE_CITY = "create table City ("
			+ "id integer primary key autoincrement, " + "city_name text, "
			+ "city_code text, " + "province_id integer)";
	/**
	 * County表建表语句
	 */
	// id 自增长主键
	// county_name 表示县名
	// county_code 表示县级代号
	// city_id County表关联City表的外键

	public static final String CREATE_COUNTY = "create table County ("
			+ "id integer primary key autoincrement, " + "county_name text, "
			+ "county_code text, " + "city_id integer)";

	public MyWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 执行SQL语句，创建Province表
		db.execSQL(CREATE_PROVINCE);
		// 执行SQL语句，创建City表
		db.execSQL(CREATE_CITY);
		// 执行SQL语句，创建County表
		db.execSQL(CREATE_COUNTY);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
