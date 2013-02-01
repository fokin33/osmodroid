package com.OsMoDroid;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.OsMoDroid.GPSLocalServiceClient.RequestCommandTask;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.ClipboardManager;
import android.text.format.Time;
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
import android.widget.ListView;
import android.widget.Toast;


public class MyDevices extends Activity implements ResultsListener{
	private ArrayAdapter<String> adapter;
	// String[] simlinksarray;
	 ArrayList<String> list;
	 ArrayList<String> listids;
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
	    setContentView(R.layout.simlinks); 
	    settings  = PreferenceManager.getDefaultSharedPreferences(this);
	    
	    final ListView lv1 = (ListView) findViewById(R.id.listView1);
	        list = new ArrayList<String>();
	        list.clear();
	        listids = new ArrayList<String>();
	        listids.clear();
	       adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list);
	       lv1.setAdapter(adapter);
	       registerForContextMenu(lv1);
	  lv1.setOnItemClickListener(new OnItemClickListener() {

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
						arg0.showContextMenuForChild(arg1);
						}
	});
	       
	    Button refsimlinkbutton = (Button) findViewById(R.id.refreshsimlinksbutton);
	    Button addsimlinkbutton = (Button) findViewById(R.id.addsimlinksbutton);
	    
	    refsimlinkbutton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				getDevices();
			}});
	    
	   
	   
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
		         AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();
			  
			 adapter.getItem(acmi.position);
			 netutil.newapicommand((ResultsListener)this, "im_send:0"+","+listids.get((int) acmi.id),"test");
				
		         
		      return true;
		    }
			  		  
		  if (item.getItemId() == 2) {
	         AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();
	    
			 
	      return true;
	    }
		  if (item.getItemId() == 3) {
		         AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();
		         ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
					
						clipboard.setText(adapter.getItem(acmi.position));
				 
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
			if (result.Jo.has("om_device")){
				
				list.clear();
				listids.clear();
				try {
					  a =	result.Jo.getJSONArray("om_device");
			 		  Log.d(getClass().getSimpleName(), a.toString());
			 		 for (int i = 0; i < a.length(); i++) {
			 			JSONObject jsonObject = a.getJSONObject(i);
			 			 
					
		list.add(jsonObject.getString("name"));
		listids.add(jsonObject.getString("app"));
		Log.d(getClass().getSimpleName(), list.toString());
		Log.d(getClass().getSimpleName(), listids.toString());
		 	 		 } 
					} catch (Exception e) {
						
						 Log.d(getClass().getSimpleName(), "эксепшн");
						//e.printStackTrace();
					}
			
   
				 Log.d(getClass().getSimpleName(),list.toString());
				
				 adapter.notifyDataSetChanged();
			}
		}
	}
}
	


