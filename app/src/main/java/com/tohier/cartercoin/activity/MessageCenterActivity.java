package com.tohier.cartercoin.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.adapter.MessageCenterAdapter;
import com.tohier.cartercoin.bean.Message;
import com.tohier.cartercoin.columnview.LoadingView;
import com.tohier.cartercoin.columnview.NoDataView;
import com.tohier.cartercoin.columnview.NoLinkView;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.Tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/25.
 */

public class MessageCenterActivity extends MyBackBaseActivity {

     private ImageView ivBack;
     private ListView listView;
     private List<Message> datas = new ArrayList<Message>();
     private MessageCenterAdapter messageCenterAdapter;
    private NoDataView iv_isnull;
    private LoadingView gifImageView;
    private NoLinkView ivNoLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_message_center_layout);

        initData();
        if (Tools.getAPNType(MessageCenterActivity.this) == true){
            ivNoLink.setVisibility(View.GONE);
            getJsonData();
        }else{
            ivNoLink.setVisibility(View.VISIBLE);
        }
        markReadAll();

    }

    @Override
    public void initData() {
        ivBack = (ImageView)this.findViewById(R.id.iv_back2);
        gifImageView = (LoadingView) this.findViewById(R.id.gif_loading);
        iv_isnull = (NoDataView) findViewById(R.id.iv_nodata);
        ivNoLink = (NoLinkView) findViewById(R.id.iv_no_link);
        listView = (ListView) this.findViewById(R.id.lv);
        listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        messageCenterAdapter = new MessageCenterAdapter(datas,this);
        listView.setAdapter(messageCenterAdapter);

        ivBack.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     finish();
                 }
             });

        ivNoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tools.getAPNType(MessageCenterActivity.this) == true){
                    ivNoLink.setVisibility(View.GONE);
                    getJsonData();
                }else{
                    ivNoLink.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    /**
     * 获取消息
     */
    public void getJsonData() {

        gifImageView.setVisibility(View.VISIBLE);
        HttpConnect.post(this, "member_push_message", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {

                JSONObject jsonObject = JSONObject.fromObject(arg0.body().string());
                String msg = jsonObject.getString("msg");
                if (jsonObject.get("status").equals("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.size(); i++) {

                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            if (jsonObject2 != null) {
                                String title = jsonObject2.getString("title");
                                String content = jsonObject2.getString("contents");
                                String time = jsonObject2.getString("createdate");

                                Message message = new Message(title,time,content);
                                datas.add(message);
                            }
                        }
                    }
                } else {
//                    sToast("暂无数据");
                }
                MessageCenterActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        messageCenterAdapter.setList(datas);
                        messageCenterAdapter.notifyDataSetChanged();

                        gifImageView.setVisibility(View.GONE);
                        if (datas.size()>0){
                            iv_isnull.setVisibility(View.GONE);
                        }else{
                            iv_isnull.setVisibility(View.VISIBLE);
                        }

                    }
                });
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                // TODO Auto-generated method stub
                gifImageView.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 将所有消息标记为全读
     */
    public void markReadAll()
    {
        HttpConnect.post(this, "member_read_message", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {

                JSONObject jsonObject = JSONObject.fromObject(arg0.body().string());
                if (jsonObject.get("status").equals("success")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            sToast("已标记为全读");
                        }
                    });
                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
        });
    }



}
