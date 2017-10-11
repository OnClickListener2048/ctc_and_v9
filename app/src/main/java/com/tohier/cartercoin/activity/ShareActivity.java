package com.tohier.cartercoin.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
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
import com.tohier.android.plus.photoView.PhotoPopupWindow;
import com.tohier.android.util.FileUtils;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;

import net.sf.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

import de.hdodenhof.circleimageview.CircleImageView;


public class ShareActivity extends MyBaseActivity  {

    private static final String WX_APP_ID = "wx7ad749f6cba84064";
    private static final String QQ_APP_ID = "1105547483";

    public IWXAPI api;

    public Tencent mTencent;


    /**
     * 二维码的url
     */
    private String fenxiangUrl = "";
    private CircleImageView civHead;
    private TextView tvNick;
    private ImageView ivErweima,qq,qzone;
    private Bitmap bitmap;


    /**
     * 分享的内容
     */
    private String title1;
    private String description1;
    private String url1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        init();

    }



    private void init() {
        civHead = (CircleImageView) findViewById(R.id.civ_head);
        tvNick = (TextView) findViewById(R.id.tv_nick);
        ivErweima = (ImageView) findViewById(R.id.iv_erweima);
        qq = (ImageView) findViewById(R.id.qq);
        qzone = (ImageView) findViewById(R.id.qzone);


//        String name = getSharedPreferences("qqisOpen", Context.MODE_PRIVATE).getString("isopen","");
//        if(!TextUtils.isEmpty(name)&&name.equals("open")) {
//            qq.setVisibility(View.VISIBLE);
//            qzone.setVisibility(View.VISIBLE);
//        }


        /**
         * 设置头像  以及昵称
         */
        final String headUrl = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getHeadUrl();
        if (headUrl!=null){
            Glide.with(this).load(headUrl).asBitmap().centerCrop().error(R.mipmap.iv_member_default_head_img).into(new BitmapImageViewTarget(civHead) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    civHead.setImageDrawable(circularBitmapDrawable);
                }
            });
        }

        String neckName = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getNickName();
        if(!TextUtils.isEmpty(neckName))
        {
            tvNick.setText(neckName);
        }

        HttpConnect.post(this, "member_share_url", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.getString("status").equals("success")) {
                    fenxiangUrl = data.getJSONArray("data").getJSONObject(0).getString("Column1");

                   runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            bitmap = createCode();
                            ivErweima.setImageBitmap(bitmap);
//                            mZProgressHUD.cancel();
                        }
                    });


                } else {
//                    sToast(data.getString("msg"));
//                    mZProgressHUD.cancel();
                }

            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
