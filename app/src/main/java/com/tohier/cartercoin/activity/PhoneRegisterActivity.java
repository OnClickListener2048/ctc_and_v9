package com.tohier.cartercoin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.android.config.IContext;
import com.tohier.android.util.BitmapUtil;
import com.tohier.android.util.FileUtils;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.MyApplication;
import com.tohier.cartercoin.config.Tools;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/3/7.
 */

public class PhoneRegisterActivity extends MyBaseActivity {

    private ImageView ivBack, iv_pwd_isShow,iv_pay_pwd_isShow;
    private LinearLayout linearLayout_into_picture;
    private Button btn_yanzhengma, btn_phone_register;

    //修改头像的view
    private View view1;

    private Button btn_tuku, btn_camera, btn_quxiao;

    private PopupWindow window;

    private static final int ALBUM_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private static final int CROP_REQUEST_CODE = 4;
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static final String TAG = "RegisterInfoActivity";
    private Uri uritempFile;

    private Bitmap photo;

    private CircleImageView iv_member_head_img;

    private EditText et_new_pwd,et_phone,et_nickname,et_yanzhengma,et_new_pay_pwd;

    private int count = 120;

    private String nickname = "";
    private String pic = "";

    private YWIMKit mIMKit;

    private ImageView checkbox_agreement;

