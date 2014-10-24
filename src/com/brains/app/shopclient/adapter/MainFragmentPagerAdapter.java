package com.brains.app.shopclient.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainFragmentPagerAdapter extends FragmentPagerAdapter{  
	List<Fragment> list;  

	public MainFragmentPagerAdapter(FragmentManager fm,List<Fragment> list) {  
		super(fm);  
		this.list = list;  

	}  

	@Override  
	public int getCount() {  
		return list.size();  
	}  

	@Override  
	public Fragment getItem(int arg0) {  
		return list.get(arg0);  
	}  
}
