package com.tohier.cartercoin.activity;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
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
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.tohier.android.util.BitmapUtil;
import com.tohier.android.util.FileUtils;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;

import net.sf.json.JSONObject;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Administrator on 2016/12/30.
 */

public class UpdateNewMemberInfoActivity extends MyBackBaseActivity {

    private ImageView ivBack,iv_member_mark;
    private CircleImageView iv_member_head_img;
    private TextView tv_nickname,tv_info_perfect_degree,tv_sex,tv_wx_num,tv_member_level,tv_member_due_time,tv_linkcode;
    private ProgressBar progressBar_info_perfect_degree;
    //微信绑定的LinearLayout
    private LinearLayout linearlayout_into_update_member_bandwx;

//    //qq绑定的LinearLayout
//    private LinearLayout linearlayout_into_update_member_bandqq;

    //进入安全中心的LinearLayout
    private LinearLayout linearlayout_into_securitycenter;

    //进入收货地址的LinearLayout
    private LinearLayout linearlayout_into_member_address;

    //进入修改昵称的LinearLayout
    private LinearLayout linearlayout_into_update_member_nickname;

    //进入修改头像的LinearLayout
    private LinearLayout linearlayout_into_update_member_img;

    //进入修改性别的LinearLayout
    private LinearLayout linearlayout_into_update_member_sex;

    //进入会员升级的LinearLayout
    private LinearLayout linearLayout_into_member_upgrade;

    //进入会员升级的LinearLayout
    private LinearLayout linearLayout_copy_linkcode;

    //微信
    private static final String WX_APP_ID = "wx7ad749f6cba84064";
    public IWXAPI api;

    //qq
    private static final String QQ_APP_ID = "1105547483";
    public Tencent mTencent;

    private String QQopenID;

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

    private GifImageView gif_loading;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.iv_back2:
                     finish();
                   break;
                case R.id.linearlayout_into_update_member_bandwx:
                    getSharedPreferences("weixinlogin", Context.MODE_APPEND).edit().putString("login","bandphone").commit();

                    api = WXAPIFactory.createWXAPI(getApplicationContext(), WX_APP_ID, true);
                    api.registerApp(WX_APP_ID);
                    final SendAuth.Req req = new SendAuth.Req();
                    req.scope = "snsapi_userinfo";
                    req.state = "wechat_sdk_demo_test";
                    if (!api.sendReq(req)) {
                        sToast("请您安装微信");
                    }
                    break;

//                case R.id.linearlayout_into_update_member_bandqq:
//                    mTencent = Tencent.createInstance(QQ_APP_ID, getApplicationContext());
//                    if (!mTencent.isSessionValid()) {
//                        mTencent.login(UpdateNewMemberInfoActivity.this, "get_simple_userinfo", listener);
//                    } else {
//                        mTencent.logout(getApplicationContext());
//                        mTencent.login(UpdateNewMemberInfoActivity.this, "get_simple_userinfo", listener);
//                    }
//                    break;
                case R.id.linearlayout_into_securitycenter:
                         startActivity(new Intent(UpdateNewMemberInfoActivity.this,SecurityCenterActivity.class));
                    break;
                case R.id.linearlayout_into_member_address:
                         startActivity(new Intent(UpdateNewMemberInfoActivity.this,ShouHuoAddressActivity.class));
                    break;

                case R.id.linearlayout_into_update_member_nickname:
                         startActivity(new Intent(UpdateNewMemberInfoActivity.this,UpdateNickNameActivity.class));
                    break;

                case R.id.linearlayout_into_update_member_sex:
                    startActivity(new Intent(UpdateNewMemberInfoActivity.this,UpdateSexActivity.class));
                    break;

                case R.id.linearLayout_into_member_upgrade:
//                    startActivity(new Intent(UpdateNewMemberInfoActivity.this,VipUpgradeActivity.class));
                    break;

                case R.id.linearLayout_copy_linkcode:
                    ClipboardManager cmb = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                    cmb.setText(tv_linkcode.getText().toString().trim());
                    sToast("已复制");
                    break;

