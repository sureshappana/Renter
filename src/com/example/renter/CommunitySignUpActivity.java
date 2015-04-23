package com.example.renter;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class CommunitySignUpActivity extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View view = inflater.inflate(R.layout.activity_sign_up_community,
				container, false);

		((Button) view.findViewById(R.id.communitySignUpBtn))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						final Context context = getActivity().getApplicationContext();
						int mZipCode = -1;
						long mContactNumber = -1;

						String mCommunityName = ((EditText) view
								.findViewById(R.id.mCommunityName)).getText()
								.toString();
						String mCommunityEmail = ((EditText) view
								.findViewById(R.id.mCommunityEmail)).getText()
								.toString();
						String mPassword = ((EditText) view
								.findViewById(R.id.mPassword)).getText()
								.toString();
						String mCPassword = ((EditText) view
								.findViewById(R.id.mCPassword)).getText()
								.toString();
						String mPostalAddress = ((EditText) view
								.findViewById(R.id.mPostalAddress)).getText()
								.toString();
						String mCity = ((EditText) view
								.findViewById(R.id.mCity)).getText().toString();
						String mState = ((EditText) view
								.findViewById(R.id.mState)).getText()
								.toString();
						String mCountry = ((EditText) view
								.findViewById(R.id.mCountry)).getText()
								.toString();
						if (!TextUtils.isEmpty(((EditText) view
								.findViewById(R.id.mZipCode)).getText()
								.toString()))
							mZipCode = Integer.parseInt(((EditText) view
									.findViewById(R.id.mZipCode)).getText()
									.toString());
						if (((EditText) view.findViewById(R.id.mContactNumber))
								.getText().toString().length() > 0)
							mContactNumber = Long.parseLong(((EditText) view
									.findViewById(R.id.mContactNumber))
									.getText().toString());
						if (!TextUtils.isEmpty(mCommunityName)
								&& !TextUtils.isEmpty(mCommunityEmail)
								&& mContactNumber != -1 && mZipCode != -1
								&& !TextUtils.isEmpty(mPassword)
								&& !TextUtils.isEmpty(mCPassword)
								&& !TextUtils.isEmpty(mPostalAddress)
								&& !TextUtils.isEmpty(mCity)
								&& !TextUtils.isEmpty(mState)
								&& !TextUtils.isEmpty(mCountry)) {

							if (mPassword.equals(mCPassword)) {
								if (android.util.Patterns.EMAIL_ADDRESS
										.matcher(mCommunityEmail).matches()) {
									Toast.makeText(context, "ok",
											Toast.LENGTH_SHORT).show();
									final ProgressDialog progressDialog = new ProgressDialog(
											view.getContext());
									progressDialog
											.setMessage("Signing up. Please wait...");
									progressDialog.setCancelable(false);
									progressDialog.show();

									ParseUser user = new ParseUser();
									user.setUsername(mCommunityEmail);
									user.setPassword(mPassword);
									user.setEmail(mCommunityEmail);
									user.put("CommunityName", mCommunityName);
									user.put("ContactNumber", mContactNumber);
									user.put("PostalAddress", mPostalAddress);
									user.put("State", mState);
									user.put("Country", mCountry);
									user.put("ZipCode", mZipCode);
									user.put("isCommunity", true);
									

									try {
										user.signUpInBackground(new SignUpCallback() {
											@Override
											public void done(com.parse.ParseException e) {
												Log.d("renter", "inside");
												if (e == null) {
													Log.d("renter", "created");
													Toast.makeText(
															context,
															"A confirmation email sent you the specified email.",
															Toast.LENGTH_SHORT)
															.show();
													startActivity(new Intent(
															view.getContext(),
															LoginActivity.class));
												

												} else {
													Toast.makeText(
															context,
															"Error in Creating account."
																	+ e.toString(),
															Toast.LENGTH_SHORT)
															.show();
													
												}
												 progressDialog.dismiss();
											}
										});
									} catch (Exception ex) {
										Toast.makeText(context,
												"Exception:" + ex.getMessage(),
												Toast.LENGTH_SHORT).show();
									}
								} else {
									Toast.makeText(context,
											"Please enter valid email address",
											Toast.LENGTH_SHORT).show();
								}
							} else {
								Toast.makeText(
										context,
										"Password and Confirmation password are not matched",
										Toast.LENGTH_SHORT).show();
							}

						} else {
							Toast.makeText(context,
									"Please fill all the details",
									Toast.LENGTH_SHORT).show();
						}

					}
				});
		return view;
	}

}
