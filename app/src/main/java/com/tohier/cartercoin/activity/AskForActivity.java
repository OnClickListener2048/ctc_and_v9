package com.tohier.cartercoin.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.adapter.AskForAdapter;
import com.tohier.cartercoin.bean.ZanRanking;
import com.tohier.cartercoin.columnview.LoadingView;
import com.tohier.cartercoin.columnview.NoDataView;
import com.tohier.cartercoin.columnview.NoLinkView;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.MyApplication;
import com.tohier.cartercoin.config.Tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AskForActivity extends MyBaseActivity {

    private ArrayList<ZanRanking> list_data1 = new ArrayList<ZanRanking>();
    private ListView listView;
    private AskForAdapter adapter;
    private LoadingView cif_loading;
    private NoDataView ivNodata;
    private NoLinkView ivNoLink;

    private SharedPreferences sharedPreferences;
    PopupWindow window;
    View contentView;
    private TextView tv_add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_for);

        init();

//        SliderConfig mConfig = new SliderConfig.Builder()
//                .primaryColor(Color.TRANSPARENT)
//                .secondaryColor(Color.TRANSPARENT)
//                .position(SliderPosition.LEFT)
//                .edge(false)
//                .build();
//
//        ISlider iSlider = SliderUtils.attachActivity(this, mConfig);
//        mConfig.setPosition(SliderPosition.LEFT);
//        iSlider.setConfig(mConfig);
    }

    private void init() {
        contentView = View.inflate(AskForActivity.this, R.layout.layout_del, null);

        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        window = new PopupWindow(contentView,
                width-120, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        sharedPreferences = getSharedPreferences("contact", 0);

        listView = (ListView) findViewById(R.id.lv_ask);

        ivNodata = (NoDataView) findViewById(R.id.iv_nodata);
        cif_loading  = (LoadingView) findViewById(R.id.cif_loading);
        ivNoLink = (NoLinkView) findViewById(R.id.iv_no_link);

        tv_add = (TextView) findViewById(R.id.tv_add);
        adapter = new AskForAdapter(list_data1, this);
        listView.setAdapter(adapter);


        if (Tools.getAPNType(AskForActivity.this) == true){
            ivNoLink.setVisibility(View.GONE);
            getAskFors();
        }else{
            ivNoLink.setVisibility(View.VISIBLE);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ZanRanking zanRanking = list_data1.get(position);
                int isFriend = 0;

                if (zanRanking.getStatus().equals("已添加")) {
                    isFriend = 0;
                } else if (zanRanking.getStatus().equals("已拒绝")) {
                    isFriend = 1;
                } else if (zanRanking.getStatus().equals("接受")) {
                    isFriend = 2;
                }
                Intent intent = new Intent(AskForActivity.this, FriendsInfoActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("zanRanking", zanRanking);
                intent.putExtras(mBundle);
                intent.putExtra("isFrend", isFriend);
                startActivity(intent);
            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                TextView tv_del = (TextView) contentView.findViewById(R.id.tv_del);
                tv_del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String zid = list_data1.get(position).getId();
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("FromMemberID",zid);
                        HttpConnect.post(AskForActivity.this,"member_friends_list_apply_delete" , map,
                                new Callback() {
                                    @Override
                                    public void onResponse(Response arg0)
                                            throws IOException {

                                        if (arg0 != null || !arg0.equals("")) {

                                            final JSONObject object = JSONObject.fromObject(arg0.body().string());

                                            if (object.get("status").equals("success")) {

                                                    Handler dataHandler = new Handler(
                                                            getMainLooper()) {
                                                        @Override
                                                        public void handleMessage(
                                                                final Message msg) {
                                                            list_data1.remove(position);
                                                            adapter.setPersonList(list_data1);
                                                            adapter.notifyDataSetChanged();
                                                            window.dismiss();
                                                        }
                                                    };
                                                    dataHandler.sendEmptyMessage(0);

                                            }else {
                                                Handler dataHandler = new Handler(
                                                        getMainLooper()) {
                                                    @Override
                                                    public void handleMessage(
                                                            final Message msg1) {


                                                    }
                                                };
                                                dataHandler.sendEmptyMessage(0);
                                            }
                                        }
                                    }


                                    @Override
                                    public void onFailure(Request arg0, IOException arg1) {


                                    }
                                });
                    }
                });



                if(!window.isShowing())
                {
                    // 设置背景颜色变暗
                    WindowManager.LayoutParams lp5 = getWindow().getAttributes();
                    lp5.alpha = 0.5f;
                    getWindow().setAttributes(lp5);
                    window.setOnDismissListener(new PopupWindow.OnDismissListener() {

                        @Override
                        public void onDismiss() {
                            WindowManager.LayoutParams lp3 = getWindow().getAttributes();
                            lp3.alpha = 1f;
                            getWindow().setAttributes(lp3);
                        }
                    });

                    window.setOutsideTouchable(true);

                    // 实例化一个ColorDrawable颜色为半透明
                    Drawable drawable = getResources().getDrawable(R.drawable.bg_shape_ffffff);
                    window.setBackgroundDrawable(drawable);

                    window.showAtLocation(findViewById(R.id.title_back), Gravity.CENTER,0,0);
                }

                return true;
            }
        });

        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AskForActivity.this, AddContactActivity.class));
            }
        });

        ivNoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tools.getAPNType(AskForActivity.this) == true){
                    ivNoLink.setVisibility(View.GONE);
                    getAskFors();
                }else{
                    ivNoLink.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void back(View view){


        Set<String> keys3 = MyApplication.maps.keySet();
        if(keys3!=null&&keys3.size()>0)
        {
            if(keys3.contains("ContactListActivity"))
            {
                finish();
            }else{
                startActivity(new Intent(this,ContactListActivity.class));
                finish();
            }
        }



    }


    /**
     * 申请数据
     */
    public void getAskFors() {
        list_data1.clear();
        cif_loading.setVisibility(View.VISIBLE);
        HttpConnect.post(this,"member_friends_list_apply" , null,
                new Callback() {

                    @Override
                    public void onResponse(Response arg0)
                            throws IOException {

                            final JSONObject object = JSONObject.fromObject(arg0
                                    .body().string());
                            if (object.get("status").equals("success")) {

                                JSONArray array = object
                                        .optJSONArray("data");
                                if (array.size() != 0) {

                                    for (int i = 0; i < array.size(); i++) {
                                        JSONObject object2 = array
                                                .getJSONObject(i);
                                        ZanRanking zanRanking = new ZanRanking();
                                        String id = object2.optString("id");
                                        String name = object2.optString("name");
                                        String pic =  object2.optString("pic");
                                        String lk = object2.optString("lk");
                                        String type = object2.optString("type");
                                        String grade =  object2.optString("grade");
                                        String inti =   object2.optString("inti");
                                        String ip =  object2.optString("ip");
                                        String mobile =  object2.optString("mobile");
                                        String remark = object2.optString("remark");
                                        String oneau = object2.optString("oneau");
                                        String sex = object2.optString("sex");
                                        String address = object2.optString("address");
                                        String status = object2.optString("status");
                                        String bgpic = object2.optString("backgroundpic");
                                        String paymentCode = object2.optString("paymentcode");

                                        zanRanking.setPic(pic);
                                        zanRanking.setArea(ip);
                                        zanRanking.setCount(inti);
                                        zanRanking.setType(type);
                                        zanRanking.setLinkCode(lk);
                                        zanRanking.setId(id);
                                        zanRanking.setName(name);
                                        zanRanking.setLevel1(grade);
                                        zanRanking.setMobile(mobile);
                                        zanRanking.setOneau(oneau);
                                        zanRanking.setRemark(remark);
                                        zanRanking.setSex(sex);
                                        zanRanking.setArea(address);
                                        zanRanking.setStatus(status);
                                        zanRanking.setBackgroundpic(bgpic);
                                        zanRanking.setPaymentCode(paymentCode);

                                        list_data1.add(zanRanking);
                                        zanRanking = null;
                                    }

                                    Handler dataHandler = new Handler(
                                            getContext().getMainLooper()) {

                                        @Override
                                        public void handleMessage(
                                                final Message msg) {

                                            adapter.setPersonList(list_data1);
                                            adapter.notifyDataSetChanged();
                                            cif_loading.setVisibility(View.GONE);


                                        }
                                    };
                                    dataHandler.sendEmptyMessage(0);

                                }
                            }else{

                                Handler dataHandler = new Handler(
                                        getContext().getMainLooper()) {

                                    @Override
                                    public void handleMessage(
                                            final Message msg) {
                                        cif_loading.setVisibility(View.GONE);
                                        if (list_data1.size()>0){
                                            ivNodata.setVisibility(View.GONE);
                                        }else{
                                            ivNodata.setVisibility(View.VISIBLE);
                                        }
                                    }
                                };
                                dataHandler.sendEmptyMessage(0);
                            }


                    }


                    @Override
                    public void onFailure(Request arg0, IOException arg1) {


                        Handler dataHandler = new Handler(
                                getContext().getMainLooper()) {

                            @Override
                            public void handleMessage(
                                    final Message msg) {
//                                sToast("链接超时！");
                                cif_loading.setVisibility(View.GONE);
                            }
                        };
                        dataHandler.sendEmptyMessage(0);
                    }
                });

    }

}
