
package com.OsMoDroid;import java.io.BufferedReader;import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;import java.io.InputStream;import java.io.InputStreamReader;import java.io.PrintWriter;import java.net.HttpURLConnection;import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.InetSocketAddress;import java.net.Proxy;import java.net.URL;import java.net.UnknownHostException;import java.text.SimpleDateFormat;import java.util.ArrayList;import java.util.Collections;
import java.util.Map;
import java.util.Date;import java.util.Iterator;import java.util.Map.Entry;
import org.json.JSONArray;import org.json.JSONException;import org.json.JSONObject;import org.osmdroid.util.GeoPoint;
import com.OsMoDroid.IM.IMWriter;
import com.OsMoDroid.LocalService.SendCoor;import com.OsMoDroid.Netutil.MyAsyncTask;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;import android.app.PendingIntent;import android.app.PendingIntent.CanceledException;import android.content.BroadcastReceiver;import android.content.SharedPreferences;
import android.content.Context;import android.content.Intent;import android.content.IntentFilter;import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.NetworkInfo;import android.os.Bundle;import android.os.Environment;
import android.os.Looper;
import android.os.SystemClock;
import android.os.Handler;import android.os.Message;import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.support.v4.app.NotificationCompat;import android.telephony.TelephonyManager;
import android.util.Log;import android.widget.Toast;
/**
 * @author dfokin
 *Class for work with LongPolling
 */
public class IM implements ResultsListener {	
	private IMWriter iMWriter;	private static final int RECONNECT_TIMEOUT = 1000*5;
	
	private static final int KEEP_ALIVE = 1000*270;

	private static final String RECONNECT_INTENT = "com.osmodroid.reconnect";
	
	private static final String KEEPALIVE_INTENT = "com.osmodroid.keepalive";

	protected  boolean running       = false;	protected boolean autoReconnect = true;	protected Integer timeout       = 0;	private HttpURLConnection con;	private InputStream instream;	String adr;	String lcursor="";	int pingTimeout=900;	Thread connectThread;		Context parent;	private String token="";	String myLongPollCh;
	ArrayList<String[]>  myLongPollChList = new ArrayList<String[]>();
	ArrayList<String>  listenArrayList = new ArrayList<String>();	int mestype=0;	LocalService localService;	FileOutputStream fos;
	ObjectOutputStream output = null;	final private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static String SERVER_IP;// = "osmo.mobi";
	static int SERVERPORT;// = 5757;
	protected boolean connOpened=false;
	protected boolean connecting=false;
	final boolean  log=true;
	public Socket socket;
	public boolean authed=false;
	public BufferedReader rd;

	public PrintWriter wr;

	long sendBytes=0;
	long recievedBytes=0;
	public boolean needopensession=false;
	public boolean needclosesession=false;
	private Thread readerThread;

	private Thread writerThread;
	private int workserverint=-1;

	private String workservername="";	
	
	Handler readHandler= new Handler(){
		@Override
		public void handleMessage(Message msg)
			{
				parseEx(msg.getData().getString("read"), msg.getData().getString("read"));
				super.handleMessage(msg);
			}
	};

	
	public IM(String server, int port, LocalService service){
		localService=service;
		parent=service;
		manager = (AlarmManager)(parent.getSystemService( Context.ALARM_SERVICE ));
		reconnectPIntent = PendingIntent.getBroadcast( parent, 0, new Intent(RECONNECT_INTENT), 0 );
		keepAlivePIntent = PendingIntent.getBroadcast( parent, 0, new Intent(KEEPALIVE_INTENT), 0 );
		
		SERVER_IP=server;
		SERVERPORT=port;
		if(!OsMoDroid.settings.getString("newkey", "").equals("")){
		start();
		}
	}
	public void sendToServer(String str)
		{
			Message msg =new Message();
			Bundle b =new Bundle();
			b.putString("write",str);
			msg.setData(b);
			if (iMWriter.handler!=null){
				iMWriter.handler.sendMessage(msg);	
			}
			else {
				addlog("panic! handler is null ");
				 if(log)Log.d(this.getClass().getName(), " handler is null!!!");
			}
				
			
		}
	
