package com.brains.app.shopclient.common;


import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.brains.app.shopclient.bean.Brand;
import com.brains.app.shopclient.bean.Category;
import com.brains.app.shopclient.bean.Home;
import com.brains.app.shopclient.bean.Order;
import com.brains.app.shopclient.bean.User;
import com.brains.app.shopclient.db.entity.Product;
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
	 * 获取品牌列表。
	 * @return List<KpResult>
	 */
	public JSONObject getHomeList()
			throws AppException {
		String url = remoteDomain + "/vsisfront/appLogin/showImage.do";
		return http.get(url).asJSONObject();
	}
	
	/**
	 * 获取品牌列表。
	 * @return List<KpResult>
	 */
	public List<Brand> getBrandList()
			throws AppException {
		String url = remoteDomain + "/vsisfront/appLogin/selectBusiness.do";
		return Brand.constructListFromJson(http.get(url).asJSONObject());
	}
	
	/**
	 * 获取品类列表。
	 * 
	 * @param kpdId
	 * @param kpdSubId
	 * @return List<KpResult>
	 */
	public List<Category> getCategoryList()
			throws AppException {
		String url = remoteDomain + "/vsisfront/appLogin/getParentType.do";
		return Category.constructListFromJson(http.get(url).asJSONObject());
	}
	
	/**
	 * 获取二级品类列表。
	 * 
	 * @param kpdId
	 * @param kpdSubId
	 * @return List<KpResult>
	 */
	public List<Category> getSubCategoryList(String categoryId)
			throws AppException {
		String url = remoteDomain + "/vsisfront/appLogin/getSecondType.do";
		return Category.constructListFromJson(http.post(url,createParams(new BasicNameValuePair("parentId", categoryId))).asJSONObject());
	}
	
	public User login(String username, String password) throws AppException {
		Util.sysLog("xxx", "username:"+ username + "\tpassword" + password);
		http.setCredentials(username, password);
		String url = remoteDomain + "/vsisfront/appLogin/appLoginIn.do";
//		String url = remoteDomain + "/webServerTest/TestServlet";
		
		return User.constructFromJson(http.get(url).asJSONObject());
	}
	
	/**
	 * 商品查询
	 * 
	 * @param keyword
	 * @param categoryId
	 * @param brandId
	 * @param sort
	 * @return Response
	 */
	public List<Product> getSerachList(String keyword,String categoryId,String brandId,String sort,int pageNum) throws AppException {
		String url = remoteDomain + "/vsisfront/appLogin/selectProduct.do";
		return Product.constructListFromJson(http.post(url,
				createParams(new BasicNameValuePair("sort", sort),
						new BasicNameValuePair("typeId", categoryId),
						new BasicNameValuePair("name", keyword),
						new BasicNameValuePair("businessId", brandId),
						new BasicNameValuePair("page", String.valueOf(pageNum))))
				.asJSONObject());
	}
	
	/**
	 * 订单查询
	 * @param flag
	 * @return Response
	 */
	public List<Order> getOrderSerachList(String flag) throws AppException {
		String url = remoteDomain + "/vsisfront/appLogin/selectOrder.do";
		return Order.constructListFromJson(http.post(url,
				createParams(new BasicNameValuePair("time", flag)))
				.asJSONObject());
	}
	
	/**
	 * 获取品牌列表。
	 * @return List<KpResult>
	 */
	public boolean doOder(String name,String tel,String add,String itemId,int num)
			throws AppException {
		String url = remoteDomain + "/vsisfront/appLogin/addOrder.do";
		http.post(url,createParams(new BasicNameValuePair("name", name),
				new BasicNameValuePair("tel", tel),
				new BasicNameValuePair("add", add),
				new BasicNameValuePair("num", String.valueOf(num)),
				new BasicNameValuePair("itemId", itemId)));
		return true;
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
