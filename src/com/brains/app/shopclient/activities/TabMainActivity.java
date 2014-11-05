package com.brains.app.shopclient.activities;

import java.util.ArrayList;

import com.brains.app.shopclient.R;
import com.brains.app.shopclient.ShoppingApp;
import com.brains.app.shopclient.activities.fragment.BrandFragment;
import com.brains.app.shopclient.activities.fragment.CartFragment;
import com.brains.app.shopclient.activities.fragment.CategoryFragment;
import com.brains.app.shopclient.activities.fragment.HomeFragment;
import com.brains.app.shopclient.activities.fragment.MyFragment;
import com.brains.app.shopclient.adapter.MainFragmentPagerAdapter;
import com.brains.app.shopclient.common.CartManager;
import com.brains.app.shopclient.common.Util;
import com.brains.framework.widget.BadgeView;


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

	private static final String TAG = "TabMainActivity";
	private ViewPager mViewPager;
	private PagerAdapter mAdapter;
	private ArrayList<Fragment> fragmentList;
	private BadgeView cartBadge;
	private RadioGroup mRadioGroup;
	private RadioButton mTabHome, mTabCategory, mMark, mTabCart,mTabMy;
	private int currentIndex = -1; //  默认 没有
	private ShoppingApp app;
	
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

		// 应数据库初始化是在install activity中进行的。
		// 现在主画面初始话App 中的 Cart对象
		Util.sysLog(TAG, "== 初始化 购物车 ==");
		app = (ShoppingApp) getApplicationContext();
		app.cart = new CartManager(app);
		
//		mInflater = LayoutInflater.from(this);
		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
		
		// tabs
		mRadioGroup = (RadioGroup) findViewById(R.id.homeradio);
		mTabHome = (RadioButton) mRadioGroup.findViewWithTag("radio_button_home");
		mTabCategory = (RadioButton) mRadioGroup.findViewWithTag("radio_button_category");
		mMark = (RadioButton) mRadioGroup.findViewWithTag("radio_button_mark");
		mTabCart = (RadioButton) mRadioGroup.findViewWithTag("radio_button_cart");
		mTabMy = (RadioButton) mRadioGroup.findViewWithTag("radio_button_my");

	    // 购物车数量显示
		cartBadge = new BadgeView(this, mTabCart);
		app.setTabCart(cartBadge);
		cartBadge.setText("11");
		cartBadge.show();
		
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
				Util.sysLog(TAG, "currentIndex:" + currentIndex + "\t position:" + position);
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
	
	
	@Override
	protected void onResume() {
		super.onResume();
		// update cart badge
		app.updateCartNum();
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
				Util.sysLog(TAG, "mTabCart click");
				mViewPager.setCurrentItem(3, true);
			}
		});
		
		cartBadge.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Util.sysLog(TAG, "cartBadge click");
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
			default:
				final Drawable drawable6 = getResources().getDrawable(R.drawable.main_bottom_tab_home_normal);
				mTabHome.setCompoundDrawablesWithIntrinsicBounds(null,drawable6,null,null);
			}
			switch(index){
			case 0:
				final Drawable drawable1 = getResources().getDrawable(R.drawable.main_bottom_tab_home_focus);
				mTabCart.setChecked(false);
				mRadioGroup.check(mTabHome.getId());
				mTabHome.setCompoundDrawablesWithIntrinsicBounds(null,drawable1,null,null);
				break;
			case 1:
				final Drawable drawable2 = getResources().getDrawable(R.drawable.main_bottom_tab_category_focus);
				mTabCart.setChecked(false);
				mRadioGroup.check(mTabCategory.getId());
				mTabCategory.setCompoundDrawablesWithIntrinsicBounds(null,drawable2,null,null);
				break;
			case 2:
				final Drawable drawable3 = getResources().getDrawable(R.drawable.main_bottom_tab_faxian_focus);
				mTabCart.setChecked(false);
				mRadioGroup.check(mMark.getId());
				mMark.setCompoundDrawablesWithIntrinsicBounds(null,drawable3,null,null);
				break;
			case 3:
				final Drawable drawable4 = getResources().getDrawable(R.drawable.main_bottom_tab_cart_focus);
				mRadioGroup.clearCheck();
				mTabCart.setChecked(true);
				mTabCart.setCompoundDrawablesWithIntrinsicBounds(null,drawable4,null,null);
				break;
			case 4:
				final Drawable drawable5 = getResources().getDrawable(R.drawable.main_bottom_tab_personal_focus);
				mTabCart.setChecked(false);
				mRadioGroup.check(mTabMy.getId());
				mTabMy.setCompoundDrawablesWithIntrinsicBounds(null,drawable5,null,null);
				break;
			}
			
			Util.sysLog(TAG, "mTabCart status:" + mTabCart.isChecked());
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		int currentIndex = mViewPager.getCurrentItem();
		fragmentList.get(currentIndex).onActivityResult(requestCode, resultCode, data);
	}
}
