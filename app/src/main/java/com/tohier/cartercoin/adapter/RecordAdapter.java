package com.tohier.cartercoin.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.switfpass.pay.MainApplication;
import com.switfpass.pay.activity.PayPlugin;
import com.switfpass.pay.bean.RequestMsg;
import com.tohier.android.config.IContext;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.activity.MeActivity;
import com.tohier.cartercoin.activity.MyBaseActivity;
import com.tohier.cartercoin.activity.RecordActivity;
import com.tohier.cartercoin.activity.ZhifubaozhifuActivity;
import com.tohier.cartercoin.alipayconfig.AlipayUtils;
import com.tohier.cartercoin.alipayconfig.PayResult;
import com.tohier.cartercoin.bean.Record;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.MyApplication;
import com.tohier.cartercoin.config.MyNetworkConnection;
import com.tohier.cartercoin.listener.RecordListener;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/12/26.
 */

public class RecordAdapter extends BaseAdapter {
    private static Context context;

    private ArrayList<Record> list = new ArrayList<Record>();
    private static View view;
    static PopupWindow window = null;
    private static ImageView ivBack;

    /**
     * 0---余额 1---支付宝 2---微信 01---支付宝余额混合支付 02---微信余额混合支付
     */
    private String payMode;
    private static String type;
    private static String pid;

    /**
     * 支付宝工具类
     */
    private AlipayUtils alipayUtils;

    private static RecordListener myListener;


    private static final int SDK_PAY_FLAG = 1;
    private ViewHolder holder;


    public void setMyListener(RecordListener myListener) {
        this.myListener = myListener;
    }

    @SuppressLint("HandlerLeak")
    private static Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText((RecordActivity)context, "支付成功", Toast.LENGTH_SHORT).show();

                        myListener.getDatas().clear();
                        myListener.setPage(1);
                        myListener.getJsonDate(null);

                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText((RecordActivity)context, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
//                            Toast.makeText((RecordActivity)context, "支付失败", Toast.LENGTH_SHORT).show();
                            show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };


    public RecordAdapter(Context context, ArrayList<Record> list, String type, ImageView ivBack) {
        this.context = context;
        this.list = list;
        this.type = type;
        this.ivBack = ivBack;
    }

    public ArrayList<Record> getList() {
        return list;
    }

    public void setList(ArrayList<Record> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            holder = new ViewHolder();
            convertView  = View.inflate(context, R.layout.list_record,null);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tvType = (TextView) convertView.findViewById(R.id.tv_type);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tvState = (TextView) convertView.findViewById(R.id.tv_state);
            holder.tvDelete = (TextView) convertView.findViewById(R.id.tv_delete);
            holder.view = convertView.findViewById(R.id.view);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String[] str = list.get(position).getTime().split(" ");
        holder.tvPrice.setText(list.get(position).getMoney());
        holder.tvTime.setText(str[0]+"\n"+str[1]);
        holder.tvType.setText(list.get(position).getType());
        holder.tvState.setText(list.get(position).getState());
        if (list.get(position).getState().equals("重新支付")){
            holder.tvState.setTextColor(0xffff0000);
            holder.tvState.setOnClickListener(new MyCLickListener(position));

            holder.view.setVisibility(View.VISIBLE);
            holder.tvDelete.setVisibility(View.VISIBLE);
            holder.tvDelete.setOnClickListener(new MyCLickListener(position));
        }else{
            holder.tvState.setTextColor(0xff009900);
            holder.view.setVisibility(View.GONE);
            holder.tvDelete.setVisibility(View.GONE);
        }

        return convertView;
    }

    private class MyCLickListener implements View.OnClickListener {
        private int position;

