package com.tohier.cartercoin.biz;

import com.tohier.cartercoin.listener.PhoneLoginListener;

/**
 * Created by 武文锴 on 2016/11/5.
 *
 */

public interface IPhoneLoginBiz {

    void login(String username, String password, PhoneLoginListener phoneLoginListener);

}
