package com.brains.app.shopclient.activities;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
	
	private RelativeLayout mTvSortDefault;
	private RelativeLayout mTvSortDesc;
	private RelativeLayout mTvSortAsc;
	private ListView mListView;
	private List mDataList;
	private int markCurrentPos;
	private View markSelected;
	
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

	}
	
	private void findView(){
		mTvSortDefault = (RelativeLayout)findViewById(R.id.search_default_sort_button);
		mTvSortDesc = (RelativeLayout)findViewById(R.id.search_price_desc_button);
		mTvSortAsc = (RelativeLayout)findViewById(R.id.search_price_ase_button);
		mTvSortDefault.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				app.showErrorWithToast("mTvSortAsc clicked!");
				mTvSortDesc.setSelected(false);
				mTvSortAsc.setSelected(true);
			}
		});
		
		mTvSortAsc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				app.showErrorWithToast("mTvSortAsc clicked!");
				mTvSortDesc.setSelected(false);
				mTvSortAsc.setSelected(true);
			}
		});
		
		mTvSortDesc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				app.showErrorWithToast("mTvSortDesc clicked!");
				mTvSortDesc.setSelected(true);
				mTvSortAsc.setSelected(false);
			}
		});
		
//		v_seleted_mark
		markSelected = findViewById(R.id.v_seleted_mark);
		int width = mTvSortDefault.getWidth();
		int height = markSelected.getHeight();
		
		markSelected.measure(width, height);
		markCurrentPos = markSelected.getLeft(); // 当前位置取得
	}
	
    /**
     * 划动顶部菜单
     * @param view
     */
    private void slideToCurrentButton(View view){
//		int xEnd = view.getLeft();
//		Util.sysLog(TAG,"xStart:" + markCurrentPos + " xEnd:" + xEnd );
//        TranslateAnimation animation = new TranslateAnimation(markCurrentPos, xEnd, 0, 0);  
//        animation.setDuration(100);  
//        animation.setFillAfter(true);  
//        markSelected.startAnimation(animation); 
//        markCurrentPos = xEnd;
    }
    
	/**
	 * 获取商品一览
	 */
	private void doGetProductList(){
		
	}
}
