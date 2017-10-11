package com.tohier.cartercoin.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.tohier.android.plus.photoView.PhotoPopupWindow;
import com.tohier.android.util.FileUtils;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.columnview.MyProgressDialog;
import com.tohier.cartercoin.config.HttpConnect;

import net.sf.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.tohier.cartercoin.activity.MeActivity.SHARE_PIC1;


public class NewShareActivity extends MyBackBaseActivity  implements View.OnClickListener{

    public static MyProgressDialog myProgressDialog2;
    private static final String WX_APP_ID = "wx7ad749f6cba84064";
    private static final String QQ_APP_ID = "1105547483";

    public IWXAPI api;

    public Tencent mTencent;
    private ImageView iv_bg;


    private Bitmap finalBitmap1,bitmap;

    private ImageView iv_wx,iv_pengyouquan,iv_qq,iv_qzong;


    /**
     * 这是分享图片所用的名称
     */
    public static final String SHARE_PIC = "shares.jpg";
    /**
     * 图片保存的路劲
     **/
    public final static String ALBUM_PATH = Environment
            .getExternalStorageDirectory() + "/download_test/";


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0x111){
                sToast("已将图片保存至相册！");
                myProgressDialog.cancel();
            }
        }
    };
    Thread thread = new Thread(){
        @Override
        public void run(){
            handler.sendEmptyMessage(0x111);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_new);

        myProgressDialog.setTitle("加载中...");
        myProgressDialog2 = myProgressDialog;
        init();

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
//        SliderConfig mConfig = new SliderConfig.Builder()
//                .primaryColor(Color.TRANSPARENT)
//                .secondaryColor(Color.TRANSPARENT)
//                .position(SliderPosition.LEFT)
//                .edge(false)
//                .build();
//
//
//
//        ISlider iSlider = SliderUtils.attachActivity(this, mConfig);
//        mConfig.setPosition(SliderPosition.LEFT);
//        iSlider.setConfig(mConfig);

        /**
         * 获取分享图片
         */
        HttpConnect.post(this, "member_share_pic_url", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {

                String str = arg0.body().string();
                JSONObject data = JSONObject.fromObject(str);
                if (data.getString( "status").equals("success")) {
                    String url1 = data.optJSONArray("data").optJSONObject(0).optString("url");
                    saveFile(url1,SHARE_PIC);
                    finalBitmap1 = convertToBitmap(ALBUM_PATH+SHARE_PIC);
                }else{
                    sToast(data.getString("msg"));
                }

            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {

            }
        });

    }



    private void init() {
        iv_bg = (ImageView) findViewById(R.id.iv_bg);
        iv_wx = (ImageView) findViewById(R.id.iv_wechat);
        iv_pengyouquan = (ImageView) findViewById(R.id.iv_pengyouquan);
        iv_qq = (ImageView) findViewById(R.id.qq);
        iv_qzong = (ImageView) findViewById(R.id.qzone);
        bitmap = convertToBitmap(ALBUM_PATH+ SHARE_PIC1);
        iv_bg.setImageBitmap(bitmap);

        iv_wx.setOnClickListener(this);
        iv_pengyouquan.setOnClickListener(this);
        iv_qq.setOnClickListener(this);
        iv_qzong.setOnClickListener(this);


    }


    public void onXiazai(View v) {
        myProgressDialog.show();
        saveImageToGallery(this,bitmap);
       handler.postDelayed(thread,1500);

    }

    /**
     * 保存图片
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
            HttpConnect.post(NewShareActivity.this, "member_task_share", null, new Callback() {
                @Override
                public void onResponse(Response arg0) throws IOException {
                }
                @Override
                public void onFailure(Request arg0, IOException arg1) {

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

    /***
     * 图片压缩方法二
     *
     * @param bgimage
     *            ：源图片资源
     * @param newWidth
     *            ：缩放后宽度
     * @param newHeight
     *            ：缩放后高度
     * @return
     */
    public Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

    /**
     * 将输入流转换为bitmap 保存到内存中
     */
    protected  void saveFile(String url,String fileName)
    {
        // 取得的是inputstream，直接从inputstream生成bitmap
        try {
            InputStream inputStream = getImageStream(url);
            if(inputStream!=null)
            {
                Bitmap mBitmap = BitmapFactory.decodeStream(inputStream);
                saveFile(mBitmap,fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     *读取sd卡下图片，由图片路劲转换为bitmap
     * @param path   图片路径
     * @return       返回值为bitmap
     */
    public static Bitmap convertToBitmap(String path) {
//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        // Calculate inSampleSize
//        options.inSampleSize = 2;
        return BitmapFactory.decodeFile(path);
    }

    /**
     * 从网络获取图片
     * @param path 加载图片的url
     */
    protected InputStream getImageStream(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(10 * 1000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return conn.getInputStream();
        }
        return null;
    }


    /*
    * 保存文件
    */
    protected void  saveFile(Bitmap bm, String fileName) throws IOException {
        File dirFile = new File(ALBUM_PATH);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(ALBUM_PATH + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 60, bos);
        bos.flush();
        bos.close();
    }

    @Override
    public void onClick(View v) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myProgressDialog.show();
            }
        });
      switch (v.getId()){
          case R.id.iv_wechat:
              if (finalBitmap1 != null){
                  Bitmap finalBitmap = zoomImage(finalBitmap1,380,675.5);
                  api = WXAPIFactory.createWXAPI(NewShareActivity.this, WX_APP_ID, true);
                  api.registerApp(WX_APP_ID);
                  if (api.isWXAppInstalled()) {
                      WXImageObject imageObject = new WXImageObject(finalBitmap);
                      final WXMediaMessage msg = new WXMediaMessage();

                      msg.mediaObject = imageObject;
                      ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                      finalBitmap.compress(Bitmap.CompressFormat.JPEG, 60, bStream);
                      msg.thumbData = bStream.toByteArray();

                      SendMessageToWX.Req req = new SendMessageToWX.Req();

                      req.transaction = "image";
                      req.message = msg;
                      req.scene = SendMessageToWX.Req.WXSceneSession;
                      api.sendReq(req);
                  } else {
                      sToast("请安装微信客户端");
                      runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              myProgressDialog.cancel();
                          }
                      });
                  }
              }

              break;
          case R.id.iv_pengyouquan:
              if (finalBitmap1 != null){
                  Bitmap finalBitmap = zoomImage(finalBitmap1,380,675.5);
                  api = WXAPIFactory.createWXAPI(NewShareActivity.this, WX_APP_ID, true);
                  api.registerApp(WX_APP_ID);
                  if (api.isWXAppInstalled()) {
                      WXImageObject imageObject = new WXImageObject(finalBitmap);
                      final WXMediaMessage msg = new WXMediaMessage();

                      msg.mediaObject = imageObject;

                      ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                      finalBitmap.compress(Bitmap.CompressFormat.JPEG, 60, bStream);
                      msg.thumbData = bStream.toByteArray();

                      SendMessageToWX.Req req = new SendMessageToWX.Req();
                      req.transaction = "image";
                      req.message = msg;
                      req.scene = SendMessageToWX.Req.WXSceneTimeline;
                      api.sendReq(req);
                  } else {
                      sToast("请安装微信客户端");
                      runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              myProgressDialog.cancel();
                          }
                      });
                  }
              }
              break;
          case R.id.qq:
              if (finalBitmap1 != null){
                  mTencent = Tencent.createInstance(QQ_APP_ID, NewShareActivity.this.getApplicationContext());
//                  if (!mTencent.isSessionValid()) {
//                      mTencent.login(NewShareActivity.this, "get_user_info", listener);
//                  }
                  Bundle params = new Bundle();
                  params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,ALBUM_PATH+SHARE_PIC);
                  params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "分乐宝");
                  params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
                  params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
                  mTencent.shareToQQ(NewShareActivity.this, params, listener);
              }
              break;
          case R.id.qzone:
              if (finalBitmap1 != null){
                  mTencent = Tencent.createInstance(QQ_APP_ID, NewShareActivity.this.getApplicationContext());
//                  if (!mTencent.isSessionValid()) {
//                      mTencent.login(NewShareActivity.this, "get_user_info", listener);
//                  }
                  Bundle params = new Bundle();
                  params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,ALBUM_PATH+SHARE_PIC);
                  params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "分乐宝");
                  params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
                  params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
                  mTencent.shareToQQ(NewShareActivity.this, params, listener);
              }

              break;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        myProgressDialog.cancel();
    }

}
