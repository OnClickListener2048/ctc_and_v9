package util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.tohier.cartercoin.config.DateDistance;

public class MiaoShaHandler extends Handler{

	private TextView daojishi;
	private Thread tr;
	private String time;

	public MiaoShaHandler(TextView daojishi) {
		super();
		this.daojishi = daojishi;
	}
	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}



	public Thread getTr() {
		return tr;
	}

	public void setTr(Thread tr) {
		this.tr = tr;
	}

	//2016/9/21 0:00:00
	//格式：1990-01-01 12:00:00 
	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		

		try {
			if(this.time!=null)
			{

				String riqi = DateDistance.getDistanceTime(time,daojishi);
				Log.i("thread","thread:"+riqi);
				daojishi.setText(riqi);
			}
		} catch (Exception e) {
		}
		try {

			if(Long.parseLong(time)>0){
				time = Long.parseLong(time)-1000+"";
				postDelayed(tr, 1000);
            }else{
				removeCallbacks(tr);
			}
		} catch (Exception e) {
		}


	}
}

