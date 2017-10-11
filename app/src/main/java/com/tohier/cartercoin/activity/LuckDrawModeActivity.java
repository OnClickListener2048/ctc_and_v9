package com.tohier.cartercoin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tohier.android.activity.base.BaseActivity;
import com.tohier.cartercoin.R;

/**
 * Created by Administrator on 2017/5/27.
 */

public class LuckDrawModeActivity extends BaseActivity {

    private LinearLayout linearLayout_shouyi_luckdraw;
    private LinearLayout linearLayout_juezhan_luckdraw;
    private LinearLayout linearLayout_xiaofei_luckdraw;
    private ImageView iv_back2;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
              switch (v.getId())
              {
                  case R.id.linearLayout_xiaofei_luckdraw:
                      getSharedPreferences("luckdraw", Context.MODE_PRIVATE).edit().putString("mode","3").commit();
                      startActivity(new Intent(LuckDrawModeActivity.this,LuckPanActivity.class));
                      break;

                  case R.id.linearLayout_shouyi_luckdraw:
                      getSharedPreferences("luckdraw", Context.MODE_PRIVATE).edit().putString("mode","1").commit();
                      startActivity(new Intent(LuckDrawModeActivity.this,LuckPanActivity.class));
                      break;

                  case R.id.linearLayout_juezhan_luckdraw:
                      getSharedPreferences("luckdraw", Context.MODE_PRIVATE).edit().putString("mode","2").commit();
                      startActivity(new Intent(LuckDrawModeActivity.this,LuckPanActivity.class));
                      break;

                  case R.id.iv_back2:
                           finish();
                      break;
              }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luckdraw_mode_layout);

        initData();
    }

    @Override
    public void initData() {
        linearLayout_shouyi_luckdraw = (LinearLayout) this.findViewById(R.id.linearLayout_shouyi_luckdraw);
        linearLayout_juezhan_luckdraw = (LinearLayout) this.findViewById(R.id.linearLayout_juezhan_luckdraw);
        linearLayout_xiaofei_luckdraw = (LinearLayout) this.findViewById(R.id.linearLayout_xiaofei_luckdraw);
        iv_back2 = (ImageView) this.findViewById(R.id.iv_back2);

        iv_back2.setOnClickListener(onClickListener);
        linearLayout_shouyi_luckdraw.setOnClickListener(onClickListener);
        linearLayout_juezhan_luckdraw.setOnClickListener(onClickListener);
        linearLayout_xiaofei_luckdraw.setOnClickListener(onClickListener);
    }
}
