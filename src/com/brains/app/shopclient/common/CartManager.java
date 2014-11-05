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
	private CartDao dao;
	private int totalNum; // 总件数
	
	/**
	 * remove the item which num is zoro
	 * @return
	 */
	public void deleteZoroProduct(Context ctx){
		List<Product> waitDelList = new ArrayList<Product>(5);
		for(Product item:cartItemList){
			if(item.getNum() <= 0){
				// 从cart 中删除
				deleteProduct(item,ctx);
				waitDelList.add(item);
			}
		}
		
		if(waitDelList.size() > 0){
			cartItemList.removeAll(waitDelList);
		}
	}
	
	/**
	 * get count of all item in cart
	 * @return
	 */
	public int getAllItemCountRealTime(Context ctx){
		dao = new CartDao(ctx);
		List<Product> list = dao.findAll();
		if(list != null){
			return list.size();
		}
		return 0;
	}
	
	public CartManager(){
		
		
//		List<Product> retList = new ArrayList<Product>();
//		Product item  = null;
//		for(int i = 0; i < 10; i++){
//			item = new Product();
//			item.setName("Iphone 6");
//			item.setItemId(String.valueOf(i));
//			item.setDesc("超级垃圾的手机 会弯的噢");
//			item.setPrice("5600");
//			item.setNum(i);
//			item.setImgSrc("http://p.zdmimg.com/201410/19/544323c8a5705.jpg_n4.jpg");
//			retList.add(item);
//		}
//		cartItemList =  retList;
	}
	
	/**
	 * 加入一件商品
	 * @param item
	 */
	public void addProduct(Product item,Context ctx){
		Util.sysLog("cart", "add 2 cart:" + item.getItemId());
		dao = new CartDao(ctx);
		Product p = dao.findById(item.getItemId());
		if(p != null && !Util.isEmpty(p.getItemId())){
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
	public void deleteProduct(Product item,Context ctx){
		dao = new CartDao(ctx);
		dao.delete(item);
	}
	
	/**
	 * 购物车是否是空
	 * @return
	 */
	public boolean isCartEmpty(){
//		List<Product> list = dao.findAll();
		if(cartItemList != null && cartItemList.size() > 0 ){
			return false;
		}
		return true;
	}
	
	/**
	 * 购物车数据刷新
	 */
	public void reloadData(Context ctx){
		dao = new CartDao(ctx);
		cartItemList.clear();
		List<Product> list = dao.findAll();
		cartItemList.addAll(list);
		list.clear();
		list = null;
	}
	
	/**
	 * 总计金额
	 * @return
	 */
	public String calTotalAmount(){
		String retVal = "";
		float count = 0f;
		int total = 0;
	
		for(Product item:cartItemList){
			if(item.isSelected()){
				total += item.getNum();
				count += item.getNum() * Util.conver2Price(item.getPrice());	
			}
		}
		totalNum = total;
		retVal = String.valueOf(count);
		return retVal;
	}
	
	
	/**
	 * 获取选中的商品数量
	 * @return
	 */
	public int getSelectedProductNum(){
		int selectedNum = 0;
		for(Product item :cartItemList){
			if(item.isSelected()){
				selectedNum++;
			}
		}
		return selectedNum;
	}
	
	/**
	 * 获取选中的商品
	 * @return
	 */
	public List<Product> getSelectedProducts(){
		List<Product> tempList = new ArrayList<Product>();
		for(Product item :cartItemList){
			if(item.isSelected()){
				tempList.add(item);
			}
		}
		return tempList;
	}
	
	/**
	 * 获取购物车商品总件数
	 * 依赖 calTotalAmount
	 * @return
	 */
	public int getTotalNum(){
		return totalNum;
	}
	
	/**
	 * 设置购物车中商品选择状态
	 * @param checked
	 */
	public void setCheckedAll(boolean checked){
		Product item = null;
		for(int i = 0; i < cartItemList.size(); i++){
			item = cartItemList.get(i);
			item.setSelected(checked);
		}
	}
	
	/**
	 * 删除已选择的商品
	 */
	public void deleteSelectedProduct(Context ctx){
		dao = new CartDao(ctx);
		for(Product item:cartItemList){
			if(item.isSelected()){
				dao.delete(item);
			}
		}
	}
}