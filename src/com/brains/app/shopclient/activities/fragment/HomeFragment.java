package com.brains.app.shopclient.activities.fragment;

import com.brains.app.shopclient.R;
import com.brains.app.shopclient.activities.SearchActivity;
import com.brains.app.shopclient.common.Util;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class HomeFragment extends BaseFragment {
	private static final int LOOP_INTERVAL = 3000;
	public static final int  SCROLL_WHAT = 0;
	
	private PullToRefreshScrollView mPullRefreshScrollView;
	private ScrollView mScrollView;
	private RelativeLayout rlSearchHeader;
	
	private ViewPager viewPagerBanner;
	
	private ImageView[] imageViews;
	private ImageView[] tips;
	
	private float startX,startY;
	
	private Handler bannerHandler;
	
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
    
	OnClickListener bannerClickListen = new OnClickListener(){
		@Override
		public void onClick(View v) {
			Log.e("BANNER", "onClick=====view=>=" + v.getTag());
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
//		rlSearchHeader.setOnTouchListener(new OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				app.showErrorWithToast("fffffffffffff2222");
//				return true;
//			}
//		});
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
				new GetDataTask().execute();
			}
		});
		
		
		// banner ad
		viewPagerBanner = (ViewPager)mPullRefreshScrollView.findViewById(R.id.viewPager_banner);
		ctx = getActivity();
		ImageView view1 = new ImageView(ctx);
		view1.setBackgroundResource(R.drawable.test_a_red);
		view1.setTag("1");
		view1.setOnClickListener(bannerClickListen);
		ImageView view2 = new ImageView(ctx);
		view2.setBackgroundResource(R.drawable.test_b_red);
		view2.setTag("2");
		view2.setOnClickListener(bannerClickListen);
		ImageView view3 = new ImageView(ctx);
		view3.setBackgroundResource(R.drawable.test_c_red);
		view3.setTag("3");
		view3.setOnClickListener(bannerClickListen);
		ImageView view4 = new ImageView(ctx);
		view4.setBackgroundResource(R.drawable.test_d_red);
		view4.setTag("4");
		view4.setOnClickListener(bannerClickListen);
		
		imageViews = new ImageView[]{
				view1,view2,view3,view4
		};
		ViewGroup group = (ViewGroup)fragmentView.findViewById(R.id.viewGroup_banner); 
		tips = new ImageView[imageViews.length];
		for(int i=0; i<tips.length; i++){  
			ImageView imageView = new ImageView(ctx);  
			imageView.setLayoutParams(new LayoutParams(10,10));  
			tips[i] = imageView;  
			if(i == 0){  
				tips[i].setBackgroundResource(R.drawable.im_input_phiz_page_indicator_sel);  
			}else{  
				tips[i].setBackgroundResource(R.drawable.im_input_phiz_page_indicator_nor);  
			}  

			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,    
					LayoutParams.WRAP_CONTENT));  
			layoutParams.leftMargin = 5;  
			layoutParams.rightMargin = 5;  
			group.addView(imageView, layoutParams);  
		}
	       
		
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
				View tempView = imageViews[position % imageViews.length ];
				container.removeView(tempView);
				container.addView(tempView);
				return tempView;
			}
			
			
		});
		
		
		
//		viewPagerBanner.setOnTouchListener(new OnTouchListener() {
//			
//		
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				
//				switch (event.getAction()) {
//				case MotionEvent.ACTION_DOWN:
//					Log.e("BANNER", "touch=======ACTION_DOWN");
//					startX = event.getX();
//					startY = event.getY();
//					stopAutoScroll();
//					break;
//				case MotionEvent.ACTION_MOVE:
//					Log.e("BANNER", "ACTION_MOVE");
//					break;
//				case MotionEvent.ACTION_UP:
//					Log.e("BANNER", "ACTION_UP");
//					startX = event.getX();
//					startY = event.getY();
//					if (Math.abs(event.getX() - startX) < 1.5 && Math.abs(event.getY() - startY) < 1.5){
//						Log.e("BANNER", "onClick====customer===");
//					}else{
//						startAutoScroll();
//					}
//					break;
//				}
//				return true;
//			}
//		});
//		
//		viewPagerBanner.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Log.e("BANNER", "onClick=======");
//			}
//		});
		
		bannerHandler = new BannerHandler();
		return fragmentView;
	}
	
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
			mPullRefreshScrollView.onRefreshComplete();
			// 滚动
			bannerHandler.sendEmptyMessage(SCROLL_WHAT);
			super.onPostExecute(result);
		}
	}

	
	
	
	@Override
	public void onResume() {
//		viewPagerBanner.startAutoScroll();
		super.onResume();
	}

	
	@Override
	public void onPause() {
//		viewPagerBanner.stopAutoScroll();
		super.onPause();
	}
	
	
	private void stopAutoScroll(){
		bannerHandler.removeMessages(SCROLL_WHAT);
	}
	
	private void startAutoScroll(){
		sendBannerMessage();
	}


	@Override
	protected void lazyLoad() {
		// TODO Auto-generated method stub
		
	}
}
