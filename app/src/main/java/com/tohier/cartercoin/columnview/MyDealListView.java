package com.tohier.cartercoin.columnview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/11/11.
 */

public class MyDealListView extends ListView {
    public MyDealListView(Context context) {
        super(context);
    }

    public MyDealListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyDealListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,

                MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);

    }


}
