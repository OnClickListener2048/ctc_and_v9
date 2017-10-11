package com.tohier.cartercoin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.bean.Goods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GoodsAdapter extends BaseAdapter {

	private List<Goods> items = new ArrayList<Goods>();
	private Context context;
	private HashMap<String,String> map = new HashMap<String,String>();

	public GoodsAdapter(List<Goods> items, Context context) {
		super();
		this.items = items;
		this.context = context;

	}
	
	public List<Goods> getItems() {
		return items;
	}

	public void setItems(List<Goods> items) {
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView ==null ){
			convertView = LayoutInflater.from(context).inflate(R.layout.goods_item_layout, null);
			viewHolder = new ViewHolder();
			viewHolder.iv_pic =  (ImageView) convertView.findViewById(R.id.iv_goods_pic);
			viewHolder.tv_goods_name =  (TextView) convertView.findViewById(R.id.tv_goods_name);
			viewHolder.tv_goods_price =  (TextView) convertView.findViewById(R.id.tv_goods_price);
			viewHolder.tv_qtysell =  (TextView) convertView.findViewById(R.id.tv_qtysell);

			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}

			Glide.with(context).load(items.get(position).getPic()).placeholder(null)
				.error(null).into( viewHolder.iv_pic);
			viewHolder.tv_goods_name.setText(items.get(position).getName());
			viewHolder.tv_goods_price.setText(items.get(position).getUnitctc());

			viewHolder.tv_qtysell.setText(items.get(position).getQtysell()+"件已售");

		return convertView;
	}
	  


	class ViewHolder
	{

		ImageView iv_pic;
		TextView tv_goods_name;
		TextView tv_goods_price;
		TextView tv_qtysell;
	}
	

	
	
}
