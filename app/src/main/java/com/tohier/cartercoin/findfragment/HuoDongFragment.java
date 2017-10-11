package com.tohier.cartercoin.findfragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.android.fragment.base.BaseFragment;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.activity.GongGaoActivity;
import com.tohier.cartercoin.activity.GongGaoDetailActivity;
import com.tohier.cartercoin.adapter.GongGaoListAdapter;
import com.tohier.cartercoin.bean.News;
import com.tohier.cartercoin.columnview.LoadingView;
import com.tohier.cartercoin.columnview.NoLinkView;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.Tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HuoDongFragment extends BaseFragment implements AbsListView.OnScrollListener{

	private View view;
	private ListView gridView;
	private ArrayList<News> datas = new ArrayList<News>();
	private GongGaoListAdapter adapter;

	private LoadingView cif_loading;

	private boolean isLastRow = false;
	private int page = 1;

	private LoadingView loadingView;
	private NoLinkView ivNoLink;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = View.inflate(getActivity(), R.layout.fragment_find_gonggao2, null);
		init();
		return view;
	}
	
	private void init() {
		gridView = (ListView) view.findViewById(R.id.content_view);

		cif_loading  = (LoadingView) view.findViewById(R.id.cif_loading);
		ivNoLink = (NoLinkView) view.findViewById(R.id.iv_no_link);

		loadingView = new LoadingView(getActivity());
		loadingView.loadMore();
		loadingView.setGravity(Gravity.CENTER);

		adapter = new GongGaoListAdapter(getActivity(),datas,"gonggao");
		gridView.setAdapter(adapter);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));

		if (Tools.getAPNType(getActivity()) == true){
			ivNoLink.setVisibility(View.GONE);
			getJsonDate(1);
		}else{
			ivNoLink.setVisibility(View.VISIBLE);
		}
		
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									final int position, long id) {

				if (datas.get(position).getStatus().equalsIgnoreCase("1")){
					Intent intent = new Intent(getActivity(),GongGaoDetailActivity.class);
					intent.putExtra("url", datas.get(position).getUrl());
					intent.putExtra("title",datas.get(position).getTitle());
					intent.putExtra("desc", datas.get(position).getDesc());
					datas.get(position).setStatus("1");
					datas.get(position).setNumber(Integer.parseInt(datas.get(position).getNumber())+1+"");
					adapter.setList(datas);
					adapter.notifyDataSetChanged();
					startActivity(intent);
				}else{
					HashMap<String,String> map = new HashMap<String, String>();
					map.put("id",datas.get(position).getId());
					HttpConnect.post((GongGaoActivity)getActivity(), "member_read_arcticle", map, new Callback() {
						@Override
						public void onResponse(Response arg0) throws IOException {
							String json = arg0.body().string();
							JSONObject data = JSONObject.fromObject(json);
							if (data.optString("status").equals("success")){
								getActivity().runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Intent intent = new Intent(getActivity(),GongGaoDetailActivity.class);
										intent.putExtra("url", datas.get(position).getUrl());
										intent.putExtra("title",datas.get(position).getTitle());
										intent.putExtra("desc", datas.get(position).getDesc());
										datas.get(position).setStatus("1");
										datas.get(position).setNumber(Integer.parseInt(datas.get(position).getNumber())+1+"");
										adapter.setList(datas);
										adapter.notifyDataSetChanged();
										startActivity(intent);
									}
								});

							}else{
							}
						}
						@Override
						public void onFailure(Request arg0, IOException arg1) {

						}
					});
				}
			}
		});


		ivNoLink.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Tools.getAPNType(getActivity()) == true){
					ivNoLink.setVisibility(View.GONE);
					getJsonDate(1);
				}else{
					ivNoLink.setVisibility(View.VISIBLE);
				}
			}
		});

		gridView.setOnScrollListener(this);
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
			cif_loading.setVisibility(View.VISIBLE);

		}else{
			if (gridView.getFooterViewsCount()<=0){
				gridView.addFooterView(loadingView);
			}
			loadingView.setVisibility(View.VISIBLE);
		}
		Map<String, String> par = new HashMap<String, String>();
		par.put("kid", "2");
		par.put("page", page+"");
		par.put("type","0");
		par.put("id", LoginUser.getInstantiation(getActivity().getApplicationContext())
				.getLoginUser().getUserId());
		// mZProgressHUD.show();
		HttpConnect.post((GongGaoActivity)getActivity(), "article_list", par, new Callback() {

			private String msg;

			@Override
			public void onResponse(Response arg0) throws IOException {
				JSONObject data = JSONObject.fromObject(arg0.body()
						.string().trim());

				msg = data.getString("msg");

				if (data.get("status").equals("success")) {
					JSONArray dataArr = data.getJSONArray("data");
					for(int i = 0 ; i < dataArr.size() ; i ++)
					{
						JSONObject jsonObjNews = dataArr.optJSONObject(i);
						if(jsonObjNews!=null)
						{
							String id = jsonObjNews.getString("id");
							String title = jsonObjNews.getString("title");
							String pic = jsonObjNews.getString("pic");
							String clicks = jsonObjNews.getString("clicks");
							String likes = jsonObjNews.getString("likes");
							String createdate = jsonObjNews.getString("createdate");
							String url = jsonObjNews.getString("url");
							String desc = jsonObjNews.getString("description");
							String number = jsonObjNews.getString("number");
							News news = new News(id, title, pic, clicks, likes, createdate,desc,url);
							news.setStatus(jsonObjNews.getString("status"));
							news.setNumber(number);
							datas.add(news);
						}
					}

					if (isClear == 1) {
						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								gridView.addFooterView(loadingView);
							}
						});

					}
				} else {
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if (isClear == 1) {

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
						cif_loading.setVisibility(View.GONE);
					}
				});
			}

			@Override
			public void onFailure(Request arg0, IOException arg1) {
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						cif_loading.setVisibility(View.GONE);
					}
				});
			}
		});
	}

}
