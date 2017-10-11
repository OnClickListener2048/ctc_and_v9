package com.tohier.cartercoin.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.tohier.android.config.IContext;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.bean.Message;
import java.util.ArrayList;
import java.util.List;

public class MessageCenterAdapter extends BaseAdapter{
	List<Message> list = new ArrayList<>();
	Context context;

	public List<Message> getList() {
		return list;
	}

	public void setList(List<Message> list) {
		this.list = list;
	}

	public MessageCenterAdapter(List<Message> list, Context context) {
		super();
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub

		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.message_center_lv_item, null);
		}
		TextView tv_title = (TextView) convertView
				.findViewById(R.id.tv_title);
		TextView tv_time = (TextView) convertView
				.findViewById(R.id.tv_time);
		TextView tv_desc = (TextView) convertView
				.findViewById(R.id.tv_desc);

		if(!TextUtils.isEmpty(list.get(position).getTitle()))
		{
			tv_title.setText(list.get(position).getTitle());
		}

		if(!TextUtils.isEmpty(list.get(position).getTime()))
		{
			tv_time.setText(list.get(position).getTime());
		}

		if(!TextUtils.isEmpty(list.get(position).getDesc()))
		{
			tv_desc.setText(list.get(position).getDesc());
		}
		return convertView;
	}


//	class MyClickListener implements OnClickListener {
//		private String id;
//		private int position;
//
//		public MyClickListener(String id,int position) {
//			this.id = id;
//			this.position = position;
//		}
//
//		@Override
//		public void onClick(View v) {
//				Map<String, String> par = new HashMap<String, String>();
//				par.put("id", id);
//				HttpConnect.post(icontext, "member_bank_delete", par,
//						new Callback() {
//
//							@Override
//							public void onResponse(Response arg0)
//									throws IOException {
//								JSONObject jsonObject = JSONObject.fromObject(arg0
//										.body().string());
//
//								if (jsonObject.getString("status")
//										.equals("success")) {
//
//									((BankListActivity)context).runOnUiThread(new Runnable() {
//
//										@Override
//										public void run() {
//											list.remove(position);
//											MessageCenterAdapter.this.notifyDataSetChanged();
//										}
//									});
//								}
//							}
//
//							@Override
//							public void onFailure(Request arg0, IOException arg1) {
//								// TODO Auto-generated method stub
//
//							}
//						});
//			}
//		}
}
