package com.brains.app.shopclient.activities;

import android.content.Intent;
import android.os.Bundle;

import com.brains.app.shopclient.R;
import com.brains.app.shopclient.common.Util;
import com.brains.framework.activities.base.BaseNormalActivity;

/**
 * 商品检索一览画面
 * @author xiect
 *
 */
public class SearchActivity extends BaseNormalActivity{
	private final static String ACTION_SEARCH = "brains.intent.action.ACTION_SEARCH";
	public static final String TAG = "SearchActivity";
	private static final String EXTRA_CATEGORY_ID = "CATEGORY_ID";
	private static final String EXTRA_KEYWORD = "KEY_WORD";
	private String mCategoryId;
	private String mKeyWord;
	
	
	/**
	 * 取得商品检索画面Intent
	 * @return
	 */
	public static Intent makeIntent() {
		Intent intent = new Intent().setAction(ACTION_SEARCH);
		return intent;
	}
	
	/**
	 * 取得商品检索画面Intent
	 * @param categoryId 分类
	 * @param keyword 关键字
	 * @return
	 */
	public static Intent makeIntent(String categoryId,String keyword) {
		Intent intent = new Intent().setAction(ACTION_SEARCH);
		intent.putExtra(EXTRA_CATEGORY_ID, categoryId);
		intent.putExtra(EXTRA_KEYWORD, keyword);
		return intent;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_search);
		findView();
		
		Intent intent = getIntent();
		if(intent != null ){
			Bundle bundle = intent.getExtras();
			if(bundle != null){
				mCategoryId = bundle.getString(EXTRA_CATEGORY_ID);
			}
		}
		if(Util.isEmpty(mCategoryId)){
			app.showErrorWithToast(R.string.error_choose_category_please);
			this.finish();
		}else{
			// 访问网络获取二级分类
			doGetProductList();
		}
	}
	
	private void findView(){
		
	}
	
	/**
	 * 获取商品一览
	 */
	private void doGetProductList(){
		
	}
}
