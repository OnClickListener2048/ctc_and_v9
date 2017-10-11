package util;

import android.os.Handler;

public class MiaoShaThread extends Thread{

	private Handler handler;
	
	public MiaoShaThread(Handler handler) {
		super();
		this.handler = handler;
	}


	@Override
	public void run() {
		super.run();
		handler.sendEmptyMessage(0);
	}
	
}
