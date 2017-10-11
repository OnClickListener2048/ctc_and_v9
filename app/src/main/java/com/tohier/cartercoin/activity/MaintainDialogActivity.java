package com.tohier.cartercoin.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import com.tohier.android.activity.base.BaseActivity;
import com.tohier.cartercoin.R;
/**
 * Created by Administrator on 2017/5/10.
 */

public class MaintainDialogActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_activity_maintain_layout);
        initData();
    }

    @Override
    public void initData() {
        ((TextView)findViewById(R.id.tv_cancel_maintain_dialog)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(!TextUtils.isEmpty(getIntent().getStringExtra("title")))
        {
            ((TextView)findViewById(R.id.tv_title)).setText(getIntent().getStringExtra("title"));
        }

        if(!TextUtils.isEmpty(getIntent().getStringExtra("content")))
        {
            ((TextView)findViewById(R.id.tv_content)).setText(getIntent().getStringExtra("content"));
        }

    }
}
