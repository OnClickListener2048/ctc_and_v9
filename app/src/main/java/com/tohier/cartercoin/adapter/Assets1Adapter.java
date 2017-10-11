package com.tohier.cartercoin.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tohier.cartercoin.R;
import com.tohier.cartercoin.bean.Assets;
import com.tohier.cartercoin.listener.MyItemOnclick;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/11.
 */

public class Assets1Adapter extends BaseAdapter {
    private Context context;
    private ArrayList<Assets> list = new ArrayList<Assets>();


    public Assets1Adapter(Context context, ArrayList<Assets> list) {
        this.context = context;
        this.list = list;
    }

    public ArrayList<Assets> getList() {
        return list;
    }

    public void setList(ArrayList<Assets> list) {
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
                convertView  = View.inflate(context,R.layout.layout_asstes_item2,null);
                holder.tvType = (TextView) convertView.findViewById(R.id.tv_type);
                holder.tvSong = (TextView) convertView.findViewById(R.id.tv_song);
                holder.llBuy = (LinearLayout) convertView.findViewById(R.id.ll_buy);
                holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
                holder.ivSong = (ImageView) convertView.findViewById(R.id.iv_shou);
                holder.tvDesc = (TextView) convertView.findViewById(R.id.tv_desc);
                holder.tt = (TextView) convertView.findViewById(R.id.tt);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (list.size() == position+1){
                holder.tt.setVisibility(View.VISIBLE);
            }

            holder.tvPrice.setText(list.get(position).getPrice());
            holder.tvSong.setText(list.get(position).getIntroduce()+" ");
            holder.tvType.setText(" "+list.get(position).getName()+" ");
            if (!list.get(position).getDesc().equals("")){
                holder.tvDesc.setText(list.get(position).getDesc());
            }else {
                holder.tvDesc.setVisibility(View.GONE);
            }

            holder.llBuy.setOnClickListener(new myClick(position));

        return convertView;
    }

    private MyItemOnclick myItemOnClick;

    public void setMyItemOnClick(MyItemOnclick myItemOnClick) {
        this.myItemOnClick = myItemOnClick;
    }


    private class myClick implements View.OnClickListener{

        private int position;

        public myClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            myItemOnClick.myItemOnClick(position,v);
        }
    }

    private class ViewHolder{
        private TextView tvType,tt;
        private TextView tvSong;
        private TextView tvPrice;
        private TextView tvDesc;
        private ImageView ivSong;
        private LinearLayout llBuy;

    }
}
