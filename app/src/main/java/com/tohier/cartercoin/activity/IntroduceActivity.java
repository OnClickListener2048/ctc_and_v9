package com.tohier.cartercoin.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.columnview.LoadingView;
import com.tohier.cartercoin.columnview.NoLinkView;
import com.tohier.cartercoin.config.DateDistance;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.Tools;

import net.sf.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IntroduceActivity extends MyBaseActivity implements View.OnClickListener{

    private static final String WX_APP_ID = "wx7ad749f6cba84064";
    private static final String QQ_APP_ID = "1105547483";
    private ImageView title_back;
    private WebView web_introduce;
    private LinearLayout ll_daojishi;
    private TextView tv_daojishi;
    private String url,url1;
    public IWXAPI api;
    private TextView tv_share_wx;
    private ImageView tvWX,tvPengyou,tvQQ,tvQZone;
    public Tencent mTencent;
    private View v1;
    private View popupView;
    private PopupWindow window;
    public java.lang.String getUrl() {
        return url;
    }

    private String date;
    private String type;

    private LoadingView loadingView;
    private NoLinkView ivNoLink;

    /**
     * 0---没有排行
     * 1---有
     */
    private String ranking;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x111){
                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//设置日期格式
                String date1 = df.format(new Date());
                String time = DateDistance.getDistanceTime(date1,date,null);


                if (time.equals("0天0小时0分0秒") && ranking.equals("1")){
                    handler.removeCallbacks(thread);
                    if (type.equals("1")){
                        startActivity(new Intent(IntroduceActivity.this,ContentActivity.class)
                                .putExtra("url",url)
                                .putExtra("code",getIntent().getStringExtra("code"))
                                .putExtra("code1",getIntent().getStringExtra("code1"))
                                .putExtra("title",getIntent().getStringExtra("title")));
                    }else{
                        startActivity(new Intent(IntroduceActivity.this,Content1Activity.class)
                                .putExtra("code",getIntent().getStringExtra("code"))
                                .putExtra("code1",getIntent().getStringExtra("code1"))
                                .putExtra("title",getIntent().getStringExtra("title"))
                                .putExtra("url",url));
                    }

                    finish();
                }else{
                    tv_daojishi.setText(time);
                    handler.postDelayed(thread,1000);
                }

            }
        }
    };

    Thread thread = new Thread(){
        @Override
        public void run() {
            super.run();
            handler.sendEmptyMessage(0x111);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);

        myProgressDialog.setTitle("加载中...");
        init();

    }

    private void init() {
        type = getIntent().getStringExtra("type");
        url = getIntent().getStringExtra("url");
        date = getIntent().getStringExtra("date");
        ranking = getIntent().getStringExtra("ranking");

        tv_share_wx = (TextView) findViewById(R.id.iv_share_wx);
        title_back = (ImageView) findViewById(R.id.title_back);
        web_introduce = (WebView) findViewById(R.id.web_introduce);
        tv_daojishi = (TextView) findViewById(R.id.tv_daojishi);
        ll_daojishi = (LinearLayout) findViewById(R.id.ll_daojishi);
        popupView = View.inflate(this,R.layout.popupwindow_share_layout,null);
        tvWX = (ImageView) popupView.findViewById(R.id.tv_wx);
        tvPengyou = (ImageView) popupView.findViewById(R.id.tv_pengtoy);
        tvQQ = (ImageView) popupView.findViewById(R.id.tv_qq);
        tvQZone = (ImageView) popupView.findViewById(R.id.tv_qzone);
        v1 = popupView.findViewById(R.id.v1);

        loadingView = (LoadingView) findViewById(R.id.loading_view);
        ivNoLink = (NoLinkView) findViewById(R.id.iv_no_link);

        web_introduce.getSettings().setJavaScriptEnabled(true);
        web_introduce.setWebChromeClient(new WebChromeClient());

        if (Tools.getAPNType(IntroduceActivity.this) == true){
            ivNoLink.setVisibility(View.GONE);
            web_introduce.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // TODO Auto-generated method stub
                    view.loadUrl(url);
                    return false;
                }
            });

            web_introduce.loadUrl(url);
        }else{
            ivNoLink.setVisibility(View.VISIBLE);
        }


        ivNoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tools.getAPNType(IntroduceActivity.this) == true){
                    ivNoLink.setVisibility(View.GONE);
                    web_introduce.getSettings().setJavaScriptEnabled(true);

                    web_introduce.setWebViewClient(new WebViewClient(){
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            // TODO Auto-generated method stub
                            view.loadUrl(url);
                            return false;
                        }
                    });

                    web_introduce.loadUrl(url);
                }else{
                    ivNoLink.setVisibility(View.VISIBLE);
                }
            }
        });


        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(thread);
                finish();
            }
        });


        HttpConnect.post(this, "member_share_content", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {

                String str = arg0.body().string();
                JSONObject data = JSONObject.fromObject(str);
                if (data.getString( "status").equals("success")) {
                    url1 = data.getJSONArray("data").getJSONObject(0).getString("pic");
                }else{
//                    sToast(data.getString("msg"));
                }

            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {

            }
        });

        tv_share_wx.setOnClickListener(this);
        tvWX.setOnClickListener(this);
        tvPengyou.setOnClickListener(this);
        tvQQ.setOnClickListener(this);
        tvQZone.setOnClickListener(this);
        v1.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.post(thread);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(thread);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_share_wx:
                window = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT);

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

                // 设置popWindow的显示和消失动画
                window.setAnimationStyle(R.style.Mypopwindow_anim_style);

                // 设置可以获取焦点
                window.setFocusable(true);
                //设置可以触摸弹出框以外的区域
                window.setOutsideTouchable(true);
                // 更新popupwindow的状态
                window.update();
                // 在底部显示
                window.showAtLocation(findViewById(R.id.iv_share_wx),
                        Gravity.BOTTOM, 0, 0);
                break;
            case R.id.tv_wx:

                api = WXAPIFactory.createWXAPI(this, WX_APP_ID, true);
                api.registerApp(WX_APP_ID);

                if (api.isWXAppInstalled()) {
                    WXWebpageObject webpage = new WXWebpageObject();
                    webpage.webpageUrl = getIntent().getStringExtra("url")+"&linkcode="+ LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getLinkCode()+"&from=1";

                    WXMediaMessage msg = new WXMediaMessage(webpage);
                    msg.title =  getIntent().getStringExtra("title");
                    msg.description = getIntent().getStringExtra("desc");
                    Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.icon1);
