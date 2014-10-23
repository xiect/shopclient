package com.brains.app.shopclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.brains.app.shopclient.R;
import com.brains.framework.activities.base.BaseNormalActivity;

/**
 * 引导画面
 * @author xiect
 *
 */
public class GuideActivity extends BaseNormalActivity implements OnPageChangeListener,OnClickListener {
	private final static String ACTION_GUIDE = "brains.intent.action.ACTION_GUIDE";
	
//	private ViewGroup viewGroup;
	private ViewPager viewPager;
	
	private int[] imageIds;
	private View[] views;
	private ImageView[] tips;
	/**
	 * 取得引导画面Intent
	 * 
	 * @return
	 */
	public static Intent makeIntent() {
		return new Intent().setAction(ACTION_GUIDE);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.layout_guide);
		
		LayoutInflater inflater = LayoutInflater.from(this);
		
		View lastView = inflater.inflate(R.layout.layout_guide_last, null);
    	// 最后一张引导图的场合，获取开启按钮
    	Button button = (Button)lastView.findViewById(R.id.startup_button_go);
    	button.setOnClickListener(this);
    	
		
		ViewGroup group = (ViewGroup)findViewById(R.id.viewGroup);  
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		
		//载入图片资源ID  
		imageIds = new int[]{
				R.drawable.startup01,
				R.drawable.startup02,
				R.drawable.startup03,
				R.drawable.startup04  
        };  
		
		//将点点加入到ViewGroup中  
        tips = new ImageView[imageIds.length+1];
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
        
      //将图片装载到数组中  
        views = new View[tips.length];  
        for(int i=0; i<views.length; i++){
        	if(i == views.length -1){
        		views[i] = lastView;
        	}else{
                ImageView imageView = new ImageView(this);  
                imageView.setBackgroundResource(imageIds[i]);
                views[i] = imageView; 
        	}
        }  
        
      //设置Adapter  
        viewPager.setAdapter(new MyAdapter());  
        //设置监听，主要是设置点点的背景  
        viewPager.setOnPageChangeListener(this);  
        //设置ViewPager的默认项, 设置为长度的100倍，这样子开始就能往左滑动  
//        viewPager.setCurrentItem((imageViews.length) * 100);  
	}
	
	/** 
     * @author xiect 
     */  
	public class MyAdapter extends PagerAdapter{  

		@Override  
		public int getCount() {  
			return tips.length;  
		}  

		@Override  
		public boolean isViewFromObject(View arg0, Object arg1) {  
			return arg0 == arg1;  
		}  
		
		@Override  
		public void destroyItem(View container, int position, Object object) {  
			((ViewPager)container).removeView(views[position]);
		}  

		/** 
		 * 载入图片进去，用当前的position
		 */  
		 @Override  
		    public Object instantiateItem(View container, int position) {  
		        try {    
		            ((ViewPager)container).addView(views[position]);  
		        }catch(Exception e){  
		        	e.printStackTrace(); 
		        }
		        return views[position];  
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
        for(int i=0; i<tips.length; i++){  
            if(i == selectItems){  
                tips[i].setBackgroundResource(R.drawable.im_input_phiz_page_indicator_sel);  
            }else{  
                tips[i].setBackgroundResource(R.drawable.im_input_phiz_page_indicator_nor);  
            }  
        }  
    }

	@Override
	public void onClick(View v) {
		// 点击“启用”按钮 跳转至主画面
		this.startActivity(MainActivity.makeIntent());
		this.finish();
	} 
}
