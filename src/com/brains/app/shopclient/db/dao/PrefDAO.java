package com.brains.app.shopclient.db.dao;

import android.content.Context;

import com.brains.app.shopclient.common.Util;
import com.brains.framework.common.Const;
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
	private final String KEY_NIKE_NAME 	= "key_nikename";
	
	
	private final String KEY_RECEIVER_NAME = "key_receiver_name";
	private final String KEY_RECEIVER_TEL = "key_receiver_tel";
	private final String KEY_RECEIVER_ZIPCODE = "key_receiver_zip_code";
	private final String KEY_RECEIVER_ADD = "key_receiver_add";
	private final String USER_FIRST_KBN = "key_user_first_kbn";
	public PrefDAO(Context context){
		super(context);
	}
	
	/**
	 * ユーザー情報を保存する
	 * @param name ユーザー名
	 * @param passworユーザーパスワード
	 */
	public void saveNikeName(String name){
		mEditor.putString(KEY_NIKE_NAME, name);
		mEditor.commit();
	}
	
	/**
	 *  get nikename
	 * @return
	 */
	public String getNikeName(){
		return mSettings.getString(KEY_NIKE_NAME, "");
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
	
	public String getReceiverName(){
		String name = mSettings.getString(KEY_RECEIVER_NAME, "");
		Util.sysLog("Pref", "receiver name:"+ name);
		return name;
	}
	
	public String getReceiverTel(){
		return mSettings.getString(KEY_RECEIVER_TEL, "");
	}
	
	public String getReceiverAdd(){
		return mSettings.getString(KEY_RECEIVER_ADD, "");
	}
	
	public String getReceiverZipCode(){
		return mSettings.getString(KEY_RECEIVER_ZIPCODE, "");
	}
	
	/**
	 * ユーザー情報を保存する
	 * @param name ユーザー名
	 * @param passworユーザーパスワード
	 */
	public void saveReceiverInfo(String name,String tel,String zipCode,String add){
		mEditor.putString(KEY_RECEIVER_NAME, name);
		mEditor.putString(KEY_RECEIVER_TEL, tel);
		mEditor.putString(KEY_RECEIVER_ZIPCODE, zipCode);
		mEditor.putString(KEY_RECEIVER_ADD, add);
		mEditor.commit();
	}
	/**
	 * 是否第一次打开应用
	 * @param value
	 */
	public void setIsFirstOpen(String value){
		mEditor.putString(USER_FIRST_KBN, value);
		mEditor.commit();
	}
	/**
	 * 是否第一次使用应用
	 */
	public boolean isFirstUse(){
		String value = mSettings.getString(USER_FIRST_KBN,Const.USER_FIRST_NO);
		if(!Util.isEmpty(value) && Const.USER_FIRST_YES.equals(value)){
			return false;
		}
		return true;
	}
}
