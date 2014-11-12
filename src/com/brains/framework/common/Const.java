package com.brains.framework.common;

/**
 * 常量
 * @author xiect
 *
 */
public interface Const {
	String DB_MAME = "xiect_shop_db";
	String APP_TAG = "shopclient";
	
//	String REMOTE_SERVER_HOST = "http://210.73.152.199:8083";
	
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
	
	// SHOP TYPE
	String TYPE_ALL = "0";
	String TYPE_1_ONE = "1";
	String TYPE_1_TWO = "2";
	String TYPE_1_THREE = "3";
	
	String TYPE_2_A = "1";
	String TYPE_2_AA = "2";
	String TYPE_2_AAA = "3";
	
	String TYPE_3_4S = "1";
	String TYPE_3_OTHER = "2";
	
	
}
