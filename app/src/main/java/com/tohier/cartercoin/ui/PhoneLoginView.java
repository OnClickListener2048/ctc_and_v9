package com.tohier.cartercoin.ui;

import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by 武文锴 on 2016/11/5.
 */

public interface PhoneLoginView {


    String getusername();
    String getpassword();
    void loginSuccess();
    void loginFail();
    void showProgress();
    void hideProgress();


}
