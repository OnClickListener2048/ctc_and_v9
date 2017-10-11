package com.tohier.cartercoin.activity;

import android.Manifest;
import android.app.TabActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.aop.custom.IMNotification;
import com.alibaba.mobileim.contact.IYWContactHeadClickListener;
import com.alibaba.mobileim.contact.IYWContactService;
import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.IYWConversationUnreadChangeListener;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWConversationType;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.aliui.NotificationInitSampleHelper;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.MyApplication;

public class MainActivity extends TabActivity{


    public static TabHost mTabHost;
    public static TabWidget tabWidget;
    public static View view;
    public static String into;
    public static MainActivity mainActivity;
    private IYWConversationUnreadChangeListener mConversationUnreadChangeListener;

    public YWIMKit mIMKit;
    private String userid;
    private IYWConversationService mConversationService;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private TextView tvCount;

    private NotificationInitSampleHelper mNotificationSettings;

    public static boolean isflag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        mTabHost = getTabHost();

        init();
    }

    private void init() {
        mainActivity = this;

        into = getIntent().getStringExtra("into");
        tabWidget = getTabWidget();
        view = findViewById(R.id.vvv);

        mNotificationSettings = new NotificationInitSampleHelper(null);
        IMNotification imNotification = new IMNotification(null);
        mNotificationSettings.setNeedSound(false);

        userid = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getLinkCode();
        try{
            mIMKit = YWAPI.getIMKitInstance(userid, MyApplication.APP_KEY);

        }catch (Exception e){}
        mConversationService = mIMKit.getConversationService();

        Intent intent = new Intent(this,MeActivity.class);
        mTabHost.addTab(mTabHost.newTabSpec("shouye_log")
                .setIndicator(setViewIndicator("首页", R.drawable.tab_shouye))
                .setContent(intent));

        Intent intent1 = new Intent(this,RevisionAccountInfoActivity2.class);
        mTabHost.addTab(mTabHost.newTabSpec("mining_log")
                .setIndicator(setViewIndicator("账户", R.drawable.tab_account))
                .setContent(intent1));


        Intent intent2 = new Intent(this,ConversationActivity.class);
        mTabHost.addTab(mTabHost.newTabSpec("shop_log")
                .setIndicator(setViewIndicator1("宝友", R.drawable.tab_shop))
                .setContent(intent2));

        final Intent intent3 = new Intent(this,TwoMActivity.class);
        mTabHost.addTab(mTabHost.newTabSpec("me_log")
                .setIndicator(setViewIndicator("我的", R.drawable.tab_me))
                .setContent(intent3));


        initConversationServiceAndListener();

//        UserProfileSampleHelper.initProfileCallback(this);


        final IYWContactService contactManager = mIMKit.getContactService();

        //头像点击的回调（开发者可以按需设置）
        contactManager.setContactHeadClickListener(new IYWContactHeadClickListener() {
            @Override
            public void onUserHeadClick(final Fragment fragment, YWConversation conversation, String userId, String appKey, boolean isConversationListPage) {

                if (userId.equalsIgnoreCase(userid)){
                    fragment.getActivity().startActivity(new Intent(fragment.getActivity(),MeInfoActivity.class));
                }else{
                    if (conversation.getConversationType() != YWConversationType.SHOP){

                            Intent intent = new Intent(fragment.getActivity(), FriendsInfoActivity.class);
                            intent.putExtra("isFrend",0);
                            intent.putExtra("isq",1);
                            intent.putExtra("userid",userId);
                            intent.putExtra("isdel",0);
                            fragment.getActivity().startActivity(intent);

                    }
                }


            }


            @Override
            public void onTribeHeadClick(Fragment fragment, YWConversation conversation, long tribeId) {
//                IMNotificationUtils.getInstance().showToast(fragment.getActivity(), "你点击了群 " + tribeId + " 的头像");
            }

            @Override
            public void onCustomHeadClick(Fragment fragment, YWConversation conversation) {
//                IMNotificationUtils.getInstance().showToast(fragment.getActivity(), "你点击了自定义会话 " + conversation.getConversationId() + " 的头像");
            }
        });


        mTabHost.getTabWidget().getChildTabViewAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isflag)
                {
                    mTabHost.setCurrentTab(0);
                }
                else
                {
                    mTabHost.setCurrentTab(1);
                }
            }
        });

        mTabHost.getTabWidget().getChildTabViewAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isflag)
                {
                    mTabHost.setCurrentTab(0);
                }
                else
                {
                    mTabHost.setCurrentTab(2);
                }
            }
        });

        mTabHost.getTabWidget().getChildTabViewAt(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isflag)
                {
                    mTabHost.setCurrentTab(0);
                }
                else
                {
                    mTabHost.setCurrentTab(3);
                }
            }
        });


        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            public void onTabChanged(String tabId) {
                if (tabId.equals("shouye_log")) {
                  MeActivity.activity.savePic();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        //resume时需要检查全局未读消息数并做处理，因为离开此界面时删除了全局消息监听器
        mConversationUnreadChangeListener.onUnreadChange();

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)  //打开相机权限
                    != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)   //可读
                            != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)  //可写
                            != PackageManager.PERMISSION_GRANTED) {
                //申请WRITE_EXTERNAL_STORAGE权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE
                                , Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }
        }


    }


    private View setViewIndicator(String str,int drawable){
        View view1 = View.inflate(this, R.layout.tab_layout,null);
        ImageView imageView = (ImageView) view1.findViewById(R.id.iv_me_tab);
        imageView.setImageResource(drawable);
        TextView textView = (TextView) view1.findViewById(R.id.tv_me);
        textView.setText(str);
        return view1;
    }


    private View setViewIndicator1(String str,int drawable){
        View view1 = View.inflate(this, R.layout.tab_layout1,null);
        ImageView imageView = (ImageView) view1.findViewById(R.id.iv_me_tab);
        imageView.setImageResource(drawable);
        TextView textView = (TextView) view1.findViewById(R.id.tv_me);
        tvCount = (TextView) view1.findViewById(R.id.tv_count);
        textView.setText(str);
        return view1;
    }


    private void initConversationServiceAndListener() {

        mConversationUnreadChangeListener = new IYWConversationUnreadChangeListener() {
            @Override
            public void onUnreadChange() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        //获取当前登录用户的所有未读数
                        int unReadCount = mConversationService.getAllUnreadCount();
                        //设置桌面角标的未读数
                        mIMKit.setShortcutBadger(unReadCount);
                        if (unReadCount > 0) {
                            tvCount.setVisibility(View.VISIBLE);
                            if (unReadCount < 100) {
                                tvCount.setText(unReadCount + "");
                            } else {
                                tvCount.setText("99+");
                            }
                        } else {
                            tvCount.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        };
        mConversationService.addTotalUnreadChangeListener(mConversationUnreadChangeListener);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mConversationUnreadChangeListener != null){
            mConversationService.removeTotalUnreadChangeListener(mConversationUnreadChangeListener);
        }
        Log.i("wyh","执行ondestroy方法。。。");
    }


}
