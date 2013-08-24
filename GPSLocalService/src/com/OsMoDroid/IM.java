
package com.OsMoDroid;import java.io.BufferedReader;import java.io.IOException;import java.io.InputStream;import java.io.InputStreamReader;import java.io.PrintWriter;import java.net.HttpURLConnection;import java.net.InetSocketAddress;import java.net.Proxy;import java.net.URL;import java.net.UnknownHostException;import java.text.SimpleDateFormat;import java.util.ArrayList;import java.util.Collections;
import java.util.Map;
import java.util.Date;import java.util.Iterator;import java.util.Map.Entry;
import org.json.JSONArray;import org.json.JSONException;import org.json.JSONObject;import com.OsMoDroid.LocalService.SendCoor;import android.app.Notification;import android.app.PendingIntent;import android.app.PendingIntent.CanceledException;import android.content.BroadcastReceiver;import android.content.SharedPreferences;
import android.content.Context;import android.content.Intent;import android.content.IntentFilter;import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.NetworkInfo;import android.os.Bundle;import android.os.Environment;
import android.os.Handler;import android.os.Message;import android.support.v4.app.NotificationCompat;import android.util.Log;import android.widget.Toast;/**
 * @author dfokin
 *Class for work with LongPolling
 */
public class IM {	protected  boolean running       = false;	protected boolean connected     = false;	protected boolean autoReconnect = true;	protected Integer timeout       = 0;	private HttpURLConnection con;	private InputStream instream;	String adr;	String mykey;//	String timestamp=Long.toString(System.currentTimeMillis());String lcursor="";	int pingTimeout=900;	Thread myThread;	private BufferedReader    in      = null;	Context parent;	String myLongPollCh;
	ArrayList<String[]>  myLongPollChList;	//ArrayList<String> list= new ArrayList<String>();	int mestype=0;	LocalService localService;	final private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		public IM(ArrayList<String[]> longPollChList, Context context,String key,LocalService localService) {
	
		this.localService=localService; 
		parent=context;
		myLongPollChList=longPollChList;
		mykey=key;
		getadres(longPollChList);
		parent.registerReceiver(bcr, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
		start();
	}

	private void getadres(ArrayList<String[]> longPollChList) {
		//подключении не вставляется
		//http://d.esya.ru/?identifier=1364306035.30188078700000:somaanjUobpong&ncrnd=1364306032050
		//http://d.esya.ru/?identifier=somaanjUobpong&ncrnd=1364306032050
		
		
		
		adr="http://d.esya.ru/?identifier="+mykey;
		for (String str[] : longPollChList){
			if  (str[2].length()>0){
				adr=adr+","+str[2]+":"+str[0];
			}
			else {
			adr=adr+","+str[0];
			}
		}
		//adr.substring(0, adr.length()-1);
		adr=adr+"&ncrnd="+Long.toString(System.currentTimeMillis());
		Log.d(this.getClass().getName(), "IM adr="+adr);
	}
	
