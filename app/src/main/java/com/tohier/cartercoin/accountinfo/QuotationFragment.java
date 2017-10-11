package com.tohier.cartercoin.accountinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.android.fragment.base.BaseFragment;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.activity.QuotationTuActivity;
import com.tohier.cartercoin.activity.RevisionAccountInfoActivity2;
import com.tohier.cartercoin.config.MyNetworkConnection;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/8/9.
 */

public class QuotationFragment extends BaseFragment {

    private View view;
    private TextView tv_btc_meiyuan,tv_eth_meiyuan,tv_ltc_meiyuan;
    private TextView tv_btc_yuan,tv_eth_yuan,tv_ltc_yuan;
    private TextView tv_btc_bilv,tv_eth_bilv,tv_ltc_bilv;

    private LinearLayout linearLayout_into_btc_quotation_tu,linearLayout_into_eth_quotation_tu,linearLayout_into_ltc_quotation_tu;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_quotation,container,false);
        initData();
        setUpView();
        return view;
    }

    @Override
    public void initData() {
        tv_btc_meiyuan = (TextView) view.findViewById(R.id.tv_btc_meiyuan);
        tv_eth_meiyuan = (TextView) view.findViewById(R.id.tv_eth_meiyuan);
        tv_ltc_meiyuan = (TextView) view.findViewById(R.id.tv_ltc_meiyuan);
        tv_btc_yuan = (TextView) view.findViewById(R.id.tv_btc_yuan);
        tv_eth_yuan = (TextView) view.findViewById(R.id.tv_eth_yuan);
        tv_ltc_yuan = (TextView) view.findViewById(R.id.tv_ltc_yuan);
        tv_btc_bilv = (TextView) view.findViewById(R.id.tv_btc_bilv);
        tv_eth_bilv = (TextView) view.findViewById(R.id.tv_eth_bilv);
        tv_ltc_bilv = (TextView) view.findViewById(R.id.tv_ltc_bilv);

        linearLayout_into_btc_quotation_tu = (LinearLayout) view.findViewById(R.id.linearLayout_into_btc_quotation_tu);
        linearLayout_into_eth_quotation_tu = (LinearLayout) view.findViewById(R.id.linearLayout_into_eth_quotation_tu);
        linearLayout_into_ltc_quotation_tu = (LinearLayout) view.findViewById(R.id.linearLayout_into_ltc_quotation_tu);

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                getYuan("btc");
                getYuan("eth");
                getYuan("ltc");
                getMeiYuan("btc");
                getMeiYuan("eth");
                getMeiYuan("ltc");
                getPrice("btc","1day");
                getPrice("eth","1day");
                getPrice("ltc","1day");
            }
        };
        timer.scheduleAtFixedRate(timerTask,0,10000);
    }

    @Override
    public void onResume() {
        super.onResume();
        getYuan("btc");
        getYuan("eth");
        getYuan("ltc");
        getMeiYuan("btc");
        getMeiYuan("eth");
        getMeiYuan("ltc");
    }

    public void setUpView()
    {
        linearLayout_into_btc_quotation_tu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                     startActivity(new Intent(getActivity(), QuotationTuActivity.class).putExtra("type","0"));
            }
        });
        linearLayout_into_eth_quotation_tu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), QuotationTuActivity.class).putExtra("type","1"));
            }
        });
        linearLayout_into_ltc_quotation_tu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), QuotationTuActivity.class).putExtra("type","2"));
            }
        });
    }

    /**
     *
     * @param type
     * type == btc 比特币
     * type == eth 以太坊
     * type == ltc 莱特币
     *
     */
    private  void getPrice(final String type, String time)
    {
        MyNetworkConnection.getNetworkConnection(((RevisionAccountInfoActivity2)getActivity())).post("post", "https://www.okcoin.cn/api/v1/kline.do?symbol="+type+"_cny&type="+time+"&size=1", null, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                try{
                    String json = response.body().string();
                    final JSONArray data = JSONArray.fromObject(json);
                    if(null!=data)
                    {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                           String aa =  data.optString(0);
                                            if(!TextUtils.isEmpty(aa))
                                            {
                                                if(aa.contains("["))
                                                {
                                                    aa = aa.substring(1,aa.length());
                                                }
                                                if(aa.contains("]"))
                                                {
                                                    aa = aa.substring(0,aa.length()-1);
                                                }
                                                String[] bb = aa.split(",");
                                                Double kai =  Double.parseDouble(bb[1]);//开盘价
                                                Double guan =  Double.parseDouble(bb[4]); //收盘价

                                                if(kai!=0&&guan!=0)
                                                {
                                                    if(guan>kai)   // 正比率
                                                    {
                                                        double add = sub(guan,kai);
                                                        String strResult = divide(add, kai, 4, BigDecimal.ROUND_HALF_EVEN)*100+"";

                                                        if(strResult.contains("."))
                                                        {
                                                            if(strResult.substring(strResult.indexOf(".")+1,strResult.length()).length()>4) {
                                                                String result = strResult.substring(0, strResult.indexOf(".") + 3) + "%";
                                                                if(type.equals("btc"))
                                                                {
                                                                    tv_btc_bilv.setText("+"+result);
                                                                }else if(type.equals("eth"))
                                                                {
                                                                    tv_eth_bilv.setText("+"+result);
                                                                }else if(type.equals("ltc"))
                                                                {
                                                                    tv_ltc_bilv.setText("+"+result);
                                                                }
                                                                if(result.equals("0.0%")||result.equals("0.00%"))
                                                                {
                                                                    if(type.equals("btc"))
                                                                    {
                                                                        tv_btc_bilv.setText("0.00%");
                                                                    }else if(type.equals("eth"))
                                                                    {
                                                                        tv_eth_bilv.setText("0.00%");
                                                                    }else if(type.equals("ltc"))
                                                                    {
                                                                        tv_ltc_bilv.setText("0.00%");
                                                                    }
                                                                }
                                                            }else
                                                            {
                                                                if(type.equals("btc"))
                                                                {
                                                                    tv_btc_bilv.setText("+"+strResult+"%");
                                                                }else if(type.equals("eth"))
                                                                {
                                                                    tv_eth_bilv.setText("+"+strResult+"%");
                                                                }else if(type.equals("ltc"))
                                                                {
                                                                    tv_ltc_bilv.setText("+"+strResult+"%");
                                                                }
                                                                if(strResult.equals("0.0%")||strResult.equals("0.00%"))
                                                                {
                                                                    if(type.equals("btc"))
                                                                    {
                                                                        tv_btc_bilv.setText("0.00%");
                                                                    }else if(type.equals("eth"))
                                                                    {
                                                                        tv_eth_bilv.setText("0.00%");
                                                                    }else if(type.equals("ltc"))
                                                                    {
                                                                        tv_ltc_bilv.setText("0.00%");
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }else if(guan<kai) //副比率
                                                    {
                                                        double jian = sub(kai,guan);
                                                        String strResult = divide(jian, kai, 4, BigDecimal.ROUND_HALF_EVEN)*100+"";
                                                        if(strResult.contains("."))
                                                        {
                                                            if(strResult.substring(strResult.indexOf(".")+1,strResult.length()).length()>4)
                                                            {
                                                               String result = strResult.substring(0,strResult.indexOf(".")+3)+"%";
                                                                if(type.equals("btc"))
                                                                {
                                                                    tv_btc_bilv.setText("-"+result);
                                                                }else if(type.equals("eth"))
                                                                {
                                                                    tv_eth_bilv.setText("-"+result);
                                                                }else if(type.equals("ltc"))
                                                                {
                                                                    tv_ltc_bilv.setText("-"+result);
                                                                }
                                                                if(result.equals("0.0%")||result.equals("0.00%"))
                                                                {
                                                                    if(type.equals("btc"))
                                                                    {
                                                                        tv_btc_bilv.setText("0.00%");
                                                                    }else if(type.equals("eth"))
                                                                    {
                                                                        tv_eth_bilv.setText("0.00%");
                                                                    }else if(type.equals("ltc"))
                                                                    {
                                                                        tv_ltc_bilv.setText("0.00%");
                                                                    }
                                                                }
                                                            }else
                                                            {
                                                                if(type.equals("btc"))
                                                                {
                                                                    tv_btc_bilv.setText("-"+strResult+"%");
                                                                }else if(type.equals("eth"))
                                                                {
                                                                    tv_eth_bilv.setText("-"+strResult+"%");
                                                                }else if(type.equals("ltc"))
                                                                {
                                                                    tv_ltc_bilv.setText("-"+strResult+"%");
                                                                }
                                                                if(strResult.equals("0.0%")||strResult.equals("0.00%"))
                                                                {
                                                                    if(type.equals("btc"))
                                                                    {
                                                                        tv_btc_bilv.setText("0.00%");
                                                                    }else if(type.equals("eth"))
                                                                    {
                                                                        tv_eth_bilv.setText("0.00%");
                                                                    }else if(type.equals("ltc"))
                                                                    {
                                                                        tv_ltc_bilv.setText("0.00%");
                                                                    }
                                                                }
                                                            }
                                                        }

                                                    }else if(guan==kai)  //平
                                                    {
                                                        if(type.equals("btc"))
                                                        {
                                                            tv_btc_bilv.setText("0.00%");
                                                        }else if(type.equals("eth"))
                                                        {
                                                            tv_eth_bilv.setText("0.00%");
                                                        }else if(type.equals("ltc"))
                                                        {
                                                            tv_ltc_bilv.setText("0.00%");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    });
                    }

                }catch (Exception e){

                }
            }
        });
    }


    /**
     *
     * @param type
     * type == btc 比特币
     * type == eth 以太坊
     * type == ltc 莱特币
     *
     */
    private  void getYuan(final String type)
    {
        MyNetworkConnection.getNetworkConnection(((RevisionAccountInfoActivity2)getActivity())).post("post", "https://www.okcoin.cn/api/v1/ticker.do?symbol="+type+"_cny", null, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try{
                    String json = response.body().string();
                    JSONObject data = JSONObject.fromObject(json);
                    final String newprice = data.optJSONObject("ticker").optString("last");

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(!TextUtils.isEmpty(newprice))
                            {
                                if(type.equals("btc"))
                                {
                                    tv_btc_yuan.setText("￥"+newprice);
                                }else if(type.equals("eth"))
                                {
                                    tv_eth_yuan.setText("￥"+newprice);
                                }else if(type.equals("ltc"))
                                {
                                    tv_ltc_yuan.setText("￥"+newprice);
                                }
                            }
                        }
                    });

                }catch (Exception e){

                }
            }
        });
    }

 /**
     *
     * @param type
     * type == btc 比特币
     * type == eth 以太坊
     * type == ltc 莱特币
     *
     */
    private  void getMeiYuan(final String type)
    {
        MyNetworkConnection.getNetworkConnection(((RevisionAccountInfoActivity2)getActivity())).post("post", "https://www.okcoin.com/api/v1/ticker.do?symbol="+type+"_usd", null, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try{
                    String json = response.body().string();
                    JSONObject data = JSONObject.fromObject(json);
                    final String newprice = data.optJSONObject("ticker").optString("last");

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(!TextUtils.isEmpty(newprice))
                            {
                                if(type.equals("btc"))
                                {
                                    tv_btc_meiyuan.setText("$"+newprice);
                                }else if(type.equals("eth"))
                                {
                                    tv_eth_meiyuan.setText("$"+newprice);
                                }else if(type.equals("ltc"))
                                {
                                    tv_ltc_meiyuan.setText("$"+newprice);
                                }
                            }
                        }
                    });

                }catch (Exception e){

                }
            }
        });
    }

    /**
     * 减法
     *
     * @param var1
     * @param var2
     * @return
     */

    public static double sub(double var1, double var2) {
        BigDecimal b1 = new BigDecimal(Double.toString(var1));
        BigDecimal b2 = new BigDecimal(Double.toString(var2));
        return b1.subtract(b2).doubleValue();
    }

              /**
     111    * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     112    * 定精度，以后的数字四舍五入。舍入模式采用用户指定舍入模式
     113    * @param v1
     114    * @param v2
     115    * @param scale 表示需要精确到小数点以后几位
     116    * @param round_mode 表示用户指定的舍入模式
     117    * @return 两个参数的商
     118    */
             public static double divide(double v1,double v2,int scale, int round_mode){
                  if(scale < 0)
                      {
                           throw new IllegalArgumentException("The scale must be a positive integer or zero");
                      }
                  BigDecimal b1 = new BigDecimal(Double.toString(v1));
                   BigDecimal b2 = new BigDecimal(Double.toString(v2));
                   return b1.divide(b2, scale, round_mode).doubleValue();
           }

}
