package com.tohier.cartercoin.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.adapter.TiXianJiLuAdapter;
import com.tohier.cartercoin.bean.TiXianData;
import com.tohier.cartercoin.columnview.LoadingView;
import com.tohier.cartercoin.columnview.NoDataView;
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

public class TiXianJiLuActivity extends MyBackBaseActivity implements OnClickListener{
	private ListView listView;
	TiXianJiLuAdapter adapter;
	private ImageView iv_back;
	ArrayList<TiXianData> list = new ArrayList<TiXianData>();
	private String msg ;
	private NoDataView iv_isnull;
	private LoadingView gifImageView;
	private NoLinkView ivNoLink;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tixianjilu);
		initView();
		if (Tools.getAPNType(TiXianJiLuActivity.this) == true){
			ivNoLink.setVisibility(View.GONE);
			getJsonData();
		}else{
			ivNoLink.setVisibility(View.VISIBLE);
		}


	}



	private void initView() {
		// TODO Auto-generated method stub
		listView = (ListView) findViewById(R.id.lv_tixianjilu);
		gifImageView = (LoadingView) findViewById(R.id.gif_loading);
		iv_isnull = (NoDataView) findViewById(R.id.iv_nodata);
		ivNoLink = (NoLinkView) findViewById(R.id.iv_no_link);

		iv_back = (ImageView) findViewById(R.id.iv_back4);
		iv_back.setOnClickListener(this);

		ivNoLink.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Tools.getAPNType(TiXianJiLuActivity.this) == true){
					ivNoLink.setVisibility(View.GONE);
					getJsonData();
				}else{
					ivNoLink.setVisibility(View.VISIBLE);
				}
			}
		});
		
	}
	
	public void getJsonData() {
		list.clear();
		gifImageView.setVisibility(View.VISIBLE);
		// TODO Auto-generated method stub
		Map<String,String> map = new HashMap<String,String>();
		map.put("id",  LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getUserId());
		
		HttpConnect.post(this, "member_cash_out_list", map, new Callback() {
			
			@Override
			public void onResponse(Response arg0) throws IOException {


				JSONObject jsonObject = JSONObject.fromObject(arg0.body().string());
				msg = jsonObject.getString("msg");
				if (jsonObject.get("status").equals("success")) {
					JSONArray jsonArray = jsonObject.getJSONArray("data");
					if (jsonArray != null) {
						for (int i = 0; i < jsonArray.size(); i++) {
							
							JSONObject jsonObject2 = jsonArray.getJSONObject(i);
							if (jsonObject2 != null) {

								String bankcard = jsonObject2
										.getString("bankcard");
								String qty = jsonObject2
										.getString("qty");
								String counterfee = jsonObject2
										.getString("counterfee");
								String createdate = jsonObject2.getString("createdate");
								String status = jsonObject2
										.getString("status");

								String cashoutid = jsonObject2
										.getString("cashoutid");
								
								TiXianData tixian = new TiXianData(bankcard, qty, createdate, status ,counterfee,cashoutid);
								list.add(tixian);
							}
						}
						TiXianJiLuActivity.this.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								gifImageView.setVisibility(View.GONE);
								adapter = new TiXianJiLuAdapter(TiXianJiLuActivity.this, list);
								listView.setAdapter(adapter);
							}
						});
					}
				} else {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							gifImageView.setVisibility(View.GONE);
							iv_isnull.setVisibility(View.VISIBLE);
						}
					});
//					sToast("暂无数据");
				}

			}
			
			@Override
			public void onFailure(Request arg0, IOException arg1) {
				// TODO Auto-generated method stub
				gifImageView.setVisibility(View.GONE);
			}
		});
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.iv_back4:
			
			this.finish();

			break;
	
		}
	}

}
