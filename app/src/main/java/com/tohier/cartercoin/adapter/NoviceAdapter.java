package com.tohier.cartercoin.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tohier.cartercoin.R;
import com.tohier.cartercoin.bean.Novice;

import java.util.ArrayList;

import static com.tohier.cartercoin.R.id.iv_jiao;


/**
 * Created by Administrator on 2017/5/16.
 */

public class NoviceAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Novice> datas;

    public NoviceAdapter(Context context,ArrayList<Novice> datas) {
        this.context = context;
        this.datas = datas;
    }

    public void setDatas(ArrayList<Novice> datas) {
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
            convertView = View.inflate(context, R.layout.layout_novice_item,null);
            viewHolder = new ViewHolder();
            viewHolder.tv_index = (TextView) convertView.findViewById(R.id.tv_index);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_desc = (TextView) convertView.findViewById(R.id.tv_desc);
            viewHolder.tv_strength = (TextView) convertView.findViewById(R.id.tv_strength);
            viewHolder.iv_jiao = (ImageView) convertView.findViewById(iv_jiao);
            viewHolder.iv_state = (ImageView) convertView.findViewById(R.id.iv_state);
            convertView.setTag(viewHolder);
        }else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_index.setText(position+1+"");
        viewHolder.tv_title.setText(datas.get(position).getTitle());
        viewHolder.tv_desc.setText(datas.get(position).getDesc());
        viewHolder.tv_strength.setText("+ "+datas.get(position).getStrength()+"体力");
        String state = datas.get(position).getState();
        if(position == 0){
            if (state.equals("0")){//显示体力
                viewHolder.iv_state.setImageResource(R.mipmap.xinshouzhiyin_red);
                viewHolder.tv_strength.setVisibility(View.VISIBLE);
            }else{
                viewHolder.iv_state.setImageResource(R.mipmap.xinshouzhiyin_red_pre);
                viewHolder.tv_strength.setVisibility(View.GONE);
            }
            viewHolder.tv_index.setBackgroundResource(R.mipmap.xinshouzhiyin_red_1);
            viewHolder.iv_jiao.setImageResource(R.mipmap.xinshouzhiyin_red1_jiao);
        }else if(position == 1){
            if (state.equals("0")){
                viewHolder.iv_state.setImageResource(R.mipmap.xinshouzhiyin_yelow);
                viewHolder.tv_strength.setVisibility(View.VISIBLE);
            }else{
                viewHolder.iv_state.setImageResource(R.mipmap.xinshouzhiyin_yelow_pre);
                viewHolder.tv_strength.setVisibility(View.GONE);
            }
            viewHolder.tv_index.setBackgroundResource(R.mipmap.xinshouzhiyin_yelow_1);
            viewHolder.iv_jiao.setImageResource(R.mipmap.xinshouzhiyin_yelow_1_jiao);
        }else if(position == 2){
            if (state.equals("0")){
                viewHolder.iv_state.setImageResource(R.mipmap.xinshouzhiyin_blue);
                viewHolder.tv_strength.setVisibility(View.VISIBLE);
            }else{
                viewHolder.iv_state.setImageResource(R.mipmap.xinshouzhiyin_blue_pre);
                viewHolder.tv_strength.setVisibility(View.GONE);
            }
            viewHolder.tv_index.setBackgroundResource(R.mipmap.xinshouzhiyin_blue_1);
            viewHolder.iv_jiao.setImageResource(R.mipmap.xinshouzhiyin_blue_1_jiao);
        }else if(position == 3){
            if (state.equals("0")){
                viewHolder.iv_state.setImageResource(R.mipmap.xinshouzhiyin_green);
                viewHolder.tv_strength.setVisibility(View.VISIBLE);
            }else{
                viewHolder.iv_state.setImageResource(R.mipmap.xinshouzhiyin_green_pre);
                viewHolder.tv_strength.setVisibility(View.GONE);
            }
            viewHolder.tv_index.setBackgroundResource(R.mipmap.xinshouzhiyin_green_1);
            viewHolder.iv_jiao.setImageResource(R.mipmap.xinshouzhiyin_green_1_jiao);
        }else if(position == 4){
            if (state.equals("0")){
                viewHolder.iv_state.setImageResource(R.mipmap.xinshouzhiyin_purple);
                viewHolder.tv_strength.setVisibility(View.VISIBLE);
            }else{
                viewHolder.iv_state.setImageResource(R.mipmap.xinshouzhiyin_purple_pre);
                viewHolder.tv_strength.setVisibility(View.GONE);
            }
            viewHolder.tv_index.setBackgroundResource(R.mipmap.xinshouzhiyin_purple_1);
            viewHolder.iv_jiao.setImageResource(R.mipmap.xinshouzhiyin_purple_1_jiao);
        }else if(position == 5){
            if (state.equals("0")){
                viewHolder.iv_state.setImageResource(R.mipmap.xinshouzhiyin_red);
                viewHolder.tv_strength.setVisibility(View.VISIBLE);
            }else{
                viewHolder.iv_state.setImageResource(R.mipmap.xinshouzhiyin_red_pre);
                viewHolder.tv_strength.setVisibility(View.GONE);
            }
            viewHolder.tv_index.setBackgroundResource(R.mipmap.xinshouzhiyin_red_1);
            viewHolder.iv_jiao.setVisibility(View.GONE);
        }

        return convertView;
    }

    class ViewHolder
    {
        TextView tv_index,tv_title,tv_desc,tv_strength;
        ImageView iv_jiao,iv_state;
    }

}
