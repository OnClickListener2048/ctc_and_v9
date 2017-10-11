package com.tohier.cartercoin.biz;

import android.content.Context;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.activity.MiningRankingListActivity;
import com.tohier.cartercoin.bean.RankingData;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.listener.LoadMiningRankingListDataListener;
import com.tohier.cartercoin.presenter.LoadMiningRankingListDataPresenter;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 武文锴 on 2016/11/5.
 */

public class LoadMiningRankingListDataBiz implements ILoadMiningRankingListBiz {

    /**
     * 上下文对象
     **/
    private Context context;
    private LoadMiningRankingListDataPresenter loadMiningRankingListDataPresenter;

    private List<RankingData> list_data = new ArrayList<>();

    /**
     * type 类型 用来判断请求的是那种排行
     */
    private String type;
    private String allInterfaceName;
    private String personalInterfaceName;

    public LoadMiningRankingListDataBiz(Context context,LoadMiningRankingListDataPresenter loadMiningRankingListDataPresenter,String type) {
        this.context = context;
        this.loadMiningRankingListDataPresenter = loadMiningRankingListDataPresenter;
        this.type = type;
    }

    public LoadMiningRankingListDataBiz(Context context,LoadMiningRankingListDataPresenter loadMiningRankingListDataPresenter) {
        this.context = context;
        this.loadMiningRankingListDataPresenter = loadMiningRankingListDataPresenter;
    }

    @Override
    public void loadData(final LoadMiningRankingListDataListener loadMiningRankingListDataListener) {
        if(type.equals("week"))
        {
            allInterfaceName = "member_mine_ranking_list_week";
        }else if(type.equals("month"))
        {
            allInterfaceName = "member_mine_ranking_list_month";
        }else if(type.equals("all"))
        {
            allInterfaceName = "member_mine_ranking_list";
        }else if(type.equals("active"))
        {
            allInterfaceName = "member_mine_ranking_list_activity";
        }
        HttpConnect.post((MiningRankingListActivity) context, allInterfaceName, null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                String msg = data.getString("msg");
                if (data.get("status").equals("success")) {

                    JSONArray array = data
                            .optJSONArray("data");
                    if (array.size() != 0) {
                        for (int i = 0; i < array.size(); i++) {
                            JSONObject object2 = array
                                    .optJSONObject(i);
                            RankingData rankingData = new RankingData();
                            rankingData.setImgUrl(object2
                                    .optString("pic"));
                            rankingData.setName(object2
                                    .optString("name"));
                            rankingData.setPrice(object2
                                    .optString("bonusrebatetotal"));
                            list_data.add(rankingData);
                        }
                    }
                    loadMiningRankingListDataPresenter.setListData(list_data);
                    loadMiningRankingListDataListener.loadSuccess();
                } else {
                    loadMiningRankingListDataPresenter.setMsg(msg);
                    loadMiningRankingListDataListener.showMsg();
                }

            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
                loadMiningRankingListDataListener.loadFail();
            }
        });
    }

    @Override
    public void isExitActive(final LoadMiningRankingListDataListener loadMiningRankingListDataListener) {
        HttpConnect.post((MiningRankingListActivity) context, "member_mine_ranking_list_activity_set", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {

                JSONObject object = JSONObject.fromObject(arg0.body().string());
                if (object.get("status").equals("success")) {

                    String msg = object.getString("msg");

                    JSONArray array = object.optJSONArray("data");
                    if (array.size() != 0) {
                        JSONObject object2 = array.optJSONObject(0);
                        String enable = object2.optString("enable");
                        if(enable.equals("true"))
                        {   //2009-01-01 12:00:00
                            String huoDongName = object2.optString("title");
                            String startdate = object2.optString("startdate");
                            loadMiningRankingListDataPresenter.setActiveNmae(huoDongName);
                            loadMiningRankingListDataPresenter.setActiveStartDate(startdate);
                            loadMiningRankingListDataListener.loadSuccess();
                        }else   //none
                        {
                            loadMiningRankingListDataListener.showMsg();
                        }
                    } else { // none
                        loadMiningRankingListDataListener.showMsg();
                    }
                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                loadMiningRankingListDataListener.loadFail();
            }
        });
    }

    @Override
    public void loadPersonalMining(final LoadMiningRankingListDataListener loadMiningRankingListDataListener, String type) {
        final RankingData rankingData = new RankingData();
        if(type.equals("week"))
        {
            personalInterfaceName = "member_mine_ranking_me_week";
        }else if(type.equals("month"))
        {
            personalInterfaceName = "member_mine_ranking_me_month";
        }else if(type.equals("all"))
        {
            personalInterfaceName = "member_mine_ranking_me";
        }else if(type.equals("active"))
        {
            personalInterfaceName = "member_mine_ranking_me_activity";
        }
        HttpConnect.post((MiningRankingListActivity) context, personalInterfaceName, null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                String msg = data.getString("msg");
                if (data.get("status").equals("success")) {

                    JSONArray array = data
                            .optJSONArray("data");
                    if (array.size() != 0) {
                            JSONObject object2 = array
                                    .optJSONObject(0);
                            rankingData.setImgUrl(object2
                                    .optString("pic"));
                            rankingData.setName(object2
                                    .optString("name"));
                            rankingData.setPrice(object2
                                    .optString("bonusrebatetotal"));
                        rankingData.setRanking(object2
                                    .optString("ranking"));
                        }
                        loadMiningRankingListDataListener.loadIndividualRankingSuccess(rankingData);
                    }else
                   {
                     loadMiningRankingListDataPresenter.setMsg(msg);
                     loadMiningRankingListDataListener.showMsg();
                   }
            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
                loadMiningRankingListDataListener.loadFail();
            }
        });
    }
}
