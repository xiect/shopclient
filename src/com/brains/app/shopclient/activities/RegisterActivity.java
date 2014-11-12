package com.brains.app.shopclient.activities;

import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;
import org.apache.james.mime4j.field.address.parser.BaseNode;

import com.brains.app.shopclient.R;
import com.brains.app.shopclient.bean.User;
import com.brains.app.shopclient.common.Util;
import com.brains.framework.activities.base.BaseNormalActivity;
import com.brains.framework.exception.AppException;
import com.brains.framework.task.GenericTask;
import com.brains.framework.task.TaskParams;
import com.brains.framework.task.TaskResult;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.AsyncTask.Status;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 注册画面
 * @author xiect
 *
 */
public class RegisterActivity extends BaseNormalActivity {
	
	private static final String ACTION = "brains.intent.action.ACTION_REGISTER";
	private static final String TAG = "RegisterActivity";
	private EditText mNameText;
	private EditText mEmailText;
	private EditText mTelText;
	private EditText mPasswordText1;
	private EditText mPasswordText2;
	
	
	private Button mRegisterButton;
	private Button mCancleButton;
	private RelativeLayout mProgressBarArea;
	
	private GenericTask mRegisterTask;
	
	private String mName;
	private String mMail;
	private String mTel;
	private String mPassword1;
	private String mPassword2;
	private User mUserResult;
	
	/**
	 * 取得注册画面Intent
	 * @return
	 */
	public static Intent makeIntent() {
		Intent intent = new Intent().setAction(ACTION);
		return intent;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layou_register);
		// 设置标题
		TextView tvTitle = (TextView) findViewById(R.id.tv_title);
		tvTitle.setText(R.string.page_title_regster);
		mProgressBarArea = (RelativeLayout)findViewById(R.id.rl_loading_area);
		
		// name
		mNameText = (EditText) findViewById(R.id.nick_input_text);
		
		// mail 
		mEmailText = (EditText) findViewById(R.id.email_input_text);
		
		// tel
		mTelText = (EditText) findViewById(R.id.login_input_tel);
		
		// password
		mPasswordText1 = (EditText) findViewById(R.id.password_input_text_1);
		mPasswordText2 = (EditText) findViewById(R.id.password_input_text_2);
		
		// regist btn
		mRegisterButton = (Button) findViewById(R.id.register_button);
		// cancel btn
		mCancleButton = (Button) findViewById(R.id.cancle_button);
		findViewById(R.id.register_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(doInputCheck()){
					doRegister();	
				}
			}
		});
		
		findViewById(R.id.cancle_button).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}	
	
	
	private boolean doInputCheck(){
		mName = mNameText.getEditableText().toString();
		if(TextUtils.isEmpty(mName)) {
			app.showMsgWithToast("登录账号由5-20个英文字母或数字组成");
			return false;
		}
		
		mMail = mEmailText.getEditableText().toString();
		if(TextUtils.isEmpty(mMail)) {
			app.showMsgWithToast("请输入正确的邮箱地址");
			return false;
		}
		
		mTel = mTelText.getEditableText().toString();
		if(TextUtils.isEmpty(mTel)) {
			app.showMsgWithToast("请输入正确的联系方式");
			return false;
		}
		
		mPassword1 = mPasswordText1.getEditableText().toString();
		if(TextUtils.isEmpty(mPassword1)) {
			app.showMsgWithToast("登录密码由5-20个英文字母或数字组成");
			return false;
		}
		
		mPassword2 = mPasswordText2.getEditableText().toString();
		if(TextUtils.isEmpty(mPassword2)) {
			app.showMsgWithToast("确认密码由5-20个英文字母或数字组成");
			return false;
		}
		
		if(!mPassword1.equals(mPassword2)){
			app.showMsgWithToast("两次输入的密码不一致");
			return false;
		}
		
		return true;
	}
	
	private void doRegister() {		
		if(mRegisterTask != null && mRegisterTask.getStatus() == Status.RUNNING) {
			return;
		} else {
			mRegisterTask = new RegisterTask();
			mRegisterTask.execute();
		}
	}
	
	private class RegisterTask extends GenericTask {
		String message = null;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressBarArea.setVisibility(View.VISIBLE);
			setWidgetEnable(false);
		}
		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
			try {
				mUserResult = app.mApi.regist(mName, mMail, mTel, mPassword1, mPassword2);
				Util.sysLog(TAG, mUserResult.getName());
				return TaskResult.OK;
			} catch (AppException e) {
				message = e.getMessage();
				e.printStackTrace();
			}

			return TaskResult.FAILED;
		}
		
		@Override
		protected void onPostExecute(TaskResult result) {
			super.onPostExecute(result);
			mProgressBarArea.setVisibility(View.GONE);
			setWidgetEnable(true);
			// 加载的场合
			if (TaskResult.OK == result) {
				// 返回成功
				// 保存用户名密码
				app.saveUserInfo(mName,mPassword1);
				// 保存别名
				app.mPrefDAO.saveNikeName(mUserResult.getNikeName());
				app.showMsgWithToast(R.string.register_success);
			} else {
				if(TaskResult.FAILED == result && message != null && message.length() > 0){
					Log.d(TAG, "layout_no_data");
					app.showErrorWithToast(message);
				}else{
					Log.d(TAG, "layout_no_data");
					app.showErrorWithToast("这位客官遇到点小麻烦，请重新登录!");
				}
			}
			
		}
		
	}
	
	private void setWidgetEnable(boolean enable) {
		mNameText.setEnabled(enable);
		mEmailText.setEnabled(enable);
		mTelText.setEnabled(enable);
		mPasswordText1.setEnabled(enable);
		mPasswordText2.setEnabled(enable);
		
		
		mRegisterButton.setEnabled(enable);
		mCancleButton.setEnabled(enable);
	}
}
