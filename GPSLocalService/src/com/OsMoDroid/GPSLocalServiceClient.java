package com.OsMoDroid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.OsMoDroid.LocalService.LocalBinder;
import com.OsMoDroid.Netutil.MyAsyncTask;
import com.OsMoDroid.R;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

import android.app.Activity;
import android.app.AlertDialog;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;
import android.provider.Settings.Global;
import android.provider.Settings.Secure;


import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.telephony.TelephonyManager;
import android.text.ClipboardManager;
import android.text.util.Linkify;
import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;

import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
  
public class GPSLocalServiceClient extends SherlockFragmentActivity  implements ResultsListener{
	
	 


//void showFragment(SherlockFragment fragment)
//	{
//		FragmentTransaction ft = fMan.beginTransaction();
//		ft.replace(R.id.fragment_container, fragment);
//		ft.commit();
//	}
void showFragment(SherlockFragment fragment, boolean backstack) {
	FragmentTransaction ft = fMan.beginTransaction();
	
	if(backstack)
		{	
			ft.replace(R.id.fragment_container, fragment);
			ft.addToBackStack("backstack");
		}
	else 
	{	
		ft.replace(R.id.fragment_container, fragment);
		fMan.popBackStack();
	}
	ft.commit();

}
 


	boolean messageShowed=false;
	public static String key = "";
	public String login = "";
	private int speed;
	int speedbearing_gpx;
	int bearing_gpx;
	private long notifyperiod = 30000;
	int hdop_gpx;
	int period_gpx;
	int distance_gpx;
	private boolean sendsound = false;
	private boolean playsound = false;
	boolean started = false;
	private boolean vibrate;
	private boolean usecourse;
	private int vibratetime;
	LocalService mService;
	boolean mBound = false;
	int speedbearing;
	int bearing;
	private boolean gpx = false;
	boolean live = true;
	int hdop;
	int period;
	int distance;
	private String pass = "";
	private String hash;
	private int n;
	private String submiturl;
	String viewurl;
	private String pdaviewurl;
	private String device="";
	private String devicename="";
	String position;
	String sendresult;
	BroadcastReceiver receiver;
	int sendcounter;
	int buffercounter=0;
	private boolean usebuffer = false;
	private boolean usewake = false;
	File fileName = null;
	PowerManager pm;
	WakeLock	wakeLock;// = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "MyWakeLock");
	String version="Unknown";
	//SharedPreferences OsMoDroid.settings;
	
