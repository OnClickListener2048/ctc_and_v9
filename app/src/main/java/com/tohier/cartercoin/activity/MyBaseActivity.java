package com.tohier.cartercoin.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.login.IYWConnectionListener;
import com.alibaba.mobileim.login.YWLoginCode;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.android.activity.base.BaseActivity;
import com.tohier.cartercoin.columnview.MyProgressDialog;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.MyApplication;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/13.
 */

public class MyBaseActivity extends BaseActivity {

    public YWIMKit mIMKit;
    public String userid;
    private IYWConnectionListener mConnectionListener;
    private AlertDialog.Builder normalDialog;
    public MyProgressDialog myProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myProgressDialog = MyProgressDialog.getInstance(this);
//        myProgressDialog.setCancelable(false);

        try{
            userid = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getLinkCode();
            mIMKit = YWAPI.getIMKitInstance(userid, MyApplication.APP_KEY);
            addConnectionListener();
        }catch(Exception e){

        }
    }

    @Override
    public void initData() {

    }


    /**
     * 监听在线状态
     */
    private void addConnectionListener() {
        mConnectionListener = new IYWConnectionListener() {
            @Override
            public void onDisconnect(int code, String info) {
                if (code == YWLoginCode.LOGON_FAIL_KICKOFF) {
                    //在其它终端登录，当前用户被踢下线
                    if(MyBaseActivity.this.isDestroyed() == false){
                        normalDialog =
                                new AlertDialog.Builder(MyBaseActivity.this,AlertDialog.THEME_HOLO_LIGHT);
                        normalDialog.setTitle("警告");
                        normalDialog.setMessage("该账号在别处登录,您被迫下线");
                        normalDialog.setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        onLogout();
                                    }
                                });
                        normalDialog.setCancelable(false);
                        try{
                            normalDialog.create().show();
                        }catch(Exception e){

                        }
                    }


                }

            }

            @Override
            public void onReConnecting() {

            }

            @Override
            public void onReConnected() {

            }
        };
        mIMKit.getIMCore().addConnectionListener(mConnectionListener);
    }

    private void onLogout(){
        Map<String, String> par = new HashMap<String, String>();
        par.put("id", LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getUserId());
        HttpConnect.post(this, "member_mine_end", par, new Callback() {
            @Override
            public void onFailure(Request arg0, IOException arg1) {
            }

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body()
                        .string());
                if (data.get("status").equals("success")) {
                    // openIM SDK提供的登录服务
                    IYWLoginService mLoginService = mIMKit.getLoginService();
                    mLoginService.logout(new IWxCallback() {
                        //此时logout已关闭所有基于IMBaseActivity的OpenIM相关Actiivity，s
                        @Override
                        public void onSuccess(Object... arg0) {
                            MyBaseActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//									InterfaceMainActivity.this.sToast("退出成功");
                                    LoginUser.getInstantiation(MyBaseActivity.this.getApplicationContext()).loginOut();
                                    LoginUser.getInstantiation(MyBaseActivity.this.getApplicationContext()).startLoginActivity(MyBaseActivity.this);
                                    finish();
                                }
                            });

                        }

                        @Override
                        public void onProgress(int arg0) {

                        }

                        @Override
                        public void onError(int arg0, String arg1) {
                        }
                    });


                } else {

                }
            }

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (normalDialog != null){
            normalDialog.show().dismiss();
        }
    }


}
