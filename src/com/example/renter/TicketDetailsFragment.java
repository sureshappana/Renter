package com.example.renter;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class TicketDetailsFragment extends Fragment {

	private static final String KEY = "key";
	Ticket mTicket;
	SimpleDateFormat mDateFormat;
	
	public static TicketDetailsFragment instanceOf(Ticket mTicket){
		TicketDetailsFragment f = new TicketDetailsFragment();
		Bundle b = new Bundle();
		b.putSerializable(KEY, (Serializable) mTicket);
		f.setArguments(b);
		return f;		
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_ticket_details_fragment, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.buttonEditTicket){
			Intent i = new Intent(getActivity(),
					TicketAddOrUpdateActivity.class);
			i.putExtra(RenterConstantVariables.EDIT_TICKET, mTicket);
			startActivity(i);
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View view =  inflater.inflate(R.layout.fragment_ticket_details, container,
				false);
		mTicket = (Ticket) getArguments().get(KEY);
		mDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		((TextView)view.findViewById(R.id.textViewTicketDetailsTicketTitle))
					.setText(mTicket.getmTitle());
		((TextView)view.findViewById(R.id.textViewTicketDetailsApartmentNo))
					.setText("Apartment No: "+mTicket.getmApartmentNo());
		((TextView)view.findViewById(R.id.textViewTicketDetailsTicketDescription))
					.setText(mTicket.getmDescription());
		((TextView)view.findViewById(R.id.textViewTicketDetailsTicketPriority))
					.setText("Priority: "+mTicket.getmPriority());
		((TextView)view.findViewById(R.id.textViewTicketDetailsTicketStatus))
					.setText("Status: "+mTicket.getmStatus());
		((TextView)view.findViewById(R.id.textViewTicketDetailsTicketStartDate))
					.setText("Ticket raised on: "+mDateFormat.format(mTicket.getmStartDate()));
		if(mTicket.getmCloseDate()==null){
			((TextView)view.findViewById(R.id.textViewTicketDetailsTicketEndDate))
						.setText("Ticket closed on: ");
		}else{
			((TextView)view.findViewById(R.id.textViewTicketDetailsTicketEndDate))
			.setText("Ticket closed on: "+mDateFormat.format(mTicket.getmCloseDate()));
		}
		
		return view;
	}


	
	
	@Override
	public void onResume() {
		ParseQuery<ParseObject> querySaveEditedTicket = 
				ParseQuery.getQuery(RenterConstantVariables.TICKETTABLE);
		querySaveEditedTicket.getInBackground(mTicket.getmId(),new GetCallback<ParseObject>() {
			
			@Override
			public void done(ParseObject mTicketToBeUpdated, ParseException e) {
				if(e==null){
					((TextView)getActivity().findViewById(R.id.textViewTicketDetailsTicketTitle))
					.setText(mTicketToBeUpdated.getString(RenterConstantVariables.TICKETTABLE_TITLE));
					((TextView)getActivity().findViewById(R.id.textViewTicketDetailsTicketDescription))
					.setText(mTicketToBeUpdated.getString(RenterConstantVariables.TICKETTABLE_DESCRIPTION));
					((TextView)getActivity().findViewById(R.id.textViewTicketDetailsApartmentNo))
					.setText("Apartment No: "+CommonFunctions.trimString
							(mTicketToBeUpdated.getString(RenterConstantVariables.TICKETTABLE_APARTMENT_NO)));
					((TextView)getActivity().findViewById(R.id.textViewTicketDetailsTicketPriority))
					.setText("Priority: "+mTicketToBeUpdated.getString(RenterConstantVariables.TICKETTABLE_PRIORITY));
					((TextView)getActivity().findViewById(R.id.textViewTicketDetailsTicketStatus))
					.setText("Status: "+mTicketToBeUpdated.getString(RenterConstantVariables.TICKETTABLE_STATUS));
					try {
						((TextView)getActivity().findViewById(R.id.textViewTicketDetailsTicketEndDate))
						.setText("Ticket closed on: "+mDateFormat.format(mTicketToBeUpdated
								.getDate(RenterConstantVariables.TICKETTABLE_ENDDATE)));
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				else{
					
				}
				
			}
		});
		super.onResume();
	}

}
