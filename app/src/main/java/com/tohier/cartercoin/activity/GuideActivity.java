package com.tohier.cartercoin.activity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.android.activity.base.BaseActivity;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.biz.LoadLoginPicBiz;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.IPUtil;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.MyNetworkConnection;
import com.tohier.cartercoin.presenter.GetMemberInfoPresenter;
import com.tohier.cartercoin.presenter.GetVersionCodePresenter;
import com.tohier.cartercoin.ui.GetMemberInfoView;
import com.tohier.cartercoin.ui.PhoneLoginView;
import com.tohier.cartercoin.ui.UpdateDialogView;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by 武文锴 on 2016/11/8.
 */

public class GuideActivity extends BaseActivity implements GetMemberInfoView, PhoneLoginView, UpdateDialogView {

    private static String ipLine;
    private static InputStream inStream;
    private static URL infoUrl;
    /**
     * 欢迎页面滑动的viewPager
     */
    private ViewPager viewpager_guide;
    /**
     * 存放滑动的视图
     */
    private ArrayList<View> viewList;//view数组
    /**
     * 视图对象
     */
    private View view1, view2, view3, view4, view5;
    /**
     * 第五个视图上边的ImageView对象
     */
    private View iv_go;
    /**
     * 清列化存储
     */
    private SharedPreferences sharedpreferences;
    private String username, password;
    private GetMemberInfoPresenter getMemberInfoPresenter;


    PopupWindow window = null;
    private View weihuView;

    private View view;
    private PopupWindow window1;

    private GifImageView iv_welcome;
    /**
     *用于是否提示更新的Presenter 负责完成View于Model间的交互
     **/
    private GetVersionCodePresenter getVersionCodePresenter;

    public static String PHONE_ID = "";//设备id
    public static String PHONE_TYPE = ""; //设备型号
    public static String LATITUDE = "";//维度
    public static String LONGITUDE = "";//经度
    public static String ADDRESS = "";//地址


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PHONE_ID = getPhoneID();
        PHONE_TYPE = "型号：" + android.os.Build.MODEL + " 版本号：" + android.os.Build.VERSION.RELEASE;
        getLocation();

        sharedpreferences = getSharedPreferences("isShowWelcomeLayout", Context.MODE_PRIVATE);
        sharedpreferences.edit().putBoolean("isShow", true).commit();
        if (sharedpreferences.getBoolean("isShow", false) == false) {
            setContentView(R.layout.activity_guide);
        } else {
            setContentView(R.layout.welcome_layout);
            weihuView = View.inflate(this, R.layout.popupwindow_weihu_update, null);

            window = new PopupWindow(weihuView, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT);

        }

