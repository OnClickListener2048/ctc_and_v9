package com.tohier.cartercoin.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.activity.ZhuanChuJiLuActivity;
import com.tohier.cartercoin.bean.ZhuanChuJiLuData;
import com.tohier.cartercoin.config.HttpConnect;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ZhuanChuJiLu_Lv_Adapter extends BaseAdapter {

	private  List<ZhuanChuJiLuData> datas = new ArrayList<ZhuanChuJiLuData>();
	private Context ctx;

	public ZhuanChuJiLu_Lv_Adapter(List<ZhuanChuJiLuData> datas, Context ctx) {
		super();
		this.datas = datas;
		this.ctx = ctx;
	}


	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null)
		{
			convertView = View.inflate(ctx, R.layout.activity_zhunchujilu_lv_item, null);
		}
		TextView tv_zhuanchudizhi = (TextView) convertView.findViewById(R.id.tv_zhuanchudizhi);
		TextView tv_zhuanchuresult = (TextView) convertView.findViewById(R.id.tv_zhuanchuresult);
		TextView tv_xuniba_count = (TextView) convertView.findViewById(R.id.tv_xuniba_count);
		TextView tv_time = (TextView) convertView.findViewById(R.id.tv_time);
		TextView tv_type = (TextView) convertView.findViewById(R.id.tv_type);

//			TextView tv_xunibi_actual_zhuanchu_count = (TextView) convertView.findViewById(R.id.tv_xunibi_actual_zhuanchu_count);
		TextView tv_xuniba_shouxufei = (TextView) convertView.findViewById(R.id.tv_xuniba_shouxufei);
		LinearLayout linearLayout_address = (LinearLayout) convertView.findViewById(R.id.linearLayout_address);
		TextView tv_count_tab = (TextView) convertView.findViewById(R.id.tv_count_tab);

		String status = datas.get(position).getStatus();
		if(!TextUtils.isEmpty(datas.get(position).getAddress()))
		{
			linearLayout_address.setVisibility(View.VISIBLE);
			tv_zhuanchudizhi.setText(datas.get(position).getAddress());
		}else
		{
			linearLayout_address.setVisibility(View.GONE);
		}

		tv_zhuanchuresult.setText(status);
		tv_xuniba_count.setText(datas.get(position).getQty());

		if(!TextUtils.isEmpty(datas.get(position).getFree()))
		{
			tv_xuniba_shouxufei.setText(datas.get(position).getFree());
		}else
		{
			tv_xuniba_shouxufei.setText("0.0000");
			tv_count_tab.setText("转入数量");
		}

		tv_time.setText(datas.get(position).getCreatedate());
		tv_type.setText(datas.get(position).getType());

		return convertView;
	}


	class MyClickListener implements View.OnClickListener {
		private String id;
		private int position;
		private String status;

		public MyClickListener(String id, int position,String status) {
			this.id = id;
			this.position = position;
			this.status = status;
		}

		@Override
		public void onClick(final View v) {
			if (v.getId() == R.id.tv_zhuanchuresult){
				if(TextUtils.equals("等待",status))
				{
					v.setClickable(false);
					HashMap<String,String> map = new HashMap<String,String>();
					map.put("id",id);
					HttpConnect.post(((ZhuanChuJiLuActivity)ctx), "member_ctc_out_delete", map, new Callback() {

						@Override
						public void onResponse(Response arg0) throws IOException {
							JSONObject jsonStr = JSONObject.fromObject(arg0.body().string());
							if (jsonStr.get("status").equals("success")) {
								((ZhuanChuJiLuActivity) ctx).runOnUiThread(new Runnable() {
									@Override
									public void run() {
										((ZhuanChuJiLuActivity)ctx).sToast("取消成功");
										datas.remove(position);
										notifyDataSetChanged();
										v.setClickable(true);
									}
								});

							} else {

								((ZhuanChuJiLuActivity) ctx).runOnUiThread(new Runnable() {
									@Override
									public void run() {
										((ZhuanChuJiLuActivity)ctx).sToast("取消失败");
										datas.remove(position);
										notifyDataSetChanged();
										v.setClickable(true);
									}
								});

							}

						}

						@Override
						public void onFailure(Request arg0, IOException arg1) {
							((ZhuanChuJiLuActivity) ctx).runOnUiThread(new Runnable() {
								@Override
								public void run() {
//									((ZhuanChuJiLuActivity)ctx).sToast("链接超时！");
									datas.remove(position);
									notifyDataSetChanged();
									v.setClickable(true);
								}
							});
						}
					});
				}

			}
		}
	}

}
