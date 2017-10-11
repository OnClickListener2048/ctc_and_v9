package com.tohier.cartercoin.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.tohier.cartercoin.config.MyApplication;

import net.sf.json.JSONObject;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import util.PopBirthHelper;

/**
 * Created by Administrator on 2016/12/12.
 */

public class UpdateMemberInfoActivity extends MyBackBaseActivity {

    private View view1;
    private Button btn_tuku, btn_camera, btn_quxiao;
    private PopupWindow window;
    private Bitmap photo;
    private static final int ALBUM_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private static final int CROP_REQUEST_CODE = 4;
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static final String TAG = "RegisterInfoActivity";
    private Uri uritempFile;
    private int i = 0;

    private PopBirthHelper popBirthHelper;

    private CircleImageView memberHeadImg;
    private TextView tvNickName,tvSex,tvName,tvNumberID,tvBirthday,tvPhoneNum,tvWXNum,tvQQNum,tvComplete;
    private ImageView ivBack;
    private LinearLayout linearlayoutIntoMemberUpgrade,linearLayoutIntoAddBackCard,linearlayout_into_update_member_img,linearlayout_into_update_member_nickname,linearlayout_into_update_member_sex,
                           linearlayout_into_update_member_reaname,linearlayout_into_update_member_idnumber,linearlayout_into_update_member_birthday,linearlayout_into_update_member_bandphone,linearlayout_into_update_member_bandwx,linearlayout_into_update_member_bandqq;

    private static final String QQ_APP_ID = "1105547483";
    public Tencent mTencent;
    private String QQopenID;

    private static final String WX_APP_ID = "wx7ad749f6cba84064";
    public IWXAPI api;
    private ProgressBar progressBarInfoPerfectDegree;
    private TextView tvInfoPerfectDegree,tv_nickname_reward_ctc,tv_name_reward_ctc,tv_idnumber_reward_ctc,tv_birthday_reward_ctc,tv_mobile_reward_ctc,tv_wx_reward_ctc,tv_qq_reward_ctc,tv_backcard_reward_ctc,tv_bankcard;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.title_back:
                     finish();
                    break;
                case R.id.tv_complete:
                    updateMemberInfo();
                    break;
                case R.id.linearlayout_into_member_upgrade:
                    startActivity(new Intent(UpdateMemberInfoActivity.this,ShouHuoAddressActivity.class));
                    break;
                case R.id.linearLayout_into_add_backcard:
                    startActivity(new Intent(UpdateMemberInfoActivity.this,AddCardActivity.class));
                    break;
                case R.id.linearlayout_into_update_member_nickname:
                    View view2 = View.inflate(UpdateMemberInfoActivity.this,R.layout.nickname_popuwindow,null);
                    final PopupWindow popupWindow = new PopupWindow(view2,WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);


                    // 设置背景颜色变暗
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha = 0.7f;
                    getWindow().setAttributes(lp);
                    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                        @Override
                        public void onDismiss() {
                            WindowManager.LayoutParams lp = getWindow().getAttributes();
                            lp.alpha = 1f;
                            getWindow().setAttributes(lp);
                        }
                    });

                    popupWindow.setFocusable(true);

                    popupWindow.setOutsideTouchable(true);

                    // 实例化一个ColorDrawable颜色为半透明
                    ColorDrawable dw2 = new ColorDrawable(0x00ffffff);
                    popupWindow.setBackgroundDrawable(dw2);

                    popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

                    // 设置popWindow的显示和消失动画
