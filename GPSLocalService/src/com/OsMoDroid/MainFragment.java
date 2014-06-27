package com.OsMoDroid;

//import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.ClipboardManager;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.OsMoDroid.GPSLocalServiceClient.RequestCommandTask;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

public class MainFragment extends SherlockFragment implements ResultsListener, GPSLocalServiceClient.upd {
	
	private BroadcastReceiver receiver;
	private GPSLocalServiceClient globalActivity;
	
	@Override
	public void onDestroy() {
		Log.d(getClass().getSimpleName(), "mainfragment onDestroy");
		super.onDestroy();
	}
	@Override
	public void onDestroyView() {
		globalActivity.mainUpdListener = null;
		super.onDestroyView();
	}
	@Override
	public void onStop() {
		Log.d(getClass().getSimpleName(), "mainfragment onStop");
		super.onStop();
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.d(getClass().getSimpleName(), "mainfragment onActivityCreated");
		super.onActivityCreated(savedInstanceState);
	}
	 @Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         Log.d(getClass().getSimpleName(), "mainfragment oncreate");
         setHasOptionsMenu(true);
        // setRetainInstance(true);
         super.onCreate(savedInstanceState);
     }
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		Log.d(getClass().getSimpleName(), "mainfragment onresume");
		globalActivity.actionBar.setTitle(getString(R.string.tracker));
		updateMainUI();
		super.onResume();
	}

	 void updateMainUI() {
		 //Log.d(getClass().getSimpleName(), "mainfragment updateMainUI");

		String startStatus =globalActivity.checkStarted() ? getString(R.string.Running)
				: getString(R.string.NotRunning);
		String statusText = //getString(R.string.Status) + startStatus+
				getString(R.string.Sendcount) + globalActivity.sendcounter+"\n" + getString(R.string.inbuffer)+globalActivity.buffercounter;

		TextView t = (TextView) getView().findViewById(R.id.serviceStatus);
		t.setText(statusText);
		if (!OsMoDroid.settings.getBoolean("usealarm", false) || OsMoDroid.settings.getString("key", "").equals("")){
			ToggleButton alarmToggle = (ToggleButton)getView().findViewById(R.id.alarmButton);
			alarmToggle.setVisibility(View.GONE);
		}else{
			ToggleButton alarmToggle = (ToggleButton)getView().findViewById(R.id.alarmButton);
			alarmToggle.setVisibility(View.VISIBLE);
		}
		
		if (OsMoDroid.settings.getBoolean("usewake", false)){
			globalActivity.wakeLock = globalActivity.pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "MyWakeLock");
			globalActivity.wakeLock.acquire();
		}
		globalActivity.started = globalActivity.checkStarted();
		
		if (globalActivity.started) {
			Button start = (Button) getView().findViewById(R.id.startButton);
			Button stop = (Button) getView().findViewById(R.id.exitButton);
			start.setEnabled(false);
			stop.setEnabled(true);
		} else {
			Button start = (Button) getView().findViewById(R.id.startButton);
			Button stop = (Button) getView().findViewById(R.id.exitButton);
			start.setEnabled(true);
			stop.setEnabled(false);
		}
		
		TextView t2 = (TextView) getView().findViewById(R.id.URL);
		t2.setText(OsMoDroid.settings.getString("devicename", "")+" :\n "+globalActivity.viewurl);
		Linkify.addLinks(t2, Linkify.ALL);
		ToggleButton globalsendToggle = (ToggleButton) getView().findViewById(R.id.toggleButton1);
		Button auth = (Button) getView().findViewById(R.id.authButton);
		
		if (OsMoDroid.settings.getString("key", "").equals("")){
		globalsendToggle.setVisibility(View.GONE);
		auth.setVisibility(View.VISIBLE);
		}
		else {
			auth.setVisibility(View.GONE);
			globalsendToggle.setVisibility(View.VISIBLE);
		}
		
		
}
	

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragment#onCreateOptionsMenu(com.actionbarsherlock.view.Menu, com.actionbarsherlock.view.MenuInflater)
	 */
	public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
		//SubMenu menu1 = menu.addSubMenu(Menu.NONE, 11, 4, "Действия");
		SubMenu menu2 = menu.addSubMenu(Menu.NONE, 12, 4, R.string.more);
		
		MenuItem auth = menu2.add(0, 1, 0, R.string.RepeatAuth);
		MenuItem mi = menu.add(0, 2, 0, R.string.Settings);
		mi.setIcon(android.R.drawable.ic_menu_preferences);
		MenuItem mi3 = menu2.add(0, 3, 0, R.string.EqualsParameters);
		MenuItem forcesenditem = menu.add(0, 9, 0, R.string.sendnow);
		forcesenditem.setIcon(android.R.drawable.ic_menu_mylocation);
		MenuItem shareadress = menu.add(0, 10, 0, R.string.sharelink);
		shareadress.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		shareadress.setIcon(android.R.drawable.ic_menu_share);
		MenuItem copyadress = menu.add(0, 11, 0, R.string.copylink);
		copyadress.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		copyadress.setIcon(android.R.drawable.ic_menu_edit);
		MenuItem about = menu.add(0, 12, 0, R.string.about);
		about.setIcon(android.R.drawable.ic_menu_info_details);
		about.setIntent(new Intent(getSherlockActivity(), AboutActivity.class));
