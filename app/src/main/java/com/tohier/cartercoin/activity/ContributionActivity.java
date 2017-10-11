package com.tohier.cartercoin.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.IYWConversationUnreadChangeListener;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.adapter.ContributionAdapter;
import com.tohier.cartercoin.bean.ContributionData;
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

/**
 * Created by Administrator on 2016/12/20.
 */

public class ContributionActivity extends MyBackBaseActivity  implements AbsListView.OnScrollListener{

    private ListView listView;
    private ContributionAdapter adapter;
    private List<ContributionData> datas = new ArrayList<ContributionData>();
    private TextView tvTitle;

    /**
     * 监听未读消息数
     */
    private IYWConversationService mConversationService;
    private IYWConversationUnreadChangeListener mConversationUnreadChangeListener;

    private NoDataView iv_isnull;
    private NoLinkView noLinkView;
    private LoadingView gif_loading,loadingView;

    private boolean isLastRow = false;

    private int page = 0;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };


    private Handler mHandler = new Handler(Looper.getMainLooper());
    private String mark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribution_layout);

        initData();

        setUp();

    }

    private void setUp() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = mIMKit.getChattingActivityIntent(datas.get(position).getLinkCode(), MyApplication.APP_KEY);
//                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {
        mConversationService = mIMKit.getConversationService();

        tvTitle = (TextView) this.findViewById(R.id.tv_title2);
        iv_isnull = (NoDataView) this.findViewById(R.id.iv_nodata);
        gif_loading = (LoadingView) this.findViewById(R.id.gif_loading);
        noLinkView = (NoLinkView) findViewById(R.id.iv_no_link);

        ((ImageView) this.findViewById(R.id.iv_back2)).setOnClickListener(onClickListener);
        listView = (ListView) this.findViewById(R.id.lv_contribution);

        loadingView = new LoadingView(this);
        loadingView.loadMore();
        loadingView.setGravity(Gravity.CENTER);

        adapter = new ContributionAdapter(this,datas,mIMKit);
        listView.setAdapter(adapter);
        listView.setSelector(new ColorDrawable(Color.TRANSPARENT));

        initConversationServiceAndListener();

        listView.setOnScrollListener(this);

        mark = getIntent().getStringExtra("mark");


        if (Tools.getAPNType(ContributionActivity.this) == true){
            noLinkView.setVisibility(View.GONE);
            if(!TextUtils.isEmpty(mark))
            {
                if(mark.equals("同班"))
                {
                    loadProfit(1,"同班");
                    tvTitle.setText("①级宝粉");
                }else if(mark.equals("同系"))
                {
                    loadProfit(1,"同系");
                    tvTitle.setText("②级宝粉");
                }else if(mark.equals("同校"))
                {
                    loadProfit(1,"同校");
                    tvTitle.setText("③级宝粉");
                }
            }
        }else{
            noLinkView.setVisibility(View.VISIBLE);
        }

        noLinkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tools.getAPNType(ContributionActivity.this) == true){
                    noLinkView.setVisibility(View.GONE);
                    if(!TextUtils.isEmpty(mark))
                    {
                        if(mark.equals("同班"))
                        {
                            loadProfit(1,"同班");
                            tvTitle.setText("①级宝粉");
                        }else if(mark.equals("同系"))
                        {
                            loadProfit(1,"同系");
                            tvTitle.setText("②级宝粉");
                        }else if(mark.equals("同校"))
                        {
                            loadProfit(1,"同校");
                            tvTitle.setText("③级宝粉");
                        }
                    }
                }else{
                    noLinkView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void loadProfit(final int isClear, String type)
    {
        if (isClear == 1) {
            datas.clear();
            gif_loading.setVisibility(View.VISIBLE);

        }else{
            if (listView.getFooterViewsCount()<=0){
                listView.addFooterView(loadingView);
            }
            loadingView.setVisibility(View.VISIBLE);
        }
        page = page + 1 ;
        HashMap<String,String> par = new HashMap<String,String>();
        par.put("page",page+"");
        if(type.equals("同班"))
        {
            par.put("id", "1");
        }else if(type.equals("同系"))
        {
            par.put("id", "2");
        }else if(type.equals("同校"))
        {
            par.put("id", "3");
        }
        HttpConnect.post(this, "member_offline_earning_detail", par, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {

                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {
                            gif_loading.setVisibility(View.GONE);
                            JSONArray array = data
                                    .optJSONArray("data");
                            if (array.size() != 0) {

                                for(int i = 0 ; i < array.size() ; i ++)
                                {
                                    final String todaybouns = array.getJSONObject(i).getString("todaybonus");
                                    final String name = array.getJSONObject(i).getString("name");
                                    final String allbouns = array.getJSONObject(i).getString("allbonus");
                                    final String mobile = array.getJSONObject(i).getString("mobile");
                                    final String linkcode = array.getJSONObject(i).getString("lickcode");
                                    final String pic = array.getJSONObject(i).getString("pic");
                                    final String id = array.getJSONObject(i).getString("id");
                                    final String count = array.getJSONObject(i).getString("count");
                                    ContributionData contributionData = new ContributionData(allbouns,todaybouns,name);
                                    contributionData.setPhone(mobile);
                                    contributionData.setLinkCode(linkcode);
                                    contributionData.setPicUrl(pic);
                                    contributionData.setId(id);
                                    contributionData.setCount(count);
                                    datas.add(contributionData);
                                }

                            }
                        }
                    };
                    dataHandler.sendEmptyMessage(0);

                }else {
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
                        adapter.setDatas(datas);
                        adapter.notifyDataSetChanged();
                        gif_loading.setVisibility(View.GONE);
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

                        gif_loading.setVisibility(View.GONE);
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

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (isLastRow && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            //加载元素
            loadProfit(2,mark);
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


    @Override
    public void onResume() {
        super.onResume();
        //resume时需要检查全局未读消息数并做处理，因为离开此界面时删除了全局消息监听器
        mConversationUnreadChangeListener.onUnreadChange();

    }
    /**
     * 当未读数发生变化时会回调该方法，开发者可以在该方法中更新未读数
     *
     */
    private void initConversationServiceAndListener() {
        mConversationUnreadChangeListener = new IYWConversationUnreadChangeListener() {

            @Override
            public void onUnreadChange() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        };
        mConversationService.addTotalUnreadChangeListener(mConversationUnreadChangeListener);
    }

}
