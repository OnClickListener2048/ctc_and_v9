package com.tohier.cartercoin.biz;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.android.config.IContext;
import com.tohier.cartercoin.bean.DealRecord;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.listener.DealListener;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/11/30.
 */

public class DealBiz implements IDealBiz {

    private IContext context;

    public DealBiz(IContext context) {
        this.context = context;
    }


    @Override
    public void
    getDealRecord(String type, String interfaces , String pager, final DealListener listener) {
        final ArrayList<DealRecord> list  = new ArrayList<DealRecord>();
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("reality",type);
        map.put("page",pager);
        HttpConnect.post(context, interfaces, map, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){
                    JSONArray array = data.optJSONArray("data");
                    DealRecord dealRecord = null;
                    for (int i = 0 ; i < array.size() ; i++ ){
                        dealRecord = new DealRecord();
                        JSONObject obj = array.optJSONObject(i);
                        dealRecord.setMoney(obj.optString("qty"));
                        dealRecord.setProfit(obj.optString("profit"));
                        dealRecord.setOrderNum(obj.optString("code"));
                        dealRecord.setType(obj.optString("cointype"));
                        dealRecord.setState(obj.optString("rise"));
                        dealRecord.setCycle(obj.optString("type"));
                        dealRecord.setOrderTime(obj.optString("createdate"));
                        dealRecord.setOrderPrice(obj.optString("beginprice"));
                        dealRecord.setExpireTime(obj.optString("dealtime"));
                        dealRecord.setExpirePrice(obj.optString("endprice"));
                        dealRecord.setResult(obj.optString("profitorloss"));
                        dealRecord.setPayAccount(obj.optString("payaccont"));
                        list.add(dealRecord);
                    }

                    listener.success(list);
                }else{
                    listener.failure(data.optString("msg"));
                }

            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
                listener.failure(arg0.toString());
            }
        });
    }
}
