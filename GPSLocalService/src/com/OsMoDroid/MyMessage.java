package com.OsMoDroid;

import org.json.JSONException;
import org.json.JSONObject;

public class MyMessage {
	//"data": "{\"u\":33424,\"from\":\"173\",\"from_app\":0,\"for\":\"173\",\"for_app\":184,\"trig\":\"173-173\",\"trig_app\":\"0-184\",\
	//"text\":\"hjutrrr\",\"time\":\"2013-03-21 21:40:51\",\"readed\":\"0000-00-00 00:00:00\",\"from_name\":\"tox\",\"from_addr\":\"tox\"}"
	
	//{"u":"33369","from":"173","from_app":"0","for":"173","for_app":"184"
	//,"trig":"173-173","trig_app":"0-184","text":"32132132","time":"2013-03-14 21:16:51","readed":"2013-03-15 00:00:04"}
	public String u;
	public String from;
	public String from_app;
	public String for_user;
	public String for_app;
	public String trig;
	public String trig_app;
	public String text;
	public String time;
	public String readed;
	public String from_name;
	public String from_addr;
	
	public MyMessage (JSONObject jo){
		try {
			this.u=jo.getString("u");
			this.from=jo.getString("from");
			this.from_app=jo.getString("from_app");
			this.for_user=jo.getString("for");
			this.for_app=jo.getString("for_app");
			this.trig=jo.getString("trig");
			this.trig_app=jo.getString("trig_app");
			this.text=jo.getString("text");
			this.time=jo.getString("time");
			this.readed=jo.getString("readed");
			this.from_name=jo.getString("from_name");
			this.from_addr=jo.getString("from_addr");
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		if (from_name==null){
			for (Device dev : LocalService.deviceList){
				if (!dev.app.equals("0")&& dev.app.equals(from_app)){
					from_name=dev.name;
				}
			}
		}
		
	}
	

}
