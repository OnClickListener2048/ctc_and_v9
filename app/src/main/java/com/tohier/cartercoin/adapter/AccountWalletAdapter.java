package com.tohier.cartercoin.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tohier.cartercoin.R;
import com.tohier.cartercoin.bean.AccountWallet;
import com.tohier.cartercoin.listener.MyItemOnclick;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/11.
 */

public class AccountWalletAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<AccountWallet> list = new ArrayList<AccountWallet>();

    public AccountWalletAdapter(Context context, ArrayList<AccountWallet> list ) {
        this.context = context;
        this.list = list;
    }

    public ArrayList<AccountWallet> getList() {
        return list;
    }

    public void setList(ArrayList<AccountWallet> list) {
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
                convertView  = View.inflate(context,R.layout.item_account,null);
                holder.tvIconType = (TextView) convertView.findViewById(R.id.tv_icon_type_name);
                holder.tvAccount = (TextView) convertView.findViewById(R.id.tv_account);
                holder.tvHave = (TextView) convertView.findViewById(R.id.tv_have);
                holder.tvNoHave = (TextView) convertView.findViewById(R.id.tv_no_have);
                holder.tvIn = (TextView) convertView.findViewById(R.id.tv_in);
                holder.tvOut = (TextView) convertView.findViewById(R.id.tv_out);
                holder.tvTx = (TextView) convertView.findViewById(R.id.tv_tx);
                holder.tvCz = (TextView) convertView.findViewById(R.id.tv_cz);
                holder.llInOrOut = (LinearLayout) convertView.findViewById(R.id.ll_in_or_out);
                holder.llRmb = (LinearLayout) convertView.findViewById(R.id.ll_rmb);
                holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tvZd = (TextView) convertView.findViewById(R.id.tv_zd);
                holder.tvZd2 = (TextView) convertView.findViewById(R.id.tv_zd2);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tvIconType.setText(list.get(position).getIconName());
            if (list.get(position).getMoney().equals("0.00") || list.get(position).getMoney().equals("0.0000") ){
                holder.tvAccount.setText("0");
            }else{
                holder.tvAccount.setText(list.get(position).getMoney());
            }
            if (list.get(position).getHaveMoney().equals("0.00") || list.get(position).getHaveMoney().equals("0.0000") ){
                holder.tvHave.setText("可用：0");
            }else{
                holder.tvHave.setText("可用："+list.get(position).getHaveMoney());
            }
            if (list.get(position).getNoHaveMoney().equals("0.00") || list.get(position).getNoHaveMoney().equals("0.0000") ){
                holder.tvNoHave.setText("锁定：0");
            }else{
                holder.tvNoHave.setText("锁定："+list.get(position).getNoHaveMoney());
            }

            if (list.get(position).getIconName().equals("人民币")){
                holder.llRmb.setVisibility(View.VISIBLE);
                holder.llInOrOut.setVisibility(View.GONE);
                holder.ivIcon.setImageResource(R.mipmap.iv_xianjin_icon);
                holder.tvNoHave.setVisibility(View.GONE);
            }else{
                holder.llRmb.setVisibility(View.GONE);
                holder.llInOrOut.setVisibility(View.VISIBLE);
                holder.tvNoHave.setVisibility(View.VISIBLE);
                if (list.get(position).getIconName().equals("阿尔法")){
                    holder.ivIcon.setImageResource(R.mipmap.iv_aerfa_icon);
                }else  if (list.get(position).getIconName().equals("TAN 唐")){
                    holder.ivIcon.setImageResource(R.mipmap.iv_tan_icon);
                }else  if (list.get(position).getIconName().equals("BTC 比特币")){
                    holder.ivIcon.setImageResource(R.mipmap.iv_btc_icon);
                }else  if (list.get(position).getIconName().equals("ETH 以太坊")){
                    holder.ivIcon.setImageResource(R.mipmap.iv_eth_icon);
                }else  if (list.get(position).getIconName().equals("LTC 莱特币")){
                    holder.ivIcon.setImageResource(R.mipmap.iv_ltc_icon);
                }
            }

            holder.tvIn.setOnClickListener(new myClick(position));
            holder.tvOut.setOnClickListener(new myClick(position));
            holder.tvTx.setOnClickListener(new myClick(position));
            holder.tvCz.setOnClickListener(new myClick(position));
            holder.tvZd.setOnClickListener(new myClick(position));
            holder.tvZd2.setOnClickListener(new myClick(position));

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
        private TextView tvIconType;
        private TextView tvAccount;
        private TextView tvHave;
        private TextView tvNoHave;
        private TextView tvIn;
        private TextView tvOut;
        private TextView tvTx,tvCz,tvZd,tvZd2;
        private LinearLayout llInOrOut,llRmb;
        private ImageView ivIcon;

    }
}
