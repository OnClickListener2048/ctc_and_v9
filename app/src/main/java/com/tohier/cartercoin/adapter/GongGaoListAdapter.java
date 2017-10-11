package com.tohier.cartercoin.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.bean.News;

import java.util.ArrayList;
import java.util.List;

public class GongGaoListAdapter extends BaseAdapter {
	private Context context;
	private List<News> list = new ArrayList<News>();
    private ListView listView = null;
	/**
	 *
	 */
	private String isZiXun;

	public GongGaoListAdapter(Context context, ArrayList<News> list,String isZiXun) {
		this.context = context;
		this.list = list;
		this.isZiXun = isZiXun;
	}

	public List<News> getList() {
		return list;
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

	public void setList(List<News> list) {
		this.list = list;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHolder holder = null;
		if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.fragment_gonggao_listview_item, null);
				holder = new ViewHolder();
				holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
				holder.tvGaiShu = (TextView) convertView.findViewById(R.id.tv_gaishu);
				holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
				holder.ivImg = (ImageView) convertView.findViewById(R.id.iv_img);
				holder.tv_mark = (TextView) convertView.findViewById(R.id.tv_mark);
				holder.iv_new = (ImageView) convertView.findViewById(R.id.iv_new);
				holder.tvCount = (TextView) convertView.findViewById(R.id.tv_count);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(Util.isOnMainThread()) {
			Glide.with(context).load(list.get(position).getPic()).placeholder(null)
					.error(null).into( holder.ivImg);
		}

		holder.tvName.setText(list.get(position).getTitle());
		holder.tvGaiShu.setText(list.get(position).getDesc());
		holder.tvTime.setText(list.get(position).getCreatedate());

		if(TextUtils.isEmpty(isZiXun))
		{
			if(Integer.parseInt(list.get(position).getKid())==2)
			{
				holder.tv_mark.setText("公告");
				holder.tv_mark.setTextColor(Color.WHITE);
				holder.tv_mark.setBackgroundResource(R.drawable.bg_shape_4cd7f9);
			}
		}else
		{
			if(isZiXun.equals("gonggao"))
			{
				holder.tv_mark.setText("公告");
				holder.tv_mark.setTextColor(Color.WHITE);
				holder.tv_mark.setBackgroundResource(R.drawable.bg_shape_4cd7f9);
			}
		}

		if (list.get(position).getStatus().equals("0")){
			holder.iv_new.setVisibility(View.VISIBLE);
		}else if (list.get(position).getStatus().equals("1")){
			holder.iv_new.setVisibility(View.GONE);
		}

		holder.tvCount.setText(list.get(position).getNumber());
		return convertView;
	}




	public final class ViewHolder {
		public TextView tvName;
		public TextView tvGaiShu;
		public TextView tvTime;
		public ImageView ivImg;
		public TextView tv_mark;
		ImageView iv_new;
		RelativeLayout rl_item;
		TextView tvCount;
	}
}
