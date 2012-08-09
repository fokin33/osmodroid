package com.OsMoDroid;

import java.util.ArrayList;
import java.util.Arrays;

import com.OsMoDroid.GPSLocalServiceClient.RequestCommandTask;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
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
	    ListView lv1 = (ListView) findViewById(R.id.listView1);
	        list = new ArrayList<String>();
	        list.clear();
	       adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list);
	       lv1.setAdapter(adapter);
	   	   
	    Button refsimlinkbutton = (Button) findViewById(R.id.refreshsimlinksbutton);
	    refsimlinkbutton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				String[] params = {
						"http://api.esya.ru/?system=om&action=device"
								+ "&key="
								+ GPSLocalServiceClient.key
								+ "&signature="
								+ GPSLocalServiceClient.SHA1(
										"system:om;action:device;key:"
												+ GPSLocalServiceClient.key
												+ ";"
												+ "--"
												+ "JGu473g9DFj3y_gsh463j48hdsgl34lqzkvnr420gdsg-32hafUehcDaw3516Ha-aghaerUhhvF42123na38Agqmznv_46bd-67ogpwuNaEv6")
										.substring(1, 25), "false", "",
						"device" };
				// String[]
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

	public void onResultsSucceeded(String... result) {
		// TODO Auto-generated method stub
		
		//list.addAll(result);
		adapter.notifyDataSetChanged();
	}

}
