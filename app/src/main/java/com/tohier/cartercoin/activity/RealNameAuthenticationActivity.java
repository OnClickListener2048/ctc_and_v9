package com.tohier.cartercoin.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.android.util.BitmapUtil;
import com.tohier.android.util.FileUtils;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.config.HttpConnect;

import net.sf.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Administrator on 2016/12/30.
 */

public class RealNameAuthenticationActivity extends MyBaseActivity {

    private ImageView iv_back2,iv_zheng,iv_fan;
    private Button btn_commit_authentication;
    private EditText et_name,et_id_number;
    private GifImageView gif_loading;
    private PopupWindow window;
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 2;

    int i = 0;

    /**
     * 1---正
     * 2---反
     */
    private int type1 = 1;
    private Bitmap photo; //身份证正面
    private Bitmap photo1;//身份证反面
    private static int RESULT_LOAD_IMAGE = 1;

    private Button btn_tuku, btn_camera, btn_quxiao;

    /**
     * 是否是全部上传
     */

    private View view1;

    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_real_name_authentication);

        myProgressDialog.setTitle("正在加载...");
        initData();
     }

    @Override
    public void initData() {
        loadMemberInfo();
        view1 = View.inflate(this, R.layout.photo_popupwindow_item, null);
        window = new PopupWindow(view1, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        iv_back2 = (ImageView) this.findViewById(R.id.iv_back2);
        iv_zheng = (ImageView) this.findViewById(R.id.iv_zheng);
        iv_fan = (ImageView) this.findViewById(R.id.iv_fan);
        btn_commit_authentication = (Button) this.findViewById(R.id.btn_commit_authentication);
        et_name = (EditText) this.findViewById(R.id.et_name);
        et_id_number = (EditText) this.findViewById(R.id.et_id_number);
        gif_loading = (GifImageView) this.findViewById(R.id.gif_loading);


        btn_tuku = (Button) view1
                .findViewById(R.id.photo_popupwindow_item_photo);
        btn_camera = (Button) view1
                .findViewById(R.id.photo_popupwindow_item_camera);
        btn_quxiao = (Button) view1
                .findViewById(R.id.photo_popupwindow_item_look);



        /**
         * 头像处理
         */
        btn_tuku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                if (FileUtils.checkSDCard()) {
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                } else {
                    RealNameAuthenticationActivity.this.sToast("请先插入SD卡");
                }
            }
        });
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                try{
                    String state = Environment.getExternalStorageState();
                    if (state.equals(Environment.MEDIA_MOUNTED)) {
                        Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
                        startActivityForResult(getImageByCamera, REQUEST_CODE_CAPTURE_CAMEIA);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){}

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

        iv_back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btn_commit_authentication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(et_name.isEnabled()&&et_id_number.isEnabled())
                {
                    if(TextUtils.isEmpty(et_name.getText().toString().trim()))
                    {
                        sToast("真实姓名不能为空");
                        return;
                    }else if(TextUtils.isEmpty(et_id_number.getText().toString().trim()))
                    {
                        sToast("身份证号不能为空");
                        return;
                    }else
                    {
                        verificationMobileIsBand();
                    }
                }else if(et_id_number.isEnabled())
                {
                        if(TextUtils.isEmpty(et_id_number.getText().toString().trim()))
                        {
                            sToast("身份证号不能为空");
                            return;
                        }else
                        {
                            if(i!=0)
                            {
                                verificationMobileIsBand();
                            }
                        }
                }else if(et_name.isEnabled())
                {
                    if(et_name.isEnabled())
                    {
                        if(TextUtils.isEmpty(et_name.getText().toString().trim()))
                        {
                            sToast("真实姓名不能为空");
                            return;
                        }else
                        {
                            if(i!=0)
                            {
                                verificationMobileIsBand();
                            }
                        }
                    }
                }else
                {
                   if(i!=0)
                      {
                       HashMap<String,String> map = new HashMap<String, String>();
                       if (photo == null) {
                           sToast("请添加图片");
                           myProgressDialog.cancel();
                           return;
                       }

                       if (photo1 == null) {
                           sToast("请添加图片");
                           myProgressDialog.cancel();
                           return;
                       }

                       myProgressDialog.show();
                       map.put("upfile:up", BitmapUtil.bitmapToBase64(photo));
                       map.put("upfile:down",BitmapUtil.bitmapToBase64(photo1));
                       HttpConnect.post(RealNameAuthenticationActivity.this, "member_idpic_up_down", map, new com.squareup.okhttp.Callback() {

                           @Override
                           public void onResponse(Response arg0) throws IOException {
                               JSONObject data = JSONObject.fromObject(arg0.body().string());
                               if (data.get("status").equals("success")) {
                                   runOnUiThread(new Runnable() {
                                       @Override
                                       public void run() {
                                           SharedPreferences sharedPreferences2 = getSharedPreferences("isExitsName", Context.MODE_PRIVATE);
                                           sharedPreferences2.edit().putString("name",et_name.getText().toString().trim()).commit();
                                           SharedPreferences sharedPreferences3 = getSharedPreferences("isIdNumberPic",Context.MODE_PRIVATE);
                                           sharedPreferences3.edit().putString("picup","有").commit();
                                           sToast("修改成功");
                                           myProgressDialog.cancel();
                                           finish();
                                       }
                                   });

                               }else
                               {
                                   final String msg8 = data.getString("msg");
                                   Handler dataHandler = new Handler(getContext()
                                           .getMainLooper()) {

                                       @Override
                                       public void handleMessage(final Message msg) {
                                           myProgressDialog.cancel();
                                           sToast(msg8);
                                       }
                                   };
                                   dataHandler.sendEmptyMessage(0);
                               }
                           }

                           @Override
                           public  void onFailure(Request arg0, IOException arg1) {
                               runOnUiThread(new Runnable() {
                                   @Override
                                   public void run() {
                                       myProgressDialog.cancel();
                                       sToast("网络质量不佳，请检查网络！");
                                   }
                               });
                           }
                       });
                   }
                }
            }
        });
    }

    private String mobile;
    private String idnumber;
    private String qqopenid;
    private String wechatopenid;
    private String agent;
    private String introducermobile;
    private String sex;
    private String introducerid;
    private String name;
    private String gesturepassword;
    private String linkcode;
    private String type;
    private String nickname;
    private String pic;
    private String birthday;
    private String id;
    private String picup;
    private String picdown;

    /**
     * 获取会员的信息
     */
    public void loadMemberInfo()
    {
        HttpConnect.post(RealNameAuthenticationActivity.this, "member_info", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    id = data.getJSONArray("data").getJSONObject(0).getString("ID");
                    birthday = data.getJSONArray("data").getJSONObject(0).getString("birthday");
                    pic = data.getJSONArray("data").getJSONObject(0).getString("pic");
                    nickname = data.getJSONArray("data").getJSONObject(0).getString("nickname");
                    type = data.getJSONArray("data").getJSONObject(0).getString("type");
                    linkcode = data.getJSONArray("data").getJSONObject(0).getString("linkcode");
                    gesturepassword = data.getJSONArray("data").getJSONObject(0).getString("gesturespassword");
                    name = data.getJSONArray("data").getJSONObject(0).getString("name");
                    introducerid = data.getJSONArray("data").getJSONObject(0).getString("introducerid");
                    sex = data.getJSONArray("data").getJSONObject(0).getString("sex");
                    introducermobile = data.getJSONArray("data").getJSONObject(0).getString("introducermobile");
                    agent = data.getJSONArray("data").getJSONObject(0).getString("agent");
                    wechatopenid = data.getJSONArray("data").getJSONObject(0).getString("wechatopenid");
                    qqopenid = data.getJSONArray("data").getJSONObject(0).getString("qqopenid");
                    idnumber = data.getJSONArray("data").getJSONObject(0).getString("idnumber");
                    mobile = data.getJSONArray("data").getJSONObject(0).getString("mobile");
                    picup = data.optJSONArray("data").optJSONObject(0).optString("picup");
                    picdown = data.optJSONArray("data").optJSONObject(0).optString("picdown");

                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {
                            i = 1;
                            if(!TextUtils.isEmpty(idnumber))
                            {
                                et_id_number.setText(idnumber.substring(0,3)+"***********"+idnumber.substring(14,18));
                                et_id_number.setEnabled(false);
                            }

                            if(!TextUtils.isEmpty(name))
                            {
                                et_name.setText(name);
                                et_name.setEnabled(false);
                            }

                            if(!TextUtils.isEmpty(picup))
                            {
                                Glide.with(RealNameAuthenticationActivity.this).load(picup).placeholder(null)
                                        .error(null).into(iv_zheng);
                            }else
                            {
                                iv_zheng.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        type1 = 1;
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
                                        window.showAtLocation(iv_back2,
                                                Gravity.BOTTOM, 0, 0);

                                    }
                                });
                            }

                            if(!TextUtils.isEmpty(picdown))
                            {
                                Glide.with(RealNameAuthenticationActivity.this).load(picdown).placeholder(null)
                                        .error(null).into(iv_fan);
                            }else
                            {
                                iv_fan.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        type1 = 2;

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
                                        window.showAtLocation(iv_back2,
                                                Gravity.BOTTOM, 0, 0);
                                    }
                                });
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
                            sToast(msg8);
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
//                        sToast("链接超时");
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        });
    }

    public void updateMemberInfo()
    {

        if (photo == null) {
            sToast("请添加图片");
            myProgressDialog.cancel();
            return;
        }

        if (photo1 == null) {
            sToast("请添加图片");
            myProgressDialog.cancel();
            return;
        }

        myProgressDialog.show();
        Map<String, String> par1 = new HashMap<String, String>();
        par1.put("pic", pic);
        par1.put("nickname", nickname);
        par1.put("name", et_name.getText().toString().trim());
        par1.put("birthday", birthday);

        String s = sex;
        if (s.equals("保密")){
            par1.put("sex", "-1");
        }else if (s.equals("帅哥")){
            par1.put("sex", "1");
        }else if (s.equals("美女")){
            par1.put("sex", "0");
        }
        par1.put("qqopenid", qqopenid);
        par1.put("wechatopenid", wechatopenid);
        par1.put("idnumber", et_id_number.getText().toString().trim());
        par1.put("phonenunber",mobile );

        HttpConnect.post(this, "member_update_information", par1, new Callback() {

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                Handler dataHandler = new Handler(getContext()
                        .getMainLooper()) {

                    @Override
                    public void handleMessage(final Message msg) {
                        sToast("网络质量不佳，请检查网络！");
                        myProgressDialog.cancel();
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject object = JSONObject.fromObject(arg0.body().string());
                if (object.get("status").equals("success")) {
                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {

                            HashMap<String,String> map = new HashMap<String, String>();
                            map.put("upfile:up", BitmapUtil.bitmapToBase64(photo));
                            map.put("upfile:down",BitmapUtil.bitmapToBase64(photo1));
                            HttpConnect.post(RealNameAuthenticationActivity.this, "member_idpic_up_down", map, new com.squareup.okhttp.Callback() {

                                @Override
                                public void onResponse(Response arg0) throws IOException {
                                    JSONObject data = JSONObject.fromObject(arg0.body().string());
                                    if (data.get("status").equals("success")) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                myProgressDialog.cancel();
                                                SharedPreferences sharedPreferences2 = getSharedPreferences("isExitsName", Context.MODE_PRIVATE);
                                                sharedPreferences2.edit().putString("name",et_name.getText().toString().trim()).commit();

                                                SharedPreferences sharedPreferences3 = getSharedPreferences("isIdNumberPic",Context.MODE_PRIVATE);
                                                sharedPreferences3.edit().putString("picup","有").commit();
                                                sToast("修改成功");
                                                finish();
                                            }
                                        });

                                    }else
                                    {
                                        final String msg8 = data.getString("msg");
                                        Handler dataHandler = new Handler(getContext()
                                                .getMainLooper()) {

                                            @Override
                                            public void handleMessage(final Message msg) {
                                                sToast(msg8);
                                                myProgressDialog.cancel();
                                            }
                                        };
                                        dataHandler.sendEmptyMessage(0);
                                    }
                                }

                                @Override
                                public  void onFailure(Request arg0, IOException arg1) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            sToast("网络质量不佳，请检查网络！");
                                            myProgressDialog.cancel();
                                        }
                                    });
                                }
                            });

                        }
                    };
                    dataHandler.sendEmptyMessage(0);


                }else{
                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {
                            myProgressDialog.cancel();
                            if(object.optString("msg")!=null)
                            {
                                sToast(object.optString("msg"));
                            }else
                            {
                                sToast("认证失败");
                            }

                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }
            }
        });
    }

    public void updateMemberInfo2()
    {
        myProgressDialog.show();
        Map<String, String> par1 = new HashMap<String, String>();
        par1.put("pic", pic);
        par1.put("nickname", nickname);
        par1.put("name", et_name.getText().toString().trim());
        par1.put("birthday", birthday);

        String s = sex;
        if (s.equals("保密")){
            par1.put("sex", "-1");
        }else if (s.equals("帅哥")){
            par1.put("sex", "1");
        }else if (s.equals("美女")){
            par1.put("sex", "0");
        }
        par1.put("qqopenid", qqopenid);
        par1.put("wechatopenid", wechatopenid);
        par1.put("idnumber", et_id_number.getText().toString().trim());
        par1.put("phonenunber",mobile );

        HttpConnect.post(this, "member_update_information", par1, new Callback() {

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                Handler dataHandler = new Handler(getContext()
                        .getMainLooper()) {

                    @Override
                    public void handleMessage(final Message msg) {
                        sToast("网络质量不佳，请检查网络！");
                        myProgressDialog.cancel();
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject object = JSONObject.fromObject(arg0.body().string());
                if (object.get("status").equals("success")) {
                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {

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
                            myProgressDialog.cancel();
                            if(object.optString("msg")!=null)
                            {
                                sToast(object.optString("msg"));
                            }else
                            {
                                sToast("认证失败");
                            }

                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }
            }
        });
    }

    /**
     * 验证输入的认证信息是否可以认证
     */
    public void verificationMobileIsBand()
    {
        myProgressDialog.show();
        Map<String, String> par1 = new HashMap<String, String>();
        String id = et_id_number.getText().toString().trim();
        String name = et_name.getText().toString().trim();
        par1.put("id",id);
        par1.put("name",name );

        HttpConnect.post(this, "member_check_idnumber", par1, new Callback() {

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         sToast("网络质量不佳，请检查网络！");
                         myProgressDialog.cancel();
                     }
                 });
            }

            @Override
            public void onResponse(final Response arg0) throws IOException {
                final JSONObject object = JSONObject.fromObject(arg0.body().string());
                if (object.get("status").equals("success")) {
                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {
                            String value = object.getJSONArray("data").getJSONObject(0).getString("value");
                            if(!TextUtils.isEmpty(value))
                            {
                                if(value.equals("1")) //可以
                                {
                                    if(!TextUtils.isEmpty(picup)&&!TextUtils.isEmpty(picdown))
                                    {
                                        updateMemberInfo2();
                                    }else
                                    {
                                        updateMemberInfo();
                                    }
                                }else if(value.equals("2"))  //省份证号有误
                                {
                                    myProgressDialog.cancel();
                                    sToast("身份证号输入有误");
                                }else if(value.equals("3"))  //  名字绑定过
                                {
                                    myProgressDialog.cancel();
                                    sToast("该姓名已有用户绑定");
                                }else if(value.equals("4"))  //   省份证号绑定过
                                {
                                    myProgressDialog.cancel();
                                    sToast("该身份证已有用户绑定");
                                }
                            }
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && null != data) {

            if (requestCode == RESULT_LOAD_IMAGE ){
                Uri selectedImage = data.getData();
                    //获取目标控件的大小
                    int targetW = iv_fan.getWidth();
                    int targetH = iv_fan.getHeight();

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

                        if (type1 == 1){
                            photo = bitmap;
                            iv_zheng.setImageBitmap(bitmap);
                        }else if (type1 == 2){
                            photo1 = bitmap;
                            iv_fan.setImageBitmap(bitmap);

                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }


//                Uri selectedImage = data.getData();
//                String[] filePathColumn = { MediaStore.Images.Media.DATA };
//
//                Cursor cursor = getContentResolver().query(selectedImage,
//                        filePathColumn, null, null, null);
//                cursor.moveToFirst();
//
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                String picturePath = cursor.getString(columnIndex);
//                cursor.close();
//
//                if (type1 == 1){
//
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inSampleSize = 9;
//
//                    photo = BitmapFactory.decodeFile(picturePath,options);
//                    iv_zheng.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//
//
//
//                }else if (type1 == 2){
//
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inSampleSize = 9;
//                    photo1 = BitmapFactory.decodeFile(picturePath,options);
//                    iv_fan.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//
//                }
            }else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA){
                Uri selectedImage = data.getData();
                if(selectedImage == null){
                    //use bundle to get data
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {

                        if (type1 == 1){
                            Bitmap photo1 = (Bitmap) bundle.get("data"); //get bitmap
                            Matrix matrix = new Matrix();
                            matrix.setScale(2f, 2f);
                            photo = Bitmap.createBitmap(photo1,0,0, photo1.getWidth(),
                                    photo1.getHeight(), matrix, true);
//                            ByteArrayOutputStream baos = null ;
//                            try{
//                                baos = new ByteArrayOutputStream();
//                                photo.compress(Bitmap.CompressFormat.JPEG, 30, baos);
                                iv_zheng.setImageBitmap(photo1);
//                            }finally{
//                                try {
//                                    if(baos != null)
//                                        baos.close() ;
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }


                        }else if (type1 == 2){
                            Bitmap photo = (Bitmap) bundle.get("data"); //get bitmap
                            Matrix matrix = new Matrix();
                            matrix.setScale(2f, 2f);
                            photo1 = Bitmap.createBitmap(photo,0,0, photo.getWidth(),
                                    photo.getHeight(), matrix, true);
//                            ByteArrayOutputStream baos = null ;
//                            try{
//                                baos = new ByteArrayOutputStream();
//                                photo1.compress(Bitmap.CompressFormat.JPEG, 30, baos);
                                iv_fan.setImageBitmap(photo);
//                            }finally{
//                                try {
//                                    if(baos != null)
//                                        baos.close() ;
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
                        }


                    } else {
                        Toast.makeText(getApplicationContext(), "err****", Toast.LENGTH_LONG).show();
                        return;
                    }
                }else{
                    //to do find the path of pic by uri
                }
            }

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(photo != null && !photo.isRecycled()){
            photo.recycle();
            photo = null;
        }
        if(photo1 != null && !photo1.isRecycled()){
            photo1.recycle();
            photo1 = null;
        }
    }
}
