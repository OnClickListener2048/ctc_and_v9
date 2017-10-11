package com.tohier.cartercoin.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.config.HttpConnect;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TransferAccountsActivity extends MyBaseActivity {

    private TextView tv_α;
    private EditText etPrice;
    private Spinner spAccount;
    private Button btnCommit;
    private String[] mItems ;

    private PopupWindow window = null;
    private View view;
    private double price;
    private ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();

    /**
     *  0，决战账户，1，为消费账户，2，为复投账户

     */
    private String type = "1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_accounts);

        initData();

        setUp();

//        SliderConfig mConfig = new SliderConfig.Builder()
//                .primaryColor(Color.TRANSPARENT)
//                .secondaryColor(Color.TRANSPARENT)
//                .position(SliderPosition.LEFT)
//                .edge(false)
//                .build();
//
//        ISlider iSlider = SliderUtils.attachActivity(this, mConfig);
//        mConfig.setPosition(SliderPosition.LEFT);
//        iSlider.setConfig(mConfig);
    }

    private void setUp() {
        spAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                type = list.get(pos).get("type");

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });



        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String p = etPrice.getText().toString();
                if (!TextUtils.isEmpty(p)){
                    if (Double.parseDouble(p)<=price){
                        show();
                    }else{
                        sToast("金额不足");
                    }
                }else{
                    sToast("请输入金额");
                }

            }
        });


        /**
         * 只能输入四位小数
         */
        etPrice.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable edt)
            {
                String temp = edt.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 4)
                {
                    edt.delete(posDot + 5, posDot + 6);
                }
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
        });
    }

    @Override
    public void initData() {

        view = View.inflate(this,R.layout.activity_prompt,null);
        ((TextView)view.findViewById(R.id.tv_title)).setText("提示");
        ((TextView)view.findViewById(R.id.tv_prompt)).setText("此转账不可逆");
        window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        tv_α = (TextView) findViewById(R.id.tv_α);
        etPrice = (EditText) findViewById(R.id.et_price);
        spAccount = (Spinner) findViewById(R.id.spinner_account);
        btnCommit = (Button) findViewById(R.id.btn_commit);



        HttpConnect.post(this, "member_exchange_list", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                HashMap<String, String> map;
                if (data.optString("status").equals("success")){
                    JSONArray array = data.optJSONArray("data");
                    mItems = new String[array.size()];
                    for (int i = 0 ; i < array.size() ; i++ ){
                        map = new HashMap<String, String>();
                        JSONObject obj = array.optJSONObject(i);
                        map.put("type",obj.optString("type"));
                        map.put("name",obj.optString("name"));
                        mItems[i] = obj.optString("name");
                        list.add(map);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            type = list.get(0).get("type");
                            // 建立Adapter并且绑定数据源
                            ArrayAdapter<String> adapter=new ArrayAdapter<String>(TransferAccountsActivity.this,android.R.layout.simple_spinner_item, mItems);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            //绑定 Adapter到控件
                            spAccount .setAdapter(adapter);
                        }
                    });
                }

            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {

            }
        });



        getAllMoney();

    }



    private void getAllMoney(){
        HttpConnect.post(this, "member_count_detial", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                final JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            price = Double.parseDouble( data.optJSONArray("data").optJSONObject(0).optString("ctc"));
                            tv_α.setText("收益账户 : "+data.optJSONArray("data").optJSONObject(0).optString("ctc"));

                        }
                    });

                }

            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
            }
        });
    }

    /**
     * 显示取消支付的提示框
     */
    public void show()
    {

        Button btnCencel1 = (Button) view.findViewById(R.id.btn_cancel);
        Button btnCommit1 = (Button) view.findViewById(R.id.btn_commit);

        btnCencel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });

        btnCommit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String,String> map = new HashMap<String, String>();
                map.put("type",type);
                map.put("qty",etPrice.getText().toString());
                HttpConnect.post(TransferAccountsActivity.this, "member_count_exchange", map, new Callback() {
                    @Override
                    public void onResponse(Response arg0) throws IOException {
                        String json = arg0.body().string();
                        final JSONObject data = JSONObject.fromObject(json);
                        if (data.optString("status").equals("success")){

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    sToast("转账成功");
                                    getAllMoney();
                                    window.dismiss();
                                    finish();
                                }
                            });

                        }else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    sToast(data.optString("msg"));
                                    window.dismiss();
                                }
                            });
                        }

                    }
                    @Override
                    public void onFailure(Request arg0, IOException arg1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                window.dismiss();
                            }
                        });
                    }
                });

            }
        });

        if(!window.isShowing())
        {
            // 设置背景颜色变暗
            WindowManager.LayoutParams lp5 = getWindow().getAttributes();
            lp5.alpha = 0.5f;
            getWindow().setAttributes(lp5);
            window.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams lp3 = getWindow().getAttributes();
                    lp3.alpha = 1f;
                    getWindow().setAttributes(lp3);
                }
            });

            window.setOutsideTouchable(true);

            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0x00ffffff);
            window.setBackgroundDrawable(dw);

            // 在底部显示
            window.showAtLocation(findViewById(R.id.t),
                    Gravity.BOTTOM, 0, 0);
        }
    }




    public void back(View view){
        finish();
    }
}
