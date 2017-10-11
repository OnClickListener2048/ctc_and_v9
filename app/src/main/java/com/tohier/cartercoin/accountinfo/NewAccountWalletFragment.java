package com.tohier.cartercoin.accountinfo;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.android.fragment.base.BaseFragment;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.activity.BillDetailsActivity;
import com.tohier.cartercoin.activity.RealNameAuthenticationActivity;
import com.tohier.cartercoin.activity.RechargeRMBActivity;
import com.tohier.cartercoin.activity.RevisionAccountInfoActivity2;
import com.tohier.cartercoin.activity.SettingPayPwdActivity;
import com.tohier.cartercoin.activity.TiXianActivity;
import com.tohier.cartercoin.activity.XuNiBiZhuanRuActivity;
import com.tohier.cartercoin.activity.ZhuanChuXuNIBiActivity;
import com.tohier.cartercoin.adapter.AccountWalletAdapter;
import com.tohier.cartercoin.bean.AccountWallet;
import com.tohier.cartercoin.columnview.LoadingView;
import com.tohier.cartercoin.columnview.NoLinkView;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.Tools;
import com.tohier.cartercoin.listener.MyItemOnclick;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Administrator WuWenKai
 * Created by Administrator on 2017/6/19.
 */

public class NewAccountWalletFragment extends BaseFragment {

    private View view;
    private ArrayList<AccountWallet> list = new ArrayList<AccountWallet>();
    private ListView listView;
    private LoadingView loadingView;
    private NoLinkView noLinkView;
    private AccountWalletAdapter accountWalletAdapter;
    private String tanin;
    private String tanout ;
    private String btcin ;
    private String btcout ;
    private String ethin ;
    private String ethout ;
    private String ltcin;
    private String ltcout;
    private String αin;
    private String αout;

    private String isPayPwd;
    private String name;

    private View view1;
    private PopupWindow window;