//                    popupWindow.setAnimationStyle(R.style.Mypopwindow_anim_style);
                    popupWindow.setAnimationStyle(R.style.Mypopwindow_anim_style);

                    popupWindow.showAtLocation(linearlayout_into_update_member_nickname,Gravity.CENTER,0,0);
                    final EditText etNick = (EditText) view2.findViewById(R.id.et_name);
                    view2.findViewById(R.id.btn_commit).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String name = etNick.getText().toString();
                            if(!TextUtils.isEmpty(name))
                            {
                                nickname = name;
                                tvNickName.setText(name);
                                popupWindow.dismiss();
                            }else
                            {
                                sToast("昵称不能为空");
                            }
                        }
                    });
                    view2.findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                        }
                    });
                    break;
                case R.id.linearlayout_into_update_member_sex:
                    AlertDialog.Builder builder1= new AlertDialog.Builder(UpdateMemberInfoActivity.this,AlertDialog.THEME_HOLO_LIGHT);
                    builder1.setTitle("性别");
                    //定义列表中的选项
                    final String[] items = new String[]{
                            "保密",
                            "美女",
                            "帅哥"
                    };

                    //设置列表选项
                    builder1.setItems(items, new DialogInterface.OnClickListener() {
                        //点击任何一个列表选项都会触发这个方法
                        //arg1：点击的是哪一个选项
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case 0:
                                    tvSex.setText("保密");
                                    sex = "保密";
                                    break;
                                case 1:
                                    tvSex.setText("美女");
                                    sex = "美女";
                                    break;
                                case 2:
                                    tvSex.setText("帅哥");
                                    sex = "帅哥";
                                    break;
                            }
                        }
                    });
                    // 取消选择
                    builder1.setNegativeButton("取消", null);
                    builder1.show();
                    break;
                case R.id.linearlayout_into_update_member_reaname:

                    View view3 = View.inflate(UpdateMemberInfoActivity.this,R.layout.reanname_popuwindow,null);
                    final PopupWindow reanamePopupWindow = new PopupWindow(view3,WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
                    reanamePopupWindow.setFocusable(true);

                    // 设置背景颜色变暗
                    WindowManager.LayoutParams lp2 = getWindow().getAttributes();
                    lp2.alpha = 0.7f;
                    getWindow().setAttributes(lp2);
                    reanamePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                        @Override
                        public void onDismiss() {
                            WindowManager.LayoutParams lp3 = getWindow().getAttributes();
                            lp3.alpha = 1f;
                            getWindow().setAttributes(lp3);
                        }
                    });


                    reanamePopupWindow.setOutsideTouchable(true);

                    // 实例化一个ColorDrawable颜色为半透明
                    ColorDrawable dw3 = new ColorDrawable(0x00ffffff);
                    reanamePopupWindow.setBackgroundDrawable(dw3);

                    reanamePopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

                    // 设置popWindow的显示和消失动画
                    reanamePopupWindow.setAnimationStyle(R.style.Mypopwindow_anim_style);

                    reanamePopupWindow.showAtLocation(linearlayout_into_update_member_nickname,Gravity.CENTER,0,0);
                    final EditText reaname = (EditText) view3.findViewById(R.id.et_name);
                    view3.findViewById(R.id.btn_commit).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String name8 = reaname.getText().toString();
                            if(!TextUtils.isEmpty(name8))
                            {
                                name = name8;
                                tvName.setText(name8);
                                reanamePopupWindow.dismiss();
                            }else
                            {
                                sToast("姓名不能为空");
                            }
                        }
                    });
                    view3.findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            reanamePopupWindow.dismiss();
                        }
                    });

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
                    window.showAtLocation(findViewById(R.id.fenxiang_title),
                            Gravity.BOTTOM, 0, 0);
                    break;
                case R.id.linearlayout_into_update_member_birthday:
                    popBirthHelper.show(tvBirthday);
                    break;
                case R.id.linearlayout_into_update_member_idnumber:
                    View view4 = View.inflate(UpdateMemberInfoActivity.this,R.layout.idnumber_popuwindow,null);
                    final PopupWindow idNumberPopupWindow = new PopupWindow(view4,WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);

                    // 设置背景颜色变暗
                    WindowManager.LayoutParams lp4 = getWindow().getAttributes();
                    lp4.alpha = 0.7f;
                    getWindow().setAttributes(lp4);
                    idNumberPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                        @Override
                        public void onDismiss() {
                            WindowManager.LayoutParams lp3 = getWindow().getAttributes();
                            lp3.alpha = 1f;
                            getWindow().setAttributes(lp3);
                        }
                    });

                    idNumberPopupWindow.setFocusable(true);

                    idNumberPopupWindow.setOutsideTouchable(true);

                    // 实例化一个ColorDrawable颜色为半透明
                    ColorDrawable dw4 = new ColorDrawable(0x00ffffff);
                    idNumberPopupWindow.setBackgroundDrawable(dw4);

                    idNumberPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

                    // 设置popWindow的显示和消失动画
                    idNumberPopupWindow.setAnimationStyle(R.style.Mypopwindow_anim_style);

                    idNumberPopupWindow.showAtLocation(linearlayout_into_update_member_nickname,Gravity.CENTER,0,0);
                    final EditText etIdNumber = (EditText) view4.findViewById(R.id.et_name);
                    view4.findViewById(R.id.btn_commit).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String name = etIdNumber.getText().toString();
                            if(!TextUtils.isEmpty(name))
                            {
                                idnumber = name;
                                tvNumberID.setText(name);
                                idNumberPopupWindow.dismiss();
                            }else
                            {
                                sToast("身份证号不能为空");
                            }
                        }
                    });
                    view4.findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            idNumberPopupWindow.dismiss();
                        }
                    });

                    break;
                case R.id.linearlayout_into_update_member_bandqq:
                    mTencent = Tencent.createInstance(QQ_APP_ID, getApplicationContext());
                    if (!mTencent.isSessionValid()) {
                        mTencent.login(UpdateMemberInfoActivity.this, "get_simple_userinfo", listener);
                    } else {
                        mTencent.logout(getApplicationContext());
                        mTencent.login(UpdateMemberInfoActivity.this, "get_simple_userinfo", listener);
                    }
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
            }
        }
    };

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

                HttpConnect.post(UpdateMemberInfoActivity.this, "member_modify_qq", par1, new Callback() {

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
                                    tvQQNum.setText("已绑定");
                                    linearlayout_into_update_member_bandqq.setClickable(false);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_memberinfo_layout);

        MyApplication.maps.put("UpdateMemberInfoActivity",this);

        initData();
        setUpView();
        loadMemberInfo();
        loadInfoComplete();

    }

    @Override
    public void initData() {
           memberHeadImg = (CircleImageView) this.findViewById(R.id.iv_member_head_img);
           tvNickName = (TextView) this.findViewById(R.id.tv_nickname);
           tvSex = (TextView) this.findViewById(R.id.tv_sex);
           tvName = (TextView) this.findViewById(R.id.tv_name);
           tvNumberID = (TextView) this.findViewById(R.id.tv_numberID);
           tvBirthday = (TextView) this.findViewById(R.id.tv_birthday);
           tvPhoneNum = (TextView) this.findViewById(R.id.tv_mobile);
           tvInfoPerfectDegree = (TextView) this.findViewById(R.id.tv_info_perfect_degree);
        tv_nickname_reward_ctc = (TextView) this.findViewById(R.id.tv_nickname_reward_ctc);
        tv_name_reward_ctc = (TextView) this.findViewById(R.id.tv_name_reward_ctc);
        tv_idnumber_reward_ctc = (TextView) this.findViewById(R.id.tv_idnumber_reward_ctc);
        tv_birthday_reward_ctc = (TextView) this.findViewById(R.id.tv_birthday_reward_ctc);
        tv_mobile_reward_ctc = (TextView) this.findViewById(R.id.tv_mobile_reward_ctc);
        tv_wx_reward_ctc = (TextView) this.findViewById(R.id.tv_wx_reward_ctc);
        tv_qq_reward_ctc = (TextView) this.findViewById(R.id.tv_qq_reward_ctc);
        tv_backcard_reward_ctc = (TextView) this.findViewById(R.id.tv_backcard_reward_ctc);
           tvWXNum = (TextView) this.findViewById(R.id.tv_wx_num);
           tvQQNum = (TextView) this.findViewById(R.id.tv_qq_num);
        tv_bankcard = (TextView) this.findViewById(R.id.tv_bankcard);
           tvComplete = (TextView) this.findViewById(R.id.tv_complete);
           ivBack = (ImageView) this.findViewById(R.id.title_back);
           progressBarInfoPerfectDegree = (ProgressBar) this.findViewById(R.id.progressBar_info_perfect_degree);
           view1 = View.inflate(this, R.layout.photo_popupwindow_item, null);
           btn_tuku = (Button) view1
                .findViewById(R.id.photo_popupwindow_item_photo);
           btn_camera = (Button) view1
                .findViewById(R.id.photo_popupwindow_item_camera);
           btn_quxiao = (Button) view1
                .findViewById(R.id.photo_popupwindow_item_look);


        linearlayoutIntoMemberUpgrade = (LinearLayout) this.findViewById(R.id.linearlayout_into_member_upgrade);
        linearLayoutIntoAddBackCard = (LinearLayout) this.findViewById(R.id.linearLayout_into_add_backcard);
        linearlayout_into_update_member_img = (LinearLayout) this.findViewById(R.id.linearlayout_into_update_member_img);
        linearlayout_into_update_member_nickname = (LinearLayout) this.findViewById(R.id.linearlayout_into_update_member_nickname);
        linearlayout_into_update_member_sex = (LinearLayout) this.findViewById(R.id.linearlayout_into_update_member_sex);
        linearlayout_into_update_member_reaname = (LinearLayout) this.findViewById(R.id.linearlayout_into_update_member_reaname);
        linearlayout_into_update_member_idnumber = (LinearLayout) this.findViewById(R.id.linearlayout_into_update_member_idnumber);
        linearlayout_into_update_member_birthday = (LinearLayout) this.findViewById(R.id.linearlayout_into_update_member_birthday);
        linearlayout_into_update_member_bandphone = (LinearLayout) this.findViewById(R.id.linearlayout_into_update_member_bandphone);
        linearlayout_into_update_member_bandwx = (LinearLayout) this.findViewById(R.id.linearlayout_into_update_member_bandwx);
        linearlayout_into_update_member_bandqq = (LinearLayout) this.findViewById(R.id.linearlayout_into_update_member_bandqq);


        popBirthHelper = new PopBirthHelper(this);
        popBirthHelper.setOnClickOkListener(new PopBirthHelper.OnClickOkListener() {
            @Override
            public void onClickOk(String birthday) {
                tvBirthday.setText(birthday);
            }
        });
    }


    private void setUpView() {
        ivBack.setOnClickListener(onClickListener);
        tvComplete.setOnClickListener(onClickListener);
        linearlayoutIntoMemberUpgrade.setOnClickListener(onClickListener);
        linearLayoutIntoAddBackCard.setOnClickListener(onClickListener);
        linearlayout_into_update_member_nickname.setOnClickListener(onClickListener);
        linearlayout_into_update_member_sex.setOnClickListener(onClickListener);
        linearlayout_into_update_member_img.setOnClickListener(onClickListener);
        linearlayout_into_update_member_reaname.setOnClickListener(onClickListener);
        linearlayout_into_update_member_birthday.setOnClickListener(onClickListener);
        linearlayout_into_update_member_idnumber.setOnClickListener(onClickListener);
        linearlayout_into_update_member_bandqq.setOnClickListener(onClickListener);
        linearlayout_into_update_member_bandwx.setOnClickListener(onClickListener);

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
                    UpdateMemberInfoActivity.this.sToast("请先插入SD卡");
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
                    UpdateMemberInfoActivity.this.sToast("请先插入SD卡");
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
    }

    /**
     * 获取会员的信息
     */
    public void loadMemberInfo()
    {
        mZProgressHUD.show();
        HttpConnect.post(UpdateMemberInfoActivity.this, "member_info", null, new Callback() {

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
                            if (pic!=null){
                                Glide.with(UpdateMemberInfoActivity.this).load(pic).asBitmap().centerCrop().into(new BitmapImageViewTarget(memberHeadImg) {
                                    @Override
                                    protected void setResource(Bitmap resource) {
                                        RoundedBitmapDrawable circularBitmapDrawable =
                                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                                        circularBitmapDrawable.setCircular(true);
                                        memberHeadImg.setImageDrawable(circularBitmapDrawable);
                                    }
                                });
                            }

                            if(!TextUtils.isEmpty(nickname))
                            {
                                tvNickName.setText(nickname);
                            }

                            if(!TextUtils.isEmpty(sex))
                            {
                                if(sex.equals("保密"))
                                {
                                    tvSex.setText("保密");
                                }else if(sex.equals("美女"))
                                {
                                    tvSex.setText("美女");
                                }else if(sex.equals("帅哥"))
                                {
                                    tvSex.setText("帅哥");
                                }
                            }

                            if(!TextUtils.isEmpty(name))
                            {
                                tvName.setText(name);
                            }

                            if(!TextUtils.isEmpty(birthday))
                            {
                                tvBirthday.setText(birthday);
                            }

                            if(!TextUtils.isEmpty(idnumber))
                            {
                                tvNumberID.setText(idnumber);
                            }

                            if(!TextUtils.isEmpty(mobile))
                            {
                                tvPhoneNum.setText(mobile.substring(0,3)+"****"+mobile.substring(7,11));
                                linearlayout_into_update_member_bandwx.setClickable(false);
                            }else
                            {
                                tvPhoneNum.setText("未绑定");
                                linearlayout_into_update_member_bandphone.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        View view3 = View.inflate(UpdateMemberInfoActivity.this,R.layout.bandphone_popuwindow,null);
                                        final PopupWindow reanamePopupWindow = new PopupWindow(view3,WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
                                        reanamePopupWindow.setFocusable(true);

                                        // 设置背景颜色变暗
                                        WindowManager.LayoutParams lp2 = getWindow().getAttributes();
                                        lp2.alpha = 0.7f;
                                        getWindow().setAttributes(lp2);
                                        reanamePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                                            @Override
                                            public void onDismiss() {
                                                WindowManager.LayoutParams lp3 = getWindow().getAttributes();
                                                lp3.alpha = 1f;
                                                getWindow().setAttributes(lp3);
                                            }
                                        });


                                        reanamePopupWindow.setOutsideTouchable(true);

                                        // 实例化一个ColorDrawable颜色为半透明
                                        ColorDrawable dw3 = new ColorDrawable(0x00ffffff);
                                        reanamePopupWindow.setBackgroundDrawable(dw3);

                                        reanamePopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

                                        // 设置popWindow的显示和消失动画
                                        reanamePopupWindow.setAnimationStyle(R.style.Mypopwindow_anim_style);

                                        reanamePopupWindow.showAtLocation(linearlayout_into_update_member_nickname,Gravity.CENTER,0,0);
                                        final EditText reaname = (EditText) view3.findViewById(R.id.et_name);
                                        view3.findViewById(R.id.btn_commit).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                String name8 = reaname.getText().toString();

                                                if(TextUtils.isEmpty(name8))
                                                {
                                                    sToast("手机号不能为空");
                                                }else if(name8.length()!=11)
                                                {
                                                    sToast("请提供正确的手机号");
                                                }else
                                                {
                                                    reanamePopupWindow.dismiss();
                                                    bandphone(name8);
                                                }
                                            }
                                        });
                                        view3.findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                reanamePopupWindow.dismiss();
                                            }
                                        });

                                    }
                                });

                            }

                            if(!TextUtils.isEmpty(wechatopenid))
                            {
                                tvWXNum.setText("已绑定");
                                linearlayout_into_update_member_bandwx.setClickable(false);
                            }else
                            {
                                tvWXNum.setText("未绑定");
                            }

                            if(!TextUtils.isEmpty(qqopenid))
                            {
                                tvQQNum.setText("已绑定");
                                linearlayout_into_update_member_bandqq.setClickable(false);
                            }else
                            {
                                tvQQNum.setText("未绑定");
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
                            mZProgressHUD.hide();
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
                        mZProgressHUD.hide();
                        sToast("链接超时");
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        });
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (i != 1 ){
           loadMemberInfo();
        }else{
            i = 0;
        }

    }


    public void updateMemberInfo()
    {
        mZProgressHUD.show();
        Map<String, String> par1 = new HashMap<String, String>();
        if (photo == null) {
            par1.put("pic", pic);
        } else {
            par1.put("upfile:pic", BitmapUtil.bitmapToBase64(photo));
        }
        par1.put("nickname", nickname);
        par1.put("name", name);
        String birthday = tvBirthday.getText().toString().trim();
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
                        mZProgressHUD.hide();
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
                            mZProgressHUD.hide();
//                            sToast("修改成功");
                        }
                    };
                    dataHandler.sendEmptyMessage(0);


                }else{
                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {
                            mZProgressHUD.hide();
                            sToast("修改失败");
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }
            }
        });
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
                // ((ImageView)findViewById(R.id.iv))
                i = 1;
                memberHeadImg.setImageBitmap(photo); // 把图片显示在ImageView控件上
                break;
        }

    }


    private void loadInfoComplete()
    {
        HttpConnect.post(this, "member_information_complete", null, new Callback() {

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                Handler dataHandler = new Handler(getContext()
                        .getMainLooper()) {

                    @Override
                    public void handleMessage(final Message msg) {
                        mZProgressHUD.dismiss();
                        sToast("链接超时");
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
                            mZProgressHUD.dismiss();

                            String sex = object.getJSONArray("data").getJSONObject(0).getString("sex");
                            String name = object.getJSONArray("data").getJSONObject(0).getString("name");
                            String brithday = object.getJSONArray("data").getJSONObject(0).getString("brithday");
                            String idnumber = object.getJSONArray("data").getJSONObject(0).getString("idnumber");
                            String mobile = object.getJSONArray("data").getJSONObject(0).getString("mobile");
                            String wechatid = object.getJSONArray("data").getJSONObject(0).getString("wechatid");
                            String qqid = object.getJSONArray("data").getJSONObject(0).getString("qqid");
                            String bankcount = object.getJSONArray("data").getJSONObject(0).getString("bankcount");
                            String perfect = object.getJSONArray("data").getJSONObject(0).getString("perfect");

                            if(!TextUtils.isEmpty(perfect))
                            {
                                progressBarInfoPerfectDegree.setProgress((int) (Float.parseFloat(perfect)*100));
                                tvInfoPerfectDegree.setText(removeDecimalPoint(String.valueOf(Float.parseFloat(perfect)*100))+"%");
                            }

                            if(!TextUtils.isEmpty(sex)&&sex.equals("0"))
                            {
                                tv_nickname_reward_ctc.setVisibility(View.VISIBLE);
                                setTextViewWidth(tvNickName);
                            }

                            if(!TextUtils.isEmpty(name)&&name.equals("0"))
                            {
                                tv_name_reward_ctc.setVisibility(View.VISIBLE);
                                setTextViewWidth(tvName);
                            }

                            if(!TextUtils.isEmpty(brithday)&&brithday.equals("0"))
                            {
                                tv_birthday_reward_ctc.setVisibility(View.VISIBLE);
                                setTextViewWidth(tvBirthday);
                            }

                            if(!TextUtils.isEmpty(idnumber)&&idnumber.equals("0"))
                            {
                                tv_idnumber_reward_ctc.setVisibility(View.VISIBLE);
                                setTextViewWidth(tvNumberID);
                            }

                            if(!TextUtils.isEmpty(mobile)&&mobile.equals("0"))
                            {
                                tv_mobile_reward_ctc.setVisibility(View.VISIBLE);
                                setTextViewWidth(tvPhoneNum);
                            }

                            if(!TextUtils.isEmpty(wechatid)&&wechatid.equals("0"))
                            {
                                tv_wx_reward_ctc.setVisibility(View.VISIBLE);
                                setTextViewWidth(tvWXNum);
                            }

                            if(!TextUtils.isEmpty(qqid)&&qqid.equals("0"))
                            {
                                tv_qq_reward_ctc.setVisibility(View.VISIBLE);
                                setTextViewWidth(tvQQNum);
                            }

                            if(!TextUtils.isEmpty(bankcount)&&bankcount.equals("0"))
                            {
                                tv_backcard_reward_ctc.setVisibility(View.VISIBLE);
                                setTextViewWidth(tv_bankcard);
                            }

                        }
                    };
                    dataHandler.sendEmptyMessage(0);


                }else{
                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {
                            mZProgressHUD.hide();
                            sToast(message);
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }
            }
        });
    }

    private void setTextViewWidth(TextView textView)
    {
        ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        textView.setLayoutParams(layoutParams);
    }

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
     * 绑定手机号
     */
        public void bandphone(final String mobile)
        {
            mZProgressHUD.show();
            Map<String, String> par = new HashMap<String, String>();
            par.put("mobile", mobile);
            HttpConnect.post(UpdateMemberInfoActivity.this, "member_modify_mobile", par, new Callback() {

                @Override
                public void onResponse(Response arg0) throws IOException {
                    final JSONObject data = JSONObject.fromObject(arg0.body().string());
                    if (data.get("status").equals("success")) {

                        UpdateMemberInfoActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sToast("绑定成功");
                                tvPhoneNum.setText(mobile.substring(0,3)+"****"+mobile.substring(7,11));
                                linearlayout_into_update_member_bandphone.setClickable(false);
                            }
                        });


                    } else {
                        UpdateMemberInfoActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sToast(data.getString("msg"));
                            }
                        });
                    }
                    UpdateMemberInfoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mZProgressHUD.cancel();
                        }
                    });
                }

                @Override
                public void onFailure(Request arg0, IOException arg1) {
                    UpdateMemberInfoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mZProgressHUD.cancel();
                            sToast("链接超时！");
                        }
                    });
                }
            });
        }
}
