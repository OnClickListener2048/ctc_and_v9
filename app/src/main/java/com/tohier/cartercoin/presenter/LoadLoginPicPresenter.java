package com.tohier.cartercoin.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.biz.LoadLoginPicBiz;
import com.tohier.cartercoin.listener.LoadLoginPicListener;
import com.tohier.cartercoin.ui.LoadLoginPicView;

/**
 * Created by 武文锴 on 2016/11/5.
 */

public class LoadLoginPicPresenter {

    private LoadLoginPicBiz loadLoginPicBiz;
    private LoadLoginPicView loadLoginPicView;
    /**
     * 上下文对象
     **/
    private Context context;

    public LoadLoginPicPresenter(Context context,LoadLoginPicView loadLoginPicView) {
        this.context = context;
        loadLoginPicBiz = new LoadLoginPicBiz(context);
        this.loadLoginPicView = loadLoginPicView;
    }

    public void loadPic()
    {
        loadLoginPicBiz.loadPic(new LoadLoginPicListener() {
            @Override
            public void loadSuccess() {
                    Handler dataHandler = new Handler(
                            context.getMainLooper()) {

                        @Override
                        public void handleMessage(
                                final Message msg) {
                            loadLoginPicView.loadSuccess();
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
                        loadLoginPicView.loadFail();
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        });
    }
}
