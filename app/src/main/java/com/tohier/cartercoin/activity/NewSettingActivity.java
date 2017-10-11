package com.tohier.cartercoin.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.conversation.EServiceContact;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.config.DataCleanManager;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.MyApplication;
import com.tohier.cartercoin.config.Tools;
import com.tohier.cartercoin.presenter.GetVersionCodePresenter;
import com.tohier.cartercoin.ui.UpdateDialogView;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Administrator on 2016/12/14.
 */

public class NewSettingActivity extends MyBaseActivity implements View.OnClickListener,UpdateDialogView{

    private LinearLayout linearlayout_clearcache,linearlayout_into_edit_opinion,linearlayout_into_helpcenter,
                                 linearlayout_into_contact_service,linearlayout_into_aboutapp,linearlayout_into_check_update,linearlayout_into_complaint;
    private Button btn_logout;
    private ImageView iv_back2;
    private TextView tv_cache_count,tv_current_banben;

    private Button btn_commit_clearcache;

    private Button btn_cancel_clearcache;
    private View clearCaChePopupWindowView;
    private PopupWindow clearCaChePopupWindow;
    private GetVersionCodePresenter getVersionCodePresenter;
    private GifImageView gif_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_setting);

        MyApplication.maps.put("SettingActivity",this);


        initData();
        setUpView();

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
        linearlayout_clearcache = (LinearLayout) this.findViewById(R.id.linearlayout_clearcache);
        linearlayout_into_check_update = (LinearLayout) this.findViewById(R.id.linearlayout_into_check_update);
        linearlayout_into_edit_opinion = (LinearLayout) this.findViewById(R.id.linearlayout_into_edit_opinion);
        linearlayout_into_helpcenter = (LinearLayout) this.findViewById(R.id.linearlayout_into_helpcenter);
        linearlayout_into_contact_service = (LinearLayout) this.findViewById(R.id.linearlayout_into_contact_service);
        linearlayout_into_aboutapp = (LinearLayout) this.findViewById(R.id.linearlayout_into_aboutapp);
        linearlayout_into_complaint = (LinearLayout) this.findViewById(R.id.linearlayout_into_complaint);
        btn_logout = (Button) this.findViewById(R.id.btn_logout);
        iv_back2 = (ImageView) this.findViewById(R.id.iv_back2);
        tv_cache_count = (TextView) this.findViewById(R.id.tv_cache_count);
        tv_current_banben = (TextView) this.findViewById(R.id.tv_current_banben);
        gif_loading = (GifImageView) this.findViewById(R.id.gif_loading);

        clearCaChePopupWindowView = View.inflate(this, R.layout.clearcache_popupwindow_item, null);
        btn_commit_clearcache = (Button) clearCaChePopupWindowView
                .findViewById(R.id.btn_commit_clearcache);
        btn_cancel_clearcache = (Button) clearCaChePopupWindowView
                .findViewById(R.id.btn_cancel_clearcache);

        String currentbanben = getAppVersionName(this);
        tv_current_banben.setText(currentbanben);
        /**
         * 初始化 app是否提示更新的 Presenter
         */
        getVersionCodePresenter = new GetVersionCodePresenter(this,this);
    }

    private void setUpView() {
        linearlayout_clearcache.setOnClickListener(this);
        linearlayout_into_check_update.setOnClickListener(this);
        linearlayout_into_edit_opinion.setOnClickListener(this);
        linearlayout_into_helpcenter.setOnClickListener(this);
        linearlayout_into_contact_service.setOnClickListener(this);
        linearlayout_into_complaint.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
        iv_back2.setOnClickListener(this);
        btn_commit_clearcache.setOnClickListener(this);
        btn_cancel_clearcache.setOnClickListener(this);


        /**
         * 设置文本内容 缓存量
         */
        try {
           String cacheCount =  DataCleanManager.getTotalCacheSize(this);
            tv_cache_count.setText(cacheCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId())
        {
            case R.id.linearlayout_clearcache:
                clearCaChePopupWindow = new PopupWindow(clearCaChePopupWindowView, WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT);
                clearCaChePopupWindow.setOutsideTouchable(true);

                // 设置背景颜色变暗
                WindowManager.LayoutParams lp5 = getWindow().getAttributes();
                lp5.alpha = 0.5f;
                getWindow().setAttributes(lp5);
                clearCaChePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                    @Override
                    public void onDismiss() {
                        WindowManager.LayoutParams lp3 = getWindow().getAttributes();
                        lp3.alpha = 1f;
                        getWindow().setAttributes(lp3);
                    }
                });

                // 实例化一个ColorDrawable颜色为半透明
                ColorDrawable dw = new ColorDrawable(0x00ffffff);
                clearCaChePopupWindow.setBackgroundDrawable(dw);

                // 设置popWindow的显示和消失动画
                clearCaChePopupWindow.setAnimationStyle(R.style.Mypopwindow_anim_style);
                // 在底部显示
                clearCaChePopupWindow.showAtLocation(findViewById(R.id.iv_back2),
                        Gravity.BOTTOM, 0, 0);
                break;
            case R.id.linearlayout_into_edit_opinion:

                break;
            case R.id.linearlayout_into_helpcenter:
                startActivity(new Intent(NewSettingActivity.this,HelperCenterActivity.class));
                break;
            case R.id.linearlayout_into_contact_service:
                //intent = new Intent(getActivity(),AddWXKeFu.class);
                //userid是客服帐号，第一个参数是客服帐号，第二个是组ID，如果没有，传0
                EServiceContact contact = new EServiceContact(MyApplication.KEFU, 0);
                //如果需要发给指定的客服帐号，不需要Server进行分流(默认Server会分流)，请调用EServiceContact对象
                //的setNeedByPass方法，参数为false。
                //contact.setNeedByPass(false);
                Intent intent1 = mIMKit.getChattingActivityIntent(contact);
                startActivity(intent1);
                break;
            case R.id.btn_logout:

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myProgressDialog.setTitle("正在退出...");
                        myProgressDialog.show();
                    }
                });

                if(LoginUser.getInstantiation(this.getApplicationContext()).isLogin()){

                    Map<String, String> par = new HashMap<String, String>();
                    HttpConnect.post(this, "member_mine_end", par, new Callback() {

                        @Override
                        public void onFailure(Request arg0, IOException arg1) {
                            NewSettingActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    myProgressDialog.cancel();
                                   sToast("网络质量不佳，请检查网络！");
                                }
                            });
                        }

                        @Override
                        public void onResponse(Response arg0) throws IOException {
                            final JSONObject data = JSONObject.fromObject(arg0.body().string());
                            if (data.get("status").equals("success")) {

                                NewSettingActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        IYWLoginService loginService = mIMKit.getIMCore().getLoginService();
                                        loginService.logout(new IWxCallback() {

                                            @Override
                                            public void onSuccess(Object... arg0) {
                                                //登出成功
                                                Intent intent5 = new Intent(NewSettingActivity.this, LoginMainActivity.class);
                                                startActivity(intent5);
                                                finish();
                                                LoginUser.getInstantiation(getApplicationContext()).loginOut();
                                                NewSettingActivity.this.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        myProgressDialog.cancel();
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onProgress(int arg0) {
                                                // TODO Auto-generated method stub
                                            }

                                            @Override
                                            public void onError(int errCode, String description) {
                                                NewSettingActivity.this.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                  myProgressDialog.cancel();
                                                    }
                                                });
                                                //登出失败，errCode为错误码,description是错误的具体描述信息
//                                                Toast.makeText(NewSettingActivity.this,description,Toast.LENGTH_LONG).show();
                                            }
                                        });

                                     }
                                });

                            } else {
                                NewSettingActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(data.optString("msg")!=null&&!data.optString("msg").equals(""))
                                        {
                                            if(!Tools.isPhonticName(data.optString("msg")))
                                            {
                                                sToast(data.optString("msg"));
                                            }
                                        }
                                        myProgressDialog.cancel();
                                    }
                                });
                            }
                        }

                    });


                    //startActivity(new Intent(this,LoginMainActivity.class));
                }

