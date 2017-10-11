package com.tohier.cartercoin.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.tohier.cartercoin.activity.LoginMainActivity;
import com.tohier.cartercoin.activity.PhoneLoginActivity;
import com.tohier.cartercoin.biz.GetMemberInfoBiz;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.listener.GetMemberInfoListener;
import com.tohier.cartercoin.ui.GetMemberInfoView;
import com.tohier.cartercoin.wxapi.WXEntryActivity;

/**
 * Created by 武文锴 on 2016/11/5.
 */

public class GetMemberInfoPresenter {

    private GetMemberInfoBiz getMemberInfoBiz;
    private GetMemberInfoView loadMemberInfoView;
    /**
     * 上下文对象
     **/
    private Context context;
    private String type;

    private String ctc;
    private String monty;
    private String coupon;
    private String allssets;
    private String juezhanAccount;
    private String xiaoFeiAccount;

    public void setJuezhanAccount(String juezhanAccount) {
        this.juezhanAccount = juezhanAccount;
    }

    public void setXiaoFeiAccount(String xiaoFeiAccount) {
        this.xiaoFeiAccount = xiaoFeiAccount;
    }

    public void setAllssets(String allssets) {
        this.allssets = allssets;
    }

    public void setCtc(String ctc) {
        this.ctc = ctc;
    }

