package com.tohier.cartercoin.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.bean.RankingData;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class WaKuangApapter extends BaseAdapter{
	private Context context;
	private List<RankingData> list = new ArrayList<RankingData>();
	private int number;
	public WaKuangApapter() {
		// TODO Auto-generated constructor stub
	}
	public WaKuangApapter(Context context, List<RankingData> list) {
		this.context = context;
		this.list = list;
	}
	
	public List<RankingData> getList() {
		return list;
	}
	public void setList(List<RankingData> list) {
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
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RankingData data = list.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.activity_ranking_item, null);
			holder = new ViewHolder();
			
			holder.itemText = (TextView) convertView
					.findViewById(R.id.ItemText);

			holder.itemImage2 = (CircleImageView) convertView
					.findViewById(R.id.ItemImage2);
			holder.itemText1 = (TextView) convertView
					.findViewById(R.id.ItemText1);
			holder.itemText2 = (TextView) convertView
					.findViewById(R.id.ItemText2);
		
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		
		
		String imgUrl = data.getImgUrl();

		
		number = position+1;
		holder.itemText.setText(""+number);

		final ViewHolder finalHolder = holder;
		Glide.with(context).load(imgUrl).asBitmap().centerCrop().error(R.mipmap.iv_member_default_head_img).into(new BitmapImageViewTarget(finalHolder.itemImage2) {
			@Override
			protected void setResource(Bitmap resource) {
				RoundedBitmapDrawable circularBitmapDrawable =
						RoundedBitmapDrawableFactory.create(context.getResources(), resource);
				circularBitmapDrawable.setCircular(true);
				finalHolder.itemImage2.setImageDrawable(circularBitmapDrawable);
			}
		});

		holder.itemText1.setText(data.getName());// 设置名字
		holder.itemText2.setText(data.getPrice());// 设置金额
	
	
	return convertView;
	}

class ViewHolder {
	public CircleImageView itemImage1, itemImage2;
	public TextView itemText;
	public TextView itemText1, itemText2 ;
}
	
}
