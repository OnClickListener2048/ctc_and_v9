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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
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
import com.tohier.cartercoin.columnview.QuickIndexBar;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.MyApplication;
import com.tohier.cartercoin.config.PinyinUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;

public class ContactListActivity extends MyBaseActivity implements View.OnClickListener, View.OnTouchListener,AbsListView.OnScrollListener{

    private CircleImageView pv_touxiang;
    private TextView tvZan,tvZaned,tvName,tvLetterTip,tvMoren,tvQinmi;
    private EditText etQuery;
    private LinearLayout ll_add_contact,ll_tribe,ll_info;
    private ImageView iv_dian;
    private ListView lvContact;
    private ArrayList<ZanRanking> list_data = new ArrayList<ZanRanking>(); //亲密度list
    private ArrayList<ZanRanking> list_data1 = new ArrayList<ZanRanking>(); //默认list
    private ArrayList<ZanRanking> list_data2 = new ArrayList<ZanRanking>(); //搜索list


    /**
     * 亲密度备份
     */
    private ArrayList<ZanRanking> list_data_copy = new ArrayList<ZanRanking>(); //亲密度list
    /**
     * 适配的list
     */
    private ArrayList<ZanRanking> list_data_defult = new ArrayList<ZanRanking>();
    private ArrayList<ZanRanking> list_data_qinmi = new ArrayList<ZanRanking>();

    private QuickIndexAdapter adapter;
    private QuickIndexBar quickIndexBar;
    private View popupView,vHead,v;
    private PopupWindow window;
    private String isIndex = "1"; //1---显示首字母排序 0---不显示
    private String isQuery = "0"; // 0---否  1---是

    private TextView tv_baoyou_order;
    private GifImageView gifLoading;


    private int count;//执行次数
    private int count1 = 30;//一次性读取的数

    private int index = 0;
    private int index1 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        init();
        setUpView();

//        SliderConfig mConfig = new SliderConfig.Builder()
//                .primaryColor(Color.TRANSPARENT)
//                .secondaryColor(Color.TRANSPARENT)
//                .position(SliderPosition.LEFT)
//                .edge(false)
//                .build();
//
//        ISlider iSlider = SliderUtils.attachActivity(this, mConfig);
//        mConfig.setPosition(SliderPosition.LEFT);
//        iSlider.setConfig(mConfig);
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
        v = vHead.findViewById(R.id.v);


        popupView = View.inflate(this,R.layout.popupwindow_layout,null);
        tvMoren = (TextView) popupView.findViewById(R.id.tv_moren);
        tvQinmi = (TextView) popupView.findViewById(R.id.tv_qinmi);
        tvLetterTip = (TextView) findViewById(R.id.tv_letter_center_tip);


        lvContact = (ListView) findViewById(R.id.lv_contact_moren);
        quickIndexBar = (QuickIndexBar) findViewById(R.id.quick_index_bar);
        gifLoading = (GifImageView) findViewById(R.id.cif_loading);


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
        adapter = new QuickIndexAdapter(list_data_defult,list_data_copy,ContactListActivity.this,tvZan);
//        adapter.setIsIndex(isIndex);
        lvContact.setAdapter(adapter);

        getZanInfo();
        getContacts(isIndex);


        quickIndexBar.setOnLetterUpdateListener(new QuickIndexBar.OnLetterUpdateListener() {
            @Override
            public void onLetterUpdate(String letter) {
                tvLetterTip.setText(letter);
                tvLetterTip.setVisibility(View.VISIBLE);//屏幕中间给出提示当前触摸的字母
                for (int i = 0; i < list_data_defult.size(); i++) {
                    if( list_data_defult.get(i).getPinYin()!=null&& list_data_defult.get(i).getPinYin().length()>0)
                    {
                        String c = list_data_defult.get(i).getPinYin().charAt(0) + "";//遍历所有item的第一个汉字,并获取拼音的首字母大写
                        if (c.equalsIgnoreCase(letter)) {//当前的首字母和触摸字母的一致,把当前的条目移动到屏幕第一个条目位置
//                        lvContact.smoothScrollToPositionFromTop(i, 0, 100);//平滑移动
                            lvContact.setSelection(i+1);
                            break;
                        }
                    }
                }

            }

            @Override
            public void onHiddenLetter() {
                tvLetterTip.setVisibility(View.GONE);//隐藏屏幕中间给出提示当前触摸的字母
            }
        });

