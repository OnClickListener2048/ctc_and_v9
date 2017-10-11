package com.tohier.cartercoin.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.tohier.cartercoin.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/11.
 */

public class MyGoodsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> list = new ArrayList<String>();
    private  ViewHolder1 holder1;


    public MyGoodsAdapter(Context context, ArrayList<String> list ) {
        this.context = context;
        this.list = list;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
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


        if (position == 0){
            if (convertView == null) {
                holder1 = new ViewHolder1();
                convertView  = View.inflate(context,R.layout.layout1,null);
                holder1.viewFlipper = (ViewFlipper) convertView.findViewById(R.id.vf_goods);
                convertView.setTag(holder1);
            } else {
                holder1 = (ViewHolder1) convertView.getTag();
            }

            ImageView imageView = new ImageView(context);
            imageView.setId(0);
            imageView.setImageResource(R.mipmap.guide01);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT));
            ImageView imageView1 = new ImageView(context);
            imageView1.setId(0);
            imageView1.setLayoutParams(new ViewGroup.LayoutParams(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT));
            imageView1.setImageResource(R.mipmap.guide02);
            ImageView imageView2 = new ImageView(context);
            imageView2.setId(0);
            imageView2.setLayoutParams(new ViewGroup.LayoutParams(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT));
            imageView2.setImageResource(R.mipmap.guide03);
//            imageView2.setBackgroundResource(R.mipmap.guide03);
            holder1.viewFlipper.addView(imageView);
            holder1.viewFlipper.addView(imageView1);
            holder1.viewFlipper.addView(imageView2);

            holder1.viewFlipper.startFlipping();



        }else{
            if (convertView == null) {
                holder = new ViewHolder();
                convertView  = View.inflate(context,R.layout.layout,null);
                holder.gridView = (GridView) convertView.findViewById(R.id.gv_goods);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

        }

        return convertView;
    }


    private class ViewHolder{
        private GridView gridView;

    }

    private class ViewHolder1{
        private ViewFlipper viewFlipper;

    }
}
