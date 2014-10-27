package com.brains.app.shopclient.activities.fragment;

import com.brains.app.shopclient.R;
import com.brains.app.shopclient.activities.LoginActivity;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * 购物车画面
 * @author xiect
 *
 */
public class CartFragment extends BaseFragment {
	private static final String TAG = "CartFragment";
	private static final int REQUEST_CODE = 1;
	private Button mBtnLogin;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		fragmentView = inflater.inflate(R.layout.fragment_main_cart, container,false);
		findView();
		return fragmentView;
	}
	
	/**
	 * 查找画面视图及绑定事件处理
	 */
	public void findView(){
		// 登录按钮
		mBtnLogin = (Button)fragmentView.findViewById(R.id.cart_no_login_but);
		mBtnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 登录画面跳转
				getActivity().startActivityForResult(LoginActivity.makeIntent(), REQUEST_CODE);
			}
		});
	}

	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
			// 登录成功的场合
		}
	}

	@Override
	protected void lazyLoad() {
		// TODO Auto-generated method stub
		
	}
}
