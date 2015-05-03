package com.community.renter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.common.renter.CommonFunctions;
import com.common.renter.RenterConstantVariables;
import com.example.renter.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SendCallback;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class NotificationsFragment extends Fragment {
	ArrayList<String> mFlatNoList, mFlatNoSelected;
	String[] mFlatNoArray;
	RadioGroup mRadioGroupNotificationSendTo;
	AlertDialog.Builder mBuilderFlatNoSelect;
	List<String> mailIds = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(
				R.layout.fragment_community_notifications, container, false);

		ParseQuery<ParseObject> mQueryRetrieveFlatMembers = ParseQuery
				.getQuery(RenterConstantVariables.FLATINFO_TABLE);
		mQueryRetrieveFlatMembers.whereEqualTo(CommonFunctions
				.trimString(RenterConstantVariables.FLATINFO_TABLE_COMMUNITY_OBJECT),
				(String) ParseUser.getCurrentUser().getObjectId());
		mQueryRetrieveFlatMembers.whereEqualTo(
				CommonFunctions.FlatInfoTableClass.TENANT_IS_OCCUPIED, true);
		mQueryRetrieveFlatMembers
				.findInBackground(new FindCallback<ParseObject>() {

					@Override
					public void done(List<ParseObject> objects, ParseException e) {

						mFlatNoList = new ArrayList<String>();
						for (int i = 0; i < objects.size(); i++) {
							String flatNo = (String) CommonFunctions
									.trimString(objects
											.get(i)
											.get(CommonFunctions.FlatInfoTableClass.TENANT_FLAT_NO)
											.toString());
							if (!mFlatNoList.contains(flatNo))
								mFlatNoList.add(flatNo);
						}
						mFlatNoArray = new String[mFlatNoList.size()];
						mFlatNoArray = (String[]) mFlatNoList
								.toArray(mFlatNoArray);
						mFlatNoSelected = new ArrayList<String>();
						try {
							mFlatNoSelected.clear();
						} catch (Exception e2) {
							e2.printStackTrace();
						}
						mFlatNoSelected.addAll(mFlatNoList);
						((TextView) getActivity().findViewById(
								R.id.textViewNotificationRecipients)).setText(CommonFunctions
								.trimString(mFlatNoSelected.toString()));
						mBuilderFlatNoSelect = new AlertDialog.Builder(
								getActivity());
						try {
							mBuilderFlatNoSelect
									.setTitle("Select the flats: ")
									.setMultiChoiceItems(
											mFlatNoArray,
											null,
											new DialogInterface.OnMultiChoiceClickListener() {

												@Override
												public void onClick(
														DialogInterface dialog,
														int which,
														boolean isChecked) {
													if (isChecked) {
														mFlatNoSelected
																.add(mFlatNoArray[which]);
													} else if (mFlatNoSelected
															.contains(mFlatNoArray[which])) {
														mFlatNoSelected
																.remove(mFlatNoSelected
																		.indexOf(mFlatNoArray[which]));
													}
												}
											})
									.setPositiveButton(
											"Ok",
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													((TextView) getActivity()
															.findViewById(
																	R.id.textViewNotificationRecipients))
															.setText(CommonFunctions
																	.trimString(mFlatNoSelected
																			.toString()));

												}
											});
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				});

		mRadioGroupNotificationSendTo = (RadioGroup) view
				.findViewById(R.id.radioGroupNotificationSendTo);
		mRadioGroupNotificationSendTo
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (checkedId == R.id.radio0NotificationSendToAll) {
							try {
								mFlatNoSelected.clear();
								mFlatNoSelected.addAll(mFlatNoList);
								((TextView) getActivity().findViewById(
										R.id.textViewNotificationRecipients))
										.setText(CommonFunctions
												.trimString(mFlatNoSelected
														.toString()));
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						} else if (checkedId == R.id.radio1NotificationSendToSpecific) {
							try {
								mFlatNoSelected.clear();
								((TextView) getActivity().findViewById(
										R.id.textViewNotificationRecipients))
										.setText("");
								AlertDialog dialog = mBuilderFlatNoSelect
										.create();
								dialog.setCancelable(true);
								dialog.show();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					}
				});

		((Button) view.findViewById(R.id.buttonNotificationSend))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						CommonFunctions.hideKeyboard(getActivity());
						String mNotificationText = ((EditText) view
								.findViewById(R.id.editTextNotificationText))
								.getText().toString();

						if (mFlatNoSelected.size() > 0) {
							if(((EditText)view.findViewById(R.id.editTextNotificationText)).getText().toString().length()>0)
							{
							Log.d("demo", mNotificationText + " Selected flats"
									+ mFlatNoSelected.toString());
							sendNotification();
							} else {
								CommonFunctions.toastMessage(getActivity(),
										"Please add message to notify");
							}
							
						}

						else {
							CommonFunctions.toastMessage(getActivity(),
									"Please add some notification");
						}
					}

				});
		return view;
	}
	private void clearEditTextFields(){
		((EditText) getActivity().findViewById(R.id.editTextNotificationText)).setText("");
		//mFlatNoSelected.clear();
		((EditText)getActivity().findViewById(R.id.editTextNotificationText)).setText("");

	}

	private void sendNotification() {

		CommonFunctions.toastMessage(getActivity(), "Sending Notification. Please wait...");
		mailIds = new ArrayList<String>();
		ParseQuery<ParseObject> queryRetrieveMailId = ParseQuery
				.getQuery(RenterConstantVariables.FLATINFO_TABLE);
		queryRetrieveMailId.whereContainedIn(
				RenterConstantVariables.FLATINFO_TABLE_FLAT_NUMBER, mFlatNoSelected);
		queryRetrieveMailId.selectKeys(Arrays
				.asList(RenterConstantVariables.FLATINFO_TABLE_TENANT_MAILID));
		queryRetrieveMailId.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					Log.d("renter",
							"Notification Fragment:Retrieved Mail Id:"
									+ objects.size());
					for (ParseObject obj : objects){
						mailIds.add(CommonFunctions.trimString(obj.get(
								RenterConstantVariables.FLATINFO_TABLE_TENANT_MAILID)
								.toString()));
						Log.d("renter",
								"Notification Fragment:Retrieved Mail Id:"
										+ CommonFunctions.trimString(obj.get(
												RenterConstantVariables.FLATINFO_TABLE_TENANT_MAILID)
												.toString()));

					}
						
					ParseQuery<ParseObject> queryRetrieveUserObject = ParseQuery
							.getQuery(RenterConstantVariables.USER_TABLE);
					queryRetrieveUserObject
							.whereContainedIn(
									RenterConstantVariables.USER_TABLE_USERNAME,
									mailIds);
					queryRetrieveUserObject
							.findInBackground(new FindCallback<ParseObject>() {

								@Override
								public void done(List<ParseObject> objects,
										ParseException e) {
									if (e == null) {
										Log.d("renter",
												"Notification Fragment:Retrieved User Objects:"
														+ objects.size());
										ParseQuery<ParseInstallation> pushQuery = ParseInstallation
												.getQuery();
										pushQuery.whereContainedIn("user",
												objects);
										ParsePush push = new ParsePush();
										push.setQuery(pushQuery);
										push.setMessage(((EditText) getActivity()
												.findViewById(
														R.id.editTextNotificationText))
												.getText().toString());
										push.sendInBackground(new SendCallback() {
											
											@Override
											public void done(ParseException e) {
												CommonFunctions.stopProgressDialog();
												clearEditTextFields();
												if(e == null){
													CommonFunctions.stopProgressDialog();
													CommonFunctions.toastMessage(getActivity(), "Notification Sent Successfully");
												} else {
													CommonFunctions.toastMessage(getActivity(), "Error in sending notification");
													Log.d("renter",
															"Error:Notification Fragment:Sending Notification "
																	+ e.toString());
													CommonFunctions.toastMessage(getActivity(), "Error in Sending Notification");

												}
												
											}
										});

									} else {
										Log.d("renter",
												"Error:Notification Fragment:Query Retrieve User Objects "
														+ e.toString());
										clearEditTextFields();
										CommonFunctions.stopProgressDialog();
										CommonFunctions.toastMessage(getActivity(), "Error in Sending Notification");
									}
								}
							});
				} else {
					Log.d("renter",
							"Error:Notification Fragment:Query Retrieve Mail Id "
									+ e.toString());
					clearEditTextFields();
					CommonFunctions.stopProgressDialog();
					CommonFunctions.toastMessage(getActivity(), "Error in Sending Notification");
				}
			}
		});
		//
		//
		// ParseQuery pushQuery = ParseInstallation.getQuery();
		// pushQuery.whereNotEqualTo("user", ParseUser.getCurrentUser());
		// JSONObject data = null;
		// try {
		// data = new JSONObject("{\"alert\": \""+
		// message.getText()+"\",\"title\": \""+ tenantName +"\"}");
		// } catch (JSONException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// ParsePush push = new ParsePush();
		// // push.setChannel(mCommunityMessagesPushMessageChannel);
		// push.setQuery(pushQuery);
		// //push.setMessage(message.getText().toString());
		// push.setData(data);
		// push.sendInBackground(new SendCallback() {

	}
}