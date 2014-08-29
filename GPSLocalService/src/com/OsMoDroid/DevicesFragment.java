package com.OsMoDroid;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.ClipboardManager;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
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

public class DevicesFragment extends SherlockFragment  {

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		registerForContextMenu(lv1);
		//globalActivity=(GPSLocalServiceClient) getSherlockActivity();
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onResume() {
		Log.d(getClass().getSimpleName(),"devicesfragment Onresume");
		globalActivity.actionBar.setTitle(R.string.devices);
if(!deviceU.equals("")){
	openDeviceChat(deviceU);
	
}
		//		if(globalActivity.NeedIntent!=null){
//			if(globalActivity.NeedIntent.getAction().equals("devicechat")){
//			globalActivity.openTabByIntent(globalActivity.NeedIntent);
//			globalActivity.NeedIntent=null;
//			}
//		}
		super.onResume();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onContextItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {
		  final AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();

		  if (item.getItemId() == 1) {

				//final AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();
			 
			
			  globalActivity.mService.myIM.sendToServer("UNSUBSCRIBE:"+LocalService.deviceAdapter.getItem(acmi.position).tracker_id);
			  
				//globalActivity.mService.saveObject(LocalService.deviceList, OsMoDroid.DEVLIST);
			  
			  
                               

		    }



		  if (item.getItemId() == 2) {
			  	openDeviceChat(LocalService.deviceList.get((int) acmi.id).tracker_id);
			   return true;

	    }

		 
 if (item.getItemId() == 3) {
			  
			  ClipboardManager clipboard = (ClipboardManager) globalActivity.getSystemService(Context.CLIPBOARD_SERVICE);
              if (LocalService.deviceList.get((int) acmi.id).url != null)
              clipboard.setText(LocalService.deviceList.get((int) acmi.id).url);
			  
			  return true;

		  }
		  
		  if (item.getItemId() == 4) {
			  
			  Intent sendIntent = new Intent(Intent.ACTION_SEND);
              sendIntent.setType("text/plain");
              sendIntent.putExtra(android.content.Intent.EXTRA_TEXT, LocalService.deviceList.get((int) acmi.id).url != null);
              startActivity(Intent.createChooser(sendIntent, getSherlockActivity().getString(R.string.sharelink)));
			  
			  return true;

		  }
		  
 if (item.getItemId() == 5) {
			  
	 Intent browseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(LocalService.deviceList.get((int) acmi.id).url));
	 startActivity(browseIntent);
             			  
			  return true;

		  }
 if (item.getItemId() == 6) 
 {
	 if(LocalService.deviceList.get((int) acmi.id).lat!=0)
	 {
		Log.d(getClass().getSimpleName(), "move to map to device");
		OsMoDroid.editor.putInt("centerlat", (int) ((LocalService.deviceList.get((int) acmi.id).lat)* 1E6));
		OsMoDroid.editor.putInt("centerlon", (int) ((LocalService.deviceList.get((int) acmi.id).lon)* 1E6));
		OsMoDroid.editor.putInt("zoom", 16);
		OsMoDroid.editor.putBoolean("isfollow", false);
		OsMoDroid.editor.commit();
		globalActivity.drawClickListener.selectItem(OsMoDroid.context.getString(R.string.map), null);
		LocalService.currentItemName=OsMoDroid.context.getString(R.string.map);
	 }
	 else
	 {
		 Toast.makeText(globalActivity, R.string.unknown_location_now, Toast.LENGTH_SHORT).show();
	 }
 }
		  
 if (item.getItemId() == 7) 
 {
	 ColorDialog.OnClickListener cl =new  ColorDialog.OnClickListener() {
		
		@Override
		public void onClick(Object tag, int color) {
			LocalService.deviceList.get((int) acmi.id).color="#"+Integer.toHexString(color);
			//SUBSCRIBE_SET|{u:123, name:"devaha", data: {color: '#000000'}}
			globalActivity.mService.myIM.sendToServer("SUBSCRIBE_SET|{u:"+LocalService.deviceList.get((int) acmi.id).u+", data: {color: '"+"#"+Integer.toHexString(color)+"'}}");
			//LocalService.deviceList.get((int) acmi.id).devicePath.clear();
			//globalActivity.mService.saveObject(LocalService.deviceList, OsMoDroid.DEVLIST);
		}
	};
	 
	 ColorDialog dialog = new ColorDialog(globalActivity, false, getView(), Color.parseColor(LocalService.deviceList.get((int) acmi.id).color), cl,  R.drawable.wheel);
		dialog.show();
 }

	    
		return super.onContextItemSelected(item);
	}

	void openDeviceChat(String u) {
		 globalActivity.drawClickListener.devchat = new DeviceChatFragment();
		Bundle bundle = new Bundle();
		bundle.putString("deviceU", u);
		globalActivity.drawClickListener.devchat.setArguments(bundle);
		globalActivity.showFragment(globalActivity.drawClickListener.devchat,true);
	}

