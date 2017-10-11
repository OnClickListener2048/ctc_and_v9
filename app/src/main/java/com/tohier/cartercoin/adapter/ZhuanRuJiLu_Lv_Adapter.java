package com.tohier.cartercoin.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

public class ZhuanRuJiLu_Lv_Adapter extends BaseAdapter {

	private  List<ZhuanChuJiLuData> datas = new ArrayList<ZhuanChuJiLuData>();
    private Context ctx;

	public ZhuanRuJiLu_Lv_Adapter(List<ZhuanChuJiLuData> datas, Context ctx) {
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
			convertView = View.inflate(ctx, R.layout.activity_zhuanrujilu_lv_item, null);
		}
			TextView tv_id = (TextView) convertView.findViewById(R.id.tv_id);
			TextView tv_actual_pay_count = (TextView) convertView.findViewById(R.id.tv_actual_pay_count);
			TextView tv_actual_arrival_count = (TextView) convertView.findViewById(R.id.tv_actual_arrival_count);
			TextView tv_commit_count = (TextView) convertView.findViewById(R.id.tv_commit_count);
			TextView tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			TextView tv_state = (TextView) convertView.findViewById(R.id.tv_state);




//			TextView tv_xuniba_count = (TextView) convertView.findViewById(R.id.tv_xuniba_count);
//			TextView tv_time = (TextView) convertView.findViewById(R.id.tv_time);
//
//			TextView tv_xunibi_actual_zhuanchu_count = (TextView) convertView.findViewById(R.id.tv_xunibi_actual_zhuanchu_count);
//			TextView tv_xuniba_shouxufei = (TextView) convertView.findViewById(R.id.tv_xuniba_shouxufei);
//
//			String status = datas.get(position).getStatus();
//			tv_zhuanchudizhi.setText(datas.get(position).getAddress());
//			tv_zhuanchuresult.setText(status);
//			tv_xuniba_count.setText(datas.get(position).getQty());
//
//		      if(!TextUtils.isEmpty(datas.get(position).getQty()))
//			  {
//				  if(ctcoutfee!=0)
//				  {
//					  tv_xuniba_shouxufei.setText(Float.parseFloat(datas.get(position).getQty())*ctcoutfee+"");
//					  tv_xunibi_actual_zhuanchu_count.setText(Float.parseFloat(datas.get(position).getQty())-Float.parseFloat(datas.get(position).getQty())*ctcoutfee+"");
//				  }else
//				  {
//					  tv_xuniba_shouxufei.setText(ctcoutfee*100+"%");
//					  tv_xunibi_actual_zhuanchu_count.setText(datas.get(position).getQty());
//				  }
//			  }
//
//			tv_time.setText(datas.get(position).getCreatedate());
//
//	     	tv_zhuanchuresult.setOnClickListener(new MyClickListener(datas.get(position).getId(),position,status));
//
//			if(TextUtils.equals("完成",status))
//			{
//				tv_zhuanchuresult.setTextColor(0xff00ff00);
//				tv_xuniba_count.setTextColor(0xff00ff00);
//			}else if(TextUtils.equals("等待",status))
//			{
//				tv_zhuanchuresult.setTextColor(0xffff9933);
//				tv_xuniba_count.setTextColor(0xffff9933);
//			}else if(TextUtils.equals("取消",status))
//			{
//				tv_zhuanchuresult.setText("驳回");
//				tv_zhuanchuresult.setTextColor(0xffff0000);
//				tv_xuniba_count.setTextColor(0xffff0000);
//
//			}else if(TextUtils.equals("驳回",status))
//			{
//				tv_zhuanchuresult.setTextColor(0xffff0000);
//				tv_xuniba_count.setTextColor(0xffff0000);
//			}
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
		public void onClick(View v) {
			if (v.getId() == R.id.tv_zhuanchuresult){
				if(TextUtils.equals("等待",status))
				{
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
									}
								});

							} else {

								((ZhuanChuJiLuActivity) ctx).runOnUiThread(new Runnable() {
									@Override
									public void run() {
										((ZhuanChuJiLuActivity)ctx).sToast("取消失败");
										datas.remove(position);
										notifyDataSetChanged();
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
								}
							});
						}
					});
				}

			}
		}
	}

}
