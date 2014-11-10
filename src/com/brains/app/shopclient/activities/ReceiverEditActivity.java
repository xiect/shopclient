package com.brains.app.shopclient.activities;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.brains.app.shopclient.R;
import com.brains.app.shopclient.common.Util;
import com.brains.framework.activities.base.BaseNormalActivity;

/**
 * 接货人信息编辑画面
 * @author xiect
 *
 */
public class ReceiverEditActivity extends BaseNormalActivity {
	private static final String ACTION = "brains.intent.action.ACTION_RECEIVER";
	private Button btnConfirm;
	private EditText tvName;
	private EditText tvTel;
	private EditText tvZipCode;
	private EditText tvAdd;
	private TextView tvTitle;
	
	/**
	 * 取得接货人信息画面Intent
	 * @return
	 */
	public static Intent makeIntent() {
		Intent intent = new Intent().setAction(ACTION);
		return intent;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_receiver_edit);
		findView();
		showData();
	}
	
	private void findView(){
		tvName = (EditText) findViewById(R.id.reciver_name);
		tvTel = (EditText) findViewById(R.id.reciver_tel);
		tvZipCode = (EditText) findViewById(R.id.reciver_zip_code);
		tvAdd = (EditText) findViewById(R.id.reciver_add);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		
		// 画面标题设置
		tvTitle.setText(R.string.page_title_receiver_info);
		btnConfirm = (Button)findViewById(R.id.reveiver_comfirm_button);
		btnConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(doCheckInput()){
					// 校验通过的场合
					// 保存收货人信息
					String name = tvName.getText().toString();
					String tel = tvTel.getText().toString();
					String zipCode = tvZipCode.getText().toString();
					String add = tvAdd.getText().toString();
					app.mPrefDAO.saveReceiverInfo(name,tel,zipCode,add);
					
					// 返回结果
					setResult(Activity.RESULT_OK);
					finish();
				}
			}
		});
	}
	
	/**
	 * 显示数据
	 */
	private void showData(){
		String name = app.mPrefDAO.getReceiverName();
		String tel = app.mPrefDAO.getReceiverTel();
		String add = app.mPrefDAO.getReceiverAdd();
		
		if(!Util.isEmpty(name)){
			tvName.setText(name);
		}
		if(!Util.isEmpty(tel)){
			tvTel.setText(tel);
		}
		if(!Util.isEmpty(add)){
			tvAdd.setText(add);
		}
	}
	
	/**
	 * 输入校验
	 * @return
	 */
	private boolean doCheckInput(){
		return true;
	}
}
