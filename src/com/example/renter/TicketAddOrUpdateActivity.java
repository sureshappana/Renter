package com.example.renter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SendCallback;

public class TicketAddOrUpdateActivity extends Activity {
	
	String mTitle, mDescription, mApartmentNo, mPriority, 
					mTenantId, mCommunityId, mStatus,mPriorityToBeUpdated,mStatusToBeUpdated;								 
	Date mStartDate,mCloseDate; 
	Date mCurrentDate;
	RadioGroup mPriorityGroup,mStatusGroup;														
	Button mStatusButton,mSubmit;
	SimpleDateFormat mDateFormat;
	TextView mTextViewTicketStartDate,mTexViewTicketEndDate;
	Ticket  mTicket;
	static String tenantName = null;
	static String tenantApartnmentNo = null;
	static boolean flag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tenant_add_ticket);
		mPriorityGroup = (RadioGroup) findViewById(R.id.radioGroupTenantTicketAddPriority);
		mStatusGroup = (RadioGroup) findViewById(R.id.radioGroupTenantTicketAddStatus);
		mTextViewTicketStartDate = (TextView) findViewById(R.id.textViewTenantAddTicketStartDate);
		mCurrentDate = new Date();
		mDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		mTextViewTicketStartDate.setText("Ticket raised on: "+mDateFormat.format(mCurrentDate));
		mTexViewTicketEndDate=(TextView) findViewById(R.id.textViewTenantAddTicketEndDate);
		mSubmit = (Button) findViewById(R.id.buttonSubmitTenantTicketAdd);
		mTexViewTicketEndDate.setText("Ticket closed on: ");
		
		// add ticket code
		if(getIntent().getExtras().get(RenterConstantVariables.ADD_TICKET)!=null){
			mSubmit.setText(getResources().getString(R.string.mTicketSubmitButtonAdd));
			mStatusGroup.setVisibility(View.INVISIBLE);
			((TextView) findViewById(R.id.textViewTenantTicketAddStatus)).setVisibility(View.INVISIBLE);
			mSubmit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mTenantId = CommonFunctions.UserTableClass.mCurrentUser;// get From Login Details
				mCommunityId = CommonFunctions.trimString(ParseUser.getCurrentUser().get(CommonFunctions.USER_TABLE_COMMUNITYID).toString());// get From Login Details
				mTitle = ((EditText) findViewById(R.id.editTextTenantTicketAddTitle)).getText().toString();
				mDescription = ((EditText) findViewById(R.id.editTextTenantTicketAddDescription)).getText().toString();
				mStatus = RenterConstantVariables.TICKET_STATUS_OPEN;
				mStartDate = new Date();
				mApartmentNo = TenantHomePageActivity.mCurrentUserFlatNo; //get from login Details
				if(mPriorityGroup.getCheckedRadioButtonId()==R.id.radio0TenantTicketAddPriority){
					mPriority = RenterConstantVariables.HIGH;
				}else if(mPriorityGroup.getCheckedRadioButtonId()==R.id.radio1TenantTicketAddPriority){
					mPriority = RenterConstantVariables.MEDIUM;
				}else if (mPriorityGroup.getCheckedRadioButtonId()==R.id.radio2TenantTicketAddPriority) {
					mPriority = RenterConstantVariables.LOW;
				}
				if(mTitle.length()==0){
					Toast.makeText(TicketAddOrUpdateActivity.this, "Please enter title"
							, Toast.LENGTH_SHORT).show();
				}else{
					
					mTicket = new Ticket();
					mTicket.setmTenantId(mTenantId);
					mTicket.setmApartmentNo(mApartmentNo);
					mTicket.setmCommunityId(mCommunityId);
					mTicket.setmTitle(mTitle);
					mTicket.setmDescription(mDescription);
					mTicket.setmStartDate(mStartDate);
					mTicket.setmPriority(mPriority);
					mTicket.setmStatus(mStatus);
					//Close Date not specified while creating ticket
					//Saving in Parse
					ParseObject ticketTable = new ParseObject(RenterConstantVariables.TICKETTABLE);
					ticketTable.put(RenterConstantVariables.TICKETTABLE_TENANT_ID, mTenantId);
					ticketTable.put(RenterConstantVariables.TICKETTABLE_COMMUNITY_ID, mCommunityId);
					ticketTable.put(RenterConstantVariables.TICKETTABLE_APARTMENT_NO, mApartmentNo);
					ticketTable.put(RenterConstantVariables.TICKETTABLE_TITLE, mTitle);
					ticketTable.put(RenterConstantVariables.TICKETTABLE_DESCRIPTION, mDescription);
					ticketTable.put(RenterConstantVariables.TICKETTABLE_PRIORITY, mPriority);
					ticketTable.put(RenterConstantVariables.TICKETTABLE_STARTDATE, mStartDate);
					ticketTable.put(RenterConstantVariables.TICKETTABLE_STATUS, mStatus);
					ticketTable.saveInBackground(new SaveCallback() {
						@Override
						public void done(ParseException e) {
							if(e==null){
								TicketListFragment.mTicketAdapter.insert(mTicket,0);
								
								sendPushNotificationToCommunity();
								
								finish();
							}else{
								Log.d("renter", "Failed " + e.toString());
							}
						}


					});
				}
			}
		});
	}// add ticket
		
		
		
		//code for edit ticket
		if(getIntent().getExtras().get(RenterConstantVariables.EDIT_TICKET)!=null){
			//if tenant login, make statusTextView and RadioGroup invisible, closedate textview clickable/focussable
			
			mSubmit.setText(getResources().getString(R.string.mTicketSubmitButtonEdit));
			if(!CommonFunctions.UserTableClass.mCurrentUserIsAdmin){
				//make end date not clickable
				mStatusGroup.setVisibility(View.INVISIBLE);
				((TextView)findViewById(R.id.textViewTenantTicketAddStatus)).setVisibility(View.INVISIBLE);
				((TextView)findViewById(R.id.textViewTenantAddTicketEndDate)).setFocusable(false);
				((TextView)findViewById(R.id.textViewTenantAddTicketEndDate)).setFocusableInTouchMode(false);
			}
			
			final Ticket mTicketBeforeEdit = (Ticket) getIntent().getExtras()
										.getSerializable(RenterConstantVariables.EDIT_TICKET);
			((EditText)findViewById(R.id.editTextTenantTicketAddTitle)).setText(mTicketBeforeEdit.getmTitle());
			((EditText)findViewById(R.id.editTextTenantTicketAddDescription)).setText(mTicketBeforeEdit.getmDescription());
			
			if(mTicketBeforeEdit.getmPriority().equalsIgnoreCase(RenterConstantVariables.HIGH)){
				((RadioButton)findViewById(R.id.radio0TenantTicketAddPriority)).setChecked(true);
			}
			else if(mTicketBeforeEdit.getmPriority().equalsIgnoreCase(RenterConstantVariables.MEDIUM)){
				((RadioButton)findViewById(R.id.radio1TenantTicketAddPriority)).setChecked(true);
			}
			else if(mTicketBeforeEdit.getmPriority().equalsIgnoreCase(RenterConstantVariables.LOW)){
				((RadioButton)findViewById(R.id.radio2TenantTicketAddPriority)).setChecked(true);
			}
			
			if((mTicketBeforeEdit).getmStatus().equalsIgnoreCase(RenterConstantVariables.TICKET_STATUS_OPEN)){
				((RadioButton)findViewById(R.id.radio0TenantTicketAddStatus)).setChecked(true);
			}
			else if((mTicketBeforeEdit).getmStatus().equalsIgnoreCase(RenterConstantVariables.TICKET_STATUS_WORKING)){
				((RadioButton)findViewById(R.id.radio1TenantTicketAddStatus)).setChecked(true);
			}
			else if((mTicketBeforeEdit).getmStatus().equalsIgnoreCase(RenterConstantVariables.TICKET_STATUS_CLOSED)){
				((RadioButton)findViewById(R.id.radio2TenantTicketAddStatus)).setChecked(true);
			}
			
			((TextView)findViewById(R.id.textViewTenantAddTicketEndDate)).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(((RadioButton)findViewById(R.id.radio2TenantTicketAddStatus)).isChecked()){ // added by midhun later
					int actualMonth,actualYear,actualDay;
						actualMonth = Integer.parseInt(DateFormat.format("MM", mCurrentDate)
								.toString());
						actualYear = Integer.parseInt(DateFormat.format("yyyy", mCurrentDate)
								.toString());
						actualDay = Integer.parseInt(DateFormat.format("dd", mCurrentDate)
								.toString());
					DatePickerDialog datePicker = new DatePickerDialog(TicketAddOrUpdateActivity.this,
							new OnDateSetListener() {

								@Override
								public void onDateSet(DatePicker view, int year,
										int monthOfYear, int dayOfMonth) {
									Calendar cal = Calendar.getInstance();
									cal.set(year, monthOfYear, dayOfMonth);
									mCloseDate=cal.getTime();
									String formattedDate = new SimpleDateFormat(
											"MM/dd/yyyy").format(cal.getTime());
									((TextView) findViewById(R.id.textViewTenantAddTicketEndDate))
											.setText("Close date: "+formattedDate);
								}
							}, actualYear, actualMonth - 1, actualDay);
					datePicker.setCancelable(false);
					datePicker.show();
					}//added by midhun later
				}
			});
			
			mTextViewTicketStartDate.setText("Ticket raised on: "+mDateFormat.format(mTicketBeforeEdit.getmStartDate()));
			try {
				mTexViewTicketEndDate.setText("Ticket closed on: "+mDateFormat.format(mTicketBeforeEdit.getmCloseDate()));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			mSubmit.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(mPriorityGroup.getCheckedRadioButtonId()==R.id.radio0TenantTicketAddPriority){
						mPriorityToBeUpdated = RenterConstantVariables.HIGH;
					}else if(mPriorityGroup.getCheckedRadioButtonId()==R.id.radio1TenantTicketAddPriority){
						mPriorityToBeUpdated = RenterConstantVariables.MEDIUM;
					}else if (mPriorityGroup.getCheckedRadioButtonId()==R.id.radio2TenantTicketAddPriority) {
						mPriorityToBeUpdated = RenterConstantVariables.LOW;
					}
					if(mStatusGroup.getCheckedRadioButtonId()==R.id.radio0TenantTicketAddStatus){
						mStatusToBeUpdated = RenterConstantVariables.TICKET_STATUS_OPEN;
					}else if(mStatusGroup.getCheckedRadioButtonId()==R.id.radio1TenantTicketAddStatus){
						mStatusToBeUpdated = RenterConstantVariables.TICKET_STATUS_WORKING;
					}else if (mStatusGroup.getCheckedRadioButtonId()==R.id.radio2TenantTicketAddStatus) {
						mStatusToBeUpdated = RenterConstantVariables.TICKET_STATUS_CLOSED;
					}
					final String mTitleToBeUpdated = ((EditText)findViewById(R.id.editTextTenantTicketAddTitle))
							.getText().toString();
					final String mDescriptionToBeUpdated = ((EditText)findViewById(R.id.editTextTenantTicketAddDescription))
							.getText().toString();
					ParseQuery<ParseObject> querySaveEditedTicket = 
							ParseQuery.getQuery(RenterConstantVariables.TICKETTABLE);
					querySaveEditedTicket.getInBackground(mTicketBeforeEdit.getmId(),new GetCallback<ParseObject>() {
						
						@Override
						public void done(ParseObject mTicketToBeUpdated, ParseException e) {
							if(e==null){
								mTicketToBeUpdated.put(RenterConstantVariables.TICKETTABLE_TITLE, mTitleToBeUpdated);
								mTicketToBeUpdated.put(RenterConstantVariables.TICKETTABLE_DESCRIPTION, mDescriptionToBeUpdated);
								mTicketToBeUpdated.put(RenterConstantVariables.TICKETTABLE_PRIORITY, mPriorityToBeUpdated);
								try {
									mTicketToBeUpdated.put(RenterConstantVariables.TICKETTABLE_ENDDATE, mCloseDate);
								} catch (Exception e1) {
									e1.printStackTrace();
								}
								mTicketToBeUpdated.put(RenterConstantVariables.TICKETTABLE_STATUS, mStatusToBeUpdated);
								mTicketToBeUpdated.saveInBackground(new SaveCallback() {
									@Override
									public void done(ParseException arg0) {
										finish();
									}
								});
							}
							else{
							}
						}
					});
				}
			});
		}//edit ticket ends
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
						tenantApartnmentNo = CommonFunctions
								.trimString(tenant
										.get(CommonFunctions.FLATINFO_TABLE_FLAT_NUMBER)
										.toString());
					}
				} else {
					Log.d("score", "Error: " + e.getMessage());
				}
			}
		});
		flag = true;
	}

	private void sendPushNotificationToCommunity() {

		String mCommunityMessagesPushMessageChannel = "Community_"
				+ CommonFunctions.trimString(ParseUser.getCurrentUser()
						.get(CommonFunctions.USER_TABLE_COMMUNITYID)
						.toString());
		ParseQuery pushQuery = ParseInstallation.getQuery();
		pushQuery.whereNotEqualTo("user", ParseUser.getCurrentUser());
		pushQuery.whereEqualTo("channels",
				mCommunityMessagesPushMessageChannel);
		
		ParsePush push = new ParsePush();
		push.setChannel(mCommunityMessagesPushMessageChannel);
		push.setQuery(pushQuery);
		push.setMessage(mApartmentNo+ " raised the new ticket: "+ mTitle);
		push.sendInBackground(new SendCallback() {

			@Override
			public void done(ParseException e) {
				if (e == null) {
					Log.d("push", "success");
				} else {
					Log.d("push", "failed:" + e.toString());
				}

			}
		});
		
	}
}