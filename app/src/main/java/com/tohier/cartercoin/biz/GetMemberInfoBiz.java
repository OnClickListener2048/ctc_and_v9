package com.tohier.cartercoin.biz;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.android.config.IContext;
import com.tohier.cartercoin.activity.MyBaseActivity;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.MyApplication;
import com.tohier.cartercoin.listener.GetMemberInfoListener;
import com.tohier.cartercoin.presenter.GetMemberInfoPresenter;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 武文锴 on 2016/11/5.
 */

public class GetMemberInfoBiz<T> implements IGetMemberInfoBiz {
    /**
     * 上下文对象
     **/
    private Context context;
    private GetMemberInfoPresenter getMemberInfoPresenter;
    private YWIMKit mIMKit;

    public GetMemberInfoBiz(Context contex) {
        this.context = contex;
    }

    public GetMemberInfoBiz(Context context, GetMemberInfoPresenter getMemberInfoPresenter) {
        this.context = context;
        this.getMemberInfoPresenter = getMemberInfoPresenter;
    }

    /**
     * 获取会员的信息
     * @param getMemberInfoListener
     */
    @Override
    public void getMemberInfo(final GetMemberInfoListener getMemberInfoListener) {
        HttpConnect.post(((IContext) context), "member_info", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
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
                    final String picup = data.optJSONArray("data").optJSONObject(0).optString("picup");
                    final String picdown = data.optJSONArray("data").optJSONObject(0).optString("picdown");
                    final String sex = data.getJSONArray("data").getJSONObject(0).getString("sex");
                    String sp  = context.getSharedPreferences("superPwd",Context.MODE_PRIVATE).getString("sp","");

                    if(!TextUtils.isEmpty(sp)&&sp.equals("1"))   //超级密码
                    {
                        SharedPreferences sharedPreferences = context.getSharedPreferences("isExitGesturesPassword",Context.MODE_PRIVATE);
                        sharedPreferences.edit().putString("gesturepassword",gesturepassword).putString("opengestures",opengestures).commit();

                        SharedPreferences sharedPreferences2 = context.getSharedPreferences("isExitsName",Context.MODE_PRIVATE);
                        sharedPreferences2.edit().putString("name",name).commit();

                        SharedPreferences sharedPreferences3 = context.getSharedPreferences("isIdNumberPic",Context.MODE_PRIVATE);
                        sharedPreferences3.edit().putString("picup",picup).commit();

                        LoginUser.getInstantiation(context.getApplicationContext()).login(id,pic,nickname,moblie,type,LoginUser.getInstantiation(context.getApplicationContext()).getLoginUser().getToken(),linkcode,sex);
                        //TODO   获取手势密码并且存储在本地
                        getMemberInfoListener.loadSuccess();

                    }else   //正常
                    {
                        mIMKit = YWAPI.getIMKitInstance(linkcode, MyApplication.APP_KEY);
                        IYWLoginService loginService = mIMKit.getLoginService();
                        YWLoginParam loginParam = YWLoginParam.createLoginParam(linkcode, aliPassword);

                        loginService.login (loginParam, new IWxCallback() {

                            @Override
                            public void onSuccess(Object... arg0) {

                                SharedPreferences sharedPreferences = context.getSharedPreferences("isExitGesturesPassword",Context.MODE_PRIVATE);
                                sharedPreferences.edit().putString("gesturepassword",gesturepassword).putString("opengestures",opengestures).commit();

                                SharedPreferences sharedPreferences2 = context.getSharedPreferences("isExitsName",Context.MODE_PRIVATE);
                                sharedPreferences2.edit().putString("name",name).commit();

                                SharedPreferences sharedPreferences3 = context.getSharedPreferences("isIdNumberPic",Context.MODE_PRIVATE);
                                sharedPreferences3.edit().putString("picup",picup).commit();

                                LoginUser.getInstantiation(context.getApplicationContext()).login(id,pic,nickname,moblie,type,LoginUser.getInstantiation(context.getApplicationContext()).getLoginUser().getToken(),linkcode,sex);
                                //TODO   获取手势密码并且存储在本地
                                getMemberInfoListener.loadSuccess();


                            }

                            @Override
                            public void onProgress(int arg0) {
                                // TODO Auto-generated method stub
                            }

                            @Override
                            public void onError(final int errCode, final String description) {
//                                ((IContext) context).sToast(description);
                                getMemberInfoListener.loadFail();

                            }
                        });
                    }
                }else
                {
                    getMemberInfoListener.loadFail();
                }
            }

