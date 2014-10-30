package com.brains.app.shopclient.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.brains.framework.exception.AppException;

/**
 * 品类
 * @author xiect
 *
 */
public class Category {
	
	private String id;
	private String name;
	private String desc;
	private String imgSrc;
	
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
	public static List<Category> constructListFromJson(JSONObject json) throws AppException {
		List<Category> categoryList = new ArrayList<Category>();
		Category category = null;
		JSONObject jsonObj = null;
		JSONArray array;
		try {
			array = json.getJSONArray("records");
			if(array != null && array.length() > 0){
				for(int i = 0; i < array.length(); i++){
					jsonObj = array.getJSONObject(i);
					category = new Category();
					category.setId(jsonObj.getString("id"));
					category.setName(jsonObj.getString("name"));
					category.setDesc(jsonObj.getString("desc"));
					category.setImgSrc(jsonObj.getString("imgSrc"));
					categoryList.add(category);
				}
			}
		} catch (Exception e) {
			throw new AppException(e);
		}
		
		return categoryList;
	}
}
