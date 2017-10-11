package com.tohier.cartercoin.transactionfragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.android.fragment.base.BaseFragment;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.activity.TransactionActivity;
import com.tohier.cartercoin.adapter.MyTransactionAdapter;
import com.tohier.cartercoin.bean.Transaction;
import com.tohier.cartercoin.columnview.LoadingView;
import com.tohier.cartercoin.columnview.NoDataView;
import com.tohier.cartercoin.config.HttpConnect;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.tohier.cartercoin.R.id.cif_loading;


public class DealRecordFragment extends BaseFragment implements AbsListView.OnScrollListener {

    private View view;
    private ListView gridView;
    private ArrayList<Transaction> datas = new ArrayList<Transaction>();
    private MyTransactionAdapter adapter;
    private LoadingView gifLoading,loadingView;
    private NoDataView ivNodata;
    private boolean isLastRow = false;
    private int page = 1;

    public ListView getGridView() {
        return gridView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_deal_record, container, false);
        init();
        return view;
    }

    private void init() {
        gridView = (ListView) view.findViewById(R.id.content_view);
        gifLoading = (LoadingView) view.findViewById(cif_loading);
        ivNodata = (NoDataView) view.findViewById(R.id.iv_nodata);

        loadingView = new LoadingView(getActivity());
        loadingView.loadMore();
        loadingView.setGravity(Gravity.CENTER);

        adapter = new MyTransactionAdapter(getActivity(),datas,1);
        gridView.setAdapter(adapter);


        /**
         * 解析数据
         */
        page = 1;
       getJsonDate(1);

        /**
         * 点击事件
         */
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
            }
        });

        gridView.setOnScrollListener(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        page = 1;
       getJsonDate(1);

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void initData() {

    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (isLastRow && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            //加载元素
            page++;
            getJsonDate(2);
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


    /**
     *
     * @param isClear 1---clear
     */
    private void getJsonDate(final int isClear){
        if (isClear == 1) {
            datas.clear();
            gifLoading.setVisibility(View.VISIBLE);

        }else{
            if (gridView.getFooterViewsCount()<=0){
                gridView.addFooterView(loadingView);
            }
            loadingView.setVisibility(View.VISIBLE);
        }

        Map<String, String> par = new HashMap<String, String>();
        par.put("page", page+"");
        // mZProgressHUD.show();
        HttpConnect.post( (TransactionActivity)getActivity(), "member_trading_log", par, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());

                if (data.get("status").equals("success")) {
                    JSONArray dataArr = data.optJSONArray("data");
                    Transaction transaction;
                    for(int i = 0 ; i < dataArr.size() ; i ++)
                    {
                        JSONObject jsonObjNews = dataArr.optJSONObject(i);
                        if(jsonObjNews!=null)
                        {
                            String rownum = jsonObjNews.optString("rownum");
                            String createdate = jsonObjNews.optString("createdate");
                            String price = jsonObjNews.optString("price");
                            String qty = jsonObjNews.optString("qty");
                            String total = jsonObjNews.optString("total");
                            transaction = new Transaction(rownum,createdate,price,qty,total);
                            datas.add(transaction);
                        }
                    }
//                    if (isClear == 1) {
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                gridView.addFooterView(loadingView);
//                            }
//                        });
//
//                    }
                }else{
                    getActivity().runOnUiThread(new Runnable() {

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

                if (getActivity() != null){
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            adapter.setList(datas);
                            adapter.notifyDataSetChanged();
                            gifLoading.setVisibility(View.GONE);
                            if (datas.size()>0){
                                ivNodata.setVisibility(View.GONE);
                            }else{
                                ivNodata.setVisibility(View.VISIBLE);
                            }

                        }
                    });
                }

            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {

                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        gifLoading.setVisibility(View.GONE);
                        if (datas.size()>0){
                            ivNodata.setVisibility(View.GONE);
                        }else{
                            ivNodata.setVisibility(View.VISIBLE);
                        }
                    }
                });

            }
        });
    }


}
