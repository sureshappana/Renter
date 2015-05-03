package com.community.renter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
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
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class FlatInfoFragment extends Fragment {
	static EditText mFlatNumber = null;
	static EditText mTenantName = null;
	static EditText mTenantMailId = null;
	static CheckBox mtenantDetailsCheckBox = null;
	static LinearLayout layout = null;
	static AlertDialog.Builder alert = null;
	static List<ParseObject> global_flatsList = new ArrayList<ParseObject>();
	public static ArrayList<Flat> flats = new ArrayList<Flat>();
	static FlatsAdapter mFlatsAdapter = null;
	static Activity global_activity = null;

	public FlatInfoFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_community_flat_info,
				container, false);
		global_activity = getActivity();
		setHasOptionsMenu(true);

		return view;
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
								final String flatNumber = mFlatNumber.getText()
										.toString();
								if (!flatNumber.isEmpty()) {

									ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
											CommonFunctions.FLATINFO_TABLE);
									query.whereEqualTo(
											CommonFunctions.FLATINFO_TABLE_COMMUNITY_OBJECT,
											CommonFunctions
													.trimString(ParseUser
															.getCurrentUser()
															.getObjectId()));
									query.whereEqualTo(
											CommonFunctions.FLATINFO_TABLE_FLAT_NUMBER,
											flatNumber);
									query.findInBackground(new FindCallback<ParseObject>() {

										@Override
										public void done(
												List<ParseObject> objects,
												ParseException e) {
											if (objects.size() != 0) {
												CommonFunctions
														.toastMessage(
																global_activity,
																"Entered flat number is already existed");
												return;
											} else {
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

												if (mtenantDetailsCheckBox
														.isChecked()) {
													String tenantName = mTenantName
															.getText()
															.toString();
													String tenantMailId = mTenantMailId
															.getText()
															.toString();
													if (!tenantName.isEmpty()
															&& !tenantMailId
																	.isEmpty()) {
														if (android.util.Patterns.EMAIL_ADDRESS
																.matcher(
																		tenantMailId)
																.matches()) {
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
															CommonFunctions
																	.toastMessage(
																			global_activity,
																			"Email is not valid");
															return;
														}

													} else {
														CommonFunctions
																.toastMessage(
																		global_activity,
																		"Enter tenant Details");
														return;
													}
												} else {
													flatInfo.add(
															CommonFunctions.FLATINFO_TABLE_ISOCCUPIED,
															false);
												}

												flatInfo.saveInBackground();
												CommonFunctions.toastMessage(
														global_activity,
														"Flat Added");
												updateList();
											}
										}
									});

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

	private static void initializeViews() {

		layout = new LinearLayout(global_activity);
		layout.setOrientation(LinearLayout.VERTICAL);

		mFlatNumber = new EditText(global_activity);
		mFlatNumber.setHint(global_activity.getResources().getString(
				R.string.flatNumberhint));
		mFlatNumber.setGravity(Gravity.CENTER_HORIZONTAL);
		layout.addView(mFlatNumber);

		mtenantDetailsCheckBox = new CheckBox(global_activity);
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

		mTenantName = new EditText(global_activity);
		mTenantName.setHint(global_activity.getResources().getString(
				R.string.tenantNamehint));
		mTenantName.setGravity(Gravity.CENTER_HORIZONTAL);
		mTenantName.setVisibility(View.INVISIBLE);
		layout.addView(mTenantName);

		mTenantMailId = new EditText(global_activity);
		mTenantMailId.setHint(global_activity.getResources().getString(
				R.string.tenantMailIdhint));
		mTenantMailId.setGravity(Gravity.CENTER_HORIZONTAL);
		mTenantMailId.setVisibility(View.INVISIBLE);
		layout.addView(mTenantMailId);
		alert = new AlertDialog.Builder(global_activity);
		alert.setView(layout);

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		updateList();
	}

	static void updateList() {
		flats.clear();
		global_flatsList.clear();
		CommonFunctions.startProgressDialog(global_activity,
				"Fetching Flat Details and its availablity. Please wait...");
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
					global_flatsList = flatsList;
					ArrayList<String> temp_list = new ArrayList<String>();
					for (int i = 0; i < flatsList.size(); i++) {
						String fNumber = CommonFunctions
								.trimString(flatsList
										.get(i)
										.get(CommonFunctions.FLATINFO_TABLE_FLAT_NUMBER)
										.toString());
						if (!temp_list.contains(fNumber)) {
							temp_list.add(fNumber);
							String temp_tenantName = "", temp_tenantMailId = "";
							if (flatsList.get(i).get(
									CommonFunctions.FLATINFO_TABLE_TENANT_NAME) != null)
								temp_tenantName = CommonFunctions
										.trimString(flatsList
												.get(i)
												.get(CommonFunctions.FLATINFO_TABLE_TENANT_NAME)
												.toString());
							if (flatsList
									.get(i)
									.get(CommonFunctions.FLATINFO_TABLE_TENANT_MAILID) != null)
								temp_tenantMailId = CommonFunctions
										.trimString(flatsList
												.get(i)
												.get(CommonFunctions.FLATINFO_TABLE_TENANT_MAILID)
												.toString());
							flats.add(new Flat(
									fNumber,
									temp_tenantName,
									temp_tenantMailId,
									CommonFunctions
											.trimString(flatsList
													.get(i)
													.get(CommonFunctions.FLATINFO_TABLE_ISOCCUPIED)
													.toString())

							));
						}
					}
					mFlatsAdapter = new FlatsAdapter(global_activity,
							R.layout.flats_list_row, flats);

					// adapter.setTextKey("flatNumber");
					ListView flatsListView = (ListView) global_activity
							.findViewById(R.id.flatsListView);
					flatsListView.setAdapter(mFlatsAdapter);
					CommonFunctions.stopProgressDialog();
					flatsListView
							.setOnItemClickListener(new AdapterView.OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
									final String flatNumber = mFlatsAdapter
											.getItem(position).getFlatNumber();
									final ArrayList<String> listItems = new ArrayList<String>();
									final ArrayList<String> listItemsMailId = new ArrayList<String>();
									final ArrayList<String> listItemsMailSelected = new ArrayList<String>();
									for (ParseObject obj : global_flatsList) {
										if (CommonFunctions
												.trimString(
														obj.get(CommonFunctions.FLATINFO_TABLE_FLAT_NUMBER)
																.toString())
												.equals(flatNumber)) {
											if (CommonFunctions
													.trimString(
															obj.get(CommonFunctions.FLATINFO_TABLE_ISOCCUPIED)
																	.toString())
													.equals("true")) {
												listItems.add(CommonFunctions
														.trimString(obj
																.get(CommonFunctions.FLATINFO_TABLE_TENANT_NAME)
																.toString()));
												listItemsMailId.add(CommonFunctions
														.trimString(obj
																.get(CommonFunctions.FLATINFO_TABLE_TENANT_MAILID)
																.toString()));
											}
										}
									}

									AlertDialog.Builder confirmationAlert = new AlertDialog.Builder(
											global_activity);
									if (listItems.size() != 0) {
										confirmationAlert.setItems(
												listItems
														.toArray(new CharSequence[listItems
																.size()]),
												null);
									} else
										confirmationAlert
												.setMessage("Flat is vacant");
									confirmationAlert
											.setTitle(flatNumber)
											.setNegativeButton(
													"Cancel",
													new DialogInterface.OnClickListener() {

														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															return;

														}
													});

									confirmationAlert.show();
								}
							});
					// flatsListView
					// .setOnItemLongClickListener(new
					// AdapterView.OnItemLongClickListener() {
					// @Override
					// public boolean onItemLongClick(
					// AdapterView<?> parent, View view,
					// final int position, long id) {
					// initializeViews();
					// alert.setTitle("Edit Flat Info:")
					// .setPositiveButton(
					// "Update",
					// new DialogInterface.OnClickListener() {
					// public void onClick(
					// DialogInterface dialog,
					// int whichButton) {
					// final String flatNumber = mFlatNumber
					// .getText()
					// .toString();
					// mFlatNumber
					// .setEnabled(false);
					// try {
					//
					// ParseQuery<ParseObject> query = ParseQuery
					// .getQuery(CommonFunctions.FLATINFO_TABLE);
					// query.getInBackground(
					// global_flatsList
					// .get(position)
					// .getObjectId(),
					// new GetCallback<ParseObject>() {
					// public void done(
					// ParseObject flatInfo,
					// ParseException e) {
					// if (e == null) {
					// // flatInfo.add(
					// // CommonFunctions.FLATINFO_TABLE_COMMUNITY_OBJECT,
					// // CommonFunctions
					// // .trimString(ParseUser
					// // .getCurrentUser()
					// // .getObjectId()));
					// // flatInfo.add(
					// // CommonFunctions.FLATINFO_TABLE_FLAT_NUMBER,
					// // flatNumber);
					//
					// if (mtenantDetailsCheckBox
					// .isChecked()) {
					// String tenantName = mTenantName
					// .getText()
					// .toString();
					// String tenantMailId = mTenantMailId
					// .getText()
					// .toString();
					// if (!tenantName
					// .isEmpty()
					// && !tenantMailId
					// .isEmpty()) {
					// flatInfo.put(
					// CommonFunctions.FLATINFO_TABLE_TENANT_NAME,
					// tenantName);
					// flatInfo.put(
					// CommonFunctions.FLATINFO_TABLE_TENANT_MAILID,
					// tenantMailId);
					// flatInfo.put(
					// CommonFunctions.FLATINFO_TABLE_ISOCCUPIED,
					// true);
					//
					// } else {
					// CommonFunctions
					// .toastMessage(
					// global_activity,
					// "Enter tenant Details");
					// return;
					// }
					// } else {
					// flatInfo.put(
					// CommonFunctions.FLATINFO_TABLE_ISOCCUPIED,
					// false);
					// }
					// flatInfo.saveInBackground();
					// CommonFunctions
					// .toastMessage(
					// global_activity,
					// "Flat Updated");
					// updateList();
					//
					// }
					// }
					// });
					//
					// } catch (Exception ex) {
					// Log.d("renter",
					// "Exception:facility update;"
					// + ex.getMessage());
					// }
					//
					// }
					//
					// })
					// .setNegativeButton(
					// "Cancel",
					// new DialogInterface.OnClickListener() {
					// public void onClick(
					// DialogInterface dialog,
					// int whichButton) {
					// }
					// })
					// .setNeutralButton(
					// "Remove Tenant Info",
					// new DialogInterface.OnClickListener() {
					//
					// @Override
					// public void onClick(
					// DialogInterface dialog,
					// int which) {
					// // TODO
					// // Auto-generated
					// // method stub
					//
					// }
					// });
					// alert.show();
					// mFlatNumber.setText(flats.get(position)
					// .getFlatNumber());
					// if (flats.get(position)
					// .getFlatOccupiedStatus()
					// .equals("true")) {
					// mtenantDetailsCheckBox.setChecked(true);
					// mTenantName.setText(flats.get(position)
					// .getTenantName());
					// mTenantMailId.setText(flats.get(
					// position).getTenantMailId());
					// }
					// return false;
					// }
					// });
				} else {
					CommonFunctions.stopProgressDialog();
					CommonFunctions
							.toastMessage(global_activity,
									"Error in retrieving the details. Check internet connection");
				}
			}
		});

	}

	public static void removeListViewItem(final int position) {

		final String flatNumber = mFlatsAdapter.getItem(position)
				.getFlatNumber();
		final ArrayList<String> listItems = new ArrayList<String>();
		final ArrayList<String> listItemsMailId = new ArrayList<String>();
		final ArrayList<String> listItemsMailSelected = new ArrayList<String>();
		for (ParseObject obj : global_flatsList) {
			if (CommonFunctions.trimString(
					obj.get(CommonFunctions.FLATINFO_TABLE_FLAT_NUMBER)
							.toString()).equals(flatNumber)) {
				if (CommonFunctions.trimString(
						obj.get(CommonFunctions.FLATINFO_TABLE_ISOCCUPIED)
								.toString()).equals("true")) {
					listItems.add(CommonFunctions.trimString(obj.get(
							CommonFunctions.FLATINFO_TABLE_TENANT_NAME)
							.toString()));
					listItemsMailId.add(CommonFunctions.trimString(obj.get(
							CommonFunctions.FLATINFO_TABLE_TENANT_MAILID)
							.toString()));
				}
			}
		}

		AlertDialog.Builder confirmationAlert = new AlertDialog.Builder(
				global_activity);
		if (listItems.size() != 0) {
			confirmationAlert.setMultiChoiceItems(
					listItems.toArray(new CharSequence[listItems.size()]),
					null, new OnMultiChoiceClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which,
								boolean isChecked) {
							if (isChecked) {
								listItemsMailSelected.add(listItemsMailId
										.get(which));
							} else if (listItemsMailSelected
									.contains(listItemsMailId.get(which))) {
								listItemsMailSelected.remove(listItemsMailId
										.get(which));
							}

						}
					});
		} else
			confirmationAlert.setMessage("Flat is vacant");
		confirmationAlert
				.setTitle(flatNumber)
				.setNeutralButton("Delete Flat",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								CommonFunctions.startProgressDialog(
										global_activity,
										"Deleting the Flat. Please wait...");

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
																			global_activity,
																			"Deleted successfully");
														} else {
															CommonFunctions
																	.toastMessage(
																			global_activity,
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
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								return;

							}
						});
		if (listItems.size() != 0)
			confirmationAlert.setPositiveButton("Remove Slected",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							deleteSelected(
									listItemsMailSelected,
									flatNumber,
									listItemsMailSelected.size() == listItemsMailId
											.size());
						}
					});
		confirmationAlert.show();
	}

	private static void deleteSelected(
			final ArrayList<String> listItemsMailSelected,
			final String flatNumber, final boolean status) {

		// CommonFunctions.startProgressDialog(
		// global_activity,
		// "Removing the selected tenants");
		ParseQuery<ParseObject> query = ParseQuery
				.getQuery(CommonFunctions.FLATINFO_TABLE);
		query.whereContainedIn(CommonFunctions.FLATINFO_TABLE_TENANT_MAILID,
				listItemsMailSelected);
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> flatInfoTableList,
					ParseException e) {
				if (e == null) {
					for (ParseObject obj : flatInfoTableList)
						obj.deleteInBackground();

					if (status) {
						ParseObject object = new ParseObject(
								CommonFunctions.FLATINFO_TABLE);
						object.add(
								CommonFunctions.FLATINFO_TABLE_COMMUNITY_OBJECT,
								CommonFunctions.trimString(ParseUser
										.getCurrentUser().getObjectId()
										.toString()));
						object.add(CommonFunctions.FLATINFO_TABLE_ISOCCUPIED,
								false);
						object.add(CommonFunctions.FLATINFO_TABLE_FLAT_NUMBER,
								flatNumber);
						object.saveInBackground(new SaveCallback() {

							@Override
							public void done(ParseException e) {
								if (e == null) {
									updateList();
								} else {
									CommonFunctions.toastMessage(
											global_activity,
											"Error in processing.");
									Log.d("renter",
											"Error in readding the flat."
													+ e.toString());

								}

							}
						});
					} else {
						updateList();
					}
				} else {
					CommonFunctions.stopProgressDialog();
					CommonFunctions.toastMessage(global_activity,
							"Error in removing the tenants");

				}

			}
		});

	}

	public static void addFlatMembers(final int position) {

		layout = new LinearLayout(global_activity);
		layout.setOrientation(LinearLayout.VERTICAL);

		mTenantName = new EditText(global_activity);
		mTenantName.setHint(global_activity.getResources().getString(
				R.string.tenantNamehint));
		mTenantName.setGravity(Gravity.CENTER_HORIZONTAL);
		layout.addView(mTenantName);

		mTenantMailId = new EditText(global_activity);
		mTenantMailId.setHint(global_activity.getResources().getString(
				R.string.tenantMailIdhint));
		mTenantMailId.setGravity(Gravity.CENTER_HORIZONTAL);
		layout.addView(mTenantMailId);

		alert = new AlertDialog.Builder(global_activity);
		alert.setView(layout)
				.setTitle(mFlatsAdapter.getItem(position).getFlatNumber())
				.setPositiveButton("Add", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						ParseQuery<ParseObject> removeFlatInfo = new ParseQuery<ParseObject>(
								CommonFunctions.FLATINFO_TABLE);
						removeFlatInfo
								.whereEqualTo(
										CommonFunctions.FLATINFO_TABLE_COMMUNITY_OBJECT,
										CommonFunctions
												.trimString(ParseUser
														.getCurrentUser()
														.getObjectId()));
						removeFlatInfo.whereEqualTo(
								CommonFunctions.FLATINFO_TABLE_ISOCCUPIED,
								false);
						removeFlatInfo
								.whereEqualTo(
										CommonFunctions.FLATINFO_TABLE_FLAT_NUMBER,
										mFlatsAdapter.getItem(position)
												.getFlatNumber());
						removeFlatInfo
								.findInBackground(new FindCallback<ParseObject>() {

									@Override
									public void done(List<ParseObject> objects,
											ParseException e) {
										if (e == null) {
											for (ParseObject obj : objects) {
												obj.deleteInBackground();
											}
										}

									}
								});

						ParseObject flatInfo = new ParseObject(
								CommonFunctions.FLATINFO_TABLE);
						flatInfo.add(
								CommonFunctions.FLATINFO_TABLE_COMMUNITY_OBJECT,
								CommonFunctions.trimString(ParseUser
										.getCurrentUser().getObjectId()));
						flatInfo.add(
								CommonFunctions.FLATINFO_TABLE_FLAT_NUMBER,
								mFlatsAdapter.getItem(position).getFlatNumber());

						String tenantName = mTenantName.getText().toString();
						String tenantMailId = mTenantMailId.getText()
								.toString();
						if (!tenantName.isEmpty() && !tenantMailId.isEmpty()) {
							if (android.util.Patterns.EMAIL_ADDRESS.matcher(
									tenantMailId).matches()) {
								flatInfo.add(
										CommonFunctions.FLATINFO_TABLE_TENANT_NAME,
										tenantName);
								flatInfo.add(
										CommonFunctions.FLATINFO_TABLE_TENANT_MAILID,
										tenantMailId);
								flatInfo.add(
										CommonFunctions.FLATINFO_TABLE_ISOCCUPIED,
										true);

								flatInfo.saveInBackground(new SaveCallback() {

									@Override
									public void done(ParseException e) {
										if (e == null) {
											CommonFunctions.toastMessage(
													global_activity,
													"Tenant Added");
											updateList();
										} else {
											CommonFunctions
													.toastMessage(
															global_activity,
															"Error in adding the tenant information");
										}

									}
								});

							} else {
								CommonFunctions.toastMessage(global_activity,
										"Email is not valid");

							}

						} else {
							CommonFunctions.toastMessage(global_activity,
									"All fields are mandatory.");
							return;
						}

					}
				}).setNegativeButton("Cancel", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
		alert.show();

	}
}
