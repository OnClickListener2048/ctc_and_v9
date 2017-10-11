package com.tohier.cartercoin.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.activity.ContactListActivity;
import com.tohier.cartercoin.activity.FriendsInfoActivity;
import com.tohier.cartercoin.bean.ZanRanking;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 创 建 人:
 * 创建日期: 2017/3/29 15:35
 * 添加备注:
 */
public class QuickIndexAdapter extends BaseAdapter {
    private ArrayList<ZanRanking> personList = new ArrayList<ZanRanking>();
    private Context context;
    private TextView tvZan;

    /**
     * 1---到底了
     */
    private String isBottom;

    public String getIsBottom() {
        return isBottom;
    }

    public void setIsBottom(String isBottom) {
        this.isBottom = isBottom;
    }

    public QuickIndexAdapter(ArrayList<ZanRanking> personList, Context context, TextView tvZan) {
        this.personList = personList;
        this.context = context;
        this.tvZan = tvZan;
    }


    @Override
    public int getCount() {
        return personList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
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
                    convertView = View.inflate(context, R.layout.item_person, null);//这个布局是每个名字上面都有一个首字母
                    viewHolder = new ViewHolder();
                    viewHolder.iv_pic = (CircleImageView) convertView.findViewById(R.id.iv_pic);
                    viewHolder.iv_dianzan_pic = (ImageView) convertView.findViewById(R.id.iv_dianzan_pic);
                    viewHolder.ivSex = (ImageView) convertView.findViewById(R.id.iv_sex);
                    viewHolder.ivVip = (ImageView) convertView.findViewById(R.id.iv_vip);
                    viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                    viewHolder.tvIndex = (TextView) convertView.findViewById(R.id.tv_index);
                    viewHolder.tvLevel = (TextView) convertView.findViewById(R.id.tv_level);
                    viewHolder.tvLevel1 = (TextView) convertView.findViewById(R.id.tv_level1);
                    viewHolder.tvCount = (TextView) convertView.findViewById(R.id.tv_count);
                    viewHolder.tv_count1 = (TextView) convertView.findViewById(R.id.tv_count1);
                    viewHolder.ll_contact = (LinearLayout) convertView.findViewById(R.id.ll_contact);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                String currentStr = "";
                ZanRanking zanRanking = personList.get(position);
                if(zanRanking.getPinYin()!=null&&zanRanking.getPinYin().length()>0)
                {
                    currentStr = zanRanking.getPinYin().charAt(0) + "";
                }

//
//                if (isIndex.equals("0")){
//                    viewHolder.tvIndex.setVisibility( View.GONE);
//                }else{
//
//                    String index = null;
//                    if (position == 0) {
//                        index = currentStr;
//                    } else {
//                        String lastStr = "";
//                        if(personList.get(position - 1).getPinYin()!=null&&personList.get(position - 1).getPinYin().length()>0)
//                        {
//                            lastStr = personList.get(position - 1).getPinYin().charAt(0) + "";
//                        }
//                        if (!lastStr.equalsIgnoreCase(currentStr)) {
//                            index = currentStr;
//                        }
//                    }
//                    //首字母相同的姓氏只显示第一个名字上面的首字母,其他的隐藏掉
//                    viewHolder.tvIndex.setVisibility(index == null ? View.GONE : View.VISIBLE);
//
//                }

                viewHolder.tvIndex.setText(currentStr.toUpperCase());
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

//                if(isBottom.equals("1") && position+1 == personList.size()){
//                    if (personList.size()>=12){
//                        viewHolder.tv_count1.setText(personList.size()+"位宝友");
//                        viewHolder.tv_count1.setVisibility(View.VISIBLE);
//                    }else{
//                        viewHolder.tv_count1.setVisibility(View.GONE);
//                    }
//
//                }else{
//                    viewHolder.tv_count1.setVisibility(View.GONE);
//                }

                viewHolder.tvLevel1.setText(zanRanking.getLevel1());
                if (TextUtils.isEmpty(zanRanking.getCount())){
                    viewHolder.tvCount.setText(0);
                }else{
                    viewHolder.tvCount.setText(zanRanking.getCount());
                }


                String zanType = zanRanking.getZanType();

                if (zanType.equals("0")){
                    viewHolder.iv_dianzan_pic.setImageResource(R.mipmap.frend_dianzan0);
                }else if (zanType.equals("1")){
                    viewHolder.iv_dianzan_pic.setImageResource(R.mipmap.frend_dianzan1);
                }else if (zanType.equals("2")){
                    viewHolder.iv_dianzan_pic.setImageResource(R.mipmap.frend_dianzan2);
                }else if (zanType.equals("3")){
                    viewHolder.iv_dianzan_pic.setImageResource(R.mipmap.frend_dianzan3);
                }

                MyOnClickListener myOnClickListener = new MyOnClickListener(position,viewHolder.iv_dianzan_pic,viewHolder.tvCount);
                viewHolder.iv_dianzan_pic.setOnClickListener(myOnClickListener);
                viewHolder.iv_pic.setOnClickListener(myOnClickListener);
//                viewHolder.ll_contact.setOnClickListener(myOnClickListener);


        return convertView;
    }


