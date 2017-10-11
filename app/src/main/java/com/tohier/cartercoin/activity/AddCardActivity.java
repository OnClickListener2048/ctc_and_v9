package com.tohier.cartercoin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.MyApplication;
import com.tohier.cartercoin.config.Tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AddCardActivity extends MyBackBaseActivity implements OnClickListener {
	private ImageView iv_back2;
	private Spinner spinner_sheng;
	private Spinner spinner_shi;
	private Spinner spinner;
	private EditText et_kaihuyinhang;
	private EditText et_kahao;
	private Button btnadd;
	private EditText tvName;
	private List<Map<String,String>> yhlist = new ArrayList<Map<String,String>>();
	private List<Map<String,String>> sslist = new ArrayList<Map<String,String>>();
	private List<Map<String,String>> slist = new ArrayList<Map<String,String>>();
    private SimpleAdapter shengAdapter ;

	//武汉 鄂州 恩施 黄石 黄冈 荆州 荆门 潜江 十堰 随州 神农架 天门 襄阳 孝感 咸宁 仙桃 宜昌

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_yinhangka);

		initView();
        setUpView();

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
		iv_back2 = (ImageView) findViewById(R.id.iv_back2);
		et_kaihuyinhang = (EditText) findViewById(R.id.et_kaihuyinhang);
		et_kahao = (EditText) findViewById(R.id.et_kahao);
		btnadd = (Button) findViewById(R.id.btnaddok);
		tvName = (EditText) findViewById(R.id.tv_username);
		spinner = (Spinner) findViewById(R.id.spinner);
		spinner_sheng = (Spinner) findViewById(R.id.spinner_sheng);
		spinner_shi = (Spinner) findViewById(R.id.spinner_shi);
		iv_back2.setOnClickListener(this);
		btnadd.setOnClickListener(this);

		loadMemberInfo();

		//南宁 北海 白色 崇左 防城港 桂林 贵港 贺州 河池 柳州 来宾 钦州 梧州 玉林

		yinhangliebiao();
		Map<String,String> map = new HashMap<String,String>();
		map.put("shengname", "请选择省份");
		map.put("shengid", "" + -1 );
		sslist.add(map);
		shengshileibiao(true,"0");

		Map<String,String> mm = new HashMap<String,String>();
		mm.put("shengname", "请选择城市"); 
		mm.put("shengid", "" + -2 );
		slist.add(mm);
		SimpleAdapter shengAdapter = new SimpleAdapter(AddCardActivity.this, slist, R.layout.spinner_sheng_layout, new String[]{"shengname"}, new int[]{R.id.tv_sheng});
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

	private String name;

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
			String name = AddCardActivity.this.getSharedPreferences("isExitsName", Context.MODE_PRIVATE).getString("name","");
			if(TextUtils.isEmpty(name)||name.equals(""))
			{
			  	sToast("请先实名认证");
			}else
			{
				if(TextUtils.isEmpty(tvName.getText().toString().trim()))
				{
					sToast("姓名不能为空");
				}else if(TextUtils.isEmpty(name))
				{
					return;
				}else if(!name.equals(tvName.getText().toString().trim()))
				{
					sToast("银行卡信息必须与实名认证信息一致");
				}else if(spinner_sheng.getSelectedItemPosition()==0)
				{
					sToast("请选择省份");
				}else if(spinner_shi.getSelectedItemPosition()==0)
				{
					sToast("请选择城市");
				}else if(TextUtils.isEmpty(et_kaihuyinhang.getText().toString().trim()))
				{
					sToast("请输入开户银行");
				}else
				{
					String cardNum = et_kahao.getText().toString().trim();
					Boolean strResult = cardNum.matches("^[0-9]*$");
					if(!strResult)
					{
						sToast("银行卡号输入不合法");
						return;
					}else if(cardNum.length()==19||cardNum.length()==16 || cardNum.length()==18)
					{
						Map<String, String> par = new HashMap<String, String>();
						par.put("id", LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getUserId());

						par.put("banktype", yhlist.get(spinner.getSelectedItemPosition()).get("bankname"));

						par.put("province", sslist.get(spinner_sheng.getSelectedItemPosition()).get("shengname")+" "+
								slist.get(spinner_shi.getSelectedItemPosition()).get("shengname"));

						par.put("bankname", et_kaihuyinhang.getText().toString().trim());

						par.put("account", et_kahao.getText().toString().trim());

						par.put("name", tvName.getText().toString().trim());

						Log.e("addCard","banktype=="+yhlist.get(spinner.getSelectedItemPosition()).get("bankname")+"\n"+"province=="+sslist.get(spinner_sheng.getSelectedItemPosition()).get("shengname")+" "+
								slist.get(spinner_shi.getSelectedItemPosition()).get("shengname")+"\n"+"bankname=="+et_kaihuyinhang.getText().toString().trim()+"\n"+"account=="+et_kahao.getText().toString().trim()+"\n"+
								"name=="+tvName.getText().toString().trim());

						HttpConnect.post(this, "member_bank_add", par, new Callback() {

							@Override
							public void onResponse(Response arg0) throws IOException {

								final JSONObject data = JSONObject.fromObject(arg0.body().string());

								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										if(data.getString("status").equals("success"))
										{
											Set<String> keys3 = MyApplication.maps.keySet();
											if(keys3!=null&&keys3.size()>0)
											{
												if(keys3.contains("BankListActivity"))
												{
													Activity activity = MyApplication.maps.get("BankListActivity");
													activity.finish();
												};
											}

											Intent intent = new Intent(AddCardActivity.this,BankListActivity.class);
											startActivity(intent);
											MyApplication.deleteActivity("BankListActivity");
											finish();
										}else
										{
											runOnUiThread(new Runnable() {
												@Override
												public void run() {
													if(!TextUtils.isEmpty(data.getString("msg")))
													{
														boolean flag = Tools.isPhonticName(data.getString("msg"));
														if(!flag)
														{
															sToast(data.getString("msg"));
														}
													}
												}
											});
										}
									}
								});
							}
							@Override
							public void onFailure(Request arg0, IOException arg1) {
								// TODO Auto-generated method stub
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
//									sToast("请检查您的网络状态");
									}
								});

							}
						});
					}else
					{
						sToast("银行卡号输入不合法");
					}
		        	}
				break;
			}
		}
	}

	public void yinhangliebiao() {

		HttpConnect.post(this, "bank_list", null, new Callback() {

			@Override
			public void onResponse(Response arg0) throws IOException {
				Log.e("jsonStr", arg0.body().toString());
				// TODO Auto-generated method stub
				JSONObject jsonObject = JSONObject.fromObject(arg0.body()
						.string().trim());
				if (jsonObject.getString("status").equals("success")) {
					
					JSONArray jsonArr_data = jsonObject.optJSONArray("data");
					if (jsonArr_data != null && jsonArr_data.size() != 0) {
						for (int i = 0; i < jsonArr_data.size(); i++) {
							JSONObject jsonObject2 = jsonArr_data
									.getJSONObject(i);
							String name = jsonObject2.optString("name");
							Log.d("ZXC", "NAME值为:" + name);
							if (name != null) {
								Map<String,String> map = new HashMap<String, String>();
								map.put("bankname",name);
								yhlist.add(map);
							}

						}
					}
					
//					sToast("成功");
					
				} else {
					if (jsonObject.optString("msg").equalsIgnoreCase("timeout") || jsonObject.optString("msg").equals("")){

					}else{
						sToast(jsonObject.getString("msg"));
					}


				}
				final SimpleAdapter bankListAdapter = new SimpleAdapter(AddCardActivity.this, yhlist, R.layout.spinner_sheng_layout, new String[]{"bankname"}, new int[]{R.id.tv_sheng});
				AddCardActivity.this.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						spinner.setAdapter(bankListAdapter);
					}
				});
			}

			@Override
			public void onFailure(Request arg0, IOException arg1) {
				// TODO Auto-generated method stub
				sToast("失败");
			}
		});

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
								shengAdapter = new SimpleAdapter(AddCardActivity.this, sslist, R.layout.spinner_sheng_layout, new String[]{"shengname"}, new int[]{R.id.tv_sheng});
							}else
							{
								slist.add(map);
								shengAdapter = new SimpleAdapter(AddCardActivity.this, slist, R.layout.spinner_sheng_layout, new String[]{"shengname"}, new int[]{R.id.tv_sheng});
							}
						}
					    if(flag==true)
					    {
					    	AddCardActivity.this.runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									spinner_sheng.setAdapter(shengAdapter);
								}
							});
					    }else
					    {
                             AddCardActivity.this.runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									spinner_shi.setAdapter(shengAdapter);
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


	/**
	 * 获取会员的信息
	 */
	public void loadMemberInfo()
	{
		HttpConnect.post(AddCardActivity.this, "member_info", null, new Callback() {

			@Override
			public void onResponse(Response arg0) throws IOException {
				JSONObject data = JSONObject.fromObject(arg0.body().string());
				if (data.get("status").equals("success")) {
					name = data.getJSONArray("data").getJSONObject(0).getString("name");
				}else
				{
					final String msg8 = data.getString("msg");
					Handler dataHandler = new Handler(getContext()
							.getMainLooper()) {

						@Override
						public void handleMessage(final Message msg) {

						}
					};
					dataHandler.sendEmptyMessage(0);
				}
			}

			@Override
			public  void onFailure(Request arg0, IOException arg1) {
				Handler dataHandler = new Handler(getContext()
						.getMainLooper()) {

					@Override
					public void handleMessage(final Message msg) {

					}
				};
				dataHandler.sendEmptyMessage(0);
			}
		});
	}

}
