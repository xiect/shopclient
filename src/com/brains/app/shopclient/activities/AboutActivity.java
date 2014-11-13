package com.brains.app.shopclient.activities;



import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.brains.app.shopclient.R;
import com.brains.framework.activities.base.BaseNormalActivity;

/**
 * 关于
 * @author xiect
 *
 */
public class AboutActivity extends BaseNormalActivity {
	private static final String ACTION = "brains.intent.action.ACTION_ABOUT";
	
	/**
	 * 取得关于画面Intent
	 * @return
	 */
	public static Intent makeIntent() {
		Intent intent = new Intent().setAction(ACTION);
		return intent;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_activity);
		TextView tvTitle = (TextView) findViewById(R.id.tv_title);
		tvTitle.setText(R.string.page_title_about);
	}
}
