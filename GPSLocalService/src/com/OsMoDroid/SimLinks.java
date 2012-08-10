package com.OsMoDroid;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.OsMoDroid.GPSLocalServiceClient.RequestCommandTask;

import android.app.Activity;
import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


public class SimLinks extends Activity implements ResultsListener{
	private ArrayAdapter<String> adapter;
	// String[] simlinksarray;
	 ArrayList<String> list;
	//
	public SimLinks() {
		// TODO Auto-generated constructor stub
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.simlinks); 
	    final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(this);
	    
	    ListView lv1 = (ListView) findViewById(R.id.listView1);
	        list = new ArrayList<String>();
	        list.clear();
	       adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list);
	       lv1.setAdapter(adapter);
	   	   
	    Button refsimlinkbutton = (Button) findViewById(R.id.refreshsimlinksbutton);
	    refsimlinkbutton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				String[] params = {
						"http://api.esya.ru/?system=om&action=get_device_links&hash="+settings.getString("hash", "")+"&n="+Integer.parseInt(settings.getString("n", "0").equals("") ? "0"
								: settings.getString("n", "0"))
								+ "&key="
								+ GPSLocalServiceClient.key
								+ "&signature="
								+ GPSLocalServiceClient.SHA1(
										"system:om;action:get_device_links;hash:"+settings.getString("hash", "")+";n:"+Integer.parseInt(settings.getString("n", "0").equals("") ? "0"
												: settings.getString("n", "0")) +";key:"
												+ GPSLocalServiceClient.key
												+ ";"
												+ "--"
												+ "JGu473g9DFj3y_gsh463j48hdsgl34lqzkvnr420gdsg-32hafUehcDaw3516Ha-aghaerUhhvF42123na38Agqmznv_46bd-67ogpwuNaEv6")
										.substring(1, 25), "false", "",
						"device" };
				new netutil.MyAsyncTask(SimLinks.this).execute(params) ;
				 
				//String[]
				// params={"http://api.esya.ru/?system=om&action=device&key="+commandJSON.optString("key")+"&signature="+SHA1("system:om;action:device;key:"+commandJSON.optString("key")+";"+"--"+"JGu473g9DFj3y_gsh463j48hdsgl34lqzkvnr420gdsg-32hafUehcDaw3516Ha-aghaerUhhvF42123na38Agqmznv_46bd-67ogpwuNaEv6").substring(1,
				// 25),"false",""};
				// +commandJSON.optString("key")+
				Log.d(getClass().getSimpleName(), params[0]);
			//	RequestCommandTask Rq = new GPSLocalServiceClient.RequestCommandTask();
			//	Rq.execute(params);
				
				
				//simlinksarray= new String[] {"df"};
			//	list.add("a");
				//adapter.notifyDataSetChanged();

			}});
	   
	    
	    // TODO Auto-generated method stub
	}

	

	public void onResultsSucceeded(JSONObject result) {
		JSONArray a = null; 
		try {
			  a =	result.getJSONArray("links");
			  Log.d(getClass().getSimpleName(), a.toString());
			  
			} catch (JSONException e) {
				
				 Log.d(getClass().getSimpleName(), "Не нашли links или другой эксепшн");
				e.printStackTrace();
			}
		 for (int i = 0; i < result.length(); i++) {
            
				// TODO Auto-generated catch block
			
			
             try {
            	 Log.d(getClass().getSimpleName(), a.getJSONObject(i).toString());
            	 list.add(a.getJSONObject(i).getString("value").toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
             catch (Exception e)
             {				e.printStackTrace();}
             
         }
		
		// TODO Auto-generated method stub
		adapter.notifyDataSetChanged();
	}

}
