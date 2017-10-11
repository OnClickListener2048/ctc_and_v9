package com.tohier.cartercoin.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.adapter.ShouHuoAddessAdapter;
import com.tohier.cartercoin.bean.Address;
import com.tohier.cartercoin.columnview.LoadingView;
import com.tohier.cartercoin.columnview.NoDataView;
import com.tohier.cartercoin.columnview.NoLinkView;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.MyApplication;
import com.tohier.cartercoin.config.Tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ShouHuoAddressActivity extends MyBaseActivity implements OnClickListener {

	final ArrayList<Address> list = new ArrayList<Address>();
	private ListView listView;
	private ImageView iv_back;
	private Button btn_add;
	ShouHuoAddessAdapter adapter;
	String account;
	private TextView tv_title;

	private LoadingView cif_loading;
	private NoDataView ivNodata;
	private NoLinkView ivNoLink;


	private SharedPreferences sp;
	/**
	 * shopType == 1 可以将地址回调
	 */
	private String shopType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_banklist);

		MyApplication.maps.put("ShouHuoAddressActivity",this);
		initView();
		if (Tools.getAPNType(ShouHuoAddressActivity.this) == true){
			ivNoLink.setVisibility(View.GONE);
			getJsonData();
		}else{
			ivNoLink.setVisibility(View.VISIBLE);
		}


		setUp();

	}

	private void setUp() {
		if(!TextUtils.isEmpty(shopType))
		{
			if (shopType.equals("1")){
				listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//						Intent intent = new Intent(ShouHuoAddressActivity.this, BuildOrderActivity.class);
//						intent.putExtra("name1",list.get(position).getConsignee())
//								.putExtra("phone",list.get(position).getMobile())
//								.putExtra("address",list.get(position).getAddress());
//						startActivity(intent);

						sp.edit().putString("name1",list.get(position).getConsignee())
								.putString("phone",list.get(position).getMobile())
								.putString("address",list.get(position).getAddress())
								.putString("id",list.get(position).getId())
								.commit();

						finish();
					}
				});
			}

		}

		ivNoLink.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Tools.getAPNType(ShouHuoAddressActivity.this) == true){
					ivNoLink.setVisibility(View.GONE);
					getJsonData();
				}else{
					ivNoLink.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	private void initView() {

		sp = getSharedPreferences("address",0);
		shopType = getIntent().getStringExtra("shopType");
		// TODO Auto-generated method stub
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("我的收货专用地址");
		listView = (ListView) findViewById(R.id.lv);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		btn_add = (Button) findViewById(R.id.btn_add);
		ivNodata = (NoDataView) findViewById(R.id.iv_nodata);
		cif_loading  = (LoadingView) findViewById(R.id.cif_loading);
		ivNoLink = (NoLinkView) findViewById(R.id.iv_no_link);

		btn_add.setText("添加收货地址");
		iv_back.setOnClickListener(this);
		btn_add.setOnClickListener(this);

		adapter = new ShouHuoAddessAdapter(list, ShouHuoAddressActivity.this ,ShouHuoAddressActivity.this,sp);
		listView.setAdapter(adapter);
		listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
	}

	public void getJsonData() {
		cif_loading.setVisibility(View.VISIBLE);
//		Map<String, String> par = new HashMap<String, String>();
//
//		String id = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getUserId();
//		par.put("id", id);

//		mZProgressHUD.show();

		list.clear();
		HttpConnect.post(this, "member_address_list", null, new Callback() {

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
								final String card_id = jsonObject2.getString("id");
								final String consignee = jsonObject2.getString("consignee");
								final String address = jsonObject2.getString("address");
								final String weChat = jsonObject2.getString("wechat");
								final String mobile = jsonObject2.getString("mobile");
								final String province = jsonObject2.getString("province");
								final String city = jsonObject2.getString("city");
								final String default_address = jsonObject2.getString("default");
								Address addressObj = new Address(consignee,address,province,city,weChat,mobile,card_id,default_address);
								list.add(addressObj);
							}
						}
						ShouHuoAddressActivity.this.runOnUiThread(new Runnable() {

							@Override
							public void run() {
//								mZProgressHUD.cancel();
								adapter.setList(list);
								adapter.notifyDataSetChanged();
								cif_loading.setVisibility(View.GONE);
								ivNodata.setVisibility(View.GONE);
							}
						});
					}

				} else {
					ShouHuoAddressActivity.this.runOnUiThread(new Runnable() {

						@Override
						public void run() {
//							mZProgressHUD.cancel();
							ShouHuoAddressActivity.this.runOnUiThread(new Runnable() {

								@Override
								public void run() {
//									sToast("暂无地址信息");
									cif_loading.setVisibility(View.GONE);
									if (list.size()>0){
										ivNodata.setVisibility(View.GONE);
									}else{
										ivNodata.setVisibility(View.VISIBLE);
									}
								}
							});
						}
					});

				}
			}

			@Override
			public void onFailure(Request arg0, IOException arg1) {
				// TODO Auto-generated method stub

				ShouHuoAddressActivity.this.runOnUiThread(new Runnable() {

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
//			sp.edit().putString("name1","")
//					.putString("phone","")
//					.putString("address","").commit();
			finish();
			BuildOrderActivity.isFirst = true;
			break;
		case R.id.btn_add:
			Intent intent = new Intent(ShouHuoAddressActivity.this,AddShouHuoAddressActivity.class);
			intent.putExtra("update","no");
			intent.putExtra("shopType",shopType);
		    startActivity(intent);
			break;
		}
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

	@Override
	public void onBackPressed() {
		super.onBackPressed();

//		sp.edit().putString("name1","")
//				.putString("phone","")
//				.putString("address","").commit();
		finish();
		BuildOrderActivity.isFirst = true;
	}

}
