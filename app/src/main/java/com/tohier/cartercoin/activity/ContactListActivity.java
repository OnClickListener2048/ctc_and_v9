package com.tohier.cartercoin.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.adapter.QuickIndexAdapter;
import com.tohier.cartercoin.bean.ZanRanking;
import com.tohier.cartercoin.columnview.LoadingView;
import com.tohier.cartercoin.columnview.NoDataView;
import com.tohier.cartercoin.columnview.NoLinkView;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.MyApplication;
import com.tohier.cartercoin.config.PinyinUtil;
import com.tohier.cartercoin.config.Tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

;

public class ContactListActivity extends MyBackBaseActivity implements View.OnClickListener,AbsListView.OnScrollListener{

    private CircleImageView pv_touxiang;
    private TextView tvZan,tvZaned,tvName,tvMoren,tvQinmi;
    private EditText etQuery;
    private LinearLayout ll_add_contact,ll_tribe,ll_info,ll_zan_record;
    private ImageView iv_dian;
    private ListView lvContact;
    private ArrayList<ZanRanking> list_data = new ArrayList<ZanRanking>(); //亲密度list
    private ArrayList<ZanRanking> list_data1 = new ArrayList<ZanRanking>(); //未点赞list
    private ArrayList<ZanRanking> list_data2 = new ArrayList<ZanRanking>(); //搜索list


    private QuickIndexAdapter adapter;
    private View popupView,vHead;
    private PopupWindow window;
    /**
     * 0---亲密度
     * 1---未点赞
     */
    private String isIndex = "0";
    private String isQuery = "0"; // 0---否  1---是

    private TextView tv_baoyou_order;
    private LoadingView gifLoading,loadingView;
    private NoDataView noDataView;
    private NoLinkView noLinkView;

    /**
     * 页数
     */
    private int page = 1;//亲密度
    private int page1 = 1;//未点赞

    private boolean isLastRow = false;