//		MenuItem exit = menu.add(0, 14, 0, R.string.exit);
		MenuItem save =menu2.add(0, 18, 0, R.string.savepref);
        MenuItem load =menu2.add(0, 19, 0, R.string.loadpref);
        MenuItem addlisten = menu.add(0, 20, 0, "listento");
        //MenuItem changeName = menu.add(0, 20, 0, R.string.changename);

                 super.onCreateOptionsMenu(menu, inflater);
	}
	@Override
	public void onPrepareOptionsMenu (Menu menu){

		 super.onPrepareOptionsMenu(menu);}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 1) {
			// case 1:
			AlertDialog alertdialog = new AlertDialog.Builder(
					getSherlockActivity()).create();
			alertdialog.setTitle(getString(R.string.AgreeRepeatAuth));

			alertdialog.setMessage(getString(R.string.ChangeAdresMonitor));

			alertdialog.setButton(getString(R.string.yes),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							globalActivity.requestHash();
							return;
						}
					});
			alertdialog.setButton2(getString(R.string.No),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

							return;
						}
					});
			alertdialog.show();
		}
		if (item.getItemId() == 2) {
			
			Intent intent = new Intent();
			intent.setClass(getSherlockActivity(),PrefActivity.class);
			startActivityForResult(intent, 0);
			
		}
		
		if (item.getItemId() == 3) {
			AlertDialog alertdialog1 = new AlertDialog.Builder(
					getSherlockActivity()).create();
			alertdialog1.setTitle(getString(R.string.AgreeParameterEqual));

			alertdialog1
					.setMessage(getString(R.string.TrackRecordParameterChanges));

			alertdialog1.setButton(getString(R.string.yes),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							globalActivity.speedbearing_gpx = globalActivity.speedbearing;
							globalActivity.bearing_gpx = globalActivity.bearing;
							globalActivity.hdop_gpx = globalActivity.hdop;
							globalActivity.period_gpx = globalActivity.period;
							globalActivity.distance_gpx = globalActivity.distance;
							OsMoDroid.editor.putString("period_gpx", Integer.toString(globalActivity.period_gpx));
							OsMoDroid.editor.putString("distance_gpx", Integer.toString(globalActivity.distance_gpx));
							OsMoDroid.editor.putString("speedbearing_gpx", Integer.toString(globalActivity.speedbearing_gpx));
							OsMoDroid.editor.putString("bearing_gpx", Integer.toString(globalActivity.bearing_gpx));
							OsMoDroid.editor.putString("hdop_gpx", Integer.toString(globalActivity.hdop_gpx));
							OsMoDroid.editor.commit();
						//	WritePref();
							return;
						}
					});

			alertdialog1.setButton2(getString(R.string.No),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

							return;
						}
					});
			alertdialog1.show();

		}
		if (item.getItemId() == 4) {

			globalActivity.auth();

	

		}

		
		if (item.getItemId() == 9) {
                    Log.d(getClass().getSimpleName(), "forcesend click");
                    if (globalActivity.conn == null || globalActivity.mService == null) {
                        Log.d(getClass().getSimpleName(), "нет бинда с сервисом");
                    } else {
                        Log.d(getClass().getSimpleName(), "вызов отправки позиции");
                        globalActivity.mService.sendPosition();
                        Log.d(getClass().getSimpleName(), "послек вызова отправки позиции");
                    }
		}
		if (item.getItemId() == 10) {
                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.setType("text/plain");
                    sendIntent.putExtra(android.content.Intent.EXTRA_TEXT,getString(R.string.iamhere)+ globalActivity.viewurl);
                    startActivity(Intent.createChooser(sendIntent, getSherlockActivity().getString(R.string.sharelink)));
		}
		if (item.getItemId() == 11) {
                    ClipboardManager clipboard = (ClipboardManager) getSherlockActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    if (globalActivity.viewurl != null)
                    clipboard.setText(globalActivity.viewurl);
                    Toast.makeText(getSherlockActivity(), R.string.linkcopied, Toast.LENGTH_SHORT).show();
		}
