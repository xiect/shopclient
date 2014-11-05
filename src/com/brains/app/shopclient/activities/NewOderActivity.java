package com.brains.app.shopclient.activities;

import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.brains.app.shopclient.R;
import com.brains.app.shopclient.common.Util;
import com.brains.app.shopclient.db.dao.PrefDAO;
import com.brains.app.shopclient.db.entity.Product;
import com.brains.framework.activities.base.BaseNormalActivity;
import com.brains.framework.exception.AppException;
import com.brains.framework.task.GenericTask;
import com.brains.framework.task.TaskParams;
import com.brains.framework.task.TaskResult;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * 新建订单画面
 * @author xiect
 *
 */
public class NewOderActivity extends BaseNormalActivity{
	private final static String ACTION = "brains.intent.action.ACTION_NEWORDER";
	private final static int REQUEST_RECEIVER = 9;
	public static final String TAG = "NewOderActivity";
	private TextView tvTitle;
	private RelativeLayout receiverEmptyLayout;
	private RelativeLayout receiverLayout;
	private PrefDAO prefDAO;
	private TextView tvName;
	private TextView tvTel;
	private TextView tvAdd;
	private ListView mListView;
	private List<Product> mListData;
	private ItemListAdapter mAdapter;
	private TextView tvMoney;
	private Button btnOrder;
	private GenericTask mOderTask;
	private RelativeLayout mProgressBarArea;
	
	
	/**
	 * 取得新建订单画面Intent
	 * 
	 * @return
	 */
	public static Intent makeIntent() {
		return new Intent().setAction(ACTION);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_new_order);
		// 初始化pref访问对象
		prefDAO = new PrefDAO(this);
		String name = prefDAO.getReceiverName();
		
		// 绑定视图及绑定事件
		findView();
		
		if(Util.isEmpty(name)){
			// 不存在配送信息的场合
			receiverEmptyLayout.setVisibility(View.VISIBLE);
			receiverLayout.setVisibility(View.GONE);
		}else{
			showReceiverInfo();
		}
		
