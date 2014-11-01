package com.brains.app.shopclient.common;

import java.util.ArrayList;
import java.util.List;

import com.brains.app.shopclient.db.dao.CartDao;
import com.brains.app.shopclient.db.entity.Product;

import android.content.Context;

/**
 * 购物车管理者
 * @author xiect
 *
 */
public class CartManager {
	
	public List<Product> cartItemList = new ArrayList<Product>();
	private Context context;
	private CartDao dao;
	
	public CartManager(Context ctx){
		context = ctx;
		dao = new CartDao(ctx);
		List<Product> retList = new ArrayList<Product>();
		Product item  = null;
		for(int i = 0; i < 10; i++){
			item = new Product();
			item.setName("Iphone 6");
			item.setItemId(String.valueOf(i));
			item.setDesc("超级垃圾的手机 会弯的噢");
			item.setPrice("5600");
			item.setImgSrc("http://p.zdmimg.com/201410/19/544323c8a5705.jpg_n4.jpg");
			retList.add(item);
		}
		cartItemList =  retList;
	}
	
	/**
	 * 加入一件商品
	 * @param item
	 */
	public void addProduct(Product item){
		Product p = dao.findById(item.getItemId());
		if(p != null && Util.isEmpty(p.getItemId())){
			// cart 中存在该商品的场合,更新cart 中的商品数量
			int num = p.getNum() + item.getNum();
			item.setNum(num);
		}
		dao.save(item);
	}
	
	/**
	 * 购物车中删除商品
	 * @param item
	 */
	public void deleteProduct(Product item){
		dao.delete(item);
	}
	
	/**
	 * 购物车是否是空
	 * @return
	 */
	public boolean isCartEmpty(){
		List<Product> list = dao.findAll();
		if(list != null && list.size() > 0 ){
			return true;
		}
		return false;
	}
	
}
