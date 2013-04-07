package com.OsMoDroid;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChannelsDevicesAdapter extends ArrayAdapter<Device> {

	private TextView channelDeviceName;
	private TextView channelDeviceWhere;
	private TextView channelDeviceSpeed;
	private TextView channelDeviceDistance;
	private Location channelDeviceLocation =new Location("");
	public ChannelsDevicesAdapter(Context context, int textViewResourceId, List<Device> objects) {
		super(context, textViewResourceId, objects);
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		 if (row == null) {
  LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         row = inflater.inflate(R.layout.channelsdeviceitem, parent, false);
		
		        }
		        Device device = getItem(position);
		        Log.d(getClass().getSimpleName(), "channeldevice:"+device.name+" "+device.lat+" "+device.lon);
		        channelDeviceName = (TextView) row.findViewById(R.id.txtName);
		        channelDeviceWhere = (TextView) row.findViewById(R.id.txtWhere);
		        channelDeviceSpeed = (TextView) row.findViewById(R.id.txtSpeed);
		        channelDeviceDistance = (TextView) row.findViewById(R.id.TextDistance);
		        if (device.name!=null){   channelDeviceName.setText(device.name);}
		        if (device.speed!=null){   channelDeviceSpeed.setText(device.speed);}
		        //if (device.lat!=null&device.lon!=null){
		        	channelDeviceWhere.setText("Координаты:"+device.lat+" "+device.lon);
		        	//}
		        if (LocalService.currentLocation!=null){
		        	channelDeviceLocation.setLatitude((device.lat));
		        	channelDeviceLocation.setLongitude((device.lon));
		        	channelDeviceDistance.setText("Дистанция:"+Integer.toString((int)LocalService.currentLocation.distanceTo(channelDeviceLocation)/1000)+" Км "+Integer.toString((int) (1000*(LocalService.currentLocation.distanceTo(channelDeviceLocation)/1000 -(int)LocalService.currentLocation.distanceTo(channelDeviceLocation)/1000)) )+" м");	
		        	
		        }
		        return row;

	}

}
