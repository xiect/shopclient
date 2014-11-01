package com.brains.app.shopclient.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import com.brains.app.shopclient.R;
import com.brains.app.shopclient.bean.User;
import com.brains.app.shopclient.common.Util;
import com.brains.framework.activities.base.BaseNormalActivity;
import com.brains.framework.exception.AppException;
import com.brains.framework.task.GenericTask;
import com.brains.framework.task.TaskParams;
import com.brains.framework.task.TaskResult;


/**
 * 登录画面
 * @author xiect
 *
 */
public class LoginActivity extends BaseNormalActivity {
	private final static String ACTION_LOGIN = "brains.intent.action.ACTION_LOGIN";

	public static final String TAG = "LoginActivity";
	private Button btnLogin;
    private GenericTask mLoginTask;
    private User mLoginResult;
    private RelativeLayout mProgressBarArea;
    private EditText mTvName;
    private EditText mTvPassword;
    private String mUserName;
    private String mPassword;
	
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
		mProgressBarArea = (RelativeLayout) findViewById(R.id.rl_loading_area);
		mTvName = (EditText) findViewById(R.id.login_input_name);
		mTvPassword = (EditText) findViewById(R.id.login_input_password);
		
		btnLogin = (Button) findViewById(R.id.login_comfirm_button);
		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String name = mTvName.getText().toString();
				String psd = mTvPassword.getText().toString();
				
				Util.sysLog(TAG, "name:" + name + "\t psd:" + psd);
				if(checkInput(name,psd)){
					doLogin(name,psd);	
				}
			}
		});

	}
	
	/**
	 * 入力校验
	 * @param name
	 * @param pwd
	 * @return
	 */
	private boolean checkInput(String name,String pwd){
		if(Util.isEmpty(name)){
			app.showErrorWithToast(R.string.login_user_name_error_hint);
			return false;
		}
		if(Util.isEmpty(pwd)){
			app.showErrorWithToast(R.string.login_user_password_hint);
			return false;
		}
		return true;
	}
	
	private void doLogin(String name ,String pwd){
		mUserName = name;
		mPassword = pwd;
		
		if (mLoginTask != null
				&& mLoginTask.getStatus() == GenericTask.Status.RUNNING) {
			return;
		} else {
			mLoginTask = new DoLoginTask();
			mLoginTask.execute();
		}
	}
	
	/**
	 * 获取Category列表数据Task
	 * 
	 * @author xiect
	 * 
	 */
	private class DoLoginTask extends GenericTask {
		String message = null;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressBarArea.setVisibility(View.VISIBLE);
		}

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
			try {
				// 获得网络数据
				String username = mUserName;
				String password = mPassword;
				mLoginResult = app.mApi.login(username, password);
				Util.sysLog(TAG, mLoginResult.getName());
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
			
			// 加载的场合
			if (TaskResult.OK == result) {
				// 返回成功
				// 保存用户名密码
				app.saveUserInfo(mUserName,mPassword);
				// 保存别名
				app.mPrefDAO.saveNikeName(mLoginResult.getNikeName());
				save4Result(Activity.RESULT_OK,mLoginResult,null);
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
	
	
	
	/**
	 * 保存结果，并退出画面
	 * @param result
	 * @param msg
	 */
	private void save4Result(int result,User user,String msg){
		Intent data = new Intent();
		if(user != null){
			data.putExtra("user_json", user.getName());	
		}
		if(Util.isEmpty(msg)){
			data.putExtra("ERROR_MSG", msg);
		}
		
		setResult(result, data);
		finish();
	}
}
