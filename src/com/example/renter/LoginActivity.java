package com.example.renter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.community.renter.CommunityMainActivity;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class LoginActivity extends Activity {

	String tenantName = null;
	String tenantApartmentNo = null;
	static boolean flag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			if (currentUser.getBoolean(CommonFunctions.USER_TABLE_ISCOMMUNITY)) {

				startActivity(new Intent(LoginActivity.this,
						CommunityMainActivity.class));
				finish();// added by midhun
			}

			else {
				if (flag == false)
					getTenantDetails();

			}
		}
		findViewById(R.id.signUpBtn).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						try {
							startActivity(new Intent(LoginActivity.this,
									SignUpActivity.class));
						} catch (Exception ex) {

							Toast.makeText(LoginActivity.this,
									"Unable to start signup activity",
									Toast.LENGTH_SHORT).show();

						}
					}
				});
		findViewById(R.id.loginBtn).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						final String email = ((EditText) findViewById(R.id.mUserName))
								.getText().toString();
						String password = ((EditText) findViewById(R.id.mPassword))
								.getText().toString();

						if (!TextUtils.isEmpty(email)
								&& !TextUtils.isEmpty(password)) {
							CommonFunctions.startProgressDialog(
									LoginActivity.this,
									"Logging in. Please wait...");
							ParseUser.logInInBackground(email, password,
									new LogInCallback() {
										public void done(ParseUser user,
												ParseException e) {
											CommonFunctions
													.stopProgressDialog();
											if (user != null) {
												if (user.get("emailVerified") == null) {
													Toast.makeText(
															LoginActivity.this,
															"Re-Create sign up account",
															Toast.LENGTH_SHORT)
															.show();

												} else if (user
														.getBoolean("emailVerified") == false) {
													Toast.makeText(
															LoginActivity.this,
															"Email is not verified",
															Toast.LENGTH_SHORT)
															.show();

												} else {
													Toast.makeText(
															LoginActivity.this,
															"Email Verified",
															Toast.LENGTH_SHORT)
															.show();
													ParseInstallation installation = ParseInstallation
															.getCurrentInstallation();
													installation
															.put("user",
																	ParseUser
																			.getCurrentUser());
													installation
															.put("channels",
																	new ArrayList<String>());
													installation
															.saveInBackground();

													if (ParseUser
															.getCurrentUser()
															.get(CommonFunctions.USER_TABLE_ISCOMMUNITY)
															.toString()
															.equals("true")) {
														String mCommunityPushMessageChannel = "Community_"
																+ CommonFunctions
																		.trimString(ParseUser
																				.getCurrentUser()
																				.getObjectId());

														List<String> subscribedChannels = ParseInstallation
																.getCurrentInstallation()
																.getList(
																		"channels");
														if (subscribedChannels == null
																|| !subscribedChannels
																		.contains(mCommunityPushMessageChannel)) {
															ParsePush
																	.subscribeInBackground(mCommunityPushMessageChannel);
														}

													} else {
														getTenantDetails();

													}

													ParseUser parseUser = ParseUser
															.getCurrentUser();
													Log.d("renter",
															"parseUser:"
																	+ parseUser
																			.toString());
													// SharedPreferences
													// settings =
													// getSharedPreferences(PREFS_NAME,
													// 0);
													if (parseUser
															.getBoolean("isCommunity")) {

														startActivity(new Intent(
																LoginActivity.this,
																CommunityMainActivity.class));
														finish();// added by
																	// midhun
													}

													else {

														startActivity(new Intent(
																LoginActivity.this,
																TenantHomePageActivity.class));
														finish();// added by
																	// midhun
													}
												}
											} else {
												CommonFunctions.toastMessage(
														LoginActivity.this,
														"Login Failed");
											}
										}
									});

						}
					}
				});
	}

	private void getTenantDetails() {
		ParseQuery<ParseObject> query = ParseQuery
				.getQuery(CommonFunctions.FLATINFO_TABLE);
		query.whereEqualTo(CommonFunctions.FLATINFO_TABLE_TENANT_MAILID,
				CommonFunctions.trimString(ParseUser.getCurrentUser()
						.getEmail()));
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> tenantList, ParseException e) {
				if (e == null) {
					if (tenantList.size() > 0) {
						ParseObject tenant = tenantList.get(0);
						tenantName = CommonFunctions.trimString(tenant.get(
								CommonFunctions.FLATINFO_TABLE_TENANT_NAME)
								.toString());
						tenantApartmentNo = CommonFunctions
								.trimString(tenant
										.get(CommonFunctions.FLATINFO_TABLE_FLAT_NUMBER)
										.toString());
						startActivity(new Intent(LoginActivity.this,
								TenantHomePageActivity.class));

						String mCommunityMessagesPushMessageChannel = "Messages_"
								+ CommonFunctions
										.trimString(ParseUser
												.getCurrentUser()
												.get(CommonFunctions.USER_TABLE_COMMUNITYID)
												.toString());
						List<String> subscribedChannels = ParseInstallation
								.getCurrentInstallation().getList("channels");
						if (subscribedChannels == null
								|| !subscribedChannels
										.contains(mCommunityMessagesPushMessageChannel)) {
							ParsePush
									.subscribeInBackground(mCommunityMessagesPushMessageChannel);
						}

						String mUserPushMessageChannel = "Tenant_"
								+ CommonFunctions
										.trimString(ParseUser
												.getCurrentUser()
												.get(CommonFunctions.USER_TABLE_COMMUNITYID)
												.toString()) + "_"
								+ tenantApartmentNo;
						if (subscribedChannels == null
								|| !subscribedChannels
										.contains(mUserPushMessageChannel)) {
							ParsePush
									.subscribeInBackground(mUserPushMessageChannel);
						}
						finish();// added by midhun
					}
				} else {
					Log.d("score", "Error: " + e.getMessage());
					CommonFunctions.toastMessage(LoginActivity.this, "Please login again");
				}
			}
		});
		flag = true;
	}

}
