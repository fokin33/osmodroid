
package com.OsMoDroid;



import android.net.ConnectivityManager;

import android.net.NetworkInfo;

import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;

import java.io.BufferedReader;

import java.io.BufferedWriter;

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
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import java.util.Iterator;

import java.util.Locale;

import java.util.TimeZone;

import java.util.concurrent.TimeUnit;

//import java.util.Locale;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import com.OsMoDroid.LocalService.SendCoor;

import com.OsMoDroid.netutil.MyAsyncTask;



import android.app.AlarmManager;

import android.app.AlertDialog;

import android.app.Notification;

import android.app.NotificationManager;

import android.app.PendingIntent;

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





public  class LocalService extends Service implements LocationListener,GpsStatus.Listener, TextToSpeech.OnInitListener,  ResultsListener {

	private static final int OSMODROID_ID = 1;

	Boolean sessionstarted=false;
	
	int notifyid=2;

	//MediaPlayer mp;

	//BufferedReader bufferedReader;

	int gpson;

	int gpsoff;

	int ineton;

	int inetoff;

	int sendpalyer;
	
	int startsound;
	
	int stopsound;

	private static SoundPool soundPool;
	//String cursendforbuffer="";

	//String prevcursendforbuffer="";

	private netutil.MyAsyncTask starttask;

	private Intent in;

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

	private float maxspeed=0;

	private float avgspeed;

	private long timeperiod=0;

	private float workdistance;

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

	private LocationManager myManager;

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

	private int sendcounter;

	private int buffercounter=0;

	BroadcastReceiver receiver;

	BroadcastReceiver checkreceiver;

	BroadcastReceiver onlinePauseforStartReciever;

	BroadcastReceiver remoteControlReciever;

	BroadcastReceiver batteryReciever;

	private final IBinder mBinder = new LocalBinder();

	private String gpxbuffer= new String();

	//private String sendbuffer = new String();

	private String Sattelite="";

	private String Accuracy="";

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

	final private static DecimalFormat df1 = new DecimalFormat("########.#");

	final private static DecimalFormat df2 = new DecimalFormat("########.##");

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

	    public static DeviceAdapter deviceAdapter;
	    public static ChannelsAdapter channelsAdapter;
	    public static ChannelsDevicesAdapter channelsDevicesAdapter;
	    public static ArrayAdapter<String> channelsmessagesAdapter;

	    static Context serContext;

