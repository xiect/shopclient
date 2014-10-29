package com.brains.app.shopclient.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.brains.framework.exception.AppException;

public class Brand {
	
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
	public static List<Brand> constructListFromJson(JSONObject json) {
		List<Brand> brandList = new ArrayList<Brand>();
		Brand brand = null;
		JSONObject jsonObj = null;
		JSONArray array;
		try {
			array = json.getJSONArray("records");
			if(array != null && array.length() > 0){
				for(int i = 0; i < array.length(); i++){
					jsonObj = array.getJSONObject(i);
					brand = new Brand();
					brand.setId(jsonObj.getString("id"));
					brand.setName(jsonObj.getString("name"));
					brand.setDesc(jsonObj.getString("desc"));
					brand.setImgSrc(jsonObj.getString("imgSrc"));
					brandList.add(brand);
				}
			}
		} catch (JSONException e) {
			new AppException(e);
		}
		
		return brandList;
	}
}
