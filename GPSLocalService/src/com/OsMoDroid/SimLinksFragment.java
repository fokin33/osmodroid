package com.OsMoDroid;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class SimLinksFragment extends SherlockFragment implements ResultsListener{
	private GPSLocalServiceClient globalActivity;
	private ArrayAdapter<String> adapter;
	ArrayList<String> list;
	ArrayList<String> listids;
	final private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
	
	void addlink_new() throws JSONException
	{
		JSONObject postjson = new JSONObject();
		postjson.put("device", OsMoDroid.settings.getString("device", ""));
		postjson.put("random", "1");
		postjson.put("until", "-1");
		netutil.newapicommand((ResultsListener)SimLinksFragment.this,getSherlockActivity(), "om_link_add","json="+postjson.toString());
	}
	
	void reflinks()
	{
		netutil.newapicommand((ResultsListener)SimLinksFragment.this,getSherlockActivity(), "om_link");
	}	

	void dellink(String linkid)
	{
		netutil.newapicommand((ResultsListener)SimLinksFragment.this,getSherlockActivity(), "om_link_delete:"+linkid);
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.simlinks, container, false);
		final ListView lv1 = (ListView) view.findViewById(R.id.listView1);
        list = new ArrayList<String>();
        list.clear();
        listids = new ArrayList<String>();
        listids.clear();
        adapter = new ArrayAdapter<String>(getSherlockActivity(),android.R.layout.simple_list_item_1, list);
        lv1.setAdapter(adapter);
        registerForContextMenu(lv1);
        lv1.setOnItemClickListener(new OnItemClickListener() {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		{
						arg0.showContextMenuForChild(arg1);
		}

        });
        if(list.size()==0){reflinks();}
		return view;
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		globalActivity=(GPSLocalServiceClient) getSherlockActivity();
		super.onActivityCreated(savedInstanceState);
	}
	 @Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setHasOptionsMenu(true);
         setRetainInstance(true);
         super.onCreate(savedInstanceState);
     }
	
	@Override
	public void onResume() {
		globalActivity.devicesTab.setText(R.string.devices);
		
		super.onResume();
	}
	@Override
	public void onAttach(Activity activity) {
		globalActivity = (GPSLocalServiceClient)activity;// TODO Auto-generated method stub
		super.onAttach(activity);
	}
	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragment#onCreateOptionsMenu(com.actionbarsherlock.view.Menu, com.actionbarsherlock.view.MenuInflater)
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		MenuItem bind = menu.add(0, 1, 0, R.string.addsymlink);
		bind.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		bind.setIcon(android.R.drawable.ic_menu_add);
		MenuItem refresh = menu.add(0, 2, 0, R.string.refresh);
		refresh.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		refresh.setIcon(android.R.drawable.ic_menu_rotate);
		super.onCreateOptionsMenu(menu, inflater);
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		 menu.add(0, 1, 0, R.string.sharelink).setIcon(android.R.drawable.ic_menu_share);
		 menu.add(0, 2, 0, R.string.deletelink).setIcon(android.R.drawable.ic_menu_delete);
		 menu.add(0, 3, 0, R.string.copylink).setIcon(android.R.drawable.ic_menu_edit);
		 menu.add(0, 5, 5, R.string.openinbrowser).setIcon(android.R.drawable.ic_menu_edit);
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	public boolean onContextItemSelected(android.view.MenuItem item) {
		AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();  
		if (item.getItemId() == 1) 
		{
			Intent sendIntent = new Intent(Intent.ACTION_SEND);
			sendIntent.setType("text/plain");
			sendIntent.putExtra(android.content.Intent.EXTRA_TEXT, adapter.getItem(acmi.position));
			startActivity(Intent.createChooser(sendIntent, "Email"));
			return true;
		}
		if (item.getItemId() == 2) 
		{
			dellink(listids.get((int) acmi.id));
			return true;
	    }
		if (item.getItemId() == 3) 
		{
			ClipboardManager clipboard = (ClipboardManager) getSherlockActivity().getSystemService(Context.CLIPBOARD_SERVICE);
			clipboard.setText(adapter.getItem(acmi.position));
			return true;
		}
		if (item.getItemId() == 5) 
		{
			Intent browseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(adapter.getItem(acmi.position)));
			startActivity(browseIntent);
			return true;
		}

		
	    
		return super.onContextItemSelected(item);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case 1:
		try {
			addlink_new();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		break;
	case 2:
		reflinks();
		break;
	default:
		break;
	}	
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResultsSucceeded(APIComResult result) {
		JSONObject a = null; 
		Log.d(getClass().getSimpleName(),"OnResultListener:"+result);	
		if (result.Jo==null&&result.ja==null)

		{
			Toast.makeText(LocalService.serContext, R.string.esya_ru_notrespond , Toast.LENGTH_LONG).show();

			}
		
		
		if (result.Command.equals("APIM")&& !(result.Jo==null))

		{

			Log.d(getClass().getSimpleName(),"APIM Response:"+result.Jo);

			
			if (result.Jo.has("om_link")){

				list.clear();

				listids.clear();

				
				try {
					JSONArray ar = new JSONArray(result.Jo.getString("om_link"));
					for (int i = 0; i < ar.length(); i++) {
			 			JSONObject jsonObject = ar.getJSONObject(i);
				if (jsonObject.getString("device").equals(LocalService.settings.getString("device", ""))){
					list.add("http://m.esya.ru/"+jsonObject.getString("url"));
					listids.add(jsonObject.getString("u"));
				}
				} 
				}
				catch (JSONException e) {
					e.printStackTrace();
				}
				adapter.notifyDataSetChanged();
			}
			else {
				reflinks();
			}
			
			
		}

		
	}

}
