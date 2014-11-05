package com.brains.app.shopclient.activities;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.brains.app.shopclient.R;
import com.brains.app.shopclient.common.Util;
import com.brains.app.shopclient.db.entity.Product;
import com.brains.framework.activities.base.BaseNormalActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 商品详细
 * @author xiect
 *
 */
public class ProductDetailActivity extends BaseNormalActivity{

	private final static String ACTION = "brains.intent.action.ACTION_ITEM_DETAIL";
	public static final String TAG = "ProductDetailActivity";
	private static final String EXTRA_ITEM_ID = "EXTRA_ITEM_ID";
	private Product mItem;
	private ImageView mItemImage;
	private ProgressBar mImageLoading;
	private TextView mTvName;
	private TextView mTvPrice;
	private TextView mTvDesc;
	private TextView mTvTitle;
	private LinearLayout btnAdd2Cart;
	
	/**
	 * 商品详细画面Intent
	 * @return
	 */
	public static Intent makeIntent(Parcelable item) {
		Intent intent = new Intent().setAction(ACTION);
		intent.putExtra(EXTRA_ITEM_ID, item);
		return intent;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_product_detail);
		Intent intent = getIntent();
		if(intent != null){
			Bundle bundle = intent.getExtras();
			if(bundle != null){
				mItem = bundle.getParcelable(EXTRA_ITEM_ID);	
			}
		}
		
		if(mItem == null){
			Util.sysLog(TAG,"item is null");
		}else{
			mItem.setNum(1); // 默认买一件
			Util.sysLog(TAG, "item name===>"+mItem.getName());
		}
		
		findView();
		showViewWithData();
	}
	
	private void findView(){
		
		btnAdd2Cart = (LinearLayout) findViewById(R.id.ll_add2cart);
		mTvTitle = (TextView) findViewById(R.id.tv_title);
		mItemImage = (ImageView) findViewById(R.id.product_image);
		mImageLoading = (ProgressBar) findViewById(R.id.loading);
		mTvName = (TextView) findViewById(R.id.product_name_text);
		mTvPrice = (TextView) findViewById(R.id.product_price_text);
		mTvDesc = (TextView) findViewById(R.id.product_description_text);
		mTvTitle.setText(R.string.page_title_product_detail);
		btnAdd2Cart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 加入购物车
				app.cart.addProduct(mItem,ProductDetailActivity.this);
				app.showMsgWithToast(R.string.msg_add2card_success);
			}
		});
	}
	
	/**
	 * 将商品信息显示在画面上
	 */
	private void showViewWithData(){
		mTvName.setText(mItem.getName());
		mTvPrice.setText(Util.formatRmb(mItem.getPrice()));
		mTvDesc.setText(mItem.getDesc());
		
		String src = mItem.getImgSrc();
		if(!Util.isEmpty(src)){
			ImageLoader.getInstance().displayImage(src, mItemImage, new ImageLoadingListener() {
				@Override
				public void onLoadingStarted(String arg0, View arg1) {
					mImageLoading.setVisibility(View.VISIBLE);
				}
				
				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
					mImageLoading.setVisibility(View.GONE);
				}
				
				@Override
				public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
					mImageLoading.setVisibility(View.GONE);
				}
				
				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
					mImageLoading.setVisibility(View.GONE);
				}
			});
		}
		
	}
}
