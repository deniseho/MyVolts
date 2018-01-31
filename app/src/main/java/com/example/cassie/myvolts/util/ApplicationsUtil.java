//reference: http://www.tuicool.com/articles/zIRNN3z
package com.example.cassie.myvolts.util;

import java.io.File;


import android.annotation.SuppressLint;
import android.app.Application;

import com.example.cassie.myvolts.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

/**
 * Created by cassie on 23/05/2017.
 */
public class ApplicationsUtil extends Application{

	@SuppressLint("MissingSuperCall")
	@Override
	public void onCreate() {

		@SuppressWarnings("deprecation")
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.imgload)
				.showImageOnFail(R.drawable.ic_error) 
				.cacheInMemory(true) 
				.cacheOnDisc(true)
				.build(); 
		
		 File cacheDir = StorageUtils.getOwnCacheDirectory(this,
				"imageloader/Cache");

		ImageLoaderConfiguration config = new ImageLoaderConfiguration
				.Builder(getApplicationContext())
				.memoryCacheExtraOptions(480, 800) // max width, max height
				.threadPoolSize(3)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new LruMemoryCache(5 * 1024 * 1024))
				.memoryCacheSize(5 * 1024 * 1024)
				.discCacheSize(50 * 1024 * 1024)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.discCacheFileCount(100)
				.discCache(new UnlimitedDiscCache(cacheDir))
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.imageDownloader(new BaseImageDownloader(getApplicationContext(), 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)
				.writeDebugLogs() // Remove for release app
				.build();
		 
		ImageLoader.getInstance().init(config);
	}

}
