package com.tohier.cartercoin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.adapter.ImageAdapter;
import com.tohier.cartercoin.bean.MyActivity;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.Tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ListActivity extends MyBackBaseActivity{



    private ListView listView;
    private ArrayList<MyActivity> list = new ArrayList<MyActivity>();
    private ImageAdapter adapter;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        init();
        setUp();

    }

    private void setUp() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                getUrl(list.get(position).getId(), position);



            }
        });
    }

    private void init() {
        listView = (ListView) findViewById(R.id.lv_activity);
        adapter = new ImageAdapter(this,list);
        listView.setAdapter(adapter);
        getActivity();
    }



    private void getActivity(){

        HttpConnect.post(this, "member_activity_min_options_list", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){
                    JSONArray array = data.optJSONArray("data");
                    MyActivity myActivity = null;
                    for (int i = 0 ; i < array.size() ; i++ ){
                     myActivity = new MyActivity();
                        JSONObject obj = array.optJSONObject(i);
                        myActivity.setType(obj.optString("type"));
                        myActivity.setId(obj.optString("id"));
                        myActivity.setImageUrl(obj.optString("pic"));
                        myActivity.setStartDate(obj.optString("startdate"));
                        myActivity.setEndDate(obj.optString("enddate"));
                        myActivity.setCode(obj.optString("code"));
                        myActivity.setCode1(obj.optString("selfcode"));
                        myActivity.setRanking(obj.optString("ranking"
                        ));
                        myActivity.setTitle(obj.optString("title"));
                        list.add(myActivity);
                    }

                }else{

                }


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });

            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {

            }
        });
    }


    public void getUrl(String id , final int position){
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("id",id);
        HttpConnect.post(this, "member_activity_url", map, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){
                    JSONArray array = data.optJSONArray("data");
                    JSONObject obj = array.optJSONObject(0);
                    url = obj.optString("url");

                    ListActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            String date = list.get(position).getStartDate();
                            String ranking = list.get(position).getRanking();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//设置日期格式
                            String date1 = df.format(new Date());

                            try {
                                if (Long.parseLong(Tools.dateToStamp(date1))>=Long.parseLong(Tools.dateToStamp(date)) && ranking.equals("1")){

                                    if (list.get(position).getType().equals("1")){
                                        startActivity(new Intent(ListActivity.this,ContentActivity.class)
                                                .putExtra("code",list.get(position).getCode())
                                                .putExtra("code1",list.get(position).getCode1())
                                                .putExtra("title",list.get(position).getTitle())
                                                .putExtra("url",url));
                                    }else if (list.get(position).getType().equals("0")){
                                        startActivity(new Intent(ListActivity.this,Content1Activity.class)
                                                .putExtra("code",list.get(position).getCode())
                                                .putExtra("code1",list.get(position).getCode1())
                                                .putExtra("title",list.get(position).getTitle())
                                                .putExtra("url",url));
                                    }


                                }else{
                                    startActivity(new Intent(ListActivity.this,IntroduceActivity.class)
                                            .putExtra("type",list.get(position).getType())
                                            .putExtra("url",url)
                                            .putExtra("code",list.get(position).getCode())
                                            .putExtra("code1",list.get(position).getCode1())
                                            .putExtra("title",list.get(position).getTitle())
                                            .putExtra("ranking",ranking)
                                            .putExtra("date",date));
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                }else{
                }
            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {

            }
        });
    }

    public void back(View view){
        finish();
    }

}
