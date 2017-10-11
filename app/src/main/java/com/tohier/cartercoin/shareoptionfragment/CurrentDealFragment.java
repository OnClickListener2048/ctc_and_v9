package com.tohier.cartercoin.shareoptionfragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.tohier.android.fragment.base.BaseFragment;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.adapter.DealAdapter;
import com.tohier.cartercoin.bean.DealRecord;
import com.tohier.cartercoin.presenter.DealPresenter;
import com.tohier.cartercoin.ui.DealView;

import java.util.ArrayList;

public class CurrentDealFragment extends BaseFragment implements DealView{
    private View view;
    private ListView lvDeal;
    private ArrayList<DealRecord> listDeal = new ArrayList<DealRecord>();
    private DealPresenter dealPresenter;
    private String type = "0";
    private DealAdapter adapter ;
    private ViewPager viewPager;
    private int isFrist = 1;

    public void setIsFrist(int isFrist) {
        this.isFrist = isFrist;
    }

    public ListView getLvDeal() {
        return lvDeal;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    private int count = 0;
    private int time;

    public void setTime(int time) {
        this.time = time;
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
                dealPresenter.getDealRecord(type,"member_option_record","1");
                handler.postDelayed(thread,time*1000);
            }
        }
    };

    Thread thread = new Thread(){
        @Override
        public void run() {
            handler.sendEmptyMessage(0x111);
        }
    };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_currentdeal_layout,container,false);

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

        adapter = new DealAdapter(getActivity(),listDeal,0);
        lvDeal.setAdapter(adapter);
        setListViewHeightBasedOnChildren(lvDeal);

        dealPresenter = new DealPresenter(this,this);
    }

    @Override
    public void initData() {
        
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        setListViewHeightBasedOnChildren(lvDeal);
        if (hidden){
            handler.removeCallbacks(thread);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.post(thread);
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(thread);
    }

    @Override
    public void failure(String msg) {

    }

    @Override
    public void getDeals(final ArrayList<DealRecord> list) {
        try{
            listDeal.clear();
            listDeal.addAll(list);
            if (isFrist == 1 ){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        setListViewHeightBasedOnChildren(lvDeal);
                    }
                });
                isFrist = 2;
            }else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
//                setListViewHeightBasedOnChildren(lvDeal);
                    }
                });
            }
        }catch(Exception e){

        }


    }

    public void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        listView.setFocusable(false);

        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        totalHeight = totalHeight+100;

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount()+1));

//        ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10); // 可删除

        listView.setLayoutParams(params);

        listView.setLayoutParams(params);
        ViewGroup.LayoutParams viewPagerParams = viewPager.getLayoutParams();
        viewPagerParams.height =  params.height;
        viewPager.setLayoutParams(viewPagerParams);
    }

}
