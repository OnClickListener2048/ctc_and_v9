package com.tohier.cartercoin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.adapter.BankListAdapter;
import com.tohier.cartercoin.bean.CardData;
import com.tohier.cartercoin.columnview.LoadingView;
import com.tohier.cartercoin.columnview.NoDataView;
import com.tohier.cartercoin.columnview.NoLinkView;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.MyApplication;
import com.tohier.cartercoin.config.Tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BankListActivity extends MyBaseActivity implements OnClickListener {
	private ListView listView;
	private ImageView iv_back;
	private Button btn_add;
	BankListAdapter adapter;
	ArrayList<CardData> list = new ArrayList<CardData>();
	String account;

	private LoadingView cif_loading;
	private NoDataView ivNodata;
	private NoLinkView ivNoLink;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_banklist);

		MyApplication.maps.put("BankListActivity",this);

		initView();
		if (Tools.getAPNType(BankListActivity.this) == true){
			ivNoLink.setVisibility(View.GONE);
			getJsonData();
		}else{
			ivNoLink.setVisibility(View.VISIBLE);
		}


//		SliderConfig mConfig = new SliderConfig.Builder()
//				.primaryColor(Color.TRANSPARENT)
//				.secondaryColor(Color.TRANSPARENT)
//				.position(SliderPosition.LEFT)
//				.edge(false)
//				.build();
//
//		ISlider iSlider = SliderUtils.attachActivity(this, mConfig);
//		mConfig.setPosition(SliderPosition.LEFT);
//		iSlider.setConfig(mConfig);
	}

	private void initView() {
		// TODO Auto-generated method stub
		listView = (ListView) findViewById(R.id.lv);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		btn_add = (Button) findViewById(R.id.btn_add);
		ivNodata = (NoDataView) findViewById(R.id.iv_nodata);
		cif_loading  = (LoadingView) findViewById(R.id.cif_loading);
		ivNoLink = (NoLinkView) findViewById(R.id.iv_no_link);

		iv_back.setOnClickListener(this);
		btn_add.setOnClickListener(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Set<String> keys3 = MyApplication.maps.keySet();
				if(keys3!=null&&keys3.size()>0)
				{
					if(keys3.contains("TiXianActivity"))
					{
						Activity activity = MyApplication.maps.get("TiXianActivity");
						activity.finish();
					};
				}

				if(keys3!=null&&keys3.size()>0)
				{
					if(keys3.contains("PresentationModeActivity"))
					{
						Activity activity = MyApplication.maps.get("PresentationModeActivity");
						activity.finish();
					};
				}

				Intent intent = new Intent(BankListActivity.this,TiXianActivity.class);
				intent.putExtra("account", list.get(position).getAccount());
				intent.putExtra("id", list.get(position).getId());
				intent.putExtra("mode", "banklist");
				intent.putExtra("icon", list.get(position).getIconUrl());
				startActivity(intent);
				finish();
			}
		});

		ivNoLink.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Tools.getAPNType(BankListActivity.this) == true){
					ivNoLink.setVisibility(View.GONE);
					getJsonData();
				}else{
					ivNoLink.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	private void getJsonData() {
		// TODO Auto-generated method stub
		cif_loading.setVisibility(View.VISIBLE);
		Map<String, String> par = new HashMap<String, String>();
		
		String id = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getUserId();
		par.put("id", id);
		
		
		HttpConnect.post(this, "member_bank_list", par, new Callback() {

			@Override
			public void onResponse(Response arg0) throws IOException {

				
				JSONObject jsonObject = JSONObject.fromObject(arg0.body()
						.string());
				if (jsonObject.getString("status").equals("success")) {
					JSONArray jsonArray = jsonObject.getJSONArray("data");
					if (jsonArray != null) {
						for (int i = 0; i < jsonArray.size(); i++) {
							
							JSONObject jsonObject2 = jsonArray.getJSONObject(i);
							if (jsonObject2 != null) {
								String card_id = jsonObject2.getString("id");

								Log.d("ASD", "银行类型是：" + card_id);
								String banktype = jsonObject2
										.getString("banktype");
								
								Log.d("ASD", "银行类型是：" + banktype);
								
								String province = jsonObject2
										.getString("province");
								Log.d("ASD", "省市是：" + province);
								
								String bankname = jsonObject2
										.getString("bankname");
								Log.d("ASD", "银行名字：：" + province);
								
								String name = jsonObject2.getString("name");

								String iconurl = jsonObject2.getString("icon");
								account = jsonObject2
										.getString("account");
								CardData cardData = new CardData(card_id, banktype, account, province, bankname, name,iconurl);
								list.add(cardData);
							}
						}
					}

				} else {

				}

				BankListActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						adapter = new BankListAdapter(list, BankListActivity.this ,BankListActivity.this);
						listView.setAdapter(adapter);
						cif_loading.setVisibility(View.GONE);
						if (list.size()>0){
							ivNodata.setVisibility(View.GONE);
						}else{
							ivNodata.setVisibility(View.VISIBLE);
						}
					}
				});

			}

			@Override
			public void onFailure(Request arg0, IOException arg1) {
				BankListActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						cif_loading.setVisibility(View.GONE);

					}
				});

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
		switch (v.getId()) {

		case R.id.iv_back:
			finish();
			break;
		case R.id.btn_add:
			Intent intent = new Intent(BankListActivity.this,AddCardActivity.class);
		    startActivity(intent);
			break;
		}
	}

	public ArrayList<CardData> getList() {
		return list;
	}

	public void setList(ArrayList<CardData> list) {
		this.list = list;
	}



//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, data);
//		if(resultCode == RESULT_OK && requestCode == 1){
//			Log.i("TAG", "执");
//			getJsonData();	
//		}
//		
//	}

}
