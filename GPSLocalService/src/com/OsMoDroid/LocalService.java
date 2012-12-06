
package com.OsMoDroid;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
//import java.util.Locale;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
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
import android.util.Log;


public class LocalService extends Service implements LocationListener,GpsStatus.Listener, TextToSpeech.OnInitListener,  ResultsListener {
	private static final int OSMODROID_ID = 1;
	int notifyid=2;
	//MediaPlayer mp;
	//BufferedReader bufferedReader;
	MediaPlayer gpson;
	MediaPlayer gpsoff;
	MediaPlayer ineton;
	MediaPlayer inetoff;
	MediaPlayer sendpalyer;
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
	private int sendcounter;
	BroadcastReceiver receiver;
	BroadcastReceiver checkreceiver;
	BroadcastReceiver mConnReceiver;
	private final IBinder mBinder = new LocalBinder();
	private String gpxbuffer= new String();
	//private String sendbuffer = new String();
	private String Sattelite="";
	private String Accuracy="";
	private boolean usebuffer = false;
	private boolean usewake=false;
	NotificationManager mNotificationManager;
	private String lastsendforbuffer="First";
	private long lastgpsontime=0;
	private long lastgpsofftime=0;
	private long notifyperiod=30000;
	private AlarmManager am;
	StringBuilder stringBuilder= new StringBuilder();
	PendingIntent pi;
	private Object[] mStartForegroundArgs = new Object[2];
	private String pass;
	private String lastsay="a";
	private netutil.MyAsyncTask imtask;
	private Boolean prevstate;
	private Boolean imrunning = true;
	private String[] imadress={};
	int gpsperiod;
	int gpsdistance;
	long prevnetworklocationtime=0;
	StringBuilder buffersb = new StringBuilder(327681);
	StringBuilder lastbuffersb = new StringBuilder(327681);
	StringBuilder sendedsb = new StringBuilder(327681);
	final private static DecimalFormat df6 = new DecimalFormat("########.######");
	final private static DecimalFormat df1 = new DecimalFormat("########.#");
	final private static DecimalFormat df2 = new DecimalFormat("########.##");
	final private static DecimalFormat df0 = new DecimalFormat("########");
	 final private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	 final private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
	 final private static SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm:ss");
	 final private static  DecimalFormatSymbols dot= new DecimalFormatSymbols();
	 TextToSpeech tts;
	    private int _langTTSavailable = -1;
	    Socket s;
	    SocketAddress sockaddr;
	    String text;
	    SharedPreferences settings;
	    private final IRemoteOsMoDroidService.Stub rBinder = new IRemoteOsMoDroidService.Stub() {

            

			public int getVersion() throws RemoteException {
				// TODO Auto-generated method stub
				return 0;
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
		
		//Log.d(getClass().getSimpleName(), "onbind() localservice");
		if (intent.equals("OsMoDroid.remote")){	
		return rBinder;}
		
		return mBinder;
		
    }

	
	
public void refresh(){
	in.removeExtra("startmessage");
	in.putExtra("position",position+"\n"+Sattelite+"\n"+"Точность:"+Accuracy);
	in.putExtra("sendresult",sendresult);
	in.putExtra("sendcounter",sendcounter);
	in.putExtra("stat", "Максимальная:"+df1.format(maxspeed*3.6)+" км/ч\n"+"Средняя:"+df1.format(avgspeed*3600)+" км/ч\n"+"Пробег:"+df2.format(workdistance/1000) + " км"+"\n"+"Интервал:"+formatInterval(timeperiod));
	sendBroadcast(in);
}
	
public void startcomand()
{
	
	Log.d(getClass().getSimpleName(), "startcommand");
	if (!settings.getString("key", "").equals("")){
		String strVersionName = getString(R.string.Unknow);
		String version=getString(R.string.Unknow);
		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			strVersionName = packageInfo.packageName + " "
					+ packageInfo.versionName;
			 version = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			//e.printStackTrace();
		}
		
		String[] a={"device","c","v"};
		String[] b={settings.getString("device", ""),"OsMoDroid",version.replace(".", "")};
		String[] params = {netutil.buildcommand(this,"start",a,b),"false","","start"};
		starttask=	new netutil.MyAsyncTask(this);
		starttask.execute(params) ;
		Log.d(getClass().getSimpleName(), "startcommand");	
	}
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
			if (position == null)return getString(R.string.NotDefined);
			else return position;
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
				if (position==null){position = ( "Ш:" + df6.format(forcenetworklocation.getLatitude())+ " Д:"+  df6.format( forcenetworklocation.getLongitude())+" С:" +df1.format(forcenetworklocation.getSpeed()*3.6));}
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
		myManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		prevstate=isOnline();
		Sattelite=getString(R.string.Sputniki);
		position=getString(R.string.NotDefined);
		Log.d(getClass().getSimpleName(),"prevstate "+ Boolean.toString(prevstate));
		sdf1.setTimeZone(TimeZone.getTimeZone("UTC"));   
		settings = PreferenceManager.getDefaultSharedPreferences(this);
		if (settings.getBoolean("im", false) && !settings.getString("lpch", "").equals("")){
		   	Log.d(getClass().getSimpleName(),"imrun");
			imrunning=true;
			String[] params = {
				"http://d.esya.ru/?identifier="+settings.getString("lpch", "")+"&ncrnd=1347794237100", "false", "",
				"messageread" };
		if (prevstate) {
		
			Log.d(getClass().getSimpleName(),"imrun and prev");
		imtask = new netutil.MyAsyncTask(LocalService.this) ;
		imtask.execute(params);
		}
		}
		
		tts = new TextToSpeech(this,
		        (OnInitListener) this  // TextToSpeech.OnInitListener
		        );
		
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
		String alarm = Context.ALARM_SERVICE;
		 am = ( AlarmManager ) getSystemService( alarm );
		
		
		 
		Intent intent = new Intent( "CHECK_GPS" );
 pi = PendingIntent.getBroadcast( this, 0, intent, 0 );
		 
	
		 
	
		
		   mConnReceiver = new BroadcastReceiver() {
		        @Override
		        public void onReceive(Context context, Intent intent) {
		            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
		            String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
		boolean isFailover = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);
		            NetworkInfo currentNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
		            NetworkInfo otherNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);
		            Log.d(getClass().getSimpleName(), reason + " NoConnectivity:" +Boolean.toString(noConnectivity)+ "IsFailover: "+Boolean.toString(isFailover));
		            if (settings.getBoolean("im", false)){
		            if (!noConnectivity&& !prevstate&& imrunning && !settings.getString("lpch", "").equals("")){
		            	String[] params = {
		            			"http://d.esya.ru/?identifier="+settings.getString("lpch", "")+"&ncrnd=1347794237100", "false", "",
		            			"messageread" };
		            	Log.d(getClass().getSimpleName(),"prevstate "+ Boolean.toString(prevstate));
		        		prevstate=true;
		        		imtask = new netutil.MyAsyncTask(LocalService.this) ;
		        		Log.d(getClass().getSimpleName(), "Новая задача по ресиверу");
		        		imtask.execute(params);
		        		
		            	
		            }
		            if (noConnectivity&& imrunning && prevstate){
		            	Log.d(getClass().getSimpleName(),"prevstate "+ Boolean.toString(prevstate));
		            	prevstate=false;
		            //	imtask.cancel(true);
		            //	imtask.Close();
		            	
		            }
		            ;
		            }
		            // do application-specific task(s) based on the current network state, such
		            // as enabling queuing of HTTP requests when currentNetworkInfo is connected etc.
		        }
		    }; 
		
		
		checkreceiver =new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent)
			{
			if ( System.currentTimeMillis()>lastgpsontime+notifyperiod && gpsbeepedon)
			{
				if(vibrate)vibrator.vibrate(vibratetime);
				
				if(playsound&&gpson!=null &&!gpson.isPlaying())gpson.start();
				//Log.d(getClass().getSimpleName(), "Звук он");
			} else {gpsbeepedon=false;}
			if ( System.currentTimeMillis()>lastgpsofftime+notifyperiod &&gpsbeepedoff)
			{
				//Log.d(getClass().getSimpleName(), "Звук офф");
				if(vibrate)vibrator.vibrate(vibratetime);
				
				if(playsound &&gpsoff!=null&& !gpsoff.isPlaying())gpsoff.start();
			} else {gpsbeepedoff=false;}
				
				
				
				
				
			}
							
		};
		
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
			
			
		
		 
		
		registerReceiver(mConnReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)); 
		
		 
		 //try {
		gpson = MediaPlayer.create(this, R.raw.gpson);
		gpsoff = MediaPlayer.create(this, R.raw.gpsoff);
		ineton = MediaPlayer.create(this, R.raw.ineton);
		inetoff = MediaPlayer.create(this, R.raw.inetoff);
		sendpalyer=MediaPlayer.create(this, R.raw.sendsound);
		
	//  } catch (Exception e) {
	     
		  
		  // Should not happen.
	        
	//    }
		
		if (gpson!=null){ gpson.setAudioStreamType(AudioManager.STREAM_MUSIC);}
		if (gpsoff!=null){ gpsoff.setAudioStreamType(AudioManager.STREAM_MUSIC);}
		if (ineton!=null){ ineton.setAudioStreamType(AudioManager.STREAM_MUSIC);}
		if (inetoff!=null){inetoff.setAudioStreamType(AudioManager.STREAM_MUSIC);}
		if (sendpalyer!=null){ sendpalyer.setAudioStreamType(AudioManager.STREAM_MUSIC);}
		//sendpalyer.setVolume(leftVolume, rightVolume)
	
		//Log.d(getClass().getSimpleName(), "oncreate() localservice");
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
	if (usewake){
		wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "MyWakeLock");
		wakeLock.acquire();
	}
		in = new Intent("OsMoDroid");

		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		int icon = R.drawable.eye;
		CharSequence tickerText = "Ждущий режим";// getString(R.string.Working);
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText, when);
	
		Intent notificationIntent = new Intent(this, GPSLocalServiceClient.class);
		notificationIntent.setAction(Intent.ACTION_MAIN);

		notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		//notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP); 
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		notification.setLatestEventInfo(getApplicationContext(), "OsMoDroid", "", contentIntent);
		mStartForegroundArgs[0]= OSMODROID_ID;
		mStartForegroundArgs[1]= notification;
		Method mStartForeground;
		
		 final Class<?>[] mStartForegroundSignature = new Class[] {
			    int.class, Notification.class};
		
		try {
				mStartForeground = getClass().getMethod("startForeground",
		                mStartForegroundSignature);
				
		      		      
		    } catch (Exception e) {
		        // Running on an older platform.
		        mStartForeground =  null;
		    }
