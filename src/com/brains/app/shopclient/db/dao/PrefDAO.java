package com.brains.app.shopclient.db.dao;

import android.content.Context;

import com.brains.framework.db.dao.BasePrefDAO;


/**
 * アプリのプリファレンスを扱うクラス。
 * @author id:language_and_engineering
 *
 */
public class PrefDAO extends BasePrefDAO {
    // ここにプリファレンスの読み書き処理を記述。
    // FWPrefDAOなどを参考に。
	
	private final String KEY_NAME 		= "key_name";
	private final String KEY_PASSWORD 	= "key_password";
	
	public PrefDAO(Context context){
		super(context);
	}
	
	/**
	 * ユーザー情報を保存する
	 * @param name ユーザー名
	 * @param passworユーザーパスワード
	 */
	public void saveUserNameAndPassword(String name,String password){
		mEditor.putString(KEY_NAME, name);
		mEditor.putString(KEY_PASSWORD, password);
		mEditor.commit();
	}
	
	/**
	 * ユーザー情報・名前を取得
	 * @return String ユーザー名
	 */
	public String getUserName(){
		return mSettings.getString(KEY_NAME, "");
	}
	
	/**
	 * ユーザー情報・パスワードを取得
	 * @return String パスワード
	 */
	public String getUserPassword(){
		return mSettings.getString(KEY_PASSWORD, "");
	}
	
	/**
	 * 登録しているユーザーの情報をクリアーする
	 */
	public void cleanUserInfo(){
		mEditor.remove(KEY_NAME);
		mEditor.remove(KEY_PASSWORD);
		mEditor.commit();
	}
}
