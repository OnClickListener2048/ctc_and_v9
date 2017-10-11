package com.tohier.cartercoin.luckpanranking;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.android.fragment.base.BaseFragment;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.activity.LuckPanRankingActivity;
import com.tohier.cartercoin.adapter.SpanRankingAdapter;
import com.tohier.cartercoin.bean.SpanRankingBean;
import com.tohier.cartercoin.columnview.LoadingView;
import com.tohier.cartercoin.columnview.NoDataView;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.Tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.tohier.cartercoin.R.id.cif_loading;

/**
 * Created by Administrator on 2016/12/8.
 */

public class LuckPanRankingFragment extends BaseFragment{

    private View view;
    private ListView lvMoney;
    private LoadingView gifLoading;
    private NoDataView ivNodata;

    private List<SpanRankingBean> list = new ArrayList<SpanRankingBean>();
    private SpanRankingAdapter adapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_money_ranking,container,false);

        initData();
        loadMoreRanking();
        return view;
    }

    @Override
    public void initData() {
        lvMoney = (ListView) view.findViewById(R.id.lv_money);
        ivNodata = (NoDataView) view.findViewById(R.id.iv_nodata);
        gifLoading = (LoadingView) view.findViewById(cif_loading);

        adapter = new SpanRankingAdapter(getActivity(),list);
        lvMoney.setAdapter(adapter);
    }

    /**
     * 多人排行
     *
     */
    public void loadMoreRanking()
    {
        gifLoading.setVisibility(View.VISIBLE);
        HttpConnect.post(((LuckPanRankingActivity)getActivity()), "member_roulette_count_list", null, new Callback() {
            @Override
            public void onResponse(final Response arg0) throws IOException {
                String json = arg0.body().string();
                final JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){
                    JSONArray array = data.optJSONArray("data");
                    SpanRankingBean spanRankingBean = null;
                    if (array!=null){
                        for (int i = 0 ; i < array.size() ; i++ ){
                            JSONObject obj = array.optJSONObject(i);
                            spanRankingBean = new SpanRankingBean(obj.optString("Nickname"),obj.optString("Pic"),obj.optString("co"),obj.optString("rownum"));
                            list.add(spanRankingBean);
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                gifLoading.setVisibility(View.GONE);
                                adapter.setList(list);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }


                }else{
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(!TextUtils.isEmpty(data.optString("msg")))
                            {
                                    if(data.optString("msg").equals("nodata"))
                                    {
                                        gifLoading.setVisibility(View.GONE);
                                        ivNodata.setVisibility(View.VISIBLE);
                                    }else
                                    {
                                        if(!Tools.isPhonticName(data.optString("msg")))
                                        {
                                            sToast(data.optString("msg"));
                                        }
                                    }
                            }
                        }
                    });
                }

            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gifLoading.setVisibility(View.GONE);
                    }
                });
            }
        });
    }
}
