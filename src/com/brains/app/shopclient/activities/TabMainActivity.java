package com.brains.app.shopclient.activities;

import java.util.ArrayList;
import java.util.List;

import com.brains.app.shopclient.R;
import com.brains.app.shopclient.activities.fragment.BrandFragment;
import com.brains.app.shopclient.activities.fragment.CartFragment;
import com.brains.app.shopclient.activities.fragment.CategoryFragment;
import com.brains.app.shopclient.activities.fragment.HomeFragment;
import com.brains.app.shopclient.activities.fragment.MyFragment;
import com.brains.app.shopclient.adapter.MainFragmentPagerAdapter;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * fragment tabs implement
 * @author xiect
 *
 */
public class TabMainActivity extends FragmentActivity {

	private ViewPager mViewPager;
	private PagerAdapter mAdapter;
	private ArrayList<Fragment> fragmentList;
	
	private RadioGroup mRadioGroup;
	private RadioButton mTabHome, mTabCategory, mMark, mTabCart,mTabMy;
	private int currentIndex = -1; //  默认 没有
	
	private final static String ACTION_TABMAIN = "brains.intent.action.ACTION_TABMAIN";
	/**
	 * 取得主页画面Intent
	 * 
	 * @return
	 */
	public static Intent makeIntent() {
		return new Intent().setAction(ACTION_TABMAIN);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.layout_tabs);
		
//		mInflater = LayoutInflater.from(this);
		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
		
		// tabs
		mRadioGroup = (RadioGroup) findViewById(R.id.homeradio);
		mTabHome = (RadioButton) mRadioGroup.findViewWithTag("radio_button_home");
		mTabCategory = (RadioButton) mRadioGroup.findViewWithTag("radio_button_category");
		mMark = (RadioButton) mRadioGroup.findViewWithTag("radio_button_mark");
		mTabCart = (RadioButton) mRadioGroup.findViewWithTag("radio_button_cart");
		mTabMy = (RadioButton) mRadioGroup.findViewWithTag("radio_button_my");
		bindingEventListenner();
		changeTabImage(0); // 默认首页
		
		// fragment
		fragmentList = new ArrayList<Fragment>();
		Fragment home = new HomeFragment();
		Fragment category = new CategoryFragment();
		Fragment brand = new BrandFragment();
		Fragment cart = new CartFragment();
		Fragment my = new MyFragment();
		fragmentList.add(home);
		fragmentList.add(category);
		fragmentList.add(brand);
		fragmentList.add(cart);
		fragmentList.add(my);
		
		// pageview adapter
		mAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
		mViewPager.setAdapter(mAdapter);
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				changeTabImage(position);
				currentIndex = position;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});

		
	}
	
	private void bindingEventListenner(){
		mTabHome.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mViewPager.setCurrentItem(0, true);
			}
		});
		
		mTabCategory.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mViewPager.setCurrentItem(1, true);
			}
		});
		
		mMark.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mViewPager.setCurrentItem(2, true);
			}
		});
		
		mTabCart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mViewPager.setCurrentItem(3, true);
			}
		});
		
		mTabMy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mViewPager.setCurrentItem(4, true);
			}
		});
	}
	
	/**
	 * change tab 选中状态的图片
	 * @param index
	 */
	protected void changeTabImage(int index){
		if(index != currentIndex){
			// 将但前选中状态还原成正常状态
			switch(currentIndex){
			case 0:
				final Drawable drawable1 = getResources().getDrawable(R.drawable.main_bottom_tab_home_normal);
				mTabHome.setCompoundDrawablesWithIntrinsicBounds(null,drawable1,null,null);
				break;
			case 1:
				final Drawable drawable2 = getResources().getDrawable(R.drawable.main_bottom_tab_category_normal);
				mTabCategory.setCompoundDrawablesWithIntrinsicBounds(null,drawable2,null,null);
				break;
			case 2:
				final Drawable drawable3 = getResources().getDrawable(R.drawable.main_bottom_tab_faxian_normal);
				mMark.setCompoundDrawablesWithIntrinsicBounds(null,drawable3,null,null);
				break;
			case 3:
				final Drawable drawable4 = getResources().getDrawable(R.drawable.main_bottom_tab_cart_normal);
				mTabCart.setCompoundDrawablesWithIntrinsicBounds(null,drawable4,null,null);
				break;
			case 4:
				final Drawable drawable5 = getResources().getDrawable(R.drawable.main_bottom_tab_personal_normal);
				mTabMy.setCompoundDrawablesWithIntrinsicBounds(null,drawable5,null,null);
				break;
			}
			switch(index){
			case 0:
				final Drawable drawable1 = getResources().getDrawable(R.drawable.main_bottom_tab_home_focus);
				mTabHome.setChecked(true);
				mTabHome.setCompoundDrawablesWithIntrinsicBounds(null,drawable1,null,null);
				break;
			case 1:
				final Drawable drawable2 = getResources().getDrawable(R.drawable.main_bottom_tab_category_focus);
				mTabCategory.setChecked(true);
				mTabCategory.setCompoundDrawablesWithIntrinsicBounds(null,drawable2,null,null);
				break;
			case 2:
				final Drawable drawable3 = getResources().getDrawable(R.drawable.main_bottom_tab_faxian_focus);
				mMark.setChecked(true);
				mMark.setCompoundDrawablesWithIntrinsicBounds(null,drawable3,null,null);
				break;
			case 3:
				final Drawable drawable4 = getResources().getDrawable(R.drawable.main_bottom_tab_cart_focus);
				mTabCart.setChecked(true);
				mTabCart.setCompoundDrawablesWithIntrinsicBounds(null,drawable4,null,null);
				break;
			case 4:
				final Drawable drawable5 = getResources().getDrawable(R.drawable.main_bottom_tab_personal_focus);
				mTabMy.setChecked(true);
				mTabMy.setCompoundDrawablesWithIntrinsicBounds(null,drawable5,null,null);
				break;
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		int currentIndex = mViewPager.getCurrentItem();
		fragmentList.get(currentIndex).onActivityResult(requestCode, resultCode, data);
	}
}
