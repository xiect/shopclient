package com.brains.app.shopclient.adapter;

import java.util.List;

import com.brains.app.shopclient.R;
import com.brains.app.shopclient.ShoppingApp;
import com.brains.app.shopclient.adapter.BrandListAdapter.ViewHolder;
import com.brains.app.shopclient.bean.Brand;
import com.brains.framework.util.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 品牌适配器
 * （业务同品类）
 * @author xiect
 *
 */
public class BrandListAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private Context context;
	private List<Brand> dataList;

	public BrandListAdapter(Context context, List<Brand> datas) {
		super();
		this.context = context;
		this.dataList = datas;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.category_list_item, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.category_item_name);
			holder.desc = (TextView) convertView.findViewById(R.id.category_item_description);
			holder.imageView = (ImageView) convertView.findViewById(R.id.category_item_img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Brand Brand = dataList.get(position);
		holder.title.setText(Brand.getName());
		holder.desc.setText(Brand.getDesc());
		String src = Brand.getImgSrc();
		if(!StringUtil.isEmpty(src)){
			ShoppingApp app = (ShoppingApp) context.getApplicationContext();
			ImageLoader.getInstance().displayImage(src, holder.imageView,app.getCategoryImgOption());
		}
		return convertView;
	}

	class ViewHolder {
		private ImageView imageView;
		private TextView title;
		private TextView desc;
	}
}