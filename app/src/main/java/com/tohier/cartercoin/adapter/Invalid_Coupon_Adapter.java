package com.tohier.cartercoin.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tohier.cartercoin.R;
import com.tohier.cartercoin.bean.YouHuiQuan;

import java.util.ArrayList;
import java.util.List;

public class Invalid_Coupon_Adapter extends BaseAdapter {

	private List<YouHuiQuan> items = new ArrayList<YouHuiQuan>();
	private Context context;
	private String couponStatus = "未使用";

	public Invalid_Coupon_Adapter(List<YouHuiQuan> items, Context context) {
		super();
		this.items = items;
		this.context = context;
	}

	public void setCouponStatus(String couponStatus) {
		this.couponStatus = couponStatus;
	}

	public List<YouHuiQuan> getItems() {
		return items;
	}

	public void setItems(List<YouHuiQuan> items) {
		this.items = items;
	}


	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}


	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
//		ViewHolder viewHolder;

		if(convertView==null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.lv_invalid_coupon_item_layout, null);
		}
//		else
//		{
//			viewHolder = (ViewHolder) convertView.getTag();
//		}
		ImageView iv_new_coupon_mark = (ImageView) convertView.findViewById(R.id.iv_new_coupon_mark);
		TextView tv_coupon_money =  (TextView) convertView.findViewById(R.id.tv_coupon_money);
		TextView tv_usage_rules =  (TextView) convertView.findViewById(R.id.tv_usage_rules);
		TextView tv_coupon_name =  (TextView) convertView.findViewById(R.id.tv_coupon_name);
		TextView  tv_coupon_term =  (TextView) convertView.findViewById(R.id.tv_coupon_term);
		TextView  tv_use_coupon =  (TextView) convertView.findViewById(R.id.tv_use_coupon);
		TextView  tv_coupon_item_￥ =  (TextView) convertView.findViewById(R.id.tv_coupon_item_￥);
		TextView  tv_coupon_item_α =  (TextView) convertView.findViewById(R.id.tv_coupon_item_α);

		if(!TextUtils.isEmpty(couponStatus)&&couponStatus.equals("未使用"))
		{
				  if(items.get(position).getSta().equals("0"))
	             {
			           iv_new_coupon_mark.setVisibility(View.GONE);
		         }else
		         {
		               iv_new_coupon_mark.setVisibility(View.VISIBLE);
				 }

//			tv_use_coupon.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					((Activity)context).runOnUiThread(new Runnable() {
//						@Override
//						public void run() {
//							Intent intent = null;
//							if(!TextUtils.isEmpty(items.get(position).getStatus()))
//							{
//								if(items.get(position).getStatus().equals("0"))  //会员升级
//								{
//									intent = new Intent(context, VipUpgradeActivity.class);
//								}else if(items.get(position).getStatus().equals("1"))  //购买矿产包
//								{
//									intent = new Intent(context, BuyAssetsActivity.class);
//								}else if(items.get(position).getStatus().equals("2"))
//								{
//									intent = new Intent(context, MallActivity.class);
//								}
//							}
//							intent.putExtra("id",items.get(position).getId())
//									.putExtra("price",items.get(position).getMoney())
//									.putExtra("min",items.get(position).getMin());
//						}
//					});
//				}
//			});
		}else
		{
			iv_new_coupon_mark.setVisibility(View.GONE);
			if(items.get(position).getStatus().equals("1"))
			{
				tv_use_coupon.setText("已使用");
			}else if(items.get(position).getStatus().equals("2"))
			{
				tv_use_coupon.setText("已过期");
			}
		}

		if(!TextUtils.isEmpty(items.get(position).getMon()))
		{
			if(items.get(position).getMon().equals("α"))
			{
				tv_coupon_item_￥.setVisibility(View.GONE);
			}else
			{
				tv_coupon_item_α.setVisibility(View.GONE);
			}
		}

		if(!TextUtils.isEmpty(items.get(position).getMoney()))
		{
			tv_coupon_money.setText(items.get(position).getMoney());
		}

		if(!TextUtils.isEmpty(items.get(position).getName()))
		{
			tv_coupon_name.setText(items.get(position).getName());
		}

		if(!TextUtils.isEmpty(items.get(position).getRemark()))
		{
			tv_usage_rules.setText(items.get(position).getRemark());
		}

		if(!TextUtils.isEmpty(items.get(position).getEndDate()))
		{
			tv_coupon_term.setText("有效期至"+items.get(position).getEndDate());
		}
		return convertView;
	}
}
