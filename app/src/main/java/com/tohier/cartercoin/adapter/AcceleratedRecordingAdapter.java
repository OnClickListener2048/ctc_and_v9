package com.tohier.cartercoin.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tohier.cartercoin.R;
import com.tohier.cartercoin.bean.AcceleratedRecordingBean;
import com.tohier.cartercoin.bean.BillDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/22.
 */

public class AcceleratedRecordingAdapter extends BaseAdapter {

    private Context context;
    private List<AcceleratedRecordingBean> datas = new ArrayList<AcceleratedRecordingBean>();

    public AcceleratedRecordingAdapter(Context context, List<AcceleratedRecordingBean> datas) {
        this.context = context;
        this.datas = datas;
    }

    public void setDatas(List<AcceleratedRecordingBean> datas) {
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
              convertView = View.inflate(context, R.layout.acceleratedrecording_lv_item,null);
              viewHolder = new ViewHolder();
              viewHolder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
              viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
              viewHolder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
              viewHolder.tv_cou = (TextView) convertView.findViewById(R.id.tv_cou);
              viewHolder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
              convertView.setTag(viewHolder);
          }else
          {
              viewHolder = (ViewHolder) convertView.getTag();
          }

          if(!TextUtils.isEmpty(datas.get(position).getAddress()))
          {
            viewHolder.tv_address.setText(datas.get(position).getAddress());
          }

          if(!TextUtils.isEmpty(datas.get(position).getName()))
          {
            viewHolder.tv_name.setText(datas.get(position).getName());
          }

          if(!TextUtils.isEmpty(datas.get(position).getType()))
          {
            viewHolder.tv_type.setText(datas.get(position).getType());
          }

          if(!TextUtils.isEmpty(datas.get(position).getCou()))
          {
            viewHolder.tv_cou.setText(datas.get(position).getCou());
          }

          if(!TextUtils.isEmpty(datas.get(position).getDate()))
          {
              viewHolder.tv_date.setText(datas.get(position).getDate());
          }
        return convertView;
    }

    class ViewHolder
    {
        TextView tv_address,tv_name,tv_type,tv_cou,tv_date;
    }

}
