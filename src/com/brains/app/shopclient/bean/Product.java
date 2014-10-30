package com.brains.app.shopclient.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.brains.framework.exception.AppException;

/**
 * 商品
 * @author xiect
 *
 */
public class Product {
	
	private String id;
	private String name;
	private String desc;
	private String price;
	private String imgSrc;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getImgSrc() {
		return imgSrc;
	}
	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}
	public static List<Product> constructListFromJson(JSONObject json) throws AppException {
		List<Product> productList = new ArrayList<Product>();
		Product product = null;
		JSONObject jsonObj = null;
		JSONArray array;
		try {
			array = json.getJSONArray("records");
			if(array != null && array.length() > 0){
				for(int i = 0; i < array.length(); i++){
					jsonObj = array.getJSONObject(i);
					product = new Product();
					product.setId(jsonObj.getString("id"));
					product.setName(jsonObj.getString("name"));
					product.setDesc(jsonObj.getString("desc"));
					product.setPrice(jsonObj.getString("price"));
					product.setImgSrc(jsonObj.getString("imgSrc"));
					productList.add(product);
				}
			}
		} catch (Exception e) {
			throw new AppException(e);
		}
		
		return productList;
	}
}