if (mStartForeground == null){
setForeground(true);
mNotificationManager.notify(OSMODROID_ID, notification);

} else
	{
	invokeMethod(mStartForeground, mStartForegroundArgs);
	}
		

		
		
	     s = new Socket( );
          sockaddr = new InetSocketAddress("esya.ru", 2145);
startcomand();
          
		
	}
	
	



	@Override
	public void onDestroy() {
		super.onDestroy();
		
		stopcomand();
		
		if (settings.getBoolean("im", false)){
			  imrunning=false;
			
			  try {
				imtask.cancel(true);
				  imtask.Close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		
			  Log.d(this.getClass().getName()," ondestroy imtask.close");
		
		//Boolean cancelresult= imtask.cancel(true);
		//Log.d(this.getClass().getName(), Boolean.toString(cancelresult));
		
		  }	
		if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
		try {
		if(receiver!= null){unregisterReceiver(receiver);}
		if(checkreceiver!= null){unregisterReceiver(checkreceiver);}
		if(mConnReceiver!= null){unregisterReceiver(mConnReceiver);}
			} catch (Exception e) {
				Log.d(getClass().getSimpleName(), "А он и не зареген");
			
			}
			
		
		
		//Log.d(getClass().getSimpleName(), "omdestroy() localservice");
		
		if (gpson!=null) gpson.stop();
		if (gpsoff!=null) gpsoff.stop();
		if (ineton!=null) ineton.stop();
		if (inetoff!=null) inetoff.stop();
		if (sendpalyer!=null) sendpalyer.stop();
		if (gpson!=null) gpson.reset();
		if (gpsoff!=null) gpsoff.reset();
		if (ineton!=null) ineton.reset();
		if (inetoff!=null) inetoff.reset();
		if (sendpalyer!=null) sendpalyer.reset();
		if (gpson!=null) gpson.release();
		if (gpsoff!=null) gpsoff.release();
		if (ineton!=null) ineton.release();
		if (inetoff!=null) inetoff.release();
		if (sendpalyer!=null) sendpalyer.release();
		if (!(wakeLock==null) &&wakeLock.isHeld())wakeLock.release();
	if (!(LocwakeLock==null)&&LocwakeLock.isHeld())LocwakeLock.release();
		if (!(SendwakeLock==null)&&SendwakeLock.isHeld())SendwakeLock.release();
		
		//String ns = Context.NOTIFICATION_SERVICE;
		//NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
	
		mNotificationManager.cancelAll();
		am.cancel(pi);
		
		// Debug.stopMethodTracing();
	}
	
	private void ReadPref() {
		//	Log.d(getClass().getSimpleName(), "readpref() gpsclient");
			
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
			usebuffer = settings.getBoolean("usebuffer", false);
			usewake = settings.getBoolean("usewake", false);
			notifyperiod = Integer.parseInt(settings.getString("notifyperiod", "30000").equals("") ? "30000" :settings.getString("notifyperiod","30000"));
			sendsound = settings.getBoolean("sendsound", false);
			//pass = settings.getString("pass", "");
		}
	
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		ReadPref();
		//sendbuffer="";
	int type = AlarmManager.ELAPSED_REALTIME_WAKEUP;
		
		long triggerTime = SystemClock.elapsedRealtime() + notifyperiod;
		am.setRepeating( type, triggerTime, notifyperiod, pi );
		registerReceiver( receiver, new IntentFilter( "android.location.GPS_FIX_CHANGE"));
		registerReceiver( checkreceiver, new IntentFilter( "CHECK_GPS"));
		sendcounter=0;
		boolean crtfile = false;
		if (gpx) { 
			String sdState = android.os.Environment.getExternalStorageState();
			if (sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {
			 File sdDir = android.os.Environment.getExternalStorageDirectory();
			// SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			 String time = sdf2.format(new Date());
			 fileName = new File (sdDir, "tracks/");
			 fileName.mkdirs();
			 fileName = new File(sdDir, "tracks/"+time+".gpx");
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
             trackwr.write("<gpx xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" version=\"1.0\" xmlns=\"http://www.topografix.com/GPX/1/0\" creator=\"OcMoDroid\" xsi:schemaLocation=\"http://www.topografix.com/GPX/1/0 http://www.topografix.com/GPX/1/0/gpx.xsd\">");
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
	myManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, gpsperiod, 0, this);
	if (settings.getBoolean("usenetwork", true)){	myManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);}
	myManager.addGpsStatusListener(this);	
}
int icon = R.drawable.eye2;
CharSequence tickerText ="Активный режим";// getString(R.string.Working);
long when = System.currentTimeMillis();

Notification notification = new Notification(icon, tickerText, when);
Intent notificationIntent = new Intent(this, GPSLocalServiceClient.class);
notificationIntent.setAction(Intent.ACTION_MAIN);
notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP); 
PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
notification.setLatestEventInfo(getApplicationContext(), "OsMoDroid", "", contentIntent);
mNotificationManager.notify(OSMODROID_ID, notification);
setstarted(true);
		Log.d(getClass().getSimpleName(), "notify:"+notification.toString());
	
	}
	
	public void stopServiceWork(){
		try {
			s.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (gpx&&fileheaderok) {
			try {
			 FileWriter trackwr = new FileWriter(fileName, true);
			 String towright = gpxbuffer;
				trackwr.write(towright.replace(",", "."));  
				gpxbuffer="";
			 
			 trackwr.write("</trkseg></trk></gpx>");
			 trackwr.flush();
			 trackwr.close();
			 
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(LocalService.this, getString(R.string.CanNotWriteEnd), Toast.LENGTH_SHORT).show();
			}
			}
			
			if (send != null ) {
				//Log.d(this.getClass().getName(), "Отменяем поток передачи.");
				send.cancel(true);
		} 
			if (myManager!=null){
			myManager.removeUpdates(this);}
			setstarted(false);
			int icon = R.drawable.eye;
			CharSequence tickerText ="Ждущий режим"; //getString(R.string.Working);
			long when = System.currentTimeMillis();

			Notification notification = new Notification(icon, tickerText, when);
			Intent notificationIntent = new Intent(this, GPSLocalServiceClient.class);
			notificationIntent.setAction(Intent.ACTION_MAIN);
			notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			//notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP); 
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
			notification.setLatestEventInfo(getApplicationContext(), "OsMoDroid", "", contentIntent);
			mNotificationManager.notify(OSMODROID_ID, notification);
	}
	
	
	private void setstarted(boolean started){
		//Log.d(getClass().getSimpleName(), "setstarted() localservice");
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("started", started);
        editor.commit();
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
			String time = sdf3.format(new Date(System.currentTimeMillis()));
			 sendresult= decodesendresult(result);
			 if (sended){
				 sendcounter=sendcounter+1;
				 if(sendsound &&sendpalyer!=null&& !sendpalyer.isPlaying())sendpalyer.start();
			 
			 sendresult= time +" "+sendresult;} else
			 {		 sendresult=getString(R.string.NoConnection);
			 internetnotify(true); 
		}
				in.removeExtra("startmessage");
			 in.putExtra("position",position+"\n"+Sattelite+"\n"+"Точность:"+Accuracy);
			in.putExtra("sendresult",sendresult);
			in.putExtra("sendcounter",sendcounter);
			in.putExtra("stat", "Максимальная:"+df1.format(maxspeed*3.6)+" км/ч\n"+"Средняя:"+df1.format(avgspeed*3600)+" км/ч\n"+"Пробег:"+df2.format(workdistance/1000) + " км"+"\n"+"Интервал:"+formatInterval(timeperiod));
			sendBroadcast(in);	
			// if (!tmp.equals(R.string.NoConnection)){ internetnotify(true); sended=false;}
			// SendwakeLock.release();
				
			
		   }
		protected String doInBackground(String... arg0) {
			try {
			//	 SendwakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SendWakeLock");
			//	SendwakeLock.acquire();
				//Log.d(this.getClass().getName(), "Начинаем отправку.");
				tmp=getPage(arg0[0], arg0[1]);
				//Log.d(this.getClass().getName(), "Отправка окончена.");
			} catch (IOException e) {
				internetnotify(false);
			//	e.printStackTrace();
				//sended=false;
				tmp="false";	
		
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
		Accuracy=Float.toString(location.getAccuracy());
		if (System.currentTimeMillis()<lastgpslocationtime+gpsperiod+1000 && location.getProvider().equals(LocationManager.NETWORK_PROVIDER))
		{
			Log.d(this.getClass().getName(),"У нас есть GPS еще");
			return;
		
		} 
		LocwakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "LocWakeLock");
			LocwakeLock.acquire();
			
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
		
		
		if (location.getSpeed()>0){
		workdistance=workdistance+location.distanceTo(prevlocation_spd);
		Log.d(this.getClass().getName(),"Log of Workdistance, Workdistance="+ Float.toString(workdistance)+" location="+location.toString()+" prevlocation_spd="+prevlocation_spd.toString()+" distanceto="+Float.toString(location.distanceTo(prevlocation_spd)));
		
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
		position = ( "Ш:" + df6.format(location.getLatitude())+ " Д:"+  df6.format( location.getLongitude())+" С:" +df1.format(location.getSpeed()*3.6));
		//position = ( "Ш:" + String.format("%.6f", location.getLatitude())+ " Д:"+  String.format("%.6f", location.getLongitude())+" С:" +String.format("%.1f", location.getSpeed()));
//if (location.getTime()>lastfix+3000)notifygps(false);
//if (location.getTime()<lastfix+3000)notifygps(true);

timeperiod= System.currentTimeMillis()-workmilli;
in.removeExtra("startmessage");
in.putExtra("position",position+"\n"+Sattelite+"\n"+"Точность:"+location.getAccuracy());
in.putExtra("sendresult",sendresult);
in.putExtra("sendcounter",sendcounter);
in.putExtra("stat", "Максимальная:"+df1.format(maxspeed*3.6)+" км/ч\n"+"Средняя:"+df1.format(avgspeed*3600)+" км/ч\n"+"Пробег:"+df2.format(workdistance/1000) + " км"+"\n"+"Интервал:"+formatInterval(timeperiod));
sendBroadcast(in);	

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
	in.removeExtra("startmessage");
	in.putExtra("position",position+"\n"+Sattelite+"\n"+"Точность:"+location.getAccuracy());
	in.putExtra("sendresult",sendresult);
	in.putExtra("sendcounter",sendcounter);
	in.putExtra("stat", "Максимальная:"+df1.format(maxspeed*3.6)+" км/ч\n"+"Средняя:"+df1.format(avgspeed*3600)+" км/ч\n"+"Пробег:"+df2.format(workdistance/1000) + " км"+"\n"+"Интервал:"+formatInterval(timeperiod));
	sendBroadcast(in);	
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
		if ((int)location.getAccuracy()<hdop_gpx&&(location.distanceTo(prevlocation_gpx)>distance_gpx || location.getTime()>(prevlocation_gpx.getTime()+period_gpx) ))
		{	writegpx(location);
		prevlocation_gpx.setLatitude(location.getLatitude());
		prevlocation_gpx.setLongitude(location.getLongitude());
		prevlocation_gpx.setTime(location.getTime());
		}
		}
	
}
		if (!hash.equals("") && live)
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
			in.removeExtra("startmessage");
			in.putExtra("position",position+"\n"+Sattelite+"\n"+"Точность:"+location.getAccuracy());
			in.putExtra("sendresult",sendresult);
			in.putExtra("sendcounter",sendcounter);
			in.putExtra("stat", "Максимальная:"+df1.format(maxspeed*3.6)+" км/ч\n"+"Средняя:"+df1.format(avgspeed*3600)+" км/ч\n"+"Пробег:"+df2.format(workdistance/1000) + " км"+"\n"+"Интервал:"+formatInterval(timeperiod));
			sendBroadcast(in);	
		
		if (
				(int)location.getAccuracy()<hdop && location.getSpeed()>=speed/3.6 &&
				(location.distanceTo(prevlocation)>distance || location.getTime()>(prevlocation.getTime()+period) || (location.getSpeed()>=(speedbearing/3.6) && Math.abs(brng-prevbrng)>=bearing))) 
		{	
	
			
			prevlocation.setLatitude(location.getLatitude());
			prevlocation.setLongitude(location.getLongitude());
			prevlocation.setTime(location.getTime());
			prevbrng=brng;
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
				 sendlocation (location);
			 }
				 
		 }
		
		}	
	
}	
LocwakeLock.release();	
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
		 //Log.d(this.getClass().getName(), "void getpage");
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
    	//Log.d(this.getClass().getName(),"Отправленный буфер"+ buf);
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
        	//Log.d(this.getClass().getName(),"Отправленный буфер"+ buf);
            os.flush();
            os.close();}
    	   			if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
    	   				//instream=con.getInputStream();
    	   				String  ret= inputStreamToString(con.getInputStream());
  			 
  			//Log.d(this.getClass().getName(), "void getpage end");
    	   				return ret;
  			    } 
    	   			else {
    	   				//Log.d(this.getClass().getName(), "void getpage end");
    	   				return getString(R.string.ServerError);
  			}
    	   		
         }
         
 	}

	private String inputStreamToString(InputStream in) throws IOException {
		//Log.d(this.getClass().getName(), "void input");
		
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
	    //Log.d(this.getClass().getName(), "void input end");
	    return stringBuilder.toString();
	}
