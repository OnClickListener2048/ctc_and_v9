package com.tohier.cartercoin.dianzanfragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.IYWConversationUnreadChangeListener;
import com.tohier.android.fragment.base.BaseFragment;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.activity.FengYunRankingActivity;
import com.tohier.cartercoin.adapter.DianZanRankingAdapter;
import com.tohier.cartercoin.bean.ZanRanking;
import com.tohier.cartercoin.columnview.PullToRefreshLayout;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.MyApplication;
import com.tohier.cartercoin.listener.StudentsListener;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

public class TongXueHui_Ranking extends BaseFragment {

	private View view;
	private GridView lv;
	private DianZanRankingAdapter adapter;
	private ArrayList<ZanRanking> list_data = new ArrayList<ZanRanking>();
	private TextView tv_danzan_tishi,tv_dianzan_time,tv_href_gonggao;
	private LoginUser.LoginUserEntity user;
	private GifImageView gifImageView;
	private StudentsListener listener;
	private Handler mHandler = new Handler(Looper.getMainLooper());

	public StudentsListener getListener() {
		return listener;
	}

	public GridView getLv() {
		return lv;
	}

	/**
	 * 监听未读消息数
	 */
	private IYWConversationService mConversationService;
	private IYWConversationUnreadChangeListener mConversationUnreadChangeListener;

	public ArrayList<ZanRanking> getList_data() {
		return list_data;
	}

	public void setList_data(ArrayList<ZanRanking> list_data) {
		this.list_data = list_data;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = View.inflate(getActivity(), R.layout.fragment_dianzan_rangking, null);

		user = LoginUser.getInstantiation(getActivity().getApplicationContext()).getLoginUser();

		//此实现不一定要放在Application onCreate中

//		//此对象获取到后，保存为全局对象，供APP使用
//		//此对象跟用户相关，如果切换了用户，需要重新获取
//		mIMKit = YWAPI.getIMKitInstance(userid, MyApplication.APP_KEY);
		mConversationService = ((FengYunRankingActivity)getActivity()).mIMKit.getConversationService();

		init();
		return view;
	}
	
	private void init() {
		gifImageView = (GifImageView) view.findViewById(R.id.gif_loading);
		tv_danzan_tishi = (TextView) view.findViewById(R.id.tv_dianzan_tishi);
		tv_href_gonggao = (TextView) view.findViewById(R.id.tv_href_gonggao);
		tv_danzan_tishi.setText("");
		tv_href_gonggao.setText("");
		lv = (GridView) view.findViewById(R.id.content_view);
		adapter = new DianZanRankingAdapter(getContext(), list_data ,1,((FengYunRankingActivity)getActivity()).mIMKit );

		listener = new StudentsListener(getActivity(),adapter,gifImageView);

		lv.setAdapter(adapter);
		lv.setSelector(new ColorDrawable(Color.TRANSPARENT));

		((PullToRefreshLayout)view. findViewById(R.id.refresh_view)).setOnRefreshListener(listener);


		/**
		 * 解析数据
		 */
		listener.getJsonDate(null);



		initConversationServiceAndListener();

		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String userid = listener.getDatas().get(position).getLinkCode();
				Intent intent = ((FengYunRankingActivity)getActivity()).mIMKit.getChattingActivityIntent(userid, MyApplication.APP_KEY);
				startActivity(intent);
			}
		});
	}


	@Override
	public void onResume() {
		super.onResume();
		//resume时需要检查全局未读消息数并做处理，因为离开此界面时删除了全局消息监听器
		mConversationUnreadChangeListener.onUnreadChange();

	}

	@Override
	public void initData() {
		
	}



	/**
	 * 当未读数发生变化时会回调该方法，开发者可以在该方法中更新未读数
	 *
	 */
	private void initConversationServiceAndListener() {
		mConversationUnreadChangeListener = new IYWConversationUnreadChangeListener() {

			@Override
			public void onUnreadChange() {
				mHandler.post(new Runnable() {
					@Override
			public void run() {
						adapter.notifyDataSetChanged();
					}
				});
			}
		};
		mConversationService.addTotalUnreadChangeListener(mConversationUnreadChangeListener);
	}
	

}
