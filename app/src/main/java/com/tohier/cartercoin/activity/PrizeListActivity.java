package com.tohier.cartercoin.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import com.tohier.android.activity.base.BaseActivity;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.adapter.PrizeListAdapter;

/**
 * Created by Administrator on 2017/5/18.
 */

public class PrizeListActivity extends BaseActivity {

    private ListView lv_prize;

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0x111)
            {
                setResult(RESULT_OK);
                finish();
            }
        }
    };

    private Thread thread = new Thread()
    {
        @Override
        public void run() {
            super.run();
            handler.sendEmptyMessage(0x111);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.popupwindow_prize_list_layout);
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.78);   //高度设置为屏幕的1.0
        p.width = (int) (d.getWidth() * 0.78);    //宽度设置为屏幕的0.8
        p.alpha = 1.0f;      //设置本身透明度
        p.dimAmount = 0.5f;      //设置黑暗度
        getWindow().setAttributes(p);
        initData();
    }

    @Override
    public void initData() {
        setFinishOnTouchOutside(false);
        String names [] = getIntent().getStringArrayExtra("names");
        lv_prize = (ListView) this.findViewById(R.id.lv_prize);
        if(null!=names)
        {
            lv_prize.setAdapter(new PrizeListAdapter(names,this));
        }
        handler.postDelayed(thread,2500);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==event.KEYCODE_BACK)
        {
            setResult(RESULT_OK);
            finish();
            return true;
        }else
        {
            return super.onKeyDown(keyCode, event);
        }
    }
}
