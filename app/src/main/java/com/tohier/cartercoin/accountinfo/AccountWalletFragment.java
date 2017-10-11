package com.tohier.cartercoin.accountinfo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.android.fragment.base.BaseFragment;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.activity.BillDetailsActivity;
import com.tohier.cartercoin.activity.CurrentCurrencyDetailActivity;
import com.tohier.cartercoin.activity.RechargeRMBActivity;
import com.tohier.cartercoin.activity.RevisionAccountInfoActivity;
import com.tohier.cartercoin.activity.RevisionAccountInfoActivity2;
import com.tohier.cartercoin.activity.TiXianActivity;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.Tools;

import net.sf.json.JSONObject;
import java.io.IOException;

/**
 * @author Administrator WuWenKai
 * Created by Administrator on 2017/6/19.
 */

public class AccountWalletFragment extends BaseFragment {

    private View view;
    private TextView tv_xianjin_account,tv_aerfa_account,tv_yitaifang_account,tv_bitebi_account;
    private TextView tv_available_xianjin_account,tv_available_aerfa_account;
    private LinearLayout linearLayout_into_xianjin_flowing_water,linearLayout_into_aerfa_account_detail,linearLayout_into_eth_flowing_water,linearLayout_into_btc_flowing_water;

    private TextView tv_xianjin_recharge,tv_xianjin_withdrawals;

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.linearLayout_into_xianjin_flowing_water:
                    BillDetailsActivity.type = "1";
                    startActivity(new Intent(getActivity(),BillDetailsActivity.class));
                    break;

                case R.id.linearLayout_into_aerfa_account_detail:
                    startActivity(new Intent(getActivity(),CurrentCurrencyDetailActivity.class).putExtra("shouyi",available).putExtra("privateMinning",pool));
                    break;

//                case R.id.linearLayout_into_eth_flowing_water:
////                    startActivity(new Intent(getActivity(),BillDetailsActivity.class));
//                    break;
//
//                case R.id.linearLayout_into_btc_flowing_water:
////                    startActivity(new Intent(getActivity(),BillDetailsActivity.class));
//                    break;

                case R.id.tv_xianjin_recharge:  //现金充值
                        startActivity(new Intent(getActivity(),RechargeRMBActivity.class));
                    break;

