package com.tohier.cartercoin.config;

import android.app.Activity;
import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.aop.AdviceBinder;
import com.alibaba.mobileim.aop.PointCutEnum;
import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.alibaba.wxlib.util.SysUtil;
import com.tohier.cartercoin.aliui.ChattingOperationCustomSample;
import com.tohier.cartercoin.aliui.ChattingUICustomSample;
import com.tohier.cartercoin.aliui.ConversationListOperationCustomSample;
import com.tohier.cartercoin.aliui.ConversationListUICustomSample;
import com.tohier.cartercoin.aliui.MyYWSDKGlobalConfig;
import com.tohier.cartercoin.signed.ResolutionUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class MyApplication extends MultiDexApplication {

    public static MyApplication application = null;
    private static final String TAG = "Init";
    public  static final String KEFU = "分乐宝在线咨询";
    //云旺OpenIM的DEMO用到的Application上下文实例
    private static Context sContext;
    public static Context getContext(){
        return sContext;
    }

    // 商户PID
    public static final String PARTNER = "2088521459673824";
    // 商户收款账号
    public static final String SELLER = "blacoincc@qq.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAM8zThSnZ0RFpucW" +
            "wtEkkHvu1PfS3UFyc7e71xSm7+pchmQvPcpHvP+fqZ3nyoscu9fHD2kXDSXqp3Ew" +
            "uC8A17WF2s69pPTuJSEIkZYDRrLY9ReV6XkepZ+xciB9r+7pjgBNDofRXlOxJXCu" +
            "a1g1lFo0mQ5Q7zpTeemIE2dL9cdnAgMBAAECgYEAitP+YfpUR7s/fCCVAdq08ETy" +
            "7uWbF8ne3OF/17eOkoHSDfhVN15ftQq+dC2lWRY0ifw3SBuI1Lnn3QBeuCehXysb" +
            "BtmX6sogrIFaeQdtuEzqvWAvhuToFdHNclTDTQ76tQ5qSA0fuaDXml7h93IsGr6w" +
            "voY//dYIx8IsRdmtIjkCQQDpb2YEzc8eOfKh8DdoAxrduhlXCsjqvepc/baK8/Q2" +
            "i9xuOe9RABELWj17+gHHjGrDb8fc62nGJVqUNnRuEFTrAkEA4zqzLEryPW6aSM9m" +
            "mxWzopLj1JWNATI0kFysZaIxwYJFZdzCwrP04lYw8CFxFWmElewVIbLP/4G68iMh" +
            "Oh7odQJABUZwLsGb4Zn7oq80AVS4obQX6ICk47Eg/7L4EhHLPgRRGeiTQOhYekIy" +
            "y39t1N2WeA4pnRNw03p6nojQfsA9uwJBAJzbEIQq2D1/9BD+2p2DxZUcQRmmgseo" +
            "mJBu6LLf2+jwvGcslWVCRHFZfj7FVwKo7FVxusSgYuTF8P7lMTKl02ECQCDx6SsU" +
            "bvjZ9lAKhq5W8+ERqMLRy0xSEpw+mjhzmv02x3LTrTkQXVd8oi113sz2ANyq8cxT" +
            "iwAtwlfOjn6IaiE=";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDPM04Up2dERabnFsLRJJB77tT3" +
            "0t1BcnO3u9cUpu/qXIZkLz3KR7z/n6md58qLHLvXxw9pFw0l6qdxMLgvANe1hdrO" +
            "vaT07iUhCJGWA0ay2PUXlel5HqWfsXIgfa/u6Y4ATQ6H0V5TsSVwrmtYNZRaNJkO" +
            "UO86U3npiBNnS/XHZwIDAQAB";

    public static boolean isFirst = true;
    //阿里百川appkey
    public static String APP_KEY = "23567513";

    public static Map<String, Activity> maps = new HashMap<String, Activity>();
    public static ArrayList<Activity>  activities = new ArrayList<>();


    public static void deleteActivity(String activityName) {
        Set<String> keySet = maps.keySet();
        if (keySet.contains(activityName)) {
            maps.remove(activityName);
        }
    }


    @Override
    public void onCreate() {


        super.onCreate();
        ResolutionUtil.getInstance().init(this);
        application = this;
        //todo Application.onCreate中，首先执行这部分代码，以下代码固定在此处，不要改动，这里return是为了退出Application.onCreate！！！
        if(mustRunFirstInsideApplicationOnCreate()){
            //todo 如果在":TCMSSevice"进程中，无需进行openIM和app业务的初始化，以节省内存
            return;
        }


        YWAPI.init(this, APP_KEY);

        CrashHandler catchHandler = CrashHandler.getInstance();
        catchHandler.init(getContext());

        //第二步：在Applicatoin类里将这个自定义接口绑定到单聊界面，加入以下代码（对于单聊界面的所有自定义UI定制只需要加一次）
        AdviceBinder.bindAdvice(PointCutEnum.CHATTING_FRAGMENT_OPERATION_POINTCUT, ChattingOperationCustomSample.class);
        AdviceBinder.bindAdvice(PointCutEnum.CHATTING_FRAGMENT_UI_POINTCUT, ChattingUICustomSample.class);
        AdviceBinder.bindAdvice(PointCutEnum.YWSDK_GLOBAL_CONFIG_POINTCUT, MyYWSDKGlobalConfig.class);
        AdviceBinder.bindAdvice(PointCutEnum.CONVERSATION_FRAGMENT_UI_POINTCUT, ConversationListUICustomSample.class);
        AdviceBinder.bindAdvice(PointCutEnum.CONVERSATION_FRAGMENT_OPERATION_POINTCUT, ConversationListOperationCustomSample.class);
        initCloudChannel(this);

//        //电商SDK初始化
//        AlibcTradeSDK.asyncInit(this, new AlibcTradeInitCallback() {
//            @Override
//            public void onSuccess() {
//                Toast.makeText(MyApplication.this, "初始化成功", Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onFailure(int code, String msg) {
////                Toast.makeText(MyApplication.this, "初始化失败,错误码="+code+" / 错误消息="+msg, Toast.LENGTH_SHORT).show();
//            }
//        });
    }

//    /**
//     * 初始化AlibabaSDK
//     * @param applicationContext
//     */
//    private void initOneSDK(final Context applicationContext) {
//        AlibabaSDK.setEnvironment(Environment.ONLINE);
//        AlibabaSDK.asyncInit(applicationContext, new InitResultCallback() {
//
//            @Override
//            public void onSuccess() {
//                Log.d(TAG, "init onesdk success");
//                //alibabaSDK初始化成功后，初始化云推送通道
//                initCloudChannel(applicationContext);
//            }
//
//            @Override
//            public void onFailure(int code, String message) {
//                Log.e(TAG, "init onesdk failed : " + message);
//            }
//        });
//    }

    /**
     * 初始化云推送通道
     * @param applicationContext
     */
    private void initCloudChannel(Context applicationContext) {
        PushServiceFactory.init(applicationContext);
        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                Log.i(TAG, "init cloudchannel success");
            }

            @Override
            public void onFailed(String errorCode, String errorMessage) {
                Log.e(TAG, "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });

        // 初始化小米通道，自动判断是否支持小米系统推送，如不支持会跳过注册
//        MiPushRegister.register(applicationContext, "小米AppID", "小米AppKey");
//         初始化华为通道，自动判断是否支持华为系统推送，如不支持会跳过注册
//        HuaWeiRegister.register(applicationContext);
    }


    private boolean mustRunFirstInsideApplicationOnCreate() {
        //必须的初始化
        SysUtil.setApplication(this);
        sContext = getApplicationContext();
        return SysUtil.isTCMSServiceProcess(sContext);
    }
}
