package com.OsMoDroid;

import android.os.Vibrator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
//import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
//import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.os.Handler;
import android.os.Binder;
//import android.os.Debug;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
//import android.preference.PreferenceManager;
//import android.util.Log;

//import android.util.Log;

//import android.util.Log;
//import android.util.Log;
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


public class LocalService extends Service implements LocationListener,GpsStatus.Listener, TextToSpeech.OnInitListener {
	private static final int OSMODROID_ID = 1;
	//MediaPlayer mp;
	//BufferedReader bufferedReader;
	MediaPlayer gpson;
	MediaPlayer gpsoff;
	MediaPlayer ineton;
	MediaPlayer inetoff;
	MediaPlayer sendpalyer;
	String cursendforbuffer="";
	String prevcursendforbuffer="";
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
	
	private boolean sended=false;
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
	private String URLadr;
	private Vibrator vibrator;
	private PowerManager pm;
	private WakeLock wakeLock;
	private WakeLock LocwakeLock;
	private WakeLock SendwakeLock;
	private SendCoor send;
	private int speedbearing_gpx;
	private int bearing_gpx;

	private int hdop_gpx;
	private int period_gpx;
	private int distance_gpx;
	private int sendcounter;
	BroadcastReceiver receiver;
	BroadcastReceiver checkreceiver;
	private final IBinder mBinder = new LocalBinder();
	private String gpxbuffer= new String();
	private String sendbuffer = new String();
	private String Sattelite="";
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

	final private static DecimalFormat df6 = new DecimalFormat("########.######");
	final private static DecimalFormat df1 = new DecimalFormat("########.#");
	final private static DecimalFormat df0 = new DecimalFormat("########");
	 final private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ssZ");
	 final private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
	 final private static SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm:ss");
	 final private static  DecimalFormatSymbols dot= new DecimalFormatSymbols();
	 //System.out.println(df2.format(1234.56));
	 private static final String enginePackageName = "com.svox.pico"; 
	    
	    private static final String SAMPLE_TEXT = "Synthesizes speech from text for immediate playback or to create a sound file."; 
	    TextToSpeech tts;
	    private int _langTTSavailable = -1;
	    
	    SharedPreferences settings;
	    
//private InputStream instream;
//BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(instream),8192);
	public class LocalBinder extends Binder {
		LocalService getService() {
    	//Log.d(getClass().getSimpleName(), "getservice() localservice");
    	 return LocalService.this;
    }
}


	@Override
	 public IBinder onBind(Intent intent) {
		//Log.d(getClass().getSimpleName(), "onbind() localservice");
			return mBinder;
			
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
		 settings = PreferenceManager.getDefaultSharedPreferences(this);
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
		sendbuffer="";
		ReadPref();
		String alarm = Context.ALARM_SERVICE;
		 am = ( AlarmManager ) getSystemService( alarm );
		
		
		 
		Intent intent = new Intent( "CHECK_GPS" );
 pi = PendingIntent.getBroadcast( this, 0, intent, 0 );
		 
		int type = AlarmManager.ELAPSED_REALTIME_WAKEUP;
		
		long triggerTime = SystemClock.elapsedRealtime() + notifyperiod;
		 
		am.setRepeating( type, triggerTime, notifyperiod, pi );
		
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
			
			
		registerReceiver( receiver, new IntentFilter( "android.location.GPS_FIX_CHANGE"));
		registerReceiver( checkreceiver, new IntentFilter( "CHECK_GPS"));
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
		sendcounter=0;
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
		CharSequence tickerText = getString(R.string.Working);
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
				 String time = sdf1.format(new Date(System.currentTimeMillis()));
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
		
		
	}
	
	