	  BroadcastReceiver reconnectReceiver = new BroadcastReceiver() {
          @Override public void onReceive( Context context, Intent _ )
          {
        	  addlog("websocket reconnect reciever trigged");
        	  start();
              context.unregisterReceiver( this ); // this == BroadcastReceiver, not Activity
          }
      };
      BroadcastReceiver keepAliveReceiver = new BroadcastReceiver() {
          @Override public void onReceive( Context context, Intent _ )
          {
              if(connOpened){
            	  addlog("websocket sendPing");
            	  if(log)Log.d(this.getClass().getName(), " send ping");
            	  sendToServer("P");
            	
              }
             
          }
      };
      
      void addlog(final String str){
    	  	localService.alertHandler.post(new Runnable()
				{
					
					@Override
					public void run()
						{
							//if(OsMoDroid.debug)ExceptionHandler.reportOnlyHandler(parent.getApplicationContext()).uncaughtException(Thread.currentThread(), new Throwable(str));
				    	  	if(OsMoDroid.debug)LocalService.debuglist.add( sdf1.format(new Date(System.currentTimeMillis()))+" "+str+" S="+sendBytes+ " R="+recievedBytes);
				  			if(LocalService.debugAdapter!=null){LocalService.debugAdapter.notifyDataSetChanged();}
							
						}
				});
    	  	
    	  
      }
      
      
      AlarmManager manager;
      PendingIntent reconnectPIntent;
      PendingIntent keepAlivePIntent;
	 
