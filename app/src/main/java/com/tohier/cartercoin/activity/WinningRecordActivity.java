package com.tohier.cartercoin.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.android.activity.base.BaseActivity;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.adapter.WinningRecordAdapter;
import com.tohier.cartercoin.bean.WinningRecordBean;
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

/**
 * Created by Administrator on 2017/5/19.
 */

public class WinningRecordActivity extends BaseActivity implements AbsListView.OnScrollListener{

    private ImageView title_back;
    private ListView lvWinningRecord;
    private WinningRecordAdapter adapter;
    private ArrayList<WinningRecordBean> datas = new ArrayList<WinningRecordBean>();
    private int page = 0;
    private boolean isLastRow = false;
    private LoadingView cif_loading,loadingView;
    private NoLinkView noLinkView;
    private NoDataView iv_isnull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_winningrecord_layout);

        initData();

        if (Tools.getAPNType(WinningRecordActivity.this) == true){
            noLinkView.setVisibility(View.GONE);

            loadData(1);
        }else{
            noLinkView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void initData() {
        cif_loading  = (LoadingView) this.findViewById(R.id.gif_loading);
        title_back = (ImageView) this.findViewById(R.id.title_back);
        lvWinningRecord = (ListView) this.findViewById(R.id.lv_winningrecord);
        noLinkView = (NoLinkView) findViewById(R.id.iv_no_link);
        iv_isnull = (NoDataView) this.findViewById(R.id.iv_nodata);

        loadingView = new LoadingView(this);
        loadingView.loadMore();
        loadingView.setGravity(Gravity.CENTER);

        adapter = new WinningRecordAdapter(WinningRecordActivity.this,datas);
        lvWinningRecord.setAdapter(adapter);

        lvWinningRecord.setSelector(new ColorDrawable(Color.TRANSPARENT));

        lvWinningRecord.setOnScrollListener(this);

        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(WinningRecordActivity.this,LuckPanActivity.class));
//                overridePendingTransition(0, 0);
                finish();
            }
        });


        noLinkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tools.getAPNType(WinningRecordActivity.this) == true){
                    noLinkView.setVisibility(View.GONE);
                    loadData(1);

                }else{
                    noLinkView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        startActivity(new Intent(WinningRecordActivity.this,LuckPanActivity.class));
//        overridePendingTransition(0, 0);
//        finish();
//    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (isLastRow && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            //加载元素
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


    private void loadData(final int isClear)
    {
        if (isClear == 1) {
            datas.clear();
            cif_loading.setVisibility(View.VISIBLE);

        }else{
            if (lvWinningRecord.getFooterViewsCount()<=0){
                lvWinningRecord.addFooterView(loadingView);
            }
            loadingView.setVisibility(View.VISIBLE);
        }
        page = page + 1 ;
        HashMap<String,String> par = new HashMap<String,String>();
        par.put("page",page+"");
        HttpConnect.post(this, "member_winning_list", par, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {

                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {
                            JSONArray array = data
                                    .optJSONArray("data");
                            if(null!=array)
                            {
                                if (array.size() != 0) {
                                    for(int i = 0 ; i < array.size() ; i ++)
                                    {
                                        final String detail = array.getJSONObject(i).optString("name");
                                        final String time = array.getJSONObject(i).optString("date");
                                        WinningRecordBean winningRecordBean = new WinningRecordBean(detail,time);
                                        datas.add(winningRecordBean);
                                    }

                                }
                            }
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }else
                {
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

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        adapter.setList(datas);
                        adapter.notifyDataSetChanged();
                        cif_loading.setVisibility(View.GONE);
                        if (datas.size()>0){
                            iv_isnull.setVisibility(View.GONE);
                        }else{
                            iv_isnull.setVisibility(View.VISIBLE);
                        }

                    }
                });
            }

            @Override
            public  void onFailure(Request arg0, IOException arg1) {
                Handler dataHandler = new Handler(getContext()
                        .getMainLooper()) {

                    @Override
                    public void handleMessage(final Message msg) {
                        cif_loading.setVisibility(View.GONE);
                        if (datas.size()>0){
                            iv_isnull.setVisibility(View.GONE);
                        }else{
                            iv_isnull.setVisibility(View.VISIBLE);
                        }

                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        });
    }
}