        public MyCLickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            view = View.inflate(context,R.layout.activity_con_firm_pay,null);
            window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT);
            switch(v.getId()){
                case R.id.tv_state:
                    String type1 = list.get(position).getType();
                    pid = list.get(position).getId();

                    if (type1.equals("支付宝")){
//                aliPay(pid,type);

                        Intent intent = new Intent(context, ZhifubaozhifuActivity.class);
                        context.startActivity(intent);
                    }else{
                        //微信支付
                        getWechatPaySign(pid,type);
                    }
                    break;
                case R.id.tv_delete:
                    holder.tvDelete.setClickable(false);
                    HashMap<String,String> map = new HashMap<String,String>();
                    map.put("id",list.get(position).getId());
                    HttpConnect.post(((MyBaseActivity)context), "member_cashin_list_cancel", map, new Callback() {
                        @Override
                        public void onResponse(Response arg0) throws IOException {
                            String json = arg0.body().string();
                            JSONObject data = JSONObject.fromObject(json);
                            if (data.optString("status").equals("success")){

                                ((MyBaseActivity)context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        list.remove(position);
                                        notifyDataSetChanged();
                                        ((MyBaseActivity) context).sToast("已取消订单");
                                        holder.tvDelete.setClickable(true);
                                    }
                                });

                            }else{
                                ((MyBaseActivity)context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        ((MyBaseActivity) context).sToast("取消订单失败");
                                        holder.tvDelete.setClickable(true);
                                    }
                                });

                            }

                        }
                        @Override
                        public void onFailure(Request arg0, IOException arg1) {
                            ((MyBaseActivity)context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((MyBaseActivity) context).sToast("取消订单失败");
                                }
                            });
                        }
                    });
                    break;
            }



        }
    }


    /**
     * 支付宝支付
     * @param pid
     * @param type
     */
    private static void aliPay(final String pid, final String type){
        if (TextUtils.isEmpty(MyApplication.PARTNER) || TextUtils.isEmpty(MyApplication.RSA_PRIVATE) || TextUtils.isEmpty(MyApplication.SELLER)) {
            new AlertDialog.Builder(context).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            ((RecordActivity)context).finish();
                        }
                    }).show();
            return;
        }

        ((RecordActivity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {

                HashMap<String, String > map = new HashMap<String, String>();
                map.put("pid",pid);
                map.put("type",type);
                MyNetworkConnection.getNetworkConnection((RecordActivity)context).post("post", MeActivity.payUrl, map, new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        String json = response.body().string();
                        JSONObject data = JSONObject.fromObject(json);
                        String sign = data.optJSONArray("data").optJSONObject(0).optString("sign");
                        final String sign1 = sign.replace('$','"');
                        Runnable payRunnable = new Runnable() {

                            @Override
                            public void run() {
                                // 构造PayTask 对象
                                PayTask alipay = new PayTask((RecordActivity)context);
                                // 调用支付接口，获取支付结果
                                String result = alipay.pay(sign1, true);

                                Message msg = new Message();
                                msg.what = SDK_PAY_FLAG;
                                msg.obj = result;
                                mHandler.sendMessage(msg);
                            }
                        };

                        Thread payThread = new Thread(payRunnable);
                        payThread.start();

                    }
                });
            }
        });
    }


    /**
     * 显示取消支付的提示框
     */
    public static void show()
    {

        Button btnCencel = (Button) view.findViewById(R.id.btn_cancel);
        Button btnCommit = (Button) view.findViewById(R.id.btn_commit);

        btnCencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });

        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWechatPaySign(pid,type);
                window.dismiss();
            }
        });

        if(!window.isShowing())
        {
            // 设置背景颜色变暗
            WindowManager.LayoutParams lp5 = ((MyBaseActivity)context).getWindow().getAttributes();
            lp5.alpha = 0.5f;
            ((MyBaseActivity)context).getWindow().setAttributes(lp5);
            window.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams lp3 = ((MyBaseActivity)context).getWindow().getAttributes();
                    lp3.alpha = 1f;
                    ((MyBaseActivity)context).getWindow().setAttributes(lp3);
                }
            });

            window.setOutsideTouchable(true);

            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0x00ffffff);
            window.setBackgroundDrawable(dw);

            // 在底部显示
            window.showAtLocation(ivBack, Gravity.BOTTOM, 0, 0);
        }
    }

    private static void getWechatPaySign(final String pid, final String type){
        HashMap<String, String > map = new HashMap<String, String>();
        map.put("pid",pid);
        map.put("type",type);

        MyNetworkConnection.getNetworkConnection(((IContext) context)).post("post", MeActivity.wxpPayUrl, map, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String json = response.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if(data.getString("status").equals("success"))
                {

                    final String tokenid = data.optJSONArray("data").optJSONObject(0).optString("tokenid");
                    final String services = data.optJSONArray("data").optJSONObject(0).optString("services");

                    ((Activity)context). runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            RequestMsg msg = new RequestMsg();
                            msg.setTokenId(tokenid);
                            msg.setTradeType(MainApplication.WX_APP_TYPE);
                            msg.setAppId("wx7ad749f6cba84064");
                            PayPlugin.unifiedAppPay(((Activity) context), msg);
                        }
                    });
                }else
                {
                    ((Activity)context). runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                }
            }
        });
    }

    private class ViewHolder{
        private TextView tvState;
        private TextView tvTime;
        private TextView tvPrice;
        private TextView tvType;
        private TextView tvDelete;
        private View view;
    }
}
