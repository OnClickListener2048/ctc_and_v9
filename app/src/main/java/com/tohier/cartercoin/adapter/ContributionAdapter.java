package com.tohier.cartercoin.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.YWConversation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.activity.ContributionActivity;
import com.tohier.cartercoin.activity.ProfitDetailsActivity;
import com.tohier.cartercoin.activity.TiXianJiLuActivity;
import com.tohier.cartercoin.bean.ContributionData;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.MyApplication;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016/12/20.
 */

public class ContributionAdapter extends BaseAdapter {

    private Context context;
    private List<ContributionData> datas = new ArrayList<ContributionData>();
    private YWIMKit mIMKit;

    public ContributionAdapter(Context context, List<ContributionData> datas,YWIMKit mIMKit) {
        this.context = context;
        this.datas = datas;
        this.mIMKit = mIMKit;
    }

    public List<ContributionData> getDatas() {
        return datas;
    }

    public void setDatas(List<ContributionData> datas) {
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView==null)
        {
            convertView = View.inflate(context, R.layout.contribution_listview_item,null);
            viewHolder = new ViewHolder();
            viewHolder.tv_msg_count = (TextView) convertView.findViewById(R.id.tv_msg_count);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tvAllContribution = (TextView) convertView.findViewById(R.id.tv_allcontribution);
            viewHolder.tvContributionToday = (TextView) convertView.findViewById(R.id.tv_contribution_today);
            viewHolder.iv_member_head_img = (CircleImageView) convertView.findViewById(R.id.iv_member_head_img);
            viewHolder.linearLayout_into_profitdetails = (LinearLayout) convertView.findViewById(R.id.linearLayout_into_profitdetails);
            viewHolder.rl_onclick = (RelativeLayout) convertView.findViewById(R.id.rl_onclick);
            viewHolder.tv_count = (TextView) convertView.findViewById(R.id.tv_count);
            convertView.setTag(viewHolder);
        }else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(!TextUtils.isEmpty(datas.get(position).getContributionToday()))
        {
            viewHolder.tvContributionToday.setText(datas.get(position).getContributionToday());
        }

        if(!TextUtils.isEmpty(datas.get(position).getName()))
        {
            viewHolder.tvName.setText(datas.get(position).getName());
        }

        if(!TextUtils.isEmpty(datas.get(position).getAllContribution()))
        {
            viewHolder.tvAllContribution.setText(datas.get(position).getAllContribution());
        }

        if(!TextUtils.isEmpty(datas.get(position).getCount()))
        {
            viewHolder.tv_count.setText(datas.get(position).getCount()+"个");
        }

        if(!TextUtils.isEmpty(datas.get(position).getPicUrl()))
        {
            final CircleImageView circleImageView = viewHolder.iv_member_head_img;
                    Glide.with(context).load(datas.get(position).getPicUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(circleImageView) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    circleImageView.setImageDrawable(circularBitmapDrawable);
                }
            });
        }else
        {
            final CircleImageView circleImageView = viewHolder.iv_member_head_img;
            circleImageView.setImageResource(R.mipmap.iv_member_default_head_img);
        }

        viewHolder.linearLayout_into_profitdetails.setOnClickListener(new MyClickListener(datas.get(position).getId(),position));
        /**
         * 未读消息数
         */
        int unReadCount = 0;
        try {
            IYWConversationService conversationService = mIMKit.getConversationService();
            YWConversation conversation = conversationService.getConversationByUserId(datas.get(position).getLinkCode());
            unReadCount = conversation.getUnreadCount();

            if (unReadCount>0){
                viewHolder.tv_msg_count.setVisibility(View.VISIBLE);
                viewHolder.tv_msg_count.setText(unReadCount+"");
                if (unReadCount>99){
                    viewHolder.tv_msg_count.setText(99+"+");
                }
            }else{
                viewHolder.tv_msg_count.setVisibility(View.GONE);
            }

        }catch (Exception e){
            viewHolder.tv_msg_count.setVisibility(View.GONE);
        }

        viewHolder.rl_onclick.setOnClickListener(new MyClickListener(datas.get(position).getId(),position));

        return convertView;
    }

    class ViewHolder
    {
       TextView tvName,tvAllContribution,tvContributionToday,tv_msg_count,tv_count;
       CircleImageView iv_member_head_img;
        LinearLayout linearLayout_into_profitdetails;
        RelativeLayout rl_onclick;
    }

    class MyClickListener implements View.OnClickListener {
        private String id;
        private int position;

        public MyClickListener(String id,int position) {
            this.id = id;
            this.position = position;
        }

        @Override
        public void onClick(final View v) {

            switch(v.getId()){
                case R.id.linearLayout_into_profitdetails:
                    Intent intent = new Intent(context,ProfitDetailsActivity.class);
                    intent.putExtra("id",datas.get(position).getId());
                    context.startActivity(intent);
                    break;
                case R.id.rl_onclick:

                    Intent intent1 = mIMKit.getChattingActivityIntent(datas.get(position).getLinkCode(), MyApplication.APP_KEY);
                    context.startActivity(intent1);
                    break;
            }

        }
    }

}
