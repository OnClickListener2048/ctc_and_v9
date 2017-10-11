package com.tohier.cartercoin.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
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
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.config.LoginUser;

import java.util.Hashtable;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/4/19.
 */

public class MyQrCode extends MyBackBaseActivity {

    private ImageView iv_back2,iv_bill_erweima;
    private CircleImageView circleImageView,circleImageView2;
    private TextView tv_nickname,tv_member_level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_myqrcode_layout);

        initData();
        setUpView();

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
        super.initData();
        iv_back2 = (ImageView) this.findViewById(R.id.iv_back2);
        iv_bill_erweima = (ImageView) this.findViewById(R.id.iv_bill_erweima);
        circleImageView = (CircleImageView) this.findViewById(R.id.circleImageView);
        circleImageView2 = (CircleImageView) this.findViewById(R.id.circleImageView2);
        tv_nickname = (TextView) this.findViewById(R.id.tv_nickname);
        tv_member_level = (TextView) this.findViewById(R.id.tv_member_level);
    }

    private void setUpView() {
        if(!TextUtils.isEmpty(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getType()))
        {
            if(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getType().equals("10"))
            {
                tv_member_level.setText("注册会员");
            }else if(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getType().equals("20"))
            {
                tv_member_level.setText("铂金会员");
            }else if(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getType().equals("30"))
            {
                tv_member_level.setText("钻石会员");
            }else if(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getType().equals("40"))
            {
                tv_member_level.setText("皇冠会员");
            }
        }

        String name = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getNickName();
        if(!TextUtils.isEmpty(name)&&!name.equals("")) {
            tv_nickname.setText(name);
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

            Glide.with(this).load(headUrl).asBitmap().centerCrop().into(new BitmapImageViewTarget(circleImageView2) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    circleImageView2.setImageDrawable(circularBitmapDrawable);
                }
            });
        }

        if(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getLinkCode()!=null)
        {
            Bitmap bitmap = createCode(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getLinkCode());
            iv_bill_erweima.setImageBitmap(bitmap);
        }


        iv_back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
