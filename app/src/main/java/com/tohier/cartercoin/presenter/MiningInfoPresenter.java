package com.tohier.cartercoin.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.tohier.cartercoin.R;
import com.tohier.cartercoin.activity.MyBaseActivity;
import com.tohier.cartercoin.bean.MiningInfo;
import com.tohier.cartercoin.biz.MiningInfoBiz;
import com.tohier.cartercoin.listener.MiningInfoListener;
import com.tohier.cartercoin.ui.MiningInfoView;

import org.apache.commons.httpclient.util.DateUtil;

import java.sql.Date;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 武文锴 on 2016/11/5.
 */

public class MiningInfoPresenter {

    private String msg;

    private MiningInfoBiz miningInfoBiz;
    private MiningInfoView miningInfoView;
    /**
     * 上下文对象
     **/
    private Context context;

    /**
     * 会员挖矿信息的实体类对象
     */
    private MiningInfo miningInfo;
    /**
     * 已挖矿时长
     **/
    private int time;
    /**
     * 此次允许挖矿的总时长
     */
    private int totalTimes = 60;
    /**
     * 用于刷时间的定时器
     */
    private Timer totleTimeTimer;
    /**
     * 已挖币的数量
     */
    private double bonus;
    private double ctc;
    /**
     * 用于刷数据的定时器
     */
    private Timer wakuangInfoTimer;
    /**
     *该挖矿时间段总的挖矿的数量
     */
    private double bonusTotal;
    private int refreshTime;
    private double oneMiao;

    public double getCtc() {
        return ctc;
    }

    public void setCtc(double ctc) {
        this.ctc = ctc;
    }

    public void setOneMiao(double oneMiao) {
        this.oneMiao = oneMiao;
    }

    public void setMiningInfo(MiningInfo miningInfo) {
        this.miningInfo = miningInfo;
    }

