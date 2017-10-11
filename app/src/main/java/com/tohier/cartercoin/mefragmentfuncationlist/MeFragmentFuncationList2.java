package com.tohier.cartercoin.mefragmentfuncationlist;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.IYWConversationUnreadChangeListener;
import com.tohier.android.fragment.base.BaseFragment;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.activity.FengYunRankingActivity;
import com.tohier.cartercoin.activity.ListActivity;
import com.tohier.cartercoin.activity.MyOrderActivity;
import com.tohier.cartercoin.activity.SecurityCenterActivity;
import com.tohier.cartercoin.activity.YouHuiJuanActivity;
import com.tohier.cartercoin.adapter.GridViewAdapter;
import com.tohier.cartercoin.bean.FuncationList;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/1.
 */

public class MeFragmentFuncationList2 extends BaseFragment {

    private View view;
    private GridView girdView;
//    private int[] icon = { R.mipmap.iv_youhuiquan,R.mipmap.iv_ctc_zhuanru,R.mipmap.iv_ctc_zhuanchu};
    private int[] icon = { R.mipmap.iv_youhuiquan,R.mipmap.iv_order,R.mipmap.iv_active, R.mipmap.iv_tongxuehui};
//    private String[] text = new String[]{"优惠券","一键购买","虚拟币转出","人民币充值","人民币提现","微信客服","我的订单","同学汇"};
//    private String[] text = new String[]{"优惠券","我的订单","活动"};
//    private String[] text = new String[]{"优惠券","资产转入","资产转出"};
    private String[] text = new String[]{"优惠券","我的订单","活动","宝友圈"};
    private ArrayList<FuncationList> data_list = new ArrayList<FuncationList>();
    public YWIMKit mIMKit;
    PopupWindow window = null;
    private ImageView ivSetting;
    private View view1;

    public void setIvSetting(ImageView ivSetting) {
        this.ivSetting = ivSetting;
    }
    /**
     * 监听未读消息数
     */
    private IYWConversationService mConversationService;

    public void setmIMKit(YWIMKit mIMKit) {
        this.mIMKit = mIMKit;
    }

    private IYWConversationUnreadChangeListener mConversationUnreadChangeListener;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private GridViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_mefuncationlist,container,false);
        getData();
        initData();
        return view;
    }
    @Override
    public void initData() {
        view1 = View.inflate(getActivity(),R.layout.popupwindow_prompt_authentication,null);
        window = new PopupWindow(view1, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);


        girdView = (GridView) view.findViewById(R.id.girdview);
        girdView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridViewAdapter(getActivity(),data_list,mIMKit);
        girdView.setAdapter(adapter);

        girdView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                if(position==0)
                {
                    intent = new Intent(getActivity(), YouHuiJuanActivity.class);
                }else if(position==1)
                {
//                c
                    intent = new Intent(getActivity(), MyOrderActivity.class);
                }else if(position==2)//活动
                {

                    intent = new Intent(getActivity(), ListActivity.class);
//                    String phoneNum = LoginUser.getInstantiation(getActivity().getApplicationContext()).getLoginUser().getPhoneNum();
//                    if(TextUtils.isEmpty(phoneNum)||phoneNum.equals(""))
//                    {
//                        show();
//                    }else
//                    {
//                        intent = new Intent(getActivity(), ZhuanChuXuNIBiActivity.class);
//                    }
                }else if(position==3)//宝友汇
                {
                    intent = new Intent(getActivity(), FengYunRankingActivity.class);

                }
                if(intent!=null)  startActivity(intent);
            }
        });

        mConversationService = mIMKit.getConversationService();

        //监听所有未读消息
        initConversationServiceAndListener();
    }


    @Override
    public void onResume() {
        super.onResume();
        //resume时需要检查全局未读消息数并做处理，因为离开此界面时删除了全局消息监听器
        mConversationUnreadChangeListener.onUnreadChange();
    }



    public ArrayList<FuncationList> getData() {
        for (int i = 0; i < icon.length; i++) {
            FuncationList tools = new FuncationList();
            tools.setImage(icon[i]);
            tools.setText(text[i]);
            data_list.add(tools);
        }
        return data_list;
    }


    /**
     * 当未读数发生变化时会回调该方法，开发者可以在该方法中更新未读数
     *
     */
    private void initConversationServiceAndListener() {
        mConversationUnreadChangeListener = new IYWConversationUnreadChangeListener() {

            @Override
            public void onUnreadChange() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        };
        mConversationService.addTotalUnreadChangeListener(mConversationUnreadChangeListener);
    }

    /**
     * 显示实名认证的提示框
     */
    public void show()
    {
        ImageView iv_into_authentication = (ImageView) view1.findViewById(R.id.iv_into_authentication);
        ImageView iv_cancel_authentication = (ImageView) view1.findViewById(R.id.iv_cancel_authentication);

        iv_cancel_authentication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });

        iv_into_authentication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),SecurityCenterActivity.class));
                window.dismiss();
            }
        });

        if(!window.isShowing())
        {

            // 设置背景颜色变暗
            WindowManager.LayoutParams lp5 = getActivity().getWindow().getAttributes();
            lp5.alpha = 0.5f;
            getActivity().getWindow().setAttributes(lp5);
            window.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams lp3 = getActivity().getWindow().getAttributes();
                    lp3.alpha = 1f;
                    getActivity().getWindow().setAttributes(lp3);
                }
            });

            window.setOutsideTouchable(true);

            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0x00ffffff);
            window.setBackgroundDrawable(dw);

            // 设置popWindow的显示和消失动画
            window.setAnimationStyle(R.style.Mypopwindow_anim_style);
            // 在底部显示
            window.showAtLocation(ivSetting,
                    Gravity.BOTTOM, 0, 0);
        }
    }
    
}
