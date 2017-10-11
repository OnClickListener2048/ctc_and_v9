package com.tohier.cartercoin.biz;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.android.config.IContext;
import com.tohier.cartercoin.activity.TransactionActivity;
import com.tohier.cartercoin.bean.Transaction;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.listener.AboutEntrustListener;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/12.
 */

public class EntrustBiz implements IEntrustBiz {

    private IContext context;

    public EntrustBiz(IContext context) {
        this.context = context;
    }

    @Override
    public void getEntrustData( final AboutEntrustListener listener) {

        HttpConnect.post(context, "member_trade_kline", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){
                    listener.success(data.optJSONArray("data").optJSONObject(0).optString("url"));
                }

            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
                listener.fail(arg0.toString());
            }
        });
    }

    /**
     * 交易平台买卖实时
     * @param listener
     */
    @Override
    public void getTradingNow(final AboutEntrustListener listener) {
        HttpConnect.post(context, "member_trading_now", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {

                try{
                    String json = arg0.body().string();
                    JSONObject data = JSONObject.fromObject(json);
                    if (data.optString("status").equalsIgnoreCase("success")){
                        JSONArray array = data.optJSONArray("data");
                        ArrayList<Transaction> list = new ArrayList<Transaction>();
                        Transaction transaction = new Transaction();
                        transaction.setState("买/卖");
                        transaction.setPrice("价格");
                        transaction.setNodeal("数量");
                        transaction.setCumulative("累计");
                        list.add(transaction);
                        if (array.size() >= 0){
                            for (int i = 0; i<array.size() ; i ++){
                                String nodeal = array.optJSONObject(i).optString("nodeal");
                                String deal = array.optJSONObject(i).optString("deal");
                                String name = array.optJSONObject(i).optString("name");
                                String price = array.optJSONObject(i).optString("price");
                                String qty = array.optJSONObject(i).optString("qty");
                                String tatal = array.optJSONObject(i).optString("total");

                                transaction = new Transaction(nodeal,deal,name,price,qty,tatal);
                                transaction.setPer(array.optJSONObject(i).optString("per"));
                                list.add(transaction);
                            }

                        }
                        listener.tradingSuccess(list);
                    }

                }catch(Exception e){
                    getTradingNow(listener);
                }

            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
                listener.fail(arg0.toString());
            }
        });
    }

    @Override
    public void getTradingAdd(String type, String count, String price , final AboutEntrustListener listener) {
        Map<String, String> par = new HashMap<String, String>();
        par.put("type", type );
        par.put("count",count);
        par.put("price",price);
        HttpConnect.post(context, "member_trading_add", par, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){
                    listener.success();
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
    public void getInfoAndRate(final AboutEntrustListener listener) {
        final HashMap<String,String> map = new HashMap<String,String>();
        HttpConnect.post(context, "member_rate", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                    String json = arg0.body().string();
                    JSONObject data = JSONObject.fromObject(json);
                    if (data.optString("status").equals("success")){
                        final String rate = data.optJSONArray("data").optJSONObject(0).optString("rate");

                        HttpConnect.post(context, "member_count_detial", null, new Callback() {
                            @Override
                            public void onResponse(Response arg0) throws IOException {
                                String json = arg0.body().string();
                                JSONObject data = JSONObject.fromObject(json);
                                if (data.optString("status").equals("success")){
                                    map.put("rate",rate);
                                    map.put("money",data.optJSONArray("data").optJSONObject(0).optString("money"));
                                    map.put("ctc",data.optJSONArray("data").optJSONObject(0).optString("ctc"));
                                    map.put("rich",data.optJSONArray("data").optJSONObject(0).optString("rich"));
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
    public void updateInfo(final AboutEntrustListener listener) {
        final HashMap<String,String> map = new HashMap<String,String>();
        map.put("id", LoginUser.getInstantiation((TransactionActivity) context).getLoginUser().getUserId());

        HttpConnect.post(context, "member_is_password_pay", map, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){
                    final String rate = data.optJSONArray("data").optJSONObject(0).optString("value");

                     listener.isUpdateInfo(rate);

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
    public void getCTCInfo(final AboutEntrustListener listener) {
        final HashMap<String,String> map = new HashMap<String,String>();

        HttpConnect.post(context, "member_price_max_min", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {

                try{
                    String json = arg0.body().string();
                    JSONObject data = JSONObject.fromObject(json);
                    if (data.optString("status").equals("success")){
                        map.put("minprice",data.optJSONArray("data").optJSONObject(0).optString("minprice"));
                        map.put("maxprice",data.optJSONArray("data").optJSONObject(0).optString("maxprice"));
                        map.put("volume",data.optJSONArray("data").optJSONObject(0).optString("volume"));
                        map.put("buymax",data.optJSONArray("data").optJSONObject(0).optString("buymax"));
                        map.put("sellmin",data.optJSONArray("data").optJSONObject(0).optString("sellmin"));
                        map.put("newprice",data.optJSONArray("data").optJSONObject(0).optString("newprice"));

                        listener.getCTCInfo(map);

                    }else{
                        listener.fail(data.optString("msg"));
                    }
                }catch(Exception e){
                    getCTCInfo(listener);
                }


            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
                listener.fail(arg0.toString());
            }
        });
    }

    @Override
    public void getPriceRangeAndCount(final AboutEntrustListener listener) {
        final HashMap<String,String> map = new HashMap<String,String>();

        HttpConnect.post(context, "member_price_count_max_min", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){
                    map.put("min",data.optJSONArray("data").optJSONObject(0).optString("min"));
                    map.put("max",data.optJSONArray("data").optJSONObject(0).optString("max"));
                    map.put("value",data.optJSONArray("data").optJSONObject(0).optString("value"));
                    map.put("price",data.optJSONArray("data").optJSONObject(0).optString("price"));
                    map.put("minprice",data.optJSONArray("data").optJSONObject(0).optString("pricemin"));
                    map.put("maxprice",data.optJSONArray("data").optJSONObject(0).optString("pricemax"));


                    listener.getPriceRangeAndCount(map);

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