    public MiningInfoPresenter(Context context, MiningInfoView miningInfoView) {
        this.context = context;
        miningInfoBiz = new MiningInfoBiz(context,this);
        this.miningInfoView = miningInfoView;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void loadMiningInfo()
    {
        miningInfoView.showProgress();
        miningInfoBiz.loadMiningInfo(new MiningInfoListener() {
            @Override
            public void loadSuccess() {
                Handler dataHandler = new Handler(
                        context.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
                        miningInfoView.hideProgress();
                        miningInfoView.getProgressBar_active_mining().setProgress((int) (Float.parseFloat(miningInfo.getActive())*100));
                        miningInfoView.getProgressBar_power_mining().setProgress((int) (Float.parseFloat(miningInfo.getEnergyvalue())*100));
                        miningInfoView.getProgressBar_share_mining().setProgress((int) (Float.parseFloat(miningInfo.getSharecount())*100));
                        miningInfoView.getProgressBar_task_mining().setProgress((int) (Float.parseFloat(miningInfo.getTask())*100));

                        miningInfoView.getTv_active_completion_percentage().setText(removeDecimalPoint(String.valueOf(Float.parseFloat(miningInfo.getActive())*100))+"%");
                        miningInfoView.getTv_power_completion_percentage().setText(removeDecimalPoint(String.valueOf(Float.parseFloat(miningInfo.getEnergyvalue())*100))+"%");
                        miningInfoView.getTv_share_completion_percentage().setText(removeDecimalPoint(String.valueOf(Float.parseFloat(miningInfo.getSharecount())*100))+"%");
                        miningInfoView.getTv_task_completion_percentage().setText(removeDecimalPoint(String.valueOf(Float.parseFloat(miningInfo.getTask())*100))+"%");

                        miningInfoView.getTv_mining_me_calculating().setText(miningInfo.getPower());
                        miningInfoView.getTv_mining_all_calculating().setText(miningInfo.getPowertotal());

                        //得到已挖矿的时间 得到此次可以挖矿的总时间
                        if(miningInfo.getTimes()!=null&&miningInfo.getTotlatimes()!=null)
                        {
                            time = Integer.parseInt(miningInfo.getTimes());

                            totalTimes = Integer.parseInt(miningInfo.getTotlatimes());
                            bonus = Double.parseDouble(miningInfo.getBonus());
                            bonusTotal = Double.parseDouble(miningInfo.getBonustotal());
                            refreshTime = Integer.parseInt(miningInfo.getRefreshtimes());
                            oneMiao = bonus/refreshTime;

                            if(Integer.parseInt(miningInfo.getTimes())<=0)   //还未开始挖矿
                            {
                                miningInfoView.getIv_mining_icon().setClickable(true);
                               return;
                            }
                            //判断 已挖矿的时间是否小于挖矿总时间   true 在挖矿中 false 挖矿结束
                            if(Integer.parseInt(miningInfo.getTimes())<Integer.parseInt(miningInfo.getTotlatimes()))  //挖矿中
                            {
                                //1.继续动画
                                //2.开启定时器
                                //3.把已挖矿的时间 与 已挖币的数量 显示上去  同时 挖矿按钮 不能点击

                                //three

                                //刚开始显示卡特币数量
                                if (totalTimes > 0 && bonusTotal >0){
                                    ctc = bonusTotal;
                                }else{

                                    ctc = 0.0000;
                                }
                                DecimalFormat df = new DecimalFormat("0.0000");
                                miningInfoView.getTv_mining_count().setText(df.format(ctc)+"");

                                miningInfoView.getTv_mining_time().setText(DateUtil.formatDate(new Date(Integer.parseInt(miningInfo.getTimes()) * 1000), "HH:mm:ss"));
                                miningInfoView.getIv_mining_icon().setBackgroundResource(R.drawable.bg_shape_c4c4c4);
                                miningInfoView.getIv_mining_icon().setText("");
                                miningInfoView.setMiningStartGifAnim(true);

                                miningInfoView.getIv_mining_icon().setClickable(false);
                                //two
                                startTimer();
                                //one TODO

                            }else                                         //停止挖矿  stopWakuang();  抽方法
                            {
                                  //1.让挖币数量与挖币时间归0
                                  //2.让挖矿动画停止   回到最初
                                  //3.让时间定时器与刷数据的计时器停止   //重新进入程序 定时器并没有开启

                                // 1  3
                                  miningInfoView.getIv_mining_icon().setClickable(true);
                                  stopWakuang(false);

                                //2 TODO
                            }
                        }
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }

            @Override
            public void loadFail() {
                Handler dataHandler = new Handler(
                        context.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
                        miningInfoView.hideProgress();
                        miningInfoView.loadFail();
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }

            @Override
            public void showMsg() {
                Handler dataHandler = new Handler(
                        context.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
                        miningInfoView.hideProgress();
                        miningInfoView.showMsg(MiningInfoPresenter.this.msg);
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        });
    }

    private void stopWakuang(boolean isExistTimer) {
        //1.让挖币数量与挖币时间归0
        //2.让挖矿动画停止
        //3.让时间定时器与刷数据的计时器停止   //重新进入程序 定时器并没有开启

        //让按钮可以点击
        //one
        miningInfoView.getTv_mining_count().setText("0.0000");
        miningInfoView.getTv_mining_time().setText("00:00:00");
        time = 0;  //已挖矿时间设定为0
        ctc = 0.0000; //已挖币的数量设定为0

        miningInfoView.getIv_mining_icon().setClickable(true);
        miningInfoView.getIv_mining_icon().setBackgroundResource(R.drawable.bg_shape_feb831);
        miningInfoView.getIv_mining_icon().setText("开始");
        miningInfoView.setMiningStartGifAnim(false);

        //two
        //TODO

        //three
        if(isExistTimer)
        {
             if(wakuangInfoTimer !=null )
             {
                 wakuangInfoTimer.cancel();
             }
            if(totleTimeTimer !=null )
            {
                totleTimeTimer.cancel();
            }
        }

    }

    private void startTimer()
    {
        TimerTask totleTimeTask = new TimerTask() {
            public void run() {
                Handler dataHandler = new Handler(context.getMainLooper()) {
                    @Override
                    public void handleMessage(final Message msg) {
                        time++;
                            if(time % refreshTime == 0)
                            {
                                Handler dataHandler = new Handler(
                                        context.getMainLooper()) {

                                    @Override
                                    public void handleMessage(
                                            final Message msg) {

                                        miningInfoBiz.loadMiningInfo(new MiningInfoListener() {
                                            @Override
                                            public void loadSuccess() {

                                                Handler dataHandler = new Handler(context.getMainLooper()) {
                                                    @Override
                                                    public void handleMessage(final Message msg) {
                                                        //得到已挖矿的时间 得到此次可以挖矿的总时间
                                                        if(miningInfo.getTimes()!=null&&miningInfo.getTotlatimes()!=null) {
//                                                        time = Integer.parseInt(miningInfo.getTimes());
                                                            totalTimes = Integer.parseInt(miningInfo.getTotlatimes());
                                                            bonus = Double.parseDouble(miningInfo.getBonus());
                                                            bonusTotal = Double.parseDouble(miningInfo.getBonustotal());
                                                            refreshTime = Integer.parseInt(miningInfo.getRefreshtimes());
                                                            oneMiao = bonus / refreshTime;
                                                            if (totalTimes > 0 && bonusTotal >0){
                                                                ctc = bonusTotal;
                                                            }else{
                                                                ctc = 0.0000;
                                                            }
                                                        }
                                                    }
                                                };
                                                dataHandler.sendEmptyMessage(0);
                                            }

                                            @Override
                                            public void loadFail() {
                                                Handler dataHandler = new Handler(
                                                        context.getMainLooper()) {

                                                    @Override
                                                    public void handleMessage(
                                                            final Message msg) {
                                                        miningInfoView.loadFail();
                                                        oneMiao = 0;
                                                    }
                                                };
                                                dataHandler.sendEmptyMessage(0);
                                            }

                                            @Override
                                            public void showMsg() {
                                                Handler dataHandler = new Handler(
                                                        context.getMainLooper()) {

                                                    @Override
                                                    public void handleMessage(
                                                            final Message msg) {
                                                        oneMiao = 0;
                                                        miningInfoView.showMsg(MiningInfoPresenter.this.msg);
                                                    }
                                                };
                                                dataHandler.sendEmptyMessage(0);
                                            }
                                        });

                                    }
                                };
                                dataHandler.sendEmptyMessage(0);
                            }
                        if (time >= totalTimes){          //停止挖矿
                            miningInfoView.getIv_mining_icon().setClickable(true);
                            stopWakuang(true);
                        }else
                        {
                            miningInfoView.getTv_mining_time().setText(DateUtil.formatDate(new Date(
                                    time * 1000), "HH:mm:ss"));
                        }
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        };


        TimerTask wakuangInfoTask = new TimerTask() {
            public void run() {

                Handler dataHandler = new Handler(context.getMainLooper()) {
                    @Override
                    public void handleMessage(final Message msg) {

                        if (time < totalTimes){
                                ctc = ctc+oneMiao;
                                DecimalFormat df = new DecimalFormat("0.0000");
                                miningInfoView.getTv_mining_count().setText(df.format(ctc)+"");
//                            miningInfoView.getTv_mining_count().playNumber(ctc,Double.parseDouble(miningInfoView.getTv_mining_count().getText().toString()));

                        }else{
                            miningInfoView.getIv_mining_icon().setClickable(true);
                            stopWakuang(true);
                        }
                    }

                };
                dataHandler.sendEmptyMessage(0);
            }
        };

        // 计时器，1秒刷时间
        totleTimeTimer = new Timer(true);
        totleTimeTimer.schedule(totleTimeTask, 1000, 1000);

        // 计时器，10秒刷信息
        wakuangInfoTimer = new Timer(true);
        wakuangInfoTimer.schedule(wakuangInfoTask, 1000, 1000);
    }


    /**
     * 开始挖矿的方法
     */
    public void startMining()
    {
            miningInfoView.getIv_mining_icon().setClickable(false);
            miningInfoBiz.startMining(new MiningInfoListener() {
                @Override
                public void loadSuccess() {


                    Handler dataHandler = new Handler(
                            context.getMainLooper()) {

                        @Override
                        public void handleMessage(
                                final Message msg) {

                            miningInfoView.getIv_mining_icon().setBackgroundResource(R.drawable.bg_shape_c4c4c4);
                            miningInfoView.getIv_mining_icon().setText("");
                            miningInfoView.setMiningStartGifAnim(true);

                            startTimer();
                            ((MyBaseActivity) context).sToast("开始挖矿");

                            miningInfoBiz.loadMiningInfo(new MiningInfoListener() {
                                @Override
                                public void loadSuccess() {

                                    Handler dataHandler = new Handler(context.getMainLooper()) {
                                        @Override
                                        public void handleMessage(final Message msg) {
                                            //得到已挖矿的时间 得到此次可以挖矿的总时间
                                            if(miningInfo.getTimes()!=null&&miningInfo.getTotlatimes()!=null) {
//                                                time = Integer.parseInt(miningInfo.getTimes());
                                                totalTimes = Integer.parseInt(miningInfo.getTotlatimes());
                                                bonus = Double.parseDouble(miningInfo.getBonus());
                                                bonusTotal = Double.parseDouble(miningInfo.getBonustotal());
                                                refreshTime = Integer.parseInt(miningInfo.getRefreshtimes());
                                                oneMiao = bonus / refreshTime;
                                                if (totalTimes > 0 && bonusTotal >0){
                                                    ctc = bonusTotal;
                                                }else{
                                                    ctc = 0.0000;
                                                }
                                            }
                                        }
                                    };
                                    dataHandler.sendEmptyMessage(0);
                                }

                                @Override
                                public void loadFail() {
                                    Handler dataHandler = new Handler(
                                            context.getMainLooper()) {

                                        @Override
                                        public void handleMessage(
                                                final Message msg) {
                                            miningInfoView.getIv_mining_icon().setClickable(true);
                                            miningInfoView.loadFail();
                                        }
                                    };
                                    dataHandler.sendEmptyMessage(0);
                                }

                                @Override
                                public void showMsg() {
                                    Handler dataHandler = new Handler(
                                            context.getMainLooper()) {

                                        @Override
                                        public void handleMessage(
                                                final Message msg) {
                                            miningInfoView.getIv_mining_icon().setClickable(true);
                                            miningInfoView.showMsg(MiningInfoPresenter.this.msg);
                                        }
                                    };
                                    dataHandler.sendEmptyMessage(0);
                                }
                            });
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }

                @Override
                public void loadFail() {
                    Handler dataHandler = new Handler(
                            context.getMainLooper()) {

                        @Override
                        public void handleMessage(
                                final Message msg) {
                            miningInfoView.getIv_mining_icon().setClickable(true);
                            miningInfoView.loadFail();
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }

                @Override
                public void showMsg() {
                    Handler dataHandler = new Handler(
                            context.getMainLooper()) {

                        @Override
                        public void handleMessage(
                                final Message msg) {
                            miningInfoView.getIv_mining_icon().setClickable(true);
                            miningInfoView.showMsg(MiningInfoPresenter.this.msg);
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }
            });
    }


    /**
     * 百分比可能会有小数 此方法去除小数
     * @param str
     * @return
     */
    public String  removeDecimalPoint(String str)
    {
       if(str.contains("."))
       {
            int count = str.indexOf(".");
            return str.substring(0,count);
       }
        return str;
    }


}