    private String available;
    private String pool;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.new_account_wallet_layout,container,false);
        initData();
        setUpView();
        return view;
    }

    @Override
    public void initData() {
        listView = (ListView) view.findViewById(R.id.lv_account);
        loadingView = (LoadingView) view.findViewById(R.id.loading_view);
        noLinkView = (NoLinkView) view.findViewById(R.id.iv_no_link);

        accountWalletAdapter = new AccountWalletAdapter(getActivity(),list);
        listView.setAdapter(accountWalletAdapter);

        view1 = View.inflate(getActivity(),R.layout.popupwindow_prompt_authentication,null);

        window = new PopupWindow(view1, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);





    }

    private void setUpView() {
        /**
         * 转入，转出
         */
        accountWalletAdapter.setMyItemOnClick(new MyItemOnclick() {
            @Override
            public void myItemOnClick(int position, View v) {
                if (v.getId() == R.id.tv_in){
                    String state = "0";
                    if(list.get(position).getIconName().equals("TAN 唐"))
                    {
                        if (tanin.equals("0")){
                            state = "0";
                        }else{
                            state = "1";
                        }
                    }else if(list.get(position).getIconName().equals("BTC 比特币"))
                    {
                        if (btcin.equals("0")){
                            state = "0";
                        }else{
                            state = "1";
                        }
                    }else if(list.get(position).getIconName().equals("ETH 以太坊"))
                    {
                        if (ethin.equals("0")){
                            state = "0";
                        }else{
                            state = "1";
                        }
                    }else if(list.get(position).getIconName().equals("LTC 莱特币"))
                    {
                        if (ltcin.equals("0")){
                            state = "0";
                        }else{
                            state = "1";
                        }
                    }else if(list.get(position).getIconName().equals("α 阿尔法"))
                    {
                        if (αin.equals("0")){
                            state = "0";
                        }else{
                            state = "1";
                        }
                    }


                    if (state.equals("1")){
                        startActivity(new Intent(getActivity(), XuNiBiZhuanRuActivity.class).putExtra("name",list.get(position).getIconName()));
                    }else{
                        sToast("暂未开通");
                    }

                }else  if (v.getId() == R.id.tv_out){
                    String state = "0";
                    if(list.get(position).getIconName().equals("TAN 唐"))
                    {
                        if (tanout.equals("0")){
                            state = "0";
                        }else{
                            state = "1";
                        }
                    }else if(list.get(position).getIconName().equals("BTC 比特币"))
                    {
                        if (btcout.equals("0")){
                            state = "0";
                        }else{
                            state = "1";
                        }
                    }else if(list.get(position).getIconName().equals("ETH 以太坊"))
                    {
                        if (ethout.equals("0")){
                            state = "0";
                        }else{
                            state = "1";
                        }
                    }else if(list.get(position).getIconName().equals("LTC 莱特币"))
                    {
                        if (ltcout.equals("0")){
                            state = "0";
                        }else{
                            state = "1";
                        }
                    } else if(list.get(position).getIconName().equals("阿尔法"))
                    {
                        if (αout.equals("0")){
                            state = "0";
                        }else{
                            state = "1";
                        }
                    }


                    if (state.equals("1")){

                        if(!TextUtils.isEmpty(name))
                        {
                            if(!TextUtils.isEmpty(isPayPwd)&&isPayPwd.equals("1")) //存在支付密码  要修改支付密码
                            {
                                startActivity(new Intent(getActivity(), ZhuanChuXuNIBiActivity.class).putExtra("name",list.get(position).getIconName()));
                            }else             //不存在需要设置
                            {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(getActivity(), SettingPayPwdActivity.class);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }else
                        {
                            //TODO 显示弹窗
                            show();
                        }
                    }else{
                        sToast("暂未开通");
                    }


                }else  if (v.getId() == R.id.tv_tx){
                    if(!TextUtils.isEmpty(isPayPwd)&&isPayPwd.equals("1")) //存在支付密码  要修改支付密码
                    {

                        startActivity(new Intent(getActivity(), TiXianActivity.class));
                    }else             //不存在需要设置
                    {

                        Intent intent = new Intent(getActivity(), SettingPayPwdActivity.class);
                        startActivity(intent);

                    }

                }else  if (v.getId() == R.id.tv_cz){
                    startActivity(new Intent(getActivity(), RechargeRMBActivity.class).putExtra("type","1"));
                } else if (v.getId() == R.id.tv_zd) {
                    if (list.get(position).getIconName().equals("人民币")) {
                        BillDetailsActivity.type = "1";
                    } else if (list.get(position).getIconName().equals("阿尔法")) {
                        BillDetailsActivity.type = "2";
                    } else if (list.get(position).getIconName().equals("TAN 唐")) {
                        BillDetailsActivity.type = "5";
                    } else if (list.get(position).getIconName().equals("BTC 比特币")) {
                        BillDetailsActivity.type = "6";
                    } else if (list.get(position).getIconName().equals("ETH 以太坊")) {
                        BillDetailsActivity.type = "7";
                    } else if (list.get(position).getIconName().equals("LTC 莱特币")) {
                        BillDetailsActivity.type = "8";
                    }
                    startActivity(new Intent(getActivity(), BillDetailsActivity.class));
                } else if (v.getId() == R.id.tv_zd2) {
                    if (list.get(position).getIconName().equals("人民币")) {
                        BillDetailsActivity.type = "1";
                    } else if (list.get(position).getIconName().equals("阿尔法")) {
                        BillDetailsActivity.type = "2";
                    } else if (list.get(position).getIconName().equals("TAN 唐")) {
                        BillDetailsActivity.type = "5";
                    } else if (list.get(position).getIconName().equals("BTC 比特币")) {
                        BillDetailsActivity.type = "6";
                    } else if (list.get(position).getIconName().equals("ETH 以太坊")) {
                        BillDetailsActivity.type = "7";
                    } else if (list.get(position).getIconName().equals("LTC 莱特币")) {
                        BillDetailsActivity.type = "8";
                    }
                    startActivity(new Intent(getActivity(), BillDetailsActivity.class));
                }
            }
        });


        noLinkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tools.getAPNType(getActivity()) == true){
                    noLinkView.setVisibility(View.GONE);
                    loadAccountInfo();
                    loadStatus();
                    HttpConnect.post((RevisionAccountInfoActivity2)getActivity(), "member_is_password_pay", null, new Callback() {
                        @Override
                        public void onResponse(Response arg0) throws IOException {
                            final JSONObject data = JSONObject.fromObject(arg0.body().string());
                            if (data.get("status").equals("success")) {
                                isPayPwd = data.getJSONArray("data").getJSONObject(0).getString("value");
                            } else {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Request arg0, IOException arg1) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                }
                            });
                        }
                    });
                }else{
                    noLinkView.setVisibility(View.VISIBLE);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(list.get(position).getIconName().equals("阿尔法"))
                {
                    BillDetailsActivity.type = "2";
                    startActivity(new Intent(getActivity(),BillDetailsActivity.class));
//                    startActivity(new Intent(getActivity(),CurrentCurrencyDetailActivity.class).putExtra("shouyi",available).putExtra("privateMinning",pool));
                }else{
                    if (list.get(position).getIconName().equals("人民币"))
                    {
                        BillDetailsActivity.type = "1";
                    }else if(list.get(position).getIconName().equals("TAN 唐"))
                    {
                        BillDetailsActivity.type = "5";
                    }else if(list.get(position).getIconName().equals("BTC 比特币"))
                    {
                        BillDetailsActivity.type = "6";
                    }else if(list.get(position).getIconName().equals("ETH 以太坊"))
                    {
                        BillDetailsActivity.type = "7";
                    }else if(list.get(position).getIconName().equals("LTC 莱特币"))
                    {
                        BillDetailsActivity.type = "8";
                    }
                    startActivity(new Intent(getActivity(),BillDetailsActivity.class));
                }

            }
        });
    }


    private void loadStatus(){
        HttpConnect.post(((RevisionAccountInfoActivity2)getActivity()), "member_assets_in_out", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    tanin = data.getJSONArray("data").getJSONObject(0).optString("tanin");//
                    tanout = data.getJSONArray("data").getJSONObject(0).optString("tanout");  //
                    btcin = data.getJSONArray("data").getJSONObject(0).getString("btcin") ;  //
                    btcout = data.getJSONArray("data").getJSONObject(0).optString("btcout");  //
                    ethin = data.getJSONArray("data").getJSONObject(0).getString("ethin");  //
                    ethout = data.getJSONArray("data").getJSONObject(0).optString("ethout");
                    ltcin = data.getJSONArray("data").getJSONObject(0).optString("ltcin");
                    ltcout = data.getJSONArray("data").getJSONObject(0).optString("ltcout");
                    αin = data.getJSONArray("data").getJSONObject(0).optString("ain");
                    αout = data.getJSONArray("data").getJSONObject(0).optString("aout");
                }
            }

            @Override
            public  void onFailure(Request arg0, IOException arg1) {
            }
        });
    }

    private void loadAccountInfo(){
        list.clear();
        loadingView.setVisibility(View.VISIBLE);
        HttpConnect.post(((RevisionAccountInfoActivity2)getActivity()), "member_count_detial", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    final String αMoney = data.getJSONArray("data").getJSONObject(0).optString("ctc");//α资产账户
                    final String money = data.getJSONArray("data").getJSONObject(0).optString("money");  //现金账户
                    final String TanMoney = data.getJSONArray("data").getJSONObject(0).getString("tanmoney") ;  //TAN账户
                    final String btcMoney = data.getJSONArray("data").getJSONObject(0).optString("btcmoney");  //BTC账户
                    final String ethMoney = data.getJSONArray("data").getJSONObject(0).getString("ethmoney");  //ETH账户
                    final String ltcMoney = data.getJSONArray("data").getJSONObject(0).optString("ltcmoney");  //LTC账户
                    final String allmoney = data.getJSONArray("data").getJSONObject(0).optString("allmoney");  //总资产

                    final String αMoneyNo = data.getJSONArray("data").getJSONObject(0).optString("ctclock");//α锁定账户
                    final String ltcMoneyNo = data.getJSONArray("data").getJSONObject(0).optString("ltclock");  //LTC锁定账户
                    final String TanMoneyNo = data.getJSONArray("data").getJSONObject(0).getString("tanlock") ;  //TAN锁定账户
                    final String btcMoneyNo = data.getJSONArray("data").getJSONObject(0).optString("btclock");  //BTC锁定账户
                    final String ethMoneyNo = data.getJSONArray("data").getJSONObject(0).getString("ethlock");  //ETH锁定账户

                    available = data.getJSONArray("data").getJSONObject(0).optString("ctc");//α资产账户
                    pool = data.getJSONArray("data").getJSONObject(0).getString("pool");  //私有矿池

                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {
                            if (!TextUtils.isEmpty(allmoney)){
                                ((RevisionAccountInfoActivity2)getActivity()).setAllAssets(allmoney);
                            }else{
                                ((RevisionAccountInfoActivity2)getActivity()).setAllAssets("0.00");
                            }


                            list.add(new AccountWallet("人民币",money,money,"0.00"));
                            list.add(new AccountWallet("阿尔法",αMoney,αMoney,αMoneyNo));
                            list.add(new AccountWallet("TAN 唐",TanMoney,TanMoney,TanMoneyNo));
                            list.add(new AccountWallet("BTC 比特币",btcMoney,btcMoney,btcMoneyNo));
                            list.add(new AccountWallet("ETH 以太坊",ethMoney,ethMoney,ethMoneyNo));
                            list.add(new AccountWallet("LTC 莱特币",ltcMoney,ltcMoney,ltcMoneyNo));

                            accountWalletAdapter.setList(list);
                            accountWalletAdapter.notifyDataSetChanged();
                            loadingView.setVisibility(View.GONE);
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
                            loadingView.setVisibility(View.GONE);
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

                        loadingView.setVisibility(View.GONE);
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        if (Tools.getAPNType(getActivity()) == true){
            noLinkView.setVisibility(View.GONE);
            loadAccountInfo();
            loadStatus();

            HttpConnect.post((RevisionAccountInfoActivity2)getActivity(), "member_is_password_pay", null, new Callback() {
                @Override
                public void onResponse(Response arg0) throws IOException {
                    final JSONObject data = JSONObject.fromObject(arg0.body().string());
                    if (data.get("status").equals("success")) {
                        isPayPwd = data.getJSONArray("data").getJSONObject(0).getString("value");
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Request arg0, IOException arg1) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                }
            });

        }else{
            noLinkView.setVisibility(View.VISIBLE);
        }

        HttpConnect.post((RevisionAccountInfoActivity2)getActivity(), "member_is_password_pay", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    isPayPwd = data.getJSONArray("data").getJSONObject(0).getString("value");
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
        });


        HttpConnect.post((RevisionAccountInfoActivity2)getActivity(), "member_info", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    name = data.getJSONArray("data").getJSONObject(0).getString("name");

                }
            }

            @Override
            public  void onFailure(Request arg0, IOException arg1) {

            }
        });

    }

    /**
     * 显示实名认证的提示框
     */
    public void show()
    {

        ImageView iv_into_authentication = (ImageView) view1.findViewById(R.id.iv_into_authentication);

        ImageView iv_cancel_authentication = (ImageView) view1.findViewById(R.id.iv_cancel_authentication);

        iv_cancel_authentication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });

        iv_into_authentication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),RealNameAuthenticationActivity.class));
                window.dismiss();
            }
        });

        if(!window.isShowing())
        {
            // 设置背景颜色变暗
            WindowManager.LayoutParams lp5 = getActivity().getWindow().getAttributes();
            lp5.alpha = 0.5f;
            getActivity().getWindow().setAttributes(lp5);
            window.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams lp3 = getActivity().getWindow().getAttributes();
                    lp3.alpha = 1f;
                    getActivity().getWindow().setAttributes(lp3);
                }
            });

            window.setOutsideTouchable(true);

            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0x00ffffff);
            window.setBackgroundDrawable(dw);

            // 设置popWindow的显示和消失动画
            window.setAnimationStyle(R.style.Mypopwindow_anim_style);
            // 在底部显示
            window.showAtLocation(view.findViewById(R.id.lv_account),
                    Gravity.BOTTOM, 0, 0);
        }
    }
}
