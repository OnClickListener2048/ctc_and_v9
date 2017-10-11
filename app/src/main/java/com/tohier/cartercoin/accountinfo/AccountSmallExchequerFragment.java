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
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.Tools;

import net.sf.json.JSONObject;

import java.io.IOException;

/**
 * Created by Administrator on 2017/6/19.
 */

public class AccountSmallExchequerFragment extends BaseFragment {

    private View view;
    private TextView tv_xiaofei_account,tv_juezhan_account,tv_available_xiaofei_account,tv_available_juezhan_account;
    private LinearLayout linearLayout_into_xiaofei_flowing_water,linearLayout_into_juezhan_flowing_water;
    private TextView tv_xiaofei_locking_account,tv_juezhan_locking_account;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account_small_exchequer,container,false);

        initData();

        return view;
    }

    @Override
    public void initData() {
        tv_xiaofei_account = (TextView) view.findViewById(R.id.tv_xiaofei_account);
        tv_juezhan_account = (TextView) view.findViewById(R.id.tv_juezhan_account);
        tv_available_xiaofei_account = (TextView) view.findViewById(R.id.tv_available_xiaofei_account);
        tv_available_juezhan_account = (TextView) view.findViewById(R.id.tv_available_juezhan_account);
        tv_xiaofei_locking_account = (TextView) view.findViewById(R.id.tv_xiaofei_locking_account);
        tv_juezhan_locking_account = (TextView) view.findViewById(R.id.tv_juezhan_locking_account);

        linearLayout_into_xiaofei_flowing_water = (LinearLayout) view.findViewById(R.id.linearLayout_into_xiaofei_flowing_water);
        linearLayout_into_juezhan_flowing_water = (LinearLayout) view.findViewById(R.id.linearLayout_into_juezhan_flowing_water);

        linearLayout_into_xiaofei_flowing_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BillDetailsActivity.type = "3";
                startActivity(new Intent(getActivity(),BillDetailsActivity.class));
            }
        });

        linearLayout_into_juezhan_flowing_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BillDetailsActivity.type = "4";
                startActivity(new Intent(getActivity(),BillDetailsActivity.class));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAccountInfo();
    }

    private void loadAccountInfo()
    {
        HttpConnect.post(this, "member_count_detial", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    final String ctcoption = data.getJSONArray("data").getJSONObject(0).optString("ctcoption");  //决战账户
                    final String ctccous = data.getJSONArray("data").getJSONObject(0).optString("ctccous");  //消费账户
                    final String goldLockingAccount = data.getJSONArray("data").getJSONObject(0).optString("gold");  //消费锁定账户
                    final String silverLockingAccount = data.getJSONArray("data").getJSONObject(0).optString("silver");  //决战锁定账户

                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {

                            if(!TextUtils.isEmpty(ctcoption))
                            {
                                if (ctcoption.equals("0.00") || ctcoption.equals("0.0000") ){
                                    tv_juezhan_account.setText("0");
                                    tv_available_juezhan_account.setText("0");
                                }else{
                                    tv_juezhan_account.setText(ctcoption);
                                    tv_available_juezhan_account.setText(ctcoption);
                                }

                            }else{
                                tv_juezhan_account.setText("0");
                                tv_available_juezhan_account.setText(0);
                            }

                            if(!TextUtils.isEmpty(ctccous))
                            {
                                if (ctccous.equals("0.00") || ctccous.equals("0.0000") ){
                                    tv_xiaofei_account.setText("0");
                                    tv_available_xiaofei_account.setText("0");
                                }else{
                                    tv_xiaofei_account.setText(ctccous);
                                    tv_available_xiaofei_account.setText(ctccous);
                                }
                            }else{
                                tv_xiaofei_account.setText("0");
                                tv_available_xiaofei_account.setText("0");
                            }

                            if(!TextUtils.isEmpty(goldLockingAccount)) //TODO
                            {
                                if (goldLockingAccount.equals("0.00") || goldLockingAccount.equals("0.0000") ){
                                    tv_xiaofei_locking_account.setText("0");
                                }else{
                                    tv_xiaofei_locking_account.setText(goldLockingAccount);
                                }

                            }else{
                                tv_xiaofei_locking_account.setText("0");
                            }

                            if(!TextUtils.isEmpty(silverLockingAccount)) //TODO
                            {
                                if (silverLockingAccount.equals("0.00") || silverLockingAccount.equals("0.0000") ){
                                    tv_juezhan_locking_account.setText("0");
                                }else{
                                    tv_juezhan_locking_account.setText(goldLockingAccount);
                                }
                            }else{
                                tv_juezhan_locking_account.setText("0");
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
