package com.tohier.cartercoin.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.bean.MyActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/12/22.
 */

public class ImageAdapter extends BaseAdapter {

    private Context context;
    private List<MyActivity> datas = new ArrayList<MyActivity>();

    public ImageAdapter(Context context, List<MyActivity> datas) {
        this.context = context;
        this.datas = datas;
    }

    public void setDatas(List<MyActivity> datas) {
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
              convertView = View.inflate(context, R.layout.layout_image_item,null);
              viewHolder = new ViewHolder();

              viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_activity);
              viewHolder.imageView1 = (ImageView) convertView.findViewById(R.id.iv_end);
              convertView.setTag(viewHolder);
          }else
          {
              viewHolder = (ViewHolder) convertView.getTag();
          }

        if(!TextUtils.isEmpty(datas.get(position).getImageUrl()))
        {
            Glide.with(context).load(datas.get(position).getImageUrl()).placeholder(null)
                    .error(null).into( viewHolder.imageView);
        }

        long date1 = System.currentTimeMillis();
        try {
            long date2 = Long.parseLong(dateToStamp(datas.get(position).getEndDate()));

            if (date1>=date2){
                viewHolder.imageView1.setVisibility(View.VISIBLE);
            }else{
                viewHolder.imageView1.setVisibility(View.GONE);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }



        return convertView;
    }

    class ViewHolder
    {
        ImageView imageView,imageView1;
    }


    /*
* 将时间转换为时间戳
*/
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }
}
