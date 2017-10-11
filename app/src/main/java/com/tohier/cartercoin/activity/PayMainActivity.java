package com.tohier.cartercoin.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.switfpass.pay.MainApplication;
import com.switfpass.pay.activity.PayPlugin;
import com.switfpass.pay.bean.RequestMsg;
import com.switfpass.pay.service.GetPrepayIdResult;
import com.switfpass.pay.utils.MD5;
import com.switfpass.pay.utils.SignUtils;
import com.switfpass.pay.utils.Util;
import com.switfpass.pay.utils.XmlUtils;
import com.tohier.cartercoin.R;

import org.apache.commons.httpclient.NameValuePair;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PayMainActivity extends Activity
{
	 private SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ssddd");
    String TAG = "PayMainActivity";
    
    private EditText money, body, mchId, notifyUrl, orderNo, signKey, appId, seller_id, credit_pay;
    
    private Spinner spinnerPayType;
    
    private ArrayAdapter adapterPayType;//新增一个适配器
    
    private int postion = 0;
    
    private boolean isWxBack = true;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_main);


        Button button = (Button)findViewById(R.id.submitPay);
        
        money = (EditText)findViewById(R.id.money);
        
        body = (EditText)findViewById(R.id.body);
        
        mchId = (EditText)findViewById(R.id.mchId);
        
        notifyUrl = (EditText)findViewById(R.id.notifyUrl);
        
        orderNo = (EditText)findViewById(R.id.orderNo);
        
        signKey = (EditText)findViewById(R.id.signKey);
        
        appId = (EditText)findViewById(R.id.appId);
        
        spinnerPayType = (Spinner)findViewById(R.id.spinnerPayType);
        
        seller_id = (EditText)findViewById(R.id.seller_id);
        
        credit_pay = (EditText)findViewById(R.id.credit_pay);
        
        //将可选内容与ArrayAdapter连接起来
        adapterPayType = ArrayAdapter.createFromResource(this, R.array.payTypes, android.R.layout.simple_spinner_item);
        
        //设置下拉列表的风格 
        adapterPayType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        //将adapter2 添加到spinner中
        spinnerPayType.setAdapter(adapterPayType);
        
        spinnerPayType.setOnItemSelectedListener(new SpinnerXMLSelectedListener());
        
        button.setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                
                new GetPrepayIdTask().execute();
            }
        });
    }
    
    private String genSign(List<NameValuePair> params)
    {
        StringBuilder sb = new StringBuilder();
        
        int i = 0;
        for (; i < params.size() - 1; i++)
        {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append(params.get(i).getName());
        sb.append('=');
        sb.append(params.get(i).getValue());
        
        String sha1 = Util.sha1(sb.toString());
        Log.d(TAG, "genSign, sha1 = " + sha1);
        return sha1;
    }
    
    private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String, String>>
    {
        
        private ProgressDialog dialog;
        
        private String accessToken;
        
        public GetPrepayIdTask(String accessToken)
        {
            this.accessToken = accessToken;
        }
        
        public GetPrepayIdTask()
        {
        }
        
        @Override
        protected void onPreExecute()
        {
            dialog =
                ProgressDialog.show(PayMainActivity.this,
                    getString(R.string.app_tip),
                    getString(R.string.getting_prepayid));
        }
        
        @Override
        protected void onPostExecute(Map<String, String> result)
        {
            if (dialog != null)
            {
                dialog.dismiss();
            }
            if (result == null)
            {
                Toast.makeText(PayMainActivity.this, getString(R.string.get_prepayid_fail), Toast.LENGTH_LONG).show();
            }
            else
            {
                if (result.get("status").equalsIgnoreCase("0")) // 成功
                { 
                    Toast.makeText(PayMainActivity.this, R.string.get_prepayid_succ, Toast.LENGTH_LONG).show();
                     RequestMsg msg = new RequestMsg();
                    msg.setTokenId(result.get("token_id"));
                    msg.setTradeType(MainApplication.WX_APP_TYPE);
                    msg.setAppId("wx2a5538052969956e");// wx2a5538052969956e 
                    PayPlugin.unifiedAppPay(PayMainActivity.this, msg); 
                }
                else
                {
                    Toast.makeText(PayMainActivity.this, getString(R.string.get_prepayid_fail), Toast.LENGTH_LONG)
                        .show();
                }
                
            } 
            
        }
        
        @Override
        protected void onCancelled()
        {
            super.onCancelled();
        }
        
        @Override
        protected Map<String, String> doInBackground(Void... params)
        {
            // 统一预下单接口
            //            String url = String.format("https://api.weixin.qq.com/pay/genprepay?access_token=%s", accessToken);
            String url = "https://pay.swiftpass.cn/pay/gateway";
            //            String entity = getParams();
            
            String entity = getParams();
            
            Log.d(TAG, "doInBackground, url = " + url);
            Log.d(TAG, "doInBackground, entity = " + entity);
            
            GetPrepayIdResult result = new GetPrepayIdResult();
            
            byte[] buf = Util.httpPost(url, entity);
            if (buf == null || buf.length == 0)
            {
                return null;
            }
            String content = new String(buf);
            Log.d(TAG, "doInBackground, content = " + content);
            result.parseFrom(content);
            try
            {
                return XmlUtils.parse(content);
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
        }
    }
    
    private String genNonceStr()
    {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }
    
    /**
     * 组装参数
     * <功能详细描述>
     * @return
     * @see [类、类#方法、类#成员]
     */
    private String getParams()
    {
        
        Map<String, String> params = new HashMap<String, String>();
        params.put("body", "SPay收款"); // 商品名称
        params.put("service", "unified.trade.pay"); // 支付类型
        params.put("version", "2.0"); // 版本
        params.put("mch_id", "755437000006"); // 威富通商户号
        //        params.put("mch_id", mchId.getText().toString()); // 威富通商户号
        params.put("notify_url", "http://zhangwei.dev.swiftpass.cn/native-pay/testPayResult"); // 后台通知url
        params.put("nonce_str", genNonceStr()); // 随机数
//        String out_trade_no = genOutTradNo();
        String out_trade_no =dateFormat.format(new Date()).toString();
        params.put("out_trade_no", out_trade_no); //订单号
        Log.i("hehui", "out_trade_no-->" + out_trade_no);
        params.put("mch_create_ip", "127.0.0.1"); // 机器ip地址
        params.put("total_fee", money.getText().toString()); // 总金额
        params.put("device_info", "WP10000100001"); // 手Q反扫这个设备号必须要传1ff9fe53f66189a6a3f91796beae39fe
        params.put("limit_credit_pay", credit_pay.getText().toString()); // 是否禁用信用卡支付， 0：不禁用（默认），1：禁用
        String sign = createSign("7daa4babae15ae17eee90c9e", params); // 9d101c97133837e13dde2d32a5054abb 威富通密钥
        
        params.put("sign", sign); // sign签名
        
        return XmlUtils.toXml(params);
    }
    
    public String createSign(String signKey, Map<String, String> params)
    {
        StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
        SignUtils.buildPayParams(buf, params, false);
        buf.append("&key=").append(signKey);
        String preStr = buf.toString();
        String sign = "";
        // 获得签名验证结果
        try
        {
            sign = MD5.md5s(preStr).toUpperCase();
        }
        catch (Exception e)
        {
            sign = MD5.md5s(preStr).toUpperCase();
        }
        return sign;
    }
    
    class SpinnerXMLSelectedListener implements OnItemSelectedListener
    {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
        {
            postion = arg2; //记录下拉选中的位置
        }
        
        public void onNothingSelected(AdapterView<?> arg0)
        {
            
        }
        
    } 
    
//    private String genOutTradNo()
//    {
//         Random random = new Random();
////         return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
////         return  MD5.getMessageDigest(String.valueOf().getBytes());
//    }
    
    private String createSign(Map<String, String> params)
    {
        Log.i("hehui", "params-->" + params.toString());
        StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
        buildPayParams(buf, params, false);
        buf.append("&key=7daa4babae15ae17eee90c9e");
        String preStr = buf.toString();
        return MD5.md5s(preStr).toUpperCase();
    }
    
    public void buildPayParams(StringBuilder sb, Map<String, String> payParams, boolean encoding)
    {
        List<String> keys = new ArrayList<String>(payParams.keySet());
        Collections.sort(keys);
        for (String key : keys)
        {
            sb.append(key).append("=");
            if (encoding)
            {
                sb.append(urlEncode(payParams.get(key)));
            }
            else
            {
                sb.append(payParams.get(key));
            }
            sb.append("&");
        }
        sb.setLength(sb.length() - 1);
    }
    
    public String urlEncode(String str)
    {
        try
        {
            return URLEncoder.encode(str, "UTF-8");
        }
        catch (Throwable e)
        {
            return str;
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        
        //        Toast.makeText(getApplicationContext(), "pay_result-->" + data.getStringExtra("pay_result"), 0).show();
    }
}
