package com.example.renter;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TicketListFragment extends Fragment {
	private OnFragmentInteractionListener mListener;
	static TicketAdapter mTicketAdapter;
	ArrayList<Ticket> tickets = new ArrayList<Ticket>();
	ListView mticketListView;
	String ticketListId,ticketListTitle,ticketListDescription,ticketListStatus,ticketListPriority,ticketListApartmentNo;
	Date ticketStartDate,ticketEndDate;

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		if(!CommonFunctions.UserTableClass.mCurrentUserIsAdmin){
		inflater.inflate(R.menu.menu_ticket_list_fragment_tenant, menu);
		}else{
			inflater.inflate(R.menu.menu_ticket_list_fragment_admin, menu);
		}
		super.onCreateOptionsMenu(menu, inflater);
	}

	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.buttonAddTicket) {
			Intent i = new Intent(getActivity(),
					TenantAddTicketActivity.class);
			i.putExtra(RenterConstantVariables.ADD_TICKET, RenterConstantVariables.ADD_TICKET);
			startActivity(i);
		}
		else if (id == R.id.sortByDate) {
			try {
				tickets.clear();
			} catch (Exception e) {
				e.printStackTrace();
			}
			mDisplayTicketInListViewSortedByUpdatedTime();
			try {
				tickets.clear();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (id == R.id.sortByStatus) {
			try {
				tickets.clear();
			} catch (Exception e) {
				e.printStackTrace();
			}
			mDisplayTicketInListViewSortedByStatus();
			try {
				tickets.clear();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		final View view = inflater.inflate(R.layout.fragment_ticket_list, container,
				false);
		try {
			tickets.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		if(!CommonFunctions.UserTableClass.mCurrentUserIsAdmin){
		queryRetriveTicket.whereEqualTo(RenterConstantVariables.TICKETTABLE_TENANT_ID, 
				CommonFunctions.UserTableClass.mCurrentUser);
		}
		else{
		queryRetriveTicket.whereExists(RenterConstantVariables.TICKETTABLE_STATUS);
		}
		queryRetriveTicket.orderByDescending("updatedAt");
		queryRetriveTicket.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> ticketList, ParseException arg1) {
				for (int i=0; i<ticketList.size(); i++){
					Ticket ticket = new Ticket();
					ticketListId = ticketList.get(i).getObjectId();
					ticket.setmId(ticketListId);
					ticketListApartmentNo = ticketList.get(i)
							.getString(RenterConstantVariables.TICKETTABLE_APARTMENT_NO);
					ticket.setmApartmentNo(ticketListApartmentNo);
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
		if(!CommonFunctions.UserTableClass.mCurrentUserIsAdmin){
			queryRetriveTicket.whereEqualTo(RenterConstantVariables.TICKETTABLE_TENANT_ID, 
					CommonFunctions.UserTableClass.mCurrentUser);
			}
			else{
			queryRetriveTicket.whereExists(RenterConstantVariables.TICKETTABLE_STATUS);
			}
		queryRetriveTicket.orderByDescending(RenterConstantVariables.TICKETTABLE_STATUS);
		queryRetriveTicket.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> ticketList, ParseException arg1) {
				for (int i=0; i<ticketList.size(); i++){
					Ticket ticket = new Ticket();
					ticketListId = ticketList.get(i).getObjectId();
					ticket.setmId(ticketListId);
					ticketListApartmentNo = ticketList.get(i)
							.getString(RenterConstantVariables.TICKETTABLE_APARTMENT_NO);
					ticket.setmApartmentNo(ticketListApartmentNo);
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
