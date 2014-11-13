package com.brains.app.shopclient.activities.fragment;

import java.util.List;

import com.brains.app.shopclient.R;
import com.brains.app.shopclient.ShoppingApp;
import com.brains.app.shopclient.activities.LoginActivity;
import com.brains.app.shopclient.activities.NewOderActivity;
import com.brains.app.shopclient.common.Util;
import com.brains.app.shopclient.db.entity.Product;
import com.brains.framework.common.Const;
import com.brains.framework.task.GenericTask;
import com.brains.framework.task.TaskParams;
import com.brains.framework.task.TaskResult;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * 购物车画面
 * @author xiect
 *
 */
public class CartFragment extends BaseFragment {
	public static final String TAG = "CartFragment";
	private static final int REQUEST_CODE = 1;
	private static final int REQUEST_CODE_4_ORDER = 2;
	private Button mBtnLogin;
	
	private LinearLayout llAreaNoLogin; 	// 未登陆按钮
	private ScrollView mNoDataView;   		// 无数据视图
	private LinearLayout llAreaErrorView; 	// 错误视图
	private ListView mListView; // 购物车列表
	private CartListAdapter mAdapter;
	private TextView tvHeaderEditText;  //  顶部编辑按钮文字(编辑/完成)
	private LinearLayout btnHeaderEdit; // 顶部编辑按钮
	
