package com.tohier.cartercoin.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tohier.cartercoin.listener.RecordListener;

/**
 * Created by Administrator on 2017/3/14.
 */

public class WXPaySuccessReceiver extends BroadcastReceiver {

    private String type;
    private RecordListener myListener;

    public WXPaySuccessReceiver(String type) {
        this.type = type;
    }

    public void setMyListener(RecordListener myListener) {
        this.myListener = myListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
         if(type!=null)
         {
             if(type.equals("购买资产包"))
             {

             }else if(type.equals("会员升级"))
             {

             }else if(type.equals("人民币充值"))
             {
//                     if(myListener!=null)
//                     {
//                         myListener.getDatas().clear();
//                         myListener.setPage(1);
//                         myListener.getJsonDate(null);
//                     }
             }
         }
    }


}
