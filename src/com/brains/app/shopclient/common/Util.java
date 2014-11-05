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
	
	/**
	 * 将数据格式化RMB表示
	 * @param rmb
	 * @return
	 */
	public static String formatRmb(String rmb){
		
		return "￥"+rmb; 
	}
	
	/**
	 * 将数据格式化商品数量表示
	 * @param num
	 * @return
	 */
	public static String formatCount(int num){
		
		return "数量："+num; 
	}
	
	/**
	 * 取得价格
	 * @param price
	 * @return
	 */
	public static float conver2Price(String price){
		float p = 0f;
		if(!isEmpty(price)){
			try{
				p = Float.valueOf(price);
			}catch(Exception e){
				p = 0f;
			}
		}
		return p;
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static String getOrderStatusDesc(String code){
			String msg = "";
			return msg;
	};
}
