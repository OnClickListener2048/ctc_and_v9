package com.tohier.cartercoin.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.bean.BillData;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016/12/22.
 */

public class BillAdapter extends BaseAdapter {

    private Context context;
    private List<BillData> datas = new ArrayList<BillData>();

    public BillAdapter(Context context, List<BillData> datas) {
        this.context = context;
        this.datas = datas;
    }

    public void setDatas(List<BillData> datas) {
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
              convertView = View.inflate(context, R.layout.bill_lv_item,null);
              viewHolder = new ViewHolder();
              viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
              viewHolder.tvMoney = (TextView) convertView.findViewById(R.id.tv_money);
              viewHolder.tvNickName = (TextView) convertView.findViewById(R.id.tv_nickname);
              viewHolder.imageView = (CircleImageView) convertView.findViewById(R.id.iv_head_img);
              convertView.setTag(viewHolder);
          }else
          {
              viewHolder = (ViewHolder) convertView.getTag();
          }
          if(!TextUtils.isEmpty(datas.get(position).getDate()))
          {
                  viewHolder.tvTime.setText(datas.get(position).getDate());
          }
        if(!TextUtils.isEmpty(datas.get(position).getHeadImg()))
        {
            final ImageView imageView = viewHolder.imageView;
            Glide.with(context).load(datas.get(position).getHeadImg()).asBitmap().centerCrop().into(new BitmapImageViewTarget(imageView) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    imageView.setImageDrawable(circularBitmapDrawable);
                }
            });
        }

        if(!TextUtils.isEmpty(datas.get(position).getMoney()))
        {
            if(!datas.get(position).getMoney().contains("-"))
            {
                viewHolder.tvMoney.setText("+"+datas.get(position).getMoney());
            }else
            {
                viewHolder.tvMoney.setText(datas.get(position).getMoney());
            }
        }

        if(!TextUtils.isEmpty(datas.get(position).getNickname()))
        {
            viewHolder.tvNickName.setText(datas.get(position).getNickname()+"-"+datas.get(position).getMoneyType());
        }
        return convertView;
    }

    class ViewHolder
    {
        TextView tvTime,tvMoney,tvNickName;
        CircleImageView imageView;
    }

}