        try {
            String info = readFileSdcardFile(Environment.getExternalStorageDirectory().getPath() + "/ryg_test1/log/crash.txt");
            uploadExceptionToServer(info);

        } catch (IOException e) {
            e.printStackTrace();
        }
        initData();
    }

    @Override
    public void initData() {

        view = View.inflate(this, R.layout.activity_prompt_update, null);
        window1 = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        /**
         * 初始化 app是否提示更新的 Presenter
         */
        getVersionCodePresenter = new GetVersionCodePresenter(this, this);

        if (sharedpreferences.getBoolean("isShow", false) == false) {
            viewpager_guide = (ViewPager) this.findViewById(R.id.viewpager_guide);
            LayoutInflater inflater = getLayoutInflater();
            view1 = inflater.inflate(R.layout.activity_viewpager_item1, null);
            view2 = inflater.inflate(R.layout.activity_viewpager_item2, null);
            view3 = inflater.inflate(R.layout.activity_viewpager_item3, null);
//            view4 = inflater.inflate(R.layout.activity_viewpager_item4, null);

            iv_go = view3.findViewById(R.id.iv_guide_item3);
            iv_go.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(GuideActivity.this, LoginMainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
            viewList.add(view1);
            viewList.add(view2);
            viewList.add(view3);

            PagerAdapter pagerAdapter = new PagerAdapter() {

                @Override
                public boolean isViewFromObject(View arg0, Object arg1) {
                    return arg0 == arg1;
                }

                @Override
                public int getCount() {
                    return viewList.size();
                }

                @Override
                public void destroyItem(ViewGroup container, int position,
                                        Object object) {
                    ((ViewPager) container).removeView(viewList.get(position));
                }

                @Override
                public Object instantiateItem(ViewGroup container, int position) {
                    ((ViewPager) container).addView(viewList.get(position));


                    return viewList.get(position);
                }
            };
            viewpager_guide.setAdapter(pagerAdapter);
            sharedpreferences.edit().putBoolean("isShow", true).commit();
        } else {
            iv_welcome = (GifImageView) this.findViewById(R.id.iv_welcome);

            Bitmap welcome_pic = LoadLoginPicBiz.convertToBitmap(LoadLoginPicBiz.ALBUM_PATH + LoadLoginPicBiz.WELCOME_PIC);
            if (welcome_pic != null) {
                iv_welcome.setImageBitmap(welcome_pic);
                iv_welcome.setScaleType(ImageView.ScaleType.FIT_XY);
            }

            AlphaAnimation alpha = new AlphaAnimation(0.2f, 1.0f);
            alpha.setDuration(1500);
            alpha.setInterpolator(new LinearInterpolator());
            iv_welcome.setAnimation(alpha);
            alpha.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    isMaintain();
                }
            });
        }


    }

    /**
     * 查看当前app是否处于维护状态
     */
    private void isMaintain() {
        Thread thrad = new Thread()
        {
            @Override
            public void run() {
                super.run();
                try {
                    ipLine = IPUtil.getNetIp();
                }catch (Exception e)
                {

                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MyNetworkConnection.getNetworkConnection(GuideActivity.this).post("post", "http://www.blacoin.cc/app/fenlebao.ashx", null, new Callback() {
                            @Override
                            public void onFailure(Request request, IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        sToast("请检查您的网络链接状态");
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Response response) throws IOException {
                                String json = response.body().string().trim();
                                try{
                                    final net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray.fromObject(json);
                                    final net.sf.json.JSONObject jsonObject = jsonArray.optJSONObject(0);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            String enable = jsonObject.optString("enable");
                                            String ip = jsonObject.optString("ip");

                                            if (enable != null && !TextUtils.isEmpty(enable)) {
                                                if (enable.equals("false")) {
//                                                    getVersionCodePresenter.getVersionCode();
                                                    if (LoginUser.getInstantiation(getApplicationContext()).isLogin()) {
                                                        SharedPreferences sharedPreferences = getSharedPreferences("isExitGesturesPassword", Context.MODE_PRIVATE);
                                                        String opengestures = sharedPreferences.getString("opengestures", "");
                                                        if (!TextUtils.isEmpty(opengestures) && opengestures.equals("True")) {
                                                            Intent intent = new Intent(GuideActivity.this, GesturesPasswordActivity.class);
                                                            intent.putExtra("into", "GuideActivity");
                                                            startActivity(intent);
                                                        } else {
                                                            Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                                                            intent.putExtra("into", "GuideActivity");
                                                            startActivity(intent);
                                                        }
                                                    } else {
                                                        Intent intent = new Intent(GuideActivity.this, LoginMainActivity.class);
                                                        startActivity(intent);
                                                    }
                                                    finish();
                                                } else {
                                                    //获取wifi服务

                                                    if(!TextUtils.isEmpty(ip)&&ip.equals(ipLine))
                                                    {
                                                        if (LoginUser.getInstantiation(getApplicationContext()).isLogin()) {
                                                            SharedPreferences sharedPreferences = getSharedPreferences("isExitGesturesPassword", Context.MODE_PRIVATE);
                                                            String opengestures = sharedPreferences.getString("opengestures", "");
                                                            if (!TextUtils.isEmpty(opengestures) && opengestures.equals("True")) {
                                                                Intent intent = new Intent(GuideActivity.this, GesturesPasswordActivity.class);
                                                                intent.putExtra("into", "GuideActivity");
                                                                startActivity(intent);
                                                            } else {
                                                                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                                                                intent.putExtra("into", "GuideActivity");
                                                                startActivity(intent);
                                                            }
                                                        } else {
                                                            Intent intent = new Intent(GuideActivity.this, LoginMainActivity.class);
                                                            startActivity(intent);
                                                        }
                                                        finish();
//                                                        getVersionCodePresenter.getVersionCode();
                                                    }else
                                                    {
                                                        String title = jsonObject.optString("title");
                                                        String content = jsonObject.optString("content");
                                                        startActivity(new Intent(GuideActivity.this,MaintainDialogActivity.class).putExtra("title",title).putExtra("content",content));
                                                    }
                                                }
                                            }
                                        }
                                    });
                                }catch (JSONException e){
                                    sToast("您的网络太慢了，请更换网络");
                                }

                            }
                        });
                    }
                });
            }
        };

        thrad.start();
    }


    /**
     * 显示实名认证的提示框
     */
    public void show(String startDate, String endDate) {
        ImageView iv_cancel_authentication = (ImageView) weihuView.findViewById(R.id.iv_cancel_authentication);
        TextView tv_start_time = (TextView) weihuView.findViewById(R.id.tv_start_time);
        TextView tv_end_time = (TextView) weihuView.findViewById(R.id.tv_end_time);

        iv_cancel_authentication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });

        tv_start_time.setText("开始时间:" + startDate);
        tv_end_time.setText("结束时间:" + endDate);

        if (!window.isShowing()) {
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
            window.showAtLocation(this.findViewById(R.id.iv_welcome),
                    Gravity.BOTTOM, 0, 0);
        }
    }


    @Override
    public void loadMemberInfoSuccess() {

    }

    @Override
    public void loadMemberInfoFail() {

    }

    @Override
    public void showMsg(String msg) {

    }

    @Override
    public String getusername() {
        return username;
    }

    @Override
    public String getpassword() {
        return password;
    }

    @Override
    public void loginSuccess() {


    }

    @Override
    public void loginFail() {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void setRMBTextView(String rmbTextView) {

    }

    @Override
    public void setXiaoFeiAccountTextView(String rmbTextView) {

    }

    @Override
    public void setJueZhanTextView(String rmbTextView) {

    }

    @Override
    public void setTerminalCtcTextView(String terminalCtcTextView) {

    }

    @Override
    public void setDemandTextView(String demandTextView) {

    }

    @Override
    public TextView setTongBanTextView(String rmbTextView) {
        return null;
    }

    @Override
    public TextView setTongXiTextView(String terminalCtcTextView) {
        return null;
    }

    @Override
    public TextView setTongXiaoTextView(String demandTextView) {
        return null;
    }

    @Override
    public void setShareTextView(String demandTextView) {

    }

    @Override
    public void setCurrentCTCPriceTextView(String demandTextView) {

    }

    @Override
    public void setallSsetsTextView(String allssets) {

    }

    @Override
    public LinearLayout getTwoLevel() {
        return null;
    }

    @Override
    public LinearLayout getThreeLevel() {
        return null;
    }

    @Override
    public void loadFail(String mark) {
//             sToast("链接失败");
        if (LoginUser.getInstantiation(getApplicationContext()).isLogin()) {
            SharedPreferences sharedPreferences = getSharedPreferences("isExitGesturesPassword", Context.MODE_PRIVATE);
            String opengestures = sharedPreferences.getString("opengestures", "");
            if (!TextUtils.isEmpty(opengestures) && opengestures.equals("True")) {
                Intent intent = new Intent(GuideActivity.this, GesturesPasswordActivity.class);
                intent.putExtra("into", "GuideActivity");
                startActivity(intent);
            } else {
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                intent.putExtra("into", "GuideActivity");
                startActivity(intent);
            }
        } else {
            Intent intent = new Intent(GuideActivity.this, LoginMainActivity.class);
            startActivity(intent);
        }
        finish();
    }


    @Override
    public void showUpdateDialog(String updateMsg, final String url5) {

        Button btnCencel = (Button) view.findViewById(R.id.btn_cancel);
        Button btnCommit = (Button) view.findViewById(R.id.btn_commit);
        TextView tvMsg = (TextView) view.findViewById(R.id.tv_msg);
        tvMsg.setText(updateMsg);

        btnCencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window1.dismiss();
//                if (LoginUser.getInstantiation(getApplicationContext()).isLogin()) {
//                    SharedPreferences sharedPreferences = getSharedPreferences("isExitGesturesPassword", Context.MODE_PRIVATE);
//                    String opengestures = sharedPreferences.getString("opengestures", "");
//                    if (!TextUtils.isEmpty(opengestures) && opengestures.equals("True")) {
//                        Intent intent = new Intent(GuideActivity.this, GesturesPasswordActivity.class);
//                        intent.putExtra("into", "GuideActivity");
//                        startActivity(intent);
//                    } else {
//                        Intent intent = new Intent(GuideActivity.this, MainActivity.class);
//                        intent.putExtra("into", "GuideActivity");
//                        startActivity(intent);
//                    }
//                } else {
//                    Intent intent = new Intent(GuideActivity.this, LoginMainActivity.class);
//                    startActivity(intent);
//                }
//                finish();
            }
        });

        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = url5;
                Intent intent = new Intent();
                intent.setData(Uri.parse(url));//Url 就是你要打开的网址
                intent.setAction(Intent.ACTION_VIEW);
                GuideActivity.this.startActivity(intent); //启动浏览器
                window1.dismiss();

