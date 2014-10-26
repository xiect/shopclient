package com.brains.app.shopclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.brains.app.shopclient.R;
import com.brains.framework.activities.base.BaseNormalActivity;


public class LoginActivity extends BaseNormalActivity {
	private final static String ACTION_LOGIN = "brains.intent.action.ACTION_LOGIN";

	public static final String TAG = "LoginActivity";
	private Button btnLogin;
	
	/**
	 * 取得登录画面Intent
	 * 
	 * @return
	 */
	public static Intent makeIntent() {
		return new Intent().setAction(ACTION_LOGIN);
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_login);
		btnLogin = (Button) findViewById(R.id.login_comfirm_button);
		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent data = new Intent();
				data.putExtra("user_id", "123456");
				setResult(1, data);
				finish();
			}
		});

	}
}
