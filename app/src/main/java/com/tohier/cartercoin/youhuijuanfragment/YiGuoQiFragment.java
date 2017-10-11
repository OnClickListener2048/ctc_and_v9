package com.tohier.cartercoin.youhuijuanfragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.tohier.android.fragment.base.BaseFragment;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.adapter.YouHuiJuan_Adapter;
import com.tohier.cartercoin.bean.YouHuiQuan;
import com.tohier.cartercoin.listener.YiGuoQiListener;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class YiGuoQiFragment extends BaseFragment {

	private View view;
	private ListView gridView;
	private List<YouHuiQuan> datas = new ArrayList<YouHuiQuan>();
	private YiGuoQiListener myListener;
	private YouHuiJuan_Adapter adapter;
	private ImageView iv_isnull;
	private GifImageView gif_loading;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = View.inflate(getActivity(), R.layout.activity_youhuijuan_listview, null);
		init();
		return view;
	}
	
	private void init() {
		gridView = (ListView) view.findViewById(R.id.listview);
		iv_isnull = (ImageView) view.findViewById(R.id.iv_isnull);
		gif_loading = (GifImageView) view.findViewById(R.id.gif_loading);
		adapter = new YouHuiJuan_Adapter(datas, getActivity());
		myListener = new YiGuoQiListener(adapter,iv_isnull,gif_loading,getActivity());
		gridView.setAdapter(adapter);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
//		((PullToRefreshLayout)view. findViewById(R.id.refresh_view)).setOnRefreshListener(myListener);
		
//
//		gridView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				Intent intent = new Intent(getActivity(),ShangPingDetailActivity.class);
//				intent.putExtra("productId", adapter.getItems().get(position).getId());
//				intent.putExtra("isMiaoSha", "jingpin");
////				intent.putExtra("money",adapter.getItems().get(position).getUnitprice());
////				intent.putExtra("ctc",adapter.getItems().get(position).getUnitctc());
////				int shengyu = Integer.parseInt(adapter.getItems().get(position).getQty())-Integer.parseInt(adapter.getItems().get(position).getQtysell());
////				getActivity().getSharedPreferences("kucun", Context.MODE_APPEND).edit().putInt("kucun",shengyu).commit();
////				intent.putExtra("url", myListener.getDatas().get(position).getUrl());
////				intent.putExtra("title",myListener.getDatas().get(position).getTitle());
////				intent.putExtra("desc", myListener.getDatas().get(position).getDesc());
//                startActivity(intent);
//			}
//		});
	}

	@Override
	public void onResume() {
		super.onResume();
		myListener.getDatas().clear();
		myListener.getJsonDate(null);
	}

	@Override
	public void onStop() {
		super.onStop();
	}
	
	@Override
	public void initData() {
		
	}

}
