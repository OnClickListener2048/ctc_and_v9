package com.tohier.cartercoin.presenter;

import com.tohier.android.config.IContext;
import com.tohier.cartercoin.activity.ShareOptionConfigActivity;
import com.tohier.cartercoin.biz.ConfigBiz;
import com.tohier.cartercoin.listener.ConfigListener;
import com.tohier.cartercoin.ui.ConfigView;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/12/7.
 */

public class ConfigPresenter {
    private IContext context;
    private ConfigBiz configBiz;
    private ConfigView configView;


    public ConfigPresenter(IContext context, ConfigView configView) {
        this.configBiz = new ConfigBiz(context);
        this.context = context;
        this.configView = configView;
    }


    public void getDealInfo(String reality){
        configBiz.getDealInfo(reality,new ConfigListener() {
            @Override
            public void success(final HashMap<String, String> ininfo) {
                ((ShareOptionConfigActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        configView.success(ininfo);
                    }
                });
            }

            @Override
            public void fail(final String msg) {
                ((ShareOptionConfigActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        configView.fail(msg);
                    }
                });
            }
        });
    }
}
