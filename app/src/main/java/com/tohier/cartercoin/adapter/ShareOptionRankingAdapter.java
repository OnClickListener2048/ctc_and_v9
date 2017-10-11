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
import com.tohier.cartercoin.bean.UserShareOptionRanking;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016/11/11.
 */

public class ShareOptionRankingAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<UserShareOptionRanking> list = new ArrayList<UserShareOptionRanking>();
    /**
     * 0---积分
     * 1---金额
     */
    private int type;


    public ShareOptionRankingAdapter(Context context, ArrayList<UserShareOptionRanking> list,int type ) {
        this.context = context;
        this.list = list;
        this.type = type;
    }

    public ArrayList<UserShareOptionRanking> getList() {
        return list;
    }

    public void setList(ArrayList<UserShareOptionRanking> list) {
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


        if (!TextUtils.isEmpty(list.get(position).getHeadUrl())){
            Glide.with(context).load(list.get(position).getHeadUrl()).asBitmap().centerCrop().error(R.mipmap.iv_member_default_head_img).into(new BitmapImageViewTarget(holder.Head) {
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



        holder.tvNick.setText(list.get(position).getNick());

        if (type == 0){
            holder.tvCount.setText(list.get(position).getCount()+" 分");
        }else{
            holder.tvCount.setText(list.get(position).getCount());
        }



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
