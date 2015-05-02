package com.example.renter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MessageAdapter extends ArrayAdapter<Message>{

	Context context = null;
	ArrayList<Message> messageList = null;
	public MessageAdapter(Context context, int resource, ArrayList<Message> objects) {
		super(context, resource, objects);
		this.context = context;
		messageList = objects;
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		setNotifyOnChange(true);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.chat_list_row,
					parent, false);
		}
		
		if(messageList != null && messageList.size() > 0){
		
			((TextView)convertView.findViewById(R.id.message)).setText(messageList.get(position).getMessage().toString());
			((TextView)convertView.findViewById(R.id.name)).setText(messageList.get(position).getName().toString());
		}
		return convertView;
	}
}
