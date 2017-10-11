package com.tohier.cartercoin.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.activity.TiXianJiLuActivity;
import com.tohier.cartercoin.bean.TiXianData;
import com.tohier.cartercoin.config.HttpConnect;
import net.sf.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TiXianJiLuAdapter extends BaseAdapter{
	Context context;
	ArrayList<TiXianData> list;
	
	
	
	public TiXianJiLuAdapter(Context context, ArrayList<TiXianData> list) {
		super();
		this.context = context;
		this.list = list;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_tixianjilu, null);
			holder = new ViewHolder();
			holder.tv_rmb = (TextView) convertView.findViewById(R.id.tv_jilu_rmb);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_jilu_time);
			holder.tv_state = (TextView) convertView.findViewById(R.id.tv_jilu_state);
			holder.tv_money_up = (TextView) convertView.findViewById(R.id.tv_jilu_money_up);
			holder.tv_money_down = (TextView) convertView.findViewById(R.id.tv_jilu_money_down);
			holder.tv_shouxufei_up = (TextView) convertView.findViewById(R.id.tv_jilu_shouxufei_up);
			holder.tv_shouxufei_down = (TextView) convertView.findViewById(R.id.tv_jilu_shouxufei_down);
			holder.tv_bank_up = (TextView) convertView.findViewById(R.id.tv_jilu_bank_up);
			holder.tv_bank_down = (TextView) convertView.findViewById(R.id.tv_jilu_bank_down);
			holder.iv_time = (ImageView) convertView.findViewById(R.id.iv_jilu_time);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tv_rmb.setText("人民币");
		holder.tv_time.setText(list.get(position).getCreatedate());

		if(!TextUtils.isEmpty(list.get(position).getStatus()))
		{
			if(list.get(position).getStatus().equals("等待"))
			{
				holder.tv_state.setText("取消");
			}

			if(list.get(position).getStatus().equals("等待"))
			{
				holder.tv_state.setOnClickListener(new MyClickListener(list.get(position).getCashoutid(),position));
			}
		}


		holder.tv_money_up.setText("提现金额");
		
		holder.tv_money_down.setText(list.get(position).getQty());
		
		holder.tv_shouxufei_up.setText("手续费");
		
		holder.tv_shouxufei_down.setText(list.get(position).getCounterfee());
		
		holder.tv_bank_up.setText("提现银行");
		holder.tv_bank_down.setText(list.get(position).getBankcard());
		holder.iv_time.setImageResource(R.mipmap.iv_time);
		return convertView;
	}

	public final class ViewHolder {
		public TextView tv_rmb;
		public TextView tv_time;
		public ImageView iv_time;
		public TextView tv_state;
		public TextView tv_money_up;
		public TextView tv_money_down;
		public TextView tv_shouxufei_up;
		public TextView tv_shouxufei_down;
		public TextView tv_bank_up;
		public TextView tv_bank_down;
	}

	class MyClickListener implements View.OnClickListener {
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
			HttpConnect.post(((TiXianJiLuActivity)context), "member_cash_out_delete", par,
					new Callback() {

						@Override
						public void onResponse(Response arg0)
								throws IOException {
							JSONObject jsonObject = JSONObject.fromObject(arg0
									.body().string());

							if (jsonObject.getString("status")
									.equals("success")) {

								((TiXianJiLuActivity)context).runOnUiThread(new Runnable() {

									@Override
									public void run() {
										list.remove(position);
										TiXianJiLuAdapter.this.notifyDataSetChanged();
										v.setClickable(true);
									}
								});
							}
						}

						@Override
						public void onFailure(Request arg0, IOException arg1) {
							// TODO Auto-generated method stub
							((TiXianJiLuActivity)context).runOnUiThread(new Runnable() {

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