	@Override
	public void onDestroy() {
		super.onDestroy();
	
		if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
		
		if(receiver!= null){unregisterReceiver(receiver);}
		if(checkreceiver!= null){unregisterReceiver(checkreceiver);}
		//Log.d(getClass().getSimpleName(), "omdestroy() localservice");
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
		myManager.removeUpdates(this);
		setstarted(false);
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
				
if (usecourse)	{
	//Log.d(this.getClass().getName(), "Запускаем провайдера 0 0");
		myManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		prevlocation= myManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		myManager.removeUpdates(this);
		myManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		myManager.addGpsStatusListener(this);
		}
else	{
	//Log.d(this.getClass().getName(), "Запускаем провайдера по настройкам");
	myManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	prevlocation= myManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

	//myManager.removeUpdates(this);
	int gpsperiod;
	int gpsdistance;
	if (period>=period_gpx&&gpx){gpsperiod=period_gpx;}else {gpsperiod=period;};
	if (distance>=distance_gpx&&gpx){gpsdistance=distance_gpx;}else {gpsdistance=distance;};
	//Log.d(this.getClass().getName(), "период"+gpsperiod+"meters"+gpsdistance);
	myManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, gpsperiod, 0, this);
	myManager.addGpsStatusListener(this);	
}
		setstarted(true);
		//Log.d(getClass().getSimpleName(), "onstart() localservice");
	
	}
	
	
	
	
	private void setstarted(boolean started){
		//Log.d(getClass().getSimpleName(), "setstarted() localservice");
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("started", started);
        editor.commit();
	}
	private class SendCoor extends AsyncTask<String, String, String> {

		
		private  String tmp;   
		protected void onPostExecute(String result) {
			//Log.d(this.getClass().getName(), "Отправка завершилась.");	
//			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			if(usebuffer&&tmp.equals("false")){
				sended=false;
				if (sendbuffer.equals("")){sendbuffer=sendbuffer+lastsendforbuffer;} else{sendbuffer=sendbuffer+"&"+lastsendforbuffer;}
			}	 
			
			String time = sdf3.format(new Date(System.currentTimeMillis()));
			 sendresult= decodesendresult(result);
			 if (sended){
				 sendcounter=sendcounter+1;
				 if(sendsound &&sendpalyer!=null&& !sendpalyer.isPlaying())sendpalyer.start();
			 
			 sendresult= time +" "+sendresult;} else
			 {		 sendresult=getString(R.string.NoConnection);
			 internetnotify(true); 
		}
			in.putExtra("position",position+"\n"+Sattelite+"\n"+"Точность:");
			in.putExtra("sendresult",sendresult);
			in.putExtra("sendcounter",sendcounter);
			sendBroadcast(in);	
			// if (!tmp.equals(R.string.NoConnection)){ internetnotify(true); sended=false;}
			 SendwakeLock.release();
				
			
		   }
		protected String doInBackground(String... arg0) {
			try {
				 SendwakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SendWakeLock");
				SendwakeLock.acquire();
				//Log.d(this.getClass().getName(), "Начинаем отправку.");
				tmp=getPage(arg0[0], arg0[1]);
				//Log.d(this.getClass().getName(), "Отправка окончена.");
			} catch (IOException e) {
				internetnotify(false);
			//	e.printStackTrace();
				//sended=false;
				tmp="false";	
		
			}
			return tmp;
		}
	}
	
	public void onLocationChanged(Location location) {
		
		
		 LocwakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "LocWakeLock");
		LocwakeLock.acquire();
		
		//Log.d(this.getClass().getName(), "Позиция получена.");
		
		//mp.release();
		if (prevlocation_gpx==null)prevlocation_gpx=location;
		if (prevlocation==null)prevlocation=location;	
		Log.d(this.getClass().getName(), df0.format(location.getSpeed()*3.6).toString());
		Log.d(this.getClass().getName(), df0.format(prevlocation.getSpeed()*3.6).toString());
		if (settings.getBoolean("usetts", false)&&tts!=null && !tts.isSpeaking() && !(df0.format(location.getSpeed()*3.6).toString()).equals(lastsay))
		{
			Log.d(this.getClass().getName(), df0.format(location.getSpeed()*3.6).toString());
			Log.d(this.getClass().getName(), df0.format(prevlocation.getSpeed()*3.6).toString());
			tts.speak(df0.format(location.getSpeed()*3.6) , TextToSpeech.QUEUE_ADD, null);
			lastsay=df0.format(location.getSpeed()*3.6).toString();	
		}
		position = ( "Ш:" + df6.format(location.getLatitude())+ " Д:"+  df6.format( location.getLongitude())+" С:" +df1.format(location.getSpeed()*3.6));
		//position = ( "Ш:" + String.format("%.6f", location.getLatitude())+ " Д:"+  String.format("%.6f", location.getLongitude())+" С:" +String.format("%.1f", location.getSpeed()));
