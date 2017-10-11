package com.tohier.cartercoin.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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
import java.util.Hashtable;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016/12/11.
 */

public class XuNiBiZhuanRuActivity extends MyBackBaseActivity {

    private CircleImageView iv_member_head_img;
    private TextView tv_member_name,tv_wallet_address,tv_copy,tv_service_charge;
    private Button btn_zhuanrujilu;
    private ImageView title_back,iv_wallet_address_generic_qr;
    private TextView tv_icon_name;
    /**
     * 币种
     */
    private String name;

    /**
     * 0---α
     * 1---TAN
     * 2---BTC
     * 3---ETH
     * 4---LTC
     */
    private String type;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xunibizhuanru);

        initData();
        setUpView();
        loadWalletAddress();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void initData() {
        iv_member_head_img = (CircleImageView) this.findViewById(R.id.iv_member_head_img);
        tv_member_name = (TextView) this.findViewById(R.id.tv_member_name);
        tv_wallet_address = (TextView) this.findViewById(R.id.tv_wallet_address);
        tv_copy = (TextView) this.findViewById(R.id.tv_copy);
        tv_service_charge = (TextView) this.findViewById(R.id.tv_service_charge);
        btn_zhuanrujilu = (Button) this.findViewById(R.id.btn_zhuanrujilu);
        title_back = (ImageView) this.findViewById(R.id.title_back);
        iv_wallet_address_generic_qr = (ImageView) this.findViewById(R.id.iv_wallet_address_generic_qr);
        tv_icon_name = (TextView) this.findViewById(R.id.tv_icon_name);

        name = getIntent().getStringExtra("name");
        tv_icon_name.setText(name);

        String name = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getNickName();
        if(!TextUtils.isEmpty(name)&&!name.equals("")) {
            tv_member_name.setText(name);
        }else
        {
            tv_member_name.setText("无名英雄");
        }

        String headUrl = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getHeadUrl();
        if(!TextUtils.isEmpty(headUrl)&&!headUrl.equals("")) {
            if(!XuNiBiZhuanRuActivity.this.isDestroyed())
            {
                Glide.with(this).load(headUrl).asBitmap().centerCrop().into(new BitmapImageViewTarget(iv_member_head_img) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        iv_member_head_img.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }
        }
    }

    private void setUpView() {
        btn_zhuanrujilu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(XuNiBiZhuanRuActivity.this,ZhuanRuJiLuActivity.class).putExtra("type",type));
            }
        });

        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 从API11开始android推荐使用android.content.ClipboardManager
                // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(tv_wallet_address.getText().toString().trim());
                sToast("已复制");
            }
        });
    }

    private void loadWalletAddress() {
        String code = "";
        if (name.equals("α 阿尔法")){
            code = "member_ctc_wallet_address";
            type = "0";
        }else if (name.equals("TAN 唐")){
            code = "member_tan_wallet_address";
            type = "1";
        }else if (name.equals("BTC 比特币")){
            code = "member_btc_wallet_address";
            type = "2";
        }else if (name.equals("ETH 以太坊")){
            code = "member_eth_wallet_address";
            type = "3";
        }else if (name.equals("LTC 莱特币")){
            code = "member_ltc_wallet_address";
            type = "4";
        }

        HttpConnect.post(this, code , null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                // TODO Auto-generated method stub
                JSONObject json = JSONObject.fromObject(arg0.body().string());

                if (json.get("status").equals("success")) {
                    final String address = json.optJSONArray("data").optJSONObject(0).optString("address");
                    if (!TextUtils.isEmpty(address)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_wallet_address.setText(address);
                                iv_wallet_address_generic_qr.setImageBitmap(createCode(address));
                            }
                        });

                    }

                } else {
                    XuNiBiZhuanRuActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                        }
                    });
                }

            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {

                XuNiBiZhuanRuActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                    }
                });
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


}
