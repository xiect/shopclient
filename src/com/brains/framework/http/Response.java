package com.brains.framework.http;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.brains.framework.exception.AppException;
import com.brains.framework.common.Const;

/**
 * HTTP通信结果封装对象
 * @author id:language_and_engineering
 *
 */
public class Response implements Serializable {
    private static final long serialVersionUID = 1L;


    private String err_msg;
    private boolean request_success_flag = false;
    private String responseText;


    /**
     * 错误消息
     */
    public Response err(String err_msg){
        this.err_msg = err_msg;
        request_success_flag = false;
        return this;
    }


    /**
     * 保持 通信成功后的结果
     * @param responseText
     */
    public void setTextOnSuccess(String responseText) {
        this.responseText = responseText;
        request_success_flag = true;
    }


    /**
     * 是否通信成功
     */
    public boolean isSuccess() {
        return request_success_flag;
    }


    /**
     * 通信成功后取得通信的Text返回值
     */
    public String getText() {
        return responseText;
    }


    /**
     * 返回通信失败的错误消息
     */
    public String getErrMsg() {
        return err_msg;
    }
    
    public JSONObject asJSONObject() throws AppException {
    	JSONObject jsonObj = null;
    	String message = "";
        try {
        	String temp = responseText;
        	if(temp.length() > 4){
        		jsonObj = new JSONObject(temp);
        		if(Const.SUCCESS.equals(jsonObj.getString("status"))){
        			// json 格式返回中状态为success
        			return jsonObj;
        		}else{
        			// json 格式返回中状态为error
        			message = jsonObj.getString("message");
        			throw new AppException(message);
        		}
        	}else{
        		return null;
        	}
        } catch (JSONException jsone) {
            throw new AppException(jsone.getMessage(), jsone);
        }
    }

    public JSONArray asJSONArray() throws AppException {
        try {
            return new JSONArray(responseText);
        } catch (Exception jsone) {
            throw new AppException(jsone.getMessage(), jsone);
        }
    }
}