//
                break;
            case R.id.iv_back2:
                finish();
                break;
            case R.id.btn_commit_clearcache:
               boolean delete = DataCleanManager.clearAllCache(this);
                if(delete)
                {
                    try {
                        String cacheCount =  DataCleanManager.getTotalCacheSize(this);
                        tv_cache_count.setText(cacheCount);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else
                {

                }
                clearCaChePopupWindow.dismiss();
                break;
            case R.id.btn_cancel_clearcache:
                 clearCaChePopupWindow.dismiss();
                break; 
            
            case R.id.linearlayout_into_check_update:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myProgressDialog.setTitle("正在加载...");
                        myProgressDialog.show();
                    }
                });
                getVersionCodePresenter.getVersionCode();
                break;

            case R.id.linearlayout_into_complaint:
                 startActivity(new Intent(NewSettingActivity.this,ComplaintActivity.class));
                break;
        }
    }

    @Override
    public void showUpdateDialog(String updateMsg, final String url5) {
        myProgressDialog.cancel();
        AlertDialog.Builder dialog = new AlertDialog.Builder(NewSettingActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        dialog.setCancelable(false);
        dialog.setTitle("更新信息");
        dialog.setMessage(updateMsg);

        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String url = url5;
                Intent intent = new Intent();
                intent.setData(Uri.parse(url));//Url 就是你要打开的网址
                intent.setAction(Intent.ACTION_VIEW);
                NewSettingActivity.this.startActivity(intent); //启动浏览器
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.create();
        dialog.show();

    }

    @Override
    public void loadFail(String mark) {
        myProgressDialog.cancel();
        boolean flag = Tools.isPhonticName(mark);
        if(!flag)
        {
            sToast(mark);
        }
    }

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }
}
