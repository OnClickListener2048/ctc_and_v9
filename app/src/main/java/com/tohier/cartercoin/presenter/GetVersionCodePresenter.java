package com.tohier.cartercoin.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.tohier.cartercoin.biz.GetVersionCodeBiz;
import com.tohier.cartercoin.config.UpdateManager;
import com.tohier.cartercoin.listener.GetVersionCodeListener;
import com.tohier.cartercoin.ui.UpdateDialogView;

/**
 * Created by 武文锴 on 2016/11/5.
 */

public class GetVersionCodePresenter {

    private GetVersionCodeBiz getVersionCodeBiz;
    private UpdateDialogView updateDialogView;
    /**
     * 上下文对象
     **/
    private Context context;

    public GetVersionCodePresenter(Context context, UpdateDialogView updateDialogView) {
        this.context = context;
        getVersionCodeBiz = new GetVersionCodeBiz(context);
        this.updateDialogView = updateDialogView;
    }

    public void getVersionCode()
    {
        getVersionCodeBiz.getVersionCode(new GetVersionCodeListener() {
            @Override
            public void loadSuccess(final String remark, final int androidVersion) {
                    Handler dataHandler = new Handler(
                            context.getMainLooper()) {

                        @Override
                        public void handleMessage(
                                final Message msg) {

                            UpdateManager updateManager = new UpdateManager("http://api.blacoin.cc", "fenlebao.apk");
                            if(androidVersion > updateManager
                                    .getVersionCode(context))//显示提示更新对话框
                            {
                                getVersionCodeBiz.getUrl(new GetVersionCodeListener() {
                                    @Override
                                    public void loadSuccess(final String url, int androidVersion) {
                                          if(!TextUtils.isEmpty(url))
                                          {
                                              Handler dataHandler = new Handler(
                                                      context.getMainLooper()) {

                                                  @Override
                                                  public void handleMessage(
                                                          final Message msg) {
                                                      updateDialogView.showUpdateDialog(remark,url);
                                                  }
                                              };
                                              dataHandler.sendEmptyMessage(0);
                                          }
                                    }

                                    @Override
                                    public void loadFail() {

                                    }
                                });
//                                updateDialogView.showUpdateDialog(remark);
                            }else
                            {
                                Handler dataHandler = new Handler(
                                        context.getMainLooper()) {

                                    @Override
                                    public void handleMessage(
                                            final Message msg) {
                                        updateDialogView.loadFail("已是最新版本");
                                    }
                                };
                                dataHandler.sendEmptyMessage(0);
                            }
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
                        updateDialogView.loadFail("链接超时");
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        });
    }
}
