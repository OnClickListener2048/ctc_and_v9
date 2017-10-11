package com.tohier.cartercoin.biz;

import com.tohier.cartercoin.listener.GetMemberInfoListener;
/**
 * Created by 武文锴 on 2016/11/5.
 *
 */

public interface IGetMemberInfoBiz {

    void getMemberInfo(GetMemberInfoListener getMemberInfoListener);


    void getMemberTotalAssets(GetMemberInfoListener getMemberInfoListener);

    //关系信息
    void getMemberRelationship(GetMemberInfoListener getMemberInfoListener);


    //当前卡特币价格信息
    void getMemberExchangerate(GetMemberInfoListener getMemberInfoListener);



}
