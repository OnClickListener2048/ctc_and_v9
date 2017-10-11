package com.tohier.cartercoin.columnview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.tohier.cartercoin.R;

/**
 * Created by Administrator on 2017/6/8.
 */

public class NoLinkView extends LinearLayout {

    private View view;

    public NoLinkView(Context context) {
        super(context);

        view = View.inflate(context, R.layout.no_link_layout,null);

        addView(view);

    }

    public NoLinkView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        view = View.inflate(context, R.layout.no_link_layout,null);

        addView(view);
    }


}