	    final static Handler alertHandler = new Handler() {



			@Override

			public void handleMessage(Message message) {



			Bundle b = message.getData();

			String text = b.getString("MessageText");

			if (b.getBoolean("om_online",false)){
				netutil.newapicommand((ResultsListener)serContext, "om_device");
			}


			if (text != null) {



			Toast.makeText(serContext, text, Toast.LENGTH_SHORT).show();

			LocalService.messagelist.add(0,text);

			Log.d(this.getClass().getName(), "List:"+LocalService.messagelist);

			 Bundle a=new Bundle();

	         a.putStringArrayList("meslist", LocalService.messagelist);

	     Intent activ=new Intent(serContext,  mesActivity.class);

	     activ.putExtras(a);

	     activ.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP	| Intent.FLAG_ACTIVITY_SINGLE_TOP);

	     PendingIntent contentIntent = PendingIntent.getActivity(serContext, OsMoDroid.notifyidApp(),activ, 0);



	 	Long when=System.currentTimeMillis();


	 	NotificationCompat.Builder notificationBuilder = null;

if (settings.getBoolean("silentnotify", false)){
	 notificationBuilder = new NotificationCompat.Builder(

				serContext.getApplicationContext())

		    	.setWhen(when)

		    	.setContentText(text)

		    	.setContentTitle("OsMoDroid")

		    	.setSmallIcon(android.R.drawable.ic_menu_send)

		    	.setAutoCancel(true)

		    	.setDefaults(Notification.DEFAULT_LIGHTS)

		    	.setContentIntent(contentIntent);
	
}
else {
		 notificationBuilder = new NotificationCompat.Builder(

				serContext.getApplicationContext())

		    	.setWhen(when)

		    	.setContentText(text)

		    	.setContentTitle("OsMoDroid")

		    	.setSmallIcon(android.R.drawable.ic_menu_send)

		    	.setAutoCancel(true)

		    	.setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_VIBRATE| Notification.DEFAULT_SOUND)

		    	.setContentIntent(contentIntent);}

			Notification notification = notificationBuilder.build();

			LocalService.mNotificationManager.notify(OsMoDroid.mesnotifyid, notification);



			 if (OsMoDroid.activityVisible) {

	    		try {



				contentIntent.send(serContext, 0, activ);

				LocalService.mNotificationManager.cancel(OsMoDroid.mesnotifyid);



			} catch (CanceledException e) {

				Log.d(this.getClass().getName(), "pending intent exception"+e);

				// TODO Auto-generated catch block

				e.printStackTrace();

			}

	    }









			}

			}

			};


	    private final IRemoteOsMoDroidService.Stub rBinder = new IRemoteOsMoDroidService.Stub() {







			public int getVersion()  {

				Log.d("OsmoDroid", "Remote getVersion");



				//Toast.makeText(LocalService.this, "vvv" , Toast.LENGTH_SHORT).show();

				return 3;

			}



			public int getBackwardCompatibleVersion()  {

				return 0;

			}



			public void Deactivate(){

				Log.d(getClass().getSimpleName(), "Remote Deactivate");

				stopServiceWork(true);

				//Toast.makeText(LocalService.this, "aaa" , Toast.LENGTH_SHORT).show();

				return;





			}



			public boolean isActive() {

				return state;

			}



			public void Activate() {

				Log.d(getClass().getSimpleName(), "Remote Deactivate");

				startServiceWork();



			}



			public int getNumberOfLayers()  {
				Log.d(getClass().getSimpleName(), "getNumberOfLayers()="+channelList.size());
				
				try {
					return channelList.size();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return 0;
			}



			public int getLayerId(int pos) {
			
				
				try {	Log.d(getClass().getSimpleName(), "getLayerId()="+channelList.get(pos).u);
					return Integer.parseInt(channelList.get(pos).u);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return 0;
			}



			public String getLayerName(int layerId) {
				Log.d(getClass().getSimpleName(), "getLayerName()");
				try {
					for (Channel channel: channelList){
						if (channel.u.equals(Integer.toString(layerId))){
							return channel.name;
					}
					}
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}



			public String getLayerDescription(int layerId)
					 {
				Log.d(getClass().getSimpleName(), "getLayerDescription()");
				try {
					for (Channel channel: channelList){
						if (channel.u.equals(Integer.toString(layerId))){
							return channel.name;
					}
					}
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}



			public int getNumberOfObjects(int layerId){
				Log.d(getClass().getSimpleName(), "getNumberOfObjects()");
				try {
					for (Channel channel: channelList){
						if (channel.u.equals(Integer.toString(layerId))){
							return channel.deviceList.size();
					}
					}
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return 0;
			}



			public int getObjectId(int layerId, int pos) {
				Log.d(getClass().getSimpleName(), "getObjectId()");
				try {
					for (Channel channel: channelList){
						if (channel.u.equals(Integer.toString(layerId))){
							return Integer.parseInt(channel.deviceList.get(pos).u);
					}
					}
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				return 0;
			}



			public float getObjectLat(int layerId, int objectId)
					 {
				Log.d(getClass().getSimpleName(), "getObjectLat()");
				try {
					for (Channel channel: channelList){
						if (channel.u.equals(Integer.toString(layerId))){
							for (Device device:channel.deviceList){
								if (device.u.equals(Integer.toString(objectId))){
									return Float.parseFloat(device.lat);			
								}
							}
							
							
					}
					}
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return 0;
			}



			public float getObjectLon(int layerId, int objectId)
					{
				Log.d(getClass().getSimpleName(), "getObjectLon()");
				try {
					for (Channel channel: channelList){
						if (channel.u.equals(Integer.toString(layerId))){
							for (Device device:channel.deviceList){
								if (device.u.equals(Integer.toString(objectId))){
									return Float.parseFloat(device.lon);		
								}
							}
							
							
					}
					}
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return 0;
			}



			public String getObjectName(int layerId, int objectId)
					 {
				Log.d(getClass().getSimpleName(), "getObjectName()");
				try {
					for (Channel channel: channelList){
						if (channel.u.equals(Integer.toString(layerId))){
							for (Device device:channel.deviceList){
								if (device.u.equals(Integer.toString(objectId))){
									return device.name;		
								}
							}
							
							
					}
					}
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}



			public String getObjectDescription(int layerId, int objectId)
					 {
				Log.d(getClass().getSimpleName(), "getObjectDescription()");
				try {
					for (Channel channel: channelList){
						if (channel.u.equals(Integer.toString(layerId))){
							for (Device device:channel.deviceList){
								if (device.u.equals(Integer.toString(objectId))){
									return device.name;		
								}
							}
							
							
					}
					}
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}



    };



		





	    private static String formatInterval(final long l)

	    {



	    	return String.format("%02d:%02d:%02d", l/(1000*60*60), (l%(1000*60*60))/(1000*60), ((l%(1000*60*60))%(1000*60))/1000);



	    }





	public class LocalBinder extends Binder {

		LocalService getService() {

    	//Log.d(getClass().getSimpleName(), "getservice() localservice");

    	 return LocalService.this;

    }

}





	@Override

	 public IBinder onBind(Intent intent) {



		Log.d(getClass().getSimpleName(), "onbind() "+intent.getAction());

		Log.d(getClass().getSimpleName(), "onbind() localservice");



		if (intent.getAction().equals("OsMoDroid.remote")){

			Log.d(getClass().getSimpleName(), "binded remote");

		return rBinder;}

		Log.d(getClass().getSimpleName(), "binded localy");

		return mBinder;



    }







public void refresh(){

	in.removeExtra("startmessage");
	in.putExtra("position", position+"\n"+Sattelite+" "+"Точность: "+Accuracy);
	in.putExtra("sendresult", sendresult);
	in.putExtra("sendcounter", sendcounter);
	in.putExtra("buffercounter", buffercounter);
	in.putExtra("stat", "Максимальная: "+df1.format(maxspeed*3.6)+" км/ч\n"+"Средняя: "+df1.format(avgspeed*3600)+" км/ч\n"+"Пробег: "+df2.format(workdistance/1000) + " км"+"\n"+"Время работы: "+formatInterval(timeperiod));
	in.putExtra("started", state);

	sendBroadcast(in);

}



public void startcomand()

{

	String strVersionName = getString(R.string.Unknow);

	String version=getString(R.string.Unknow);

	Log.d(getClass().getSimpleName(), "startcommand");

	try {

		PackageInfo packageInfo = getPackageManager().getPackageInfo(

				getPackageName(), 0);

		strVersionName = packageInfo.packageName + " "

				+ packageInfo.versionName;

		 version = packageInfo.versionName;

	} catch (NameNotFoundException e) {

		//e.printStackTrace();

	}

//	if (!settings.getString("key", "").equals("")&&!settings.getString("device", "").equals("")){
//
//		String[] a={"device","c","v"};
//
//		String[] b={settings.getString("device", ""),"OsMoDroid",version.replace(".", "")};
//
//		String[] params = {netutil.buildcommand(this,"start",a,b),"false","","start"};
//
//		starttask=	new netutil.MyAsyncTask(this);
//
//		starttask.execute(params) ;
//
//		Log.d(getClass().getSimpleName(), "startcommand");
//
//	}

	//else {

		

		String[] params = {"http://a.t.esya.ru/?act=start&hash="+settings.getString("hash", "")+"&n="+settings.getString("n", "")+"&c=OsMoDroid&v="+version.replace(".", ""),"false","","start"};

		starttask=	new netutil.MyAsyncTask(this);

		starttask.execute(params) ;

		Log.d(getClass().getSimpleName(), "startcommand");

		

	//}

	//a.t.esya.ru/?act=session_start&hash=*&n=*&ttl=900

	//String[] a={"hasn","n","ttl"};

	//String[] b={settings.getString("hash", ""),settings.getString("n", ""),"900",};





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

	//		Log.d(getClass().getSimpleName(), "position() localservice");

			String result = getString(R.string.NotDefined)+"\n"+getString(R.string.speed);
			if (position == null) {return result;}

			else {return position;}

		}



		public  String getSendResult()  {

		//	Log.d(getClass().getSimpleName(), "sendresult() localservice");

		//		if (sendresult == null) return "";

			//else





			  return sendresult;

		}



		public void sendPosition() {

		Location forcelocation = myManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		Location forcenetworklocation = myManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		//Log.d(this.getClass().getName(), forcelocation.toString());



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

				//Log.d(this.getClass().getName(), "sendbuffer отправляемый "+sendbuffer);



				forcesend.execute(URLadr," ");

			}



		}

		else{



			if (position==null){position = ( "Ш:" + df6.format(forcelocation.getLatitude())+ " Д:"+  df6.format( forcelocation.getLongitude())+" С:" +df1.format(forcelocation.getSpeed()*3.6));}

			URLadr="http://t.esya.ru/?"+  df6.format( forcelocation.getLatitude()) +":"+ df6.format( forcelocation.getLongitude())+":"+ df1.format(forcelocation.getAccuracy())

				+":"+df1.format( forcelocation.getAltitude())+":"+df1.format( forcelocation.getSpeed())+":"+hash+":"+n;

		Log.d(this.getClass().getName(), URLadr);



			SendCoor forcesend = new SendCoor();

			//Log.d(this.getClass().getName(), "sendbuffer отправляемый "+sendbuffer);



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

		 //Debug.startMethodTracing("startsbuf");

		super.onCreate();

		serContext=LocalService.this;

		myManager = (LocationManager) getSystemService(LOCATION_SERVICE);



		Sattelite=getString(R.string.Sputniki);

		position=getString(R.string.NotDefined)+ "\n"+getString(R.string.speed);



		sdf1.setTimeZone(TimeZone.getTimeZone("UTC"));

		settings = PreferenceManager.getDefaultSharedPreferences(this);

		currentLocation = new Location("");
		currentLocation.setLatitude((double)settings.getFloat("lat", 0f));
		currentLocation.setLongitude((double)settings.getFloat("lon", 0f));







		//Locale. .setDefault(Locale.US);

	   // DecimalFormat df = new DecimalFormat("%.6f");

		//DecimalFormatSymbols dot= new DecimalFormatSymbols();

		dot.setDecimalSeparator('.');

	df1.setDecimalSeparatorAlwaysShown(false);

	df6.setDecimalSeparatorAlwaysShown(false);

		df1.setDecimalFormatSymbols(dot);

		df6.setDecimalFormatSymbols(dot);





		//sendbuffer = new String[2048];



		ReadPref();

		deviceAdapter = new DeviceAdapter(getApplicationContext(),R.layout.deviceitem, LocalService.deviceList);
		channelsAdapter = new ChannelsAdapter(getApplicationContext(),R.layout.deviceitem, LocalService.channelList);

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

				Log.d(getClass().getSimpleName(), "Заряд:"+batteryprocent);

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



			//	if(playsound&&gpson!=null &&!gpson.isPlaying())gpson.start();
				if (playsound){soundPool.play(gpson, 1f, 1f, 1, 0, 1f);}
				//Log.d(getClass().getSimpleName(), "Звук он");

			} else {gpsbeepedon=false;}

			if ( System.currentTimeMillis()>lastgpsofftime+notifyperiod &&gpsbeepedoff)

			{

				//Log.d(getClass().getSimpleName(), "Звук офф");

				if(vibrate)vibrator.vibrate(vibratetime);



				//if(playsound &&gpsoff!=null&& !gpsoff.isPlaying())gpsoff.start();
				if (playsound){soundPool.play(gpsoff, 1f, 1f, 1, 0, 1f);}

			} else {gpsbeepedoff=false;}











			}



		};



		remoteControlReciever = new BroadcastReceiver() {



			@Override

			public void onReceive(Context context, Intent intent) {

				Log.d(getClass().getSimpleName(), "remoteControlReciever intent "+intent);

				if (intent.getStringExtra("command").equals("start")){
                                    if (!state){
                                        startServiceWork();
                                        try {
                                            Pong(context);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
				}

				if (intent.getStringExtra("command").equals("stop")){
                                    if (state){
                                        stopServiceWork(true);
                                        try {
                                            Pong(context);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
				}

				if (intent.getStringExtra("command").equals("ping")){
                                    try {
                                        Pong(context);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
				}

				if (intent.getStringExtra("command").equals("batteryinfo")){
                                    try {
                                        batteryinfo(context);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
				}

				if (intent.getStringExtra("command").equals("closeclient")){
                                    //try {
                                        //Intent i = new Intent(this, LocalService.class);
                                        //stopService(intent);
                                        //finish();
                                    //} catch (JSONException e) {
                                    //    e.printStackTrace();
                                    //}
                                    Toast.makeText(LocalService.this, "Попытка закрыть клиент удалённо" , Toast.LENGTH_SHORT).show();
				}
				
				if (intent.getStringExtra("command").equals("getpreferences")){
                    try {
                    	getpreferences(context);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
}



			}

		};

		registerReceiver( remoteControlReciever, new IntentFilter( "OsMoDroid_Control"));



		receiver = new BroadcastReceiver() {

			@Override

			public void onReceive(Context context, Intent intent) {

				boolean gpxfix = false;

				gpxfix=	intent.getBooleanExtra("enabled", false);

				if(gpxfix){//if (lastgpsontime+notifyperiod<System.currentTimeMillis()){



					//if(vibrate)vibrator.vibrate(vibratetime);



					//if(playsound)gpson.start();

					lastgpsontime=System.currentTimeMillis();

					gpsbeepedon=true;
					
					if (playsound&&!firstgpsbeepedon){
						firstgpsbeepedon=true;
						soundPool.play(gpson, 1f, 1f, 1, 0, 1f);}

				//}

				}

				else {

					//if (lastgpsofftime+notifyperiod<System.currentTimeMillis()){

					//if(vibrate)vibrator.vibrate(vibratetime);

					//if(playsound)gpsoff.start();

					lastgpsofftime=System.currentTimeMillis();

					gpsbeepedoff=true;

					//}

				}

			}







				//Toast.makeText(LocalService.this, Boolean.toString(gpxfix) , Toast.LENGTH_SHORT).show();

			//	Log.d("lbr", "Message received: " + intent.getStringExtra("position") );





		};

















		 //try {

		soundPool = new SoundPool(10, AudioManager.STREAM_NOTIFICATION, 0);
		
		gpson = soundPool.load(this, R.raw.gpson, 1);
		
		gpsoff = soundPool.load(this, R.raw.gpsoff, 1);
		
		ineton = soundPool.load(this, R.raw.ineton, 1);
		
		inetoff = soundPool.load(this, R.raw.inetoff, 1);
		
		sendpalyer = soundPool.load(this, R.raw.sendsound, 1);
		
		startsound = soundPool.load(this, R.raw.start, 1);
		
		stopsound = soundPool.load(this, R.raw.stop, 1);
		
//		gpson = MediaPlayer.create(this, R.raw.gpson);
//
//		gpsoff = MediaPlayer.create(this, R.raw.gpsoff);
//
//		ineton = MediaPlayer.create(this, R.raw.ineton);
//
//		inetoff = MediaPlayer.create(this, R.raw.inetoff);
//
//		sendpalyer=MediaPlayer.create(this, R.raw.sendsound);


	//  } catch (Exception e) {





		  // Should not happen.



	//    }



//		if (gpson!=null){ gpson.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);}
//		if (gpsoff!=null){ gpsoff.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);}
//		if (ineton!=null){ ineton.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);}
//		if (inetoff!=null){inetoff.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);}
//		if (sendpalyer!=null){ sendpalyer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);}
		//sendpalyer.setVolume(leftVolume, rightVolume)

		//Log.d(getClass().getSimpleName(), "oncreate() localservice");
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

		in = new Intent("OsMoDroid");
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

///////////////////////
s = new Socket( );
//sockaddr = new InetSocketAddress("esya.ru", 2145);
///////////////////////

if (live){

	if (isOnline()){

	startcomand();
	if (!settings.getString("key", "" ).equals("") ){

		netutil.newapicommand((ResultsListener)LocalService.this, "om_device");
		netutil.newapicommand((ResultsListener)LocalService.this, "om_device_channel_adaptive:"+settings.getString("device", ""));

	}
	}

	else {

		 onlinePauseforStartReciever = new BroadcastReceiver() {



			@Override

			public void onReceive(Context context, Intent intent) {

				Log.d(this.getClass().getName(), "OnlinePauseforStartReciever"+this);

				Log.d(this.getClass().getName(), "OnlinePauseforStartReciever"+this+" Intent:"+intent);

				if (intent.getAction().equals(android.net.ConnectivityManager.CONNECTIVITY_ACTION)) {

					Bundle extras = intent.getExtras();

					Log.d(this.getClass().getName(), "OnlinePauseforStartReciever"+this+ " "+intent.getExtras());

					if(extras.containsKey("networkInfo")) {

						NetworkInfo netinfo = (NetworkInfo) extras.get("networkInfo");

						Log.d(this.getClass().getName(), "OnlinePauseforStartReciever"+this+ " "+netinfo);

						Log.d(this.getClass().getName(), "OnlinePauseforStartReciever"+this+ " "+netinfo.getType());

						if(netinfo.isConnected()) {

							Log.d(this.getClass().getName(), "OnlinePauseforStartReciever"+this+" Network is connected");

							startcomand();
							if (!settings.getString("key", "" ).equals("") ){

								netutil.newapicommand((ResultsListener)LocalService.this, "om_device");

							}

							unregisterReceiver(onlinePauseforStartReciever);

						}

						else {

							System.out.println("OnlinePauseforStartReciever offline"+this.toString());

						}

					}

					else if(extras.containsKey("noConnectivity")) {

						System.out.println("OnlinePauseforStartReciever offline2"+this.toString());

					}

			    }

			}

		};

		registerReceiver(onlinePauseforStartReciever, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));







	}



}



if (settings.getBoolean("im", false) && !settings.getString("key", "" ).equals("") ){

//mesIM = new IM(settings.getString("key", "")+",im_messages,om_online",this,1);
	ArrayList<String[]> longPollchannels =new ArrayList<String[]>();
	longPollchannels.add(new String[] {"om_online","o"}); 
	longPollchannels.add(new String[] {"im_messages","m"});
	

	
if(!settings.getString("lpch", "").equals("")){ 
		//	myIM = new IM(settings.getString("lpch", "")+"ctrl",this,0);
	longPollchannels.add(new String[] {settings.getString("lpch", "")+"ctrl","r"});
			}

myIM = new IM( longPollchannels ,this,settings.getString("key", ""));	
}




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
	
	void getpreferences(Context context) throws JSONException{
        JSONObject postjson = new JSONObject();
        //for (Channel channel : LocalService.channelList)
//        for (Entry header : settings.getAll().entrySet()) {
//        	
//        postjson.put((String) header.getKey(), header.getValue());
//        
//        }
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


	@Override

	public void onDestroy() {

		super.onDestroy();

		if (state){ stopServiceWork(true);}

		if(myIM!=null){  myIM.close();}

		
				

		stopcomand();



	

		try {

		if(receiver!= null){unregisterReceiver(receiver);}

		} catch (Exception e) {

			Log.d(getClass().getSimpleName(), "А он и не зареген");



		}

		try {

		if(checkreceiver!= null){unregisterReceiver(checkreceiver);}

	} catch (Exception e) {

		Log.d(getClass().getSimpleName(), "А он и не зареген");



	}

		try {

			if(onlinePauseforStartReciever!= null){unregisterReceiver(onlinePauseforStartReciever);}

		} catch (Exception e) {

			Log.d(getClass().getSimpleName(), "А он и не зареген");



		}

		try {

			if(remoteControlReciever!= null){unregisterReceiver(remoteControlReciever);}

		} catch (Exception e) {

			Log.d(getClass().getSimpleName(), "А он и не зареген");



		}

		try {

			if(remoteControlReciever!= null){unregisterReceiver(batteryReciever);}

		} catch (Exception e) {

			Log.d(getClass().getSimpleName(), "А он и не зареген");



		}







		//Log.d(getClass().getSimpleName(), "omdestroy() localservice");

if (soundPool!=null) {soundPool.release();}

//		if (gpson!=null) gpson.stop();
//
//		if (gpsoff!=null) gpsoff.stop();
//
//		if (ineton!=null) ineton.stop();
//
//		if (inetoff!=null) inetoff.stop();
//
//		if (sendpalyer!=null) sendpalyer.stop();
//
//		if (gpson!=null) gpson.reset();
//
//		if (gpsoff!=null) gpsoff.reset();
//
//		if (ineton!=null) ineton.reset();
//
//		if (inetoff!=null) inetoff.reset();
//
//		if (sendpalyer!=null) sendpalyer.reset();
//
//		if (gpson!=null) gpson.release();
//
//		if (gpsoff!=null) gpsoff.release();
//
//		if (ineton!=null) ineton.release();
//
//		if (inetoff!=null) inetoff.release();
//
//		if (sendpalyer!=null) sendpalyer.release();

		if (!(wakeLock==null) &&wakeLock.isHeld())wakeLock.release();

	if (!(LocwakeLock==null)&&LocwakeLock.isHeld())LocwakeLock.release();

		if (!(SendwakeLock==null)&&SendwakeLock.isHeld())SendwakeLock.release();



		//String ns = Context.NOTIFICATION_SERVICE;

		//NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);



		mNotificationManager.cancelAll();





		// Debug.stopMethodTracing();

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

		super.onStart(intent, startId);





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
		Log.d(getClass().getSimpleName(), "applyPreferecne end");

	}
	

	public void startServiceWork (){

		firstsend=true;

		avgspeed=0;

		maxspeed=0;

		workdistance=0;

		timeperiod=0;

		workmilli=0;

		prevlocation_gpx=null;

		prevlocation=null;

		prevlocation_spd=null;

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
CharSequence tickerText = "Мониторинг запущен"; //getString(R.string.Working);
long when = System.currentTimeMillis();
Notification notification = new Notification(icon, tickerText, when);
Intent notificationIntent = new Intent(this, GPSLocalServiceClient.class);
notificationIntent.setAction(Intent.ACTION_MAIN);
notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
notification.setLatestEventInfo(getApplicationContext(), "OsMoDroid", "Мониторинг активен", contentIntent);



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

setForeground(true);

mNotificationManager.notify(OSMODROID_ID, notification);



} else

{

invokeMethod(mStartForeground, mStartForegroundArgs);

}







setstarted(true);

if (live){

String[] params = {"http://a.t.esya.ru/?act=session_start&hash="+settings.getString("hash", "")+"&n="+settings.getString("n", "")+"&ttl="+settings.getString("session_ttl", "30"),"false","","session_start"};

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







	private void requestLocationUpdates() {
		if (usecourse)	{
		
			//Log.d(this.getClass().getName(), "Запускаем провайдера 0 0");
		
		
		
				prevlocation= myManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				
				
		
		
		
				myManager.removeUpdates(this);
		
				myManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		
			if (settings.getBoolean("usenetwork", true)){	myManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);}
		
				myManager.addGpsStatusListener(this);
		
				}
		
		else	{
		
			//Log.d(this.getClass().getName(), "Запускаем провайдера по настройкам");
		
		
		
			prevlocation= myManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		
		
			//myManager.removeUpdates(this);
		
		
		
			if (period>=period_gpx&&gpx){gpsperiod=period_gpx;}else {gpsperiod=period;};
		
			if (distance>=distance_gpx&&gpx){gpsdistance=distance_gpx;}else {gpsdistance=distance;};
		
			//Log.d(this.getClass().getName(), "период"+gpsperiod+"meters"+gpsdistance);
		
			myManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, gpsperiod, 0, LocalService.this);
		
			if (settings.getBoolean("usenetwork", true)){	myManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);}
		
			myManager.addGpsStatusListener(this);
		
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

		SharedPreferences.Editor editor = settings.edit();

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
                    String[] params = {"http://a.t.esya.ru/?act=session_stop&hash="+settings.getString("hash", "")+"&n="+settings.getString("n", ""),"false","","session_stop"};
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

			setForeground(false);



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
	}





	private void setstarted(boolean started){

		//Log.d(getClass().getSimpleName(), "setstarted() localservice");

		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

		SharedPreferences.Editor editor = settings.edit();

        editor.putBoolean("started", started);

        editor.commit();

        state=started;

        refresh();

	}

	public class SendCoor extends AsyncTask<String, String, String> {





		private  String tmp;

		protected void onPostExecute(String result) {

			// Toast.makeText(LocalService.this, text,Toast.LENGTH_LONG ).show();

			//Log.d(this.getClass().getName(), "Отправка завершилась.");

//			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

			//if(usebuffer&&tmp.equals("false")){

				//sended=false;

				//if (sendbuffer.equals("")){sendbuffer=sendbuffer+lastsendforbuffer;} else{sendbuffer=sendbuffer+"&"+lastsendforbuffer;}

			//}

			Log.d(getClass().getSimpleName(), "SendCoorOnPostExecute, Result="+result);

			if (result.equals("NoConnection")){

				 sendresult=getString(R.string.NoConnection);

				 internetnotify(true);



			}

			else {





			String time = sdf3.format(new Date(System.currentTimeMillis()));

			 sendresult= decodesendresult(result);

			 if (sended){

				 sendcounter=sendcounter+1;

				 //if( sendsound &&sendpalyer!=null&& !sendpalyer.isPlaying())sendpalyer.start();
				if (sendsound){ soundPool.play(sendpalyer, 1f, 1f, 1, 0, 1f);}


			 sendresult= time +" "+sendresult;}

			 else

			 {

				 sendresult= time +" "+sendresult;







		}

			}

			refresh();

			// if (!tmp.equals(R.string.NoConnection)){ internetnotify(true); sended=false;}

			// SendwakeLock.release();





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

		currentLocation.set(location);
		
		if (LocalService.channelsDevicesAdapter!=null&&LocalService.currentChannel!=null)
		{
			 Log.d(this.getClass().getName(), "Adapter:"+ LocalService.channelsDevicesAdapter.toString());
			
			 for (Channel channel:LocalService.channelList){
					if(channel.u.equals(LocalService.currentChannel.u)){
						LocalService.currentchanneldeviceList.clear();
						LocalService.currentchanneldeviceList.addAll(channel.deviceList);
				 }
					
				}
			 
			 
			 LocalService.channelsDevicesAdapter.notifyDataSetChanged();
		}
		
		Accuracy=Float.toString(location.getAccuracy());

		if (System.currentTimeMillis()<lastgpslocationtime+gpsperiod+1000 && location.getProvider().equals(LocationManager.NETWORK_PROVIDER))

		{

			Log.d(this.getClass().getName(),"У нас есть GPS еще");

			return;



		}

		//LocwakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "LocWakeLock");

		//	LocwakeLock.acquire();



			if (prevlocation_gpx==null)prevlocation_gpx=location;

			if (prevlocation==null)prevlocation=location;

			if (prevlocation_spd==null&&location.getProvider().equals(LocationManager.GPS_PROVIDER))prevlocation_spd=location;



		//Toast.makeText(getApplicationContext(), location.getProvider(), 1).show();



		if (System.currentTimeMillis()>lastgpslocationtime+gpsperiod+1000 && location.getProvider().equals(LocationManager.NETWORK_PROVIDER))

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



workmilli= System.currentTimeMillis();

		//Log.d(this.getClass().getName(),"workmilli="+ Float.toString(workmilli));

		firstsend=false;

		}

		if (location.getSpeed()>maxspeed){

			maxspeed=location.getSpeed();

		}





		if (location.getSpeed()>=speed_gpx/3.6 && (int)location.getAccuracy()<hdop_gpx && prevlocation_spd!=null ){

		workdistance=workdistance+location.distanceTo(prevlocation_spd);

		Log.d(this.getClass().getName(),"Log of Workdistance, Workdistance="+ Float.toString(workdistance)+" location="+location.toString()+" prevlocation_spd="+prevlocation_spd.toString()+" distanceto="+Float.toString(location.distanceTo(prevlocation_spd)));

		prevlocation_spd.setLatitude(location.getLatitude());

		prevlocation_spd.setLongitude(location.getLongitude());

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

		prevlocation_gpx.setLatitude(location.getLatitude());

		prevlocation_gpx.setLongitude(location.getLongitude());

		prevlocation_gpx.setTime(location.getTime());

		prevbrng_gpx=brng_gpx;

		writegpx(location);

	}

	}

	else {

		//Log.d(this.getClass().getName(), "Пишем трек без курса");

		if (location.getSpeed()>=speed_gpx/3.6&&(int)location.getAccuracy()<hdop_gpx&&(location.distanceTo(prevlocation_gpx)>distance_gpx || location.getTime()>(prevlocation_gpx.getTime()+period_gpx) ))

		{	writegpx(location);

		prevlocation_gpx.setLatitude(location.getLatitude());

		prevlocation_gpx.setLongitude(location.getLongitude());

		prevlocation_gpx.setTime(location.getTime());

		}

		}



}

		if (!hash.equals("") && live&&sessionstarted)

		{

		if(usecourse){

			//Log.d(this.getClass().getName(), "Попали в проверку курса для отправки");

			 //Log.d(this.getClass().getName(), "Accuracey"+location.getAccuracy()+"hdop"+hdop);

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





			prevlocation.setLatitude(location.getLatitude());

			prevlocation.setLongitude(location.getLongitude());

			prevlocation.setTime(location.getTime());

			prevbrng=brng;
			Log.d(this.getClass().getName(), "send(location)="+location);
		sendlocation ( location);





		}



		}

		 else {

			 //Log.d(this.getClass().getName(), "Отправляем без курса");

			 if ((int)location.getAccuracy()<hdop &&location.getSpeed()>=speed/3.6 &&(location.distanceTo(prevlocation)>distance || location.getTime()>(prevlocation.getTime()+period)))

			 {

				 //Log.d(this.getClass().getName(), "Accuracey"+location.getAccuracy()+"hdop"+hdop);

					prevlocation.setLatitude(location.getLatitude());

					prevlocation.setLongitude(location.getLongitude());

					prevlocation.setTime(location.getTime());
					Log.d(this.getClass().getName(), "send(location)="+location);
				 sendlocation (location);

			 }



		 }



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

		 notifywarnactivity("Команда:Отправка положения "+str);

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
	int Count = 0;
	int CountFix = 0;
	boolean hasA = false;
	boolean hasE = false;

	GpsStatus xGpsStatus = myManager.getGpsStatus(null) ;

	Iterable<GpsSatellite> iSatellites = xGpsStatus.getSatellites() ;

	Iterator<GpsSatellite> it = iSatellites.iterator() ;

	while ( it.hasNext() ) {
		GpsSatellite oSat = (GpsSatellite) it.next() ;

		Count=Count+1;

		hasA = oSat.hasAlmanac();
		hasE = oSat.hasEphemeris();

		if(oSat.usedInFix() ){
			CountFix=CountFix+1;
			if(oSat.getPrn()>MaxPrn) MaxPrn=oSat.getPrn();
			//Log.e("A fost folosit ", "int fix!");
		}
	}

	Sattelite=getString(R.string.Sputniki)+Count+":"+CountFix; //+" ("+hasA+"-"+hasE+")";
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

        	   tts.speak("Поехали!", TextToSpeech.QUEUE_ADD, null);





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



	void notifywarnactivity(String info) {



		Long when=System.currentTimeMillis();

		Intent notificationIntent = new Intent(this, WarnActivity.class);

		notificationIntent.removeExtra("info");

		notificationIntent.putExtra("info", info);

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












public void onResultsSucceeded(APIComResult result) {
	JSONArray a = null;




	if (result.Jo==null&&result.ja==null)

	{



		Log.d(getClass().getSimpleName(),"notifwar1 Команда:"+result.Command+" Ответ сервера:"+result.rawresponse+ " Запрос:"+result.url);

	//		notifywarnactivity("Команда:"+result.Command+" Ответ сервера:"+result.rawresponse+ " Запрос:"+result.url);
		Toast.makeText(LocalService.this, "Esya.ru не отвечает. Проверьте работу интернета." , Toast.LENGTH_LONG).show();

		}

	if (!(result.Jo==null)) {
		if (result.Jo.has("error")){

		Log.d(getClass().getSimpleName(),"notifwar2:"+result.Jo.optString("error")+" "+result.Jo.optString("error_description"));

		notifywarnactivity("Команда:"+result.Command+" Код ошибки:"+result.Jo.optString("error")+" Расшифровка:"+result.Jo.optString("error_description")+" Запрос:"+result.url);
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
		//Toast.makeText(this,result.Jo.optString("state")+" "+ result.Jo.optString("error_description:ru"),5).show();
		if (!result.Jo.optString("lpch").equals("")&& !result.Jo.optString("lpch").equals(settings.getString("lpch", ""))){

			ArrayList<String[]> longPollchannels =new ArrayList<String[]>();
			longPollchannels.add(new String[] {settings.getString("lpch", "")+"ctrl","r"});
			
if (myIM!=null){
		myIM.removechannels(longPollchannels);	
}
			
			SharedPreferences.Editor editor = settings.edit();

			editor.putString("lpch", result.Jo.optString("lpch"));

			editor.commit();

//			if (settings.getBoolean("im", false)){
//if(myIM!=null){  myIM.close();}
//			myIM = new IM(settings.getString("lpch", "")+"ctrl",this,0);}

			longPollchannels =new ArrayList<String[]>();
			longPollchannels.add(new String[] {settings.getString("lpch", "")+"ctrl","r"});
			
if (myIM!=null){
		myIM.addchannels(longPollchannels);	
}
			
		}
		if(!result.Jo.optString("device").equals("")){
			SharedPreferences.Editor editor = settings.edit();

			editor.putString("device", result.Jo.optString("device"));
			editor.putString("view-url", "http://m.esya.ru/"+result.Jo.optString("device_url"));
			editor.putString("devicename", result.Jo.optString("device_name"));
			
			editor.commit();
			refresh();
			
		}
		
		if(!result.Jo.optString("session_ttl").equals("")){
			SharedPreferences.Editor editor = settings.edit();

			editor.putString("session_ttl", result.Jo.optString("session_ttl"));
			
			
			editor.commit();
			
			
		}
		

				if (!result.Jo.optString("motd").equals("") ||!result.Jo.optString("query_per_day").equals("")){

				in.putExtra("startmessage", result.Jo.optString("motd")+"\n"+ "Отправок в день:" +result.Jo.optString("query_per_day")

				+"\n"+ "Отправок в неделю:"+result.Jo.optString("query_per_week")+ "\n" +"Отправок в месяц:"+result.Jo.optString("query_per_month"));

				sendBroadcast(in);

			}

	}

	if (result.Command.equals("APIM")&& !(result.Jo==null))




	{

		Log.d(getClass().getSimpleName(),"APIM Response:"+result.Jo);

		if (result.Jo.has("om_device")){
			deviceList.clear();

			try {
				  a =	result.Jo.getJSONArray("om_device");
		 		  Log.d(getClass().getSimpleName(), a.toString());
		 		 for (int i = 0; i < a.length(); i++) {
		 			JSONObject jsonObject = a.getJSONObject(i);
		Device devitem = new Device(jsonObject.getString("u"), jsonObject.getString("name"),jsonObject.getString("app")
			,jsonObject.getString("last"),
			"http://m.esya.ru/"+jsonObject.getString("url"),
			jsonObject.getString("where"),
			jsonObject.getString("lat"),
			jsonObject.getString("lon"),
			jsonObject.getString("online"),
			jsonObject.getString("state")
			);

	deviceList.add(devitem);



		 		 }
				} catch (Exception e) {

					 Log.d(getClass().getSimpleName(), "эксепшн");
					//e.printStackTrace();
				}


			 Log.d(getClass().getSimpleName(),deviceList.toString());

			 if (deviceAdapter!=null) {deviceAdapter.notifyDataSetChanged();}
		}
		if (result.Jo.has("om_device_channel_adaptive:"+settings.getString("device", ""))){
			channelList.clear();

			try {
				  a =	result.Jo.getJSONArray("om_device_channel_adaptive:"+settings.getString("device", ""));
		 		  Log.d(getClass().getSimpleName(), a.toString());
		 		 for (int i = 0; i < a.length(); i++) {
		 			JSONObject jsonObject = a.getJSONObject(i);
		//Channel chanitem = new Channel( jsonObject.getString("name"),jsonObject.getString("u"),jsonObject.getString("created")	);
		 			Channel chanitem = new Channel( jsonObject);
		channelList.add(chanitem);
		if (currentChannel!=null) {
			if(chanitem.u.equals(currentChannel.u)){
				currentchanneldeviceList.clear();
				currentchanneldeviceList.addAll(chanitem.deviceList);
				 if (channelsDevicesAdapter!=null) {channelsDevicesAdapter.notifyDataSetChanged();}
			}
		}

		 		 }
		 		 
		 		 
				} catch (Exception e) {

					 Log.d(getClass().getSimpleName(), "om_device_channel_adaptive эксепшн"+e.getMessage());
					e.printStackTrace();
				}


			 Log.d(getClass().getSimpleName(),channelList.toString());
			 
			 if (channelsAdapter!=null) {channelsAdapter.notifyDataSetChanged();}
			
		}
		
		
	

		Log.d(getClass().getSimpleName(),"APIM Response:"+result.Jo);

	}





















}
}






