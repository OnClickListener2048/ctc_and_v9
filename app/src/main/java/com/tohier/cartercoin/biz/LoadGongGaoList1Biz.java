package com.tohier.cartercoin.biz;

import android.content.Context;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.activity.MeActivity;
import com.tohier.cartercoin.bean.News;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.listener.LoadGongGaoListener;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 武文锴 on 2016/11/5.
 */

public class LoadGongGaoList1Biz implements ILoadGongGaoBiz {

    /**
     * 上下文对象
     **/
    private Context ctx;
    private int page1 = 1;
    private int page2 = 1;

    public LoadGongGaoList1Biz(Context context) {
        this.ctx = context;
    }

    @Override
    public void loadGongGao1(final LoadGongGaoListener loadGongGaoListener) {
        final List<News> newses = new ArrayList<News>();
        Map<String, String> par = new HashMap<String, String>();
        par.put("kid", "1");
        par.put("page", page1+"");
        par.put("id", LoginUser.getInstantiation(ctx.getApplicationContext())
                .getLoginUser().getUserId());
        HttpConnect.post(((MeActivity)ctx), "article_list", par, new Callback() {

            private String msg;

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body()
                        .string().trim());

                msg = data.getString("msg");

                if (data.get("status").equals("success")) {
                    JSONArray dataArr = data.getJSONArray("data");
                    for(int i = 0 ; i < dataArr.size() ; i ++)
                    {
                        JSONObject jsonObjNews = dataArr.optJSONObject(i);
                        if(jsonObjNews!=null)
                        {
                            String id = jsonObjNews.optString("id");
                            String title = jsonObjNews.optString("title");
                            String pic = jsonObjNews.optString("pic");
                            String clicks = jsonObjNews.optString("clicks");
                            String likes = jsonObjNews.optString("likes");
                            String createdate = jsonObjNews.optString("createdate");
                            String url = jsonObjNews.optString("url");
                            String desc = jsonObjNews.optString("description");
                            News news = new News(id, title, pic, clicks, likes, createdate,desc,url);
                            news.setStatus(jsonObjNews.optString("status"));
                            newses.add(news);
                        }
                    }
                    page1 = page1+1;
                    loadGongGaoListener.loadSuccess(newses);
                } else {
                    loadGongGaoListener.showMsg(msg);
                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                loadGongGaoListener.loadFail();
            }
        });
    }

    @Override
    public void loadGongGao2(final LoadGongGaoListener loadGongGaoListener) {
        final List<News> newses = new ArrayList<News>();
        Map<String, String> par = new HashMap<String, String>();
        par.put("kid", "1");
        par.put("page", page2+"");
        par.put("type","2");
        par.put("id", LoginUser.getInstantiation(ctx.getApplicationContext())
                .getLoginUser().getUserId());
        HttpConnect.post(((MeActivity)ctx), "article_list", par, new Callback() {

            private String msg;

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body()
                        .string().trim());

                msg = data.getString("msg");

                if (data.get("status").equals("success")) {
                    JSONArray dataArr = data.getJSONArray("data");
                    for(int i = 0 ; i < dataArr.size() ; i ++)
                    {
                        JSONObject jsonObjNews = dataArr.optJSONObject(i);
                        if(jsonObjNews!=null)
                        {
                            String kid = jsonObjNews.getString("kid");
                            String id = jsonObjNews.getString("id");
                            String title = jsonObjNews.getString("title");
                            String pic = jsonObjNews.getString("pic");
                            String clicks = jsonObjNews.getString("clicks");
                            String likes = jsonObjNews.getString("likes");
                            String createdate = jsonObjNews.getString("createdate");
                            String url = jsonObjNews.getString("url");
                            String desc = jsonObjNews.getString("description");
                            String number = jsonObjNews.getString("number");
                            News news = new News(id,title,pic,clicks,likes,createdate,desc,kid,url);
                            news.setStatus(jsonObjNews.optString("status"));
                            news.setNumber(number);
                            newses.add(news);
                        }
                    }
                    page2 = page2+1;
                    loadGongGaoListener.loadSuccess(newses);
                } else {
                    loadGongGaoListener.showMsg(msg);
                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                loadGongGaoListener.loadFail();
            }
        });
    }
}
