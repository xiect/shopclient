package com.brains.app.shopclient.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;

import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.brains.app.shopclient.R;
import com.brains.app.shopclient.bean.Order;
import com.brains.app.shopclient.common.Util;
import com.brains.app.shopclient.db.entity.Product;
import com.brains.framework.activities.base.BaseNormalActivity;
import com.brains.framework.common.Const;
import com.brains.framework.exception.AppException;
import com.brains.framework.task.GenericTask;
import com.brains.framework.task.TaskParams;
import com.brains.framework.task.TaskResult;
import com.brains.framework.util.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * 订单检索一览画面
 * @author xiect
 *
 */
public class OrderSearchActivity extends BaseNormalActivity implements OnClickListener {
	private final static String ACTION = "brains.intent.action.ACTION_ORDER_SEARCH";
	public static final String TAG = "OrderSearchActivity";


	private int mCurrentPageNo; // 当前页数
	protected ProgressBar loadMoreGIFBtn;
	
	private RelativeLayout mTvOneMonth;
	private RelativeLayout mTvOneMonthBefore;
	
	private ListView mListView;
	private List<Order> mDataList = new ArrayList<Order>();
	private int markCurrentPos;
	private ImageButton markSelected;
	private int[] startPointArr = new int[2];

	private SearchTask mSearchTask;
	private RelativeLayout mLoading;
	private LinearLayout mNetErrorView;
	private View mListFooter;
	private String mSortMod  = Const.ORDER_ONE_MONTH;
	private TextView mTvNoData;
	private TextView mTvTitle;
	private OrderListAdapter mListViewAdapter;
	private boolean isGetMore; // 是否加载更多
	
