package com.brains.app.shopclient.db.dao;

import java.util.List;

import android.content.Context;


import com.brains.app.shopclient.common.Util;
import com.brains.app.shopclient.db.entity.Message;
import com.brains.framework.db.DBHelper;
import com.brains.framework.db.dao.BaseDAO;
import com.brains.framework.db.dao.Finder;

/**
 * 公告消息Dao (Message)
 * @author xiect
 *
 */
public class MessageDao extends BaseDAO<Message>{
	
	/**
	 * 公告消息Dao
	 * @param context
	 */
	public MessageDao(Context context){
		 helper = new DBHelper(context);
	}
	
	/**
	 * 消息保存到DB中
	 * @param msg
	 */
	public void save(Message msg){
		List<Message> result = new Finder<Message>(helper).where("messageid =" + msg.getMessageId() ).findAll(Message.class);
		if(result != null && result.size() > 0){
			// 已存在该消息的场合不处理
			result.clear();
			result = null;
		}else{
			// 不存在的场合将该公告插入数据库
			msg.save(helper);	
		}
	}
	
    /**
     * 根据List中Message 的消息Id 查询数据库
     * @return List<Message>
     */
    public List<Message> findByIds(List<Message> msgList) {
    	String condition = "";
    	
    	if(msgList != null && msgList.size() > 0){
    		StringBuffer buf = new StringBuffer();
    		Message msg = null;
    		int maxSize = msgList.size();
    		for(int i = 0; i < maxSize; i++){
    			msg = msgList.get(i);
    			if(i == 0){
    				buf.append("'");
    				buf.append(msg.getMessageId());
    				buf.append("'");
    			}else{
    				buf.append(",'");
    				buf.append(msg.getMessageId());
    				buf.append("'");
    			}
    		}
    		condition = buf.toString();
    	}
    	
    	if(Util.isEmpty(condition)){
    		return null;
    	}else{
    		List<Message> result = new Finder<Message>(helper).where("messageid in ( " +condition + ")" ).findAll(Message.class);
        	return result;	
    	}
    }
    
    /**
     * Message Table all data clear
     */
    public void clearAll(){
    	new Message().deleteAll(helper);
    }
}
