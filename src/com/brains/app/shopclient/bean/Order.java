package com.brains.app.shopclient.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.brains.framework.exception.AppException;

/**
 * 品类
 * @author xiect
 *
 */
/**
 * @author xiect
 *
 */
public class Order {
	
	private String id;
	private String state;
	private String totalPrice;
	private String orderTime;
	private String tel;
	private String name;
	private String paymentNo;
	private String remark;
	private String imgSrc;
	private String pName;
	
	public String getpName() {
		return pName;
	}



	public void setpName(String pName) {
		this.pName = pName;
	}



	public String getImgSrc() {
		return imgSrc;
	}



	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getState() {
		return state;
	}



	public void setState(String state) {
		this.state = state;
	}



	public String getTotalPrice() {
		return totalPrice;
	}



	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}



	public String getOrderTime() {
		return orderTime;
	}



	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}



	public String getTel() {
		return tel;
	}



	public void setTel(String tel) {
		this.tel = tel;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getPaymentNo() {
		return paymentNo;
	}



	public void setPaymentNo(String paymentNo) {
		this.paymentNo = paymentNo;
	}



	public String getRemark() {
		return remark;
	}



	public void setRemark(String remark) {
		this.remark = remark;
	}



	public static List<Order> constructListFromJson(JSONObject json) throws AppException {
		List<Order> orderList = new ArrayList<Order>();
		Order order = null;
		JSONObject jsonObj = null;
		JSONArray array;
		try {
			array = json.getJSONArray("records");
			if(array != null && array.length() > 0){
				for(int i = 0; i < array.length(); i++){
					jsonObj = array.getJSONObject(i);
					order = new Order();
					order.setId(jsonObj.getString("id"));
					order.setName(jsonObj.getString("name"));
					order.setState(jsonObj.getString("state"));
					order.setTotalPrice(jsonObj.getString("totalPrice"));
					order.setOrderTime(jsonObj.getString("orderTime"));
					order.setTel(jsonObj.getString("tel"));
					order.setPaymentNo(jsonObj.getString("paymentNo"));
					order.setRemark(jsonObj.getString("remark"));
					order.setpName(jsonObj.getString("pName"));
					order.setImgSrc(jsonObj.getString("imgSrc"));
					orderList.add(order);
				}
			}
		} catch (Exception e) {
			throw new AppException(e);
		}
		
		return orderList;
	}
}
