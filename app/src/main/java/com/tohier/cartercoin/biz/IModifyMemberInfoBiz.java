package com.tohier.cartercoin.biz;

import com.tohier.cartercoin.listener.ModifyMemberInfoListener;

/**
 * Created by 武文锴 on 2016/11/5.
 *
 */

public interface IModifyMemberInfoBiz {

    void modifyMemberInfo(ModifyMemberInfoListener modifyMemberInfoListener, String photo, String nickName);

}