//if (location.getTime()>lastfix+3000)notifygps(false);
//if (location.getTime()<lastfix+3000)notifygps(true);



in.putExtra("position",position+"\n"+Sattelite+"\n"+"Точность:"+location.getAccuracy());
in.putExtra("sendcounter",sendcounter);
sendBroadcast(in);	



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
	in.putExtra("position",position+"\n"+Sattelite+"\n"+"Точность:"+location.getAccuracy());
	in.putExtra("sendcounter",sendcounter);
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
			in.putExtra("position",position+"\n"+Sattelite+"\n"+"Точность:"+location.getAccuracy());
			in.putExtra("sendcounter",sendcounter);
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
      		 con.setReadTimeout(5000+ sendbuffer.length()/1024);
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
       		 con.setReadTimeout(5000+ sendbuffer.length()/1024);
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
//	     stringBuilder = new StringBuilder();
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

private String decodesendresult(String str){
	
	//Log.d(this.getClass().getName(), "Ответ сервера"+str);
	int s=-1;
	int l=-1;
	
	try {
		JSONObject result = new JSONObject(str);
		


		if(result.has("s")){ s = result.optInt("s");}
		if (result.has("l")){ l =result.optInt("l");}
		
		if (s==0 ) {int code=result.optInt("code");str=getString(R.string.error)+code+" "+ result.optString("error");
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
	
		return str;// TODO Auto-generated catch block
		
	}
//	if (str.equals("{\"s\":1}\n")){str=getString(R.string.succes);
	//sended=true;
	//}else {
		//str="НОТ";
//	sended=true;};
	//str="xc";
	return str;
	
}

private void writegpx(Location location){
	FileWriter trackwr;
	 long gpstime = location.getTime();
	 Date date = new Date(gpstime);
	// SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ssZ");
	 String strgpstime = sdf1.format(date);
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
	URLadr="http://t.esya.ru/?"+  df6.format( location.getLatitude()) +":"+ df6.format(location.getLongitude())+":"+ df1.format( location.getAccuracy())
	+":"+df1.format( location.getAltitude())+":"+df1.format( location.getSpeed())+":"+hash+":"+n+":"+location.getTime()/1000;
else
	URLadr="http://t.esya.ru/?"+  df6.format( location.getLatitude()) +":"+ df6.format( location.getLongitude())+":"+ df1.format(location.getAccuracy())
	+":"+df1.format( location.getAltitude())+":"+df1.format( location.getSpeed())+":"+hash+":"+n;
//URLadr=URLadr.replace(",", ".");
//Log.d(this.getClass().getName(), URLadr);
//lat:lon:altitude:speed:time
 
if (usebuffer){
	//Log.d(this.getClass().getName(),"Входим"+ sendbuffer);
	cursendforbuffer = "log[]="+df6.format( location.getLatitude()) +":"+ df6.format( location.getLongitude())
+":"+df1.format( location.getAltitude())+":"+df1.format( location.getSpeed())+":"+location.getTime()/1000;	
//cursendforbuffer=cursendforbuffer.replace(",", ".");
	}
//Log.d(this.getClass().getName(),"Входим2"+ sendbuffer);
//if (prevcursendforbuffer.equals(""))prevcursendforbuffer=cursendforbuffer;
 //Log.d(this.getClass().getName(), "sendbuffer "+sendbuffer);
 //Log.d(this.getClass().getName(), "cursendbuffer "+cursendforbuffer);
 //Log.d(this.getClass().getName(), "lastsendforbuffer "+lastsendforbuffer);
if (send == null ||
	send.getStatus().equals(AsyncTask.Status.FINISHED)) {
//	 Log.d(this.getClass().getName(), sendbuffer);
	if(usebuffer){
	//	Log.d(this.getClass().getName(),"Ластсендфорбуфер "+ lastsendforbuffer);
	if (sended )
	{
		//Log.d(this.getClass().getName(),"До обрезки"+ sendbuffer);
	//Log.d(this.getClass().getName(),"Что отрезаем"+ lastsendforbuffer);
	//sendbuffer=t.replace(t.indexOf(lastsendforbuffer), t.lastIndexOf(lastsendforbuffer), "").toString();
  if (sendbuffer.length()>1)
    sendbuffer=sendbuffer.substring(lastsendforbuffer.length());
	   //sendbuffer=sendbuffer.replace(lastsendforbuffer, " ");
   sendbuffer=sendbuffer.trim();
		//Log.d(this.getClass().getName(),"результат обрезки"+ sendbuffer);
		} else {if (sendbuffer.equals("")){
						sendbuffer=sendbuffer+prevcursendforbuffer;
			//			Log.d(this.getClass().getName(),"Назначение 1"+ sendbuffer);				
		} else{sendbuffer=sendbuffer+"&"+prevcursendforbuffer;
	//	Log.d(this.getClass().getName(),"Назначение 2"+ sendbuffer);
		}}
	//if (sendbuffer.equals("")){sendbuffer=sendbuffer+cursendforbuffer;} else{sendbuffer=sendbuffer+"&"+cursendforbuffer;}
	//Log.d(this.getClass().getName(),"Не обрезали "+ sendbuffer);
	lastsendforbuffer=sendbuffer;
prevcursendforbuffer=cursendforbuffer;
//Log.d(this.getClass().getName(),"Не обрезали2 "+ sendbuffer);
	}
	
//	Log.d(this.getClass().getName(), sendbuffer);
send = new SendCoor();	
//Log.d(this.getClass().getName(), "sendbuffer отправляемый "+sendbuffer);
if(usebuffer)
send.execute(URLadr,sendbuffer);
else send.execute(URLadr," ");
//		sendbuffer[sendcounter]=URLadr;
} else
{
	if(usebuffer)
	{
	if (sendbuffer.equals("")){sendbuffer=sendbuffer+cursendforbuffer;} else{sendbuffer=sendbuffer+"&"+cursendforbuffer;}
	}
}
//Log.d(this.getClass().getName(), "void sendlocation end");
}
//private void notifygps(boolean gps){
//	if (!gps){
//		if(!gpsbeepedoff){
//	//long[] pattern = {0,50, 0, 30, 0, 50};
//		//vibrator.vibrate(pattern, 2);
//			if (vibrate)vibrator.vibrate(vibratetime);
//			if (playsound){mp = MediaPlayer.create(this, R.raw.gpsoff);
//			
//			mp.start();
//			Toast.makeText(LocalService.this, "гпс поява", Toast.LENGTH_SHORT).show();
//}
//	Log.d(this.getClass().getName(), "gps propal");
//		
//		gpsbeepedoff=true;
//		gpsbeepedon=false;
//		}	
//	}
//	else{
//
//			if(!gpsbeepedon){
//				//long[] pattern = {0,50, 0, 30, 0, 50};
//				Log.d(this.getClass().getName(), "Интернет появился");
//				//vibrator.vibrate(pattern, 2);
//				if(vibrate)vibrator.vibrate(vibratetime);
//				if (playsound){mp = MediaPlayer.create(this, R.raw.gpson);
//				
//				mp.start();
//				Toast.makeText(LocalService.this, "гпс пропал", Toast.LENGTH_SHORT).show();
//				}
//				
//				gpsbeepedon=true;
//				gpsbeepedoff=false;
//		}
//	}
//	
//}



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
           _langTTSavailable = tts.setLanguage(Locale.US); // Locale.FRANCE etc.
          if (_langTTSavailable == TextToSpeech.LANG_MISSING_DATA ||
          	_langTTSavailable == TextToSpeech.LANG_NOT_SUPPORTED) {
        	  Log.d(this.getClass().getName(), "Нету языка");
        	  
           } else if ( _langTTSavailable >= 0 && settings.getBoolean("usetts", false)) {
        	   Log.d(this.getClass().getName(), "Произносим");
        	   tts.speak("Ryabotayem!", TextToSpeech.QUEUE_ADD, null);
			 
			
          }
      } else {
    	  Log.d(this.getClass().getName(), "Инициализация файлед");
    	  
    	  // Initialization failed.
      }
}

}	

