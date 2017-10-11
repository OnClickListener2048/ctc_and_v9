package com.tohier.cartercoin.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tohier.cartercoin.R;
import com.tohier.cartercoin.bean.DealRecord;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/11.
 */

public class DealOrderAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<DealRecord> list = new ArrayList<DealRecord>();
    private String currentPrice;
    private DecimalFormat df;
    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {

        this.currentPrice = df.format(currentPrice);

    }

    private long time ;

    public DealOrderAdapter(Context context, ArrayList<DealRecord> list ) {
        this.context = context;
        this.list = list;
        df   = new DecimalFormat("######0.00");
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView  = View.inflate(context,R.layout.deal_order_item,null);
            holder.tvType = (TextView) convertView.findViewById(R.id.tv_cointype);
            holder.tvRise = (TextView) convertView.findViewById(R.id.tv_rise);
            holder.tvDate = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tvNewPrice = (TextView) convertView.findViewById(R.id.tv_newprice);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (list.get(position).getState().equals("买涨")){
            holder.tvRise.setTextColor(0xfff694a0);
        }else{
            holder.tvRise.setTextColor(0xff90ca6e);
        }

        try {
//            time = Long.parseLong(Long.parseLong(Tools.dateToStamp(list.get(position).getExpireTime()))
//                    -Long.parseLong(Tools.dateToStamp(list.get(position).getOrderTime()))+"");
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.tvType.setText(list.get(position).getType()+"/"+list.get(position).getMoney()+list.get(position).getPayAccount());
        holder.tvRise.setText(list.get(position).getState());

        String time = list.get(position).getOrderTime();
        String[] str = time.split(" ");
        String[] str1 = list.get(position).getExpireTime().split(" ");
        String[] t = str[1].split(":");
        if (t[0].length()==1){
            holder.tvDate.setText("开始:"+str[1]+"\n结束:0"+str1[1]);
        }else{
            holder.tvDate.setText("开始:"+str[1]+"\n结束:"+str1[1]);
        }

        holder.tvPrice.setText(list.get(position).getOrderPrice());

        holder.tvNewPrice.setText(currentPrice);

        String color = position % 2 == 0 ? "#88f2f4f9" : "#88e7ebf2";
        convertView.setBackgroundColor(Color.parseColor(color));
        return convertView;
    }





    public void updateNotify( ArrayList<DealRecord> list1){
        this.list = list1;
        notifyDataSetInvalidated();
    }


    private class ViewHolder{
        private TextView tvType;
        private TextView tvRise;
        private TextView tvDate;
        private TextView tvPrice;
        private TextView tvNewPrice;

    }
}
