package com.brains.app.shopclient.activities;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brains.app.shopclient.R;
import com.brains.app.shopclient.bean.Order;
import com.brains.app.shopclient.common.Util;
import com.brains.framework.activities.base.BaseNormalActivity;
import com.brains.framework.task.GenericTask;
import com.brains.framework.task.TaskParams;
import com.brains.framework.task.TaskResult;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * 订单详情画面
 * @author xiect
 *
 */
public class OrderDetailActivity extends BaseNormalActivity {
	private final static String ACTION = "brains.intent.action.ACTION_ORDER_DETAIL";

	public static final String TAG = "OrderDetailActivity";
	private static final String EXTRA_ORDER_ID = "EXTRA_ORDER_ID";
    private RelativeLayout mProgressBarArea;
    
    private TextView tvTitle;
    private TextView tvOrderStatus;
    private TextView tvOrderNum;
    private TextView tvOrderAmount;
    private TextView tvItemAmount;
    private ImageView imageView;
    private TextView tvItemName;
    private TextView tvName;
    private TextView tvTel;
    private TextView tvAdd;
    private Order mOrder;
    
    private ListView mListView;
    private OrderItemListAdapter mAdatper;

	
	/**
	 * 订单详情Intent
	 * 
	 * @return
	 */
	public static Intent makeIntent(Order order) {
		Intent intent = new Intent().setAction(ACTION);
		intent.putExtra(EXTRA_ORDER_ID, order);
		return intent;
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_order_detail);
		
		Intent intent = getIntent();
		if(intent != null){
			Bundle bundle = intent.getExtras();
			if(bundle != null){
				mOrder = bundle.getParcelable(EXTRA_ORDER_ID);	
			}
		}
		
		if(mOrder == null){
			Util.sysLog(TAG,"mOrder is null");
		}else{
			Util.sysLog(TAG, "item name===>"+mOrder.getName());
			findView();
		}
	}
	
	/**
	 * 初始话视图
	 */
	private void findView(){
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvTitle.setText(R.string.page_title_order_detail);
		
		tvOrderStatus = (TextView) findViewById(R.id.detail_order_status_comment);
		tvOrderNum = (TextView) findViewById(R.id.detail_order_id_number_comment);
		tvOrderAmount = (TextView) findViewById(R.id.order_money_content);
		tvItemAmount = (TextView) findViewById(R.id.item_money_content);
		
		tvItemName = (TextView) findViewById(R.id.show_comment);
		
		tvName = (TextView) findViewById(R.id.title_id);
		tvTel = (TextView) findViewById(R.id.title_comment);
		tvAdd = (TextView) findViewById(R.id.show_comment);
		
		tvOrderStatus.setText(mOrder.getState());
		tvOrderNum.setText(mOrder.getPaymentNo());
		tvOrderAmount.setText(Util.formatRmb(mOrder.getTotalPrice()));
		tvItemAmount.setText(Util.formatRmb(mOrder.getTotalPrice()));
		
		tvName.setText(mOrder.getName());
		tvAdd.setText(mOrder.getRemark());
		tvTel.setText(mOrder.getTel());
		
		mListView = (ListView) findViewById(R.id.order_product_list);
		List<Order> itemList = new ArrayList<Order>();
		itemList.add(mOrder);
		mAdatper = new OrderItemListAdapter(context, itemList);
		mListView.setAdapter(mAdatper);
	}
	
	/**
	 * 获取Category列表数据Task
	 * 
	 * @author xiect
	 * 
	 */
	private class DoLoginTask extends GenericTask {
		String message = null;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressBarArea.setVisibility(View.VISIBLE);
		}

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
//			try {
//				// 获得网络数据
//				String username = mUserName;
//				String password = mPassword;
//				mLoginResult = app.mApi.login(username, password);
//				Util.sysLog(TAG, mLoginResult.getName());
//				return TaskResult.OK;
//			} catch (AppException e) {
//				message = e.getMessage();
//				e.printStackTrace();
//			}
			return TaskResult.FAILED;
		}

		@Override
		protected void onPostExecute(TaskResult result) {
			super.onPostExecute(result);
			mProgressBarArea.setVisibility(View.GONE);
			
//			// 加载的场合
//			if (TaskResult.OK == result) {
//				// 返回成功
//				// 保存用户名密码
//				app.saveUserInfo(mUserName,mPassword);
//				// 保存别名
//				app.mPrefDAO.saveNikeName(mLoginResult.getNikeName());
//				save4Result(Activity.RESULT_OK,mLoginResult,null);
//			} else {
//				if(TaskResult.FAILED == result && message != null && message.length() > 0){
//					Log.d(TAG, "layout_no_data");
//					app.showErrorWithToast(message);
//				}else{
//					Log.d(TAG, "layout_no_data");
//					app.showErrorWithToast("这位客官遇到点小麻烦，请重新登录!");
//				}
//			}
			
		}
	}
}

class OrderItemListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Context context;
	private List<Order> dataList;
	
	
	/**
	 * 商品列表适配器
	 * @param context
	 * @param datas
	 */
	public OrderItemListAdapter(Context context,List<Order> list) {
		super();
		this.context = context;
		dataList = list;
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
			convertView = inflater.inflate(R.layout.order_detail_item_list_item, null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView.findViewById(R.id.cart_single_product_image);
			holder.name = (TextView) convertView.findViewById(R.id.cart_single_product_name);
			holder.price = (TextView) convertView.findViewById(R.id.cart_single_product_price);
			holder.num = (TextView) convertView.findViewById(R.id.shopping_cart_product_num);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Order order = dataList.get(position);
		holder.name.setText(order.getpName());
		holder.price.setText(Util.formatRmb(order.getTotalPrice()));
//		holder.num.setText(Util.formatCount(order.get));
		String src = order.getImgSrc();
		if(!Util.isEmpty(src)){
			ImageLoader.getInstance().displayImage(src, holder.imageView);
		}
		return convertView;
	}

	class ViewHolder {
		private ImageView imageView;// 商品图片
		private TextView name;		// 商品名称
		private TextView price;		// 商品价格
		private TextView num;   	// 商品数量
	}
}