                case R.id.linearlayout_into_update_member_img:
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
                    window.showAtLocation(findViewById(R.id.iv_back2),
                            Gravity.BOTTOM, 0, 0);
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_new_member_info_layout);

        initData();
        loadMemberInfo();
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
                if (FileUtils.checkSDCard()){
                    window.dismiss();
                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            IMAGE_UNSPECIFIED);
                    startActivityForResult(intent, ALBUM_REQUEST_CODE);
                }else{
                    UpdateNewMemberInfoActivity.this.sToast("请先插入SD卡");
                }
            }
        });
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FileUtils.checkSDCard()){
                    window.dismiss();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                            Environment.getExternalStorageDirectory(), "temp.jpg")));
                    startActivityForResult(intent, CAMERA_REQUEST_CODE);
                }else{
                    UpdateNewMemberInfoActivity.this.sToast("请先插入SD卡");
                }
            }
        });

        btn_quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FileUtils.checkSDCard()){
                    window.dismiss();
                }
            }
        });

        //返回按钮
        ivBack = (ImageView) this.findViewById(R.id.iv_back2);
        iv_member_mark = (ImageView) this.findViewById(R.id.iv_member_mark);
        //头像
        iv_member_head_img = (CircleImageView) this.findViewById(R.id.iv_member_head_img);
        //昵称
        tv_nickname = (TextView) this.findViewById(R.id.tv_nickname);
        tv_member_level = (TextView) this.findViewById(R.id.tv_member_level);
        tv_member_due_time = (TextView) this.findViewById(R.id.tv_member_due_time);
        //完成度百分比
        tv_info_perfect_degree = (TextView) this.findViewById(R.id.tv_info_perfect_degree);
        //链接码
        tv_linkcode = (TextView) this.findViewById(R.id.tv_linkcode);
        //性别
        tv_sex = (TextView) this.findViewById(R.id.tv_sex);
        //显示微信是否绑定
        tv_wx_num = (TextView) this.findViewById(R.id.tv_wx_num);
        //显示qq是否绑定
//        tv_qq_num = (TextView) this.findViewById(R.id.tv_qq_num);
        //信息完善度进度条
        progressBar_info_perfect_degree = (ProgressBar) this.findViewById(R.id.progressBar_info_perfect_degree);

        linearlayout_into_update_member_bandwx = (LinearLayout) this.findViewById(R.id.linearlayout_into_update_member_bandwx);
