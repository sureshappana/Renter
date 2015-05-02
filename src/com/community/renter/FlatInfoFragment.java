package com.community.renter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.renter.CommonFunctions;
import com.example.renter.R;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class FlatInfoFragment extends Fragment {
	OnFragmentInteractionListener mListener;
	EditText mFlatNumber = null;
	EditText mTenantName = null;
	EditText mTenantMailId = null;
	CheckBox mtenantDetailsCheckBox = null;
	LinearLayout layout = null;
	AlertDialog.Builder alert = null;
	List<ParseObject> global_facilityList = null;
	public static ArrayList<Flat> flats = new ArrayList<Flat>();
	static FlatsAdapter mFlatsAdapter = null;
	static Context global_context = null;

	public FlatInfoFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_community_flat_info,
				container, false);
		global_context = getActivity();
		setHasOptionsMenu(true);

		return view;
	}

	public interface OnFragmentInteractionListener {
		public void gotoFlatFragment();

		public void flatSelected(String flatNumber);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.flats_add_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_addFlat:
			// mListener.gotoFlatFragment();
			addFlat();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	};

	private void addFlat() {
		initializeViews();
		alert.setTitle("Add Flat:")
				.setPositiveButton("Add",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								String flatNumber = mFlatNumber.getText()
										.toString();
								if (!flatNumber.isEmpty()) {
									ParseObject flatInfo = new ParseObject(
											CommonFunctions.FLATINFO_TABLE);
									flatInfo.add(
											CommonFunctions.FLATINFO_TABLE_COMMUNITY_OBJECT,
											CommonFunctions
													.trimString(ParseUser
															.getCurrentUser()
															.getObjectId()));
									flatInfo.add(
											CommonFunctions.FLATINFO_TABLE_FLAT_NUMBER,
											flatNumber);

									if (mtenantDetailsCheckBox.isChecked()) {
										String tenantName = mTenantName
												.getText().toString();
										String tenantMailId = mTenantMailId
												.getText().toString();
										if (!tenantName.isEmpty()
												&& !tenantMailId.isEmpty()) {
											flatInfo.add(
													CommonFunctions.FLATINFO_TABLE_TENANT_NAME,
													tenantName);
											flatInfo.add(
													CommonFunctions.FLATINFO_TABLE_TENANT_MAILID,
													tenantMailId);
											flatInfo.add(
													CommonFunctions.FLATINFO_TABLE_ISOCCUPIED,
													true);

										} else {
											CommonFunctions.toastMessage(
													getActivity(),
													"Enter tenant Details");
											return;
										}
									} else {
										flatInfo.add(
												CommonFunctions.FLATINFO_TABLE_ISOCCUPIED,
												false);
									}
									flatInfo.add(
											CommonFunctions.FLATINFO_TABLE_ADDEDBY,
											ParseUser.getCurrentUser()
													.getEmail());

									flatInfo.saveInBackground();
									CommonFunctions.toastMessage(getActivity(),
											"Flat Added");
									updateList();
								}
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
							}
						});
		alert.show();

	}

	private void initializeViews() {

		layout = new LinearLayout(getActivity());
		layout.setOrientation(LinearLayout.VERTICAL);

		mFlatNumber = new EditText(getActivity());
		mFlatNumber.setHint(getResources().getString(R.string.flatNumberhint));
		mFlatNumber.setGravity(Gravity.CENTER_HORIZONTAL);
		layout.addView(mFlatNumber);

		mtenantDetailsCheckBox = new CheckBox(getActivity());
		mtenantDetailsCheckBox.setHint("Enter tenant Details");
		mtenantDetailsCheckBox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							mTenantName.setVisibility(View.VISIBLE);
							mTenantMailId.setVisibility(View.VISIBLE);
						} else {
							mTenantName.setVisibility(View.INVISIBLE);
							mTenantMailId.setVisibility(View.INVISIBLE);
						}
					}
				});
		layout.addView(mtenantDetailsCheckBox);

		mTenantName = new EditText(getActivity());
		mTenantName.setHint(getResources().getString(R.string.tenantNamehint));
		mTenantName.setGravity(Gravity.CENTER_HORIZONTAL);
		mTenantName.setVisibility(View.INVISIBLE);
		layout.addView(mTenantName);

		mTenantMailId = new EditText(getActivity());
		mTenantMailId.setHint(getResources().getString(
				R.string.tenantMailIdhint));
		mTenantMailId.setGravity(Gravity.CENTER_HORIZONTAL);
		mTenantMailId.setVisibility(View.INVISIBLE);
		layout.addView(mTenantMailId);
		alert = new AlertDialog.Builder(getActivity());
		alert.setView(layout);

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

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		updateList();
	}

	void updateList() {
		flats.clear();
		CommonFunctions
				.startProgressDialog(getActivity(),
						"Fetching Facility Details and its availablity. Please wait...");
		ParseQuery<ParseObject> queryRetriveFlatInfo = ParseQuery
				.getQuery(CommonFunctions.FLATINFO_TABLE);
		queryRetriveFlatInfo.whereEqualTo(
				CommonFunctions.FLATINFO_TABLE_COMMUNITY_OBJECT,
				CommonFunctions.trimString(ParseUser.getCurrentUser()
						.getObjectId()));
		queryRetriveFlatInfo
				.addAscendingOrder(CommonFunctions.FLATINFO_TABLE_FLAT_NUMBER);
		queryRetriveFlatInfo.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> flatsList, ParseException arg1) {
				CommonFunctions.stopProgressDialog();
				if (arg1 == null) {
					global_facilityList = flatsList;
					for (int i = 0; i < flatsList.size(); i++) {
						String temp_tenantName = "", temp_tenantMailId = "";
						if (flatsList.get(i).get(
								CommonFunctions.FLATINFO_TABLE_TENANT_NAME) != null)
							temp_tenantName = CommonFunctions
									.trimString(flatsList
											.get(i)
											.get(CommonFunctions.FLATINFO_TABLE_TENANT_NAME)
											.toString());
						if (flatsList.get(i).get(
								CommonFunctions.FLATINFO_TABLE_TENANT_MAILID) != null)
							temp_tenantMailId = CommonFunctions
									.trimString(flatsList
											.get(i)
											.get(CommonFunctions.FLATINFO_TABLE_TENANT_MAILID)
											.toString());
						flats.add(new Flat(
								CommonFunctions
										.trimString(flatsList
												.get(i)
												.get(CommonFunctions.FLATINFO_TABLE_FLAT_NUMBER)
												.toString()),
								temp_tenantName,
								temp_tenantMailId,
								CommonFunctions
										.trimString(flatsList
												.get(i)
												.get(CommonFunctions.FLATINFO_TABLE_ISOCCUPIED)
												.toString())

						));
					}
					mFlatsAdapter = new FlatsAdapter(getActivity(),
							R.layout.flats_list_row, flats);

					// adapter.setTextKey("flatNumber");
					ListView flatsListView = (ListView) getActivity()
							.findViewById(R.id.flatsListView);
					flatsListView.setAdapter(mFlatsAdapter);
					CommonFunctions.stopProgressDialog();
					flatsListView
							.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
								@Override
								public boolean onItemLongClick(
										AdapterView<?> parent, View view,
										final int position, long id) {
									initializeViews();
									alert.setTitle("Edit Flat Info:")
											.setPositiveButton(
													"Update",
													new DialogInterface.OnClickListener() {
														public void onClick(
																DialogInterface dialog,
																int whichButton) {
															final String flatNumber = mFlatNumber
																	.getText()
																	.toString();
															mFlatNumber
																	.setEnabled(false);
															try {

																ParseQuery<ParseObject> query = ParseQuery
																		.getQuery(CommonFunctions.FLATINFO_TABLE);
																query.getInBackground(
																		global_facilityList
																				.get(position)
																				.getObjectId(),
																		new GetCallback<ParseObject>() {
																			public void done(
																					ParseObject flatInfo,
																					ParseException e) {
																				if (e == null) {
																					// flatInfo.add(
																					// CommonFunctions.FLATINFO_TABLE_COMMUNITY_OBJECT,
																					// CommonFunctions
																					// .trimString(ParseUser
																					// .getCurrentUser()
																					// .getObjectId()));
																					// flatInfo.add(
																					// CommonFunctions.FLATINFO_TABLE_FLAT_NUMBER,
																					// flatNumber);

																					if (mtenantDetailsCheckBox
																							.isChecked()) {
																						String tenantName = mTenantName
																								.getText()
																								.toString();
																						String tenantMailId = mTenantMailId
																								.getText()
																								.toString();
																						if (!tenantName
																								.isEmpty()
																								&& !tenantMailId
																										.isEmpty()) {
																							flatInfo.put(
																									CommonFunctions.FLATINFO_TABLE_TENANT_NAME,
																									tenantName);
																							flatInfo.put(
																									CommonFunctions.FLATINFO_TABLE_TENANT_MAILID,
																									tenantMailId);
																							flatInfo.put(
																									CommonFunctions.FLATINFO_TABLE_ISOCCUPIED,
																									true);

																						} else {
																							CommonFunctions
																									.toastMessage(
																											getActivity(),
																											"Enter tenant Details");
																							return;
																						}
																					} else {
																						flatInfo.put(
																								CommonFunctions.FLATINFO_TABLE_ISOCCUPIED,
																								false);
																					}
																					flatInfo.saveInBackground();
																					CommonFunctions
																							.toastMessage(
																									getActivity(),
																									"Flat Updated");
																					updateList();

																				}
																			}
																		});

															} catch (Exception ex) {
																Log.d("renter",
																		"Exception:facility update;"
																				+ ex.getMessage());
															}

														}

													})
											.setNegativeButton(
													"Cancel",
													new DialogInterface.OnClickListener() {
														public void onClick(
																DialogInterface dialog,
																int whichButton) {
														}
													})
											.setNeutralButton(
													"Remove Tenant Info",
													new DialogInterface.OnClickListener() {

														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															// TODO
															// Auto-generated
															// method stub

														}
													});
									alert.show();
									mFlatNumber.setText(flats.get(position)
											.getFlatNumber());
									if (flats.get(position)
											.getFlatOccupiedStatus()
											.equals("true")) {
										mtenantDetailsCheckBox.setChecked(true);
										mTenantName.setText(flats.get(position)
												.getTenantName());
										mTenantMailId.setText(flats.get(
												position).getTenantMailId());
									}
									return false;
								}
							});
				}
			}
		});

	}

	public static void removeListViewItem(final int position) {
		AlertDialog.Builder confirmationAlert = new AlertDialog.Builder(
				global_context);
		confirmationAlert
				.setTitle("Confirm to delete?")
				.setMessage("Click yes to proceed")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								CommonFunctions.startProgressDialog(
										global_context,
										"Deleting the item. Please wait...");

								ParseQuery<ParseObject> query = ParseQuery
										.getQuery(CommonFunctions.FLATINFO_TABLE);

								query.whereEqualTo(
										CommonFunctions.FLATINFO_TABLE_COMMUNITY_OBJECT,
										CommonFunctions.trimString(ParseUser
												.getCurrentUser().getObjectId()
												.toString()));
								query.whereEqualTo(
										CommonFunctions.FLATINFO_TABLE_FLAT_NUMBER,
										mFlatsAdapter.getItem(position)
												.getFlatNumber());
								query.findInBackground(new FindCallback<ParseObject>() {
									public void done(List<ParseObject> appList,
											ParseException e) {
										CommonFunctions.stopProgressDialog();
										if (e == null) {
											for (ParseObject obj : appList) {
												obj.deleteInBackground(new DeleteCallback() {
													@Override
													public void done(
															ParseException arg0) {
														if (arg0 == null) {
															mFlatsAdapter
																	.remove(mFlatsAdapter
																			.getItem(position));
															CommonFunctions
																	.toastMessage(
																			global_context,
																			"Deleted successfully");
														} else {
															CommonFunctions
																	.toastMessage(
																			global_context,
																			"Error in deleting flat. Error:"
																					+ arg0.toString());
														}
													}
												});
											}
										}
									}
								});

							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						return;

					}
				});
		confirmationAlert.show();
	}
}