//                sToast("链接超时！");
//                mZProgressHUD.cancel();
            }
        });


        HttpConnect.post(this, "member_share_content", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {

                String str = arg0.body().string();
                JSONObject data = JSONObject.fromObject(str);
                if (data.getString( "status").equals("success")) {
                    title1 = data.getJSONArray("data").getJSONObject(0).getString("title");
                    description1 = data.getJSONArray("data").getJSONObject(0).getString("description");
                    url1 = data.getJSONArray("data").getJSONObject(0).getString("pic");
                }else{
//                    sToast(data.getString("msg"));
                }

            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {

            }
        });



    }


    public void onXiazai(View v) {

        View view =getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        saveImageToGallery(this,bitmap);
        sToast("已将图片保存至相册！");
    }

    /**
     * 保存二维码
     * @param context
     * @param bmp
     */
    private void saveImageToGallery(Context context, Bitmap bmp) {
        PhotoPopupWindow.getImageUri("");
        File appDir = new File(FileUtils.SDPATH);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = "fenxiang" + ".png";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }


    /**
     * 生成二维码
     * @return
     */
    private Bitmap createCode() {
        int width = 180;
        int height = 180;
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 0);
        BitMatrix bitMatrix;
        try {

            QRCodeWriter writer = new QRCodeWriter();
            bitMatrix = writer.encode(fenxiangUrl,// id
                    BarcodeFormat.QR_CODE, width, height, hints);
            return bitMatrix2Bitmap(bitMatrix);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private Bitmap bitMatrix2Bitmap(BitMatrix matrix) {
        int w = matrix.getWidth();
        int h = matrix.getHeight();
        int[] rawData = new int[w * h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int color = Color.WHITE;
                if (matrix.get(i, j)) {
                    color = Color.BLACK;
                }
                rawData[i + (j * w)] = color;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        bitmap.setPixels(rawData, 0, w, 0, 0, w, h);
        return bitmap;
    }



    public void onWeChat(View view){
        api = WXAPIFactory.createWXAPI(this, WX_APP_ID, true);
        api.registerApp(WX_APP_ID);

        if (api.isWXAppInstalled()) {
            WXWebpageObject webpage = new WXWebpageObject();
            webpage.webpageUrl = fenxiangUrl;

            WXMediaMessage msg = new WXMediaMessage(webpage);
            msg.title = title1;
            msg.description = description1;
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

    }


//    public void onWeChat(View view){
//        api = WXAPIFactory.createWXAPI(this, WX_APP_ID, true);
//        api.registerApp(WX_APP_ID);
//        if (api.isWXAppInstalled()) {
//            Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.icon1);
//            WXImageObject imageObject = new WXImageObject(thumb);
//            final WXMediaMessage msg = new WXMediaMessage();
//
//            msg.mediaObject = imageObject;
//
//            Bitmap thumb1 = BitmapFactory.decodeResource(getResources(), R.mipmap.icon1);
//            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
//            thumb1.compress(Bitmap.CompressFormat.PNG, 100, bStream);
//            msg.thumbData = bStream.toByteArray();
//
//            SendMessageToWX.Req req = new SendMessageToWX.Req();
//            req.transaction = "image";
//            req.message = msg;
//            req.scene = SendMessageToWX.Req.WXSceneSession;
//            api.sendReq(req);
//        } else {
//            sToast("请安装微信客户端");
//        }
//    }
//
//    public void onPengYouquan(View view){
//        api = WXAPIFactory.createWXAPI(this, WX_APP_ID, true);
//        api.registerApp(WX_APP_ID);
//        if (api.isWXAppInstalled()) {
//            Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.icon1);
//            WXImageObject imageObject = new WXImageObject(thumb);
//            final WXMediaMessage msg = new WXMediaMessage();
//
//            msg.mediaObject = imageObject;
//
//            Bitmap thumb1 = BitmapFactory.decodeResource(getResources(), R.mipmap.icon1);
//            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
//            thumb1.compress(Bitmap.CompressFormat.PNG, 100, bStream);
//            msg.thumbData = bStream.toByteArray();
//
//            SendMessageToWX.Req req = new SendMessageToWX.Req();
//            req.transaction = "image";
//            req.message = msg;
//            req.scene = SendMessageToWX.Req.WXSceneTimeline;
//            api.sendReq(req);
//        } else {
//            sToast("请安装微信客户端");
//        }
//    }

    public void onPengYouquan(View view){
        api = WXAPIFactory.createWXAPI(this, WX_APP_ID, true);
        api.registerApp(WX_APP_ID);
        if (api.isWXAppInstalled()) {
            WXWebpageObject webpage = new WXWebpageObject();
            webpage.webpageUrl = fenxiangUrl;

            WXMediaMessage msg = new WXMediaMessage(webpage);
            msg.title = title1;
            msg.description = description1;
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
    }



    public void onQQ(View v) {

        mTencent = Tencent.createInstance(QQ_APP_ID, this.getApplicationContext());
        if (!mTencent.isSessionValid()) {
            mTencent.login(this, "get_user_info", listener);
        }

        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title1);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, description1);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, fenxiangUrl);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, url1);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "分乐宝");
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
        mTencent.shareToQQ(this, params, listener);
    }

    public void onQZone(View v) {

        mTencent = Tencent.createInstance(QQ_APP_ID, this.getApplicationContext());
        if (!mTencent.isSessionValid()) {
            mTencent.login(this, "get_user_info", listener);
        }

        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title1);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, description1);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, fenxiangUrl);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, url1);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "分乐宝");
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        mTencent.shareToQQ(this, params, listener);
    }

    private IUiListener listener = new IUiListener() {

        @Override
        public void onCancel() {
            sToast("用户关闭");
        }

        @Override
        public void onComplete(Object arg0) {
            sToast("QQ分享成功");
        }

        @Override
        public void onError(UiError arg0) {
            sToast("QQ分享失败"+arg0.toString());
        }
    };



    public void back(View view){
        finish();
    }

    @Override
    public void initData() {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//      Tencent.onActivityResultData(requestCode, resultCode, data, mUiListener);
        if(requestCode == Constants.REQUEST_QQ_SHARE || requestCode == Constants.REQUEST_QZONE_SHARE){
            if (resultCode == Constants.ACTIVITY_OK) {
                Tencent.handleResultData(data, listener);
            }
        }
    }

}
