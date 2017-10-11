package com.tohier.cartercoin.mefragmentfuncationlist;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.conversation.EServiceContact;
import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.IYWConversationUnreadChangeListener;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.android.fragment.base.BaseFragment;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.activity.BuyAssetsActivity;
import com.tohier.cartercoin.activity.HelperCenterActivity;
import com.tohier.cartercoin.activity.NewTransactionActivity;
import com.tohier.cartercoin.activity.RechargeRMBActivity;
import com.tohier.cartercoin.activity.SecurityCenterActivity;
import com.tohier.cartercoin.activity.ShareOptionActivity;
import com.tohier.cartercoin.activity.TiXianActivity;
import com.tohier.cartercoin.activity.TransactionActivity;
//import com.tohier.cartercoin.activity.VipUpgradeActivity;
import com.tohier.cartercoin.adapter.GridViewAdapter;
import com.tohier.cartercoin.bean.FuncationList;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.MyApplication;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/1.
 */

public class MeFragmentFuncationList1 extends BaseFragment {


    private String[] textStrings;
    private View view;
    private GridView girdView;
    private String rmb;

    private ImageView ivSetting;

    PopupWindow window = null;
    private GridViewAdapter adapter;

    /**
     * 监听未读消息数
     */
    private IYWConversationService mConversationService;
    private View view1;

    public void setmIMKit(YWIMKit mIMKit) {
        this.mIMKit = mIMKit;
    }

    private IYWConversationUnreadChangeListener mConversationUnreadChangeListener;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public YWIMKit mIMKit;


    public void setRmb(String rmb) {
        this.rmb = rmb;
    }

    //    private int[] icon = { R.mipmap.iv_chongzhi, R.mipmap.iv_tixian,R.mipmap.iv_shengji,R.mipmap.iv_order,
//            R.mipmap.iv_ctc_zhuanchu,R.mipmap.iv_ctc_zhuanru, R.mipmap.iv_tongxuehui,R.mipmap.iv_more};
//    private String[] text = new String[]{"充值","提现","升级","订单","虚拟币转出","虚拟币转入","同学汇","更多服务"};
//
//        private int[] icon = { R.mipmap.iv_chongzhi, R.mipmap.iv_tixian,R.mipmap.iv_buy_assetpackage,R.mipmap.iv_shengji,R.mipmap.iv_ctc_zhuanru,
//            R.mipmap.iv_ctc_zhuanchu, R.mipmap.iv_tongxuehui,R.mipmap.zhibo};
//

    private int[] icon = { R.mipmap.iv_chongzhi, R.mipmap.iv_tixian,R.mipmap.iv_buy_assetpackage,R.mipmap.iv_shengji,R.mipmap.iv_helper_center,
            R.mipmap.iv_share_option,R.mipmap.iv_deal,R.mipmap.zhibo};
//    private String[] text = new String[]{"充值","提现","升级","订单","虚拟币转出","虚拟币转入","同学汇","收货地址"};


//    private String[] text = new String[]{"充值","提现","资产包","我的VIP","资产转入","资产转出","宝友汇","在线咨询"};
    private String[] text = new String[]{"充值","提现","矿产包","我的VIP","帮助中心","决战","交易","在线咨询"};

