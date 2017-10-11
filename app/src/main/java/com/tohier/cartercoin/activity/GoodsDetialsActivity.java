package com.tohier.cartercoin.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.mobileim.conversation.EServiceContact;
import com.example.imagedemo.ImagePagerActivity;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.bean.Goods;
import com.tohier.cartercoin.bean.Product;
import com.tohier.cartercoin.columnview.MyGridView;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.MyApplication;
import com.tohier.cartercoin.loader.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerClickListener;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GoodsDetialsActivity extends MyBaseActivity implements View.OnClickListener{

    private TextView tvGoodsName,tvGoodsName1,tvBuy,tvGoodssPrice,tvGoodsCount,tvBuyCount;
    private ImageView ivKeFu,ivReduce,ivAdd;
    private WebView webView;
    private String productId;
    private ArrayList<String> imageUrl = new ArrayList<String>();
    private List<Product> datas = new ArrayList<Product>();
    private List<Goods> goods1 = new ArrayList<Goods>();
    private MyGridView gridView;
    private Banner banner;
    private Goods goods;
    private int buyCount = 1;
    private double ctc;
    private double xiaofei;
    private PopupWindow window;
    private int count = 1;
    private int countAllow = 1;
    private View view1;

    private String renZhengType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detials);
        configImageLoader();
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

    private void setUp() {
        tvBuy.setOnClickListener(this);
        ivAdd.setOnClickListener(this);
        ivReduce.setOnClickListener(this);
        ivKeFu.setOnClickListener(this);
    }

    private void init() {

         view1 = View.inflate(GoodsDetialsActivity.this,R.layout.popupwindow_prompt_authentication,null);

        window = new PopupWindow(view1, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        
        HttpConnect.post(this, "member_count_detial", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){
                    ctc = Double.parseDouble(data.optJSONArray("data").optJSONObject(0).optString("ctc"));
                    xiaofei = Double.parseDouble(data.optJSONArray("data").optJSONObject(0).optString("ctccous"));
                }

            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {

            }
        });

        productId = getIntent().getStringExtra("id");
        tvGoodsCount = (TextView) findViewById(R.id.tv_goods_count);
        tvGoodsName = (TextView) findViewById(R.id.tv_goods_name);
        tvGoodsName1 = (TextView) findViewById(R.id.tv_goods_name1);
        tvBuy = (TextView) findViewById(R.id.tv_buy);
        tvGoodssPrice = (TextView) findViewById(R.id.tv_goods_price);
        tvBuyCount = (TextView) findViewById(R.id.tv_buy_count);
        ivKeFu = (ImageView) findViewById(R.id.iv_kefu);
        ivReduce = (ImageView) findViewById(R.id.iv_reduce);
        ivAdd = (ImageView) findViewById(R.id.iv_add);
        banner = (Banner) findViewById(R.id.banner);
        webView = (WebView) findViewById(R.id.web_detials);

        //设置轮播时间
        banner.setDelayTime(2500);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.RIGHT);

        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);
                return false;
            }
        });
        getPic();
        getJsonDate();

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tv_buy:
                  String name = GoodsDetialsActivity.this.getSharedPreferences("isExitsName", Context.MODE_PRIVATE).getString("name","");
                  if(TextUtils.isEmpty(name)||name.equals(""))
                  {
                      renZhengType = "name";
                      show();
                  }else
                  {
                      HttpConnect.post(this, "member_is_password_pay", null, new Callback() {
                          @Override
                          public void onResponse(Response arg0) throws IOException {
                              JSONObject data = JSONObject.fromObject(arg0.body()
                                      .string().trim());

                              if (data.get("status").equals("success")) {
                                  JSONArray dataArr = data.getJSONArray("data");

                                  JSONObject jsonObjNews = dataArr.optJSONObject(0);
                                  if(jsonObjNews!=null)
                                  {
                                      final String value = jsonObjNews.optString("value");

                                      runOnUiThread(new Runnable() {

                                          @Override
                                          public void run() {
                                              if (value.equals("0")){
                                                  sToast("请先设置交易密码");
                                                  startActivity(new Intent(GoodsDetialsActivity.this,SettingPayPwdActivity.class));
                                              }else{
                                                  try{

                                                      BigDecimal bd5 = new BigDecimal(goods.getUnitctc());
                                                      BigDecimal bd6 = new BigDecimal(buyCount);
                                                      double d = bd5.multiply(bd6).doubleValue();
//                                               if (ctc>=bd5.multiply(bd6).doubleValue()){
                                                      startActivity(new Intent(GoodsDetialsActivity.this,BuildOrderActivity.class)
                                                              .putExtra("name",goods.getName())
                                                              .putExtra("pic",goods.getPic())
                                                              .putExtra("ctcprice",goods.getUnitctc())
                                                              .putExtra("count",buyCount+"")
                                                              .putExtra("id",goods.getId())
                                                              .putExtra("desc",goods.getDesc())
                                                              .putExtra("price",bd5.multiply(bd6)+""));
                                                      finish();
//                                               }else{
//                                                   sToast("α资产不够，请充值");
//                                               }
                                                  }catch (Exception e){
                                                      sToast("商品已下架");
                                                  }

                                              }
                                          }
                                      });
                                  }

                              }
                          }

                          @Override
                          public void onFailure(Request arg0, IOException arg1) {


                          }
                      });

                  }
                break;
            case R.id.iv_kefu:

                //intent = new Intent(getActivity(),AddWXKeFu.class);
                //userid是客服帐号，第一个参数是客服帐号，第二个是组ID，如果没有，传0
                EServiceContact contact = new EServiceContact(MyApplication.KEFU, 0);
                //如果需要发给指定的客服帐号，不需要Server进行分流(默认Server会分流)，请调用EServiceContact对象
                //的setNeedByPass方法，参数为false。
                //contact.setNeedByPass(false);
                Intent intent1 = mIMKit.getChattingActivityIntent(contact);
                startActivity(intent1);
                break;
            case R.id.iv_reduce:
                if (buyCount>1){
                    buyCount-=1;
                }else{
                    sToast("商品数量不能再少了");
                }

                tvBuyCount.setText(buyCount+"");

                break;
            case R.id.iv_add:
                if (buyCount<count ){
                    if (buyCount < countAllow){
                        buyCount+=1;
                    }else{
                        sToast("最多允许购买"+countAllow+"件");
                    }

                }else{
                    sToast("库存不足");
                }
                tvBuyCount.setText(buyCount+"");
                break;
        }
    }



    public void getJsonDate() {
        Map<String, String> par5 = new HashMap<String, String>();
        par5.put("id",productId);
        par5.put("mid", LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getUserId());

        HttpConnect.post(this, "products_info", par5, new Callback() {


            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body()
                        .string().trim());

                if (data.get("status").equals("success")) {
                    JSONArray dataArr = data.getJSONArray("data");

                        JSONObject jsonObjNews = dataArr.optJSONObject(0);
                        if(jsonObjNews!=null)
                        {
                            String id = jsonObjNews.optString("id");
                            String code = jsonObjNews.optString("code");
                            String name = jsonObjNews.optString("name");
                            String pic = jsonObjNews.optString("pic");
                            String unitprice = jsonObjNews.optString("unitprice");
                            String unitctc = jsonObjNews.optString("unitctc");
                            String description = jsonObjNews.optString("description");
                            String qty = jsonObjNews.optString("qty");
                            String qtyallow = jsonObjNews.optString("qtyallow");
                            String qtysell = jsonObjNews.optString("qtysell");
                            String brandname = jsonObjNews.optString("brandname");
                            String brandpic = jsonObjNews.optString("brandpic");
                            String content = jsonObjNews.optString("content");
                            String datebegin = jsonObjNews.optString("datebegin");
                            String qtyme = jsonObjNews.optString("qtyme");
                            goods = new Goods(id, code, name, pic, unitprice, unitctc, description, qty, qtyallow, qtysell, brandname, brandpic);
                            goods.setContent(content);
                            goods.setQtyme(qtyme);
                            goods.setDatebegin(datebegin);

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    tvGoodsName.setText(goods.getName());
                                    tvGoodsName1.setText(goods.getName());
                                    tvGoodssPrice.setText("单价: "+goods.getUnitctc()+" 金豆");
                                    int qty = Integer.parseInt(goods.getQty());
                                    int qtySell = Integer.parseInt(goods.getQtysell());
                                    int qty1 =  qty - qtySell;
                                    tvGoodsCount.setText( qty1+"件");

                                    count = qty1;

                                    countAllow = Integer.parseInt(goods.getQtyallow()) - Integer.parseInt(goods.getQtyme());
                                    webView.loadUrl(goods.getContent());

                                }
                            });
                        }

                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {


            }
        });
    }


    private void getPic()
    {
        Map<String, String> par = new HashMap<String, String>();
        par.put("id",productId);
        HttpConnect.post(this, "products_pic_list", par, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body()
                        .string());

                datas.clear();

                if (data.get("status").equals("success")) {
                    JSONArray dataArr = data.getJSONArray("data");
                    for(int i = 0 ; i < dataArr.size() ; i ++)
                    {
                        JSONObject jsonObjNews = dataArr.optJSONObject(i);
                        if(jsonObjNews!=null)
                        {
                            String picpath = jsonObjNews.getString("pic");
                            Product product = new Product();
                            product.setPicpath(picpath);
                            datas.add(product);
                        }
                    }
                }
                if(datas!=null&&datas.size()>0)
                {
                    imageUrl.clear();

                    for(int i = 0 ; i < datas.size() ; i ++)
                    {
                        imageUrl.add(datas.get(i).getPicpath());
                    }
                }
                Handler dataHandler = new Handler(
                        getContext().getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
                        if(imageUrl!=null&&imageUrl.size()>0)
                        {
                            //简单使用
                            if(imageUrl!=null)
                                try{
                                    banner.setImages(imageUrl)
                                            .setImageLoader(new GlideImageLoader(null))
                                            .setOnBannerClickListener(new OnBannerClickListener() {
                                                @Override
                                                public void OnBannerClick(int position) {
                                                    Intent intent = new Intent(GoodsDetialsActivity.this, ImagePagerActivity.class);
                                                    // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
                                                    //ArrayList<String>
                                                    intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, imageUrl);
                                                    intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                                                    startActivity(intent);
                                                }
                                            });
                                    banner.start();
                                }catch(Exception e){
                                    banner.setImages(imageUrl)
                                            .setImageLoader(new GlideImageLoader(null))
                                            .setOnBannerClickListener(new OnBannerClickListener() {
                                                @Override
                                                public void OnBannerClick(int position) {
                                                    Intent intent = new Intent(GoodsDetialsActivity.this, ImagePagerActivity.class);
                                                    // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
                                                    //ArrayList<String>
                                                    intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, imageUrl);
                                                    intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                                                    startActivity(intent);
                                                }
                                            });
                                    banner.start();
                                }


                        }
                    }
                };
                dataHandler.sendEmptyMessage(0);
                // mZProgressHUD.cancel();
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
//                sToast("链接超时！");
                // mZProgressHUD.cancel();
            }
        });
    }



    //如果你需要考虑更好的体验，可以这么操作
    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }


    /**
     * 配置ImageLoder
     */
    private void configImageLoader() {
        // 初始化ImageLoader
        @SuppressWarnings("deprecation")
        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.mipmap.icon_stub) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.icon_empty) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.icon_error) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(options)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }


    public void back(View view){
        finish();
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
                if(!TextUtils.isEmpty(renZhengType))
                {
                    if(renZhengType.equals("pwd"))
                    {
                        startActivity(new Intent(GoodsDetialsActivity.this,SettingPayPwdActivity.class));
                        window.dismiss();
                        finish();
                    }else
                    {
                        startActivity(new Intent(GoodsDetialsActivity.this,RealNameAuthenticationActivity.class));
                        window.dismiss();
                        finish();
                    }
                }

            }
        });

        if(!window.isShowing())
        {
            // 设置背景颜色变暗
            WindowManager.LayoutParams lp5 = getWindow().getAttributes();
            lp5.alpha = 0.5f;
            getWindow().setAttributes(lp5);
            window.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams lp3 = getWindow().getAttributes();
                    lp3.alpha = 1f;
                    getWindow().setAttributes(lp3);
                }
            });

            window.setOutsideTouchable(true);

            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0x00ffffff);
            window.setBackgroundDrawable(dw);

            // 设置popWindow的显示和消失动画
            window.setAnimationStyle(R.style.Mypopwindow_anim_style);
            // 在底部显示
            window.showAtLocation(findViewById(R.id.tv_goods_name),
                    Gravity.BOTTOM, 0, 0);
        }
    }
}
