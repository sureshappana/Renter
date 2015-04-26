package com.example.renter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.community.renter.CommunityMainActivity;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

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
													if(parseUser.getBoolean("isCommunity"))
													startActivity(new Intent(
															LoginActivity.this,
															CommunityMainActivity.class));
													else
														startActivity(new Intent(
																LoginActivity.this,
																TenantHomePageActivity.class));
												}
											} else {
												// Signup failed. Look at the
												// ParseException to see what
												// happened.
												Toast.makeText(
														LoginActivity.this,
														"Login Failed",
														Toast.LENGTH_SHORT)
														.show();
											}
											CommonFunctions
											.stopProgressDialog();
										}
									});

						}
					}
				});
	}
}
