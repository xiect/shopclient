package com.brains.app.shopclient.db.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.brains.app.shopclient.bean.Category;
import com.brains.app.shopclient.db.entity.lib.LogicalEntity;
import com.brains.framework.exception.AppException;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;


/**
 * 商品
 * @author id:language_and_engineering
 *
 */
public class Product extends LogicalEntity<Product> implements Parcelable{ 
   
    private static final long serialVersionUID = 1L;

    public Product(){
    	
    }
    
    public Product(Parcel p){
    	itemId = p.readString();
    	name = p.readString();
    	desc = p.readString();
    	imgSrc = p.readString();
    	price = p.readString();
    	num = p.readInt();
    }
    
    @Override
    public String tableName(){return "t_card";}

    @Override
    public final String[] columns(){
        return new String[]{ "id","itemId","name","desc","imgSrc", "price","num"};
    }


    private String itemId;
	private String name;
	private String desc;
	private String price;
	private String imgSrc;
	private int num;

	private boolean isSelected = true;
	

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

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

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getImgSrc() {
		return imgSrc;
	}

	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}
    // ----- LP変換(Logical <-> Physical) -----


    /**
     * DBの格納値から論理エンティティを構成
     */
    @Override
    public Product logicalFromPhysical(Cursor c) {
        setId(c.getLong(0));
        setItemId(c.getString(1));
        setName( c.getString(2));
        setDesc(c.getString(3));
        setImgSrc(c.getString(4));
        setPrice(c.getString(5));
        setNum(c.getInt(6));
        return this;
    }


    /**
     * 自身をDBに新規登録可能なデータ型に変換して返す
     */
    @Override
    protected ContentValues toPhysicalEntity(ContentValues values) {
    	// entityをContentValueに変換
    	if( getId() != null){
    		values.put("id", getId());
    	}
    	values.put("itemId", getName());
    	values.put("name", getName());
    	values.put("desc", getDesc());
    	values.put("imgSrc",getImgSrc());
    	values.put("price", getPrice());
    	values.put("num", getNum());
    	return values;
    }

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(itemId);
		dest.writeString(name);
		dest.writeString(name);
		dest.writeString(imgSrc);
		dest.writeString(price);
		dest.writeInt(num);
	}
	
	/**
	 * Intent 传值
	 */
	public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>(){
		public Product createFromParcel(Parcel p) {
			return new Product(p);
		}

		@Override
		public Product[] newArray(int size) {
			return new Product[size];
		}
	};
	
	/**
	 * 解析JSONArray
	 * @throws AppException 
	 */
	public static final List<Product> constructListFromJson(JSONObject json) throws AppException{
		List<Product> list = new ArrayList<Product>();
		Product item = null;
		JSONObject jsonObj = null;
		JSONArray array;
		try {
			array = json.getJSONArray("records");
			if(array != null && array.length() > 0){
				for(int i = 0; i < array.length(); i++){
					jsonObj = array.getJSONObject(i);
					item = new Product();
					item.setItemId(jsonObj.getString("id"));
					item.setName(jsonObj.getString("name"));
					if(jsonObj.has("price")){
						item.setPrice(jsonObj.getString("price"));
					}
					item.setDesc(jsonObj.getString("desc"));
					item.setImgSrc(jsonObj.getString("imgSrc"));
					item.isSelected = true;
					list.add(item);
				}
			}
		} catch (Exception e) {
			throw new AppException(e);
		}
		
		return list;
	}
}
