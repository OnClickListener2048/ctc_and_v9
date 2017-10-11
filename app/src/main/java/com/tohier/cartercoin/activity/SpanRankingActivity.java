package com.tohier.cartercoin.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.adapter.SpanRankingAdapter;
import com.tohier.cartercoin.bean.SpanRankingBean;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.Tools;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;

public class SpanRankingActivity extends MyBaseFragmentActivity{

    private CircleImageView circleImageView_touxiang;
    private TextView tv_name,tv_ranking,tv_count;
    private ListView lv_span_ranking;
    private GifImageView cif_loading;
    private ImageView iv_nodata;
    private List<SpanRankingBean> list = new ArrayList<SpanRankingBean>();
    private SpanRankingAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_span_ranking);

        initData();

    }

    @Override
    public void initData() {
        super.initData();
        circleImageView_touxiang = (CircleImageView) this.findViewById(R.id.circleImageView_touxiang);
        tv_name = (TextView) this.findViewById(R.id.tv_name);
        tv_ranking = (TextView) this.findViewById(R.id.tv_ranking);
        tv_count = (TextView) this.findViewById(R.id.tv_count);
        lv_span_ranking = (ListView) this.findViewById(R.id.lv_span_ranking);
        cif_loading = (GifImageView) this.findViewById(R.id.cif_loading);
        iv_nodata = (ImageView) this.findViewById(R.id.iv_nodata);

        adapter = new SpanRankingAdapter(SpanRankingActivity.this,list);
        lv_span_ranking.setAdapter(adapter);

        loadMeRanking();
        loadMoreRanking();
    }

    /**
     * 个人排行
     *
     */
    public void loadMeRanking()
    {
        HttpConnect.post(SpanRankingActivity.this, "member_roulette_count_list_myself", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){
                    JSONArray array = data.optJSONArray("data");
                    if (array!=null){
                        final JSONObject obj = array.optJSONObject(0);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(!TextUtils.isEmpty(obj.optString("Nickname")))
                                {
                                   tv_name.setText(obj.optString("Nickname"));
                                }

                                if(!TextUtils.isEmpty(obj.optString("Pic")))
                                {
                                    Glide.with(SpanRankingActivity.this).load(obj.optString("Pic")).asBitmap().centerCrop().into(new BitmapImageViewTarget(circleImageView_touxiang) {
                                        @Override
                                        protected void setResource(Bitmap resource) {
                                            RoundedBitmapDrawable circularBitmapDrawable =
                                                    RoundedBitmapDrawableFactory.create(SpanRankingActivity.this.getResources(), resource);
                                            circularBitmapDrawable.setCircular(true);
                                            circleImageView_touxiang.setImageDrawable(circularBitmapDrawable);
                                        }
                                    });
                                }

                                if(!TextUtils.isEmpty(obj.optString("co")))
                                {
                                    tv_count.setText(obj.optString("co")+" 次");
                                }

                                if(!TextUtils.isEmpty(obj.optString("rownum")))
                                {
                                    tv_ranking.setText("第"+obj.optString("rownum")+"名");
                                }
                            }
                        });
                    }

                }else{

                }

            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {

                   }
               });
            }
        });
    }

    /**
     * 多人排行
     *
     */
    public void loadMoreRanking()
    {
        HttpConnect.post(SpanRankingActivity.this, "member_roulette_count_list", null, new Callback() {
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cif_loading.setVisibility(View.GONE);
                                adapter.setList(list);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }


                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(!TextUtils.isEmpty(data.optString("msg")))
                            {
                                if(!Tools.isPhonticName(data.optString("msg")))
                                {
                                    if(data.optString("msg").equals("nodate"))
                                    {
                                        cif_loading.setVisibility(View.GONE);
                                        iv_nodata.setVisibility(View.VISIBLE);
                                    }else
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        cif_loading.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    public void back(View v)
    {
        finish();
    }

}
