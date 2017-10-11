package com.tohier.cartercoin.mallfragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.android.fragment.base.BaseFragment;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.activity.GoodsDetialsActivity;
import com.tohier.cartercoin.activity.MallActivity;
import com.tohier.cartercoin.adapter.GoodsAdapter;
import com.tohier.cartercoin.bean.Goods;
import com.tohier.cartercoin.bean.Product;
import com.tohier.cartercoin.columnview.BaseScrollView;
import com.tohier.cartercoin.columnview.LoadingView;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.loader.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerClickListener;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.GridViewWithHeaderAndFooter;

/**
 * Created by Administrator on 2016/12/16.
 */

public class MyMallFragment extends BaseFragment implements AbsListView.OnScrollListener{

    private View view;

    private List<String> imageUrl = new ArrayList<String>();
    private List<String> imageId = new ArrayList<String>();
    private List<Product> datas = new ArrayList<Product>();
    private List<Goods> goods = new ArrayList<Goods>();
    private GridViewWithHeaderAndFooter gridView;
    private Banner banner;
    private GoodsAdapter adapter;
    private LoadingView gifLoading;
    private BaseScrollView scrollView ;

    private int page = 1;
    public BaseScrollView getContent_view() {
        return scrollView;
    }

    private String typeId;

    private boolean isLastRow = false;
    private View bannerView , loadView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_mall_layout,container,false);

        initialize();
        setUp();

        return view;
    }

    private void setUp() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                startActivity(new Intent(getActivity(), GoodsDetialsActivity.class).putExtra("id",adapter.getItems().get(position).getId()));
            }
        });

        gridView.setOnScrollListener(this);

    }

    private void  initialize() {
        typeId = getArguments().getString("type","0");

        gifLoading = (LoadingView) view.findViewById(R.id.cif_loading);

        bannerView = View.inflate(getActivity(),R.layout.layout_banner,null);
        banner = (Banner) bannerView.findViewById(R.id.banner);
        gridView = (GridViewWithHeaderAndFooter) view.findViewById(R.id.gv_goods);
        scrollView = (BaseScrollView) view.findViewById(R.id.content_view);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));

        gridView.addHeaderView(bannerView);
//        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
//        loadView = layoutInflater.inflate(R.layout.loading_more_layout, null);
//        gridView.addFooterView(loadView);

        adapter = new GoodsAdapter(goods, getActivity());

        gridView.setAdapter(adapter);
//        ((PullToRefreshLayout)view. findViewById(R.id.refresh_view)).setOnRefreshListener(listener);
        getJsonDate(1);
        //设置轮播时间
        banner.setDelayTime(2500);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.RIGHT);
        getPic();

    }



    private void getPic()
    {
        Map<String, String> par = new HashMap<String, String>();
        par.put("kind",typeId);
        HttpConnect.post(this, "member_shuffle", par, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject data = JSONObject.fromObject(arg0.body()
                        .string());

                datas.clear();

                if (data.get("status").equals("success")) {
                    JSONArray dataArr = data.getJSONArray("data");
                    for(int i = 0 ; i < dataArr.size() ; i ++)
                    {
                        JSONObject jsonObjNews = dataArr.optJSONObject(i);
                        if(jsonObjNews!=null)
                        {
                            String id = jsonObjNews.getString("id");
                            String picpath = jsonObjNews.getString("pic");
                            String url = jsonObjNews.getString("url");
                            Product product = new Product(id,picpath,url);
                            datas.add(product);
                        }
                    }
                }
                if(datas!=null&&datas.size()>0)
                {
                    imageUrl.clear();
                    imageId.clear();

                    for(int i = 0 ; i < datas.size() ; i ++)
                    {
                        imageUrl.add(datas.get(i).getPicpath());
                        imageId.add(datas.get(i).getId());
                    }
                }
                Handler dataHandler = new Handler(
                        getContext().getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
                        if(imageUrl!=null&&imageUrl.size()>0)
                        {
                            //简单使用
                            if(imageUrl!=null){
                                banner.setImages(imageUrl)
                                        .setImageLoader(new GlideImageLoader(null))
                                        .setOnBannerClickListener(new OnBannerClickListener() {
                                            @Override
                                            public void OnBannerClick(int position) {
                                                startActivity(new Intent(getActivity(), GoodsDetialsActivity.class).putExtra("id",datas.get(position-1).getId()));
                                            }
                                        });
                                banner.start();
                            }
                        }
                    }
                };
                dataHandler.sendEmptyMessage(0);
                // mZProgressHUD.cancel();
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
//                sToast("链接超时！");
                // mZProgressHUD.cancel();
            }
        });
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        getPic();
    }



    //如果你需要考虑更好的体验，可以这么操作
    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }


    @Override
    public void onResume() {
        super.onResume();

    }






    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

        } else {
            //相当于Fragment的onPause
        }
    }

    @Override
    public void initData() {

    }

    private void getJsonDate(int isClear) {
        if (isClear == 1) {
            goods.clear();
            gifLoading.setVisibility(View.VISIBLE);
        }

        Map<String, String> par = new HashMap<String, String>();
        par.put("page", page+"");
        par.put("kid",typeId);

        HttpConnect.post((MallActivity)getActivity(), "products_list", par, new Callback() {


            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject data = JSONObject.fromObject(arg0.body()
                        .string().trim());

                if (data.get("status").equals("success")) {
                    JSONArray dataArr = data.getJSONArray("data");
                    for(int i = 0 ; i < dataArr.size() ; i ++)
                    {
                        JSONObject jsonObjNews = dataArr.optJSONObject(i);
                        if(jsonObjNews!=null)
                        {
                            String id = jsonObjNews.getString("id");
                            String code = jsonObjNews.getString("code");
                            String name = jsonObjNews.getString("name");
                            String pic = jsonObjNews.getString("pic");
                            String unitprice = jsonObjNews.getString("unitprice");
                            String unitctc = jsonObjNews.getString("unitctc");
                            String description = jsonObjNews.getString("description");
                            String qty = jsonObjNews.getString("qty");
                            String qtyallow = jsonObjNews.getString("qtyallow");
                            String qtysell = jsonObjNews.getString("qtysell");
                            String brandname = jsonObjNews.getString("brandname");
                            String brandpic = jsonObjNews.getString("brandpic");
                            String datebegin = jsonObjNews.getString("datebegin");
                            String dateend = jsonObjNews.getString("dateend");
                            String sysdate = jsonObjNews.getString("sysdate");
                            Goods goods1 = new Goods(id, code, name, pic, unitprice, unitctc, description, qty, qtyallow, qtysell, brandname, brandpic, datebegin, dateend, sysdate);
                            goods.add(goods1);
                        }
                    }


                } else {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                        }
                    });
                }


                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        adapter.setItems(goods);
                        adapter.notifyDataSetChanged();

                        gifLoading.setVisibility(View.GONE);


                    }
                });
            }


            @Override
            public void onFailure(Request arg0, IOException arg1) {

                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        gifLoading.setVisibility(View.GONE);

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
            getJsonDate(2);
            isLastRow = false;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//如果当前列表的数量等于查询的总数量,则不做任何操作

        //判断是否滚到最后一行
        if (firstVisibleItem + visibleItemCount == totalItemCount  && totalItemCount > 0) {
            isLastRow = true;
        }

    }


}
