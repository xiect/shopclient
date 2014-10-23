package com.brains.app.shopclient.activities;

import com.brains.app.shopclient.R;
import com.brains.app.shopclient.R.id;
import com.brains.app.shopclient.R.layout;
import com.brains.app.shopclient.R.menu;
import com.brains.framework.activities.base.BaseNormalActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;

/**
 * 主画面
 * @author xiect
 *
 */
public class MainActivity extends BaseNormalActivity{
	private final static String ACTION_MAIN = "brains.intent.action.ACTION_MAIN";

	public static final String TAG = "HomeActivity";
	
	private TabHost mTabHost;
	private RadioGroup mRadioGroup;
	private RadioButton mTabHome, mTabCategory, mMark, mTabCart,mTabMy;
	
	/**
	 * 取得主页画面Intent
	 * 
	 * @return
	 */
	public static Intent makeIntent() {
		return new Intent().setAction(ACTION_MAIN);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_home);
		
	}
	
	private void findViews() {
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mRadioGroup = (RadioGroup) findViewById(R.id.homeradio);
		mTabHome = (RadioButton) mRadioGroup.findViewWithTag("radio_button_home");
		mTabCategory = (RadioButton) mRadioGroup.findViewWithTag("radio_button_category");
		mMark = (RadioButton) mRadioGroup.findViewWithTag("radio_button_mark");
		mTabCart = (RadioButton) mRadioGroup.findViewWithTag("radio_button_cart");
		mTabMy = (RadioButton) mRadioGroup.findViewWithTag("radio_button_my");
	}
	
	private void initViews(String remindMode) {
		// 首页
		Intent intent = HomeActivity.makeIntent(); 
		mTabHost.addTab(mTabHost.newTabSpec("tab1")
				.setIndicator("tab1")
				.setContent(intent));
		// 品类	 
		intent = CategoryActivity.makeIntent();
		mTabHost.addTab(mTabHost.newTabSpec("tab2")
				.setIndicator("tab2")
				.setContent(intent));
		
		// 品牌
		intent = BrandActivity.makeIntent(); 
		mTabHost.addTab(mTabHost.newTabSpec("tab3")
				.setIndicator("tab3")
				.setContent(intent));
		// 购物车
		intent = CartActivity.makeIntent();  
		mTabHost.addTab(mTabHost.newTabSpec("tab4")
				.setIndicator("tab4")
				.setContent(intent));
		// 我的
		intent = MyActivity.makeIntent();  
		mTabHost.addTab(mTabHost.newTabSpec("tab4")
				.setIndicator("tab4")
				.setContent(intent));
	}
}