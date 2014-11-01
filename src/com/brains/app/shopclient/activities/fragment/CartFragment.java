package com.brains.app.shopclient.activities.fragment;

import java.util.List;

import com.brains.app.shopclient.R;
import com.brains.app.shopclient.ShoppingApp;
import com.brains.app.shopclient.activities.LoginActivity;
import com.brains.app.shopclient.common.Util;
import com.brains.app.shopclient.db.entity.Product;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

/**
 * 购物车画面
 * @author xiect
 *
 */
public class CartFragment extends BaseFragment {
	private static final String TAG = "CartFragment";
	private static final int REQUEST_CODE = 1;
	private Button mBtnLogin;
	
	private LinearLayout llAreaNoLogin; 	// 未登陆按钮
	private ScrollView mNoDataView;   		// 无数据视图
	private LinearLayout llAreaErrorView; 	// 错误视图
	private ListView mListView; // 购物车列表
	private CartListAdapter mAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		fragmentView = inflater.inflate(R.layout.fragment_main_cart, container,false);
		findView();
		lazyLoad();
		return fragmentView;
	}
	
	/**
	 * 查找画面视图及绑定事件处理
	 */
	public void findView(){
		//  登录Area
		llAreaNoLogin = (LinearLayout)fragmentView.findViewById(R.id.shopping_cart_function_layout);
		// 暂无数据画面
		mNoDataView = (ScrollView) fragmentView.findViewById(R.id.shopping_cart_no_data);
		// 网络错误画面
		llAreaErrorView = (LinearLayout)fragmentView.findViewById(R.id.shopping_cart_load_error);
		// 购物车列表
		mListView = (ListView) fragmentView.findViewById(R.id.shopping_cart_list);
		mAdapter = new CartListAdapter(ctx); 
		mListView.setAdapter(mAdapter);
		
		// 登录按钮
		mBtnLogin = (Button)fragmentView.findViewById(R.id.cart_no_login_but);
		mBtnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 登录画面跳转
				getActivity().startActivityForResult(LoginActivity.makeIntent(), REQUEST_CODE);
			}
		});
		
		// 是否已经登录判定,未登录的场合显示登录按钮
		if(app.isLogin()){
			llAreaNoLogin.setVisibility(View.GONE);
		}else{
			llAreaNoLogin.setVisibility(View.VISIBLE);
		}
		
		// 网络不可用的场合，显示网络错误画面
		// TODO XIECT
//		if(!app.isNetworkAvailable()){
//			llAreaErrorView.setVisibility(View.VISIBLE);
//			mNoDataView.setVisibility(View.GONE);
//		}
	}

	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
			// 登录成功的场合，隐藏登录按钮
			llAreaNoLogin.setVisibility(View.GONE);
		}
	}

	@Override
	protected void lazyLoad() {
		Util.sysLog(TAG, "===> cart === lazyLoad===");
		if(!isPrepared && isVisible && isCreatedView){
			Util.sysLog(TAG," call doGetBrandList===lazyLoad");
			if(app.cart.isCartEmpty()){
				mNoDataView.setVisibility(View.VISIBLE);
				mListView.setVisibility(View.GONE);
			}else{
				mNoDataView.setVisibility(View.GONE);
				mListView.setVisibility(View.VISIBLE);
			}
		}else{
			Util.sysLog(TAG," call donothing===lazyLoad");
		}
	}
	
	
	
}

class CartListAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private Context context;
	private List<Product> dataList;

	/**
	 * 商品列表适配器
	 * @param context
	 * @param datas
	 */
	public CartListAdapter(Context context) {
		super();
		this.context = context;
		ShoppingApp app = (ShoppingApp)context.getApplicationContext();
		this.dataList = app.cart.cartItemList;
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
			convertView = inflater.inflate(R.layout.shopping_cart_list_item, null);
			holder = new ViewHolder();
//			holder.title = (TextView) convertView.findViewById(R.id.product_name_text);
//			holder.desc = (TextView) convertView.findViewById(R.id.product_description_text);
//			holder.price = (TextView) convertView.findViewById(R.id.product_price_text);
//			holder.imageView = (ImageView) convertView.findViewById(R.id.product_image);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
//		Product product = dataList.get(position);
//		holder.title.setText(product.getName());
//		holder.desc.setText(product.getDesc());
//		holder.price.setText(product.getPrice());
//		String src = product.getImgSrc();
//		if(!StringUtil.isEmpty(src)){
//			ImageLoader.getInstance().displayImage(src, holder.imageView);
//		}
		return convertView;
	}

	class ViewHolder {
//		private ImageView imageView;
//		private TextView title;
//		private TextView title;
//		private TextView title;
//		private TextView title;
//		private TextView title;
//		private TextView title;
//		private TextView desc;
//		private TextView price;
	}
}
