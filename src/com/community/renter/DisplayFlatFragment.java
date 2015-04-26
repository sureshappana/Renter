package com.community.renter;

import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.renter.CommonFunctions;
import com.example.renter.R;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class DisplayFlatFragment extends Fragment {
	OnFragmentInteractionListener mListener;
	String flatNumber;

	public DisplayFlatFragment(String flatNumber) {
		this.flatNumber = flatNumber;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.flats_delete_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_editFlat:
			mListener.gotoFlatInfoFragment();
			return true;
		case R.id.action_deleteFlat:
			CommonFunctions.startProgressDialog(getActivity(),"Deleting flat info.Please wait..." );
			ParseQuery<ParseObject> query = ParseQuery
					.getQuery(CommonFunctions.FLATINFO_OBJECT);
			query.whereEqualTo("flatNumber", flatNumber);
			query.whereEqualTo("isMainTenant", true);
			query.whereEqualTo("communityObject", ParseUser.getCurrentUser()
					.getObjectId());
			query.findInBackground(new FindCallback<ParseObject>() {
				public void done(List<ParseObject> appList, ParseException e) {
					CommonFunctions.stopProgressDialog();	
					if (e == null) {
						for (ParseObject obj : appList) {

							obj.deleteInBackground(new DeleteCallback() {
								@Override
								public void done(ParseException arg0) {
				
									if (arg0 == null) {
										Log.d("renter", "Flat Delete Success");
										CommonFunctions.toastMessage(getActivity().getApplicationContext(),
												"Flat removed from the database");
									} else {
										Log.d("renter", "Flat Delete Failed.Exception:"+arg0.toString());
										CommonFunctions.toastMessage(getActivity().getApplicationContext(),
												"Error in removing flat.Exception:"+arg0.toString());
									}
								}
							});

						}
					} else {
						CommonFunctions.stopProgressDialog();
						Log.d("renter", "Flat Delete Failed.Exception:"+e.getMessage().toString());
						CommonFunctions.toastMessage(getActivity().getApplicationContext(),
								"Error in removing flat.Exception:"+e.getMessage().toString());

					}
					
				}
				
			});
			
			mListener.gotoFlatInfoFragment();
			return true;
		case R.id.action_deleteTenant:
			mListener.gotoFlatInfoFragment();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		final View view = inflater.inflate(
				R.layout.fragment_community_display_flat, container, false);

		((TextView) view.findViewById(R.id.flatNumberTextView))
				.setText(flatNumber);

		ParseQuery<ParseObject> query = ParseQuery
				.getQuery(CommonFunctions.FLATINFO_OBJECT);
		query.whereEqualTo("flatNumber", flatNumber);
		query.whereEqualTo("isMainTenant", true);
		query.whereEqualTo("communityObject", ParseUser.getCurrentUser()
				.getObjectId());
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> appList, ParseException e) {

				if (e == null) {
					for (ParseObject obj : appList) {
						String tenantName = obj.get("tenantName").toString();
						String tenantMailId = obj.get("tenantMailId")
								.toString();
						if (tenantName == "" && tenantMailId == "") {
							tenantName = "Not occupied";
							tenantMailId = "Not occupied";
						} else if (tenantName == "")
							tenantName = "Not Available";
						else if (tenantMailId == "")
							tenantMailId = "Not Available";
						((TextView) view.findViewById(R.id.tenantNameTextView))
								.setText(CommonFunctions.trimString(tenantName));
						((TextView) view
								.findViewById(R.id.tenantMailIdTextView))
								.setText(CommonFunctions
										.trimString(tenantMailId));
						/*
						 * obj.deleteInBackground(new DeleteCallback() {
						 * 
						 * @Override public void done(ParseException arg0) { if
						 * (arg0 == null) { Log.d("del", "Success"); } else {
						 * Log.d("del", "Failed " + arg0.toString()); } } });
						 */
					}
				} else {
					Log.d("score", "Error: " + e.getMessage());
				}
			}
		});

		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException ex) {
			throw new ClassCastException(activity.toString()
					+ " should implment OnFragmentInteractionListener");
		}
	}

	public interface OnFragmentInteractionListener {
		public void gotoFlatInfoFragment();
	}
}