      public void setkeepAliveAlarm(){
    	  if(log)Log.d(this.getClass().getName(), "void setKeepAliveAlarm");
    	  addlog("websocket void setkeepalive");
    	  parent.registerReceiver(keepAliveReceiver, new IntentFilter(KEEPALIVE_INTENT));
    	  manager.cancel(keepAlivePIntent);
    	  manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, KEEP_ALIVE, KEEP_ALIVE, keepAlivePIntent);
      }
      
      public void disablekeepAliveAlarm(){
    	  if(log)Log.d(this.getClass().getName(), "void disableKeepAliveAlarm");
    	  addlog("websocket void disablekeepalive");
    	  try {
			parent.unregisterReceiver(keepAliveReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	  manager.cancel(keepAlivePIntent);
      }
      
    synchronized  public void setReconnectAlarm() 
	    {	
    	  if(log)Log.d(this.getClass().getName(), "void setReconnectAlarm");
    	  
    	  localService.alertHandler.post(new Runnable(){
				 @Override
				public void run()
					{
						addlog("websocket setReconnectAlarn");
						
					}
			 });
    	  parent.registerReceiver( reconnectReceiver, new IntentFilter(RECONNECT_INTENT) );
    	  manager.cancel(reconnectPIntent);
    	  manager.set( AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + RECONNECT_TIMEOUT, reconnectPIntent );
	    }
	

	
	
	public void addchannels(ArrayList<String[]> longPollChList){
		
		myLongPollChList.addAll(longPollChList);
		//getadres(myLongPollChList);
		resubscribe();
		
		
	}
	
public void removechannels(ArrayList<String[]> longPollChList){
	for (String[] extstr: longPollChList){
		if(log)Log.d(this.getClass().getName(), "extstr="+extstr[0]);
		 for (Iterator<String[]> iter = myLongPollChList.iterator(); iter.hasNext();) {
		      String[] s = iter.next();
		      if (s[0].equals(extstr[0])) {
		    	 if(log)Log.d(this.getClass().getName(), "myLongPollChList.count="+myLongPollChList.size());
		    	iter.remove();
		        if(log)Log.d(this.getClass().getName(), "iter removed");
		        if(log)Log.d(this.getClass().getName(), "myLongPollChList.count="+myLongPollChList.size());
		      }
		    }
	}
		//getadres(myLongPollChList);
	resubscribe();
	}
	String getMessageType(String ids){
	for (String[] str: myLongPollChList){
		if (str[0].equals(ids)){
			return str[1];
		}
	}
	
	return ids;
	
}			public void addtoDeviceChat(String message) {String u = "";			try {
				MyMessage mes =new MyMessage( new JSONObject(message));
				if(log)Log.d(this.getClass().getName(), "MyMessage,from "+mes.from);
				if(log)Log.d(this.getClass().getName(), "DeviceList= "+LocalService.deviceList);
if (mes.from.equals(OsMoDroid.settings.getString("device", ""))){
	for (Device dev : LocalService.deviceList){
		if((dev.tracker_id).equals(mes.to)){
			u=dev.tracker_id;
		}
	}
} else {
				for (Device dev : LocalService.deviceList){
					if((dev.tracker_id).equals(mes.from)){
						u=dev.tracker_id;
					}
				}
}		
				
				if (LocalService.currentDevice!=null&& u ==LocalService.currentDevice.tracker_id){
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
			if (!u.equals("")){
			Message msg = new Message();			Bundle b = new Bundle();			b.putString("deviceU", u);			msg.setData(b);			localService.alertHandler.sendMessage(msg);		}			}	private BroadcastReceiver bcr = new BroadcastReceiver() {		@Override		public void onReceive(Context context, Intent intent) {			addlog("network broadcast recive");		//	if(log)Log.d(this.getClass().getName(), "BCR"+this);		//	if(log)Log.d(this.getClass().getName(), "BCR"+this+" Intent:"+intent);			if (intent.getAction().equals(android.net.ConnectivityManager.CONNECTIVITY_ACTION)) {				Bundle extras = intent.getExtras();			//	if(log)Log.d(this.getClass().getName(), "BCR"+this+ " "+intent.getExtras());				if(extras.containsKey("networkInfo")) {					NetworkInfo netinfo = (NetworkInfo) extras.get("networkInfo");				//	if(log)Log.d(this.getClass().getName(), "BCR"+this+ " "+netinfo);				//	if(log)Log.d(this.getClass().getName(), "BCR"+this+ " "+netinfo.getType());					if(netinfo.isConnected()) {						if(log)Log.d(this.getClass().getName(), "BCR Network is connected");						if(log)Log.d(this.getClass().getName(), "Running:"+running);						// Network is connected
						addlog("webcoket Network is connected, running="+running);						if(!running ) {							//SetAlarm();
							start();
							addlog("webcoket start by broadcast because no running");
						}											}					else {						if(log)Log.d(this.getClass().getName(), "BCR Network is not connected");						if(log)Log.d(this.getClass().getName(), "Running:"+running);
						addlog("webcoket Network is not connected, running="+running);						if (running)
						{							addlog("webcoket stop by broadcast because running");
							stop();
						}
											}				}				else if(extras.containsKey("noConnectivity")) {					if(log)Log.d(this.getClass().getName(), "BCR Network is noConnectivity");					if(log)Log.d(this.getClass().getName(), "Running:"+running);
					addlog("webcoket Network is not connected, running="+running);					if (running)
					{						addlog("webcoket stop by broadcast because running");
						stop();
					}
									}		    }		}	};

	

	

	


	





	 /**
	 * Выключает IM
	 */
	void close(){		if(log)Log.d(this.getClass().getName(), "void IM.close");
		addlog("webcoket void close");
		try {
			parent.unregisterReceiver(bcr);
		} catch (Exception e) {
		
			e.printStackTrace();
		}
				stop();	};
	public void gettoken()
	
	{
		addlog("Start gettoket");
		Log.d(getClass().getSimpleName(), "http://api.osmo.mobi/prepare");
	        APIcomParams params = new APIcomParams("http://api.osmo.mobi/prepare","key="+OsMoDroid.settings.getString("newkey", "")+"&protocol=1","gettoken"); 
	        MyAsyncTask sendidtask = new Netutil.MyAsyncTask(this);
	        sendidtask.execute(params) ;
	        Log.d(getClass().getSimpleName(), "gettoken start to execute");

	}
	
		 void start(){		if(log)Log.d(this.getClass().getName(), "void IM.start");
		addlog("webcoket void start");
		running = true;		connecting=true;
		localService.refresh();
		iMWriter=new IMWriter();
		writerThread = new Thread(iMWriter);
		connectThread = new Thread(new IMConnect());
		readerThread = new Thread(new IMReader());
		connectThread.setPriority(Thread.MIN_PRIORITY);
		readerThread.setPriority(Thread.MIN_PRIORITY);
		writerThread.setPriority(Thread.MIN_PRIORITY);
		gettoken();
		
		
		parent.registerReceiver(bcr, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
		  }
	 public class IMWriter implements Runnable{
		 boolean error=false;
		 Looper l;
		 public Handler handler;
		@Override
		public void run()
			
			{
				 Looper.prepare();
				    l=Looper.myLooper();
				    handler = new Handler(){

						@Override
						public void handleMessage(Message msg)
							{
								Bundle b = msg.getData();
								if(running){
									if (socket.isConnected()&&wr!=null){
										 setReconnectAlarm(); 
										 try
												{
													
													Thread.sleep(500);
												} catch (InterruptedException e)
												{
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
										 wr.println(b.getString("write"));
										
										 error=wr.checkError();
										
										 if(log)Log.d(this.getClass().getName(), "wr write "+b.getString("write")+" error="+error);
										 addlog("wr write "+b.getString("write")+" error="+error);
										 if(error){
											 setReconnectOnError();
											 Looper.myLooper().quit();
										 }
										 else{
											 sendBytes=sendBytes+b.getString("write").getBytes().length;
										 }
										 }
								}
								else {
									addlog("not connected now");
								}
							
								super.handleMessage(msg);
							}
				    	
				    };
				    Looper.loop();
				    
			
	 }
	 
	
	 }
	 private class IMReader implements Runnable{
		
		private StringBuilder stringBuilder = new StringBuilder(1024);
		private String str;
		 @Override
		public void run()
			{
				
					while (connOpened){
						 try
							{
								stringBuilder.setLength(0);
								int c = 0;
								int i=0;
								while (!(c==10) ) {
								c = rd.read();
								if (!(c==-1))
									{stringBuilder.append((char) c);
								} 
								else {
									 if(log)Log.d(this.getClass().getName(), "inputstream c=-1 ");
									 addlog("inputstream c=-1 ");
									 setReconnectOnError();
									 break;
								}
								i=i+1;
								}
								if (stringBuilder.length()!=0&&connOpened){
									str=stringBuilder.toString();
									recievedBytes=recievedBytes+str.getBytes().length;
									Message msg =new Message();
									Bundle b =new Bundle();
									b.putString("read",str);
									msg.setData(b);
									localService.alertHandler.post(new Runnable(){
										 @Override
										public void run()
											{
												addlog(str);
												 							}
									 });
									
									readHandler.sendMessage(msg);
								
								}
							} catch (IOException e)
							{
								 setReconnectOnError();
								e.printStackTrace();
							}
						
					
					 
					}
				}
				
			}
	 
	 
	 private class IMConnect implements Runnable {
		 
		 
		@Override
		public void run()
			{
				SocketAddress sockAddr;
				 try {
					 if(workservername.equals(""))
						 {
							 InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
							 sockAddr = new InetSocketAddress(serverAddr, SERVERPORT);
						 }
					 else
						 {
							 InetAddress serverAddr = InetAddress.getByName(workservername);
							 sockAddr = new InetSocketAddress(serverAddr, workserverint);
						 }
					 workserverint=-1;
					 workservername="";
					 socket=new Socket();
					 
					socket.connect(sockAddr, 5000);
					 connOpened=true;
					 connecting=false;
					 
					 localService.alertHandler.post(new Runnable(){
						 @Override
						public void run()
							{
								addlog("TCP Connected");
								localService.refresh();
								
							}
					 });
					 
					 rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					 wr =new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"),true);
					 readerThread.start();
					 writerThread.start();
					 
				 } catch (final Exception e1) {
					 e1.printStackTrace();
					 connecting=false;
					 setReconnectOnError();
					 localService.alertHandler.post(new Runnable(){
						 @Override
						public void run()
							{
								 addlog("could no conenct to socket"+e1.getMessage());
								 							}
					 });
					 
					
				 }
								
			}
		 
	 }
	 
	 void stop (){
		 if(log)Log.d(this.getClass().getName(), "void IM.stop");
		 addlog("webcoket void stop");
		 running = false;
		 if(iMWriter.handler!=null){iMWriter.handler.getLooper().quit();}
		 if(socket!=null){
		 try
			{
				socket.close();
			} catch (IOException e)
			{
				addlog("exeption close socket "+e.getMessage());
				e.printStackTrace();
			}
		 }
		 
		 manager.cancel(reconnectPIntent);		 

			}

	 void resubscribe() {
		// addlog("webcoket resubscribe");
		 if(connOpened){

			 if(log)Log.d(this.getClass().getName(), "websocket unsubscribe all");
		for (String str[] : myLongPollChList){
			
			   if(log)Log.d(this.getClass().getName(), "websocket subscribe:"+str[0]);
            }
		
				
		
		}
	}

	private void addToChannelChat(String toParse, String topic) {
		if(log)Log.d(this.getClass().getName(), "type=chch");
		if(log)Log.d(this.getClass().getName(), "Сообщение в чат канала " + toParse);
		ChannelChatMessage m =new ChannelChatMessage();
		String fromDevice="Незнамо кто";
		//09-16 18:25:41.057: D/com.OsMoDroid.IM(1474):     "data": "0|40+\u041e\u043f\u0430\u0441\u043d\u043e +2013-09-16 22:25:44"
		//"data": "0|40|cxbcxvbcxvbcxvb|2013-03-14 22:42:34"
		String[] data = toParse.split("\\|");
		//if(log)Log.d(this.getClass().getName(), "data[0]=" + data[0] + " data[1]=" + data[1] + " data[2]=" + data[2]);
		String[] datanew = data[1].split("\\+");
		//if(log)Log.d(this.getClass().getName(), "datanew[0]=" + datanew[0] + " datanew[1]=" + datanew[1] + " datanew[2]=" + datanew[2]);
		for (final Channel channel : LocalService.channelList) {
			if(log)Log.d(this.getClass().getName(), "chanal nest" + channel.name);
			if (topic.equals(channel.group_id+"_chat")){
						
			
			for (Device device : channel.deviceList) {
				if(log)Log.d(this.getClass().getName(), "device nest" + device.name + " " + device.tracker_id);
				if (datanew[0].equals((device.tracker_id))) {
					if(log)Log.d(this.getClass().getName(), "Сообщение от устройства в канале " + device.toString());
					fromDevice = device.name;
				}
				if (datanew[0].equals(OsMoDroid.settings.getString("device", ""))){
					fromDevice=localService.getString(R.string.iam);
					if(log)Log.d(this.getClass().getName(), "Сообщение от устройства в канале от меня ");
				}
				if (datanew[0].equals("0")){
				fromDevice=localService.getString(R.string.observers);
				}
			}
			Intent intent =new Intent(localService, GPSLocalServiceClient.class).putExtra("channelpos", channel.u);
			intent.setAction("channelchat");
			PendingIntent contentIntent = PendingIntent.getActivity(localService,333,intent, PendingIntent.FLAG_CANCEL_CURRENT);
			Long when=System.currentTimeMillis();
		 	NotificationCompat.Builder notificationBuilder =new NotificationCompat.Builder(
					localService.getApplicationContext())
			    	.setWhen(when)
			    	.setContentText(channel.name+" "+fromDevice+": "+Netutil.unescape(datanew[1]))
			    	.setContentTitle("OsMoDroid")
			    	.setSmallIcon(android.R.drawable.ic_menu_send)
			    	.setAutoCancel(true)
			    	.setDefaults(Notification.DEFAULT_LIGHTS)
			    	.setContentIntent(contentIntent);
	
	if (!OsMoDroid.settings.getBoolean("silentnotify", false)){
			 notificationBuilder.setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_VIBRATE| Notification.DEFAULT_SOUND);
			    	}
				Notification notification = notificationBuilder.build();
				LocalService.mNotificationManager.notify(OsMoDroid.mesnotifyid, notification);
		
		if (LocalService.channelsmessagesAdapter!=null&& LocalService.currentChannel != null&&LocalService.currentChannel.u==channel.u&&LocalService.chatVisible ){
			LocalService.mNotificationManager.cancel(OsMoDroid.mesnotifyid);
		}
	
			
			
			
					channel.messagesstringList.clear();
					m.text=Netutil.unescape(datanew[1]);
					m.time=datanew[2];
					channel.messagesstringList.add(m);
					localService.alertHandler.post(new Runnable(){
						public void run() {
							if (LocalService.channelsmessagesAdapter!=null&& LocalService.currentChannel != null&&LocalService.currentChannel.u==channel.u ){
								LocalService.currentChannel.messagesstringList.addAll(channel.messagesstringList);
								LocalService.channelsmessagesAdapter.notifyDataSetChanged();
							}
						}
					});
			}
		}
	}

	@SuppressWarnings("static-access")
	void parseEx (String toParse, String topic){
		addlog("recieve "+toParse);
		if(log)Log.d(this.getClass().getName(), "recive "+toParse);
		manager.cancel(reconnectPIntent);
	if(toParse.equals("P|\n")){
		addlog("recieve pong");
		
		return;
		}
	
	JSONObject jo = new JSONObject();
	JSONArray ja = new JSONArray();
	String c="";
	String d="";
	try
		{
			c = toParse.substring(0, toParse.indexOf('|'));
			d = toParse.substring(toParse.indexOf('|')+1);
		} catch (Exception e1)
		{
			c=toParse;
			
		}
	
	try
		{
			
			jo = new JSONObject(d);
		}
	catch (JSONException e) {
		try {
			e.printStackTrace();
			ja=new JSONArray(d);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
	}
	
	if(c.equals("NEED_AUTH")){
		sendToServer( "AUTH|{\"key\":\""+OsMoDroid.settings.getString("newkey", "")+"\"}");
	}
	if(c.equals("NEED_TOKEN")){
		sendToServer( "TOKEN|"+token);
	}
	
	
	if(c.equals("TOKEN")){
		if(!jo.has("error")){
			authed=true;
			if(needopensession){
				sendToServer("TRACKER_SESSION_OPEN");
			}
			if(needclosesession){
				sendToServer("TRACKER_SESSION_CLOSE");
			}
			if(OsMoDroid.gpslocalserviceclientVisible){
				sendToServer("MOTD");
			}
			
			sendToServer("GROUP_GET_ALL");
			setkeepAliveAlarm();
			String listen = "";
			for (String str : listenArrayList){
				listen=("LISTEN:"+str)+"=";
			}
			if(!listen.equals(""))
				{
					sendToServer(listen);
				}
		}
	}
	
	
	if(c.equals("TRACKER_SESSION_OPEN")){
		localService.sessionstarted=true;
		needopensession=false;
		OsMoDroid.editor.putString("viewurl","http://test1342.osmo.mobi/u/"+jo.optString("url"));
		OsMoDroid.editor.commit();
		localService.refresh();
	}
	else
		if(c.equals("TRACKER_SESSION_CLOSE")){
			localService.sessionstarted=false;
			needclosesession=false;
			}
	
	if(c.equals("T")){
		localService.sendcounter++;
		localService.sending="";
		localService.refresh();
		return;
	}
	if(c.equals("MOTD")){
		localService.motd=LocalService.unescape(d);
		localService.refresh();
	}
	if(c.contains("GROUP_JOIN")){
		sendToServer("GROUP_CONNECT"+c.substring(c.indexOf(":")));
	}
	if(c.contains("GROUP_CONNECT"))
		{
		if(!jo.has("error")){
			String listen="";
			Channel ch = new Channel(jo, localService);
			
			if(!LocalService.channelList.contains(ch))
			{
				LocalService.channelList.add(ch);
				
			}
			addlog(ch.deviceList.toString());
					if (LocalService.channelsAdapter!=null ){
						
						LocalService.channelsAdapter.notifyDataSetChanged();
			
					}
					for(Device dev: ch.deviceList){
						listen=listen+("LISTEN:"+dev.tracker_id)+"=";
					}
					if(!listen.equals(""))
					{
						sendToServer(listen);
					}		
			}
		else {
			Toast.makeText(localService, "No group", Toast.LENGTH_SHORT);
		}
		}
	if(c.contains("GROUP_GET_ALL"))
	{
		 
			for (int i = 0; i < ja.length(); i++) {
		 			JSONObject jsonObject;
					try {
						jsonObject = ja.getJSONObject(i);
						sendToServer("GROUP_CONNECT:"+jsonObject.getString("group_id"));
					}
					catch (Exception e) {
						// TODO: handle exception
					}
			}
		
	}
	//LT:fI8qCrlvw6j0dEKZtB9h|L59.252465:30.324515S20.3A124.3H2.5C235
	if(c.contains("LT"))
		
		{
		for (Channel ch : LocalService.channelList)
			{
			for (Device dev : ch.deviceList){
			if (c.substring(c.indexOf(":")+1, c.length()).equals(dev.tracker_id)){
				
				dev.lat=Float.parseFloat(d.substring(d.indexOf("L")+1, d.indexOf(":")));
				for (int i = d.indexOf(":")+1; i <= d.length(); i++) {
					if(!Character.isDigit(d.charAt(i))){
						if(!Character.toString(d.charAt(i)).equals(".")){
							dev.lon=Float.parseFloat(d.substring(d.indexOf(":")+1, i));
							break;
						}
					}
				
				}
				for (int i = d.indexOf("S")+1; i <= d.length(); i++) {
					if(!Character.isDigit(d.charAt(i))){
						if(!Character.toString(d.charAt(i)).equals(".")){
							dev.speed=d.substring(d.indexOf("S")+1, i);
							break;
						}
					}
				
				}
				
				if(LocalService.devlistener!=null){LocalService.devlistener.onDeviceChange(dev);}
				}
				}
			}
		}
	
//	try
//		{
//			
//			if(jo.has("server")){
//				
//			}
//			if(jo.has("c")){
//				if(jo.getString("c").equals("AU")){
//					if(jo.has("device")){
//					authed=true;
//					setkeepAliveAlarm();
//					sendToServer("TRACKER_SESSION_OPEN");
//					}
//				}
//				else
//					if(jo.getString("c").equals("session_open")){
//						sessionstarted=true;
//						OsMoDroid.editor.putString("viewurl","http://test1342.osmo.mobi/u/"+jo.optString("url"));
//						OsMoDroid.editor.commit();
//						localService.refresh();
//					}
//				else
//					if(jo.getString("c").equals("session_close")){
//						sessionstarted=false;
//						close();
//						}
//			}
//		} catch (JSONException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		

	
	
	
	}

	void ondisconnect(){
		
	}
	
	private void setReconnectOnError()
		{	
			localService.alertHandler.post(new Runnable()
				{
					
					@Override
					public void run()
						{
							ondisconnect();
							
						}
				});
			
			
			try
				{
					socket.close();
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			disablekeepAliveAlarm();
			authed=false;
			connecting=false;
			connOpened=false;
			running=false;
			localService.refresh();
			if(localService.isOnline()){
				 setReconnectAlarm();
			 }
		}

	@Override
	public void onResultsSucceeded(APIComResult result) {
		if(result.Command.equals("gettoken")&&!(result.Jo==null)){
			addlog("Recieve token "+result.Jo.toString());
			if(log)Log.d(getClass().getSimpleName(),"gettoken response:"+result.Jo.toString());
			//Toast.makeText(localService,result.Jo.optString("state")+" "+ result.Jo.optString("error_description"),5).show();
			if(result.Jo.has("token")){
				try {
					token=result.Jo.getString("token");
					workservername=result.Jo.optString("address").substring(0, result.Jo.optString("address").indexOf(':'));
					workserverint=Integer.parseInt(result.Jo.optString("address").substring( result.Jo.optString("address").indexOf(':')+1));
					connectThread.start();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
	}}
