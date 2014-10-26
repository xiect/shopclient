package com.brains.app.shopclient.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.brains.app.shopclient.R;
import com.brains.framework.activities.base.BaseNormalActivity;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;


/**
 * TAB 首页
 * @author xiect
 *
 */
public class HomeActivity extends BaseNormalActivity implements OnPageChangeListener {
	private final static String ACTION_HOME = "brains.intent.action.ACTION_HOME";

	public static final String TAG = "HomeActivity";
	
	PullToRefreshScrollView mPullRefreshScrollView;
	ScrollView mScrollView;
	
	ViewPager viewPagerBanner;
	
	ImageView[] imageViews;
	ImageView[] tips;
	
	/**
	 * 取得主页画面Intent
	 * 
	 * @return
	 */
	public static Intent makeIntent() {
		return new Intent().setAction(ACTION_HOME);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_home);
		
		mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
		mPullRefreshScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				new GetDataTask().execute();
			}
		});

		mScrollView = mPullRefreshScrollView.getRefreshableView();
		
		viewPagerBanner = (ViewPager)mPullRefreshScrollView.findViewById(R.id.viewPager_banner);
//		jazzyViewPager.setTransitionEffect(TransitionEffect.FlipHorizontal);
		ImageView view1 = new ImageView(this);
		view1.setBackgroundResource(R.drawable.test_a_red);
		ImageView view2 = new ImageView(this);
		view2.setBackgroundResource(R.drawable.test_b_red);
		ImageView view3 = new ImageView(this);
		view3.setBackgroundResource(R.drawable.test_c_red);
		ImageView view4 = new ImageView(this);
		view4.setBackgroundResource(R.drawable.test_d_red);
		
		imageViews = new ImageView[]{
				view1,view2,view3,view4
		};
		ViewGroup group = (ViewGroup)findViewById(R.id.viewGroup_banner); 
		tips = new ImageView[imageViews.length];
		for(int i=0; i<tips.length; i++){  
			ImageView imageView = new ImageView(this);  
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
				return imageViews.length;
			}
			
			@Override
			public void  destroyItem(ViewGroup container, int pos, Object object){
				container.removeView(imageViews[pos]);
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				container.addView(imageViews[position]);
				return imageViews[position];
			}
			
			
		});
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
        for(int i=0; i<tips.length; i++){  
            if(i == selectItems){  
                tips[i].setBackgroundResource(R.drawable.im_input_phiz_page_indicator_sel);  
            }else{  
                tips[i].setBackgroundResource(R.drawable.im_input_phiz_page_indicator_nor);  
            }  
        }  
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
			// Do some stuff here

			// Call onRefreshComplete when the list has been refreshed.
			mPullRefreshScrollView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}
}
