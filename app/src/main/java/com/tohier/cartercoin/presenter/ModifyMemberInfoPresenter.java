package com.tohier.cartercoin.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.tohier.android.config.IContext;
import com.tohier.cartercoin.biz.ModifyMemberInfoBiz;
import com.tohier.cartercoin.listener.ModifyMemberInfoListener;
/**
 * Created by 武文锴 on 2016/11/5.
 */

public class ModifyMemberInfoPresenter {

    private ModifyMemberInfoBiz modifyMemberInfoBiz;
    /**
     * 上下文对象
     **/
    private Context context;

    /**
     * 用于获取会员信息的Presenter
     */
    private GetMemberInfoPresenter getMemberInfoPresenter;

    public ModifyMemberInfoPresenter(Context context, GetMemberInfoPresenter getMemberInfoPresenter) {
        this.context = context;
        modifyMemberInfoBiz = new ModifyMemberInfoBiz(context);
        this.getMemberInfoPresenter = getMemberInfoPresenter;
    }

    /**
     * 用于微信登录 qq登录 之后 将昵称与头像传到服务器
     */
    public void modifyMemberInfo(String  photo, String nickName)
    {
        modifyMemberInfoBiz.modifyMemberInfo(new ModifyMemberInfoListener() {
            @Override
            public void loadSuccess() {
                Handler dataHandler = new Handler(
                        context.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
                        getMemberInfoPresenter.getMemberInfo();
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
//                        ((IContext)context).sToast("链接失败");
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        },photo,nickName);
    }
}
