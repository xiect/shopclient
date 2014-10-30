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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.brains.app.shopclient.R;
import com.brains.app.shopclient.bean.Product;
import com.brains.app.shopclient.common.Util;
import com.brains.framework.activities.base.BaseNormalActivity;
import com.brains.framework.common.Const;
import com.brains.framework.exception.AppException;
import com.brains.framework.task.GenericTask;
import com.brains.framework.task.TaskParams;
import com.brains.framework.task.TaskResult;
import com.brains.framework.util.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * 商品检索一览画面
 * @author xiect
 *
 */
public class SearchActivity extends BaseNormalActivity{
	private final static String ACTION_SEARCH = "brains.intent.action.ACTION_SEARCH";
	public static final String TAG = "SearchActivity";
	private static final String EXTRA_CATEGORY_ID = "CATEGORY_ID";
	private static final String EXTRA_SHOP_ID = "SHOP_ID";
	private static final String EXTRA_KEYWORD = "KEY_WORD";
	private static final String EXTRA_SEARCH_MOD = "SEARCH_MOD";
	
	private String mCategoryId; // 分类ID
	private String mKeyWord; // 关键字
	private String mBrandId; //  品牌ID
	private String mSearchMod; //  检索模式
	private String mSortMod;  //  排序模式
	
	private RelativeLayout mTvSortDefault;
	private RelativeLayout mTvSortDesc;
	private RelativeLayout mTvSortAsc;
	private ListView mListView;
	private List<Product> mDataList = new ArrayList<Product>();
	private int markCurrentPos;
	private ImageButton markSelected;
	private int[] startPointArr = new int[3];
	private EditText mTxtKeyword;
	private Button mBtnSearch; // 搜索按钮
	private SearchTask mSearchTask;
	private RelativeLayout mLoading;
	private TextView mTvNoData;
	private ProductListAdapter mListViewAdapter;
	
	/**
	 * 取得商品检索画面Intent
	 * @return
	 */
	public static Intent makeIntent() {
		Intent intent = new Intent().setAction(ACTION_SEARCH);
		return intent;
	}
	
	/**
	 * 取得商品检索画面Intent
	 * @param categoryId 分类
	 * @param keyword 关键字
	 * @param shopId 商户Id
	 * @return
	 */
	public static Intent makeIntent4Category(String categoryId) {
		Intent intent = new Intent().setAction(ACTION_SEARCH);
		intent.putExtra(EXTRA_CATEGORY_ID, categoryId);
		intent.putExtra(EXTRA_SEARCH_MOD, EXTRA_CATEGORY_ID);
		
		return intent;
	}
	/**
	 * 取得商品检索画面Intent
	 * @param keyword 关键字
	 * @return
	 */
	public static Intent makeIntent4Keyword(String keyword) {
		Intent intent = new Intent().setAction(ACTION_SEARCH);
		intent.putExtra(EXTRA_KEYWORD, keyword);
		intent.putExtra(EXTRA_SEARCH_MOD, EXTRA_KEYWORD);
		return intent;
	}
	/**
	 * 取得商品检索画面Intent
	 * @param categoryId 分类
	 * @param keyword 关键字
	 * @param shopId 商户Id
	 * @return
	 */
	public static Intent makeIntent4Brand(String shopId) {
		Intent intent = new Intent().setAction(ACTION_SEARCH);
		intent.putExtra(EXTRA_SHOP_ID, shopId);
		intent.putExtra(EXTRA_SEARCH_MOD, EXTRA_SHOP_ID);
		return intent;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_search);
		findView();
		