	@Override
	public void onDetach() {
		globalActivity=null;
		
		Log.d(getClass().getSimpleName(),"devicesfragment OnDetach");
		super.onDetach();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateContextMenu(android.view.ContextMenu, android.view.View, android.view.ContextMenu.ContextMenuInfo)
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
		if(LocalService.deviceAdapter.getItem(acmi.position).subscribed){
		 menu.add(0, 1, 8, R.string.delete).setIcon(android.R.drawable.ic_menu_delete);
		}
		 //   menu.add(0, 2, 2, R.string.messages).setIcon(android.R.drawable.ic_menu_delete);
		   
		//    menu.add(0, 3, 3, R.string.copylink).setIcon(android.R.drawable.ic_menu_edit);
		//    menu.add(0, 4, 4, R.string.sharelink).setIcon(android.R.drawable.ic_menu_edit);
		//    menu.add(0, 5, 5, R.string.openinbrowser).setIcon(android.R.drawable.ic_menu_edit);
		    menu.add(0, 6, 6, R.string.showonmap).setIcon(android.R.drawable.ic_menu_edit);
		    menu.add(0, 7, 7, R.string.color).setIcon(android.R.drawable.ic_menu_edit);
		super.onCreateContextMenu(menu, v, menuInfo);
	}


	private GPSLocalServiceClient globalActivity;
	private ListView lv1;
	 String deviceU="";
	//private boolean openedbynotification=false;
	
	
	public void needToOpenChat(String i)
	{
		deviceU=i;
	}
	
	 @Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setHasOptionsMenu(true);
         Bundle bundle = getArguments();
		   
		   Log.d(getClass().getSimpleName(),"devicesfragment OnCreate bundle="+bundle);
         //setRetainInstance(true);
         Log.d(getClass().getSimpleName(),"devicesfragment OnCreate");
     }
	
	@Override
	public void onAttach(Activity activity) {
		Log.d(getClass().getSimpleName(),"devicesfragment OnAttach");
		globalActivity = (GPSLocalServiceClient)activity;// TODO Auto-generated method stub
		super.onAttach(activity);
	}
	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragment#onCreateOptionsMenu(com.actionbarsherlock.view.Menu, com.actionbarsherlock.view.MenuInflater)
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		MenuItem bind = menu.add(0, 1, 1, R.string.binddevice);
		bind.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		bind.setIcon(android.R.drawable.ic_menu_add);
		MenuItem refresh = menu.add(0, 2, 2, R.string.refresh);
		refresh.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		refresh.setIcon(android.R.drawable.ic_menu_rotate);
		super.onCreateOptionsMenu(menu, inflater);
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragment#onPrepareOptionsMenu(com.actionbarsherlock.view.Menu)
	 */
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onPrepareOptionsMenu(menu);
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragment#onOptionsItemSelected(com.actionbarsherlock.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 1) {

			// final View textEntryView = factory.inflate(R.layout.dialog,
			// null);
			LinearLayout layout = new LinearLayout(getSherlockActivity());
			layout.setOrientation(LinearLayout.VERTICAL);
			final TextView txv5 = new TextView(getSherlockActivity());
			txv5.setText(R.string.name);
			layout.addView(txv5);
			final EditText inputhash = new EditText(getSherlockActivity());
			
			layout.addView(inputhash);

			final TextView txv3 = new TextView(getSherlockActivity());
			txv3.setText(R.string.trackerid);
			layout.addView(txv3);

			final EditText inputN = new EditText(getSherlockActivity());
			inputN.setInputType(InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
			layout.addView(inputN);
		//	final EditText input = new EditText(this);
			

			AlertDialog alertdialog3 = new AlertDialog.Builder(
					getSherlockActivity())
					.setTitle(R.string.bindapp)
					.setView(layout)
					.setPositiveButton(R.string.yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									 
									if (!( inputhash.getText().toString().equals(""))&&!( inputN.getText().toString().equals(""))) {
										Device dev = new Device();
										dev.name=inputhash.getText().toString();
										dev.tracker_id=inputN.getText().toString();
										//LocalService.deviceList.add(dev);
										//globalActivity.mService.myIM.sendToServer("LN:"+dev.tracker_id);
										globalActivity.mService.myIM.sendToServer("SUBSCRIBE:"+dev.tracker_id+"|"+Uri.encode(dev.name));
//										if(LocalService.deviceAdapter!=null)
//										{
//											LocalService.deviceAdapter.notifyDataSetChanged();
//										}
//										globalActivity.mService.saveObject(LocalService.deviceList, OsMoDroid.DEVLIST);
										
									} 
								}
							})
					.setNegativeButton(R.string.No,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									/* User clicked cancel so do some stuff */
								}
							}).create();

			alertdialog3.show();

			// 6564 2638 7281 2680

		}
		if (item.getItemId() == 2) {
			globalActivity.mService.myIM.sendToServer("DEVICE_GET_ALL");
		}
		
		return super.onOptionsItemSelected(item);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	
		
		View view=inflater.inflate(R.layout.mydevices, container, false);
		lv1 = (ListView) view.findViewById(R.id.mydeviceslistView);



		   //if (LocalService.deviceAdapter==null){    LocalService.deviceAdapter = new DeviceAdapter(getApplicationContext(),R.layout.deviceitem, LocalService.deviceList);}

		       lv1.setAdapter(LocalService.deviceAdapter);

		       registerForContextMenu(lv1);

		  lv1.setOnItemClickListener(new OnItemClickListener() {



			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,

					long arg3) {

							arg0.showContextMenuForChild(arg1);

							}

		});

		    

		
		return view;

	}
	
	@Override
	public void onPause() {
		deviceU="";
		Log.d(getClass().getSimpleName(),"onpause");
		//openedbynotification=true;
		super.onPause();
	}

	

	



}
