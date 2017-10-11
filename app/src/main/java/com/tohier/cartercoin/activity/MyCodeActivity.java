package com.tohier.cartercoin.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016/12/21.
 */

public class MyCodeActivity extends MyBackBaseActivity {

    private ImageView ivBack,iv_member_level_icon,iv_bill_erweima;
    private TextView tv_member_level_msg,tv_member_name;
    private CircleImageView circleImageView;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           switch (v.getId())
           {
               case R.id.iv_back2:
                       finish();
                   break;
           }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycode_layout);

        initData();
        setUpView();
        loadWolletAddress();

//        SliderConfig mConfig = new SliderConfig.Builder()
//                .primaryColor(Color.TRANSPARENT)
//                .secondaryColor(Color.TRANSPARENT)
//                .position(SliderPosition.LEFT)
//                .edge(false)
//                .build();
//
//        ISlider iSlider = SliderUtils.attachActivity(this, mConfig);
//        mConfig.setPosition(SliderPosition.LEFT);
//        iSlider.setConfig(mConfig);
    }

    @Override
    public void initData() {
        ivBack = (ImageView) this.findViewById(R.id.iv_back2);
        circleImageView = (CircleImageView) this.findViewById(R.id.iv_member_head_img);
        iv_member_level_icon = (ImageView) this.findViewById(R.id.iv_member_level_icon);
        iv_bill_erweima = (ImageView) this.findViewById(R.id.iv_bill_erweima);
        tv_member_level_msg = (TextView) this.findViewById(R.id.tv_member_level_msg);
        tv_member_name = (TextView) this.findViewById(R.id.tv_member_name);


        if(!TextUtils.isEmpty(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getType()))
        {
            if(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getType().equals("10"))
            {
                tv_member_level_msg.setText("注册会员");
                iv_member_level_icon.setImageResource(R.mipmap.putong);
            }else if(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getType().equals("20"))
            {
                tv_member_level_msg.setText("铂金会员");
                iv_member_level_icon.setImageResource(R.mipmap.baijin);
            }else if(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getType().equals("30"))
            {
                tv_member_level_msg.setText("钻石会员");
                iv_member_level_icon.setImageResource(R.mipmap.zhuanshi);
            }else if(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getType().equals("40"))
            {
                tv_member_level_msg.setText("皇冠会员");
                iv_member_level_icon.setImageResource(R.mipmap.huangguan);
            }
        }

        String name = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getNickName();
        if(!TextUtils.isEmpty(name)&&!name.equals("")) {
               tv_member_name.setText(name);
        }

        String headUrl = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getHeadUrl();
        if(!TextUtils.isEmpty(headUrl)&&!headUrl.equals("")) {
            Glide.with(this).load(headUrl).asBitmap().centerCrop().into(new BitmapImageViewTarget(circleImageView) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    circleImageView.setImageDrawable(circularBitmapDrawable);
                }
            });
        }
    }

    private void setUpView() {
        ivBack.setOnClickListener(onClickListener);
    }

    private void loadWolletAddress() {
        HttpConnect.post(this, "member_collection_qr", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    if (data.getJSONArray("data").getJSONObject(0)
                            .getString("address") != null) {
                        final String address = data.getJSONArray("data")
                                .getJSONObject(0).getString("address");
                        Handler dataHandler = new Handler(
                                getContext().getMainLooper()) {
                            @Override
                            public void handleMessage(
                                    final Message msg)
                            {
                                Bitmap bitmap = createCode(address);
                                iv_bill_erweima.setImageBitmap(bitmap);
                            }
                        };
                        dataHandler.sendEmptyMessage(0);
                    }
                } else {
                    // sToast(data.getString("msg"));
                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
//                sToast("链接超时！");
            }
        });
    }

    private Bitmap bitMatrix2Bitmap(BitMatrix matrix) {
        int w = matrix.getWidth();
        int h = matrix.getHeight();
        int[] rawData = new int[w * h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int color = Color.WHITE;
                if (matrix.get(i, j)) {
                    color = Color.BLACK;
                }
                rawData[i + (j * w)] = color;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        bitmap.setPixels(rawData, 0, w, 0, 0, w, h);
        return bitmap;
    }

    private Bitmap createCode(String url) {
        int width = 180;
        int height = 180;
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 0);
        BitMatrix bitMatrix;
        try {

            QRCodeWriter writer = new QRCodeWriter();
            bitMatrix = writer.encode(url,// id
                    BarcodeFormat.QR_CODE, width, height, hints);
            return bitMatrix2Bitmap(bitMatrix);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                String scanResult = data.getStringExtra(CaptureActivity.EXTRA_RESULT);
                if (scanResult != null && scanResult.length() > 0) {
                    thisPersonExist(scanResult);
//                    startActivity(new Intent(ReceivablesActivity.this,PayMoneyActivity.class));
                }
            }
        }
    }

    /**
     * 验证付款人是否存在
     */
    public void thisPersonExist(final String linkcode)
    {
        Map<String, String> par = new HashMap<String, String>();
        par.put("linkcode", linkcode);
        HttpConnect.post(this, "member_payment_val", par, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success"))
                {
                        Handler dataHandler = new Handler(
                                getContext().getMainLooper()) {
                            @Override
                            public void handleMessage(
                                    final Message msg)
                            {
                                final String pic = data.getJSONArray("data").getJSONObject(0).getString("Pic");
                                final String name = data.getJSONArray("data").getJSONObject(0).getString("Name");
                                final String nickname = data.getJSONArray("data").getJSONObject(0).getString("Nickname");
                                Intent intent = new Intent(MyCodeActivity.this,PayMoneyActivity.class);
                                intent.putExtra("pic",pic);
                                intent.putExtra("nickname",nickname);
                                intent.putExtra("linkcode",linkcode);
                                startActivity(intent);
                            }
                        };
                        dataHandler.sendEmptyMessage(0);
                } else
                {
                    Handler dataHandler = new Handler(
                            getContext().getMainLooper()) {
                        @Override
                        public void handleMessage(
                                final Message msg)
                        {
                              sToast("请提供正确的二维码以便扫描");
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1)
            {
//                sToast("链接超时！");
            }
        });
    }
}
