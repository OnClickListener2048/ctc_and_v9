package com.tohier.cartercoin.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tohier.cartercoin.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/12/22.
 */

public class ZanAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String,String>> datas;

    public ZanAdapter(Context context,ArrayList<HashMap<String,String>> datas) {
        this.context = context;
        this.datas = datas;
    }

    public void setDatas(ArrayList<HashMap<String,String>> datas) {
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
              convertView = View.inflate(context, R.layout.layout_zan_item,null);
              viewHolder = new ViewHolder();
              viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
              viewHolder.tvZanCount = (TextView) convertView.findViewById(R.id.tv_count);
              convertView.setTag(viewHolder);
          }else
          {
              viewHolder = (ViewHolder) convertView.getTag();
          }

        viewHolder.tvTime.setText(datas.get(position).get("time"));
        viewHolder.tvZanCount.setText(datas.get(position).get("count"));

        return convertView;
    }

    class ViewHolder
    {
        TextView tvTime,tvZanCount;
    }

}
