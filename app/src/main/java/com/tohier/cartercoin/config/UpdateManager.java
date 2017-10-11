package com.tohier.cartercoin.config;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.tohier.android.config.IContext;
import com.tohier.android.util.FileUtils;
import java.io.File;
import java.util.HashMap;

/**
 * app升级管理器
 * @author Camile
 *
 */

public class UpdateManager {

	HashMap<String, String> mHashMap = new HashMap<String, String>();

	private String mSavePath;
    private int progress;
	private DownAPKAsyncTask mDownAPKAsyncTask;
    private AlertDialog dialog;
	
//	private boolean cancelUpdate = false;

	public UpdateManager(String url, String name) {
		// TODO Auto-generated constructor stub
    	mHashMap.put("url", url);
    	mHashMap.put("name", name);
	}


	public AlertDialog getDialog() {
		return dialog;
	}

	public void setDialog(AlertDialog dialog) {
		this.dialog = dialog;
	}

	/**
	 * 得到当前的版本号 
	 * @param context
	 * @return
	 */
	public int getVersionCode(Context context)
	{
		int versionCode = 0;
		try
		{
			// 通过AndroidManifest.xml得到android:versionCode
			versionCode = context.getPackageManager().getPackageInfo("com.tohier.cartercoin", 0).versionCode;
		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * 判断是否存在SD卡，存在的话下载新版本
	 * @param context
	 * @return
	 */
	public boolean downloadApk(Context context , TextView tv_pro, ProgressBar bar)
	{
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			 mDownAPKAsyncTask = new DownAPKAsyncTask(context,tv_pro,bar);
			mDownAPKAsyncTask.execute(100);
		}else{
			Toast.makeText(context, "SD卡不存在，安装失败", Toast.LENGTH_SHORT).show();
		}
		return true;
	
	}

	/**
	 * 安装新版本的
	 */
	
	private void installApk(IContext context)
	{
		
		//需要一个name
		File apkfile = new File(FileUtils.SDPATH, mHashMap.get("name"));
		if (!apkfile.exists())
		{
			return;
		}
	
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		try {
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.getContext().startActivity(i);
		} catch (Exception e) {
			// TODO: handle exception
			Log.i("ts", e.getMessage());
		}
	}
	
	
	public class DownAPKAsyncTask extends AsyncTask<Integer, Integer, String> {

		private IContext context;
		private TextView tv_pro;
		private ProgressBar bar;
		public DownAPKAsyncTask(Context context , TextView tv_pro, ProgressBar bar) {
			// TODO Auto-generated constructor stub
			this.context = (IContext) context;
			this.tv_pro = tv_pro;
			this.bar = bar;
		}
		@SuppressWarnings("deprecation")
		protected String doInBackground(Integer... params) {
//			// TODO Auto-generated method stub
//			NetworkConnection.getNetworkConnection(context).postValues(
//					"DownLoadTag", mHashMap.get("url")+"/"+mHashMap.get("name"), null, new Callback() {
//
//				@Override
//				public void onResponse(Response arg0) throws IOException {
//					// TODO Auto-generated method stub
//
////					InputStream is = arg0.body().bytes();
//					String fileName = mHashMap.get("name");
//					File f = new File(FileUtils.SDPATH + "download");
//					if (!FileUtils.isFileExist("")) {
//						FileUtils.createSDDir("");
//					}
//					long len = arg0.body().contentLength();
//					InputStream is = arg0.body().byteStream();
//					FileOutputStream fos = new FileOutputStream(FileUtils.SDPATH+fileName);
//					int count = 0;
//					// 缓存
//					byte buf[] = new byte[20480*8];
//					int numread = 0;
//					while(InterfaceMainActivity.isCancel == false){
//						numread = is.read(buf);
//						count += numread;
//						// 计算进度条位置
//						progress = (int) (((float) count / len) * 100);
//						publishProgress(progress);
//						Log.i("progress","当前进度："+progress);
//						if (numread ==-1)
//						{
//							// 下载完成
//							Log.i("progress","下载完成");
//							break;
//						}
//						// 写入文件
//						fos.write(buf, 0, numread);
//						fos.flush();
//
//					}
//					is.close();
//					fos.close();
//
//				}
//				//联网错误失败
//				@Override
//				public void onFailure(Request arg0, IOException arg1) {
//					// TODO Auto-generated method stub
//				}
//			});
			return null;
		}
		
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			tv_pro.setText(values[0]+"%");
			bar.setProgress(values[0]);
			if(values[0] == 100){
				dialog.dismiss();
				installApk(context);
			}
			super.onProgressUpdate(values);
		}
		@Override
		protected void onPostExecute(String result) {//异步处理完成之后调用
			// TODO Auto-generated method stub
		}
	}


	public void cencelUpdate(){
	if (mDownAPKAsyncTask!=null)
		mDownAPKAsyncTask.cancel(true);

	}
}


