package com.brains.app.shopclient.activities.fragment;

import com.brains.app.shopclient.ShoppingApp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {
	protected Context ctx;
	protected ShoppingApp app;
	protected boolean isVisible;
	protected boolean isPrepared;
	protected View fragmentView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ctx = getActivity();
		Log.e("BASE","ctx class name:"+ctx.getClass().getName());
		app = (ShoppingApp)getActivity().getApplicationContext();
		Log.e("BASE","ctx class name2222:");
//		isPrepared = true;
		return null;
	}

	@Override    
	public void setUserVisibleHint(boolean isVisibleToUser) {       
		super.setUserVisibleHint(isVisibleToUser);
		Log.e("BASE"," setUserVisibleHint:" + getUserVisibleHint());
		if(getUserVisibleHint()) {            
			isVisible = true;          
			onVisible();      
		} else {            
			isVisible = false;         
			onInvisible();     
		} 
	}
	
	protected void onVisible(){  
		lazyLoad();
	}
	
	protected abstract void lazyLoad();
	
	protected void onInvisible(){
		
	}
}
