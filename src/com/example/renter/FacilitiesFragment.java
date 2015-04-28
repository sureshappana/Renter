package com.example.renter;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.community.renter.FacilityAdapter;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class FacilitiesFragment extends Fragment {

	static ArrayList<Facility> facilities = new ArrayList<Facility>();
	ListView facilitiesListView = null;
	EditText mFacilityName = null;
	EditText mFacilityTotal = null;
	EditText mFacilityOccupied = null;
	LinearLayout layout = null;
	List<ParseObject> global_facilityList = null;
	String facilityName = null;
	int facilityTotal = -1;
	int facilityOccupied = 0;
	AlertDialog.Builder alert = null;

	public FacilitiesFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View view = inflater.inflate(
				R.layout.fragment_community_facilities, container, false);
		setHasOptionsMenu(true);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);

		updateFacilityListView();
	}

	private void updateFacilityListView() {
		facilities.clear();
		CommonFunctions
				.startProgressDialog(getActivity(),
						"Fetching Facility Details and its availablity. Please wait...");
		ParseQuery<ParseObject> queryRetriveFacility = ParseQuery
				.getQuery(CommonFunctions.FACILITY_TABLE);
		if (ParseUser.getCurrentUser().getBoolean("isCommunity")) {
			queryRetriveFacility.whereEqualTo(
					CommonFunctions.FACILITY_TABLE_COMMUNITY_OBJECT,
					CommonFunctions.trimString(ParseUser.getCurrentUser()
							.getObjectId()));
		} else {
			queryRetriveFacility.whereEqualTo(
					CommonFunctions.FACILITY_TABLE_COMMUNITY_OBJECT,
					CommonFunctions.trimString(ParseUser.getCurrentUser()
							.get("CommunityId").toString()));
		}

		Log.d("renter",
				"queryuser:"
						+ CommonFunctions.trimString(ParseUser.getCurrentUser()
								.getObjectId()));
		queryRetriveFacility.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> facilityList, ParseException arg1) {
				CommonFunctions.stopProgressDialog();
				if (arg1 == null) {
					global_facilityList = facilityList;
					Log.d("renter", "facilityObjetSize:" + facilities.size());

					for (int i = 0; i < facilityList.size(); i++) {
						facilities
								.add(new Facility(
										facilityList
												.get(i)
												.get(CommonFunctions.FACILITY_TABLE_FACILITY_NAME)
												.toString(),
										Integer.parseInt(facilityList
												.get(i)
												.get(CommonFunctions.FACILITY_TABLE_FACILITY_TOTAL)
												.toString()),
										Integer.parseInt(facilityList
												.get(i)
												.get(CommonFunctions.FACILITY_TABLE_FACILITY_TOTAL)
												.toString())
												- Integer
														.parseInt(facilityList
																.get(i)
																.get(CommonFunctions.FACILITY_TABLE_FACILITY_OCCUPIED)
																.toString())));
						Log.d("renter", facilities.toString());
					}

					facilitiesListView = (ListView) getActivity().findViewById(
							R.id.facilitiesListView);

					FacilityAdapter mFacilityAdapter = new FacilityAdapter(
							getActivity(), R.layout.facility_list_row,
							facilities);
					facilitiesListView.setAdapter(mFacilityAdapter);
					facilitiesListView
							.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

								@Override
								public boolean onItemLongClick(
										AdapterView<?> parent, View view,
										final int position, long id) {
									CommonFunctions.toastMessage(getActivity(),
											"long click");
									Log.d("renter",
											"facilities:listItemLongClick");

									initializeViews();

									alert.setTitle("Edit Facility:")
											.setPositiveButton(
													"Update",
													new DialogInterface.OnClickListener() {
														public void onClick(
																DialogInterface dialog,
																int whichButton) {
															if (validateAlertDialogInputs()) {
																Log.d("renter",
																		"facilities:updating_before_parse_query");
																try {

																	ParseQuery<ParseObject> query = ParseQuery
																			.getQuery(CommonFunctions.FACILITY_TABLE);
																	query.getInBackground(
																			global_facilityList
																					.get(position)
																					.getObjectId(),
																			new GetCallback<ParseObject>() {
																				public void done(
																						ParseObject addFacility,
																						ParseException e) {
																					if (e == null) {
																						addFacility
																								.put(CommonFunctions.FACILITY_TABLE_FACILITY_NAME,
																										facilityName);
																						addFacility
																								.put(CommonFunctions.FACILITY_TABLE_FACILITY_TOTAL,
																										facilityTotal);
																						addFacility
																								.put(CommonFunctions.FACILITY_TABLE_FACILITY_OCCUPIED,
																										facilityOccupied);
																						addFacility
																								.put(CommonFunctions.FACILITY_TABLE_COMMUNITY_OBJECT,
																										CommonFunctions
																												.trimString(ParseUser
																														.getCurrentUser()
																														.getObjectId()));
																						addFacility
																								.saveInBackground();
																						updateFacilityListView();
																					}
																				}
																			});

																} catch (Exception ex) {
																	Log.d("renter",
																			"Exception:facility update;"
																					+ ex.getMessage());
																}
																Log.d("renter",
																		"facilities:updating_after_parse_query");
															}

														}
													})
											.setNegativeButton(
													"Cancel",
													new DialogInterface.OnClickListener() {
														public void onClick(
																DialogInterface dialog,
																int whichButton) {
															/*
															 * User clicked
															 * cancel so do some
															 * stuff
															 */
														}
													});
									alert.show();
									mFacilityName.setText(facilities.get(
											position).getFacilityName());
									mFacilityName.setEnabled(false);
									mFacilityTotal.setText(""
											+ facilities.get(position)
													.getFacilityTotal());
									mFacilityOccupied
											.setText(""
													+ (facilities.get(position)
															.getFacilityTotal() - facilities
															.get(position)
															.getFacilityAvailable()));

									return false;
								}
							});
				} else {
					global_facilityList = null;
					CommonFunctions.toastMessage(
							getActivity(),
							"Error in fetching the facility info:"
									+ arg1.getMessage());
				}
			}
		});

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.facility_add_menu, menu);
		if (!ParseUser.getCurrentUser().getBoolean("isCommunity")) {
			MenuItem item = menu.findItem(R.id.action_addFacility);
			item.setVisible(false);

		}
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_refreshFacilityInfo:
			updateFacilityListView();
			return true;
		case R.id.action_addFacility:
			addFacilityFunction();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void addFacilityFunction() {
		initializeViews();
		final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
		alert.setTitle("Add Facility:")
				.setView(layout)
				.setPositiveButton("Add",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								mFacilityName.setEnabled(true);
								if (validateAlertDialogInputs()) {

									if (facilities != null) {
										for (Facility facility : facilities) {
											if (facility
													.getFacilityName()
													.toLowerCase()
													.equals(facilityName
															.toLowerCase())) {
												CommonFunctions
														.toastMessage(
																getActivity(),
																"Facility already existed.");
												return;
											}
										}
									}
									// CommonFunctions
									// .startProgressDialog(getActivity(),
									// "Facility is being added. Please wait...");
									Log.d("renter",
											"facilities:adding_before_parse_query");
									try {
										ParseObject addFacility = new ParseObject(
												CommonFunctions.FACILITY_TABLE);
										addFacility
												.put(CommonFunctions.FACILITY_TABLE_FACILITY_NAME,
														facilityName);
										addFacility
												.put(CommonFunctions.FACILITY_TABLE_FACILITY_TOTAL,
														facilityTotal);
										addFacility
												.put(CommonFunctions.FACILITY_TABLE_FACILITY_OCCUPIED,
														facilityOccupied);
										addFacility
												.put(CommonFunctions.FACILITY_TABLE_COMMUNITY_OBJECT,
														CommonFunctions
																.trimString(ParseUser
																		.getCurrentUser()
																		.getObjectId()));
										addFacility.saveInBackground();
									} catch (Exception ex) {
										Log.d("renter",
												"Exception:facility add;"
														+ ex.getMessage());
									}
									Log.d("renter",
											"facilities:adding_after_parse_query");
									updateFacilityListView();
									// CommonFunctions.stopProgressDialog();
									CommonFunctions.toastMessage(getActivity(),
											"Facililty Added");
								}

							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								/*
								 * User clicked cancel so do some stuff
								 */
							}
						});
		alert.show();

	}

	public boolean validateAlertDialogInputs() {

		facilityName = mFacilityName.getText().toString();
		if (!mFacilityTotal.getText().toString().isEmpty())
			facilityTotal = Integer.parseInt(mFacilityTotal.getText()
					.toString());

		if (!mFacilityOccupied.getText().toString().isEmpty())
			facilityOccupied = Integer.parseInt(mFacilityOccupied.getText()
					.toString());

		if (facilityName.isEmpty() || facilityTotal == -1) {
			CommonFunctions.toastMessage(getActivity(),
					"Facility Name and Total facilities should not be blank");
			return false;
		} else if (facilityTotal < facilityOccupied) {
			CommonFunctions.toastMessage(getActivity(),
					"Occupied should be less than total");
			return false;
		}
		return true;
	}

	private void initializeViews() {
		layout = new LinearLayout(getActivity());
		layout.setOrientation(LinearLayout.VERTICAL);

		mFacilityName = new EditText(getActivity());
		mFacilityName.setHint(getResources().getString(R.string.mFacilityName));
		mFacilityName.setGravity(Gravity.CENTER_HORIZONTAL);
		layout.addView(mFacilityName);

		mFacilityTotal = new EditText(getActivity());
		mFacilityTotal.setHint(getResources()
				.getString(R.string.mFacilityTotal));
		mFacilityTotal.setInputType(InputType.TYPE_CLASS_NUMBER);
		mFacilityTotal.setGravity(Gravity.CENTER_HORIZONTAL);
		layout.addView(mFacilityTotal);

		mFacilityOccupied = new EditText(getActivity());
		mFacilityOccupied.setHint(getResources().getString(
				R.string.mFacilityOccupied));
		mFacilityOccupied.setInputType(InputType.TYPE_CLASS_NUMBER);
		mFacilityOccupied.setGravity(Gravity.CENTER_HORIZONTAL);
		layout.addView(mFacilityOccupied);
		alert = new AlertDialog.Builder(getActivity());
		alert.setView(layout);

	}
}