	public ActionBar actionBar;
	private ArrayList<String> mDrawerItems=new ArrayList<String>();
	static DrawerLayout mDrawerLayout;
	static ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mTitle;
	private CharSequence mDrawerTitle;
	static FragmentManager fMan;
	static DrawerItemClickListener drawClickListener = new DrawerItemClickListener();
	upd mainUpdListener;
	private BroadcastReceiver mIMstatusReciever;
	//private boolean afterrotate=false;
	private Intent needIntent;
	public interface upd {
		void update();
	}
	
	
	ServiceConnection conn = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder service) {
			 Log.d(this.getClass().getSimpleName(), "onserviceconnected gpsclient");
			
			LocalBinder binder = (LocalBinder) service;
			mService = binder.getService();
			mBound = true;
			 
			invokeService();
			started = true;
			updateMainUI();
			if (live&&!OsMoDroid.settings.getString("hash", "" ).equals("")&&OsMoDroid.settings.getLong("laststartcommandtime", 0)<System.currentTimeMillis()-14400000){
				mService.startcomand();
				}
			if (!OsMoDroid.settings.getString("key", "" ).equals("") ){
			Netutil.newapicommand((ResultsListener)mService.serContext, "om_device_get:"+OsMoDroid.settings.getString("device", ""));
			}
			if(needIntent!=null){
				intentAction(needIntent);
				needIntent=null;
			}
		}

		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
			
			Log.d(this.getClass().getSimpleName(), "onservicedisconnected gpsclient");
		}
	};
	private boolean proceednewintent=false;
	private ArrayAdapter<String> menuAdapter;
	
	

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


	 protected void updateMainUI() {
		 Log.d(this.getClass().getSimpleName(), "updateMainUI gpsclient");
		 if(mainUpdListener!=null){
			 Log.d(this.getClass().getSimpleName(), "updateMainUI not null gpsclient");
			mainUpdListener.update();
		}
		
	}

	@Override
	 protected void onPause(){
		 Log.d(this.getClass().getSimpleName(), "onPause() gpsclient");
	 
		 
	 if (!(wakeLock==null) &&wakeLock.isHeld())wakeLock.release();

		super.onPause();

	 }

	@Override
	protected void onStop() {
		Log.d(this.getClass().getSimpleName(), "onStop() gpsclient");
		OsMoDroid.gpslocalserviceclientVisible=false;
		super.onStop();

	}



	@Override
	protected void onSaveInstanceState(Bundle outState) {
		//outState.putString("currentItem", drawClickListener.currentItemName);
		Log.d(this.getClass().getSimpleName(), "onsave gpsclient");
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		drawClickListener = new DrawerItemClickListener();
		Log.d(this.getClass().getSimpleName(), "onCreate() gpsclient");
		super.onCreate(savedInstanceState);
		PreferenceManager.setDefaultValues(this, R.xml.pref, true);
		ReadPref();
		String sdState = android.os.Environment.getExternalStorageState();

		
		if (sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {

		 File sdDir = android.os.Environment.getExternalStorageDirectory();

		 fileName = new File (sdDir, "OsMoDroid/");

		 fileName.mkdirs();

		 fileName = new File(sdDir, "OsMoDroid/settings.dat");

		 }
		

		pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

		String strVersionName = getString(R.string.Unknow);

		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			strVersionName = packageInfo.packageName + " "
					+ packageInfo.versionName;
			version=packageInfo.versionName;
		} catch (NameNotFoundException e) {
			//e.printStackTrace();
		}
		OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
			
			  public void onSharedPreferenceChanged(SharedPreferences prefs, String keychanged) {
			    if ((keychanged.equals("hash")||keychanged.equals("n")) ) {
			    	Log.d(this.getClass().getSimpleName(), "Сменился хэш");
			    	device="";
			    	devicename="";

			    
			    	OsMoDroid.editor.remove("device");
			    	OsMoDroid.editor.remove("devicename");
			    	OsMoDroid.editor.commit();



			    }

			  
			  }
			};


			OsMoDroid.settings.registerOnSharedPreferenceChangeListener(listener);
			setContentView(R.layout.activity_main);
	        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	        mDrawerList = (ListView) findViewById(R.id.left_drawer);
		        // Set the adapter for the list view
		    	setupDrawerList();
		    	drawClickListener.globalActivity=this;
		        mDrawerList.setOnItemClickListener(drawClickListener);
		        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		        mDrawerLayout.setBackgroundColor(Color.WHITE);
			
		        fMan=getSupportFragmentManager();
			
	         actionBar = getSupportActionBar();
	         actionBar.setDisplayHomeAsUpEnabled(true);
	         actionBar.setHomeButtonEnabled(true);

	        mTitle = mDrawerTitle = getTitle();
	        mDrawerToggle = new ActionBarDrawerToggle(
	                this,                  /* host Activity */
	                mDrawerLayout,         /* DrawerLayout object */
	                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
	                R.string.drawer_open,  /* "open drawer" description for accessibility */
	                R.string.drawer_close  /* "close drawer" description for accessibility */
	                ) {
	            public void onDrawerClosed(View view) {
	            	super.onDrawerClosed(view);
	            	
	            }

	            public void onDrawerOpened(View drawerView) {
	            	  super.onDrawerOpened(drawerView);
	            	
	            }
	        };
	        mDrawerLayout.setDrawerListener(mDrawerToggle);
			bindService();
			if(savedInstanceState!=null){
			//	afterrotate=true;
				//drawClickListener.selectItem(savedInstanceState.getString("currentItem"));
			}else
			{
				
				//drawClickListener.selectItem(getString(R.string.tracker));
			}
			mIMstatusReciever = new BroadcastReceiver(){

				@Override
				public void onReceive(Context context, Intent intent) {
					if (intent.hasExtra("connecting")&&intent.hasExtra("connect")&&!OsMoDroid.settings.getString("key", "").equals(""))
					{
						
						if(intent.getBooleanExtra("connect", false)&&!intent.getBooleanExtra("connecting", false))
							{
								actionBar.setLogo(R.drawable.eyeo);
							}
						else if (intent.getBooleanExtra("connecting", false))
							{
								actionBar.setLogo(R.drawable.eyeu);
							}
						else	
							{
								actionBar.setLogo(R.drawable.eyen);
							}
					}
					
				}
				
			};
			registerReceiver(mIMstatusReciever, new IntentFilter("OsMoDroid"));
	}


	void setupDrawerList() {
		mDrawerItems.clear();
		if (!OsMoDroid.settings.getString("key", "" ).equals("") ){
		  
		 for (String s: new String[] {
				    getString(R.string.tracker), getString(R.string.stat),getString(R.string.map),
				    getString(R.string.chanals),getString(R.string.devices),getString(R.string.links),
				    getString(R.string.notifications), getString(R.string.tracks) , getString(R.string.exit)})
		 {
		 mDrawerItems.add(s);
		 
		 }
		 }
		else{
			for (String s:  new String[] {
				    getString(R.string.tracker), getString(R.string.stat),getString(R.string.map),
				    getString(R.string.tracks),getString(R.string.exit)} ){
				mDrawerItems.add(s);
			}
		}
		
		menuAdapter=new ArrayAdapter<String>(this,R.layout.drawer_list_item, mDrawerItems);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,R.layout.drawer_list_item, mDrawerItems));
	}
	 
	
	 @Override
		protected void onPostCreate(Bundle savedInstanceState) {
		 Log.d(this.getClass().getSimpleName(), "onpostcreate gpsclient");	
		 super.onPostCreate(savedInstanceState);
			mDrawerToggle.syncState();
		}
		
		@Override
		public void onConfigurationChanged(Configuration newConfig) {
			Log.d(this.getClass().getSimpleName(), "onconfigchanged gpsclient");
			super.onConfigurationChanged(newConfig);
			mDrawerToggle.onConfigurationChanged(newConfig);
		}
		
		 @Override
			public boolean onOptionsItemSelected(MenuItem item) {
			
				 if(item.getItemId()==android.R.id.home)
				 {
					 if(mDrawerLayout.isDrawerOpen(mDrawerList))
					 {
						 mDrawerLayout.closeDrawer(mDrawerList);
					 }
					 else {
						mDrawerLayout.openDrawer(mDrawerList);
					}
				 }
				return super.onOptionsItemSelected(item);
			}	
	 

	@Override
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		 Log.d(this.getClass().getSimpleName(), "void onActivityResult");
		 updateMainUI();
		if (conn == null || mService == null) {

		} else {

			mService.applyPreference();
			

		}
	   
	  }
	
	
	@Override
	protected void onResume() {
		super.onResume();
		 Log.d(this.getClass().getSimpleName(), "onResume() gpsclient");
		OsMoDroid.gpslocalserviceclientVisible=true;
	
		ReadPref();
		started = checkStarted();
		
		
		if (hash.equals("") && live) {
			RequestAuthTask requestAuthTask = new RequestAuthTask();
			requestAuthTask.execute();
			
		}
		

	}



	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();
		Log.d(this.getClass().getSimpleName(), "onresumefragments gpsclient");
		updateMainUI();
		if(!proceednewintent){
		if(mBound){
		intentAction(getIntent());
		
		}
		else{
			needIntent=getIntent();
		}
		}
		proceednewintent=false;
