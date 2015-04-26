package com.example.renter;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TicketListFragment extends Fragment {
	private OnFragmentInteractionListener mListener;
	static TicketAdapter mTicketAdapter;
	ArrayList<Ticket> tickets = new ArrayList<Ticket>();
	ListView mticketListView;
	String ticketListId,ticketListTitle,ticketListDescription,ticketListStatus,ticketListPriority;
	Date ticketStartDate,ticketEndDate;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_ticket_list, container,
				false);
		mDisplayTicketInListViewSortedByUpdatedTime();
		try {
			tickets.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
	}

	public void mDisplayTicketInListViewSortedByUpdatedTime(){
		ParseQuery<ParseObject> queryRetriveTicket = 
				ParseQuery.getQuery(RenterConstantVariables.TICKETTABLE);
		queryRetriveTicket.whereExists(RenterConstantVariables.TICKETTABLE_STATUS);
		queryRetriveTicket.orderByDescending("updatedAt");
		queryRetriveTicket.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> ticketList, ParseException arg1) {
				for (int i=0; i<ticketList.size(); i++){
					Ticket ticket = new Ticket();
					ticketListId = ticketList.get(i).getObjectId();
					ticket.setmId(ticketListId);
					ticketListTitle = ticketList.get(i)
							.getString(RenterConstantVariables.TICKETTABLE_TITLE);
					ticket.setmTitle(ticketListTitle);
					ticketListDescription = ticketList.get(i)
							.getString(RenterConstantVariables.TICKETTABLE_DESCRIPTION);
					ticket.setmDescription(ticketListDescription);
					ticketListStatus = ticketList.get(i)
							.getString(RenterConstantVariables.TICKETTABLE_STATUS);
					ticket.setmStatus(ticketListStatus);
					ticketStartDate = (Date) ticketList.get(i)
							.getDate(RenterConstantVariables.TICKETTABLE_STARTDATE);
					ticket.setmStartDate(ticketStartDate);
					ticketEndDate = (Date) ticketList.get(i)
							.getDate(RenterConstantVariables.TICKETTABLE_ENDDATE);
					ticket.setmCloseDate(ticketEndDate);
					ticketListPriority = ticketList.get(i)
							.getString(RenterConstantVariables.TICKETTABLE_PRIORITY);
					ticket.setmPriority(ticketListPriority);
					tickets.add(ticket);
				}
				mticketListView = (ListView) getActivity().findViewById(R.id.listViewTicket);
				mTicketAdapter = new TicketAdapter(getActivity(), tickets);
				mticketListView.setAdapter(mTicketAdapter);
				mticketListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
					mListener.onClickingonTicket(tickets.get(position));
					}
				});
			}
		});
	}
	
	public void mDisplayTicketInListViewSortedByStatus(){
		ParseQuery<ParseObject> queryRetriveTicket = 
				ParseQuery.getQuery(RenterConstantVariables.TICKETTABLE);
		queryRetriveTicket.whereExists(RenterConstantVariables.TICKETTABLE_STATUS);
		queryRetriveTicket.orderByDescending(RenterConstantVariables.TICKETTABLE_STATUS);
		queryRetriveTicket.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> ticketList, ParseException arg1) {
				for (int i=0; i<ticketList.size(); i++){
					Ticket ticket = new Ticket();
					ticketListId = ticketList.get(i).getObjectId();
					ticket.setmId(ticketListId);
					ticketListTitle = ticketList.get(i)
							.getString(RenterConstantVariables.TICKETTABLE_TITLE);
					ticket.setmTitle(ticketListTitle);
					ticketListDescription = ticketList.get(i)
							.getString(RenterConstantVariables.TICKETTABLE_DESCRIPTION);
					ticket.setmDescription(ticketListDescription);
					ticketListStatus = ticketList.get(i)
							.getString(RenterConstantVariables.TICKETTABLE_STATUS);
					ticket.setmStatus(ticketListStatus);
					ticketStartDate = (Date) ticketList.get(i)
							.getDate(RenterConstantVariables.TICKETTABLE_STARTDATE);
					ticket.setmStartDate(ticketStartDate);
					ticketEndDate = (Date) ticketList.get(i)
							.getDate(RenterConstantVariables.TICKETTABLE_ENDDATE);
					ticket.setmCloseDate(ticketEndDate);
					ticketListPriority = ticketList.get(i)
							.getString(RenterConstantVariables.TICKETTABLE_PRIORITY);
					ticket.setmPriority(ticketListPriority);
					tickets.add(ticket);
				}
				mticketListView = (ListView) getActivity().findViewById(R.id.listViewTicket);
				mTicketAdapter = new TicketAdapter(getActivity(), tickets);
				mticketListView.setAdapter(mTicketAdapter);
				mticketListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
					mListener.onClickingonTicket(tickets.get(position));
					}
				});
			}
		});
	}


	public interface OnFragmentInteractionListener {
		public void onClickingonTicket(Ticket mTicket);
	}
}