                case R.id.tv_xianjin_withdrawals:  //现金提现
                    startActivity(new Intent(getActivity(),TiXianActivity.class));
                    break;
            }
        }
    };
    private String available;
    private String pool;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account_wallet,container,false);
        initData();
        setUpView();
        return view;
    }

    @Override
    public void initData() {
        tv_xianjin_account = (TextView) view.findViewById(R.id.tv_xianjin_account);
        tv_available_xianjin_account = (TextView) view.findViewById(R.id.tv_available_xianjin_account);
        tv_aerfa_account = (TextView) view.findViewById(R.id.tv_aerfa_account);
        tv_available_aerfa_account = (TextView) view.findViewById(R.id.tv_available_aerfa_account);
//        tv_yitaifang_account = (TextView) view.findViewById(R.id.tv_yitaifang_account);
//        tv_bitebi_account = (TextView) view.findViewById(R.id.tv_bitebi_account);
        tv_xianjin_recharge = (TextView) view.findViewById(R.id.tv_xianjin_recharge);
        tv_xianjin_withdrawals = (TextView) view.findViewById(R.id.tv_xianjin_withdrawals);

        linearLayout_into_xianjin_flowing_water = (LinearLayout) view.findViewById(R.id.linearLayout_into_xianjin_flowing_water);
        linearLayout_into_aerfa_account_detail = (LinearLayout) view.findViewById(R.id.linearLayout_into_aerfa_account_detail);
//        linearLayout_into_eth_flowing_water = (LinearLayout) view.findViewById(R.id.linearLayout_into_eth_flowing_water);
//        linearLayout_into_btc_flowing_water = (LinearLayout) view.findViewById(R.id.linearLayout_into_btc_flowing_water);
        loadAccountInfo();
    }

    private void setUpView() {
        tv_xianjin_recharge.setOnClickListener(onClickListener);
        tv_xianjin_withdrawals.setOnClickListener(onClickListener);
        linearLayout_into_xianjin_flowing_water.setOnClickListener(onClickListener);
        linearLayout_into_aerfa_account_detail.setOnClickListener(onClickListener);
//        linearLayout_into_eth_flowing_water.setOnClickListener(onClickListener);
//        linearLayout_into_btc_flowing_water.setOnClickListener(onClickListener);
    }

    private void loadAccountInfo()
    {
        HttpConnect.post(this, "member_count_detial", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    available = data.getJSONArray("data").getJSONObject(0).optString("ctc");//α资产账户
//                    final String rich = data.getJSONArray("data").getJSONObject(0).getString("ctcrecat");    //复投账户
                    final String money = data.getJSONArray("data").getJSONObject(0).optString("money");  //现金账户
//                    final String field = data.getJSONArray("data").getJSONObject(0).getString("field") ;  //投资账户
                    final String ctcoption = data.getJSONArray("data").getJSONObject(0).optString("ctcoption");  //决战账户
                    pool = data.getJSONArray("data").getJSONObject(0).getString("pool");  //私有矿池
                    final String ctccous = data.getJSONArray("data").getJSONObject(0).optString("ctccous");  //消费账户
                    final String allmoney = data.getJSONArray("data").getJSONObject(0).optString("allmoney");  //总资产
//                    final String ctcpro = data.getJSONArray("data").getJSONObject(0).getString("fieldctcpro");  //投资百分比
//                    final String richpro = data.getJSONArray("data").getJSONObject(0).getString("ctcrecatpro");   //复投百分比
//                    final String moneypro = data.getJSONArray("data").getJSONObject(0).getString("moneypro");  //现金百分比
//                    final String avaictcpro = data.getJSONArray("data").getJSONObject(0).getString("ctcpro"); //α资产账户百分比
//                    final String ctccouspro = data.getJSONArray("data").getJSONObject(0).getString("ctccouspro"); //消费账户百分比
//                    final String ctcoptionpro = data.getJSONArray("data").getJSONObject(0).getString("ctcoptionpro"); //决战账户百分比
//                    final String poolpro = data.getJSONArray("data").getJSONObject(0).getString("poolpro"); //决战账户百分比

                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {


                            if(!TextUtils.isEmpty(allmoney))
                            {
                                ((RevisionAccountInfoActivity2)getActivity()).setAllAssets(allmoney);
                            }

                            if(!TextUtils.isEmpty(available))
                            {
                                tv_aerfa_account.setText(available);
                                tv_available_aerfa_account.setText("可用:"+available);
                            }else{
                                tv_aerfa_account.setText("0");
                                tv_available_aerfa_account.setText("可用:"+0);
                            }
//
//                            if(!TextUtils.isEmpty(pool))
//                            {
//                                tv_private_mining.setText(pool+" α");
//                            }else{
//                                tv_private_mining.setTextColor(0xffff0001);
//                                tv_private_mining.setText("0α");
//                            }
//
//                            //消费
//                            if(!TextUtils.isEmpty(ctccous))
//                            {
//                                tv_consumption_count.setText(ctccous+" 个");
//                            }else{
//                                tv_consumption_count.setText("0个");
//                            }
//                            //决战
//                            if(!TextUtils.isEmpty(ctcoption))
//                            {
//                                tv_droiyan_count.setText(ctcoption+" 个");
//                            }else{
//                                tv_droiyan_count.setText("0个");
//                            }
//
                            if(!TextUtils.isEmpty(money)) //TODO
                            {
                                tv_xianjin_account.setText(money);
                                tv_available_xianjin_account.setText("可用:"+money);
                            }else{
                                tv_xianjin_account.setText("0");
                                tv_available_xianjin_account.setText("可用:"+"0");
                            }
//
////                            if(!TextUtils.isEmpty(rich))
////                            {
////                                tvRichCount.setText(rich+" α");
////                            }else{
////                                tvRichCount.setText("0 RMB");
////                            }
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }else
                {
                    final String msg8 = data.optString("msg");
                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {
                            if(!TextUtils.isEmpty(msg8))
                            {
                                if(!Tools.isPhonticName(msg8))
                                {
                                    sToast(msg8);
                                }
                            }
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

}
