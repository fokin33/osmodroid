package com.OsMoDroid;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class WarnActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.warnactivity);
		 String info = getIntent().getStringExtra("info");
		 TextView txv = (TextView) findViewById(R.id.infotextView);
		 txv.setText(info);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	

}