	public void addchannels(ArrayList<String[]> longPollChList){
		
		myLongPollChList.addAll(longPollChList);
		getadres(myLongPollChList);
		disconnect();
		
		
	}
	
public void removechannels(ArrayList<String[]> longPollChList){
		
		//myLongPollChList.removeAll(longPollChList);
	for (String[] extstr: longPollChList){
		
		 for (Iterator<String[]> iter = myLongPollChList.iterator(); iter.hasNext();) {
		      String[] s = iter.next();
		      if (s.equals(extstr)) {
		        iter.remove();
		      }
		     
		    }
		
		
	}
	
	
		getadres(myLongPollChList);
		disconnect();
		
		
	}
	String getMessageType(String ids){
	for (String[] str: myLongPollChList){
		if (str[0].equals(ids)){
			return str[1];
		}
	}
	
	return ids;
	
}			public void addtoDeviceChat(String message) {int u = -1;			try {
				MyMessage mes =new MyMessage( new JSONObject(message));
				Log.d(this.getClass().getName(), "MyMessage,from "+mes.from);
if (mes.from.equals(localService.settings.getString("device", ""))){
	for (Device dev : LocalService.deviceList){
		if(Integer.toString(dev.u).equals(mes.to)){
			u=dev.u;
		}
	}
} else {
				for (Device dev : LocalService.deviceList){
					if(Integer.toString(dev.u).equals(mes.from)){
						u=dev.u;
					}
				}
}		
				
				if (LocalService.currentDevice!=null&& u ==LocalService.currentDevice.u){
					boolean contains = false;
					for (MyMessage mes1: LocalService.chatmessagelist){
						
						
						if(mes1.u==mes.u){
							 contains = true;
							
						}
						
					}
					if (!contains){
						LocalService.chatmessagelist.add(mes);
						Collections.sort(LocalService.chatmessagelist);
						
						localService.alertHandler.post(new Runnable(){
							public void run() {
								if (LocalService.chatmessagesAdapter!=null){
									LocalService.chatmessagesAdapter.notifyDataSetChanged();
								}
							}
						});
						
						
					}
				
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (u!=-1){
			Message msg = new Message();			Bundle b = new Bundle();			b.putInt("deviceU", u);			msg.setData(b);			localService.alertHandler.sendMessage(msg);		}			}	private BroadcastReceiver bcr = new BroadcastReceiver() {		@Override		public void onReceive(Context context, Intent intent) {			Log.d(this.getClass().getName(), "BCR"+this);			Log.d(this.getClass().getName(), "BCR"+this+" Intent:"+intent);			if (intent.getAction().equals(android.net.ConnectivityManager.CONNECTIVITY_ACTION)) {				Bundle extras = intent.getExtras();				Log.d(this.getClass().getName(), "BCR"+this+ " "+intent.getExtras());				if(extras.containsKey("networkInfo")) {					NetworkInfo netinfo = (NetworkInfo) extras.get("networkInfo");					Log.d(this.getClass().getName(), "BCR"+this+ " "+netinfo);					Log.d(this.getClass().getName(), "BCR"+this+ " "+netinfo.getType());					if(netinfo.isConnected()) {						Log.d(this.getClass().getName(), "BCR"+this+" Network is connected");						Log.d(this.getClass().getName(), "BCR"+this+" Running:"+running);						// Network is connected						if(!running ) {							System.out.println("Starting from Reciever"+this.toString());							myThread.interrupt();							start();						}					}					else {						System.out.println("Stoping1 from Reciever"+this.toString());						stop();					}				}				else if(extras.containsKey("noConnectivity")) {					System.out.println("Stoping2 from Reciever"+this.toString());					stop();				}		    }		}	};	 /**
	 * Выключает IM
	 */
	void close(){		parent.unregisterReceiver(bcr);		stop();	};	 void start(){		this.running = true;		System.out.println("About to notify state from start()");		System.out.println("State notifed of start()");		myThread = new Thread(new IMRunnable());		myThread.start();	}void parseEx (String toParse){

//	[
//	  {
//	    "ids": { "om_omc_RAzHkQ9JzY5_chat": "1364554177.56155052100000" },
//	    "data": "0|40|fgfg|2013-03-29 14:49:37"
//	  }
//	]
	
	
	try {
		Log.d(this.getClass().getName(), "toParse= "+ toParse);

		//Toast.makeText(LocalService.serContext, toParse, Toast.LENGTH_LONG).show();
		JSONArray result = new JSONArray(toParse);

		for (int i = 0; i < result.length(); i++) {

		    JSONObject jsonObject = result.getJSONObject(i);
		    Iterator<String> it = (new JSONObject(jsonObject.optString("ids"))).keys();

			  while (it.hasNext())
		    
		    

          	{
		    	
		    	String keyname= it.next();
		    	Log.d(this.getClass().getName(), "keyname="+keyname);
		    	lcursor = (new JSONObject(jsonObject.optString("ids"))).optString(keyname);
		    	 Log.d(this.getClass().getName(), "lcursor="+ lcursor);
		    	 for (String[] str:myLongPollChList){
		    		 if (str[0].equals(keyname)){
		    			 str[2]=lcursor;
		    		 }
		    	 }

if (getMessageType( keyname).equals("o")){
	Log.d(this.getClass().getName(), "type=o");
	String status = "";
	String messageText = "";
    String[] data = jsonObject.optString("data").split("-");
    Log.d(this.getClass().getName(), "data[0]="+data[0]+" data[1]="+data[1]+" data[2]="+data[2]);
    Log.d(this.getClass().getName(), "LocalService.DeviceList="+LocalService.deviceList.toString());
    final ArrayList<Device> tempdeviceList = new ArrayList<Device>();
    tempdeviceList.addAll(LocalService.deviceList);
    
    for (Device device : tempdeviceList){
        if (data[1].equals(Integer.toString(device.u))&&device.u!=(Integer.parseInt(LocalService.settings.getString("device", ""))) ){
        	//LocalService.deviceList.remove(device);
        	int idx=LocalService.deviceList.indexOf(device);
            if (LocalService.settings.getBoolean("statenotify", true)&&data[0].equals("state")) {
                if (data[2].equals("1")) { status="запущен";device.state = "1"; } else { status="остановлен";device.state = "0"; }
                messageText = messageText+" Мониторинг на устройстве \""+device.name+"\" "+status;
                Log.d(this.getClass().getName(), "DeviceState="+messageText);
                
            }
            if (LocalService.settings.getBoolean("onlinenotify", false)&&data[0].equals("online")&&device.u!=(Integer.parseInt(LocalService.settings.getString("device", ""))) ){
                if (data[2].equals("1")&&device.online.equals("0")) { status="вошло в сеть";  device.online = "1";}
                if (data[2].equals("0")&&device.online.equals("1")) { status="покинуло сеть"; device.online = "0"; }
               
                messageText = messageText+" Устройство \""+device.name+"\" "+status;
                Log.d(this.getClass().getName(), "DeviceOnline="+messageText);
            }
            if (data[0].equals("geozone")&&device.u!=(Integer.parseInt(LocalService.settings.getString("device", ""))) ){
                messageText = messageText+" Устройство \""+device.name+"\" "+data[2];
                Log.d(this.getClass().getName(), "DeviceGeoZone="+messageText);
            }
            
            if (LocalService.deviceAdapter!=null) { 
            //	LocalService.deviceAdapter.notifyDataSetChanged();
            	Log.d(getClass().getSimpleName(),"devicelist="+ LocalService.deviceList);
            	
            	localService.alertHandler.post(new Runnable() {

					public void run() {

						LocalService.deviceList.clear();
		            	LocalService.deviceAdapter.notifyDataSetChanged();
		            	LocalService.deviceList.addAll(tempdeviceList);
		            	LocalService.deviceAdapter.notifyDataSetChanged();}
				});
            	
            	if (!messageText.equals("")){
            	Message msg = new Message();

    			Bundle b = new Bundle();

    			b.putBoolean("om_online", true);
    			b.putString("MessageText", sdf1.format(new Date())+" "+messageText);

    			msg.setData(b);

    			localService.alertHandler.sendMessage(msg);
    			Log.d(this.getClass().getName(), "Sended message:"+messageText);
            	}
            	//lv1.setAdapter(LocalService.deviceAdapter);
            	} else { Log.d(this.getClass().getName(), "deviceadapter is null");}
        }
    }
	
}



if (getMessageType( keyname).equals("m")){
	
	Log.d(this.getClass().getName(), "type=m");
	
	  String messageJSONText = jsonObject.optString("data");
	  
	  if (!messageJSONText.equals("")){

			addtoDeviceChat(messageJSONText);

			}
}

if (getMessageType( keyname).equals("r")){
	Log.d(this.getClass().getName(), "type=r");
	if (jsonObject.optString("data").equals("start")){
        if (!localService.state){
        	localService.alertHandler.post(new Runnable() {

				public void run() {

        	localService.startServiceWork();
            try {
                localService.Pong(localService);
            } catch (JSONException e) {
                e.printStackTrace();
            }}});
        }
}

if (jsonObject.optString("data").equals("stop")){
        if (localService.state){
        	localService.alertHandler.post(new Runnable() {

				public void run() {

            localService.stopServiceWork(true);
            try {
                localService.Pong(localService);
            } catch (JSONException e) {
                e.printStackTrace();
            }}});
        }
}

if (jsonObject.optString("data").equals("ping")){
        try {
            localService.Pong(localService);
        } catch (JSONException e) {
            e.printStackTrace();
        }
}

if (jsonObject.optString("data").length()>7&&jsonObject.optString("data").substring(0, 7).equals("routeto")){
	int pluspos=jsonObject.optString("data").lastIndexOf("_");
	localService.informRemoteClientRouteTo(Float.parseFloat(jsonObject.optString("data").substring(8, pluspos)), Float.parseFloat(jsonObject.optString("data").substring(pluspos+1, jsonObject.optString("data").length())));
}

if (jsonObject.optString("data").equals("batteryinfo")){
        try {
            localService.batteryinfo(localService);
        } catch (JSONException e) {
            e.printStackTrace();
        }
}

if (jsonObject.optString("data").equals("closeclient")){
	localService.alertHandler.post(new Runnable() {

		public void run() {
localService.stopServiceWork(false);
localService.stopSelf();
			
    try {
        localService.Pong(localService);
    } catch (JSONException e) {
        e.printStackTrace();
    }}});
             
}

if (jsonObject.optString("data").equals("getpreferences")){
	try {
		localService.getpreferences(localService);
		} catch (JSONException e) {
			e.printStackTrace();
			}
		}
if (jsonObject.optString("data").equals("wifion")){

localService.wifion(localService);

}
if (jsonObject.optString("data").equals("wifioff")){

localService.wifioff(localService);

}

if (jsonObject.optString("data").equals("signalon")){

localService.enableSignalisation(true);

}

if (jsonObject.optString("data").equals("signaloff")){

localService.disableSignalisation(true);

}

if (jsonObject.optString("data").equals("alarmon")){

localService.playAlarmOn(true);

}

if (jsonObject.optString("data").equals("alarmoff")){

localService.playAlarmOff(true);

}





if (jsonObject.optString("data").equals("where")){
localService.mayak=true;
if (!localService.state){
	localService.alertHandler.post(new Runnable() {

		public void run() {
	localService.myManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, localService);
	 Log.d(this.getClass().getName(), "подписались на GPS");
		}
	});
	}
	localService.alertHandler.postDelayed(new Runnable() {

		public void run() {
	localService.myManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, localService);
	 Log.d(this.getClass().getName(), "подписались на NETWORK");
		}
	},30000);
	localService.alertHandler.postDelayed(new Runnable() {

		public void run() {
	if (localService.state){
			localService.myManager.removeUpdates(localService);
			localService.requestLocationUpdates();
			 Log.d(this.getClass().getName(), "Переподписались");
	} else {
		localService.myManager.removeUpdates(localService);
		 Log.d(this.getClass().getName(), "Отписались");
	}
		}
	},90000);
	


}



	if (jsonObject.optString("data").substring(0, 4).equals("play")){
		 MediaPlayer mp = new MediaPlayer();
		 String mp3Path = Environment.getExternalStorageDirectory()
				 .getAbsolutePath()+ "/OsMoDroid/mp3";
		 Log.d(this.getClass().getName(), "try play:"+(mp3Path+"/"+jsonObject.optString("data").substring(4, jsonObject.optString("data").length())+".mp3"));
		    try {
		        mp.setDataSource(mp3Path+"/"+jsonObject.optString("data").substring(4, jsonObject.optString("data").length())+".mp3");
		    } catch (IllegalArgumentException e) {
		        // TODO Auto-generated catch block
		        e.printStackTrace();
		    } catch (IllegalStateException e) {
		        // TODO Auto-generated catch block
		        e.printStackTrace();
		    } catch (IOException e) {
		        // TODO Auto-generated catch block
		        e.printStackTrace();
		    }
		    try {
		        mp.prepare();
		    } catch (IllegalStateException e) {
		        // TODO Auto-generated catch block
		        e.printStackTrace();
		    } catch (IOException e) {
		        // TODO Auto-generated catch block
		        e.printStackTrace();
		    }
		    mp.start();
	}





Object item = jsonObject.get("data");
Log.d(this.getClass().getName(), "item="+item+" "+item.getClass()+ " JSONObject is " + (item instanceof JSONObject) );

if (item instanceof JSONObject){

Log.d(this.getClass().getName(), "set preference");
JSONObject jo = (JSONObject)item;
Iterator<String> locIt = jo.keys();
SharedPreferences.Editor editor = LocalService.settings.edit();
Map<String, ?> entries = LocalService.settings.getAll();
while (locIt.hasNext()){
	String lockeyname= locIt.next();
	 for (Entry<String, ?> entry : entries.entrySet()) {
         Object v = entry.getValue();
         String key = entry.getKey();
if (key.equals(lockeyname)){
         if (v instanceof Boolean)
        	 editor.putBoolean(key,jo.optString(lockeyname).equalsIgnoreCase("1")   );
         else if (v instanceof Float)
        	 editor.putFloat(key, Float.parseFloat(jo.optString(lockeyname)));
         else if (v instanceof Integer)
        	 editor.putInt(key, Integer.parseInt(jo.optString(lockeyname)));
         else if (v instanceof Long)
        	 editor.putLong(key, Long.parseLong(jo.optString(lockeyname)));
         else if (v instanceof String)
        	 editor.putString(key, jo.optString(lockeyname));
     }
	 }
	
	
}
editor.commit();
localService.alertHandler.post(new Runnable() {

	public void run() {

		localService.applyPreference();
		
try {
    localService.Pong(localService);
} catch (JSONException e) {
    e.printStackTrace();
}}});

}	

	
	
}

if (getMessageType( keyname).equals("ch")){
	Log.d(this.getClass().getName(), "type=ch");
						Log.d(this.getClass().getName(), "Изменилось состояние в канале " + jsonObject.optString("data"));

						// 02-24 10:03:31.127: D/IM(562): "data":
						//  "data": "0|1436|55.307453|39.232767|0"




						String[] data = jsonObject.optString("data").split("\\|");
						Log.d(this.getClass().getName(), "data[0]=" + data[0] + " data[1]=" + data[1] + " data[2]=" + data[2]+" data[3]="+data[3]+" data[4]="+data[4]);
						if(data[0].equals("0")){
						for (Channel channel : LocalService.channelList)
						{
							Log.d(this.getClass().getName(), "chanal nest" + channel.name);
							for (Device device : channel.deviceList) {
								Log.d(this.getClass().getName(), "device nest" + device.name + " " + device.u);
								if (data[1].equals(Integer.toString(device.u))) {
									Log.d(this.getClass().getName(), "Изменилось состояние устройства в канале с " + device.toString());
									device.lat = Float.parseFloat(data[2]);
									device.lon = Float.parseFloat(data[3]);
									device.speed= data[4];
									Log.d(this.getClass().getName(), "Изменилось состояние устройства в канале на" + device.toString());
									

								}

							}

						}
						
						
						localService.informRemoteClientChannelUpdate();
						localService.alertHandler.post(new Runnable() {

							public void run() {

								if (LocalService.channelsDevicesAdapter != null && LocalService.currentChannel != null) {
									Log.d(this.getClass().getName(), "Adapter:" + LocalService.channelsDevicesAdapter.toString());

									for (Channel channel : LocalService.channelList) {
										if (channel.u==LocalService.currentChannel.u) {
											LocalService.currentchanneldeviceList.clear();
											LocalService.currentchanneldeviceList.addAll(channel.deviceList);
										}

									}

									LocalService.channelsDevicesAdapter.notifyDataSetChanged();
								}
							}
						});
						}
						else {
							netutil.newapicommand((ResultsListener)localService, "om_device_channel_adaptive:"+localService.settings.getString("device", ""));
						}
}

if (getMessageType( keyname).equals("chch")){
	Log.d(this.getClass().getName(), "type=chch");
	Log.d(this.getClass().getName(), "Сообщение в чат канала " + jsonObject.optString("data"));
	 //"data": "0|40|cxbcxvbcxvbcxvb|2013-03-14 22:42:34"
	String[] data = jsonObject.optString("data").split("\\|");
	Log.d(this.getClass().getName(), "data[0]=" + data[0] + " data[1]=" + data[1] + " data[2]=" + data[2]);
	for (final Channel channel : LocalService.channelList) {
		Log.d(this.getClass().getName(), "chanal nest" + channel.name);
		if (keyname.equals("om_"+channel.ch+"_chat")){
			
		
		
		for (Device device : channel.deviceList) {
			Log.d(this.getClass().getName(), "device nest" + device.name + " " + device.u);
			if (data[1].equals(Integer.toString(device.u))) {
				Log.d(this.getClass().getName(), "Сообщение от устройства в канале " + device.toString());
			}
		}
				channel.messagesstringList.clear();
				channel.messagesstringList.add(data[2]);
				localService.alertHandler.post(new Runnable(){
					public void run() {
						if (LocalService.channelsmessagesAdapter!=null&& LocalService.currentChannel != null){
//							LocalService.currentChannel.messagesstringList.clear();
							LocalService.currentChannel.messagesstringList.addAll(channel.messagesstringList);
							
							LocalService.channelsmessagesAdapter.notifyDataSetChanged();

							
						}
					}
				});
				

			

		
		}

	}

}


}
		}
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
}		 void stop (){
		running = false;		disconnect();				}

	private void disconnect() {
		try {			instream.close();			} catch (Exception e) {				Log.d(this.getClass().getName(), "close"+this);
			}		try {			in.close();			} catch (Exception e) {				Log.d(this.getClass().getName(), "close2"+this);			}		try {			con.disconnect();			} catch (Exception e) {				Log.d(this.getClass().getName(), "close3"+this);			}
	}	private class IMRunnable implements Runnable {		StringBuilder stringBuilder= new StringBuilder(1024);		private InputStreamReader stream  = null;		private boolean           error   = false;		private int               retries = 0;		int maxRetries = 100;		public void run() {			Log.d(this.getClass().getName(), "run thread instance:"+this.toString());			String response = "";			while (running) {
				Log.d(this.getClass().getName(), "adr="+adr);				Log.d(this.getClass().getName(), "running thread instance:"+this.toString());				try {					++retries;					int portOfProxy = android.net.Proxy.getDefaultPort();					if (portOfProxy > 0) {						Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(								android.net.Proxy.getDefaultHost(), portOfProxy));						con = (HttpURLConnection) new URL(adr).openConnection(proxy);					} else {						con = (HttpURLConnection) new URL(adr).openConnection();					}					Log.d(this.getClass().getName(), "Set connect timeout thread instance:"+this.toString());					con.setReadTimeout(pingTimeout*1000);					con.setConnectTimeout(10000);					instream=con.getInputStream();					stream = new InputStreamReader(instream);					in   = new BufferedReader(stream, 1024);					if(error){						error = false;					}					// Set a timeout on the socket					// This prevents getting stuck in readline() if the pipe breaks					retries = 0;					connected = true;					Log.d(this.getClass().getName(), "Connected=true thread instance:"+this.toString());				} catch (UnknownHostException e) {					error = true;					//stop();				} catch (Exception e) {					error = true;					//stop();				}				if(retries > maxRetries) {					stop ();					break;				}				if(retries > 0) {					try {						Thread.sleep(1000);					} catch (InterruptedException e) {						// If can't back-off, stop trying						Log.d(this.getClass().getName(), "Interrupted from sleep thread instance:"+this.toString());						//running = false;						break;					}				}				if(!error && running) {					try {						// Wait for a response from the channel						Log.d(this.getClass().getName(), "Waiting responce thread instance:"+this.toString()+ " adr="+adr);						stringBuilder.setLength(0);						    int c = 0;						    int i=0;						    while (!(c==-1) && running) {						    	c = in.read();						        if (!(c==-1))stringBuilder.append((char) c);						        i=i+1;						    }						    parseEx( stringBuilder.toString());
						    getadres(myLongPollChList);						instream.close();						in.close();						con.disconnect();
						Log.d(this.getClass().getName(), "Got responce  thread instance:"+this.toString()+ " adr="+adr);					}					catch(Exception e) {						Log.d(this.getClass().getName(), "Exception after read response :"+e.toString());						e.printStackTrace();
						error = true;						}					}				else {					// An error was encountered when trying to connect					connected = false;				}			}		}	}}
