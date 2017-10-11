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

public class NoDataView extends LinearLayout {

    private View view;
    public NoDataView(Context context) {
        super(context);

        view = View.inflate(context, R.layout.no_data_layout,null);

        addView(view);

    }

    public NoDataView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        view = View.inflate(context, R.layout.no_data_layout,null);

        addView(view);
    }


}
