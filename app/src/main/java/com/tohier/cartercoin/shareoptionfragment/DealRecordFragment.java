package com.tohier.cartercoin.shareoptionfragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tohier.android.fragment.base.BaseFragment;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.activity.MoreDealRecordActivity;
import com.tohier.cartercoin.adapter.DealAdapter;
import com.tohier.cartercoin.bean.DealRecord;
import com.tohier.cartercoin.presenter.DealPresenter;
import com.tohier.cartercoin.ui.DealView;

import java.util.ArrayList;

public class DealRecordFragment extends BaseFragment implements DealView {
    private View view;
    private ListView lvDeal;
    private TextView tvMore;
    private ArrayList<DealRecord> listDeal = new ArrayList<DealRecord>();
    private ImageView imageView;
    private DealPresenter dealPresenter;
    private String type = "0";
    private DealAdapter adapter ;
    private ViewPager viewPager;
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
                dealPresenter.getDealRecord(type,"member_option_self_has","1");
//                handler.postDelayed(thread,time*1000);
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
        view =  inflater.inflate(R.layout.fragment_dealrecord_layout,container,false);
        init();
        setup();

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (getUserVisibleHint() && lvDeal.getVisibility() != View.VISIBLE) {
            dealPresenter.getDealRecord(type,"member_option_self_has","1");
        }
        super.onActivityCreated(savedInstanceState);
    }

    private void setup() {
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


        tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MoreDealRecordActivity.class).putExtra("type",type));
            }
        });

    }


    private void init() {
        lvDeal = (ListView) view.findViewById(R.id.lv_deal);
        tvMore = (TextView) view.findViewById(R.id.tv_more);
        imageView = (ImageView) view.findViewById(R.id.imageview);
        adapter = new DealAdapter(getActivity(),listDeal,0);
        lvDeal.setAdapter(adapter);
        setListViewHeightBasedOnChildren(lvDeal,listDeal.size());


        dealPresenter = new DealPresenter(this,this);

    }

    @Override
    public void initData() {
        
    }


    @Override
    public void onResume() {
        super.onResume();
//        handler.post(thread);
        dealPresenter.getDealRecord(type,"member_option_self_has","1");
    }

    @Override
    public void onStop() {
        super.onStop();
//        handler.removeCallbacks(thread);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        dealPresenter.getDealRecord(type,"member_option_self_has","1");
//        setListViewHeightBasedOnChildren(lvDeal);
        if (hidden){
            handler.removeCallbacks(thread);
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        //判断Fragment中的ListView时候存在，判断该Fragment时候已经正在前台显示  通过这两个判断，就可以知道什么时候去加载数据了
        if (isVisibleToUser && isVisible() && lvDeal.getVisibility() != View.VISIBLE) {
            dealPresenter.getDealRecord(type,"member_option_self_has","1"); //加载数据的方法
        }
        super.setUserVisibleHint(isVisibleToUser);
    }



    @Override
    public void failure(String msg) {
//        sToast(msg);
    }

    @Override
    public void getDeals(final ArrayList<DealRecord> list) {
        listDeal.clear();
        listDeal.addAll(list);
        if (isFrist == 1 ){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                    setListViewHeightBasedOnChildren(lvDeal,list.size());
                }
            });
            isFrist = 2;
        }else {
            try{
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }catch (Exception e){

            }

        }

    }


    public void setListViewHeightBasedOnChildren(ListView listView,int size) {
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


        if (size>3){
            totalHeight = totalHeight+200;
        }else{
            totalHeight = totalHeight+400;
        }


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
