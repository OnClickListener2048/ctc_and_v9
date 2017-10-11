package com.tohier.cartercoin.activity;

import android.app.FragmentTransaction;
import android.os.Bundle;

import com.tohier.android.activity.base.BaseFragmentActivity;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.spanfragment.JueZhanSpanFragment;
import com.tohier.cartercoin.spanfragment.ShouYiSpanFragment;
import com.tohier.cartercoin.spanfragment.XiaoFeiSpanFragment;

/**
 * Created by Administrator on 2017/5/27.
 */

public class TestActivity extends BaseFragmentActivity {

    private XiaoFeiSpanFragment xiaoFeiSpanFragment;
    private ShouYiSpanFragment shouYiSpanFragment;
    private JueZhanSpanFragment jueZhanSpanFragment;
    private FragmentTransaction beginTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_span_layout);
        initData();

    }

    @Override
    public void initData() {
        beginTransaction = getFragmentManager().beginTransaction();
        jueZhanSpanFragment = new JueZhanSpanFragment();
        shouYiSpanFragment = new ShouYiSpanFragment();
        xiaoFeiSpanFragment = new XiaoFeiSpanFragment();

        beginTransaction.add(R.id.relativeLayout,jueZhanSpanFragment).show(jueZhanSpanFragment).commit();
    }
}