		Intent intent = getIntent();
		if(intent != null ){
			Bundle bundle = intent.getExtras();
			if(bundle != null){
				mCategoryId = bundle.getString(EXTRA_CATEGORY_ID);
				mKeyWord = bundle.getString(EXTRA_KEYWORD);
				mBrandId = bundle.getString(EXTRA_SHOP_ID);
				mSearchMod = bundle.getString(EXTRA_SEARCH_MOD);
			}
		}
		mSortMod = Const.SORT_DEFAULT;
		// 初始话参数校验
		doinitParamCheck();
		// 取得查询一览数据
		doGetProductList();
	}
	
	/**
	 * 校验初始化参数
	 * 检验失败的场合 画面自动退出并返回上一画面
	 */
	private void doinitParamCheck(){
		if(EXTRA_CATEGORY_ID.equals(mSearchMod)){
			// 分类查询商品的场合
			if(Util.isEmpty(mCategoryId)){
				// 提示请选择有效分类并退出
				app.showErrorWithToast(R.string.error_choose_category_please);
				finish();
			}
		}
		
		if(EXTRA_SHOP_ID.equals(mBrandId)){
			// brand 
			if(Util.isEmpty(mBrandId)){
				// 提示请选择有效Brand并退出
				app.showErrorWithToast(R.string.error_choose_brand_please);
				finish();
			}
		}
	}
	
	private void findView(){
		mLoading = (RelativeLayout) findViewById(R.id.rl_oading_area);
		mTvNoData = (TextView)findViewById(R.id.tv_hint_no_data);
		
		mTxtKeyword = (EditText)findViewById(R.id.homeActivity_autoComplete);
		mBtnSearch = (Button) findViewById(R.id.btn_search_btn);
		mTvSortDefault = (RelativeLayout)findViewById(R.id.search_default_sort_button);
		mTvSortDesc = (RelativeLayout)findViewById(R.id.search_price_desc_button);
		mTvSortAsc = (RelativeLayout)findViewById(R.id.search_price_ase_button);
		markSelected = (ImageButton)findViewById(R.id.v_seleted_mark);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		app.showErrorWithToast("screen width:"+ dm.widthPixels);
		
		int oneOfthree = dm.widthPixels / 3;
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
		startPointArr[2] = oneOfthree * 2 ;
		markCurrentPos = 0; // 当前位置取得
		
		// 列表适配
		mListView = (ListView)findViewById(R.id.lv_result_list);
		mListViewAdapter = new ProductListAdapter(this,mDataList);
		mListView.setAdapter(mListViewAdapter);
		// 注册事件
		registerOnClickListener(mListView); 
		
		// bunding event
		mTvSortDefault.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mTvSortDesc.setSelected(false);
				mTvSortAsc.setSelected(false);
				mTvSortDefault.setSelected(true);
				slideToCurrentButton(startPointArr[0]);
			}
		});
		
		mTvSortAsc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mTvSortDesc.setSelected(false);
				mTvSortAsc.setSelected(true);
				mTvSortDefault.setSelected(false);
				slideToCurrentButton(startPointArr[2]);
			}
		});
		
		mTvSortDesc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mTvSortDesc.setSelected(true);
				mTvSortAsc.setSelected(false);
				mTvSortDefault.setSelected(false);
				slideToCurrentButton(startPointArr[1]);
			}
		});
		mBtnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(checkInput()){
					// 执行查询操作(keyword)
					mSearchMod = EXTRA_KEYWORD;
					doGetProductList();
				}else{
					// 提示错误信息
					app.showErrorWithToast(R.string.msg_please_input_keyword);
				}
			}
		});
	}
	
	private void registerOnClickListener(ListView listView) {
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(arg2 < mDataList.size()){
					Product product = mDataList.get(arg2);
					String id = product.getId();
					if(!Util.isEmpty(id)){
						// 商品详细画面迁移
						// TODO XIECT
//						startActivity(SearchActivity.makeIntent4Category(id);
//						overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
					}
				}
			}
		});
	}
	
	/**
	 * keyword 输入校验
	 */
	private boolean checkInput(){
		mKeyWord = mTxtKeyword.getText().toString();
		Util.sysLog(TAG, "mTxtKeyword==> "+mKeyWord);
		if(Util.isEmpty(mKeyWord)){
			return false;
		}
		return true;
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
    
	private void showViewWithNoData() {
		Log.e(TAG, "showViewWithNoData");
		// 隐藏列表
		mListView.setVisibility(View.GONE);
		// 显示无数据画面
		mTvNoData.setVisibility(View.VISIBLE);
	}
	
	private void showViewWithData() {
		Log.e(TAG, "showViewWithData");
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
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mLoading.setVisibility(View.VISIBLE);
		}

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
			try {
				List<Product> tempList = app.mApi.getSerachList(mKeyWord, mCategoryId, mBrandId, mSortMod);
				mDataList.addAll(tempList);
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
			// 加载的场合
			if (TaskResult.OK == result && mDataList != null
					&& mDataList.size() > 0) {
				// 显示画面
				showViewWithData();
			} else {
				if(TaskResult.FAILED == result && message != null && message.length() > 0){
					Log.d(TAG, "layout_no_data");
					app.showErrorWithToast(message);
				}else{
					Log.d(TAG, "layout_no_data");
				}
				showViewWithNoData();
			}
		}
	}
	
	
	
}


class ProductListAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private Context context;
	private List<Product> dataList;

	/**
	 * 商品列表适配器
	 * @param context
	 * @param datas
	 */
	public ProductListAdapter(Context context, List<Product> datas) {
		super();
		this.context = context;
		this.dataList = datas;
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
		Product product = dataList.get(position);
		holder.title.setText(product.getName());
		holder.desc.setText(product.getDesc());
		holder.price.setText(product.getPrice());
		String src = product.getImgSrc();
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
