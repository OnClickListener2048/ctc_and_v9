package com.tohier.cartercoin.biz;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.android.config.IContext;
import com.tohier.cartercoin.bean.UserShareOptionRanking;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.listener.ShareOptionRankingListener;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by Administrator on 2016/12/8.
 */

public class ShareOptionRankingBiz implements IShareOptionRankingBiz {

    private IContext context;

    public ShareOptionRankingBiz(IContext context) {
        this.context = context;
    }

    @Override
    public void getAllRanking(String code, final ShareOptionRankingListener listener) {
        final ArrayList<UserShareOptionRanking> list  = new ArrayList<UserShareOptionRanking>();
        HttpConnect.post(context, code, null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){
                    JSONArray array = data.optJSONArray("data");
                    UserShareOptionRanking userShareOptionRanking = null;
                    if (array!=null){
                        for (int i = 0 ; i < array.size() ; i++ ){
                            userShareOptionRanking = new UserShareOptionRanking();
                            JSONObject obj = array.optJSONObject(i);
                            userShareOptionRanking.setId(obj.optString("id"));
                            userShareOptionRanking.setRanking(obj.optString("rownum"));
                            userShareOptionRanking.setCount(obj.optString("profit"));
                            userShareOptionRanking.setHeadUrl(obj.optString("pic"));
                            userShareOptionRanking.setNick(obj.optString("name"));

                            list.add(userShareOptionRanking);
                        }
                    }


                    listener.successRanking(list);
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

    @Override
    public void getSelfRanking(String code, final ShareOptionRankingListener listener) {

        HttpConnect.post(context, code, null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){
                    JSONArray array = data.optJSONArray("data");
                    UserShareOptionRanking userShareOptionRanking = new UserShareOptionRanking();
                    if (array!=null){
                        JSONObject obj = array.optJSONObject(0);
                        userShareOptionRanking.setId(obj.optString("id"));
                        userShareOptionRanking.setRanking(obj.optString("rownum"));
                        userShareOptionRanking.setCount(obj.optString("profit"));
                        userShareOptionRanking.setHeadUrl(obj.optString("pic"));
                        userShareOptionRanking.setNick(obj.optString("name"));

                    }
                    listener.successSelfRanking(userShareOptionRanking);
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
