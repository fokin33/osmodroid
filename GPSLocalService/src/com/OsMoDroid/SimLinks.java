package com.OsMoDroid;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


public class SimLinks extends Activity {
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
				
				//simlinksarray= new String[] {"df"};
				list.add("a");
				adapter.notifyDataSetChanged();

			}});
	   
	    
	    // TODO Auto-generated method stub
	}

}
