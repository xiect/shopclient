package com.brains.app.shopclient.common;

import android.util.Log;

import com.brains.framework.common.BaseUtil;

/**
 * アプリ内でのコアな共通処理
 * @author id:language_and_engineering
 *
 */
public class Util extends BaseUtil {

    // アプリの紹介URLとか？
    //public static String getMarketURIOfThisApp();

	/**
	 * 字符串是否是空
	 * @param str
	 * @return true 空 false 不是空
	 */
	public static boolean isEmpty(String str){
		if(str == null || str.length() < 1){
			return true;
		}
		return false;
	}
	
	/**
	 * 将null字符串转换为 空串“”
	 * @param str
	 * @return
	 */
	public static String nvl(String str){
		if(str == null){
			return "";
		}else{
			return str;
		}
	}
	
	/**
	 * 统一日志入口
	 * @param tag
	 * @param message
	 */
	public static void sysLog(String tag,String message){
		Log.e(tag,message);
	}
}
