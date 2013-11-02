package com.OsMoDroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

public class MainFragment extends SherlockFragment implements ResultsListener {
	
	private BroadcastReceiver receiver;
	private GPSLocalServiceClient globalActivity;
	
	@Override
	public void onDestroy() {
		if (receiver != null) {
			globalActivity.unregisterReceiver(receiver);
		}
		super.onDestroy();
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		globalActivity = (GPSLocalServiceClient) getSherlockActivity();
		super.onActivityCreated(savedInstanceState);
	}
	 @Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setHasOptionsMenu(true);
         setRetainInstance(true);
         super.onCreate(savedInstanceState);
     }
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
	//	updateServiceStatus(getView());
		globalActivity.updateMainUI();
		super.onResume();
	}


	

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragment#onCreateOptionsMenu(com.actionbarsherlock.view.Menu, com.actionbarsherlock.view.MenuInflater)
	 */
	public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
		//SubMenu menu1 = menu.addSubMenu(Menu.NONE, 11, 4, "Действия");
		SubMenu menu2 = menu.addSubMenu(Menu.NONE, 12, 4, "Ещё");

		MenuItem auth = menu2.add(0, 1, 0, R.string.RepeatAuth);
		MenuItem mi = menu.add(0, 2, 0, R.string.Settings);
		mi.setIcon(android.R.drawable.ic_menu_preferences);
		MenuItem mi3 = menu2.add(0, 3, 0, R.string.EqualsParameters);
		MenuItem forcesenditem = menu.add(0, 9, 0, R.string.sendnow);
		forcesenditem.setIcon(android.R.drawable.ic_menu_mylocation);
		MenuItem shareadress = menu.add(0, 10, 0, "Поделиться ссылкой");
		shareadress.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		shareadress.setIcon(android.R.drawable.ic_menu_share);
		MenuItem copyadress = menu.add(0, 11, 0, "Копировать ссылку");
		copyadress.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		copyadress.setIcon(android.R.drawable.ic_menu_edit);
		MenuItem about = menu.add(0, 12, 0, R.string.about);
		about.setIcon(android.R.drawable.ic_menu_info_details);
		about.setIntent(new Intent(getSherlockActivity(), aboutActivity.class));
		MenuItem exit = menu.add(0, 14, 0, "Выход");
		MenuItem save =menu2.add(0, 18, 0, "Сохранить настройки на карту");
        MenuItem load =menu2.add(0, 19, 0, "Загрузить настройки с карты");
              

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

							SharedPreferences.Editor editor = globalActivity.settings.edit();
							editor.putString("period_gpx", Integer.toString(globalActivity.period_gpx));
							editor.putString("distance_gpx", Integer.toString(globalActivity.distance_gpx));
							editor.putString("speedbearing_gpx", Integer.toString(globalActivity.speedbearing_gpx));
							editor.putString("bearing_gpx", Integer.toString(globalActivity.bearing_gpx));
							editor.putString("hdop_gpx", Integer.toString(globalActivity.hdop_gpx));
							editor.commit();
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
                    sendIntent.putExtra(android.content.Intent.EXTRA_TEXT,"Я тут! "+ globalActivity.viewurl);
                    startActivity(Intent.createChooser(sendIntent, "Поделиться ссылкой"));
		}
		if (item.getItemId() == 11) {
                    ClipboardManager clipboard = (ClipboardManager) getSherlockActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    if (globalActivity.viewurl != null)
                    clipboard.setText(globalActivity.viewurl);
                    Toast.makeText(getSherlockActivity(), "Ссылка скопирована в буфер обмена", Toast.LENGTH_SHORT).show();
		}
		if (item.getItemId() == 14) {
                    Intent i = new Intent(getSherlockActivity(), LocalService.class);
                    globalActivity.stopService(i);
                    globalActivity.finish();
		}
		if (item.getItemId() == 18) {
          if (globalActivity.fileName!=null){
        	  globalActivity.saveSharedPreferencesToFile(globalActivity.fileName);}
}
		if (item.getItemId() == 19) {
			 if (globalActivity.fileName!=null&&globalActivity.fileName.exists()){
				 globalActivity.loadSharedPreferencesFromFile(globalActivity.fileName);
				 globalActivity.ReadPref();
					globalActivity.updateMainUI();
					globalActivity.mService.applyPreference();
			 
			 }
	          
		}
		
		
		return super.onOptionsItemSelected(item);

	}




	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		 final View view =  inflater.inflate(R.layout.main,container, false);
			final ToggleButton alarmToggle = (ToggleButton) view.findViewById(R.id.alarmButton);
			if(globalActivity.settings.contains("signalisation")){
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
			if (globalActivity.settings.getString("key", "").equals("")){
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
                         alertdialog.setTitle("Остановка");

                         alertdialog.setMessage("Закрыть сессию?");

                         alertdialog.setButton(getString(R.string.yes),
                                         new DialogInterface.OnClickListener() {
                                                 public void onClick(DialogInterface dialog, int which) {
                                                         stopsession=true;
                                                         if (!OsMoDroid.settings.getBoolean("automaticupload", true)){
                                                                 AlertDialog alertdialog1 = new AlertDialog.Builder(
                                                                                 getSherlockActivity()).create();
                                                                 alertdialog1.setTitle("Загрузка");

                                                                 alertdialog1.setMessage("Загрузить на ТреРа?");

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
                                                                 LocalService.uploadto=true;
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
                                                         if (!OsMoDroid.settings.getBoolean("automaticupload", true)){
                                                                 AlertDialog alertdialog1 = new AlertDialog.Builder(
                                                                                 getSherlockActivity()).create();
                                                                 alertdialog1.setTitle("Загрузка");

                                                                 alertdialog1.setMessage("Загрузить на ТреРа?");

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
                                                                 LocalService.uploadto=true;
                                                                 globalActivity.stop(stopsession);
                                                                 updateServiceStatus(view);

                                                         }
                                                         return;
                                                 }
                                         });
                         alertdialog.show();
                         
}
else {
 
 if (!OsMoDroid.settings.getBoolean("automaticupload", true)){
         AlertDialog alertdialog1 = new AlertDialog.Builder(
                         getSherlockActivity()).create();
         alertdialog1.setTitle("Загрузка");

         alertdialog1.setMessage("Загрузить на ТреРа?");

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
         LocalService.uploadto=true;
         globalActivity.stop(stopsession);
         updateServiceStatus(view);

 }
 
}


                 
                 }
         });

			start.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					globalActivity.startlocalservice();

				}
			});

			receiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, final Intent intent) {
					TextView dt = (TextView) view.findViewById(R.id.URL);
					dt.setText(globalActivity.settings.getString("devicename", "")+" : "+globalActivity.viewurl);

					Linkify.addLinks(dt, Linkify.ALL);
					TextView t = (TextView) view.findViewById(R.id.Location);
					globalActivity.sendcounter = intent.getIntExtra("sendcounter", 0);
					globalActivity.buffercounter = intent.getIntExtra("buffercounter", 0);
					globalActivity.position = intent.getStringExtra("position");
					globalActivity.sendresult = intent.getStringExtra("sendresult");
					String stat = intent.getStringExtra("stat");
					String startmessage = intent.getStringExtra("startmessage");
					if (intent.hasExtra("globalsend")){
						final ToggleButton globalsendToggle = (ToggleButton) view.findViewById(R.id.toggleButton1);
						globalsendToggle.setOnClickListener(new OnClickListener() {
							
							public void onClick(View v) {
	globalsendToggle.toggle();
	String boolglobalsend =intent.getBooleanExtra("globalsend", false) ? "0" : "1";
	netutil.newapicommand((ResultsListener)globalActivity.mService,(Context)globalActivity, "om_device_channel_active:"+globalActivity.settings.getString("device", "")+",0,"+boolglobalsend);

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

					if (!(startmessage==null)&&!globalActivity.messageShowed) {
						TextView t2 = (TextView) view.findViewById(R.id.URL);
						t2.setText(globalActivity.settings.getString("devicename", "")+" : "+globalActivity.viewurl);
						Linkify.addLinks(t2, Linkify.ALL);
						
						globalActivity.messageShowed=true;
						AlertDialog alertdialog = new AlertDialog.Builder(
								globalActivity).create();
						alertdialog.setTitle("Сообщение от сервера");

						alertdialog.setMessage(startmessage);



						alertdialog.setButton(getString(R.string.yes),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {

										return;
									}
								});

						alertdialog.show();

					}



					if (globalActivity.position == null){globalActivity.position = context.getString(R.string.NotDefined);}


					if (globalActivity.sendresult == null){	globalActivity.sendresult = "";}
					t.setText(

							globalActivity.position+"\n"+stat);
					TextView t2 = (TextView) view.findViewById(R.id.Send);

					updateServiceStatus(view);
					if (!(globalActivity.sendresult == null)){t2.setText(getString(R.string.Sended) + (globalActivity.sendresult));}
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
		globalActivity = (GPSLocalServiceClient)activity;// TODO Auto-generated method stub
		super.onAttach(activity);
	}
	
	private void updateServiceStatus(View view) {
		// Log.d(getClass().getSimpleName(), "updateservicestatus() gpsclient");
		String startStatus =globalActivity.checkStarted() ? getString(R.string.Running)
				: getString(R.string.NotRunning);
		String statusText = //getString(R.string.Status) + startStatus+
				getString(R.string.Sendcount) + globalActivity.sendcounter + " В буфере: "+globalActivity.buffercounter;
		TextView t = (TextView) view.findViewById(R.id.serviceStatus);
		t.setText(statusText);
	}
	
	
	@Override
	public void onResultsSucceeded(APIComResult result) {
		// TODO Auto-generated method stub
		
	}

}
