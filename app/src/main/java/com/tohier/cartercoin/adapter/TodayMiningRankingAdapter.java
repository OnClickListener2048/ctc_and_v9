package com.tohier.cartercoin.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.activity.NewMiningActivity;
import com.tohier.cartercoin.bean.TodayMiningRankingBean;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/3/13.
 */

public class TodayMiningRankingAdapter extends BaseAdapter {

    private List<TodayMiningRankingBean> datas = new ArrayList<TodayMiningRankingBean>();
    private Context context;

    public TodayMiningRankingAdapter(List<TodayMiningRankingBean> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    public void setDatas(List<TodayMiningRankingBean> datas) {
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
        return  0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            convertView = View.inflate(context, R.layout.miningfragment_today_mining_ranking_layout,null);
        }

         TextView tv_mining_ranking = (TextView) convertView.findViewById(R.id.tv_mining_ranking);
         TextView tv_mining_ranking2 = (TextView) convertView.findViewById(R.id.tv_mining_ranking2);
         TextView tv_nickname = (TextView) convertView.findViewById(R.id.tv_nickname);
         TextView tv_mining_count = (TextView) convertView.findViewById(R.id.tv_mining_count);
        final CircleImageView circleImageView = (de.hdodenhof.circleimageview.CircleImageView) convertView.findViewById(R.id.imageview);

        if(position==0)
        {
            tv_mining_ranking.setVisibility(View.VISIBLE);
            tv_mining_ranking.setText("");
            tv_mining_ranking.setBackgroundResource(R.mipmap.jin);
            tv_mining_ranking2.setVisibility(View.GONE);
        }else if(position==1)
        {
            tv_mining_ranking.setVisibility(View.VISIBLE);
            tv_mining_ranking.setText("");
            tv_mining_ranking.setBackgroundResource(R.mipmap.yin);
            tv_mining_ranking2.setVisibility(View.GONE);
        }else if(position==2)
        {
            tv_mining_ranking.setVisibility(View.VISIBLE);
            tv_mining_ranking.setText("");
            tv_mining_ranking.setBackgroundResource(R.mipmap.tong);
            tv_mining_ranking2.setVisibility(View.GONE);
        }else
        {
            tv_mining_ranking.setVisibility(View.GONE);
            tv_mining_ranking2.setText(position+1+"");
            tv_mining_ranking2.setBackgroundColor(0xffffffff);
        }

        tv_nickname.setText(datas.get(position).getNickname());
        tv_mining_count.setText(datas.get(position).getMiningCount());
        if(!((NewMiningActivity)context).isDestroyed())
        {
            Glide.with(context).load(datas.get(position).getPic()).asBitmap().centerCrop().error(R.mipmap.iv_member_default_head_img).into(new BitmapImageViewTarget(circleImageView) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    circleImageView.setImageDrawable(circularBitmapDrawable);
                }
            });
        }

        return convertView;
    }
}
