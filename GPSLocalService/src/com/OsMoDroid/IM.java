
package com.OsMoDroid;import java.io.BufferedReader;import java.io.IOException;import java.io.InputStream;import java.io.InputStreamReader;import java.io.PrintWriter;import java.net.HttpURLConnection;import java.net.InetSocketAddress;import java.net.Proxy;import java.net.URL;import java.net.UnknownHostException;import java.text.SimpleDateFormat;import java.util.ArrayList;import java.util.Collections;
import java.util.Date;import java.util.Iterator;import org.json.JSONArray;import org.json.JSONException;import org.json.JSONObject;import com.OsMoDroid.LocalService.SendCoor;import android.app.Notification;import android.app.PendingIntent;import android.app.PendingIntent.CanceledException;import android.content.BroadcastReceiver;import android.content.Context;import android.content.Intent;import android.content.IntentFilter;import android.net.NetworkInfo;import android.os.Bundle;import android.os.Handler;import android.os.Message;import android.support.v4.app.NotificationCompat;import android.util.Log;import android.widget.Toast;public class IM {	protected  boolean running       = false;	protected boolean connected     = false;	protected boolean autoReconnect = true;	protected Integer timeout       = 0;	private HttpURLConnection con;	private InputStream instream;	String adr;	String mykey;//	String timestamp=Long.toString(System.currentTimeMillis());String lcursor="";	int pingTimeout=900;	Thread myThread;	private BufferedReader    in      = null;	Context parent;	String myLongPollCh;
	ArrayList<String[]>  myLongPollChList;	//ArrayList<String> list= new ArrayList<String>();	int mestype=0;	LocalService localService;	final private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	public IM(ArrayList<String[]> longPollChList, Context context,String key,LocalService localService) {
	
		
		parent=context;
		myLongPollChList=longPollChList;
		getadres(longPollChList);
		mykey=key;
		parent.registerReceiver(bcr, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
		start();
	}

	private void getadres(ArrayList<String[]> longPollChList) {
		//подключении не вставляется
		//http://d.esya.ru/?identifier=1364306035.30188078700000:somaanjUobpong&ncrnd=1364306032050
		//http://d.esya.ru/?identifier=somaanjUobpong&ncrnd=1364306032050
		
		
		
		adr="http://d.esya.ru/?identifier="+lcursor+mykey;
		for (String str[] : longPollChList){
			adr=adr+","+str[0];
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
		
		myLongPollChList.removeAll(longPollChList);
		getadres(myLongPollChList);
		disconnect();
		
		
	}
	//	public IM(String longPollch, Context context,int type) {////		adr="http://d.esya.ru/?identifier="+longPollch+"+&ncrnd="+timestamp;//		//http://d.esya.ru/?identifier=u883006497ctrl
//		//http://d.esya.ru/?identifier=iR7xp5Ub5nEoLLVGwrUL
//		//http://api.esya.ru/?system=om&action=start&key=iR7xp5Ub5nEoLLVGwrUL&device=1526&c=OsMoDroid&v=10041&signature=ec095fafe945a755744a53a2//		this.parent=context;////		myLongPollCh=longPollch;////		mestype=type;////////		parent.registerReceiver(bcr, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));////		start();////////////////	}String getMessageType(String ids){
	for (String[] str: myLongPollChList){
		if (str[0].equals(ids)){
			return str[1];
		}
	}
	
	return ids;
	
}			public void addtoDeviceChat(String message) {int u = 0;			try {
				MyMessage mes =new MyMessage( new JSONObject(message));
				for (Device dev : LocalService.deviceList){
					if(dev.app.equals(mes.for_app)){
						u=dev.u;
					}
				}
				
				
				if (LocalService.currentDevice!=null&& mes.from_app.equals(LocalService.currentDevice.app)){
				LocalService.chatmessagelist.add(mes);
				Collections.sort(LocalService.chatmessagelist);
				LocalService.chatmessagesAdapter.notifyDataSetChanged();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (u!=0){
			Message msg = new Message();			Bundle b = new Bundle();			b.putInt("deviceU", u);			msg.setData(b);			localService.alertHandler.sendMessage(msg);		}			}	private BroadcastReceiver bcr = new BroadcastReceiver() {		@Override		public void onReceive(Context context, Intent intent) {			Log.d(this.getClass().getName(), "BCR"+this);			Log.d(this.getClass().getName(), "BCR"+this+" Intent:"+intent);			if (intent.getAction().equals(android.net.ConnectivityManager.CONNECTIVITY_ACTION)) {				Bundle extras = intent.getExtras();				Log.d(this.getClass().getName(), "BCR"+this+ " "+intent.getExtras());				if(extras.containsKey("networkInfo")) {					NetworkInfo netinfo = (NetworkInfo) extras.get("networkInfo");					Log.d(this.getClass().getName(), "BCR"+this+ " "+netinfo);					Log.d(this.getClass().getName(), "BCR"+this+ " "+netinfo.getType());					if(netinfo.isConnected()) {						Log.d(this.getClass().getName(), "BCR"+this+" Network is connected");						Log.d(this.getClass().getName(), "BCR"+this+" Running:"+running);						// Network is connected						if(!running ) {							System.out.println("Starting from Reciever"+this.toString());							myThread.interrupt();							start();						}					}					else {						System.out.println("Stoping1 from Reciever"+this.toString());						stop();					}				}				else if(extras.containsKey("noConnectivity")) {					System.out.println("Stoping2 from Reciever"+this.toString());					stop();				}		    }		}	};	 void close(){		parent.unregisterReceiver(bcr);		stop();	};	 void start(){		this.running = true;		System.out.println("About to notify state from start()");		System.out.println("State notifed of start()");		myThread = new Thread(new IMRunnable());		myThread.start();	}void parseEx (String toParse){
	
	
	try {
		Log.d(this.getClass().getName(), "toParse= "+ toParse);

		//Toast.makeText(LocalService.serContext, toParse, Toast.LENGTH_LONG).show();
		JSONArray result = new JSONArray(toParse);

		for (int i = 0; i < result.length(); i++) {

		    JSONObject jsonObject = result.getJSONObject(i);
		    Iterator it = (new JSONObject(jsonObject.optString("ids"))).keys();

			  while (it.hasNext())
		    
		    

          	{
		    	
		    	String keyname= (String) it.next();
		    	Log.d(this.getClass().getName(), "keyname="+keyname);
		    	lcursor = (new JSONObject(jsonObject.optString("ids"))).optString(keyname)+":";
		    	 Log.d(this.getClass().getName(), "lcursor="+ lcursor);

if (getMessageType( keyname).equals("o")){
	Log.d(this.getClass().getName(), "type=o");
	String status;
	String messageText = "";
    String[] data = jsonObject.optString("data").split("-");
    Log.d(this.getClass().getName(), "data[0]="+data[0]+" data[1]="+data[1]+" data[2]="+data[2]);
    Log.d(this.getClass().getName(), "LocalService.DeviceList="+LocalService.deviceList.toString());
    for (Device device : LocalService.deviceList){
        if (data[1].equals(Integer.toString(device.u))&&device.u!=(Integer.parseInt(LocalService.settings.getString("device", ""))) ){
            if (data[0].equals("state")) {
                if (data[2].equals("1")) { status="запущен"; } else { status="остановлен"; }
                messageText = messageText+" Мониторинг на устройстве \""+device.name+"\" "+status;
                Log.d(this.getClass().getName(), "DeviceState="+messageText);
             //   device.state = "1";
            }
            if (data[0].equals("online")&&device.u!=(Integer.parseInt(LocalService.settings.getString("device", ""))) ){
                if (data[2].equals("1")) { status="вошло в сеть"; } else { status="покинуло сеть"; }
             //   device.online = "1";
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
            	
            	Message msg = new Message();

    			Bundle b = new Bundle();

    			b.putBoolean("om_online", true);
    			b.putString("MessageText", messageText);

    			msg.setData(b);

    			localService.alertHandler.sendMessage(msg);
    			Log.d(this.getClass().getName(), "Sended message:"+messageText);
            	
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
	Intent intent = new Intent("OsMoDroid_Control");

	intent.putExtra("command", jsonObject.optString("data"));

	Log.d(this.getClass().getName(), "Сигнал для сервиса "+ jsonObject.getString("data"));

	parent.sendBroadcast(intent);
	
	
}

if (getMessageType( keyname).equals("ch")){
	Log.d(this.getClass().getName(), "type=ch");
						Log.d(this.getClass().getName(), "Изменилось состояние в канале " + jsonObject.optString("data"));

						// 02-24 10:03:31.127: D/IM(562): "data":
						// "0|1436|38|15|0|0|0|271"
						String[] data = jsonObject.optString("data").split("\\|");
						Log.d(this.getClass().getName(), "data[0]=" + data[0] + " data[1]=" + data[1] + " data[2]=" + data[2]);
						for (Channel channel : LocalService.channelList) {
							Log.d(this.getClass().getName(), "chanal nest" + channel.name);
							for (Device device : channel.deviceList) {
								Log.d(this.getClass().getName(), "device nest" + device.name + " " + device.u);
								if (data[1].equals(Integer.toString(device.u))) {
									Log.d(this.getClass().getName(), "Изменилось состояние устройства в канале с " + device.toString());
									device.lat = Float.parseFloat(data[2]);
									device.lon = Float.parseFloat(data[3]);
									Log.d(this.getClass().getName(), "Изменилось состояние устройства в канале на" + device.toString());

								}

							}

						}

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
				channel.messagesstringList.clear();
				channel.messagesstringList.add(data[2]);
				localService.alertHandler.post(new Runnable(){
					public void run() {
						if (LocalService.channelsmessagesAdapter!=null&& LocalService.currentChannel != null){
//							LocalService.currentChannel.messagesstringList.clear();
							LocalService.currentChannel.messagesstringList.addAll(0,channel.messagesstringList);
							
							LocalService.channelsmessagesAdapter.notifyDataSetChanged();

							
						}
					}
				});
				

			}

		}
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
		try {			instream.close();			} catch (Exception e) {				e.printStackTrace();			}		try {			in.close();			} catch (Exception e) {				e.printStackTrace();			}		try {			con.disconnect();			} catch (Exception e) {				e.printStackTrace();			}
	}	private class IMRunnable implements Runnable {		StringBuilder stringBuilder= new StringBuilder(1024);		private InputStreamReader stream  = null;		private boolean           error   = false;		private int               retries = 0;		int maxRetries = 100;		public void run() {			Log.d(this.getClass().getName(), "run thread instance:"+this.toString());			String response = "";			while (running) {
				Log.d(this.getClass().getName(), "adr="+adr);				Log.d(this.getClass().getName(), "running thread instance:"+this.toString());				try {					++retries;					int portOfProxy = android.net.Proxy.getDefaultPort();					if (portOfProxy > 0) {						Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(								android.net.Proxy.getDefaultHost(), portOfProxy));						con = (HttpURLConnection) new URL(adr).openConnection(proxy);					} else {						con = (HttpURLConnection) new URL(adr).openConnection();					}					Log.d(this.getClass().getName(), "Set connect timeout thread instance:"+this.toString());					con.setReadTimeout(pingTimeout*1000);					con.setConnectTimeout(10000);					instream=con.getInputStream();					stream = new InputStreamReader(instream);					in   = new BufferedReader(stream, 1024);					if(error){						error = false;					}					// Set a timeout on the socket					// This prevents getting stuck in readline() if the pipe breaks					retries = 0;					connected = true;					Log.d(this.getClass().getName(), "Connected=true thread instance:"+this.toString());				} catch (UnknownHostException e) {					error = true;					//stop();				} catch (Exception e) {					error = true;					//stop();				}				if(retries > maxRetries) {					stop ();					break;				}				if(retries > 0) {					try {						Thread.sleep(1000);					} catch (InterruptedException e) {						// If can't back-off, stop trying						Log.d(this.getClass().getName(), "Interrupted from sleep thread instance:"+this.toString());						//running = false;						break;					}				}				if(!error && running) {					try {						// Wait for a response from the channel						Log.d(this.getClass().getName(), "Waiting responce thread instance:"+this.toString()+ " adr="+adr);						stringBuilder.setLength(0);						    int c = 0;						    int i=0;						    while (!(c==-1) && running) {						    	c = in.read();						        if (!(c==-1))stringBuilder.append((char) c);						        i=i+1;						    }						    parseEx( stringBuilder.toString());
						    getadres(myLongPollChList);						instream.close();						in.close();						con.disconnect();
						Log.d(this.getClass().getName(), "Got responce  thread instance:"+this.toString()+ " adr="+adr);					}					catch(Exception e) {						Log.d(this.getClass().getName(), "Exception:"+e.toString()+e.getMessage());						error = true;						}					}				else {					// An error was encountered when trying to connect					connected = false;				}			}		}	}}
