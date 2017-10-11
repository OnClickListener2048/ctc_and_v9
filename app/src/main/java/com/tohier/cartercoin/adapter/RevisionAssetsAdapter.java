package com.tohier.cartercoin.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.activity.RevisionBuyAssetsActivity;
import com.tohier.cartercoin.bean.RevisionAssetsBean;
import com.tohier.cartercoin.config.DateDistance;
import com.tohier.cartercoin.listener.MyItemOnclick;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/11.
 */

public class RevisionAssetsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<RevisionAssetsBean> list = new ArrayList<RevisionAssetsBean>();
    boolean isFirst = true;
    public List<Handler> handlerList = new ArrayList<Handler>();
    public List<Thread> threadList = new ArrayList<Thread>();


    public List<Handler> getHandlerList() {
        return handlerList;
    }

    public List<Thread> getThreadList() {
        return threadList;
    }

    public RevisionAssetsAdapter(Context context, ArrayList<RevisionAssetsBean> list ) {
        this.context = context;
        this.list = list;
    }

    public ArrayList<RevisionAssetsBean> getList() {
        return list;
    }

    public void setList(ArrayList<RevisionAssetsBean> list) {
        this.list = list;
        isFirst = false;
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
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

//        ViewHolder viewHolder = null;
            convertView = View.inflate(context,R.layout.revision_buy_assets_lv_item,null);
////            viewHolder = new ViewHolder();
//            viewHolder.iv_shopping_icon = (ImageView) convertView.findViewById(R.id.iv_shopping_icon);
//            viewHolder.iv_miaosha_icon = (ImageView) convertView.findViewById(R.id.iv_miaosha_icon);
//            viewHolder.iv_shouqing_icon = (ImageView) convertView.findViewById(R.id.iv_shouqing_icon);
//            viewHolder.iv_time_tishi = (TextView) convertView.findViewById(R.id.iv_time_tishi);
//            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
//            viewHolder.tv_jindou = (TextView) convertView.findViewById(R.id.tv_jindou);
//            viewHolder.tv_yindou = (TextView) convertView.findViewById(R.id.tv_yindou);
//            viewHolder.iv_current_price = (TextView) convertView.findViewById(R.id.iv_current_price);
//            viewHolder.iv_duraction_price = (TextView) convertView.findViewById(R.id.iv_duraction_price);
//            viewHolder.tv_daojishi = (TextView) convertView.findViewById(R.id.tv_daojishi);
//            viewHolder.tv_xiangou_count = (TextView) convertView.findViewById(R.id.tv_xiangou_count);
//            viewHolder.tv_buy = (TextView) convertView.findViewById(R.id.tv_buy);
//            viewHolder.pro = (ProgressBar) convertView.findViewById(R.id.pro);
//            viewHolder.tv_percentage_sold = (TextView) convertView.findViewById(R.id.tv_percentage_sold);
//            convertView.setTag(viewHolder);
//        else
//        {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }


        try {
            ImageView iv_shopping_icon = (ImageView) convertView.findViewById(R.id.iv_shopping_icon);
            ImageView iv_miaosha_icon = (ImageView) convertView.findViewById(R.id.iv_miaosha_icon);
            ImageView iv_shouqing_icon = (ImageView) convertView.findViewById(R.id.iv_shouqing_icon);
            TextView iv_time_tishi = (TextView) convertView.findViewById(R.id.iv_time_tishi);
            TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            TextView tv_yindou = (TextView) convertView.findViewById(R.id.tv_yindou);
            TextView iv_current_price = (TextView) convertView.findViewById(R.id.iv_current_price);
            TextView iv_duraction_price = (TextView) convertView.findViewById(R.id.iv_duraction_price);
            TextView tv_daojishi = (TextView) convertView.findViewById(R.id.tv_daojishi);
            TextView tv_xiangou_count = (TextView) convertView.findViewById(R.id.tv_xiangou_count);
            TextView tv_buy = (TextView) convertView.findViewById(R.id.tv_buy);
            ProgressBar pro = (ProgressBar) convertView.findViewById(R.id.pro);
            TextView tv_percentage_sold = (TextView) convertView.findViewById(R.id.tv_percentage_sold);
            TextView tv_jindou = (TextView) convertView.findViewById(R.id.tv_jindou);


            if(!TextUtils.isEmpty(list.get(position).getName()))
            {
                tv_name.setText(list.get(position).getName());
            }

            if(!TextUtils.isEmpty(list.get(position).getPic()))
            {
                Glide.with(context).load(list.get(position).getPic()).placeholder(null)
                        .error(null).into( iv_shopping_icon);
            }

            if(!TextUtils.isEmpty(list.get(position).getGold()))
            {
                tv_jindou.setText("金豆:"+list.get(position).getGold()+"个");
            }


            if(!TextUtils.isEmpty(list.get(position).getSilver()))
            {
                tv_yindou.setText("银豆:"+list.get(position).getSilver()+"个");
            }

            if(!TextUtils.isEmpty(list.get(position).getReductionPrice()))
            {
                iv_current_price.setText(list.get(position).getReductionPrice()+"元");
            }

            if(!TextUtils.isEmpty(list.get(position).getPrice()))
            {
                if(!TextUtils.isEmpty(list.get(position).getType())&&list.get(position).getType().equals("0"))
                {
                    iv_duraction_price.setText(list.get(position).getPrice()+"元");
                    iv_duraction_price.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG); //中划线
                }else
                {
                    iv_duraction_price.setText("");
                }
            }

            if(!TextUtils.isEmpty(list.get(position).getStatus()))
            {
                if(list.get(position).getStatus().equals("已结束"))
                {
                    int pre = Integer.parseInt(list.get(position).getPre());
                    if(pre<=0) //售罄
                    {
                        iv_shouqing_icon.setVisibility(View.VISIBLE);
                    }
                    tv_buy.setText("已结束");
                    tv_buy.setBackgroundResource(R.drawable.bg_shape_666666);
                    iv_time_tishi.setVisibility(View.GONE);
                    if(!TextUtils.isEmpty(list.get(position).getCou()))
                    {
                        tv_xiangou_count.setText("已售"+list.get(position).getCou()+"份");
                    }
                    pro.setVisibility(View.VISIBLE);
                    tv_percentage_sold.setVisibility(View.VISIBLE);
                    if(!TextUtils.isEmpty(list.get(position).getQty()))
                    {
                        pro.setMax(Integer.parseInt(list.get(position).getQty()));
                    }
                    if(!TextUtils.isEmpty(list.get(position).getCou()))
                    {
                        pro.setProgress(Integer.parseInt(list.get(position).getCou()));
                    }

                    if(!TextUtils.isEmpty(list.get(position).getPro()))
                    {
                        tv_percentage_sold.setText(removeDecimalPoint(Double.parseDouble(list.get(position).getPro())*100+"")+"%");
                    }

                    //设置卖出百分比
                    //TODO
                }else if(list.get(position).getStatus().equals("未开始"))
                {
                    if(list.get(position).getType().equals("0"))
                    {
                        iv_miaosha_icon.setVisibility(View.VISIBLE);
                    }
                    if(!TextUtils.isEmpty(list.get(position).getPre()))
                    {
                        tv_xiangou_count.setText("限购"+list.get(position).getPre()+"份"); //剩余多少
                    }
                    iv_time_tishi.setVisibility(View.VISIBLE);
                    iv_time_tishi.setText("即将开始");
                    tv_buy.setText("等待...");
                    tv_buy.setBackgroundResource(R.drawable.bg_shape_666666);
                    //开启倒计时
                    //TODO
                    if(list.get(position).getIstart()<=0) //活动开始
                    {
                        //1.展示 秒杀icon
                        //1.展示 秒杀icon
                        tv_daojishi.setVisibility(View.GONE);
                        //2.展示 秒杀提示语
                        iv_time_tishi.setVisibility(View.VISIBLE);
                        iv_time_tishi.setText("正在抢购中");
                        //3.展示 progress 卖出百分比
                        // TODO 设置百分比
                        pro.setVisibility(View.VISIBLE);
                        tv_percentage_sold.setVisibility(View.VISIBLE);
                        //4.展示 剩余数量
                        if(!TextUtils.isEmpty(list.get(position).getPre()))
                        {
                            tv_xiangou_count.setText("仅剩"+list.get(position).getPre()+"份"); //剩余多少
                        }
                        //5.改变 按钮风格 并且可以点击
                        tv_buy.setBackgroundResource(R.drawable.bg_shape_fe3338);
                        tv_buy.setText("马上抢");

                        if(!TextUtils.isEmpty(list.get(position).getPro()))
                        {
                            tv_percentage_sold.setText(removeDecimalPoint(Double.parseDouble(list.get(position).getPro())*100+"")+"%");
                        }
                    }else   //未开始
                    {
                        tv_daojishi.setVisibility(View.VISIBLE);
                        long tempTime = list.get(position).getIstart();
                        long day = tempTime / (24 * 60 * 60 * 1000);
                        long hour = (tempTime / (60 * 60 * 1000) - day * 24);
                        long min = ((tempTime / (60 * 1000)) - day * 24 * 60 - hour * 60);
                        long sec = (tempTime / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
                        if(String.valueOf(hour).length()==1)
                        {
                            if(String.valueOf(min).length()==1&&String.valueOf(sec).length()==1)
                            {
                                tv_daojishi.setText(day + "天0" + hour + ":0" + min + ":0" + sec);
                            }else if(String.valueOf(min).length()==1)
                            {
                                tv_daojishi.setText(day + "天0" + hour + ":0" + min + ":" + sec);
                            }else if(String.valueOf(sec).length()==1)
                            {
                                tv_daojishi.setText(day + "天0" + hour + ":" + min + ":0" + sec);
                            }else
                            {
                                tv_daojishi.setText(day + "天0" + hour + ":" + min + ":" + sec);
                            }
                        }else
                        {
                            if(String.valueOf(min).length()==1&&String.valueOf(sec).length()==1)
                            {
                                tv_daojishi.setText(day + "天" + hour + ":0" + min + ":0" + sec);
                            }else if(String.valueOf(min).length()==1)
                            {
                                tv_daojishi.setText(day + "天" + hour + ":0" + min + ":" + sec);
                            }else if(String.valueOf(sec).length()==1)
                            {
                                tv_daojishi.setText(day + "天" + hour + ":" + min + ":0" + sec);
                            }else
                            {
                                tv_daojishi.setText(day + "天" + hour + ":" + min + ":" + sec);
                            }
                        }
                    }
                }else if(list.get(position).getStatus().equals("抢购中"))
                {
                    tv_daojishi.setVisibility(View.GONE);
                    if(!TextUtils.isEmpty(list.get(position).getPre()))
                    {
                        int pre = Integer.parseInt(list.get(position).getPre());
                        if(pre<=0) //售罄
                        {
                            iv_time_tishi.setVisibility(View.GONE);
                            iv_shouqing_icon.setVisibility(View.VISIBLE);
                            tv_buy.setText("已结束");
                            tv_buy.setBackgroundResource(R.drawable.bg_shape_666666);
                            if(!TextUtils.isEmpty(list.get(position).getCou()))
                            {
                                tv_xiangou_count.setText("已售"+list.get(position).getCou()+"份");
                            }
                            pro.setVisibility(View.VISIBLE);
                            tv_percentage_sold.setVisibility(View.VISIBLE);
                            if(!TextUtils.isEmpty(list.get(position).getQty()))
                            {
                                pro.setMax(Integer.parseInt(list.get(position).getQty()));
                            }
                            if(!TextUtils.isEmpty(list.get(position).getCou()))
                            {
                                pro.setProgress(Integer.parseInt(list.get(position).getCou()));
                            }
                            //设置卖出百分比
                            //TODO
                            if(!TextUtils.isEmpty(list.get(position).getPro()))
                            {
                                tv_percentage_sold.setText(removeDecimalPoint(Double.parseDouble(list.get(position).getPro())*100+"")+"%");
                            }

                        }else    // 表示还有商品 还在抢购
                        {
                            iv_shouqing_icon.setVisibility(View.GONE);
                            //1.展示 秒杀icon
                            if(list.get(position).getType().equals("0"))
                            {
                                iv_miaosha_icon.setVisibility(View.VISIBLE);
                            }
                            //2.展示 秒杀提示语
                            iv_time_tishi.setVisibility(View.VISIBLE);
                            iv_time_tishi.setText("正在抢购中");
                            //3.展示 progress 卖出百分比
                            // TODO 设置百分比
                            pro.setVisibility(View.VISIBLE);
                            //4.展示 剩余数量
                            if(!TextUtils.isEmpty(list.get(position).getPre()))
                            {
                                tv_xiangou_count.setText("仅剩"+list.get(position).getPre()+"份"); //剩余多少
                            }
                            //5.改变 按钮风格 并且可以点击
                            tv_buy.setBackgroundResource(R.drawable.bg_shape_fe3338);
                            tv_buy.setText("马上抢");

                            tv_percentage_sold.setVisibility(View.VISIBLE);
                            if(!TextUtils.isEmpty(list.get(position).getQty()))
                            {
                                pro.setMax(Integer.parseInt(list.get(position).getQty()));
                            }
                            if(!TextUtils.isEmpty(list.get(position).getCou()))
                            {
                                pro.setProgress(Integer.parseInt(list.get(position).getCou()));
                            }

                            if(!TextUtils.isEmpty(list.get(position).getPro()))
                            {
                                tv_percentage_sold.setText(removeDecimalPoint(Double.parseDouble(list.get(position).getPro())*100+"")+"%");
                            }
                        }
                    }

                }
            }

            if(!TextUtils.isEmpty(tv_buy.getText()))
            {
                if(tv_buy.getText().toString().trim().equals("已结束"))
                {
                    tv_buy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((RevisionBuyAssetsActivity)context).sToast("已售罄");
                        }
                    });
                }else if(tv_buy.getText().toString().trim().equals("等待..."))
                {
                    tv_buy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((RevisionBuyAssetsActivity)context).sToast("未到售卖时间");
                        }
                    });
                }else
                {
                    tv_buy.setOnClickListener(new myClick(position));
                }
            }
        }catch (Exception e)
        {

        }


        return convertView;
    }

    int result = 0;
    private Thread thread;

    public void start() {
        thread = new Thread() {
            public void run() {
                while (true) {
                    try {
                        if (list == null || result == list.size()) {
                            break;
                        }
                        sleep(100);
                        for (RevisionAssetsBean revisionAssetsBean : list) {
                                    if(revisionAssetsBean.getIstart()<=0) //活动开始
                                    {

                                    }else   //未开始
                                    {
                                        revisionAssetsBean.setIstart(revisionAssetsBean.getIstart()-100);
                                    }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }


    /**
     * 百分比可能会有小数 此方法去除小数
     * @param str
     * @return
     */
    public String  removeDecimalPoint(String str)
    {
        if(str.contains("."))
        {
            int count = str.indexOf(".");
            return str.substring(0,count);
        }
        return str;
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
//
//    private class ViewHolder{
//        private ImageView iv_shopping_icon;
//        private ImageView iv_miaosha_icon;
//        private ImageView iv_shouqing_icon;
//        private TextView iv_time_tishi;
//        private TextView tv_name;
//        private TextView tv_jindou;
//        private TextView tv_yindou;
//        private TextView iv_current_price;
//        private TextView iv_duraction_price;
//        private ProgressBar pro;
//        private TextView tv_daojishi;
//        private TextView tv_xiangou_count;
//        private TextView tv_buy;
//        private TextView tv_percentage_sold;
//    }
}
