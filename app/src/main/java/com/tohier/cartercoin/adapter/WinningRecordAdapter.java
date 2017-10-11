package com.tohier.cartercoin.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tohier.cartercoin.R;
import com.tohier.cartercoin.bean.WinningRecordBean;

import java.util.ArrayList;

public class WinningRecordAdapter extends BaseAdapter{
	Context context;
	ArrayList<WinningRecordBean> list;



	public WinningRecordAdapter(Context context, ArrayList<WinningRecordBean> list) {
		super();
		this.context = context;
		this.list = list;
	}

	public void setList(ArrayList<WinningRecordBean> list) {
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
					R.layout.winningrecord_lv_item, null);
			holder = new ViewHolder();
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if(!TextUtils.isEmpty(list.get(position).getDetail()))
		{
				holder.tv_title.setText("您在幸运大转轮抽中"+list.get(position).getDetail());
		}

		if(!TextUtils.isEmpty(list.get(position).getTime()))
		{
				holder.tv_time.setText(list.get(position).getTime());

		}

		return convertView;
	}

	public final class ViewHolder {
		public TextView tv_title;
		public TextView tv_time;
	}


}
