package com.OsMoDroid;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DeviceAdapter extends ArrayAdapter<Device> {

	private TextView deviceName;
	private TextView deviceWhere;
	private TextView deviceLast;
	public DeviceAdapter(Context context, int textViewResourceId, List<Device> objects) {
		super(context, textViewResourceId, objects);
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		 if (row == null) {

		            
		
		            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		            row = inflater.inflate(R.layout.deviceitem, parent, false);
		
		            
	
		        }
		
		 
		
		
		        Device device = getItem(position);
		
		      
		
		       // DeviceonlineIcon = (ImageView) row.findViewById(R.id.country_icon);
		
		     
		
		        deviceName = (TextView) row.findViewById(R.id.txtName);
		        deviceWhere = (TextView) row.findViewById(R.id.txtWhere);
		        deviceLast = (TextView) row.findViewById(R.id.txtLast);
		        deviceName.setText(device.name);
		        deviceWhere.setText(device.where);
		        deviceLast.setText(device.last);
		        Log.d(getClass().getSimpleName(),"device.name="+device.name.toString());
		        Log.d(getClass().getSimpleName(),"device.online="+device.online.toString());
		        Log.d(getClass().getSimpleName(),"device.state="+device.state.toString());
		        if (device.online.equals("1")){
		        	 
		        	deviceName.setTextColor(Color.GREEN);
		        }
		        else 
		        {
		        	deviceName.setTextColor(Color.WHITE);
		        }
		        if (device.state.equals("1")){
		        	deviceWhere.setTextColor(Color.GREEN);
		        }
		        else
		        {
		        	deviceWhere.setTextColor(Color.WHITE);
		        }
		       
		        				
		
		        return row;

	}

}
