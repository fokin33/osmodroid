package com.OsMoDroid;

public class Device {
	public String u;
	public String name;
	public String app;
	public String last;
	public String url;
	public String where;
	public String lat;
	public String lon;
	public String online;
	public String state;
	public String uid;
	
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
	 String uid){
		 this.u=u;
		 this.name=name;
		  this.app=app;
		  this.last=last;
		  this.url=url;
		  this.where=where;
		  this.lat=lat;
		  this.lon=lon;
		  this.online=online;
		  this.state=state;
		  this.uid=uid;
		}

	@Override
	public String toString() {
		
		
		return "Device:u="+u+",name="+name+",app="+app+",last="+last+",url="+url+",where="+where+",lat="+lat+",lon="+lon
				+",online="+online+",state="+state+",uid="+uid;
	}

	

}
