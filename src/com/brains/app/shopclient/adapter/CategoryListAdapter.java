package com.brains.app.shopclient.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.brains.app.shopclient.R;
import com.brains.app.shopclient.bean.Category;
import com.brains.framework.util.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CategoryListAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private Context context;
	private List<Category> dataList;

	public CategoryListAdapter(Context context, List<Category> datas) {
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
		Category category = dataList.get(position);
		holder.title.setText(category.getName());
		holder.desc.setText(category.getDesc());
		String src = category.getImgSrc();
		if(!StringUtil.isEmpty(src)){
			ImageLoader.getInstance().displayImage(src, holder.imageView);
		}
		return convertView;
	}

	class ViewHolder {
		private ImageView imageView;
		private TextView title;
		private TextView desc;
	}
}
