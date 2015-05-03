package com.example.renter;

import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class TenantSignUpActivity extends Fragment {

	static ParseObject tenantObject = null;
	static String mailId = null;
	static String communityObject = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.activity_tenant_sign_up,
				container, false);
		doUIchanges(view, false);

		view.findViewById(R.id.mTenantSingUpButton).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						String password = ((EditText) view
								.findViewById(R.id.mTenantPassword)).getText()
								.toString();
						String cpassword = ((EditText) view
								.findViewById(R.id.mTenantCPassword)).getText()
								.toString();
						String contactNumber = ((EditText) view
								.findViewById(R.id.mTenantContactNumber))
								.getText().toString();
						if (password.length() <= 0 || cpassword.length() <= 0
								|| contactNumber.length() <= 0) {
							CommonFunctions.toastMessage(getActivity(),
									"Please enter all the details");
						} else {
							if (!mailId.isEmpty()) {

								ParseUser user = new ParseUser();
								user.setUsername(mailId);
								user.setPassword(password);
								user.setEmail(mailId);
								user.put("ContactNumber", Long.parseLong(contactNumber));
								user.put("isCommunity", false);
								user.put("CommunityId", communityObject);
								try {
									user.signUpInBackground(new SignUpCallback() {
										@Override
										public void done(
												com.parse.ParseException e) {
											Log.d("renter", "inside");
											if (e == null) {
												Log.d("renter", "created");
												CommonFunctions
														.toastMessage(
																getActivity().getApplicationContext(),
																"A confirmation email sent you the specified email.");
												ParseUser.logOut();
												startActivity(new Intent(
														view.getContext(),
														LoginActivity.class));

											} else {
												CommonFunctions
														.toastMessage(
																getActivity().getApplicationContext(),
																"Error in Creating account."
																		+ e.toString());
											}
											// progressDialog.dismiss();
											CommonFunctions
													.stopProgressDialog();
										}
									});
								} catch (Exception ex) {
									CommonFunctions.toastMessage(getActivity().getApplicationContext(),
											"Exception:" + ex.getMessage());
								}

							}
						}

					}
				});

		view.findViewById(R.id.mVerifyEmailButton).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						mailId = ((EditText) view
								.findViewById(R.id.mTenantEmail)).getText()
								.toString();

						if (mailId.length() > 0) {
							CommonFunctions.startProgressDialog(getActivity(),
									"Validating email");
							ParseQuery<ParseObject> query = ParseQuery
									.getQuery(CommonFunctions.FLATINFO_TABLE);
							query.whereEqualTo("tenantMailId", mailId);
							//query.whereEqualTo("isMainTenant", true);

							query.findInBackground(new FindCallback<ParseObject>() {
								public void done(List<ParseObject> appList,
										ParseException e) {
									CommonFunctions.stopProgressDialog();
									if (e == null) {
										CommonFunctions.stopProgressDialog();
										if (appList.size() == 0) {
											CommonFunctions
													.toastMessage(
															getActivity()
																	.getApplicationContext(),
															"Please enter the email registered with your community");
										} else {
											CommonFunctions
													.toastMessage(
															getActivity()
																	.getApplicationContext(),
															"Mail Id is availalable");
											tenantObject = appList.get(0);
											doUIchanges(view, true);
											((TextView) view
													.findViewById(R.id.mTenantName)).setText(CommonFunctions
													.trimString(tenantObject
															.get("tenantName")
															.toString()));
											((TextView) view
													.findViewById(R.id.mFlatNumberTextView)).setText(CommonFunctions
													.trimString(tenantObject
															.get("flatNumber")
															.toString()));
											communityObject= CommonFunctions.trimString(tenantObject.get("communityObject").toString());
											ParseQuery<ParseObject> query = ParseQuery
													.getQuery(CommonFunctions.USER_TABLE);
											// query.whereEqualTo("objectId",
											// appList.get(0).get("communityObject").toString());
											Log.d("renter",
													"communityObject:"
															+ tenantObject
																	.get("communityObject")
																	.toString());
											query.getInBackground(
													CommonFunctions
															.trimString(tenantObject
																	.get("communityObject")
																	.toString()),
													new GetCallback<ParseObject>() {
														public void done(
																ParseObject object,
																ParseException e) {
															if (e == null) {
																if (object != null)
																	((TextView) view
																			.findViewById(R.id.mCommunityNameTextView))
																			.setText(object
																					.get("CommunityName")
																					.toString());
															} else {
																CommonFunctions
																		.toastMessage(
																				getActivity()
																						.getApplicationContext(),
																				"Error in retrieving Community Details");

																Log.d("renter",
																		"Error in retrieving Community Details.Exception:"
																				+ e.toString());
															}
														}
													});

										}
									}
								}
							});
						} else {
							CommonFunctions
									.toastMessage(getActivity()
											.getApplicationContext(),
											"Please enter the email registered with your community");
						}
					}
				});
		return view;
	}

	private void doUIchanges(View view, boolean flag) {
		if (!flag) {
			view.findViewById(R.id.mCommunityNameTitleTextView).setVisibility(
					View.INVISIBLE);
			view.findViewById(R.id.mTenantName).setVisibility(View.INVISIBLE);
			view.findViewById(R.id.mTenantSingUpButton).setVisibility(
					View.INVISIBLE);
			view.findViewById(R.id.mTenantContactNumber).setVisibility(
					View.INVISIBLE);
			view.findViewById(R.id.mTenantCPassword).setVisibility(
					View.INVISIBLE);
			view.findViewById(R.id.mTenantPassword).setVisibility(
					View.INVISIBLE);
			view.findViewById(R.id.mCommunityNameTextView).setVisibility(
					View.INVISIBLE);
			view.findViewById(R.id.mTenantNameTitle).setVisibility(
					View.INVISIBLE);
			view.findViewById(R.id.mFlatNumberTextView).setVisibility(
					View.INVISIBLE);
			view.findViewById(R.id.mFlatNumberTitleTextView).setVisibility(
					View.INVISIBLE);
			view.findViewById(R.id.mVerifyEmailButton).setEnabled(!flag);
		} else {
			view.findViewById(R.id.mCommunityNameTitleTextView).setVisibility(
					View.VISIBLE);
			view.findViewById(R.id.mTenantName).setVisibility(View.VISIBLE);
			view.findViewById(R.id.mTenantSingUpButton).setVisibility(
					View.VISIBLE);
			view.findViewById(R.id.mTenantContactNumber).setVisibility(
					View.VISIBLE);
			view.findViewById(R.id.mTenantCPassword)
					.setVisibility(View.VISIBLE);
			view.findViewById(R.id.mTenantPassword).setVisibility(View.VISIBLE);
			view.findViewById(R.id.mCommunityNameTextView).setVisibility(
					View.VISIBLE);
			view.findViewById(R.id.mTenantNameTitle)
					.setVisibility(View.VISIBLE);
			view.findViewById(R.id.mFlatNumberTextView).setVisibility(
					View.VISIBLE);
			view.findViewById(R.id.mFlatNumberTitleTextView).setVisibility(
					View.VISIBLE);
			view.findViewById(R.id.mVerifyEmailButton).setEnabled(flag);
		}
	}
}
