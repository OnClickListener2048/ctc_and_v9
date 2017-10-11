package com.tohier.cartercoin.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.bean.ZanRanking;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/17.
 */

public class HorizontalListViewAdapter extends BaseAdapter {
    /** 上下文 */
    private Context mContext;
    private ArrayList<ZanRanking> personList = new ArrayList<ZanRanking>();

    public ArrayList<ZanRanking> getPersonList() {
        return personList;
    }

    public void setPersonList(ArrayList<ZanRanking> personList) {
        this.personList = personList;
    }

    /** 构造方法 */
    public HorizontalListViewAdapter(Context context,ArrayList<ZanRanking> personList) {
        this.mContext = context;
        this.personList = personList;

    }


    @Override
    public int getCount() {
        return personList.size();
    }

    @Override
    public Object getItem(int position) {
        return personList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.horizontal_list_item, null);
            holder.iv_pic = (ImageView) convertView
                    .findViewById(R.id.iv_pic);
            holder.tv_name = (TextView) convertView
                    .findViewById(R.id.tv_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ZanRanking zanRanking = personList.get(position);

        holder.tv_name.setText(zanRanking.getName());
        Glide.with(mContext).load(zanRanking.getPic()).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.iv_pic) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                holder.iv_pic.setImageDrawable(circularBitmapDrawable);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        /** 图像 */
        private ImageView iv_pic;
        private TextView tv_name;
    }
}