    /**
     * 为同学点赞
     */
    private class MyOnClickListener implements View.OnClickListener{

        private int position ;
        private ImageView iv_dianzan_pic;
        private TextView tvCount;
        String msg = "";

        public MyOnClickListener(int position , ImageView iv_dianzan_pic , TextView tvCount) {
            this.position = position;
            this.iv_dianzan_pic = iv_dianzan_pic;
            this.tvCount = tvCount;
        }

        @Override
        public void onClick(final View v) {
                if (v.getId() == R.id.iv_dianzan_pic){
                    v.setClickable(false);
                    String zid = personList.get(position).getId();
                    String id =  LoginUser.getInstantiation(context.getApplicationContext()).getLoginUser().getUserId();
                    if (zid.equals(id)){
//                        ((FengYunRankingActivity)context).sToast("在自恋的路上你已经找不到对手了~");
                        return;
                    }
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("zid",zid);
                    HttpConnect.post((ContactListActivity)context,"member_friends_be_praise" , map,
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

                                                    String zanType = personList.get(position).getZanType();

                                                    if (zanType.equals("0")){
                                                        iv_dianzan_pic.setImageResource(R.mipmap.frend_dianzan2);
                                                        personList.get(position).setZanType("2");
                                                    }else if (zanType.equals("1")){
                                                        iv_dianzan_pic.setImageResource(R.mipmap.frend_dianzan2);
                                                        personList.get(position).setZanType("2");
                                                    }else if (zanType.equals("2")){
                                                        iv_dianzan_pic.setImageResource(R.mipmap.frend_dianzan2);
                                                        personList.get(position).setZanType("2");
                                                    }else if (zanType.equals("3")){
                                                        iv_dianzan_pic.setImageResource(R.mipmap.frend_dianzan0);
                                                        personList.get(position).setZanType("0");
                                                    }
                                                    if (!TextUtils.isEmpty(personList.get(position).getCount())){
                                                        tvCount.setText(Integer.parseInt(personList.get(position).getCount())+1+"");
                                                    }else{
                                                        tvCount.setText(1+"");
                                                    }

                                                    tvZan.setText(" "+(Integer.parseInt(tvZan.getText().toString().substring(1,tvZan.getText().toString().length()))+1));
                                                    personList.get(position).setCount(Integer.parseInt(personList.get(position).getCount())+1+"");
                                                    ScaleAnimation animation = (ScaleAnimation) AnimationUtils.loadAnimation(context, R.anim.scale);
                                                    iv_dianzan_pic.setAnimation(animation);

                                                    notifyDataSetChanged();
//                                                    ((ContactListActivity)context).sToast("点赞成功");
                                                    v.setClickable(true);
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
//                                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                                    }
                                                    v.setClickable(true);

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
//                                            ((ContactListActivity)context).sToast("请检查网络");
                                            v.setClickable(true);
                                        }
                                    };
                                    dataHandler.sendEmptyMessage(0);
                                }
                            });
                }else if (v.getId() == R.id.iv_pic){
                    ZanRanking zanRanking = personList.get(position);
                    Intent intent = new Intent(context, FriendsInfoActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable("zanRanking",zanRanking);
                    intent.putExtras(mBundle);
                    intent.putExtra("isFrend",0);
                    intent.putExtra("position",position);
                    context.startActivity(intent);
                }


        }
    }

    class ViewHolder
    {
        CircleImageView iv_pic;
        ImageView iv_dianzan_pic,ivSex,ivVip;
        TextView tvName;
        TextView tvIndex ;
        TextView tvLevel ;
        TextView tvLevel1;
        TextView tv_count1;
        TextView tvCount ;
        LinearLayout ll_contact;
    }

}
