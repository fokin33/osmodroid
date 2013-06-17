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
import android.widget.ToggleButton;

public class ChannelsAdapter extends ArrayAdapter<Channel> {

	private TextView channelName;
	private TextView channelCreated;
	ToggleButton tg;
	
	public ChannelsAdapter(Context context, int textViewResourceId, List<Channel> objects) {
		super(context, textViewResourceId, objects);
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		 if (row == null) {
  LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         row = inflater.inflate(R.layout.channelsitem, parent, false);
		
		        }
		        Channel channel = getItem(position);
		     
		        channelName = (TextView) row.findViewById(R.id.txtName);
		        channelCreated = (TextView) row.findViewById(R.id.txtCreated);
		        tg = (ToggleButton) row.findViewById(R.id.toggleButton1);
		        if (channel.name!=null){   channelName.setText(channel.name);}
		        if (channel.created!=null){channelCreated.setText(channel.created);}
		        if (channel.send!=null){tg.setChecked(true);}
		     
		        return row;

	}

}
