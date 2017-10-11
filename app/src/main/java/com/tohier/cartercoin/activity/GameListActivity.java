package com.tohier.cartercoin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.android.activity.base.BaseActivity;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.config.HttpConnect;
import net.sf.json.JSONObject;
import java.io.IOException;

/**
 * Created by Administrator on 2017/5/19.
 */

public class GameListActivity extends BaseActivity {

    private ImageView iv_back2,iv_into_jiuzhan,iv_into_luckpan,iv_into_mining;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_games_list_layout);

        initData();
    }

    @Override
    public void initData() {
        iv_back2 = (ImageView) this.findViewById(R.id.iv_back2);
        iv_into_jiuzhan = (ImageView) this.findViewById(R.id.iv_into_jiuzhan);
        iv_into_luckpan = (ImageView) this.findViewById(R.id.iv_into_luckpan);
        iv_into_mining = (ImageView) this.findViewById(R.id.iv_into_mining);
        view = this.findViewById(R.id.view_test);

        iv_back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_into_jiuzhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GameListActivity.this,ShareOptionActivity.class));
            }
        });

        iv_into_luckpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getSharedPreferences("luckdraw", Context.MODE_PRIVATE).edit().putString("mode","2").commit();
                startActivity(new Intent(GameListActivity.this,NewSpanLayoutActivity2.class));
            }
        });

        iv_into_mining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getSharedPreferences("luckdraw", Context.MODE_PRIVATE).edit().putString("mode","2").commit();
                startActivity(new Intent(GameListActivity.this,NewMiningActivity.class));
            }
        });

        HttpConnect.post(this, "member_hide_list", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    final String min = data.getJSONArray("data").getJSONObject(0).getString("minning");
                    final String option = data.getJSONArray("data").getJSONObject(0).getString("options");

                    GameListActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(!TextUtils.isEmpty(min)&&min.equals("0"))
                            {
                                iv_into_mining.setVisibility(View.VISIBLE);
                                view.setVisibility(View.VISIBLE);
                            }

                            if(!TextUtils.isEmpty(option)&&option.equals("0"))
                            {
                                iv_into_jiuzhan.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {

            }
        });

    }




}
