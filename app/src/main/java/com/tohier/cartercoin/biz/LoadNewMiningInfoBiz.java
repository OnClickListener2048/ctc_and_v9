package com.tohier.cartercoin.biz;

import android.content.Context;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.android.config.IContext;
import com.tohier.cartercoin.activity.NewMiningActivity;
import com.tohier.cartercoin.bean.NewMiningInfo;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.listener.NewMiningInfoListener;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/10.
 */

public class LoadNewMiningInfoBiz implements INewMningInfoBiz {

    private Context context;

    public LoadNewMiningInfoBiz(Context context) {
        this.context = context;
    }

    @Override
    public void loadMiningInfo(final NewMiningInfoListener miningInfoListener) {
        HttpConnect.post(((IContext) context), "member_minning_bouns_ball", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject jsonObject = JSONObject.fromObject(arg0.body().string());
                String msg = jsonObject.optString("msg");
                if (jsonObject.get("status").equals("success")) {
                   if(jsonObject!=null)
                   {
                       List<NewMiningInfo> datas = new ArrayList<NewMiningInfo>();
                       JSONArray dataArr = jsonObject.optJSONArray("data");
                       if(dataArr!=null)
                       {
                           for(int i = 0 ; i < dataArr.size() ; i ++)
                           {
                               NewMiningInfo newMiningInfo = new NewMiningInfo(dataArr.getJSONObject(i).getString("id"), dataArr.getJSONObject(i).getString("endtime"),dataArr.getJSONObject(i).getString("qty"));
                               datas.add(newMiningInfo);
                           }
                           miningInfoListener.loadSuccess(datas);
                       }
                   }
                }else
                {
                    miningInfoListener.showMsg(msg);
                }
            }

            @Override
            public  void onFailure(Request arg0, IOException arg1) {
                miningInfoListener.loadFail();
            }
        });
    }

    @Override
    public void startMining(final NewMiningInfoListener miningInfoListener) {
        HttpConnect.post(((IContext) context), "member_mine_start", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                String msg = data.getString("msg");
                if (data.get("status").equals("success")) {
                    miningInfoListener.loadSuccess(null);
                }else
                {
                    miningInfoListener.showMsg(msg);
                }
            }

            @Override
            public  void onFailure(Request arg0, IOException arg1) {
                miningInfoListener.loadFail();
            }
        });
    }


}