	private CheckBox btnSelectAll; 	// 全选按钮
	private Button btnOrder;			// 结算按钮
	private RelativeLayout btnDelete;   // 删除按钮
	private TextView tvTotal;			// 总计
	private RelativeLayout footEditView; // 底部编辑视图
	private RelativeLayout footNormalView; // 底部编辑视图
	private FrameLayout footView;
	private GenericTask mLoadCartTask; // 加载购物车数据
	private boolean isCheckedAll = true;
	private TextView mTvNoDataDesc;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		fragmentView = inflater.inflate(R.layout.fragment_main_cart, container,false);
		findView();
//		lazyLoad();
		return fragmentView;
	}
	
	/**
	 * 更新支付金额View
	 */
	void updateAmount(){
		Util.sysLog(TAG, "==更新支付金额View==");
		// 设置商品总计金额
		String totalAmount = app.cart.calTotalAmount();
		tvTotal.setText(Util.formatRmb(totalAmount));
		// 设置商品 总件数
		String btnTitle = getResources().getString(R.string.settle_accounts_value);  
		btnTitle = String.format(btnTitle, app.cart.getTotalNum());
		btnOrder.setText(btnTitle);
	}
	
	/**
	 * 查找画面视图及绑定事件处理
	 */
	public void findView(){
		//  登录Area
		llAreaNoLogin = (LinearLayout)fragmentView.findViewById(R.id.shopping_cart_function_layout);
		// 暂无数据画面
		mNoDataView = (ScrollView) fragmentView.findViewById(R.id.shopping_cart_no_data);
		mTvNoDataDesc = (TextView) fragmentView.findViewById(R.id.cart_no_data_state);
		
		// 网络错误画面
		llAreaErrorView = (LinearLayout)fragmentView.findViewById(R.id.shopping_cart_load_error);
		// 购物车列表
		mListView = (ListView) fragmentView.findViewById(R.id.shopping_cart_list);
		mAdapter = new CartListAdapter(ctx,this); 
		mListView.setAdapter(mAdapter);
		// 顶部编辑按钮
		btnHeaderEdit = (LinearLayout)fragmentView.findViewById(R.id.shopping_cart_delete_right);
		tvHeaderEditText = (TextView) fragmentView.findViewById(R.id.shopping_cart_text);
		// 底部全选按钮
		btnSelectAll = (CheckBox) fragmentView.findViewById(R.id.cart_select_all_button);
		btnOrder = (Button) fragmentView.findViewById(R.id.cart_order);
		btnDelete= (RelativeLayout) fragmentView.findViewById(R.id.shopping_cart_edit_to_delete);
		// 底部编辑视图
		footEditView = (RelativeLayout) fragmentView.findViewById(R.id.shopping_cart_edit_view);
		// 底部正常显示视图
		footNormalView = (RelativeLayout) fragmentView.findViewById(R.id.shopping_cart_show_view);
		// 总计
		tvTotal = (TextView) fragmentView.findViewById(R.id.cart_count_price_tv);
		footView = (FrameLayout) fragmentView.findViewById(R.id.shopping_cart_count_bottom_layout);
		
		// 登录按钮
		mBtnLogin = (Button)fragmentView.findViewById(R.id.cart_no_login_but);
		mBtnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 登录画面跳转
				startActivityForResult(LoginActivity.makeIntent(), REQUEST_CODE);
			}
		});
		btnHeaderEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String txt = tvHeaderEditText.getText().toString();
				if(Const.BTN_EDIT.equals(txt)){
					//  点击编辑按钮
					tvHeaderEditText.setText(R.string.select_yb_button_completed);
					doEditList();
				}else{
					// 点击完成按钮
					tvHeaderEditText.setText(R.string.shopping_cart_product_edit);
					// 删除数量为0 的产品
					app.cart.deleteZoroProduct(getActivity());
					// 更新badage
					app.updateCartNum(getActivity());
					// 显示正常画面
					doEditListComplete();
				}
			}
		});
		btnOrder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Util.sysLog(TAG, "btnOrder btn click");
				int selectedNum = app.cart.getSelectedProductNum();
				if(selectedNum == 1){
					if(app.isLogin()){
						// 一件商品的场合 订单画面迁移
						ctx.startActivity(NewOderActivity.makeIntent());
					}else{
						// 未登录的情况下 去登录画面
						startActivityForResult(LoginActivity.makeIntent(), REQUEST_CODE_4_ORDER);
					}
				}else if(selectedNum > 1){
					String msg = getResources().getString(R.string.msg_product_selected_one_please);
					msg = String.format(msg, selectedNum);
					app.showErrorWithToast(msg);
				}else{
					app.showErrorWithToast(R.string.msg_product_not_selected);
				}
			}
		});
		btnDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Util.sysLog(TAG, "delete btn click");
				int selectedNum = app.cart.getSelectedProductNum();
				if(selectedNum > 0){
					String msg = getResources().getString(R.string.product_delete_string);
					msg = String.format(msg, selectedNum);
					showDeleteAlert(msg,ctx);
				}else{
					app.showErrorWithToast(R.string.msg_product_not_selected);
				}
			}
		});
		// 全选/全不选
		btnSelectAll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isCheckedAll){
					Util.sysLog(TAG, "===全不选===");
					isCheckedAll = false;
				}else{
					Util.sysLog(TAG, "===全选===");
					// 全选的场合
					isCheckedAll = true;
				}
				// 取消全选
				app.cart.setCheckedAll(isCheckedAll);
				// 刷新画面
				mAdapter.notifyDataSetChanged();
				btnSelectAll.setChecked(isCheckedAll);
			}
		});
		// 是否已经登录判定,未登录的场合显示登录按钮
		showLoginView();
		
		// 网络不可用的场合，显示网络错误画面
		if(!app.isNetworkAvailable()){
			llAreaErrorView.setVisibility(View.VISIBLE);
			mNoDataView.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 删除确认对话框
	 * @param msg
	 * @param ctx
	 */
	private void showDeleteAlert(String msg,Context ctx) {
		AlertDialog.Builder builder = new Builder(ctx);
		builder.setTitle("提示")
			.setMessage(msg)
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// 删除购物车中的商品
					Util.sysLog(TAG, "删除购物车中的商品");
					app.cart.deleteSelectedProduct(getActivity());
					showCartListView();
				}
			})
			.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	/**
	 * 是否已经登录判定,未登录的场合显示登录按钮
	 */
	private void showLoginView(){
		// 是否已经登录判定,未登录的场合显示登录按钮
		if(app.isLogin()){
			llAreaNoLogin.setVisibility(View.GONE);
		}else{
			llAreaNoLogin.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 显示购物车列表画面
	 */
	private void showCartListView(){
		Util.sysLog(TAG, "显示购物车列表画面");
		if(app.cart.isCartEmpty()){
			Util.sysLog(TAG, "购物车数据是空，显示空画面");
			mTvNoDataDesc.setText(R.string.cart_empty_tip);
			mNoDataView.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
			llAreaErrorView.setVisibility(View.GONE);
			// 隐藏合计
			footView.setVisibility(View.GONE);
		}else{
			Util.sysLog(TAG, "购物车中有数据，正常显示画面");
			mNoDataView.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);
			llAreaErrorView.setVisibility(View.GONE);
			footView.setVisibility(View.VISIBLE);
			// 设置商品总计金额
			updateAmount();
			
//			// 设置商品总计金额
//			String totalAmount = app.cart.calTotalAmount();
//			tvTotal.setText(Util.formatRmb(totalAmount));
//			// 设置商品 总件数
//			String btnTitle = getResources().getString(R.string.settle_accounts_value);  
//			btnTitle = String.format(btnTitle, app.cart.getTotalNum());
//			btnOrder.setText(btnTitle);
			
		}
		mAdapter.notifyDataSetChanged();
	}
	
	/**
	 * 显示加载中画面
	 */
	private void showErrorView(){
		// 显示加载中
		llAreaErrorView.setVisibility(View.VISIBLE);
		// 隐藏加载中画面
		mNoDataView.setVisibility(View.GONE);
		// TODO 购物车0件的场合
	}
	
	/**
	 * 显示加载中画面
	 */
	private void showLoadingView(){
		// 显示加载中
		mNoDataView.setVisibility(View.VISIBLE);
		mTvNoDataDesc.setText(R.string.cart_loading);
		llAreaErrorView.setVisibility(View.GONE);
	}
	
	/**
	 * 点击编辑事件处理
	 */
	private void doEditList(){
		footEditView.setVisibility(View.VISIBLE);
		footNormalView.setVisibility(View.GONE);
		mAdapter.showEditView();
	}
	
	/**
	 * 购物车列表编辑完成
	 */
	private void doEditListComplete(){
		footEditView.setVisibility(View.GONE);
		footNormalView.setVisibility(View.VISIBLE);
		mAdapter.showNormalView();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Util.sysLog(TAG, "onActivityResult requestCode:" + requestCode +"\t resultCode:" + resultCode);
		if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
			Util.sysLog(TAG, "onActivityResult cart page");
			// 登录成功的场合，隐藏登录按钮
			llAreaNoLogin.setVisibility(View.GONE);
		}else if(requestCode == REQUEST_CODE_4_ORDER && resultCode == Activity.RESULT_OK){
			Util.sysLog(TAG, "onActivityResult go new order pag");
			// 登录成功的场合，隐藏登录按钮
			llAreaNoLogin.setVisibility(View.GONE);
			// go new order page
			ctx.startActivity(NewOderActivity.makeIntent());
		}
		
	}

	
	@Override
	public void onResume() {
		super.onResume();
		Util.sysLog(TAG, "onResume");
		lazyLoad();
	}

	@Override
	protected void lazyLoad() {
		Util.sysLog(TAG, "===> cart === lazyLoad===isPrepared:" + isPrepared + "\t isVisible:" + isVisible + "\tisCreatedView:" + isCreatedView);
		if(isVisible && isCreatedView){
			Util.sysLog(TAG," call doGetBrandList===lazyLoad");
			showLoginView();
			// 从数据库加载购物车数据
			doLoadCartData();
		}else{
			Util.sysLog(TAG," call donothing===lazyLoad");
		}
	}
	
	/**
	 * 获取购物车数据
	 */
	private void doLoadCartData(){
		if (mLoadCartTask != null
				&& mLoadCartTask.getStatus() == GenericTask.Status.RUNNING) {
			return;
		} else {
			mLoadCartTask = new LoadCartTask();
			mLoadCartTask.execute();
		}
	}
	
	/**
	 * 加载购物车数据类
	 * @author xiect
	 *
	 */
	private class LoadCartTask extends GenericTask {
		private String message;
		@Override
		protected void onPostExecute(TaskResult result) {
			super.onPostExecute(result);
			Util.sysLog(TAG," call onPostExecute=");
			if(TaskResult.OK == result){
				// 显示购物车画面
				showCartListView();
			}else{
				// 显示错误画面
				showErrorView();
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// 显示加载中画面
			showLoadingView();
		}

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
			try{
				Util.sysLog(TAG," call _doInBackground=");
				app.cart.reloadData(getActivity());
			}catch(Exception e){
				e.printStackTrace();
				message = e.getMessage();
				Util.sysLog(TAG, "error:=>"+message);
				return TaskResult.FAILED;	
			}
			return TaskResult.OK;
		}
		
	}
	
}

class CartListAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private Context context;
	private List<Product> dataList;
	private boolean isEdit;
	private CartFragment fragment; 
	/**
	 * 显示编辑列表
	 */
	public void showEditView(){
		isEdit = true;
		this.notifyDataSetChanged();
	}
	
	public void showNormalView(){
		isEdit = false;
		this.notifyDataSetChanged();
	}
	
	/**
	 * 商品列表适配器
	 * @param context
	 * @param datas
	 */
	public CartListAdapter(Context context,CartFragment frg) {
		super();
		this.context = context;
		fragment = frg;
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
			holder.check = (CheckBox) convertView.findViewById(R.id.cart_single_product_cb); 	
			holder.imageView = (ImageView) convertView.findViewById(R.id.cart_single_product_image);
			holder.name = (TextView) convertView.findViewById(R.id.cart_single_product_name);
			holder.price = (TextView) convertView.findViewById(R.id.cart_single_product_price);
			holder.num = (TextView) convertView.findViewById(R.id.shopping_cart_product_num);
			holder.numEditView = (RelativeLayout) convertView.findViewById(R.id.cart_single_product_num_edit);
			holder.editNum = (TextView)convertView.findViewById(R.id.cart_single_product_et_num);
			holder.btnAdd = (ImageButton)convertView.findViewById(R.id.cart_single_product_num_add); 
			holder.btnReduce = (ImageButton)convertView.findViewById(R.id.cart_single_product_num_reduce);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Product product = dataList.get(position);
		holder.check.setChecked(product.isSelected());
		holder.name.setText(product.getName());
		holder.price.setText(Util.formatRmb(product.getPrice()));
		holder.num.setText(Util.formatCount(product.getNum()));
		holder.editNum.setText(String.valueOf(product.getNum()));
		holder.postion = position;
		
		String src = product.getImgSrc();
		if(!Util.isEmpty(src)){
			ImageLoader.getInstance().displayImage(src, holder.imageView);
		}
		
		if(isEdit){
			// 编辑模式
			holder.numEditView.setVisibility(View.VISIBLE);
			holder.num.setVisibility(View.GONE);
			
		}else{
			// 正常模式
			holder.numEditView.setVisibility(View.GONE);
			holder.num.setVisibility(View.VISIBLE);
		}
		
		// 事件处理
		holder.btnReduce.setTag(holder);
		holder.btnReduce.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ViewHolder parent = (ViewHolder) v.getTag();
				int position = parent.postion;

				Util.sysLog("Cart", "--- ===position=" + position);
				if (position < dataList.size()) {
					Product item = dataList.get(position);
					if (item.getNum() > 0) {
						// 大于0时可减
						int num = item.getNum() - 1;
						parent.editNum.setText(String.valueOf(num));
						item.setNum(num);
					} else {
						// 小于0件时按钮不可用
						v.setEnabled(false);
					}
					if (item.getNum() < Const.MAX_NUM) {
						// 小于最大件数的场合 加按钮可用
						parent.btnAdd.setEnabled(true);
					}
					
					if (item.getNum() <= 0) {
						v.setEnabled(false);
					}
					if(fragment != null){
						fragment.updateAmount();	
					}
				}
			}
		});
		holder.btnAdd.setTag(holder);
		holder.btnAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ViewHolder parent = (ViewHolder)v.getTag();
				int position = parent.postion;
				
				Util.sysLog("Cart", "+++ ===position=" + position);
				if(position < dataList.size()){
					Product item = dataList.get(position);
					if(item.getNum() < Const.MAX_NUM){
						int num = item.getNum() + 1;
						parent.editNum.setText(String.valueOf(num));
						// 小于最大件数时可增加
						item.setNum(num);
					}else{
						// 大于最大件数是add按钮不可用
						v.setEnabled(false);
					}
					if(item.getNum() > 0){
						// 大于0 的场合 减按钮可用
						parent.btnReduce.setEnabled(true);
					}
					if (item.getNum() >= Const.MAX_NUM) {
						v.setEnabled(false);
					}
					if(fragment != null){
						fragment.updateAmount();	
					}
				}
			}
		});
		holder.check.setId(position);
		holder.check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				int position = buttonView.getId();
				if(position < dataList.size()){
					Util.sysLog(CartFragment.TAG, "row:" + position + "\t ischecked:" + isChecked);
					dataList.get(position).setSelected(isChecked);
					if(fragment != null){
						fragment.updateAmount();	
					}
				}
			}
		});
		
		
		return convertView;
	}

	class ViewHolder {
		private CheckBox check;		// 是否选择
		private ImageView imageView;// 商品图片
		private TextView name;		// 商品名称
		private TextView price;		// 商品价格
		private TextView num;   	// 商品数量
		private TextView editNum;	// 数量编辑
		private RelativeLayout numEditView;// 数量编辑视图
		private ImageButton btnAdd; // 数量加
		private ImageButton btnReduce;// 数量减
		private int postion;
	}
}
