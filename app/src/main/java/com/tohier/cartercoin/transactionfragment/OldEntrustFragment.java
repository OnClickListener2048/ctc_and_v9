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
import com.tohier.cartercoin.adapter.EntrustAdapter;
import com.tohier.cartercoin.bean.Entrust;
import com.tohier.cartercoin.columnview.LoadingView;
import com.tohier.cartercoin.columnview.NoDataView;
import com.tohier.cartercoin.config.HttpConnect;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OldEntrustFragment extends BaseFragment implements AbsListView.OnScrollListener{

    private View view;
    private ListView gridView;
    private List<Entrust> datas = new ArrayList<Entrust>();
    private EntrustAdapter adapter;
    private LoadingView gifLoading,loadingView;
    private NoDataView ivNOdata;
    private boolean isLastRow = false;
    private int page = 1;

    public ListView getGridView() {
        return gridView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_entrust, container, false);
        init();
        return view;
    }

    private void init() {
        gridView = (ListView) view.findViewById(R.id.content_view);
        gifLoading = (LoadingView) view.findViewById(R.id.cif_loading);
        ivNOdata = (NoDataView) view.findViewById(R.id.iv_nodata);
        loadingView = new LoadingView(getActivity());
        loadingView.loadMore();
        loadingView.setGravity(Gravity.CENTER);

        adapter = new EntrustAdapter(datas, getActivity(),1);
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

    private void getJsonDate(final int isClear) {
        if (isClear == 1) {
            datas.clear();
            gifLoading.setVisibility(View.VISIBLE);

        }else{
            if (gridView.getFooterViewsCount()<=0){
                gridView.addFooterView(loadingView);
            }
            loadingView.setVisibility(View.VISIBLE);
        }
        String code = "";
        code = "member_trading_me_log";

        Map<String, String> par = new HashMap<String, String>();
        par.put("page", page+"");

        HttpConnect.post((TransactionActivity)getActivity(), code, par, new Callback() {

            private String msg;

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body()
                        .string().trim());

                if (data.get("status").equals("success")) {
                    JSONArray dataArr = data.optJSONArray("data");
                    Entrust entrust;
                    for(int i = 0 ; i < dataArr.size() ; i ++)
                    {
                        JSONObject jsonObjNews = dataArr.optJSONObject(i);
                        if(jsonObjNews!=null)
                        {
                            String id = jsonObjNews.optString("id");
                            String rownum = jsonObjNews.optString("rownum");
                            String name = jsonObjNews.optString("name");
                            String createdate = jsonObjNews.optString("createdate");
                            String price = jsonObjNews.optString("price");
                            String qty = jsonObjNews.optString("qty");
                            String deal = jsonObjNews.optString("deal");
                            String nodeal = jsonObjNews.optString("nodeal");
                            String total = jsonObjNews.optString("total");

                            entrust = new Entrust(id,rownum,name,qty,price,deal,nodeal,createdate);
                            entrust.setTotal(total);
                            datas.add(entrust);
                        }
                    }
                }else{
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (isClear == 1) {
//                                ivNOdata.setVisibility(View.VISIBLE);
                            }else {
                                loadingView.noMoreData("没有更多数据了");
                            }
                        }
                    });
                }

                try{
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            adapter.setItems(datas);
                            adapter.notifyDataSetChanged();
                            gifLoading.setVisibility(View.GONE);
                            if (datas.size()>0){
                                ivNOdata.setVisibility(View.GONE);
                            }else{
                                ivNOdata.setVisibility(View.VISIBLE);
                            }

                        }
                    });
                }catch (Exception e){}



            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {

                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (datas.size()>0){
                            ivNOdata.setVisibility(View.GONE);
                        }else{
                            ivNOdata.setVisibility(View.VISIBLE);
                        }

                    }
                });

            }
        });
    }
}
