package com.brains.app.shopclient.common;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.util.Log;

import com.brains.app.shopclient.bean.Category;
import com.brains.framework.common.Const;
import com.brains.framework.exception.AppException;
import com.brains.framework.http.HttpClient;

/**
 * 服务器通信wrap
 * 
 * @author xiect
 * 
 */
public class Api implements java.io.Serializable {
	
	private String remoteDomain = Const.REMOTE_SERVER_HOST;

	private static final long serialVersionUID = 1L;
	
	private HttpClient http = null;

	public HttpClient getHttp() {
		return http;
	}

	public void setHttp(HttpClient http) {
		this.http = http;
	}

	public Api() {
		http = new HttpClient();
	}

	/**
	 * Reset HttpClient's Credentials
	 */
	public void reset() {
		http.reset();
	}
	
	/**
	 * 生成POST Parameters助手
	 * 
	 * @param nameValuePair
	 *            参数(一个或多个)
	 * @return post parameters
	 */
	private ArrayList<BasicNameValuePair> createParams(
			BasicNameValuePair... nameValuePair) {
		ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		for (BasicNameValuePair param : nameValuePair) {
			params.add(param);
		}
		return params;
	}

	// ===============
	// Api 远程调用方法
	// ===============
	/**
	 * 考评单下一级列表。
	 * 
	 * @param kpdId
	 * @param kpdSubId
	 * @return List<KpResult>
	 */
	public List<Category> getCategoryList()
			throws AppException {
		String url = remoteDomain + "/phoneapp/control/queryInstDetailItemList";
		
		Category category1 = new Category();
		category1.setName("手机");
		category1.setName("电子产品手机电池电池电池电池电池电池");
		category1.setImgSrc("http://p.zdmimg.com/201410/24/5449c05168659.jpg_v1.jpg");
		Category category2 = new Category();
		category2.setName("口酒");
		category2.setName("使用天然原料的Burt's Bees 小蜜蜂品牌护手霜");
		category2.setImgSrc("http://p.zdmimg.com/201410/26/544cbe4370f4b.jpg_v1.jpg");
		Category category3 = new Category();
		category3.setName("滤网");
		category3.setName("百人城利口酒，20ml餐后一口闷~Berentzen百人");
		category3.setImgSrc("http://p.zdmimg.com/201410/26/544cc76903683.jpg_v1.jpg");
		List<Category> retList = new ArrayList<Category>();
		retList.add(category1);
		retList.add(category2);
		retList.add(category3);
		
		return null;
//		return Category.constructListFromJson(http.get(url).asJSONObject());
	}
//
//	/**
//	 * 系统公告列表获的。
//	 * 
//	 * @param maxId
//	 * @return Response
//	 */
//	public List<Message> getMessageList(String maxId) throws AppException {
//		String url = remoteDomain + "/phoneapp/control/querySysMessage";
//		return Message.constructMessageList(http.post(url,
//				createParams(new BasicNameValuePair("maxId", maxId)))
//				.asJSONObject());
//	}
//
//	// 登陆操作
//	// 返回true连接成功，返回false网络连接失败
//	public JSONObject login(String username, String password) throws AppException {
//		http.setCredentials(username, password);
//		String url = remoteDomain + "/phoneapp/control/login?userLoginId=" + username
//				+ "&password=" + password;
//		return http.get(url).asJSONObject();
//	}
//
//	/**
//	 * 根据考评对象（店铺）获取考评单列表操作
//	 * @param kpId
//	 * @return
//	 * @throws AppException
//	 */
//	public JSONObject queryReviewerKpb(String kpId) throws AppException {
//		String url = remoteDomain + "/phoneapp/control/queryReviewerKpb";
//		return http.post(url,createParams(new BasicNameValuePair("kpId", kpId))).asJSONObject();
//	}
//
//	/**
//	 * 获取考评单所有项目明细
//	 * 
//	 * @param instId
//	 * @param instItemId
//	 * @return
//	 * @throws AppException
//	 */
//	public JSONObject queryInstDetailInfo(String instId, String instItemId)
//			throws AppException {
//		String url = remoteDomain + "/phoneapp/control/queryInstDetailInfo";
//		JSONObject jo = http.post(
//				url,
//				createParams(new BasicNameValuePair("instId", instId),
//						new BasicNameValuePair("instItemId", instItemId)))
//				.asJSONObject();
//
//		return jo;
//	}
//
//	/**
//	 * 根据检索条件查找门店/标超
//	 * @return
//	 */
//	public List<Kps> getShopList(String keyWord,int page) throws AppException {
//		String url = remoteDomain + "/phoneapp/control/queryShopList";
//		return Kps.constructKpsList(http.post(url, createParams(new BasicNameValuePair("index", String.valueOf(page)), 
//				new BasicNameValuePair("keyword", keyWord))).asJSONObject());
//	}
//	
//	/**
//	 * 查找所有门店/标超
//	 * @return
//	 */
//	public List<Kps> queryAllKps() throws AppException {
//		String url = remoteDomain + "/phoneapp/control/queryShopList";
//		return Kps.constructKpsList(http.get(url, null).asJSONObject());
//	}
//
//	/*
//	 * 保存考评结果
//	 */
//	public Map<String, String> saveResult(Map<String, Object> saveresult) throws AppException, JSONException {
//		Map<String, String> mapresult = FastMap.newInstance();
//		Gson gson = new Gson();
//
//		String url = remoteDomain + "/phoneapp/control/saveKaoPingResult";
//		JSONObject jo = http.post(
//				url,
//				createParams(
//						new BasicNameValuePair("instId", saveresult.get("instId").toString()), 
//						new BasicNameValuePair("instItemId", saveresult.get("instItemId").toString()), 
//						new BasicNameValuePair("uuid", saveresult.get("uuid").toString()), 
//						new BasicNameValuePair("json", gson.toJson(saveresult.get("json")))))
//				.asJSONObject();
////		if(UtilValidate.isEmpty(jo)){
////			mapresult.put("status", "error");
////			mapresult.put("message","网络请求异常");
////			return mapresult;
////		}
//		Object status = jo.get("status");
//		if("success".equals(status)){
//			mapresult.put("status", "success");
//		}else{
//			mapresult.put("status", "error");
//			mapresult.put("message",jo.get("message").toString());
//		}
//
//		return mapresult;
//	}
//	
//	/**
//	 * 取所有已经考评过的考评单、考评单子项列表
//	 * @return
//	 * @throws AppException
//	 */
//	public List<KpdTree> queryCompletedKpd(String kpObjId) throws AppException{
//		String url = remoteDomain + "/phoneapp/control/queryReviewKpbInst";
//		JSONObject resultJson = http.post(url, createParams(
//				new BasicNameValuePair("kpId", kpObjId))).asJSONObject();
//		List<KpdTree> ktList = KpdTree.constructKpdTreeList(resultJson);
//		return ktList;
//	}
//	
//	/**
//	 * 根据GPS地理位置信息获取当前所在店铺
//	 * @param location
//	 * @return
//	 * @throws AppException
//	 */
//	public Kps queryShopByLocation(Location location) throws AppException, JSONException {
//		String url = remoteDomain + "/phoneapp/control/queryShopByLocation";
//		JSONObject jo = http.post(
//				url,
//				createParams(
//					new BasicNameValuePair("jingdu", String.valueOf(location.getLatitude())),
//					new BasicNameValuePair("weidu", String.valueOf(location.getLongitude())))
//				).asJSONObject();
//		Kps kps = new Kps();
//		kps.setKpId(jo.getString("kpId"));
//		kps.setCategoryId(jo.getString("categoryId"));
//		kps.setKpName(jo.getString("kpName"));
//		kps.setDescription(jo.getString("description"));
//		kps.setGeoJingdu(jo.getDouble("geoJingdu"));
//		kps.setGeoWeidu(jo.getDouble("geoWeidu"));
//		return kps;
//	}
//
//	/**
//	 * 保存考评结果附件
//	 * @return
//	 * @throws AppException
//	 * @throws JSONException 
//	 */
//	public Map<String, String> saveResultAccessories(Picture pic) throws AppException, JSONException{
//		String url = remoteDomain + "/phoneapp/control/saveInstResultContent";
//		Map<String, String> mapresult = FastMap.newInstance();
//		JSONObject resultJson = http.post(url, createParams(
//				new BasicNameValuePair("detailItemId", Util.nvl(pic.getDetailItemId())),
//				new BasicNameValuePair("kpInstId", Util.nvl(pic.getKpInstId())),
//				new BasicNameValuePair("instItemId", Util.nvl(pic.getInstItemId())),
//				new BasicNameValuePair("kpItemId", Util.nvl(pic.getKpItemId())),
//				new BasicNameValuePair("detailJingdu", Util.nvl(pic.getDetailJingdu())),
//				new BasicNameValuePair("detailWeidu", Util.nvl(pic.getDetailWeidu())),
//				new BasicNameValuePair("uuid", Util.nvl(pic.getUuid()))
//				),new File(pic.getFilePath()),"file").asJSONObject();
//		Object status = resultJson.get("status");
//		if("success".equals(status)){
//			mapresult.put("status", "success");
//		}else{
//			mapresult.put("status", "error");
//			mapresult.put("message",resultJson.get("message").toString());
//		}
//		return mapresult;
//	}
}
