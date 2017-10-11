package com.tohier.cartercoin.activity;

import android.os.Bundle;
import android.util.Log;
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
import java.util.Map;

public class ZhuanChuJiLuActivity extends
		MyBackBaseActivity {

	private ListView autoListView;
	private ImageView iv_back;
	private NoDataView iv_isnull;
	private String ctcoutfee;
	private LoadingView gif_loading;
	private NoLinkView noLinkView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_tixianjilu);

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
		gif_loading = (LoadingView) this.findViewById(R.id.gif_loading);
		iv_back = (ImageView) this.findViewById(R.id.iv_back4);
		iv_isnull = (NoDataView) this.findViewById(R.id.iv_nodata);
		noLinkView = (NoLinkView) findViewById(R.id.iv_no_link);
		((TextView)this.findViewById(R.id.tv_title4)).setText("转出记录");

	}


	private void setUpView() {
		iv_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ZhuanChuJiLuActivity.this.finish();
			}
		});

		noLinkView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Tools.getAPNType(ZhuanChuJiLuActivity.this) == true){
					noLinkView.setVisibility(View.GONE);
					getJsonString();
				}else{
					noLinkView.setVisibility(View.VISIBLE);
				}
			}
		});


//		autoListView.setonRefreshListener(refreshListener);
	}

	private void getJsonString() {

		gif_loading.setVisibility(View.VISIBLE);

		HttpConnect.post(this, "member_ctc_out_fee", null, new Callback() {

			@Override
			public void onResponse(Response arg0) throws IOException {
				// TODO Auto-generated method stub
				JSONObject json = JSONObject.fromObject(arg0.body().string());

				if (json.get("status").equals("success")) {
					ctcoutfee = json.getJSONArray("data").getJSONObject(0).getString("ctcoutfee");
					final List<ZhuanChuJiLuData> datas = new ArrayList<ZhuanChuJiLuData>();
					//在访问网络为果之前 先显示进度条

					Map<String,String> map = new HashMap<String,String>();
					map.put("id", getIntent().getStringExtra("type"));
					HttpConnect.post(ZhuanChuJiLuActivity.this, "member_ctc_out_list", map, new Callback() {

						@Override
						public void onResponse(Response arg0) throws IOException {
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
											Log.e("tag1", address);
											String qty = jsonObj.getString("qty");
											Log.e("tag2", qty);
											String createdate = jsonObj.getString("createdate");
											Log.e("tag3", createdate);
											String status = jsonObj.getString("status");
											Log.e("tag4", status);
											String id = jsonObj.getString("id");
											String free = jsonObj.getString("free");
											ZhuanChuJiLuData  zhuanChuJiLuData =  new ZhuanChuJiLuData(id , address, qty, createdate, status,free);
											datas.add(zhuanChuJiLuData);
										}
									}
								}
							} else {
								ZhuanChuJiLuActivity.this.runOnUiThread(new Runnable() {

									@Override
									public void run() {
										gif_loading.setVisibility(View.GONE);
										iv_isnull.setVisibility(View.VISIBLE);
									}
								});
//									sToast("暂无数据");
							}
							ZhuanChuJiLuActivity.this.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									gif_loading.setVisibility(View.GONE);
									if (datas.size()>0){
										iv_isnull.setVisibility(View.GONE);
									}else{
										iv_isnull.setVisibility(View.VISIBLE);
									}
									autoListView.setAdapter(new ZhuanChuJiLu_Lv_Adapter(datas, ZhuanChuJiLuActivity.this));
								}
							});
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
				} else {
					ZhuanChuJiLuActivity.this.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							gif_loading.setVisibility(View.GONE);
							iv_isnull.setVisibility(View.VISIBLE);
						}
					});
				}

			}

			@Override
			public void onFailure(Request arg0, IOException arg1) {

				ZhuanChuJiLuActivity.this.runOnUiThread(new Runnable() {

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