    /**
     * 同意协议的变量值
     * @param savedInstanceState
     *
     */
    private boolean agreementIsChecked = false;
    private TextView tv_into_agreement;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0x111){
                count--;
                if (count<=0){
                    btn_yanzhengma.setText("获取短信验证码   ");
                    btn_yanzhengma.setClickable(true);
                    count = 120;
                    handler.removeCallbacks(thread);
                }else{
                    btn_yanzhengma.setText("验证码已发送 （"+count+"s）   ");
                    btn_yanzhengma.setClickable(false);
                    postDelayed(thread,1000);
                }
            }
        }
    };
    Thread thread = new Thread(){
        @Override
        public void run(){
            handler.sendEmptyMessage(0x111);
        }
    };


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_back:
                    finish();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LoginMainActivity.myProgressDialog1.cancel();
                        }
                    });

                    break;
                case R.id.iv_pwd_isShow:
                    if(et_new_pwd.getTransformationMethod()== PasswordTransformationMethod.getInstance())
                    {
                        //显示
                        et_new_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        iv_pwd_isShow.setImageResource(R.mipmap.iv_pay_pwd_show);
                    }else
                    {
                        et_new_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        iv_pwd_isShow.setImageResource(R.mipmap.iv_pwd_no_show);
                    }
                    break;
                case R.id.iv_pay_pwd_isShow:
                    if(et_new_pay_pwd.getTransformationMethod()== PasswordTransformationMethod.getInstance())
                    {
                        //显示
                        et_new_pay_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        iv_pay_pwd_isShow.setImageResource(R.mipmap.iv_pay_pwd_show);
                    }else
                    {
                        et_new_pay_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        iv_pay_pwd_isShow.setImageResource(R.mipmap.iv_pwd_no_show);
                    }
                    break;
                case R.id.linearLayout_into_picture:
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
                    window.showAtLocation(findViewById(R.id.title_back),
                            Gravity.BOTTOM, 0, 0);
                    break;
                case R.id.btn_yanzhengma:
                    onValidateCode();
                    break;
                case R.id.btn_phone_register:
                    if(agreementIsChecked)
                    {
                    btn_phone_register.setClickable(false);
//                    String digits = "[/\\\\\\\"\\n\\t]";
//                    Pattern pattern = Pattern.compile(digits);
//                    Matcher matcher = pattern.matcher(et_nickname.getText().toString().trim());
//                    if(photo==null&&pic==null)
//                    {
//                        sToast("请为用户设置一个默认头像吧！");
//                        btn_phone_register.setClickable(true);
//                        return;
//                    }if(matcher.find())
//                    {
//                        sToast("昵称包含非法字符，请重新输入");
//                        btn_phone_register.setClickable(true);
//                        return;
//                    }else
                    if(TextUtils.isEmpty(et_phone.getText().toString().trim()))
                    {
                        sToast("请填写手机号");
                        btn_phone_register.setClickable(true);
                        return;
                    }else if(et_phone.getText().toString().trim().length()!=11)
                    {
                        sToast("请正确填写手机号");
                        btn_phone_register.setClickable(true);
                        return;
                    }else if(TextUtils.isEmpty(et_yanzhengma.getText().toString().trim()))
                    {
                        sToast("请填写短信验证码");
                        btn_phone_register.setClickable(true);
                        return;
                    }else if(TextUtils.isEmpty(et_new_pwd.getText().toString().trim()))
                    {
                        sToast("请填写登录密码");
                        btn_phone_register.setClickable(true);
                        return;
                    }else if(et_new_pwd.getText().toString().trim().length()!=6)
                    {
                        sToast("请填写6位数的登录密码");
                        btn_phone_register.setClickable(true);
                        return;
                    }else   //允许修改
                    {
                        memberRegister();
                    }
                    }else
                    {
                        sToast("请查看宝粉协议");
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_phone_register);
        myProgressDialog.setTitle("正在登录...");
        init();
        setUpView();
    }


    private void init() {
        checkbox_agreement = (ImageView) this.findViewById(R.id.checkbox_agreement);

        agreementIsChecked = true;
        checkbox_agreement.setImageResource(R.mipmap.iv_agreement_checked);
        tv_into_agreement = (TextView) this.findViewById(R.id.tv_into_agreement);
        tv_into_agreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PhoneRegisterActivity.this,LoginXieyiActivity.class));
            }
        });

        checkbox_agreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(agreementIsChecked==false)
                {
                    agreementIsChecked = true;
                    checkbox_agreement.setImageResource(R.mipmap.iv_agreement_checked);
                }else
                {
                    agreementIsChecked = false;
                    checkbox_agreement.setImageResource(R.mipmap.iv_agreement_no_checked);
                }
            }
        });

        ivBack = (ImageView) this.findViewById(R.id.title_back);
        iv_pwd_isShow = (ImageView) this.findViewById(R.id.iv_pwd_isShow);
        iv_pay_pwd_isShow = (ImageView) this.findViewById(R.id.iv_pay_pwd_isShow);
        linearLayout_into_picture = (LinearLayout) this.findViewById(R.id.linearLayout_into_picture);
        btn_yanzhengma = (Button) this.findViewById(R.id.btn_yanzhengma);
        btn_phone_register = (Button) this.findViewById(R.id.btn_phone_register);
        iv_member_head_img = (CircleImageView) this.findViewById(R.id.iv_member_head_img);
        et_new_pwd = (EditText) this.findViewById(R.id.et_new_pwd);
        et_phone = (EditText) this.findViewById(R.id.et_phone);
        et_nickname = (EditText) this.findViewById(R.id.et_nickname);
        et_yanzhengma = (EditText) this.findViewById(R.id.et_yanzhengma);
        et_new_pay_pwd = (EditText) this.findViewById(R.id.et_new_pay_pwd);

        //qq微信登录的时候把头像跟昵称获取过来默认设置 用户也可以修改
        if(null!=getIntent().getStringExtra("nickname"))
        {
            nickname = getIntent().getStringExtra("nickname");
        }
        if(null!=nickname)
        {
             et_nickname.setText(nickname);
        }

        if(null!= getIntent().getStringExtra("pic"))
        {
            pic = getIntent().getStringExtra("pic");
        }

        if(null!=pic)
        {
            if (pic!=null&&!pic.equals("")){
                Glide.with(PhoneRegisterActivity.this).load(pic).asBitmap().centerCrop().error(R.mipmap.iv_member_default_head_img).into(new BitmapImageViewTarget(iv_member_head_img) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        iv_member_head_img.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }
        }

        view1 = View.inflate(this, R.layout.photo_popupwindow_item, null);
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
                if (FileUtils.checkSDCard()) {
                    window.dismiss();
                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            IMAGE_UNSPECIFIED);
                    startActivityForResult(intent, ALBUM_REQUEST_CODE);
                } else {
                    PhoneRegisterActivity.this.sToast("请先插入SD卡");
                }
            }
        });
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FileUtils.checkSDCard()) {
                    window.dismiss();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                            Environment.getExternalStorageDirectory(), "temp.jpg")));
                    startActivityForResult(intent, CAMERA_REQUEST_CODE);
                } else {
                    PhoneRegisterActivity.this.sToast("请先插入SD卡");
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
        ivBack.setOnClickListener(onClickListener);
        iv_pwd_isShow.setOnClickListener(onClickListener);
        iv_pay_pwd_isShow.setOnClickListener(onClickListener);
        linearLayout_into_picture.setOnClickListener(onClickListener);
        btn_yanzhengma.setOnClickListener(onClickListener);
        btn_phone_register.setOnClickListener(onClickListener);
    }


    @Override
    public void initData() {

    }

    public void memberRegister()
    {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myProgressDialog.show();
            }
        });

            Map<String, String> par = new HashMap<String, String>();
            if(!TextUtils.isEmpty(getIntent().getStringExtra("type")))
            {
                par.put("type",getIntent().getStringExtra("type") );
            }

            if(!TextUtils.isEmpty(getIntent().getStringExtra("type")))
            {
                if(getIntent().getStringExtra("type").equals("qq")||getIntent().getStringExtra("type").equals("wechat"))
                {
                    if(!TextUtils.isEmpty(getIntent().getStringExtra("openid")))
                    {
                        par.put("code", getIntent().getStringExtra("openid"));
                    }
                }else if(getIntent().getStringExtra("type").equals("mobile"))
                {
                    par.put("code", et_phone.getText().toString().trim());
                }
            }

            if (photo == null) {
                par.put("pic", pic);
            } else {
                par.put("upfile:pic", BitmapUtil.bitmapToBase64(photo));
            }

           if(nickname.equals(""))
           {
               par.put("nickname", "无名英雄");
           }else
           {
               par.put("nickname", nickname);
           }

            par.put("password", et_new_pwd.getText().toString().trim());

            par.put("mobile", et_phone.getText().toString().trim());

            par.put("sms", et_yanzhengma.getText().toString().trim());
            par.put("from", "0");

            par.put("uuid", GuideActivity.PHONE_ID);
            par.put("geographic", GuideActivity.LONGITUDE+","+GuideActivity.LATITUDE );
            par.put("geographicBac",GuideActivity.ADDRESS );
            par.put("browserSystem", GuideActivity.PHONE_TYPE);


            HttpConnect.post(((IContext) PhoneRegisterActivity.this), "member_register_v3", par, new Callback() {

                @Override
                public void onResponse(Response arg0) throws IOException {
                    final JSONObject data = JSONObject.fromObject(arg0.body().string());
                    if (data.get("status").equals("success")) {

                        final String token = data.getJSONArray("data").getJSONObject(0).optString("token");
                        LoginUser.getInstantiation(getApplicationContext()).setToken(token);

                        HttpConnect.post(((IContext) PhoneRegisterActivity.this), "member_info", null, new Callback() {

                            @Override
                            public void onResponse(Response arg0) throws IOException {
                                final JSONObject data = JSONObject.fromObject(arg0.body().string());
                                if (data.get("status").equals("success")) {
                                    final String id = data.optJSONArray("data").optJSONObject(0).optString("ID");
                                    final String birthday = data.optJSONArray("data").optJSONObject(0).optString("birthday");
                                    final String pic = data.optJSONArray("data").optJSONObject(0).optString("pic");
                                    final String nickname = data.optJSONArray("data").optJSONObject(0).optString("nickname");
                                    final String type = data.optJSONArray("data").optJSONObject(0).optString("type");
                                    final String linkcode = data.optJSONArray("data").optJSONObject(0).optString("linkcode");
                                    final String aliPassword = data.optJSONArray("data").getJSONObject(0).getString("alipassword");
                                    final String gesturepassword = data.optJSONArray("data").optJSONObject(0).optString("gesturespassword");
                                    final String moblie = data.optJSONArray("data").optJSONObject(0).optString("mobile");
                                    final String opengestures = data.optJSONArray("data").optJSONObject(0).optString("opengestures");
                                    final String name = data.optJSONArray("data").optJSONObject(0).optString("name");
                                    final String sex = data.optJSONArray("data").optJSONObject(0).optString("sex");


                                    mIMKit = YWAPI.getIMKitInstance(linkcode, MyApplication.APP_KEY);
                                    IYWLoginService loginService = mIMKit.getLoginService();
                                    YWLoginParam loginParam = YWLoginParam.createLoginParam(linkcode, aliPassword);

                                    loginService.login(loginParam, new IWxCallback() {

                                        @Override
                                        public void onSuccess(Object... arg0) {
                                            if(getIntent().getStringExtra("type").equals("mobile"))
                                            {
                                                getSharedPreferences("login_mode",Context.MODE_PRIVATE).edit().putString("type","phoneLogin").putString("username",et_phone.getText().toString().trim())
                                                        .putString("password",et_new_pwd.getText().toString().trim()).commit();
                                            }else if(getIntent().getStringExtra("type").equals("wechat"))
                                            {
                                                getSharedPreferences("login_mode",Context.MODE_PRIVATE).edit().putString("type","wxLogin").putString("openId",getIntent().getStringExtra("openid")).commit();
                                            }else if(getIntent().getStringExtra("type").equals("qq"))
                                            {
                                                getSharedPreferences("login_mode",Context.MODE_PRIVATE).edit().putString("type","qqLogin").putString("openId",getIntent().getStringExtra("openid")).commit();
                                            }

                                            SharedPreferences sharedPreferences = getSharedPreferences("isExitGesturesPassword",Context.MODE_PRIVATE);
                                            sharedPreferences.edit().putString("gesturepassword",gesturepassword).putString("opengestures",opengestures).commit();

                                            SharedPreferences sharedPreferences2 = getSharedPreferences("isExitsName",Context.MODE_PRIVATE);
                                            sharedPreferences2.edit().putString("name",name).commit();

                                            LoginUser.getInstantiation(getApplicationContext()).login(id,pic,nickname,moblie,type,LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getToken(),linkcode,sex);

                                            MyApplication.deleteActivity("LoginMainActivity");
                                            startActivity(new Intent(PhoneRegisterActivity.this,MainActivity.class));
                                            finish();
                                        }

                                        @Override
                                        public void onProgress(int arg0) {
                                            // TODO Auto-generated method stub
                                        }

                                        @Override
                                        public void onError(final int errCode, final String description) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    btn_phone_register.setClickable(true);
                                                    ((IContext) PhoneRegisterActivity.this).sToast(description);
                                                    myProgressDialog.cancel();
                                                }
                                            });
                                        }
                                    });


                                }else
                                {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            btn_phone_register.setClickable(true);
                                            if(data.getString("msg")!=null)
                                            {
                                                boolean flag = Tools.isPhonticName(data.getString("msg"));
                                                if(!flag)
                                                {
                                                    sToast(data.getString("msg"));
                                                }
                                            }
                                            myProgressDialog.cancel();
                                        }
                                    });
                                }
                            }

                            @Override
                            public  void onFailure(Request arg0, IOException arg1) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        btn_phone_register.setClickable(true);
                                        myProgressDialog.cancel();
