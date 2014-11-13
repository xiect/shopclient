package com.brains.app.shopclient.activities.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.brains.app.shopclient.R;
import com.brains.app.shopclient.activities.ProductDetailActivity;
import com.brains.app.shopclient.activities.SearchActivity;
import com.brains.app.shopclient.activities.SubCategoryActivity;
import com.brains.app.shopclient.bean.Category;
import com.brains.app.shopclient.bean.Home;
import com.brains.app.shopclient.common.Util;
import com.brains.app.shopclient.db.dao.PrefDAO;
import com.brains.app.shopclient.db.entity.Product;
import com.brains.framework.exception.AppException;
import com.brains.framework.task.GenericTask;
import com.brains.framework.task.TaskParams;
import com.brains.framework.task.TaskResult;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.nostra13.universalimageloader.core.ImageLoader;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * 首页画面
 * @author xiect
 *
 */
public class HomeFragment extends BaseFragment implements OnPageChangeListener,OnClickListener{
	private static final String TAG = "HomeFragment";
	private static final int LOOP_INTERVAL = 3000;
	public static final int  SCROLL_WHAT = 0;
	
	private PullToRefreshScrollView mPullRefreshScrollView;
	private RelativeLayout rlSearchHeader;
	
	private ViewPager viewPagerBanner;
	private ImageView[] tips;
	private List<ImageView> mImageViewList;
	
	private Handler bannerHandler;
	private LinearLayout mErrorView;
	private GetDataTask mTask;
	private RelativeLayout mProgressBar;
	private boolean isReadyShow;
	
	private ViewGroup llRecommendGroup;
	private List<Product> mSusumeList;
	private RelativeLayout[] susumeViews;
	private ImageView[] susumeImageViews;
	private TextView[] susumeNameViews;
	private TextView[] susumePriceViews;
	
