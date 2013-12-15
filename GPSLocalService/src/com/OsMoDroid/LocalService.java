
package com.OsMoDroid;



import android.net.ConnectivityManager;

import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.Vibrator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.BufferedWriter;
import java.io.StreamCorruptedException;

import java.io.File;

import java.io.FileWriter;

import java.io.IOException;

import java.io.InputStream;

import java.io.InputStreamReader;

import java.io.OutputStream;

import java.io.OutputStreamWriter;

import java.io.PrintWriter;

import java.io.Reader;

import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.Method;

import java.net.HttpURLConnection;

import java.net.InetSocketAddress;

import java.net.Proxy;

import java.net.Socket;

import java.net.SocketAddress;

import java.net.URL;



import java.text.DecimalFormat;

import java.text.DecimalFormatSymbols;

//import java.text.NumberFormat;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import java.util.Iterator;

import java.util.Locale;

import java.util.TimeZone;

import java.util.concurrent.TimeUnit;

//import java.util.Locale;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import com.OsMoDroid.Channel.Point;
import com.OsMoDroid.LocalService.SendCoor;

import com.OsMoDroid.netutil.MyAsyncTask;



import android.app.AlarmManager;

import android.app.AlertDialog;

import android.app.Notification;

import android.app.NotificationManager;

import android.app.PendingIntent;

import android.app.Activity;
import android.app.Service;
import android.app.PendingIntent.CanceledException;

import android.content.BroadcastReceiver;

import android.content.Context;

import android.content.DialogInterface;

import android.content.Intent;

import android.content.IntentFilter;

import android.content.SharedPreferences;

import android.content.pm.PackageInfo;

import android.content.pm.PackageManager.NameNotFoundException;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


import android.os.AsyncTask;

import android.os.Bundle;

//import android.os.Handler;

import android.os.Binder;

//import android.os.Debug;

import android.os.BatteryManager;

import android.os.IBinder;

import android.os.PowerManager;

import android.os.PowerManager.WakeLock;

import android.os.RemoteException;

import android.os.SystemClock;

import android.preference.PreferenceManager;

import android.speech.tts.TextToSpeech;

import android.speech.tts.TextToSpeech.OnInitListener;

//import android.preference.PreferenceManager;

//import android.util.Log;



//import android.util.Log;



//import android.util.Log;

//import android.util.Log;

import android.view.Display;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import android.widget.TextView;

import android.widget.Toast;

import android.location.GpsSatellite;

import android.location.GpsStatus;

import android.location.Location;

import android.location.LocationListener;

import android.location.LocationManager;

import android.media.AudioManager;
import android.media.SoundPool;




//import android.media.RingtoneManager;

//import org.apache.http.HttpResponse;

//import org.apache.http.HttpStatus;

//import org.apache.http.client.HttpClient;

//import org.apache.http.client.methods.HttpPost;

//import org.apache.http.impl.client.DefaultHttpClient;

//import org.apache.http.util.EntityUtils;

//import android.media.Ringtone;

import android.speech.tts.TextToSpeech;

import android.speech.tts.TextToSpeech.OnInitListener;

import android.support.v4.app.NotificationCompat;

import android.util.Log;





public  class LocalService extends Service implements LocationListener,GpsStatus.Listener, TextToSpeech.OnInitListener,  ResultsListener, SensorEventListener  {

