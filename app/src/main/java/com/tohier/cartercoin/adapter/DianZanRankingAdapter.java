package com.tohier.cartercoin.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.YWConversation;
import com.bumptech.glide.Glide;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.activity.FengYunRankingActivity;
import com.tohier.cartercoin.bean.ZanRanking;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DianZanRankingAdapter extends BaseAdapter{
	private Context context;
	private List<ZanRanking> list = new ArrayList<ZanRanking>();
	private int number;
	private int type;  //1---同学汇  2---风云榜
	private YWIMKit mIMKit;


	public DianZanRankingAdapter() {
		// TODO Auto-generated constructor stub
	}
	public DianZanRankingAdapter(Context context, List<ZanRanking> list, int type, YWIMKit mIMKit) {
		this.context = context;
		this.list = list;
		this.type = type;
		this.mIMKit = mIMKit;
	}
	
	public List<ZanRanking> getList() {
		return list;
	}
	public void setList(List<ZanRanking> list) {
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
		ZanRanking data = list.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.fragment_fengyun_rangking_lv_item, null);
			holder = new ViewHolder();
			holder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);

			holder.iv_pic = (CircleImageView) convertView
					.findViewById(R.id.iv_pic);
			holder.tv_count = (TextView) convertView
					.findViewById(R.id.tv_count);
			holder.tv_ranking = (TextView) convertView
					.findViewById(R.id.tv_ranking);
			holder.iv_dianzan_pic = (ImageView) convertView
					.findViewById(R.id.iv_dianzan_pic);
			holder.tv_level = (TextView) convertView
					.findViewById(R.id.tv_level);
			holder.tv_power = (TextView) convertView.findViewById(R.id.tv_power);
			holder.tv = (TextView) convertView.findViewById(R.id.tv);
			holder.tv_msg_count = (TextView) convertView.findViewById(R.id.tv_msg_count);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}


		String p =  list.get(position).getPraiseme();
		if(p !=null){
			if (!p.equals("0") ) {
				holder.iv_dianzan_pic.setImageResource(R.mipmap.dianzhihou);
			}else{
				holder.iv_dianzan_pic.setImageResource(R.mipmap.dianzhiqian);
			}
		}



		String imgUrl = data.getPic();
		if (type == 2){//风云榜
			holder.tv_msg_count.setVisibility(View.GONE);
			holder.tv.setVisibility(View.GONE);
			number = position+1;
			holder.tv_count.setText(""+number);
		}else{//同学汇

			int unReadCount = 0;
			try {
				IYWConversationService conversationService = mIMKit.getConversationService();
				YWConversation conversation = conversationService.getConversationByUserId(list.get(position).getLinkCode());
				unReadCount = conversation.getUnreadCount();

				if (unReadCount>0){
					holder.tv_msg_count.setVisibility(View.VISIBLE);
					holder.tv_msg_count.setText(unReadCount+"");
					if (unReadCount>99){
						holder.tv_msg_count.setText(99+"+");
					}
				}else{
					holder.tv_msg_count.setVisibility(View.GONE);
				}

			}catch (Exception e){
				holder.tv_msg_count.setVisibility(View.GONE);
			}



		}

		Glide.with(context).load(imgUrl).placeholder(R.mipmap.iv_member_default_head_img)
				.error(R.mipmap.iv_member_default_head_img).into(holder.iv_pic);
		holder.tv_name.setText(data.getName());// 设置名字
		holder.tv_ranking.setText(data.getPraisenum());// 设置赞数
		holder.tv_level.setText(data.getLevel());   //关系

		String type = data.getType();
		if (type.equals("10")){
			holder.tv_power.setText("注册会员");
		}else if (type.equals("20")){
			holder.tv_power.setText("铂金会员");
		}else if (type.equals("30")){
			holder.tv_power.setText("钻石会员");
		}else if (type.equals("40")){
			holder.tv_power.setText("皇冠会员");
		}

		MyOnClickListener myOnClickListener = new MyOnClickListener(position,holder.tv_ranking,holder.iv_dianzan_pic);
		holder.iv_dianzan_pic.setOnClickListener(myOnClickListener);

	return convertView;
	}

	/**
	 * 为同学点赞
	 */
	private class MyOnClickListener implements View.OnClickListener{

		private int position ;
		private TextView tv_ranking;
		private ImageView iv_dianzan_pic;

		public MyOnClickListener(int position ,TextView tv_ranking, ImageView iv_dianzan_pic) {
			this.position = position;
			this.tv_ranking = tv_ranking;
			this.iv_dianzan_pic = iv_dianzan_pic;
		}

		@Override
		public void onClick(View v) {
			if ((list.get(position).getPraiseme()).equals("0") ) {
				String zid = list.get(position).getId();
				String id =  LoginUser.getInstantiation(context.getApplicationContext()).getLoginUser().getUserId();
				if (zid.equals(id)){
					((FengYunRankingActivity)context).sToast("在自恋的路上你已经找不到对手了~");
					return;
				}
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", id);
				map.put("zid",zid);
				HttpConnect.post((FengYunRankingActivity)context,"member_convergence_praise_add" , map,
						new Callback() {
							@Override
							public void onResponse(Response arg0)
									throws IOException {

								if (arg0 != null || !arg0.equals("")) {

									final JSONObject object = JSONObject.fromObject(arg0
											.body().string());
									if (object.get("status").equals("success")) {

										Handler dataHandler = new Handler(
												context.getMainLooper()) {
											@Override
											public void handleMessage(
													final Message msg) {
												int count = Integer.parseInt(tv_ranking.getText().toString())+1;
												tv_ranking.setText(count+"");
												list.get(position).setPraisenum(count+"");
												iv_dianzan_pic.setImageResource(R.mipmap.dianzhihou);
												ScaleAnimation animation = (ScaleAnimation) AnimationUtils.loadAnimation(context, R.anim.scale);
												iv_dianzan_pic.setAnimation(animation);

												list.get(position).setPraiseme(1+"");
												notifyDataSetChanged();
												((FengYunRankingActivity)context).sToast("点赞成功");
											}
										};
										dataHandler.sendEmptyMessage(0);
									}else {
										Handler dataHandler = new Handler(
												context.getMainLooper()) {
											@Override
											public void handleMessage(
													final Message msg) {
												Toast.makeText(context, "今天的点赞次数已经用尽", Toast.LENGTH_SHORT).show();
											}
										};
										dataHandler.sendEmptyMessage(0);
									}
								}
							}


							@Override
							public void onFailure(Request arg0, IOException arg1) {

								Handler dataHandler = new Handler(
										context.getMainLooper()) {

									@Override
									public void handleMessage(
											final Message msg) {
//										((FengYunRankingActivity)context).sToast("请检查网络");
									}
								};
								dataHandler.sendEmptyMessage(0);
							}
						});
			}else{
				((FengYunRankingActivity)context).sToast("你已经为他点过赞了");
			}

		}
	}

class ViewHolder {
	public CircleImageView iv_pic;
	public TextView tv_count,tv;
	public TextView tv_name, tv_ranking ;
	public ImageView iv_dianzan_pic;
	public TextView tv_level,tv_power,tv_msg_count;
}
	
}
