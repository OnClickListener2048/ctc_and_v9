package com.tohier.cartercoin.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.bean.ZanRanking;
import com.tohier.cartercoin.config.HttpConnect;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class ConversationActivity extends MyBaseFragmentActivity{

    private FrameLayout frameLayout;
    public static ConversationActivity activity;
    private ZanRanking zanRanking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        init();


    }

    private void init() {
        activity = this;

        frameLayout = (FrameLayout) findViewById(R.id.realtabcontent);

        Fragment fragment1 = mIMKit.getConversationFragment();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction =manager.beginTransaction();
        // 把碎片添加到碎片中
        transaction.add(R.id.realtabcontent,fragment1).show(fragment1);
        transaction.commit();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_OK) {
            if (requestCode == 0) {
                String scanResult = data.getStringExtra(CaptureActivity.EXTRA_RESULT);
                if (scanResult != null && scanResult.length() > 0) {
                    thisPersonExist(scanResult);
                }
            }
        }
    }

    /**
     * 验证付款人是否存在
     */
    public void thisPersonExist(final String linkcode)
    {
        String lk = linkcode.substring(linkcode.indexOf(":")+1,linkcode.length());
        Map<String, String> par = new HashMap<String, String>();
        par.put("number", lk);
        HttpConnect.post(this,"member_friends_search" , par,
                new Callback() {
                    @Override
                    public void onResponse(Response arg0)
                            throws IOException {
                        final JSONObject object = JSONObject.fromObject(arg0
                                .body().string());
                        if (object.get("status").equals("success")) {

                            JSONArray array = object
                                    .optJSONArray("data");
                            if (array.size() != 0) {
                                    JSONObject object2 = array
                                            .getJSONObject(0);
                                    zanRanking = new ZanRanking();
                                    String id = object2.optString("id");
                                    String name = object2.optString("name");
                                    String pic =  object2.optString("pic");
                                    String lk = object2.optString("lk");
                                    String type = object2.optString("type");
                                    String grade =  object2.optString("grade");
                                    String inti =   object2.optString("inti");
                                    String ip =  object2.optString("ip");
                                    String mobile =  object2.optString("mobile");
                                    String remark = object2.optString("remark");
                                    String oneau = object2.optString("oneau");
                                    String sex = object2.optString("sex");
                                    String sta = object2.optString("sta");
                                    String bgpic = object2.optString("backgroundpic");
                                    String paymentcode = object2.optString("paymentcode");

                                    zanRanking.setPic(pic);
                                    zanRanking.setArea(ip);
                                    zanRanking.setCount(inti);
                                    zanRanking.setType(type);
                                    zanRanking.setLinkCode(lk);
                                    zanRanking.setId(id);
                                    zanRanking.setName(name);
                                    zanRanking.setLevel1(grade);
                                    zanRanking.setMobile(mobile);
                                    zanRanking.setOneau(oneau);
                                    zanRanking.setRemark(remark);
                                    zanRanking.setSex(sex);
                                    zanRanking.setSta(sta);
                                    zanRanking.setBackgroundpic(bgpic);
                                    zanRanking.setPaymentCode(paymentcode);
                                Handler dataHandler = new Handler(
                                        getContext().getMainLooper()) {

                                    @Override
                                    public void handleMessage(
                                            final Message msg) {
                                        int isFriend = 0;
                                        if(zanRanking.getSta().equals("1")){
                                            isFriend = 0;
                                        }else if (zanRanking.getSta().equals("0") || zanRanking.getSta().equals("2")){
                                            isFriend = 1;
                                        }
                                        Intent intent = new Intent(ConversationActivity.this, FriendsInfoActivity.class);
                                        Bundle mBundle = new Bundle();
                                        mBundle.putSerializable("zanRanking",zanRanking);
                                        intent.putExtras(mBundle);
                                        intent.putExtra("isFrend",isFriend);
                                        startActivity(intent);
                                    }
                                };
                                dataHandler.sendEmptyMessage(0);

                            }
                        }else{
                            Handler dataHandler = new Handler(
                                    getContext().getMainLooper()) {

                                @Override
                                public void handleMessage(
                                        final Message msg) {
                                    sToast("该用户不存在！");
                                }
                            };
                            dataHandler.sendEmptyMessage(0);
                        }

                    }


                    @Override
                    public void onFailure(Request arg0, IOException arg1) {


                        Handler dataHandler = new Handler(
                                getContext().getMainLooper()) {

                            @Override
                            public void handleMessage(
                                    final Message msg) {
//                                sToast("链接超时！");
                            }
                        };
                        dataHandler.sendEmptyMessage(0);
                    }
                });
    }


    public void show1(){
        WindowManager.LayoutParams lp5 = ConversationActivity.this.getWindow().getAttributes();
        lp5.alpha = 0.5f;
        getWindow().setAttributes(lp5);
    }

    public void dismiss1(){
        WindowManager.LayoutParams lp3 = getWindow().getAttributes();
        lp3.alpha = 1f;
        getWindow().setAttributes(lp3);
    }

    /**
     * 双击退出程序
     */
    private long mExitTime;
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    sToast("再按一次退出程序");
                    mExitTime = System.currentTimeMillis();

                } else {
                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startMain);
                }
            }
            return true;
    }

}
