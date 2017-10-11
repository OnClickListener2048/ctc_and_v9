package com.tohier.cartercoin.mefragmentgonggaolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.tohier.android.fragment.base.BaseFragment;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.activity.GongGaoDetailActivity;
import com.tohier.cartercoin.adapter.GongGaoListAdapter;
import com.tohier.cartercoin.bean.News;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/1.
 */

public class MeFragmentGongGaoList1 extends BaseFragment{

    private View view;
    private ListView listView;
    private ArrayList<News> datas = new ArrayList<News>();

    /**
     * 构造方法将传过来 我的页面的 viewpager 以及 srcollview 对象
     */
    private ViewPager viewPager;
    private GongGaoListAdapter gongGaoListAdapter;


    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    public MeFragmentGongGaoList1() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_gonggao_listview_layout,container,false);
        initData();
        setUpView();
        return view;
    }

    @Override
    public void initData() {
        listView = (ListView) view.findViewById(R.id.listview_gonggao);
        gongGaoListAdapter = new GongGaoListAdapter(getActivity(),datas,"213");
        listView.setAdapter(gongGaoListAdapter);
        setListViewHeightBasedOnChildren(listView);
    }


    private void setUpView() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getActivity(),GongGaoDetailActivity.class);
                intent.putExtra("url", gongGaoListAdapter.getList().get(position).getUrl());
                intent.putExtra("title",gongGaoListAdapter.getList().get(position).getTitle());
                intent.putExtra("desc", gongGaoListAdapter.getList().get(position).getDesc());
                startActivity(intent);
            }
        });
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {

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

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

//        ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10); // 可删除

        listView.setLayoutParams(params);
        ViewGroup.LayoutParams viewPagerParams = viewPager.getLayoutParams();
        viewPagerParams.height =  listView.getLayoutParams().height;
        viewPager.setLayoutParams(viewPagerParams);
    }

    public ListView getListView() {
        return listView;
    }


    public void loadSuccess(List<News> news) {
//        datas.addAll(news);
        List<News> list = gongGaoListAdapter.getList();
        for(int i = 0 ; i < news.size() ; i ++)
        {
            if(!list.contains(news.get(i).getTitle()))
            {
                list.add(news.get(i));
            }
        }
        gongGaoListAdapter.setList(list);
        gongGaoListAdapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(listView);
    }
}
