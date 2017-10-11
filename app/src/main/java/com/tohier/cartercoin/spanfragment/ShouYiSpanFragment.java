package com.tohier.cartercoin.spanfragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.activity.SpanActivity;
import com.tohier.cartercoin.bean.PrizeBean;
import com.tohier.cartercoin.columnview.LuckSpanShouYi;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.Tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/5/27.
 */

public class ShouYiSpanFragment extends Fragment {

    private LuckSpanShouYi mLuckSpan;

    private View view;

    //奖项的名称
    private String[] mPrizeName2 = null;
    private String[] mPrizeDesc = null;
    private String []    mPrizeIcon2 = new String[50];
    private int []    mPrizeColor2 = new int[50];

    private String currentColor = "";

    private List<PrizeBean> datas = new ArrayList<PrizeBean>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shouyispan_layout,null,false);
        mLuckSpan = (LuckSpanShouYi) view.findViewById(R.id.ls_lucky);
        loadLuckListData();
        return view;
    }

    public LuckSpanShouYi getmLuckSpan() {
        return mLuckSpan;
    }


    public void loadLuckListData()
    {
        HashMap<String,String> par = new HashMap<>();
//        Random random = new Random();
//        // 0-4 的随机数 不包括5
//        group = random.nextInt(5);
//        // 1- 5 的随机数
//        group = group + 1 ;
        par.put("group","1");
        par.put("type","1");
        HttpConnect.post(((SpanActivity)getActivity()), "member_roulette_list", par, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {

                    Handler dataHandler = new Handler(getActivity()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {
                            JSONArray array = data
                                    .optJSONArray("data");
                            if (array.size() != 0) {
                                mPrizeName2 = new String[array.size()];
                                mPrizeDesc = new String[array.size()];
                                for(int i = 0 ; i < array.size() ; i ++)
                                {
                                    final String id = array.getJSONObject(i).getString("id");
                                    String name = array.getJSONObject(i).getString("name");
                                    final String money = array.getJSONObject(i).getString("money");
                                    final String pic = array.getJSONObject(i).getString("pic");
                                    final String type = array.getJSONObject(i).getString("type");
                                    PrizeBean prizeBean = new PrizeBean(id,name,money,pic,type);
                                    datas.add(prizeBean);

                                    mPrizeIcon2[i] = pic;

                                    String desc = "";
                                    if(name.contains("("))
                                    {
                                        desc = name.substring(name.indexOf("("),name.length());
                                        name = name.substring(0,name.indexOf("("));
                                    }
                                    mPrizeDesc[i] = desc;
                                    mPrizeName2[i] = name;

                                    if(null!= LoginUser.getInstantiation(getActivity().getApplicationContext()).getLoginUser())
                                    {
                                        if(null!=LoginUser.getInstantiation(getActivity().getApplicationContext()).getLoginUser().getSex())
                                        {
                                            String sex = LoginUser.getInstantiation(getActivity().getApplicationContext()).getLoginUser().getSex();
                                            //默认是男生的
                                            if(sex.equals("美女"))
                                            {
                                                if(currentColor.equals(""))
                                                {
                                                    mPrizeColor2[i] = 0Xfff986c1;
                                                    currentColor = "0Xfff986c1";
                                                }else if(currentColor.equals("0XFFe90dc7"))
                                                {
                                                    mPrizeColor2[i] = 0Xfff986c1;
                                                    currentColor = "0Xfff986c1";
                                                }else if(currentColor.equals("0Xfff986c1"))
                                                {
                                                    mPrizeColor2[i] = 0XFFf668bd;
                                                    currentColor = "0XFFf668bd";
                                                }else if(currentColor.equals("0XFFf668bd"))
                                                {
                                                    mPrizeColor2[i] = 0XFFe90dc7;
                                                    currentColor = "0XFFe90dc7";
                                                }
                                            }else if(sex.equals("帅哥"))
                                            {
                                                if(currentColor.equals(""))
                                                {
                                                    mPrizeColor2[i] = 0XFFe4f5d5;
                                                    currentColor = "0XFFe4f5d5";
                                                }else if(currentColor.equals("0XFFa9e7e6"))
                                                {
                                                    mPrizeColor2[i] = 0XFFe4f5d5;
                                                    currentColor = "0XFFe4f5d5";
                                                }else if(currentColor.equals("0XFFe4f5d5"))
                                                {
                                                    mPrizeColor2[i] = 0XFF88b8b7;
                                                    currentColor = "0XFF88b8b7";
                                                }else if(currentColor.equals("0XFF88b8b7"))
                                                {
                                                    mPrizeColor2[i] = 0XFFa9e7e6;
                                                    currentColor = "0XFFa9e7e6";
                                                }
                                            }else
                                            {
                                                if(currentColor.equals(""))
                                                {
                                                    mPrizeColor2[i] = 0XFF6d428d;
                                                    currentColor = "0XFF6d428d";
                                                }else if(currentColor.equals("0XFFeb6100"))
                                                {
                                                    mPrizeColor2[i] = 0XFF6d428d;
                                                    currentColor = "0XFF6d428d";
                                                }else if(currentColor.equals("0XFF6d428d"))
                                                {
                                                    mPrizeColor2[i] = 0XFFbc4e00;
                                                    currentColor = "0XFFbc4e00";
                                                }else if(currentColor.equals("0XFFbc4e00"))
                                                {
                                                    mPrizeColor2[i] = 0XFFeb6100;
                                                    currentColor = "0XFFeb6100";
                                                }
                                            }
                                        }
                                    }else
                                    {
                                        if(currentColor.equals(""))
                                        {
                                            mPrizeColor2[i] = 0XFFe4f5d5;
                                            currentColor = "0XFFe4f5d5";
                                        }else if(currentColor.equals("0XFFa9e7e6"))
                                        {
                                            mPrizeColor2[i] = 0XFFe4f5d5;
                                            currentColor = "0XFFe4f5d5";
                                        }else if(currentColor.equals("0XFFe4f5d5"))
                                        {
                                            mPrizeColor2[i] = 0XFF88b8b7;
                                            currentColor = "0XFF88b8b7";
                                        }else if(currentColor.equals("0XFF88b8b7"))
                                        {
                                            mPrizeColor2[i] = 0XFFa9e7e6;
                                            currentColor = "0XFFa9e7e6";
                                        }
                                    }
                                }
                                mLuckSpan.setmPrizeName(mPrizeName2);
                                mLuckSpan.setmPrizeDesc(mPrizeDesc);
                                mLuckSpan.setmPrizeIcon2(mPrizeIcon2);
                                mLuckSpan.setmSpanColor(mPrizeColor2);
                                mLuckSpan.setmSpanCount(mPrizeName2.length);
                                mLuckSpan.startLoadBitmap();
                                mLuckSpan.startCanvas();
                                //TODO  显示
//                                mIvStart.setVisibility(View.VISIBLE);
                            }
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }else
                {
                    final String msg8 = data.getString("msg");
                    Handler dataHandler = new Handler(getActivity()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {
                            if(null!=msg8&&!Tools.isPhonticName(msg8))
                            {
                                ((SpanActivity)getActivity()).sToast(msg8);
                            }
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }
            }

            @Override
            public  void onFailure(Request arg0, IOException arg1) {
                Handler dataHandler = new Handler(getActivity()
                        .getMainLooper()) {

                    @Override
                    public void handleMessage(final Message msg) {
                        ((SpanActivity)getActivity()).sToast("请检查您的网络链接状态");
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        });
    }

}
