package com.tohier.cartercoin.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.android.config.IContext;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.activity.AlipayListActivity;
import com.tohier.cartercoin.activity.BankListActivity;
import com.tohier.cartercoin.bean.AlipayData;
import com.tohier.cartercoin.bean.CardData;
import com.tohier.cartercoin.config.HttpConnect;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AlipayListAdapter extends BaseAdapter{
	List<AlipayData> list = new ArrayList<>();
	Context context;

	public List<AlipayData> getList() {
		return list;
	}

	public void setList(List<AlipayData> list) {
		this.list = list;
	}

	public AlipayListAdapter(List<AlipayData> list, Context context) {
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
			convertView = View.inflate(context, R.layout.item_alipaylist, null);
		}
		TextView tv_account = (TextView) convertView
				.findViewById(R.id.tv_account);
		TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
		ImageView iv_delete = (ImageView) convertView
				.findViewById(R.id.iv_delete);
			tv_account.setText("账    号:   "+list.get(position).getAccount());
		tv_name.setText("户    名:   " + list.get(position).getName());
		iv_delete.setOnClickListener(new MyClickListener(list.get(position).getId(),position));
		return convertView;
	}


	class MyClickListener implements OnClickListener {
		private String id;
		private int position;

		public MyClickListener(String id,int position) {
			this.id = id;
			this.position = position;
		}

		@Override
		public void onClick(final View v) {
			    v.setClickable(false);
				Map<String, String> par = new HashMap<String, String>();
				par.put("id", id);
				HttpConnect.post(((AlipayListActivity) context), "member_bank_delete", par,
						new Callback() {

							@Override
							public void onResponse(Response arg0)
									throws IOException {
								JSONObject jsonObject = JSONObject.fromObject(arg0
										.body().string());

								if (jsonObject.getString("status")
										.equals("success")) {

									((AlipayListActivity)context).runOnUiThread(new Runnable() {

										@Override
										public void run() {
											list.remove(position);
											AlipayListAdapter.this.notifyDataSetChanged();
											v.setClickable(true);
										}
									});
								}
							}

							@Override
							public void onFailure(Request arg0, IOException arg1) {
								// TODO Auto-generated method stub
								((BankListActivity)context).runOnUiThread(new Runnable() {

									@Override
									public void run() {
										v.setClickable(true);
									}
								});
							}
						});
			}
		}
}