    private boolean isFrist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        init();
        setUpView();

    }

    private void init() {
        MyApplication.maps.put("ContactListActivity",ContactListActivity.this);

        //头布局
        vHead= View.inflate(this, R.layout.activity_contact_header, null);
        pv_touxiang = (CircleImageView) vHead.findViewById(R.id.pv_touxiang);
        tvName = (TextView) vHead.findViewById(R.id.tv_name);
        tvZan = (TextView) vHead.findViewById(R.id.tv_zan);
        tvZaned = (TextView) vHead.findViewById(R.id.tv_zaned);
        tv_baoyou_order = (TextView) findViewById(R.id.tv_baoyou_order);
        etQuery = (EditText) vHead.findViewById(R.id.et_query);
        ll_add_contact = (LinearLayout) vHead.findViewById(R.id.ll_add_contact);
        ll_tribe = (LinearLayout) vHead.findViewById(R.id.ll_tribe);
        ll_info = (LinearLayout) vHead.findViewById(R.id.ll_info);
        iv_dian = (ImageView) vHead.findViewById(R.id.iv_dian);
        ll_zan_record = (LinearLayout) vHead.findViewById(R.id.ll_zan_record);

        popupView = View.inflate(this,R.layout.popupwindow_layout,null);
        tvMoren = (TextView) popupView.findViewById(R.id.tv_moren);
        tvQinmi = (TextView) popupView.findViewById(R.id.tv_qinmi);

        lvContact = (ListView) findViewById(R.id.lv_contact_moren);
        gifLoading = (LoadingView) findViewById(R.id.cif_loading);
        noDataView = (NoDataView) findViewById(R.id.iv_nodata);
        noLinkView = (NoLinkView) findViewById(R.id.iv_no_link);
        loadingView = new LoadingView(this);
        loadingView.loadMore();
        loadingView.setGravity(Gravity.CENTER);

        tvName.setFocusable(true);

        Glide.with(this).load(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getHeadUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(pv_touxiang) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                pv_touxiang.setImageDrawable(circularBitmapDrawable);
            }
        });

        lvContact.addHeaderView(vHead);

        tvName.setText(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getNickName());
        adapter = new QuickIndexAdapter(list_data,ContactListActivity.this,tvZan);
        adapter.setIsBottom("0");
        lvContact.setAdapter(adapter);

        if (Tools.getAPNType(ContactListActivity.this) == true){
            noLinkView.setVisibility(View.GONE);
            getZanInfo();
            getContacts(list_data,isIndex,page+"",1);
        }else{
            noLinkView.setVisibility(View.VISIBLE);
        }


    }

    private void setUpView() {
        ll_add_contact.setOnClickListener(this);
        ll_tribe.setOnClickListener(this);
        ll_info.setOnClickListener(this);
        ll_zan_record.setOnClickListener(this);

        tvMoren.setOnClickListener(this);
        tvQinmi.setOnClickListener(this);

        lvContact.setOnScrollListener(this);

        etQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                list_data2.clear();
                if (TextUtils.isEmpty(s.toString())){
                    isQuery = "0";
                    if (isIndex.equals("1")){
                        adapter.setPersonList(list_data1);
                    }else if (isIndex.equals("0")){//亲密度
                        adapter.setPersonList(list_data);
                    }
                    adapter.notifyDataSetChanged();

                    ll_info.setVisibility(View.VISIBLE);
//                    ll_tribe.setVisibility(View.VISIBLE);
//                    ll_add_contact.setVisibility(View.VISIBLE);
                    loadingView.noMoreData(list_data.size()+"位联系人");
                }else{
                    isQuery = "1";
                    for (ZanRanking z :list_data){
                        if (z.getName().contains(s.toString()) || z.getLinkCode().contains(s.toString()) || z.getMobile().contains(s.toString()) || contain2(z.getPinYin(),s.toString())){
                            list_data2.add(z);
                        }
                    }

                    adapter.setPersonList(list_data2);
                    adapter.notifyDataSetChanged();

                    ll_info.setVisibility(View.GONE);
//                    ll_tribe.setVisibility(View.GONE);
                    ll_add_contact.setVisibility(View.GONE);
                    loadingView.noMoreData(list_data2.size()+"位联系人");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

//
//        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ZanRanking zanRanking = null;
//                if (isIndex.equals("0") && isQuery.equals("0")){
//                    zanRanking = list_data.get(position-1);
//                }else if (isIndex.equals("1") && isQuery.equals("0")){
//                    zanRanking = list_data1.get(position-1);
//                }else {
//                    zanRanking = list_data2.get(position-1);
//                }
//                Intent intent = new Intent(ContactListActivity.this, FriendsInfoActivity.class);
//                Bundle mBundle = new Bundle();
//                mBundle.putSerializable("zanRanking",zanRanking);
//                intent.putExtras(mBundle);
//                intent.putExtra("isFrend",0);
//                intent.putExtra("position",position);
//                startActivity(intent);
//            }
//        });

        noLinkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tools.getAPNType(ContactListActivity.this) == true){
                    noLinkView.setVisibility(View.GONE);
                    getZanInfo();
                    getContacts(list_data,isIndex,page+"",1);
                }else{
                    noLinkView.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    /**
     * 宝友数据
     * @param isIndex 列表排序类型
     */
    public void getContacts(final ArrayList<ZanRanking> list_data, String isIndex, final String page, final int isClear) {

        if (Tools.getAPNType(ContactListActivity.this) == true){
            noLinkView.setVisibility(View.GONE);
            if (isClear == 1) {
                gifLoading.setVisibility(View.VISIBLE);

            }else{
                if (lvContact.getFooterViewsCount()<=0){
                    lvContact.addFooterView(loadingView);
                }
                loadingView.setVisibility(View.VISIBLE);
            }
            HashMap<String , String> map = new HashMap<String,String>();
            map.put("type",isIndex);
            map.put("page",page);
            HttpConnect.post(this,"member_friends_list" , map,
                    new Callback() {

                        @Override
                        public void onResponse(final Response arg0)
                                throws IOException {
                            final JSONObject object = JSONObject.fromObject(arg0
                                    .body().string());
                            if (object.get("status").equals("success")) {

                                final JSONArray array = object
                                        .optJSONArray("data");
                                if (array.size() != 0 ) {

                                    Handler dataHandler = new Handler(
                                            getContext().getMainLooper()) {

                                        @Override
                                        public void handleMessage(
                                                final Message msg) {
                                            for (int i = 0; i < array.size(); i++) {
                                                JSONObject object2 = array
                                                        .getJSONObject(i);
                                                ZanRanking zanRanking = new ZanRanking();
                                                String id = object2.optString("id");
                                                String name = object2.optString("name");
                                                String pic =  object2.optString("pic");
                                                String lk = object2.optString("lk");
                                                String type = object2.optString("type");
                                                String grade =  object2.optString("grade");
                                                String inti =   object2.optString("inti");
                                                String ip =  object2.optString("ip");
                                                String mobile =  object2.optString("mobile");
                                                String remark = object2.optString("remark");
                                                String oneau = object2.optString("oneau");
                                                String sex = object2.optString("sex");
                                                String bgpic = object2.optString("backgroundpic");
                                                String paymentCode = object2.optString("paymentcode");
                                                String pinyin = "";
                                                try{
                                                    pinyin = PinyinUtil.getPinyin(name.trim().substring(0,1));
                                                }catch (Exception e){
                                                    pinyin = "#";
                                                }
                                                zanRanking.setPic(pic);
                                                zanRanking.setArea(ip);
                                                zanRanking.setCount(inti);
                                                zanRanking.setType(type);
                                                zanRanking.setLinkCode(lk);
                                                zanRanking.setId(id);
                                                zanRanking.setName(name);
                                                zanRanking.setLevel1(grade);
                                                zanRanking.setPinYin(pinyin);
                                                zanRanking.setMobile(mobile);
                                                zanRanking.setOneau(oneau);
                                                zanRanking.setRemark(remark);
                                                zanRanking.setSex(sex);
                                                zanRanking.setBackgroundpic(bgpic);
                                                zanRanking.setPaymentCode(paymentCode);

                                                String pra = object2.optString("pra");
                                                String bepra = object2.optString("bepra");
                                                String zanType = "";

                                                if (pra.equals("0") && bepra.equals("1")){
                                                    zanType = "1";
                                                }else if (pra.equals("1") && bepra.equals("0")){
                                                    zanType = "0";
                                                }else if (pra.equals("1") && bepra.equals("1")){
                                                    zanType = "2";
                                                }else if (pra.equals("0") && bepra.equals("0")){
                                                    zanType = "3";
                                                }

                                                zanRanking.setZanType(zanType);

                                                list_data.add(zanRanking);
                                                zanRanking = null;
                                            }

                                            adapter.setPersonList(list_data);
//                                                if (list_data.size()<50){
//                                                    adapter.setIsBottom("1");
//                                                }else{
//                                                    adapter.setIsBottom("0");
//                                                }
                                            adapter.notifyDataSetChanged();
                                            gifLoading.setVisibility(View.GONE);
                                            if (isFrist == true){
                                                lvContact.setSelection(0);
                                            }

                                        }
                                    };
                                    dataHandler.sendEmptyMessage(0);

                                }else{
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            noDataView.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }

                            }else {
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        if (isClear == 1) {
//                                ivNodata.setVisibility(View.VISIBLE);
                                        }else {
                                            if (list_data.size()>12){
                                                loadingView.noMoreData(list_data.size()+"位联系人");
                                            }else{
                                                loadingView.noMoreData("没有更多宝友了");
                                            }

                                        }
                                    }
                                });
                            }

                        }


                        @Override
                        public void onFailure(Request arg0, IOException arg1) {


                            Handler dataHandler = new Handler(
                                    getContext().getMainLooper()) {

                                @Override
                                public void handleMessage(
                                        final Message msg) {
                                    gifLoading.setVisibility(View.GONE);
                                    if (list_data.size()>0){
                                        noDataView.setVisibility(View.GONE);
                                    }else{
                                        noDataView.setVisibility(View.VISIBLE);
                                    }
                                    tvMoren.setEnabled(true);
                                    tvQinmi.setEnabled(true);
                                }
                            };
                            dataHandler.sendEmptyMessage(0);
                        }
                    });

        } else{
            noLinkView.setVisibility(View.VISIBLE);
        }

    }


    /**
     * 自己的赞
     */
    public void getZanInfo(){
        HttpConnect.post(this, "member_be_praise", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equalsIgnoreCase("success")) {
                    final String have = data.optJSONArray("data").optJSONObject(0).optString("have");
                    final String be = data.optJSONArray("data").optJSONObject(0).optString("be");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvZan.setText(" "+have);
                            tvZaned.setText(" "+be);
                        }
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvZan.setText(" "+0);
                            tvZaned.setText(" "+0);
                        }
                    });
                }

            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {

            }
        });
    }
    /**
     * 申请数据
     */
    public void getAskFors() {

        HttpConnect.post(this,"member_friends_list_apply" , null,
                new Callback() {

                    @Override
                    public void onResponse(Response arg0)
                            throws IOException {

                        final JSONObject object = JSONObject.fromObject(arg0
                                .body().string());
                        if (object.get("status").equals("success")) {

                            final JSONArray array = object
                                    .optJSONArray("data");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (array.size() > 0) {

                                        for (int i = 0; i < array.size(); i++) {
                                            if (array.getJSONObject(i).optString("status").equals("接受")){
                                                iv_dian.setVisibility(View.VISIBLE);
                                                return;
                                            }else{
                                                iv_dian.setVisibility(View.GONE);
                                                return;
                                            }
                                        }

                                    }else{
                                        iv_dian.setVisibility(View.GONE);
                                    }
                                }
                            });

                        }else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    iv_dian.setVisibility(View.GONE);
                                }
                            });
                        }

                    }


                    @Override
                    public void onFailure(Request arg0, IOException arg1) {


                        Handler dataHandler = new Handler(
                                getContext().getMainLooper()) {

                            @Override
                            public void handleMessage(
                                    final Message msg) {
//                                sToast("链接超时！");
                            }
                        };
                        dataHandler.sendEmptyMessage(0);
                    }
                });

    }


    public void back(View view){
        finish();
    }

    /**
     * 排序
     * @param view
     */
    public void orderBy(View view){

        //创建PopupWindow对象，指定宽度和高度
        window = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        //设置动画
        window.setAnimationStyle(R.style.popup_window_anim1);
        //设置背景颜色
        Drawable drawable = getResources().getDrawable(R.mipmap.xiala);
        window.setBackgroundDrawable(drawable);
        // 设置可以获取焦点
        window.setFocusable(true);
        //设置可以触摸弹出框以外的区域
        window.setOutsideTouchable(true);
        // 更新popupwindow的状态
        window.update();
        //以下拉的方式显示，并且可以设置显示的位置
        window.showAsDropDown(findViewById(R.id.iv_order_by),0,20);


    }

    @Override
    public void onClick(View v1) {
        switch (v1.getId()){
            case R.id.ll_zan_record:
                startActivity(new Intent(this,ZanRecordActivity.class));
                break;
            case R.id.ll_info:
                startActivity(new Intent(this,AskForActivity.class));
                break;
            case R.id.ll_add_contact:
                startActivity(new Intent(this,AddContactActivity.class));
                break;
            case R.id.ll_tribe:
                break;
            case R.id.tv_moren://亲密度
                isFrist = true;
                page = 1;
                isIndex = "0";
                if (list_data.size()>0){
                    list_data.clear();
                }
                getContacts(list_data,isIndex,page+"",1);
//                gifLoading.setVisibility(View.GONE);

                if (window.isShowing()){
                    window.dismiss();
                }
                tv_baoyou_order.setText("亲密度 ");
                break;
            case R.id.tv_qinmi:
                isFrist = true;
                page1 = 1;
                isIndex = "1";

                if (list_data1.size()>0){
                    list_data1.clear();
                }
                getContacts(list_data1,isIndex,page1+"",1);

                if (window.isShowing()){
                    window.dismiss();
                }
                tv_baoyou_order.setText("未点赞 ");
                break;

        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        getAskFors();
    }

    /***
     * 是否包含指定字符串,不区分大小写
     * @param input : 原字符串
     * @param regex
     * @return
     */
    public static boolean contain2(String input, String regex) {
        if(TextUtils.isEmpty(input)){
            return false;
        }
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(input);
        boolean result = m.find();
        return result;
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (isLastRow && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            //加载元素

            if (!isQuery.equals("1")){
                isFrist = false;
                if (isIndex.equals("0")){
                    page++;
                    getContacts(list_data,isIndex,page+"",2);

                }else  if (isIndex.equals("1")){
                    page1++;
                    getContacts(list_data1,isIndex,page1+"",2);

                }
            }
            isLastRow = false;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //如果当前列表的数量等于查询的总数量,则不做任何操作


        //判断是否滚到最后一行
        if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0) {
            isLastRow = true;
        }




    }


}
