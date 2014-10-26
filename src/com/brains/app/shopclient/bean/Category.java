package com.brains.app.shopclient.bean;

import java.util.List;

import org.json.JSONObject;

/**
 * 品类
 * @author xiect
 *
 */
public class Category {
	
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
	
	public static List<Category> constructListFromJson(JSONObject json){
		return null;
	}
}
