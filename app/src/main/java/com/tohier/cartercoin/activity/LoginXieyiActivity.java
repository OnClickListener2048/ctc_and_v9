package com.tohier.cartercoin.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;

import com.tohier.cartercoin.R;

public class LoginXieyiActivity extends MyBackBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login_xieyi);
//
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

		RelativeLayout titleBack = (RelativeLayout) findViewById(R.id.title_back);
		titleBack.setOnClickListener(new TitleBack());
	}

	class TitleBack implements OnClickListener {
		@Override
		public void onClick(View v) {
			finish();
		}
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}
}