		// 从数据库Cart 中获取一选择的商品
		// 业务需要：只能一件商品一单
		mListData = app.cart.getSelectedProducts();
		int selectedNum = mListData.size();
		if(selectedNum != 1){
			String msg = getResources().getString(R.string.msg_product_selected_one_please);
			msg = String.format(msg, selectedNum);
			app.showErrorWithToast(msg);
			finish();
		}
		// 显示订单商品一览
		mAdapter = new ItemListAdapter(this,mListData);
		mListView.setAdapter(mAdapter);
		float totalPay = 0f;
		for(Product item:mListData){
			totalPay += item.getNum() * Util.conver2Price(item.getPrice());
		}
		tvMoney.setText(Util.formatRmb(String.valueOf(totalPay)));
	}
	
	private void findView(){
		mProgressBarArea = (RelativeLayout) findViewById(R.id.rl_loading_area);
		btnOrder = (Button) findViewById(R.id.submit_order);
		tvMoney = (TextView) findViewById(R.id.fill_order_money_info);
		tvName = (TextView) findViewById(R.id.textview_receiver_name_content);
		tvTel = (TextView) findViewById(R.id.textview_receiver_mobile_content);
		tvAdd = (TextView) findViewById(R.id.textview_receiver_address_content);
		// 标题 订单
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvTitle.setText(R.string.page_title_order);
		receiverEmptyLayout = (RelativeLayout) findViewById(R.id.layout_receiver_empty); 
		receiverLayout = (RelativeLayout) findViewById(R.id.layout_receiver_info);
		
		// 订单商品一览
		mListView = (ListView) findViewById(R.id.shopping_item_list);
		
		// 事件处理
		receiverEmptyLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doEditReceiverInfo();
			}
		});
		
		receiverLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doEditReceiverInfo();
			}
		});
		
		btnOrder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(doInpuCheck()){
					// check ok
					doOder();
				}
			}
		});
	}
	
	/**
	 * 订单入力校验
	 * @return
	 */
	private boolean doInpuCheck(){
		boolean isOK = true;
		String name = tvName.getText().toString();
		String tel = tvTel.getText().toString();
		
		if(Util.isEmpty(name) && Util.isEmpty(tel)){
			app.showMsgWithToast(R.string.msg_please_input_receiver_info);
			isOK = false;
		}
		return isOK;
	}
	
	/**
	 * receiver 编辑画面迁移
	 */
	private void doEditReceiverInfo(){
		// 编辑画面迁移
		Util.sysLog(TAG, "receiver 编辑画面迁移");
		startActivityForResult(ReceiverEditActivity.makeIntent(), REQUEST_RECEIVER);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(REQUEST_RECEIVER == requestCode && RESULT_OK == resultCode){
			showReceiverInfo();
		}
	}
	
	/**
	 * 下单成功对话框
	 * @param msg
	 * @param ctx
	 */
	private void showOderSuccessAlert(String msg,Context ctx) {
		AlertDialog.Builder builder = new Builder(ctx);
		builder.setTitle("提示")
			.setMessage(msg)
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					// 画面关闭 回到Cart画面
					finish();
				}
			});
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	/**
	 * 显示有数据的收件人信息
	 */
	private void showReceiverInfo(){
		// 获取收件人信息
		String name = prefDAO.getReceiverName();
		String tel = prefDAO.getReceiverTel();
		String add = prefDAO.getReceiverAdd();
		String zipCode = prefDAO.getReceiverZipCode();
		
		tvName.setText(name);
		tvTel.setText(tel);
		tvAdd.setText(add);
		
		receiverEmptyLayout.setVisibility(View.GONE);
		receiverLayout.setVisibility(View.VISIBLE);
	}

	
	/**
	 * 下单
	 * @param name
	 * @param pwd
	 */
	private void doOder(){
		if (mOderTask != null
				&& mOderTask.getStatus() == GenericTask.Status.RUNNING) {
			return;
		} else {
			mOderTask = new DoOrderTask();
			mOderTask.execute();
		}
	}
	
	private class DoOrderTask extends GenericTask {
		String message = null;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressBarArea.setVisibility(View.VISIBLE);
		}

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
			try {
				// 获得网络数据
				String name = tvName.getText().toString();
				String tel = tvTel.getText().toString();
				String add = tvAdd.getText().toString();
				Product item = mListData.get(0);
				String itemId = "";
				int num = 0;
				if(item != null){
					itemId = item.getItemId();
					num = item.getNum();
				}
				// 下单
				app.mApi.doOder(name,tel,add,itemId,num);
				// 下单后将该商品从购物车中移除
				app.cart.deleteProduct(item,NewOderActivity.this);
				return TaskResult.OK;
			} catch (AppException e) {
				message = e.getMessage();
				e.printStackTrace();
			}
			return TaskResult.FAILED;
		}

		@Override
		protected void onPostExecute(TaskResult result) {
			super.onPostExecute(result);
			mProgressBarArea.setVisibility(View.GONE);
			// 加载的场合
			if (TaskResult.OK == result) {
				// 下单成功的场合提示
//				app.showMsgWithToast(R.string.order_success);
				showOderSuccessAlert(getResources().getString(R.string.order_success),context);
			} else {
				if(TaskResult.FAILED == result && message != null && message.length() > 0){
					Log.d(TAG, "layout_no_data");
					app.showErrorWithToast(message);
				}else{
					Log.d(TAG, "layout_no_data");
					app.showErrorWithToast("这位客官遇到点小麻烦 下单未能成功!");
				}
			}
		}
	}
}


class ItemListAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private Context context;
	private List<Product> dataList;
	private boolean isEdit;
	
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
	public ItemListAdapter(Context context,List<Product> list) {
		super();
		this.context = context;
		this.dataList = list;
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
		holder.check.setVisibility(View.GONE);
		
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
