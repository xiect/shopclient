package com.brains.app.shopclient.activities.fragment;

import com.brains.app.shopclient.R;
import com.brains.app.shopclient.activities.AboutActivity;
import com.brains.app.shopclient.activities.LoginActivity;
import com.brains.app.shopclient.activities.OrderSearchActivity;
import com.brains.app.shopclient.common.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 我的：个人中心
 * @author xiect
 *
 */
public class MyFragment extends BaseFragment {
	private final int REQUEST_CODE_LOGIN = 1;
	private final int REQUEST_CODE_4_ORDER = 2;
	private ListView menuList;
	private final String TAG = "MyFragment";
	private Button btnLogin;
	
	private RelativeLayout mHeader4LoginArea;
	private RelativeLayout mHeader4NotLoginArea;
	private TextView mTvUserName;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		fragmentView = inflater.inflate(R.layout.fragment_main_my, container,false);
		findViews();
		initView();
		return fragmentView;
	}

	private void findViews(){
		TextView tvTitle = (TextView) fragmentView.findViewById(R.id.tv_title);
		tvTitle.setText(R.string.tab_my);
		mHeader4LoginArea = (RelativeLayout)fragmentView.findViewById(R.id.personal_for_login_info);
		mHeader4NotLoginArea = (RelativeLayout)fragmentView.findViewById(R.id.personal_for_not_login);
		mTvUserName = (TextView)fragmentView.findViewById(R.id.who_and_say_hello);
		showTopHeaderView();
	}
	
	/**
	 * 显示顶部登录区域
	 */
	private void showTopHeaderView(){
		// 已登录判断
		if(app.isLogin()){
			mHeader4LoginArea.setVisibility(View.VISIBLE);
			mHeader4NotLoginArea.setVisibility(View.GONE);
			mTvUserName.setText(app.mPrefDAO.getNikeName());
		}else{
			mHeader4LoginArea.setVisibility(View.GONE);
			mHeader4NotLoginArea.setVisibility(View.VISIBLE);
		}
	}
	
	private OnClickListener btnLoginListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			Log.d(TAG,"btnLogin clicked");
			Intent intent = LoginActivity.makeIntent();
			startActivityForResult(intent, REQUEST_CODE_LOGIN);
//			overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out); 
		}
	};
	
	/**
	 * 确认对话框
	 * @param msg
	 * @param ctx
	 */
	private void showConfirmAlert(String msg,Context ctx) {
		AlertDialog.Builder builder = new Builder(ctx);
		builder.setTitle("提示")
			.setMessage(msg)
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// 退出应用
					doExit();
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
	 * 订单查询
	 */
	private void doQueryOrder(){
		if(app.isLogin()){
			// already login,go to order center page 
			startActivity(OrderSearchActivity.makeIntent());
			getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
		}else{
			// not login ,go to login page
			startActivityForResult(LoginActivity.makeIntent(), REQUEST_CODE_4_ORDER);
		}
	}
	
	/**
	 * 退出操作
	 */
	private void menuExitClicked(){
		String msg = getResources().getString(R.string.msg_confirm_eixt_system);
		showConfirmAlert(msg,getActivity());
	}	
	
	/**
	 * 退出操作
	 */
	private void doExit(){
		getActivity().finish();
	}
	
	/**
	 * 注销操作
	 */
	private void doLogoff(){
		if(app.isLogin()){
			app.doLogOff();
			showTopHeaderView();
			app.showMsgWithToast(R.string.msg_logoff_success);
		}else{
			app.showMsgWithToast(R.string.msg_logoff_not_login);
		}
	}
	
	private void initView() {
		
		btnLogin = (Button) this.fragmentView.findViewById(R.id.personal_click_for_login);
		btnLogin.setOnClickListener(btnLoginListener);
		
		menuList = (ListView)this.fragmentView.findViewById(R.id.menu_list_view);
		String[] menuTitles = new String[]{
				getString(R.string.person_menu_query_order),
//				getString(R.string.person_menu_account),
				getString(R.string.person_menu_about),
				getString(R.string.person_menu_logoff),
				getString(R.string.person_menu_exit)
		};

		int[] menuIcons = new int[]{
				R.drawable.person_order,
//				R.drawable.person_account_center,
				R.drawable.person_account_center,
				R.drawable.person_about_icon,
				R.drawable.person_exit_icon
		};

		PersonMenuAdapter adapter = new PersonMenuAdapter(getActivity() ,menuTitles ,menuIcons);

		menuList.setAdapter(adapter);
		menuList.setCacheColorHint(Color.TRANSPARENT);
		menuList.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			private static final int MENU_ORDER = 0;   		// 订单查询
//			private static final int MENU_ACCOUNT = 1; 		// 账号管理
			private static final int MENU_ABOUT   = 1; 		// 关于
			private static final int MENU_LOGOFF    = 2; 	// 注销
			private static final int MENU_EXIT    = 3; 		// 退出

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				Util.sysLog(TAG, "position:" + position);
				switch(position){
				case MENU_ORDER:
					// 订单查询
					doQueryOrder();
					break;
//				case MENU_ACCOUNT:
//					// 账号管理
//					break;
				case MENU_ABOUT:
					// 关于
					getActivity().startActivity(AboutActivity.makeIntent());
					getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
					break;
				case MENU_LOGOFF:
					// 注销
					doLogoff();
					break;
				case MENU_EXIT:
					// 退出
					menuExitClicked();
					break;
				default:
					Log.d(TAG,"menu  position ng:" + position);
				}
			}
		} );
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Util.sysLog("xxx", "=========onActivityResult: requestCode:" +requestCode +"\t result code:"+ resultCode);
		if(requestCode == REQUEST_CODE_LOGIN && resultCode == Activity.RESULT_OK){
			// 登录成功的场合
			// 隐藏登录按钮 显示 用户名称
			showTopHeaderView();
		}else if(requestCode == REQUEST_CODE_4_ORDER && resultCode == Activity.RESULT_OK){
			// 登录成功的场合,goto order center 
			getActivity().startActivity(OrderSearchActivity.makeIntent());
			getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
		}
	}

	@Override
	protected void lazyLoad() {
		Util.sysLog(TAG, "lazyLoad");
		if(isVisible && isCreatedView){
			Util.sysLog(TAG, "判定是否登录显示登录区域");
			// 判定是否登录显示登录区域
			showTopHeaderView();			
		}
	}
}

class PersonMenuAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Context context;
	private String[] data;
	private int[] icons;
	private int e;

	public PersonMenuAdapter(Context context, String[] data, int[] icons) {
		super();
		this.context = context;
		this.data = data;
		this.icons = icons;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		e = data.length - 1;
	}

	@Override
	public int getCount() {
		return data.length;
	}

	@Override
	public Object getItem(int position) {
		return data[position];
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {

			convertView = inflater.inflate(R.layout.menulist_item, null);
			holder = new ViewHolder();
			holder.menuText = (TextView) convertView
					.findViewById(R.id.menu_text);
			holder.menuIcon = (ImageView) convertView
					.findViewById(R.id.menu_icon);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.menuText.setText(data[position]);
		holder.menuIcon.setImageResource(icons[position]);

		if (position != e) {
			if (position == 0) {
				convertView.setBackgroundResource(R.drawable.circle_list_top);
			} else {
				convertView
						.setBackgroundResource(R.drawable.circle_list_middle);
			}
		} else {
			convertView.setBackgroundResource(R.drawable.circle_list_bottom);
		}
		return convertView;
	}

	class ViewHolder {
		private ImageView menuIcon;
		private TextView menuText;
	}
}
