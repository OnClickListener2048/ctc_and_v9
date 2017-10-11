package com.tohier.cartercoin.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tohier.cartercoin.R;
import com.tohier.cartercoin.bean.DealRecord;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/11.
 */

public class DealAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<DealRecord> list = new ArrayList<DealRecord>();
    private int type;


    public DealAdapter(Context context, ArrayList<DealRecord> list , int type) {
        this.context = context;
        this.list = list;
        this.type = type;
    }

    public ArrayList<DealRecord> getList() {
        return list;
    }

    public void setList(ArrayList<DealRecord> list) {
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
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView  = View.inflate(context,R.layout.deal_item,null);
            holder.tvType = (TextView) convertView.findViewById(R.id.tv_type);
            holder.tvRise = (TextView) convertView.findViewById(R.id.tv_rise);
            holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tvState = (TextView) convertView.findViewById(R.id.tv_orderstate);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (list.get(position).getResult().equals("盈利")){
            holder.tvState.setTextColor(0xfff694a0);
        }else{
            holder.tvState.setTextColor(0xff90ca6e);
        }

        if (list.get(position).getState().equals("买涨")){
            holder.tvRise.setTextColor(0xfff694a0);
        }else{
            holder.tvRise.setTextColor(0xff90ca6e);
        }
        holder.tvType.setText(list.get(position).getType()+"/"+list.get(position).getPayAccount());
        holder.tvRise.setText(list.get(position).getState());
        String time = "";
        if (type == 1){
            time = list.get(position).getOrderTime();
        }else{
            time = list.get(position).getExpireTime();
        }

        String[] str = time.split(" ");
        holder.tvDate.setText(str[0]+"\n"+str[1]);
        holder.tvPrice.setText(list.get(position).getMoney());

        holder.tvState.setText(list.get(position).getProfit());

        String color = position % 2 == 0 ? "#88f2f4f9" : "#88e7ebf2";
        convertView.setBackgroundColor(Color.parseColor(color));
        return convertView;
    }


    private class ViewHolder{
        private TextView tvType;
        private TextView tvRise;
        private TextView tvDate;
        private TextView tvPrice;
        private TextView tvState;

    }
}
