package com.tohier.cartercoin.biz;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.activity.MeActivity;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.listener.LoadLoginPicListener;
import net.sf.json.JSONObject;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 武文锴 on 2016/11/5.
 */

public class LoadLoginPicBiz implements ILoadLoginPicBiz {

    /**
     * 这是登录页面的按钮   保存背景图片所用的名称
     */
    public static final String BUTTON_PIC = "iv_login_btn_icon_bg.png";

    /**
     * 这是登录页面的logo   保存背景图片所用的名称
     */
    public static final String BACKGROUND_PIC = "login_ctc_icon.png";

    /**
     * 这是欢迎页面的logo   保存背景图片所用的名称
     */
    public static final String WELCOME_PIC = "welcome_page_img.png";

    /**
     * 图片保存的路径
     **/
    public final static String ALBUM_PATH = Environment
            .getExternalStorageDirectory() + "/download_test/";

    /**
     * 上下文对象
     **/
    private Context context;

    public LoadLoginPicBiz(Context context) {
        this.context = context;
    }

    @Override
    public void loadPic(final LoadLoginPicListener loadLoginPicListener) {
        //mZProgressHUD.show();
        HttpConnect.post((MeActivity) context, "member_welcome_background", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    final String buttonpic = data.optJSONArray("data").optJSONObject(0).optString("buttonpic");
                    final String backgroundpic = data.optJSONArray("data").optJSONObject(0).optString("backgroundpic");
                    final String welcomepic = data.optJSONArray("data").optJSONObject(0).optString("welcomepic");

                    saveFile(buttonpic,BUTTON_PIC);
                    saveFile(backgroundpic,BACKGROUND_PIC);
                    saveFile(welcomepic,WELCOME_PIC);
                    loadLoginPicListener.loadSuccess();
                } else {
                    loadLoginPicListener.loadFail();
                }

            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
                loadLoginPicListener.loadFail();
            }
        });
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
        bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
        bos.flush();
        bos.close();
    }


    /**
     *读取sd卡下图片，由图片路劲转换为bitmap
     * @param path   图片路径
     * @return       返回值为bitmap
     */
    public static Bitmap convertToBitmap(String path) {

       Bitmap bitmap = BitmapFactory.decodeFile(path);

        return bitmap;
    }

}
