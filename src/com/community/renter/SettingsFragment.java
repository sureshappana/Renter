package com.community.renter;

import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.renter.CommonFunctions;
import com.example.renter.R;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class SettingsFragment extends Fragment {

	EditText mEditName, mEditPhoneNumber;
	String TenantName, CommunityName;
	String ContactNumber;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(
				R.layout.fragment_community_settings, container, false);
		// Retrieve values from Parse
		if (!CommonFunctions.UserTableClass.mCurrentUserIsAdmin) {
			ContactNumber =  ParseUser.getCurrentUser()
					.get(CommonFunctions.UserTableClass.CONTACT_NUMBER).toString();
			CommonFunctions.startProgressDialog(getActivity(),
					"Opening Setting");
			ParseQuery<ParseObject> mQueryGetTenantName = ParseQuery
					.getQuery(CommonFunctions.FLATINFO_TABLE);
			mQueryGetTenantName
					.whereEqualTo(
							CommonFunctions
									.trimString(CommonFunctions.FlatInfoTableClass.TENANT_MAIL_ID),
							CommonFunctions.UserTableClass.mCurrentUser);
			mQueryGetTenantName
					.findInBackground(new FindCallback<ParseObject>() {

						@Override
						public void done(List<ParseObject> objects,
								ParseException e) {
							TenantName = CommonFunctions
									.trimString(objects
											.get(0)
											.get(CommonFunctions.FlatInfoTableClass.TENANT_NAME)
											.toString());
							Log.d("obj", TenantName + "");
							mEditName.setText(TenantName);// Retrieved value
															// from parse
							((TextView) view
									.findViewById(R.id.textViewSettingsDisplayName))
									.setText(TenantName);
							CommonFunctions.stopProgressDialog();
						}

					});

		} else {
			CommunityName = (String) ParseUser.getCurrentUser().get(
					CommonFunctions.UserTableClass.COMMUNITY_NAME);
			ContactNumber =  ParseUser.getCurrentUser().get(
					CommonFunctions.UserTableClass.CONTACT_NUMBER).toString();
		}

		mEditName = (EditText) view.findViewById(R.id.editTextSettingsEditName);

		mEditPhoneNumber = (EditText) view
				.findViewById(R.id.editTextSettingsEditPhoneNumber);

		if (!CommonFunctions.UserTableClass.mCurrentUserIsAdmin) {

			mEditPhoneNumber.setText(ContactNumber+"");// Retrieved value from
													// parse
		} else {
			mEditName.setText(CommunityName);// Retrieved value from parse
			mEditPhoneNumber.setText(ContactNumber+"");// Retrieved value from
			((TextView) view
					.findViewById(R.id.textViewSettingsDisplayName))
					.setText(CommunityName);
		}
		view.findViewById(R.id.buttonSettingsSaveChanges).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						final String mUpdatedName = mEditName.getText()
								.toString();
						final String mUpdatedPhoneNumber = mEditPhoneNumber
								.getText().toString();
						ParseQuery<ParseObject> mQueryUpdateContactNoFromSettings = ParseQuery
								.getQuery(CommonFunctions.USER_TABLE);
						mQueryUpdateContactNoFromSettings.getInBackground(
								ParseUser.getCurrentUser().getObjectId(),
								new GetCallback<ParseObject>() {

									@Override
									public void done(ParseObject object,
											ParseException e) {
										if (e == null) {
											object.put(
													CommonFunctions.UserTableClass.CONTACT_NUMBER,
													mUpdatedPhoneNumber);
										} else {
											Log.d("obj", e.toString());
										}
									}
								});

						if (!CommonFunctions.UserTableClass.mCurrentUserIsAdmin) {
							ParseQuery<ParseObject> mQueryUpdateNameFromSettings = ParseQuery
									.getQuery(CommonFunctions.FLATINFO_TABLE);
							mQueryUpdateNameFromSettings.whereEqualTo(
									CommonFunctions
											.trimString(CommonFunctions.FlatInfoTableClass.TENANT_MAIL_ID),
									ParseUser.getCurrentUser().getUsername());
							mQueryUpdateNameFromSettings
									.getFirstInBackground(new GetCallback<ParseObject>() {

										@Override
										public void done(ParseObject object,
												ParseException e) {
											object.put(
													CommonFunctions.FlatInfoTableClass.TENANT_NAME,
													mUpdatedName);

										}
									});
						} else {
							ParseQuery<ParseObject> mQueryUpdateNameFromSettings = ParseQuery
									.getQuery(CommonFunctions.USER_TABLE);
							mQueryUpdateNameFromSettings.getInBackground(
									ParseUser.getCurrentUser().getObjectId(),
									new GetCallback<ParseObject>() {

										@Override
										public void done(ParseObject object,
												ParseException e) {
											if (e == null) {
												object.put(
														CommonFunctions.UserTableClass.COMMUNITY_NAME,
														mUpdatedName);
											} else {
												Log.d("obj", e.toString());
											}
										}
									});
						}

						// remove fragment
					}
				});
		return view;
	}

}