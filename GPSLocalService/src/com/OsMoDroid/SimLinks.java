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
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


public class SimLinks extends Activity implements ResultsListener{
	private ArrayAdapter<String> adapter;
	// String[] simlinksarray;
	 ArrayList<String> list;
	 SharedPreferences settings;
	 final private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
	 //
	public SimLinks() {
		// TODO Auto-generated constructor stub
	}
	
	void reflinks(){
		String[] params = {
				"http://api.esya.ru/?system=om&action=device_links&device="+settings.getString("device", "")
						+ "&key="
						+ GPSLocalServiceClient.key
						+ "&signature="
						+ GPSLocalServiceClient.SHA1(
								"system:om;action:device_links;device:"+settings.getString("device", "") +";key:"
										+ GPSLocalServiceClient.key
										+ ";"
										+ "--"
										+ "JGu473g9DFj3y_gsh463j48hdsgl34lqzkvnr420gdsg-32hafUehcDaw3516Ha-aghaerUhhvF42123na38Agqmznv_46bd-67ogpwuNaEv6")
								.substring(1, 25), "false", "",
				"device_links" };
		new netutil.MyAsyncTask(SimLinks.this).execute(params) ;
	 
	
		Log.d(getClass().getSimpleName(), params[0]);
	}
	
	void addlink(){
		//--
		//[19:27:37.138] GET http://api.esya.ru/?system=om&key=H83_fdDGd34i85gDsd4f&action=add_link&url=zxccvb&item=764&type=0&on=2012-08-16%2019:27:00&off=2012-08-17%2000:00:00&format=jsonp&callback=jQuery17107581103815227868_1345130731429&_=1345130857118 [HTTP/1.1 200 OK 122мс]
		String[] params = {
				"http://api.esya.ru/?system=om&action=link_add&url=" +System.currentTimeMillis() +"&item="+settings.getString("device", "")
				
						+ "&key="
						+ GPSLocalServiceClient.key
						+ "&signature="
						+ GPSLocalServiceClient.SHA1(
								"system:om;action:link_add;url:"+System.currentTimeMillis()+";item:"+settings.getString("device", "") +";key:"
										+ GPSLocalServiceClient.key
										+ ";"
										+ "--"
										+ "JGu473g9DFj3y_gsh463j48hdsgl34lqzkvnr420gdsg-32hafUehcDaw3516Ha-aghaerUhhvF42123na38Agqmznv_46bd-67ogpwuNaEv6")
								.substring(1, 25), "false", "",
				"link_add" };
		new netutil.MyAsyncTask(SimLinks.this).execute(params) ;
	 
	
		Log.d(getClass().getSimpleName(), params[0]);
	
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
	       adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list);
	       lv1.setAdapter(adapter);
	     
	       lv1.setOnItemClickListener(
	    	        new OnItemClickListener()
	    	        {

						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							// TODO Auto-generated method stub
							
							
							Intent sendIntent = new Intent(Intent.ACTION_SEND);
							sendIntent.setType("text/plain");
							sendIntent.putExtra(android.content.Intent.EXTRA_TEXT, adapter.getItem(arg2));
							startActivity(Intent.createChooser(sendIntent, "Email"));
						}

//	    	            @Override
//	    	            public void onItemClick(AdapterView<?> arg0, View view,
//	    	                    int position, long id) {
//	    	                // TODO Auto-generated method stub
//	    	                Object o = list1.getItemAtPosition(position);
//	    	                String pen = o.toString();
//	    	             //   Toast.makeText(getApplicationContext(), "You have chosen the pen: " + " " + pen, Toast.LENGTH_LONG).show();
//
//	    	            }   
	    	        }       
	    	);

	       

	       
	       
	    Button refsimlinkbutton = (Button) findViewById(R.id.refreshsimlinksbutton);
	    Button addsimlinkbutton = (Button) findViewById(R.id.addsimlinksbutton);
	    if (settings.getString("device", "").equals("")){
	    	addsimlinkbutton.setEnabled(false);
	    	refsimlinkbutton.setEnabled(false);
	    } else {  reflinks();}
	    refsimlinkbutton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				reflinks();
				
			

			}});
	    
	    addsimlinkbutton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				addlink();
				

			}});
	    
	    
	   
	  	
	    // TODO Auto-generated method stub
	}

	

	public void onResultsSucceeded(APIComResult result) {
		JSONObject a = null; 
		
		if (result.Command.equals("device_links")) {
		list.clear();
		try {
			  a =	result.Jo.getJSONObject("links");
			  Log.d(getClass().getSimpleName(), a.toString());
			 
			   Iterator i = a.keys();
			  while (i.hasNext())
          	{
          		String keyname = (String)i.next();
list.add(a.getString(keyname));
Log.d(getClass().getSimpleName(), list.toString());
          	}
			  
			  
			} catch (Exception e) {
				
				 Log.d(getClass().getSimpleName(), "Не нашли links или другой эксепшн");
				e.printStackTrace();
			}
	
         
		 Log.d(getClass().getSimpleName(),list.toString());
		// TODO Auto-generated method stub
		adapter.notifyDataSetChanged();
		}
		
		if (result.Command.equals("link_add")) 
		{
			
				
					Toast.makeText(this,result.Jo.optString("state")+" "+ result.Jo.optString("error_description"),5).show();
					 reflinks();	
					
			
				}
			 
			
			
			Log.d(getClass().getSimpleName(),"Добавляли линк");
		
			 
			 
			 
			}
		
		
	}
	


