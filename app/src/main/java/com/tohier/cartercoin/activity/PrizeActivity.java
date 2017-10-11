package com.tohier.cartercoin.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import com.tohier.android.activity.base.BaseActivity;
import com.tohier.cartercoin.R;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2017/5/18.
 */

public class PrizeActivity extends BaseActivity {

    private ImageView imageView;
    private TextView textView;
    private String prizeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popupwindow_prize_layout);

        initData();
    }

    @Override
    public void initData() {
        imageView = (ImageView) findViewById(R.id.iv_prize_pictures);
        textView = (TextView) findViewById(R.id.iv_prize_name);

        setFinishOnTouchOutside(false);

        prizeName = getIntent().getStringExtra("prizeName");
        final String prizeUrl = getIntent().getStringExtra("prizeUrl");
        final ScaleAnimation scaleAnimation = (ScaleAnimation) AnimationUtils.loadAnimation(this, R.anim.prize_scale_in);

        if(!TextUtils.isEmpty(prizeUrl))
        {
            final Thread thread = new Thread()
            {
                @Override
                public void run() {
                    super.run();
                    final Bitmap bitmap = returnBitmap(prizeUrl);
                    if(null!=bitmap)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(bitmap);
                                imageView.startAnimation(scaleAnimation);
                                scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        Thread thread = new Thread()
                                        {
                                            @Override
                                            public void run() {
                                                super.run();
                                                try {
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            if(!TextUtils.isEmpty(prizeName))
                                                            {
                                                                textView.setText(prizeName);
                                                            }
                                                        }
                                                    });

                                                    Thread.sleep(500);
                                                    PrizeActivity.this.runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                           setResult(RESULT_OK);
                                                            finish();
                                                        }
                                                    });
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        };
                                        thread.start();
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });
                            }
                        });
                    }
                }
            };
            thread.start();
        }else
        {
            imageView.startAnimation(scaleAnimation);
            scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Thread thread = new Thread()
                    {
                        @Override
                        public void run() {
                            super.run();
                            try {
                                Thread.sleep(1000);
                                PrizeActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                });
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    thread.start();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    /**
     * 根据图片的url路径获得Bitmap对象
     * @param url
     * @return
     */
    private Bitmap returnBitmap(String url) {
        URL fileUrl = null;
        Bitmap bitmap = null;

        try {
            fileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            HttpURLConnection conn = (HttpURLConnection) fileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.setConnectTimeout(50000);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;

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
