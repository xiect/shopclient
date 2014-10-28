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
//			{"message":"登录成功！","records":[]{"nickName":"张三","telephone":"null","email":"123456789@163.com","userName":"zhangsan"},"status":"0"}
			user = new User();
			JSONObject jsonUser = json.getJSONObject("user");
			user.name = jsonUser.getString("userName");
			user.nikeName = jsonUser.getString("nickName");
			user.tel = jsonUser.getString("telephone");
			jsonUser = null;
			json = null;
		}catch(JSONException e){
			throw new AppException(e);
		}
		return user;
	}
}
