package com.OsMoDroid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.PathOverlay;

import android.util.Log;

public class Device implements Serializable{
	public int u;
	public String name;
	public String app;
	public String last;
	public String url;
	public String where;
	public float lat;
	public float lon;
	public String online;
	public String state;
	public String uid;
	public String speed="";
	public String color="AAAAAA";
	public String ch;
	public List<IGeoPoint> devicePath= new ArrayList<IGeoPoint>();
	public PathOverlay p;
	public Device(){
		
	}
	
	public Device( String u,
	 String name,
	 String app,
	 String last,
	 String url,
	 String where,
	 String lat,
	 String lon,
	 String online,
	 String state,
	 String uid
	 
	 ){
		Log.d(getClass().getSimpleName(), "u="+u+" "+"name="+name+"app="+app+" "+"last="
	 +last+" "+"url="+url+" "+"where="+where+" "+"lat="+lat+" "+"lon="+lon+" "+"online="+state+" "+"uid="+uid+" ");
		 this.u=Integer.parseInt(u);
		 this.name=name;
		  this.app=app;
		  this.last=last;
		  this.url=url;
		  this.where=where;
		   try {
			if (lat!=null){ this.lat=  Float.parseFloat(lat);}
			   if (lon!=null){ this.lon=  Float.parseFloat(lon);}
		} catch (NumberFormatException e) {
			Log.d(getClass().getSimpleName(),e+" "+ e.getMessage());
			e.printStackTrace();
		}
		  this.online=online;
		  this.state=state;
		  this.uid=uid;
		 
		}
	
	public Device( String u,
			 String name,
			 String app,
			 String last,
			 String url,
			 String where,
			 String lat,
			 String lon,
			 String online,
			 String state,
			 String uid,
			 String color,
			 String ch){
				Log.d(getClass().getSimpleName(), "u="+u+" "+"name="+name+"app="+app+" "+"last="
			 +last+" "+"url="+url+" "+"where="+where+" "+"lat="+lat+" "+"lon="+lon+" "+"online="+state+" "+"uid="+uid+" ");
				 this.u=Integer.parseInt(u);
				 this.name=name;
				  this.app=app;
				  this.last=last;
				  this.url=url;
				  this.where=where;
				   try {
					if (lat!=null){ this.lat=  Float.parseFloat(lat);}
					   if (lon!=null){ this.lon=  Float.parseFloat(lon);}
				} catch (NumberFormatException e) {
					Log.d(getClass().getSimpleName(),e+" "+ e.getMessage());
					e.printStackTrace();
				}
				  this.online=online;
				  this.state=state;
				  this.uid=uid;
				  if (!color.equals("")){this.color=color;}
				}
	
	

	public Device(String u, String name, String online, String uid) {
		 this.u=Integer.parseInt(u);
		 this.name=name;
		 this.uid=uid;
		 this.online=online;
	}

	@Override
	public String toString() {
		
		
		return "Device:u="+u+",name="+name+",app="+app+",last="+last+",url="+url+",where="+where+",lat="+lat+",lon="+lon
				+",online="+online+",state="+state+",uid="+uid + ",speed="+speed+" color="+color;
	}

	

}
