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
import com.tohier.cartercoin.activity.BankListActivity;
import com.tohier.cartercoin.bean.CardData;
import com.tohier.cartercoin.config.HttpConnect;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class BankListAdapter extends BaseAdapter{
	ArrayList<CardData> list = new ArrayList<>();
	IContext icontext;
	Context context;

	public ArrayList<CardData> getList() {
		return list;
	}

	public void setList(ArrayList<CardData> list) {
		this.list = list;
	}

	public BankListAdapter(ArrayList<CardData> list, Context context,
						   IContext icontext) {
		super();
		this.list = list;
		this.context = context;
		this.icontext = icontext;
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
			convertView = View.inflate(context, R.layout.item_banklist, null);
		}
		TextView tv_banktype = (TextView) convertView
				.findViewById(R.id.tv_banktype);
		TextView tv_province = (TextView) convertView
				.findViewById(R.id.tv_province);
		TextView tv_account = (TextView) convertView
				.findViewById(R.id.tv_account);
		TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
		ImageView iv_delete = (ImageView) convertView
				.findViewById(R.id.iv_delete);

		ImageView iv_bank_icon = (ImageView) convertView
				.findViewById(R.id.iv_bank_icon);

		Glide.with(context).load(list.get(position).getIconUrl()).placeholder(null)
				.error(null).into( iv_bank_icon);

		tv_banktype.setText(list.get(position).getBanktype());
		
		iv_delete.setTag(list.get(position).getAccount());
		
		int length = list.get(position).getAccount().length();
		if(length==16)
		{
			String end = list.get(position).getAccount().substring(12, list.get(position).getAccount().length());

			tv_account.setText("卡    号:   * * * *  * * * *  * * * *  " +end);
		}else if(length==19)
		{
			String end = list.get(position).getAccount().substring(15, list.get(position).getAccount().length());
			tv_account.setText("卡    号:   * * * *  * * * *  * * * *  * * *  " +end);
		}else
		{
			tv_account.setText("卡    号:   "+list.get(position).getAccount() );
		}
		tv_name.setText("户    名:   " + list.get(position).getName());
		tv_province.setText("开户行:   " + list.get(position).getProvince()+"   "+list.get(position).getBankname());
		
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
				HttpConnect.post(icontext, "member_bank_delete", par,
						new Callback() {

							@Override
							public void onResponse(Response arg0)
									throws IOException {
								JSONObject jsonObject = JSONObject.fromObject(arg0
										.body().string());

								if (jsonObject.getString("status")
										.equals("success")) {

									((BankListActivity)context).runOnUiThread(new Runnable() {

										@Override
										public void run() {
											list.remove(position);
											BankListAdapter.this.notifyDataSetChanged();
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