	@Override
	public boolean onUnbind(Intent intent) {
		binded=false;
		disconnectChannels();
		return super.onUnbind(intent);
	}
	boolean binded=false;
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	final double calibration = SensorManager.STANDARD_GRAVITY;
	float currentAcceleration;
	private static final int OSMODROID_ID = 1;
	Boolean sessionstarted=false;
	Boolean globalsend=false;
	Boolean signalisationOn=false;
	int notifyid=2;
	int gpson;
	int gpsoff;
	int ineton;
	int inetoff;
	int sendpalyer;
	int startsound;
	int stopsound;
	int alarmsound;
	int signalonoff;
	private static SoundPool soundPool;
	private netutil.MyAsyncTask starttask;
	private Intent in;
	public boolean mayak=false;
	private boolean glonas=false;
	private boolean playsound=false;
	private boolean sendsound=false;
	private boolean vibrate;
	private boolean usecourse;
	private int vibratetime;
	private double brng;
	private double brng_gpx;
	private double prevbrng;
	private double prevbrng_gpx;
	private float speedbearing ;
	private float bearing ;
	private int speed;
	private boolean beepedon=false;
	private boolean beepedoff=false;
	private boolean gpsbeepedon=false;
	private boolean gpsbeepedoff=false;
	float maxspeed=0;
	float avgspeed;
	float currentspeed;
	long timeperiod=0;
	float workdistance;
	private long workmilli=0;
	private boolean firstsend=true;
	private boolean sended=true;
	private boolean gpx = false;
	private boolean live = true;
	private int hdop;
	private boolean fileheaderok = false;
	private File fileName = null;
	private int period;
	private int distance;
	private String hash;
	private int n;
	private String position;
	private String sendresult="";
	public LocationManager myManager;
	private Location prevlocation;
	public static Location currentLocation;
	private Location prevlocation_gpx;
	private Location prevlocation_spd;
	private String URLadr;
	private Vibrator vibrator;
	private PowerManager pm;
	private WakeLock wakeLock;
	private WakeLock LocwakeLock;
	private WakeLock SendwakeLock;
	private SendCoor send;
	private int speedbearing_gpx;
	private int bearing_gpx;
	private long lastgpslocationtime=0;
	private int hdop_gpx;
	private int period_gpx;
	private int distance_gpx;
	private int speed_gpx;
	int sendcounter;
	int writecounter=0;
	private int buffercounter=0;
	BroadcastReceiver receiver;
	BroadcastReceiver checkreceiver;
	BroadcastReceiver onlinePauseforStartReciever;
	BroadcastReceiver batteryReciever;
	private final IBinder mBinder = new LocalBinder();
	private String gpxbuffer= new String();
	private String satellite="";
	private String accuracy="";
	private boolean usebuffer = false;
	private boolean usewake=false;
	static NotificationManager mNotificationManager;
	private String lastsendforbuffer="First";
	private long lastgpsontime=0;
	private long lastgpsofftime=0;
	private long notifyperiod=30000;
	private AlarmManager am;
	StringBuilder stringBuilder= new StringBuilder();
	PendingIntent pi;
	private Object[] mStartForegroundArgs = new Object[2];
	private Object[] mStopForegroundArgs = new Object[1];
	private String pass;
	private String lastsay="a";
	Boolean state=false;
	int gpsperiod;
	int gpsdistance;
	long prevnetworklocationtime=0;
	StringBuilder buffersb = new StringBuilder(327681);
	StringBuilder lastbuffersb = new StringBuilder(327681);
	StringBuilder sendedsb = new StringBuilder(327681);
	private int lcounter=0;
	private int scounter=0;
	final private static DecimalFormat df6 = new DecimalFormat("########.######");
	final static DecimalFormat df1 = new DecimalFormat("########.#");
	final static DecimalFormat df2 = new DecimalFormat("########.##");
	final private static DecimalFormat df0 = new DecimalFormat("########");
	final private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	final private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
	final private static SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm:ss");
	final private static  DecimalFormatSymbols dot= new DecimalFormatSymbols();
	protected boolean firstgpsbeepedon=false;
	static IM myIM;
	TextToSpeech tts;
	private int _langTTSavailable = -1;
	Socket s;
	SocketAddress sockaddr;
	String text;
	static SharedPreferences settings;
	int batteryprocent=-1;
	int plugged=-1;
	int temperature=-1;
	int voltage=-1;
	public static List<Channel> channelList = new ArrayList<Channel>();
	public static List<Device> deviceList= new ArrayList<Device>();
	public static Channel currentChannel;
	public static List<Device> currentchanneldeviceList= new ArrayList<Device>();
    public static ArrayList<String> messagelist= new ArrayList<String>();
    public static List<MyMessage> chatmessagelist= new ArrayList<MyMessage>();
    public static Device currentDevice;
    public static DeviceAdapter deviceAdapter;
    public static ChannelsAdapter channelsAdapter;
    public static ChannelsDevicesAdapter channelsDevicesAdapter;
    public static ArrayAdapter<String> channelsmessagesAdapter;
    public static DeviceChatAdapter chatmessagesAdapter;
    static Context serContext;
	protected static boolean uploadto=false;
    final  Handler alertHandler = new Handler() {
			@Override
			public void handleMessage(Message message) {
			Log.d(this.getClass().getName(), "Handle message "+message.toString());
			Bundle b = message.getData();
			Log.d(this.getClass().getName(), "deviceU "+b.getInt("deviceU"));
			if(b.getInt("deviceU") != -1){
				Intent intent =new Intent(LocalService.this, GPSLocalServiceClient.class).putExtra("deviceU", b.getInt("deviceU" ));
				intent.setAction("devicechat");
				PendingIntent contentIntent = PendingIntent.getActivity(serContext,333,intent, PendingIntent.FLAG_CANCEL_CURRENT);
				Long when=System.currentTimeMillis();
			 	NotificationCompat.Builder notificationBuilder =new NotificationCompat.Builder(
						serContext.getApplicationContext())
				    	.setWhen(when)
				    	.setContentText(getString(R.string.message))
				    	.setContentTitle("OsMoDroid")
				    	.setSmallIcon(android.R.drawable.ic_menu_send)
				    	.setAutoCancel(true)
				    	.setDefaults(Notification.DEFAULT_LIGHTS)
				    	.setContentIntent(contentIntent);

		if (!settings.getBoolean("silentnotify", false)){
				 notificationBuilder.setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_VIBRATE| Notification.DEFAULT_SOUND);
				    	}
					Notification notification = notificationBuilder.build();
					LocalService.mNotificationManager.notify(OsMoDroid.mesnotifyid, notification);
			}
			if (LocalService.currentDevice!=null){
				LocalService.mNotificationManager.cancel(OsMoDroid.mesnotifyid);
			}
			String text = b.getString("MessageText");
			if (b.getBoolean("om_online",false)){

			}

			if (text != null && !text.equals("")) {
			Toast.makeText(serContext, text, Toast.LENGTH_SHORT).show();
			LocalService.messagelist.add(0,text);
			Log.d(this.getClass().getName(), "try to save messaglsit");
			saveObject(messagelist, OsMoDroid.NOTIFIESFILENAME);
		    Log.d(this.getClass().getName(), "Success saved messaglsit");
			Log.d(this.getClass().getName(), "List:"+LocalService.messagelist);
			Bundle a=new Bundle();
			a.putStringArrayList("meslist", LocalService.messagelist);
			Intent activ=new Intent(serContext,  GPSLocalServiceClient.class);
			activ.setAction("notif");
			activ.putExtras(a);
			PendingIntent contentIntent = PendingIntent.getActivity(serContext, OsMoDroid.notifyidApp(),activ, 0);
			Long when=System.currentTimeMillis();
			NotificationCompat.Builder notificationBuilder =new NotificationCompat.Builder(
				serContext.getApplicationContext())
		    	.setWhen(when)
		    	.setContentText(text)
		    	.setContentTitle("OsMoDroid")
		    	.setSmallIcon(android.R.drawable.ic_menu_send)
		    	.setAutoCancel(true)
		    	.setDefaults(Notification.DEFAULT_LIGHTS)
		    	.setContentIntent(contentIntent);
			if (!settings.getBoolean("silentnotify", false)){
				notificationBuilder.setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_VIBRATE| Notification.DEFAULT_SOUND);
		    	}
			Notification notification = notificationBuilder.build();
			LocalService.mNotificationManager.notify(OsMoDroid.mesnotifyid, notification);
			if (OsMoDroid.mesactivityVisible) {
	    		try {
				contentIntent.send(serContext, 0, activ);
				LocalService.mNotificationManager.cancel(OsMoDroid.mesnotifyid);
	    		} catch (CanceledException e) {
				Log.d(this.getClass().getName(), "pending intent exception"+e);
				e.printStackTrace();

			}

	    }
	}
}
};

	final RemoteCallbackList<IRemoteOsMoDroidListener> remoteListenerCallBackList = new RemoteCallbackList<IRemoteOsMoDroidListener>();
	private final IRemoteOsMoDroidService.Stub rBinder = new IRemoteOsMoDroidService.Stub() {
			public int getVersion()  {
				return 5;
			}
			public int getBackwardCompatibleVersion()  {
				return 0;
			}
			public void Deactivate(){
				if (state){
		        	alertHandler.post(new Runnable() {
						public void run() {
							stopServiceWork(false);
		                }});
		        }
				return;
			}
			public boolean isActive() {
				return state;
			}
			public void Activate() {
				if (!state){
		        	alertHandler.post(new Runnable() {
						public void run() {
		        	startServiceWork();
		            }});
		        }
			}


			public int getNumberOfLayers()  {
				try {
					return channelList.size();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return 0;
			}



			public int getLayerId(int pos) {
				try {	
					return channelList.get(pos).u;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return 0;
			}

			public String getLayerName(int layerId) {
				try {
					for (Channel channel: channelList){
						if (channel.u==layerId){
							return channel.name;
					}
					}
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			public String getLayerDescription(int layerId)
				{
				try {
					for (Channel channel: channelList){
						if (channel.u==layerId){
							return channel.name;
					}
					}
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}



			public int getNumberOfObjects(int layerId){
				try {
					for (Channel channel: channelList){
						if (channel.u==layerId){
							return channel.deviceList.size();
					}
					}
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				return 0;
			}



			public int getObjectId(int layerId, int pos) {
				try {
					for (Channel channel: channelList){
						if (channel.u==layerId){
							return channel.deviceList.get(pos).u;
					}
					}
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				return 0;
			}
			public float getObjectLat(int layerId, int objectId)
			{
				try {
					for (Channel channel: channelList){
						if (channel.u==layerId){
							for (Device device:channel.deviceList){
								if (device.u==objectId){
									return device.lat;			
								}
							}
					}
					}
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				return 0;
			}



			public float getObjectLon(int layerId, int objectId)
					{
				try {
					for (Channel channel: channelList){
						if (channel.u==layerId){
							for (Device device:channel.deviceList){
								if (device.u==objectId){
									return device.lon;		
								}
							}
							
							
					}
					}									
				} catch (Exception e) {
					e.printStackTrace();
				}
				return 0;
			}



			public String getObjectName(int layerId, int objectId)
					 {
				try {
					for (Channel channel: channelList){
						if (channel.u==layerId){
							for (Device device:channel.deviceList){
								if (device.u==objectId){
									return device.name;		
								}
							}
					}
					}
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}



			public String getObjectDescription(int layerId, int objectId)
					 {
				try {
					for (Channel channel: channelList){
						if (channel.u==layerId){
							for (Device device:channel.deviceList){
								if (device.u==objectId){
									return device.name;		
								}
							}
							
							
					}
					}
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}



			public void registerListener(IRemoteOsMoDroidListener listener)
					throws RemoteException {
				if (listener!=null){
					remoteListenerCallBackList.register(listener);
				}
				
			}



			public void unregisterListener(IRemoteOsMoDroidListener listener)
					throws RemoteException {
				if (listener!=null){
					remoteListenerCallBackList.unregister(listener);
				}
				
			}



			public String getObjectSpeed(int layerId, int objectId)
			 {
					try {
						for (Channel channel: channelList){
							if (channel.u==layerId){
								for (Device device:channel.deviceList){
									if (device.u==objectId){
										return device.speed;			
									}
								}
						}
						}
						
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
			}



			public String getObjectColor(int layerId, int objectId)
					throws RemoteException {
				try {
					for (Channel channel: channelList){
						if (channel.u==layerId){
							for (Device device:channel.deviceList){
								if (device.u==objectId){
									return device.color;			
								}
							}
						}
					}
			} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}



			@Override
			public void refreshChannels() throws RemoteException {
				netutil.newapicommand((ResultsListener)LocalService.this, "om_device_channel_adaptive:"+settings.getString("device", ""));
			}
			@Override
			public int getNumberOfGpx(int layerId) throws RemoteException {
				try {
					for (Channel channel: channelList){
						if (channel.u==layerId){
							return channel.gpxList.size();
					}
					}
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				return 0;
							}



			@Override
			public String getGpxFile(int layerId, int pos)
					throws RemoteException {
				try {
					for (Channel channel: channelList){
						if (channel.u==layerId){
							return channel.gpxList.get(pos).gpxfile.getPath();
					}
					}
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public int getGpxColor(int layerId, int pos) throws RemoteException {
				try {
					for (Channel channel: channelList){
						if (channel.u==layerId){
							return channel.gpxList.get(pos).color;
					}
					}
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				return Color.MAGENTA;
			}

			@Override
			public int getNumberOfPoints(int layerId) throws RemoteException {
				try {
					for (Channel channel: channelList){
						if (channel.u==layerId){
							return channel.pointList.size();
					}
					}
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				return 0;
			}



			@Override
			public int getPointId(int layerId, int pos) throws RemoteException {
				try {
					for (Channel channel: channelList){
						if (channel.u==layerId){
							return channel.pointList.get(pos).u;
					}
					}
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				return 0;
			}



			@Override
			public float getPointLat(int layerId, int pointId)
					throws RemoteException {
				try {
					for (Channel channel: channelList){
						if (channel.u==layerId){
							for (Point point:channel.pointList){
								if (point.u==pointId){
									return point.lat;			
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return 0;
			}



			@Override
			public float getPointLon(int layerId, int pointId)
					throws RemoteException {
				try {
					for (Channel channel: channelList){
						if (channel.u==layerId){
							for (Point point:channel.pointList){
								if (point.u==pointId){
									return point.lon;			
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return 0;
			}



			@Override
			public String getPointName(int layerId, int pointId)
					throws RemoteException {
				try {
					for (Channel channel: channelList){
						if (channel.u==layerId){
							for (Point point:channel.pointList){
								if (point.u==pointId){
									return point.name;			
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}



			@Override
			public String getPointDescription(int layerId, int pointId)
					throws RemoteException {
				try {
					for (Channel channel: channelList){
						if (channel.u==layerId){
							for (Point point:channel.pointList){
								if (point.u==pointId){
									return point.description;			
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}



			@Override
			public String getPointColor(int layerId, int pointId)
					throws RemoteException {
				try {
					for (Channel channel: channelList){
						if (channel.u==layerId){
							for (Point point:channel.pointList){
								if (point.u==pointId){
									return point.color;			
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		
    };
    public SharedPreferences.Editor editor;
    private Float sensivity;
    private int alarmStreamId=0;
    private int count=0;
    private int countFix=0;
	private Notification notification;
	private PendingIntent osmodroidLaunchIntent;
	private String strVersionName;
	private String androidver;
	private ObjectInputStream input;
	     static String formatInterval(final long l)
	    {
	    	return String.format("%02d:%02d:%02d", l/(1000*60*60), (l%(1000*60*60))/(1000*60), ((l%(1000*60*60))%(1000*60))/1000);
	    }

	public class LocalBinder extends Binder {
		LocalService getService() {
		return LocalService.this;

    }

}
	@Override
	 public IBinder onBind(Intent intent) {
		binded=true;
		connectChannels();
		Log.d(getClass().getSimpleName(), "onbind() "+intent.getAction());
		Log.d(getClass().getSimpleName(), "onbind() localservice");
		if (intent.getAction().equals("OsMoDroid.remote"))
			{
			Log.d(getClass().getSimpleName(), "binded remote");
				if (!settings.getString("key", "" ).equals("") )
				{
					netutil.newapicommand((ResultsListener)LocalService.this, "om_device_channel_adaptive:"+settings.getString("device", ""));
				}
				return rBinder;
			}
		else {
		Log.d(getClass().getSimpleName(), "binded localy");
			}
		return mBinder;
    }


public synchronized void informRemoteClientChannelUpdate(){
	final int N = remoteListenerCallBackList.beginBroadcast();
    for (int i=0; i<N; i++) {
        try {
        	remoteListenerCallBackList.getBroadcastItem(i).channelUpdated();
        } catch (RemoteException e) {
            // The RemoteCallbackList will take care of removing
            // the dead object for us.
        }
    }
    remoteListenerCallBackList.finishBroadcast();
    Log.d(getClass().getSimpleName(), "inform client channelUpdated");
}

public synchronized void informRemoteClientChannelsListUpdate(){
	final int N = remoteListenerCallBackList.beginBroadcast();
    for (int i=0; i<N; i++) {
        try {
        	remoteListenerCallBackList.getBroadcastItem(i).channelsListUpdated();
        } catch (RemoteException e) {
            // The RemoteCallbackList will take care of removing
            // the dead object for us.
        }
    }
    remoteListenerCallBackList.finishBroadcast();
    Log.d(getClass().getSimpleName(), "inform client channelsListUpdated");
}

public synchronized void informRemoteClientRouteTo(float Lat, float Lon){
	final int N = remoteListenerCallBackList.beginBroadcast();
    for (int i=0; i<N; i++) {
        try {
        	remoteListenerCallBackList.getBroadcastItem(i).routeTo(Lat, Lon);
        } catch (RemoteException e) {
            // The RemoteCallbackList will take care of removing
            // the dead object for us.
        }
    }
    remoteListenerCallBackList.finishBroadcast();
    Log.d(getClass().getSimpleName(), "inform client routeTo");
}



public void refresh(){

	in.removeExtra("startmessage");
	in.putExtra("position", position+"\n"+satellite+" "+getString(R.string.accuracy)+accuracy);
	in.putExtra("sendresult", sendresult);
	in.putExtra("buffercounter", buffercounter);
	in.putExtra("stat", getString(R.string.maximal)+df1.format(maxspeed*3.6)+getString(R.string.kmch)+getString(R.string.average)+df1.format(avgspeed*3600)+getString(R.string.kmch)+getString(R.string.going)+df2.format(workdistance/1000) + getString(R.string.km)+"\n"+getString(R.string.worktime)+formatInterval(timeperiod));
	in.putExtra("started", state);
	in.putExtra("globalsend", globalsend);
	in.putExtra("signalisationon", signalisationOn);
	in.putExtra("sendcounter", sendcounter);
	in.putExtra("writecounter", writecounter);
	in.putExtra("currentspeed", df1.format(currentspeed*3.6));
	in.putExtra("avgspeed", df1.format(avgspeed*3600));
	in.putExtra("maxspeed", df1.format(maxspeed*3.6));
	in.putExtra("workdistance", df2.format(workdistance/1000));
	in.putExtra("timeperiod", formatInterval(timeperiod));
	sendBroadcast(in);

}



public void startcomand()

{
	String version = getversion();
	APIcomParams params = new APIcomParams("http://a.t.esya.ru/?act=start&hash="+settings.getString("hash", "")+"&n="+settings.getString("n", "")+"&c=OsMoDroid&v="+version.replace(".", "")+"&key="+settings.getString("key", ""),null,"start"); 
	starttask=	new netutil.MyAsyncTask(this);
	starttask.execute(params) ;
	Log.d(getClass().getSimpleName(), "startcommand");

}


private String getversion() {
	androidver = android.os.Build.VERSION.RELEASE;
	strVersionName = getString(R.string.Unknow);
	String version=getString(R.string.Unknow);
	Log.d(getClass().getSimpleName(), "startcommand");
	try {
		PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		strVersionName = packageInfo.packageName + " "+ packageInfo.versionName;
		version = packageInfo.versionName;
		}
	catch (NameNotFoundException e)
	{
		e.printStackTrace();
	}
	return version;
}

public void stopcomand()
{
	if ( starttask!=null)
	{
		starttask.cancel(true);
		starttask.Close();
	}

}

		public String getPosition()  {
			String result = getString(R.string.NotDefined)+"\n"+getString(R.string.speed);
			if (position == null) {return result;}
			else {return position;}

		}

		public  String getSendResult()  {
			  return sendresult;
		}



		public void sendPosition() {
		Location forcelocation = myManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		Location forcenetworklocation = myManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if (forcelocation==null) {
			if (forcenetworklocation==null)
			{

			}

			else
			{
				if (position==null){position = ( df6.format(forcenetworklocation.getLatitude())+", "+df6.format( forcenetworklocation.getLongitude())+"\nСкорость:" +df1.format(forcenetworklocation.getSpeed()*3.6))+" Км/ч";}
				URLadr="http://t.esya.ru/?"+  df6.format( forcenetworklocation.getLatitude()) +":"+ df6.format( forcenetworklocation.getLongitude())+":"+ df1.format(forcenetworklocation.getAccuracy())
					+":"+df1.format( forcenetworklocation.getAltitude())+":"+df1.format( forcenetworklocation.getSpeed())+":"+hash+":"+n;
				Log.d(this.getClass().getName(), URLadr);
				SendCoor forcesend = new SendCoor();
				forcesend.execute(URLadr," ");
			}
		}
		else{
			if (position==null){position = ( "Ш:" + df6.format(forcelocation.getLatitude())+ " Д:"+  df6.format( forcelocation.getLongitude())+" С:" +df1.format(forcelocation.getSpeed()*3.6));}
			URLadr="http://t.esya.ru/?"+  df6.format( forcelocation.getLatitude()) +":"+ df6.format( forcelocation.getLongitude())+":"+ df1.format(forcelocation.getAccuracy())
				+":"+df1.format( forcelocation.getAltitude())+":"+df1.format( forcelocation.getSpeed())+":"+hash+":"+n;
			Log.d(this.getClass().getName(), URLadr);
			SendCoor forcesend = new SendCoor();
			forcesend.execute(URLadr," ");

		}
	}

		public int getSendCounter(){
			return sendcounter;
		}

		void invokeMethod(Method method, Object[] args) {
		    try {
		        method.invoke(this, args);
		    } catch (InvocationTargetException e) {
		        // Should not happen.
		    } catch (IllegalAccessException e) {
		    	// Should not happen.
		    }

		}
	@Override
	public void onCreate() {
		super.onCreate();
		getversion();
		serContext=LocalService.this;
		settings = PreferenceManager.getDefaultSharedPreferences(this);
		editor = settings.edit();
		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		mAccelerometer=mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		if(settings.contains("signalisation")){
			mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
			signalisationOn=true;
			Log.d(this.getClass().getName(), "Enable signalisation after start ");
		}
		myManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		satellite=getString(R.string.Sputniki);
		position=getString(R.string.NotDefined)+ "\n"+getString(R.string.speed);
		sdf1.setTimeZone(TimeZone.getTimeZone("UTC"));
		currentLocation = new Location("");
		prevlocation = new Location("");
		prevlocation_gpx = new Location("");
		prevlocation_spd = new Location("");
		currentLocation.setLatitude((double)settings.getFloat("lat", 0f));
		currentLocation.setLongitude((double)settings.getFloat("lon", 0f));
		dot.setDecimalSeparator('.');
		df1.setDecimalSeparatorAlwaysShown(false);
		df6.setDecimalSeparatorAlwaysShown(false);
		df1.setDecimalFormatSymbols(dot);
		df6.setDecimalFormatSymbols(dot);
		ReadPref();
		deviceAdapter = new DeviceAdapter(getApplicationContext(),R.layout.deviceitem, LocalService.deviceList);
		channelsAdapter = new ChannelsAdapter(getApplicationContext(),R.layout.deviceitem, LocalService.channelList, this);
		String alarm = Context.ALARM_SERVICE;
		am = ( AlarmManager ) getSystemService( alarm );
		Intent intent = new Intent( "CHECK_GPS" );
		pi = PendingIntent.getBroadcast( this, 0, intent, 0 );
		batteryReciever = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				int rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
				int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
				plugged=intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
				temperature=intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
				voltage=intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
				int level = -1;
				if (rawlevel >= 0 && scale > 0) {
					level = (rawlevel * 100) / scale;
				}
				batteryprocent=level;
			}

		};
		registerReceiver(batteryReciever, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		checkreceiver =new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent)
			{
			if ( System.currentTimeMillis()>lastgpsontime+notifyperiod && gpsbeepedon)
				{
				if(vibrate)vibrator.vibrate(vibratetime);
				if (playsound){soundPool.play(gpson, 1f, 1f, 1, 0, 1f);}
				} else
					{gpsbeepedon=false;}

			if ( System.currentTimeMillis()>lastgpsofftime+notifyperiod &&gpsbeepedoff)
			{
				if(vibrate)vibrator.vibrate(vibratetime);
				if (playsound){soundPool.play(gpsoff, 1f, 1f, 1, 0, 1f);}
			} else 
				{gpsbeepedoff=false;}
			}
		};

		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				boolean gpxfix = false;
				gpxfix=	intent.getBooleanExtra("enabled", false);
				if(gpxfix)
					{
					lastgpsontime=System.currentTimeMillis();
					gpsbeepedon=true;
					if (playsound&&!firstgpsbeepedon)
						{
						firstgpsbeepedon=true;
						soundPool.play(gpson, 1f, 1f, 1, 0, 1f);
						}
					}
				else 
					{
					lastgpsofftime=System.currentTimeMillis();
					gpsbeepedoff=true;
					}

			}
		};
		soundPool = new SoundPool(10, AudioManager.STREAM_NOTIFICATION, 0);
		gpson = soundPool.load(this, R.raw.gpson, 1);
		gpsoff = soundPool.load(this, R.raw.gpsoff, 1);
		ineton = soundPool.load(this, R.raw.ineton, 1);
		inetoff = soundPool.load(this, R.raw.inetoff, 1);
		sendpalyer = soundPool.load(this, R.raw.sendsound, 1);
		startsound = soundPool.load(this, R.raw.start, 1);
		stopsound = soundPool.load(this, R.raw.stop, 1);
		alarmsound = soundPool.load(this, R.raw.signal, 1);
		signalonoff = soundPool.load(this, R.raw.signalonoff, 1);
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		in = new Intent("OsMoDroid");
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
///////////////////////
s = new Socket( );
//sockaddr = new InetSocketAddress("esya.ru", 2145);
///////////////////////
if (live&&!settings.getString("hash", "" ).equals(""))
{
	if (isOnline())
		{
			if (settings.getLong("laststartcommandtime", 0)<System.currentTimeMillis()-14400000)
			{
				startcomand();
			}	
	
			if (!settings.getString("key", "" ).equals("") )
			{
				String om_device_get = settings.getString("om_device_get", "");
				String om_device = settings.getString("om_device", "");
				String om_device_channel_adaptive = settings.getString("om_device_channel_adaptive", "");
				
				if(settings.getBoolean("ondestroy", false))
				{
					Log.d(this.getClass().getName(), "oncreate was after ondestroy ");
					netutil.newapicommand((ResultsListener)LocalService.this, "om_device_get:"+settings.getString("device", ""));
					netutil.newapicommand((ResultsListener)LocalService.this, "om_device");
					netutil.newapicommand((ResultsListener)LocalService.this, "om_device_channel_adaptive:"+settings.getString("device", ""));
				}
				else
				{
					Log.d(this.getClass().getName(), "oncreate was not after ondestroy ");
					if (om_device_get.equals(""))
					{
						netutil.newapicommand((ResultsListener)LocalService.this, "om_device_get:"+settings.getString("device", ""));
					} else
					{
						globalsend=settings.getBoolean("globalsend", false);
					}
					if (om_device.equals(""))
					{
						netutil.newapicommand((ResultsListener)LocalService.this, "om_device");
					} else
					{
						List<Device> loaded=(List<Device>) loadObject(OsMoDroid.DEVLIST, deviceList.getClass());
						if (loaded!=null)
						{
							deviceList.addAll(loaded);
						}

					}
					if (om_device_channel_adaptive.equals(""))
					{
						netutil.newapicommand((ResultsListener)LocalService.this, "om_device_channel_adaptive:"+settings.getString("device", ""));
					} else
					{
						try {
							channelListFromJSONArray(new JSONArray(om_device_channel_adaptive));
						} catch (JSONException e) {
							netutil.newapicommand((ResultsListener)LocalService.this, "om_device_channel_adaptive:"+settings.getString("device", ""));
							e.printStackTrace();
						}
					}
					
				}
			}
		}

	else 
		{
		 onlinePauseforStartReciever = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
			Log.d(this.getClass().getName(), "OnlinePauseforStartReciever"+this);
			Log.d(this.getClass().getName(), "OnlinePauseforStartReciever"+this+" Intent:"+intent);
			if (intent.getAction().equals(android.net.ConnectivityManager.CONNECTIVITY_ACTION))
			{
				Bundle extras = intent.getExtras();
				Log.d(this.getClass().getName(), "OnlinePauseforStartReciever"+this+ " "+intent.getExtras());
				if(extras.containsKey("networkInfo")) 
					{
						NetworkInfo netinfo = (NetworkInfo) extras.get("networkInfo");
						Log.d(this.getClass().getName(), "OnlinePauseforStartReciever"+this+ " "+netinfo);
						Log.d(this.getClass().getName(), "OnlinePauseforStartReciever"+this+ " "+netinfo.getType());
						if(netinfo.isConnected()) 
							{
								Log.d(this.getClass().getName(), "OnlinePauseforStartReciever"+this+" Network is connected");
							if (settings.getLong("laststartcommandtime", 0)<System.currentTimeMillis()-14000000)
								{
									startcomand();
								}
							if (!settings.getString("key", "" ).equals("") )
							{
								String om_device_get = settings.getString("om_device_get", "");
								String om_device = settings.getString("om_device", "");
								String om_device_channel_adaptive = settings.getString("om_device_channel_adaptive", "");
								
								if(settings.getBoolean("ondestroy", false))
								{
									netutil.newapicommand((ResultsListener)LocalService.this, "om_device_get:"+settings.getString("device", ""));
									netutil.newapicommand((ResultsListener)LocalService.this, "om_device");
									netutil.newapicommand((ResultsListener)LocalService.this, "om_device_channel_adaptive:"+settings.getString("device", ""));
								}
								else
								{
									if (om_device_get.equals(""))
									{
										netutil.newapicommand((ResultsListener)LocalService.this, "om_device_get:"+settings.getString("device", ""));
									} else
									{
										globalsend=settings.getBoolean("globalsend", false);
									}
									if (om_device.equals(""))
									{
										netutil.newapicommand((ResultsListener)LocalService.this, "om_device");
									} else
									{
										List loaded=(List<Device>) loadObject(OsMoDroid.DEVLIST, deviceList.getClass());
										if (loaded!=null)
										{
											deviceList.addAll(loaded);
										}

									}
									if (om_device_channel_adaptive.equals(""))
									{
										netutil.newapicommand((ResultsListener)LocalService.this, "om_device_channel_adaptive:"+settings.getString("device", ""));
									} else
									{
										try {
											channelListFromJSONArray(new JSONArray(om_device_channel_adaptive));
										} catch (JSONException e) {
											netutil.newapicommand((ResultsListener)LocalService.this, "om_device_channel_adaptive:"+settings.getString("device", ""));
											e.printStackTrace();
										}
									}
									
								}
							}
							unregisterReceiver(onlinePauseforStartReciever);
						}
						else {
							System.out.println("OnlinePauseforStartReciever offline"+this.toString());
						}
					}
					else if(extras.containsKey("noConnectivity")) 
						{
							System.out.println("OnlinePauseforStartReciever offline2"+this.toString());
						}
			    }
			}
		};
		registerReceiver(onlinePauseforStartReciever, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
	}
}
if (settings.getBoolean("im", false) && !settings.getString("key", "" ).equals("") ){
	Log.d(this.getClass().getName(), "try load longPollchannels");
	ArrayList<String[]>  entries =  (ArrayList<String[]>)loadObject(OsMoDroid.FILENAME, new ArrayList<String[]>().getClass());
	
	
	//Log.d(this.getClass().getName(), "entries="+entries);
	if(entries==null){
			{
				ArrayList<String[]> longPollchannels =new ArrayList<String[]>();
				longPollchannels.add(new String[] {"om_online","o",""}); 
				if(!settings.getString("lpch", "").equals(""))
				{ 
				longPollchannels.add(new String[] {"ctrl_"+settings.getString("lpch", ""),"r",""});
				longPollchannels.add(new String[] {settings.getString("lpch", "")+"_chat","m",""});
				}
				myIM = new IM( longPollchannels ,this,settings.getString("key", ""), this);
			}
		} else
		{
			Log.d(this.getClass().getName(), "start IM with saves longpollchannels");
			myIM = new IM( entries ,this,settings.getString("key", ""), this);
		}
		try {
		Log.d(this.getClass().getName(), "try load notifications");
		input = new ObjectInputStream(openFileInput(OsMoDroid.NOTIFIESFILENAME));
		LocalService.messagelist.addAll((ArrayList<String>)input.readObject());
		Log.d(this.getClass().getName(), "success load notification");
		}
		catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
}		
if (settings.getBoolean("started", false)){
	startServiceWork();
}
settings.edit().putBoolean("ondestroy", false).commit();
	}

	void Pong(Context context) throws JSONException{
            netutil.newapicommand((ResultsListener)context, "om_device_pong:"+settings.getString("device", "")+","+Long.toString(System.currentTimeMillis()));
	}

	void batteryinfo(Context context) throws JSONException{
            JSONObject postjson = new JSONObject();
            postjson.put("batteryprocent", batteryprocent);
            postjson.put("temperature", temperature);
            postjson.put("voltage", voltage);
            postjson.put("plugged", plugged);
            netutil.newapicommand((ResultsListener)context, "om_device_pong:"+settings.getString("device", "")+","+Long.toString(System.currentTimeMillis()), "json="+postjson.toString());
	}
	private String capitalize(String s) {
		  if (s == null || s.length() == 0) {
		    return "";
		  }
		  char first = s.charAt(0);
		  if (Character.isUpperCase(first)) {
		    return s;
		  } else {
		    return Character.toUpperCase(first) + s.substring(1);
		  }
		} 
	public String getDeviceName() {
		  String manufacturer = Build.MANUFACTURER;
		  String model = Build.MODEL;
		  if (model.startsWith(manufacturer)) {
		    return capitalize(model);
		  } else {
		    return capitalize(manufacturer) + " " + model;
		  }
		}
	void systeminfo(Context context) throws JSONException{
		WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		int width = display.getWidth();  // deprecated
		int height = display.getHeight();  // deprecated
		JSONObject postjson = new JSONObject();
        postjson.put("version", strVersionName);
        postjson.put("androidversion", androidver);
        postjson.put("devicename", getDeviceName());
        postjson.put("display", Integer.toString(width)+"x"+Integer.toString(height));
        netutil.newapicommand((ResultsListener)context, "om_device_pong:"+settings.getString("device", "")+","+Long.toString(System.currentTimeMillis()), "json="+postjson.toString());
}
	
	void vibrate (Context context,long milliseconds) {
		vibrator.vibrate(milliseconds);
	}
	
	
	void satelliteinfo(Context context) throws JSONException{
        JSONObject postjson = new JSONObject();
        postjson.put("view", count);
        postjson.put("active", countFix);
        postjson.put("accuracy", accuracy);
        netutil.newapicommand((ResultsListener)context, "om_device_pong:"+settings.getString("device", "")+","+Long.toString(System.currentTimeMillis()), "json="+postjson.toString());
}
	
	void getpreferences(Context context) throws JSONException{
        JSONObject postjson = new JSONObject();
        postjson.put("speed",speed);
        postjson.put("period",period);
        postjson.put("distance",distance);
        postjson.put("hash",hash);
        postjson.put("n",n);
        postjson.put("speedbearing",speedbearing);
        postjson.put("bearing",bearing); 
        postjson.put("hdop",hdop); 
        postjson.put("gpx",gpx); 
        postjson.put("live",live);
        postjson.put("vibrate",vibrate);
        postjson.put("usecourse",usecourse);
        postjson.put("vibratetime",vibratetime);
        postjson.put("playsound",playsound);
        postjson.put("period_gpx",period_gpx);
        postjson.put("distance_gpx",distance_gpx);
        postjson.put("speedbearing_gpx",speedbearing_gpx);
        postjson.put("bearing_gpx",bearing_gpx);
        postjson.put("hdop_gpx",hdop_gpx); 
        postjson.put("speed_gpx",speed_gpx);
        postjson.put("usebuffer",usebuffer);
        postjson.put("usewake",usewake); 
        postjson.put("notifyperiod",notifyperiod); 
        postjson.put("sendsound",sendsound); 
        netutil.newapicommand((ResultsListener)context, "om_device_pong:"+settings.getString("device", "")+","+Long.toString(System.currentTimeMillis()), "json="+postjson.toString());
}

	void wifion(Context context) {
		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		wifi.setWifiEnabled(true);
        netutil.newapicommand((ResultsListener)context, "om_device_pong:"+settings.getString("device", "")+","+Long.toString(System.currentTimeMillis()));
	}
	void wifioff(Context context) {
		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		wifi.setWifiEnabled(false);
        netutil.newapicommand((ResultsListener)context, "om_device_pong:"+settings.getString("device", "")+","+Long.toString(System.currentTimeMillis()));
	}
	
	void wifiinfo(Context context) throws JSONException {
		 JSONObject postjson = new JSONObject();
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (mWifi.isConnected()) {
			WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInfo = wifi.getConnectionInfo();
			String wifiname = wifiInfo.getSSID();
			String mac = wifiInfo.getMacAddress();
			String strength = Integer.toString(wifiInfo.getRssi());
			  postjson.put("ssid", wifiname);
		         postjson.put("mac", mac);
		         postjson.put("strength", strength);
		}
		else
		{
			postjson.put("state", "noconnect");
		}
	         netutil.newapicommand((ResultsListener)context, "om_device_pong:"+settings.getString("device", "")+","+Long.toString(System.currentTimeMillis()), "json="+postjson.toString());
	}
	
	

	@Override

	public void onDestroy() {
		super.onDestroy();
		mSensorManager.unregisterListener(this);
		Log.d(this.getClass().getName(), "Disable signalisation after destroy");
		if (state){ stopServiceWork(false);}
		if(myIM!=null){  myIM.close();}
		deleteFile(OsMoDroid.FILENAME);
		deleteFile(OsMoDroid.NOTIFIESFILENAME);
		deleteFile(OsMoDroid.DEVLIST);
		stopcomand();
		try {
		if(receiver!= null){unregisterReceiver(receiver);}
		} catch (Exception e) {
			Log.d(getClass().getSimpleName(), "А он и не зареген");
		}
		try {
		if(checkreceiver!= null){unregisterReceiver(checkreceiver);}
		} catch (Exception e) 
			{
			Log.d(getClass().getSimpleName(), "А он и не зареген");
			}

		try {
			if(onlinePauseforStartReciever!= null){unregisterReceiver(onlinePauseforStartReciever);}
		} catch (Exception e) {
			Log.d(getClass().getSimpleName(), "А он и не зареген");
		}
		try {
			if(batteryReciever!= null){unregisterReceiver(batteryReciever);}
		} catch (Exception e) {
			Log.d(getClass().getSimpleName(), "А он и не зареген");
		}
		if (soundPool!=null) {soundPool.release();}
		if (!(wakeLock==null) &&wakeLock.isHeld())wakeLock.release();
		if (!(LocwakeLock==null)&&LocwakeLock.isHeld())LocwakeLock.release();
		if (!(SendwakeLock==null)&&SendwakeLock.isHeld())SendwakeLock.release();
		mNotificationManager.cancelAll();
		remoteListenerCallBackList.kill();
		settings.edit().remove("globalsend").commit();
		settings.edit().putBoolean("ondestroy", true).commit();

	}



	private void ReadPref() {

			Log.d(getClass().getSimpleName(), "readpref() localserv");



			speed =  Integer.parseInt(settings.getString("speed", "3").equals(

					"") ? "3" : settings.getString("speed", "3"));

			period = Integer.parseInt(settings.getString("period", "10000").equals("") ? "10000" : settings.getString("period", "10000") );

			distance = Integer.parseInt(settings.getString("distance", "50").equals("") ? "50" :settings.getString("distance","50"));

			hash = settings.getString("hash", "");

			n = Integer.parseInt(settings.getString("n", "0").equals("") ? "0" :settings.getString("n","0"));



			speedbearing = Integer

					.parseInt(settings.getString("speedbearing", "2").equals("")? "2" :settings.getString("speedbearing","2"));

			bearing = Integer.parseInt(settings.getString("bearing", "10").equals("") ? "10" :settings.getString("bearing","2"));

			hdop = Integer.parseInt(settings.getString("hdop", "30").equals("") ? "30" :settings.getString("hdop","30"));

			gpx = settings.getBoolean("gpx", false);

			live = settings.getBoolean("live", true);

			vibrate = settings.getBoolean("vibrate", false);

			usecourse = settings.getBoolean("usecourse", false);

			vibratetime = Integer.parseInt(settings.getString("vibratetime", "200").equals("") ? "200" :settings.getString("vibratetime","0"));

			playsound = settings.getBoolean("playsound", false);

			period_gpx = Integer.parseInt(settings.getString("period_gpx", "0").equals("") ? "0" :settings.getString("period_gpx","0"));

			distance_gpx = Integer.parseInt(settings.getString("distance_gpx", "0").equals("") ? "0" :settings.getString("distance_gpx","0"));

			speedbearing_gpx = Integer

					.parseInt(settings.getString("speedbearing_gpx", "0").equals("")? "0" :settings.getString("speedbearing_gpx","0"));

			bearing_gpx = Integer.parseInt(settings.getString("bearing_gpx", "0").equals("") ? "0" :settings.getString("bearing","0"));

			hdop_gpx = Integer.parseInt(settings.getString("hdop_gpx", "30").equals("") ? "30" :settings.getString("hdop_gpx","30"));

			speed_gpx =  Integer.parseInt(settings.getString("speed_gpx", "3").equals(

					"") ? "3" : settings.getString("speed_gpx", "3"));

			usebuffer = settings.getBoolean("usebuffer", false);

			usewake = settings.getBoolean("usewake", false);

			notifyperiod = Integer.parseInt(settings.getString("notifyperiod", "30000").equals("") ? "30000" :settings.getString("notifyperiod","30000"));

			sendsound = settings.getBoolean("sendsound", false);

			//pass = settings.getString("pass", "");

			Log.d(getClass().getSimpleName(), "localserv hash:"+hash);

		}





	@Override

	public void onStart(Intent intent, int startId) {
		Log.d(getClass().getSimpleName(), "on start ");
		
		super.onStart(intent, startId);





	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(getClass().getSimpleName(), "on startcommand");
		
		 return START_STICKY;
	}


	public void applyPreference(){
		if (state) {
		myManager.removeUpdates(this);
		ReadPref();
		ttsManage();
		manageGPSFixAlarm();
		
		if ( gpx && !fileheaderok) {
			openGPX();
		}
		if (!gpx && fileheaderok) {
			closeGPX();
		}
		requestLocationUpdates();
		}
		manageIM();
		Log.d(getClass().getSimpleName(), "applyPreferecne end");

	}
	

	public void startServiceWork (){

		firstsend=true;

		avgspeed=0;

		maxspeed=0;

		workdistance=0;

		timeperiod=0;

		workmilli=0;

		buffercounter=0;

		 buffersb.setLength(0);

		 lastbuffersb.setLength(0);

		 sendedsb.setLength(0);

		 lcounter=0;

		 scounter=0;

		 sendcounter=0;

		 sended=true;



		ReadPref();

		//sendbuffer="";

		if (settings.getBoolean("sendsound", false)){
			 soundPool.play(startsound, 1f, 1f, 1, 0, 1f);
		}

		ttsManage();

		manageGPSFixAlarm();

		sendcounter=0;

		boolean crtfile = false;

		if (gpx) {

			openGPX();



			}

requestLocationUpdates();



int icon = R.drawable.eye;
CharSequence tickerText = getString(R.string.monitoringstarted); //getString(R.string.Working);
long when = System.currentTimeMillis();
notification = new Notification(icon, tickerText, when);
Intent notificationIntent = new Intent(this, GPSLocalServiceClient.class);
notificationIntent.setAction(Intent.ACTION_MAIN);
notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
osmodroidLaunchIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
notification.setLatestEventInfo(getApplicationContext(), "OsMoDroid", getString(R.string.monitoringactive), osmodroidLaunchIntent);



mStartForegroundArgs[0]= OSMODROID_ID;

mStartForegroundArgs[1]= notification;

Method mStartForeground;

final Class<?>[] mStartForegroundSignature = new Class[] {

	    int.class, Notification.class};

try {

		mStartForeground = getClass().getMethod("startForeground",

                mStartForegroundSignature);

			    } catch (Exception e) {

      mStartForeground =  null;

    }

if (mStartForeground == null){

//setForeground(true);

mNotificationManager.notify(OSMODROID_ID, notification);



} else

{

invokeMethod(mStartForeground, mStartForegroundArgs);

}







setstarted(true);

if (live){

//String[] params = {"http://a.t.esya.ru/?act=session_start&hash="+settings.getString("hash", "")+"&n="+settings.getString("n", "")+"&ttl="+settings.getString("session_ttl", "30"),"false","","session_start"};
APIcomParams params = new APIcomParams("http://a.t.esya.ru/?act=session_start&hash="+settings.getString("hash", "")+"&n="+settings.getString("n", "")+"&ttl="+settings.getString("session_ttl", "30"),null,"session_start"); 
new netutil.MyAsyncTask(this).execute(params);}

		Log.d(getClass().getSimpleName(), "notify:"+notification.toString());





	}







	private void manageGPSFixAlarm() {
		int type = AlarmManager.ELAPSED_REALTIME_WAKEUP;



		long triggerTime = SystemClock.elapsedRealtime() + notifyperiod;

		if (playsound||vibrate){am.setRepeating( type, triggerTime, notifyperiod, pi );} else {am.cancel(pi);}

		registerReceiver( receiver, new IntentFilter( "android.location.GPS_FIX_CHANGE"));

		registerReceiver( checkreceiver, new IntentFilter( "CHECK_GPS"));
	}

private void manageIM(){
	if (settings.getBoolean("im", false) && myIM==null&& !settings.getString("key", "" ).equals("") ){

		
			ArrayList<String[]> longPollchannels =new ArrayList<String[]>();
			longPollchannels.add(new String[] {"online","o",""}); 
			if(!settings.getString("lpch", "").equals(""))
			{ 
			longPollchannels.add(new String[] {"ctrl_"+settings.getString("lpch", ""),"r",""});
			longPollchannels.add(new String[] {settings.getString("lpch", "")+"_chat","m",""});
			}
			myIM = new IM( longPollchannels ,this,settings.getString("key", ""), this);	
			Log.d(getClass().getSimpleName(), "om_device_channel_adaptive from manageim");
			netutil.newapicommand((ResultsListener)LocalService.this, "om_device_channel_adaptive:"+settings.getString("device", ""));
		}
	if (!settings.getBoolean("im", false) && myIM!=null){
		myIM.close();
		myIM=null;
	}
		
		
	
}





	private void ttsManage() {
		if (settings.getBoolean("usetts", false) && tts==null){ tts = new TextToSpeech(this,

		        (OnInitListener) this  // TextToSpeech.OnInitListener

		        );} 
		if (!settings.getBoolean("usetts", false) && tts!=null)
		{
		        	

		                tts.stop();

		                tts.shutdown();
		                tts=null;
		                

		            
		        }
	}







	public void requestLocationUpdates() {
		if (usecourse)	{
		
			//Log.d(this.getClass().getName(), "Запускаем провайдера 0 0");
		
		
		
				
				
				
		
		
		
				myManager.removeUpdates(LocalService.this);
		
			if (settings.getBoolean("usegps", true))	{myManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, LocalService.this);
			myManager.addGpsStatusListener(LocalService.this);}
		
			if (settings.getBoolean("usenetwork", true)){	myManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, LocalService.this);}
		
			
		
				}
		
		else	{
		
			//Log.d(this.getClass().getName(), "Запускаем провайдера по настройкам");
		
		
		
			
		
		
		
			//myManager.removeUpdates(this);
		
		
		
			if (period>=period_gpx&&gpx){gpsperiod=period_gpx;}else {gpsperiod=period;};
		
			if (distance>=distance_gpx&&gpx){gpsdistance=distance_gpx;}else {gpsdistance=distance;};
		
			//Log.d(this.getClass().getName(), "период"+gpsperiod+"meters"+gpsdistance);
			List<String> list = myManager.getAllProviders();
			if (settings.getBoolean("usegps", true))
			{
				if(list.contains(LocationManager.GPS_PROVIDER))
				{
			myManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, gpsperiod, 0, LocalService.this);
			myManager.addGpsStatusListener(LocalService.this);
				}
			}
		
			if (settings.getBoolean("usenetwork", true)){	
				
				if(list.contains(LocationManager.NETWORK_PROVIDER))
				{
				myManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, gpsperiod, gpsdistance, LocalService.this);
				}
				}
		
			
		
		}
	}







	/**
	 * 
	 */
	private void openGPX() {
		boolean crtfile;
		String sdState = android.os.Environment.getExternalStorageState();

		if (sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {

		 File sdDir = android.os.Environment.getExternalStorageDirectory();

		// SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

		 String time = sdf2.format(new Date());

		 fileName = new File (sdDir, "OsMoDroid/");

		 fileName.mkdirs();

		 fileName = new File(sdDir, "OsMoDroid/"+time+".gpx");

		 }

		if (!fileName.exists())

		{

			try {

				crtfile= fileName.createNewFile();

			} catch (IOException e) {

				e.printStackTrace();

			}

			//Log.d(getClass().getSimpleName(), Boolean.toString(crtfile));

		}

		try {
		                // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ssZ");

		                String time = sdf1.format(new Date(System.currentTimeMillis()))+"Z";

		                FileWriter trackwr = new FileWriter(fileName);
		                trackwr.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		                trackwr.write("<gpx xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" version=\"1.1\" xmlns=\"http://www.topografix.com/GPX/1/1\" creator=\"OsMoDroid\" xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\">");
		                trackwr.write("<time>" +time + "</time>");
		                trackwr.write("<trk>");
		                trackwr.write("<name>" + time + "</name>");
		                trackwr.write("<trkseg>");
		                trackwr.flush();
		                trackwr.close();

		                fileheaderok=true;
		} catch (Exception e) {
		                //e.printStackTrace();
		                Toast.makeText(LocalService.this, getString(R.string.CanNotWriteHeader), Toast.LENGTH_SHORT).show();
		}
	}









	public void stopServiceWork(Boolean stopsession){

		

        editor.putFloat("lat", (float) currentLocation.getLatitude());
        editor.putFloat("lon", (float) currentLocation.getLongitude());

        editor.commit();
        firstgpsbeepedon=false;
        if (tts != null) {

            tts.stop();

            tts.shutdown();

        }
        
        
		if (settings.getBoolean("sendsound", false)){
			 soundPool.play(stopsound, 1f, 1f, 1, 0, 1f);
		}
		
		am.cancel(pi);

		if (live&&stopsession){
                    //String[] params = {"http://a.t.esya.ru/?act=session_stop&hash="+settings.getString("hash", "")+"&n="+settings.getString("n", ""),"false","","session_stop"};
                    APIcomParams params = new APIcomParams("http://a.t.esya.ru/?act=session_stop&hash="+settings.getString("hash", "")+"&n="+settings.getString("n", "")+"&ttl="+settings.getString("session_ttl", "30"),null,"session_stop");
                    new netutil.MyAsyncTask(this).execute(params);

                
		}

		try {

			s.close();

		} catch (IOException e1) {

			// TODO Auto-generated catch block

			e1.printStackTrace();

		}

		if (gpx&&fileheaderok) {

			closeGPX();

			}



			if (send != null ) {

				Log.d(this.getClass().getName(), "Отменяем asynctask передачи send.");
				Log.d(this.getClass().getName(), "send.status="+send.getStatus().toString());

				send.cancel(true);

		}

			if (myManager!=null){

			myManager.removeUpdates(this);}

			setstarted(false);



//			int icon = R.drawable.eye;

//			CharSequence tickerText ="Ждущий режим"; //getString(R.string.Working);

//			long when = System.currentTimeMillis();

//			Notification notification = new Notification(icon, tickerText, when);

//			Intent notificationIntent = new Intent(this, GPSLocalServiceClient.class);

//			notificationIntent.setAction(Intent.ACTION_MAIN);

//			notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);

//			PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

//			notification.setLatestEventInfo(getApplicationContext(), "OsMoDroid", "", contentIntent);

//			mNotificationManager.notify(OSMODROID_ID, notification);





			mStopForegroundArgs[0]= Boolean.TRUE;

			Method mStopForeground;

			final Class<?>[] mStopForegroundSignature = new Class[] {boolean.class};

			try {

					mStopForeground = getClass().getMethod("stopForeground",

			                mStopForegroundSignature);

						    } catch (Exception e) {

			      mStopForeground =  null;

			    }

			if (mStopForeground == null){

			mNotificationManager.cancel(OSMODROID_ID);

			//setForeground(false);



			} else

			{

				mNotificationManager.cancel(OSMODROID_ID);

				invokeMethod(mStopForeground, mStopForegroundArgs);

			}





		//	mNotificationManager.cancel(OSMODROID_ID);



	}







	/**
	 * 
	 */
	private void closeGPX() {
		try {

		 FileWriter trackwr = new FileWriter(fileName, true);

		 String towright = gpxbuffer;

			trackwr.write(towright.replace(",", "."));

			gpxbuffer="";



		 trackwr.write("</trkseg></trk></gpx>");

		 trackwr.flush();

		 trackwr.close();

		 fileheaderok=false;

		} catch (Exception e) {

			e.printStackTrace();

			Toast.makeText(LocalService.this, getString(R.string.CanNotWriteEnd), Toast.LENGTH_SHORT).show();

		}
		 if (fileName.length()>1024&&uploadto){
             upload(fileName);        
             }
             if (fileName.length()<1024)
             {fileName.delete();
		
		Toast.makeText(LocalService.this, R.string.tracktoshort , Toast.LENGTH_LONG).show();
		}
		
	}


	public void upload (File file){
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(), 0);
		
		NotificationCompat.Builder notificationBuilder =new NotificationCompat.Builder(

				serContext.getApplicationContext())

		    	.setWhen(System.currentTimeMillis())

		    	.setContentText(file.getName())

		    	.setContentTitle(getString(R.string.osmodroiduploadfile))

		    	.setSmallIcon(android.R.drawable.arrow_up_float)

		    	.setAutoCancel(true)
		    	.setContentIntent(contentIntent)
		    	.setProgress(100, 0, false);
		    	;


			Notification notification = notificationBuilder.build();
			int uploadid = OsMoDroid.uploadnotifyid();

			LocalService.mNotificationManager.notify(uploadid, notification);
		
		
		netutil.newapicommand((ResultsListener)LocalService.this,  "tr_track_upload:1", file,notificationBuilder,uploadid);
	}


	private void setstarted(boolean started){

		//Log.d(getClass().getSimpleName(), "setstarted() localservice");

		editor.putBoolean("started", started);

        editor.commit();

        state=started;

        refresh();

	}

	public class SendCoor extends AsyncTask<String, String, String> {





		private  String tmp;
		
		

		protected void onPostExecute(String result) {

			Log.d(getClass().getSimpleName(), "SendCoorOnPostExecute, Result=" + result);

			if (result.equals("NoConnection")) {

				sendresult = getString(R.string.NoConnection);

				internetnotify(true);

			}

			else {

				String time = sdf3.format(new Date(System.currentTimeMillis()));

				sendresult = decodesendresult(result);

				if (sended) {

					sendcounter = sendcounter + 1;

					if (sendsound && !mayak) {
						soundPool.play(sendpalyer, 1f, 1f, 1, 0, 1f);
						mayak = false;
					}

					sendresult = time + " " + sendresult;
				}

				else

				{

					sendresult = time + " " + sendresult;

				}

			}
			//notification.tickerText=sendresult;
			if (notification!=null){
			notification.setLatestEventInfo(getApplicationContext(), "OsMoDroid", "Отправлено:"+sendcounter+" Записано:"+writecounter, osmodroidLaunchIntent);
			mNotificationManager.notify(OSMODROID_ID, notification);
			}
			refresh();

		}

		protected String doInBackground(String... arg0) {

			Log.d(getClass().getSimpleName(), "SendCoorOnPostExecute, doInBackground begin");
			try {

			//	 SendwakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SendWakeLock");

			//	SendwakeLock.acquire();

				Log.d(this.getClass().getName(), "Начинаем отправку.");

				tmp=getPage(arg0[0], arg0[1]);

				Log.d(this.getClass().getName(), "Отправка окончена.");

			} catch (IOException e) {

				internetnotify(false);

			//	e.printStackTrace();

				sended=false;

				tmp="NoConnection";

				Log.d(this.getClass().getName(), "Exception. NoConnection"+e.toString());

			}

//		    try {

//

//	            if (!s.isConnected()){ s.connect(sockaddr, 10000); s.setSoTimeout(5000);}

//

//	            PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(s.getOutputStream())),true);

//

//				 out.println("arg0");







//			//	  text = inputStreamToString(s.getInputStream());

//			//	  Log.d(this.getClass().getName(),"text:"+ text);

//

//

//	        }

//

//

//		    catch (IOException e) {

//	        	 Log.d(this.getClass().getName(),"Exeption socket:"+ e.toString());

//	        	e.printStackTrace();

//	        }



			return tmp;

		}

	}



	public void onLocationChanged(Location location) {

		if (!state){
			myManager.removeUpdates(this);
		}
		
		currentLocation.set(location);
		
		if (LocalService.channelsDevicesAdapter!=null&&LocalService.currentChannel!=null)
		{
			 Log.d(this.getClass().getName(), "Adapter:"+ LocalService.channelsDevicesAdapter.toString());
			
			 for (Channel channel:LocalService.channelList){
					if(channel.u==LocalService.currentChannel.u){
						LocalService.currentchanneldeviceList.clear();
						LocalService.currentchanneldeviceList.addAll(channel.deviceList);
				 }
					
				}
			 
			 
			 LocalService.channelsDevicesAdapter.notifyDataSetChanged();
		}
		
		accuracy=Float.toString(location.getAccuracy());

		if (System.currentTimeMillis()<lastgpslocationtime+gpsperiod+30000 && location.getProvider().equals(LocationManager.NETWORK_PROVIDER))

		{

			Log.d(this.getClass().getName(),"У нас есть GPS еще");

			return;



		}

		//LocwakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "LocWakeLock");

		//	LocwakeLock.acquire();



			//if (prevlocation_gpx==null)prevlocation_gpx.set(location);

			//if (prevlocation==null)prevlocation=location;

			//if (prevlocation_spd==null&&location.getProvider().equals(LocationManager.GPS_PROVIDER))prevlocation_spd=location;



		//Toast.makeText(getApplicationContext(), location.getProvider(), 1).show();



		if (System.currentTimeMillis()>lastgpslocationtime+gpsperiod+30000 && location.getProvider().equals(LocationManager.NETWORK_PROVIDER))

		{

			Log.d(this.getClass().getName(),"У нас уже нет GPS");

			if ((location.distanceTo(prevlocation)>distance && System.currentTimeMillis()>(prevnetworklocationtime+period)))

			{

				prevnetworklocationtime=System.currentTimeMillis();

				sendlocation(location);

				return;

			}



		}









		if  (firstsend)

		{

sendlocation(location);
prevlocation.set(location);
prevlocation_gpx.set(location);
prevlocation_spd.set(location);

prevbrng=brng;

workmilli= System.currentTimeMillis();

		//Log.d(this.getClass().getName(),"workmilli="+ Float.toString(workmilli));

		firstsend=false;

		}
		currentspeed=location.getSpeed();
		if (location.getSpeed()>maxspeed){

			maxspeed=location.getSpeed();

		}





		if (location.getSpeed()>=speed_gpx/3.6 && (int)location.getAccuracy()<hdop_gpx && prevlocation_spd!=null ){

		workdistance=workdistance+location.distanceTo(prevlocation_spd);

		Log.d(this.getClass().getName(),"Log of Workdistance, Workdistance="+ Float.toString(workdistance)+" location="+location.toString()+" prevlocation_spd="+prevlocation_spd.toString()+" distanceto="+Float.toString(location.distanceTo(prevlocation_spd)));

		prevlocation_spd.set(location);

		}

		//Log.d(this.getClass().getName(),"workmilli="+ Float.toString(workmilli)+" gettime="+location.getTime());

		//Log.d(this.getClass().getName(),"diff="+ Float.toString(location.getTime()-workmilli));

		if (( System.currentTimeMillis()-workmilli)>0){

		avgspeed=workdistance/( System.currentTimeMillis()-workmilli);

		//Log.d(this.getClass().getName(),"avgspeed="+ Float.toString(avgspeed));

		}

		//mp.release();





		//Log.d(this.getClass().getName(), df0.format(location.getSpeed()*3.6).toString());

		//Log.d(this.getClass().getName(), df0.format(prevlocation.getSpeed()*3.6).toString());

		if (settings.getBoolean("usetts", false)&&tts!=null && !tts.isSpeaking() && !(df0.format(location.getSpeed()*3.6).toString()).equals(lastsay))

		{

			//Log.d(this.getClass().getName(), df0.format(location.getSpeed()*3.6).toString());

			//Log.d(this.getClass().getName(), df0.format(prevlocation.getSpeed()*3.6).toString());

			tts.speak(df0.format(location.getSpeed()*3.6) , TextToSpeech.QUEUE_ADD, null);

			lastsay=df0.format(location.getSpeed()*3.6).toString();

		}

		position = ( df6.format(location.getLatitude())+", "+df6.format( location.getLongitude())+"\nСкорость:" +df1.format(location.getSpeed()*3.6))+" Км/ч";

		//position = ( String.format("%.6f", location.getLatitude())+", "+String.format("%.6f", location.getLongitude())+" = "+String.format("%.1f", location.getSpeed()));

//if (location.getTime()>lastfix+3000)notifygps(false);

//if (location.getTime()<lastfix+3000)notifygps(true);



timeperiod= System.currentTimeMillis()-workmilli;

refresh();





if (location.getProvider().equals(LocationManager.GPS_PROVIDER))

{



	lastgpslocationtime=System.currentTimeMillis();







if (gpx && fileheaderok) {



	if (usecourse){



	double lon1=location.getLongitude();

	double lon2=prevlocation_gpx.getLongitude();

	double lat1=location.getLatitude();

	double lat2=prevlocation_gpx.getLatitude();

	double dLon=lon2-lon1;

	double y = Math.sin(dLon) * Math.cos(lat2);

	double x = Math.cos(lat1)*Math.sin(lat2) -

	Math.sin(lat1)*Math.cos(lat2)*Math.cos(dLon);

	brng_gpx = Math.toDegrees(Math.atan2(y, x)); //.toDeg();

	position=position+"\n"+getString(R.string.TrackCourseChange)+df1.format( Math.abs(brng_gpx-prevbrng_gpx));

	refresh();

		//Log.d(this.getClass().getName(), "Попали в проверку курса для трека");

	if ((int)location.getAccuracy()<hdop_gpx &&(location.distanceTo(prevlocation_gpx)>distance_gpx || location.getTime()>(prevlocation_gpx.getTime()+period_gpx) || (location.getSpeed()>=speedbearing_gpx/3.6 && Math.abs(brng_gpx-prevbrng_gpx)>=bearing_gpx)))

	{

		prevlocation_gpx.set(location);

		prevbrng_gpx=brng_gpx;

		writegpx(location);

	}

	}

	else {

		//Log.d(this.getClass().getName(), "Пишем трек без курса");

		if (location.getSpeed()>=speed_gpx/3.6&&(int)location.getAccuracy()<hdop_gpx&&(location.distanceTo(prevlocation_gpx)>distance_gpx || location.getTime()>(prevlocation_gpx.getTime()+period_gpx) ))

		{	writegpx(location);

		prevlocation_gpx.set(location);

		}

		}



}
Log.d(this.getClass().getName(), "sessionstarted="+sessionstarted);
		if (!hash.equals("") && live&&sessionstarted)

		{

		if(usecourse){

			Log.d(this.getClass().getName(), "Попали в проверку курса для отправки");

			 Log.d(this.getClass().getName(), "Accuracey"+location.getAccuracy()+"hdop"+hdop);

			double lon1=location.getLongitude();

			double lon2=prevlocation.getLongitude();



			double lat1=location.getLatitude();

			double lat2=prevlocation.getLatitude();

			double dLon=lon2-lon1;

			double y = Math.sin(dLon) * Math.cos(lat2);

			double x = Math.cos(lat1)*Math.sin(lat2) -

			Math.sin(lat1)*Math.cos(lat2)*Math.cos(dLon);

			brng = Math.toDegrees(Math.atan2(y, x)); //.toDeg();

			position=position+"\n"+getString(R.string.SendCourseChange)+df1.format( Math.abs(brng-prevbrng));

			refresh();



		if (

				(int)location.getAccuracy()<hdop && location.getSpeed()>=speed/3.6 &&

				(location.distanceTo(prevlocation)>distance || location.getTime()>(prevlocation.getTime()+period) || (location.getSpeed()>=(speedbearing/3.6) && Math.abs(brng-prevbrng)>=bearing)))

		{





			prevlocation.set(location);

			prevbrng=brng;
			Log.d(this.getClass().getName(), "send(location)="+location);
		sendlocation ( location);





		}
else {
	Log.d(this.getClass().getName(),Float.toString(location.distanceTo(prevlocation)));
	Log.d(this.getClass().getName(), Integer.toString(distance));
	Log.d(this.getClass().getName(), Long.toString(location.getTime()));
	Log.d(this.getClass().getName(), Long.toString(prevlocation.getTime()+period));
	Log.d(this.getClass().getName(),  Float.toString((location.getSpeed())));
	Log.d(this.getClass().getName(),  Float.toString((float) ((speedbearing/3.6))));
	Log.d(this.getClass().getName(), Boolean.toString( ( Math.abs(brng-prevbrng)>=bearing)));
	Log.d(this.getClass().getName(), "не выдержали проверку на курс");
}


		}

		 else {

			 Log.d(this.getClass().getName(), "Отправляем без курса");

			 if ((int)location.getAccuracy()<hdop &&location.getSpeed()>=speed/3.6 &&(location.distanceTo(prevlocation)>distance || location.getTime()>(prevlocation.getTime()+period)))

			 {

				 //Log.d(this.getClass().getName(), "Accuracey"+location.getAccuracy()+"hdop"+hdop);

				 prevlocation.set(location);
					Log.d(this.getClass().getName(), "send(location)="+location);
				 sendlocation (location);

			 } else {
				 Log.d(this.getClass().getName(), "не отправляем без курса");
			 }



		 }



		}
		else {
			Log.d(this.getClass().getName(), " not !hash.equals() && live&&sessionstarted");	
		}



}

//LocwakeLock.release();

}



	public void onProviderDisabled(String provider) {

		// TODO Auto-generated method stub



	}



	public void onProviderEnabled(String provider) {

		// TODO Auto-generated method stub



	}



	public void onStatusChanged(String provider, int status, Bundle extras) {



		// TODO Auto-generated method stub



	}

	 private String getPage(String adr, String buf) throws IOException {

		 Log.d(this.getClass().getName(), "void getpage");

		 int portOfProxy = android.net.Proxy.getDefaultPort();

         if( portOfProxy > 0 ){

            Proxy proxy = new Proxy (Proxy.Type.HTTP ,new InetSocketAddress( android.net.Proxy.getDefaultHost(), portOfProxy )  );

            HttpURLConnection con = (HttpURLConnection) new URL(adr).openConnection(proxy);

            con.setReadTimeout(5000);

      	    con.setConnectTimeout(15000);

      	  if(usebuffer){

      		 con.setReadTimeout(5000+ buf.length());

      		  con.setRequestMethod("POST");

			 con.setDoOutput(true);

         con.setDoInput(true);}

      	 con.connect();



    if(usebuffer){

    	OutputStream os = con.getOutputStream();



    	os.write( buf.getBytes());

    	Log.d(this.getClass().getName(),"Отправленный буфер"+ buf);

        os.flush();

        os.close();}

   	   			if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {

   	   		  String  ret= inputStreamToString(con.getInputStream());



   	   		  con.disconnect();

    	   				return ret;

 			} else {

 				//internetnotify(false);

 				return getString(R.string.ServerError);



 			}

         }

         else {

       	  HttpURLConnection con = (HttpURLConnection) new URL(adr).openConnection();

             con.setReadTimeout(5000);

       	    con.setConnectTimeout(15000);

       	 if(usebuffer){

       		 con.setReadTimeout(5000+ buf.length());

       		 con.setRequestMethod("POST");

			 con.setDoOutput(true);

         con.setDoInput(true);

       	 }

         con.connect();





        if(usebuffer){

           	OutputStream os = con.getOutputStream();

        	os.write( buf.getBytes());

        	Log.d(this.getClass().getName(),"Отправленный буфер"+ buf);

            os.flush();

            os.close();}

    	   			if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {

    	   				//instream=con.getInputStream();
    	   				Log.d(this.getClass().getName(),"getpage http_ok");
    	   				String  ret= inputStreamToString(con.getInputStream());



  			Log.d(this.getClass().getName(), "void getpage end");

    	   				return ret;

  			    }

    	   			else {

    	   				Log.d(this.getClass().getName(), "void getpage end22");

    	   				return getString(R.string.ServerError);

  			}



         }



 	}



	private String inputStreamToString(InputStream in) throws IOException {

		Log.d(this.getClass().getName(), "void inputstreamtostring begin");



		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in),8192);

	     stringBuilder.setLength(0);



	    int c = 0;

	    int i=0;



	    while (!(c==-1)) {



	    	c = bufferedReader.read();

	        if (!(c==-1))stringBuilder.append((char) c);

	        i=i+1;

	    }



	    bufferedReader.close();

//return chbuf.toString();

	    Log.d(this.getClass().getName(), "void inputstreamtostring end");

	    return stringBuilder.toString();

	}

private void internetnotify(boolean internet){

	if (!internet){

		if(!beepedoff){

	//long[] pattern = {0,50, 0, 30, 0, 50};

		//vibrator.vibrate(pattern, 2);

			if (vibrate)vibrator.vibrate(vibratetime);

			//if (playsound &&inetoff!=null&& !inetoff.isPlaying()){inetoff.start();}
 if (playsound){soundPool.play(inetoff, 1f, 1f, 1, 0, 1f);}
	//Log.d(this.getClass().getName(), "Интернет пропал");



		beepedoff=true;

		beepedon=false;

		}

	}

	else{



			if(!beepedon){

				//long[] pattern = {0,50, 0, 30, 0, 50};

				//Log.d(this.getClass().getName(), "Интернет появился");

				//vibrator.vibrate(pattern, 2);

				if(vibrate)vibrator.vibrate(vibratetime);

//				if (playsound &&ineton!=null&&!ineton.isPlaying()){
//
//
//
//				ineton.start();
//
//				}
				if (playsound) {soundPool.play(ineton, 1f, 1f, 1, 0, 1f);}

				beepedon=true;

				beepedoff=false;

		}

	}



}

public static String unescape (String s)

{

    while (true)

    {

        int n=s.indexOf("&#");

        if (n<0) break;

        int m=s.indexOf(";",n+2);

        if (m<0) break;

        try

        {

            s=s.substring(0,n)+(char)(Integer.parseInt(s.substring(n+2,m)))+

                s.substring(m+1);

        }

        catch (Exception e)

        {

            return s;

        }

    }

    s=s.replace("&quot;","\"");

    s=s.replace("&lt;","<");

    s=s.replace("&gt;",">");

    s=s.replace("&amp;","&");

    return s;

}







private String decodesendresult(String str){



	Log.d(this.getClass().getName(), "Ответ сервера:"+str);

	int s=-1;

	int l=-1;



	try {

		JSONObject result = new JSONObject(str);







		if(result.has("s")){ s = result.optInt("s");}

		if (result.has("l")){ l =result.optInt("l");}



		if (s==0 ) {

		int code=result.optInt("error");

		str=getString(R.string.error)+code+" "+ unescape(result.optString("description:ru"));

		sended=false;

		 stopServiceWork(true);
		 if (code==5||code==6){
			 if(!OsMoDroid.gpslocalserviceclientVisible){
			 notifywarnactivity(getString(R.string.warnhash), true);}
			 else {
				 warnwronghash ();
			 }
		 
		 }
		 else
		 {
			 notifywarnactivity(getString(R.string.commansendlocation)+str, false);
		 }

		}

		if (s==1|| s==2) {

			if (l!=-1){str=getString(R.string.succes)+getString(R.string.buffer)+ l;}

			else str=getString(R.string.succes);

		sended=true;

		//result=null;

		}

	} catch (JSONException e) {

		//e.printStackTrace();

		sended=false;

		Log.d(this.getClass().getName(), "decodesendresult return:"+str);

		return str;// TODO Auto-generated catch block



	}

//	if (str.equals("{\"s\":1}\n")){str=getString(R.string.succes);

	//sended=true;

	//}else {

		//str="НОТ";

//	sended=true;};

	//str="xc";

	Log.d(this.getClass().getName(), "decodesendresult return:"+str);

	return str;



}



private void writegpx(Location location){

	FileWriter trackwr;

	 long gpstime = location.getTime();

	 Date date = new Date(gpstime);

	// SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ssZ");



	 String strgpstime = sdf1.format(date)+"Z";
	 writecounter++;
	if ((gpxbuffer).length()<5000)gpxbuffer = gpxbuffer +  "<trkpt lat=\"" + df6.format( location.getLatitude())+"\""

			+ " lon=\"" + df6.format( location.getLongitude())

			+ "\"><ele>" +df1.format( location.getAltitude())

			+ "</ele><time>" + strgpstime

			+ "</time><speed>" + df1.format( location.getSpeed())

			+ "</speed>" +"<hdop>"+df1.format( location.getAccuracy()/4)+"</hdop>"  +"</trkpt>";

	else



	 try {

		trackwr = new FileWriter(fileName, true);



		String towright = gpxbuffer;

		trackwr.write(towright);//.replace(",", "."));

		trackwr.flush();

		trackwr.close();

		gpxbuffer="";

	} catch (IOException e) {

		e.printStackTrace();

	}



}

private void sendlocation (Location location){

	Log.d(this.getClass().getName(), "void sendlocation");

//http://t.esya.ru/?60.452323:30.153262:5:53:25:hadfDgF:352

//	- 0 = latitudedecimal(9,6) (широта)

//	- 1 = longitudedecimal(9,6) (долгота)

//	- 2 = HDOPfloat (горизонтальная ошибка: метры)

//	- 3 = altitudefloat (высота на уровнем моря: метры)

//	- 4 = speedfloat(1) (скорость: метры в секунду)

//	- 5 = hashstring (уникальный хеш пользователя)

//	- 6 = checknumint(3) (контрольное число к хешу)

if (usebuffer)

	{URLadr="http://t.esya.ru/?"+  df6.format( location.getLatitude()) +":"+ df6.format(location.getLongitude())+":"+ df1.format( location.getAccuracy())

	+":"+df1.format( location.getAltitude())+":"+df1.format( location.getSpeed())+":"+hash+":"+n+":"+location.getTime()/1000;

	}

else

	{URLadr="http://t.esya.ru/?"+  df6.format( location.getLatitude()) +":"+ df6.format( location.getLongitude())+":"+ df1.format(location.getAccuracy())

	+":"+df1.format( location.getAltitude())+":"+df1.format( location.getSpeed())+":"+hash+":"+n;

	}



if (send!=null)
{
	Log.d(this.getClass().getName(), "send.status="+send.getStatus().toString() +" isCanceled="+send.isCancelled());	
}

if (send == null ||send.getStatus().equals(AsyncTask.Status.FINISHED) || send.isCancelled())

//если задач по отправке не существует или закончена или отменена

{
	Log.d(this.getClass().getName(), "sendlocation send==null or FINISHED or isCanceled");


	if(usebuffer){

// если используется буфер
		Log.d(this.getClass().getName(), "sendlocation if usebuffer");
	if (sended )

	{
		Log.d(this.getClass().getName(), "sendlocation if sended");
//и прошлая отправка успешная

		Log.d(this.getClass().getName(), "buffersb.delete "+buffersb.toString());

		buffersb.delete(0, lastbuffersb.length());

		buffercounter=buffercounter-lcounter;
		sendcounter=sendcounter+lcounter;

		Log.d(this.getClass().getName(), "buffersb.delete "+buffersb.toString());

	}

	else

	{
		Log.d(this.getClass().getName(), "sendlocation if not sended");
		//прошлая отправка не успешная

		Log.d(this.getClass().getName(), "buffersb.append "+buffersb.toString());





		if (buffersb.length()==0)

		{
			Log.d(this.getClass().getName(), "sendlocation if buffersb.l=0");

			buffersb.append("log[]=").append(sendedsb);

			buffercounter=buffercounter+scounter;

		}

		else

		{
			Log.d(this.getClass().getName(), "sendlocation if buffersb.l!=0");

			buffersb.append("&log[]=").append(sendedsb);

			buffercounter=buffercounter+scounter;

		}

		Log.d(this.getClass().getName(), "buffersb.append "+buffersb.toString());



	}

				}
	Log.d(this.getClass().getName(), "sendlocation send=new SendCoor");
	send = new SendCoor();

	if(usebuffer)

	{

		Log.d(this.getClass().getName(), "sendlocation if usebuffer");

		lastbuffersb.setLength(0);

		lastbuffersb.append(buffersb);

		lcounter=buffercounter;

		sendedsb.setLength(0);

		sendedsb.append(df6.format( location.getLatitude())).append(":").append(df6.format( location.getLongitude())).append(":").append(df1.format( location.getAltitude())).append(":").append(df1.format( location.getSpeed())).append(":").append(location.getTime()/1000);

		scounter=1;

		Log.d(this.getClass().getName(), "send.execute "+lastbuffersb.toString());

		send.execute(URLadr,lastbuffersb.toString());}

	else {
		Log.d(this.getClass().getName(), "sendlocation if not usebuffer");

		send.execute(URLadr," ");}

	}



 else

{
	 Log.d(this.getClass().getName(), "sendlocation send!=null or not FINISHED or not canceled");
	if(usebuffer)

	{
		Log.d(this.getClass().getName(), "sendlocation if usebuffer");
	if (buffersb.length()==0)


	{
		Log.d(this.getClass().getName(), "sendlocation if buffersb.l=0");
		Log.d(this.getClass().getName(), "2buffersb.append "+buffersb.toString());

		buffersb.append("log[]=").append(df6.format( location.getLatitude())).append(":").append(df6.format( location.getLongitude())).append(":").append(df1.format( location.getAltitude())).append(":").append(df1.format( location.getSpeed())).append(":").append(location.getTime()/1000);

		buffercounter=buffercounter+1;

		Log.d(this.getClass().getName(), "2buffersb.append "+buffersb.toString());}

	else{
		Log.d(this.getClass().getName(), "sendlocation if buffersb!=0");

		Log.d(this.getClass().getName(), "3uffersb.append "+buffersb.toString());

		buffersb.append("&log[]=").append(df6.format( location.getLatitude())).append(":").append(df6.format( location.getLongitude())).append(":").append(df1.format( location.getAltitude())).append(":").append(df1.format( location.getSpeed())).append(":").append(location.getTime()/1000);

		buffercounter=buffercounter+1;

		Log.d(this.getClass().getName(), "3buffersb.append "+buffersb.toString());}

	}

}



}

public void onGpsStatusChanged(int event) {
	int MaxPrn = 0;
	int count1 = 0;
	int countFix1 =0;
	boolean hasA = false;
	boolean hasE = false;

	GpsStatus xGpsStatus = myManager.getGpsStatus(null) ;

	Iterable<GpsSatellite> iSatellites = xGpsStatus.getSatellites() ;

	Iterator<GpsSatellite> it = iSatellites.iterator() ;

	while ( it.hasNext() ) {
		GpsSatellite oSat = (GpsSatellite) it.next() ;

		count1=count1+1;

		hasA = oSat.hasAlmanac();
		hasE = oSat.hasEphemeris();

		if(oSat.usedInFix() ){
			countFix1=countFix1+1;
			if(oSat.getPrn()>MaxPrn) MaxPrn=oSat.getPrn();
			//Log.e("A fost folosit ", "int fix!");
		}
	}

	satellite=getString(R.string.Sputniki)+count+":"+countFix; //+" ("+hasA+"-"+hasE+")";
	count=count1;
	countFix=countFix1;
	refresh();
}

public void onInit(int status) {

	// TODO Auto-generated method stub

	  if (status == TextToSpeech.SUCCESS) {

          // Set preferred language to US english.

           _langTTSavailable = tts.setLanguage(Locale.getDefault()); // Locale.FRANCE etc.

          if (_langTTSavailable == TextToSpeech.LANG_MISSING_DATA ||

          	_langTTSavailable == TextToSpeech.LANG_NOT_SUPPORTED) {

        	  Log.d(this.getClass().getName(), "Нету языка");



           } else if ( _langTTSavailable >= 0 && settings.getBoolean("usetts", false)) {

        	   Log.d(this.getClass().getName(), "Произносим");

        	   tts.speak(getString(R.string.letsgo), TextToSpeech.QUEUE_ADD, null);





          }

      } else {

    	  Log.d(this.getClass().getName(), "Инициализация TTS не выполнилась");



    	  // Initialization failed.

      }

}

public boolean isOnline() {

    ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

    NetworkInfo nInfo = cm.getActiveNetworkInfo();

    if (nInfo != null && nInfo.isConnected()) {

        Log.v("status", "ONLINE");

        return true;

    }

    else {

        Log.v("status", "OFFLINE");

        return false;

    }

}

	void warnwronghash (){
		Intent notificationIntent = new Intent(this, WarnActivity.class);

		notificationIntent.removeExtra("info");

		notificationIntent.putExtra("info", getString(R.string.warnhash));
		
		notificationIntent.removeExtra("neednewhash");

		notificationIntent.putExtra("neednewhash", true);

		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP	| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		
		getApplication().startActivity(notificationIntent);
	}

	void notifywarnactivity(String info, boolean b) {



		Long when=System.currentTimeMillis();

		Intent notificationIntent = new Intent(this, WarnActivity.class);

		notificationIntent.removeExtra("info");

		notificationIntent.putExtra("info", info);
		
		notificationIntent.removeExtra("neednewhash");

		notificationIntent.putExtra("neednewhash", b);


		//notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP	| Intent.FLAG_ACTIVITY_SINGLE_TOP);

		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP	| Intent.FLAG_ACTIVITY_SINGLE_TOP);

		PendingIntent contentIntent = PendingIntent.getActivity(this, OsMoDroid.notifyidApp(),notificationIntent, 0);

		NotificationCompat.Builder notificationBuilder=null;
		
		if (settings.getBoolean("silentnotify", false)) {
			 notificationBuilder = new NotificationCompat.Builder(

				    	getApplicationContext())

				    	.setWhen(when)

				    	.setContentText(info)

				    	.setContentTitle("OsMoDroid")

				    	.setSmallIcon(R.drawable.warn)

				    	.setAutoCancel(true)

				    	.setDefaults(Notification.DEFAULT_LIGHTS)

				    	.setContentIntent(contentIntent);
		}
		else
		{
		 notificationBuilder = new NotificationCompat.Builder(

		    	getApplicationContext())

		    	.setWhen(when)

		    	.setContentText(info)

		    	.setContentTitle("OsMoDroid")

		    	.setSmallIcon(R.drawable.warn)

		    	.setAutoCancel(true)

		    	.setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_VIBRATE| Notification.DEFAULT_SOUND)

		    	.setContentIntent(contentIntent);}

			Notification notification = notificationBuilder.build();

			mNotificationManager.notify(OsMoDroid.warnnotifyid, notification);

	}


void connectChannels(){
	Log.d(getClass().getSimpleName(),"void connecChannels");
	for (Channel ch : LocalService.channelList){
		if (!ch.connected){
		ch.connect();}
	}
	
	
}
void disconnectChannels(){
	Log.d(getClass().getSimpleName(),"void disconnectChannels");
	for (Channel ch : LocalService.channelList){
		if (ch.connected){ch.disconnect();}
	}
	
}








public void onResultsSucceeded(APIComResult result) {
	JSONArray a = null;




	if (result.Jo==null&&result.ja==null)

	{



		Log.d(getClass().getSimpleName(),"notifwar1 Команда:"+result.Command+" Ответ сервера:"+result.rawresponse+ getString(R.string.query)+result.url);

	//		notifywarnactivity("Команда:"+result.Command+" Ответ сервера:"+result.rawresponse+ " Запрос:"+result.url);
		Toast.makeText(LocalService.this, R.string.esya_ru_notrespond , Toast.LENGTH_LONG).show();

		}

	if (!(result.Jo==null)) {
		if (result.Jo.has("error")){
			if ((result.Command.equals("session_start")||result.Command.equals("start"))&&result.Jo.optString("error").equals("200")){
				stopServiceWork(false);
				if(!OsMoDroid.gpslocalserviceclientVisible){
				
				notifywarnactivity(getString(R.string.warnhash), true);}
				else {
					warnwronghash ();
				}
			}else
			{
		Log.d(getClass().getSimpleName(),"notifwar2:"+result.Jo.optString("error")+" "+result.Jo.optString("error_description"));
		
		notifywarnactivity(getString(R.string.comand)+result.Command+getString(R.string.errorcode)+result.Jo.optString("error")+getString(R.string.detalisation)+result.Jo.optString("error_description")+getString(R.string.query)+result.url+ getString(R.string.versionosmodroid)+strVersionName, false);
			}
		}

	}
	if (result.Command.equals("session_start"))
	{
		sessionstarted=true;
	}
	if (result.Command.equals("session_stop"))
	{
		sessionstarted=false;
	}
	

	if (result.Command.equals("start")&& !(result.Jo==null))

	{
		
        editor.putLong("laststartcommandtime", System.currentTimeMillis());
        editor.commit();
		if (!result.Jo.optString("lpch").equals("")&& !result.Jo.optString("lpch").equals(settings.getString("lpch", ""))){

			ArrayList<String[]> longPollchannels =new ArrayList<String[]>();
			longPollchannels.add(new String[] {"ctrl_"+settings.getString("lpch", ""),"r",""});
			longPollchannels.add(new String[] {settings.getString("lpch", "")+"_chat","m",""});
			
if (myIM!=null){
		myIM.removechannels(longPollchannels);	
}
			editor.putString("lpch", result.Jo.optString("lpch"));

			editor.commit();

			longPollchannels =new ArrayList<String[]>();
			longPollchannels.add(new String[] {"ctrl_"+settings.getString("lpch", ""),"r",""});
			longPollchannels.add(new String[] {settings.getString("lpch", "")+"_chat","m",""});
			
if (myIM!=null){
		myIM.addchannels(longPollchannels);	
}
			
		}
		if(!result.Jo.optString("device").equals("")){
			editor.putString("device", result.Jo.optString("device"));
			editor.putString("view-url", "http://m.esya.ru/"+result.Jo.optString("device_url"));
			editor.putString("devicename", result.Jo.optString("device_name"));
			
			editor.commit();
			refresh();
			
		}
		
		if(!result.Jo.optString("session_ttl").equals("")){
			editor.putString("session_ttl", result.Jo.optString("session_ttl"));
			editor.commit();
			
			
		}
		if(!result.Jo.optString("uid").equals("")){
			editor.putString("uid", result.Jo.optString("uid"));
			editor.commit();
			
			
		}
		

				if (!result.Jo.optString("motd").equals("") ||!result.Jo.optString("query_per_day").equals("")){

				in.putExtra("startmessage", result.Jo.optString("motd")+"\n"+ getString(R.string.dayssend) +result.Jo.optString("query_per_day")

				+"\n"+ getString(R.string.wekksends)+result.Jo.optString("query_per_week")+ "\n" +getString(R.string.monthsends)+result.Jo.optString("query_per_month"));

				sendBroadcast(in);

			}

	}

	if (result.Command.equals("APIM")&& !(result.Jo==null))




	{

		Log.d(getClass().getSimpleName(),"APIM Response:"+result.Jo);

		 
		    Iterator<String> it = (result.Jo).keys();

			  while (it.hasNext())
		    
		    

        	{
		    	
		    	String keyname= it.next();
		    	if(keyname.contains("om_device_channel_active")){
		    		netutil.newapicommand((ResultsListener)LocalService.this, "om_device_get:"+settings.getString("device", ""));
		    		netutil.newapicommand((ResultsListener)LocalService.this, "om_device_channel_adaptive:"+settings.getString("device", ""));
		    	 }
		    	if(keyname.contains("om_channel_overlay_get")){
		    		for (Channel ch : channelList){
		    			if (result.Jo.has("om_channel_overlay_get:"+Integer.toString(ch.u))){
		    				try {
		    				for (int i = 0; i < result.Jo.getJSONArray("om_channel_overlay_get:"+Integer.toString(ch.u)).length(); i++) {
		    					JSONObject jsonObject = result.Jo.getJSONArray("om_channel_overlay_get:"+Integer.toString(ch.u)).getJSONObject(i);
		    					ch.downloadgpx(jsonObject.getJSONObject("data").getString("download_url"), jsonObject.getJSONObject("data").getString("u"),jsonObject.getString("color"));
		    				}
		    				}
		    				catch (JSONException e) {
		    					Log.d(getClass().getSimpleName(), e.getMessage());
							}
		    				informRemoteClientChannelsListUpdate();
		    			}
		    		}
		    	}
		    	if(keyname.contains("om_channel_point_list")){
		    		for (Channel ch: channelList){
		    			if (result.Jo.has("om_channel_point_list:"+Integer.toString(ch.u))){
		    				try {
		    					ch.getPointList(result.Jo.getJSONArray("om_channel_point_list:"+Integer.toString(ch.u)));
			    				}		    					
		    				 catch (Exception e) {
		    					e.printStackTrace();
							}
		    				informRemoteClientChannelsListUpdate();
		    			}
		    		}
		    	}
        	}
		
		
		if (result.Jo.has("om_device_get:"+settings.getString("device", ""))){
			try {
				JSONObject jsonObject =	result.Jo.getJSONObject("om_device_get:"+settings.getString("device", ""));
		 		 // Log.d(getClass().getSimpleName(), a.toString());
				settings.edit().putString("om_device_get", jsonObject.toString()).commit();
		 	if (jsonObject.getString("channel_send").equals("1")){
		 		globalsend=true;
		 		settings.edit().putBoolean("globalsend", globalsend).commit();
		 		
		 	} else
		 	{
		 		globalsend=false;
		 		settings.edit().putBoolean("globalsend", globalsend).commit();
		 	}
		 	refresh();
			} catch (Exception e) {

					 Log.d(getClass().getSimpleName(), "om_device_get эксепшн"+e.getMessage());
					e.printStackTrace();
				}
		}
		if (result.Jo.has("tr_track_upload:1")){
				LocalService.mNotificationManager.cancel(result.notificationid);
		}
		
		if (result.Jo.has("om_device")){
			deviceList.clear();
			deviceList.add(new Device("0",getString(R.string.observers),"1", settings.getString("uid", "0")));
		
			try {
				  a =	result.Jo.getJSONArray("om_device");
				  //settings.edit().putString("om_device", a.toString()).commit();
				  Log.d(getClass().getSimpleName(), a.toString());
		 		  deviceListFromJSONArray(a);
		 		  saveObject(deviceList, OsMoDroid.DEVLIST);
				} catch (Exception e) {
					 Log.d(getClass().getSimpleName(), "эксепшн");
				}
			 Log.d(getClass().getSimpleName(),deviceList.toString());
			 if (deviceAdapter!=null) {deviceAdapter.notifyDataSetChanged();}
		}
		if (result.Jo.has("om_device_channel_adaptive:"+settings.getString("device", ""))){
			channelList.clear();
				try {
				  a =	result.Jo.getJSONArray("om_device_channel_adaptive:"+settings.getString("device", ""));
				  settings.edit().putString("om_device_channel_adaptive", a.toString()).commit();
				  Log.d(getClass().getSimpleName(), a.toString());
				  channelListFromJSONArray(a);
					}
				catch (Exception e) {
					Log.d(getClass().getSimpleName(), "om_device_channel_adaptive эксепшн"+e.getMessage());
					e.printStackTrace();
				}


			 Log.d(getClass().getSimpleName(),channelList.toString());
			 
			 if (channelsAdapter!=null) {channelsAdapter.notifyDataSetChanged();}
			 Log.d(getClass().getSimpleName(),"binded="+binded);
			 if (binded){
				 connectChannels();
				 informRemoteClientChannelsListUpdate();
				 Log.d(getClass().getSimpleName(), "inform cleint");
			 }
			for (Channel channel : channelList){
				netutil.newapicommand((ResultsListener)this, "om_channel_overlay_get:"+channel.u);
				netutil.newapicommand((ResultsListener)this, "om_channel_point_list:"+channel.u);
			}
			
		}
		
	

		Log.d(getClass().getSimpleName(),"APIM Response:"+result.Jo);

	}


}


static void deviceListFromJSONArray(JSONArray a) throws JSONException {
	for (int i = 0; i < a.length(); i++) {
		JSONObject jsonObject = a.getJSONObject(i);
Device devitem = new Device(jsonObject.getString("u"), jsonObject.getString("name"),jsonObject.getString("app")
,jsonObject.getString("last"),
"http://m.esya.ru/"+jsonObject.getString("url"),
jsonObject.getString("where"),
jsonObject.getString("lat"),
jsonObject.getString("lon"),
jsonObject.getString("online"),
jsonObject.getString("state"), jsonObject.getString("uid")
);

deviceList.add(devitem);



	 }
}


void channelListFromJSONArray(JSONArray a) throws JSONException {
	for (int i = 0; i < a.length(); i++) {
		JSONObject jsonObject = a.getJSONObject(i);
//Channel chanitem = new Channel( jsonObject.getString("name"),jsonObject.getString("u"),jsonObject.getString("created")	);
		Channel chanitem = new Channel( jsonObject, LocalService.this);
channelList.add(chanitem);
if (currentChannel!=null) {
if(chanitem.u==currentChannel.u){
	currentchanneldeviceList.clear();
	currentchanneldeviceList.addAll(chanitem.deviceList);
	 if (channelsDevicesAdapter!=null) {channelsDevicesAdapter.notifyDataSetChanged();}
}
}

	 }
}

public void playAlarmOn (Boolean remote){
	if (alarmStreamId==0){alarmStreamId = soundPool.play(alarmsound, 1f, 1f, 1, -1, 1f);}
	if (remote){netutil.newapicommand((ResultsListener)LocalService.this, "om_device_pong:"+settings.getString("device", "")+","+Long.toString(System.currentTimeMillis()));}
	else{
		  JSONObject postjson = new JSONObject();
        try {
			postjson.put("alarm", "on");
		} catch (JSONException e) {
			e.printStackTrace();
		}
        netutil.newapicommand((ResultsListener)LocalService.this, "om_device_pong:"+settings.getString("device", "")+","+Long.toString(System.currentTimeMillis()), "json="+postjson.toString());
	
	}
	Log.d(this.getClass().getName(), "play alarm on ");
	
}

public void playAlarmOff (Boolean remote){
	soundPool.stop(alarmStreamId);
	alarmStreamId=0;
	if (remote){netutil.newapicommand((ResultsListener)LocalService.this, "om_device_pong:"+settings.getString("device", "")+","+Long.toString(System.currentTimeMillis()));}
	else{
		  JSONObject postjson = new JSONObject();
          try {
			postjson.put("alarm", "off");
		} catch (JSONException e) {
			e.printStackTrace();
		}
          netutil.newapicommand((ResultsListener)LocalService.this, "om_device_pong:"+settings.getString("device", "")+","+Long.toString(System.currentTimeMillis()), "json="+postjson.toString());
	
	}
	Log.d(this.getClass().getName(), "play alarm off ");
	
}


public void enableSignalisation (Boolean remote){
	editor.putLong("signalisation", System.currentTimeMillis());
	editor.commit();
	mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	Log.d(this.getClass().getName(), "Enable signalisation ");
	soundPool.play(signalonoff, 1f, 1f, 1, 0, 1f);
	signalisationOn=true;
	
	if (remote){refresh();
		netutil.newapicommand((ResultsListener)LocalService.this, "om_device_pong:"+settings.getString("device", "")+","+Long.toString(System.currentTimeMillis()));}
	else{
		  JSONObject postjson = new JSONObject();
        try {
			postjson.put("signal", "on");
		} catch (JSONException e) {
			e.printStackTrace();
		}
        netutil.newapicommand((ResultsListener)LocalService.this, "om_device_pong:"+settings.getString("device", "")+","+Long.toString(System.currentTimeMillis()), "json="+postjson.toString());
	
	}
	}
	


public void disableSignalisation (Boolean remote){
	editor.remove("signalisation");
	editor.commit();
	mSensorManager.unregisterListener(this);
	Log.d(this.getClass().getName(), "Disable signalisation ");
	playAlarmOff(remote);
	soundPool.play(signalonoff, 1f, 1f, 1, 0, 1f);
	signalisationOn=false;
	
	if (remote){refresh();
	netutil.newapicommand((ResultsListener)LocalService.this, "om_device_pong:"+settings.getString("device", "")+","+Long.toString(System.currentTimeMillis()));}
	else{
		  JSONObject postjson = new JSONObject();
        try {
			postjson.put("signal", "off");
		} catch (JSONException e) {
			e.printStackTrace();
		}
        netutil.newapicommand((ResultsListener)LocalService.this, "om_device_pong:"+settings.getString("device", "")+","+Long.toString(System.currentTimeMillis()), "json="+postjson.toString());
	
	}
	
}

public void onAccuracyChanged(Sensor sensor, int accuracy) {
	// TODO Auto-generated method stub
	
}


public void onSensorChanged(SensorEvent event) {
	
	double x = event.values[0];
    double y = event.values[1];
    double z = event.values[2];

    double a = Math.round(Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)
            + Math.pow(z, 2)));
    currentAcceleration = Math.abs((float) (a - calibration));
    
    
    try {
		sensivity = ((float)Integer.parseInt(settings.getString("sensivity", "5")))/10f;
		
	} catch (NumberFormatException e) {
		sensivity=0.5f;
	}
    
    if(settings.contains("signalisation")&& settings.getLong("signalisation", 0)+10000<System.currentTimeMillis()&&currentAcceleration>sensivity){
    	editor.putLong("signalisation", System.currentTimeMillis());
    	editor.commit();
    	netutil.newapicommand((ResultsListener)LocalService.this, "om_device_alarm");
    	Log.d(this.getClass().getName(), "Alarm Alarm Alarm "+Float.toString(currentAcceleration));
    }

	
}


void saveObject(Object obj, String filename)  {
	try {
		FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE);
		ObjectOutputStream output = new ObjectOutputStream(fos);
		output.writeObject(obj);
		output.flush();
		output.close();
	} catch (Exception e) {
		e.printStackTrace();
	}
	
}

Object loadObject(String filename, Class type){
	try {
		input = new ObjectInputStream(openFileInput(filename));
		
		return type.cast(input.readObject());
		
	} catch (StreamCorruptedException e) {
		e.printStackTrace();
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	}
	return null;
	
}

}







