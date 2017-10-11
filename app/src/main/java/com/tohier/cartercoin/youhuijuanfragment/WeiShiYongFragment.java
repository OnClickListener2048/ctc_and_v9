package com.tohier.cartercoin.youhuijuanfragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.tohier.android.fragment.base.BaseFragment;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.adapter.YouHuiJuan_Adapter;
import com.tohier.cartercoin.bean.YouHuiQuan;
import com.tohier.cartercoin.listener.WeiShiYongListener;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class WeiShiYongFragment extends BaseFragment {

	private View view;
	private ListView gridView;
	private List<YouHuiQuan> datas = new ArrayList<YouHuiQuan>();
	private WeiShiYongListener myListener;
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
		myListener = new WeiShiYongListener(getActivity(),adapter,iv_isnull,gif_loading);
		gridView.setAdapter(adapter);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));

//		((PullToRefreshLayout)view. findViewById(R.id.refresh_view)).setOnRefreshListener(myListener);
		
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}
		});
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

	public ImageView getIv_isnull() {
		return iv_isnull;
	}

	public GifImageView getGif_loading() {
		return gif_loading;
	}
}
