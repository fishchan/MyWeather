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

	/**
	 * WeatherPhenomenon表建表语句
	 */
	// id 自增长主键
	// wphe_cname天气现象中文名称
	// wphe_ename天气现象英文名称
	// wphe_code天气现象编号
	public static final String CREATE_WEATHERPHENOMENON = "create table WeatherPhenomenon("
			+ "id integer primary key autoincrement, "
			+ "wphe_cname text, "
			+ "wphe_ename text, " + "wphe_code text)";

	/**
	 * WindDirection表建表语句
	 */
	// id 自增长主键
	// wdir_cname风向中文名称
	// wdir_ename风向英文名称
	// wdir_code风向编号
	public static final String CREATE_WINDDIRECTION = "create table WindDirection("
			+ "id integer primary key autoincrement, "
			+ "wdir_cname text, "
			+ "wdir_ename text, " + "wdir_code text)";

	/**
	 * WindPower表建表语句
	 */
	// id 自增长主键
	// wpow_cname风力中文名称
	// wpow_ename风力英文名称
	// wpow_code风力编号
	public static final String CREATE_WINDPOWER = "create table WindPower("
			+ "id integer primary key autoincrement, " + "wpow_cname text, "
			+ "wpow_ename text, " + "wpow_code text)";

	public static final String INSERTWPHE = "insert into WeatherPhenomenon (wphe_cname,wphe_ename,wphe_code) values(?,?,?)";
	public static final String INSERTWDIR = "insert into WindDirection (wdir_cname,wdir_ename,wdir_code) values(?,?,?)";
	public static final String INSERTWPOW = "insert into WindPower (wpow_cname,wpow_ename,wpow_code) values(?,?,?)";

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
		// 执行SQL语句，创建WeatherPhenomenon表
		db.execSQL(CREATE_WEATHERPHENOMENON);
		// 执行SQL语句，创建WindDirection表
		db.execSQL(CREATE_WINDDIRECTION);
		// 执行SQL语句，创建WindPower表
		db.execSQL(CREATE_WINDPOWER);

		 db.execSQL(INSERTWPHE,new String[] { "晴", "Sunny", "00" });
		 db.execSQL(INSERTWPHE,new String[] { "多云", "Cloudy", "01" });
		 db.execSQL(INSERTWPHE,new String[] { "阴", "Overcast", "02" });
		 db.execSQL(INSERTWPHE,new String[] { "阵雨", "Shower", "03" });
		 db.execSQL(INSERTWPHE,new String[] { "雷阵雨", "Thundershower", "04" });
		 db.execSQL(INSERTWPHE,new String[] { "雷阵雨伴有冰雹", "Thundershower with hail", "05" });
		 db.execSQL(INSERTWPHE,new String[] { "雨夹雪", "Sleet", "06" });
		 db.execSQL(INSERTWPHE,new String[] { "小雨", "Light rain", "07" });
		 db.execSQL(INSERTWPHE,new String[] { "中雨", "Moderate rain", "08" });
		 db.execSQL(INSERTWPHE,new String[] { "大雨", "Heavy rain", "09" });
		 db.execSQL(INSERTWPHE,new String[] { "暴雨", "Storm", "10" });
		 db.execSQL(INSERTWPHE,new String[] { "大暴雨", "Heavy storm", "11" });
		 db.execSQL(INSERTWPHE,new String[] { "特大暴雨", "Severe storm", "12" });
		 db.execSQL(INSERTWPHE,new String[] { "阵雪", "Snow flurry", "13" });
		 db.execSQL(INSERTWPHE,new String[] { "小雪", "Light snow", "14" });
		 db.execSQL(INSERTWPHE,new String[] { "中雪", "Moderate snow", "15" });
		 db.execSQL(INSERTWPHE,new String[] { "大雪", "Heavy snow", "16" });
		 db.execSQL(INSERTWPHE,new String[] { "暴雪", "Snowstorm", "17" });
		 db.execSQL(INSERTWPHE,new String[] { "雾", "Foggy", "18" });
		 db.execSQL(INSERTWPHE,new String[] { "冻雨", "Ice rain", "19" });
		 db.execSQL(INSERTWPHE,new String[] { "沙尘暴", "Duststorm", "20" });
		 db.execSQL(INSERTWPHE,new String[] { "小到中雨", "Light to moderate rain", "21" });
		 db.execSQL(INSERTWPHE,new String[] { "中到大雨", "Moderate to heavy rain", "22" });
		 db.execSQL(INSERTWPHE,new String[] { "大到暴雨", "Heavy rain to storm", "23" });
		 db.execSQL(INSERTWPHE,new String[] { "暴雨到大暴雨", "Storm to heavy storm", "24" });
		 db.execSQL(INSERTWPHE,new String[] { "大暴雨到特大暴雨", "Heavy to severe storm", "25" });
		 db.execSQL(INSERTWPHE,new String[] { "小到中雪", "Light to moderate snow", "26" });
		 db.execSQL(INSERTWPHE,new String[] { "中到大雪", "Moderate to heavy snow", "27" });
		 db.execSQL(INSERTWPHE,new String[] { "大到暴雪", "Heavy snow to snowstorm", "28" });
		 db.execSQL(INSERTWPHE,new String[] { "浮尘", "Dust", "29" });
		 db.execSQL(INSERTWPHE,new String[] { "扬沙", "Sand", "30" });
		 db.execSQL(INSERTWPHE,new String[] { "强沙尘暴", "Sandstorm", "31" });
		 db.execSQL(INSERTWPHE,new String[] { "霾", "Haze", "53" });
		 db.execSQL(INSERTWPHE,new String[] { "无", "Unknown", "99" });
		
		 db.execSQL(INSERTWDIR,new String[] { "无持续风向", "No wind", "0" });
		 db.execSQL(INSERTWDIR,new String[] { "东北风", "Northeast", "1" });
		 db.execSQL(INSERTWDIR,new String[] { "东风", "East", "2" });
		 db.execSQL(INSERTWDIR,new String[] { "东南风", "Southeast", "3" });
		 db.execSQL(INSERTWDIR,new String[] { "南风", "South", "4" });
		 db.execSQL(INSERTWDIR,new String[] { "西南风", "Southwest", "5" });
		 db.execSQL(INSERTWDIR,new String[] { "西风", "West", "6" });
		 db.execSQL(INSERTWDIR,new String[] { "西北风", "Northwest", "7" });
		 db.execSQL(INSERTWDIR,new String[] { "北风", "North", "8" });
		 db.execSQL(INSERTWDIR,new String[] { "旋转风", "Whirl wind", "9" });
		
		 db.execSQL(INSERTWPOW,new String[] { "微风", "<10m/h", "0" });
		 db.execSQL(INSERTWPOW,new String[] { "3-4 级", "10~17m/h", "1" });
		 db.execSQL(INSERTWPOW,new String[] { "4-5 级", "￼17~25m/h", "2" });
		 db.execSQL(INSERTWPOW,new String[] { "5-6 级", "25~34m/h", "3" });
		 db.execSQL(INSERTWPOW,new String[] { "6-7 级", "34~43m/h", "4" });
		 db.execSQL(INSERTWPOW,new String[] { "7-8 级", "43~54m/h", "5" });
		 db.execSQL(INSERTWPOW,new String[] { "8-9 级", "54~65m/h", "6" });
		 db.execSQL(INSERTWPOW,new String[] { "9-10 级", "65~77m/h", "7" });
		 db.execSQL(INSERTWPOW,new String[] { "10-11 级", "77~89m/h", "8" });
		 db.execSQL(INSERTWPOW,new String[] { "11-12 级", "89~102m/h", "9" });
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
