package com.tohier.cartercoin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.bean.News;

import java.util.ArrayList;
import java.util.List;

public class Fragment_HuoDong_Adapter extends BaseAdapter {
	List<News> items = new ArrayList<News>();
	Context context;

	public Fragment_HuoDong_Adapter(List<News> items, Context context) {
		super();
		this.items = items;
		this.context = context;
	}
	
	public List<News> getItems() {
		return items;
	}

	public void setItems(List<News> items) {
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
		if(convertView==null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.fragment_gonggao_gridview_item, null);
		    viewHolder = new ViewHolder();
		    viewHolder.iv_new =  (ImageView) convertView.findViewById(R.id.iv_new);
		    viewHolder.tv_new_desc =  (TextView) convertView.findViewById(R.id.tv_news_desc);
		    viewHolder.tv_time =  (TextView) convertView.findViewById(R.id.tv_time);
		    convertView.setTag(viewHolder);
		}else
		{
			viewHolder = (ViewHolder) convertView.getTag();	
		}
		
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		int height = wm.getDefaultDisplay().getHeight();
		LayoutParams layoutParams = viewHolder.iv_new.getLayoutParams();
		layoutParams.height = (int) (height/3.6);
		viewHolder.iv_new.setLayoutParams(layoutParams);
		
		Glide.with(context).load(items.get(position).getPic()).placeholder(null)
		    .error(null).into( viewHolder.iv_new);
		viewHolder.tv_new_desc.setText(items.get(position).getTitle());
		viewHolder.tv_time.setText(items.get(position).getCreatedate());
		return convertView;
	}
	
	class ViewHolder
	{
		ImageView iv_new;
		TextView tv_new_desc;
		TextView tv_time;
	}
	

}