//            Bitmap thumb = returnBitMap(url1);
                    ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                    thumb.compress(Bitmap.CompressFormat.PNG, 100, bStream);
                    msg.thumbData = bStream.toByteArray();

                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = "text";
                    req.message = msg;
                    req.scene = SendMessageToWX.Req.WXSceneSession;

                    api.sendReq(req);
                } else {
                    sToast("请安装微信客户端");
                }
                window.dismiss();

                break;
            case R.id.tv_pengtoy:
                api = WXAPIFactory.createWXAPI(this, WX_APP_ID, true);
                api.registerApp(WX_APP_ID);
                if (api.isWXAppInstalled()) {
                    WXWebpageObject webpage = new WXWebpageObject();
                    webpage.webpageUrl = getIntent().getStringExtra("url")+"&linkcode="+ LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getLinkCode()+"&from=1";

                    WXMediaMessage msg = new WXMediaMessage(webpage);
                    msg.title = getIntent().getStringExtra("title");
                    msg.description = getIntent().getStringExtra("desc");
                    Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.icon1);
//            Bitmap thumb = returnBitMap(url1);
                    ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                    thumb.compress(Bitmap.CompressFormat.PNG, 100, bStream);
                    msg.thumbData = bStream.toByteArray();

                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = "text";
                    req.message = msg;
                    req.scene = SendMessageToWX.Req.WXSceneTimeline;

                    api.sendReq(req);
                } else {
                    sToast("请安装微信客户端");
                }
                window.dismiss();
                break;
            case R.id.tv_qq:
                myProgressDialog.show();
                mTencent = Tencent.createInstance(QQ_APP_ID, this.getApplicationContext());
//                if (!mTencent.isSessionValid()) {
//                    mTencent.login(this, "get_user_info", listener);
//                }

                final Bundle params = new Bundle();
                params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
                params.putString(QQShare.SHARE_TO_QQ_TITLE, getIntent().getStringExtra("title"));
                params.putString(QQShare.SHARE_TO_QQ_SUMMARY, getIntent().getStringExtra("desc"));
                params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, getIntent().getStringExtra("url")+"&linkcode="+ LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getLinkCode()+"&from=1");
                params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, url1);
                params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "分乐宝");
                params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
                mTencent.shareToQQ(this, params, listener);
                window.dismiss();
                break;
            case R.id.tv_qzone:
                myProgressDialog.show();
                mTencent = Tencent.createInstance(QQ_APP_ID, this.getApplicationContext());
//                if (!mTencent.isSessionValid()) {
//                    mTencent.login(this, "get_user_info", listener);
//                }

                final Bundle params1 = new Bundle();
                params1.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
                params1.putString(QQShare.SHARE_TO_QQ_TITLE,  getIntent().getStringExtra("title"));
                params1.putString(QQShare.SHARE_TO_QQ_SUMMARY, getIntent().getStringExtra("desc"));
                params1.putString(QQShare.SHARE_TO_QQ_TARGET_URL, getIntent().getStringExtra("url")+"&linkcode="+ LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getLinkCode()+"&from=1");
                params1.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,url1);
                params1.putString(QQShare.SHARE_TO_QQ_APP_NAME, "分乐宝");
                params1.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
                mTencent.shareToQQ(this, params1, listener);
                window.dismiss();
                break;
            case R.id.v1:
                window.dismiss();
                break;
        }
    }

    private IUiListener listener = new IUiListener() {

        @Override
        public void onCancel() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    myProgressDialog.cancel();
                    sToast("用户关闭");
                }
            });

        }

        @Override
        public void onComplete(Object arg0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    myProgressDialog.cancel();
                    sToast("QQ分享成功");
                }
            });

        }

        @Override
        public void onError(final UiError arg0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    myProgressDialog.cancel();
                    sToast("QQ分享失败"+arg0.toString());
                }
            });

        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//      Tencent.onActivityResultData(requestCode, resultCode, data, mUiListener);
        if(requestCode == Constants.REQUEST_QQ_SHARE || requestCode == Constants.REQUEST_QZONE_SHARE){
            if (resultCode == Constants.ACTIVITY_OK) {
                Tencent.handleResultData(data, listener);
            }
        }
    }



    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                loadingView.setVisibility(View.GONE);
            } else {
                if (loadingView.getVisibility() == View.GONE)
                    loadingView.setVisibility(View.VISIBLE);
//	                pro.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

    }

}
