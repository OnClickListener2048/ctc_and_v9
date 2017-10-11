package com.tohier.cartercoin.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.adapter.ZhuanChuJiLu_Lv_Adapter;
import com.tohier.cartercoin.bean.ZhuanChuJiLuData;
import com.tohier.cartercoin.columnview.LoadingView;
import com.tohier.cartercoin.columnview.NoDataView;
import com.tohier.cartercoin.columnview.NoLinkView;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.Tools;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ZhuanRuJiLuActivity extends MyBackBaseActivity {


	private ListView autoListView;
	private ImageView iv_back;

	private NoDataView iv_isnull;
	private LoadingView gif_loading;
	private NoLinkView noLinkView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_zhuanrujilu);

		init();
		setUpView();


		if (Tools.getAPNType(this) == true){
			noLinkView.setVisibility(View.GONE);
			getJsonString();
		}else{
			noLinkView.setVisibility(View.VISIBLE);
		}

	}

	private void init() {
		autoListView = (ListView) this.findViewById(R.id.lv_tixianjilu);
		iv_back = (ImageView) this.findViewById(R.id.iv_back4);
		iv_isnull = (NoDataView) this.findViewById(R.id.iv_nodata);
		noLinkView = (NoLinkView) findViewById(R.id.iv_no_link);
		gif_loading = (LoadingView) this.findViewById(R.id.gif_loading);
		((TextView)this.findViewById(R.id.tv_title4)).setText("转入记录");

	}


	private void setUpView() {
		iv_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ZhuanRuJiLuActivity.this.finish();
			}
		});


		noLinkView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Tools.getAPNType(ZhuanRuJiLuActivity.this) == true){
					noLinkView.setVisibility(View.GONE);
					getJsonString();
				}else{
					noLinkView.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	private void getJsonString() {
		gif_loading.setVisibility(View.VISIBLE);
		HashMap<String ,String > map = new HashMap<String ,String >();
		map.put("id",getIntent().getStringExtra("type"));
		HttpConnect.post(ZhuanRuJiLuActivity.this, "member_ctc_in_list", map, new Callback() {

			@Override
			public void onResponse(Response arg0) throws IOException {
				final List<ZhuanChuJiLuData> datas = new ArrayList<ZhuanChuJiLuData>();
				JSONObject jsonStr = JSONObject.fromObject(arg0.body().string());
				if (jsonStr.get("status").equals("success")) {
					net.sf.json.JSONArray data  = jsonStr.getJSONArray("data");
					if(data!=null&&data.size()!=0)
					{
						for(int i = 0 ; i < data.size() ; i ++)
						{
							JSONObject jsonObj = data.optJSONObject(i);
							if(jsonObj!=null)
							{
								String address =	jsonObj.getString("address");
								String qty = jsonObj.getString("qty");
								String createdate = jsonObj.getString("createdate");
								String status = jsonObj.getString("status");
								String id = jsonObj.getString("id");
								ZhuanChuJiLuData  zhuanChuJiLuData =  new ZhuanChuJiLuData(id , address, qty, createdate, status,"");
								datas.add(zhuanChuJiLuData);
							}
						}
						ZhuanRuJiLuActivity.this.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								gif_loading.setVisibility(View.GONE);
								if (datas.size()>0){
									iv_isnull.setVisibility(View.GONE);
								}else{
									iv_isnull.setVisibility(View.VISIBLE);
								}
								autoListView.setAdapter(new ZhuanChuJiLu_Lv_Adapter(datas, ZhuanRuJiLuActivity.this));
							}
						});
					}
				}else
				{
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							gif_loading.setVisibility(View.GONE);
							if (datas.size()>0){
								iv_isnull.setVisibility(View.GONE);
							}else{
								iv_isnull.setVisibility(View.VISIBLE);
							}
							autoListView.setAdapter(new ZhuanChuJiLu_Lv_Adapter(datas, ZhuanRuJiLuActivity.this));
						}
					});
				}

			}

			@Override
			public void onFailure(Request arg0, IOException arg1) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						gif_loading.setVisibility(View.GONE);
						iv_isnull.setVisibility(View.VISIBLE);
					}
				});
			}
		});
	}

	@Override
	public void initData() {

	}
}
