package com.tohier.cartercoin.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tohier.cartercoin.R;

/**
 * Created by Administrator on 2017/6/9.
 */

public class PrizeListAdapter extends BaseAdapter{

    private String names[] = null;
    private Context contexts = null;

    public PrizeListAdapter(String[] names, Context contexts) {
        this.names = names;
        this.contexts = contexts;
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        return names[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            convertView = View.inflate(contexts, R.layout.lv_prize_item,null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.lv_prize_name);
        textView.setText(names[position]);
        return convertView;
    }

}
