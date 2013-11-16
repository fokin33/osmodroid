package com.OsMoDroid;


import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Address;
import android.util.Log;

public class Channel {
	public ArrayList<File> gpxList = new ArrayList<File>();
	public String name;
	public int u;
	public String created;
	public String ch;
	public String url;
	public List<Device> deviceList= new ArrayList<Device>();
	public List<String> messagesstringList= new ArrayList<String>();
	public Boolean connected=false;
	public Boolean send=false;
	LocalService localService;
	ResultsListener gpxdownloadListener = new ResultsListener() {
		
		@Override
		public void onResultsSucceeded(APIComResult result) {
			Log.d(getClass().getSimpleName(),"download result="+result.savename);
			addtrack(result.savename);
			localService.informRemoteClientChannelsListUpdate();
					}
	};
	public Channel(){
		
	}
	
	public void addtrack(File file){
		if (!gpxList.contains(file)){
			gpxList.add(file);
			Log.d(getClass().getSimpleName(),"add download result to gpxlist="+gpxList.size());
		}
	}
	
//	public Channel( 
//	 String name,
//	 String u,
//	 String created){
//		 this.u=Integer.parseInt(u);
//		 this.name=name;
//		 this.created=created;
//		}
	//{"uid":"173","lon":"30","count":"0","zoom":"14","track":"0","code":"zxc","url":"ByIWINB22Fj74452is","main":"0","private":"0","u":"51","created":"2013-02-20 12:54:18","color":"0","name":"zxc","me":{"channel_name":"zxc","uid":"173","icon":"1","u":"19","channel_url":"ByIWINB22Fj74452is","added":"2013-02-24 13:40:27","color":"FFFFFF","name":"TEST2","device":"1436","active":"1","role":"0","channel":"51"},"ch":"omc_RAzHkQ9JzY5","user":[{"uid":"173","icon":"1","lon":"41.332442","u":"1436","color":"FFFFFF","name":"TEST2","state":"0","active":"1","role":"0","lat":"54.95365","online":"1"},{"uid":"173","icon":"1","lon":"37.685934","u":"40","color":"FFFFFF","name":"Денис","state":"0","active":"1","role":"0","lat":"55.682043","online":"1"}],"lat":"60","zone":"0"},{"uid":"173","lon":"30","count":"0","zoom":"14","track":"0","code":"123","url":"w2GjFV1BcviZz6qo83","main":"0","private":"0","u":"54","created":"2013-02-25 14:46:35","color":"0","name":"Каналья","me":{"channel_name":"Каналья","uid":"173","icon":"1","u":"21","channel_url":"w2GjFV1BcviZz6qo83","added":"2013-02-25 18:48:42","color":"FFFFFF","name":"TEST2","device":"1436","active":"1","role":"0","channel":"54"},"ch":"omc_qg9q1W06aB6","user":[{"uid":"173","icon":"1","lon":"41.332442","u":"1436","color":"FFFFFF","name":"TEST2","state":"0","active":"1","role":"0","lat":"54.95365","online":"1"}],"lat":"60","zone":"0"}]}
	public Channel(JSONObject jo, LocalService localService)
	{
		this.localService=localService;
		this.name=jo.optString("name");
		this.u=Integer.parseInt(jo.optString("u"));
		this.created=jo.optString("created");
		this.ch=jo.optString("ch");
		this.url="http://esya.ru/om/"+jo.optString("url");
		
		if (jo.optJSONObject("me")!=null&&jo.optJSONObject("me").optString("active").equals("1")){
	 		this.send=true;
	 	} else
	 	{
	 		this.send=false;
	 	}
		
	
		 JSONArray a =jo.optJSONArray("user");
		for (int i = 0; i < a.length(); i++) {
	 			JSONObject jsonObject;
				try {
					jsonObject = a.getJSONObject(i);
				
//	 			 u = 1163
//	 				    uid = 192
//	 				    lat = 54.907503
//	 				    lon = 41.271125
//	 				    state = 0
//	 				    online = 0
//	 				    name = toxIC jiayu
//	 				    icon = 1
		 			
	if (!jsonObject.getString("u").equals(LocalService.settings.getString("device", ""))){
		this.deviceList.add(new Device(jsonObject.getString("u")
				, jsonObject.getString("name"),""
				,"",
				"",
				"",
				jsonObject.getString("lat"),
				jsonObject.getString("lon"),
				jsonObject.getString("online"),
				jsonObject.getString("state"), "", jsonObject.optString("color")
				) );}
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}   
		

	//channelList.add(chanitem);
//	netutil.newapicommand((ResultsListener)serContext, "om_channel_user:"+chanitem.u);


	 		 }
		
		//connect();
				}
	
	@Override
	public boolean equals(Object o) {
		
		 if((o instanceof Channel) && this.u == ((Channel)o).u )  
	        {    
	            return true;    
	        }  
	        else  
	        {  
	            return false;  
	        }  
	}

	public void connect(){
		Log.d(getClass().getSimpleName(),"Channel connecting");	
	//	chanIM= new IM("om_"+this.ch+",om_"+this.ch+"_chat", LocalService.serContext, 2);
		ArrayList<String[]> longPollchannels =new ArrayList<String[]>();
		longPollchannels.add(new String[] {this.ch,"ch",""});
		longPollchannels.add(new String[] {this.ch+"_chat","chch",""});
		
		if (LocalService.myIM!=null){
			LocalService.myIM.removechannels(longPollchannels);
			LocalService.myIM.addchannels(longPollchannels);	
	}
		connected=true;
		
	}

	public void disconnect(){
		Log.d(getClass().getSimpleName(),"Channel disconnecting");	
		//	chanIM= new IM("om_"+this.ch+",om_"+this.ch+"_chat", LocalService.serContext, 2);
			ArrayList<String[]> longPollchannels =new ArrayList<String[]>();
			longPollchannels.add(new String[] {this.ch,"ch",""});
			longPollchannels.add(new String[] {this.ch+"_chat","chch",""});
			
			if (LocalService.myIM!=null){
				LocalService.myIM.removechannels(longPollchannels);	}	
			connected=false;
	}
	
	public void downloadgpx(String url, String u){
		 File sdDir = android.os.Environment.getExternalStorageDirectory();
			 File fileName = new File (sdDir, "OsMoDroid/channelsgpx/");
			 fileName.mkdirs();
			 fileName = new File(sdDir, "OsMoDroid/channelsgpx/"+u+".gpx");
			 Log.d(getClass().getSimpleName(),"filename="+fileName);
			 if(!fileName.exists())
			 	{
				 netutil.downloadfile(gpxdownloadListener, url, fileName);
			 	}	else
			 	{
			 		addtrack(fileName);
			 	}
	}
	
	@Override
	public String toString() {
		
		
		return "Channel:u="+u+",name="+name+",added="+created;
	}

	

}
