package com.tohier.cartercoin.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.tohier.cartercoin.R;
import com.tohier.cartercoin.bean.Transaction;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/11.
 */

public class MyTransactionAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Transaction> list = new ArrayList<Transaction>();
    private double Proportion,Proportion1;
    GridView gridView;

    /**
     * type==0   交易平台买卖实时
     * type==1   交易平台成交记录
     */
    private int type;

    public MyTransactionAdapter(Context context, ArrayList<Transaction> list , int type) {
        this.context = context;
        this.list = list;
        this.type = type;
    }

    public ArrayList<Transaction> getList() {
        return list;
    }

    public void setList(ArrayList<Transaction> list) {
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
            convertView  = View.inflate(context,R.layout.list_deal,null);
            holder.tvBuy = (TextView) convertView.findViewById(R.id.tv_buy);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tvCount = (TextView) convertView.findViewById(R.id.tv_count);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tvCumulative = (TextView) convertView.findViewById(R.id.tv_cumulative);
            holder.view = convertView.findViewById(R.id.bg_pro);
            holder.v1 = convertView.findViewById(R.id.v1);
            holder.v2 = convertView.findViewById(R.id.v2);
            holder.v3 = convertView.findViewById(R.id.v3);
            holder.v4 = convertView.findViewById(R.id.v4);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (type == 0 ){
            holder.tvTime.setVisibility(View.GONE);
            holder.tvBuy.setVisibility(View.VISIBLE);
            String state = list.get(position).getState().substring(0,1);
            if (list.get(position).getState().equals("买/卖")){
                holder.tvBuy.setTextColor(0xff000000);
                holder.v1.setVisibility(View.VISIBLE);
                holder.v3.setVisibility(View.VISIBLE);
                holder.v4.setVisibility(View.VISIBLE);
            }else{
                holder.v1.setVisibility(View.GONE);
                holder.v2.setVisibility(View.GONE);
                holder.v3.setVisibility(View.GONE);
                holder.v4.setVisibility(View.GONE);
                if (state.equals("买")){
                    holder.tvBuy.setTextColor(0xffe7463f);
                }else if (state.equals("卖")){
                    holder.tvBuy.setTextColor(0xff8fca69);
                }
            }
            if (position != 0){
                 WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                 int width = wm.getDefaultDisplay().getWidth();

                 ViewGroup.LayoutParams layoutParams = holder.view.getLayoutParams();
//                float f = Float.parseFloat(list.get(position).getDeal())/Float.parseFloat(list.get(position).getCount());

                double f = 0;
                try{
                    f = Double.parseDouble(list.get(position).getPer());
                }catch(Exception e){
                    f = 0;
                }

                 layoutParams.width = (int) (width/2*f);
                 holder.view.setLayoutParams(layoutParams);


                if (state.equals("买")){
                    holder.view.setBackgroundColor(0xffE7463F);

                }else if (state.equals("卖")){
//                    holder.view.setBackgroundColor(0xff8fca69);
                    holder.view.setBackgroundColor(0xff8fca69);

                }

            }else{
                holder.view.setBackgroundColor(0xffffffff);
            }
            holder.tvCount.setText(list.get(position).getNodeal());
            holder.tvBuy.setText(list.get(position).getState());

        }else if (type == 1){
            holder.tvTime.setVisibility(View.VISIBLE);
            holder.tvBuy.setVisibility(View.GONE);
            holder.tvTime.setText(list.get(position).getTime());
            holder.tvCount.setText(list.get(position).getCount());
        }




        holder.tvPrice.setText(list.get(position).getPrice());

        holder.tvCumulative.setText(list.get(position).getCumulative());

//        String color = position % 2 == 0 ? "#88f2f4f9" : "#88e7ebf2";
//        convertView.setBackgroundColor(Color.parseColor(color));

        return convertView;
    }


    private class ViewHolder{
        private TextView tvBuy;
        private TextView tvTime;
        private TextView tvPrice;
        private TextView tvCount;
        private TextView tvCumulative; //累计
        private View view ,v1,v2,v3,v4;

    }
}
