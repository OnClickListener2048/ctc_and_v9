package com.tohier.cartercoin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.android.activity.base.BaseActivity;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.bean.MyActivity;
import com.tohier.cartercoin.columnview.LoadingView;
import com.tohier.cartercoin.columnview.NoLinkView;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.Tools;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/31.
 */

public class MiningActiveActivity extends BaseActivity {

    private WebView web_update_apk;
    private ImageView iv_back;
    private TextView tv_target_more,fragment_share_title_text;
    private ProgressBar pro;
    private Button btn_into_mining_active_detail,btn_invitation_friends;
    private MyActivity myActivity;
    private LoadingView gif_loading;
    private NoLinkView ivNoLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mining_active_layout);
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.8);   //高度设置为屏幕的1.0
        p.alpha = 1.0f;      //设置本身透明度
        p.dimAmount = 0.5f;      //设置黑暗度
        getWindow().setAttributes(p);

        init();
    }

    private void init() {
        gif_loading = (LoadingView) this.findViewById(R.id.loading_view);

        myActivity = new MyActivity();
     fragment_share_title_text = (TextView) findViewById(R.id.fragment_share_title_text);
        btn_into_mining_active_detail = (Button) findViewById(R.id.btn_into_mining_active_detail);
        btn_invitation_friends = (Button) findViewById(R.id.btn_invitation_friends);
        tv_target_more = (TextView) findViewById(R.id.tv_target_more);
        fragment_share_title_text.setText("任务");
//        tv_target_more.setText("我要分享>>");
//        tv_target_more.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        tv_target_more.setVisibility(View.GONE);
        pro = (ProgressBar) findViewById(R.id.pro);
        iv_back = (ImageView) findViewById(R.id.title_back);


        ivNoLink = (NoLinkView) findViewById(R.id.iv_no_link);

        if (Tools.getAPNType(MiningActiveActivity.this) == true){
            ivNoLink.setVisibility(View.GONE);
            getUrl();
        }else{
            ivNoLink.setVisibility(View.VISIBLE);
            gif_loading.setVisibility(View.GONE);
        }


        ivNoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tools.getAPNType(MiningActiveActivity.this) == true) {
                    ivNoLink.setVisibility(View.GONE);
                    getUrl();

                }else{
                    ivNoLink.setVisibility(View.VISIBLE);
                    gif_loading.setVisibility(View.GONE);
                }

            }
        });


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_target_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MiningActiveActivity.this,NewShareActivity.class));
                finish();
            }
        });

        btn_invitation_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MiningActiveActivity.this,NewShareActivity.class));
                finish();
            }
        });

        btn_into_mining_active_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_into_mining_active_detail.setClickable(false);
                Map<String, String> par = new HashMap<String, String>();
                HttpConnect.post(MiningActiveActivity.this, "member_minning_task_url", par, new Callback() {

                    @Override
                    public void onResponse(Response arg0) throws IOException {
                        final JSONObject active = JSONObject.fromObject(arg0.body().string());

                        if (active.get("status").equals("success")) {

                            JSONObject obj = active.getJSONArray("data").getJSONObject(0);
                            final String desc = obj.optString("description");
                            final String url  = obj.optString("url");
                            final String title = obj.optString("title");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btn_into_mining_active_detail.setClickable(true);
                                    Intent intent = new Intent( MiningActiveActivity.this,GongGaoDetailActivity.class);
                                    intent.putExtra("url", url);
                                    intent.putExtra("title",title);
                                    intent.putExtra("desc", desc);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Request arg0, IOException arg1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btn_into_mining_active_detail.setClickable(true);
                            }
                        });
                    }
                });
            }
        });


    }

    private void getUrl()
    {
        HttpConnect.post(this, "member_mining_task", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    final String url = data.getJSONArray("data").getJSONObject(0).getString("Value");
                    Handler dataHandler = new Handler(getContext().getMainLooper()) {
                        @Override
                        public void handleMessage(final Message msg) {
                            web_update_apk = (WebView)findViewById(R.id.web);
                            web_update_apk.getSettings().setJavaScriptEnabled(true);

                            web_update_apk.setWebViewClient(new WebViewClient(){
                                @Override
                                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                    // TODO Auto-generated method stub
                                    view.loadUrl(url);
                                    return false;
                                }
                            });
                            web_update_apk.setWebChromeClient(new MiningActiveActivity.WebChromeClient());
                            web_update_apk.loadUrl(url+"?"+LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getToken());
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                } else {
                    Handler dataHandler = new Handler(getContext().getMainLooper()) {
                        @Override
                        public void handleMessage(final Message msg) {
//						    AssetsExplainActivity.this.sToast("暂无数据");
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                    //sToast(data.getString("msg"));
                }
                //mZProgressHUD.cancel();
            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
                //mZProgressHUD.cancel();
                Handler dataHandler = new Handler(getContext().getMainLooper()) {
                    @Override
                    public void handleMessage(final Message msg) {
//						AssetsExplainActivity.this.sToast("请检查您的网络链接");
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        });
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                gif_loading.setVisibility(View.GONE);
//                pro.setVisibility(View.GONE);
            } else {
                if (gif_loading.getVisibility() == View.GONE)
                    gif_loading.setVisibility(View.VISIBLE);
//                pro.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

    }








    @Override
    public void initData() {
        // TODO Auto-generated method stub

    }
}