	/**
	 * 取得商品检索画面Intent
	 * @return
	 */
	public static Intent makeIntent() {
		Intent intent = new Intent().setAction(ACTION);
		return intent;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_order_search);
		findView();
		// 取得查询一览数据
		doGetProductList();
	}
	
	private void findView(){
		mTvTitle = (TextView) findViewById(R.id.tv_title);
		mLoading = (RelativeLayout) findViewById(R.id.rl_oading_area);
		mTvNoData = (TextView)findViewById(R.id.tv_hint_no_data);
		mNetErrorView = (LinearLayout) findViewById(R.id.main_loading_error_tips);
		

		mTvOneMonth = (RelativeLayout)findViewById(R.id.search_one_month_button);
		mTvOneMonthBefore = (RelativeLayout)findViewById(R.id.search_one_month_before_button);
	
		markSelected = (ImageButton)findViewById(R.id.v_seleted_mark);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		
		Button btnReload = (Button)findViewById(R.id.loading_error_but);
		btnReload.setOnClickListener(this);
		
		int oneOfthree = dm.widthPixels / 2;
		int markW = oneOfthree / 3;
		markW = oneOfthree - markW;
		int offset = (oneOfthree - markW) / 2 ;
		Util.sysLog(TAG, "oneOfthree:" + oneOfthree + "\t markW:" + markW + "\t offset:" + offset);
		LayoutParams param = markSelected.getLayoutParams();
		param.width = markW;
		markSelected.setLayoutParams(param);
		markSelected.setX(offset);
		
		startPointArr[0] = 0;
		startPointArr[1] = oneOfthree ;
		markCurrentPos = 0; // 当前位置取得
		
		// 列表适配
		mListView = (ListView)findViewById(R.id.lv_result_list);
		mListViewAdapter = new OrderListAdapter(this,mDataList);
		mListView.setAdapter(mListViewAdapter);
		// 注册事件
		registerOnClickListener(mListView); 
		mListFooter = View.inflate(this, R.layout.listview_footer, null);
	    mListView.addFooterView(mListFooter, null, true);
		loadMoreGIFBtn = (ProgressBar) mListFooter.findViewById(R.id.rectangleProgressBar);
		// 设定标题
		mTvTitle.setText(R.string.page_title_order_search);
		
		// bunding event
		mTvOneMonth.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mTvOneMonthBefore.setSelected(false);
				mTvOneMonth.setSelected(true);
				slideToCurrentButton(startPointArr[0]);
				doSort(Const.ORDER_ONE_MONTH);
			}
		});
		
		mTvOneMonthBefore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mTvOneMonth.setSelected(false);
				mTvOneMonthBefore.setSelected(true);
				slideToCurrentButton(startPointArr[1]);
				doSort(Const.ORDER_ONE_MONTH_BEFORE);
			}
		});
	}
	
	
	/**
	 * 重新排序
	 */
	private void doSort(String sort){
		mSortMod = sort;
		mCurrentPageNo = 1;
		isGetMore = false;
		doGetProductList();
	}
	
	private void registerOnClickListener(ListView listView) {
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO CLICK
				if(arg2 < mDataList.size()){
					Order order = mDataList.get(arg2);
					String id = order.getId();
					if(!Util.isEmpty(id)){
						// 订单详细画面迁移
						startActivity(OrderDetailActivity.makeIntent());
						overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
					}
				}else{
					// 加载更多
					Util.sysLog(TAG, "pos:==>" + arg2);
					isGetMore = true;
					doGetProductList();
				}
			}
		});
	}
	
    /**
     * 划动顶部菜单
     * @param view
     */
    private void slideToCurrentButton(int targetPos){
		int xEnd = targetPos;
		Util.sysLog(TAG,"xStart:" + markCurrentPos + " xEnd:" + xEnd );
        TranslateAnimation animation = new TranslateAnimation(markCurrentPos, xEnd, 0, 0);  
        animation.setDuration(100);  
        animation.setFillAfter(true);  
        markSelected.startAnimation(animation); 
        markCurrentPos = xEnd;
    }
    
	private void showViewWithNetError() {
		Log.e(TAG, "showViewWithNoData");
		mNetErrorView.setVisibility(View.VISIBLE);
		// 隐藏列表
		mListView.setVisibility(View.GONE);
		// 显示无数据画面
		mTvNoData.setVisibility(View.GONE);
	}
    
    
	private void showViewWithNoData() {
		Log.e(TAG, "showViewWithNoData");
		// 网络错误失败画面隐藏
		mNetErrorView.setVisibility(View.GONE);
		// 隐藏列表
		mListView.setVisibility(View.GONE);
		// 显示无数据画面
		mTvNoData.setVisibility(View.VISIBLE);
	}
	
	private void showViewWithData() {
		Log.e(TAG, "showViewWithData");
		// 网络错误失败画面隐藏
		mNetErrorView.setVisibility(View.GONE);
		// 隐藏错误画面
		mTvNoData.setVisibility(View.GONE);
		mListView.setVisibility(View.VISIBLE);
		// 数据更新后刷新列表
		mListViewAdapter.notifyDataSetChanged();
	}
	
	/**
	 * 获取商品一览
	 */
	private void doGetProductList(){
		if(mSearchTask != null &&
				mSearchTask.getStatus() == GenericTask.Status.RUNNING) {
			return;
		}
		
		mSearchTask = new SearchTask();
		mSearchTask.execute();
	}
	
	/**
	 * 检索task
	 * @author xiect
	 *
	 */
	private class SearchTask extends GenericTask {
		private String message;
		private List<Order> tempList;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mLoading.setVisibility(View.VISIBLE);
			mTvNoData.setVisibility(View.GONE);
			mNetErrorView.setVisibility(View.GONE);
		}

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
			try {
				if(!isGetMore){
					mCurrentPageNo = 1;
				}
				tempList = app.mApi.getOrderSerachList(mSortMod);
			} catch (AppException e) {
				message = e.getMessage();
				e.printStackTrace();
				return TaskResult.FAILED;
			}
			return TaskResult.OK;
		}
		
		@Override
		protected void onPostExecute(TaskResult result) {
			super.onPostExecute(result);
			// 隐藏loading
			mLoading.setVisibility(View.GONE);
			if(isGetMore){
				// 加载更多的场合
				if (TaskResult.OK == result && tempList != null
						&& tempList.size() > 0) {
					mDataList.addAll(tempList);
					// 判断是否要显示更多按钮
					if(Const.PAGE_COUNT == tempList.size()){
						// 显示更多按钮
						loadMoreGIFBtn.setVisibility(View.VISIBLE);
					}else{
						loadMoreGIFBtn.setVisibility(View.GONE);
					}
					// 显示画面
					showViewWithData();
				} else {
					if(TaskResult.FAILED == result && message != null && message.length() > 0){
						Log.d(TAG, "layout_no_data");
						app.showErrorWithToast(message);
						showViewWithNetError();
					}else{
						Log.d(TAG, "layout_no_data");
						showViewWithNoData();
					}
				}  
			}else{
				if (TaskResult.OK == result && tempList != null
						&& tempList.size() > 0) {
					mDataList.clear();
					mDataList.addAll(tempList);
					// 判断是否要显示更多按钮
					if(Const.PAGE_COUNT == tempList.size()){
						// 显示更多按钮
						loadMoreGIFBtn.setVisibility(View.VISIBLE);
					}else{
						loadMoreGIFBtn.setVisibility(View.GONE);
					}
					// 显示List画面
					showViewWithData();
				} else {
					if(TaskResult.FAILED == result && message != null && message.length() > 0){
						Log.d(TAG, "layout_no_data");
						// 显示网络失败画面
						app.showErrorWithToast(message);
						showViewWithNetError();
					}else{
						// 显示无结果集画面
						Log.d(TAG, "layout_no_data");
						showViewWithNoData();
					}
				}  
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}


class OrderListAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private Context context;
	private List<Order> dataList;

	/**
	 * 订单列表适配器
	 * @param context
	 * @param datas
	 */
	public OrderListAdapter(Context context, List<Order> datas) {
		super();
		this.context = context;
		this.dataList = datas;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
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
			convertView = inflater.inflate(R.layout.product_item, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.product_name_text);
			holder.desc = (TextView) convertView.findViewById(R.id.product_description_text);
			holder.price = (TextView) convertView.findViewById(R.id.product_price_text);
			holder.imageView = (ImageView) convertView.findViewById(R.id.product_image);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Order order = dataList.get(position);
		holder.title.setText(order.getName());
//		holder.desc.setText(order.getDesc());
//		holder.price.setText(order.getPrice());
		
		String src = order.getImgSrc();
		if(!StringUtil.isEmpty(src)){
			ImageLoader.getInstance().displayImage(src, holder.imageView);
		}
		return convertView;
	}

	class ViewHolder {
		private ImageView imageView;
		private TextView title;
		private TextView desc;
		private TextView price;
	}
}
