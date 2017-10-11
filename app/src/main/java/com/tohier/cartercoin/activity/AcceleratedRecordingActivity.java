package com.tohier.cartercoin.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.adapter.AcceleratedRecordingAdapter;
import com.tohier.cartercoin.bean.AcceleratedRecordingBean;
import com.tohier.cartercoin.columnview.LoadingView;
import com.tohier.cartercoin.columnview.NoDataView;
import com.tohier.cartercoin.columnview.NoLinkView;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.Tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/4.
 */

public class AcceleratedRecordingActivity extends MyBackBaseActivity implements AbsListView.OnScrollListener{

    private ImageView iv_back2;
    private ListView listView;
    private AcceleratedRecordingAdapter acceleratedRecordingAdapter;
    private List<AcceleratedRecordingBean> datas = new ArrayList<AcceleratedRecordingBean>();

    private NoDataView iv_isnull;
    private LoadingView gifImageView,loadingView;
    private NoLinkView noLinkView;

    private boolean isLastRow = false;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_accelerated_recording_layout);

        initData();
        setUpView();
        loadAdapter();
        if (Tools.getAPNType(AcceleratedRecordingActivity.this) == true){
            noLinkView.setVisibility(View.GONE);

            loadData(1);
        }else{
            noLinkView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initData() {
        iv_back2 = (ImageView) this.findViewById(R.id.iv_back2);
        listView = (ListView) this.findViewById(R.id.lv);
        iv_isnull = (NoDataView) this.findViewById(R.id.iv_nodata);
        noLinkView = (NoLinkView) findViewById(R.id.iv_no_link);
        gifImageView = (LoadingView) this.findViewById(R.id.gif_loading);
        listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        loadingView = new LoadingView(this);
        loadingView.loadMore();
        loadingView.setGravity(Gravity.CENTER);
    }

    private void loadAdapter()
    {
        acceleratedRecordingAdapter = new AcceleratedRecordingAdapter(this,datas);
        listView.setAdapter(acceleratedRecordingAdapter);
    }

    private void setUpView() {
        iv_back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        noLinkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tools.getAPNType(AcceleratedRecordingActivity.this) == true){
                    noLinkView.setVisibility(View.GONE);

                    loadData(1);
                }else{
                    noLinkView.setVisibility(View.VISIBLE);
                }
            }
        });

        listView.setOnScrollListener(this);
    }


    private void loadData(final int isClear)
    {
        if (isClear == 1) {
            datas.clear();
            gifImageView.setVisibility(View.VISIBLE);

        }else{
            if (listView.getFooterViewsCount()<=0){
                listView.addFooterView(loadingView);
            }
            loadingView.setVisibility(View.VISIBLE);
        }

        Map<String, String> par = new HashMap<String, String>();
        par.put("page", page+"");
        HttpConnect.post(this, "member_assets_accelerate_list", par, new Callback() {

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
                                String address = jsonObject2.getString("address");
                                String name = jsonObject2.getString("name");
                                String type = jsonObject2.getString("type");
                                String cou = jsonObject2.getString("cou");
                                String date = jsonObject2.getString("date");

                                AcceleratedRecordingBean acceleratedRecordingBean = new AcceleratedRecordingBean(address,name,type,cou,date);
                                datas.add(acceleratedRecordingBean);
                            }
                        }
                    }

                }else{
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (isClear == 1) {
//                                ivNodata.setVisibility(View.VISIBLE);
                            }else {
                                loadingView.noMoreData("没有更多数据了");
                            }
                        }
                    });
                }
                AcceleratedRecordingActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        acceleratedRecordingAdapter.setDatas(datas);
                        acceleratedRecordingAdapter.notifyDataSetChanged();
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
                AcceleratedRecordingActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        gifImageView.setVisibility(View.GONE);
                        if (datas.size()>0){
                            iv_isnull.setVisibility(View.GONE);
                        }else{
                            iv_isnull.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (isLastRow && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            //加载元素
            page++;
            loadData(2);
            isLastRow = false;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //如果当前列表的数量等于查询的总数量,则不做任何操作


        //判断是否滚到最后一行
        if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0) {
            isLastRow = true;
        }

    }

}
