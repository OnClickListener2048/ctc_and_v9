package com.tohier.cartercoin.biz;

import android.content.Context;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.android.config.IContext;
import com.tohier.cartercoin.activity.GuideActivity;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.listener.PhoneLoginListener;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 武文锴 on 2016/11/5.
 */

public class PhoneLoginBiz implements IPhoneLoginBiz {


    /**
     * 上下文对象
     **/
    private Context context;


    public PhoneLoginBiz(Context contex) {
        this.context = contex;
    }

    @Override
    public void login(String username, String password, PhoneLoginListener phoneLoginListener) {
        isFirstLogin(username,password,phoneLoginListener);
    }

    private void isFirstLogin(final String username, String password, final PhoneLoginListener phoneLoginListener) {
        Map<String, String> par = new HashMap<String, String>();
        par.put("code", username);
        par.put("password", password);
        par.put("type", "mobile");
        par.put("pic", "");
        par.put("nickname", "");
        par.put("uuid", GuideActivity.PHONE_ID);
        par.put("geographic", GuideActivity.LONGITUDE+","+GuideActivity.LATITUDE );
        par.put("geographicBac",GuideActivity.ADDRESS );
        par.put("browserSystem", GuideActivity.PHONE_TYPE);
        HttpConnect.post(((IContext) context), "member_login_v3", par, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                   if (data.get("status").equals("success")) {

                    final String token = data.getJSONArray("data").getJSONObject(0).optString("token");
                    final String sp = data.getJSONArray("data").getJSONObject(0).optString("sp");
                       LoginUser.getInstantiation(context.getApplicationContext()).setToken(token);
                       context.getSharedPreferences("superPwd",Context.MODE_PRIVATE).edit().putString("sp",sp).commit();
                       phoneLoginListener.loadSuccess();
                }else
                {
                    phoneLoginListener.showLoginMsg(data.getString("msg"));
                }
            }

            @Override
            public  void onFailure(Request arg0, IOException arg1) {
                phoneLoginListener.loadFail();
            }
        });
    }
}
