package com.tohier.cartercoin.biz;

import android.text.TextUtils;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.android.config.IContext;
import com.tohier.cartercoin.activity.MyBaseActivity;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.MyNetworkConnection;
import com.tohier.cartercoin.listener.ShareOptionListener;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Double.parseDouble;

/**
 * Created by Administrator on 2016/11/21.
 */

public class ShareOptionBiz implements IShareOptionBiz {


    private IContext context;

    public ShareOptionBiz(IContext context) {
        this.context = context;
    }

    @Override
    public void getBtcKLineUrl( String time,String coinType,final ShareOptionListener listener) {
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("option",time);
        map.put("code",coinType);
        HttpConnect.post(context, "member_option_kline", map, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){
                    listener.successKLineUrl(data.optJSONArray("data").optJSONObject(0).optString("url"));
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



    /**
     * 获取比特币的价格
     * @param listener
     * http://api.btctrade.com/api/ticker?coin=btc
     */
    @Override
    public void getBtcPrice(final ShareOptionListener listener) {
        MyNetworkConnection.getNetworkConnection(context).post("post", "https://www.okcoin.cn/api/v1/ticker.do?symbol=btc_cny ", null, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

                listener.fail(request.toString());

            }

            @Override
            public void onResponse(Response response) throws IOException {
                try{
                    String json = response.body().string();
                    JSONObject data = JSONObject.fromObject(json);
                    listener.successPrice(data.optJSONObject("ticker").optDouble("last"),data.optString("date"));
                }catch (Exception e){

                }

//                JSONObject data = JSONObject.fromObject(json);
//                listener.successPrice(data.optDouble("last"),data.optString("time"));
            }
        });
    }

//    /**
//     * 获取期权时间段
//     * @param listener
//     */
//    @Override
//    public void getTimes(String coinType,final ShareOptionListener listener) {
//
//        final ArrayList<HashMap<String, String>> list  = new ArrayList<HashMap<String, String>>();
//
//        HashMap<String,String> par = new HashMap<String,String>();
//        par.put("id",coinType);
//        HttpConnect.post(context, "member_time_type", par, new Callback() {
//            @Override
//            public void onResponse(Response arg0) throws IOException {
//                String json = arg0.body().string();
//                JSONObject data = JSONObject.fromObject(json);
//                HashMap<String, String> map;
//                if (data.optString("status").equals("success")){
//                    JSONArray array = data.optJSONArray("data");
//                    for (int i = 0 ; i < array.size() ; i++ ){
//                        map = new HashMap<String, String>();
//                        JSONObject obj = array.optJSONObject(i);
//
//
//
//                        map.put("type",obj.optString("type"));
//                        map.put("time",obj.optString("time"));
//                        map.put("value",obj.optString("value"));
//                        list.add(map);
//                    }
//
//                    listener.successTimes(list);
//                }else{
//                    listener.fail(data.optString("msg"));
//                }
//
//            }
//            @Override
//            public void onFailure(Request arg0, IOException arg1) {
//                listener.fail(arg0.toString());
//            }
//        });
//    }


