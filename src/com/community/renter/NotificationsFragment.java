package com.community.renter;

import java.util.ArrayList;
import java.util.List;

import com.example.renter.CommonFunctions;
import com.example.renter.R;
import com.example.renter.R.layout;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class NotificationsFragment extends Fragment {
	ArrayList<String> mFlatNoList,mFlatNoSelected;
	String[] mFlatNoArray;
	RadioGroup mRadioGroupNotificationSendTo;
	AlertDialog.Builder mBuilderFlatNoSelect;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view =  inflater.inflate(R.layout.fragment_community_notifications,
				container, false);
		
		ParseQuery<ParseObject> mQueryRetrieveFlatMembers = ParseQuery.getQuery(CommonFunctions.FLATINFO_TABLE);
		mQueryRetrieveFlatMembers.whereEqualTo(CommonFunctions.trimString(CommonFunctions.FLATINFO_TABLE_COMMUNITY_OBJECT), 
						(String)ParseUser.getCurrentUser().getObjectId());
		mQueryRetrieveFlatMembers.whereEqualTo(CommonFunctions.FlatInfoTableClass.TENANT_IS_OCCUPIED, true);
		mQueryRetrieveFlatMembers.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				
				mFlatNoList = new ArrayList<String>();
				for(int i=0;i<objects.size();i++)
				{
					String flatNo = (String) CommonFunctions.trimString
							(objects.get(i).get(CommonFunctions.FlatInfoTableClass.TENANT_FLAT_NO).toString());
					mFlatNoList.add(flatNo);
				}
				mFlatNoArray = new String[mFlatNoList.size()];
				mFlatNoArray =  (String[]) mFlatNoList.toArray(mFlatNoArray);
				mFlatNoSelected = new ArrayList<String>();
				try {
					mFlatNoSelected.clear();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				mFlatNoSelected.addAll(mFlatNoList);
				((TextView) getActivity().findViewById(R.id.textViewNotificationRecipients))
				.setText(CommonFunctions.trimString(mFlatNoSelected.toString()));
				mBuilderFlatNoSelect = new AlertDialog.Builder(getActivity());
				try {
					mBuilderFlatNoSelect.setTitle("Select the flats: ")
										.setMultiChoiceItems(mFlatNoArray, null, new DialogInterface.OnMultiChoiceClickListener() {
											
											@Override
											public void onClick(DialogInterface dialog, int which, boolean isChecked) {
												if(isChecked){
													mFlatNoSelected.add(mFlatNoArray[which]);
												}
											}
										})
										.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
											
											@Override
											public void onClick(DialogInterface dialog, int which) {
												((TextView) getActivity().findViewById(R.id.textViewNotificationRecipients))
													.setText(CommonFunctions.trimString(mFlatNoSelected.toString()));
												
											}
										});
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		
		
		mRadioGroupNotificationSendTo= (RadioGroup) view.findViewById(R.id.radioGroupNotificationSendTo);
		mRadioGroupNotificationSendTo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
		
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId==R.id.radio0NotificationSendToAll){
					try {
						mFlatNoSelected.clear();
						mFlatNoSelected.addAll(mFlatNoList);
						((TextView) getActivity().findViewById(R.id.textViewNotificationRecipients))
						.setText(CommonFunctions.trimString(mFlatNoSelected.toString()));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				else if(checkedId == R.id.radio1NotificationSendToSpecific){
					try {
						mFlatNoSelected.clear();
						((TextView) getActivity().findViewById(R.id.textViewNotificationRecipients))
						.setText("");
						AlertDialog dialog = mBuilderFlatNoSelect.create();
						dialog.setCancelable(true);
						dialog.show();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			}
		});
		
		((Button) view.findViewById(R.id.buttonNotificationSend)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String mNotificationText = ((EditText) view.findViewById(R.id.editTextNotificationText)).getText().toString();
				//send push notification and remove the fragment
				if(mNotificationText.length()>0){
					Log.d("demo", mNotificationText + "Selected flats"+ mFlatNoSelected.toString());
				}
				else{
					CommonFunctions.toastMessage(getActivity(), "Please add some notification");
				}
			}
		});
		return view;
	}

}