private void internetnotify(boolean internet){
	if (!internet){
		if(!beepedoff){
	//long[] pattern = {0,50, 0, 30, 0, 50};
		//vibrator.vibrate(pattern, 2);
			if (vibrate)vibrator.vibrate(vibratetime);
			if (playsound &&inetoff!=null&& !inetoff.isPlaying()){
			
			inetoff.start();
			
}
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
				if (playsound &&ineton!=null&&!ineton.isPlaying()){
				
				ineton.start();
				}
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
		sended=true;
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
//	Log.d(this.getClass().getName(), "void sendlocation");
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


if (send == null ||send.getStatus().equals(AsyncTask.Status.FINISHED))
//если задач по отправке не существует или закончена
{

	if(usebuffer){
// если используется буфер
	if (sended )
	{
//и прошлая отправка успешная
		Log.d(this.getClass().getName(), "buffersb.delete "+buffersb.toString());
		buffersb.delete(0, lastbuffersb.length());
		Log.d(this.getClass().getName(), "buffersb.delete "+buffersb.toString());
	}
	else
	{
		//прошлая отправка не успешная
		Log.d(this.getClass().getName(), "buffersb.append "+buffersb.toString());
		
		
		if (buffersb.length()==0)
		{
			buffersb.append("log[]=").append(sendedsb);	
		}
		else
		{
			buffersb.append("&log[]=").append(sendedsb);
		}
		Log.d(this.getClass().getName(), "buffersb.append "+buffersb.toString());
		
	}
				}
	send = new SendCoor();	
	if(usebuffer)
	{
		lastbuffersb.setLength(0);
		lastbuffersb.append(buffersb);
		sendedsb.setLength(0);
		sendedsb.append(df6.format( location.getLatitude())).append(":").append(df6.format( location.getLongitude())).append(":").append(df1.format( location.getAltitude())).append(":").append(df1.format( location.getSpeed())).append(":").append(location.getTime()/1000);
		Log.d(this.getClass().getName(), "send.execute "+lastbuffersb.toString());
		send.execute(URLadr,lastbuffersb.toString());}
	else {send.execute(URLadr," ");}
	}

 else
{
	if(usebuffer)
	{
	if (buffersb.length()==0)
	{	Log.d(this.getClass().getName(), "2buffersb.append "+buffersb.toString());
		buffersb.append("log[]=").append(df6.format( location.getLatitude())).append(":").append(df6.format( location.getLongitude())).append(":").append(df1.format( location.getAltitude())).append(":").append(df1.format( location.getSpeed())).append(":").append(location.getTime()/1000);
		Log.d(this.getClass().getName(), "2buffersb.append "+buffersb.toString());}
	else{	Log.d(this.getClass().getName(), "3uffersb.append "+buffersb.toString());
		buffersb.append("&log[]=").append(df6.format( location.getLatitude())).append(":").append(df6.format( location.getLongitude())).append(":").append(df1.format( location.getAltitude())).append(":").append(df1.format( location.getSpeed())).append(":").append(location.getTime()/1000);
		Log.d(this.getClass().getName(), "3buffersb.append "+buffersb.toString());}
	}
}

}



public void onGpsStatusChanged(int event) {
	// TODO Auto-generated method stub
	int MaxPrn =0;
	int Count =0;
	int CountFix =0;
	GpsStatus xGpsStatus = myManager.getGpsStatus(null) ; 
	Iterable<GpsSatellite> iSatellites = xGpsStatus.getSatellites() ; 
	Iterator<GpsSatellite> it = iSatellites.iterator() ; 
	while ( it.hasNext() ) 
	{ 
	        GpsSatellite oSat = (GpsSatellite) it.next() ; 
	        Count=Count+1;
	        if(oSat.usedInFix() ){
	        	CountFix=CountFix+1;
	        	if(oSat.getPrn()>MaxPrn)MaxPrn=oSat.getPrn();
	        	//Log.e("A fost folosit ", "int fix!");
	        }
	        
//	        if(oSat.toString()!=null){
	//
//	            Log.e("Test", "SNR:"+oSat.getSnr()+"; Azimuth:"+oSat.getAzimuth()+"; Elevation:"+oSat.getElevation()+" "+oSat.toString()+"; PRN:"+oSat.getPrn());
//	        }
	} 
	if (MaxPrn>32){
		Sattelite=getString(R.string.Sputniki)+Count+":"+CountFix+getString(R.string.GlonasFindString);
//		if (!glonas){
//		if (vibrate){
//			long[] pattern = {50, 50, 50, 100, 200};
//		vibrator.vibrate(pattern, -1);}
//		if (playsound){
//		Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//		Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
//		r.play();
//		}
//		glonas=true;
//		}
	//in.putExtra("position",position+"\n"+Sattelite);
	//in.putExtra("sendcounter",sendcounter);
	//sendBroadcast(in);	
	}else {Sattelite=getString(R.string.Sputniki)+Count+":"+CountFix;
	
	//in.putExtra("position",position+"\n"+Sattelite);
	//in.putExtra("sendcounter",sendcounter);
	//sendBroadcast(in);
	};		
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
    	  Log.d(this.getClass().getName(), "Инициализация файлед");
    	  
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


public void onResultsSucceeded(APIComResult result) {
	// TODO Auto-generated method stub
	String toprint = "";
	if (result.Command.equals("im_get_all")) {
		String messagestext="";
		try {
			
		
		        JSONObject jsonObject = result.Jo.getJSONObject("messages");
		        Iterator i = jsonObject.keys();
				  while (i.hasNext())
	          	{
	          		
	String keyname= (String)i.next();          		 
	messagestext="Сообщение номер:"+jsonObject.getJSONObject(keyname).optString("u")+"\n"+"Дата:"+jsonObject.getJSONObject(keyname).optString("time")+"\n"+jsonObject.getJSONObject(keyname).optString("text");
	
	if (settings.getBoolean("usetts", false)&&tts!=null )
	{
		
		Log.d(this.getClass().getName(), "try to say message");
		tts.speak("Сообщение:"+jsonObject.getJSONObject(keyname).optString("text") , TextToSpeech.QUEUE_ADD, null);
		
	}
	          	
	          	}
		        Log.d(getClass().getSimpleName(),messagestext);
		      
		        
				Notification mesnotification = new Notification(R.drawable.eye, (CharSequence)messagestext, System.currentTimeMillis());
				mesnotification.setLatestEventInfo(getApplicationContext(), "Сообщение:"+"\n", (CharSequence)messagestext, PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0));
				mesnotification.flags |= Notification.FLAG_AUTO_CANCEL;
		mNotificationManager.notify(notifyid, mesnotification);
		        notifyid++;
		        
		        
		        LinearLayout layout = new LinearLayout(this);
				layout.setOrientation(LinearLayout.VERTICAL);
				
		        final TextView txv3 = new TextView(this);
				txv3.setText(messagestext);
				txv3.setTextSize(16);
				txv3.setBackgroundColor(Color.GREEN);
				layout.addView(txv3);
		        
		        
				Toast toast = new Toast(getApplicationContext()); 
				toast.setView(layout); 
				toast.setDuration(Toast.LENGTH_LONG);
				toast.show(); 
		        vibrator.vibrate(400);
		        
		        
			
		
					
					
		} catch (Exception e) {
			Log.d(getClass().getSimpleName(),"exeption in analise im_get_all result");
			//e.printStackTrace();
		}
		
		
		
		
		
		
	}
	
	if (result.Command.equals("start")&& !(result.Jo==null))
	{
		Toast.makeText(this,result.Jo.optString("state")+" "+ result.Jo.optString("error_description:ru"),5).show();
		if (!result.Jo.optString("lpch").equals("")){
			SharedPreferences.Editor editor = settings.edit();

			
			editor.putString("lpch", result.Jo.optString("lpch"));

			editor.commit();
		}
		
			if (!result.Jo.optString("motd").equals("")){
			
				in.removeExtra("startmessage");
				in.putExtra("position",position+"\n"+Sattelite+"\n"+"Точность:"+Accuracy);
				in.putExtra("sendresult",sendresult);
				in.putExtra("sendcounter",sendcounter);
				in.putExtra("stat", "Максимальная:"+df1.format(maxspeed*3.6)+" км/ч\n"+"Средняя:"+df1.format(avgspeed*3600)+" км/ч\n"+"Пробег:"+df2.format(workdistance/1000) + " км"+"\n"+"Интервал:"+formatInterval(timeperiod));
				in.putExtra("startmessage", result.Jo.optString("motd")+"\n"+ "Отправок в день:" +result.Jo.optString("query_per_day")
						+"\n"+ "Отправок в неделю:"+result.Jo.optString("query_per_week")+ "\n" +"Отправок в месяц:"+result.Jo.optString("query_per_month"));
				sendBroadcast(in);
				

				


				//Toast.makeText(this, result.Jo.optString("motd")+"\n"+ "Отправок в день:" +result.Jo.optString("query_per_day")+"\n"+ "Отправок в неделю:"+result.Jo.optString("query_per_week")+ "\n" +"Отправок в месяц:"+result.Jo.optString("query_per_month"),Toast.LENGTH_LONG ).show();

				//Toast.makeText(this, result.Jo.optString("motd")+"\n"+ "Отправок в день:" +result.Jo.optString("query_per_day")
					//	+"\n"+ "Отправок в неделю:"+result.Jo.optString("query_per_week")+ "\n" +"Отправок в месяц:"+result.Jo.optString("query_per_month"),Toast.LENGTH_LONG ).show();


				//Toast.makeText(this, result.Jo.optString("motd")+"\n"+ "Отправок в день:" +result.Jo.optString("query_per_day")
					//	+"\n"+ "Отправок в неделю:"+result.Jo.optString("query_per_week")+ "\n" +"Отправок в месяц:"+result.Jo.optString("query_per_month"),Toast.LENGTH_LONG ).show();

				}
			
				
				
		
			}
	
	
	
	if (result.Command.equals("messageread")) 
	{
		if (settings.getBoolean("im", false)){	
		String[] params = {
				"http://d.esya.ru/?identifier="+settings.getString("lpch", "")+"&ncrnd=1347794237100", "false", "",
			"messageread" };
		
		try {
		
			for (int i = 0; i < result.ja.length(); i++) {
		        JSONObject jsonObject = result.ja.getJSONObject(i);
		        Log.i(getClass().getSimpleName(), jsonObject.optString("data")+ jsonObject.optString("ids"));
		        toprint=toprint+jsonObject.optString("data")+jsonObject.optString("ids");
		        String[] a={"device","from"};
				String[] b={settings.getString("device", ""),jsonObject.optString("data")};
				String[] params2 = {netutil.buildcommand(this,"im_get_all",a,b),"false","","im_get_all"};
				new netutil.MyAsyncTask(this).execute(params2) ;
		        
		        
			}
			
			Log.d(getClass().getSimpleName(),"messageread"+toprint);
				
				//	Toast.makeText(this,toprint,5).show();
				
					
					
		} catch (Exception e) {
			Log.d(getClass().getSimpleName(),"exeption in analise messageread result");
			//e.printStackTrace();
		}
				if (imrunning &&isOnline() && settings.getBoolean("im", false) && !settings.getString("lpch", "").equals("")){
				new netutil.MyAsyncTask(LocalService.this).execute(params) ;
				}
			 
			
			
				
		}
			}
		 
		
		
	
	
	
}

}	