        quickIndexBar.setOnTouchListener(this);


    }

    private void setUpView() {
        ll_add_contact.setOnClickListener(this);
        ll_tribe.setOnClickListener(this);
        ll_info.setOnClickListener(this);

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
                        quickIndexBar.setVisibility(View.VISIBLE);
                        v.setVisibility(View.GONE);
                        adapter.setPersonList(list_data_defult);
                    }else if (isIndex.equals("0")){
                        quickIndexBar.setVisibility(View.GONE);
                        v.setVisibility(View.VISIBLE);
                        adapter.setPersonList(list_data_qinmi);
                    }
//                    adapter.setIsIndex(isIndex);
                    adapter.notifyDataSetChanged();

                    ll_info.setVisibility(View.VISIBLE);
//                    ll_tribe.setVisibility(View.VISIBLE);
                    ll_add_contact.setVisibility(View.VISIBLE);

                }else{
                    isQuery = "1";
                    for (ZanRanking z :list_data_copy){
                        if (z.getName().contains(s.toString()) || z.getLinkCode().contains(s.toString()) || z.getMobile().contains(s.toString()) || contain2(z.getPinYin(),s.toString())){
                            list_data2.add(z);
                        }
                    }
                    quickIndexBar.setVisibility(View.GONE);
                    v.setVisibility(View.VISIBLE);
                    adapter.setPersonList(list_data2);
                    adapter.setIsIndex("0");
                    adapter.notifyDataSetChanged();

                    ll_info.setVisibility(View.GONE);
//                    ll_tribe.setVisibility(View.GONE);
                    ll_add_contact.setVisibility(View.GONE);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ZanRanking zanRanking = null;
                if (isIndex.equals("0") && isQuery.equals("0")){
                    zanRanking = list_data_qinmi.get(position-1);
                }else if (isIndex.equals("1") && isQuery.equals("0")){
                    zanRanking = list_data_defult.get(position-1);
                }else {
                    zanRanking = list_data2.get(position-1);
                }
                Intent intent = new Intent(ContactListActivity.this, FriendsInfoActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("zanRanking",zanRanking);
                intent.putExtras(mBundle);
                intent.putExtra("isFrend",0);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });


    }


    /**
     * 宝友数据
     * @param isIndex 列表排序类型
     */
    public void getContacts(final String isIndex) {
        list_data1.clear();
        list_data.clear();
        gifLoading.setVisibility(View.VISIBLE);

            HttpConnect.post(this,"member_friends_list" , null,
                    new Callback() {

                        @Override
                        public void onResponse(Response arg0)
                                throws IOException {
                                final JSONObject object = JSONObject.fromObject(arg0
                                        .body().string());
                                if (object.get("status").equals("success")) {

                                    final JSONArray array = object
                                            .optJSONArray("data");
                                    if (array.size() != 0) {

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
                                                    list_data1.add(zanRanking);
                                                    zanRanking = null;
                                                }
                                                list_data_copy.addAll(list_data);

                                                Collections.sort(list_data1);
                                                quickIndexBar.setVisibility(View.VISIBLE);
                                                getContactDatas(list_data1,list_data_defult,isIndex,index);
                                                getContactDatas1(list_data,list_data_qinmi,index1);




                                            }
                                        };
                                        dataHandler.sendEmptyMessage(0);

                                    }
                                }else{
                                    Handler dataHandler = new Handler(
                                            getContext().getMainLooper()) {

                                        @Override
                                        public void handleMessage(
                                                final Message msg) {
                                            gifLoading.setVisibility(View.GONE);
                                        }
                                    };
                                    dataHandler.sendEmptyMessage(0);
                                }

                            }


                        @Override
                        public void onFailure(Request arg0, IOException arg1) {


                            Handler dataHandler = new Handler(
                                    getContext().getMainLooper()) {

                                @Override
                                public void handleMessage(
                                        final Message msg) {
//                                    sToast("链接超时！");
                                    gifLoading.setVisibility(View.GONE);
                                    tvMoren.setEnabled(true);
                                    tvQinmi.setEnabled(true);
                                }
                            };
                            dataHandler.sendEmptyMessage(0);
                        }
                    });

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
        Drawable drawable = getResources().getDrawable(R.mipmap.xiala1);
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
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                //按下
                quickIndexBar.setBackgroundColor(0xffe7e7e7);
                break;
            case MotionEvent.ACTION_MOVE:
                //移动
                break;
            case MotionEvent.ACTION_UP:
                //松开
                quickIndexBar.setBackgroundColor(0x00ffffff);
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v1) {
        switch (v1.getId()){
            case R.id.ll_info:
                startActivity(new Intent(this,AskForActivity.class));
                break;
            case R.id.ll_add_contact:
                startActivity(new Intent(this,AddContactActivity.class));
                break;
            case R.id.ll_tribe:
                break;
            case R.id.tv_moren:
                isIndex = "1";
//                adapter.setIsIndex(isIndex);
                adapter.setPersonList(list_data_defult);
                adapter.notifyDataSetChanged();
                quickIndexBar.setVisibility(View.VISIBLE);
                v.setVisibility(View.GONE);
//                gifLoading.setVisibility(View.GONE);

                if (window.isShowing()){
                    window.dismiss();
                }

                tv_baoyou_order.setText("默认");
                break;
            case R.id.tv_qinmi:
                isIndex = "0";
//                adapter.setIsIndex(isIndex);
                adapter.setPersonList(list_data_qinmi);
                adapter.notifyDataSetChanged();
                quickIndexBar.setVisibility(View.GONE);
                v.setVisibility(View.VISIBLE);
//                gifLoading.setVisibility(View.GONE);

                if (window.isShowing()){
                    window.dismiss();
                }
                tv_baoyou_order.setText("亲密度");
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

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //如果当前列表的数量等于查询的总数量,则不做任何操作

        if (isIndex.equals("1")){
            if(adapter.getCount() >= list_data_copy.size()){
                return;
            }

            if (view.getLastVisiblePosition() == (totalItemCount - 1)) { //判断是否滑动到最底部
                getContactDatas(list_data1,list_data_defult,isIndex,index);

            }
        }else{
            if(adapter.getCount() >= list_data_copy.size()){
                return;
            }
            if (view.getLastVisiblePosition() == (totalItemCount - 1)) { //判断是否滑动到最底部
                getContactDatas(list_data,list_data_qinmi,isIndex,index1);
                //已经滑动最底部了。
                Log.i("scorll","执行1");
            }
        }

    }

    /**
     *
     * @param list_data 原集合
     * @param list_data1 分批加载的集合
     */
    private void getContactDatas(ArrayList<ZanRanking> list_data , ArrayList<ZanRanking> list_data1 , String isIndex , int index){
        int x = list_data_copy.size()/count1;
        int y = list_data_copy.size()%count1;
        if (y == 0){
            count = x;
        }else{
            count = x+1;
        }

        if (index<count){

            if(list_data.size()!=0)
            {

                if (list_data.size()/count1>0 ){
                    for(int i = 0 ; i < count1 ; i ++)
                    {
                        ZanRanking zanRanking = list_data.get(0);
                        if (zanRanking != null){
                            list_data1.add(zanRanking);
                            list_data.remove(0);
                        }

                    }
                }else {
                    if (list_data.size()%count1>0){
                        for(int i = 0 ; i < list_data.size() ; i ++)
                        {
                            ZanRanking zanRanking = list_data.get(i);
                            if (zanRanking != null){
                                list_data1.add(zanRanking);
                            }
                        }
                    }

                }


            }

            index++;
//            adapter.setIsIndex(isIndex);
            adapter.setPersonList(list_data1);
            adapter.notifyDataSetChanged();
            v.setVisibility(View.GONE);
            gifLoading.setVisibility(View.GONE);
            Log.i("scorll","执行");
        }

    }


    /**
     *
     * @param list_data 原集合
     * @param list_data1 分批加载的集合
     */
    private void getContactDatas1(ArrayList<ZanRanking> list_data , ArrayList<ZanRanking> list_data1 , int index){
        int x = list_data_copy.size()/count1;
        int y = list_data_copy.size()%count1;
        if (y == 0){
            count = x;
        }else{
            count = x+1;
        }

        if (index<count){

            if(list_data.size()!=0)
            {

                if (list_data.size()/count1>0 ){
                    for(int i = 0 ; i < count1 ; i ++)
                    {
                        ZanRanking zanRanking = list_data.get(0);
                        if (zanRanking != null){
                            list_data1.add(zanRanking);
                            list_data.remove(0);
                        }

                    }
                }else {
                    if (list_data.size()%count1>0){
                        for(int i = 0 ; i < list_data.size() ; i ++)
                        {
                            ZanRanking zanRanking = list_data.get(i);
                            if (zanRanking != null){
                                list_data1.add(zanRanking);
                            }
                        }
                    }

                }


            }

            index++;

        }

    }

}
