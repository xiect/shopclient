package com.brains.app.shopclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brains.app.shopclient.R;
import com.brains.app.shopclient.common.Util;
import com.brains.app.shopclient.db.dao.PrefDAO;
import com.brains.framework.activities.base.BaseNormalActivity;


/**
 * 新建订单画面
 * @author xiect
 *
 */
public class NewOderActivity extends BaseNormalActivity{
	private final static String ACTION = "brains.intent.action.ACTION_NEWORDER";
	private final static int REQUEST_RECEIVER = 9;
	public static final String TAG = "NewOderActivity";
	private TextView tvTitle;
	private RelativeLayout receiverEmptyLayout;
	private RelativeLayout receiverLayout;
	private PrefDAO prefDAO;
	private TextView tvName;
	private TextView tvTel;
	private TextView tvAdd;
	
	/**
	 * 取得新建订单画面Intent
	 * 
	 * @return
	 */
	public static Intent makeIntent() {
		return new Intent().setAction(ACTION);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_new_order);
		// 初始化pref访问对象
		prefDAO = new PrefDAO(this);
		String name = prefDAO.getReceiverName();
		
		// 绑定视图及绑定事件
		findView();
		
		if(Util.isEmpty(name)){
			// 不存在配送信息的场合
			receiverEmptyLayout.setVisibility(View.VISIBLE);
			receiverLayout.setVisibility(View.GONE);
		}else{
			showReceiverInfo();
		}
	}
	
	private void findView(){
		tvName = (TextView) findViewById(R.id.textview_receiver_name_content);
		tvTel = (TextView) findViewById(R.id.textview_receiver_mobile_content);
		tvAdd = (TextView) findViewById(R.id.textview_receiver_address_content);
		// 标题 订单
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvTitle.setText(R.string.page_title_order);
		receiverEmptyLayout = (RelativeLayout) findViewById(R.id.layout_receiver_empty); 
		receiverLayout = (RelativeLayout) findViewById(R.id.layout_receiver_info);
		// 事件处理
		receiverEmptyLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doEditReceiverInfo();
			}
		});
		
		receiverLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doEditReceiverInfo();
			}
		});
	}
	
	/**
	 * receiver 编辑画面迁移
	 */
	private void doEditReceiverInfo(){
		// 编辑画面迁移
		Util.sysLog(TAG, "receiver 编辑画面迁移");
		startActivityForResult(ReceiverEditActivity.makeIntent(), REQUEST_RECEIVER);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(REQUEST_RECEIVER == requestCode && RESULT_OK == resultCode){
			showReceiverInfo();
		}
	}
	
	/**
	 * 显示有数据的收件人信息
	 */
	private void showReceiverInfo(){
		// 获取收件人信息
		String name = prefDAO.getReceiverName();
		String tel = prefDAO.getReceiverTel();
		String add = prefDAO.getReceiverAdd();
		String zipCode = prefDAO.getReceiverZipCode();
		
		tvName.setText(name);
		tvTel.setText(tel);
		tvAdd.setText(add);
		
		receiverEmptyLayout.setVisibility(View.GONE);
		receiverLayout.setVisibility(View.VISIBLE);
	}
	
}
