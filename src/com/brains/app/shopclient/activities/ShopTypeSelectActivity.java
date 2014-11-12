package com.brains.app.shopclient.activities;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;


import com.brains.app.shopclient.R;
import com.brains.app.shopclient.common.Util;
import com.brains.framework.activities.base.BaseNormalActivity;
import com.brains.framework.common.Const;

/**
 * 商户类型选择画面
 * @author xiect
 *
 */
public class ShopTypeSelectActivity extends BaseNormalActivity implements OnClickListener{
	private static final String ACTION = "brains.intent.action.ACTION_SHOP_TYPE";
	public static final String EXTRA_ZIZHI = "EXTRA_ZIZHI";
	public static final String EXTRA_XINYU = "EXTRA_XINYU";
	public static final String EXTRA_CATE = "EXTRA_CATE";
	
	private String typeZizhi;
	private String typeXinyu;
	private String typeCategory;
	
	private RadioGroup group1;
	private RadioGroup group2;
	private RadioGroup group3;
	
	/**
	 * 取得关于画面Intent
	 * @return
	 */
	public static Intent makeIntent(String zizhi,String xinyu ,String category) {
		Intent intent = new Intent().setAction(ACTION);
		intent.putExtra(EXTRA_ZIZHI, zizhi);
		intent.putExtra(EXTRA_XINYU, xinyu);
		intent.putExtra(EXTRA_CATE, category);
		return intent;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_shop_type);
		
		Intent intent = getIntent();
		if(intent != null && intent.getExtras() != null){
			Bundle bundle = intent.getExtras();
			typeZizhi = bundle.getString(EXTRA_ZIZHI, Const.TYPE_ALL);
			typeXinyu = bundle.getString(EXTRA_XINYU, Const.TYPE_ALL);
			typeCategory = bundle.getString(EXTRA_CATE, Const.TYPE_ALL);
		}else{
			typeZizhi = Const.TYPE_ALL;
			typeXinyu = Const.TYPE_ALL;
			typeCategory = Const.TYPE_ALL;	
		}
		
		group1 = (RadioGroup) findViewById(R.id.shop_type_group1);
		group2 = (RadioGroup) findViewById(R.id.shop_type_group2);
		group3 = (RadioGroup) findViewById(R.id.shop_type_group3);
		
		int max = group1.getChildCount();
		for(int i = 0; i< max; i++){
			group1.getChildAt(i).setOnClickListener(this);
		}
		
		max = group2.getChildCount();
		for(int i = 0; i< max; i++){
			group2.getChildAt(i).setOnClickListener(this);
		}
		
		max = group3.getChildCount();
		for(int i = 0; i< max; i++){
			group3.getChildAt(i).setOnClickListener(this);
		}
		
		showWithData();
	}
	
	private void showWithData(){
		int index = Integer.parseInt(typeZizhi);
		
		group1.check(group1.getChildAt(index).getId());
		
		index = Integer.parseInt(typeXinyu);
		group2.check(group2.getChildAt(index).getId());
		
		index = Integer.parseInt(typeCategory);
		group3.check(group3.getChildAt(index).getId());
	}

	@Override
	public void onClick(View v) {
		String selectTag = v.getTag().toString();
		String tempStr = selectTag.substring(0,1);
		int index = Integer.parseInt(tempStr);
		String value = selectTag.substring(1,2); 
		Util.sysLog("type", "index:" + index +"\t value:" +  value);
		switch (index) {
		case 1:
			typeZizhi = value;
			break;
		case 2:
			typeXinyu = value;
			break;
		case 3:
			typeCategory = value;
			break;

		default:
			break;
		}
		
		Util.sysLog("type", "type selected:" + v.getTag().toString());
		Intent data = new Intent();
		data.putExtra(EXTRA_ZIZHI, typeZizhi);
		data.putExtra(EXTRA_XINYU, typeXinyu);
		data.putExtra(EXTRA_CATE, typeCategory);
		setResult(Activity.RESULT_OK, data);
		finish();
	}	
}
