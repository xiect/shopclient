package com.brains.app.shopclient;

import java.io.File;

import com.brains.app.shopclient.common.Api;
import com.brains.app.shopclient.common.CartManager;
import com.brains.app.shopclient.common.CrashHandler;
import com.brains.app.shopclient.common.Util;
import com.brains.app.shopclient.db.dao.PrefDAO;
import com.brains.framework.widget.BadgeView;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;
import android.graphics.Bitmap;

public class ShoppingApp extends Application {
	private static final String TAG = "ShoppingApp";
	public Api mApi;
	private DisplayImageOptions categoryLoadOption;
	public PrefDAO mPrefDAO;
	public CartManager cart;
	private BadgeView cartBadge;
	
	public void setTabCart(BadgeView tab){

		if(tab != null){
			cartBadge = tab;
		}
	}
	
	/**
	 * 实例化
	 */
	public void onCreate() {
		super.onCreate();
		
		Util.sysLog(TAG, "===>app onCreate");
		
		mPrefDAO = new PrefDAO(this);
		// 初始化图片下载对象
		initImageLoader();
		// 错误处理
		CrashHandler crashHandler = CrashHandler.getInstance();  
		// 注册crashHandler  
		crashHandler.init(getApplicationContext());  
//		// 发送以前没发送的报告(可选)  
//		crashHandler.sendPreviousReportsToServer();

		Log.d(TAG, "实例化远程调用接口");
		mApi = new Api();
	}
	
	private void initImageLoader(){
		categoryLoadOption = new DisplayImageOptions.Builder()
        .showImageOnLoading(R.drawable.product_icon_default) // resource or drawable
        .showImageForEmptyUri(R.drawable.product_icon_default) // resource or drawable
        .showImageOnFail(R.drawable.product_icon_default) // resource or drawable
        .resetViewBeforeLoading(false)  // default
        .delayBeforeLoading(500)
        .cacheInMemory(true) // default
        .cacheOnDisk(true) // default
        .imageScaleType(ImageScaleType.EXACTLY) 
        .bitmapConfig(Bitmap.Config.RGB_565)
        .build();
		
		
		File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "imageloader/Cache"); 
		
		Util.sysLog(TAG, "image cache path:" + cacheDir.getAbsolutePath());
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)  
		.defaultDisplayImageOptions(categoryLoadOption)
		.memoryCacheExtraOptions(480, 800) 
		.diskCacheExtraOptions(480, 800, null)
		.threadPoolSize(2)
		.threadPriority(Thread.NORM_PRIORITY - 2)  
		.denyCacheImageMultipleSizesInMemory()  
		.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))  
		.memoryCacheSize(2 * 1024 * 1024)    
		.diskCacheFileNameGenerator(new Md5FileNameGenerator())
        .diskCacheSize(50 * 1024 * 1024)
        .diskCacheFileCount(100)
        .diskCache(new UnlimitedDiscCache(cacheDir)) 
		.tasksProcessingOrder(QueueProcessingType.FIFO)  
		.defaultDisplayImageOptions(DisplayImageOptions.createSimple())  
		.writeDebugLogs() 
		.build(); 
		
		ImageLoader.getInstance().init(config);
	}
	
	public DisplayImageOptions getCategoryImgOption(){
		if(categoryLoadOption == null){
			initImageLoader();
		}
		return categoryLoadOption;
	}
	

	/**
	 * 实现通过Toast 显示短暂错误信息
	 * @param msg
	 */
	public void showErrorWithToast(int resId){
		 Toast toast = Toast.makeText(this,resId, Toast.LENGTH_LONG);
	     toast.show();
	}
	
	/**
	 * 实现通过Toast 显示短暂错误信息
	 * @param msg
	 */
	public void showErrorWithToast(String msg){
		 Toast toast = Toast.makeText(this,msg, Toast.LENGTH_LONG);
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
	
	public void showMsgWithToast(int resId){
		 Toast toast = Toast.makeText(this, resId, Toast.LENGTH_LONG);
	     toast.show();
	}
	
	/**
	 * 持久化用户名密码
	 * @param username
	 * @param password
	 */
	public void saveUserInfo(String username,String password){
		mPrefDAO.saveUserNameAndPassword(username, password);
	}
	
	/**
	 * 是否已经登录
	 * @return
	 */
	public boolean isLogin(){
		String name = mPrefDAO.getUserName();
		String password = mPrefDAO.getUserPassword();
		if(!Util.isEmpty(name) && !Util.isEmpty(password)){
			Util.sysLog(TAG, "是否已经登录:YES");
			return true;
		}
		Util.sysLog(TAG, "是否已经登录:NO");
		return false;
	}
	
	/**
	 * 网络是否可用
	 * @return
	 */
	public boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if(activeNetInfo != null && activeNetInfo.isAvailable()){
			Util.sysLog(TAG, "---> net work is available");
			return true;
		}
		return false;
	}	
	
	public void doLogOff(){
		// 将用户名 密码至空
		mPrefDAO.saveUserNameAndPassword("", "");
		mPrefDAO.saveNikeName("");
	}
	
	/**
	 * update cart badge
	 */
	public void updateCartNum(Context ctx){
		Util.sysLog(TAG, "updateCartNum call");
		if(cartBadge != null){
			int num = cart.getAllItemCountRealTime(ctx);
			Util.sysLog(TAG, "updateCartNum:" + num);
			if(num > 0){
				cartBadge.setText(String.valueOf(num));
				cartBadge.show();
			}else{
				cartBadge.hide();	
			}
		}
	}
}
