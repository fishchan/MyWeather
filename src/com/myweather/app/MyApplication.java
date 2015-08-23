package com.myweather.app;

import android.app.Application;

public class MyApplication extends Application {
	/**
	 * 编写自己的Application，管理全局状态信息，比如Context
	 * 
	 */
	private static MyApplication instance;

	public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
