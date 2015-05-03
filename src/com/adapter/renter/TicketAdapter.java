package com.adapter.renter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.classes.renter.Ticket;
import com.common.renter.CommonFunctions;
import com.common.renter.CommonFunctions.UserTableClass;
import com.example.renter.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TicketAdapter extends ArrayAdapter<Ticket>{
	Context context;
	ArrayList<Ticket> tickets;
	public TicketAdapter(Context context, ArrayList<Ticket> tickets) {
		super(context, R.layout.ticket_list_row,tickets);
		this.context = context;
		this.tickets = tickets;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		setNotifyOnChange(true);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.ticket_list_row,
					parent, false);
		}
		SimpleDateFormat mDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		TextView mTicketTitle = (TextView) convertView.findViewById(R.id.textViewTitleTicketAdapter);
		TextView mTicketDescription = (TextView) convertView.findViewById(R.id.textViewDescriptionTicketAdapter);
		TextView mTicketStartDate = (TextView) convertView.findViewById(R.id.textViewStartDateTicketAdapter);
		TextView mTicketStatus = (TextView) convertView.findViewById(R.id.textViewStatusTicketAdapter);
		TextView mTicketApartmentNo = (TextView) convertView.findViewById(R.id.textViewApartmentNoTicketAdapter);
		
		if(tickets!=null){
			
			mTicketTitle.setText(tickets.get(position).getmTitle());
			mTicketDescription.setText(tickets.get(position).getmDescription());
			mTicketStartDate.setText(mDateFormat.format(tickets.get(position).getmStartDate()));
			mTicketStatus.setText(tickets.get(position).getmStatus());
			if(CommonFunctions.UserTableClass.mCurrentUserIsAdmin){
				mTicketApartmentNo.setText(tickets.get(position).getmApartmentNo());
			}
			return convertView;
		}
		return null;
	}
}
