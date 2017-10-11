package com.tohier.cartercoin.presenter;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.activity.MyBaseActivity;
import com.tohier.cartercoin.activity.NewMiningActivity;
import com.tohier.cartercoin.bean.NewMiningInfo;
import com.tohier.cartercoin.biz.LoadNewMiningInfoBiz;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.Tools;
import com.tohier.cartercoin.listener.NewMiningInfoListener;
import com.tohier.cartercoin.ui.NewMiningInfoView;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/3/10.
 */

public class NewMiningInfoPresenter {

    public boolean isOver = false;
    private LoadNewMiningInfoBiz loadNewMiningInfoBiz;
    private NewMiningInfoView newMiningInfoView;
    private Context context;
    public List<NewMiningInfo> chengshuData = new ArrayList<NewMiningInfo>();
    public List<NewMiningInfo> weichengshuData = new ArrayList<NewMiningInfo>();
    private int matureCount;

    //小球所处的x轴的坐标
    Integer location[] = {0,240,480,720,960,1200};
    //占位符 标记那个x轴坐标已经显示
    ArrayList<Integer> list = new ArrayList<Integer>();

    //弹出小矿的宽度
    int viewKuan = 0;

    Handler  handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0x001)
            {
                handler.postDelayed(thread,1000);
            }
        }
    };

    Thread thread = new Thread()
    {
        @Override
        public void run() {
            super.run();
            handler.sendEmptyMessage(0x001);
        }
    };
    private Timer timer;
    private TimerTask timerTask;
    private int chengshuCount;

    public ArrayList<Integer> getList() {
        return list;
    }

    public NewMiningInfoPresenter(Context context, NewMiningInfoView newMiningInfoView) {
        this.context = context;
        loadNewMiningInfoBiz = new LoadNewMiningInfoBiz(context);
        this.newMiningInfoView = newMiningInfoView;
    }


    public void loadMiningInfo()
    {
        loadNewMiningInfoBiz.loadMiningInfo(new NewMiningInfoListener() {
            @Override
            public void loadSuccess(final List<NewMiningInfo> datas) { //显示小球
                Handler dataHandler = new Handler(
                        context.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
                        newMiningInfoView.hideLoadGif();
                        ifstartMining(datas);
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
                        newMiningInfoView.hideLoadGif();
//                        ((MyBaseActivity) context).sToast("请检查您的网络链接状态");

                    }
                };
                dataHandler.sendEmptyMessage(0);
            }

            @Override
            public void showMsg(final String msg87) {
                Handler dataHandler = new Handler(
                        context.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {

                        if(!TextUtils.isEmpty(msg87))
                        {
//                            if(msg87.equals("nodate"))
//                            {
//                                loadMiningInfo();
//                            }else
//                            {
                                boolean flag = Tools.isPhonticName(msg87);
                                if(!flag)
                                {
                                    ((MyBaseActivity)context).sToast(msg87);
                                }
//                            }
                        }
                        newMiningInfoView.showHand();
                        newMiningInfoView.stopAnim();
                        newMiningInfoView.hideLoadGif();
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        });
    }

    /**
     * 开始挖矿的方法
     */
    public void startMining()
    {
        if(isOver==true)
        {
            if(newMiningInfoView.getQiuCount()>0)
            {
                ((MyBaseActivity)context).sToast("先收取完才可以挖矿哦");
            }else
            {
                start();
            }
        }else
        {
            start();
        }
    }

    public void start()
    {
        newMiningInfoView.getStartMiningImg().setClickable(false);
                        loadNewMiningInfoBiz.startMining(new NewMiningInfoListener() {
                            @Override
                            public void loadSuccess(List<NewMiningInfo> datas) {
                                Handler dataHandler = new Handler(
                                        context.getMainLooper()) {

                                    @Override
                                    public void handleMessage(
                                            final Message msg) {
                        newMiningInfoView.getStartMiningImg().setClickable(true);
                        isOver = false;
                        newMiningInfoView.hideHand();
                        newMiningInfoView.startAnim();
//                        ((MyBaseActivity) context).sToast("开始挖矿");
                        loadMiningInfo();
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

//                        ((MyBaseActivity) context).sToast("请检查您的网络链接状态");
                        newMiningInfoView.getStartMiningImg().setClickable(true);
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }

            @Override
            public void showMsg(final String msg87) {
                Handler dataHandler = new Handler(
                        context.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
                        newMiningInfoView.getStartMiningImg().setClickable(true);
                        if(!TextUtils.isEmpty(msg87))
                        {
                            boolean flag = Tools.isPhonticName(msg87);
                            if(!flag)
                            {
                                if(msg87.equals("启动失败"))
                                {
                                    ((MyBaseActivity)context).sToast("宝宝正在挖矿中，根本停不下来！");
                                }else
                                {
                                    ((MyBaseActivity)context).sToast(msg87);
                                }
                            }
                        }
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        });
    }


    /**
     * 是否全部成熟
     */
    public boolean allmature = false;

    public void  ifstartMining(List<NewMiningInfo> datas)
    {
        /**
         * 计算几个求成熟
         * 成熟的要加点击事件
         * 如果最后一个也成熟的话就是  6个 else 5个  屏幕少于6  else 5个就在显示其他成熟的
         * 还有未成熟的要继续倒计时,继续显示计算
         * 同时本次挖矿到计算都结束，如果没有全收取完 不可以点击开始 二次挖矿
         */
        if(datas!=null&&datas.size()>0)
        {
               for(int i = 0 ; i < datas.size() ; i ++)
               {
                   if(datas.get(i).getEndDate()!=null)
                   {
                       if(Integer.parseInt(datas.get(i).getEndDate())==0)  //当结束时间等于0 就代表已经成熟
                       {
                           chengshuData.add(datas.get(i));
                           if(chengshuData.size()>=datas.size())  //代表全部都成熟    代表挖矿结束
                           {
                               allmature = true;
                               isOver = true;
                           }
                       }else   //未成熟的出现
                       {
                           allmature = false;
                           weichengshuData.add(datas.get(i));
                       }
                   }
                   if(i==datas.size()-1&&datas.get(i).getEndDate()!=null&&Integer.parseInt(datas.get(i).getEndDate())!=0)
                   {
                       newMiningInfoView.showThisEndTime(datas.get(i).getEndDate());
                   }
               }
        }



        matureCount = chengshuData.size();

        newMiningInfoView.showCollectCount(matureCount);

        if(allmature)//全部成熟    没有收取不允许 开启二次挖矿   并且6个view 都显示成熟的  取前6个未成熟的view
        {
            newMiningInfoView.showHand();
            //TODO
            /**
             * 显示6个成熟view
             */
            if(chengshuData.size()<6)
            {
                chengshuCount = chengshuData.size();
                for(int i = 0 ; i < chengshuCount ; i++)
                {
                    NewMiningInfo newMiningInfo = chengshuData.get(0);
                    //TODO 显示成熟的求 显示6个  显示过的要移除出去
                    loadViewShowView(newMiningInfo,true);

                    chengshuData.remove(0);
                }
            }else
            {
                chengshuCount = 6;
                for(int i = 0 ; i < 6 ; i++)
                {
                    NewMiningInfo newMiningInfo = chengshuData.get(0);
                    //TODO 显示成熟的求 显示6个  显示过的要移除出去
                    loadViewShowView(newMiningInfo,true);

                    chengshuData.remove(0);
                }
            }


            //TODO   不能点击挖矿按钮

            //TODO  并且设定点击事件 可以采摘

        }else   //有未成熟的出现   显示一个未成熟小球 显示5个成熟的小球
        {
            //TODO 显示未成熟小球
           //从未成熟的集合中取第一个 进行显示
            NewMiningInfo newMiningInfo = weichengshuData.get(0);
            endDate = Integer.parseInt(newMiningInfo.getEndDate());
            loadViewShowView(newMiningInfo,false);
            //TODO 取前五个成熟的小球进行显示

            /**
             *  前提是 要有五个成熟的小球
             *  chengshuCount 成熟小球的数量  >0   有成熟小球出现
             */
            if(chengshuData.size()>0)
            {
                //大于等于5就取前5个进行显示
                if(chengshuData.size()>=5)
                {
                    chengshuCount = 5;
                    for(int i = 0 ; i <5;i++)
                    {
                            //从成熟的集合中获取前五个进行显示
                            //显示之后应该从成熟集合中移除
                            NewMiningInfo chengshuNewMiningInfo = chengshuData.get(0);
                            loadViewShowView(chengshuNewMiningInfo,true);
                           chengshuData.remove(0);
                    }
                }else  //小于5   有几个显示几个
                {
                    //从成熟的集合中获取进行显示  全部进行显示 并且显示之后将成熟的集合清空
                    //并且要在倒计时的时候 在到计算结束之后将当前小球 从未成熟的队列中 移除到成熟的队列中
                    chengshuCount =  chengshuData.size();
                    for(int i = 0 ; i < chengshuData.size(); i ++)
                    {
                        NewMiningInfo chengshuNewMiningInfo = chengshuData.get(i);
                        loadViewShowView(chengshuNewMiningInfo,true);
                    }
                    chengshuData.clear();
                }
            }
        }

        }


    private int endDate;
    private int thePreviousDate;


    int it = 0;
    int into = 0;
    int weichengshuLocation = 0;

    private void loadViewShowView(final NewMiningInfo newMiningInfo, boolean isChengShu)
    {
        into = 0;
        final View view = View.inflate(context, R.layout.fragment_mining_qiuqiu_layout,null);
        final TextView textView = (TextView) view.findViewById(R.id.tv_mining_count);
        final ImageView iv_mining_hand = (ImageView) view.findViewById(R.id.iv_mining_hand);
        final ImageView image = (ImageView) view.findViewById(R.id.image);
        final TextView tv_mining_prompt = (TextView) view.findViewById(R.id.tv_mining_prompt);

        int width =  newMiningInfoView.getViewWidth();
        location = new Integer[]{0, width, 2 * width, 3 * width, 4 * width, 5 * width};

        floatAnim(view,0);

        String random_money2 = Tools.getRandomCount(400, 10);
        int s2 = Integer.parseInt(random_money2);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        // while循环 随即抽取集合的数字给数组
        if(allmature)
        {
            while (true) {
                if(into==1)
                {
                    into = 0;
                    break;
                }
                Random random = new Random();
                it = random.nextInt(6);
                //如果list存在这个数组就继续循环
                if (list.contains(it)) {
                    continue;
                } else {
                    into = into +1;
                    //如果list不存在这个数字，那么就把这个数字给数组，并且在list中加入这个数字
                    list.add(it);

                    lp.setMargins(location[it], s2, 0, 0);
                    view.setLayoutParams(lp);
                    newMiningInfoView.addView(view);
                }
            }
        }else
        {
            while (true) {
                if(into==1)
                {
                    into = 0;
                    break;
                }
                Random random = new Random();
                it = random.nextInt(6);
                //如果list存在这个数组就继续循环
                if (list.contains(it)) {
                    continue;
                } else {
                    weichengshuLocation = it;
                    into = into +1;
                    //如果list不存在这个数字，那么就把这个数字给数组，并且在list中加入这个数字
                    list.add(it);

                    lp.setMargins(location[it], s2, 0, 0);
                    view.setLayoutParams(lp);
                    newMiningInfoView.addView(view);
                }
            }
        }

        if(isChengShu)
        {
            tv_mining_prompt.setText("可收取");
            textView.setText(newMiningInfo.getBouns());
            view.setTag(R.id.tag_first,it);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO  采摘的接口　
                    pick(newMiningInfo.getId(),view);
                }
            });
        }else
        {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            if (timerTask != null) {
                timerTask.cancel();
                timerTask = null;
            }
            iv_mining_hand.setImageResource(R.mipmap.iv_hammer);
            image.setAlpha(0.7f);
            //TODO 开启线程 显示的是倒计时
            timer = new Timer();

            timerTask = new TimerTask() {
                @Override
                public void run() {
                    checkWeiChengShuArr(textView,view,iv_mining_hand,image,tv_mining_prompt);
                }
            };
            timer.scheduleAtFixedRate(timerTask,1000,1000);
        }
    }


    /**
     * 查看未成熟的集合中 是否还有未成熟的小球
     * @param textView
     */
    private void checkWeiChengShuArr(final TextView textView, final View view, final ImageView imageView, final ImageView image,final TextView tv_mining_prompt)
    {
        if(endDate<=0)        //剩余时间等于0 转到下一个未成熟的 endDate重新赋值  否则的话 就没有下一个 线程彻底停止
        {
            if(weichengshuData.size()>0) //将未成熟集合中的 下一个 进行显示
            {
                String date = weichengshuData.get(0).getEndDate();
                endDate = Integer.parseInt(date);
                endDate = endDate - thePreviousDate;
                if(endDate<=0)
                {
                    final NewMiningInfo newMiningInfo = weichengshuData.get(0);
                    weichengshuData.remove(0);
                    thePreviousDate = Integer.parseInt(newMiningInfo.getEndDate());

                    final int qiuCount = newMiningInfoView.getQiuCount();

                    if(weichengshuData.size()==0)
                    {
                        Handler dataHandler = new Handler(
                                context.getMainLooper()) {

                            @Override
                            public void handleMessage(
                                    final Message msg) {
                                allmature = true;

                                isOver = true;
                                newMiningInfoView.showHand();
                                newMiningInfoView.stopAnim();
                                image.setAlpha(1.0f);
                                imageView.setImageResource(R.mipmap.iv_mining_hand);
                                tv_mining_prompt.setText("可收取");
                                textView.setText(newMiningInfo.getBouns());
                                timer.cancel();
                                view.setTag(R.id.tag_first,weichengshuLocation);
                                view.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        pick(newMiningInfo.getId(),view);
                                    }
                                });

                            }
                        };
                        dataHandler.sendEmptyMessage(0);
                    }else   //当前的小球成熟   查看当期屏幕成熟小球 是否满5个  不满的话就填充
                    {
                        Handler dataHandler = new Handler(
                                context.getMainLooper()) {

                            @Override
                            public void handleMessage(
                                    final Message msg) {
                                if(qiuCount<6)
                                {
                                    final View view5 = View.inflate(context, R.layout.fragment_mining_qiuqiu_layout,null);
                                    final TextView textView5 = (TextView) view5.findViewById(R.id.tv_mining_count);
                                    final TextView tv_mining_prompt = (TextView) view5.findViewById(R.id.tv_mining_prompt);
                                    tv_mining_prompt.setText("可收取");
                                    textView5.setText(newMiningInfo.getBouns());

                                    floatAnim(view5,0);

                                    String random_money2 = Tools.getRandomCount(400, 10);
                                    int s2 = Integer.parseInt(random_money2);
                                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                                    while (true) {
                                        if(into==1)
                                        {
                                            into = 0;
                                            break;
                                        }
                                        Random random = new Random();
                                        it = random.nextInt(6);
                                        //如果list存在这个数组就继续循环
                                        if (list.contains(it)) {
                                            continue;
                                        } else {
                                            into = into +1;
                                            //如果list不存在这个数字，那么就把这个数字给数组，并且在list中加入这个数字
                                            list.add(it);

                                            lp.setMargins(location[it], s2, 0, 0);
                                            view5.setLayoutParams(lp);
                                            newMiningInfoView.addView(view5);
                                        }
                                    }

                                    AlphaAnimation   alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                                    alphaAnimation.setDuration(300);
                                    alphaAnimation.setFillAfter(true);
                                    view5.startAnimation(alphaAnimation);
                                    alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                                        @Override
                                        public void onAnimationStart(Animation animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animation animation) {
                                        }

                                        @Override
                                        public void onAnimationRepeat(Animation animation) {

                                        }
                                    });

                                    view5.setTag(R.id.tag_first,weichengshuLocation);
                                    view5.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            pick(newMiningInfo.getId(),view5);
                                        }
                                    });
                                }else
                                {
                                    chengshuData.add(newMiningInfo);
                                }
                            }
                        };
                        dataHandler.sendEmptyMessage(0);
                    }
                    ((MyBaseActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(allmature)
                            {
                                int count = chengshuData.size()+newMiningInfoView.getQiuCount();
                                newMiningInfoView.showCollectCount(count);
                            }else
                            {
                                int count = chengshuData.size()+newMiningInfoView.getQiuCount()-1;
                                newMiningInfoView.showCollectCount(count);
                            }
                        }
                    });
                }else
                {
                    chengshuDaoJiShi(textView, view,imageView,image,tv_mining_prompt);
                }
            }
        }else
        {
            chengshuDaoJiShi(textView,view,imageView,image,tv_mining_prompt);
        }
    }

    private void chengshuDaoJiShi(final TextView textView, final View view,final  ImageView imageView,final ImageView image,final TextView tv_mining_prompt)
    {
        if(endDate!=0)
        {
            endDate = endDate - 1;
            if(endDate<=0)  //倒计时结束   迁移到成熟集合中去   必须显示倒计时 除非最后一个未成熟的小球变成成熟状态
            {
                final NewMiningInfo newMiningInfo = weichengshuData.get(0);
                weichengshuData.remove(0);
                thePreviousDate = Integer.parseInt(newMiningInfo.getEndDate());

                final int qiuCount = newMiningInfoView.getQiuCount();

                if(weichengshuData.size()==0)
                {
                    Handler dataHandler = new Handler(
                            context.getMainLooper()) {

                        @Override
                        public void handleMessage(
                                final Message msg) {
                            allmature = true;
                            isOver = true;
                            newMiningInfoView.showHand();
                            newMiningInfoView.stopAnim();
                            image.setAlpha(1.0f);
                            imageView.setImageResource(R.mipmap.iv_mining_hand);
                            tv_mining_prompt.setText("可收取");
                            textView.setText(newMiningInfo.getBouns());
                            timer.cancel();
                            view.setTag(R.id.tag_first,weichengshuLocation);
                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    pick(newMiningInfo.getId(),view);
                                }
                            });

                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }else   //当前的小球成熟   查看当期屏幕成熟小球 是否满5个  不满的话就填充
                {
                    Handler dataHandler = new Handler(
                            context.getMainLooper()) {

                        @Override
                        public void handleMessage(
                                final Message msg) {
                            if(qiuCount<6)
                            {
                                final View view5 = View.inflate(context, R.layout.fragment_mining_qiuqiu_layout,null);
                                final TextView textView5 = (TextView) view5.findViewById(R.id.tv_mining_count);
                                final TextView tv_mining_prompt = (TextView) view5.findViewById(R.id.tv_mining_prompt);
                                tv_mining_prompt.setText("可收取");
                                textView5.setText(newMiningInfo.getBouns());
                                floatAnim(view5,0);

                                String random_money2 = Tools.getRandomCount(400, 10);
                                int s2 = Integer.parseInt(random_money2);
                                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                                while (true) {
                                    if(into==1)
                                    {
                                        into = 0;
                                        break;
                                    }
                                    Random random = new Random();
                                    it = random.nextInt(6);
                                    //如果list存在这个数组就继续循环
                                    if (list.contains(it)) {
                                        continue;
                                    } else {
                                        into = into +1;
                                        //如果list不存在这个数字，那么就把这个数字给数组，并且在list中加入这个数字
                                        list.add(it);

                                        lp.setMargins(location[it], s2, 0, 0);
                                        view5.setLayoutParams(lp);
                                        newMiningInfoView.addView(view5);

                                    }
                                }

                                AlphaAnimation   alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                                alphaAnimation.setDuration(300);
                                alphaAnimation.setFillAfter(true);
                                view5.startAnimation(alphaAnimation);
                                alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });

                                view5.setTag(R.id.tag_first,it);
                                view5.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        pick(newMiningInfo.getId(),view5);
                                    }
                                });
                            }else
                            {
                                chengshuData.add(newMiningInfo);
                            }
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }
                ((MyBaseActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(allmature)
                        {
                            int count = chengshuData.size()+newMiningInfoView.getQiuCount();
                            newMiningInfoView.showCollectCount(count);
                        }else
                        {
                            int count = chengshuData.size()+newMiningInfoView.getQiuCount()-1;
                            newMiningInfoView.showCollectCount(count);
                        }
                    }
                });
            }else
            {
                Handler dataHandler = new Handler(
                        context.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {

                        int h=endDate/3600;
                        int m=(endDate%3600)/60;
                        int s=(endDate%3600)%60;

                        textView.setText(h+":"+m+":"+s);

                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        }
    }


    private void floatAnim(View view,int delay){

            List<Animator> animators = new ArrayList<Animator>();
            ObjectAnimator translationYAnim = ObjectAnimator.ofFloat(view, "translationY", -6.0f,6.0f,-6.0f);
            translationYAnim.setDuration(1500);
            translationYAnim.setRepeatCount(ValueAnimator.INFINITE);
//        translationYAnim.setRepeatMode(ValueAnimator.INFINITE);
            translationYAnim.start();
            animators.add(translationYAnim);

            AnimatorSet btnSexAnimatorSet = new AnimatorSet();
            btnSexAnimatorSet.playTogether(animators);
            btnSexAnimatorSet.setStartDelay(delay);
            btnSexAnimatorSet.start();

    }


    String qId = "";
    /**
     * 采摘
     */
    private void pick(final String id, final View view)
    {
        view.setClickable(false);
        Map<String, String> par = new HashMap<String, String>();
        par.put("mineid", id);
        HttpConnect.post(((MyBaseActivity)context), "member_mine_collect", par, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject jsonObject = JSONObject.fromObject(arg0.body().string());
                final String msg = jsonObject.optString("msg");
                if (jsonObject.get("status").equals("success")) {
                     //TODO 采摘成功  动画开始 资产分配
                    Handler dataHandler = new Handler(
                            context.getMainLooper()) {

                        @Override
                        public void handleMessage(
                                final Message msg8) {

                            ((NewMiningActivity)context).loadMemberPrivateMiningPool();

                            final String available = jsonObject.getJSONArray("data").getJSONObject(0).getString("bonus2");//收益
//                            final String rich = jsonObject.getJSONArray("data").getJSONObject(0).getString("bonus3");    //复投账户
                            final String ctccous = jsonObject.getJSONArray("data").getJSONObject(0).getString("bonus4");  //消费账户
                            final String ctcoption = jsonObject.getJSONArray("data").getJSONObject(0).getString("bonus9");  //决战账户

//                            newMiningInfoView.setFuTou(Double.parseDouble(rich));
                            newMiningInfoView.setJueZhan(Double.parseDouble(ctcoption));
                            newMiningInfoView.setXiaoFei(Double.parseDouble(ctccous));
                            newMiningInfoView.setShouYi(Double.parseDouble(available));
                            newMiningInfoView.jian();

                            if(!allmature)
                            {
                                AlphaAnimation   alpha = new AlphaAnimation(1.0f, 0.0f);
                                alpha.setDuration(300);
                                alpha.setFillAfter(true);
                                view.setAnimation(alpha);
                                alpha.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        //view消失
                                        newMiningInfoView.removeView(view);
                                        list.remove(view.getTag(R.id.tag_first));
                                        if(allmature)
                                        {
                                            int count = chengshuData.size()+newMiningInfoView.getQiuCount();
                                            newMiningInfoView.showCollectCount(count);
                                        }else
                                        {
                                            int count = chengshuData.size()+newMiningInfoView.getQiuCount()-1;
                                            newMiningInfoView.showCollectCount(count);
                                        }
                                        int qiuCount = newMiningInfoView.getQiuCount();
                                        if(qiuCount<6)
                                        {
                                            if(chengshuData.size()>0)
                                            {
                                                final View view5 = View.inflate(context, R.layout.fragment_mining_qiuqiu_layout,null);
                                                final TextView textView5 = (TextView) view5.findViewById(R.id.tv_mining_count);
                                                final TextView tv_mining_prompt = (TextView) view5.findViewById(R.id.tv_mining_prompt);
                                                tv_mining_prompt.setText("可收取");

                                                textView5.setText(chengshuData.get(0).getBouns());

                                                floatAnim(view5,0);

                                                String random_money2 = Tools.getRandomCount(400, 10);
                                                int s2 = Integer.parseInt(random_money2);
                                                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                                                while (true) {
                                                    if(into==1)
                                                    {
                                                        into = 0;
                                                        break;
                                                    }
                                                    Random random = new Random();
                                                    it = random.nextInt(6);
                                                    //如果list存在这个数组就继续循环
                                                    if (list.contains(it)) {
                                                        continue;
                                                    } else {
                                                        into = into +1;
                                                        //如果list不存在这个数字，那么就把这个数字给数组，并且在list中加入这个数字
                                                        list.add(it);

                                                        lp.setMargins(location[it], s2, 0, 0);
                                                        view5.setLayoutParams(lp);
                                                        newMiningInfoView.addView(view5);
                                                    }
                                                }

                                                AlphaAnimation  alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                                                alphaAnimation.setDuration(300);
                                                alphaAnimation.setFillAfter(true);
                                                view5.startAnimation(alphaAnimation);
                                                alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                                                    @Override
                                                    public void onAnimationStart(Animation animation) {

                                                    }

                                                    @Override
                                                    public void onAnimationEnd(Animation animation) {
                                                    }

                                                    @Override
                                                    public void onAnimationRepeat(Animation animation) {

                                                    }
                                                });

                                                qId = chengshuData.get(0).getId();
                                                chengshuData.remove(0);
                                                view5.setTag(qId);
                                                view5.setTag(R.id.tag_first,it);
                                                view5.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        pick(((String) v.getTag()),view5);
                                                    }
                                                });
                                            }
                                        }
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });
                            }else
                            {
                                //view消失
                                newMiningInfoView.removeView(view);
                                list.remove(view.getTag(R.id.tag_first));
                                if(allmature)
                                {
                                    int count = chengshuData.size()+newMiningInfoView.getQiuCount();
                                    newMiningInfoView.showCollectCount(count);
                                }else
                                {
                                    int count = chengshuData.size()+newMiningInfoView.getQiuCount()-1;
                                    newMiningInfoView.showCollectCount(count);
                                }
                                int qiuCount = newMiningInfoView.getQiuCount();
                                if(qiuCount<6)
                                {
                                    if(chengshuData.size()>0)
                                    {
                                        final View view5 = View.inflate(context, R.layout.fragment_mining_qiuqiu_layout,null);
                                        final TextView textView5 = (TextView) view5.findViewById(R.id.tv_mining_count);
                                        final TextView tv_mining_prompt = (TextView) view5.findViewById(R.id.tv_mining_prompt);
                                        tv_mining_prompt.setText("可收取");

                                        textView5.setText(chengshuData.get(0).getBouns());

                                        floatAnim(view5,0);

                                        String random_money2 = Tools.getRandomCount(400, 10);
                                        int s2 = Integer.parseInt(random_money2);

                                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                                        while (true) {
                                            if(into==1)
                                            {
                                                into = 0;
                                                break;
                                            }
                                            Random random = new Random();
                                            it = random.nextInt(6);
                                            //如果list存在这个数组就继续循环
                                            if (list.contains(it)) {
                                                continue;
                                            } else {
                                                into = into +1;
                                                //如果list不存在这个数字，那么就把这个数字给数组，并且在list中加入这个数字
                                                list.add(it);

                                                lp.setMargins(location[it], s2, 0, 0);
                                                view5.setLayoutParams(lp);
                                                newMiningInfoView.addView(view5);
                                            }
                                        }

                                        AlphaAnimation  alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                                        alphaAnimation.setDuration(300);
                                        alphaAnimation.setFillAfter(true);
                                        view5.startAnimation(alphaAnimation);
                                        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                                            @Override
                                            public void onAnimationStart(Animation animation) {

                                            }

                                            @Override
                                            public void onAnimationEnd(Animation animation) {
                                            }

                                            @Override
                                            public void onAnimationRepeat(Animation animation) {

                                            }
                                        });

                                        qId = chengshuData.get(0).getId();
                                        chengshuData.remove(0);
                                        view5.setTag(qId);
                                        view5.setTag(R.id.tag_first,it);

                                        view5.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                pick(((String) v.getTag()),view5);
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }else
                {
                    Handler dataHandler = new Handler(
                            context.getMainLooper()) {

                        @Override
                        public void handleMessage(
                                final Message msg8) {
                            view.setClickable(true);
                            if(!TextUtils.isEmpty(msg))
                            {
                                boolean flag = Tools.isPhonticName(msg);
                                if(!flag)
                                {
                                    if(msg.equals("收矿失败"))
                                    {
                                        //view消失
                                        newMiningInfoView.removeView(view);
                                        list.remove(view.getTag(R.id.tag_first));
                                        if(allmature)
                                        {
                                            int count = chengshuData.size()+newMiningInfoView.getQiuCount();
                                            newMiningInfoView.showCollectCount(count);
                                        }else
                                        {
                                            int count = chengshuData.size()+newMiningInfoView.getQiuCount()-1;
                                            newMiningInfoView.showCollectCount(count);
                                        }
                                        int qiuCount = newMiningInfoView.getQiuCount();
                                        if(qiuCount<6)
                                        {
                                            if(chengshuData.size()>0)
                                            {
                                                final View view5 = View.inflate(context, R.layout.fragment_mining_qiuqiu_layout,null);
                                                final TextView textView5 = (TextView) view5.findViewById(R.id.tv_mining_count);
                                                final TextView tv_mining_prompt = (TextView) view5.findViewById(R.id.tv_mining_prompt);
                                                tv_mining_prompt.setText("可收取");

                                                textView5.setText(chengshuData.get(0).getBouns());

                                                floatAnim(view5,0);

                                                String random_money2 = Tools.getRandomCount(400, 10);
                                                int s2 = Integer.parseInt(random_money2);

                                                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                                                while (true) {
                                                    if(into==1)
                                                    {
                                                        into = 0;
                                                        break;
                                                    }
                                                    Random random = new Random();
                                                    it = random.nextInt(6);
                                                    //如果list存在这个数组就继续循环
                                                    if (list.contains(it)) {
                                                        continue;
                                                    } else {
                                                        into = into +1;
                                                        //如果list不存在这个数字，那么就把这个数字给数组，并且在list中加入这个数字
                                                        list.add(it);

                                                        lp.setMargins(location[it], s2, 0, 0);
                                                        view5.setLayoutParams(lp);
                                                        newMiningInfoView.addView(view5);
                                                    }
                                                }

                                                AlphaAnimation  alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                                                alphaAnimation.setDuration(300);
                                                alphaAnimation.setFillAfter(true);
                                                view5.startAnimation(alphaAnimation);
                                                alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                                                    @Override
                                                    public void onAnimationStart(Animation animation) {

                                                    }

                                                    @Override
                                                    public void onAnimationEnd(Animation animation) {
                                                    }

                                                    @Override
                                                    public void onAnimationRepeat(Animation animation) {

                                                    }
                                                });

                                                qId = chengshuData.get(0).getId();
                                                chengshuData.remove(0);
                                                view5.setTag(qId);

                                                view5.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        pick(((String) v.getTag()),view5);
                                                    }
                                                });
                                            }
                                        }
                                    }else
                                    {
                                        if(!msg.equals("体力不足"))
                                        {
                                            ((MyBaseActivity)context).sToast(msg);
                                        }
                                    }
                                    if(msg.equals("体力不足"))
                                    {
                                        newMiningInfoView.showShareWin();
                                    }
                                }
                            }


                        }
                    };
                    dataHandler.sendEmptyMessage(0);

                }
            }

            @Override
            public  void onFailure(Request arg0, IOException arg1) {
                Handler dataHandler = new Handler(
                        context.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg8) {
                        view.setClickable(true);
//                        ((MyBaseActivity)context).sToast("请检查您的网络链接状态");
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        });
    }

}
