package com.OsMoDroid;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class WarnActivity extends Activity {

	@Override
    public void onBackPressed() {
		Intent i = new Intent(this, GPSLocalServiceClient.class);
		i.setAction(Intent.ACTION_MAIN);
		i.addCategory(Intent.CATEGORY_LAUNCHER);
	    startActivity(i);
    super.onBackPressed();
    }
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		 
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		 Log.d("WarnActivity", "OnDestroy");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		 Log.d("WarnActivity", "OnPause");
		 finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(this);
		setContentView(R.layout.warnactivity);
		Log.d("WarnActivity", getIntent().getStringExtra("info"));
		 TextView txv = (TextView) findViewById(R.id.infotextView);
		 txv.setText(getIntent().getStringExtra("info"));
		 Button support = (Button) findViewById(R.id.supportbutton);
		support.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						 
						Intent sendIntent = new Intent(Intent.ACTION_SEND);
						
						 sendIntent.setType("text/plain");
							sendIntent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { "support@esya.ru" } );
							sendIntent.putExtra(android.content.Intent.EXTRA_TEXT, getIntent().getStringExtra("info") + " ����������:"+settings.getString("device", "")+ " hash:"+settings.getString("hash", ""));
							sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Error");
							startActivity(Intent.createChooser(sendIntent, "Email"));
					}
				});
		Log.d("WarnActivity", "OnResume");
	}

	

}