//        linearlayout_into_update_member_bandqq = (LinearLayout) this.findViewById(R.id.linearlayout_into_update_member_bandqq);
        linearlayout_into_securitycenter = (LinearLayout) this.findViewById(R.id.linearlayout_into_securitycenter);
        linearlayout_into_member_address = (LinearLayout) this.findViewById(R.id.linearlayout_into_member_address);
        linearlayout_into_update_member_nickname = (LinearLayout) this.findViewById(R.id.linearlayout_into_update_member_nickname);
        linearlayout_into_update_member_img = (LinearLayout) this.findViewById(R.id.linearlayout_into_update_member_img);
        linearlayout_into_update_member_sex = (LinearLayout) this.findViewById(R.id.linearlayout_into_update_member_sex);
        linearLayout_into_member_upgrade = (LinearLayout) this.findViewById(R.id.linearLayout_into_member_upgrade);
        linearLayout_copy_linkcode = (LinearLayout) this.findViewById(R.id.linearLayout_copy_linkcode);
        gif_loading = (GifImageView) this.findViewById(R.id.gif_loading);
    }

    private void setUpView()
    {
        ivBack.setOnClickListener(onClickListener);
        linearlayout_into_update_member_bandwx.setOnClickListener(onClickListener);
//        linearlayout_into_update_member_bandqq.setOnClickListener(onClickListener);
        linearlayout_into_securitycenter.setOnClickListener(onClickListener);
        linearlayout_into_member_address.setOnClickListener(onClickListener);
        linearlayout_into_update_member_nickname.setOnClickListener(onClickListener);
        linearlayout_into_update_member_img.setOnClickListener(onClickListener);
        linearlayout_into_update_member_sex.setOnClickListener(onClickListener);
        linearLayout_into_member_upgrade.setOnClickListener(onClickListener);
        linearLayout_copy_linkcode.setOnClickListener(onClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMemberInfo();
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


    /**
     * 获取会员的信息
     */
    public void loadMemberInfo()
    {
        HttpConnect.post(UpdateNewMemberInfoActivity.this, "member_info", null, new Callback() {

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

                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {

                            loadInfoComplete();
                            /**
                             * 设置头像  以及昵称
                             */
                            if (pic!=null&&!pic.equals("")){
                                Glide.with(UpdateNewMemberInfoActivity.this).load(pic).asBitmap().centerCrop().into(new BitmapImageViewTarget(iv_member_head_img) {
                                    @Override
                                    protected void setResource(Bitmap resource) {
                                        RoundedBitmapDrawable circularBitmapDrawable =
                                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                                        circularBitmapDrawable.setCircular(true);
                                        iv_member_head_img.setImageDrawable(circularBitmapDrawable);
                                    }
                                });
                            }else
                            {
                                iv_member_head_img.setImageResource(R.mipmap.iv_member_default_head_img);
                            }

                            if(!TextUtils.isEmpty(nickname))
                            {
                                tv_nickname.setText(nickname);
                            }

                            if(!TextUtils.isEmpty(linkcode))
                            {
                                tv_linkcode.setText(linkcode);
                            }

                            if(!TextUtils.isEmpty(sex))
                            {
                                if(sex.equals("保密"))
                                {
                                    tv_sex.setText("保密");
                                }else if(sex.equals("美女"))
                                {
                                    tv_sex.setText("美女");
                                }else if(sex.equals("帅哥"))
                                {
                                    tv_sex.setText("帅哥");
                                }
                            }

                            if(!TextUtils.isEmpty(wechatopenid))
                            {
                                tv_wx_num.setText("已绑定");
                                linearlayout_into_update_member_bandwx.setClickable(false);
                            }else
                            {
                                tv_wx_num.setText("未绑定");
                            }

//                            if(!TextUtils.isEmpty(qqopenid))
//                            {
//                                tv_qq_num.setText("已绑定");
////                                linearlayout_into_update_member_bandqq.setClickable(false);
//                            }else
//                            {
//                                tv_qq_num.setText("未绑定");
//                            }

                            if(!TextUtils.isEmpty(type))
                            {
                                if(type.equals("10"))
                                {
                                    tv_member_level.setText("注册会员");
                                    iv_member_mark.setImageResource(R.mipmap.iv_putong_memeber);
                                }else if(type.equals("20"))
                                {
                                    tv_member_level.setText("VIP铂金会员");
                                    iv_member_mark.setImageResource(R.mipmap.iv_baijin_member);
                                }else if(type.equals("30"))
                                {
                                    tv_member_level.setText("VIP钻石会员");
                                    iv_member_mark.setImageResource(R.mipmap.iv_zhuanshi_member);
                                }else if(type.equals("40"))
                                {
                                    tv_member_level.setText("VIP皇冠会员");
                                    iv_member_mark.setImageResource(R.mipmap.iv_huangguan_member);
                                }
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
                            gif_loading.setVisibility(View.GONE);
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

    /**
     * 获取是否完善信息 具体到每一项
     */
    private void loadInfoComplete()
    {
        HttpConnect.post(this, "member_information_complete", null, new Callback() {

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                Handler dataHandler = new Handler(getContext()
                        .getMainLooper()) {

                    @Override
                    public void handleMessage(final Message msg) {
//                        sToast("链接超时");
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject object = JSONObject.fromObject(arg0.body().string());
                final String message = object.getString("msg");
                if (object.get("status").equals("success")) {
                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {

                            loadMemberDueTime();
//                            String sex = object.getJSONArray("data").getJSONObject(0).getString("sex");
//                            String name = object.getJSONArray("data").getJSONObject(0).getString("name");
//                            String brithday = object.getJSONArray("data").getJSONObject(0).getString("brithday");
//                            String idnumber = object.getJSONArray("data").getJSONObject(0).getString("idnumber");
//                            String mobile = object.getJSONArray("data").getJSONObject(0).getString("mobile");
//                            String wechatid = object.getJSONArray("data").getJSONObject(0).getString("wechatid");
//                            String qqid = object.getJSONArray("data").getJSONObject(0).getString("qqid");
//                            String bankcount = object.getJSONArray("data").getJSONObject(0).getString("bankcount");
                            String perfect = object.getJSONArray("data").getJSONObject(0).getString("perfect");

                            if(!TextUtils.isEmpty(perfect))
                            {
                                progressBar_info_perfect_degree.setProgress((int) (Float.parseFloat(perfect)*100));
                                tv_info_perfect_degree.setText(removeDecimalPoint(String.valueOf(Float.parseFloat(perfect)*100))+"%");
                            }
                        }
                    };
                    dataHandler.sendEmptyMessage(0);


                }else{
                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {
                            gif_loading.setVisibility(View.GONE);
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }
            }
        });
    }


    private IUiListener listener = new IUiListener() {

        @Override
        public void onCancel() {
            sToast("您取消了QQ登录");
        }

        @Override
        public void onComplete(Object arg0) {
            sToast("进入授权");
            org.json.JSONObject jo = (org.json.JSONObject) arg0;

            initLoginID(jo);
        }

        @Override
        public void onError(UiError arg0) {
            sToast("QQ登录出错");
        }
    };

    private void initLoginID(org.json.JSONObject jsonObject) {
        try {
            if (jsonObject.getString("ret").equals("0")) {
                String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
                String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
                QQopenID = jsonObject.getString(Constants.PARAM_OPEN_ID);
                mTencent.setOpenId(QQopenID);
                mTencent.setAccessToken(token, expires);
                getuserInfo();
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    private void getuserInfo() {
        UserInfo qqInfo = new UserInfo(this, mTencent.getQQToken());
        qqInfo.getUserInfo(getQQinfoListener);
    }

    private IUiListener getQQinfoListener = new IUiListener() {
        @Override
        public void onComplete(Object response) {
            try {
                org.json.JSONObject jsonObject = (org.json.JSONObject) response;

                int ret = jsonObject.getInt("ret");

                String headImgUrl = jsonObject.getString("figureurl_qq_2");
                String nickname = jsonObject.getString("nickname");

                Map<String, String> par1 = new HashMap<String, String>();
                par1.put("id", LoginUser.getInstantiation(getApplicationContext())
                        .getLoginUser().getUserId());
                par1.put("openid", QQopenID);

                HttpConnect.post(UpdateNewMemberInfoActivity.this, "member_modify_qq", par1, new Callback() {

                    @Override
                    public void onFailure(Request arg0, IOException arg1) {
                        Handler dataHandler = new Handler(
                                getContext().getMainLooper()) {
                            @Override
                            public void handleMessage(
                                    final Message msg)
                            {
//                                sToast("请检查你的网络状态");
                            }
                        };
                        dataHandler.sendEmptyMessage(0);
                    }

                    @Override
                    public void onResponse(Response arg0) throws IOException {
                        JSONObject object = JSONObject.fromObject(arg0.body().string());
                        if (object.get("status").equals("success")) {
                            Handler dataHandler = new Handler(
                                    getContext().getMainLooper()) {
                                @Override
                                public void handleMessage(
                                        final Message msg)
                                {
                                    sToast("QQ已绑定成功");
//                                    tv_qq_num.setText("已绑定");
//                                    linearlayout_into_update_member_bandqq.setClickable(false);
                                }
                            };
                            dataHandler.sendEmptyMessage(0);
                        }else
                        {
                            Handler dataHandler = new Handler(
                                    getContext().getMainLooper()) {
                                @Override
                                public void handleMessage(
                                        final Message msg)
                                {
                                    sToast("该QQ已有用户绑定");
                                }
                            };
                            dataHandler.sendEmptyMessage(0);
                        }
                    }
                });
                //处理自己需要的信息
            } catch (Exception e) {

            }
        }

        @Override
        public void onError(UiError uiError) {

        }

        @Override
        public void onCancel() {

        }
    };


    /**
     * 百分比可能会有小数 此方法去除小数
     * @param str
     * @return
     */
    public String  removeDecimalPoint(String str)
    {
        if(str.contains("."))
        {
            int count = str.indexOf(".");
            return str.substring(0,count);
        }
        return str;
    }

    /**
     * 百分比可能会有小数 此方法去除空格
     * @param str
     * @return
     */
    public String  remove(String str)
    {
        if(str.contains(" "))
        {
            int count = str.indexOf(" ");
            return str.substring(0,count);
        }
        return str;
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
     * 照相必备1
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_LOGIN || requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode, resultCode, data, listener);
            return;
        }

        if (resultCode != Activity.RESULT_OK)
            return;

        switch (requestCode) {
            case ALBUM_REQUEST_CODE:
                Log.i(TAG, "相册，开始裁剪");
                Log.i(TAG, "相册 [ " + data + " ]");
                if (data == null) {
                    return;
                }
                startPhotoZoom(data.getData(), 300);
                break;
            case CAMERA_REQUEST_CODE:
                Log.i(TAG, "相机, 开始裁剪");
                File picture = new File(Environment.getExternalStorageDirectory()
                        + "/temp.jpg");
                startPhotoZoom(Uri.fromFile(picture), 300);
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
                updateMemberInfo();
                break;
        }

    }

    public void updateMemberInfo()
    {
        gif_loading.setVisibility(View.VISIBLE);
        Map<String, String> par1 = new HashMap<String, String>();
        if (photo == null) {
            par1.put("pic", pic);
        } else {
            par1.put("upfile:pic", BitmapUtil.bitmapToBase64(photo));
        }
        par1.put("nickname", nickname);
        par1.put("name", name);
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
        par1.put("idnumber", idnumber);
        par1.put("phonenunber",mobile );

        HttpConnect.post(this, "member_update_information", par1, new Callback() {

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                Handler dataHandler = new Handler(getContext()
                        .getMainLooper()) {

                    @Override
                    public void handleMessage(final Message msg) {
                        sToast("链接超时");
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject object = JSONObject.fromObject(arg0.body().string());
                if (object.get("status").equals("success")) {
                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {
                            gif_loading.setVisibility(View.GONE);
                            UpdateNewMemberInfoActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (photo == null) {
                                        Glide.with(UpdateNewMemberInfoActivity.this).load(pic).asBitmap().centerCrop().into(new BitmapImageViewTarget(iv_member_head_img) {
                                            @Override
                                            protected void setResource(Bitmap resource) {
                                                RoundedBitmapDrawable circularBitmapDrawable =
                                                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                                                circularBitmapDrawable.setCircular(true);
                                                iv_member_head_img.setImageDrawable(circularBitmapDrawable);
                                            }
                                        });
                                    } else {
                                        iv_member_head_img.setImageBitmap(photo);
                                    }
                                    sToast("修改成功");
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
                            gif_loading.setVisibility(View.GONE);
                            sToast("修改失败");
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }
            }
        });
    }



    /**
     * 获取会员到期时间
     */
    private void loadMemberDueTime()
    {
        HttpConnect.post(this, "member_upgrade_endtime", null, new Callback() {

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                Handler dataHandler = new Handler(getContext()
                        .getMainLooper()) {

                    @Override
                    public void handleMessage(final Message msg) {
//                        sToast("链接超时");
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject object = JSONObject.fromObject(arg0.body().string());
                final String message = object.getString("msg");
                if (object.get("status").equals("success")) {
                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {
                            gif_loading.setVisibility(View.GONE);
                            String day = object.getJSONArray("data").getJSONObject(0).getString("day");

                            if(!type.equals("10"))
                            {
                                day = remove(day);
                                day = day.replaceAll("/","-");
                                tv_member_due_time.setText(day+" 到期");
                            }

//                            String name = object.getJSONArray("data").getJSONObject(0).getString("name");
//                            String brithday = object.getJSONArray("data").getJSONObject(0).getString("brithday");
//                            String idnumber = object.getJSONArray("data").getJSONObject(0).getString("idnumber");
//                            String mobile = object.getJSONArray("data").getJSONObject(0).getString("mobile");
//                            String wechatid = object.getJSONArray("data").getJSONObject(0).getString("wechatid");
//                            String qqid = object.getJSONArray("data").getJSONObject(0).getString("qqid");
//                            String bankcount = object.getJSONArray("data").getJSONObject(0).getString("bankcount");

                        }
                    };
                    dataHandler.sendEmptyMessage(0);


                }else{
                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {
                            gif_loading.setVisibility(View.GONE);
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }
            }
        });
    }


}