    public void setMonty(String monty) {
        this.monty = monty;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public GetMemberInfoPresenter(Context context, GetMemberInfoView loadMemberInfoView, String type) {
        this.context = context;
        getMemberInfoBiz = new GetMemberInfoBiz(context);
        this.loadMemberInfoView = loadMemberInfoView;
        this.type = type;
    }

    public GetMemberInfoPresenter(Context context, GetMemberInfoView loadMemberInfoView) {
        this.context = context;
        getMemberInfoBiz = new GetMemberInfoBiz(context,this);
        this.loadMemberInfoView = loadMemberInfoView;
    }


    /**
     * 登录获取会员信息 并且记录 上次登录的模式
     */
    public void getMemberInfo() {
        getMemberInfoBiz.getMemberInfo(new GetMemberInfoListener() {
            @Override
            public void loadSuccess() {
                Handler dataHandler = new Handler(
                        context.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
                        if(type.equals("phoneLogin"))
                        {
                            context.getSharedPreferences("login_mode",Context.MODE_PRIVATE).edit().putString("type","phoneLogin").putString("username",((LoginMainActivity)context).getusername())
                            .putString("password",((LoginMainActivity)context).getpassword()).commit();
                        }else if(type.equals("wxLogin"))
                        {
                            context.getSharedPreferences("login_mode",Context.MODE_PRIVATE).edit().putString("type","wxLogin").putString("openId",((WXEntryActivity)context).getOpenid()).commit();
                        }else if(type.equals("qqLogin"))
                        {
                            context.getSharedPreferences("login_mode",Context.MODE_PRIVATE).edit().putString("type","qqLogin").putString("openId",((LoginMainActivity)context).getOpenID()).commit();
                        }else if(type.equals("sms"))
                        {
                            context.getSharedPreferences("login_mode",Context.MODE_PRIVATE).edit().putString("type","sms").putString("sms",((LoginMainActivity)context).getSms()).commit();
                        }
                        loadMemberInfoView.loadMemberInfoSuccess();
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }

            @Override
            public void loadFail() {
                Handler dataHandler = new Handler(
                        context.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
                        loadMemberInfoView.loadMemberInfoFail();
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }

            @Override
            public void showMsg(String msg) {

            }

            @Override
            public void loadRelationshipInfoSuccess(String share, String tongzhuo, String tongzu, String tongban) {

            }

            @Override
            public void loadExchangerateInfoSuccess(String price) {

            }
        });
    }

    /**
     * 免登陆获取会员信息
     */
    public void VerificationTokenGetMemberInfo() {
        getMemberInfoBiz.getMemberInfo(new GetMemberInfoListener() {
            @Override
            public void loadSuccess() {
                Handler dataHandler = new Handler(
                        context.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
                        loadMemberInfoView.loadMemberInfoSuccess();
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }

            @Override
            public void loadFail() {
                Handler dataHandler = new Handler(
                        context.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
                        loadMemberInfoView.loadMemberInfoFail();
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }

            @Override
            public void showMsg(String msg) {

            }

            @Override
            public void loadRelationshipInfoSuccess(String share, String tongzhuo, String tongzu, String tongban) {

            }

            @Override
            public void loadExchangerateInfoSuccess(String price) {

            }
        });
    }


    /**
     * 获取会员的账户信息
     */
    public void getMemberTotalAssets() {
        loadMemberInfoView.showProgress();
        getMemberInfoBiz.getMemberTotalAssets(new GetMemberInfoListener() {
            @Override
            public void loadSuccess() {
                Handler dataHandler = new Handler(
                        context.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
                        loadMemberInfoView.hideProgress();
                        loadMemberInfoView.setDemandTextView(coupon);
                        loadMemberInfoView.setRMBTextView(monty);
                        loadMemberInfoView.setTerminalCtcTextView(ctc);
                        loadMemberInfoView.setallSsetsTextView(allssets);
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }

            @Override
            public void loadFail() {
                Handler dataHandler = new Handler(
                        context.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
                        loadMemberInfoView.hideProgress();
                        loadMemberInfoView.loadMemberInfoFail();
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }

            @Override
            public void showMsg(final String msg1) {
                Handler dataHandler = new Handler(
                        context.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
                        loadMemberInfoView.hideProgress();
                        loadMemberInfoView.showMsg(msg1);
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }

            @Override
            public void loadRelationshipInfoSuccess(String share, String tongzhuo, String tongzu, String tongban) {

            }

            @Override
            public void loadExchangerateInfoSuccess(String price) {

            }
        });
    }


    /**
     * 获取会员的关系信息
     */
    public void getMemberRelationshipInfo() {
        loadMemberInfoView.showProgress();
        getMemberInfoBiz.getMemberRelationship(new GetMemberInfoListener() {
            @Override
            public void loadSuccess() {

            }

            @Override
            public void loadFail() {
                Handler dataHandler = new Handler(
                        context.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
                        loadMemberInfoView.hideProgress();
                        loadMemberInfoView.loadMemberInfoFail();
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }

            @Override
            public void showMsg(final String msg1) {
                Handler dataHandler = new Handler(
                        context.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
                        loadMemberInfoView.hideProgress();
                        loadMemberInfoView.showMsg(msg1);
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }

            @Override
            public void loadRelationshipInfoSuccess(final String share, final String tongzhuo, final String tongzu, final String tongban) {
                Handler dataHandler = new Handler(
                        context.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
                        String type = LoginUser.getInstantiation(context.getApplicationContext()).getLoginUser().getType();

                        loadMemberInfoView.setTongXiaoTextView("       "+tongban);
                        loadMemberInfoView.setTongXiTextView("       "+tongzu);
                        loadMemberInfoView.setTongBanTextView("       "+tongzhuo);
                        loadMemberInfoView.setShareTextView(share);
                        if(type.equals("10"))
                        {
                            loadMemberInfoView.getTwoLevel().setVisibility(View.GONE);
                            loadMemberInfoView.getThreeLevel().setVisibility(View.GONE);
//                            TextView textView2 = loadMemberInfoView.setTongXiTextView(tongzu);
//                            textView2.setVisibility(View.INVISIBLE);
//                            TextView textView4 = loadMemberInfoView.setTongXiaoTextView(tongban);
//                            textView4.setVisibility(View.INVISIBLE);

                        }else if(type.equals("20"))
                        {
                            loadMemberInfoView.getTwoLevel().setVisibility(View.VISIBLE);
                            loadMemberInfoView.getThreeLevel().setVisibility(View.GONE);
//                            TextView textView4 = loadMemberInfoView.setTongXiaoTextView(tongban);
//                            textView4.setVisibility(View.INVISIBLE);
//                            loadMemberInfoView.getTwoLevel().setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(context,ContributionActivity.class);
//                                    intent.putExtra("mark","同系");
//                                    context.startActivity(intent);
//                                }
//                            });
                        }else
                        {
                            loadMemberInfoView.getTwoLevel().setVisibility(View.VISIBLE);
                            loadMemberInfoView.getThreeLevel().setVisibility(View.VISIBLE);
//                            loadMemberInfoView.getTwoLevel().setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(context,ContributionActivity.class);
//                                    intent.putExtra("mark","同系");
//                                    context.startActivity(intent);
//                                }
//                            });
//
//                            loadMemberInfoView.getThreeLevel().setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(context,ContributionActivity.class);
//                                    intent.putExtra("mark","同校");
//                                    context.startActivity(intent);
//                                }
//                            });
                        }
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }

            @Override
            public void loadExchangerateInfoSuccess(String price) {

            }
        });
    }


    /**
     * 获取当前卡特币价格
     */
    public void getMemberExchangerateInfo() {
        loadMemberInfoView.showProgress();
        getMemberInfoBiz.getMemberExchangerate(new GetMemberInfoListener() {
            @Override
            public void loadSuccess() {

            }

            @Override
            public void loadFail() {
                Handler dataHandler = new Handler(
                        context.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
                        loadMemberInfoView.hideProgress();
                        loadMemberInfoView.loadMemberInfoFail();
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }

            @Override
            public void showMsg(final String msg1) {
                Handler dataHandler = new Handler(
                        context.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
                        loadMemberInfoView.hideProgress();
                        loadMemberInfoView.showMsg(msg1);
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }

            @Override
            public void loadRelationshipInfoSuccess(String share, String tongzhuo, String tongzu, String tongban) {

            }

            @Override
            public void loadExchangerateInfoSuccess(final String price) {
                Handler dataHandler = new Handler(
                        context.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
                       loadMemberInfoView.setCurrentCTCPriceTextView(price);
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        });
    }


}
