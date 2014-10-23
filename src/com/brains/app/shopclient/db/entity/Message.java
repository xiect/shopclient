package com.brains.app.shopclient.db.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.brains.app.shopclient.db.entity.lib.LogicalEntity;
import com.brains.framework.exception.AppException;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;



/**
 * 已读消息表
 * @author xiect
 *
 */
public class Message extends LogicalEntity<Message> implements Parcelable {
	private static final long serialVersionUID = 1L;
	
	public Message(){
		
	}
	
	public Message(Parcel p){
		messageId = p.readString();
		msgTitle = p.readString();
		msgContent = p.readString();
		createTime = p.readString();
	}
	
	@Override
	public String tableName() {
		return "messages";
	}

	@Override
	public String[] columns() {
		return new String[] { "messageid", "msgtitle", "createtime" };
	}
	
	private String messageId;
	
	// 公告标题
	private String msgTitle;
	
	// 公告内容
	private String msgContent;
	
	// 创建时间
	private String createTime;
	
	// 是否已读
	private boolean isRead;
	
	
	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getMsgTitle() {
		return msgTitle;
	}

	public void setMsgTitle(String msgTitle) {
		this.msgTitle = msgTitle;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public String getCreateTime() {
		if(createTime!=null){
			createTime = createTime.substring(0, 16);
		}
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	@Override
	public Message logicalFromPhysical(Cursor c) {
		setMessageId(c.getString(0));
	        setMsgTitle(c.getString(1));
	        setCreateTime( c.getString(2) );
		return this;
	}

	@Override
	protected ContentValues toPhysicalEntity(ContentValues values) {
		if( getMessageId() != null) {
			values.put("messageid", getMessageId());
		}
		values.put("msgtitle", getMsgTitle());
		values.put("createtime", getCreateTime());
		return values;
	}

	/**
	 * 
	 * @param json
	 * @return
	 */
	public static List<Message> constructMessageList(JSONObject jsonObj) throws AppException{
		List<Message> list = null;
		try {
			
			JSONArray array = jsonObj.getJSONArray("msgs");
			
			int len = array.length();
			list = new ArrayList<Message>(len);
			JSONObject obj = null;
			Message bean = null;
			for(int i = 0; i < len; i++){
				obj = array.getJSONObject(i);
				bean = new Message();
				bean.messageId = obj.getString("messageId");
				bean.msgTitle = obj.getString("msgTitle");
				bean.msgContent = obj.getString("msgContent");
				bean.createTime = obj.getString("createTime");
				list.add(bean);
			}
		} catch (Exception e) {
			throw new AppException(e.getMessage());
		}
		return list;
	}

	/**
	 * Intent 传值
	 */
	public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>(){
		public Message createFromParcel(Parcel p) {
			return new Message(p);
		}

		@Override
		public Message[] newArray(int size) {
			return new Message[size];
		}
	};
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel p, int arg1) {
		p.writeString(messageId);
		p.writeString(msgTitle);
		p.writeString(msgContent);
		p.writeString(createTime);
	}
}
