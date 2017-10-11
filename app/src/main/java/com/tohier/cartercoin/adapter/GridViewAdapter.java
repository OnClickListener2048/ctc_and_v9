package com.tohier.cartercoin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.conversation.EServiceContact;
import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.YWConversation;
import com.tohier.android.view.ResizableImageView;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.bean.FuncationList;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<FuncationList> list = new ArrayList<FuncationList>();
	public YWIMKit mIMKit;

	public GridViewAdapter(Context context, ArrayList<FuncationList> list, YWIMKit mIMKit) {
		this.context = context;
		this.list = list;
		this.mIMKit = mIMKit;
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
		// TODO Auto-generated method stub

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_mefuncationlist, null);
			holder = new ViewHolder();
			holder.itemImage1 = (ResizableImageView) convertView
					.findViewById(R.id.my_gridview_item);

			holder.itemText = (TextView) convertView.findViewById(R.id.my_text);
			holder.tv_msg_count = (TextView) convertView.findViewById(R.id.tv_msg_count);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tv_msg_count.setVisibility(View.GONE);
		int unReadCount = 0;

		try{

			IYWConversationService conversationService = mIMKit.getConversationService();
			int count = conversationService.getAllUnreadCount();

			EServiceContact contact = new EServiceContact("分乐宝在线咨询", 0);
			YWConversation conversation = conversationService.getConversation(contact);
			unReadCount = conversation.getUnreadCount();

			if (list.get(position).getText().equals("宝友汇")){
				int mCount = count-unReadCount;
				if (mCount>0){
					holder.tv_msg_count.setVisibility(View.VISIBLE);
					holder.tv_msg_count.setText(mCount+"");
					if (mCount>99){
						holder.tv_msg_count.setText("99+");
					}
				}else{
					holder.tv_msg_count.setVisibility(View.GONE);
				}
			}

			if (list.get(position).getText().equals("在线咨询")){

				if (unReadCount>0){
					holder.tv_msg_count.setVisibility(View.VISIBLE);
					holder.tv_msg_count.setText(unReadCount+"");
					if (unReadCount>99){
						holder.tv_msg_count.setText("99+");
					}
				}else{
					holder.tv_msg_count.setVisibility(View.GONE);
				}
			}
		}catch(Exception e){
		}


		holder.itemImage1.setImageResource(list.get(position).getImage());
		holder.itemText.setText(list.get(position).getText());
		return convertView;

	}

	public final class ViewHolder {
		public ResizableImageView itemImage1;
		public TextView itemText,tv_msg_count;


	}
}
