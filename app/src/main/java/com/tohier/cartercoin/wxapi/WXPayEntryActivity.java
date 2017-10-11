package com.tohier.cartercoin.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.activity.BuyAssetsRecordActivity;
import com.tohier.cartercoin.activity.RecordActivity;
import com.tohier.cartercoin.activity.UpgradeRecordActivity;
import com.tohier.cartercoin.adapter.RecordAdapter;
import com.tohier.cartercoin.adapter.UpgradeRecordAdapter;
import com.tohier.cartercoin.config.MyApplication;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler
{
    
    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
    
    private RelativeLayout mLayout;
    
    private TextView tvOrderNo, tvOrderTime, tvMoney;
    private IWXAPI api;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_results);
        api = WXAPIFactory.createWXAPI(this, "wx7ad749f6cba84064");//appid需换成商户自己开放平台appid
        api.handleIntent(getIntent(), this);
    }
    
    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);
    }
    
    @Override
    public void onReq(BaseReq req)
    {
    }
    
    @Override
    public void onResp(BaseResp resp)
    {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX)
        {
            // resp.errCode == -1 原因：支付错误,可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等
            // resp.errCode == -2 原因 用户取消,无需处理。发生场景：用户不支付了，点击取消，返回APP
            if (resp.errCode == 0) // 支付成功
            {
                Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
                sendBroadcast(new Intent().setAction("paysuccess"));
                finish();
            }else if(resp.errCode==-2)
            {
                finish();
            }
            finish();
        }
        finish();
    }
}