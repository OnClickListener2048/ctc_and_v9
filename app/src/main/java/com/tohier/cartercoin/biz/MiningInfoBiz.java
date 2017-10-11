package com.tohier.cartercoin.biz;

import android.content.Context;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.activity.NewMiningActivity;
import com.tohier.cartercoin.bean.MiningInfo;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.listener.MiningInfoListener;
import com.tohier.cartercoin.presenter.MiningInfoPresenter;
import net.sf.json.JSONObject;
import java.io.IOException;

/**
 * Created by 武文锴 on 2016/11/5.
 */

public class MiningInfoBiz implements IMningInfoBiz {

    /**
     * 上下文对象
     **/
    private Context context;
    private MiningInfoPresenter miningInfoPresenter;


    public MiningInfoBiz(Context context,MiningInfoPresenter miningInfoPresenter) {
        this.context = context;
        this.miningInfoPresenter = miningInfoPresenter;
    }

    @Override
    public void loadMiningInfo(final MiningInfoListener miningInfoListener) {
        HttpConnect.post(((NewMiningActivity)context), "member_mine_info", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                String msg = data.optString("msg");
                if (data.get("status").equals("success")) {
                    String times = data.getJSONArray("data").getJSONObject(0).optString("times");   //已挖矿时间（秒）
                    String bonus = data.getJSONArray("data").getJSONObject(0).optString("bonus");   //刷新要增加的货币的数量
                    String bonustotal = data.getJSONArray("data").getJSONObject(0).optString("bonustotal");  //之前挖币的数量
                    String refreshtimes = data.getJSONArray("data").getJSONObject(0).optString("refreshtimes");   //刷新的时间
                    String power = data.getJSONArray("data").getJSONObject(0).optString("power");    //会员算力
                    String powertotal = data.getJSONArray("data").getJSONObject(0).optString("powertotal");  //全网算力
                    String sharecount = data.getJSONArray("data").getJSONObject(0).optString("sharecount");   //分享数量
                    String energyvalue = data.getJSONArray("data").getJSONObject(0).optString("energyvalue");  //能量值
                    String active = data.getJSONArray("data").getJSONObject(0).optString("active");            //活动
                    String task = data.getJSONArray("data").getJSONObject(0).optString("task");               //任务量
                    String ctc = data.getJSONArray("data").getJSONObject(0).optString("ctc");                  //卡特币的数量
                    String totlatimes = data.getJSONArray("data").getJSONObject(0).optString("totlatimes");   //总的挖矿时间

                    MiningInfo miningInfo = new MiningInfo(times,bonus,bonustotal,refreshtimes,power,powertotal,sharecount,energyvalue,active,task,ctc,totlatimes);
                    miningInfoPresenter.setMiningInfo(miningInfo);
                    miningInfoListener.loadSuccess();
                }else
                {
                    miningInfoPresenter.setMsg(msg);
                    miningInfoListener.showMsg();
                }
            }

            @Override
            public  void onFailure(Request arg0, IOException arg1) {
                miningInfoListener.loadFail();
            }
        });
    }

    @Override
    public void startMining(final MiningInfoListener miningInfoListener) {
        HttpConnect.post(((NewMiningActivity)context), "member_mine_start", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                String msg = data.getString("msg");
                if (data.get("status").equals("success")) {
                    miningInfoListener.loadSuccess();
                }else
                {
                    miningInfoPresenter.setMsg(msg);
                    miningInfoListener.showMsg();
                }
            }

            @Override
            public  void onFailure(Request arg0, IOException arg1) {
                miningInfoListener.loadFail();
            }
        });
    }
}
