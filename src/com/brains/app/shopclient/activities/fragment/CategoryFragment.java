package com.brains.app.shopclient.activities.fragment;

import java.util.List;

import com.brains.app.shopclient.R;
import com.brains.app.shopclient.activities.LoginActivity;
import com.brains.app.shopclient.bean.Category;

import com.brains.framework.exception.AppException;
import com.brains.framework.task.GenericTask;
import com.brains.framework.task.TaskParams;
import com.brains.framework.task.TaskResult;
import com.brains.framework.util.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 品类画面
 * @author xiect
 *
 */
public class CategoryFragment extends BaseFragment {
	private ListView listView;
	// 获得品类数据Task
	private GenericTask mCategoryTask;
	private List<Category> mCategoryList;
	private static final String TAG = "CategoryFragment";
	private CategoryListAdapter mCategoryListAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		fragmentView = inflater.inflate(R.layout.fragment_main_category, container,false);
		listView = (ListView)fragmentView.findViewById(R.id.category_main_list);
		inflater.inflate(R.layout.fragment_main_category, container,false);
		lazyLoad();
		return fragmentView;
	}
	
	@Override
	public  void lazyLoad(){
		Log.e(TAG," isPrepared :" + isPrepared +"\tisVisible"+isVisible);
		if(!isPrepared && isVisible){
			Log.e(TAG," call doGetCategoryList===lazyLoad");
			doGetCategoryList();
		}else{
			Log.e(TAG," call donothing===lazyLoad");
		}
	}
	
	private void doGetCategoryList(){
		if (mCategoryTask != null
				&& mCategoryTask.getStatus() == GenericTask.Status.RUNNING) {
			return;
		} else {
			mCategoryTask = new GetCategoryListTask();
			mCategoryTask.execute();
		}
	}
	
	/**
	 * 获取Category列表数据Task
	 * 
	 * @author xiect
	 * 
	 */
	private class GetCategoryListTask extends GenericTask {
		String message = null;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//			if(isGetMore && mProgressBarMore != null){
//				mProgressBarMore.setVisibility(View.VISIBLE);	
//			}
		}

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
			try {
				// 获得网络数据
				mCategoryList = app.mApi.getCategoryList();
				return TaskResult.OK;
			} catch (AppException e) {
				message = e.getMessage();
				e.printStackTrace();
			}
			return TaskResult.FAILED;
		}

		@Override
		protected void onPostExecute(TaskResult result) {
			super.onPostExecute(result);
			

			// 初次加载的场合
			if (TaskResult.OK == result && mCategoryList != null
					&& mCategoryList.size() > 0) {
				// 显示画面
				showViewWithData();
			} else {
				if(TaskResult.FAILED == result && message != null && message.length() > 0){
					Log.d(TAG, "layout_no_data");
					app.showErrorWithToast(message);
				}else{
					Log.d(TAG, "layout_no_data");
					showViewWithNoData();
				}
			}
		}
	}
	
	
	private void showViewWithNoData() {
		Log.e(TAG, "showViewWithNoData");
		isPrepared = false;
		listView = (ListView)fragmentView.findViewById(R.id.category_main_list);
		listView.setVisibility(View.GONE);
		LinearLayout layout = (LinearLayout)fragmentView.findViewById(R.id.main_loading_error_tips);
		layout.setVisibility(View.VISIBLE);
	}
	
	private void showViewWithData() {
		Log.e(TAG, "showViewWithData");
		isPrepared = true;
		listView = (ListView)fragmentView.findViewById(R.id.category_main_list);
		listView.setVisibility(View.VISIBLE);	
		mCategoryListAdapter = new CategoryListAdapter(app,mCategoryList);
		listView.setAdapter(mCategoryListAdapter);
		registerOnClickListener(listView);
	}
	
	private void registerOnClickListener(ListView listView) {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				getActivity().startActivity(LoginActivity.makeIntent());
			}
		});
	}
}



class CategoryListAdapter extends BaseAdapter {

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
