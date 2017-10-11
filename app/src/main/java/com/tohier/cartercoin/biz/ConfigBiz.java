package com.tohier.cartercoin.biz;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.android.config.IContext;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.listener.ConfigListener;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/12/7.
 */

public class ConfigBiz implements IConfigBiz {

    private IContext context;

    public ConfigBiz(IContext context) {
        this.context = context;
    }
    @Override
    public void getDealInfo(String reality,final ConfigListener listener) {
        final HashMap<String,String> map = new HashMap<String,String>();
        map.put("reality",reality);
        HttpConnect.post(context, "member_options_wallter_reality", map, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){
                    JSONArray array = data.optJSONArray("data");

                    map.put("count",array.optJSONObject(0).optString("count"));
                    map.put("profit",array.optJSONObject(0).optString("profit"));
                    map.put("accumulated",array.optJSONObject(0).optString("accumulated"));
                    map.put("allcount",array.optJSONObject(0).optString("allcount"));
                    map.put("ctccount",array.optJSONObject(0).optString("ctccount"));
                    listener.success(map);
                }else{
                    listener.fail(data.optString("msg"));
                }

            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
                listener.fail(arg0.toString());
            }
        });
    }
}
