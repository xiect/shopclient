package com.brains.app.shopclient.activities;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;

import android.widget.EditText;
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
	private ImageButton markSelected;
	private int[] startPointArr = new int[3];
	private EditText mTxtKeyword;
	private Button mBtnSearch; // 搜索按钮
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
		mTxtKeyword = (EditText)findViewById(R.id.homeActivity_autoComplete);
		mBtnSearch = (Button) findViewById(R.id.btn_search_btn);
		mTvSortDefault = (RelativeLayout)findViewById(R.id.search_default_sort_button);
		mTvSortDesc = (RelativeLayout)findViewById(R.id.search_price_desc_button);
		mTvSortAsc = (RelativeLayout)findViewById(R.id.search_price_ase_button);
		markSelected = (ImageButton)findViewById(R.id.v_seleted_mark);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		app.showErrorWithToast("screen width:"+ dm.widthPixels);
		
		int oneOfthree = dm.widthPixels / 3;
		int markW = oneOfthree / 3;
		markW = oneOfthree - markW;
		int offset = (oneOfthree - markW) / 2 ;
		Util.sysLog(TAG, "oneOfthree:" + oneOfthree + "\t markW:" + markW + "\t offset:" + offset);
		LayoutParams param = markSelected.getLayoutParams();
		param.width = markW;
		markSelected.setLayoutParams(param);
		markSelected.setX(offset);
		
		startPointArr[0] = 0;
		startPointArr[1] = oneOfthree ;
		startPointArr[2] = oneOfthree * 2 ;
		markCurrentPos = 0; // 当前位置取得
		
		// bunding event
		mTvSortDefault.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mTvSortDesc.setSelected(false);
				mTvSortAsc.setSelected(false);
				mTvSortDefault.setSelected(true);
				slideToCurrentButton(startPointArr[0]);
			}
		});
		
		mTvSortAsc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mTvSortDesc.setSelected(false);
				mTvSortAsc.setSelected(true);
				mTvSortDefault.setSelected(false);
				slideToCurrentButton(startPointArr[2]);
			}
		});
		
		mTvSortDesc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mTvSortDesc.setSelected(true);
				mTvSortAsc.setSelected(false);
				mTvSortDefault.setSelected(false);
				slideToCurrentButton(startPointArr[1]);
			}
		});
		mBtnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(checkInput()){
					// 执行查询操作(keyword)
					
				}else{
					// 提示错误信息
					app.showErrorWithToast(R.string.msg_please_input_keyword);
				}
			}
		});
	}
	
	/**
	 * keyword 输入校验
	 */
	private boolean checkInput(){
		mKeyWord = mTxtKeyword.getText().toString();
		Util.sysLog(TAG, "mTxtKeyword==> "+mKeyWord);
		if(Util.isEmpty(mKeyWord)){
			return false;
		}
		return true;
	}
	
    /**
     * 划动顶部菜单
     * @param view
     */
    private void slideToCurrentButton(int targetPos){
		int xEnd = targetPos;
		Util.sysLog(TAG,"xStart:" + markCurrentPos + " xEnd:" + xEnd );
        TranslateAnimation animation = new TranslateAnimation(markCurrentPos, xEnd, 0, 0);  
        animation.setDuration(100);  
        animation.setFillAfter(true);  
        markSelected.startAnimation(animation); 
        markCurrentPos = xEnd;
    }
    
	/**
	 * 获取商品一览
	 */
	private void doGetProductList(){
		
	}
}
