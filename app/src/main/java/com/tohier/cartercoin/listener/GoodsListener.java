package com.tohier.cartercoin.listener;

import android.content.Context;
import android.view.View;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.activity.MallActivity;
import com.tohier.cartercoin.adapter.GoodsAdapter;
import com.tohier.cartercoin.bean.Goods;
import com.tohier.cartercoin.columnview.PullToRefreshLayout;
import com.tohier.cartercoin.config.HttpConnect;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class GoodsListener implements PullToRefreshLayout.OnRefreshListener {

	private int page = 1;
	private Context ctx;
	private List<Goods> datas = new ArrayList<Goods>();
	private GoodsAdapter adapter;
	private String typeId = "0";
	private GifImageView gifLoading;
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public List<Goods> getDatas() {
		return datas;
	}

	public void setDatas(List<Goods> datas) {
		this.datas = datas;
	}

	public GoodsListener(Context ctx, GoodsAdapter adapter, GifImageView gifLoading) {
		super();
		this.ctx = ctx;
		this.adapter = adapter;
		this.gifLoading = gifLoading;

	}



	@Override
	public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
		
		// 下拉刷新操作
		// 千万别忘了告诉控件刷新完毕了哦！
				new Thread()
				{
					@Override
					public void run() {
						page = 1;
						datas.clear();
						getJsonDate(pullToRefreshLayout);
					}
				}.start();
	}

	@Override
	public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
		// 加载操作
		// 千万别忘了告诉控件刷新完毕了哦！
		new Thread()
		{
			@Override
			public void run() {
				page = page + 1;
				getJsonDate(pullToRefreshLayout);
			}
		}.start();
//		pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
	}

	public void getJsonDate(final PullToRefreshLayout pullToRefreshLayout) {
		((MallActivity)ctx).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				gifLoading.setVisibility(View.VISIBLE);
			}
		});
		Map<String, String> par = new HashMap<String, String>();
		par.put("page", page+"");
		par.put("kid",typeId);
		// mZProgressHUD.show();
		HttpConnect.post(((MallActivity)ctx), "products_list", par, new Callback() {


			@Override
			public void onResponse(Response arg0) throws IOException {
				final JSONObject data = JSONObject.fromObject(arg0.body()
						.string().trim());

				if (data.get("status").equals("success")) {
					  JSONArray dataArr = data.getJSONArray("data");
					   for(int i = 0 ; i < dataArr.size() ; i ++)
					   {
						   JSONObject jsonObjNews = dataArr.optJSONObject(i);
						   if(jsonObjNews!=null)
						   {
							   String id = jsonObjNews.getString("id");
							   String code = jsonObjNews.getString("code");
							   String name = jsonObjNews.getString("name");
							   String pic = jsonObjNews.getString("pic");
							   String unitprice = jsonObjNews.getString("unitprice");
							   String unitctc = jsonObjNews.getString("unitctc");
							   String description = jsonObjNews.getString("description");
							   String qty = jsonObjNews.getString("qty");
							   String qtyallow = jsonObjNews.getString("qtyallow");
							   String qtysell = jsonObjNews.getString("qtysell");
							   String brandname = jsonObjNews.getString("brandname");
							   String brandpic = jsonObjNews.getString("brandpic");
							   String datebegin = jsonObjNews.getString("datebegin");
							   String dateend = jsonObjNews.getString("dateend");
							   String sysdate = jsonObjNews.getString("sysdate");
							   Goods goods = new Goods(id, code, name, pic, unitprice, unitctc, description, qty, qtyallow, qtysell, brandname, brandpic, datebegin, dateend, sysdate);
							   datas.add(goods);
						   }
					   }


				} else {
					((MallActivity)ctx).runOnUiThread(new Runnable() {

								@Override
								public void run() {
								}
							});
				}


				((MallActivity)ctx).runOnUiThread(new Runnable() {

					@Override
					public void run() {
						adapter.setItems(datas);
						adapter.notifyDataSetChanged();

						gifLoading.setVisibility(View.GONE);
						if(pullToRefreshLayout!=null)
						{
							pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
						}
					}
				});
			}


			@Override
			public void onFailure(Request arg0, IOException arg1) {
				
				((MallActivity)ctx).runOnUiThread(new Runnable() {

					@Override
					public void run() {
						
						if(pullToRefreshLayout!=null)
						{
							pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
						}
					}
				});
				
			}
		});
	}

}
