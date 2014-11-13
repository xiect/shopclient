package com.brains.app.shopclient.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.brains.app.shopclient.db.entity.Product;
import com.brains.framework.exception.AppException;

public class Home {
	private List<Product> itemList;
	private List<Category> categoryList;
	private List<Product> susumeList;
	
	
	public List<Product> getSusumeList() {
		return susumeList;
	}



	public void setSusumeList(List<Product> susumeList) {
		this.susumeList = susumeList;
	}



	public List<Product> getItemList() {
		return itemList;
	}



	public void setItemList(List<Product> itemList) {
		this.itemList = itemList;
	}



	public List<Category> getCategoryList() {
		return categoryList;
	}



	public void setCategoryList(List<Category> categoryList) {
		this.categoryList = categoryList;
	}



	public static Home constructListFromJson(JSONObject json) throws AppException {
		Home home = new Home();
		List<Category> categoryList = new ArrayList<Category>();
		List<Product> itemList = new ArrayList<Product>();
		List<Product> susumeList = new ArrayList<Product>();
		JSONObject jsonObj = null;
		JSONArray array;
		Category category;
		Product item;
		try {
			array = json.getJSONArray("recomments");
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
			
			
			array = json.getJSONArray("banners");
			if(array != null && array.length() > 0){
				for(int i = 0; i < array.length(); i++){
					jsonObj = array.getJSONObject(i);
					item = new Product();
					item.setItemId(jsonObj.getString("id"));
					item.setUrl(jsonObj.getString("sendUrl"));
					item.setImgSrc(jsonObj.getString("imgSrc"));
					itemList.add(item);
				}
			}
			
			array = json.getJSONArray("products");
			if(array != null && array.length() > 0){
				for(int i = 0; i < array.length(); i++){
					jsonObj = array.getJSONObject(i);
					item = new Product();
					item.setItemId(jsonObj.getString("id"));
					item.setName(jsonObj.getString("name"));
					item.setPrice(jsonObj.getString("price"));
					item.setDesc(jsonObj.getString("desc"));
					item.setImgSrc(jsonObj.getString("imgSrc"));
					susumeList.add(item);
				}
			}
			
			home.setCategoryList(categoryList);
			home.setItemList(itemList);
			home.setSusumeList(susumeList);
		} catch (Exception e) {
			throw new AppException(e);
		}
		
		return home;
	}
}