	private class BannerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
        	super.handleMessage(msg);
        	switch (msg.what) {
        	case SCROLL_WHAT:
        		viewPagerBanner.setCurrentItem(viewPagerBanner.getCurrentItem() + 1);
        		sendBannerMessage();
        	default:
        		break;
        	}
        }
    }
    
	OnClickListener susumeClickListen = new OnClickListener(){
		@Override
		public void onClick(View v) {
			Util.sysLog(TAG, "susume view:" + v.getClass().getName());
			Util.sysLog(TAG, "susume tag:" + v.getTag());
			String tag = (String)v.getTag();
			if(!Util.isEmpty(tag)){
				int index = Integer.parseInt(tag);
				Product item = mSusumeList.get(index);
				// 商品详细迁移
				startActivity(ProductDetailActivity.makeIntent(item));
//				getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);	
			}
		}
	};
	
	OnClickListener bannerClickListen = new OnClickListener(){
		@Override
		public void onClick(View v) {
			Util.sysLog(TAG, "banner view:" + v.getClass().getName());
			Util.sysLog(TAG, "banner targeturl:" + v.getTag());
			String url = (String)v.getTag();
			if(!Util.isEmpty(url)){
				Uri uri = Uri.parse(url);  
				Intent it = new Intent(Intent.ACTION_VIEW, uri);  
				getActivity().startActivity(it);
			}
			// TODO 商品详细迁移
//			startActivity(ProductDetailActivity.makeIntent());
//			getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
		}
	};
	
	
    private void sendBannerMessage(){
    	bannerHandler.removeMessages(SCROLL_WHAT);
    	bannerHandler.sendEmptyMessageDelayed(SCROLL_WHAT,LOOP_INTERVAL);
    }
    
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		fragmentView = inflater.inflate(R.layout.fragment_main_home, container,false);
		rlSearchHeader = (RelativeLayout) fragmentView.findViewById(R.id.home_title_search);
		rlSearchHeader.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ctx.startActivity(SearchActivity.makeIntent());
			}
		});
		
		mPullRefreshScrollView = (PullToRefreshScrollView) fragmentView.findViewById(R.id.pull_refresh_scrollview);
		mPullRefreshScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				// 获取最新首页信息
				doGetHomeDataList();
			}
		});
		
		// 加载中视图
		mProgressBar = (RelativeLayout) fragmentView.findViewById(R.id.rl_oading_area);
		// 网络错误画面隐藏
		mErrorView = (LinearLayout)fragmentView.findViewById(R.id.home_load_failed);
		mErrorView.setVisibility(View.GONE);
		
		// banner ad
		viewPagerBanner = (ViewPager)mPullRefreshScrollView.findViewById(R.id.viewPager_banner);
		// recommend container
		llRecommendGroup = (ViewGroup) fragmentView.findViewById(R.id.ll_recommend_group);
		
		// 推荐商品
		susumeViews = new RelativeLayout[6];
		susumeImageViews = new ImageView[6];
		susumeNameViews = new TextView[6];
		susumePriceViews = new TextView[6];
		
		susumeViews[0] = (RelativeLayout)fragmentView.findViewById(R.id.home_row1_left);
		susumeViews[1] = (RelativeLayout)fragmentView.findViewById(R.id.home_row1_right);
		susumeViews[2] = (RelativeLayout)fragmentView.findViewById(R.id.home_row2_left);
		susumeViews[3] = (RelativeLayout)fragmentView.findViewById(R.id.home_row2_right);
		susumeViews[4] = (RelativeLayout)fragmentView.findViewById(R.id.home_row3_left);
		susumeViews[5] = (RelativeLayout)fragmentView.findViewById(R.id.home_row3_right);
		
		susumeImageViews[0] = (ImageView)fragmentView.findViewById(R.id.home_row1_left_image);
		susumeImageViews[1] = (ImageView)fragmentView.findViewById(R.id.home_row1_right_image);
		susumeImageViews[2] = (ImageView)fragmentView.findViewById(R.id.home_row2_left_image);
		susumeImageViews[3] = (ImageView)fragmentView.findViewById(R.id.home_row2_right_image);
		susumeImageViews[4] = (ImageView)fragmentView.findViewById(R.id.home_row3_left_image);
		susumeImageViews[5] = (ImageView)fragmentView.findViewById(R.id.home_row3_right_image);
		
		susumeNameViews[0] = (TextView)fragmentView.findViewById(R.id.home_row1_left_name);
		susumeNameViews[1] = (TextView)fragmentView.findViewById(R.id.home_row1_right_name);
		susumeNameViews[2] = (TextView)fragmentView.findViewById(R.id.home_row2_left_name);
		susumeNameViews[3] = (TextView)fragmentView.findViewById(R.id.home_row2_right_name);
		susumeNameViews[4] = (TextView)fragmentView.findViewById(R.id.home_row3_left_name);
		susumeNameViews[5] = (TextView)fragmentView.findViewById(R.id.home_row3_right_name);
		
		susumePriceViews[0] = (TextView)fragmentView.findViewById(R.id.home_row1_left_price);
		susumePriceViews[1] = (TextView)fragmentView.findViewById(R.id.home_row1_right_price);
		susumePriceViews[2] = (TextView)fragmentView.findViewById(R.id.home_row2_left_price);
		susumePriceViews[3] = (TextView)fragmentView.findViewById(R.id.home_row2_right_price);
		susumePriceViews[4] = (TextView)fragmentView.findViewById(R.id.home_row3_left_price);
		susumePriceViews[5] = (TextView)fragmentView.findViewById(R.id.home_row3_right_price);
		
		// 加载缓存数据
		loadCache();
		
		// 加载首页数据
		lazyLoad();
		
		viewPagerBanner.setOnPageChangeListener(this);  
		bannerHandler = new BannerHandler();
		return fragmentView;
	}

	private class GetDataTask extends GenericTask {
		String message = null;
		Home tempHomeData;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Util.sysLog(TAG, "start get home data from net...");
			mProgressBar.setVisibility(View.VISIBLE);
			isReadyShow = false;
		}

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
			try {
				// 获得网络数据
				JSONObject json = app.mApi.getHomeList();
				tempHomeData = Home.constructListFromJson(json);
				PrefDAO dao = new PrefDAO(getActivity());
				// 更新缓存
				dao.saveHomeData(json.toString());
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
			mPullRefreshScrollView.onRefreshComplete();
			isReadyShow = true;
			// 隐藏loading
			mProgressBar.setVisibility(View.GONE);
			if (TaskResult.OK == result && tempHomeData != null) {
				if(tempHomeData.getCategoryList() != null 
						&& tempHomeData.getItemList() != null
						&& tempHomeData.getCategoryList().size() > 0 
						&& tempHomeData.getItemList().size() > 0){
					// 显示画面
					showViewWithData(tempHomeData.getItemList(),tempHomeData.getCategoryList(),tempHomeData.getSusumeList());					
				}
			} else {
				if(TaskResult.FAILED == result && message != null && message.length() > 0){
					Log.d(TAG, "layout_no_data");
					app.showErrorWithToast(message);
				}else{
					Log.d(TAG, "layout_no_data");
//					showViewWithNoData();
				}
				
			}
		}
	}

	
	
	
	@Override
	public void onResume() {
		super.onResume();
		startAutoScroll();
	}

	
	@Override
	public void onPause() {
		super.onPause();
		stopAutoScroll();
	}
	
	
	private void stopAutoScroll(){
		bannerHandler.removeMessages(SCROLL_WHAT);
	}
	
	private void startAutoScroll(){
		sendBannerMessage();
	}


	@Override
	protected void lazyLoad() {
		Util.sysLog(TAG,"Home: lazyLoad===isCreatedView:" + isCreatedView +"\tisVisible:" + isVisible);
		if(isCreatedView && isVisible && !isReadyShow){
			Util.sysLog(TAG, "do lazyLoad");
			doGetHomeDataList();
		}else{
			Util.sysLog(TAG, "do nothing");
		}
	}
	
	/**
	 * 加载缓存
	 */
	private void loadCache() {
		Util.sysLog(TAG,"Home: loadCache===");
		String jsonStr = app.mPrefDAO.getHomeData();
		if(Util.isEmpty(jsonStr)){
			// 缓存不存在的场合
			return;
		}
		// 显示缓存
		JSONObject json;
		Home tempHomeData = null;
		try {
			json = new JSONObject(jsonStr);
			tempHomeData = Home.constructListFromJson(json);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		List<Category> tempCategoryList = tempHomeData.getCategoryList();
		List<Product> tempItemList = tempHomeData.getItemList();
		List<Product> tempSusumeList = tempHomeData.getSusumeList();
		if(tempCategoryList == null 
				|| tempCategoryList.size() < 1
				|| tempItemList == null
				|| tempItemList.size() < 1
				|| tempSusumeList == null
				|| tempSusumeList.size() < 1){
			// 缓存不正停止加载
			return;
		}
		
		// 显示画面
		showViewWithData(tempItemList,tempCategoryList,tempSusumeList);
	}
	
	/**
	 * 用数据填充视图
	 * banner & category
	 */
	private void showViewWithData(List<Product> bannerList,List<Category> recommendList,List<Product> susumeList){
		// 前提list 是有数据的
		int imageSize = bannerList.size();
		mImageViewList = new ArrayList<ImageView>(imageSize);
		ImageView tempImgView = null;
		Context context = getActivity();
		String src = "";
		Product item = null;
		// 实例化 ImageView
		for(int i = 0; i < imageSize ; i++){
			item = bannerList.get(i);
			tempImgView = new ImageView(context);
			tempImgView.setTag(item.getUrl());
			tempImgView.setBackgroundResource(R.drawable.product_icon_default);
//			tempImgView.setTag(String.valueOf(i));
			tempImgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			tempImgView.setOnClickListener(bannerClickListen);
			mImageViewList.add(tempImgView);
			src = item.getImgSrc();
			if(!Util.isEmpty(src)){
				ImageLoader.getInstance().displayImage(src, tempImgView,app.getCategoryImgOption());	
			}
		}
		
		// 根据banner 个数生成tip图标
		ViewGroup group = (ViewGroup)fragmentView.findViewById(R.id.viewGroup_banner); 
		group.removeAllViews();
		tips = new ImageView[mImageViewList.size()];
		for(int i=0; i<tips.length; i++){  
			ImageView imageView = new ImageView(context);  
			imageView.setLayoutParams(new LayoutParams(10,10));  
			tips[i] = imageView;  
			if(i == 0){  
				tips[i].setBackgroundResource(R.drawable.im_input_phiz_page_indicator_sel);  
			}else{  
				tips[i].setBackgroundResource(R.drawable.im_input_phiz_page_indicator_nor);  
			}  

			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));  
			layoutParams.leftMargin = 5;  
			layoutParams.rightMargin = 5;  
			group.addView(imageView, layoutParams);  
		}
		
		// 设置banner滚动的 adapter
		viewPagerBanner.setAdapter(new PagerAdapter(){
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return Integer.MAX_VALUE;
			}

			@Override
			public void  destroyItem(ViewGroup container, int pos, Object object){
				// do nothing
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				View tempView = mImageViewList.get(position % mImageViewList.size());
				container.removeView(tempView);
				container.addView(tempView);
				return tempView;
			}
		});
		
		
		// show Category
		Util.sysLog(TAG, "recommendList size:" + recommendList.size());
		showRecommendWithData(recommendList);
		
		// showSusume
		Util.sysLog(TAG, "susumeList size:" + susumeList.size());
		showSusumeWithData(susumeList);
		
		isReadyShow = true;
	}
	
	private void showSusumeWithData(List<Product> susumeList){
		this.mSusumeList = susumeList;
		int len = susumeList.size();
		Product item = null;
		for(int index = 0;index < len; index++){
			item = susumeList.get(index);
			susumeNameViews[index].setText(item.getName());
			susumePriceViews[index].setText(Util.formatRmb(item.getPrice()));
			String src = item.getImgSrc();
			if(!Util.isEmpty(src)){
				ImageLoader.getInstance().displayImage(src,susumeImageViews[index],app.getCategoryImgOption());
			}
			susumeViews[index].setOnClickListener(susumeClickListen);
		}
	}
	
	private void showRecommendWithData(List<Category> recommendsList){
		llRecommendGroup.removeAllViews();
		int len = recommendsList.size();
		View view = null;
		TextView tvName = null;
		TextView tvDesc = null;
		ImageView imageView = null; 
		LinearLayout.LayoutParams layoutParams;
		Category cat = null;
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for(int i = 0; i < len; i++){
			cat = recommendsList.get(i);
			view = inflater.inflate(R.layout.category_list_item, null);
			view.setTag(cat.getId());
			tvName = (TextView)view.findViewById(R.id.category_item_name);
			tvName.setText(cat.getName());
			
			tvDesc = (TextView)view.findViewById(R.id.category_item_description);
			tvDesc.setText(cat.getDesc());
			
			if(!Util.isEmpty(cat.getImgSrc())){
				imageView = (ImageView)view.findViewById(R.id.category_item_img);
				ImageLoader.getInstance().displayImage(cat.getImgSrc(), imageView,app.getCategoryImgOption());
			}
			
			layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));  
			layoutParams.leftMargin = 5;  
			layoutParams.rightMargin = 5;  
			llRecommendGroup.addView(view, layoutParams);  
			view.setOnClickListener(this);
		}
	}
	
	/**
	 * 获取网络数据
	 */
	private void doGetHomeDataList(){
		if (mTask != null
				&& mTask.getStatus() == GenericTask.Status.RUNNING) {
			return;
		} else {
			mTask = new GetDataTask();
			mTask.execute();
		}
	}


	@Override
	public void onPageScrollStateChanged(int arg0) {
	}


	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}


	@Override
	public void onPageSelected(int arg0) {
		setImageBackground(arg0);
	}
	
    /** 
     * 设置选中的tip的背景 
     * @param selectItems 
     */  
    private void setImageBackground(int selectItems){  
    	Util.sysLog(TAG, "selectItems --->" + selectItems);
    	if(tips != null && tips.length > 0 && mImageViewList != null){
    		int index = selectItems % mImageViewList.size();
    		for(int i=0; i<tips.length; i++){  
                if(i == index){  
                    tips[i].setBackgroundResource(R.drawable.im_input_phiz_page_indicator_sel);  
                }else{  
                    tips[i].setBackgroundResource(R.drawable.im_input_phiz_page_indicator_nor);  
                }  
            }	
    	} 
    }


	@Override
	public void onClick(View v) {
		// category click event
		String categoryId = (String)v.getTag();
		Util.sysLog(TAG, "categoryId ===>" + categoryId);
		getActivity().startActivity(SubCategoryActivity.makeIntent(categoryId));
	}
}
