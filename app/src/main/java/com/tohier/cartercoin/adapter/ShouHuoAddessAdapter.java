package com.tohier.cartercoin.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.android.config.IContext;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.activity.AddShouHuoAddressActivity;
import com.tohier.cartercoin.activity.ShouHuoAddressActivity;
import com.tohier.cartercoin.bean.Address;
import com.tohier.cartercoin.config.HttpConnect;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ShouHuoAddessAdapter extends BaseAdapter implements OnClickListener{
	ArrayList<Address> list;
	IContext icontext;
	Context context;
	private int position;
	private SharedPreferences sp;
	private ArrayList<String> arrayList = new ArrayList<String>();



	public ArrayList<Address> getList() {
		return list;
	}

	public void setList(ArrayList<Address> list) {
		this.list = list;
	}

	public ShouHuoAddessAdapter(ArrayList<Address> list, Context context,
								IContext icontext,SharedPreferences sp) {
		super();
		this.list = list;
		this.context = context;
		this.icontext = icontext;
		this.sp = sp;
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
			convertView = View.inflate(context, R.layout.item_shouhuoaddress, null);
		}
		TextView tv_shouhuoName = (TextView) convertView
				.findViewById(R.id.tv_shouhuoName);
		TextView tv_shouhuoPhone = (TextView) convertView
				.findViewById(R.id.tv_shouhuoPhone);
		TextView tv_shouhuoAddress = (TextView) convertView
				.findViewById(R.id.tv_shouhuoAddress);
		ImageView iv_delete = (ImageView) convertView
				.findViewById(R.id.iv_del);
		ImageView iv_edit = (ImageView) convertView
				.findViewById(R.id.iv_edit);
		ImageView iv_setting_default = (ImageView) convertView
				.findViewById(R.id.iv_setting_default);

		LinearLayout linear_set_default  = (LinearLayout) convertView
				.findViewById(R.id.linear_set_default);

		LinearLayout linear_edit  = (LinearLayout) convertView
				.findViewById(R.id.linear_edit);

		LinearLayout linear_del  = (LinearLayout) convertView
				.findViewById(R.id.linear_del);

		tv_shouhuoName.setText("收货姓名: "+list.get(position).getConsignee());
		tv_shouhuoPhone.setText("    手机号: " + list.get(position).getMobile());
		tv_shouhuoAddress.setText("收货地址: "+list.get(position).getAddress());

		if(list.get(position).getDefault_address().equals("True"))
		{
			iv_setting_default.setImageResource(R.mipmap.checked);
		}else
		{
			iv_setting_default.setImageResource(R.mipmap.unchecked);
		}

		linear_set_default.setTag(list.get(position).getId());
		linear_set_default.setOnClickListener(this);
		linear_edit.setTag(position);
		linear_edit.setOnClickListener(this);
		linear_del.setTag(position);
		linear_del.setOnClickListener(this);
		return convertView;
	}

	@Override
	public void onClick(final View v) {
		 switch (v.getId())
		 {
			 case R.id.linear_del:
				 final int position =  (Integer) v.getTag();
				 Map<String, String> par = new HashMap<String, String>();

				 par.put("id", list.get(position).getId());
				 HttpConnect.post(icontext, "member_address_delete", par,
						 new Callback() {

							 @Override
							 public void onResponse(Response arg0)
									 throws IOException {
								 JSONObject jsonObject = JSONObject.fromObject(arg0
										 .body().string());

								 if (jsonObject.getString("status")
										 .equals("success")) {
									 list.remove(position);

									 ((ShouHuoAddressActivity)context).runOnUiThread(new Runnable() {

										 @Override
										 public void run() {
											 ShouHuoAddessAdapter.this.notifyDataSetChanged();
											 arrayList.clear();
											 for (Address address : list){
												 arrayList.add(address.getId());
											 }

											 if (!arrayList.contains(sp.getString("id",""))){
												 sp.edit().putString("name1","")
														 .putString("id","")
														 .putString("phone","")
														 .putString("address","").commit();
											 }
										 }
									 });
								 }
							 }

							 @Override
							 public void onFailure(Request arg0, IOException arg1) {
								 // TODO Auto-generated method stub

							 }
						 });
				 break;
			 case R.id.linear_edit:
				 Intent intent = new Intent(context,AddShouHuoAddressActivity.class);
				 intent.putExtra("update","yes");
				 intent.putExtra("id", list.get((Integer) v.getTag()).getId());
				 intent.putExtra("name", list.get((Integer) v.getTag()).getConsignee());

				 intent.putExtra("province", list.get((Integer) v.getTag()).getProvince());
				 intent.putExtra("city", list.get((Integer) v.getTag()).getCity());

				 intent.putExtra("mobile", list.get((Integer) v.getTag()).getMobile());

				 intent.putExtra("address", list.get((Integer) v.getTag()).getAddress());
				 context.startActivity(intent);
				 break;
			 case R.id.linear_set_default:
				 Map<String, String> param = new HashMap<String, String>();

				 param.put("id",  v.getTag().toString().trim());
				 HttpConnect.post(icontext, "member_address_default", param,
						 new Callback() {

							 @Override
							 public void onResponse(Response arg0)
									 throws IOException {
								 JSONObject jsonObject = JSONObject.fromObject(arg0
										 .body().string());

								 if (jsonObject.getString("status")
										 .equals("success")) {

									 Handler dataHandler = new Handler(context
											 .getMainLooper()) {

										 @Override
										 public void handleMessage(final Message msg) {
											 ((ShouHuoAddressActivity)context).getJsonData();
										 }
									 };
									 dataHandler.sendEmptyMessage(0);
								 }
							 }

							 @Override
							 public void onFailure(Request arg0, IOException arg1) {
								 // TODO Auto-generated method stub
								 Handler dataHandler = new Handler(
										 context.getMainLooper()) {

									 @Override
									 public void handleMessage(
											 final Message msg) {
//										 ((ShouHuoAddressActivity)context).sToast("链接超时！");
									 }
								 };
								 dataHandler.sendEmptyMessage(0);

							 }
						 });
				 break;
		 }

	}
}