    /**
     * 获取期权时间段
     * @param listener
     */
    @Override
    public void getTimes(String coinType,final ShareOptionListener listener) {

        final ArrayList<HashMap<String, String>> list  = new ArrayList<HashMap<String, String>>();

        HttpConnect.post(context, "member_options_count_time_new", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                HashMap<String, String> map;
                if (data.optString("status").equals("success")){
                    JSONArray array = data.optJSONArray("data");
                    for (int i = 1 ; i < 4 ; i++ ){
                        map = new HashMap<String, String>();
                        map.put("type",array.optJSONObject(0).optString("type"+i));
                        map.put("time",array.optJSONObject(0).optString("time"+i));
                        map.put("value",array.optJSONObject(0).optString("value"+i));
                        list.add(map);
                    }

                    listener.successTimes(list);
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

    /**
     * 获取币种
     * @param listener
     */
    @Override
    public void getCoinType(final ShareOptionListener listener) {
        final ArrayList<HashMap<String,String>> list  = new ArrayList<HashMap<String,String>>();

        HttpConnect.post(context, "member_coin_type", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){
                    JSONArray array = data.optJSONArray("data");
                    for (int i = 0 ; i < array.size() ; i++ ){
                        HashMap<String,String> map = new HashMap<String,String>();
                        JSONObject obj = array.optJSONObject(i);
                        map.put("cointype",obj.optString("cointype"));
                        map.put("code",obj.optString("code"));

                        list.add(map);
                    }

                    listener.successCoinType(list);
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
    public void getTrends(final String type, final ShareOptionListener listener) {
        final ArrayList<HashMap<String, String>> list  = new ArrayList<HashMap<String, String>>();
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("reality",type);
        HttpConnect.post(context, "member_profit_new", map, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                try{
                    String json = arg0.body().string();
                    JSONObject data = JSONObject.fromObject(json);
                    HashMap<String, String> map;
                    if (data.optString("status").equals("success")){
                        JSONArray array = data.optJSONArray("data");
                        for (int i = 0 ; i < array.size() ; i++ ){
                            map = new HashMap<String, String>();
                            JSONObject obj = array.optJSONObject(i);
                            map.put("profit",obj.optString("profit"));
                            map.put("time",obj.optString("date"));
                            map.put("name",obj.optString("name"));
                            list.add(map);
                        }

                        listener.successTrends(list);
                    }else{
                        listener.fail(data.optString("msg"));
                    }

                }catch(Exception e){
                    getTrends(type,listener);
                }
            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
                listener.fail(arg0.toString());
            }
        });
    }



    @Override
    public void getCTC(String type, final ShareOptionListener listener) {
        String path = "";
        String id = "";
        try{
             id = LoginUser.getInstantiation(((MyBaseActivity)context).getApplicationContext()).getLoginUser().getUserId();
        }catch(Exception e){
            id = "0";
        }

        final HashMap<String,String> map = new HashMap<String,String>();
        map.put("id",id);
        if (type.equals("0")){
          path = "member_count_detial";
        }else{
            path = "member_wallet_total_simulation";
        }

        HttpConnect.post(context, path, map, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {

                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){

                    double lock = 0;
                    if(data.optJSONArray("data").optJSONObject(0).optString("lock").equals("")){
                        lock = 0;
                    }else{
                        lock = parseDouble(data.optJSONArray("data").optJSONObject(0).optString("lock"));
                    }

                    double ctc = 0.0;
                    String profitmoney = data.optJSONArray("data").optJSONObject(0).optString("profitmoney");
                    if(TextUtils.isEmpty(profitmoney)||profitmoney.equals(""))
                    {
                        ctc = 0.0;
                    }else
                    {
                        ctc = parseDouble(profitmoney);
                    }

//                    int c = (int) (ctc-lock);
//                    if (c<=0){
//                        c = 0;
//                    }
                    int money = (int) Double.parseDouble(data.optJSONArray("data").optJSONObject(0).optString("money"));
                    int ctcoption = (int) Double.parseDouble(data.optJSONArray("data").optJSONObject(0).optString("ctcoption"));
                    listener.success(ctc+"",money+"",ctcoption+"");
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


    /**
     * 期权交易
     * @param time  时间
     * @param payAccount  交易类型
     * @param shareOptionType  实盘  虚拟盘
     * @param type   时间段  1 5  15
     * @param state  买入方向
     * @param money  下注金额
     * @param price   当前比特币价格
     * @param cointype  币种
     */
    @Override
    public void optionDeal(String time ,String payAccount,String shareOptionType, String type, String state, String money,  String price, String cointype, final ShareOptionListener listener) {
        Map<String, String> par = new HashMap<String, String>();
        par.put("begintime", time );
        par.put("reality", shareOptionType );
        par.put("type", type );
        par.put("rise",state);
        par.put("beginPrice",price);
        par.put("count",money);
        par.put("cointype",cointype);
        par.put("payaccount",payAccount);
        HttpConnect.post(context, "member_options_add", par, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){
                    listener.successOptionDeal();
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
