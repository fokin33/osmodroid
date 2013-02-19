package com.OsMoDroid;

import java.util.ArrayList;
import java.util.List;

public class Channel {
	
	public String name;
	public String u;
	public String added;
	public List<Device> deviceList= new ArrayList<Device>();
	
	public Channel(){
		
	}
	
	public Channel( 
	 String name,
	 String u,
	 String added){
		 this.u=u;
		 this.name=name;
		 this.added=added;
		}

	@Override
	public String toString() {
		
		
		return "Channel:u="+u+",name="+name+",added="+added;
	}

	

}
