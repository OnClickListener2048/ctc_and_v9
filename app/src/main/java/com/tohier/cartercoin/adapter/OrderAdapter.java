package com.tohier.cartercoin.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.activity.ConFirmActivity;
import com.tohier.cartercoin.bean.Order;

import java.util.ArrayList;


public class OrderAdapter extends BaseAdapter{
	private ArrayList<Order> list;
	private Context context;

	public ArrayList<Order> getList() {
		return list;
	}

	public void setList(ArrayList<Order> list) {
		this.list = list;
	}

	public OrderAdapter(ArrayList<Order> list, Context context) {
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
			convertView = View.inflate(context, R.layout.order_item, null);
		}
		TextView tv_orderNum = (TextView) convertView
				.findViewById(R.id.tv_ordernum);
		TextView tv_order_state = (TextView) convertView
				.findViewById(R.id.tv_order_state);
		ImageView iv_pic = (ImageView) convertView
				.findViewById(R.id.iv_goods_pic);
		TextView tv_goods_name = (TextView) convertView
				.findViewById(R.id.tv_goods_name);
		TextView tv_express = (TextView) convertView.findViewById(R.id.tv_express);
		TextView tv_goods_count = (TextView) convertView.findViewById(R.id.tv_goods_count);
		TextView tv_order_total = (TextView) convertView.findViewById(R.id.tv_order_total);
		TextView tv_price = (TextView) convertView.findViewById(R.id.tv_goods_price);
		TextView tv_pay = (TextView) convertView.findViewById(R.id.tv_pay);

		String state = list.get(position).getState();
		if (state.equals("待付款")){
			tv_pay.setVisibility(View.VISIBLE);
		}else{
			tv_pay.setVisibility(View.GONE);
		}

		if (state.equals("已发货")){
			tv_express.setText(list.get(position).getExpressName()+": "+list.get(position).getExpressNum());
		}else{
			tv_express.setText("");
		}

		tv_order_state.setText(state);
		tv_orderNum.setText("订单编号:  "+list.get(position).getOrderNum());
		tv_goods_name.setText(list.get(position).getGoodsName());
		tv_goods_count.setText("x"+list.get(position).getCount());
		tv_order_total.setText(list.get(position).getTotalPrice()+"金豆");
		tv_price.setText(list.get(position).getGoodsPrice()+"金豆");
		Glide.with(context).load(list.get(position).getGoodsPic()).placeholder(null)
				.error(null).into(iv_pic);

		tv_pay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				context.startActivity(new Intent(context,ConFirmActivity.class)
						.putExtra("pid",list.get(position).getId())
						.putExtra("price",list.get(position).getTotalPrice()));
			}
		});
		return convertView;
	}

	


}
