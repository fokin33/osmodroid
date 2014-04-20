package com.OsMoDroid;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.ClipboardManager;
import android.text.Layout;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
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

import com.OsMoDroid.Netutil.MyAsyncTask;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class ChannelDevicesFragment extends SherlockFragment implements ResultsListener {
	
	ArrayList<MyAsyncTask> t= new ArrayList<Netutil.MyAsyncTask>();
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
	public void onDestroyView() {
		for (MyAsyncTask task: t){
			task.cancel(true);
		}
		super.onDestroyView();
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
		 Log.d(getClass().getSimpleName(), "ChannelDevicesFragment onDestroy");
		 LocalService.currentchanneldeviceList.clear();
		   LocalService.currentChannel=null;
			 
			 
			super.onDestroy();
		}
	
	 @Override
     public void onCreate(Bundle savedInstanceState) {
		 Log.d(getClass().getSimpleName(), "ChannelDevicesFragment onCreate");
		 super.onCreate(savedInstanceState);
         setHasOptionsMenu(true);
         //setRetainInstance(true);
         super.onCreate(savedInstanceState);
     }
	
	@Override
	public void onAttach(Activity activity) {
		Log.d(getClass().getSimpleName(), "ChannelDevicesFragment onAttach");
		globalActivity = (GPSLocalServiceClient)activity;// TODO Auto-generated method stub
		super.onAttach(activity);
	}
	@Override
	public void onDetach() {
		Log.d(getClass().getSimpleName(), "ChannelDevicesFragment onDetach");
		LocalService.chatmessagelist.clear();
		LocalService.currentDevice=null;
		globalActivity=null;
		super.onDetach();
	}
	@Override
	public void onResume() {
		Log.d(getClass().getSimpleName(), "ChannelDevicesFragment onResume");
		LocalService.chatVisible=true;
		
		globalActivity.actionBar.setTitle(getString(R.string.chanal)+LocalService.currentChannel.name);
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
	public void onPause() {
		Log.d(getClass().getSimpleName(), "ChannelDevicesFragment onPause");
		LocalService.chatVisible=false;
		super.onPause();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId()==3){
			t.add(Netutil.newapicommand((ResultsListener)LocalService.serContext, "om_device_channel_adaptive:"+OsMoDroid.settings.getString("device", "")));
			t.add(Netutil.newapicommand((ResultsListener)ChannelDevicesFragment.this,getSherlockActivity(), "om_channel_chat_get:"+LocalService.currentChannel.u));

		}
		return super.onOptionsItemSelected(item);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		 Log.d(getClass().getSimpleName(), "ChannelDevicesFragment onCreateView");
		Bundle bundle = getArguments();
		   if(bundle != null){
		      channelpos = bundle.getInt("channelpos", -1);
		   
		   }
		for (Channel ch : LocalService.channelList){
			if(ch.u==channelpos){
				LocalService.currentchanneldeviceList=ch.deviceList;
				LocalService.currentChannel=ch;
			}
		}
		
		View view=inflater.inflate(R.layout.mychannelsdevices, container, false);  
		 
		 //LocalService.currentchanneldeviceList= LocalService.channelList.get(channelpos).deviceList;
		   //LocalService.currentChannel= LocalService.channelList.get(channelpos); 
		   
		    LocalService.channelsDevicesAdapter = new ChannelsDevicesAdapter(getSherlockActivity(),R.layout.channelsdeviceitem,  LocalService.currentchanneldeviceList);
	LocalService.channelsmessagesAdapter = new ChannelChatAdapter(globalActivity,  R.layout.devicechatitem, LocalService.currentChannel.messagesstringList);
			//(getSherlockActivity(), R.layout.channelchatitem, LocalService.currentChannel.messagesstringList );
			
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
				t.add(Netutil.newapicommand((ResultsListener)ChannelDevicesFragment.this,(Context)getSherlockActivity(), "om_channel_chat_post","json="+postjson.toString()));
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
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
				{
					arg0.showContextMenuForChild(arg1);
				}
		});
		  lv2.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
				{
					ChannelChatMessage m =(ChannelChatMessage)arg0.getItemAtPosition(arg2);
					input.setText(m.from+", "+input.getText());
					input.setSelection(input.length());
					OsMoDroid.inputMethodManager.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
				}
		});
		t.add(Netutil.newapicommand((ResultsListener)LocalService.serContext, "om_device_channel_adaptive:"+OsMoDroid.settings.getString("device", "")));
		t.add(Netutil.newapicommand((ResultsListener)ChannelDevicesFragment.this,getSherlockActivity(), "om_channel_chat_get:"+LocalService.currentChannel.u));
		return view;

	}
	
	
	// only will trigger it if no physical keyboard is open
	
	 


	@Override
	public void onResultsSucceeded(APIComResult result) {


		Log.d(getClass().getSimpleName(),"OnResultListener Command:"+result.Command+",Jo="+result.Jo);
		if (!(result.Jo==null)  ) {

			try {
				Toast.makeText(getSherlockActivity(),result.Jo.optString("state")+" "+ result.Jo.optString("error_description"),5).show();
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
		
		//"om_channel_chat_get:"+LocalService.currentChannel.u
		if (result.Jo.has("om_channel_chat_get:"+LocalService.currentChannel.u)){
//		    		u = 468
//		    	    device = 5837
//		    	    text = не ник аккаунта
//		    	    time = 2014-04-18 18:38:22
			
			LocalService.currentChannel.messagesstringList.clear();
			
			try {
				  JSONArray a = result.Jo.getJSONArray("om_channel_chat_get:"+LocalService.currentChannel.u);
		 		  Log.d(getClass().getSimpleName(), a.toString());
		 		 for (int i = 0; i < a.length(); i++) {
		 			JSONObject jsonObject = a.getJSONObject(i);
		 			ChannelChatMessage m =new ChannelChatMessage();
		 			for(Device dev:LocalService.currentChannel.deviceList){
		 				if(dev.u==jsonObject.optInt("device")){
		 					m.from=dev.name;
		 					m.device=dev.u;
		 					m.color=Color.parseColor("#40"+dev.color);
		 				}
		 			}
		 			if (jsonObject.optInt("device")==0)
		 				{
		 				m.from=getSherlockActivity().getString(R.string.observers);
		 				m.color=Color.parseColor("#40006400");
		 				}
		 				
		 			if (jsonObject.optInt("device")==Integer.parseInt(OsMoDroid.settings.getString("device", "0"))){
		 				m.from=getSherlockActivity().getString(R.string.iam);
		 				m.color=Color.parseColor("#40AAAAAA");
		 			}
		 			m.text=Netutil.unescape(jsonObject.optString("text"));
					m.time=jsonObject.optString("time");
		 			LocalService.currentChannel.messagesstringList.add(m);
		 			//Collections.sort(LocalService.currentChannel.messagesstringList);
				 if (LocalService.channelsmessagesAdapter!=null) {LocalService.channelsmessagesAdapter.notifyDataSetChanged();}
			}
		
		 		 
				} catch (Exception e) {

					 Log.d(getClass().getSimpleName(), "om_device_channel_adaptive эксепшн"+e.getMessage());
					e.printStackTrace();
				}


		}
		}
		
		
		
		
	}

}