//                                        sToast("请检查您的网络链接状态！");
                                    }
                                });
                            }
                        });
                    }else
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btn_phone_register.setClickable(true);
                                myProgressDialog.cancel();
                                if(data.getString("msg")!=null)
                                {
                                    boolean flag = Tools.isPhonticName(data.getString("msg"));
                                    if(!flag)
                                    {
                                        sToast(data.getString("msg"));
                                    }
                                }
                            }
                        });
                    }
                }

                @Override
                public  void onFailure(Request arg0, IOException arg1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btn_phone_register.setClickable(true);
                            myProgressDialog.cancel();
//                            sToast("请检查您的网络链接状态！");
                        }
                    });
                }
            });
    }


    public void onValidateCode() {
        if(TextUtils.isEmpty(et_phone.getText().toString().trim()))
        {
            sToast("请输入手机号");
            return;
        }
        if(et_phone.getText().toString().trim().length()!=11)
        {
            sToast("手机号输入不合法");
            return;
        }
//        gif_loading.setVisibility(View.VISIBLE);
        String phone = ((EditText) findViewById(R.id.et_phone)).getText().toString();
        if (StringUtils.isNotBlank(phone) && NumberUtils.isNumber(phone) && phone.length() == 11) {
            Map<String, String> par = new HashMap<String, String>();
            par.put("mobile", phone);
            par.put("code","member_register_v3");
            HttpConnect.post(this, "sms_member_code", par, new Callback() {

                @Override
                public void onResponse(Response arg0) throws IOException {
                    JSONObject data = JSONObject.fromObject(arg0.body().string());
                    if (data.get("status").equals("success")) {
                        handler.post(thread);
                    } else {
                        sToast(data.getString("msg"));
                    }
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            gif_loading.setVisibility(View.GONE);
//                        }
//                    });
                }

                @Override
                public void onFailure(Request arg0, IOException arg1) {
//                    sToast("链接超时！");
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            gif_loading.setVisibility(View.GONE);
//                        }
//                    });
                }
            });
        } else {
            sToast("请填入正确的手机号!");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK)
            return;

        switch (requestCode) {
            case ALBUM_REQUEST_CODE:
                Log.i(TAG, "相册，开始裁剪");
                Log.i(TAG, "相册 [ " + data + " ]");
                if (data == null) {
                    return;
                }
                startPhotoZoom(data.getData(), 100);
                break;
            case CAMERA_REQUEST_CODE:
                Log.i(TAG, "相机, 开始裁剪");
                File picture = new File(Environment.getExternalStorageDirectory()
                        + "/temp.jpg");
                startPhotoZoom(Uri.fromFile(picture), 100);
                break;
            case CROP_REQUEST_CODE:
                Log.i(TAG, "相册裁剪成功");
                Log.i(TAG, "裁剪以后 [ " + data + " ]");
                // 将Uri图片转换为Bitmap
                try {
                    photo = BitmapFactory.decodeStream(getContentResolver()
                            .openInputStream(uritempFile));
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                iv_member_head_img.setImageBitmap(photo);
                break;
        }
    }

        /**
         * 裁剪图片
         */

    private void startPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);

        /**
         * 此方法返回的图片只能是小图片（sumsang测试为高宽160px的图片）
         * 故将图片保存在Uri中，调用时将Uri转换为Bitmap，此方法还可解决miui系统不能return data的问题
         */
        // intent.putExtra("return-data", true);

        // uritempFile为Uri类变量，实例化uritempFile
        uritempFile = Uri.parse("file://" + "/"
                + Environment.getExternalStorageDirectory().getPath() + "/"
                + "small.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        startActivityForResult(intent, CROP_REQUEST_CODE);
    }


    /**
     * 返回键监听
     */
    @Override
    public void onBackPressed() {
        finish();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LoginMainActivity.myProgressDialog1.cancel();
            }
        });
    }
}