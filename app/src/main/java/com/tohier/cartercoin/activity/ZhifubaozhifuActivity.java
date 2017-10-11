package com.tohier.cartercoin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.ClipboardManager;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.conversation.EServiceContact;
import com.bumptech.glide.Glide;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.MyApplication;
import com.tohier.cartercoin.config.RGBLuminanceSource;

import net.sf.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

public class ZhifubaozhifuActivity extends MyBackBaseActivity {

	private TextView tv1;
	private TextView tvKefu;
	private ImageView ivSave,ivSao,ivKefu;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_zhifubaozhifu);
		{
			tv1 = (TextView) findViewById(R.id.tv_copy);
			ivSave = (ImageView) findViewById(R.id.iv_save);
			ivSao = (ImageView) findViewById(R.id.iv_sao);
		}

//		SliderConfig mConfig = new SliderConfig.Builder()
//				.primaryColor(Color.TRANSPARENT)
//				.secondaryColor(Color.TRANSPARENT)
//				.position(SliderPosition.LEFT)
//				.edge(false)
//				.build();
//
//		ISlider iSlider = SliderUtils.attachActivity(this, mConfig);
//		mConfig.setPosition(SliderPosition.LEFT);
//		iSlider.setConfig(mConfig);


		ImageView titleBack = (ImageView) findViewById(R.id.title_back);
		titleBack.setOnClickListener(new TitleBack());
		final ImageView iv = (ImageView) findViewById(R.id.zhifubao_erweima);

		TextView tv = (TextView) findViewById(R.id.zhifu_fukuanfang);

		tvKefu = (TextView) findViewById(R.id.tv_kefu);
		ivKefu = (ImageView) findViewById(R.id.iv_kefu);

		HttpConnect.post(this, "company_account", null, new Callback() {
			@Override
			public void onResponse(Response arg0) throws IOException {
				JSONObject data = JSONObject.fromObject(arg0.body().string());
				if (data.get("status").equals("success")) {
					final String url = data.optJSONArray("data").optJSONObject(0).optString("alipaypic");
					final String number = data.optJSONArray("data").optJSONObject(0).optString("alipayaccount");
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Glide.with(ZhifubaozhifuActivity.this).load(url).error(null).into(iv);
							tv1.setText("收款方："+number);
						}
					});
				}
			}
			@Override
			public void onFailure(Request arg0, IOException arg1) {

			}
		});

		tv.setText("付款方：" + LoginUser.getInstantiation(this).getLoginUser().getNickName());

		// Handler myHandler = new Handler() {
		// public void handleMessage(Message msg) {
		// mZProgressHUD.show();
		// getFinalImage();
		// mZProgressHUD.cancel();
		// }
		// };
		// myHandler.sendEmptyMessage(0);
		
		tv1.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				 ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
				 // 将文本内容放到系统剪贴板里。
			 	StringBuffer sb = new StringBuffer(tv1.getText().toString());
			 	String str = sb.substring(4, tv1.getText().toString().length());
		        cm.setText(str);
		        sToast("已复制");
				return true;
			}
		});

		ivKefu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				EServiceContact contact = new EServiceContact(MyApplication.KEFU, 0);
				Intent intent1 = mIMKit.getChattingActivityIntent(contact);
				startActivity(intent1);
			}
		});

		tvKefu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				EServiceContact contact = new EServiceContact(MyApplication.KEFU, 0);
				Intent intent1 = mIMKit.getChattingActivityIntent(contact);
				startActivity(intent1);
			}
		});

		/**
		 * 将图片保存到本地
		 */
		ivSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				saveImageToGallery(getContext(),myShot(mActivity) );
				sToast("已将图片保存至相册！");
			}
		});

		/**
		 * 识别二维码
		 */
		ivSao.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Result result = parseQRcodeBitmap(myShot(mActivity));
				startActivity(new Intent(ZhifubaozhifuActivity.this,AlipayActivity.class).putExtra("url",result.getText()));
			}
		});
	}

	private class TitleBack implements OnClickListener {
		@Override
		public void onClick(View v) {
			finish();
		}
	}


	private Bitmap myShot(Activity activity) {
		// 获取windows中最顶层的view
		View view = activity.getWindow().getDecorView();
		view.buildDrawingCache();
		// 获取状态栏高度
		Rect rect = new Rect();
		view.getWindowVisibleDisplayFrame(rect);
		int statusBarHeights = rect.top;
		Display display = activity.getWindowManager().getDefaultDisplay();
		// 获取屏幕宽和高
		int widths = display.getWidth();
		int heights = display.getHeight();
		// 允许当前窗口保存缓存信息
		view.setDrawingCacheEnabled(true);
		// 去掉状态栏
		Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache(), 0,
				statusBarHeights, widths, (heights - statusBarHeights));

		// 销毁缓存信息
		view.destroyDrawingCache();
		return bmp;
	}
	
	 public static void saveImageToGallery(Context context, Bitmap bmp) {
	        // 首先保存图片
	        File appDir = new File(Environment.getExternalStorageDirectory(), "SchoolPicture");
	        if (!appDir.exists()) {
	            appDir.mkdir();
	        }
	        String fileName = System.currentTimeMillis() + ".jpg";
	        File file = new File(appDir, fileName);
	        try {
	            FileOutputStream fos = new FileOutputStream(file);
	            bmp.compress(CompressFormat.JPEG, 100, fos);
	            fos.flush();
	            fos.close();
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	 
	        // 其次把文件插入到系统图库
	        try {
	            String saveImg=  MediaStore.Images.Media.insertImage(context.getContentResolver(),
	                    file.getAbsolutePath(), fileName, null);
	            if(saveImg != null)
	            {
	                Toast toast=Toast.makeText(context,    "截图成功请到图库查看", Toast.LENGTH_LONG);
	                toast.setGravity(Gravity.CENTER, 0, 0);
	                toast.show();
	            }
	            else {
	                Toast toast=Toast.makeText(context,    "未能截取到图片信息", Toast.LENGTH_LONG);
	                toast.setGravity(Gravity.CENTER, 0, 0);
	                toast.show();
	            }
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        }
	        // 最后通知图库更新
	       
	        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,    Uri.fromFile(new File(file.getPath()))));
	    }
	
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}


	//解析二维码图片,返回结果封装在Result对象中
	private com.google.zxing.Result  parseQRcodeBitmap(Bitmap bitmap){
//		//解析转换类型UTF-8
		Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
		hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
//		//获取到待解析的图片
//		BitmapFactory.Options options = new BitmapFactory.Options();
//		//如果我们把inJustDecodeBounds设为true，那么BitmapFactory.decodeFile(String path, Options opt)
//		//并不会真的返回一个Bitmap给你，它仅仅会把它的宽，高取回来给你
//		options.inJustDecodeBounds = true;
//		//此时的bitmap是null，这段代码之后，options.outWidth 和 options.outHeight就是我们想要的宽和高了
//		Bitmap bitmap = BitmapFactory.decodeFile(bitmapPath,options);
//		//我们现在想取出来的图片的边长（二维码图片是正方形的）设置为400像素
//		//以上这种做法，虽然把bitmap限定到了我们要的大小，但是并没有节约内存，如果要节约内存，我们还需要使用inSimpleSize这个属性
//		options.inSampleSize = options.outHeight / 400;
//		if(options.inSampleSize <= 0){
//			options.inSampleSize = 1; //防止其值小于或等于0
//		}
//		options.inJustDecodeBounds = false;
//		bitmap = BitmapFactory.decodeFile(bitmapPath, options);
		//新建一个RGBLuminanceSource对象，将bitmap图片传给此对象
		RGBLuminanceSource rgbLuminanceSource = new RGBLuminanceSource(bitmap);
		//将图片转换成二进制图片
		BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(rgbLuminanceSource));
		//初始化解析对象
		QRCodeReader reader = new QRCodeReader();
		//开始解析
		Result result = null;
		try {
			result = reader.decode(binaryBitmap, hints);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
}
