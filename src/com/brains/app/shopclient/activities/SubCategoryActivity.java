package com.brains.app.shopclient.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.AdapterView.OnItemClickListener;

import com.brains.app.shopclient.R;
import com.brains.app.shopclient.adapter.CategoryListAdapter;
import com.brains.app.shopclient.bean.Category;
import com.brains.app.shopclient.common.Util;
import com.brains.framework.activities.base.BaseNormalActivity;
import com.brains.framework.exception.AppException;
import com.brains.framework.task.GenericTask;
import com.brains.framework.task.TaskParams;
import com.brains.framework.task.TaskResult;


/*
 * 二级品类
 * @author xiect
 *
 */
public class SubCategoryActivity extends BaseNormalActivity{
	private final static String ACTION_CATEGORY = "brains.intent.action.ACTION_SUBCATEGORY";

	public static final String TAG = "SubCategoryActivity";
	public static final String EXTRA_CATEGORY_ID = "CATEGORY_ID";
	private ListView mListView;
	
	// 获得品类数据Task
	private GenericTask mCategoryTask;
	private List<Category> mCategoryList = new ArrayList<Category>(1);
	private CategoryListAdapter mCategoryListAdapter;
	private LinearLayout mLlErrorArea;
	private ProgressBar mProgressBar;
	private Button mBtnReload;
	private String mCategoryId;// 主类Id
	
	/**
	 * 取得二级品类画面Intent
	 * @param categoryId 一级品类Id
	 * @return
	 */
	public static Intent makeIntent(String categoryId) {
		Intent intent = new Intent().setAction(ACTION_CATEGORY);
		intent.putExtra(EXTRA_CATEGORY_ID, categoryId);
		return intent;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main_category);
		findView();
		
		Intent intent = getIntent();
		if(intent != null ){
			Bundle bundle = intent.getExtras();
			if(bundle != null){
				mCategoryId = bundle.getString(EXTRA_CATEGORY_ID);
			}
		}
		if(Util.isEmpty(mCategoryId)){
			app.showErrorWithToast(R.string.error_choose_category_please);
			this.finish();
		}else{
			// 访问网络获取二级分类
			doGetCategoryList();
		}
	}
	
	private void findView(){
		mProgressBar = (ProgressBar)findViewById(R.id.top_refresh_progressBar);
		mBtnReload = (Button) findViewById(R.id.loading_error_but);
		mBtnReload.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doGetCategoryList();
			}
		});
		
		mListView = (ListView) findViewById(R.id.category_main_list);
		mCategoryListAdapter = new CategoryListAdapter(app,mCategoryList);
		mListView.setAdapter(mCategoryListAdapter);
		registerOnClickListener(mListView);
		mLlErrorArea = (LinearLayout)findViewById(R.id.main_loading_error_tips);
	}
	
	/**
	 * 获取子分类
	 */
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
			mLlErrorArea.setVisibility(View.GONE);
			mProgressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
			try {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				// 获得网络数据
				List<Category> tempList = app.mApi.getSubCategoryList(mCategoryId);
				mCategoryList.addAll(tempList);
				tempList = null;
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
			// 加载的场合
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
				}
				showViewWithNoData();
			}
		}
	}
	
	private void showViewWithNoData() {
		Log.e(TAG, "showViewWithNoData");
		// 隐藏列表
		mListView.setVisibility(View.GONE);
		// 隐藏loading
		mProgressBar.setVisibility(View.GONE);
		// 显示错误画面
		mLlErrorArea.setVisibility(View.VISIBLE);
	}
	
	private void showViewWithData() {
		Log.e(TAG, "showViewWithData");
		mProgressBar.setVisibility(View.GONE);
		// 隐藏错误画面
		mLlErrorArea.setVisibility(View.GONE);
		mListView.setVisibility(View.VISIBLE);
		// 数据更新后刷新列表
		mCategoryListAdapter.notifyDataSetChanged();
	}
	
	private void registerOnClickListener(ListView listView) {
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				startActivity(LoginActivity.makeIntent());
			}
		});
	}
}
