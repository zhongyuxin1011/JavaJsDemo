package com.zyx1011.javajsdemo;

import android.app.Application;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class GlobalApp extends Application {
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() {
		super.onCreate();
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder() //
				.showImageForEmptyUri(R.drawable.ic_launcher) //
				.showImageOnFail(R.drawable.ic_launcher) //
				.cacheInMemory(true) //
				.cacheOnDisk(true) //
				.build();//

		ImageLoaderConfiguration config = new ImageLoaderConfiguration//
				.Builder(getApplicationContext())//
						.defaultDisplayImageOptions(defaultOptions)//
						.discCacheSize(50 * 1024 * 1024)//
						.discCacheFileCount(100)// ª∫¥Ê“ª∞Ÿ’≈Õº∆¨
						.writeDebugLogs()//
						.build();//
		ImageLoader.getInstance().init(config);
	}
}