            @Override
            public  void onFailure(Request arg0, IOException arg1) {
                getMemberInfoListener.loadFail();
            }
        });
    }

    /**
     * 获取会员的账户信息
     * @param getMemberInfoListener
     */
    @Override
    public void getMemberTotalAssets(final GetMemberInfoListener getMemberInfoListener) {
        HttpConnect.post(((MyBaseActivity)context), "member_count_detial", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    final String money = data.optJSONArray("data").optJSONObject(0).optString("money");
                    final String ctc = data.optJSONArray("data").optJSONObject(0).optString("ctc");
                    final String count = data.optJSONArray("data").optJSONObject(0).optString("coupon");
                    final String allssets = data.optJSONArray("data").optJSONObject(0).optString("allmoney");
                    final String ctcoption = data.getJSONArray("data").getJSONObject(0).getString("ctcoption");  //决战账户
                    final String ctccous = data.getJSONArray("data").getJSONObject(0).getString("ctccous");  //消费账户
                    getMemberInfoPresenter.setCoupon(count);
                    getMemberInfoPresenter.setCtc(ctc);
                    getMemberInfoPresenter.setMonty(money);
                    getMemberInfoPresenter.setAllssets(allssets);
                    getMemberInfoPresenter.setXiaoFeiAccount(ctccous);
                    getMemberInfoPresenter.setJuezhanAccount(ctcoption);
                    getMemberInfoListener.loadSuccess();
                } else {
                    String msg = data.getString("msg");
                    getMemberInfoListener.showMsg(msg);
                }
            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
               getMemberInfoListener.loadFail();
            }
        });
    }

    /**
     * 获取会员的关系信息
     * @param getMemberInfoListener
     */
    @Override
    public void getMemberRelationship(final GetMemberInfoListener getMemberInfoListener) {
        Map<String, String> par = new HashMap<String, String>();
        par.put("id", LoginUser.getInstantiation(context.getApplicationContext())
                .getLoginUser().getUserId());
        HttpConnect.post(((IContext) context), "member_income_info", par, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    final String Share = data.optJSONArray("data").optJSONObject(0).optString("Share");
                    final String TotalNetPoint1 = data.optJSONArray("data").optJSONObject(0).optString("TotalNetPoint1");
                    final String TotalNetPoint2 = data.optJSONArray("data").optJSONObject(0).optString("TotalNetPoint2");
                    final String TotalNetPoint3 = data.optJSONArray("data").optJSONObject(0).optString("TotalNetPoint3");
                    getMemberInfoListener.loadRelationshipInfoSuccess(Share,TotalNetPoint1,TotalNetPoint2,TotalNetPoint3);
                } else {
                    String msg = data.getString("msg");
                    getMemberInfoListener.showMsg(msg);
                }
            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
                getMemberInfoListener.loadFail();
            }
        });
    }

    @Override
    public void getMemberExchangerate(final GetMemberInfoListener getMemberInfoListener) {
        HttpConnect.post(((MyBaseActivity)context), "member_rate", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    final String rate = data.getJSONArray("data").getJSONObject(0).getString("rate");
                    getMemberInfoListener.loadExchangerateInfoSuccess(rate);
                } else {
                    String msg = data.getString("msg");
                    getMemberInfoListener.showMsg(msg);
                }
            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
                getMemberInfoListener.loadFail();
            }
        });
    }
}
