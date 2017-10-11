package com.tohier.cartercoin.biz;

import com.tohier.cartercoin.listener.SetGesturesPasswordListener;

/**
 * Created by 武文锴 on 2016/11/5.
 *
 */

public interface ISetGesturesPasswordBiz {

    void setGesturesPassword(String gesturesPassword,SetGesturesPasswordListener setGesturesPasswordListener);
    void checkGesturesPassword(String gesturesPassword,SetGesturesPasswordListener setGesturesPasswordListener);
}
