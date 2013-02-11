package com.OsMoDroid;

import java.text.SimpleDateFormat;
import java.util.ArrayList;import java.util.HashMap;
import java.util.Arrays;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.OsMoDroid.GPSLocalServiceClient.RequestCommandTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.ClipboardManager;
import android.text.format.Time;
import android.text.util.Linkify;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MyDevices extends Activity implements ResultsListener{

	 SharedPreferences settings;
	 final private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
	 //
	public MyDevices() {
		// TODO Auto-generated constructor stub
	}
	
	void getDevices(){
		netutil.newapicommand((ResultsListener)MyDevices.this, "om_device");
		Log.d(getClass().getSimpleName(), "GetDevices");
	}
	
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.mydevices); 
	    settings  = PreferenceManager.getDefaultSharedPreferences(this);
	    
	    final ListView lv1 = (ListView) findViewById(R.id.mydeviceslistView);
	      
	       LocalService.deviceAdapter = new DeviceAdapter(this,R.layout.deviceitem, LocalService.deviceList);
	       lv1.setAdapter(LocalService.deviceAdapter);
	       registerForContextMenu(lv1);
	  lv1.setOnItemClickListener(new OnItemClickListener() {

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
						arg0.showContextMenuForChild(arg1);
						}
	});
	       
	    Button refsimlinkbutton = (Button) findViewById(R.id.refreshsimlinksbutton);    
	    refsimlinkbutton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				getDevices();
			}});
	    
	   
	    getDevices(); 
	}
	
	
	 @Override
	  public void onCreateContextMenu(ContextMenu menu, View v,
	      ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    menu.add(0, 1, 0, "Написать сообщение").setIcon(android.R.drawable.ic_menu_share);;
	    menu.add(0, 2, 0, "Проверить связь").setIcon(android.R.drawable.ic_menu_delete);;
	    menu.add(0, 3, 0, "Проверить батарею").setIcon(android.R.drawable.ic_menu_edit);;
	    
	    
	  }

	  @Override
	  public boolean onContextItemSelected(MenuItem item) {
	  
		  if (item.getItemId() == 1) {
				 final AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();
				 LocalService.deviceAdapter.getItem(acmi.position);
			  LinearLayout layout = new LinearLayout(this);
				layout.setOrientation(LinearLayout.VERTICAL);
				final TextView txv = new TextView(this);
				txv.setText("Ваше сообщение:");
				layout.addView(txv);
				final EditText input = new EditText(this);
				layout.addView(input);
				
				AlertDialog alertdialog3 = new AlertDialog.Builder(
						this)
						.setTitle("Отправка сообщения")
						.setView(layout)
						.setPositiveButton(R.string.yes,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										 
										if (!(input.getText().toString().equals(""))) {
											JSONObject postjson = new JSONObject();
											
											try {
											postjson.put("text", input.getText().toString());
											netutil.newapicommand((ResultsListener) MyDevices.this, "im_send:0"+","+LocalService.deviceList.get((int) acmi.id).app,"json="+postjson.toString());
											} catch (JSONException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
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

			  
		
		
				
		         
		      return true;
		    }
			  		  
		  if (item.getItemId() == 2) {
	         AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();
	    
			 
	      return true;
	    }
		  if (item.getItemId() == 3) {
		         AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();
		         ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
					
						clipboard.setText(LocalService.deviceAdapter.getItem(acmi.position).url);
				 
		      return true;
		    }
		  
	    return super.onContextItemSelected(item);
	  }
	
	

	public void onResultsSucceeded(APIComResult result) {
		JSONArray a = null; 
		Log.d(getClass().getSimpleName(),"OnResultListener:"+result);	
		if (result.Command.equals("APIM")&& !(result.Jo==null))
		{
			Log.d(getClass().getSimpleName(),"APIM Response:"+result.Jo);
			if (result.Jo.has("om_device")){				LocalService.deviceList.clear();				//list.clear();				//listids.clear();				try {					  a =	result.Jo.getJSONArray("om_device");			 		  Log.d(getClass().getSimpleName(), a.toString());			 		 for (int i = 0; i < a.length(); i++) {			 			JSONObject jsonObject = a.getJSONObject(i);			 			 							//list.add(jsonObject.getString("name"));		//listids.add(jsonObject.getString("app"));		Log.d(getClass().getSimpleName(), LocalService.deviceList.toString());		//Log.d(getClass().getSimpleName(), listids.toString());		Device devitem = new Device(jsonObject.getString("u"), jsonObject.getString("name"),jsonObject.getString("app")				,jsonObject.getString("last"),				jsonObject.getString("url"),				jsonObject.getString("where"),				jsonObject.getString("lat"),				jsonObject.getString("lon"),				jsonObject.getString("online"),				jsonObject.getString("state")				); 	 				LocalService.deviceList.add(devitem);			 		 			 		 			 		 			 		 } 					} catch (Exception e) {												 Log.d(getClass().getSimpleName(), "эксепшн");						//e.printStackTrace();					}			   				 Log.d(getClass().getSimpleName(),LocalService.deviceList.toString());								 LocalService.deviceAdapter.notifyDataSetChanged();
			}
		}
	}
}
	


