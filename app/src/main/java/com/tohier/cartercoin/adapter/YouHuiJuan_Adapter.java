package com.tohier.cartercoin.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tohier.cartercoin.R;
import com.tohier.cartercoin.bean.YouHuiQuan;

import java.util.ArrayList;
import java.util.List;

public class YouHuiJuan_Adapter extends BaseAdapter {

	private List<YouHuiQuan> items = new ArrayList<YouHuiQuan>();
	private Context context;

	public YouHuiJuan_Adapter(List<YouHuiQuan> items, Context context) {
		super();
		this.items = items;
		this.context = context;
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
	public View getView(int position, View convertView, ViewGroup parent) {
//		ViewHolder viewHolder;

		if(convertView==null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.activity_youhuijuan_gridview_item, null);
//			viewHolder = new ViewHolder();
//			convertView.setTag(viewHolder);
		}
//		else
//		{
//			viewHolder = (ViewHolder) convertView.getTag();
//		}
		RelativeLayout rl_bg = (RelativeLayout) convertView.findViewById(R.id.rl_bg);
		TextView tv_money =  (TextView) convertView.findViewById(R.id.tv_money);
		TextView tv_title_quan =  (TextView) convertView.findViewById(R.id.tv_title_quan);
		TextView tv_shuoming =  (TextView) convertView.findViewById(R.id.tv_shuoming);
		TextView  tv_youxiao_time =  (TextView) convertView.findViewById(R.id.tv_youxiao_time);
		TextView  tv_money_mark =  (TextView) convertView.findViewById(R.id.tv_money_mark);

		String name = items.get(position).getName();
		if (name.equals("商城购物券")){
			rl_bg.setBackgroundResource(R.mipmap.youhuijuan_bg_shangcheng);
			tv_money_mark.setText("α");
		}else if (name.equals("会员升级券")){
			rl_bg.setBackgroundResource(R.mipmap.youhuijuan_bg_shenji);
			tv_money_mark.setText("¥");
		}else if (name.equals("期权现金")){
			rl_bg.setBackgroundResource(R.mipmap.youhuijuan_bg_juezhanxianjin);
			tv_money_mark.setText("¥");
		}else if(name.equals("期权α资产"))
		{
			rl_bg.setBackgroundResource(R.mipmap.youhuijuan_bg_juezhanzichan);
			tv_money_mark.setText("α");
		}else if(name.equals("矿产包购买券"))
		{
			rl_bg.setBackgroundResource(R.mipmap.youhuijuan_bg_zichanbaoxianjin);
			tv_money_mark.setText("¥");
		}

		Log.i("ctc","money:"+items.get(position).getMoney());
		tv_money.setText(items.get(position).getMoney());
		tv_title_quan.setText(name);
		tv_shuoming.setText("说明："+items.get(position).getRemark());
		tv_youxiao_time.setText("有效期至"+items.get(position).getEndDate());


		return convertView;
	}

	
//	class ViewHolder
//	{
//		RelativeLayout rl_bg;
//        TextView tv_money_mark;
//        TextView tv_shuoming;
//		TextView tv_money;
//		TextView tv_title_quan;
//		TextView tv_youxiao_time;
//	}
//
}
