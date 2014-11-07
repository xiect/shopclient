package com.brains.framework.common;

/**
 * 常量
 * @author xiect
 *
 */
public interface Const {
	String DB_MAME = "xiect_shop_db";
	String APP_TAG = "shopclient";
	
//	String REMOTE_SERVER_HOST = "http://192.168.1.104:8080";
	
	String REMOTE_SERVER_HOST = "http://proem.meibu.net:8085";
	 
	String SUCCESS = "0";
	String RESULT_FLAG = "status";
	
	long GPS_UPDATE_INTERVAL = 60;
	
	float GPS_MIN_DISPLACEMENT = 8;
	
	// 商品排序
	String SORT_DEFAULT = "0";    // 默认排序
	String SORT_PRICE_ASC = "1";  // 升序
	String SORT_PRICE_DESC = "2"; // 降序
	int PAGE_COUNT = 20; //  每页显示的件数
	int MAX_NUM = 100;
	String BTN_OK = "完成";
	String BTN_EDIT = "编辑";
	
	String ORDER_ONE_MONTH = "0";    		// 近一个月
	String ORDER_ONE_MONTH_BEFORE = "1";    // 一个月之前
	
	String USER_FIRST_YES = "YES";
	String USER_FIRST_NO = "N0";
	
	String ORDER_STATUS_1 = "完成";
	String ORDER_STATUS_2 = "完成";
	String ORDER_STATUS_3 = "完成";
	String ORDER_STATUS_4 = "完成";
	String ORDER_STATUS_5 = "完成";
	String ORDER_STATUS_6 = "完成";
	String ORDER_STATUS_7 = "完成";
	String ORDER_STATUS_8 = "完成";
	String ORDER_STATUS_9 = "完成";
	
	
}
