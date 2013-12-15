package com.OsMoDroid;

import java.io.File;

import android.graphics.Color;

public class ColoredGPX {
	public ColoredGPX(File fileName, String scolor) {
		gpxfile=fileName;
		try {
			color= Color.parseColor("#" + scolor);	
		} catch (Exception e) {
			color=Color.MAGENTA;
		}
		
		
	}
	File gpxfile;
	int color;
	@Override
	public boolean equals(Object o) {
		 if((o instanceof ColoredGPX) && this.gpxfile.getName().equals(((ColoredGPX)o).gpxfile.getName() ))  
	        {    
	            return true;    
	        }  
	        else  
	        {  
	            return false;  
	        }  
	}
}
