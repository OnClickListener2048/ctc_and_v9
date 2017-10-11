package com.tohier.cartercoin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.MyApplication;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AddShouHuoAddressActivity extends MyBackBaseActivity implements OnClickListener {

	private ImageView iv_back2;
	private Spinner spinner_sheng;
	private Spinner spinner_shi;
	private EditText spinner;
	private EditText et_kaihuyinhang;
	private EditText et_kahao;
	private Button btnadd;
	private EditText tvName;
	private String yhNmae;
	private List<String> yhlist = new ArrayList<String>();
	private List<Map<String,String>> sslist = new ArrayList<Map<String,String>>();
	private List<Map<String,String>> slist = new ArrayList<Map<String,String>>();
    private SimpleAdapter shengAdapter ;
	private TextView tv_title2;
	private String update;
    private String interfaceName = "";

	private String shopType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_shouhuoaddress);

		tv_title2 = (TextView) findViewById(R.id.tv_title2);
		btnadd = (Button) findViewById(R.id.btnaddok);
		update = getIntent().getStringExtra("update");

		initView();
        setUpView();


		if(!TextUtils.isEmpty(update)&&update.equals("yes"))
		{
			tv_title2.setText("修改购物地址");
			btnadd.setText("确认修改");

			if(!TextUtils.isEmpty(getIntent().getStringExtra("name")))
			{
				tvName.setText(getIntent().getStringExtra("name"));
			}

			if(!TextUtils.isEmpty(getIntent().getStringExtra("address")))
			{
				spinner.setText(getIntent().getStringExtra("address"));
			}

			if(!TextUtils.isEmpty(getIntent().getStringExtra("mobile")))
			{
				et_kahao.setText(getIntent().getStringExtra("mobile"));
			}


		}
	}

	private void initView() {
		// TODO Auto-generated method stub

		iv_back2 = (ImageView) findViewById(R.id.iv_back2);
		et_kaihuyinhang = (EditText) findViewById(R.id.et_kaihuyinhang);
		et_kahao = (EditText) findViewById(R.id.et_kahao);
		tvName = (EditText) findViewById(R.id.tv_username);
		spinner = (EditText) findViewById(R.id.spinner);
		spinner_sheng = (Spinner) findViewById(R.id.spinner_sheng);
		spinner_shi = (Spinner) findViewById(R.id.spinner_shi);
		iv_back2.setOnClickListener(this);
		btnadd.setOnClickListener(this);

		Map<String,String> map = new HashMap<String,String>();
		map.put("shengname", "请选择省份");
		map.put("shengid", "" + -1 );
		sslist.add(map);
		shengshileibiao(true,"0");

		Map<String,String> mm = new HashMap<String,String>();
		mm.put("shengname", "请选择城市");
		mm.put("shengid", "" + -2 );
		slist.add(mm);
		SimpleAdapter shengAdapter = new SimpleAdapter(AddShouHuoAddressActivity.this, slist, R.layout.spinner_sheng_layout, new String[]{"shengname"}, new int[]{R.id.tv_sheng});
	    spinner_shi.setAdapter(shengAdapter);

//		 tvName.setText(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getNickName());
	}


	private void setUpView() {
		spinner_sheng.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String shengId = sslist.get(position).get("shengid");
				slist.clear();
				Map<String,String> map = new HashMap<String,String>();
				map.put("shengname", "请选择城市");
				map.put("shengid", "" + -1 );
				slist.add(map);
				shengshileibiao(false, shengId);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

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
		case R.id.iv_back2:
			finish();
			break;
		case R.id.btnaddok:
			Set<String> keys = MyApplication.maps.keySet();
			if(keys!=null&&keys.size()>0)
			{
				if(keys.contains("ShouHuoAddressActivity"))
				{
					Activity activity = MyApplication.maps.get("ShouHuoAddressActivity");
					activity.finish();
				} ;
			}


			if(TextUtils.isEmpty(tvName.getText().toString()))
			{
				sToast("收货人名不能为空");
			}else if(spinner_sheng.getSelectedItemPosition()==0)
			{
				sToast("请选择省份");
			}else if(spinner_shi.getSelectedItemPosition()==0)
			{
				sToast("请选择城市");
			}else if(TextUtils.isEmpty(spinner.getText().toString()))
			{
				sToast("请输入详细地址");
			}
			else if(TextUtils.isEmpty(et_kahao.getText().toString().trim()))
			{
				sToast("请输入手机号");
			}else if(et_kahao.getText().toString().trim().toString().length()!=11)
			{
				sToast("手机号输入错误");
			}else
			{
				Map<String, String> par = new HashMap<String, String>();

				if(!TextUtils.isEmpty(update)&&update.equals("yes"))
				{
					par.put("id", getIntent().getStringExtra("id"));
					interfaceName = "member_address_modify";
				}else
				{
					par.put("id", LoginUser.getInstantiation(getApplicationContext())
							.getLoginUser().getUserId());
					interfaceName = "member_address_add";
				}

				par.put("consignee", tvName.getText().toString().trim());
				par.put("address", spinner.getText().toString().trim());
				par.put("province",sslist.get(spinner_sheng.getSelectedItemPosition()).get("shengname"));
				par.put("city", slist.get(spinner_shi.getSelectedItemPosition()).get("shengname"));
				par.put("wechat", "");
				par.put("mobile", et_kahao.getText().toString().trim());

				HttpConnect.post(this, interfaceName, par, new Callback() {

					@Override
					public void onResponse(Response arg0) throws IOException {

						JSONObject json = JSONObject.fromObject(arg0.body().string());

						if(json.getString("status").equals("success"))
						{
							try{
								shopType = getIntent().getStringExtra("shopType");
							}catch(Exception e){
								shopType = "3";
							}
							Intent intent = new Intent(AddShouHuoAddressActivity.this,ShouHuoAddressActivity.class);
							intent.putExtra("shopType",shopType);
							startActivity(intent);
							finish();
						}else
						{
							sToast("失败...");
						}

					}
					@Override
					public void onFailure(Request arg0, IOException arg1) {
						// TODO Auto-generated method stub
						sToast("失败");
					}
				});
			}
			break;
		}
	}


	private void shengshileibiao(final boolean flag,String id) {
		// TODO Auto-generated method stub
		Map<String, String> par = new HashMap<String, String>();

		if(flag==true)
		{
			par.put("parentid", "0");
		}else
		{
			par.put("parentid", id);
		}



		HttpConnect.post(this, "area_list", par, new Callback() {

			@Override
			public void onResponse(Response arg0) throws IOException {
				// TODO Auto-generated method stub
				JSONObject jsonObject = JSONObject.fromObject(arg0.body()
						.string());
				if (jsonObject.get("status").equals("success")) {

					JSONArray jsonArr_data = jsonObject.optJSONArray("data");
					if (jsonArr_data != null && jsonArr_data.size() != 0) {
						for (int i = 0; i < jsonArr_data.size(); i++) {
							JSONObject jsonObject2 = jsonArr_data
									.getJSONObject(i);
							String name = jsonObject2.getString("name");
							String id = jsonObject2.getString("id");

							Map<String,String> map = new HashMap<String,String>();

							Log.d("zjh", "NAME值为:" + name);

							map.put("shengname", name);
							map.put("shengid", id );


							if(flag==true)
							{
								sslist.add(map);
								shengAdapter = new SimpleAdapter(AddShouHuoAddressActivity.this, sslist, R.layout.spinner_sheng_layout, new String[]{"shengname"}, new int[]{R.id.tv_sheng});
							}else
							{
								slist.add(map);
								shengAdapter = new SimpleAdapter(AddShouHuoAddressActivity.this, slist, R.layout.spinner_sheng_layout, new String[]{"shengname"}, new int[]{R.id.tv_sheng});
							}
						}
					    if(flag==true)
					    {
					    	AddShouHuoAddressActivity.this.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									spinner_sheng.setAdapter(shengAdapter);
									if(!TextUtils.isEmpty(update)&&update.equals("yes"))
									{
										if(!TextUtils.isEmpty(getIntent().getStringExtra("province")))
										{
											for(int i = 0 ; i < sslist.size() ; i ++)
											{
												String shengname = sslist.get(i).get("shengname");
												if(getIntent().getStringExtra("province").equals(shengname))
												{
													spinner_sheng.setSelection(i);
												}
											}
										}
									}
								}
							});
					    }else
					    {
                             AddShouHuoAddressActivity.this.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									spinner_shi.setAdapter(shengAdapter);
									if(!TextUtils.isEmpty(update)&&update.equals("yes"))
									{
										if(!TextUtils.isEmpty(getIntent().getStringExtra("city")))
										{
											for(int i = 0 ; i < slist.size() ; i ++)
											{
												String shengname = slist.get(i).get("shengname");
												if(getIntent().getStringExtra("city").equals(shengname))
												{
													spinner_shi.setSelection(i);
												}
											}
										}
									}
								}
							});
					    }


					}
//					sToast("成功");
				} else {
//					sToast(jsonObject.getString("msg"));

				}
			}

			@Override
			public void onFailure(Request arg0, IOException arg1) {
				// TODO Auto-generated method stub
				sToast("失败");
			}
		});
	}

}
