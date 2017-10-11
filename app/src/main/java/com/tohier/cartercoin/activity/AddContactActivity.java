package com.tohier.cartercoin.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.adapter.ContactAdapter;
import com.tohier.cartercoin.adapter.HorizontalListViewAdapter;
import com.tohier.cartercoin.bean.ZanRanking;
import com.tohier.cartercoin.columnview.HorizontalListView;
import com.tohier.cartercoin.columnview.LoadingView;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class AddContactActivity extends MyBaseActivity implements View.OnClickListener {

    private boolean isrepeat = false;
    private LinearLayout llErweima,llSaoYiSao;
    private HorizontalListView hlv_contact;
    private TextView llQuery;
    private EditText etQuery;
    private ListView lvContact;
    private ContactAdapter adapter;
    private HorizontalListViewAdapter adapter1;
    private ArrayList<ZanRanking> list_data = new ArrayList<ZanRanking>();
    private ArrayList<ZanRanking> list_tuijian= new ArrayList<ZanRanking>();
    private ImageView ivCancel;
    private GifImageView gifLloading;
    private ArrayList<HashMap<String, String>> readContact = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String, String>> readContact1 = new ArrayList<HashMap<String, String>>();
    private int count;//手机号执行次数
    private int count1 = 400;//一次性读取的数

    private int c = 0;

    private LoadingView ll_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        init();
        setUp();

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
        llErweima = (LinearLayout) findViewById(R.id.ll_erweima);
        llSaoYiSao = (LinearLayout) findViewById(R.id.ll_saoyisao);
        llQuery  = (TextView) findViewById(R.id.ll_query);
        etQuery = (EditText) findViewById(R.id.et_query);
        lvContact = (ListView) findViewById(R.id.lv_contact);
        ivCancel = (ImageView) findViewById(R.id.iv_cancel);
        ll_loading = (LoadingView) findViewById(R.id.cif_loading);
        hlv_contact = (HorizontalListView) findViewById(R.id.hlv_contact);

        adapter = new ContactAdapter(list_data,this);
        lvContact.setAdapter(adapter);

        adapter1 = new HorizontalListViewAdapter(this,list_tuijian);
        hlv_contact.setAdapter(adapter1);

        readContact = readContact();
        readContact1 = readContact();

        getTuiJianContact();

        int size = readContact.size();
        int x = readContact.size()/count1;
        int y = readContact.size()%count1;
        if (y == 0){
            count = x;
        }else{
            count = x+1;
        }

        for (int j = 0;j < count; j++){

            if(readContact.size()!=0)
            {
                ll_loading.setVisibility(View.VISIBLE);
                StringBuffer stringBuffer = new StringBuffer();

                if (readContact.size()/count1>0 ){
                    for(int i = 0 ; i < count1 ; i ++)
                    {
                        HashMap<String,String>  hashMap = readContact.get(0);
                        if(hashMap!=null)
                        {
                            String phone =  hashMap.get("phone");
                            if(i!=readContact.size()-1)
                            {
                                stringBuffer.append(phone+",");
                            }else
                            {
                                stringBuffer.append(phone);
                            }

                            readContact.remove(0);
                        }
                    }
                }else {
                    if (readContact.size()%count1>0){
                        for(int i = 0 ; i < readContact.size() ; i ++)
                        {
                            HashMap<String,String>  hashMap = readContact.get(i);
                            if(hashMap!=null)
                            {
                                String phone =  hashMap.get("phone");
                                if(i!=readContact.size()-1)
                                {
                                    stringBuffer.append(phone+",");
                                }else
                                {
                                    stringBuffer.append(phone);
                                }
                            }
                        }
                    }

                }

                String contactPhoneStr = stringBuffer.toString();
                getPhoneContact(contactPhoneStr);

            }
        }

    }


    /**
     * 一次最多读取四百个
     * @param contactPhoneStr
     */
    private void getPhoneContact(String contactPhoneStr){
        c++;
        Map<String, String> map = new HashMap<String, String>();
        map.put("number",contactPhoneStr);
        HttpConnect.post(this,"member_friends_phone" , map,
                new Callback() {

                    @Override
                    public void onResponse(Response arg0)
                            throws IOException {

                        final JSONObject object = JSONObject.fromObject(arg0
                                .body().string());
                        if (object.get("status").equals("success")) {

                            JSONArray array = object
                                    .optJSONArray("data");
                            if (array.size() != 0) {

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
                                    String sta = object2.optString("sta");
                                    String bgpic = object2.optString("backgroundpic");
                                    String paymentCode = object2.optString("paymentcode");

                                    zanRanking.setPic(pic);
                                    zanRanking.setArea(ip);
                                    zanRanking.setCount(inti);
                                    zanRanking.setType(type);
                                    zanRanking.setLinkCode(lk);
                                    zanRanking.setId(id);
                                    zanRanking.setPaymentCode(paymentCode);
                                    if(readContact1.size()!=0)
                                    {
                                        for(int a = 0 ; a < readContact1.size() ; a++)
                                        {
                                            HashMap<String,String> contact = readContact1.get(a);
                                            if(contact.get("phone").equals(mobile))
                                            {
                                                zanRanking.setName(name+"（"+contact.get("name")+"）");
                                            }
                                        }
                                    }
                                    zanRanking.setLevel1(grade);
                                    zanRanking.setMobile(mobile);
                                    zanRanking.setOneau(oneau);
                                    zanRanking.setRemark(remark);
                                    zanRanking.setSex(sex);
                                    zanRanking.setSta(sta);
                                    zanRanking.setBackgroundpic(bgpic);

                                    list_data.add(zanRanking);

                                    zanRanking = null;
                                }

                                Handler dataHandler = new Handler(
                                        getContext().getMainLooper()) {

                                    @Override
                                    public void handleMessage(
                                            final Message msg) {

                                        if (c == count){
                                            adapter.setPersonList(list_data);
                                            adapter.notifyDataSetChanged();
                                            ll_loading.setVisibility(View.GONE);
                                        }

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
//                                    if (c == count){
                                        ll_loading.setVisibility(View.GONE);
//                                    }
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
//                                if (c == count){
                                    ll_loading.setVisibility(View.GONE);
//                                }
                            }
                        };
                        dataHandler.sendEmptyMessage(0);
                    }
                });
    }


    private void setUp() {
        llErweima.setOnClickListener(this);
        llSaoYiSao.setOnClickListener(this);
        llQuery.setOnClickListener(this);
        ivCancel.setOnClickListener(this);

             /**
         * 进入详情
         */
        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ZanRanking zanRanking = list_data.get(position);
                int isFriend = 0;

                if(zanRanking.getSta().equals("1")){
                    isFriend = 0;
                }else if (zanRanking.getSta().equals("0") || zanRanking.getSta().equals("2")){
                    isFriend = 1;
                }
                Intent intent = new Intent(AddContactActivity.this, FriendsInfoActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("zanRanking",zanRanking);
                intent.putExtras(mBundle);
                intent.putExtra("isFrend",isFriend);
                startActivity(intent);
            }
        });

        /**
         * 进入详情
         */
        hlv_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ZanRanking zanRanking = list_tuijian.get(position);
                int isFriend = 0;

                if(zanRanking.getSta().equals("1")){
                    isFriend = 0;
                }else if (zanRanking.getSta().equals("0") || zanRanking.getSta().equals("2")){
                    isFriend = 1;
                }
                Intent intent = new Intent(AddContactActivity.this, FriendsInfoActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("zanRanking",zanRanking);
                intent.putExtras(mBundle);
                intent.putExtra("isFrend",isFriend);
                startActivity(intent);
            }
        });


        etQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")){
                    ivCancel.setVisibility(View.GONE);
                }else{
                    ivCancel.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    public void back(View view){
        finish();
    }


    /**
     *得到联系人
     **/
    private ArrayList<HashMap<String, String>> readContact() {
        // 首先,从raw_contacts中读取联系人的id("contact_id")
        // 其次, 根据contact_id从data表中查询出相应的电话号码和联系人名称
        // 然后,根据mimetype来区分哪个是联系人,哪个是电话号码
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try{
            Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null, null);
            //moveToNext方法返回的是一个boolean类型的数据
            while (cursor.moveToNext( )) {
                isrepeat = false;
                //读取通讯录的姓名
                HashMap<String, String> map = new HashMap<String, String>();

                String name = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                //读取通讯录的号码
                String number = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                for(int i = 0 ; i < list.size() ; i++)
                {
                    HashMap<String,String> hashMap = list.get(i);
                    if(hashMap.get("phone").equals(number))
                    {
                        isrepeat = true;
                    }
                }
                if(!isrepeat)
                {
                    if(number.contains("+86"))
                    {
                        number = number.replace("+86","");
                    }else if(number.contains("+86 "))
                    {
                        number = number.replace("+86 ","");
                    }
                    map.put("name", name);
                    map.put("phone", number);
                    list.add(map);
                }
            }
            cursor.close();
        }catch(Exception e){

        }

        return list;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_cancel:
                etQuery.setText("");
                break;
            case R.id.ll_erweima:
                startActivity(new Intent(this,MyQrCode.class));
                break;
            case R.id.ll_saoyisao:
                startActivityForResult(new Intent(this, CaptureActivity.class), 0);
                break;
            case R.id.ll_query:
                final String str = etQuery.getText().toString();
                if (!TextUtils.isEmpty(str)){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("number",str);
                    HttpConnect.post(this,"member_friends_search" , map,
                            new Callback() {

                                @Override
                                public void onResponse(Response arg0)
                                        throws IOException {
                                    list_data.clear();

                                    final JSONObject object = JSONObject.fromObject(arg0
                                            .body().string());
                                    if (object.get("status").equals("success")) {

                                        JSONArray array = object
                                                .optJSONArray("data");
                                        if (array.size() != 0) {

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
                                                String sta = object2.optString("sta");
                                                String bgpic = object2.optString("backgroundpic");
                                                String paymentCode = object2.optString("paymentcode");

                                                zanRanking.setPic(pic);
                                                zanRanking.setArea(ip);
                                                zanRanking.setCount(inti);
                                                zanRanking.setType(type);
                                                zanRanking.setLinkCode(lk);
                                                zanRanking.setId(id);
                                                zanRanking.setName(name);
                                                zanRanking.setLevel1(grade);
                                                zanRanking.setMobile(mobile);
                                                zanRanking.setOneau(oneau);
                                                zanRanking.setRemark(remark);
                                                zanRanking.setSex(sex);
                                                zanRanking.setSta(sta);
                                                zanRanking.setBackgroundpic(bgpic);
                                                zanRanking.setPaymentCode(paymentCode);

                                                list_data.add(zanRanking);

                                                zanRanking = null;
                                            }

                                            Handler dataHandler = new Handler(
                                                    getContext().getMainLooper()) {

                                                @Override
                                                public void handleMessage(
                                                        final Message msg) {
                                                    adapter.setPersonList(list_data);
                                                    adapter.notifyDataSetChanged();
                                                    ll_loading.setVisibility(View.GONE);

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
                                                if (str.equalsIgnoreCase(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getPhoneNum())){
                                                    sToast("不能添加自己为好友！");
                                                }else{
                                                    sToast("该用户不存在！");
                                                }

                                                adapter.setPersonList(list_data);
                                                adapter.notifyDataSetChanged();
                                                ll_loading.setVisibility(View.GONE);
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
//                                            sToast("链接超时！");
                                            ll_loading.setVisibility(View.GONE);
                                        }
                                    };
                                    dataHandler.sendEmptyMessage(0);
                                }
                            });
                }
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_OK) {
            if (requestCode == 0) {
                String scanResult = data.getStringExtra(CaptureActivity.EXTRA_RESULT);
                if (scanResult != null && scanResult.length() > 0) {
                    thisPersonExist(scanResult);
                }
            }
        }
    }

    /**
     * 验证付款人是否存在
     */
    public void thisPersonExist(final String linkcode)
    {
        String lk = linkcode.substring(linkcode.indexOf(":")+1,linkcode.length());
        Map<String, String> par = new HashMap<String, String>();
        par.put("number", lk);
        HttpConnect.post(this,"member_friends_search" , par,
                new Callback() {
                    @Override
                    public void onResponse(Response arg0)
                            throws IOException {
                        list_data.clear();
                        final JSONObject object = JSONObject.fromObject(arg0
                                .body().string());
                        if (object.get("status").equals("success")) {

                            JSONArray array = object
                                    .optJSONArray("data");
                            if (array.size() != 0) {

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
                                    String sta = object2.optString("sta");
                                    String bgpic = object2.optString("backgroundpic");

                                    zanRanking.setPic(pic);
                                    zanRanking.setArea(ip);
                                    zanRanking.setCount(inti);
                                    zanRanking.setType(type);
                                    zanRanking.setLinkCode(lk);
                                    zanRanking.setId(id);
                                    zanRanking.setName(name);
                                    zanRanking.setLevel1(grade);
                                    zanRanking.setMobile(mobile);
                                    zanRanking.setOneau(oneau);
                                    zanRanking.setRemark(remark);
                                    zanRanking.setSex(sex);
                                    zanRanking.setSta(sta);
                                    zanRanking.setBackgroundpic(bgpic);

                                    list_data.add(zanRanking);

                                    zanRanking = null;
                                }

                                Handler dataHandler = new Handler(
                                        getContext().getMainLooper()) {

                                    @Override
                                    public void handleMessage(
                                            final Message msg) {
                                        adapter.setPersonList(list_data);
                                        adapter.notifyDataSetChanged();

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
                                    sToast("该用户不存在！");
                                    adapter.setPersonList(list_data);
                                    adapter.notifyDataSetChanged();
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
//                                sToast("链接超时！");
                            }
                        };
                        dataHandler.sendEmptyMessage(0);
                    }
                });
    }


    /**
     * 推荐好友
     */
    private void getTuiJianContact(){
        HttpConnect.post(this,"member_friends_recommend" , null,
                new Callback() {
                    @Override
                    public void onResponse(Response arg0)
                            throws IOException {
                        list_tuijian.clear();
                        final JSONObject object = JSONObject.fromObject(arg0
                                .body().string());
                        if (object.get("status").equals("success")) {

                            JSONArray array = object
                                    .optJSONArray("data");
                            if (array.size() != 0) {

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
                                    String sta = object2.optString("sta");
                                    String bgpic = object2.optString("backgroundpic");
                                    String paymentCode = object2.optString("paymentcode");

                                    zanRanking.setPic(pic);
                                    zanRanking.setArea(ip);
                                    zanRanking.setCount(inti);
                                    zanRanking.setType(type);
                                    zanRanking.setLinkCode(lk);
                                    zanRanking.setId(id);
                                    zanRanking.setName(name);
                                    zanRanking.setLevel1(grade);
                                    zanRanking.setMobile(mobile);
                                    zanRanking.setOneau(oneau);
                                    zanRanking.setRemark(remark);
                                    zanRanking.setSex(sex);
                                    zanRanking.setSta(sta);
                                    zanRanking.setBackgroundpic(bgpic);
                                    zanRanking.setPaymentCode(paymentCode);

                                    list_tuijian.add(zanRanking);

                                    zanRanking = null;
                                }

                                Handler dataHandler = new Handler(
                                        getContext().getMainLooper()) {

                                    @Override
                                    public void handleMessage(
                                            final Message msg) {
                                        adapter1.setPersonList(list_tuijian);
                                        adapter1.notifyDataSetChanged();

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
//                                sToast("链接超时！");
                            }
                        };
                        dataHandler.sendEmptyMessage(0);
                    }
                });
    }
}
