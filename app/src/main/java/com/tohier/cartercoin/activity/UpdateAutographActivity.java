package com.tohier.cartercoin.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.Tools;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/12/30.
 */

public class UpdateAutographActivity extends MyBackBaseActivity {

    private ImageView iv_back2;
    private Button btn_commit_update_nickname;
    private EditText et_nickname;
    private int i = 0;
    private TextView tv_title2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update_autograph_layout);

        initData();

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

    @Override
    public void initData() {
            loadMemberInfo();
              iv_back2 = (ImageView) this.findViewById(R.id.iv_back2);
              btn_commit_update_nickname = (Button) this.findViewById(R.id.btn_commit_update_nickname);
              et_nickname = (EditText) this.findViewById(R.id.et_nickname);
              iv_back2.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                    finish();
                  }
             });
        btn_commit_update_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_commit_update_nickname.setClickable(false);
                if(TextUtils.isEmpty(et_nickname.getText().toString().trim()))
                {
                          sToast("请填写签名");
                          btn_commit_update_nickname.setClickable(true);
                          return;
                }else
                {
                          if(i==1)
                          {
//                              String digits = "\"";
                              String digits = "[/\\\\\\\"\\n\\t]";
                              Pattern pattern = Pattern.compile(digits);
                              Matcher matcher = pattern.matcher(et_nickname.getText().toString().trim());
                              if(matcher.find())
                              {
                                  btn_commit_update_nickname.setClickable(true);
                                  sToast("包含非法字符，请重新输入");
                              }else
                              {
                                  updateMemberInfo();
                              }
                          }else
                          {
                              btn_commit_update_nickname.setClickable(true);
//                              sToast("请检查您的网络状态");
                          }
                }
            }
        });


    }

    private String oneautograph;


    /**
     * 获取会员的信息
     */
    public void loadMemberInfo()
    {
        HttpConnect.post(UpdateAutographActivity.this, "member_info", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    oneautograph = data.getJSONArray("data").getJSONObject(0).optString("oneautograph");

                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {
                            i = 1;
                            if(oneautograph!=null&&!TextUtils.isEmpty(oneautograph))
                            {
                                et_nickname.setHint(oneautograph);

                            }
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }else
                {
                    final String msg8 = data.getString("msg");
                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {
                            boolean flag = Tools.isPhonticName(msg8);
                            if(!flag)
                            {
                                sToast(msg8);
                            }
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


    public void updateMemberInfo()
    {
        myProgressDialog.setTitle("正在加载...");
        myProgressDialog.show();
        Map<String, String> par1 = new HashMap<String, String>();
        par1.put("oneautograph",et_nickname.getText().toString().trim() );

        HttpConnect.post(this, "member_oneautograph_modify", par1, new Callback() {

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                Handler dataHandler = new Handler(getContext()
                        .getMainLooper()) {

                    @Override
                    public void handleMessage(final Message msg) {

                    }
                };
                dataHandler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject object = JSONObject.fromObject(arg0.body().string());
                if (object.get("status").equals("success")) {
                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {
                            btn_commit_update_nickname.setClickable(true);
                            myProgressDialog.cancel();
                            sToast("修改成功");
                            finish();
                        }
                    };
                    dataHandler.sendEmptyMessage(0);


                }else{
                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {
                            btn_commit_update_nickname.setClickable(true);
                            myProgressDialog.cancel();
                            sToast("修改失败");
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }
            }
        });
    }
}
