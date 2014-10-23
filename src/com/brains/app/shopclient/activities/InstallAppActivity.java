package com.brains.app.shopclient.activities;

import com.brains.app.shopclient.R;
import com.brains.app.shopclient.activities.lib.InstallAppUserBaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;


/**
 * アプリの初期化処理を実行する画面。 アプリ内でのLAUNCHERアクティビティ。
 * 
 * @author id:language_and_engineering
 * 
 */
public class InstallAppActivity extends InstallAppUserBaseActivity {
	// NOTE: 应用初始化流程，参照父class
	// ------------- アプリ初期化処理 -----------------

	Handler mHandler = new Handler();
	
	// RDBの初期化は，スキーマ定義の専用クラスを参照。	
	@Override
	protected void init_db_preferences() {
	}

	// 延时毫秒数
	private long mStartSec;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// 无标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        // 取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		// 启动欢迎页面
		this.setContentView(R.layout.layout_welcome);
		mStartSec = System.currentTimeMillis();
		Log.d("install","start===>:" + mStartSec);
		super.onCreate(savedInstanceState);		
	}

	@Override
	protected void init_etc() {
	
	}

	// ------------- 初期化完了時 -----------------

	@Override
	protected void onInstallCompleted() {
		Log.d("install","onInstallCompleted");
		goHomePage();
	}

	@Override
	protected void onInstallSkipped() {
		Log.d("install","onInstallSkipped");
		goHomePage();
	}
	
	/**
	 * 跳转至应用首页
	 */
	private void goHomePage(){
		long endSec = System.currentTimeMillis();
		Log.d("install","end:" + endSec);
		
		// 启动初始化前后时差取得
		long diffSec = endSec - mStartSec;
		Log.d("install","diffSec:" + diffSec);
		// 2000
		long CONST_DEFY = 3000;
		if(diffSec >= CONST_DEFY){
			// 超出延时毫秒数的场合，直接跳转至首页
			mHandler.post(new HomeThread());
		}else{
			Log.d("install", "dely:" + (CONST_DEFY - diffSec));
			mHandler.postDelayed(new HomeThread(), CONST_DEFY - diffSec);
		}
	}
	
	private class GuideThread implements Runnable{
		@Override
		public void run() {
			Intent intent = GuideActivity.makeIntent();
			InstallAppActivity.this.startActivity(intent);
			InstallAppActivity.this.finish();
		}
	}
	
	private class HomeThread implements Runnable{
		@Override
		public void run() {
			Intent intent = GuideActivity.makeIntent();
			InstallAppActivity.this.startActivity(intent);
			InstallAppActivity.this.finish();
		}
	}
	
}
