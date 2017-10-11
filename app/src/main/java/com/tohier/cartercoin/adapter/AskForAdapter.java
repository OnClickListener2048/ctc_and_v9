package com.tohier.cartercoin.adapter;

import android.app.Activity;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMCore;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWConversationCreater;
import com.alibaba.mobileim.conversation.YWMessage;
import com.alibaba.mobileim.conversation.YWMessageChannel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.activity.AddContactActivity;
import com.tohier.cartercoin.activity.AskForActivity;
import com.tohier.cartercoin.bean.ZanRanking;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.MyApplication;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;


public class AskForAdapter extends BaseAdapter {
    private ArrayList<ZanRanking> personList = new ArrayList<ZanRanking>();
    private Context context;

    public AskForAdapter(ArrayList<ZanRanking> personList, Context context) {
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
            convertView = View.inflate(context, R.layout.item_ask, null);//这个布局是每个名字上面都有一个首字母
            viewHolder = new ViewHolder();
            viewHolder.iv_pic = (CircleImageView) convertView.findViewById(R.id.iv_pic);
            viewHolder.tv_agree = (TextView) convertView.findViewById(R.id.tv_agree);
            viewHolder.tv_refuse = (TextView) convertView.findViewById(R.id.tv_refuse);
            viewHolder.ivSex = (ImageView) convertView.findViewById(R.id.iv_sex);
            viewHolder.ivVip = (ImageView) convertView.findViewById(R.id.iv_vip);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tvIndex = (TextView) convertView.findViewById(R.id.tv_index);
            viewHolder.tvLevel = (TextView) convertView.findViewById(R.id.tv_level);
            viewHolder.tvLevel1 = (TextView) convertView.findViewById(R.id.tv_level1);
            viewHolder.tvArea = (TextView) convertView.findViewById(R.id.tv_area);
            viewHolder.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
            viewHolder.ll_no = (LinearLayout) convertView.findViewById(R.id.ll_no);

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

        if (zanRanking.getStatus().equals("接受")){
            viewHolder.ll_no.setVisibility(View.VISIBLE);
            viewHolder.tv_state.setVisibility(View.GONE);
        }else if(zanRanking.getStatus().equals("已添加")){
            viewHolder.ll_no.setVisibility(View.GONE);
            viewHolder.tv_state.setVisibility(View.VISIBLE);
        }else if(zanRanking.getStatus().equals("已拒绝")){
            viewHolder.ll_no.setVisibility(View.GONE);
            viewHolder.tv_state.setVisibility(View.VISIBLE);
        }
        viewHolder.tv_state.setText(zanRanking.getStatus());

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

        MyOnClickListener myOnClickListener = new MyOnClickListener(position);
        viewHolder.tv_agree.setOnClickListener(myOnClickListener);
        viewHolder.tv_refuse.setOnClickListener(myOnClickListener);

        return convertView;
    }





    /**
     * 好友审批
     */
    private class MyOnClickListener implements View.OnClickListener{

        private int position ;
        String msg = "";
        String status = "";
        public MyOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.tv_agree){
                status = "1";
            }else if (v.getId() == R.id.tv_refuse){
                status = "2";
            }

                String zid = personList.get(position).getId();
                Map<String, String> map = new HashMap<String, String>();
                map.put("FromMemberID",zid);
                map.put("status",status);
                HttpConnect.post((AskForActivity)context,"member_friends_list_do" , map,
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

                                                context.getSharedPreferences("contact",0).edit().putString("isFresh","2").commit();
                                                if (status.equals("1")){
                                                    ((AskForActivity)context).sToast("添加成功");

                                                    //创建一条文本或者表情消息
                                                    YWMessage message = YWMessageChannel.createTextMessage("我们已经是宝友了，现在可以开始聊天了。");
                                                    YWIMCore imCore = YWAPI.createIMCore(LoginUser.getInstantiation(context.getApplicationContext()).getLoginUser().getLinkCode(), MyApplication.APP_KEY);
                                                    //创建一个与消息接收者的聊天会话，userId：表示聊天对象id
                                                    final YWConversationCreater conversationCreater = imCore.getConversationService().getConversationCreater();
                                                    YWConversation conversation = conversationCreater.createConversationIfNotExist(personList.get(position).getLinkCode());
                                                    //将消息发送给对方
                                                    conversation.getMessageSender().sendMessage(message, 120, null);
                                                    personList.get(position).setStatus("已添加");
                                                    notifyDataSetChanged();
                                                    Set<String> keys3 = MyApplication.maps.keySet();
                                                    if(keys3!=null&&keys3.size()>0)
                                                    {
                                                        if(keys3.contains("ContactListActivity"))
                                                        {
                                                            Activity activity = MyApplication.maps.get("ContactListActivity");
                                                            activity.finish();
                                                            MyApplication.deleteActivity("ContactListActivity");
                                                        };
                                                    }

                                                }else if (status.equals("2")){
                                                    ((AskForActivity)context).sToast("已拒绝添加");
                                                }
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
        TextView tvIndex ;
        TextView tvLevel ;
        TextView tvLevel1 ;
        TextView tvArea ;
        TextView tv_refuse;
        TextView tv_agree;
        TextView tv_state;
        LinearLayout ll_no;
    }

}
