package com.brains.app.shopclient.activities.fragment;

import java.util.ArrayList;
import java.util.List;

import com.brains.app.shopclient.R;
import com.brains.app.shopclient.activities.SearchActivity;
import com.brains.app.shopclient.activities.SubCategoryActivity;
import com.brains.app.shopclient.adapter.CategoryListAdapter;
import com.brains.app.shopclient.bean.Category;

import com.brains.framework.exception.AppException;
import com.brains.framework.task.GenericTask;
import com.brains.framework.task.TaskParams;
import com.brains.framework.task.TaskResult;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
	private List<Category> mCategoryList = new ArrayList<Category>(1);
	private static final String TAG = "CategoryFragment";
	private CategoryListAdapter mCategoryListAdapter;
	private LinearLayout mLlErrorArea;
	private ProgressBar mProgressBar;
	private Button mBtnReload;
	private RelativeLayout rlSearchHeader;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		fragmentView = inflater.inflate(R.layout.fragment_main_category, container,false);
		findView();
		lazyLoad();
		return fragmentView;
	}
	
	private void findView(){
		mProgressBar = (ProgressBar)fragmentView.findViewById(R.id.top_refresh_progressBar);
		rlSearchHeader = (RelativeLayout)fragmentView.findViewById(R.id.home_title_search);
		rlSearchHeader.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ctx.startActivity(SearchActivity.makeIntent());
			}
		});
		mBtnReload = (Button) fragmentView.findViewById(R.id.loading_error_but);
		mBtnReload.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doGetCategoryList();
			}
		});
		
		listView = (ListView)fragmentView.findViewById(R.id.category_main_list);
		mCategoryListAdapter = new CategoryListAdapter(app,mCategoryList);
		listView.setAdapter(mCategoryListAdapter);
		registerOnClickListener(listView);
		mLlErrorArea = (LinearLayout)fragmentView.findViewById(R.id.main_loading_error_tips);
	}
	
	@Override
	public  void lazyLoad(){
		Log.e(TAG," isPrepared :" + isPrepared +"\tisVisible"+isVisible);
		if(!isPrepared && isVisible && isCreatedView){
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
		List<Category> tempList;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mLlErrorArea.setVisibility(View.GONE);
			mProgressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
			try {
				// 获得网络数据
				tempList = app.mApi.getCategoryList();
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
			
			// 隐藏loading
			mProgressBar.setVisibility(View.GONE);
			// 加载的场合
			if (TaskResult.OK == result && tempList != null
					&& tempList.size() > 0) {
				mCategoryList.addAll(tempList);
				tempList = null;
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
		// 隐藏列表
		listView.setVisibility(View.GONE);
		// 显示错误画面
		mLlErrorArea.setVisibility(View.VISIBLE);
	}
	
	private void showViewWithData() {
		Log.e(TAG, "showViewWithData");
		isPrepared = true;
		
		// 隐藏错误画面
		mLlErrorArea.setVisibility(View.GONE);
		listView.setVisibility(View.VISIBLE);
		// 数据更新后刷新列表
		mCategoryListAdapter.notifyDataSetChanged();
	}
	
	private void registerOnClickListener(ListView listView) {
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(arg2 < mCategoryList.size()){
					String categoryId = mCategoryList.get(arg2).getId();
					getActivity().startActivity(SubCategoryActivity.makeIntent(categoryId));
					getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out); 
				}
			}
		});
	}
}