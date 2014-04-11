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

public class DebugFragment extends SherlockFragment {
	private GPSLocalServiceClient globalActivity;
	
	
	final private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
	
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.simlinks, container, false);
		final ListView lv1 = (ListView) view.findViewById(R.id.listView1);
       lv1.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL); 
        LocalService.debugAdapter = new ArrayAdapter<String>(getSherlockActivity(),android.R.layout.simple_list_item_1, LocalService.debuglist);
        lv1.setAdapter(LocalService.debugAdapter);
       
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
         //setRetainInstance(true);
         super.onCreate(savedInstanceState);
     }
	
	@Override
	public void onResume() {
		globalActivity.actionBar.setTitle("debug");
		
		super.onResume();
	}
	@Override
	public void onDetach() {
		globalActivity=null;
		super.onDetach();
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
		MenuItem clear = menu.add(0, 1, 0, "Очитсить");
		super.onCreateOptionsMenu(menu, inflater);
	}
	
		
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case 1:
		LocalService.debuglist.clear();
		LocalService.debugAdapter.notifyDataSetChanged();
		break;
	
	default:
		break;
	}	
		return super.onOptionsItemSelected(item);
	}



}
