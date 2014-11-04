package com.brains.app.shopclient.db.dao;

import java.util.List;

import android.content.Context;


import com.brains.app.shopclient.db.entity.Message;
import com.brains.app.shopclient.db.entity.Product;
import com.brains.framework.db.DBHelper;
import com.brains.framework.db.dao.BaseDAO;
import com.brains.framework.db.dao.Finder;

/**
 * 购物车Dao (Product)
 * @author xiect
 *
 */
public class CartDao extends BaseDAO<Product>{
	
	/**
	 * 购物车Dao
	 * @param context
	 */
	public CartDao(Context context){
		 helper = new DBHelper(context);
	}
	
	/**
	 * 商品保存到DB中
	 * @param msg
	 */
	public void save(Product item){
		// 先将card中旧数据删除
		delete(item);
		//  删除后将新数据保存到数据库中
		item.save(helper);
	}
	
	/**
	 * 删除指定的商品
	 * @param itemId
	 */
	public void delete(Product item){
		item.deleteByCondition(helper, "itemId = ?", new String[]{item.getItemId()});
	}
	
	/**
	 * 从购物车中找出指定的商品
	 * @param itemId
	 * @return
	 */
	public Product findById(String itemId){
		List<Product> result = new Finder<Product>(helper).where("itemId = " +itemId + "" ).findAll(Product.class);
		if(result != null && result.size() > 0){
			return result.get(0);
		}
		return null;
	}
    
    /**
     * cart Table all data clear
     */
    public void clearAll(){
    	new Product().deleteAll(helper);
    }
    
    public List<Product> findAll(){
    	return findAll(helper, Product.class);
    }
}
