package com.brains.app.shopclient;

import java.io.File;

import com.brains.app.shopclient.common.Api;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;
import android.graphics.Bitmap;

public class ShoppingApp extends Application {
	private static final String TAG = "ShoppingApp";
	public Api mApi;
	private DisplayImageOptions categoryLoadOption;
	
	/**
	 * 实例化
	 */
	public void onCreate() {
		super.onCreate();
		initImageLoader();
		
		Log.d(TAG, "实例化远程调用接口");
		mApi = new Api();
	}
	
	private void initImageLoader(){
		File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "imageloader/Cache"); 
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)  
		.memoryCacheExtraOptions(480, 800) 
		.diskCacheExtraOptions(480, 800, null)
		.threadPoolSize(2)
		.threadPriority(Thread.NORM_PRIORITY - 2)  
		.denyCacheImageMultipleSizesInMemory()  
		.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))  
		.memoryCacheSize(2 * 1024 * 1024)    
		.diskCache(new UnlimitedDiscCache(cacheDir)) 
        .diskCacheSize(50 * 1024 * 1024)
        .diskCacheFileCount(100)
		.tasksProcessingOrder(QueueProcessingType.FIFO)  
		.defaultDisplayImageOptions(DisplayImageOptions.createSimple())  
		.writeDebugLogs() 
		.build(); 
		
		ImageLoader.getInstance().init(config);
		
		categoryLoadOption = new DisplayImageOptions.Builder()
        .showImageOnLoading(R.drawable.product_icon_default) // resource or drawable
        .showImageForEmptyUri(R.drawable.product_icon_default) // resource or drawable
        .showImageOnFail(R.drawable.product_icon_default) // resource or drawable
        .resetViewBeforeLoading(false)  // default
        .delayBeforeLoading(1000)
        .cacheInMemory(false) // default
        .cacheOnDisk(true) // default
        .imageScaleType(ImageScaleType.EXACTLY) 
        .bitmapConfig(Bitmap.Config.RGB_565)
        .build();
	}
	
	public DisplayImageOptions getCategoryImgOption(){
		return categoryLoadOption;
	}
	

	/**
	 * 实现通过Toast 显示短暂错误信息
	 * @param msg
	 */
	public void showErrorWithToast(String msg){
		 Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
	     toast.show();
	}
	/**
	 * 实现通过Toast 显示短暂信息提示
	 * @param msg
	 */
	public void showMsgWithToast(String msg){
		 Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
	     toast.show();
	}
}
