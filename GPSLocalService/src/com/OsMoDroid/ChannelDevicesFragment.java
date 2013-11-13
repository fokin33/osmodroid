package com.OsMoDroid;

import java.text.SimpleDateFormat;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class ChannelDevicesFragment extends SherlockFragment implements ResultsListener {
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		globalActivity = (GPSLocalServiceClient) getSherlockActivity();
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {
		 final AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();
		  if (item.getItemId() == 1) 
		  {
			String latitude =Float.toString(LocalService.channelsDevicesAdapter.getItem(acmi.position).lat);
			String longitude=Float.toString(LocalService.channelsDevicesAdapter.getItem(acmi.position).lon);;
			Intent intent= new Intent(android.content.Intent.ACTION_VIEW,Uri.parse("geo:"+latitude+","+longitude+"?z=10"));
			try {
				startActivity(intent);
			} catch (Exception e) {
				Toast.makeText(getSherlockActivity(), R.string.nomapapp, Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}  
			return super.onContextItemSelected(item);
		  }
		return super.onContextItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		   menu.add(0, 1, 0, R.string.showonmap).setIcon(android.R.drawable.ic_menu_mylocation);;
		super.onCreateContextMenu(menu, v, menuInfo);
	}


	ListView lv1;
	 ListView lv2;
	 Button sendButton;
	 EditText input;
	final private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
	
	
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	
	private GPSLocalServiceClient globalActivity;
	private int channelpos;
	 @Override
	public void onDestroy() {
			 LocalService.currentchanneldeviceList.clear();
			   LocalService.currentChannel=null;
			 
			 
			super.onDestroy();
		}
	
	 @Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setHasOptionsMenu(true);
         setRetainInstance(true);
         super.onCreate(savedInstanceState);
     }
	
	@Override
	public void onAttach(Activity activity) {
		globalActivity = (GPSLocalServiceClient)activity;// TODO Auto-generated method stub
		super.onAttach(activity);
	}
	@Override
	public void onDetach() {
		LocalService.chatmessagelist.clear();
		   
		 LocalService.currentDevice=null;
		super.onDetach();
	}
	@Override
	public void onResume() {
		globalActivity.channelsTab.setText(R.string.chanal+LocalService.currentChannel.name);
		super.onResume();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		 MenuItem refresh = menu.add(0, 3, 0, R.string.refresh);
		 refresh.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		 refresh.setIcon(android.R.drawable.ic_menu_rotate); 
			
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId()==3){
			netutil.newapicommand((ResultsListener)LocalService.serContext, "om_device_channel_adaptive:"+OsMoDroid.settings.getString("device", ""));
			netutil.newapicommand((ResultsListener)ChannelDevicesFragment.this,getSherlockActivity(), "om_channel_chat_get:"+LocalService.currentChannel.u);

		}
		return super.onOptionsItemSelected(item);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		   if(bundle != null){
		      channelpos = bundle.getInt("channelpos", -1);
		   
		   }
		    
		
		View view=inflater.inflate(R.layout.mychannelsdevices, container, false);  
		 
		 LocalService.currentchanneldeviceList= LocalService.channelList.get(channelpos).deviceList;
		   LocalService.currentChannel= LocalService.channelList.get(channelpos); 
		   
		    LocalService.channelsDevicesAdapter = new ChannelsDevicesAdapter(getSherlockActivity(),R.layout.channelsdeviceitem,  LocalService.currentchanneldeviceList);
	LocalService.channelsmessagesAdapter = new ArrayAdapter<String>(getSherlockActivity(), android.R.layout.simple_list_item_1, LocalService.currentChannel.messagesstringList );

		    lv1 = (ListView) view.findViewById(R.id.mychannelsdeviceslistView);
		    lv2 = (ListView) view.findViewById(R.id.mychannelsmessages);
		    input =(EditText) view.findViewById(R.id.mychannelsdeviceseditText1);
		    input.requestFocus();
		    
	sendButton= (Button) view.findViewById(R.id.mychanneldevicesendButton);
	sendButton.setOnClickListener(new OnClickListener() {

		public void onClick(View v) {

			if (!(input.getText().toString().equals(""))) {

				JSONObject postjson = new JSONObject();



				try {

				postjson.put("text", input.getText().toString());
				postjson.put("channel", LocalService.currentChannel.u);
				postjson.put("device", OsMoDroid.settings.getString("device", ""));
				//http://apim.esya.ru/?key=H8&query=om_channel_chat_post&format=jsonp
				//json={"channel":"51","device":"40","text":"789"}
				netutil.newapicommand((ResultsListener)ChannelDevicesFragment.this,(Context)getSherlockActivity(), "om_channel_chat_post","json="+postjson.toString());
				input.setText("");
				} catch (JSONException e) {

					// TODO Auto-generated catch block

					e.printStackTrace();

				}

			}


		}});

		       lv1.setAdapter(LocalService.channelsDevicesAdapter);
		       lv2.setAdapter(LocalService.channelsmessagesAdapter);
		       if (LocalService.channelsDevicesAdapter!=null) {LocalService.channelsDevicesAdapter.notifyDataSetChanged();}
		       if (LocalService.channelsmessagesAdapter!=null) {LocalService.channelsmessagesAdapter.notifyDataSetChanged();}

		       registerForContextMenu(lv1);

		  lv1.setOnItemClickListener(new OnItemClickListener() {



			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,

					long arg3) {

							arg0.showContextMenuForChild(arg1);

							}

		});



		  
		netutil.newapicommand((ResultsListener)LocalService.serContext, "om_device_channel_adaptive:"+OsMoDroid.settings.getString("device", ""));
		netutil.newapicommand((ResultsListener)ChannelDevicesFragment.this,getSherlockActivity(), "om_channel_chat_get:"+LocalService.currentChannel.u);

		
		return view;

	}
	
	


	@Override
	public void onResultsSucceeded(APIComResult result) {


		Log.d(getClass().getSimpleName(),"OnResultListener Command:"+result.Command+",Jo="+result.Jo);
		if (!(result.Jo==null)  ) {

			Toast.makeText(getSherlockActivity(),result.Jo.optString("state")+" "+ result.Jo.optString("error_description"),5).show();
			
		}
		
		//"om_channel_chat_get:"+LocalService.currentChannel.u
		if (result.Jo.has("om_channel_chat_get:"+LocalService.currentChannel.u)){
			
			LocalService.currentChannel.messagesstringList.clear();

			try {
				  JSONArray a = result.Jo.getJSONArray("om_channel_chat_get:"+LocalService.currentChannel.u);
		 		  Log.d(getClass().getSimpleName(), a.toString());
		 		 for (int i = 0; i < a.length(); i++) {
		 			JSONObject jsonObject = a.getJSONObject(i);
		
		 			
		 			LocalService.currentChannel.messagesstringList.add(0, jsonObject.optString("text"));
		
				 if (LocalService.channelsmessagesAdapter!=null) {LocalService.channelsmessagesAdapter.notifyDataSetChanged();}
			}
		
		 		 
				} catch (Exception e) {

					 Log.d(getClass().getSimpleName(), "om_device_channel_adaptive эксепшн"+e.getMessage());
					e.printStackTrace();
				}


		
		}
		
		
		
		
	}

}
