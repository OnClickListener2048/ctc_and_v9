package com.tohier.cartercoin.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.activity.TransactionActivity;
import com.tohier.cartercoin.bean.Entrust;
import com.tohier.cartercoin.config.HttpConnect;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EntrustAdapter extends BaseAdapter {
	private List<Entrust> items = new ArrayList<Entrust>();
	private Context context;
	private int type; // 0---当前委托   1---历史委托


	public EntrustAdapter(List<Entrust> items, Context context , int type) {
		super();
		this.items = items;
		this.context = context;
		this.type = type;
	}
	
	public List<Entrust> getItems() {
		return items;
	}

	public void setItems(List<Entrust> items) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.entrust_item, null);
		    viewHolder = new ViewHolder();
		    viewHolder.ivState =  (ImageView) convertView.findViewById(R.id.iv_state);
			viewHolder.ivUpdate = (ImageView) convertView.findViewById(R.id.iv_update);
		    viewHolder.tvEntrustCount =  (TextView) convertView.findViewById(R.id.tv_entrust_count);
		    viewHolder.tvEntrustPrice =  (TextView) convertView.findViewById(R.id.tv_entrust_price);
			viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
			convertView.setTag(viewHolder);
			viewHolder.textView = (TextView) convertView.findViewById(R.id.tvtv);
		}else
		{
			viewHolder = (ViewHolder) convertView.getTag();	
		}

		if (items.get(position).getState().equals("买")){
			viewHolder.ivState.setImageResource(R.mipmap.entrust_buy);

		}else if(items.get(position).getState().equals("卖")){
			viewHolder.ivState.setImageResource(R.mipmap.entrust_sell);
		}

		if (type == 0 ){
			viewHolder.ivUpdate.setImageResource(R.mipmap.entrust_clost);
			viewHolder.ivUpdate.setOnClickListener(new MyClick(viewHolder,position));
			viewHolder.textView.setText("数量/未成交");
			viewHolder.tvEntrustCount.setText(items.get(position).getEntrustCount()+"/"+items.get(position).getNoCjCount());
		}else{
			viewHolder.ivUpdate.setImageResource(R.mipmap.entrust_start);
			viewHolder.textView.setText("数量");
			viewHolder.tvEntrustCount.setText(items.get(position).getEntrustCount());
		}


		viewHolder.tvEntrustPrice.setText(items.get(position).getEntrustPricre());
		String time = items.get(position).getTime();

		String[] str = time.split(" ");
		viewHolder.tvTime.setText(str[0]+"\n"+str[1]);

		return convertView;
	}

	class MyClick implements View.OnClickListener {
		private int position;
		private ViewHolder viewHolder;

		public MyClick(ViewHolder viewHolder, int position) {
			this.viewHolder = viewHolder;
			this.position = position;
		}

		@Override
		public void onClick(final View v) {
			((TransactionActivity) context).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					v.setClickable(false);
				}
			});
			final HashMap<String,String> map = new HashMap<String,String>();
			String id = items.get(position).getId();
			map.put("id", id);

			HttpConnect.post((TransactionActivity)context, "member_cancellation", map, new Callback() {
				@Override
				public void onResponse(Response arg0) throws IOException {
					String json = arg0.body().string();
					final JSONObject data = JSONObject.fromObject(json);
					if (data.optString("status").equals("success")){

						((TransactionActivity) context).runOnUiThread(new Runnable() {
							@Override
							public void run() {
								v.setClickable(true);
								items.remove(position);
								notifyDataSetChanged();
								((TransactionActivity) context).sToast("已取消委托");
							}
						});

					}else{

						((TransactionActivity) context).runOnUiThread(new Runnable() {
							@Override
							public void run() {
								items.remove(position);
								notifyDataSetChanged();

								if (!data.optString("msg").equalsIgnoreCase("timeout") || !TextUtils.isEmpty(data.optString("msg"))){
									((TransactionActivity) context).sToast(data.optString("msg"));
								}


								v.setClickable(true);
							}
						});
					}

				}
				@Override
				public void onFailure(Request arg0, IOException arg1) {
					((TransactionActivity) context).sToast(arg0.toString());
					((TransactionActivity) context).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							v.setClickable(true);
						}
					});
				}
			});


		}
	}

	
	class ViewHolder
	{
		private ImageView ivState,ivUpdate;
		private TextView tvEntrustCount;
		private TextView tvEntrustPrice;
		private TextView tvTime,textView;
	}
	

}
