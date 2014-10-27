package com.brains.app.shopclient.activities.fragment;

import com.brains.app.shopclient.R;
import com.brains.app.shopclient.activities.LoginActivity;
import com.brains.app.shopclient.common.Util;

import android.support.v4.app.Fragment;
import android.content.Context;
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
import android.widget.Toast;

/**
 * 我的：个人中心
 * @author xiect
 *
 */
public class MyFragment extends BaseFragment {
	private final int REQUEST_CODE_LOGIN = 1;
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
		// 已登录判断
		if(app.isLogin()){
			mHeader4LoginArea.setVisibility(View.VISIBLE);
			mHeader4NotLoginArea.setVisibility(View.GONE);
			mTvUserName.setText(app.mPrefDAO.getUserName());
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
			getActivity().startActivityForResult(intent, REQUEST_CODE_LOGIN);
		}
	};
	
	private void initView() {
		
		btnLogin = (Button) this.fragmentView.findViewById(R.id.personal_click_for_login);
		btnLogin.setOnClickListener(btnLoginListener);
		
		menuList = (ListView)this.fragmentView.findViewById(R.id.menu_list_view);
		String[] menuTitles = new String[]{
				getString(R.string.person_menu_query_order),
				getString(R.string.person_menu_account),
				getString(R.string.person_menu_about),
				getString(R.string.person_menu_exit),
		};

		int[] menuIcons = new int[]{
				R.drawable.person_order,
				R.drawable.person_account_center,
				R.drawable.person_about_icon,
				R.drawable.person_exit_icon
		};

		PersonMenuAdapter adapter = new PersonMenuAdapter(getActivity() ,menuTitles ,menuIcons);

		menuList.setAdapter(adapter);
		menuList.setCacheColorHint(Color.TRANSPARENT);
		menuList.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			private static final int MENU_ORDER = 0;   // 订单查询
			private static final int MENU_ACCOUNT = 1; // 账号管理
			private static final int MENU_ABOUT   = 2; // 关于
			private static final int MENU_EXIT    = 3; // 退出

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {

				switch(position){
				case MENU_ORDER:
					// 设置
					// TODO 

					break;
				case MENU_ACCOUNT:
					// 账号管理
					break;
				case MENU_ABOUT:
					// 关于
					break;
				case MENU_EXIT:
					// 退出
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
		Util.sysLog("xxx", "=========onActivityResult:" + resultCode);
	}

	@Override
	protected void lazyLoad() {
		// TODO Auto-generated method stub
		
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
