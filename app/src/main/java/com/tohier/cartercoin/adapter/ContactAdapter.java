package com.tohier.cartercoin.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.activity.AddContactActivity;
import com.tohier.cartercoin.bean.ZanRanking;
import com.tohier.cartercoin.config.HttpConnect;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 创 建 人: 路好营
 * 创建日期: 2017/3/29 15:35
 * 添加备注:
 */
public class ContactAdapter extends BaseAdapter {
    private ArrayList<ZanRanking> personList = new ArrayList<ZanRanking>();
    private Context context;

    public ContactAdapter(ArrayList<ZanRanking> personList, Context context) {
        this.personList = personList;
        this.context = context;
    }


    @Override
    public int getCount() {
        return personList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void setPersonList(ArrayList<ZanRanking> personList) {
        this.personList = personList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_person1, null);//这个布局是每个名字上面都有一个首字母
            viewHolder = new ViewHolder();
            viewHolder.iv_pic = (CircleImageView) convertView.findViewById(R.id.iv_pic);
            viewHolder.tvAddContact = (TextView) convertView.findViewById(R.id.tv_add_contact1);
            viewHolder.tvAddContact1 = (TextView) convertView.findViewById(R.id.tv_add_contact);
            viewHolder.ivSex = (ImageView) convertView.findViewById(R.id.iv_sex);
            viewHolder.ivVip = (ImageView) convertView.findViewById(R.id.iv_vip);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tvIndex = (TextView) convertView.findViewById(R.id.tv_index);
            viewHolder.tvLevel = (TextView) convertView.findViewById(R.id.tv_level);
            viewHolder.tvLevel1 = (TextView) convertView.findViewById(R.id.tv_level1);
            viewHolder.tvArea = (TextView) convertView.findViewById(R.id.tv_area);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        ZanRanking zanRanking = personList.get(position);

        viewHolder.tvName.setText(zanRanking.getName());
        Glide.with(context).load(zanRanking.getPic()).asBitmap().centerCrop().into(new BitmapImageViewTarget(viewHolder.iv_pic) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                viewHolder.iv_pic.setImageDrawable(circularBitmapDrawable);
            }
        });

        String type = zanRanking.getType();
        viewHolder.tvLevel.setText(type);

        if (type.equals("注册会员")){
            viewHolder.ivVip.setImageResource(R.mipmap.iv_putong_memeber);
        }else if(type.equals("铂金会员")){
            viewHolder.ivVip.setImageResource(R.mipmap.iv_baijin_member);
        }else if(type.equals("钻石会员")){
            viewHolder.ivVip.setImageResource(R.mipmap.iv_zhuanshi_member);
        }else if(type.equals("皇冠会员")){
            viewHolder.ivVip.setImageResource(R.mipmap.iv_huangguan_member);
        }

        if (zanRanking.getSex().equals("美女")){
            viewHolder.ivSex.setVisibility(View.VISIBLE);
            viewHolder.ivSex.setImageResource(R.mipmap.girl);
        }else if (zanRanking.getSex().equals("帅哥")){
            viewHolder.ivSex.setImageResource(R.mipmap.boy);
            viewHolder.ivSex.setVisibility(View.VISIBLE);
        }else {
            viewHolder.ivSex.setVisibility(View.GONE);
        }


        viewHolder.tvLevel1.setText(zanRanking.getLevel1());
        viewHolder.tvArea.setText(zanRanking.getArea());

        if (zanRanking.getSta().equals("0")){
            viewHolder.tvAddContact.setVisibility(View.VISIBLE);
            viewHolder.tvAddContact1.setVisibility(View.GONE);
            MyOnClickListener myOnClickListener = new MyOnClickListener(position,viewHolder.tvAddContact1,viewHolder.tvAddContact);
            viewHolder.tvAddContact.setOnClickListener(myOnClickListener);
        }else if (zanRanking.getSta().equals("1")){
            viewHolder.tvAddContact.setVisibility(View.GONE);
            viewHolder.tvAddContact1.setVisibility(View.VISIBLE);
            viewHolder.tvAddContact1.setText("已添加");
            viewHolder.tvAddContact1.setTextColor(0xff808080);

        }else if (zanRanking.getSta().equals("2")){
            viewHolder.tvAddContact.setVisibility(View.GONE);
            viewHolder.tvAddContact1.setVisibility(View.VISIBLE);
            viewHolder.tvAddContact1.setText("等待验证");
            viewHolder.tvAddContact1.setTextColor(0xff808080);
        }

        return convertView;
    }





    /**
     * 添加宝友
     */
    private class MyOnClickListener implements View.OnClickListener{

        private int position ;
        private TextView iv_dianzan_pic,tvContact;
        String msg = "";

        public MyOnClickListener(int position , TextView iv_dianzan_pic , TextView tvContact){
            this.position = position;
            this.iv_dianzan_pic = iv_dianzan_pic;
            this.tvContact = tvContact;
        }

        @Override
        public void onClick(View v) {

                String zid = personList.get(position).getId();
                Map<String, String> map = new HashMap<String, String>();
                map.put("tomemberid",zid);
                HttpConnect.post((AddContactActivity)context,"member_add_friends" , map,
                        new Callback() {
                            @Override
                            public void onResponse(Response arg0)
                                    throws IOException {

                                if (arg0 != null || !arg0.equals("")) {

                                    final JSONObject object = JSONObject.fromObject(arg0.body().string());
                                    msg = object.getString("msg");

                                    if (object.get("status").equals("success")) {
                                        Handler dataHandler = new Handler(
                                                context.getMainLooper()) {
                                            @Override
                                            public void handleMessage(
                                                    final Message msg) {

                                                tvContact.setVisibility(View.GONE);
                                                iv_dianzan_pic.setVisibility(View.VISIBLE);
                                                iv_dianzan_pic.setText("等待验证");
                                                personList.get(position).setSta("2");
                                                notifyDataSetChanged();

//                                                ((AddContactActivity)context).sToast("申请已发送");

                                            }
                                        };
                                        dataHandler.sendEmptyMessage(0);
                                    }else {
                                        Handler dataHandler = new Handler(
                                                context.getMainLooper()) {
                                            @Override
                                            public void handleMessage(
                                                    final Message msg1) {
                                                if (!TextUtils.isEmpty(msg)){
                                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        };
                                        dataHandler.sendEmptyMessage(0);
                                    }
                                }
                            }


                            @Override
                            public void onFailure(Request arg0, IOException arg1) {

                                Handler dataHandler = new Handler(
                                        context.getMainLooper()) {

                                    @Override
                                    public void handleMessage(
                                            final Message msg) {
//                                        ((AddContactActivity)context).sToast("请检查网络");
                                    }
                                };
                                dataHandler.sendEmptyMessage(0);
                            }
                        });

        }
    }

    class ViewHolder
    {
        CircleImageView iv_pic;
        ImageView ivSex,ivVip;
        TextView tvName;
        TextView tvAddContact,tvAddContact1;
        TextView tvIndex ;
        TextView tvLevel ;
        TextView tvLevel1 ;
        TextView tvArea ;
    }

}
