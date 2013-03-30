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
import android.net.Uri;import android.os.Bundle;
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


public class SimLinks extends Activity implements ResultsListener{
	private ArrayAdapter<String> adapter;
	// String[] simlinksarray;
	 ArrayList<String> list;
	 ArrayList<String> listids;
	 SharedPreferences settings;
	 final private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");

	public SimLinks() {
	
	}
	
	void reflinks(){			netutil.newapicommand((ResultsListener)SimLinks.this, "om_link");	}
	
	void addlink_new() throws JSONException{
		JSONObject postjson = new JSONObject();
		postjson.put("device", settings.getString("device", ""));
		postjson.put("random", "1");		
		postjson.put("until", "-1");
		netutil.newapicommand((ResultsListener)SimLinks.this, "om_link_add","json="+postjson.toString());
	}
	

	void dellink(String linkid)	{		netutil.newapicommand((ResultsListener)SimLinks.this, "om_link_delete:"+linkid);	}

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
			try {
					addlink_new();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}});
	   
	}
	
	
	 @Override
	  public void onCreateContextMenu(ContextMenu menu, View v,
	      ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    menu.add(0, 1, 0, "Поделиться ссылкой").setIcon(android.R.drawable.ic_menu_share);
	    menu.add(0, 2, 0, "Удалить ссылку").setIcon(android.R.drawable.ic_menu_delete);
	    menu.add(0, 3, 0, "Копировать ссылку").setIcon(android.R.drawable.ic_menu_edit);	    	    menu.add(0, 5, 5, "Открыть в браузере").setIcon(android.R.drawable.ic_menu_edit);
	    
	    
	  }

	  @Override
	  public boolean onContextItemSelected(MenuItem item) {
	  
		  if (item.getItemId() == 1) {
		         AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();
			  Intent sendIntent = new Intent(Intent.ACTION_SEND);
				sendIntent.setType("text/plain");
				sendIntent.putExtra(android.content.Intent.EXTRA_TEXT, adapter.getItem(acmi.position));
				startActivity(Intent.createChooser(sendIntent, "Email"));
		         
		      return true;
		    }
			  		  
		  if (item.getItemId() == 2) {
	         AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();
	     dellink(listids.get((int) acmi.id));
			 
	      return true;
	    }
		  if (item.getItemId() == 3) {
		         AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();
		         ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
					
						clipboard.setText(adapter.getItem(acmi.position));
				 
		      return true;
		    }		  if (item.getItemId() == 5) {			  AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();				 Intent browseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(LocalService.deviceList.get((int) acmi.id).url));				 startActivity(browseIntent);			             			  						  return true;					  }
		  
	    return super.onContextItemSelected(item);
	  }
	
	

	public void onResultsSucceeded(APIComResult result) {
		JSONObject a = null; 
		Log.d(getClass().getSimpleName(),"OnResultListener:"+result);	
		if (result.Jo==null&&result.ja==null)		{			Toast.makeText(LocalService.serContext, "Esya.ru не отвечает. Проверьте работу интернета." , Toast.LENGTH_LONG).show();			}						if (result.Command.equals("APIM")&& !(result.Jo==null))
		{
			Log.d(getClass().getSimpleName(),"APIM Response:"+result.Jo);
						if (result.Jo.has("om_link")){				list.clear();				listids.clear();								try {					JSONArray ar = new JSONArray(result.Jo.getString("om_link"));					for (int i = 0; i < ar.length(); i++) {			 			JSONObject jsonObject = ar.getJSONObject(i);				if (jsonObject.getString("device").equals(LocalService.settings.getString("device", ""))){					list.add("http://m.esya.ru/"+jsonObject.getString("url"));					listids.add(jsonObject.getString("u"));				}				} 				}				catch (JSONException e) {					e.printStackTrace();				}				adapter.notifyDataSetChanged();			}			else {				reflinks();			}						
		}
		
		
	}
		
		
	}
	


