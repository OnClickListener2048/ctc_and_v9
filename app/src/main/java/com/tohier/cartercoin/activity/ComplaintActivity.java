package com.tohier.cartercoin.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.android.util.BitmapUtil;
import com.tohier.android.util.FileUtils;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.Tools;

import net.sf.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/8.
 */
public class ComplaintActivity extends MyBaseActivity{

    private ImageView iv_back,iv_pic;
    private EditText et_title,et_content;
    private Button btn_commit;

    //修改头像的view
    private View view1;

    private Button btn_tuku, btn_quxiao;

    private PopupWindow window;


    private Bitmap photo;

    private static int RESULT_LOAD_IMAGE = 1;
    String SD_CARD_TEMP_DIR;


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            switch (v.getId())
            {
                case R.id.iv_back:
                          finish();
                    break;

                case R.id.btn_commit:
                    if(TextUtils.isEmpty(et_title.getText().toString().trim()))
                    {
                              sToast("请填写标题");
                        return;
                    }else if(TextUtils.isEmpty(et_content.getText().toString().trim()))
                    {
                        sToast("请填写内容");
                        return;
                    }else
                    {
                        Map<String, String> par = new HashMap<String, String>();
                        par.put("title", et_title.getText().toString().trim());
                        par.put("contents",  et_content.getText().toString().trim());
                        par.put("type",  "0");
                        if (photo != null) {
                            par.put("upfile:pic", BitmapUtil.bitmapToBase64(photo));
                        }

                        HttpConnect.post(ComplaintActivity.this, "member_proposal", par, new Callback() {

                            @Override
                            public void onResponse(Response arg0) throws IOException {
                                // TODO Auto-generated method stub
                                JSONObject jsonObject = JSONObject.fromObject(arg0
                                        .body().string());
                                final String msg = jsonObject.getString("msg");
                                if (jsonObject.get("status").equals("success")) {
                                    ComplaintActivity.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                          sToast("提交成功");
                                            finish();
                                        }
                                    });
                                } else {
                                    ComplaintActivity.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            if(msg!=null&&!TextUtils.isEmpty(msg))
                                            {
                                                boolean flag = Tools.isPhonticName(msg);
                                                if(!flag)
                                                {
                                                    sToast(msg);
                                                }
                                            }
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onFailure(Request arg0, IOException arg1) {
                                // TODO Auto-generated method stub
                                ComplaintActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
//                                        sToast("请检查您的网络连接状态");
                                    }
                                });
                            }
                        });
                    }
                    break;

                case R.id.iv_pic:
                    window = new PopupWindow(view1, WindowManager.LayoutParams.MATCH_PARENT,
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
                    // 在底部显示
                    window.showAtLocation(findViewById(R.id.iv_back),
                            Gravity.BOTTOM, 0, 0);
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_complaint_layout);

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

        SD_CARD_TEMP_DIR = Environment.getExternalStorageDirectory()
                + File.separator + "tmp.jpg";//设定照相后保存的文件名，类似于缓存文件

        iv_back = (ImageView) this.findViewById(R.id.iv_back);
        iv_pic = (ImageView) this.findViewById(R.id.iv_pic);
        et_title = (EditText) this.findViewById(R.id.et_title);
        et_content = (EditText) this.findViewById(R.id.et_content);
        btn_commit = (Button) this.findViewById(R.id.btn_commit);


        view1 = View.inflate(this, R.layout.complaint_photo_popupwindow_item, null);
        btn_tuku = (Button) view1
                .findViewById(R.id.photo_popupwindow_item_photo);
        btn_quxiao = (Button) view1
                .findViewById(R.id.photo_popupwindow_item_look);

        /**
         * 头像处理
         */
        btn_tuku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FileUtils.checkSDCard()) {
                    window.dismiss();
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                } else {
                    ComplaintActivity.this.sToast("请先插入SD卡");
                }
            }
        });

        btn_quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FileUtils.checkSDCard()) {
                    window.dismiss();
                }
            }
        });
    }

    private void setUpView() {
        iv_back.setOnClickListener(onClickListener);
        btn_commit.setOnClickListener(onClickListener);
        iv_pic.setOnClickListener(onClickListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            //获取目标控件的大小
            int targetW = iv_pic.getWidth();
            int targetH = iv_pic.getHeight();

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            try {
                //inJustDecodeBounds为true，可以加载源图片的尺寸大小，decodeStream方法返回的bitmap为null
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, bmOptions);
                // 得到源图片的尺寸
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;
                //通过比较获取较小的缩放比列
                int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
                // 将inJustDecodeBounds置为false，设置bitmap的缩放比列
                bmOptions.inJustDecodeBounds = false;
                if (scaleFactor != 1){
                    if (scaleFactor>=4){

                        if (scaleFactor>=5){
                            if (scaleFactor>=6){
                                if (scaleFactor>=6){

                                    bmOptions.inSampleSize = scaleFactor+10;
                                }else{
                                    bmOptions.inSampleSize = scaleFactor+8;
                                }
                            }else{
                                bmOptions.inSampleSize = scaleFactor+7;
                            }
                        }else{
                            bmOptions.inSampleSize = scaleFactor+6;
                        }

                    }else{
                        bmOptions.inSampleSize = scaleFactor+5;
                    }

                }else{
                    bmOptions.inSampleSize = scaleFactor+2;
                }

                bmOptions.inPurgeable = true;
                //再次decode获取bitmap
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, bmOptions);
                int size = bitmap.getByteCount();
                Log.i("wangge","size = "+size+"scaleFactor = "+bmOptions.inSampleSize);
                    photo = bitmap;
                iv_pic.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}
