package com.brains.app.shopclient.activities.fragment;

import java.util.ArrayList;
import java.util.List;

import com.brains.app.shopclient.R;
import com.brains.app.shopclient.activities.SearchActivity;
import com.brains.app.shopclient.activities.ShopTypeSelectActivity;
import com.brains.app.shopclient.adapter.BrandListAdapter;
import com.brains.app.shopclient.bean.Brand;
import com.brains.app.shopclient.common.Util;

import com.brains.framework.common.Const;
import com.brains.framework.exception.AppException;
import com.brains.framework.task.GenericTask;
import com.brains.framework.task.TaskParams;
import com.brains.framework.task.TaskResult;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

/**
 * 品牌
 * @author xiect
 *
 */
public class BrandFragment extends BaseFragment {
	private ListView listView;
	// 获得品牌数据Task
	private GenericTask mBrandTask;
	private List<Brand> mBrandList = new ArrayList<Brand>(1);
	private static final String TAG = "BrandFragment";
	private BrandListAdapter mBrandListAdapter;
	private LinearLayout mLlErrorArea;
	private ProgressBar mProgressBar;
	private Button mBtnReload;
	private RelativeLayout rlSearchHeader;
	private LinearLayout btnHeaderChoose; // 顶部按钮
	private static final int REQUEST_CODE_4_TYPE = 1;
	
	private String mZizhi;
	private String mXinyu;
	private String mCategory;
	private String mKeyword;
	private TextView tvNoData;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		mZizhi = Const.TYPE_ALL;
		mXinyu = Const.TYPE_ALL;
		mCategory = Const.TYPE_ALL;
		mKeyword = "";
		
		fragmentView = inflater.inflate(R.layout.fragment_main_brand, container,false);
		findView();
		lazyLoad();
		return fragmentView;
	}
	
	private void findView(){
		tvNoData = (TextView) fragmentView.findViewById(R.id.tv_hint_no_data);
		
		btnHeaderChoose = (LinearLayout)fragmentView.findViewById(R.id.common_header_right_btn_area);
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
				doGetBrandList();
			}
		});
		btnHeaderChoose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(ShopTypeSelectActivity.makeIntent(mZizhi,mXinyu,mCategory,mKeyword), REQUEST_CODE_4_TYPE);
			}
		});
		
		listView = (ListView)fragmentView.findViewById(R.id.brand_main_list);
		mBrandListAdapter = new BrandListAdapter(app,mBrandList);
		listView.setAdapter(mBrandListAdapter);
		registerOnClickListener(listView);
		mLlErrorArea = (LinearLayout)fragmentView.findViewById(R.id.main_loading_error_tips);
	}
	
	@Override
	public  void lazyLoad(){
		Util.sysLog(TAG," isPrepared :" + isPrepared +"\tisVisible"+isVisible);
		if(!isPrepared && isVisible && isCreatedView){
			Util.sysLog(TAG," call doGetBrandList===lazyLoad");
			doGetBrandList();
		}else{
			Util.sysLog(TAG," call donothing===lazyLoad");
		}
	}
	
	private void doGetBrandList(){
		if (mBrandTask != null
				&& mBrandTask.getStatus() == GenericTask.Status.RUNNING) {
			return;
		} else {
			mBrandTask = new GetBrandListTask();
			mBrandTask.execute();
		}
	}
	
	/**
	 * 获取Brand列表数据Task
	 * 
	 * @author xiect
	 * 
	 */
	private class GetBrandListTask extends GenericTask {
		String message = null;
		List<Brand> tempList;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			tvNoData.setVisibility(View.GONE);
			mLlErrorArea.setVisibility(View.GONE);
			mProgressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
			try {
				// 获得网络数据
				tempList = app.mApi.getBrandList(mZizhi,mXinyu,mCategory,mKeyword);
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
				mBrandList.clear();
				mBrandList.addAll(tempList);
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
		Util.sysLog(TAG, "showViewWithNoData");
		isPrepared = false;
		// 隐藏列表
		listView.setVisibility(View.GONE);
		// 显示错误画面
		mLlErrorArea.setVisibility(View.GONE);
		// 显示无数据画面
		tvNoData.setVisibility(View.VISIBLE);
	}
	
	private void showViewWithData() {
		Util.sysLog(TAG, "showViewWithData");
		isPrepared = true;
		tvNoData.setVisibility(View.GONE);
		// 隐藏错误画面
		mLlErrorArea.setVisibility(View.GONE);
		listView.setVisibility(View.VISIBLE);
		// 数据更新后刷新列表
		mBrandListAdapter.notifyDataSetChanged();
	}
	
	private void registerOnClickListener(ListView listView) {
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(arg2 < mBrandList.size()){
					String brandId = mBrandList.get(arg2).getId();
					getActivity().startActivity(SearchActivity.makeIntent4Brand(brandId));
					getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out); 
				}
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Util.sysLog(TAG, "onActivityResult===");
		if(requestCode == REQUEST_CODE_4_TYPE && resultCode == Activity.RESULT_OK){
			if(data != null){
				String value = data.getStringExtra(ShopTypeSelectActivity.EXTRA_ZIZHI); 
				if(!Util.isEmpty(value)){
					mZizhi = value;
				}
				
				value = data.getStringExtra(ShopTypeSelectActivity.EXTRA_XINYU); 
				if(!Util.isEmpty(value)){
					mXinyu = value;
				}
				
				value = data.getStringExtra(ShopTypeSelectActivity.EXTRA_CATE); 
				if(!Util.isEmpty(value)){
					mCategory = value;
				}
				
				value = data.getStringExtra(ShopTypeSelectActivity.EXTRA_KEYWORD); 
				mKeyword = value;
				Util.sysLog(TAG, "mZizhi==="+mZizhi +"\tmXinyu:" + mXinyu + "\tmCategory:" + mCategory + "\tmKeyword:" + mKeyword);
				// reload data
				doGetBrandList();
			}
		}
	}
}