//		if (getIntent().getAction().equals(Intent.ACTION_MAIN)&&checkStarted()){
//			StatFragment stat = new StatFragment();
//			showFragment(stat);
//		}
		
	}


	private void intentAction(Intent intent) {
		Log.d(this.getClass().getSimpleName(), "intentaction ");
		if(intent.getAction()!=null){
		if (intent.getAction().equals("devicechat"))
		{
			//DeviceChatFragment openFragment = new DeviceChatFragment();	
			Bundle bundle = new Bundle();
			bundle.putInt("deviceU", intent.getIntExtra("deviceU", -1));
			//openFragment.setArguments(bundle);
			//showFragment(openFragment,true);
			drawClickListener.selectItem(getString(R.string.devices),bundle);
			Log.d(this.getClass().getSimpleName(), "on new intent=devicechat");
		} else
		if (intent.getAction().equals("notif"))
		{
			Log.d(this.getClass().getSimpleName(), "on new intent=notif");
			NotifFragment notif =new NotifFragment();
			drawClickListener.selectItem(getString(R.string.notifications),null);
			//showFragment(notif,false);
		} else
		if (intent.getAction().equals("channelchat"))
		{
			Log.d(this.getClass().getSimpleName(), "on new intent=channelchat");
			//ChannelDevicesFragment openFragment = new ChannelDevicesFragment();	
			Bundle bundle = new Bundle();
			bundle.putInt("channelpos", intent.getIntExtra("channelpos", -1));
			//openFragment.setArguments(bundle);
			//showFragment(openFragment,true);
			drawClickListener.selectItem(getString(R.string.chanals),bundle);
		} else
		if (intent.getAction().equals(Intent.ACTION_MAIN)){
			if(!LocalService.currentItemName.equals("")){
				drawClickListener.selectItem(LocalService.currentItemName,null);
			}else{
			Log.d(this.getClass().getSimpleName(), "on new intent=MAIN");
			drawClickListener.selectItem(getString(R.string.tracker),null);
			}
		}
		Intent i = getIntent();
		i.setAction(null);
		setIntent(i);
		
		}
	}
	@Override
	protected void onNewIntent(Intent intent) {
	if (OsMoDroid.gpslocalserviceclientVisible){
		Log.d(this.getClass().getSimpleName(), "on new intent="+intent.getIntExtra("deviceU", -1));
		intentAction(intent);
		
	}
	proceednewintent=true;
		super.onNewIntent(intent);
	}
	
	


	


	void auth() {
		// final View textEntryView = factory.inflate(R.layout.dialog,
		// null);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		final TextView txv5 = new TextView(this);
		txv5.setText(R.string.login);
		layout.addView(txv5);
		final EditText inputlogin = new EditText(this);
		inputlogin.setText(login);
		layout.addView(inputlogin);

		final TextView txv3 = new TextView(this);
		txv3.setText(R.string.password);
		layout.addView(txv3);

		final EditText input = new EditText(this);
		//input2.setText("Ваше имя");
		layout.addView(input);
//	final EditText input = new EditText(this);
		//final TextView txv4 = new TextView(this);
		//txv4.setText("Одноразовый пароль можно получить по адресу http://esya.ru/app.html?act=add при наличии регистрации");
		//Linkify.addLinks(txv4, Linkify.ALL);
		//layout.addView(txv4);

		AlertDialog alertdialog3 = new AlertDialog.Builder(
				GPSLocalServiceClient.this)
				.setTitle(R.string.appauth)
				.setView(layout)
				.setPositiveButton(R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								pass = input.getText().toString();
								if (!(pass.equals(""))) {
									String[] params = {
											"http://auth.api.esya.ru",
											"true",
											"login=" + inputlogin.getText().toString() + "&password="
													+ pass + "&key=G94y",
											"auth" };
									RequestCommandTask Rq = new RequestCommandTask();
									Rq.execute(params);
								} else {
									Toast.makeText(
											GPSLocalServiceClient.this,
											R.string.noappcode, 5).show();
								}
							}
						})
				.setNegativeButton(R.string.No,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {


							}
						}).create();

		alertdialog3.show();
	}


	boolean checkStarted() {

		// Log.d(this.getClass().getSimpleName(), "oncheckstartedy() gpsclient");
		return OsMoDroid.settings.getBoolean("started", false);

	}

	void ReadPref() {
		 Log.d(this.getClass().getSimpleName(), "readpref() gpsclient");


		speed =  Integer.parseInt(OsMoDroid.settings.getString("speed", "3").equals(
				"") ? "3" : OsMoDroid.settings.getString("speed", "3"));
		period = Integer.parseInt(OsMoDroid.settings.getString("period", "10000").equals(
				"") ? "10000" : OsMoDroid.settings.getString("period", "10000"));
		distance = Integer.parseInt(OsMoDroid.settings.getString("distance", "50")
				.equals("") ? "50" : OsMoDroid.settings.getString("distance", "50"));
		hash = OsMoDroid.settings.getString("hash", "");
		n = Integer.parseInt(OsMoDroid.settings.getString("n", "0").equals("") ? "0"
				: OsMoDroid.settings.getString("n", "0"));
		submiturl = OsMoDroid.settings.getString("submit-url", "");
		viewurl = OsMoDroid.settings.getString("view-url", "");
		pdaviewurl = OsMoDroid.settings.getString("pda-view-url", "");
		speedbearing = Integer.parseInt(OsMoDroid.settings.getString("speedbearing", "2")
				.equals("") ? "2" : OsMoDroid.settings.getString("speedbearing", "2"));
		bearing = Integer.parseInt(OsMoDroid.settings.getString("bearing", "10").equals(
				"") ? "10" : OsMoDroid.settings.getString("bearing", "2"));
		hdop = Integer
				.parseInt(OsMoDroid.settings.getString("hdop", "30").equals("") ? "30"
						: OsMoDroid.settings.getString("hdop", "30"));
		gpx = OsMoDroid.settings.getBoolean("gpx", false);
		live = OsMoDroid.settings.getBoolean("live", true);
		vibrate = OsMoDroid.settings.getBoolean("vibrate", false);
		usecourse = OsMoDroid.settings.getBoolean("usecourse", false);
		vibratetime = Integer.parseInt(OsMoDroid.settings.getString("vibratetime", "200")
				.equals("") ? "200" : OsMoDroid.settings.getString("vibratetime", "0"));
		playsound = OsMoDroid.settings.getBoolean("playsound", false);
		period_gpx = Integer.parseInt(OsMoDroid.settings.getString("period_gpx", "0")
				.equals("") ? "0" : OsMoDroid.settings.getString("period_gpx", "0"));
		distance_gpx = Integer.parseInt(OsMoDroid.settings.getString("distance_gpx", "0")
				.equals("") ? "0" : OsMoDroid.settings.getString("distance_gpx", "0"));
		speedbearing_gpx = Integer.parseInt(OsMoDroid.settings.getString(
				"speedbearing_gpx", "0").equals("") ? "0" : OsMoDroid.settings.getString(
				"speedbearing_gpx", "0"));
		bearing_gpx = Integer.parseInt(OsMoDroid.settings.getString("bearing_gpx", "0")
				.equals("") ? "0" : OsMoDroid.settings.getString("bearing", "0"));
		hdop_gpx = Integer.parseInt(OsMoDroid.settings.getString("hdop_gpx", "30")
				.equals("") ? "30" : OsMoDroid.settings.getString("hdop_gpx", "30"));
		usebuffer = OsMoDroid.settings.getBoolean("usebuffer", false);
		usewake = OsMoDroid.settings.getBoolean("usewake", false);
		notifyperiod = Integer.parseInt(OsMoDroid.settings.getString("notifyperiod",
				"30000").equals("") ? "30000" : OsMoDroid.settings.getString(
				"notifyperiod", "30000"));
		sendsound = OsMoDroid.settings.getBoolean("sendsound", false);
		// pass = OsMoDroid.settings.getString("pass", "");
		login = OsMoDroid.settings.getString("login", "");
		key = OsMoDroid.settings.getString("key", "");
		device = OsMoDroid.settings.getString("device", "");
		Log.d(this.getClass().getSimpleName(), "readpref() hash:"+hash);
	}

	void startlocalservice(){
		//Intent i = new Intent(this, LocalService.class);
		//startService(i);
		started = true;
		mService.startServiceWork();

	}

	private void bindService() {
		Log.d(this.getClass().getSimpleName(), "bindservice gpsclient");
		Intent i = new Intent("OsMoDroid.local");
		if(!mBound){
		bindService(i, conn, Context.BIND_AUTO_CREATE);
		
		Intent is = new Intent(this, LocalService.class);
		startService(is);
		}
		//updateServiceStatus();
	}

	void stop(Boolean stopsession) {
		Log.d(this.getClass().getSimpleName(), "stop() gpsclient");
		mService.stopServiceWork(stopsession);
		//Intent i = new Intent(this, LocalService.class);
		//stopService(i);

	}

	private void invokeService() {
		Log.d(this.getClass().getSimpleName(), "invokeservice() gpsclient");

		if (conn == null || mService == null) {

		} else {

			mService.refresh();
			updateMainUI();

		}
	}

	

	
	
	
	@Override
	protected void onDestroy() {
		Log.d(this.getClass().getSimpleName(), "onDestroy() gpsclient");
		
if (mBound) {

			try {
				unbindService(conn);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.d(this.getClass().getSimpleName(), "Исключение при отсоединении от сервиса");
				e.printStackTrace();
			}
		}

		conn = null;
		// releaseService();
		if (receiver != null) {
			unregisterReceiver(receiver);
		}
		if (mIMstatusReciever!=null){
			unregisterReceiver(mIMstatusReciever);
		}


		// 
		super.onDestroy();
	}

	public String getPage(String adr, boolean dopost, String post)
			throws IOException {
		// Log.d(this.getClass().getSimpleName(), "getpage() gpsclient");
		Log.d(this.getClass().getSimpleName(), adr);
		HttpURLConnection con;
		int portOfProxy = android.net.Proxy.getDefaultPort();
		if (portOfProxy > 0) {
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
					android.net.Proxy.getDefaultHost(), portOfProxy));
			con = (HttpURLConnection) new URL(adr).openConnection(proxy);
		} else {
			con = (HttpURLConnection) new URL(adr).openConnection();
		}
		con.setReadTimeout(15000);
		con.setConnectTimeout(15000);
		if (dopost) {
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setDoInput(true);
			OutputStream os = con.getOutputStream();
			os.write(post.getBytes());
			Log.d(this.getClass().getName(), "Что POSTим" + post);
			os.flush();
			os.close();
		}

		con.connect();
		if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
			// String str=inputStreamToString(con.getInputStream());
			return inputStreamToString(con.getInputStream());
		} else {
			Log.d(this.getClass().getName(),
					Integer.toString(con.getResponseCode()));
			// String str=inputStreamToString(con.getInputStream());
			// Log.d(this.getClass().getName(),str);
			// return str;
			return getString(R.string.ErrorRecieve);

		}

	}

	public String inputStreamToString(InputStream in) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(in));
		StringBuilder stringBuilder = new StringBuilder();
		String line = null;

		while ((line = bufferedReader.readLine()) != null) {
			stringBuilder.append(line + "\n");
			 if (!bufferedReader.ready()) {
			       break;
			    }
		}

		bufferedReader.close();
		return stringBuilder.toString();
	}

	public static String bytesToHex(byte[] b) {
		char hexDigit[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		StringBuffer buf = new StringBuffer();
		for (int j = 0; j < b.length; j++) {
			buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
			buf.append(hexDigit[b[j] & 0x0f]);
		}
		return buf.toString();
	}

	public static String SHA1(String text) {
		//Log.d(this.getClass().getName(), text);
		MessageDigest md;
		byte[] sha1hash = new byte[40];
		try {
			md = MessageDigest.getInstance("SHA-1");

			// md.update(text.getBytes());//, 0, text.length());
			sha1hash = md.digest(text.getBytes());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bytesToHex(sha1hash);
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
		void requestHash (){
			RequestAuthTask requestAuthTask = new RequestAuthTask();
			requestAuthTask.execute();
			
			
		}
				
	 class RequestAuthTask extends AsyncTask<Void, Void, Void> {
		private String authtext;
		String adevice;
		private Boolean Err = true;
		ProgressDialog dialog = ProgressDialog.show(GPSLocalServiceClient.this,
				"", getString(R.string.AuthWait), true);

		// Dialog dial = Dialog.

		protected void onPreExecute() {
			// dialog.dismiss();
			dialog.show();
		}

		protected void onPostExecute(Void params) {
			// Log.d(this.getClass().getName(), "Задание окончило выполнятся.");
			dialog.dismiss();
			if (Err) {
				Toast.makeText(GPSLocalServiceClient.this,
						getString(R.string.CheckInternet), 5).show();
			} else {


				//WritePref();
				
				OsMoDroid.editor.putString("hash", hash);
				OsMoDroid.editor.putString("n", Integer.toString(n));
				OsMoDroid.editor.putString("submit-url", submiturl);
				OsMoDroid.editor.putString("view-url", viewurl);
				OsMoDroid.editor.putString("pda-view-url", pdaviewurl);
				OsMoDroid.editor.putString("device", adevice);
				OsMoDroid.editor.putString("key", "");
				OsMoDroid.editor.commit();
				ReadPref();
				setupDrawerList();
				updateMainUI();
				Log.d(this.getClass().getName(), "Задание окончило выполнятся.Bind");
				bindService();

			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				// Log.d(this.getClass().getName(),
				// "Начинаем запрос авторизации.");
				//authtext = getPage("http://auth.t.esya.ru/?who=OsMoDroid",false, "");
				
				authtext = getPage("http://a.t.esya.ru/?act=new&c=OsMoDroid&v="+version+"&n="+Uri.encode(getDeviceName()),	false, "");
				//{"device":1235,"hash":"JoqQtav","n":"2515","url":"26CstQLcgzIYTOin"}
				JSONObject auth = new JSONObject(authtext);
				Log.d(this.getClass().getName(), auth.toString());
				hash = auth.getString("hash");
				n = auth.getInt("n");
				submiturl = auth.optString("submit-url");
				viewurl =  "http://m.esya.ru/"+auth.optString("url");
				pdaviewurl = auth.optString("pda-view-url");
				adevice= auth.getString("u");
				// Log.d(this.getClass().getName(), "Авторизация закончилась.");
				if (hash.equals("")) {
					// Log.d(this.getClass().getName(), "Косяк.");
					Err = true;
				}
				Err = false;
			} catch (IOException e) {

				e.printStackTrace();
				// Log.d(this.getClass().getName(), "Косяк2.");
				Err = true;
				// finish();

			} catch (JSONException e) {
				e.printStackTrace();
				// Log.d(this.getClass().getName(), "Косяк3.");
				Err = true;

			}
			return null;
		}
	}

	public class RequestCommandTask extends AsyncTask<String, Void, String> {
		private String Commandtext;
		private String adevice;
		private String akey;
		private String aviewurl;
		private String adevicename;
		// private Boolean Err = true;
		ProgressDialog dialog = ProgressDialog.show(GPSLocalServiceClient.this,
				"", getString(R.string.commandpleasewait), true);

		// Dialog dial = Dialog.

		protected void onPreExecute() {
			// dialog.dismiss();
			dialog.show();
		}

		@Override
		protected void onPostExecute(String resultString) {

			dialog.dismiss();
			if (resultString == null) {
				Toast.makeText(GPSLocalServiceClient.this,
						getString(R.string.CheckInternet), 5).show();
			} else {
				// commandJSON=resultJSON;
if (!(adevice==null)){device=adevice;

OsMoDroid.editor.putString("device", adevice);
OsMoDroid.editor.commit();}
if (!(adevicename==null)){devicename=unescape(adevicename);
OsMoDroid.editor.putString("devicename", unescape(adevicename));
OsMoDroid.editor.commit();

}
if (!(akey==null))
{key=akey;
OsMoDroid.editor.putString("key", key);
OsMoDroid.editor.remove("laststartcommandtime");
OsMoDroid.editor.commit();
Netutil.newapicommand((Context) GPSLocalServiceClient.this, "om_device_bind:"+OsMoDroid.settings.getString("hash", "")+","+OsMoDroid.settings.getString("n", ""));
mService.startcomand();
//mDrawerItems = ;
setupDrawerList();

updateMainUI();
Netutil.newapicommand((ResultsListener)mService, "om_device_get:"+OsMoDroid.settings.getString("device", ""));


}
if (!(aviewurl==null)){viewurl=aviewurl;}

				updateMainUI();

				

				OsMoDroid.editor.putString("view-url", viewurl);


				OsMoDroid.editor.commit();

				ReadPref();
				Toast.makeText(GPSLocalServiceClient.this,
						resultString.toString(), 5).show();
			}

		}

		@Override
		protected String doInBackground(String... params) {
			JSONObject resJSON = null;
			String returnstr = null;
			try {
				// Log.d(this.getClass().getName(),
				// "Начинаем запрос авторизации.");
				Commandtext = getPage(params[0],
						Boolean.parseBoolean(params[1]), params[2]);
				Log.d(this.getClass().getName(), Commandtext);
				resJSON = new JSONObject(Commandtext);
				//Toast.makeText(this,resJSON.optString("state")+" "+ resJSON.optString("error_description"),5).show();
				// return new JSONObject(Commandtext);

				// Log.d(this.getClass().getName(), "Авторизация закончилась.");

			} catch (IOException e) {

				e.printStackTrace();
				return null;
				// Log.d(this.getClass().getName(), "Косяк2.");
				// Err = true;
				// finish();

			} catch (JSONException e) {
				e.printStackTrace();
				return null;
				// Log.d(this.getClass().getName(), "Косяк3.");
				// Err = true;

			}
			if (resJSON == null)
				return null;
			else {
				// commandJSON=resultJSON;
				if (params[3].equals("auth")) {
					if (!(resJSON.optString("key").equals(""))) {
						akey = resJSON.optString("key");
						returnstr = resJSON.optString("state")+" "+ resJSON.optString("error_description");
					}
				}

				if (params[3].equals("device_link")) {
					if (!(resJSON.optString("url").equals(""))) {
						aviewurl = resJSON.optString("url");
						returnstr = resJSON.optString("state")+" "+ resJSON.optString("error_description");
					}
				}

				if (params[3].equals("device")) {
					if (!(resJSON.optJSONArray("devices") == null)) {
						for (int i = 0; i < resJSON.optJSONArray("devices")
								.length(); i++) {
							try {
								if (resJSON.optJSONArray("devices")
										.getJSONObject(i).getString("hash")
										.equals(hash)) {
									adevice = resJSON.optJSONArray("devices")
											.getJSONObject(i).getString("u");
									adevicename=resJSON.optJSONArray("devices")
											.getJSONObject(i).getString("name");
									Log.d(this.getClass().getName(), adevice);
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						returnstr = resJSON.optString("state")+" "+ resJSON.optString("error_description");
//						if (adevice == null) {
//							returnstr = "Устройство узнать не удалось";
//						} else {
//
//							returnstr = "Устройство найдено";
//						}

					}

				}

				if (params[3].equals("channel_enter")) {
					if (!(resJSON.optString("state").equals(""))) {
						returnstr = resJSON.optString("state")+" "+ resJSON.optString("error_description");

					} else {
						returnstr = getString(R.string.resultenterchanalnoget);
					}
				}

				if (params[3].equals("get_device_links")) {
					if (!(resJSON.optString("state").equals(""))) {
						returnstr = resJSON.optJSONArray("links")+" "+ resJSON.optString("error_description");

					} else {
						returnstr = getString(R.string.linksnotget);
					}
				}

				// Toast.makeText(GPSLocalServiceClient.this,resJSON.toString(),5).show();

			}

			return returnstr;

			// return null;
		}
	}



	public void onResultsSucceeded(APIComResult result) {

		if (result.Jo==null&&result.ja==null ){Toast.makeText(this,R.string.noanswerfromserver,5).show();}

		if (!(result.Jo==null)  ) {

			Toast.makeText(this,result.Jo.optString("state")+" "+ result.Jo.optString("error_description"),5).show();
		}		


		if (result.Command.equals("device_link")&& !(result.Jo==null)) {
			Toast.makeText(this,result.Jo.optString("state")+" "+ result.Jo.optString("error_description"),5).show();
			if (!(result.Jo.optString("url").equals(""))) {
				viewurl = result.Jo.optString("url");
				updateMainUI();

				

				OsMoDroid.editor.putString("view-url", viewurl);


				OsMoDroid.editor.commit();

				//returnstr = "URL найден";
			} else {
				Toast.makeText(this, R.string.urlnoget,Toast.LENGTH_LONG ).show();
			}
		}

			//Log.d(this.getClass().getSimpleName(),"Добавляли линк");





	}
	
	boolean saveSharedPreferencesToFile(File dst) {
	    boolean res = false;
	    ObjectOutputStream output = null;
	    try {
	        output = new ObjectOutputStream(new FileOutputStream(dst));
	        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
	        output.writeObject(pref.getAll());
	        Toast.makeText(this, R.string.prefsaved, Toast.LENGTH_SHORT).show();
	        res = true;
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }finally {
	        try {
	            if (output != null) {
	                output.flush();
	                output.close();
	            }
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
	    }
	    return res;
	}

	@SuppressWarnings({ "unchecked" }) boolean loadSharedPreferencesFromFile(File src) {
	    boolean res = false;
	    ObjectInputStream input = null;
	    try {
	        input = new ObjectInputStream(new FileInputStream(src));
	        OsMoDroid.editor.clear();
	            Map<String, ?> entries = (Map<String, ?>) input.readObject();
	            for (Entry<String, ?> entry : entries.entrySet()) {
	                Object v = entry.getValue();
	                String key = entry.getKey();

	                if (v instanceof Boolean)
	                	OsMoDroid.editor.putBoolean(key, ((Boolean) v).booleanValue());
	                else if (v instanceof Float)
	                	OsMoDroid.editor.putFloat(key, ((Float) v).floatValue());
	                else if (v instanceof Integer)
	                	OsMoDroid.editor.putInt(key, ((Integer) v).intValue());
	                else if (v instanceof Long)
	                	OsMoDroid.editor.putLong(key, ((Long) v).longValue());
	                else if (v instanceof String)
	                	OsMoDroid.editor.putString(key, ((String) v));
	            }
	            OsMoDroid.editor.commit();
	            Toast.makeText(this, R.string.prefloaded, Toast.LENGTH_SHORT).show();
	            setupDrawerList();
	        res = true;         
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    }finally {
	        try {
	            if (input != null) {
	                input.close();
	            }
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
	    }
	    return res;
	}


//	@Override
//public void onTabSelected(Tab tab, FragmentTransaction ft)
//{
//// Select proper stack
//		Log.d(this.getClass().getSimpleName(),"tab="+tab);
//		Log.d(this.getClass().getSimpleName(),"tag="+tab.getTag());
//		Log.d(this.getClass().getSimpleName(),"backStacks="+backStacks);
//		
//		Stack<String> backStack = backStacks.get(tab.getTag());
//Log.d(this.getClass().getSimpleName(),"backStack="+backStack);
//if (backStack.isEmpty())
//{
//// If it is empty instantiate and add initial tab fragment
//SherlockFragment fragment;
//switch ((TabType) tab.getTag())
//{
//case MAIN:
//fragment = (SherlockFragment) SherlockFragment.instantiate(this, MainFragment.class.getName());
//break;
//case DEVICES:
//	fragment = (SherlockFragment) SherlockFragment.instantiate(this, DevicesFragment.class.getName());
//break;
//case CHANNELS:
//	fragment = (SherlockFragment) SherlockFragment.instantiate(this, ChannelsFragment.class.getName());
//break;
//case LINKS:
//	fragment = (SherlockFragment) SherlockFragment.instantiate(this, SimLinksFragment.class.getName());
//break;
//case TRACKS:
//	fragment = (SherlockFragment) SherlockFragment.instantiate(this, TracFileListFragment.class.getName());
//break;
//case NOTIFS:
//	fragment = (SherlockFragment) SherlockFragment.instantiate(this, NotifFragment.class.getName());
//break;
//case STAT:
//	fragment = (SherlockFragment) SherlockFragment.instantiate(this, StatFragment.class.getName());
//break;
//case MAP:
//	fragment = (SherlockFragment) SherlockFragment.instantiate(this, MapFragment.class.getName());
//break;
//default:
//throw new java.lang.IllegalArgumentException("Unknown tab");
//}
//addFragment(fragment, backStack, ft);
//}
//else
//{
//// Show topmost fragment
//showFragment(backStack, ft);
//}
//}
 
//@Override
//public void onTabUnselected(Tab tab, FragmentTransaction ft)
//{
//// Select proper stack
//Stack<String> backStack = backStacks.get(tab.getTag());
//// Get topmost fragment
//String tag = backStack.peek();
//SherlockFragment fragment = (SherlockFragment) getSupportFragmentManager().findFragmentByTag(tag);
//// Detach it
//ft.detach(fragment);
//}
// 
//@Override
//public void onTabReselected(Tab tab, FragmentTransaction ft)
//{
//// Select proper stack
//Stack<String> backStack = backStacks.get(tab.getTag());
// 
//if (backStack.size() > 1)
////ft.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left);
//// Clean the stack leaving only initial fragment
//while (backStack.size() > 1)
//{
//// Pop topmost fragment
//String tag = backStack.pop();
//SherlockFragment fragment = (SherlockFragment) getSupportFragmentManager().findFragmentByTag(tag);
//// Remove it
//if(fragment!=null){ft.remove(fragment);}
//}
//showFragment(backStack, ft);
//}

}
