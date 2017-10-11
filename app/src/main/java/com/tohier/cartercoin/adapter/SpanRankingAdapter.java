package com.tohier.cartercoin.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.bean.SpanRankingBean;
import com.tohier.cartercoin.bean.UserShareOptionRanking;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016/11/11.
 */

public class SpanRankingAdapter extends BaseAdapter {
    private Context context;
    private List<SpanRankingBean> list = new ArrayList<SpanRankingBean>();
    /**
     * 0---积分
     * 1---金额
     */
    private int type;


    public SpanRankingAdapter(Context context, List<SpanRankingBean> list) {
        this.context = context;
        this.list = list;
    }

    public List<SpanRankingBean> getList() {
        return list;
    }

    public void setList(List<SpanRankingBean> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView  = View.inflate(context,R.layout.item_shareoptionranking,null);
            holder.tvRanking = (TextView) convertView.findViewById(R.id.tv_ranking);
            holder.Head = (CircleImageView) convertView.findViewById(R.id.circleImageView_head);
            holder.tvNick = (TextView) convertView.findViewById(R.id.tv_nick);
            holder.tvCount = (TextView) convertView.findViewById(R.id.tv_count);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.tvRanking.setText(list.get(position).getRanking());

        if (!TextUtils.isEmpty(list.get(position).getPic())){
            Glide.with(context).load(list.get(position).getPic()).asBitmap().centerCrop().error(R.mipmap.iv_member_default_head_img).into(new BitmapImageViewTarget(holder.Head) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    holder.Head.setImageDrawable(circularBitmapDrawable);
                }
            });
        }else{
            holder.Head.setImageResource(R.mipmap.iv_member_default_head_img);
        }



        holder.tvNick.setText(list.get(position).getName());

        holder.tvCount.setText(list.get(position).getCount()+" 次");



//        String color = position % 2 == 0 ? "#88f2f4f9" : "#88e7ebf2";
//        convertView.setBackgroundColor(Color.parseColor(color));
        return convertView;
    }





    private class ViewHolder{
        private TextView tvRanking;
        private CircleImageView Head;
        private TextView tvNick;
        private TextView tvCount;

    }
}
