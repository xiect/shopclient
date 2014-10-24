package com.brains.app.shopclient.activities;

import com.brains.app.shopclient.R;
import com.brains.app.shopclient.R.id;
import com.brains.app.shopclient.R.layout;
import com.brains.app.shopclient.R.menu;
import com.brains.framework.activities.base.BaseNormalActivity;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;

/**
 * 主画面
 * @author xiect
 *
 */
public class MainActivity extends TabActivity {
	private final static String ACTION_MAIN = "brains.intent.action.ACTION_MAIN";

	public static final String TAG = "MainActivity";
	
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
		setContentView(R.layout.layout_main);
		findViews();
		initCustomViews();
		
//		// 初始状态
//		mTabHome.setChecked(true);
//		mTabHome.setPressed(false);
//		mTabHost.setCurrentTab(0);
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
	
	private void bindListener() {
		mTabHome.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mTabHost.setCurrentTab(0);
			}
		});
		mTabCategory.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mTabHost.setCurrentTab(1);
			}
		});
		mMark.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mTabHost.setCurrentTab(2);
			}
		});
		mTabCart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mTabHost.setCurrentTab(3);
			}
		});
		mTabMy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mTabHost.setCurrentTab(4);
			}
		});
	}
	private void initCustomViews() {
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