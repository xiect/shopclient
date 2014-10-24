package com.brains.app.shopclient.activities.fragment;

import com.brains.app.shopclient.R;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 我的：个人中心
 * @author xiect
 *
 */
public class MyFragment extends Fragment {
	private ListView menuList;
	private View view;
	private final String TAG = "MyFragment";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_main_my, container,false);
		TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
		tvTitle.setText(R.string.tab_my);

		initView();
		return view;
	}

	private void initView() {
		menuList = (ListView)this.view.findViewById(R.id.menu_list_view);
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
