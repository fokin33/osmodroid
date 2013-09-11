package com.OsMoDroid;

import java.text.SimpleDateFormat;

import android.util.Log;

public class TrackFile {
	final private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	public String fileName;
	public String fileDate;
	public String fileSize;
	
	public TrackFile(String fileName, long date, long size){
		this.fileName=fileName;
		this.fileDate=sdf1.format(date);
		this.fileSize=Long.toString(size/1024)+" Kb";
		
	}

}
