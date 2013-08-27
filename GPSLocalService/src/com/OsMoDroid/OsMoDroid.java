package com.OsMoDroid;

import com.dropbox.sync.android.DbxAccountManager;

import android.app.Application;

public class OsMoDroid extends Application {
	String consumerKey = this.getResources().getText(R.string.dropbox_consumer_key).toString();
	String consumerSecret = this.getText(R.string.dropbox_consumer_secret).toString();
	public static DbxAccountManager mDbxAcctMgr;
	public static boolean mesactivityVisible=false;
	public static boolean gpslocalserviceclientVisible=false;
	private static int notifyid=1;
	  @Override
	public void onCreate() {
		
		
		mDbxAcctMgr= DbxAccountManager.getInstance(getApplicationContext(), consumerKey, consumerSecret);
		super.onCreate();
	}

	final public static int warnnotifyid=3;
	  final public static int mesnotifyid=2;
	  
	  public static int notifyidApp (){
		  
		  return notifyid++;
	  }
	  
  
	  
	

}