//		if (item.getItemId() == 14) {
//                    Intent i = new Intent(getSherlockActivity(), LocalService.class);
//                    globalActivity.stopService(i);
//                    globalActivity.finish();
//		}
		if (item.getItemId() == 18) {
          if (globalActivity.fileName!=null){
        	  globalActivity.saveSharedPreferencesToFile(globalActivity.fileName);}
}
		if (item.getItemId() == 19) {
			 if (globalActivity.fileName!=null&&globalActivity.fileName.exists()){
				 globalActivity.loadSharedPreferencesFromFile(globalActivity.fileName);
				 globalActivity.ReadPref();
					updateMainUI();
					globalActivity.mService.applyPreference();
			 
			 }
	          
		}
		if (item.getItemId() == 20) {
			// final View textEntryView = factory.inflate(R.layout.dialog,
			// null);
			LinearLayout layout = new LinearLayout(globalActivity);
			layout.setOrientation(LinearLayout.VERTICAL);
			final TextView txv5 = new TextView(globalActivity);
			txv5.setText("listen id");
			layout.addView(txv5);
	

			final EditText input = new EditText(globalActivity);
			//input2.setText("Ваше имя");
			layout.addView(input);
//		final EditText input = new EditText(this);
			//final TextView txv4 = new TextView(this);
			//txv4.setText("Одноразовый пароль можно получить по адресу http://esya.ru/app.html?act=add при наличии регистрации");
			//Linkify.addLinks(txv4, Linkify.ALL);
			//layout.addView(txv4);

			AlertDialog alertdialog3 = new AlertDialog.Builder(
					globalActivity)
					.setTitle(R.string.appauth)
					.setView(layout)
					.setPositiveButton(R.string.yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									String pass = input.getText().toString();
									if (!(pass.equals(""))) {
										globalActivity.mService.myIM.listenArrayList.add(input.getText().toString());
										if(globalActivity.mService.myIM.authed){
											globalActivity.mService.myIM.sendToServer("LISTEN|"+pass);
										}
									} else {
										Toast.makeText(
												globalActivity,
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
		
		return super.onOptionsItemSelected(item);

	}




	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(getClass().getSimpleName(), "mainfragment onCreateView");
		 
		
		final View view =  inflater.inflate(R.layout.main,container, false);
		if(!OsMoDroid.settings.getString("startmessage", "").equals("")){
		TextView tt = (TextView) view.findViewById(R.id.Location);
		tt.setText(getString(R.string.servermessage)+":\n"+OsMoDroid.settings.getString("startmessage", ""));
		}
		
		
//		 if (globalActivity.mService!=null&&globalActivity.mService.myIM!=null&&!OsMoDroid.settings.getString("key", "").equals("")){
//			 if(globalActivity.mService.myIM.connOpened&&!globalActivity.mService.myIM.connecting){
//		
//				 globalActivity.actionBar.setLogo(R.drawable.eyeo);
//			 } else if (globalActivity.mService.myIM.connecting) 
//			 {
//				 globalActivity.actionBar.setLogo(R.drawable.eyeu);
//			 }
//			 else
//			 {
//				 globalActivity.actionBar.setLogo(R.drawable.eyen);
//			 }
//				 
//		 }
		
		
			final ToggleButton alarmToggle = (ToggleButton) view.findViewById(R.id.alarmButton);
			if(OsMoDroid.settings.contains("signalisation")){
				alarmToggle.setChecked(true);
			} else 
			{
				alarmToggle.setChecked(false);
			}
			alarmToggle.setOnClickListener(new OnClickListener() {
				
				

				public void onClick(View v) {
					if (globalActivity.conn == null || globalActivity.mService == null) {

					} else {
	if (alarmToggle.isChecked()){
		globalActivity.mService.enableSignalisation(false);}
	else {
		globalActivity.mService.disableSignalisation(false);
	}
						

					}

				}
			});
			
			ToggleButton globalsendToggle = (ToggleButton) view.findViewById(R.id.toggleButton1);
			Button auth = (Button) view.findViewById(R.id.authButton);
			auth.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					globalActivity.auth();
					
				}
			});
			if (OsMoDroid.settings.getString("key", "").equals("")){
			globalsendToggle.setVisibility(View.GONE);
			}
			else {
				auth.setVisibility(View.GONE);
			}
			Button start = (Button) view.findViewById(R.id.startButton);
			Button exit = (Button) view.findViewById(R.id.exitButton);

			start.setEnabled(false);
			exit.setEnabled(false);

			 exit.setOnClickListener(new OnClickListener() {
                 Boolean stopsession=true;
                 public void onClick(View v) {
                         
                         
                         
if (globalActivity.live){
         
                         AlertDialog alertdialog = new AlertDialog.Builder(
                                         getSherlockActivity()).create();
                         alertdialog.setTitle(getSherlockActivity().getString(R.string.Stoping));

                         alertdialog.setMessage(getSherlockActivity().getString(R.string.closesession));

                         alertdialog.setButton(getString(R.string.yes),
                                         new DialogInterface.OnClickListener() {
                                                 public void onClick(DialogInterface dialog, int which) {
                                                         stopsession=true;
                                                         if (OsMoDroid.settings.getBoolean("gpx", true)&&OsMoDroid.settings.getBoolean("automaticupload", true)){
                                                                 AlertDialog alertdialog1 = new AlertDialog.Builder(
                                                                                 getSherlockActivity()).create();
                                                                 alertdialog1.setTitle(getSherlockActivity()
																		.getString(
																				R.string.loading));

                                                                 alertdialog1.setMessage(getSherlockActivity()
																		.getString(
																				R.string.uploadtrera));

                                                                 alertdialog1.setButton(getString(R.string.yes),
                                                                                 new DialogInterface.OnClickListener() {
                                                                                         public void onClick(DialogInterface dialog, int which) {
                                                                                                 LocalService.uploadto=true;
                                                                                                 globalActivity.stop(stopsession);
                                                                                                 updateServiceStatus(view);
                                                                                                 return;
                                                                                         }
                                                                                 });
                                                                 alertdialog1.setButton2(getString(R.string.No),
                                                                                 new DialogInterface.OnClickListener() {
                                                                                         public void onClick(DialogInterface dialog, int which) {
                                                                                                 LocalService.uploadto=false;
                                                                                                 globalActivity.stop(stopsession);
                                                                                                 updateServiceStatus(view);
                                                                                                 return;
                                                                                         }
                                                                                 });
                                                                 alertdialog1.show();
                                                         }
                                                         else
                                                         {
                                                                 LocalService.uploadto=false;
                                                                 globalActivity.stop(stopsession);
                                                                 updateServiceStatus(view);

                                                         }
                                                         return;
                                                 }
                                         });
                         alertdialog.setButton2(getString(R.string.No),
                                         new DialogInterface.OnClickListener() {
                                                 public void onClick(DialogInterface dialog, int which) {
                                                         stopsession=false;
                                                         if (OsMoDroid.settings.getBoolean("gpx", true)&&OsMoDroid.settings.getBoolean("automaticupload", true)){
                                                                 AlertDialog alertdialog1 = new AlertDialog.Builder(
                                                                                 getSherlockActivity()).create();
                                                                 alertdialog1.setTitle(getSherlockActivity()
																		.getString(
																				R.string.loading));

                                                                 alertdialog1.setMessage(getSherlockActivity()
																		.getString(
																				R.string.uploadtrera));

                                                                 alertdialog1.setButton(getString(R.string.yes),
                                                                                 new DialogInterface.OnClickListener() {
                                                                                         public void onClick(DialogInterface dialog, int which) {
                                                                                                 LocalService.uploadto=true;
                                                                                                 globalActivity.stop(stopsession);
                                                                                                 updateServiceStatus(view);
                                                                                                 return;
                                                                                         }
                                                                                 });
                                                                 alertdialog1.setButton2(getString(R.string.No),
                                                                                 new DialogInterface.OnClickListener() {
                                                                                         public void onClick(DialogInterface dialog, int which) {
                                                                                                 LocalService.uploadto=false;
                                                                                                 globalActivity.stop(stopsession);
                                                                                                 updateServiceStatus(view);
                                                                                                 return;
                                                                                         }
                                                                                 });
                                                                 alertdialog1.show();
                                                         }
                                                         else
                                                         {
                                                                 LocalService.uploadto=false;
                                                                 globalActivity.stop(stopsession);
                                                                 updateServiceStatus(view);

                                                         }
                                                         return;
                                                 }
                                         });
                         alertdialog.show();
                         
}
else {
 
 if (OsMoDroid.settings.getBoolean("gpx", true)&&OsMoDroid.settings.getBoolean("automaticupload", true)){
         AlertDialog alertdialog1 = new AlertDialog.Builder(
                         getSherlockActivity()).create();
         alertdialog1.setTitle(getSherlockActivity().getString(R.string.loading));

         alertdialog1.setMessage(getSherlockActivity().getString(R.string.uploadtrera));

         alertdialog1.setButton(getString(R.string.yes),
                         new DialogInterface.OnClickListener() {
                                 public void onClick(DialogInterface dialog, int which) {
                                         LocalService.uploadto=true;
                                         globalActivity.stop(stopsession);
                                         updateServiceStatus(view);
                                         return;
                                 }
                         });
         alertdialog1.setButton2(getString(R.string.No),
                         new DialogInterface.OnClickListener() {
                                 public void onClick(DialogInterface dialog, int which) {
                                         LocalService.uploadto=false;
                                         globalActivity.stop(stopsession);
                                         updateServiceStatus(view);
                                         return;
                                 }
                         });
         alertdialog1.show();
 }
 else
 {
         LocalService.uploadto=false;
         globalActivity.stop(stopsession);
         updateServiceStatus(view);

 }
 
}


                 
                 }
         });

			start.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if(OsMoDroid.settings.getBoolean("usegps", true)){
						if(!globalActivity.mService.myManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
						{
							AlertDialog alertdialog1 = new AlertDialog.Builder(
			                         getSherlockActivity()).create();
			         alertdialog1.setTitle(getSherlockActivity().getString(R.string.needgps));

			         alertdialog1.setMessage(getSherlockActivity().getString(R.string.enablegps));

			         alertdialog1.setButton(getString(R.string.yes),
			                         new DialogInterface.OnClickListener() {
			                                 public void onClick(DialogInterface dialog, int which) {
			                                	 Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			                                	    startActivity(intent);
			                                         return;
			                                 }
			                         });
			         alertdialog1.setButton2(getString(R.string.No),
	                         new DialogInterface.OnClickListener() {
	                                 public void onClick(DialogInterface dialog, int which) {
	                                         return;
	                                 }
	                         });
			         alertdialog1.show();
						}
								
					}
					
					globalActivity.startlocalservice();
					

				}
			});

			receiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, final Intent intent) {
					TextView dt = (TextView) view.findViewById(R.id.URL);
					dt.setText(OsMoDroid.settings.getString("devicename", "")+" :\n "+OsMoDroid.settings.getString("viewurl", ""));

					Linkify.addLinks(dt, Linkify.ALL);
					//TextView t = (TextView) view.findViewById(R.id.Location);
					globalActivity.sendcounter = intent.getIntExtra("sendcounter", 0);
					globalActivity.buffercounter = intent.getIntExtra("buffercounter", 0);
					globalActivity.position = intent.getStringExtra("position");
					globalActivity.sendresult = intent.getStringExtra("sendresult");
					String stat = intent.getStringExtra("stat");
					String startmessage = intent.getStringExtra("motd");
					if (intent.hasExtra("globalsend")){
						final ToggleButton globalsendToggle = (ToggleButton) view.findViewById(R.id.toggleButton1);
						globalsendToggle.setOnClickListener(new OnClickListener() {
							
							public void onClick(View v) {
	globalsendToggle.toggle();
	String boolglobalsend =intent.getBooleanExtra("globalsend", false) ? "0" : "1";
	Netutil.newapicommand((ResultsListener)globalActivity.mService,(Context)globalActivity, "om_device_channel_active:"+OsMoDroid.settings.getString("device", "")+",0,"+boolglobalsend);

							}
						});
						globalsendToggle.setChecked(intent.getBooleanExtra("globalsend", false));
						
					}
					if (intent.hasExtra("signalisationon")){
						ToggleButton alarmButton=(ToggleButton) view.findViewById(R.id.alarmButton);
						alarmButton.setChecked(intent.getBooleanExtra("signalisationon", false));
					}
					
					if (intent.hasExtra("started")){
						
						Button start = (Button) view.findViewById(R.id.startButton);
						Button stop = (Button) view.findViewById(R.id.exitButton);


							start.setEnabled(!intent.getBooleanExtra("started", false));

							stop.setEnabled(intent.getBooleanExtra("started", false));
							globalActivity.started=intent.getBooleanExtra("started", false);
					}
					
				

//					if (!(startmessage==null)&&!globalActivity.messageShowed) {
					if (!(startmessage==null)) {
						
						TextView tt = (TextView) view.findViewById(R.id.Location);
						tt.setText(getString(R.string.servermessage)+":\n"+startmessage);
						globalActivity.messageShowed=true;
						

					}



					//if (globalActivity.position == null){globalActivity.position = context.getString(R.string.NotDefined);}


					if (globalActivity.sendresult == null){	globalActivity.sendresult = "";}
					TextView t2 = (TextView) view.findViewById(R.id.Send);
					updateServiceStatus(view);
					if (!(globalActivity.sendresult == null)){t2.setText(getString(R.string.Sended) +"\n"+ (globalActivity.sendresult));}
				}


			};

			globalActivity.registerReceiver(receiver, new IntentFilter("OsMoDroid"));
		return view;

	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		Log.d(getClass().getSimpleName(), "mainfragment onAttach");
		globalActivity = (GPSLocalServiceClient)activity;// TODO Auto-generated method stub
		globalActivity.mainUpdListener = this;
		super.onAttach(activity);
	}
	@Override
	public void onDetach() {
		Log.d(getClass().getSimpleName(), "mainfragment onDetach");
		if (receiver != null) {
			globalActivity.unregisterReceiver(receiver);
		}
		
		globalActivity=null;
		super.onDetach();
	}
	
	private void updateServiceStatus(View view) {
		//Log.d(getClass().getSimpleName(), "mainfragment updateservicestatus() gpsclient");
		String startStatus =globalActivity.checkStarted() ? getString(R.string.Running)
				: getString(R.string.NotRunning);
		String statusText = //getString(R.string.Status) + startStatus+
				getString(R.string.Sendcount) + globalActivity.sendcounter +"\n"+ getSherlockActivity().getString(R.string.inbuffer)+globalActivity.buffercounter;
		TextView t = (TextView) view.findViewById(R.id.serviceStatus);
		t.setText(statusText);
	}
	
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		if(globalActivity!=null&&globalActivity.mService!=null){globalActivity.mService.refresh();}
		super.onViewCreated(view, savedInstanceState);
	}
	@Override
	public void onResultsSucceeded(APIComResult result) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void update() {
		updateMainUI();
		
	}

}
