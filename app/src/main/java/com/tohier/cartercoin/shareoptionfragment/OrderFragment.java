package com.tohier.cartercoin.shareoptionfragment;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.tohier.android.fragment.base.BaseFragment;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.activity.ShareOptionActivity;
import com.tohier.cartercoin.adapter.DealOrderAdapter;
import com.tohier.cartercoin.bean.DealRecord;
import com.tohier.cartercoin.presenter.DealPresenter;
import com.tohier.cartercoin.presenter.ShareOptionPresener;
import com.tohier.cartercoin.ui.DealView;
import com.tohier.cartercoin.ui.ShareOptionView;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderFragment extends BaseFragment implements DealView,ShareOptionView {
    private View view;
    private ListView lvDeal;
    private ImageView imageView;
    private ArrayList<DealRecord> listDeal = new ArrayList<DealRecord>();
    private DealPresenter dealPresenter;
    private String type = "0";
    private DealOrderAdapter adapter ;
    private ShareOptionPresener shareOptionPresener;
    private ViewPager viewPager;
    private double currentPrice;
    private int isFrist = 1;

    public Handler getHandler() {
        return handler;
    }

    public Thread getThread() {
        return thread;
    }

    private int time;

    public void setTime(int time) {
        this.time = time;
    }

    public void setIsFrist(int isFrist) {
        this.isFrist = isFrist;
    }

    public ListView getLvDeal() {
        return lvDeal;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0x111){
                dealPresenter.getDealRecord(type,"member_option_self_no","1");
                handler.postDelayed(thread,time*1000);
            }else if(msg.what == 0x112){
                shareOptionPresener.getBtcPrice();
                handler.postDelayed(thread1,1000);
            }
        }
    };

    Thread thread = new Thread(){
        @Override
        public void run() {
            handler.sendEmptyMessage(0x111);
        }
    };

    Thread thread1 = new Thread(){
        @Override
        public void run() {
            handler.sendEmptyMessage(0x112);
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_order_layout,container,false);

        init();
        setUp();

        return view;
    }

    private void setUp() {
//        lvDeal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                DealRecord dealRecord = listDeal.get(position);
//
//                Intent intent = new Intent();
//                intent.setClass(getActivity(), DealDetialsActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("DealRecord", dealRecord);
//                intent.putExtras(bundle);
//                getActivity().startActivity(intent);
//            }
//        });


    }

    private void init() {
        lvDeal = (ListView) view.findViewById(R.id.lv_deal);
        imageView = (ImageView) view.findViewById(R.id.imageview);
        adapter = new DealOrderAdapter(getActivity(),listDeal);
        lvDeal.setAdapter(adapter);
        setListViewHeightBasedOnChildren(lvDeal);


        dealPresenter = new DealPresenter(this,this);
        shareOptionPresener = new ShareOptionPresener(this,(ShareOptionActivity)getActivity());
        handler.post(thread1);

    }

    @Override
    public void initData() {

    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        handler.removeCallbacks(thread1);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        setListViewHeightBasedOnChildren(lvDeal);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            handler.post(thread);
        }else{
            handler.removeCallbacks(thread);
        }

    }

    @Override
    public void getBtcKLineUrl(String url) {

    }

    @Override
    public void fail(String msg) {
    }

    @Override
    public void getBtcPrice(double price, String time) {
        currentPrice = price;
        adapter.setCurrentPrice(currentPrice);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void getTimes(ArrayList<HashMap<String, String>> times) {

    }

    @Override
    public void getCoinTypes(ArrayList<HashMap<String,String>> coinTypes) {

    }

    @Override
    public void getTrends(ArrayList<HashMap<String, String>> trends) {

    }

    @Override
    public void getCTC(String ctc, String money,String ctcoption) {

    }

    @Override
    public void optionDeal() {

    }

    @Override
    public void failure(String msg) {
        if (msg.equalsIgnoreCase("nodata")){
            try{
                listDeal.clear();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.updateNotify(listDeal);

                    }
                });
            }catch (Exception e){}

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void getDeals(final ArrayList<DealRecord> list) {
        if (list.size()>0){
            listDeal.clear();
            listDeal.addAll(list);
            if (isFrist == 1 ){
                if(null!=getActivity())
                {
                    if(!getActivity().isDestroyed())
                    {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.updateNotify(listDeal);
                                setListViewHeightBasedOnChildren(lvDeal);
                            }
                        });
                    }
                }
                isFrist = 2;
            }else {
                if(null!=getActivity())
                {
                    if(!getActivity().isDestroyed())
                    {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.updateNotify(listDeal);
                            }
                        });
                    }
                }
            }

            if(null!=getActivity())
            {
                if(!getActivity().isDestroyed())
                {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setVisibility(View.GONE);
                        }
                    });
                }
            }
        }else{

            if(null!=getActivity())
            {
                if(!getActivity().isDestroyed())
                {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setVisibility(View.VISIBLE);
                            handler.removeCallbacks(thread);
                        }
                    });
                }
            }
        }

    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        listView.setFocusable(false);

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        totalHeight = totalHeight+500;



        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() + 1));

//        ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10); // 可删除

        listView.setLayoutParams(params);

        listView.setLayoutParams(params);
        ViewGroup.LayoutParams viewPagerParams = viewPager.getLayoutParams();
        viewPagerParams.height =  params.height;
        viewPager.setLayoutParams(viewPagerParams);

        if (listDeal.size()==0){
            imageView.setVisibility(View.VISIBLE);

        }else{
            imageView.setVisibility(View.GONE);
        }
    }

}
