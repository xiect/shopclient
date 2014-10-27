package com.brains.app.shopclient.bean;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.brains.framework.exception.AppException;

/**
 * 
 * @author xiect
 *
 */
public class User {
	private String name;
	private String nikeName;
	private String icon;
	private String tel;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNikeName() {
		return nikeName;
	}
	public void setNikeName(String nikeName) {
		this.nikeName = nikeName;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public static User constructFromJson(JSONObject json) throws AppException{
		User user = null;
		try{
			user = new User();
			user.name = json.getString("userName");
			user.nikeName = json.getString("nickName");
			user.tel = json.getString("telephone");
		}catch(JSONException e){
			throw new AppException(e);
		}
		return user;
	}
}