    private ArrayList<FuncationList> data_list = new ArrayList<FuncationList>();
    private ViewPager viewPager;

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    public void setIvSetting(ImageView ivSetting) {
        this.ivSetting = ivSetting;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_mefuncationlist,container,false);
        getData();
        init();
        setUpView();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void init() {
        girdView = (GridView) view.findViewById(R.id.girdview);
        adapter = new GridViewAdapter(getActivity(),data_list,mIMKit);
        girdView.setAdapter(adapter);
        girdView.setSelector(new ColorDrawable(Color.TRANSPARENT));

        view1 = View.inflate(getActivity(),R.layout.popupwindow_prompt_authentication,null);

        window = new PopupWindow(view1, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        // 获取listview的adapter
        ListAdapter listAdapter = girdView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        // 固定列宽，有多少列
        int col = 4;// listView.getNumColumns();
        int totalHeight = 0;
        // i每次加4，相当于listAdapter.getCount()小于等于4时 循环一次，计算一次item的高度，
        // listAdapter.getCount()小于等于8时计算两次高度相加
        for (int i = 0; i < listAdapter.getCount(); i += col) {
            // 获取listview的每一个item
            View listItem = listAdapter.getView(i, null, girdView);
            listItem.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            listItem.measure(0, 0);
            // 获取item的高度和
            totalHeight += listItem.getMeasuredHeight()+girdView.getVerticalSpacing();
        }
        // 获取listview的布局参数
        ViewGroup.LayoutParams params = girdView.getLayoutParams();
        // 设置高度
        params.height = totalHeight+girdView.getVerticalSpacing();
        // 设置参数
        girdView.setLayoutParams(params);

        ViewGroup.LayoutParams viewPagerParams = viewPager.getLayoutParams();
        viewPagerParams.height =  params.height;
        viewPager.setLayoutParams(viewPagerParams);

        mConversationService = mIMKit.getConversationService();

        //监听所有未读消息
        initConversationServiceAndListener();


        HttpConnect.post(this, "member_buy_upgrade_count", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                final JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            textStrings = new String[]{data.optJSONArray("data").optJSONObject(0).optString("one"),
                                    data.optJSONArray("data").optJSONObject(0).optString("two"),
                                    data.optJSONArray("data").optJSONObject(0).optString("three"),
                                    data.optJSONArray("data").optJSONObject(0).optString("four"),
                                    data.optJSONArray("data").optJSONObject(0).optString("five"),
                                    data.optJSONArray("data").optJSONObject(0).optString("six"),
                                    data.optJSONArray("data").optJSONObject(0).optString("severn"),
                                    data.optJSONArray("data").optJSONObject(0).optString("eight"),
                                    data.optJSONArray("data").optJSONObject(0).optString("nine"),
                                    data.optJSONArray("data").optJSONObject(0).optString("ten"),
                                    data.optJSONArray("data").optJSONObject(0).optString("eleven"),
                                    data.optJSONArray("data").optJSONObject(0).optString("twelve"),};

                        }
                    });

                }

            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initData() {

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

    private void setUpView() {
        girdView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                if(position==0)   //充值
                {
                    intent = new Intent(getActivity(), RechargeRMBActivity.class).putExtra("type","1");
                }else if(position==1) //提现
                {
                        String name = getActivity().getSharedPreferences("isExitsName", Context.MODE_PRIVATE).getString("name","");
                        if(TextUtils.isEmpty(name)||name.equals(""))
                        {
                            show();
                        }else
                        {
                            String phoneNum = LoginUser.getInstantiation(getActivity().getApplicationContext()).getLoginUser().getPhoneNum();
                            if(TextUtils.isEmpty(phoneNum))
                            {
                                show();
                            }else
                            {
                                intent = new Intent(getActivity(), TiXianActivity.class);
                            }
                        }
                }else if(position==2)  //购买资产包
                {
                    intent = new Intent(getActivity(), BuyAssetsActivity.class);
//                    intent = new Intent(getActivity(), VipUpgradeActivity.class);
                }else if(position==3)  //会员升级
                {
//                    intent = new Intent(getActivity(), VipUpgradeActivity.class);
//                    Bundle bundle=new Bundle();
//                    bundle.putStringArray("textStrings", textStrings);
//                    intent.putExtras(bundle);
                }else if(position==4)  //虚拟币转入
                {
//                    intent = new Intent(getActivity(), XuNiBiZhuanRuActivity.class);
                    intent = new Intent(getActivity(), HelperCenterActivity.class);
                }else if(position==5)  //决战
                {
//                    String phoneNum = LoginUser.getInstantiation(getActivity().getApplicationContext()).getLoginUser().getPhoneNum();
//                    if(TextUtils.isEmpty(phoneNum)||phoneNum.equals(""))
//                    {
//                        show();
//                    }else
//                    {
//                        intent = new Intent(getActivity(), ZhuanChuXuNIBiActivity.class);
//                    }
                    intent = new Intent(getActivity(), ShareOptionActivity.class);
                }else if(position==6)  //交易                    //TODO
                {
                    intent = new Intent(getActivity(), NewTransactionActivity.class);
                }else if(position==7)//在线咨询
                {
                    //intent = new Intent(getActivity(),AddWXKeFu.class);
                    //userid是客服帐号，第一个参数是客服帐号，第二个是组ID，如果没有，传0
                    EServiceContact contact = new EServiceContact(MyApplication.KEFU, 0);
                    //如果需要发给指定的客服帐号，不需要Server进行分流(默认Server会分流)，请调用EServiceContact对象
                    //的setNeedByPass方法，参数为false。
                    //contact.setNeedByPass(false);

                    intent = mIMKit.getChattingActivityIntent(contact);
                }
                if(intent!=null)  startActivity(intent);

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        //resume时需要检查全局未读消息数并做处理，因为离开此界面时删除了全局消息监听器
        mConversationUnreadChangeListener.onUnreadChange();
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
}
