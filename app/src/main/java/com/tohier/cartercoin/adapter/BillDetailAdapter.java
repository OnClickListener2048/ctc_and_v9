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
import com.tohier.cartercoin.bean.BillDetail;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016/12/22.
 */

public class BillDetailAdapter extends BaseAdapter {

    private Context context;
    private List<BillDetail> datas = new ArrayList<BillDetail>();

    public BillDetailAdapter(Context context, List<BillDetail> datas) {
        this.context = context;
        this.datas = datas;
    }

    public void setDatas(List<BillDetail> datas) {
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
              convertView = View.inflate(context, R.layout.billdetail_lv_item,null);
              viewHolder = new ViewHolder();
              viewHolder.tv_whereabouts = (TextView) convertView.findViewById(R.id.tv_whereabouts);
              viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
              viewHolder.tv_balance = (TextView) convertView.findViewById(R.id.tv_balance);
              viewHolder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
              convertView.setTag(viewHolder);
          }else
          {
              viewHolder = (ViewHolder) convertView.getTag();
          }
          if(!TextUtils.isEmpty(datas.get(position).getDate()))
          {
                  viewHolder.tv_time.setText(datas.get(position).getDate());
          }
          if(!TextUtils.isEmpty(datas.get(position).getAfter())&&!TextUtils.isEmpty(datas.get(position).getPaycount()))
          {
                 viewHolder.tv_balance.setText("余额："+datas.get(position).getAfter()+datas.get(position).getPaycount());
          }

        if(!TextUtils.isEmpty(datas.get(position).getType()))
        {
            viewHolder.tv_whereabouts.setText(datas.get(position).getType());
        }

        if(!TextUtils.isEmpty(datas.get(position).getMoney())&&!TextUtils.isEmpty(datas.get(position).getPaycount()))
        {
            viewHolder.tv_money.setText(datas.get(position).getMoney()+datas.get(position).getPaycount());
        }
        return convertView;
    }

    class ViewHolder
    {
        TextView tv_whereabouts,tv_time,tv_balance,tv_money;
    }

}
