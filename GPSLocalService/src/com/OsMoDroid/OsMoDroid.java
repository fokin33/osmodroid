package com.OsMoDroid;

import android.app.Application;

public class OsMoDroid extends Application {

	private static final int MIN_UPLOAD_ID = 4;
	private static final int MAX_UPLOAD_ID = 1000;
	public static boolean mesactivityVisible = false;
	public static boolean gpslocalserviceclientVisible = false;
	private static int notifyid = 1;
	final public static int warnnotifyid = 3;
	final public static int mesnotifyid = 2;
	static int uploadnotifyid = MIN_UPLOAD_ID;

	public static int notifyidApp() {

		return notifyid++;
	}

	public static int uploadnotifyid() {

		if (uploadnotifyid < MAX_UPLOAD_ID) {
			return uploadnotifyid++;
		} else {
			return MIN_UPLOAD_ID;
		}

	}

}
