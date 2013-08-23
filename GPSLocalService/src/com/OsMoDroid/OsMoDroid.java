package com.OsMoDroid;

import android.app.Application;

public class OsMoDroid extends Application {
	
	
	  public static boolean mesactivityVisible=false;
	  public static boolean gpslocalserviceclientVisible=false;
	  private static int notifyid=1;
	  final public static int warnnotifyid=3;
	  final public static int mesnotifyid=2;
	  
	  public static int notifyidApp (){
		  
		  return notifyid++;
	  }
	  
  
	  
	

}