//                if (LoginUser.getInstantiation(getApplicationContext()).isLogin()) {
//                    SharedPreferences sharedPreferences = getSharedPreferences("isExitGesturesPassword", Context.MODE_PRIVATE);
//                    String opengestures = sharedPreferences.getString("opengestures", "");
//                    if (!TextUtils.isEmpty(opengestures) && opengestures.equals("True")) {
//                        Intent intent5 = new Intent(GuideActivity.this, GesturesPasswordActivity.class);
//                        intent.putExtra("into", "GuideActivity");
//                        startActivity(intent);
//                    } else {
//                        Intent intent5 = new Intent(GuideActivity.this, MainActivity.class);
//                        intent.putExtra("into", "GuideActivity");
//                        startActivity(intent);
//                    }
//                } else {
//                    Intent intent5 = new Intent(GuideActivity.this, LoginMainActivity.class);
//                    startActivity(intent);
//                }
//                finish();
            }
        });

        if (!window1.isShowing()) {
            // 设置背景颜色变暗
            WindowManager.LayoutParams lp5 = getWindow().getAttributes();
            lp5.alpha = 0.5f;
            getWindow().setAttributes(lp5);
            window1.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams lp3 = getWindow().getAttributes();
                    lp3.alpha = 1f;
                    getWindow().setAttributes(lp3);
                }
            });

            window1.setOutsideTouchable(true);

            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0x00ffffff);
            window1.setBackgroundDrawable(dw);

            // 在底部显示
            window1.showAtLocation(iv_welcome,
                    Gravity.BOTTOM, 0, 0);
        }


    }


    /**
     * 获取设备id
     * @return
     */
    private String getPhoneID() {

        String szImei = "";
        try{
            TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            szImei = TelephonyMgr.getDeviceId();
        }catch (Exception e){
            szImei = "";
        }
        String m_szDevIDShort = "35" + //we make this look like a valid IMEI
                Build.BOARD.length() % 10 +
                Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 +
                Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 +
                Build.HOST.length() % 10 +
                Build.ID.length() % 10 +
                Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 +
                Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 +
                Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 digits

        String m_szAndroidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        WifiManager wm = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();

        BluetoothAdapter m_BluetoothAdapter = null; // Local Bluetooth adapter
        m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String m_szBTMAC = m_BluetoothAdapter.getAddress();


        String m_szLongID =  szImei + m_szDevIDShort
                + m_szAndroidID + m_szWLANMAC + m_szBTMAC;
// compute md5
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.update(m_szLongID.getBytes(), 0, m_szLongID.length());
// get md5 bytes
        byte p_md5Data[] = m.digest();
// create a hex string
        String m_szUniqueID = new String();
        for (int i = 0; i < p_md5Data.length; i++) {
            int b = (0xFF & p_md5Data[i]);
// if it is a single digit, make sure it have 0 in front (proper padding)
            if (b <= 0xF)
                m_szUniqueID += "0";
// add number to string
            m_szUniqueID += Integer.toHexString(b);
        }   // hex string to uppercase
        m_szUniqueID = m_szUniqueID.toUpperCase();
        return m_szUniqueID;
    }

    private void getLocation() {
        //获取地理位置管理器
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);

        try{
            String locationProvider = null;
            if (providers.contains(LocationManager.GPS_PROVIDER)) {
                //如果是GPS
                locationProvider = LocationManager.GPS_PROVIDER;
            } else  {
                //如果是Network
                locationProvider = LocationManager.NETWORK_PROVIDER;
            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = locationManager.getLastKnownLocation(locationProvider);
            if(location!=null){
                LATITUDE = location.getLatitude()+"";
                LONGITUDE = location.getLongitude()+"";
                MyNetworkConnection.getNetworkConnection(GuideActivity.this).post("get", "http://api.map.baidu.com/geocoder?output=json&location="+LATITUDE+","+LONGITUDE, null, new Callback(){

                    @Override
                    public void onFailure(Request request, IOException e) {

                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        String json = response.body().string();
                        JSONObject data = JSONObject.fromObject(json);
                        if(data.getString("status").equalsIgnoreCase("OK")){

                            ADDRESS = data.optJSONObject("result").optJSONObject("addressComponent").optString("city")+","+data.optJSONObject("result").optString("formatted_address");
                        }
                    }
                });

            }
        }catch (Exception e){}

    }




    public String readFileSdcardFile(String fileName) throws IOException{
        String res="";
        File file = new File(fileName);
        if (file.isFile() && file.exists()) {
            try{
                FileInputStream fin = new FileInputStream(fileName);

                int length = fin.available();

                byte [] buffer = new byte[length];
                fin.read(buffer);
                res = new String(buffer);

                fin.close();
            }

            catch(Exception e){
                e.printStackTrace();
            }
        }

        return res;
    }



    private void uploadExceptionToServer(String info) {
        final File file = new File(Environment.getExternalStorageDirectory().getPath() + "/ryg_test1/log/crash.txt");
        if (file.isFile() && file.exists()) {
            Map<String, String> par = new HashMap<String, String>();
            par.put("Type", GuideActivity.PHONE_TYPE);
            par.put("ErrInfo", info);
            HttpConnect.post(this, "member_android_log", par, new Callback() {

                @Override
                public void onResponse(Response arg0) throws IOException {

                    try{
                        JSONObject json = JSONObject.fromObject(arg0.body().string());

                        if(json.getString("status").equals("success"))
                        {

                            if (file.isFile() && file.exists()) {
                                file.delete();
                            }
                        }else
                        {

                        }
                    }catch(Exception e){

                    }


                }
                @Override
                public void onFailure(Request arg0, IOException arg1) {
                }
            });
        }

    }

}
