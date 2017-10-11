package com.tohier.cartercoin.orderfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.tohier.cartercoin.activity.GoodsDetialsActivity;
import com.tohier.cartercoin.activity.MyOrderActivity;
import com.tohier.cartercoin.adapter.OrderAdapter;
import com.tohier.cartercoin.bean.Order;
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
import java.util.Map;

/**
 * Created by Administrator on 2016/12/22.
 */

public class OrderFragment extends BaseFragment implements AbsListView.OnScrollListener{

    private View view;
    private LoadingView gifImageView,loadingView;
    private NoDataView ivNodata;
    private NoLinkView noLinkView;
    private ListView listView;
    private OrderAdapter adapter;
    private ArrayList<Order> datas = new ArrayList<Order>();

    private String typeId;

    private boolean isLastRow = false;
    private int page = 1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_order_layout,container,false);

        init();

        return view;
    }

    private void init() {
        typeId = getArguments().getString("type","0");
        listView = (ListView) view.findViewById(R.id.content_view);
        ivNodata = (NoDataView) view.findViewById(R.id.iv_nodata);
        gifImageView = (LoadingView) view.findViewById(R.id.cif_loading);
        noLinkView = (NoLinkView) view.findViewById(R.id.iv_no_link);

        loadingView = new LoadingView(getActivity());
        loadingView.loadMore();
        loadingView.setGravity(Gravity.CENTER);
        adapter = new OrderAdapter(datas,getActivity());
        listView.setAdapter(adapter);

        /**
         * 解析数据
         */
        if (Tools.getAPNType(getActivity()) == true){
            noLinkView.setVisibility(View.GONE);
            getJsonDate(1);
        }else{
            noLinkView.setVisibility(View.VISIBLE);
        }


        /**
         * 点击事件
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if (position != datas.size()) {
                    String id1 = datas.get(position).getGoodsId();
                    startActivity(new Intent(getActivity(), GoodsDetialsActivity.class).putExtra("id", id1));
                }

            }
        });

        listView.setOnScrollListener(this);

        noLinkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tools.getAPNType(getActivity()) == true){
                    noLinkView.setVisibility(View.GONE);
                    getJsonDate(1);

                }else{
                    noLinkView.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        getJsonDate(1);

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
            gifImageView.setVisibility(View.VISIBLE);

        }else{
            if (listView.getFooterViewsCount()<=0){
                listView.addFooterView(loadingView);
            }
            loadingView.setVisibility(View.VISIBLE);
        }

        Map<String, String> par = new HashMap<String, String>();
        if (typeId.equals("0")){ //全部
            par.put("type","0");
            par.put("status","5");
        }else if (typeId.equals("1")){ //代付款
            par.put("type","1");
            par.put("status","0");
        }else if (typeId.equals("2")){//待发货
            par.put("type","1");
            par.put("status","2");
        }else if (typeId.equals("3")){//已发货
            par.put("type","1");
            par.put("status","3");
        }

        par.put("page",page+"");
        HttpConnect.post((MyOrderActivity)getActivity(), "member_order_list", par, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body()
                        .string());

                if (data.get("status").equals("success")) {
                    JSONArray dataArr = data.getJSONArray("data");
                    for(int i = 0 ; i < dataArr.size() ; i ++)
                    {
                        JSONObject jsonObjNews = dataArr.optJSONObject(i);
                        if(jsonObjNews!=null)
                        {
                            String id = jsonObjNews.optString("id");
                            String code  = jsonObjNews.optString("code");
                            String totalctc  = jsonObjNews.optString("totalctc");
                            String products = jsonObjNews.optString("products");
                            String logisticscompany  = jsonObjNews.optString("logisticscompany");
                            String logisticsnu = jsonObjNews.optString("logisticsnu");
                            String price = jsonObjNews.optString("price");
                            String qty  = jsonObjNews.optString("qty");
                            String logisticsstatus  = jsonObjNews.optString("logisticsstatus");
                            String pic  = jsonObjNews.optString("pic");
                            String goodsId = jsonObjNews.optString("productID");
                            Order order = new Order();
                            order.setCount(qty);
                            order.setExpressName(logisticscompany);
                            order.setExpressNum(logisticsnu);
                            order.setGoodsName(products);
                            order.setGoodsPic(pic);
                            order.setGoodsPrice(price);
                            order.setState(logisticsstatus);
                            order.setTotalPrice(totalctc);
                            order.setOrderNum(code);
                            order.setGoodsId(goodsId);
                            order.setId(id);
                            datas.add(order);
                        }
                    }

                } else
                {
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

                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        adapter.setList(datas);
                        adapter.notifyDataSetChanged();
                        if (datas.size()>0){
                            ivNodata.setVisibility(View.GONE);
                        }else{
                            ivNodata.setVisibility(View.VISIBLE);
                        }
                        gifImageView.setVisibility(View.GONE);

                    }
                });
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {

                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        gifImageView.setVisibility(View.GONE);
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
