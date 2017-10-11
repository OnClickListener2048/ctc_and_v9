package com.tohier.cartercoin.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.bean.ShareBonus;
import com.tohier.cartercoin.columnview.LoadingView;
import com.tohier.cartercoin.columnview.NoDataView;
import com.tohier.cartercoin.columnview.NoLinkView;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.Tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ShareBonusRecordActivity extends MyBackBaseActivity {

    private ImageView iv_back;
    private ExpandableListView lv_share_bnous;
    private NoDataView noDataView;
    private NoLinkView noLinkView;
    private LoadingView loadingView;

    private ArrayList<ShareBonus> list = new ArrayList<ShareBonus>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_bonus_record);

        init();
        setUp();

    }

    private void init() {
        iv_back = (ImageView) findViewById(R.id.iv_back2);
        lv_share_bnous = (ExpandableListView ) findViewById(R.id.lv_share_bonus);
        noDataView = (NoDataView) findViewById(R.id.iv_nodata);
        noLinkView = (NoLinkView) findViewById(R.id.iv_no_link);
        loadingView = (LoadingView) findViewById(R.id.gif_loading);

        lv_share_bnous.setGroupIndicator(null);

        if (Tools.getAPNType(ShareBonusRecordActivity.this) == true){
            noLinkView.setVisibility(View.GONE);

            getJsonDate();
        }else{
            noLinkView.setVisibility(View.VISIBLE);
        }
    }



    private void setUp() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        noLinkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tools.getAPNType(ShareBonusRecordActivity.this) == true){
                    noLinkView.setVisibility(View.GONE);
                    getJsonDate();

                }else{
                    noLinkView.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    private void getJsonDate() {

        loadingView.setVisibility(View.VISIBLE);
        HttpConnect.post(ShareBonusRecordActivity.this, "member_bouns_profit_list", null, new Callback() {

            private String msg;

            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject data = JSONObject.fromObject(arg0.body()
                        .string().trim());

                msg = data.getString("msg");

                if (data.get("status").equals("success")) {
                    JSONArray jsonArray = data.getJSONArray("data");
                    if (jsonArray != null) {
                        ShareBonus shareBonus = null;
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                            if (jsonObject2 != null) {
                                final String money = jsonObject2.getString("Money");
                                final String ctcMoney = jsonObject2.getString("CtcMoney");
                                final String date = jsonObject2.getString("CreateData");
                                final String cirMoney = jsonObject2.getString("CirMoney");
                                final String percentage = jsonObject2.getString("Percentage");
                                final String allBounsProfit  = jsonObject2.getString("AllBounsProfit");
                                shareBonus = new ShareBonus(money,date,ctcMoney,cirMoney,percentage,allBounsProfit);
                                list.add(shareBonus);
                            }
                        }
                    }

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingView.setVisibility(View.GONE);
                        lv_share_bnous.setAdapter(new MyAdapter());
                        if (list.size()>0){
                            noDataView.setVisibility(View.GONE);
                        }else{
                            noDataView.setVisibility(View.VISIBLE);
                        }

                    }
                });
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        loadingView.setVisibility(View.GONE);
                        if (list.size()>0){
                            noDataView.setVisibility(View.GONE);
                        }else{
                            noDataView.setVisibility(View.VISIBLE);
                        }
                    }
                });

            }
        });
    }





    class MyAdapter extends BaseExpandableListAdapter {

        //得到子item需要关联的数据
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return list.get(groupPosition);
        }

        //得到子item的ID
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return groupPosition;
        }

        //设置子item的组件
        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) ShareBonusRecordActivity.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.layout_children, null);
            }
            TextView tv_α_me = (TextView) convertView.findViewById(R.id.tv_α_me);
            TextView tv_α_all = (TextView) convertView.findViewById(R.id.tv_α_all);
            TextView tv_α_rate = (TextView) convertView.findViewById(R.id.tv_α_rate);
            TextView tv_money_all = (TextView) convertView.findViewById(R.id.tv_money_all);
            tv_α_me.setText("我的阿尔法："+list.get(groupPosition).getΑ_me());
            tv_α_all.setText("流通阿尔法："+list.get(groupPosition).getΑ_all());
            tv_α_rate.setText("我的占比："+list.get(groupPosition).getRate());
            tv_money_all.setText("全站收益总计："+list.get(groupPosition).getMoneyAll());

            return convertView;
        }

        //获取当前父item下的子item的个数
        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }
        //获取当前父item的数据
        @Override
        public Object getGroup(int groupPosition) {
            return list.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return list.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }
        //设置父item组件
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) ShareBonusRecordActivity.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.layout_parent, null);
            }
            TextView tv_money = (TextView) convertView.findViewById(R.id.tv_money);
            TextView tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            ImageView iv_jiantou = (ImageView) convertView.findViewById(R.id.iv_jiantou);
            tv_money.setText(list.get(groupPosition).getMoney()+"RMB");
            tv_time.setText(list.get(groupPosition).getTime().split(" ")[0]);
            if(isExpanded){//up
                iv_jiantou.setImageResource(R.mipmap.share_bonus_up);
            }else {//down
                iv_jiantou.setImageResource(R.mipmap.share_bonus_down);
            }
            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    }

}
